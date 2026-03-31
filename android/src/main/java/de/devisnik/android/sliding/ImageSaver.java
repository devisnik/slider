package de.devisnik.android.sliding;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ImageSaver {

	interface Callback {
		void onImageSaved(String path);
	}

	private final ExecutorService itsExecutor;
	private final Handler itsMainHandler;
	private final File itsCacheDir;

	ImageSaver(File cacheDir) {
		this(cacheDir, Looper.getMainLooper(), Executors.newSingleThreadExecutor());
	}

	ImageSaver(File cacheDir, Looper looper, ExecutorService executor) {
		itsCacheDir = cacheDir;
		itsMainHandler = new Handler(looper);
		itsExecutor = executor;
	}

	void save(String path, int minSize, Callback callback) {
		ImageFactory imageFactory = new ImageFactory();
		ImageCache imageCache = new ImageCache(itsCacheDir);

		itsExecutor.submit(() -> {
			Bitmap bitmap = imageFactory.createFromPath(path, minSize / 2);
			imageCache.put(bitmap);
			itsMainHandler.post(() -> callback.onImageSaved(path));
		});
	}

	void shutdown() {
		itsExecutor.shutdownNow();
		itsMainHandler.removeCallbacksAndMessages(null);
	}
}

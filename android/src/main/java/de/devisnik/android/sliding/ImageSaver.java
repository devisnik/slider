package de.devisnik.android.sliding;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ImageSaver {

	interface Callback {
		void onImageSaved(String uriString);
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

	void save(Uri uri, ContentResolver resolver, int minSize, Callback callback) {
		ImageFactory imageFactory = new ImageFactory();
		ImageCache imageCache = new ImageCache(itsCacheDir);
		String uriString = uri.toString();

		itsExecutor.submit(() -> {
			Bitmap bitmap = imageFactory.createFromUri(resolver, uri, itsCacheDir, minSize / 2);
			imageCache.put(bitmap);
			itsMainHandler.post(() -> callback.onImageSaved(uriString));
		});
	}

	void shutdown() {
		itsExecutor.shutdownNow();
		itsMainHandler.removeCallbacksAndMessages(null);
	}
}

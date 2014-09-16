package de.devisnik.android.sliding;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class ImageCache {
	private static final String CACHED_IMAGE = "cached_image";
	private static final String TAG = "ImageCache";
	private final File itsCacheDir;

	abstract class CacheFileCommand<RESULT, STREAM extends Closeable> {

		private final RESULT itsErrorResult;

		CacheFileCommand(final RESULT errorResult) {
			itsErrorResult = errorResult;
		}

		protected final File createCacheFile() {
			return new File(itsCacheDir, CACHED_IMAGE);
		}

		public final RESULT execute() {
			STREAM stream = null;
			File file = createCacheFile();
			try {
				stream = createStream(file);
				return execute(stream);
			} catch (FileNotFoundException e) {
				Logger.e(TAG, e.getMessage());
				return itsErrorResult;
			} finally {
				if (stream != null)
					try {
						getFD(stream).sync();
						stream.close();
					} catch (IOException e) {
						Logger.e(TAG, e.getMessage());
						e.printStackTrace();
					}
			}
		}

		abstract STREAM createStream(File file) throws FileNotFoundException;

		abstract FileDescriptor getFD(STREAM stream) throws IOException;

		abstract RESULT execute(STREAM stream);
	}

	private class WriteFileCommand extends CacheFileCommand<Boolean, FileOutputStream> {

		private final Bitmap itsBitmap;

		WriteFileCommand(final Bitmap bitmap) {
			super(Boolean.FALSE);
			itsBitmap = bitmap;
		}

		@Override
		FileOutputStream createStream(final File file) throws FileNotFoundException {
			return new FileOutputStream(file);
		}

		@Override
		FileDescriptor getFD(final FileOutputStream stream) throws IOException {
			return stream.getFD();
		}

		@Override
		Boolean execute(final FileOutputStream stream) {
			return itsBitmap.compress(CompressFormat.PNG, 100, stream);
		}
	}

	private class ReadFileCommand extends CacheFileCommand<Bitmap, FileInputStream> {

		ReadFileCommand() {
			super(null);
		}

		@Override
		FileInputStream createStream(final File file) throws FileNotFoundException {
			return new FileInputStream(file);
		}

		@Override
		Bitmap execute(final FileInputStream stream) {
			return BitmapFactory.decodeStream(stream);
		}

		@Override
		FileDescriptor getFD(final FileInputStream stream) throws IOException {
			return stream.getFD();
		}
	}

	public ImageCache(final File cacheDir) {
		itsCacheDir = cacheDir;
	}

	public boolean put(final Bitmap bitmap) {
		if (bitmap == null)
			return false;
		return new WriteFileCommand(bitmap).execute();
	}

	public Bitmap get() {
		return new ReadFileCommand().execute();
	}
}

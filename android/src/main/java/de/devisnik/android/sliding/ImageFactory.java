package de.devisnik.android.sliding;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import de.devisnik.sliding.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageFactory {

	private static final String TAG = "ImageFactory";
	private String itsImagePath;
	private int itsMinSize;

	public ImageFactory() {
	}

	public Bitmap createFromUri(ContentResolver resolver, Uri uri, File tempDir, int minSize) {
		File tempFile = copyToTempFile(resolver, uri, tempDir);
		if (tempFile == null) return null;
		try {
			return createFromPath(tempFile.getAbsolutePath(), minSize);
		} finally {
			tempFile.delete();
		}
	}

	private static File copyToTempFile(ContentResolver resolver, Uri uri, File tempDir) {
		File tempFile = new File(tempDir, "picker_temp_image");
		try (InputStream in = resolver.openInputStream(uri);
			 OutputStream out = new FileOutputStream(tempFile)) {
			if (in == null) return null;
			byte[] buffer = new byte[8192];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			return tempFile;
		} catch (IOException e) {
			Logger.e(TAG, "Failed to copy URI to temp file: " + e.getMessage());
			tempFile.delete();
			return null;
		}
	}

	private Bitmap createFromPath(String imagePath, int minSize) {
		itsImagePath = imagePath;
		itsMinSize = minSize;
		return createBitmapFromPath();
	}

	private Bitmap createBitmapFromPath() {
		int sampleSize = computeSampleSize();
		Logger.d(TAG, "samplesize: " + sampleSize);
		Options options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(itsImagePath, options);
		Logger.d(TAG, "bitmapsize: " + options.outWidth + ", " + options.outHeight);
		return applyExifRotation(bitmap);
	}

	private Bitmap applyExifRotation(Bitmap bitmap) {
		if (bitmap == null) return null;
		try {
			ExifInterface exif = new ExifInterface(itsImagePath);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			Matrix matrix = new Matrix();
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:    matrix.postRotate(90);           break;
				case ExifInterface.ORIENTATION_ROTATE_180:   matrix.postRotate(180);          break;
				case ExifInterface.ORIENTATION_ROTATE_270:   matrix.postRotate(270);          break;
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL: matrix.postScale(-1, 1);      break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:   matrix.postScale(1, -1);      break;
				case ExifInterface.ORIENTATION_TRANSPOSE:
					matrix.postRotate(90); matrix.postScale(-1, 1);                           break;
				case ExifInterface.ORIENTATION_TRANSVERSE:
					matrix.postRotate(270); matrix.postScale(-1, 1);                          break;
				default: return bitmap;
			}
			Bitmap rotated = Bitmap.createBitmap(
					bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return rotated;
		} catch (IOException e) {
			Logger.e(TAG, "Failed to read EXIF data: " + e.getMessage());
			return bitmap;
		}
	}

	private Point computeImageSize() {
		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(itsImagePath, options);
		return new Point(options.outWidth, options.outHeight);
	}

	private int computeSampleSize() {
		Point imageSize = computeImageSize();
		Logger.d(TAG, "imagesize: " + imageSize);
		Point ratio = Point.divide(imageSize, itsMinSize);
		return ratio.min();
	}
}

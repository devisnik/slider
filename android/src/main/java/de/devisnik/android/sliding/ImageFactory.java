package de.devisnik.android.sliding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import de.devisnik.sliding.Point;

public class ImageFactory {

	private static final String TAG = "ImageFactory";
	private String itsImagePath;
	private int itsMinSize;

	public ImageFactory() {
	}

	public Bitmap createFromPath(String imagePath, int minSize) {
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
		return bitmap;
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

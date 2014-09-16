package de.devisnik.android.sliding.tile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import de.devisnik.android.sliding.Settings;
import de.devisnik.sliding.Point;

public class PieceDrawerFactory {

	private final int itsWidth;
	private final int itsHeight;
	private final Settings itsSettings;
	private final Point itsPieceSize;

	PieceDrawerFactory(final int width, final int height, final Settings settings) {
		itsWidth = width;
		itsHeight = height;
		itsSettings = settings;
		itsPieceSize = Point.divide(width, height, settings.getFrameSize(width, height));
	}

	public IPieceDrawer create() {
		if (itsSettings.isUseDefaultImage())
			return createCachedNumberDrawer();
		Bitmap storedImage = itsSettings.readImage();
		if (storedImage == null)
			return createCachedNumberDrawer();
		return new ImagePieceDrawer(createTargetBitmap(storedImage), itsPieceSize);
	}

	private Bitmap createTargetBitmap(final Bitmap bitmap) {
		if (bitmap == null)
			return null;
		Config bitmapConfig = bitmap.getConfig();
		Bitmap output = Bitmap.createBitmap(itsWidth, itsHeight, bitmapConfig == null ? Config.ARGB_8888
				: Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		Rect clipRect = computeClipRectangle(bitmap);
		Rect targetRect = new Rect(0, 0, itsWidth, itsHeight);
		paint.setAntiAlias(true);
		canvas.drawBitmap(bitmap, clipRect, targetRect, paint);
		return output;
	}

	private Rect computeClipRectangle(final Bitmap bitmap) {
		float targetRatio = ((float) itsWidth) / itsHeight;
		Point bitmapArea = new Point(bitmap.getWidth(), bitmap.getHeight());
		float bitmapRatio = bitmapArea.ratio();
		Point clipArea = bitmapArea.copy();
		if (targetRatio <= bitmapRatio)
			clipArea.x = Math.round(targetRatio * clipArea.y);
		else
			clipArea.y = Math.round(clipArea.x / targetRatio);
		Point shift = Point.diff(bitmapArea, clipArea).divideBy(2);
		return new Rect(shift.x, shift.y, clipArea.x + shift.x, clipArea.y + shift.y);
	}

	private IPieceDrawer createCachedNumberDrawer() {
		return new BitmapCachingPieceDrawer(new NumberPieceDrawer(itsPieceSize));
	}

}

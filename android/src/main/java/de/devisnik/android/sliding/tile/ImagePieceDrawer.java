package de.devisnik.android.sliding.tile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public class ImagePieceDrawer implements IPieceDrawer {

	private final Bitmap itsImage;
	private final Rect itsDstRect;
	private final Rect itsSrcRect;
	private final Point itsTileSize;
	private final Point itsHomeUpperLeft;
	private final Paint mPaint;

	public ImagePieceDrawer(final Bitmap image, final Point tileSize) {
		itsImage = image;
		itsTileSize = tileSize;
		itsDstRect = new Rect(0, 0, tileSize.x, tileSize.y);
		itsSrcRect = new Rect();
		itsHomeUpperLeft = new Point(0, 0);
		mPaint = new Paint();
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
	}

	@Override
	public Point getTileSize() {
		return itsTileSize;
	}

	public void drawTile(final IPiece piece, final Canvas canvas, final Paint paint) {
		itsHomeUpperLeft.set(itsTileSize).multiplyBy(piece.getHomePosition());
		itsSrcRect.set(itsHomeUpperLeft.x, itsHomeUpperLeft.y, itsHomeUpperLeft.x + itsTileSize.x, itsHomeUpperLeft.y
				+ itsTileSize.y);
		canvas.drawBitmap(itsImage, itsSrcRect, itsDstRect, mPaint);
	}
}

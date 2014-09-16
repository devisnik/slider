package de.devisnik.android.sliding.tile;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public class BitmapCachingPieceDrawer implements IPieceDrawer {

	private final IPieceDrawer itsTileDrawer;
	private final HashMap<IPiece, Bitmap> itsCache = new HashMap<IPiece, Bitmap>();
	private final Paint mPaint;

	public BitmapCachingPieceDrawer(final IPieceDrawer tileDrawer) {
		itsTileDrawer = tileDrawer;
		mPaint = new Paint();
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
	}

	@Override
	public Point getTileSize() {
		return itsTileDrawer.getTileSize();
	}

	@Override
	public void drawTile(final IPiece piece, final Canvas canvas, final Paint paint) {
		Bitmap bitmap = itsCache.get(piece);
		if (bitmap == null)
			bitmap = cacheBitmap(piece, null);
		canvas.drawBitmap(bitmap, 0f, 0f, mPaint);
	}

	private Bitmap cacheBitmap(final IPiece piece, final Paint paint) {
		Point tileSize = itsTileDrawer.getTileSize();
		Bitmap bitmap = Bitmap.createBitmap(tileSize.x, tileSize.y, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		itsTileDrawer.drawTile(piece, canvas, paint);
		itsCache.put(piece, bitmap);
		return bitmap;
	}

}

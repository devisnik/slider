package de.devisnik.android.sliding.tile;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Shader.TileMode;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public class NumberPieceDrawer implements IPieceDrawer {

	private static final int TEXT_COLOR = 0xff444444;
	private final Paint itsPaint;
	private final Paint itsBackPaint;
	private final Rect itsBackRect;
	private final float itsTextPosX;
	private final float itsTextPosY;
	private final Point itsTileSize;
	private final Paint itsBorderPaint;

	public NumberPieceDrawer(final Point tileSize) {
		itsTileSize = tileSize;
		itsPaint = new Paint();
		itsPaint.setColor(TEXT_COLOR);
		itsPaint.setAntiAlias(true);
		itsPaint.setStrokeWidth(2);
		itsPaint.setStrokeCap(Paint.Cap.ROUND);
		itsPaint.setStyle(Paint.Style.FILL);
		itsPaint.setTextSize(tileSize.x / 3);
		itsPaint.setTextAlign(Align.CENTER);

		itsBackPaint = new Paint();
		itsBackPaint.setShader(new RadialGradient(tileSize.x / 2, tileSize.y / 2, tileSize.x / 2 - 8, 0xff000000,
				TEXT_COLOR, TileMode.CLAMP));

		itsBackRect = new Rect(0, 0, tileSize.x, tileSize.y);
		itsTextPosX = tileSize.x / 2f;
		itsTextPosY = (tileSize.y - itsPaint.ascent() - itsPaint.descent()) / 2;

		itsBorderPaint = new Paint();
		itsBorderPaint.setStyle(Style.STROKE);
		itsBorderPaint.setStrokeWidth(1);
		itsBorderPaint.setColor(0x33000000);
	}

	@Override
	public Point getTileSize() {
		return itsTileSize;
	}

	@Override
	public void drawTile(final IPiece piece, final Canvas canvas, final Paint paint) {
		canvas.drawRect(itsBackRect, itsBackPaint);
		canvas.drawRect(itsBackRect, itsBorderPaint);
		canvas.drawText(piece.getLabel(), itsTextPosX, itsTextPosY, itsPaint);
	}

}

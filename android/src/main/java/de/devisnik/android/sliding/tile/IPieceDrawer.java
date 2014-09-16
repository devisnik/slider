package de.devisnik.android.sliding.tile;

import android.graphics.Canvas;
import android.graphics.Paint;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public interface IPieceDrawer {

	Point getTileSize();
	void drawTile(IPiece piece, Canvas canvas, Paint paint);
}
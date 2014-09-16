package de.devisnik.android.sliding.tile;

import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import de.devisnik.android.sliding.Logger;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public class CachingPieceDrawer implements IPieceDrawer {

	private static final String TAG = "CachingTileDrawer";
	private final IPieceDrawer itsTileDrawer;
	private HashMap<IPiece, Picture> itsCache = new HashMap<IPiece, Picture>();

	public CachingPieceDrawer(IPieceDrawer tileDrawer) {
		itsTileDrawer = tileDrawer;
	}
	
	@Override
	public Point getTileSize() {
		return itsTileDrawer.getTileSize();
	}

	@Override
	public void drawTile(IPiece piece, Canvas canvas, Paint paint) {
		if (!itsCache.containsKey(piece))
			cachePicture(piece, paint);
		canvas.drawPicture(itsCache.get(piece));
	}

	private void cachePicture(IPiece piece, Paint paint) {
		Logger.d(TAG, "caching piece " + piece.getLabel());
		Picture picture = new Picture();
		Point tileSize = itsTileDrawer.getTileSize();
		Canvas recording = picture.beginRecording(tileSize.x, tileSize.y);
		itsTileDrawer.drawTile(piece, recording, paint);
		picture.endRecording();
		itsCache.put(piece, picture);
	}

}

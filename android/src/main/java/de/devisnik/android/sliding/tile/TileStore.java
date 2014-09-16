package de.devisnik.android.sliding.tile;

import java.util.HashMap;
import java.util.Iterator;

import de.devisnik.android.sliding.Settings;
import de.devisnik.sliding.IFrame;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public class TileStore implements Iterable<Tile> {
	private final IPieceDrawer itsPieceDrawer;
	private final HashMap<IPiece, Tile> itsCache;

	public TileStore(final int width, final int height, final Settings settings, final IFrame frame) {
		itsPieceDrawer = new PieceDrawerFactory(width, height, settings).create();
		Point frameSize = settings.getFrameSize(width, height);
		itsCache = new HashMap<IPiece, Tile>(frameSize.x * frameSize.y);
		init(frame);
	}

	private void init(final IFrame frame) {
		for (IPiece piece : frame)
			itsCache.put(piece, create(piece));
	}

	private Tile create(final IPiece piece) {
		return new Tile(piece, itsPieceDrawer);
	}

	public Tile get(final IPiece piece) {
		return itsCache.get(piece);
	}

	@Override
	public Iterator<Tile> iterator() {
		return itsCache.values().iterator();
	}
}

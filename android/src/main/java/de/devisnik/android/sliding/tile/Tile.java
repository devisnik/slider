package de.devisnik.android.sliding.tile;

import android.graphics.Canvas;
import android.graphics.Rect;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;
import de.devisnik.sliding.animation.IShiftable;

public class Tile implements IShiftable {

	private final IPiece itsPiece;
	private final Point itsPosition;
	private final IPieceDrawer itsDrawer;
	private boolean itsHidden;
	private boolean mDirty = true;
	private final Rect mDirtyRegion;

	public Tile(final IPiece piece, final IPieceDrawer drawer) {
		itsPiece = piece;
		itsDrawer = drawer;
		itsPosition = Point.times(piece.getPosition(), getSize());
		mDirtyRegion = new Rect(itsPosition.x, itsPosition.y, itsPosition.x + itsDrawer.getTileSize().x, itsPosition.y
				+ itsDrawer.getTileSize().y);
	}

	@Override
	public Point getSize() {
		return itsDrawer.getTileSize();
	}

	public void setHidden(final boolean value) {
		itsHidden = value;
	}

	@Override
	public void shift(final Point delta) {
		itsPosition.add(delta);
		shiftRect(delta);
		if (delta.x != 0 || delta.y != 0)
			mDirty = true;
	}

	private void shiftRect(final Point delta) {
		mDirtyRegion.union(mDirtyRegion.left + delta.x, mDirtyRegion.top + delta.y);
		mDirtyRegion.union(mDirtyRegion.right + delta.x, mDirtyRegion.bottom + delta.y);
	}

	public boolean isDirty() {
		return mDirty;
	}

	public void draw(final Canvas canvas) {
		mDirtyRegion.set(itsPosition.x, itsPosition.y, itsPosition.x + itsDrawer.getTileSize().x, itsPosition.y
				+ itsDrawer.getTileSize().y);
		if (itsHidden)
			return;
		canvas.save();
		canvas.translate(itsPosition.x, itsPosition.y);
		itsDrawer.drawTile(itsPiece, canvas, null);
		canvas.restore();

		mDirty = false;
	}

	public Rect getDirtyRegion() {
		return mDirtyRegion;
	}

}

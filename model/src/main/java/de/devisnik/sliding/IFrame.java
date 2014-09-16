package de.devisnik.sliding;

/**
 * A rectangular frame filled with sliding pieces, containing one hole.
 * This models a 15-puzzle-like sliding game.
 */
public interface IFrame extends Iterable<IPiece> {

	Point getSize();

	IPiece getPieceAt(int posX, int posY);

	IHole getHole();

	boolean execute(IMove move);

	void addListener(IFrameListener listener);

	void removeListener(IFrameListener listener);
}

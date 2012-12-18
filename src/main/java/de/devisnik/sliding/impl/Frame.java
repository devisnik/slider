/*
 * Created on 21.07.2006 by leck
 *
 */
package de.devisnik.sliding.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.devisnik.sliding.IFrame;
import de.devisnik.sliding.IFrameListener;
import de.devisnik.sliding.IHole;
import de.devisnik.sliding.IMove;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.MoveFactory;
import de.devisnik.sliding.Point;
import de.devisnik.sliding.ShiftingEvent;

public class Frame implements IFrame {

	public static void main(String[] args) {
		Frame shiftingGame = new Frame(5, 5);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.UP);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.UP);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.LEFT);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.LEFT);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.UP);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.LEFT);
		System.out.println(shiftingGame.toString());
		shiftingGame.moveHole(MoveFactory.DOWN);
		System.out.println(shiftingGame.toString());
	}

	private int itsDimX;
	private int itsDimY;
	private Hole itsHole;
	private Piece[][] itsPieces;

	private List<IFrameListener> itsShiftingGameListeners = new ArrayList<IFrameListener>();

	public Frame(int x, int y) {
		itsDimX = x;
		itsDimY = y;
		createPieces(x, y);
	}

	public void addListener(IFrameListener listener) {
		if (listener == null)
			return;
		itsShiftingGameListeners.add(listener);
	}

	private Piece createPieceAt(int x, int y) {
		return new Piece(x, y, "" + (y * itsDimX + x + 1));
	}

	private void createPieces(int dimX, int dimY) {
		itsPieces = new Piece[dimX][dimY];
		for (int y = 0; y < dimY; y++)
			for (int x = 0; x < dimX; x++)
				itsPieces[x][y] = createPieceAt(x, y);
		itsHole = new Hole(dimX - 1, dimY - 1, " ");
		itsPieces[dimX - 1][dimY - 1] = itsHole;
	}

	private void exchange(Point first, Point second) {
		Piece buffer = itsPieces[first.x][first.y];
		putPieceAt(itsPieces[second.x][second.y], first.x, first.y);
		putPieceAt(buffer, second.x, second.y);
	}

	public boolean execute(IMove move) {
		ShiftingEvent[] event = moveHole(move);
//		fireShiftingEvent(event[1]);
		fireSwap(event[0].getPiece(), event[1].getPiece());
		return true;
	}

	protected final void fireSwap(IPiece left, IPiece right) {
		for (IFrameListener listener : itsShiftingGameListeners)
			listener.handleSwap(left, right);
	}

	protected final void fireShiftingEvents(final ShiftingEvent[] events) {
		for (IFrameListener listener : itsShiftingGameListeners)
			listener.handleShifting(events);
	}

	public IHole getHole() {
		return itsHole;
	}

	public IPiece getPieceAt(int posX, int posY) {
		return itsPieces[posX][posY];
	}

	public Point getSize() {
		return new Point(itsDimX, itsDimY);
	}

	protected final ShiftingEvent[] moveHole(IMove move) {
		Point holePosition = getHole().getPosition().copy();
		Point target = Point.sum(holePosition, move.getX(), move.getY());
		IPiece piece = getPieceAt(target.x, target.y);
		exchange(holePosition, target);
		Point oldPosition = holePosition;
		return new ShiftingEvent[] { new ShiftingEvent(piece, target, oldPosition),
				new ShiftingEvent(itsHole, oldPosition, target) };
	}

	protected final void putPieceAt(Piece piece, int x, int y) {
		itsPieces[x][y] = piece;
		piece.setPosition(x, y);
	}

	public void removeListener(IFrameListener listener) {
		itsShiftingGameListeners.remove(listener);
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (int y = 0; y < itsDimY; y++) {
			for (int x = 0; x < itsDimX; x++) {
				String label = itsPieces[x][y].getLabel();
				stringBuffer.append(label);
				stringBuffer.append(" ");
			}
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}

	public Iterator<IPiece> iterator() {
		return new PieceIterator(itsPieces);
	}
}

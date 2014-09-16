package de.devisnik.sliding.impl;

import java.util.ArrayList;

import de.devisnik.sliding.FromHomeShiftingEvent;
import de.devisnik.sliding.IMove;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.IRandom;
import de.devisnik.sliding.IRobotFrame;
import de.devisnik.sliding.MoveFactory;
import de.devisnik.sliding.Point;
import de.devisnik.sliding.ShiftingEvent;
import de.devisnik.sliding.ToHomeShiftingEvent;

public class RobotFrame extends Frame implements IRobotFrame {

	private Scrambler itsScrambler;
	private IMove[] itsMoves = new IMove[0];
	private int itsNextMoveIndex = -1;

	public RobotFrame(int x, int y, IRandom random) {
		super(x, y);
		itsScrambler = new Scrambler(this, random);
	}

	public final boolean isResolved() {
		return itsNextMoveIndex == -1;
	}

	public boolean replayNext() {
		if (isResolved())
			return false;
		execute(MoveFactory.getInverse(itsMoves[itsNextMoveIndex]));
		itsNextMoveIndex--;
		return true;
	}

	public boolean scramble() {
		if (!isResolved())
			return false;
		itsMoves = itsScrambler.scramble();
		itsNextMoveIndex = itsMoves.length - 1;
		ArrayList<ShiftingEvent> events = new ArrayList<ShiftingEvent>();
		for (IPiece piece : this)
			events.add(new FromHomeShiftingEvent(piece));
		fireShiftingEvents(events.toArray(new ShiftingEvent[0]));
		return true;
	}

	public void resolve() {
		if (isResolved())
			return;
		ArrayList<ShiftingEvent> events = new ArrayList<ShiftingEvent>();
		for (IPiece piece : this) {
			events.add(new ToHomeShiftingEvent(piece));
			putAtHome(piece);
		}
		itsNextMoveIndex = -1;
		fireShiftingEvents(events.toArray(new ShiftingEvent[0]));
	}

	private void putAtHome(IPiece piece) {
		Point homePosition = piece.getHomePosition();
		putPieceAt((Piece) piece, homePosition.x, homePosition.y);
	}

}

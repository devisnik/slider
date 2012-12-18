package de.devisnik.sliding;

public class FromHomeShiftingEvent extends ShiftingEvent {

	public FromHomeShiftingEvent(IPiece piece) {
		super(piece, piece.getHomePosition(), piece.getPosition());
	}

}

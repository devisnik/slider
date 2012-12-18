package de.devisnik.sliding;

public class ToHomeShiftingEvent extends ShiftingEvent {

	public ToHomeShiftingEvent(IPiece piece) {
		super(piece, piece.getPosition(), piece.getHomePosition());
	}

}

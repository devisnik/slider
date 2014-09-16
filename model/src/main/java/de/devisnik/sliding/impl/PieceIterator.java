package de.devisnik.sliding.impl;

import java.util.ArrayList;
import java.util.Iterator;

import de.devisnik.sliding.IPiece;

class PieceIterator implements Iterator<IPiece> {

	private int itsCurrent;
	private final ArrayList<IPiece> itsPieceList;

	public PieceIterator(final IPiece[][] pieces) {
		itsPieceList = new ArrayList<IPiece>(pieces.length*pieces[0].length);
		for (IPiece[] row : pieces) {
			for (IPiece piece : row) {
				itsPieceList.add(piece);
			}
		}
		itsCurrent = 0;
	}

	public boolean hasNext() {
		return itsCurrent < itsPieceList.size();
	}

	public IPiece next() {
		IPiece value = itsPieceList.get(itsCurrent);
		itsCurrent++;
		return value;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}

package de.devisnik.sliding;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Random;

import org.junit.Test;

public class FrameScramblerTest {

	@Test
	public void testScrambleInt() {
		IFrame frame = FrameFactory.create(4, 3);
		FrameScrambler frameScrambler = new FrameScrambler(frame, new IRandom() {
			
			Random mRandom = new Random();
			@Override
			public int nextInt(int border) {
				return mRandom.nextInt(border);
			}
		});
		IMove[] moves = frameScrambler.scramble(20);
		for (int i = moves.length-1; i >= 0; i--) {
			frame.execute(MoveFactory.getInverse(moves[i]));
		}
		for (Iterator<IPiece> iterator = frame.iterator(); iterator.hasNext();) {
			IPiece piece = (IPiece) iterator.next();
			assertEquals(true, piece.isAtHome());
		}
	}

}

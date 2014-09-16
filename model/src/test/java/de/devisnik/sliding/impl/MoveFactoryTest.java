package de.devisnik.sliding.impl;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import de.devisnik.sliding.IMove;
import de.devisnik.sliding.MoveFactory;

public class MoveFactoryTest {

	@Test
	public void shouldKnowInverseMoves() {
		assertEquals(MoveFactory.DOWN, MoveFactory.getInverse(MoveFactory.UP));
		assertEquals(MoveFactory.LEFT,
				MoveFactory.getInverse(MoveFactory.RIGHT));
		assertEquals(MoveFactory.RIGHT,
				MoveFactory.getInverse(MoveFactory.LEFT));
		assertEquals(MoveFactory.UP, MoveFactory.getInverse(MoveFactory.DOWN));
	}

	@Test
	public void shouldConstructMultiMovesInOneDirection() {
		IMove[] threeHorizonalMoves = MoveFactory.getMovesTo(3, 0);
		assertEquals(3, threeHorizonalMoves.length);
		for (int i = 0; i < threeHorizonalMoves.length; i++) {
			assertEquals(MoveFactory.RIGHT, threeHorizonalMoves[i]);
		}
		IMove[] twoVerticalMoves = MoveFactory.getMovesTo(0, 2);
		assertEquals(2, twoVerticalMoves.length);
		for (int i = 0; i < twoVerticalMoves.length; i++) {
			assertEquals(MoveFactory.DOWN, twoVerticalMoves[i]);
		}
	}
}

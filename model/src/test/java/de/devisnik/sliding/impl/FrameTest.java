package de.devisnik.sliding.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.devisnik.sliding.IFrameListener;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.MoveFactory;
import de.devisnik.sliding.Point;
import de.devisnik.sliding.ShiftingEvent;

public class FrameTest {

	private Frame mFrame;

	@Before
	public void createFrame() {
		mFrame = new Frame(3, 3);
	}

	@Test
	public void testNotifyListener() {
		final boolean[] swapped = new boolean[1];
		mFrame.addListener(new IFrameListener() {
			@Override
			public void handleSwap(IPiece left, IPiece right) {
				swapped[0] = true;
			}

			@Override
			public void handleShifting(ShiftingEvent[] event) {
			}
		});
		mFrame.execute(MoveFactory.UP);
		assertEquals(true, swapped[0]);
	}

	@Test
	public void testAddRemoveListener() {
		final int[] count = new int[1];
		IFrameListener listener = new IFrameListener() {

			@Override
			public void handleSwap(IPiece left, IPiece right) {
				count[0]++;
			}

			@Override
			public void handleShifting(ShiftingEvent[] event) {
			}
		};
		mFrame.addListener(listener);
		mFrame.execute(MoveFactory.UP);
		mFrame.removeListener(listener);
		mFrame.execute(MoveFactory.DOWN);
		assertEquals(1, count[0]);
	}

	@Test
	public void testAddNullListener() {
		mFrame.addListener(null);
		mFrame.execute(MoveFactory.UP);
	}

	@Test
	public void testMovingHole() {
		mFrame.execute(MoveFactory.LEFT);
		assertEquals(mFrame.getHole(), mFrame.getPieceAt(1, 2));
		assertEquals(mFrame.getPieceAt(2, 2).getHomePosition(), new Point(1, 2));

		mFrame.execute(MoveFactory.UP);
		assertEquals(mFrame.getHole(), mFrame.getPieceAt(1, 1));
		assertEquals(mFrame.getPieceAt(1, 2).getHomePosition(), new Point(1, 1));

		mFrame.execute(MoveFactory.RIGHT);
		assertEquals(mFrame.getHole(), mFrame.getPieceAt(2, 1));
		assertEquals(mFrame.getPieceAt(1, 1).getHomePosition(), new Point(2, 1));

		mFrame.execute(MoveFactory.DOWN);
		assertEquals(mFrame.getHole(), mFrame.getPieceAt(2, 2));
		assertEquals(mFrame.getPieceAt(2, 1).getHomePosition(), new Point(1, 2));

	}

	@Test
	public void testGetHole() {
		assertEquals(mFrame.getHole(), mFrame.getPieceAt(2, 2));
	}

	@Test
	public void testGetPieceAt() {
		for (IPiece piece : mFrame)
			assertEquals(piece.getPosition(), piece.getHomePosition());
	}

	@Test
	public void testGetSize() {
		assertEquals(new Point(3, 3), mFrame.getSize());
	}

	@Test
	public void shouldIterateOverAllPieces() {
		int count = 0;
		for (@SuppressWarnings("unused") IPiece piece : mFrame)
			count++;
		Point size = mFrame.getSize();
		assertEquals(size.x * size.y, count);
	}
}

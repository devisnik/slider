package de.devisnik.sliding;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import de.devisnik.sliding.FrameFactory;
import de.devisnik.sliding.IFrame;
import de.devisnik.sliding.IRandom;
import de.devisnik.sliding.Point;

public class FrameFactoryTest {

	@Test
	public void testCreate() {
		IFrame frame = FrameFactory.create(3, 4);
		assertEquals(new Point(3,4),frame.getSize());
		assertEquals(frame.getHole(), frame.getPieceAt(2, 3));
	}

	@Test
	public void testCreateRobot() {
		IFrame frame = FrameFactory.createRobot(3, 4, new IRandom() {
			
			private Random random = new Random();

			@Override
			public int nextInt(int border) {
				return random.nextInt(border);
			}
		});
		assertEquals(new Point(3,4),frame.getSize());
	}

}

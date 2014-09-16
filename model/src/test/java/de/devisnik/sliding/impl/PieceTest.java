package de.devisnik.sliding.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.devisnik.sliding.Point;

public class PieceTest {

	private static final String LABEL = "X";
	private Piece mPiece;
	@Before
	public void createTestPiece() {
		mPiece = new Piece(1, 2, LABEL);
	}
	
	@Test
	public void shouldReturnPositionGivenInConstructor() {
		assertEquals(new Point(1,2), mPiece.getPosition());
	}

	@Test
	public void shouldReturnLabelGivenInConstructor() {
		assertEquals(LABEL, mPiece.getLabel());
	}
	
	@Test
	public void shouldBeInitiallyAtHome() {
		assertEquals(true, mPiece.isAtHome());
	}
	
	@Test
	public void shouldNotBeHomeAfterChangingPosition() {
		mPiece.setPosition(0, 0);
		assertEquals(false, mPiece.isAtHome());
	}
	
	@Test
	public void shouldReturnNewPositionAfterSetting() {
		mPiece.setPosition(0, 0);
		assertEquals(new Point(0,0), mPiece.getPosition());
	}
}

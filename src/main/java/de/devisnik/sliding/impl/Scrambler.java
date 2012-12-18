package de.devisnik.sliding.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.devisnik.sliding.FrameFactory;
import de.devisnik.sliding.IMove;
import de.devisnik.sliding.IRandom;
import de.devisnik.sliding.MoveFactory;
import de.devisnik.sliding.Point;
 
public class Scrambler {

	private final static IMove[] MOVES = new IMove[] { MoveFactory.LEFT,
			MoveFactory.RIGHT, MoveFactory.DOWN, MoveFactory.UP };

	private final Frame itsFrame;

	private final IRandom itsRandom;

	public Scrambler(Frame frame, IRandom random) {
		this.itsFrame = frame;
		this.itsRandom = random;
	}

	public IMove[] scramble(int numberOfMoves) {
		return scrambleGame(numberOfMoves);
	}

	public IMove[] scramble() {
		Point size = itsFrame.getSize();
		int pieces = size.x * size.y;
		return scramble(pieces * 10);
	}

	private IMove[] scrambleGame(int numberOfMoves) {
		final IMove[] moves = new IMove[numberOfMoves];
		IMove lastInverse = null;
		for (int index = 0; index < moves.length; index++) {
			IMove randomMove = moveRandomAvoiding(lastInverse);
			lastInverse = MoveFactory.getInverse(randomMove);
			moves[index] = randomMove;
		}
		return moves;
	}

	private IMove moveRandomAvoiding(IMove avoid) {
		List<IMove> validMoves = determinePossibleMoves();
		validMoves.remove(avoid);
		return executeRandomMoveFrom(validMoves);
	}

	private IMove executeRandomMoveFrom(List<IMove> moves) {
		IMove move = moves.get(itsRandom.nextInt(moves.size()));
		itsFrame.moveHole(move);
		return move;
	}

	public List<IMove> determinePossibleMoves() {
		ArrayList<IMove> moves = new ArrayList<IMove>();
		moves.addAll(Arrays.asList(MOVES));
		Point holePosition = itsFrame.getHole().getPosition();
		if (holePosition.x == 0)
			moves.remove(MoveFactory.LEFT);
		Point frameDimension = itsFrame.getSize();
		if (holePosition.x == frameDimension.x - 1)
			moves.remove(MoveFactory.RIGHT);
		if (holePosition.y == 0)
			moves.remove(MoveFactory.UP);
		if (holePosition.y == frameDimension.y - 1)
			moves.remove(MoveFactory.DOWN);
		return moves;
	}

	public static void main(String[] args) {
		new Scrambler((Frame) FrameFactory.create(4, 4), new IRandom() {
			Random random = new Random();

			public int nextInt(int border) {
				return random.nextInt(border);
			}
		}).scramble();
	}
}

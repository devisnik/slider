/*
 * Created on 07.11.2006 by leck
 *
 */
package de.devisnik.sliding;

import java.util.Arrays;

import de.devisnik.sliding.impl.Move;

public final class MoveFactory {

    public final static IMove LEFT = new Move(-1,0);
    public final static IMove RIGHT = new Move(1,0);
    public final static IMove DOWN = new Move(0, 1);
    public final static IMove UP = new Move(0, -1);

    private MoveFactory() {
    }

    public static IMove[] getMovesTo(int x, int y) {
    	if (x != 0 && y != 0)
			return new IMove[0];
    	if (x != 0)
			return createValueArray(Math.abs(x), x > 0 ? RIGHT : LEFT);
    	if (y != 0)
			return createValueArray(Math.abs(y), y > 0 ? DOWN : UP);
    	throw new IllegalArgumentException();
    }
    
    private static IMove[] createValueArray(int length, IMove value) {
    	IMove[] moves = new IMove[length];
    	Arrays.fill(moves, value);
    	return moves;
    }
    
    public static IMove getInverse(IMove move) {
        if (move == LEFT)
			return RIGHT;
        if (move == RIGHT)
			return LEFT;
        if (move == DOWN)
			return UP;
        if (move == UP)
			return DOWN;
        throw new IllegalArgumentException();
    }

}

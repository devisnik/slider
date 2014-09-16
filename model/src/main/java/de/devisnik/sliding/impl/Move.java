/*
 * Created on 26.10.2006 by leck
 *
 */
package de.devisnik.sliding.impl;

import de.devisnik.sliding.IMove;

public class Move implements IMove {
    
	private final int x;
    private final int y;

    public Move(int moveX, int moveY) {
        x = moveX;
        y = moveY;
    }
    
    public int getX() {
		return x;
	}
    
    public int getY() {
		return y;
	}
    
}

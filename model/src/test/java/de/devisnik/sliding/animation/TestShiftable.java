/*
 * Created on 10.11.2006 by leck
 *
 */
package de.devisnik.sliding.animation;

import de.devisnik.sliding.Point;
import de.devisnik.sliding.animation.IShiftable;

class TestShiftable implements IShiftable {

    
    private final Point itsPosition;

    public TestShiftable(int startX, int startY) {
        itsPosition = new Point(startX, startY);
    }

    public Point getPosition() {
        return itsPosition;
    }

    public boolean isDisposed() {
        return false;
    }

    public void setPosition(int x, int y) {
        itsPosition.x = x;
        itsPosition.y = y;
    }

	public Point getSize() {
		return new Point(10,10);
	}

	@Override
	public void shift(Point delta) {
		// TODO Auto-generated method stub
		
	}
}
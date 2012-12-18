package de.devisnik.sliding.animation;

import de.devisnik.sliding.Point;

public class ShiftAnimation extends Animation {

	public ShiftAnimation(IShiftable shiftable, Point piecePositionDelta,
			int steps) {
		super(shiftable, new Translation(Point.times(shiftable.getSize(),
				piecePositionDelta), steps, new AccelerateDecelerateInterpolator()));
	}

}

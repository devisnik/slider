package de.devisnik.sliding.animation;

import de.devisnik.sliding.Point;

public class Translation implements IMovement {

	private int itsSteps;
	private Point itsTarget;
	private Point itsCurrentPoint = new Point(0, 0);
	private int itsCurrentIndex = 0;
	private final IInterpolator itsInterpolator;

	public Translation(int width, int height, int steps, IInterpolator interpolator) {
		this(new Point(width, height), steps, interpolator);
	}

	public Translation(Point target, int steps, IInterpolator interpolator) {
		itsSteps = steps;
		itsTarget = target;
		itsInterpolator = interpolator;
	}

	public int goneStepsNumber() {
		return itsCurrentIndex;
	}

	public boolean hasNext() {
		return itsCurrentIndex < itsSteps;
	}

	public Point next() {
		itsCurrentIndex++;
		float interpolatedTime = itsInterpolator.getInterpolation(((float)itsCurrentIndex)/itsSteps);
		Point target = Point.times(itsTarget, interpolatedTime);
//		Point target = Point.times(itsTarget, itsCurrentIndex).divideBy(itsSteps);
		Point step = Point.diff(target, itsCurrentPoint);
		itsCurrentPoint = target;
		return step;
	}
}

package de.devisnik.sliding.animation;

import de.devisnik.sliding.Point;

public interface IMovement {

	int goneStepsNumber();

	boolean hasNext();

	Point next();

}
package de.devisnik.sliding.animation;

import de.devisnik.sliding.Point;

public interface IShiftable {

	Point getSize();
	void shift(Point delta);
}

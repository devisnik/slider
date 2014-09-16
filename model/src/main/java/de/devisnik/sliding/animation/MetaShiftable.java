package de.devisnik.sliding.animation;
import java.util.ArrayList;

import de.devisnik.sliding.Point;

public class MetaShiftable implements IShiftable {

	private final Point itsSize;
	private final ArrayList<Animation> itsShifters = new ArrayList<Animation>();
	private final int itsSteps;

	public MetaShiftable(int steps) {
		itsSteps = steps;
		itsSize = new Point(steps, steps);
	}

	public void addShifterFor(IShiftable shiftable, Point shift) {
		ShiftAnimation shifter = new ShiftAnimation(shiftable, shift, itsSteps);
		itsShifters.add(shifter);
	}

	public Point getSize() {
		return itsSize;
	}

	public void shift(Point delta) {
		for (Animation shifter : itsShifters)
			shifter.stepNext();
	}

}
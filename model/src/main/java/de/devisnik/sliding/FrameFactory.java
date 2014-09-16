package de.devisnik.sliding;

import de.devisnik.sliding.impl.RobotFrame;
import de.devisnik.sliding.impl.Frame;

public class FrameFactory {

	private FrameFactory() {		
	}
	
	public static IFrame create(int dimX, int dimY) {
		return new Frame(dimX, dimY);
	}
	
	public static IRobotFrame createRobot(int dimX, int dimY, IRandom random) {
		return new RobotFrame(dimX, dimY, random);
	}
}

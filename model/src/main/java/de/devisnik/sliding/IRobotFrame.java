package de.devisnik.sliding;

public interface IRobotFrame extends IFrame {

	boolean scramble();
	boolean replayNext();	
	void resolve();
	boolean isResolved();
}

package de.devisnik.sliding.animation;


public class Animation {

	private final IShiftable itsShiftable;
	private final IMovement itsMovement;

	public Animation(IShiftable shiftable, IMovement transition) {
		itsShiftable = shiftable;
		itsMovement = transition;
	}

	public boolean hasNext() {
		return itsMovement.hasNext();
	}

	public void stepNext() {
		itsShiftable.shift(itsMovement.next());
	}
	
	public int currentStep() {		
		return itsMovement.goneStepsNumber();
	}
}

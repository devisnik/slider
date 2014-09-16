package de.devisnik.sliding.animation;

public abstract class AnimationRunner {

	protected final Animation itsAnimation;

	public AnimationRunner(Animation animation) {
		itsAnimation = animation;
	}

	public void start() {
		onStartShifting();
		schedule(new Runnable() {

			public void run() {
				itsAnimation.stepNext();
				if (itsAnimation.hasNext())
					schedule(this);
				else
					onDoneShifting();
			}
		});
	}

	protected void onDoneShifting() {
		// does nothing
	}

	protected void onStartShifting() {
		// does nothing
	}

	protected abstract void schedule(Runnable runnable);

}

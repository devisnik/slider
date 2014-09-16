package de.devisnik.android.sliding;

import android.os.Handler;
import de.devisnik.android.sliding.tile.TileShifter;
import de.devisnik.android.sliding.tile.TileStore;
import de.devisnik.sliding.IFrameListener;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.IRobotFrame;
import de.devisnik.sliding.IShifterListener;
import de.devisnik.sliding.ShiftingEvent;

final class Replayer implements Runnable, IFrameListener {

	private final static String TAG = "Replayer";

	private final class Scrambler implements Runnable {
		@Override
		public void run() {
			itsAnimationTracker.inc();
			itsFrame.scramble();
			itsTileStore.get(itsFrame.getHole()).setHidden(true);
		}
	}

	private final class Resolver implements Runnable {
		@Override
		public void run() {
			itsAnimationTracker.inc();
			itsFrame.resolve();
		}
	}

	private class Rescheduler implements IShifterListener {
		@Override
		public void doneShifting() {
			if (itsRunning)
				reschedule();
		}
	}

	private class AnimationTracker implements IShifterListener {

		private int itsCounter;

		public void inc() {
			itsCounter++;
		}

		public boolean isAnimationRunning() {
			return itsCounter > 0;
		}

		@Override
		public void doneShifting() {
			itsCounter--;
			if (itsCounter == 0)
				reschedule();
		}
	}

	private final IRobotFrame itsFrame;
	private final Handler itsHandler = new Handler();
	private boolean itsRunning;
	private final ISpeed itsSpeed;
	private final Rescheduler itsRescheduler = new Rescheduler();
	private final AnimationTracker itsAnimationTracker = new AnimationTracker();
	private final Scrambler itsScrambler = new Scrambler();
	private final Resolver itsResolver = new Resolver();
	private final boolean itsPreview;
	private final TileStore itsTileStore;

	Replayer(final IRobotFrame frame, final TileStore tileStore, final ISpeed speed, final boolean preview) {
		itsFrame = frame;
		itsTileStore = tileStore;
		itsPreview = preview;
		itsFrame.addListener(this);
		itsSpeed = speed;
	}

	public void onClick() {
		if (!itsFrame.isResolved())
			post(itsResolver);
		else
			post(itsScrambler);
	}

	@Override
	public void run() {
		if (itsAnimationTracker.isAnimationRunning())
			return;
		if (!itsFrame.replayNext()) {
			itsTileStore.get(itsFrame.getHole()).setHidden(false);
			post(itsScrambler, itsPreview ? 1000 : itsSpeed.getWaitAfterSolved());
		}
	}

	private void post(final Runnable runnable, final int delayInMillis) {
		itsHandler.removeCallbacks(runnable);
		itsHandler.postDelayed(runnable, delayInMillis);
	}

	private void post(final Runnable runnable) {
		itsHandler.removeCallbacks(runnable);
		itsHandler.post(runnable);
	}

	public final void start() {
		Logger.d(TAG, "start");
		if (itsRunning)
			return;
		reschedule();
		itsRunning = true;
	}

	private void reschedule() {
		post(this);
	}

	public final void pause() {
		Logger.d(TAG, "pause");
		itsHandler.removeCallbacks(this);
		itsRunning = false;
	}

	@Override
	public void handleSwap(final IPiece left, final IPiece right) {
		// Logger.d(TAG, "handleSwap");
		TileShifter tileShifter = TileShifter.create(itsTileStore, itsHandler, itsSpeed.getShiftDuration(),
				itsRescheduler);
		tileShifter.animate(left, right).start();
	}

	@Override
	public void handleShifting(final ShiftingEvent[] events) {
		// Logger.d(TAG, "handleShifting");
		TileShifter tileShifter = TileShifter.create(itsTileStore, itsHandler, itsSpeed.getScrambleDuration(),
				itsAnimationTracker);
		tileShifter.animate(events).start();
	}
}
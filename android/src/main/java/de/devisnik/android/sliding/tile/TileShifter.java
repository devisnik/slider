package de.devisnik.android.sliding.tile;

import android.os.Handler;
import android.os.SystemClock;
import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.IShifterListener;
import de.devisnik.sliding.Point;
import de.devisnik.sliding.ShiftingEvent;
import de.devisnik.sliding.animation.AnimationRunner;
import de.devisnik.sliding.animation.MetaShiftable;
import de.devisnik.sliding.animation.ShiftAnimation;

public class TileShifter extends AnimationRunner {

	private static final int STEP_DURATION = 16;
	private final Handler itsHandler;
	private long itsStartTime;
	private final IShifterListener itsListener;
	private final MetaShiftable itsShiftable;
	private final TileStore itsTileStore;

	public static TileShifter create(final TileStore tileStore, final Handler handler, final int durationInMillis,
			final IShifterListener listener) {
		int steps = durationInMillis / STEP_DURATION;
		MetaShiftable metaShiftable = new MetaShiftable(steps);
		return new TileShifter(tileStore, metaShiftable, handler, steps, listener);
	}

	private TileShifter(final TileStore tileStore, final MetaShiftable shiftable, final Handler handler,
			final int steps, final IShifterListener listener) {
		super(new ShiftAnimation(shiftable, new Point(0, 0), steps));
		itsTileStore = tileStore;
		itsShiftable = shiftable;
		itsHandler = handler;
		itsListener = listener;
	}

	@Override
	protected void onStartShifting() {
		itsStartTime = SystemClock.uptimeMillis();
	}

	@Override
	protected void onDoneShifting() {
		if (itsListener != null)
			itsListener.doneShifting();
	}

	@Override
	protected void schedule(final Runnable runnable) {
		itsHandler.postAtTime(runnable, itsStartTime + itsAnimation.currentStep() * STEP_DURATION);
	}

	public TileShifter animate(final IPiece left, final IPiece right) {
		Point diff = Point.diff(left.getPosition(), right.getPosition());
		animate(left, diff);
		animate(right, Point.times(diff, -1));
		return this;
	}

	private void animate(final IPiece piece, final Point diff) {
		itsShiftable.addShifterFor(itsTileStore.get(piece), diff);
	}

	private void animate(final ShiftingEvent event) {
		itsShiftable.addShifterFor(itsTileStore.get(event.getPiece()),
				Point.diff(event.getNewPosition(), event.getOldPosition()));
	}

	public TileShifter animate(final ShiftingEvent[] events) {
		for (ShiftingEvent event : events) {
			animate(event);
		}
		return this;
	}
}

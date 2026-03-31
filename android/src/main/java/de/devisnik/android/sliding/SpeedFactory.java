package de.devisnik.android.sliding;

import android.content.res.Resources;

public class SpeedFactory {

	private static class Speed implements ISpeed {

		private final int itsScrambleDuration;
		private final int itsShiftDuration;

		Speed(int scrambleDuration, int shiftDuration) {
			itsScrambleDuration = scrambleDuration;
			itsShiftDuration = shiftDuration;
		}

		@Override
		public int getScrambleDuration() {
			return itsScrambleDuration;
		}

		@Override
		public int getShiftDuration() {
			return itsShiftDuration;
		}
	}

	private static final ISpeed SLOWEST = new Speed(1800, 900);
	private static final ISpeed SLOW = new Speed(1200, 600);
	private static final ISpeed NORMAL = new Speed(800, 400);
	private static final ISpeed FAST = new Speed(500, 250);
	private static final ISpeed FASTEST = new Speed(400, 200);
	private final Resources itsResources;

	public SpeedFactory(Resources resources) {
		itsResources = resources;
	}

	private String getString(int key) {
		return itsResources.getString(key);
	}

	public ISpeed getSpeed(String value) {
		if (getString(R.string.frame_speed_fastest_key).equals(value))
			return FASTEST;
		if (getString(R.string.frame_speed_fast_key).equals(value))
			return FAST;
		if (getString(R.string.frame_speed_normal_key).equals(value))
			return NORMAL;
		if (getString(R.string.frame_speed_slow_key).equals(value))
			return SLOW;
		if (getString(R.string.frame_speed_slowest_key).equals(value))
			return SLOWEST;
		return NORMAL;
	}
}

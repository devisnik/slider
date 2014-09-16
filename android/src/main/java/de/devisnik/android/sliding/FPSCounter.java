package de.devisnik.android.sliding;

import java.util.concurrent.TimeUnit;

import android.os.SystemClock;

public class FPSCounter {

	private static final long UNIT = TimeUnit.SECONDS.toMillis(1);
	private long mIntervalStart = 0;
	private int mCounter;
	private String mFps = "unknown";

	public void inc() {
		long uptimeMillis = SystemClock.uptimeMillis();
		if (uptimeMillis > mIntervalStart + UNIT) {
			mFps = Integer.toString(mCounter);
			mCounter = 0;
			mIntervalStart = uptimeMillis;
		}
		mCounter += 1;
	}

	public String getFPS() {
		return mFps;
	}
}

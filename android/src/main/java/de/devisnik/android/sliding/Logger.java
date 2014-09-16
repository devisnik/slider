package de.devisnik.android.sliding;

import android.util.Log;

public class Logger {

	private final static String APPLICATION_TAG = "slider";

	public static int i(final String tag, final String msg) {
		if (Log.isLoggable(APPLICATION_TAG, Log.INFO))
			return Log.i(APPLICATION_TAG, tag + ": " + msg);
		return 0;
	}

	public static int d(final String tag, final String msg) {
		if (Log.isLoggable(APPLICATION_TAG, Log.DEBUG))
			return Log.d(APPLICATION_TAG, tag + ": " + msg);
		return 0;
	}

	public static int w(final String tag, final String msg) {
		if (Log.isLoggable(APPLICATION_TAG, Log.WARN))
			return Log.w(APPLICATION_TAG, tag + ": " + msg);
		return 0;
	}

	public static int e(final String tag, final String msg) {
		if (Log.isLoggable(APPLICATION_TAG, Log.ERROR))
			return Log.e(APPLICATION_TAG, tag + ": " + msg);
		return 0;
	}

	public static boolean isDebugEnabled() {
		return Log.isLoggable(APPLICATION_TAG, Log.DEBUG);
	}
}

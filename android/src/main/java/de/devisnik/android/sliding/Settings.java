package de.devisnik.android.sliding;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import de.devisnik.sliding.Point;

public class Settings {

	private static final String DEFAULT_PUZZLE_SIZE_KEY = "frame_medium_key";
	private static final String DEF_PACKAGE = "de.devisnik.android.sliding";
	private final SharedPreferences itsPreferences;
	private final Resources itsResources;
	private final SpeedFactory itsSpeedFactory;
	private final ImageCache itsImageCache;
	private Bitmap itsImage;

	public Settings(final Context context, final SharedPreferences preferences) {
		itsResources = context.getResources();
		itsPreferences = preferences;
		itsSpeedFactory = new SpeedFactory(itsResources);
		itsImageCache = new ImageCache(context.getCacheDir());
	}

	public Bitmap readImage() {
		if (itsImage == null)
			itsImage = itsImageCache.get();
		return itsImage;
	}

	public void clearCachedImage() {
		itsImage = null;
	}

	private String getString(final int key) {
		return itsResources.getString(key);
	}

	private String getStringPreference(final int key, final String defaultValue) {
		return itsPreferences.getString(getString(key), defaultValue);
	}

	private String getStringPreference(final int key, final int defaultValueKey) {
		return itsPreferences.getString(getString(key), getString(defaultValueKey));
	}

	public Point getFrameSize(final int width, final int height) {
		String framePref = getStringPreference(R.string.pref_key_puzzle_size, DEFAULT_PUZZLE_SIZE_KEY);
		int identifier = itsResources.getIdentifier(framePref, "array", DEF_PACKAGE);
		int[] frameArray = itsResources.getIntArray(identifier);
		Point point = new Point(frameArray[0], frameArray[1]);
		if (width > height)
			point.flip();
		return point;
	}

	public boolean isUseDefaultImage() {
		return itsPreferences.getBoolean(getString(R.string.pref_key_use_default_image), false);
	}

	public String getImagePath() {
		return getStringPreference(R.string.pref_key_select_image, null);
	}

	public ISpeed getSpeed() {
		String speedSetting = getStringPreference(R.string.pref_key_puzzle_speed, R.string.frame_speed_normal_key);
		return itsSpeedFactory.getSpeed(speedSetting);
	}
}

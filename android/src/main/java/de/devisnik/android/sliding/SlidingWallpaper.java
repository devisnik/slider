package de.devisnik.android.sliding;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class SlidingWallpaper extends WallpaperService {

	public static final String SHARED_PREFS_NAME = "slider";

	private final Handler mHandler = new Handler();

	@Override
	public Engine onCreateEngine() {
		return new SliderEngine();
	}

	class SliderEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

		private static final String TAG = "SliderEngine";
		private static final String TAP_COMMAND = "android.wallpaper.tap";
		private static final int TARGET_FPS = 90;
		private static final int TARGET_FRAME_DURATION = 1000 / TARGET_FPS;
		private final Runnable mDrawFrame = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private volatile boolean mVisible;
		private FrameDrawer itsFrameDrawer;
		private volatile int itsWidth;
		private volatile int itsHeight;
		private final Settings itsSettings;

		@SuppressLint("NewApi")
		SliderEngine() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
				setOffsetNotificationsEnabled(false);
			SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
			preferences.registerOnSharedPreferenceChangeListener(this);
			itsSettings = new Settings(SlidingWallpaper.this, preferences);
		}

		@Override
		public void onDestroy() {
			mHandler.removeCallbacks(mDrawFrame);
			super.onDestroy();
		}

		@Override
		public void onVisibilityChanged(final boolean visible) {
			if (mVisible == visible)
				return;
			mVisible = visible;
			Logger.d(TAG, "visibility change to: " + Boolean.toString(visible));
			if (visible)
				makeVisible();
			else
				makeInvisible();
		}

		private void makeInvisible() {
			Logger.d(TAG, "makeInvisible");
			if (itsFrameDrawer != null)
				itsFrameDrawer.stop();
			mHandler.removeCallbacks(mDrawFrame);
		}

		private void makeVisible() {
			Logger.d(TAG, "makeVisible");
			// bug fix: NPE is thrown here on some (which?) devices (reported via google play bug reports)
			if (itsFrameDrawer == null)
				return;
			itsFrameDrawer.start();
			mHandler.removeCallbacks(mDrawFrame);
			mHandler.post(mDrawFrame);
		}

		@Override
		public void onSurfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
			super.onSurfaceChanged(holder, format, width, height);
			Logger.d(TAG, "surface change: " + width + "," + height);
			itsWidth = width;
			itsHeight = height;
			createFrameDrawer();
		}

		private void createFrameDrawer() {
			Logger.d(TAG, "createFrameDrawer");
			makeInvisible();
			itsFrameDrawer = new FrameDrawer(itsSettings, itsWidth, itsHeight, isPreview());
			if (mVisible)
				makeVisible();
		}

		@Override
		public void onSurfaceCreated(final SurfaceHolder holder) {
			Logger.d(TAG, "onSurfaceCreated");
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(final SurfaceHolder holder) {
			Logger.d(TAG, "onSurfaceDestroyed");
			mVisible = false;
			makeInvisible();
			super.onSurfaceDestroyed(holder);
		}

		@Override
		public Bundle onCommand(final String action, final int x, final int y, final int z, final Bundle extras,
				final boolean resultRequested) {
			Logger.d(TAG, "onCommand: " + action);
			if (TAP_COMMAND.equals(action))
				itsFrameDrawer.handleTap();
			return super.onCommand(action, x, y, z, extras, resultRequested);
		}

		@SuppressWarnings("boxing")
		@Override
		public void onOffsetsChanged(final float xOffset, final float yOffset, final float xStep, final float yStep,
				final int xPixels, final int yPixels) {
			PropertiesBuilder builder = new PropertiesBuilder();
			builder.append("xOffset", xOffset).append("yOffset", yOffset);
			builder.append("xStep", xStep).append("yStep", yStep);
			builder.append("xPixels", xPixels).append("yPixels", yPixels);
			Logger.d(TAG, "onOffsetChanged: " + builder.toString());
		}

		void drawFrame() {
			long startTime = SystemClock.uptimeMillis();
			itsFrameDrawer.draw(getSurfaceHolder());

			// Reschedule the next redraw
			mHandler.removeCallbacks(mDrawFrame);
			if (mVisible)
				mHandler.postDelayed(mDrawFrame,
						Math.max(1, TARGET_FRAME_DURATION - (SystemClock.uptimeMillis() - startTime)));
		}

		@Override
		public void onSharedPreferenceChanged(final SharedPreferences arg0, final String key) {
			Logger.d(TAG, "onSharedPreferenceChanged: key = " + key);
			itsSettings.clearCachedImage();
			createFrameDrawer();
		}
	}
}

package de.devisnik.android.sliding;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OpenActivity extends Activity {

	@Override
	public void onCreate(final Bundle icicle) {
		super.onCreate(icicle);
		// straight to our wallpaper preview & settings if this is supported by the device
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			startWallpaperSettings();
			return;
		}
		setContentView(R.layout.installer);
		Button startChooser = (Button) findViewById(R.id.startChooserButton);
		startChooser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				startWallpaperChooser();
			}
		});
	}

	private void startWallpaperChooser() {
		Intent intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
		startActivity(intent);
		finish();
	}

	/**
	 * Starting with API 16 we can start the wallpaper settings directly.
	 */
	@TargetApi(16)
	private void startWallpaperSettings() {
		Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
		intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
				new ComponentName(this, SlidingWallpaper.class));
		startActivity(intent);
		finish();
	}
}

package de.devisnik.android.sliding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;

public class OpenPreviewActivity extends Activity {
	private static final String EXTRA_LIVE_WALLPAPER_INTENT = "android.live_wallpaper.intent";
	private static final String EXTRA_LIVE_WALLPAPER_SETTINGS = "android.live_wallpaper.settings";
	private static final String EXTRA_LIVE_WALLPAPER_PACKAGE = "android.live_wallpaper.package";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// :-( leads to a security exception
		Intent sliderIntent = new Intent(WallpaperService.SERVICE_INTERFACE);
		sliderIntent.setClassName("de.devisnik.android.sliding",
				"de.devisnik.android.sliding.SlidingWallpaper");

		Intent preview = new Intent();
		preview.setClassName("com.android.wallpaper.livepicker",
				"com.android.wallpaper.livepicker.LiveWallpaperPreview");
		preview.putExtra(EXTRA_LIVE_WALLPAPER_INTENT, sliderIntent);
		preview.putExtra(EXTRA_LIVE_WALLPAPER_SETTINGS,
				"de.devisnik.android.sliding.SlidingPreferences");
		preview.putExtra(EXTRA_LIVE_WALLPAPER_PACKAGE, "de.devisnik.android.sliding");
		try {
			startActivity(preview);
		} catch (SecurityException e) {
			System.out.println(e);
		}

		finish();
	}
}

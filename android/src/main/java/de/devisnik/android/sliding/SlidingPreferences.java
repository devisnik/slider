package de.devisnik.android.sliding;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.provider.MediaStore;
import android.widget.Toast;

public class SlidingPreferences extends PreferenceActivity implements OnPreferenceClickListener {

	private static final int SAVE_IMAGE_PROGRESS = 2222;
	private static final int REQUEST_SELECT_IMAGE = 1111;
	private static final String TAG = SlidingPreferences.class.getSimpleName();
	private final SummaryUpdater itsSummaryUpdater = new SummaryUpdater();
	private SaveImageTask itsSaveImageTask;

	static class SaveImageTask extends AsyncTask<String, Void, String> {

		private final ImageFactory itsImageFactory;
		private SlidingPreferences itsActivity;
		private final ImageCache itsImageCache;
		private final int itsMinSize;
		private final SharedPreferences itsPreferences;
		private final String itsImagePrefKey;

		public SaveImageTask(final SlidingPreferences activity, final SharedPreferences preferences,
				final String imagePrefKey) {
			itsActivity = activity;
			itsPreferences = preferences;
			itsImagePrefKey = imagePrefKey;
			itsImageFactory = new ImageFactory();
			itsImageCache = new ImageCache(activity.getCacheDir());
			itsMinSize = computeMinSize(activity);
		}

		private static int computeMinSize(final Activity activity) {
			int width = activity.getWindowManager().getDefaultDisplay().getWidth();
			int height = activity.getWindowManager().getDefaultDisplay().getHeight();
			int maxDisplay = Math.max(width, height);
			int minSize = maxDisplay > 0 ? maxDisplay : 480;
			while (minSize * minSize > 2 * width * height)
				minSize /= 2;
			Logger.d("SaveImageTask", "maxDisplay=" + maxDisplay + ", minSize will be " + minSize);
			return minSize;
		}

		@Override
		protected void onPreExecute() {
			if (itsActivity != null)
				itsActivity.showImageProgress();
		}

		@Override
		protected String doInBackground(final String... params) {
			String imagePath = params[0];
			Bitmap bitmap = itsImageFactory.createFromPath(imagePath, itsMinSize / 2);
			itsImageCache.put(bitmap);
			return imagePath;
		}

		@Override
		protected void onPostExecute(final String imagePath) {
			if (itsActivity != null)
				itsActivity.hideImageProgress();
			Editor editor = itsPreferences.edit();
			editor.putString(itsImagePrefKey, imagePath);
			editor.commit();
		}

		void detach() {
			itsActivity = null;
		}

		void attach(final SlidingPreferences activity) {
			itsActivity = activity;
		}

	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(SlidingWallpaper.SHARED_PREFS_NAME);
		itsSaveImageTask = (SaveImageTask) getLastNonConfigurationInstance();
		if (itsSaveImageTask != null)
			itsSaveImageTask.attach(this);
		addPreferencesFromResource(R.xml.preferences);
		Preference imagePreference = findPreferenceWithKey(R.string.pref_key_select_image);
		imagePreference.setOnPreferenceClickListener(this);
		adjustListPreference(R.string.pref_key_puzzle_size);
		adjustListPreference(R.string.pref_key_puzzle_speed);
	}

	private Intent createSelectIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		return intent;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		itsSaveImageTask.detach();
		return itsSaveImageTask;
	}

	private Preference findPreferenceWithKey(final int key) {
		return findPreference(getString(key));
	}

	private void adjustListPreference(final int key) {
		final ListPreference preference = (ListPreference) findPreferenceWithKey(key);
		itsSummaryUpdater.onPreferenceChange(preference, preference.getValue());
		preference.setOnPreferenceChangeListener(itsSummaryUpdater);
	}

	@Override
	public boolean onPreferenceClick(final Preference preference) {
		startActivityForResult(createSelectIntent(), REQUEST_SELECT_IMAGE);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		if (id == SAVE_IMAGE_PROGRESS)
			return createProgressDialog();
		return super.onCreateDialog(id);
	}

	private Dialog createProgressDialog() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.image_progress_message));
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		return dialog;
	}

	public void showImageProgress() {
		showDialog(SAVE_IMAGE_PROGRESS);
	}

	public void hideImageProgress() {
		removeDialog(SAVE_IMAGE_PROGRESS);
		itsSaveImageTask = null;
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == REQUEST_SELECT_IMAGE)
			handleSelectResult(resultCode, data);
		else
			super.onActivityResult(requestCode, resultCode, data);
	}

	public void handleSelectResult(final int resultCode, final Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;
		Logger.d(TAG, "selected gallery image: " + data.getDataString());
		String path = convertDataUriToPath(data.getData());
		Logger.d(TAG, "selected image as path: " + path);
		if (path != null)
			saveImage(path);
		else
			Toast.makeText(this, "unable to read image", Toast.LENGTH_SHORT).show();
		// TODO: handle picasa images, see http://code.google.com/p/android/issues/detail?id=21234
	}

	private void saveImage(final String path) {
		itsSaveImageTask = new SaveImageTask(this, getPreferenceManager().getSharedPreferences(),
				getString(R.string.pref_key_select_image));
		itsSaveImageTask.execute(path);
	}

	private String convertDataUriToPath(final Uri data) {
		if (data == null)
			return null;
		Cursor cursor = getContentResolver().query(data,
				new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
		try {
			if (cursor == null || !cursor.moveToFirst())
				return null;
			return cursor.getString(0);
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

}

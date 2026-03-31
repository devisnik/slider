package de.devisnik.android.sliding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.widget.Toast;

public class SlidingPreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    private static final int REQUEST_SELECT_IMAGE = 1111;
    private static final String TAG = SlidingPreferencesFragment.class.getSimpleName();
    private final SummaryUpdater itsSummaryUpdater = new SummaryUpdater();
    private ImageSaver itsImageSaver;
    private ProgressDialog itsProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(SlidingWallpaper.SHARED_PREFS_NAME);
        itsImageSaver = new ImageSaver(getActivity().getCacheDir());
        addPreferencesFromResource(R.xml.preferences);
        Preference imagePreference = findPreferenceWithKey(R.string.pref_key_select_image);
        imagePreference.setOnPreferenceClickListener(this);
        adjustListPreference(R.string.pref_key_puzzle_size);
        adjustListPreference(R.string.pref_key_puzzle_speed);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        itsImageSaver.shutdown();
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
        startActivityForResult(
                new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                REQUEST_SELECT_IMAGE);
        return true;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
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
            Toast.makeText(getActivity(), "unable to read image", Toast.LENGTH_SHORT).show();
        // TODO: handle picasa images, see http://code.google.com/p/android/issues/detail?id=21234
    }

    private void saveImage(final String path) {
        showImageProgress();
        int minSize = computeMinSize(getActivity());
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        String prefKey = getString(R.string.pref_key_select_image);
        itsImageSaver.save(path, minSize, savedPath -> {
            hideImageProgress();
            prefs.edit().putString(prefKey, savedPath).commit();
        });
    }

    private static int computeMinSize(final Activity activity) {
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        int maxDisplay = Math.max(width, height);
        int minSize = maxDisplay > 0 ? maxDisplay : 480;
        while (minSize * minSize > 2 * width * height)
            minSize /= 2;
        Logger.d("SlidingPreferences", "maxDisplay=" + maxDisplay + ", minSize will be " + minSize);
        return minSize;
    }

    private String convertDataUriToPath(final Uri data) {
        if (data == null)
            return null;
        Cursor cursor = getActivity().getContentResolver().query(data,
                new String[]{android.provider.MediaStore.Images.ImageColumns.DATA},
                null, null, null);
        try {
            if (cursor == null || !cursor.moveToFirst())
                return null;
            return cursor.getString(0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void showImageProgress() {
        itsProgressDialog = new ProgressDialog(getActivity());
        itsProgressDialog.setMessage(getString(R.string.image_progress_message));
        itsProgressDialog.setCancelable(false);
        itsProgressDialog.setIndeterminate(true);
        itsProgressDialog.show();
    }

    public void hideImageProgress() {
        if (itsProgressDialog != null) {
            itsProgressDialog.dismiss();
            itsProgressDialog = null;
        }
    }
}

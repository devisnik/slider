package de.devisnik.android.sliding;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toolbar;

public class SlidingPreferences extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_activity);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setActionBar(toolbar);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, new SlidingPreferencesFragment())
					.commit();
		}
	}

	@Override
	public boolean onNavigateUp() {
		finish();
		return true;
	}

}

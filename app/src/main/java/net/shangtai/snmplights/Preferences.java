package net.shangtai.snmplights;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import android.util.Log;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public final static String PREF_OIDBASE="net.shangtai.snmplights.OIDBASE";
	public final static String PREF_USERNAME="net.shangtai.snmplights.USERNAME";
	public final static String PREF_AUTH_PASSWORD="net.shangtai.snmplights.AUTH_PASSWORD";
	public final static String PREF_AUTH_PROTOCOL="net.shangtai.snmplights.AUTH_PROTOCOL";
	public final static String PREF_HOST="net.shangtai.snmplights.HOST";
	public final static String PREF_PORT="net.shangtai.snmplights.PORT";

	public final static String PASSWORD_STARS="********";

	private final static String[] keys={
		PREF_OIDBASE,
		PREF_USERNAME,
		PREF_AUTH_PASSWORD,
		PREF_AUTH_PROTOCOL,
		PREF_HOST,
		PREF_PORT
	};

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		prefs=PreferenceManager.getDefaultSharedPreferences(this);

		addPreferencesFromResource(R.layout.preferences);
	}

	@Override
	public void onResume() {
		super.onResume();

		setSummaries();
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}

	/* Implements OnSharedPreferenceChangeListener */
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		setPreference(key);
	}

	public void setSummaries() {
		int i;

		for (i=0;i<keys.length;i++) {
			setPreference(keys[i]);
		}
	}

	public void setPreference(String key) {
		Preference p;
		p=findPreference(key);

		if (key.equals(PREF_AUTH_PASSWORD)) {
			p.setSummary(PASSWORD_STARS);
		} else {
			p.setSummary(prefs.getString(key, ""));
		}
	}
}


package net.shangtai.snmplights;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import net.shangtai.snmphelper.SnmpHelperMap;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public final static String PREF_OIDBASE="net.shangtai.snmplights.OIDBASE";
	public final static String PREF_USERNAME="net.shangtai.snmplights.USERNAME";
	public final static String PREF_AUTH_PASSWORD="net.shangtai.snmplights.AUTH_PASSWORD";
	public final static String PREF_AUTH_PROTOCOL="net.shangtai.snmplights.AUTH_PROTOCOL";
	public final static String PREF_PRIV_PASSWORD="net.shangtai.snmplights.PRIV_PASSWORD";
	public final static String PREF_PRIV_PROTOCOL="net.shangtai.snmplights.PRIV_PROTOCOL";
	public final static String PREF_HOST="net.shangtai.snmplights.HOST";
	public final static String PREF_PORT="net.shangtai.snmplights.PORT";

	public final static String PASSWORD_STARS="********";

	private final static String[] keys={
		PREF_OIDBASE,
		PREF_USERNAME,
		PREF_AUTH_PASSWORD,
		PREF_AUTH_PROTOCOL,
		PREF_PRIV_PASSWORD,
		PREF_PRIV_PROTOCOL,
		PREF_HOST,
		PREF_PORT
	};

	private Map<String,String> knownNames = new HashMap<String,String>();

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		prefs=PreferenceManager.getDefaultSharedPreferences(this);

		addPreferencesFromResource(R.layout.preferences);

		createKnownNamesMap();
		loadAuthProtocols();
		loadPrivProtocols();
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
		setPreferenceSummary(key);
	}

	public void setSummaries() {
		int i;

		for (i=0;i<keys.length;i++) {
			setPreferenceSummary(keys[i]);
		}
	}

	public void setPreferenceSummary(String key) {
		Preference p;
		p=findPreference(key);
		String value=prefs.getString(key, "");

		if (key.equals(PREF_AUTH_PASSWORD) || key.equals(PREF_PRIV_PASSWORD)) {
			if (value.isEmpty()) {
				p.setSummary("");
			} else {
				p.setSummary(PASSWORD_STARS);
			}
		} else if (key.equals(PREF_AUTH_PROTOCOL) || key.equals(PREF_PRIV_PROTOCOL)) {
			p.setSummary(getKnownListName(value));
		} else {
			p.setSummary(value);
		}
	}

	private void createKnownNamesMap() {
		knownNames.put("", getResources().getString(R.string.none));
		knownNames.put("md5", "MD5");
		knownNames.put("sha", "SHA");
		knownNames.put("sha2", "SHA2");
		knownNames.put("des", "DES");
		knownNames.put("3des", "3DES");
		knownNames.put("aes", "AES");
		knownNames.put("aes128", "AES-128");
		knownNames.put("aes192", "AES-192");
		knownNames.put("aes256", "AES-256");
	}

	private void loadAuthProtocols() {
		ListPreference p=(ListPreference)findPreference(PREF_AUTH_PROTOCOL);

		ArrayList<String> options = new ArrayList<String>(Arrays.asList(SnmpHelperMap.getAvailableAuthMethods()));

		Collections.sort(options);
		options.add(0, "");

		// ORDER IS IMPORTANT, VALUES BEFORE ENTRIES
		p.setEntryValues(options.toArray(new String[options.size()]));
		p.setEntries(optionsListToNamesArray(options));
	}

	private void loadPrivProtocols() {
		ListPreference p=(ListPreference)findPreference(PREF_PRIV_PROTOCOL);

		ArrayList<String> options = new ArrayList<String>(Arrays.asList(SnmpHelperMap.getAvailablePrivMethods()));

		Collections.sort(options);
		options.add(0, "");

		// ORDER IS IMPORTANT, VALUES BEFORE ENTRIES
		p.setEntryValues(options.toArray(new String[options.size()]));
		p.setEntries(optionsListToNamesArray(options));
	}

	private String[] optionsListToNamesArray(ArrayList<String> options) {
		int i;

		for (i = 0;i<options.size();i++) {
			options.set(i, getKnownListName(options.get(i)));
		}

		return options.toArray(new String[options.size()]);
	}

	private String getKnownListName(String key) {
		if (knownNames.keySet().contains(key))
			return knownNames.get(key);

		return key;
	}
}


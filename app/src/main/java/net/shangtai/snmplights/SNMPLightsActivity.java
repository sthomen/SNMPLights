package net.shangtai.snmplights;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.util.Log;

public class SNMPLightsActivity extends Activity {
	// logging tag
	public final static String TAG="SNMPLights";
	// fragment
	public final static String FRAG_DEVICES="net.shangtai.snmplights.DEVICES";
	private static final String FRAG_PREFS = "net.shangtai.snmplights.PREFERENCES";

	private FragmentManager fm;

	public void onCreate(Bundle state) {
		super.onCreate(state);

		fm=getFragmentManager();

		setContentView(R.layout.main);

		ActionBar ab=getActionBar();

		ab.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME);

		addDevicesFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				refresh();
				break;
			case R.id.menu_prefs:
				addPreferencesFragment();
				return true;	/* this event was handled here */
		}

		return super.onOptionsItemSelected(item);
	}

	protected void refresh() {
		DevicesFragment df;
		df=(DevicesFragment)fm.findFragmentByTag(FRAG_DEVICES);
		df.refresh();
	}

	protected void addDevicesFragment() {
		DevicesFragment df;

		df=(DevicesFragment)fm.findFragmentByTag(FRAG_DEVICES);

		if (df == null) {
			df = new DevicesFragment();
 
			fm.beginTransaction()
				.add(R.id.main, df, FRAG_DEVICES)
				.commit();
		}
	}

	protected void addPreferencesFragment() {
		PreferencesFragment pf;

		pf=(PreferencesFragment) fm.findFragmentByTag(FRAG_PREFS);

		if (pf == null) {
			pf = new PreferencesFragment();

			fm.beginTransaction()
				.replace(R.id.main, pf, FRAG_PREFS)
				.addToBackStack(FRAG_PREFS)
				.commit();
		}
	}
}

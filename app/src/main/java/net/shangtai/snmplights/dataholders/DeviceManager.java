package net.shangtai.snmplights;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.util.Log;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

import java.io.IOException;
import java.lang.IllegalArgumentException;

import net.shangtai.snmphelper.SnmpHelper;
import net.shangtai.snmphelper.SnmpHelperTree;

public class DeviceManager {
	// NOTE: Order is important
	private static final String[] mib = new String[] {
		"index",
		"protocol",
		"model",
		"value",
		"name"
	};

	private SharedPreferences prefs;
	private SnmpHelper helper = new SnmpHelper();

	private String oidbase="";
	private List<Device> devices = new ArrayList<Device>();

	private Context context;

	public DeviceManager(Context context) {
		this.context=context;
		prefs=PreferenceManager.getDefaultSharedPreferences(context);

		oidbase=prefs.getString(Preferences.PREF_OIDBASE, "");

		setupHelper();

		instantiateDevices(loadDevices());
	}

	/*
	 * Setup & loading
	 */
	private void setupHelper() {
		String hoststr=createHostString("udp",
			prefs.getString(Preferences.PREF_HOST, ""),
			Integer.valueOf(prefs.getString(Preferences.PREF_PORT, "161")));

		String authhash=prefs.getString(Preferences.PREF_AUTH_PROTOCOL, "");
		String authpass=prefs.getString(Preferences.PREF_AUTH_PASSWORD, "");
		String privhash=prefs.getString(Preferences.PREF_PRIV_PROTOCOL, "");
		String privpass=prefs.getString(Preferences.PREF_PRIV_PASSWORD, "");

		helper.setVersion("3")
			.setAddress(hoststr)
			.setUsername(prefs.getString(Preferences.PREF_USERNAME, ""));

		if (!authhash.isEmpty()) {
			helper.setAuthHash(authhash)
				.setAuthPassword(authpass);
		}

		if (!privhash.isEmpty()) {
			helper.setPrivHash(privhash)
				.setPrivPassword(privpass);
		}

	}

	private String createHostString(String protocol, String host, Integer port) {
		StringBuilder sb=new StringBuilder();

		sb.append(protocol);
		sb.append(":");
		sb.append(host);
		sb.append("/");
		sb.append(String.valueOf(port));

		return sb.toString();
	}

	private Map<Integer,String[]> loadDevices() {
		Map<Integer,String[]> devs = new HashMap<Integer,String[]>();

		try {
			if (!helper.validateSetup())
				return null;

			Map<String,String> results = helper.walk(oidbase).getContents();
			Set<String> resultkeys = results.keySet();

			// device

			int index;
			int type;
			String value;
			String[] current;

			if (results.size() == 0)
				return null;

			Iterator iter = resultkeys.iterator();

			do {
				String key = (String)iter.next();
				String[] parts = key.split("\\.");

				index=Integer.valueOf(parts[parts.length - 1]);
				type=Integer.valueOf(parts[parts.length - 2]);

				value=results.get(key);

				if (!devs.containsKey(index)) {
					current=new String[mib.length+1];
				} else {
					current=devs.get(index);
				}

				current[type]=value;
				devs.put(index, current);

			} while (iter.hasNext());
		} catch (IOException e) {
			Log.e(SNMPLightsActivity.TAG, e.getMessage());
			return null;
		}

		return devs;
	}

	private void instantiateDevices(Map<Integer,String[]> devs) {
		if (devs == null)
			return;

		Device device;
		Set<Integer> devskeys = devs.keySet();
		Iterator iter = devskeys.iterator();

		do {
			Integer index=(Integer)iter.next();
			String[] values=devs.get(index);

			/* XXX
			 * The only real indication we have that the device is capable
			 * of dimming is the model.
			 */

			if (values[getMibIndex("model")].contains("dimmer")) {	// dimmable
				device = (Device)new Dimmer(this);
			} else {						// switch
				device = (Device)new Switch(this);
			}

			device.setIndex(index)
				.setProtocol(values[getMibIndex("protocol")])
				.setModel(values[getMibIndex("model")])
				.setValue(values[getMibIndex("value")])
				.setName(values[getMibIndex("name")]);

			devices.add(device);
		} while (iter.hasNext());
	}

	/*
	 * Usage
	 */

	public boolean isValid() {
		return helper.validateSetup();
	}

	public int countDevices() {
		return devices.size();
	}

	public Device getDeviceByIndex(int index) {
		int size=devices.size();

		if (index < 0 || index > size || size == 0)
			return null;

		return devices.get(index);
	}

	public void setValue(int index, Integer value) {
		setValue(index, value.toString());
	}

	public void setValue(int index, String value) {
		String oidstr=indexToOIDString(getMibIndex("value"), index);

		Log.d(SNMPLightsActivity.TAG, oidstr + ": " + value);

		try {
			helper.set(oidstr, "gauge32", value);
		} catch (IOException e) {
			Log.e(SNMPLightsActivity.TAG, e.getMessage());
		}
	}

	/*
	 * Utility
	 */

	private String indexToOIDString(Integer type, Integer index) {
		StringBuilder sb=new StringBuilder();

		sb.append(oidbase);
		sb.append(".");
		sb.append(type.toString());
		sb.append(".");
		sb.append(index.toString());

		return sb.toString();
	}

	private int getMibIndex(String name) {
		for (int i=0;i<mib.length;i++) {
			if (mib[i].equals(name))
				return i+1;		/* starts from 1 */
		}

		throw new IllegalArgumentException("Attempted to get the index of a MIB that doesn't exist");
	}
}

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

	public DeviceManager(Context context) {
		prefs=PreferenceManager.getDefaultSharedPreferences(context);

		oidbase=prefs.getString(Preferences.PREF_OIDBASE, "");

		setupHelper();

		instantiateDevices(loadDevices());
	}

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

		Log.d(SNMPLightsActivity.TAG, "loadDevices");

		try {
			Map<String,String> results = helper.walk(oidbase).getContents();
			Set<String> resultkeys = results.keySet();

			Log.d(SNMPLightsActivity.TAG, results.toString());

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

				index=Integer.valueOf(parts[parts.length]);
				type=Integer.valueOf(parts[parts.length - 1]);

				value=results.get(key);

				if (!devs.containsKey(index)) {
					current=new String[mib.length];
				} else {
					current=devs.get(index);
				}

				current[type]=value;
				devs.put(index, current);

				Log.d(SNMPLightsActivity.TAG, new Integer(index).toString() + ": " + mib[type] + " -> " + value);

			} while (iter.hasNext());
		} catch (IOException e) {
			Log.e(SNMPLightsActivity.TAG, e.getMessage());
			return null;
		}

		return devs;
	}

	private void instantiateDevices(Map<Integer,String[]> devs) {
		Log.d(SNMPLightsActivity.TAG, "instantiateDevices");

		if (devs == null)
			return;

		Log.d(SNMPLightsActivity.TAG, "instantiateDevices: past null check");

		Device device;
		Set<Integer> devskeys = devs.keySet();
		Iterator iter = devskeys.iterator();

		int index=1;

		do {
			String[] values=(String[])iter.next();

			if (values[getMibIndex("model")].contains("dimmer")) {	// dimmable
				device = (Device)new Dimmer(this);
			} else {						// switch
				device = (Device)new Switch(this);
			}

			device.setIndex(index++)
				.setProtocol(values[getMibIndex("protocol")])
				.setModel(values[getMibIndex("model")])
				.setValue(values[getMibIndex("value")])
				.setName(values[getMibIndex("name")]);

			devices.add(device);
		} while (iter.hasNext());

		Log.d(SNMPLightsActivity.TAG, devices.toString());
	}

	private int getMibIndex(String name) {
		for (int i=0;i<mib.length;i++) {
			if (mib[i].equals(name))
				return i;
		}

		throw new IllegalArgumentException("Attempted to get the index of a MIB that doesn't exist");
	}
}

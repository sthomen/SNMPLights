package net.shangtai.snmplights;

import android.app.IntentService;
import android.content.Intent;

import org.snmp4j.Snmp;

public class SNMPClientService extends IntentService {
    private Snmp snmp = null;
    private Device[] devices=null;

    public SNMPClientService() {
        super("SNMPClientService");
        snmp=new Snmp();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public void probeDevices() {
        snmp.
    }
}

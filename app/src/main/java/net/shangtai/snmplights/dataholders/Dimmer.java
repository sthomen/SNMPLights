package net.shangtai.snmplights.dataholders;

public class Dimmer extends Device {
	public Dimmer(DeviceManager dm) {
		this.dm = dm;
	}

	public void dim(int value) {
		dm.setValue(index, value);
	}
}


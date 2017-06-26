package net.shangtai.snmplights;

public class Switch extends Device {
	public Switch(DeviceManager dm) {
		this.dm = dm;
	}

	public void on() {
		dm.setValue(index, 1);
	}

	public void off() {
		dm.setValue(index, 0);
	}
}

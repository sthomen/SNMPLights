package net.shangtai.snmplights;

public abstract class Device {
	protected Integer index = null;
	protected String protocol = null;
	protected String model = null;
	protected Integer value = null;
	protected String name = null;
	protected DeviceManager dm = null;

	public Integer getIndex() {
		return index;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getModel() {
		return model;
	}

	public Integer getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public Device setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public Device setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	public Device setModel(String model) {
		this.model = model;
		return this;
	}

	public Device setValue(String value) {
		this.value = Integer.valueOf(value);
		return this;
	}

	public Device setValue(Integer value) {
		this.value = value;
		return this;
	}

	public Device setName(String name) {
		this.name = name;
		return this;
	}
}

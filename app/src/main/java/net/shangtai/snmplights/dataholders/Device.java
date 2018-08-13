package net.shangtai.snmplights.dataholders;

public abstract class Device {
	protected Integer index = null;
	protected String protocol = null;
	protected String model = null;
	protected String value = null;
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

	public String getValue() {
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
		this.value = value;
		return this;
	}

	public Device setName(String name) {
		this.name = name;
		return this;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Device: ");
		sb.append(this.getClass().getSimpleName());
		sb.append(" index=");
		sb.append(this.index);
		sb.append(" protocol=");
		sb.append(this.protocol);
		sb.append(" model=");
		sb.append(this.model);
		sb.append(" value=");
		sb.append(this.value);
		sb.append(" name=");
		sb.append(this.name);

		return sb.toString();
	}
}

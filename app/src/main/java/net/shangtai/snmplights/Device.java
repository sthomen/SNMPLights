package net.shangtai.snmplights;
public class Device {
    private Integer index;
    private String protocol;
    private String model;
    private Integer value;
    private String name;

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

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }
}

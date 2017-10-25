package com.viettel.fw.gencode;

/**
 * Created by vtsoft on 4/13/2015.
 */
public class EndPointInfo {
    private String id;
    private String implementor;
    private String address;

    public EndPointInfo(String id, String implementor, String address) {
        this.id = id;
        this.implementor = implementor;
        this.address = address;
    }

    @Override
    public String toString() {
        return "<jaxws:endpoint" +
                "\n id=\"" + id + "\"" +
                "\n implementor=\"" + implementor + "\"" +
                "\n address=\"" + address + "\">" +
                "\n        <jaxws:handlers>" +
                "\n            <bean parent=\"genericWebInfoHandler\"/>" +
                "\n        </jaxws:handlers>" +
                "\n</jaxws:endpoint>"
                ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImplementor() {
        return implementor;
    }

    public void setImplementor(String implementor) {
        this.implementor = implementor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

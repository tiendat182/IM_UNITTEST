package com.viettel.ws.provider;

import com.google.common.base.Strings;
import com.viettel.fw.common.util.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vtsoft on 4/3/2015.
 */
public class WsEndpoint {
    public static final String PW_TEXT = "PW_TEXT";
    public static final String PW_DIGEST = "PW_DIGEST";
    public static final String PW_NONE = "PW_NONE";

    private String name;
    private String address;
    private List<String> alternateAddressList = new ArrayList<>();
    private String targetNameSpace;
    private String userName;
    private String password;
    private int connectionTimeout = 0;
    private int receiveTimeout = 0;
    private String passwordType = PW_NONE;
    private String prefix;
    //MinhNH
    private boolean injectGenericWebInfo = true;
    private boolean ignoreSecurity;

    public WsEndpoint() {
    }

    public WsEndpoint(String name, String address, String targetNameSpace) {
        setAddress(address);
        setTargetNameSpace(targetNameSpace);
        this.name = name;
        this.address = address;
        this.targetNameSpace = targetNameSpace;
    }

    public WsEndpoint(String name, String address, String targetNameSpace, String userName, String password) {
        setAddress(address);
        setTargetNameSpace(targetNameSpace);
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressConnectBean(String endpointName) {
        return getAddressConnectBean(address, endpointName);
    }

    public String getAddressConnectBean(String orgAddr, String endpointName) {
        String nAddress = orgAddr;

        if (!Strings.isNullOrEmpty(orgAddr)) {
            if (!orgAddr.endsWith("/")) nAddress = orgAddr + "/";
        }

        if (!DataUtil.isNullOrEmpty(prefix)) {
            return nAddress + prefix + endpointName;
        } else {
            return nAddress + endpointName;
        }
    }

    public List<String> getAlternateAddressConnectBean(String endpointName) {
        return alternateAddressList.stream().map(x -> getAddressConnectBean(x, endpointName)).collect(Collectors.toList());
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTargetNameSpace() {
        return targetNameSpace;
    }

    public void setTargetNameSpace(String targetNameSpace) {
        this.targetNameSpace = targetNameSpace;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String toString() {
        return "WsEndpoint{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", targetNameSpace='" + targetNameSpace + '\'' +
                ", passwordType='" + passwordType + '\'' +
                ", connectionTimeout='" + connectionTimeout + '\'' +
                '}';
    }

    public String getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(String passwordType) {
        this.passwordType = passwordType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(int receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public boolean isInjectGenericWebInfo() {
        return injectGenericWebInfo;
    }

    public void setInjectGenericWebInfo(boolean injectGenericWebInfo) {
        this.injectGenericWebInfo = injectGenericWebInfo;
    }

    public List<String> getAlternateAddressList() {
        return alternateAddressList;
    }

    public void setAlternateAddressList(List<String> alternateAddressList) {
        this.alternateAddressList = alternateAddressList;
    }

    public boolean isIgnoreSecurity() {
        return ignoreSecurity;
    }

    public void setIgnoreSecurity(boolean ignoreSecurity) {
        this.ignoreSecurity = ignoreSecurity;
    }
}

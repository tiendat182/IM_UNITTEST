package com.viettel.ws.common.utils;


import com.viettel.fw.dto.UserDTO;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by LamNV5 on 5/26/2015.
 */

@XmlRootElement(namespace="http://service.ws.viettel.com/")
public class GenericWebInfo implements Serializable {
    private String staffCode;
    private String shopCode;
    private Long staffId;
    private Long shopId;
    private String ipAddress;
    private String language;
    private String country;
    private String reqId;
    private Long userId;
    private String sessionId;
    private String currentAddress;
    private String serverPort;
    private String serverAddress;
    private String appCode;
    private String portAddress;

    public GenericWebInfo() {
    }


    public GenericWebInfo(String staffCode, String shopCode, String ipAddress, String language, String country) {
        this.staffCode = staffCode;
        this.shopCode = shopCode;
        this.ipAddress = ipAddress;
        this.language = language;
        this.country = country;
    }

    public UserDTO getStaffDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setStaffCode(getStaffCode());
        userDTO.setStaffId(getStaffId());
        userDTO.setShopCode(getShopCode());
        userDTO.setShopId(getShopId());
        return userDTO;
    }


    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(String portAddress) {
        this.portAddress = portAddress;
    }
}

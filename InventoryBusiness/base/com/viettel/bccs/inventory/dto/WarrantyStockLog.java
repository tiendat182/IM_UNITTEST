/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ThanhNT77
 */
public class WarrantyStockLog implements Serializable {
    private Long id;
    private Date startDate;
    private String nodeWebservice;
    private String methodName;
    private String paramsInput;
    private String responseCode;
    private String description;
    private String resultCall;
    private Date endDate;
    private String ipClient;
    private String duration;

    public WarrantyStockLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getNodeWebservice() {
        return nodeWebservice;
    }

    public void setNodeWebservice(String nodeWebservice) {
        this.nodeWebservice = nodeWebservice;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamsInput() {
        return paramsInput;
    }

    public void setParamsInput(String paramsInput) {
        this.paramsInput = paramsInput;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResultCall() {
        return resultCall;
    }

    public void setResultCall(String resultCall) {
        this.resultCall = resultCall;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getIpClient() {
        return ipClient;
    }

    public void setIpClient(String ipClient) {
        this.ipClient = ipClient;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
    
}

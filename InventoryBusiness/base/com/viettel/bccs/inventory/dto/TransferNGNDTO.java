package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by ChungNV7 on 1/8/2016.
 */
public class TransferNGNDTO extends BaseDTO {

    private String rowKey;
    private Long isdn;
    private String result;
    private String resultDesc;
    private String curAreaCode;
    private String newAreaCode;
    private Long telecomService;
    private Long ownerId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Long getIsdn() {
        return isdn;
    }

    public void setIsdn(Long isdn) {
        this.isdn = isdn;
    }

    public String getCurAreaCode() {
        return curAreaCode;
    }

    public void setCurAreaCode(String curAreaCode) {
        this.curAreaCode = curAreaCode;
    }

    public String getNewAreaCode() {
        return newAreaCode;
    }

    public void setNewAreaCode(String newAreaCode) {
        this.newAreaCode = newAreaCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Long getTelecomService() {
        return telecomService;
    }

    public void setTelecomService(Long telecomService) {
        this.telecomService = telecomService;
    }

    @Override
    public String toString() {
        return rowKey + "";
    }

}

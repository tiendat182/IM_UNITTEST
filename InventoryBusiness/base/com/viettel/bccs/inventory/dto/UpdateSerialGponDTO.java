package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hoangnt14 on 6/28/2016.
 */
public class UpdateSerialGponDTO extends BaseDTO implements Serializable {
    private Long prodOfferingId;
    private String serial;
    private String stockGPon;
    private String status;
    private String cadId;
    private String macId;
    private Long success;

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public Long getProdOfferingId() {
        return prodOfferingId;
    }

    public void setProdOfferingId(Long prodOfferingId) {
        this.prodOfferingId = prodOfferingId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getStockGPon() {
        return stockGPon;
    }

    public void setStockGPon(String stockGPon) {
        this.stockGPon = stockGPon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCadId() {
        return cadId;
    }

    public void setCadId(String cadId) {
        this.cadId = cadId;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }
}

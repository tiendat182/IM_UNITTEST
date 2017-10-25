package com.viettel.bccs.sale.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class SaleTransSerialDTO extends BaseDTO implements Serializable {

    private Date dateDeliver;
    private String fromSerial;
    private Long quantity;
    private Date saleTransDate;
    private Long saleTransDetailId;
    private Long saleTransId;
    private Long saleTransSerialId;
    private Long stockModelId;
    private String toSerial;
    private Long userDeliver;
    private Long userUpdate;

    public static enum COLUMNS {DATEDELIVER, FROMSERIAL, QUANTITY, SALETRANSDATE, SALETRANSDETAILID, SALETRANSID, SALETRANSSERIALID, STOCKMODELID, TOSERIAL, USERDELIVER, USERUPDATE, EXCLUSE_ID_LIST}

    ;

    public String getKeySet() {
        return keySet;
    }

    public Date getDateDeliver() {
        return this.dateDeliver;
    }

    public void setDateDeliver(Date dateDeliver) {
        this.dateDeliver = dateDeliver;
    }

    public String getFromSerial() {
        return this.fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getSaleTransDate() {
        return this.saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Long getSaleTransDetailId() {
        return this.saleTransDetailId;
    }

    public void setSaleTransDetailId(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public Long getSaleTransId() {
        return this.saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Long getSaleTransSerialId() {
        return this.saleTransSerialId;
    }

    public void setSaleTransSerialId(Long saleTransSerialId) {
        this.saleTransSerialId = saleTransSerialId;
    }

    public Long getStockModelId() {
        return this.stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getToSerial() {
        return this.toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Long getUserDeliver() {
        return this.userDeliver;
    }

    public void setUserDeliver(Long userDeliver) {
        this.userDeliver = userDeliver;
    }

    public Long getUserUpdate() {
        return this.userUpdate;
    }

    public void setUserUpdate(Long userUpdate) {
        this.userUpdate = userUpdate;
    }
}

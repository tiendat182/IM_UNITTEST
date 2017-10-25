package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransDetailIm1DTO extends BaseDTO implements Serializable {
    private Long stockTransDetailId;
    private Long stockTransId;
    private Long stockModelId;
    private Long stateId;
    private Long quantityRes;
    private Long quantityReal;
    private Date createDatetime;
    private String note;
    private Long checkDial;
    private Long dialStatus;
    private Long modelProgramId;
    private String modelProgramName;
    private Long stockModelIdSwap;
    private Long quantityOverlimit;
    private Long priceOverlimit;
    private Long valid;
    private Long stockTypeId;
    private Long depositPrice;
    private Long checkSerial;
    private String stockModelName;
    private String strStateIdAfter;
    private Long prodOfferIdChange;
    private List<StockTransSerialIm1DTO> lstStockTransSerial;

    public String getKeySet() {
        return keySet;
    }

    public String getStockModelName() {
        return stockModelName;
    }

    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }

    public Long getProdOfferIdChange() {
        return prodOfferIdChange;
    }

    public void setProdOfferIdChange(Long prodOfferIdChange) {
        this.prodOfferIdChange = prodOfferIdChange;
    }

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public Long getStockTypeId() {
        return stockTypeId;
    }

    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public List<StockTransSerialIm1DTO> getLstStockTransSerial() {
        return lstStockTransSerial;
    }

    public void setLstStockTransSerial(List<StockTransSerialIm1DTO> lstStockTransSerial) {
        this.lstStockTransSerial = lstStockTransSerial;
    }

    public Long getStockTransDetailId() {
        return this.stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getStockModelId() {
        return this.stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getQuantityRes() {
        return this.quantityRes;
    }

    public void setQuantityRes(Long quantityRes) {
        this.quantityRes = quantityRes;
    }

    public Long getQuantityReal() {
        return this.quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getCheckDial() {
        return this.checkDial;
    }

    public void setCheckDial(Long checkDial) {
        this.checkDial = checkDial;
    }

    public Long getDialStatus() {
        return this.dialStatus;
    }

    public void setDialStatus(Long dialStatus) {
        this.dialStatus = dialStatus;
    }

    public Long getModelProgramId() {
        return this.modelProgramId;
    }

    public void setModelProgramId(Long modelProgramId) {
        this.modelProgramId = modelProgramId;
    }

    public String getModelProgramName() {
        return this.modelProgramName;
    }

    public void setModelProgramName(String modelProgramName) {
        this.modelProgramName = modelProgramName;
    }

    public Long getStockModelIdSwap() {
        return this.stockModelIdSwap;
    }

    public void setStockModelIdSwap(Long stockModelIdSwap) {
        this.stockModelIdSwap = stockModelIdSwap;
    }

    public Long getQuantityOverlimit() {
        return this.quantityOverlimit;
    }

    public void setQuantityOverlimit(Long quantityOverlimit) {
        this.quantityOverlimit = quantityOverlimit;
    }

    public Long getPriceOverlimit() {
        return this.priceOverlimit;
    }

    public void setPriceOverlimit(Long priceOverlimit) {
        this.priceOverlimit = priceOverlimit;
    }

    public Long getValid() {
        return this.valid;
    }

    public void setValid(Long valid) {
        this.valid = valid;
    }

    public String getStrStateIdAfter() {
        return strStateIdAfter;
    }

    public void setStrStateIdAfter(String strStateIdAfter) {
        this.strStateIdAfter = strStateIdAfter;
    }
}

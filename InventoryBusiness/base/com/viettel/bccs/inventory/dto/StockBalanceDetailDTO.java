package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockBalanceDetailDTO extends BaseDTO implements Serializable {

    private Date createDatetime;
    private Long prodOfferId;
    private Long quantity;
    private Long quantityBccs;
    private Long quantityErp;
    private Long quantityReal;
    private Long stockBalanceDetailId;
    private Long stockRequestId;
    private String checkSerial;
    private String ownerCode;
    private String ownerName;
    private String prodOfferCode;
    private String prodOfferName;
    private String divInspectFN;
    private String divBCCSFN;
    private String type;
    private String note;
    private List<StockBalanceSerialDTO> lstSerialDTO;
    private List<StockBalanceSerialDTO> lstStockBalanceSerialDTO;

    public List<StockBalanceSerialDTO> getLstStockBalanceSerialDTO() {
        return lstStockBalanceSerialDTO;
    }

    public void setLstStockBalanceSerialDTO(List<StockBalanceSerialDTO> lstStockBalanceSerialDTO) {
        this.lstStockBalanceSerialDTO = lstStockBalanceSerialDTO;
    }

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantityBccs() {
        return this.quantityBccs;
    }

    public void setQuantityBccs(Long quantityBccs) {
        this.quantityBccs = quantityBccs;
    }

    public Long getQuantityErp() {
        return this.quantityErp;
    }

    public void setQuantityErp(Long quantityErp) {
        this.quantityErp = quantityErp;
    }

    public Long getQuantityReal() {
        return this.quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getStockBalanceDetailId() {
        return this.stockBalanceDetailId;
    }

    public void setStockBalanceDetailId(Long stockBalanceDetailId) {
        this.stockBalanceDetailId = stockBalanceDetailId;
    }

    public Long getStockRequestId() {
        return this.stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public List<StockBalanceSerialDTO> getLstSerialDTO() {
        return lstSerialDTO;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public void setLstSerialDTO(List<StockBalanceSerialDTO> lstSerialDTO) {
        this.lstSerialDTO = lstSerialDTO;
    }

    public String getDivBCCSFN() {
        return divBCCSFN;
    }

    public void setDivBCCSFN(String divBCCSFN) {
        this.divBCCSFN = divBCCSFN;
    }

    public String getDivInspectFN() {
        return divInspectFN;
    }

    public void setDivInspectFN(String divInspectFN) {
        this.divInspectFN = divInspectFN;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

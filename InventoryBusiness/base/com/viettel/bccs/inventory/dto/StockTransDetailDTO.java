package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransDetailDTO extends BaseDTO implements Serializable {

    private Long amount;
    private Date createDatetime;
    private Long price;
    private Long prodOfferId;
    private Long quantity;
    private Long stateId;
    private Long stockTransDetailId;
    private Long stockTransId;
    private Long prodOfferTypeId;
    private String prodOfferTypeName;
    private String tableName;
    private String prodOfferName;
    private String unit;
    private String stateName;
    private Long total;
    private Long checkSerial;
    private Long avaiableQuantity;
    private List<StockTransSerialDTO> lstStockTransSerial;
    private List<StockDeviceTransferDTO> lstStockDeviceTransfer;
    private String strStateIdAfter;
    private Long productOfferPriceId;
    private Long basisPrice;
    //
    private int index;
    private Long ownerID;
    private Long ownerType;
    private Long depositPrice;
    private String prodOfferCode;
    private Long quantityBccs;
    private Long quantityInspect;
    private Long quantityFinance;
    private Long prodOfferIdSwap;
    private String actionCode;
    private String createDatetimeString;
    private Long prodOfferIdChange;

    String prodOfferCodeNew;
    Long transDetailID;
    String name;
    Long realQuantity;
    Long totalPrice;
    String receivingUnit;
    String note;
    String fromSerial;
    String toSerial;

    public Long getTransDetailID() {
        return transDetailID;
    }

    public void setTransDetailID(Long transDetailID) {
        this.transDetailID = transDetailID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(Long realQuantity) {
        this.realQuantity = realQuantity;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReceivingUnit() {
        return receivingUnit;
    }

    public void setReceivingUnit(String receivingUnit) {
        this.receivingUnit = receivingUnit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Long getProdOfferIdChange() {
        return prodOfferIdChange;
    }

    public void setProdOfferIdChange(Long prodOfferIdChange) {
        this.prodOfferIdChange = prodOfferIdChange;
    }

    public String getKeySet() {
        return keySet;
    }

    public StockTransDetailDTO() {
    }

    public StockTransDetailDTO(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public StockTransDetailDTO(Long prodOfferId, Long stateId) {
        this.prodOfferId = prodOfferId;
        this.stateId = stateId;
    }

    public Long getQuantityBccs() {
        return quantityBccs;
    }

    public void setQuantityBccs(Long quantityBccs) {
        this.quantityBccs = quantityBccs;
    }

    public Long getQuantityInspect() {
        return quantityInspect;
    }

    public void setQuantityInspect(Long quantityInspect) {
        this.quantityInspect = quantityInspect;
    }

    public Long getQuantityFinance() {
        return quantityFinance;
    }

    public void setQuantityFinance(Long quantityFinance) {
        this.quantityFinance = quantityFinance;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price) {
        this.price = price;
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

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public String getProdOfferTypeName() {
        return prodOfferTypeName;
    }

    public void setProdOfferTypeName(String prodOfferTypeName) {
        this.prodOfferTypeName = prodOfferTypeName;
    }

    public List<StockTransSerialDTO> getLstStockTransSerial() {
        return lstStockTransSerial;
    }

    public void setLstStockTransSerial(List<StockTransSerialDTO> lstStockTransSerial) {
        this.lstStockTransSerial = lstStockTransSerial;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }

    public Long getAvaiableQuantity() {
        return avaiableQuantity;
    }

    public void setAvaiableQuantity(Long avaiableQuantity) {
        this.avaiableQuantity = avaiableQuantity;
    }

    public String getStrStateIdAfter() {
        return this.strStateIdAfter;
    }

    public void setStrStateIdAfter(String strStateIdAfter) {
        this.strStateIdAfter = strStateIdAfter;
    }

    public List<StockTransSerialDTO> getLstSerial() {
        if (DataUtil.isNullOrEmpty(this.lstStockTransSerial)) {
            lstStockTransSerial = Lists.newArrayList();
        }
        return lstStockTransSerial;
    }

    public Long getProductOfferPriceId() {
        return productOfferPriceId;
    }

    public void setProductOfferPriceId(Long productOfferPriceId) {
        this.productOfferPriceId = productOfferPriceId;
    }

    public Long getBasisPrice() {
        return basisPrice;
    }

    public void setBasisPrice(Long basisPrice) {
        this.basisPrice = basisPrice;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getProdOfferIdSwap() {
        return prodOfferIdSwap;
    }

    public void setProdOfferIdSwap(Long prodOfferIdSwap) {
        this.prodOfferIdSwap = prodOfferIdSwap;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getCreateDatetimeString() {
        return createDatetimeString;
    }

    public void setCreateDatetimeString(String createDatetimeString) {
        this.createDatetimeString = createDatetimeString;
    }

    public String getProdOfferCodeNew() {
        return prodOfferCodeNew;
    }

    public void setProdOfferCodeNew(String prodOfferCodeNew) {
        this.prodOfferCodeNew = prodOfferCodeNew;
    }

    public List<StockDeviceTransferDTO> getLstStockDeviceTransfer() {
        return lstStockDeviceTransfer;
    }

    public void setLstStockDeviceTransfer(List<StockDeviceTransferDTO> lstStockDeviceTransfer) {
        this.lstStockDeviceTransfer = lstStockDeviceTransfer;
    }
}

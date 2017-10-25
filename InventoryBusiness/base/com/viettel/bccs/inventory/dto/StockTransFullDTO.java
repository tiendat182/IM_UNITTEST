package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;
import java.util.List;

/**
 * @author luannt23 on 12/16/2015.
 */
public class StockTransFullDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Long stockTransDetailId;
    private Long productOfferTypeId;
    private String productOfferTypeName;
    private String stockTypeName;
    private Long prodOfferId;
    private String prodOfferCode;
    private String prodOfferName;
    private String checkSerial;
    private String checkDeposit;
    private String accountingModelCode;
    private String unit;
    private String fromOwnerType;
    private Long fromOwnerId;
    private String fromOwnerName;
    private String toOwnerType;
    private Long toOwnerId;
    private String toOwnerName;
    private Date createDatetime;
    private Long stockTransType;
    private Long stockTransId;
    private Long reasonId;
    private String stockTransStatus;
    private String payStatus;
    private String depositStatus;
    private String actionCode;
    private String actionCodeNote;//ma phieu xuat , dung cho upload file
    private Long actionId;
    private String actionType;
    private String username;
    private Long actionStaffId;
    private String actionStaffCode;
    private Long stateId;
    private String strStateId;
    private Long quantity;
    private String strQuantity;
    private Long quantityRemain;
    private Long basisPrice;
    private String stateName;
    private Long regionStockTransId;
    private String isAutoGen;
    private String tableName;
    private String fromSerial;
    private String toSerial;
    private Long sourcePrice;
    private Long depositPrice;
    private List<Long> lstPrice;
    private Long totalPrice;
    private List<StockTransSerialDTO> lstSerial;
    private Long priceId;
    private List<ProductOfferPriceDTO> productOfferPriceDTOs;
    private ProductOfferPriceDTO productOfferPriceDTO;
    private Long price;
    private Long amount;
    private Integer index = 0;
    private String msgError;
    private StockTransSerialDTO stockTransSerialDTOPrint;
    private Long serialStatus;
    private String serialContent;
    private String fromOwnerCode;
    private String toOwnerCode;
    private boolean choose4Divide;
    private boolean choose4Export;
    private String syncERP;
    private String note;
    private Long telectomServiceId;

    public ProductOfferPriceDTO getProductOfferPriceDTO() {
        return productOfferPriceDTO;
    }

    public void setProductOfferPriceDTO(ProductOfferPriceDTO productOfferPriceDTO) {
        this.productOfferPriceDTO = productOfferPriceDTO;
    }

    public List<ProductOfferPriceDTO> getProductOfferPriceDTOs() {
        return productOfferPriceDTOs;
    }

    public void setProductOfferPriceDTOs(List<ProductOfferPriceDTO> productOfferPriceDTOs) {
        this.productOfferPriceDTOs = productOfferPriceDTOs;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    /**
     * ham tra ve list serial neu co data
     *
     * @return
     * @author ThanhNT
     */
    public boolean getEnoughIsdn() {
        Long currentQuantity = 0L;
        if (DataUtil.isNullOrEmpty(lstSerial))
            return false;
        for (StockTransSerialDTO serialDTO : lstSerial) {
            currentQuantity += serialDTO.getQuantity();
        }
        return DataUtil.safeEqual(quantity, currentQuantity);
    }

    public boolean getHaveListSerial() {
        return lstSerial != null && lstSerial.size() > 0;
    }

    public List<StockTransSerialDTO> getLstSerial() {
        if (DataUtil.isNullOrEmpty(this.lstSerial)) {
            lstSerial = Lists.newArrayList();
        }
        return lstSerial;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Long> getLstPrice() {
        return lstPrice;
    }

    public void setLstPrice(List<Long> lstPrice) {
        this.lstPrice = lstPrice;
    }

    public void setLstSerial(List<StockTransSerialDTO> lstSerial) {
        this.lstSerial = lstSerial;
    }

    public StockTransFullDTO() {
    }

    public Long getStockTransDetailId() {
        return stockTransDetailId;
    }

    public void setStockTransDetailId(Long stockTransDetailId) {
        this.stockTransDetailId = stockTransDetailId;
    }

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getStockTypeName() {
        return stockTypeName;
    }

    public void setStockTypeName(String stockTypeName) {
        this.stockTypeName = stockTypeName;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }

    public String getCheckDeposit() {
        return checkDeposit;
    }

    public void setCheckDeposit(String checkDeposit) {
        this.checkDeposit = checkDeposit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(String fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public String getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(String toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(Long stockTransType) {
        this.stockTransType = stockTransType;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getStockTransStatus() {
        return stockTransStatus;
    }

    public void setStockTransStatus(String stockTransStatus) {
        this.stockTransStatus = stockTransStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getBasisPrice() {
        return basisPrice;
    }

    public void setBasisPrice(Long basisPrice) {
        this.basisPrice = basisPrice;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getRegionStockTransId() {
        return regionStockTransId;
    }

    public void setRegionStockTransId(Long regionStockTransId) {
        this.regionStockTransId = regionStockTransId;
    }

    public String getIsAutoGen() {
        return isAutoGen;
    }

    public void setIsAutoGen(String isAutoGen) {
        this.isAutoGen = isAutoGen;
    }

    public String getProductOfferTypeName() {
        return productOfferTypeName;
    }

    public void setProductOfferTypeName(String productOfferTypeName) {
        this.productOfferTypeName = productOfferTypeName;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public Long getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(Long sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getQuantityRemain() {
        return quantityRemain;
    }

    public void setQuantityRemain(Long quantityRemain) {
        this.quantityRemain = quantityRemain;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public StockTransSerialDTO getStockTransSerialDTOPrint() {
        return stockTransSerialDTOPrint;
    }

    public void setStockTransSerialDTOPrint(StockTransSerialDTO stockTransSerialDTOPrint) {
        this.stockTransSerialDTOPrint = stockTransSerialDTOPrint;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public String getStrStateId() {
        return strStateId;
    }

    public void setStrStateId(String strStateId) {
        this.strStateId = strStateId;
    }

    public String getStrQuantity() {
        return strQuantity;
    }

    public void setStrQuantity(String strQuantity) {
        this.strQuantity = strQuantity;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getActionCodeNote() {
        return actionCodeNote;
    }

    public void setActionCodeNote(String actionCodeNote) {
        this.actionCodeNote = actionCodeNote;
    }

    public Long getSerialStatus() {
        return serialStatus;
    }

    public void setSerialStatus(Long serialStatus) {
        this.serialStatus = serialStatus;
    }

    public String getSerialContent() {
        return serialContent;
    }

    public void setSerialContent(String serialContent) {
        this.serialContent = serialContent;
    }

    public String getFromOwnerCode() {
        return fromOwnerCode;
    }

    public void setFromOwnerCode(String fromOwnerCode) {
        this.fromOwnerCode = fromOwnerCode;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public boolean isChoose4Divide() {
        return choose4Divide;
    }

    public void setChoose4Divide(boolean choose4Divide) {
        this.choose4Divide = choose4Divide;
    }

    public boolean isChoose4Export() {
        return choose4Export;
    }

    public void setChoose4Export(boolean choose4Export) {
        this.choose4Export = choose4Export;
    }

    public String getSyncERP() {
        return syncERP;
    }

    public void setSyncERP(String syncERP) {
        this.syncERP = syncERP;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getActionStaffCode() {
        return actionStaffCode;
    }

    public void setActionStaffCode(String actionStaffCode) {
        this.actionStaffCode = actionStaffCode;
    }

    public Long getTelectomServiceId() {
        return telectomServiceId;
    }

    public void setTelectomServiceId(Long telectomServiceId) {
        this.telectomServiceId = telectomServiceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof StockTransFullDTO) {
            StockTransFullDTO stockTransFullDTO = (StockTransFullDTO) obj;
            if (!DataUtil.isNullOrZero(stockTransFullDTO.getActionId())) {
                return DataUtil.safeEqual(stockTransFullDTO.getActionId(), this.actionId);
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}


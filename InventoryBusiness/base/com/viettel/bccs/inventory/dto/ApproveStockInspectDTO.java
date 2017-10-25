package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

/**
 * Created by hoangnt14 on 2/19/2016.
 */
public class ApproveStockInspectDTO extends BaseDTO implements Serializable {
    private Long stockInspectId;
    private Long prodOfferId;
    private Long productOfferTypeId;
    private String productCode;
    private String productName;
    private String ownerName;
    private String approveStatusString;
    private Long quantitySystem;
    private Long quantityReal;
    private Long quantityFinance;
    private Long diff;
    private Long ownerId;
    private String ownerType;
    private Long approveUserId;
    private Long userCreateId;
    private String approveStatus;
    private Long stateId;
    private String stateName;
    private String approveNote;
    private String issueDateTime;
    private String requestUser;
    private String approveUser;
    private Boolean selected;
    private Long quantity;
    private String fromSerial;
    private String toSearial;
    private String productStatus;

    public String getDiffColor() {
        return diff != null && !diff.equals(0L) ? "background-color:#b3d1ff" : "";
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSearial() {
        return toSearial;
    }

    public void setToSearial(String toSearial) {
        this.toSearial = toSearial;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getIssueDateTime() {
        return issueDateTime;
    }

    public void setIssueDateTime(String issueDateTime) {
        this.issueDateTime = issueDateTime;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getStockInspectId() {
        return stockInspectId;
    }

    public void setStockInspectId(Long stockInspectId) {
        this.stockInspectId = stockInspectId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getApproveStatusString() {
        return approveStatusString;
    }

    public void setApproveStatusString(String approveStatusString) {
        this.approveStatusString = approveStatusString;
    }

    public Long getQuantitySystem() {
        return quantitySystem;
    }

    public void setQuantitySystem(Long quantitySystem) {
        this.quantitySystem = quantitySystem;
    }

    public Long getQuantityReal() {
        return quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public Long getDiff() {
        return diff;
    }

    public void setDiff(Long diff) {
        this.diff = diff;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Long getUserCreateId() {
        return userCreateId;
    }

    public void setUserCreateId(Long userCreateId) {
        this.userCreateId = userCreateId;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public Long getQuantityFinance() {
        return quantityFinance;
    }

    public void setQuantityFinance(Long quantityFinance) {
        this.quantityFinance = quantityFinance;
    }
}

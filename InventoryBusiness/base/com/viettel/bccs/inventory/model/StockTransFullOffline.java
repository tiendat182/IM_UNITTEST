/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Vietvt6
 */
@Entity
@Table(name = "STOCK_TRANS_FULL_OFFLINE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockTransFullOffline.findAll", query = "SELECT s FROM StockTransFullOffline s")})
public class StockTransFullOffline implements Serializable {
public static enum COLUMNS {ACTIONCODE, ACTIONSTAFFID, ACTIONSTATUS, BASISPRICE, CHECKDEPOSIT, CHECKSERIAL, CREATEDATETIME, CREATEUSER, DEPOSITSTATUS, FROMOWNERID, FROMOWNERTYPE, NOTE, PAYSTATUS, PRODOFFERCODE, PRODOFFERID, PRODOFFERNAME, PRODUCTOFFERTYPEID, PRODUCTOFFERTYPENAME, QUANTITY, REASONID, STATEID, STATENAME, STATUS, STOCKTRANSACTIONID, STOCKTRANSDETAILID, STOCKTRANSID, STOCKTRANSTYPE, TOOWNERID, TOOWNERTYPE, UNIT, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_TRANS_SERIAL_GENERATOR", sequenceName = "STOCK_TRANS_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_SERIAL_GENERATOR")
    private Long stockTransDetailId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_OFFER_TYPE_ID")
    private Long productOfferTypeId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_OFFER_TYPE_NAME")
    private String productOfferTypeName;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_CODE")
    private String prodOfferCode;
    @Basic(optional = false)
    @Column(name = "PROD_OFFER_NAME")
    private String prodOfferName;
    @Column(name = "CHECK_SERIAL")
    private Long checkSerial;
    @Column(name = "CHECK_DEPOSIT")
    private Long checkDeposit;
    @Column(name = "UNIT")
    private String unit;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "STOCK_TRANS_TYPE")
    private String stockTransType;
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "PAY_STATUS")
    private String payStatus;
    @Column(name = "DEPOSIT_STATUS")
    private String depositStatus;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ACTION_ID")
    private Long stockTransActionId;
    @Column(name = "ACTION_STATUS")
    private String actionStatus;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "ACTION_STAFF_ID")
    private Long actionStaffId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "BASIS_PRICE")
    private Long basisPrice;
    @Column(name = "STATE_NAME")
    private String stateName;


    public StockTransFullOffline() {
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

    public String getProductOfferTypeName() {
        return productOfferTypeName;
    }

    public void setProductOfferTypeName(String productOfferTypeName) {
        this.productOfferTypeName = productOfferTypeName;
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

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }

    public Long getCheckDeposit() {
        return checkDeposit;
    }

    public void setCheckDeposit(Long checkDeposit) {
        this.checkDeposit = checkDeposit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
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

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

}

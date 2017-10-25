/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_KIT", catalog = "", schema = "BCCS_IM")
@NamedQueries({
        @NamedQuery(name = "StockKit.findAll", query = "SELECT s FROM StockKit s")})
public class StockKit implements Serializable {
    public static enum COLUMNS {ID, PRODOFFERID, IMSI, ISDN, ICCID, PUK, PIN, PUK2, PIN2, HLRID, SIMTYPE, AUCSTATUS, AUCREGDATE, AUCREMOVEDATE, HLRSTATUS, HLRREGDATE, HLRREMOVEDATE, OWNERID, OWNERTYPE, OLDOWNERID, OLDOWNERTYPE, STATUS, TELECOMSERVICEID, CHECKDIAL, DIALSTATUS, SERIAL, STATEID, CREATEDATE, CREATEUSER, USERSESSIONID, OWNERRECEIVERID, OWNERRECEIVERTYPE, RECEIVERNAME, DEPOSITPRICE, SALEDATE, CONNECTIONTYPE, CONNECTIONSTATUS, CONNECTIONDATE, STOCKTRANSID, CONTRACTCODE, POCODE, SERIALREAL, ORDERCODE, STARTDATEWARRANTY, UPDATEDATETIME, UPDATENUMBER, BANKPLUSSTATUS, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_KIT_ID_GENERATOR", sequenceName = "STOCK_KIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_KIT_ID_GENERATOR")
    private Long id;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "IMSI")
    private String imsi;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "ICCID")
    private String iccid;
    @Column(name = "PUK")
    private String puk;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "PUK2")
    private String puk2;
    @Column(name = "PIN2")
    private String pin2;
    @Column(name = "HLR_ID")
    private String hlrId;
    @Column(name = "SIM_TYPE")
    private String simType;
    @Column(name = "AUC_STATUS")
    private Character aucStatus;
    @Column(name = "AUC_REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aucRegDate;
    @Column(name = "AUC_REMOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aucRemoveDate;
    @Column(name = "HLR_STATUS")
    private Long hlrStatus;
    @Column(name = "HLR_REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hlrRegDate;
    @Column(name = "HLR_REMOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hlrRemoveDate;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "OLD_OWNER_ID")
    private Long oldOwnerId;
    @Column(name = "OLD_OWNER_TYPE")
    private Long oldOwnerType;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "CHECK_DIAL")
    private Long checkDial;
    @Column(name = "DIAL_STATUS")
    private Long dialStatus;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "USER_SESSION_ID")
    private String userSessionId;
    @Column(name = "OWNER_RECEIVER_ID")
    private Long ownerReceiverId;
    @Column(name = "OWNER_RECEIVER_TYPE")
    private Long ownerReceiverType;
    @Column(name = "RECEIVER_NAME")
    private String receiverName;
    @Column(name = "DEPOSIT_PRICE")
    private Long depositPrice;
    @Column(name = "SALE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Column(name = "CONNECTION_TYPE")
    private Long connectionType;
    @Column(name = "CONNECTION_STATUS")
    private Long connectionStatus;
    @Column(name = "CONNECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date connectionDate;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "SERIAL_REAL")
    private Long serialReal;
    @Column(name = "ORDER_CODE")
    private String orderCode;
    @Column(name = "START_DATE_WARRANTY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateWarranty;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "UPDATE_NUMBER")
    private Long updateNumber;
    @Column(name = "BANKPLUS_STATUS")
    private Long bankplusStatus;


    public StockKit() {
    }

    public StockKit(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPuk2() {
        return puk2;
    }

    public void setPuk2(String puk2) {
        this.puk2 = puk2;
    }

    public String getPin2() {
        return pin2;
    }

    public void setPin2(String pin2) {
        this.pin2 = pin2;
    }

    public String getHlrId() {
        return hlrId;
    }

    public void setHlrId(String hlrId) {
        this.hlrId = hlrId;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public Character getAucStatus() {
        return aucStatus;
    }

    public void setAucStatus(Character aucStatus) {
        this.aucStatus = aucStatus;
    }

    public Date getAucRegDate() {
        return aucRegDate;
    }

    public void setAucRegDate(Date aucRegDate) {
        this.aucRegDate = aucRegDate;
    }

    public Date getAucRemoveDate() {
        return aucRemoveDate;
    }

    public void setAucRemoveDate(Date aucRemoveDate) {
        this.aucRemoveDate = aucRemoveDate;
    }

    public Long getHlrStatus() {
        return hlrStatus;
    }

    public void setHlrStatus(Long hlrStatus) {
        this.hlrStatus = hlrStatus;
    }

    public Date getHlrRegDate() {
        return hlrRegDate;
    }

    public void setHlrRegDate(Date hlrRegDate) {
        this.hlrRegDate = hlrRegDate;
    }

    public Date getHlrRemoveDate() {
        return hlrRemoveDate;
    }

    public void setHlrRemoveDate(Date hlrRemoveDate) {
        this.hlrRemoveDate = hlrRemoveDate;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(Long oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public Long getOldOwnerType() {
        return oldOwnerType;
    }

    public void setOldOwnerType(Long oldOwnerType) {
        this.oldOwnerType = oldOwnerType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Long getCheckDial() {
        return checkDial;
    }

    public void setCheckDial(Long checkDial) {
        this.checkDial = checkDial;
    }

    public Long getDialStatus() {
        return dialStatus;
    }

    public void setDialStatus(Long dialStatus) {
        this.dialStatus = dialStatus;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    public Long getOwnerReceiverId() {
        return ownerReceiverId;
    }

    public void setOwnerReceiverId(Long ownerReceiverId) {
        this.ownerReceiverId = ownerReceiverId;
    }

    public Long getOwnerReceiverType() {
        return ownerReceiverType;
    }

    public void setOwnerReceiverType(Long ownerReceiverType) {
        this.ownerReceiverType = ownerReceiverType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Long depositPrice) {
        this.depositPrice = depositPrice;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Long getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(Long connectionType) {
        this.connectionType = connectionType;
    }

    public Long getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public Long getSerialReal() {
        return serialReal;
    }

    public void setSerialReal(Long serialReal) {
        this.serialReal = serialReal;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getStartDateWarranty() {
        return startDateWarranty;
    }

    public void setStartDateWarranty(Date startDateWarranty) {
        this.startDateWarranty = startDateWarranty;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Long getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(Long updateNumber) {
        this.updateNumber = updateNumber;
    }

    public Long getBankplusStatus() {
        return bankplusStatus;
    }

    public void setBankplusStatus(Long bankplusStatus) {
        this.bankplusStatus = bankplusStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockKit)) {
            return false;
        }
        StockKit other = (StockKit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "gencode.StockKit[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_BALANCE_REQUEST")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockBalanceRequest.findAll", query = "SELECT s FROM StockBalanceRequest s")})
public class StockBalanceRequest implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, CREATEUSER, LISTEMAILSIGN, OWNERID, OWNERTYPE, STATUS, STOCKREQUESTID, UPDATEDATETIME, UPDATEUSER, TYPE, STOCKTRANSID, ACCOUNTNAME, ACCOUNTPAS, REQUESTCODE, EXCLUSE_ID_LIST}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ID")
    @SequenceGenerator(name = "STOCK_REQUEST_ID_BALANCE_GENERATOR", sequenceName = "STOCK_BALANCE_REQUEST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_REQUEST_ID_BALANCE_GENERATOR")
    private Long stockRequestId;
    @Basic(optional = false)
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Basic(optional = false)
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "LIST_EMAIL_SIGN")
    private String listEmailSign;
    @Basic(optional = false)
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "STOCK_TRANS_ACTION_ID")
    private Long stockTransActionId;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ACCOUNT_PASS")
    private String accountPass;
    @Column(name = "REQUEST_CODE")
    private String requestCode;

    public StockBalanceRequest() {
    }

    public StockBalanceRequest(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public StockBalanceRequest(Long stockRequestId, Long ownerType, Long ownerId, Long status, String createUser) {
        this.stockRequestId = stockRequestId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.status = status;
        this.createUser = createUser;
    }

    public Long getStockRequestId() {
        return stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getListEmailSign() {
        return listEmailSign;
    }

    public void setListEmailSign(String listEmailSign) {
        this.listEmailSign = listEmailSign;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPass() {
        return accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }


    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockRequestId != null ? stockRequestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockBalanceRequest)) {
            return false;
        }
        StockBalanceRequest other = (StockBalanceRequest) object;
        if ((this.stockRequestId == null && other.stockRequestId != null) || (this.stockRequestId != null && !this.stockRequestId.equals(other.stockRequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockBalanceRequest[ stockRequestId=" + stockRequestId + " ]";
    }

}

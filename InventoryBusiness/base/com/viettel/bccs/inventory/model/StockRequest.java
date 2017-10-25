package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 8/9/2016.
 */

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_REQUEST")
@XmlRootElement
public class StockRequest implements Serializable {
public static enum COLUMNS {CREATEDATETIME, CREATEUSER, NOTE, OWNERID, OWNERTYPE, REQUESTCODE, REQUESTTYPE, STATUS, STOCKREQUESTID, STOCKTRANSID, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_REQUEST_ID")
    @SequenceGenerator(name = "STOCK_REQUEST_ID_GENERATOR", sequenceName = "STOCK_REQUEST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_REQUEST_ID_GENERATOR")
    private Long stockRequestId;
    @Column(name = "REQUEST_CODE")
    private String requestCode;
    @Basic(optional = false)
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Basic(optional = false)
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
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
    @Column(name = "REQUEST_TYPE")
    private Long requestType;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ACCOUNT_PASS")
    private String accountPass;
    @Column(name = "SIGN_USER_LIST")
    private String signUserList;
    @Lob
    @Column(name = "FILE_CONTENT")
    private byte[] fileContent;

    public StockRequest() {
    }

    public StockRequest(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public StockRequest(Long stockRequestId, Long ownerType, Long ownerId, String status, String createUser) {
        this.stockRequestId = stockRequestId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.status = status;
        this.createUser = createUser;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getSignUserList() {
        return signUserList;
    }

    public void setSignUserList(String signUserList) {
        this.signUserList = signUserList;
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

    public Long getStockRequestId() {
        return stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getRequestType() {
        return requestType;
    }

    public void setRequestType(Long requestType) {
        this.requestType = requestType;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(object instanceof StockRequest)) {
            return false;
        }
        StockRequest other = (StockRequest) object;
        if ((this.stockRequestId == null && other.stockRequestId != null) || (this.stockRequestId != null && !this.stockRequestId.equals(other.stockRequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.StockRequest[ stockRequestId=" + stockRequestId + " ]";
    }

}

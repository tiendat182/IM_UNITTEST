package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "CHANGE_MODEL_TRANS")
@XmlRootElement
public class ChangeModelTrans implements Serializable {
    public static enum COLUMNS {APPROVEDATE, APPROVEUSERID, CHANGEMODELTRANSID, CREATEDATE, CREATEUSERID, FROMOWNERID, FROMOWNERTYPE, LISTSTOCKTRANSID, STATUS, STOCKTRANSID, TOOWNERID, TOOWNERTYPE,REQUESTTYPE, EXCLUSE_ID_LIST, RETRY, ERRORCODE, ERRORCODEDESCRIPTION}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CHANGE_MODEL_TRANS_ID")
    @SequenceGenerator(name = "CHANGE_MODEL_TRANS_ID_GENERATOR", sequenceName = "CHANGE_MODEL_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_MODEL_TRANS_ID_GENERATOR")
    private Long changeModelTransId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "FROM_OWNER_ID")
    private Long fromOwnerId;
    @Column(name = "FROM_OWNER_TYPE")
    private Long fromOwnerType;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "APPROVE_USER_ID")
    private Long approveUserId;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "LIST_STOCK_TRANS_ID")
    private String listStockTransId;
    @Column(name = "REQUEST_TYPE")
    private String requestType;
    @Column(name = "RETRY")
    private Long retry;
    @Column(name = "ERROR_CODE")
    private String errorCode;
    @Column(name = "ERROR_CODE_DESCRIPTION")
    private String errorCodeDescription;

    public Long getRetry() {
        return retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public ChangeModelTrans() {
    }

    public ChangeModelTrans(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }

    public Long getChangeModelTransId() {
        return changeModelTransId;
    }

    public void setChangeModelTransId(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getListStockTransId() {
        return listStockTransId;
    }

    public void setListStockTransId(String listStockTransId) {
        this.listStockTransId = listStockTransId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (changeModelTransId != null ? changeModelTransId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChangeModelTrans)) {
            return false;
        }
        ChangeModelTrans other = (ChangeModelTrans) object;
        if ((this.changeModelTransId == null && other.changeModelTransId != null) || (this.changeModelTransId != null && !this.changeModelTransId.equals(other.changeModelTransId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.ChangeModelTrans[ changeModelTransId=" + changeModelTransId + " ]";
    }

}


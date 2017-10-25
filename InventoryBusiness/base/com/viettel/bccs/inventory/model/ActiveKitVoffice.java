/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Dungha7
 */
@Entity
@Table(name = "ACTIVE_KIT_VOFFICE")
@XmlRootElement
public class ActiveKitVoffice implements Serializable {
    public static enum COLUMNS {ACCOUNTNAME, ACCOUNTPASS, ACTIONCODE, ACTIVEKITVOFFICEID, CHANGEMODELTRANSID, CREATEDATE, CREATEUSERID, LASTMODIFY, NOTE, PREFIXTEMPLATE, SIGNUSERLIST, STATUS, STOCKTRANSACTIONID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ACTIVE_KIT_VOFFICE_ID")
    private Long activeKitVofficeId;
    @Column(name = "SIGN_USER_LIST")
    private String signUserList;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Basic(optional = false)
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "PREFIX_TEMPLATE")
    private String prefixTemplate;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ACCOUNT_PASS")
    private String accountPass;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "CHANGE_MODEL_TRANS_ID")
    private Long changeModelTransId;
    @Column(name = "STOCK_TRANS_ACTION_ID")
    private Long stockTransActionId;
    @Column(name = "ACTION_CODE")
    private String actionCode;

    public ActiveKitVoffice() {
    }

    public ActiveKitVoffice(Long activeKitVofficeId) {
        this.activeKitVofficeId = activeKitVofficeId;
    }

    public ActiveKitVoffice(Long activeKitVofficeId, Date createDate) {
        this.activeKitVofficeId = activeKitVofficeId;
        this.createDate = createDate;
    }

    public Long getActiveKitVofficeId() {
        return activeKitVofficeId;
    }

    public void setActiveKitVofficeId(Long activeKitVofficeId) {
        this.activeKitVofficeId = activeKitVofficeId;
    }

    public String getSignUserList() {
        return signUserList;
    }

    public void setSignUserList(String signUserList) {
        this.signUserList = signUserList;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getPrefixTemplate() {
        return prefixTemplate;
    }

    public void setPrefixTemplate(String prefixTemplate) {
        this.prefixTemplate = prefixTemplate;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getChangeModelTransId() {
        return changeModelTransId;
    }

    public void setChangeModelTransId(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activeKitVofficeId != null ? activeKitVofficeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActiveKitVoffice)) {
            return false;
        }
        ActiveKitVoffice other = (ActiveKitVoffice) object;
        if ((this.activeKitVofficeId == null && other.activeKitVofficeId != null) || (this.activeKitVofficeId != null && !this.activeKitVofficeId.equals(other.activeKitVofficeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.ActiveKitVoffice[ activeKitVofficeId=" + activeKitVofficeId + " ]";
    }

}

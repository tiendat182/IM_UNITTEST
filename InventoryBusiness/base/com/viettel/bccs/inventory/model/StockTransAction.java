/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_TRANS_ACTION")
@NamedQueries({
    @NamedQuery(name = "StockTransAction.findAll", query = "SELECT s FROM StockTransAction s")})
public class StockTransAction implements Serializable {
public static enum COLUMNS {ACTIONCODE, ACTIONSTAFFID, CREATEDATETIME, CREATEUSER, DOCUMENTSTATUS, LOGCMDCODE, LOGNOTECODE, NOTE, REGIONOWNERID, STATUS, STOCKTRANSACTIONID, STOCKTRANSID, EXCLUSE_ID_LIST, ACTIONCODESHOP};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_ACTION_ID")
    @SequenceGenerator(name = "STOCK_TRANS_ACTION_ID_GENERATOR", sequenceName = "STOCK_TRANS_ACTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_ACTION_ID_GENERATOR")
    private Long stockTransActionId;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "ACTION_STAFF_ID")
    private Long actionStaffId;
    @Column(name = "DOCUMENT_STATUS")
    private String documentStatus;
    @Column(name = "LOG_CMD_CODE")
    private String logCmdCode;
    @Column(name = "LOG_NOTE_CODE")
    private String logNoteCode;
    @Column(name = "REGION_OWNER_ID")
    private Long regionOwnerId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "ACTION_CODE_SHOP")
    private String actionCodeShop;
    @Column(name = "SIGN_CA_STATUS")
    private String signCaStatus;

    public StockTransAction() {
    }

    public StockTransAction(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getLogCmdCode() {
        return logCmdCode;
    }

    public void setLogCmdCode(String logCmdCode) {
        this.logCmdCode = logCmdCode;
    }

    public String getLogNoteCode() {
        return logNoteCode;
    }

    public void setLogNoteCode(String logNoteCode) {
        this.logNoteCode = logNoteCode;
    }

    public Long getRegionOwnerId() {
        return regionOwnerId;
    }

    public void setRegionOwnerId(Long regionOwnerId) {
        this.regionOwnerId = regionOwnerId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getActionCodeShop() {
        return actionCodeShop;
    }

    public void setActionCodeShop(String actionCodeShop) {
        this.actionCodeShop = actionCodeShop;
    }

    public String getSignCaStatus() {
        return signCaStatus;
    }

    public void setSignCaStatus(String signCaStatus) {
        this.signCaStatus = signCaStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransActionId != null ? stockTransActionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransAction)) {
            return false;
        }
        StockTransAction other = (StockTransAction) object;
        if ((this.stockTransActionId == null && other.stockTransActionId != null) || (this.stockTransActionId != null && !this.stockTransActionId.equals(other.stockTransActionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockTransAction[ stockTransActionId=" + stockTransActionId + " ]";
    }

}

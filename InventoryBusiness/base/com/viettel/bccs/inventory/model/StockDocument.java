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
 * @author tuydv1
 */
@Entity
@Table(name = "STOCK_DOCUMENT")
@XmlRootElement
public class StockDocument implements Serializable {
    public static enum COLUMNS {ACTIONCODE, ACTIONID, CREATEDATE, DELIVERYRECORDS, DELIVERYRECORDSNAME, DESTROYDATE, DESTROYUSER, EXPORTNOTE, EXPORTNOTENAME, FROMSHOPCODE, REASON, STATUS, STOCKDOCUMENTID, TOSHOPCODE, USERCREATE, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Column(name = "ACTION_ID")
    private Long actionId;
    @Column(name = "ACTION_CODE")
    private String actionCode;
    @Column(name = "USER_CREATE")
    private String userCreate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "FROM_SHOP_CODE")
    private String fromShopCode;
    @Column(name = "TO_SHOP_CODE")
    private String toShopCode;
    @Column(name = "EXPORT_NOTE_NAME")
    private String exportNoteName;
    @Lob
    @Column(name = "EXPORT_NOTE")
    private byte[] exportNote;
    @Column(name = "DELIVERY_RECORDS_NAME")
    private String deliveryRecordsName;
    @Lob
    @Column(name = "DELIVERY_RECORDS")
    private byte[] deliveryRecords;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_DOCUMENT_ID")
    @SequenceGenerator(name = "STOCK_DOCUMENT_ID_GENERATOR", sequenceName = "STOCK_DOCUMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_DOCUMENT_ID_GENERATOR")
    private Long stockDocumentId;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "DESTROY_USER")
    private String destroyUser;
    @Column(name = "DESTROY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date destroyDate;
    @Column(name = "REASON")
    private String reason;

    public StockDocument() {
    }

    public StockDocument(Long stockDocumentId) {
        this.stockDocumentId = stockDocumentId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFromShopCode() {
        return fromShopCode;
    }

    public void setFromShopCode(String fromShopCode) {
        this.fromShopCode = fromShopCode;
    }

    public String getToShopCode() {
        return toShopCode;
    }

    public void setToShopCode(String toShopCode) {
        this.toShopCode = toShopCode;
    }

    public String getExportNoteName() {
        return exportNoteName;
    }

    public void setExportNoteName(String exportNoteName) {
        this.exportNoteName = exportNoteName;
    }

    public byte[] getExportNote() {
        return exportNote;
    }

    public void setExportNote(byte[] exportNote) {
        this.exportNote = exportNote;
    }

    public String getDeliveryRecordsName() {
        return deliveryRecordsName;
    }

    public void setDeliveryRecordsName(String deliveryRecordsName) {
        this.deliveryRecordsName = deliveryRecordsName;
    }

    public byte[] getDeliveryRecords() {
        return deliveryRecords;
    }

    public void setDeliveryRecords(byte[] deliveryRecords) {
        this.deliveryRecords = deliveryRecords;
    }

    public Long getStockDocumentId() {
        return stockDocumentId;
    }

    public void setStockDocumentId(Long stockDocumentId) {
        this.stockDocumentId = stockDocumentId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getDestroyUser() {
        return destroyUser;
    }

    public void setDestroyUser(String destroyUser) {
        this.destroyUser = destroyUser;
    }

    public Date getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Date destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockDocumentId != null ? stockDocumentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDocument)) {
            return false;
        }
        StockDocument other = (StockDocument) object;
        if ((this.stockDocumentId == null && other.stockDocumentId != null) || (this.stockDocumentId != null && !this.stockDocumentId.equals(other.stockDocumentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockDocument[ stockDocumentId=" + stockDocumentId + " ]";
    }

}

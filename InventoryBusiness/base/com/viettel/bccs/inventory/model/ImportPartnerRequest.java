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
 * @author sinhhv
 */
@Entity
@Table(name = "IMPORT_PARTNER_REQUEST")
public class ImportPartnerRequest implements Serializable {
    public static enum COLUMNS {APPROVESTAFFID, CONTRACTCODE, CONTRACTDATE, CONTRACTIMPORTDATE, CREATEDATE, CREATESHOPID, CREATESTAFFID, CURRENCYTYPE, DELIVERYLOCATION, DOCUMENTCONTENT, DOCUMENTNAME, IMPORTDATE, IMPORTPARTNERREQUESTID, IMPORTSTAFFID, LASTMODIFY, PARTNERID, POCODE, PODATE, REASON, REQUESTCODE, REQUESTDATE, STATUS, TOOWNERID, TOOWNERTYPE, UNITPRICE, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IMPORT_PARTNER_REQUEST_ID")
    @SequenceGenerator(name = "IMPORT_PARTNER_RQ_ID_GEN", sequenceName = "IMPORT_PARTNER_REQUEST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMPORT_PARTNER_RQ_ID_GEN")
    private Long importPartnerRequestId;
    @Column(name = "CREATE_SHOP_ID")
    private Long createShopId;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "REQUEST_CODE")
    private String requestCode;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "TO_OWNER_ID")
    private Long toOwnerId;
    @Column(name = "TO_OWNER_TYPE")
    private Long toOwnerType;
    @Column(name = "IMPORT_STAFF_ID")
    private Long importStaffId;
    @Column(name = "IMPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "DOCUMENT_NAME")
    private String documentName;
    @Column(name = "DOCUMENT_CONTENT")
    private byte[] documentContent;
    @Column(name = "REASON")
    private String reason;
    @Column(name = "APPROVE_STAFF_ID")
    private Long approveStaffId;
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "CONTRACT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractDate;
    @Column(name = "PO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date poDate;
    @Column(name = "DELIVERY_LOCATION")
    private String deliveryLocation;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Column(name = "PARTNER_ID")
    private Long partnerId;
    @Column(name = "CURRENCY_TYPE")
    private String currencyType;
    @Column(name = "UNIT_PRICE")
    private Long unitPrice;
    @Column(name = "CONTRACT_IMPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractImportDate;

    public ImportPartnerRequest() {
    }

    public ImportPartnerRequest(Long importPartnerRequestId) {
        this.importPartnerRequestId = importPartnerRequestId;
    }

    public Long getImportPartnerRequestId() {
        return importPartnerRequestId;
    }

    public void setImportPartnerRequestId(Long importPartnerRequestId) {
        this.importPartnerRequestId = importPartnerRequestId;
    }

    public Long getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
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

    public Long getImportStaffId() {
        return importStaffId;
    }

    public void setImportStaffId(Long importStaffId) {
        this.importStaffId = importStaffId;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public byte[] getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(byte[] documentContent) {
        this.documentContent = documentContent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getApproveStaffId() {
        return approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
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

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getPoDate() {
        return poDate;
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getContractImportDate() {
        return contractImportDate;
    }

    public void setContractImportDate(Date contractImportDate) {
        this.contractImportDate = contractImportDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (importPartnerRequestId != null ? importPartnerRequestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportPartnerRequest)) {
            return false;
        }
        ImportPartnerRequest other = (ImportPartnerRequest) object;
        if ((this.importPartnerRequestId == null && other.importPartnerRequestId != null) || (this.importPartnerRequestId != null && !this.importPartnerRequestId.equals(other.importPartnerRequestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.ImportPartnerRequest[ importPartnerRequestId=" + importPartnerRequestId + " ]";
    }

}

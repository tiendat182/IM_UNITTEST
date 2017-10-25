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
@Table(name = "IMPORT_PARTNER_DETAIL")
public class ImportPartnerDetail implements Serializable {
    public static enum COLUMNS {CREATEDATE, IMPORTPARTNERDETAILID, IMPORTPARTNERREQUESTID, PRODOFFERID, QUANTITY, STATEID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "IMPORT_PARTNER_DETAIL_GENERATOR", sequenceName = "IMPORT_PARTNER_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMPORT_PARTNER_DETAIL_GENERATOR")

    @Column(name = "IMPORT_PARTNER_DETAIL_ID")
    private Long importPartnerDetailId;

    @Column(name = "IMPORT_PARTNER_REQUEST_ID")
    private Long importPartnerRequestId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public ImportPartnerDetail() {
    }

    public ImportPartnerDetail(Long importPartnerDetailId) {
        this.importPartnerDetailId = importPartnerDetailId;
    }

    public ImportPartnerDetail(Long importPartnerDetailId, long importPartnerRequestId) {
        this.importPartnerDetailId = importPartnerDetailId;
        this.importPartnerRequestId = importPartnerRequestId;
    }

    public Long getImportPartnerDetailId() {
        return importPartnerDetailId;
    }

    public void setImportPartnerDetailId(Long importPartnerDetailId) {
        this.importPartnerDetailId = importPartnerDetailId;
    }

    public long getImportPartnerRequestId() {
        return importPartnerRequestId;
    }

    public void setImportPartnerRequestId(long importPartnerRequestId) {
        this.importPartnerRequestId = importPartnerRequestId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (importPartnerDetailId != null ? importPartnerDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportPartnerDetail)) {
            return false;
        }
        ImportPartnerDetail other = (ImportPartnerDetail) object;
        if ((this.importPartnerDetailId == null && other.importPartnerDetailId != null) || (this.importPartnerDetailId != null && !this.importPartnerDetailId.equals(other.importPartnerDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.ImportPartnerDetail[ importPartnerDetailId=" + importPartnerDetailId + " ]";
    }

}

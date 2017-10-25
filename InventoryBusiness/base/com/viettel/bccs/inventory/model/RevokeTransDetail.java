/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "REVOKE_TRANS_DETAIL")
@NamedQueries({
    @NamedQuery(name = "RevokeTransDetail.findAll", query = "SELECT r FROM RevokeTransDetail r")})
public class RevokeTransDetail implements Serializable {
public static enum COLUMNS {CREATEDATE, DESCRIPTION, ERRCODE, OLDOWNERID, OLDOWNERTYPE, OLDSERIAL, PRODOFFERID, PRODOFFERTYPEID, QUANTITY, REVOKEPRICE, REVOKETRANSDETAILID, REVOKETRANSID, REVOKEDSERIAL, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REVOKE_TRANS_DETAIL_ID")
    private Long revokeTransDetailId;
    @Column(name = "REVOKE_TRANS_ID")
    private Long revokeTransId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "REVOKE_PRICE")
    private Long revokePrice;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "OLD_SERIAL")
    private String oldSerial;
    @Column(name = "REVOKED_SERIAL")
    private String revokedSerial;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ERR_CODE")
    private String errCode;
    @Column(name = "OLD_OWNER_TYPE")
    private Short oldOwnerType;
    @Column(name = "OLD_OWNER_ID")
    private Long oldOwnerId;
    @Column(name = "PROD_OFFER_TYPE_ID")
    private Long prodOfferTypeId;

    public RevokeTransDetail() {
    }

    public RevokeTransDetail(Long revokeTransDetailId) {
        this.revokeTransDetailId = revokeTransDetailId;
    }

    public Long getRevokeTransDetailId() {
        return revokeTransDetailId;
    }

    public void setRevokeTransDetailId(Long revokeTransDetailId) {
        this.revokeTransDetailId = revokeTransDetailId;
    }

    public Long getRevokeTransId() {
        return revokeTransId;
    }

    public void setRevokeTransId(Long revokeTransId) {
        this.revokeTransId = revokeTransId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getRevokePrice() {
        return revokePrice;
    }

    public void setRevokePrice(Long revokePrice) {
        this.revokePrice = revokePrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOldSerial() {
        return oldSerial;
    }

    public void setOldSerial(String oldSerial) {
        this.oldSerial = oldSerial;
    }

    public String getRevokedSerial() {
        return revokedSerial;
    }

    public void setRevokedSerial(String revokedSerial) {
        this.revokedSerial = revokedSerial;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Short getOldOwnerType() {
        return oldOwnerType;
    }

    public void setOldOwnerType(Short oldOwnerType) {
        this.oldOwnerType = oldOwnerType;
    }

    public Long getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(Long oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (revokeTransDetailId != null ? revokeTransDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RevokeTransDetail)) {
            return false;
        }
        RevokeTransDetail other = (RevokeTransDetail) object;
        if ((this.revokeTransDetailId == null && other.revokeTransDetailId != null) || (this.revokeTransDetailId != null && !this.revokeTransDetailId.equals(other.revokeTransDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RevokeTransDetail[ revokeTransDetailId=" + revokeTransDetailId + " ]";
    }
    
}

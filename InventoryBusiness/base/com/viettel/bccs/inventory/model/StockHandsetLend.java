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
 *
 * @author tuydv1
 */
@Entity
@Table(name = "STOCK_HANDSET_LEND")
public class StockHandsetLend implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATESTAFFID, ID, LASTMODIFY, OWNERID, OWNERTYPE, PRODOFFERID, SERIAL, STATUS, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_HANDSET_LEND_ID_GENERATOR", sequenceName = "STOCK_HANDSET_LEND_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_HANDSET_LEND_ID_GENERATOR")
    private Long id;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "STATUS")
    private Long status;

    public StockHandsetLend() {
    }

    public StockHandsetLend(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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
        if (!(object instanceof StockHandsetLend)) {
            return false;
        }
        StockHandsetLend other = (StockHandsetLend) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockHandsetLend[ id=" + id + " ]";
    }
    
}

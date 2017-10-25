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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "AP_LOCK_TRANS_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApLockTransDetail.findAll", query = "SELECT a FROM ApLockTransDetail a")})
public class ApLockTransDetail implements Serializable {
public static enum COLUMNS {CREATEDATE, ID, LOCKTYPE, POSID, PRODOFFERID, QUANTITY, REQUESTID, SERIAL, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "REQUEST_ID")
    private String requestId;
    @Column(name = "POS_ID")
    private Long posId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "LOCK_TYPE")
    private Short lockType;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public ApLockTransDetail() {
    }

    public ApLockTransDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Short getLockType() {
        return lockType;
    }

    public void setLockType(Short lockType) {
        this.lockType = lockType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApLockTransDetail)) {
            return false;
        }
        ApLockTransDetail other = (ApLockTransDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "algorithm.ApLockTransDetail[ id=" + id + " ]";
    }
    
}

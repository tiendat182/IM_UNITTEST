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
 * @author ThanhNT77
 */
@Entity
@Table(name = "REVOKE_KIT_TRANS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "RevokeKitTrans.findAll", query = "SELECT r FROM RevokeKitTrans r")})
public class RevokeKitTrans implements Serializable {
    public static enum COLUMNS {ADDMONEYDATE, ADDMONEYSTATUS, CREATEDATE, CREATESTAFFID, CREATESHOPID, ID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "REVOKE_KIT_TRANS_SEQ_ID_GENERATOR", sequenceName = "REVOKE_KIT_TRANS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVOKE_KIT_TRANS_SEQ_ID_GENERATOR")
    private Long id;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_STAFF_ID")
    private Long createStaffId;
    @Column(name = "ADD_MONEY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addMoneyDate;
    @Column(name = "ADD_MONEY_STATUS")
    private Long addMoneyStatus;
    @Column(name = "CREATE_SHOP_ID")
    private Long createShopId;

    public RevokeKitTrans() {
    }

    public RevokeKitTrans(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateStaffId() {
        return createStaffId;
    }

    public void setCreateStaffId(Long createStaffId) {
        this.createStaffId = createStaffId;
    }

    public Date getAddMoneyDate() {
        return addMoneyDate;
    }

    public void setAddMoneyDate(Date addMoneyDate) {
        this.addMoneyDate = addMoneyDate;
    }

    public Long getAddMoneyStatus() {
        return addMoneyStatus;
    }

    public void setAddMoneyStatus(Long addMoneyStatus) {
        this.addMoneyStatus = addMoneyStatus;
    }

    public Long getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
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
        if (!(object instanceof RevokeKitTrans)) {
            return false;
        }
        RevokeKitTrans other = (RevokeKitTrans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RevokeKitTrans[ id=" + id + " ]";
    }

}

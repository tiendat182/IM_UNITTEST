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
@Table(name = "APN")
public class Apn implements Serializable {
    public static enum COLUMNS {APNCODE, APNID, APNNAME, CENTERCODE, CREATETIME, CREATEUSER, DESCRIPTION, LASTUPDATETIME, LASTUPDATEUSER, STATUS, EXCLUSE_ID_LIST}
    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "APN_ID")
    @SequenceGenerator(name = "APN_ID_GENERATOR", sequenceName = "APN_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APN_ID_GENERATOR")
    private Long apnId;
    @Column(name = "APN_CODE")
    private String apnCode;
    @Column(name = "APN_NAME")
    private String apnName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "CENTER_CODE")
    private String centerCode;

    public Apn() {
    }

    public Apn(Long apnId) {
        this.apnId = apnId;
    }

    public Long getApnId() {
        return apnId;
    }

    public void setApnId(Long apnId) {
        this.apnId = apnId;
    }

    public String getApnCode() {
        return apnCode;
    }

    public void setApnCode(String apnCode) {
        this.apnCode = apnCode;
    }

    public String getApnName() {
        return apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apnId != null ? apnId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Apn)) {
            return false;
        }
        Apn other = (Apn) object;
        if ((this.apnId == null && other.apnId != null) || (this.apnId != null && !this.apnId.equals(other.apnId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.Apn[ apnId=" + apnId + " ]";
    }

}

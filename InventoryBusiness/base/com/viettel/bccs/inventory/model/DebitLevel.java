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
 * @author thetm1
 */
@Entity
@Table(name = "DEBIT_LEVEL")
@NamedQueries({
    @NamedQuery(name = "DebitLevel.findAll", query = "SELECT d FROM DebitLevel d")})
public class DebitLevel implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, DEBITAMOUNT, DEBITDAYTYPE, DEBITLEVEL, ID, LASTUPDATETIME, LASTUPDATEUSER, STATUS, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "DEBIT_LEVEL_ID_GENERATOR", sequenceName = "DEBIT_LEVEL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_LEVEL_ID_GENERATOR")
    private Long id;
    @Column(name = "DEBIT_DAY_TYPE")
    private String debitDayType;
    @Column(name = "DEBIT_LEVEL")
    private String debitLevel;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DEBIT_AMOUNT")
    private Long debitAmount;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

    public DebitLevel() {
    }

    public DebitLevel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDebitDayType() {
        return debitDayType;
    }

    public void setDebitDayType(String debitDayType) {
        this.debitDayType = debitDayType;
    }

    public String getDebitLevel() {
        return debitLevel;
    }

    public void setDebitLevel(String debitLevel) {
        this.debitLevel = debitLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Long debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
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
        if (!(object instanceof DebitLevel)) {
            return false;
        }
        DebitLevel other = (DebitLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.DebitLevel[ id=" + id + " ]";
    }
    
}

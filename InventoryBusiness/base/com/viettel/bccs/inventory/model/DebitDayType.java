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
 * @author thetm1
 */
@Entity
@Table(name = "DEBIT_DAY_TYPE")
@NamedQueries({
    @NamedQuery(name = "DebitDayType.findAll", query = "SELECT d FROM DebitDayType d")})
public class DebitDayType implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, DEBITDAYTYPE, ENDDATE, FILECONTENT, FILENAME, ID, LASTUPDATETIME, LASTUPDATEUSER, STARTDATE, STATUS, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "DEBIT_DAY_TYPE_ID_GENERATOR", sequenceName = "DEBIT_DAY_TYPE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_DAY_TYPE_ID_GENERATOR")
    private Long id;
    @Column(name = "DEBIT_DAY_TYPE")
    private String debitDayType;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
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
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_CONTENT")
    private byte[] fileContent;
//    @Lob
//    @Column(name = "FILE_CONTENT")
//    private Serializable fileContent;
    @Column(name = "STATUS")
    private String status;

    public DebitDayType() {
    }

    public DebitDayType(Long id) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    //    public Serializable getFileContent() {
//        return fileContent;
//    }
//
//    public void setFileContent(Serializable fileContent) {
//        this.fileContent = fileContent;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        if (!(object instanceof DebitDayType)) {
            return false;
        }
        DebitDayType other = (DebitDayType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.DebitDayType[ id=" + id + " ]";
    }
    
}

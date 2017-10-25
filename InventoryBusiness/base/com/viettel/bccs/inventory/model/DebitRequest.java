/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author thetm1
 */
@Entity
@Table(name = "DEBIT_REQUEST")
public class DebitRequest implements Serializable {
    public static enum COLUMNS {CREATEDATE, CREATEUSER, DEBITREQUESTDETAILLIST, DESCRIPTION, FILECONTENT, FILENAME, LASTUPDATETIME, LASTUPDATEUSER, REQUESTCODE, REQUESTID, REQUESTOBJECTTYPE, STATUS, EXCLUSE_ID_LIST}

    ;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "DEBIT_REQUEST_ID_GENERATOR", sequenceName = "DEBIT_REQUEST_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_REQUEST_ID_GENERATOR")
    @Column(name = "REQUEST_ID")
    private Long requestId;
    @Column(name = "REQUEST_CODE")
    private String requestCode;
    @Column(name = "REQUEST_OBJECT_TYPE")
    private String requestObjectType;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "FILE_CONTENT")
    private byte[] fileContent;
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
    @Column(name = "DESCRIPTION")
    private String description;

    public DebitRequest() {
    }

    public DebitRequest(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestObjectType() {
        return requestObjectType;
    }

    public void setRequestObjectType(String requestObjectType) {
        this.requestObjectType = requestObjectType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DebitRequest)) {
            return false;
        }
        DebitRequest other = (DebitRequest) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.DebitRequest[ requestId=" + requestId + " ]";
    }

}

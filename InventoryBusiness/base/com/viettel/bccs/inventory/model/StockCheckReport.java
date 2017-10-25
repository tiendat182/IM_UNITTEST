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
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_CHECK_REPORT")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "StockCheckReport.findAll", query = "SELECT s FROM StockCheckReport s")})
public class StockCheckReport implements Serializable {
public static enum COLUMNS {CHECKDATE, CREATEDATE, CREATEUSER, FILECONTENT, FILENAME, OWNERCODE, OWNERID, OWNERNAME, OWNERTYPE, STOCKCHECKREPORTID, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "STOCK_CHECK_REPORT_ID_GENERATOR", sequenceName = "STOCK_CHECK_REPORT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_CHECK_REPORT_ID_GENERATOR")
    @Column(name = "STOCK_CHECK_REPORT_ID")
    private Long stockCheckReportId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Short ownerType;
    @Column(name = "OWNER_CODE")
    private String ownerCode;
    @Column(name = "OWNER_NAME")
    private String ownerName;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Lob
    @Column(name = "FILE_CONTENT")
    private byte[] fileContent;
    @Column(name = "CHECK_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkDate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_USER")
    private String createUser;

    public StockCheckReport() {
    }

    public StockCheckReport(Long stockCheckReportId) {
        this.stockCheckReportId = stockCheckReportId;
    }

    public Long getStockCheckReportId() {
        return stockCheckReportId;
    }

    public void setStockCheckReportId(Long stockCheckReportId) {
        this.stockCheckReportId = stockCheckReportId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Short getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Short ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockCheckReportId != null ? stockCheckReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockCheckReport)) {
            return false;
        }
        StockCheckReport other = (StockCheckReport) object;
        if ((this.stockCheckReportId == null && other.stockCheckReportId != null) || (this.stockCheckReportId != null && !this.stockCheckReportId.equals(other.stockCheckReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockCheckReport[ stockCheckReportId=" + stockCheckReportId + " ]";
    }

}

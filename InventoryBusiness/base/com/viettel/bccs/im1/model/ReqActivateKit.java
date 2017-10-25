/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "REQ_ACTIVATE_KIT", schema = "BCCS_IM")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "ReqActivateKit.findAll", query = "SELECT r FROM ReqActivateKit r")})
public class ReqActivateKit implements Serializable {
public static enum COLUMNS {CREATEDATE, ERRORDESCRIPTION, FROMSERIAL, PROCESSCOUNT, PROCESSDATE, PROCESSSTATUS, REQID, SALETRANSDATE, SALETRANSID, SALETRANSTYPE, SALETYPE, SHOPID, STAFFID, TOSERIAL, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REQ_ID")
    @SequenceGenerator(name = "REQ_ID_GENERATOR", sequenceName = "BCCS_IM.REQ_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQ_ID_GENERATOR")
    private Long reqId;
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "SALE_TRANS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleTransDate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "SALE_TYPE")
    private Long saleType;
    @Column(name = "PROCESS_STATUS")
    private Long processStatus;
    @Column(name = "PROCESS_COUNT")
    private Long processCount;
    @Column(name = "PROCESS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processDate;
    @Column(name = "SALE_TRANS_TYPE")
    private Long saleTransType;
    @Column(name = "ERROR_DESCRIPTION")
    private String errorDescription;

    public ReqActivateKit() {
    }

    public ReqActivateKit(Long reqId) {
        this.reqId = reqId;
    }

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getSaleType() {
        return saleType;
    }

    public void setSaleType(Long saleType) {
        this.saleType = saleType;
    }

    public Long getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Long processStatus) {
        this.processStatus = processStatus;
    }

    public Long getProcessCount() {
        return processCount;
    }

    public void setProcessCount(Long processCount) {
        this.processCount = processCount;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Long getSaleTransType() {
        return saleTransType;
    }

    public void setSaleTransType(Long saleTransType) {
        this.saleTransType = saleTransType;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reqId != null ? reqId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReqActivateKit)) {
            return false;
        }
        ReqActivateKit other = (ReqActivateKit) object;
        if ((this.reqId == null && other.reqId != null) || (this.reqId != null && !this.reqId.equals(other.reqId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.im1.ReqActivateKit[ reqId=" + reqId + " ]";
    }

}

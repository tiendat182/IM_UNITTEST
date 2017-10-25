/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.im1.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "VC_REQUEST", schema = "BCCS_IM")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "VcRequest.findAll", query = "SELECT v FROM VcRequest v")})
public class VcRequest implements Serializable {
    public static enum COLUMNS {CREATETIME, FROMSERIAL, LASTPROCESSTIME, RANGEID, REQUESTID, REQUESTTYPE, SHOPID, STAFFID, STARTPROCESSTIME, STATUS, SYSCREATETIME, SYSPROCESSTIME, TOSERIAL, TRANSID, USERID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_ID")
    @SequenceGenerator(name = "REQUEST_ID_GENERATOR", sequenceName = "BCCS_IM.VC_REQ_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_ID_GENERATOR")
    private Long requestId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "LAST_PROCESS_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastProcessTime;
    @Column(name = "REQUEST_TYPE")
    private Long requestType;
    @Basic(optional = false)
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Basic(optional = false)
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "TRANS_ID")
    private Long transId;
    @Column(name = "START_PROCESS_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startProcessTime;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "STAFF_ID")
    private Long staffId;
    @Column(name = "RANGE_ID")
    private Long rangeId;
    @Column(name = "SYS_CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysCreateTime;
    @Column(name = "SYS_PROCESS_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sysProcessTime;

    public VcRequest() {
    }

    public VcRequest(Long requestId) {
        this.requestId = requestId;
    }

    public VcRequest(Long requestId, String fromSerial, String toSerial) {
        this.requestId = requestId;
        this.fromSerial = fromSerial;
        this.toSerial = toSerial;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastProcessTime() {
        return lastProcessTime;
    }

    public void setLastProcessTime(Date lastProcessTime) {
        this.lastProcessTime = lastProcessTime;
    }

    public Long getRequestType() {
        return requestType;
    }

    public void setRequestType(Long requestType) {
        this.requestType = requestType;
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

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public Date getStartProcessTime() {
        return startProcessTime;
    }

    public void setStartProcessTime(Date startProcessTime) {
        this.startProcessTime = startProcessTime;
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

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public Date getSysCreateTime() {
        return sysCreateTime;
    }

    public void setSysCreateTime(Date sysCreateTime) {
        this.sysCreateTime = sysCreateTime;
    }

    public Date getSysProcessTime() {
        return sysProcessTime;
    }

    public void setSysProcessTime(Date sysProcessTime) {
        this.sysProcessTime = sysProcessTime;
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
        if (!(object instanceof VcRequest)) {
            return false;
        }
        VcRequest other = (VcRequest) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.im1.VcRequest[ requestId=" + requestId + " ]";
    }

}

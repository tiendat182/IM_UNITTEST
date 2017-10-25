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
 * @author sinhhv
 */
@Entity
@Table(name = "APN_IP")
public class ApnIp implements Serializable {
public static enum COLUMNS {APNID, APNIPID, CENTERCODE, CREATETIME, CREATEUSER, IP, LASTUPDATETIME, LASTUPDATEUSER, STATUS, SUBNET, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "APN_IP_ID")
    @SequenceGenerator(name = "APN_IP_ID_GENERATOR", sequenceName = "APN_IP_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APN_IP_ID_GENERATOR")
    private Long apnIpId;
    @Column(name = "APN_ID")
    private Long apnId;
    @Column(name = "IP")
    private String ip;
    @Column(name = "CENTER_CODE")
    private String centerCode;
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
    @Column(name = "SUB_NET")
    private String subNet;

    @Transient
    private transient String apnStr;

    public ApnIp() {
    }

    public ApnIp(Long apnIpId) {
        this.apnIpId = apnIpId;
    }

    public Long getApnIpId() {
        return apnIpId;
    }

    public void setApnIpId(Long apnIpId) {
        this.apnIpId = apnIpId;
    }

    public Long getApnId() {
        return apnId;
    }

    public void setApnId(Long apnId) {
        this.apnId = apnId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
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

    public String getSubNet() {
        return subNet;
    }

    public void setSubNet(String subNet) {
        this.subNet = subNet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apnIpId != null ? apnIpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApnIp)) {
            return false;
        }
        ApnIp other = (ApnIp) object;
        if ((this.apnIpId == null && other.apnIpId != null) || (this.apnIpId != null && !this.apnIpId.equals(other.apnIpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.ApnIp[ apnIpId=" + apnIpId + " ]";
    }

    public String getApnStr() {
        return apnStr;
    }

    public void setApnStr(String apnStr) {
        this.apnStr = apnStr;
    }
}

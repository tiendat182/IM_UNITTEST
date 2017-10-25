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
 * @author hoangnt14
 */
@Entity
@Table(name = "BRAS_IPPOOL")
@NamedQueries({
        @NamedQuery(name = "BrasIppool.findAll", query = "SELECT b FROM BrasIppool b")})
public class BrasIppool implements Serializable {
    public static enum COLUMNS {BRASIP, CENTER, DOMAIN, IPLABEL, IPMASK, IPPOOL, IPTYPE, POOLID, POOLNAME, POOLUNIQ, PROVINCE, STATUS, CREATEDATETIME, CREATEUSER, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "POOL_ID")
    @SequenceGenerator(name = "POOL_ID_GENERATOR", sequenceName = "BRAS_IPPOOL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POOL_ID_GENERATOR")
    private Long poolId;
    @Column(name = "BRAS_ID")
    private Long brasId;
    @Column(name = "POOL_NAME")
    private String poolName;
    @Column(name = "POOL_UNIQ")
    private String poolUniq;
    @Column(name = "DOMAIN_ID")
    private Long domainId;
    @Column(name = "IP_POOL")
    private String ipPool;
    @Column(name = "IP_MASK")
    private String ipMask;
    @Column(name = "CENTER")
    private String center;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "IP_TYPE")
    private String ipType;
    @Column(name = "IP_LABEL")
    private String ipLabel;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Basic(optional = false)
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public BrasIppool() {
    }

    public BrasIppool(Long poolId) {
        this.poolId = poolId;
    }

    public Long getPoolId() {
        return poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }


    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolUniq() {
        return poolUniq;
    }

    public void setPoolUniq(String poolUniq) {
        this.poolUniq = poolUniq;
    }

    public Long getBrasId() {
        return brasId;
    }

    public void setBrasId(Long brasId) {
        this.brasId = brasId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getIpPool() {
        return ipPool;
    }

    public void setIpPool(String ipPool) {
        this.ipPool = ipPool;
    }

    public String getIpMask() {
        return ipMask;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIpType() {
        return ipType;
    }

    public void setIpType(String ipType) {
        this.ipType = ipType;
    }

    public String getIpLabel() {
        return ipLabel;
    }

    public void setIpLabel(String ipLabel) {
        this.ipLabel = ipLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poolId != null ? poolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BrasIppool)) {
            return false;
        }
        BrasIppool other = (BrasIppool) object;
        if ((this.poolId == null && other.poolId != null) || (this.poolId != null && !this.poolId.equals(other.poolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.BrasIppool[ poolId=" + poolId + " ]";
    }

}

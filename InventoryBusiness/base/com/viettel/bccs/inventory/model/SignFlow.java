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
@Table(name = "SIGN_FLOW")
@NamedQueries({
    @NamedQuery(name = "SignFlow.findAll", query = "SELECT s FROM SignFlow s")})
public class SignFlow implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, LASTUPDATETIME, LASTUPDATEUSER, NAME, SHOPCODE, SHOPID, SIGNFLOWID, STATUS, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SIGN_FLOW_ID")
    @SequenceGenerator(name = "SIGN_FLOW_ID_GENERATOR", sequenceName = "SIGN_FLOW_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIGN_FLOW_ID_GENERATOR")
    private Long signFlowId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "STATUS")
    private String status;
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

    public SignFlow() {
    }

    public SignFlow(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        hash += (signFlowId != null ? signFlowId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SignFlow)) {
            return false;
        }
        SignFlow other = (SignFlow) object;
        if ((this.signFlowId == null && other.signFlowId != null) || (this.signFlowId != null && !this.signFlowId.equals(other.signFlowId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.SignFlow[ signFlowId=" + signFlowId + " ]";
    }
    
}

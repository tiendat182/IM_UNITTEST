/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author thetm1
 */
@Entity
@Table(name = "SIGN_FLOW_DETAIL")
@NamedQueries({
    @NamedQuery(name = "SignFlowDetail.findAll", query = "SELECT s FROM SignFlowDetail s")})
public class SignFlowDetail implements Serializable {
public static enum COLUMNS {EMAIL, SIGNFLOWDETAILID, SIGNFLOWID, SIGNORDER, STATUS, VOFFICEROLEID, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SIGN_FLOW_DETAIL_ID")
    @SequenceGenerator(name = "SIGN_FLOW_DETAIL_ID_GENERATOR", sequenceName = "SIGN_FLOW_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIGN_FLOW_DETAIL_ID_GENERATOR")
    private Long signFlowDetailId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @Column(name = "SIGN_ORDER")
    private Long signOrder;
    @Column(name = "VOFFICE_ROLE_ID")
    private Long vofficeRoleId;
    @Column(name = "SIGN_FLOW_ID")
    private Long signFlowId;

    public SignFlowDetail() {
    }

    public SignFlowDetail(Long signFlowDetailId) {
        this.signFlowDetailId = signFlowDetailId;
    }

    public SignFlowDetail(Long signFlowDetailId, Long signOrder) {
        this.signFlowDetailId = signFlowDetailId;
        this.signOrder = signOrder;
    }

    public Long getSignFlowDetailId() {
        return signFlowDetailId;
    }

    public void setSignFlowDetailId(Long signFlowDetailId) {
        this.signFlowDetailId = signFlowDetailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSignOrder() {
        return signOrder;
    }

    public void setSignOrder(Long signOrder) {
        this.signOrder = signOrder;
    }

    public Long getVofficeRoleId() {
        return vofficeRoleId;
    }

    public void setVofficeRoleId(Long vofficeRoleId) {
        this.vofficeRoleId = vofficeRoleId;
    }

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (signFlowDetailId != null ? signFlowDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SignFlowDetail)) {
            return false;
        }
        SignFlowDetail other = (SignFlowDetail) object;
        if ((this.signFlowDetailId == null && other.signFlowDetailId != null) || (this.signFlowDetailId != null && !this.signFlowDetailId.equals(other.signFlowDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.SignFlowDetail[ signFlowDetailId=" + signFlowDetailId + " ]";
    }
    
}

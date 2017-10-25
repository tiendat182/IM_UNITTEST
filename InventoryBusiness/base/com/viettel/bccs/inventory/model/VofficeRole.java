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
@Table(name = "VOFFICE_ROLE")
@NamedQueries({ @NamedQuery(name = "VofficeRole.findAll", query = "SELECT v FROM VofficeRole v")})
public class VofficeRole implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, LASTUPDATETIME, LASTUPDATEUSER, ROLECODE, ROLENAME, STATUS, VOFFICEROLEID, EXCLUSE_ID_LIST};

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "VOFFICE_ROLE_ID")
    @SequenceGenerator(name = "VOFFICE_ROLE_ID_GENERATOR", sequenceName = "VOFFICE_ROLE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOFFICE_ROLE_ID_GENERATOR")
    private Long vofficeRoleId;
    @Column(name = "ROLE_CODE")
    private String roleCode;
    @Column(name = "ROLE_NAME")
    private String roleName;
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

    public VofficeRole() {
    }

    public VofficeRole(Long vofficeRoleId) {
        this.vofficeRoleId = vofficeRoleId;
    }

    public Long getVofficeRoleId() {
        return vofficeRoleId;
    }

    public void setVofficeRoleId(Long vofficeRoleId) {
        this.vofficeRoleId = vofficeRoleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
        hash += (vofficeRoleId != null ? vofficeRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VofficeRole)) {
            return false;
        }
        VofficeRole other = (VofficeRole) object;
        if ((this.vofficeRoleId == null && other.vofficeRoleId != null) || (this.vofficeRoleId != null && !this.vofficeRoleId.equals(other.vofficeRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.VofficeRole[ vofficeRoleId=" + vofficeRoleId + " ]";
    }
    
}

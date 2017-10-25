/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "LOCK_USER_TYPE")
@NamedQueries({
    @NamedQuery(name = "LockUserType.findAll", query = "SELECT l FROM LockUserType l")})
public class LockUserType implements Serializable {
public static enum COLUMNS {ACTIONURL, AUTO, CHECKRANGE, DESCRIPTION, EFFECTROLE, ID, NAME, PARAMS, REDIRECTURL, SQLCHECKTRANS, SQLCMD, STATUS, VALIDRANGE, WARNINGCONTENT, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "WARNING_CONTENT")
    private String warningContent;
    @Column(name = "ACTION_URL")
    private String actionUrl;
    @Column(name = "STATUS")
    private Character status;
    @Column(name = "PARAMS")
    private String params;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CHECK_RANGE")
    private Long checkRange;
    @Column(name = "VALID_RANGE")
    private Long validRange;
    @Column(name = "AUTO")
    private Character auto;
    @Column(name = "SQL_CMD")
    private String sqlCmd;
    @Column(name = "SQL_CHECK_TRANS")
    private String sqlCheckTrans;
    @Column(name = "EFFECT_ROLE")
    private String effectRole;
    @Column(name = "REDIRECT_URL")
    private String redirectUrl;

    public LockUserType() {
    }

    public LockUserType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarningContent() {
        return warningContent;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCheckRange() {
        return checkRange;
    }

    public void setCheckRange(Long checkRange) {
        this.checkRange = checkRange;
    }

    public Long getValidRange() {
        return validRange;
    }

    public void setValidRange(Long validRange) {
        this.validRange = validRange;
    }

    public Character getAuto() {
        return auto;
    }

    public void setAuto(Character auto) {
        this.auto = auto;
    }

    public String getSqlCmd() {
        return sqlCmd;
    }

    public void setSqlCmd(String sqlCmd) {
        this.sqlCmd = sqlCmd;
    }

    public String getSqlCheckTrans() {
        return sqlCheckTrans;
    }

    public void setSqlCheckTrans(String sqlCheckTrans) {
        this.sqlCheckTrans = sqlCheckTrans;
    }

    public String getEffectRole() {
        return effectRole;
    }

    public void setEffectRole(String effectRole) {
        this.effectRole = effectRole;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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
        if (!(object instanceof LockUserType)) {
            return false;
        }
        LockUserType other = (LockUserType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.LockUserType[ id=" + id + " ]";
    }
    
}

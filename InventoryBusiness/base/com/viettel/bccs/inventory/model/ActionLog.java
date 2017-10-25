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
 * @author hoangnt14
 */
@Entity
@Table(name = "ACTION_LOG")
@NamedQueries({
    @NamedQuery(name = "ActionLog.findAll", query = "SELECT a FROM ActionLog a")})
public class ActionLog implements Serializable {
public static enum COLUMNS {ACTIONDATE, ACTIONID, ACTIONIP, ACTIONTYPE, ACTIONUSER, DESCRIPTION, OBJECTID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ACTION_ID")
    @SequenceGenerator(name = "ACTION_ID_GENERATOR", sequenceName = "ACTION_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTION_ID_GENERATOR")
    private Long actionId;
    @Column(name = "ACTION_TYPE")
    private String actionType;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION_USER")
    private String actionUser;
    @Column(name = "ACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Column(name = "ACTION_IP")
    private String actionIp;
    @Column(name = "OBJECT_ID")
    private Long objectId;

    public ActionLog() {
    }

    public ActionLog(Long actionId) {
        this.actionId = actionId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionUser() {
        return actionUser;
    }

    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionIp() {
        return actionIp;
    }

    public void setActionIp(String actionIp) {
        this.actionIp = actionIp;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionId != null ? actionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionLog)) {
            return false;
        }
        ActionLog other = (ActionLog) object;
        if ((this.actionId == null && other.actionId != null) || (this.actionId != null && !this.actionId.equals(other.actionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai2.ActionLog[ actionId=" + actionId + " ]";
    }
    
}

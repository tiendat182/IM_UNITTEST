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
@Table(name = "ACTION_LOG_DETAIL")
@NamedQueries({
    @NamedQuery(name = "ActionLogDetail.findAll", query = "SELECT a FROM ActionLogDetail a")})
public class ActionLogDetail implements Serializable {
public static enum COLUMNS {ACTIONDATE, ACTIONDETAILID, ACTIONID, COLUMNNAME, DESCRIPTION, NEWVALUE, OLDVALUE, TABLENAME, EXCLUSE_ID_LIST};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ACTION_DETAIL_ID")
    @SequenceGenerator(name = "ACTION_DETAIL_ID_GENERATOR", sequenceName = "ACTION_LOG_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTION_DETAIL_ID_GENERATOR")
    private Long actionDetailId;
    @Column(name = "ACTION_ID")
    private Long actionId;
    @Column(name = "TABLE_NAME")
    private String tableName;
    @Column(name = "COLUMN_NAME")
    private String columnName;
    @Column(name = "OLD_VALUE")
    private String oldValue;
    @Column(name = "NEW_VALUE")
    private String newValue;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;

    public ActionLogDetail() {
    }

    public ActionLogDetail(Long actionDetailId) {
        this.actionDetailId = actionDetailId;
    }

    public Long getActionDetailId() {
        return actionDetailId;
    }

    public void setActionDetailId(Long actionDetailId) {
        this.actionDetailId = actionDetailId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actionDetailId != null ? actionDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActionLogDetail)) {
            return false;
        }
        ActionLogDetail other = (ActionLogDetail) object;
        if ((this.actionDetailId == null && other.actionDetailId != null) || (this.actionDetailId != null && !this.actionDetailId.equals(other.actionDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai2.ActionLogDetail[ actionDetailId=" + actionDetailId + " ]";
    }
    
}

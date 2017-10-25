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
 * @author sinhhv
 */
@Entity
@Table(name = "GROUP_FILTER_RULE")
public class GroupFilterRule implements Serializable {
    public static enum COLUMNS {GROUPTYPE,GROUPFILTERRULECODE, GROUPFILTERRULEID, NAME, NOTES, PARENTID, TELECOMSERVICEID, STATUS, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "GROUP_FILTER_RULE_ID_GENERATOR", sequenceName = "GROUP_FILTER_RULE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUP_FILTER_RULE_ID_GENERATOR")
    @Column(name = "GROUP_FILTER_RULE_ID")
    private Long groupFilterRuleId;
    @Basic(optional = false)
    @Column(name = "GROUP_FILTER_RULE_CODE")
    private String groupFilterRuleCode;
    @Basic(optional = false)
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private Character status;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "GROUP_TYPE")
    private String groupType;
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

    public GroupFilterRule() {
    }

    public GroupFilterRule(Long groupFilterRuleId) {
        this.groupFilterRuleId = groupFilterRuleId;
    }

    public GroupFilterRule(Long groupFilterRuleId, String groupFilterRuleCode) {
        this.groupFilterRuleId = groupFilterRuleId;
        this.groupFilterRuleCode = groupFilterRuleCode;
    }

    public Long getGroupFilterRuleId() {
        return groupFilterRuleId;
    }

    public void setGroupFilterRuleId(Long groupFilterRuleId) {
        this.groupFilterRuleId = groupFilterRuleId;
    }

    public String getGroupFilterRuleCode() {
        return groupFilterRuleCode;
    }

    public void setGroupFilterRuleCode(String groupFilterRuleCode) {
        this.groupFilterRuleCode = groupFilterRuleCode;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
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
        hash += (groupFilterRuleId != null ? groupFilterRuleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupFilterRule)) {
            return false;
        }
        GroupFilterRule other = (GroupFilterRule) object;
        if ((this.groupFilterRuleId == null && other.groupFilterRuleId != null) || (this.groupFilterRuleId != null && !this.groupFilterRuleId.equals(other.groupFilterRuleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.GroupFilterRule[ groupFilterRuleId=" + groupFilterRuleId + " ]";
    }

}

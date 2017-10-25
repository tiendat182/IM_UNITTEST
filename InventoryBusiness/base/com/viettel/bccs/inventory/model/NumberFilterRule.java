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
 * @author hungdv24
 */
@Entity
@Table(name = "NUMBER_FILTER_RULE")
public class NumberFilterRule implements Serializable {
    public static enum COLUMNS {CONDITION, FILTERRULEID, GROUPFILTERRULEID, MASKMAPPING, NAME, NOTES, RULEORDER, STATUS, PRODOFFERID, CREATEUSER, LASTUPDATEUSER, CREATEDATE, LASTUPDATETIME, EXCLUSE_ID_LIST, }



    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "NUMBER_FILTER_RULE_ID_GENERATOR", sequenceName = "NUMBER_FILTER_RULE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NUMBER_FILTER_RULE_ID_GENERATOR")
    @Column(name = "FILTER_RULE_ID")
    private Long filterRuleId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MASK_MAPPING")
    private String maskMapping;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "RULE_ORDER")
    private Long ruleOrder;
    @Column(name = "CONDITION")
    private String condition;
    @Column(name = "GROUP_FILTER_RULE_ID")
    private Long groupFilterRuleId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    public NumberFilterRule() {
    }

    public NumberFilterRule(Long filterRuleId) {
        this.filterRuleId = filterRuleId;
    }

    public Long getFilterRuleId() {
        return filterRuleId;
    }

    public void setFilterRuleId(Long filterRuleId) {
        this.filterRuleId = filterRuleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaskMapping() {
        return maskMapping;
    }

    public void setMaskMapping(String maskMapping) {
        this.maskMapping = maskMapping;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(Long ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getGroupFilterRuleId() {
        return groupFilterRuleId;
    }

    public void setGroupFilterRuleId(Long groupFilterRuleId) {
        this.groupFilterRuleId = groupFilterRuleId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filterRuleId != null ? filterRuleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NumberFilterRule)) {
            return false;
        }
        NumberFilterRule other = (NumberFilterRule) object;
        if ((this.filterRuleId == null && other.filterRuleId != null) || (this.filterRuleId != null && !this.filterRuleId.equals(other.filterRuleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "abc.NumberFilterRule[ filterRuleId=" + filterRuleId + " ]";
    }

}

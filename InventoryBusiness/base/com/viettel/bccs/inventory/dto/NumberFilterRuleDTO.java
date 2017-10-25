package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NumberFilterRuleDTO extends BaseDTO implements Serializable {

    private String condition;
    private Date createDate;
    private String createUser;
    private Long filterRuleId;
    private Long groupFilterRuleId;//Nhom luat
    private Long aggregateFilterRuleId; // Tap luat
    private Date lastUpdateTime;
    private String lastUpdateUser;
    private String maskMapping;
    private String name;
    private String notes;
    private Long prodOfferId;
    private Long ruleOrder;
    private String status;
    private List<Long> lsFilterRuleId = Lists.newArrayList(); // danh sach id nhom luat
    public String getKeySet() {
        return keySet; }

    public String getCondition() {
        return this.condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Long getFilterRuleId() {
        return this.filterRuleId;
    }
    public void setFilterRuleId(Long filterRuleId) {
        this.filterRuleId = filterRuleId;
    }
    public Long getGroupFilterRuleId() {
        return this.groupFilterRuleId;
    }
    public void setGroupFilterRuleId(Long groupFilterRuleId) {
        this.groupFilterRuleId = groupFilterRuleId;
    }
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    public String getMaskMapping() {
        return this.maskMapping;
    }
    public void setMaskMapping(String maskMapping) {
        this.maskMapping = maskMapping;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNotes() {
        return this.notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public Long getRuleOrder() {
        return this.ruleOrder;
    }
    public void setRuleOrder(Long ruleOrder) {
        this.ruleOrder = ruleOrder;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAggregateFilterRuleId() {
        return aggregateFilterRuleId;
    }

    public void setAggregateFilterRuleId(Long aggregateFilterRuleId) {
        this.aggregateFilterRuleId = aggregateFilterRuleId;
    }

    public List<Long> getLsFilterRuleId() {
        return lsFilterRuleId;
    }

    public void setLsFilterRuleId(List<Long> lsFilterRuleId) {
        this.lsFilterRuleId = lsFilterRuleId;
    }
}

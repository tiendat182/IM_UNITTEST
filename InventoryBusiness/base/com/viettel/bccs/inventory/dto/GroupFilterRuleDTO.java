package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.text.Collator;
import java.util.Date;
import java.util.Locale;

public class GroupFilterRuleDTO extends BaseDTO implements Serializable,Comparable<GroupFilterRuleDTO> {

    private String groupFilterRuleCode;
    private Long groupFilterRuleId;
    private String name;
    private String notes;
    private Long parentId;
    private Long telecomServiceId;
    private String status;
    private String telecomServiceName;
    private String groupType;
    private Date createDate;
    private String createUser;
    private Date lastUpdateTime;
    private String lastUpdateUser;
    public String getKeySet() {
        return keySet; }
    public String getGroupFilterRuleCode() {
        return this.groupFilterRuleCode;
    }
    public void setGroupFilterRuleCode(String groupFilterRuleCode) {
        this.groupFilterRuleCode = groupFilterRuleCode;
    }
    public Long getGroupFilterRuleId() {
        return this.groupFilterRuleId;
    }
    public void setGroupFilterRuleId(Long groupFilterRuleId) {
        this.groupFilterRuleId = groupFilterRuleId;
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
    public Long getParentId() {
        return this.parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelecomServiceName() {
        return telecomServiceName;
    }

    public void setTelecomServiceName(String telecomServiceName) {
        this.telecomServiceName = telecomServiceName;
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
    public int compareTo(GroupFilterRuleDTO o) {
        Collator collate = Collator.getInstance(new Locale("vi"));
        return collate.compare(this.getName().toLowerCase(),o.getName().toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

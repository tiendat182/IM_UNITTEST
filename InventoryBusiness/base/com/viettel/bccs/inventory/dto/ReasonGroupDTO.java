package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class ReasonGroupDTO extends BaseDTO implements Serializable {
    private String description;
    private String reasonGroupCode;
    private Long reasonGroupId;
    private String reasonGroupName;
    private String status;

    public String getKeySet() {
        return keySet; }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getReasonGroupCode() {
        return this.reasonGroupCode;
    }
    public void setReasonGroupCode(String reasonGroupCode) {
        this.reasonGroupCode = reasonGroupCode;
    }
    public Long getReasonGroupId() {
        return this.reasonGroupId;
    }
    public void setReasonGroupId(Long reasonGroupId) {
        this.reasonGroupId = reasonGroupId;
    }
    public String getReasonGroupName() {
        return this.reasonGroupName;
    }
    public void setReasonGroupName(String reasonGroupName) {
        this.reasonGroupName = reasonGroupName;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

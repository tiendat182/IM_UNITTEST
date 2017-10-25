package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class NumberActionDetailDTO extends BaseDTO implements Serializable {

    private String columnName;
    private String newDetailValue;
    private String newValue;
    private Long numberActionDetailId;
    private Long numberActionId;
    private String oldDetailValue;
    private String oldValue;

    public String getKeySet() {
        return keySet; }
    public String getColumnName() {
        return this.columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getNewDetailValue() {
        return this.newDetailValue;
    }
    public void setNewDetailValue(String newDetailValue) {
        this.newDetailValue = newDetailValue;
    }
    public String getNewValue() {
        return this.newValue;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    public Long getNumberActionDetailId() {
        return this.numberActionDetailId;
    }
    public void setNumberActionDetailId(Long numberActionDetailId) {
        this.numberActionDetailId = numberActionDetailId;
    }
    public Long getNumberActionId() {
        return this.numberActionId;
    }
    public void setNumberActionId(Long numberActionId) {
        this.numberActionId = numberActionId;
    }
    public String getOldDetailValue() {
        return this.oldDetailValue;
    }
    public void setOldDetailValue(String oldDetailValue) {
        this.oldDetailValue = oldDetailValue;
    }
    public String getOldValue() {
        return this.oldValue;
    }
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
}

package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class StockTypeIm1DTO extends BaseDTO implements Serializable {
  private Long stockTypeId;
    private String name;
    private Long status;
    private String notes;
    private String tableName;
    private Long checkExp;
    public String getKeySet() {
        return keySet;
    }
    public Long getStockTypeId() {
        return this.stockTypeId;
    }
    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public String getNotes() {
        return this.notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getTableName() {
        return this.tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public Long getCheckExp() {
        return this.checkExp;
    }
    public void setCheckExp(Long checkExp) {
        this.checkExp = checkExp;
    }
}

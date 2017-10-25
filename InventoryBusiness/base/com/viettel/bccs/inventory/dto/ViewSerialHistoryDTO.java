/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author User
 */
public class ViewSerialHistoryDTO extends BaseDTO {

    private Long stockTransId;
    private String stockTransType;
    private String stockTransTypeName;
    private String exportStore;
    private String importStore;
    private String status;
    private String statusName;
    private String userSerial;
    private Date createDate;

    public ViewSerialHistoryDTO() {
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getStockTransType() {
        return stockTransType;
    }

    public void setStockTransType(String stockTransType) {
        this.stockTransType = stockTransType;
    }

    public String getStockTransTypeName() {
        return stockTransTypeName;
    }

    public void setStockTransTypeName(String stockTransTypeName) {
        this.stockTransTypeName = stockTransTypeName;
    }

    public String getExportStore() {
        return exportStore;
    }

    public void setExportStore(String exportStore) {
        this.exportStore = exportStore;
    }

    public String getImportStore() {
        return importStore;
    }

    public void setImportStore(String importStore) {
        this.importStore = importStore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getUserSerial() {
        return userSerial;
    }

    public void setUserSerial(String userSerial) {
        this.userSerial = userSerial;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

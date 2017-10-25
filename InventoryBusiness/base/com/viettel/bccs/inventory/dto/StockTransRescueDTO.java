package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StockTransRescueDTO extends BaseDTO implements Serializable {

    private Long approveStaffId;
    private Date createDate;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private Long reasonId;
    private String requestCode;
    private Long status;
    private Long stockTransId;
    private Long toOwnerId;
    private Long toOwnerType;
    private Date fromDate;
    private Date toDate;
    private String strCreateDate;
    private String fromStock;
    private String toStock;
    private String reasonName;
    private String strStaus;
    private String staffRequest;
    private Date requestDate;
    private String description;
    private String errorStatusMgs;
    private String role;

    public String getKeySet() {
        return keySet;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorStatusMgs() {
        return errorStatusMgs;
    }

    public void setErrorStatusMgs(String errorStatusMgs) {
        this.errorStatusMgs = errorStatusMgs;
    }

    private List<StockHandsetRescueDTO> lstSelection;

    public List<StockHandsetRescueDTO> getLstSelection() {
        return lstSelection;
    }

    public void setLstSelection(List<StockHandsetRescueDTO> lstSelection) {
        this.lstSelection = lstSelection;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStaffRequest() {
        return staffRequest;
    }

    public void setStaffRequest(String staffRequest) {
        this.staffRequest = staffRequest;
    }

    public String getFromStock() {
        return fromStock;
    }

    public void setFromStock(String fromStock) {
        this.fromStock = fromStock;
    }

    public String getToStock() {
        return toStock;
    }

    public void setToStock(String toStock) {
        this.toStock = toStock;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getStrStaus() {
        return strStaus;
    }

    public void setStrStaus(String strStaus) {
        this.strStaus = strStaus;
    }

    public String getStrCreateDate() {
        return strCreateDate;
    }

    public void setStrCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    public Long getApproveStaffId() {
        return this.approveStaffId;
    }

    public void setApproveStaffId(Long approveStaffId) {
        this.approveStaffId = approveStaffId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getFromOwnerId() {
        return this.fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getFromOwnerType() {
        return this.fromOwnerType;
    }

    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public Long getReasonId() {
        return this.reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getToOwnerId() {
        return this.toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public Long getToOwnerType() {
        return this.toOwnerType;
    }

    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}

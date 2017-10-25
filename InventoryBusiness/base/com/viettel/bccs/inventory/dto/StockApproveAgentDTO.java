package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 * Created by hoangnt14 on 3/25/2016.
 */
public class StockApproveAgentDTO extends BaseDTO {

    private Long saleTransId;
    private String saleTransCode;
    private Date saleTransDate;
    private String staffCreate;
    private String shopExp;
    private String shopImp;
    private String approveStatus;
    private String approveStatusName;
    private String status;
    private Long totalAmount;
    private Long totalDiscount;
    private Long maxMoneyDebit;
    private Long currentDebit;
    private String approveStaff;
    private Date approveDate;
    private boolean selected;
    private String saleTransDateString;
    private String approveDateString;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public String getSaleTransCode() {
        return saleTransCode;
    }

    public void setSaleTransCode(String saleTransCode) {
        this.saleTransCode = saleTransCode;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public String getSaleTransDateString() {
        if (!DataUtil.isNullObject(saleTransDate)) {
            saleTransDateString = DateUtil.date2ddMMyyyyHHMMss(saleTransDate);
        }
        return saleTransDateString;
    }

    public void setSaleTransDateString(String saleTransDateString) {
        this.saleTransDateString = saleTransDateString;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public String getStaffCreate() {
        return staffCreate;
    }

    public void setStaffCreate(String staffCreate) {
        this.staffCreate = staffCreate;
    }

    public String getShopExp() {
        return shopExp;
    }

    public void setShopExp(String shopExp) {
        this.shopExp = shopExp;
    }

    public String getShopImp() {
        return shopImp;
    }

    public void setShopImp(String shopImp) {
        this.shopImp = shopImp;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Long totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Long getMaxMoneyDebit() {
        return maxMoneyDebit;
    }

    public void setMaxMoneyDebit(Long maxMoneyDebit) {
        this.maxMoneyDebit = maxMoneyDebit;
    }

    public Long getCurrentDebit() {
        return currentDebit;
    }

    public void setCurrentDebit(Long currentDebit) {
        this.currentDebit = currentDebit;
    }

    public String getApproveStaff() {
        return approveStaff;
    }

    public void setApproveStaff(String approveStaff) {
        this.approveStaff = approveStaff;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveStatusName() {
        return approveStatusName;
    }

    public void setApproveStatusName(String approveStatusName) {
        this.approveStatusName = approveStatusName;
    }

    public String getApproveDateString() {
        if (!DataUtil.isNullObject(approveDate)) {
            approveDateString = DateUtil.date2ddMMyyyyHHMMss(approveDate);
        }
        return approveDateString;
    }

    public void setApproveDateString(String approveDateString) {
        this.approveDateString = approveDateString;
    }
}
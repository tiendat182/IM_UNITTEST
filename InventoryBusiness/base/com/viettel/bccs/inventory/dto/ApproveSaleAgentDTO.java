package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 * Created by hoangnt14 on 3/15/2016.
 */
public class ApproveSaleAgentDTO extends BaseDTO {

    private Long saleTransId;
    private String saleTransCode;
    private Date saleTransDate;
    private String staffCreate;
    private String shopExp;
    private String shopImp;
    private String approveStatus;
    private Long totalAmount;
    private Long discountAmount;
    private Date startDate;
    private Date endDate;
    private boolean selected;

    public boolean getSelected() {
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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

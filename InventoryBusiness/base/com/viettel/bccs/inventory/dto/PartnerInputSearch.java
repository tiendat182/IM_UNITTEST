package com.viettel.bccs.inventory.dto;

import java.io.Serializable;
import java.util.Date;

public class PartnerInputSearch implements Serializable {
    private String address; //dia chi
    private String contactName;
    private Date endDate; // ngay ket thuc
    private String fax; //fax
    private String partnerCode; //ma doi tac
    private String partnerName; //ten doi tac
    private Long partnerType; // loai doi tac
    private String phone; // so dien thoai
    private Date startDate; // ngay bat dau
    private Long status; // trang thai
    private String a4Key;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Long getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(Long partnerType) {
        this.partnerType = partnerType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getA4Key() {
        return a4Key;
    }

    public void setA4Key(String a4Key) {
        this.a4Key = a4Key;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}

package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class PartnerDTO extends BaseDTO implements Serializable {

    private String address;
    private String contactName;
    private Date endDate;
    private String fax;
    private String partnerCode;
    private Long partnerId;
    private String partnerName;
    private Long partnerType;
    private String phone;
    private Date staDate;
    private Long status;
    private String a4Key;
    private boolean searchDTO = false;

    public String getA4Key() {
        return a4Key;
    }

    public void setA4Key(String a4Key) {
        this.a4Key = a4Key;
    }

    public String getKeySet() {
        return keySet;
    }

    public PartnerDTO() {
        searchDTO = false;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public Long getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return this.partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Long getPartnerType() {
        return this.partnerType;
    }

    public void setPartnerType(Long partnerType) {
        this.partnerType = partnerType;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getStaDate() {
        return this.staDate;
    }

    public void setStaDate(Date staDate) {
        this.staDate = staDate;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public boolean isSearchDTO() {
        return searchDTO;
    }

    public void setSearchDTO(boolean searchDTO) {
        this.searchDTO = searchDTO;
    }
}

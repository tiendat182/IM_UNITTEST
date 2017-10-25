package com.viettel.bccs.im1.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class InvoiceTemplateIm1DTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long amount;
    private Long bookTypeId;
    private Date createDate;
    private String createUser;
    private String description;
    private Long invoiceTemplateId;
    private Date lastUpdateDate;
    private String lastUpdateUser;
    private Long preAmount;
    private String serialNo;
    private Long shopId;
    private Long staffId;
    private Long type;
    private Long usedAmount;
    public Long getAmount() {
        return this.amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public Long getBookTypeId() {
        return this.bookTypeId;
    }
    public void setBookTypeId(Long bookTypeId) {
        this.bookTypeId = bookTypeId;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getInvoiceTemplateId() {
        return this.invoiceTemplateId;
    }
    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }
    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    public Long getPreAmount() {
        return this.preAmount;
    }
    public void setPreAmount(Long preAmount) {
        this.preAmount = preAmount;
    }
    public String getSerialNo() {
        return this.serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public Long getShopId() {
        return this.shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public Long getStaffId() {
        return this.staffId;
    }
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
    public Long getType() {
        return this.type;
    }
    public void setType(Long type) {
        this.type = type;
    }
    public Long getUsedAmount() {
        return this.usedAmount;
    }
    public void setUsedAmount(Long usedAmount) {
        this.usedAmount = usedAmount;
    }
}

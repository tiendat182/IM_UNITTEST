package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class ReportChangeHandsetDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long adjustAmount;
    private Long changeType;
    private Date createDate;
    private String custName;
    private String custTel;
    private String damageGoodStatus;
    private Long devStaffId;
    private Long prodOfferIdNew;
    private Long prodOfferIdOld;
    private Long reportChangeHandsetId;
    private String serialNew;
    private String serialOld;
    private Long shopId;
    private Long staffId;
    private Long stockTransId;

    private Long saleTransId;
    private Date saleTransDate;
    private String shopName;
    private String staffName;
    private Long priceSaled;
    private String prodOfferName;
    private String serialSaled;
    private String checkStock;
    private String payMethod;
    private String contractNo;
    private String telNumber;
    private String company;
    private String address;
    private String tin;
    private String note;
    private Long reasonId;
    private Long telecomServiceId;

    public Long getAdjustAmount() {
        return this.adjustAmount;
    }
    public void setAdjustAmount(Long adjustAmount) {
        this.adjustAmount = adjustAmount;
    }
    public Long getChangeType() {
        return this.changeType;
    }
    public void setChangeType(Long changeType) {
        this.changeType = changeType;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCustName() {
        return this.custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getCustTel() {
        return this.custTel;
    }
    public void setCustTel(String custTel) {
        this.custTel = custTel;
    }
    public String getDamageGoodStatus() {
        return this.damageGoodStatus;
    }
    public void setDamageGoodStatus(String damageGoodStatus) {
        this.damageGoodStatus = damageGoodStatus;
    }
    public Long getDevStaffId() {
        return this.devStaffId;
    }
    public void setDevStaffId(Long devStaffId) {
        this.devStaffId = devStaffId;
    }
    public Long getProdOfferIdNew() {
        return this.prodOfferIdNew;
    }
    public void setProdOfferIdNew(Long prodOfferIdNew) {
        this.prodOfferIdNew = prodOfferIdNew;
    }
    public Long getProdOfferIdOld() {
        return this.prodOfferIdOld;
    }
    public void setProdOfferIdOld(Long prodOfferIdOld) {
        this.prodOfferIdOld = prodOfferIdOld;
    }
    public Long getReportChangeHandsetId() {
        return this.reportChangeHandsetId;
    }
    public void setReportChangeHandsetId(Long reportChangeHandsetId) {
        this.reportChangeHandsetId = reportChangeHandsetId;
    }
    public String getSerialNew() {
        return this.serialNew;
    }
    public void setSerialNew(String serialNew) {
        this.serialNew = serialNew;
    }
    public String getSerialOld() {
        return this.serialOld;
    }
    public void setSerialOld(String serialOld) {
        this.serialOld = serialOld;
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
    public Long getStockTransId() {
        return this.stockTransId;
    }
    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Date getSaleTransDate() {
        return saleTransDate;
    }

    public String getStrSaleTransDate() {
        return saleTransDate == null ? "" : DateUtil.date2ddMMyyyyHHMMss(saleTransDate);
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getPriceSaled() {
        return priceSaled;
    }

    public void setPriceSaled(Long priceSaled) {
        this.priceSaled = priceSaled;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getSerialSaled() {
        return serialSaled;
    }

    public void setSerialSaled(String serialSaled) {
        this.serialSaled = serialSaled;
    }

    public String getCheckStock() {
        return checkStock;
    }

    public void setCheckStock(String checkStock) {
        this.checkStock = checkStock;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
}

package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

public class RevokeKitDetailDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Long accountBookBankplusId;
    private String actStatus;
    private String actStatusName;
    private String addMoneyResult;
    private Long addMoneyStatus;
    private String addMoneyStatusName;
    private Date createDate;
    private String dataProductCode;
    private Date dataRegisterDate;
    private Long id;
    private String isdn;
    private Date lastModify;
    private Long mainBalance;
    private String orgOwnerCode;
    private Long orgOwnerId;
    private Long orgOwnerType;
    private Long price;
    private Long prodOfferId;
    private String productCode;
    private Long promotionBalance;
    private Long registerStatus;
    private Date revokeDate;
    private String revokeDescription;
    private Long revokeKitTransId;
    private Long revokeStatus;
    private String revokeStatusName;
    private Long revokeType;
    private String revokeTypeName;
    private Date saleDate;
    private Long saleDiscount;
    private Long salePrice;
    private Long saleTransDetailId;
    private Long saleTransId;
    private Long saleTransRevokeId;
    private String serial;
    private Date staDatetime;
    private Long stage;
    private Long stockTransId;
    private Long subId;
    private String subStatus;
    private Long tk10;
    private Long tk27;
    private Long tk34;
    private Long tk46;
    private Long tkData;
    private Date verifyDate;
    private String verifyDescription;
    private Long verifyStatus;
    private String verifyStatusName;
    private String verifyType;
    private String reasonCode;


    public String getStrVerifyStatusName() {
        if (DataUtil.safeEqual(verifyStatus, 0L)) {
            return IMServiceUtil.getTextKey("revoke.kit.search.verify.status0");
        } else if (DataUtil.safeEqual(verifyStatus, 1L)) {
            return IMServiceUtil.getTextKey("revoke.kit.search.verify.status1");
        }
        return IMServiceUtil.getTextKey("revoke.kit.search.verify.status.other");
    }

    public Long getAccountBookBankplusId() {
        return this.accountBookBankplusId;
    }

    public void setAccountBookBankplusId(Long accountBookBankplusId) {
        this.accountBookBankplusId = accountBookBankplusId;
    }

    public String getActStatus() {
        return this.actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }

    public String getAddMoneyResult() {
        return this.addMoneyResult;
    }

    public void setAddMoneyResult(String addMoneyResult) {
        this.addMoneyResult = addMoneyResult;
    }

    public Long getAddMoneyStatus() {
        return this.addMoneyStatus;
    }

    public void setAddMoneyStatus(Long addMoneyStatus) {
        this.addMoneyStatus = addMoneyStatus;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDataProductCode() {
        return this.dataProductCode;
    }

    public void setDataProductCode(String dataProductCode) {
        this.dataProductCode = dataProductCode;
    }

    public Date getDataRegisterDate() {
        return this.dataRegisterDate;
    }

    public void setDataRegisterDate(Date dataRegisterDate) {
        this.dataRegisterDate = dataRegisterDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Date getLastModify() {
        return this.lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public Long getMainBalance() {
        return this.mainBalance;
    }

    public void setMainBalance(Long mainBalance) {
        this.mainBalance = mainBalance;
    }

    public String getOrgOwnerCode() {
        return this.orgOwnerCode;
    }

    public void setOrgOwnerCode(String orgOwnerCode) {
        this.orgOwnerCode = orgOwnerCode;
    }

    public Long getOrgOwnerId() {
        return this.orgOwnerId;
    }

    public void setOrgOwnerId(Long orgOwnerId) {
        this.orgOwnerId = orgOwnerId;
    }

    public Long getOrgOwnerType() {
        return this.orgOwnerType;
    }

    public void setOrgOwnerType(Long orgOwnerType) {
        this.orgOwnerType = orgOwnerType;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getProdOfferId() {
        return this.prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getPromotionBalance() {
        return this.promotionBalance;
    }

    public void setPromotionBalance(Long promotionBalance) {
        this.promotionBalance = promotionBalance;
    }

    public Long getRegisterStatus() {
        return this.registerStatus;
    }

    public void setRegisterStatus(Long registerStatus) {
        this.registerStatus = registerStatus;
    }

    public Date getRevokeDate() {
        return this.revokeDate;
    }

    public void setRevokeDate(Date revokeDate) {
        this.revokeDate = revokeDate;
    }

    public String getRevokeDescription() {
        return this.revokeDescription;
    }

    public void setRevokeDescription(String revokeDescription) {
        this.revokeDescription = revokeDescription;
    }

    public Long getRevokeKitTransId() {
        return this.revokeKitTransId;
    }

    public void setRevokeKitTransId(Long revokeKitTransId) {
        this.revokeKitTransId = revokeKitTransId;
    }

    public Long getRevokeStatus() {
        return this.revokeStatus;
    }

    public void setRevokeStatus(Long revokeStatus) {
        this.revokeStatus = revokeStatus;
    }

    public Long getRevokeType() {
        return this.revokeType;
    }

    public void setRevokeType(Long revokeType) {
        this.revokeType = revokeType;
    }

    public Date getSaleDate() {
        return this.saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Long getSaleDiscount() {
        return this.saleDiscount;
    }

    public void setSaleDiscount(Long saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public Long getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(Long salePrice) {
        this.salePrice = salePrice;
    }

    public Long getSaleTransDetailId() {
        return this.saleTransDetailId;
    }

    public void setSaleTransDetailId(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public Long getSaleTransId() {
        return this.saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Long getSaleTransRevokeId() {
        return this.saleTransRevokeId;
    }

    public void setSaleTransRevokeId(Long saleTransRevokeId) {
        this.saleTransRevokeId = saleTransRevokeId;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getStaDatetime() {
        return this.staDatetime;
    }

    public void setStaDatetime(Date staDatetime) {
        this.staDatetime = staDatetime;
    }

    public Long getStage() {
        return this.stage;
    }

    public void setStage(Long stage) {
        this.stage = stage;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getSubStatus() {
        return this.subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public Long getTk10() {
        return this.tk10;
    }

    public void setTk10(Long tk10) {
        this.tk10 = tk10;
    }

    public Long getTk27() {
        return this.tk27;
    }

    public void setTk27(Long tk27) {
        this.tk27 = tk27;
    }

    public Long getTk34() {
        return this.tk34;
    }

    public void setTk34(Long tk34) {
        this.tk34 = tk34;
    }

    public Long getTk46() {
        return this.tk46;
    }

    public void setTk46(Long tk46) {
        this.tk46 = tk46;
    }

    public Long getTkData() {
        return this.tkData;
    }

    public void setTkData(Long tkData) {
        this.tkData = tkData;
    }

    public Date getVerifyDate() {
        return this.verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getActStatusName() {
        return actStatusName;
    }

    public void setActStatusName(String actStatusName) {
        this.actStatusName = actStatusName;
    }

    public String getAddMoneyStatusName() {
        return addMoneyStatusName;
    }

    public void setAddMoneyStatusName(String addMoneyStatusName) {
        this.addMoneyStatusName = addMoneyStatusName;
    }

    public String getRevokeStatusName() {
        return revokeStatusName;
    }

    public void setRevokeStatusName(String revokeStatusName) {
        this.revokeStatusName = revokeStatusName;
    }

    public String getRevokeTypeName() {
        return revokeTypeName;
    }

    public void setRevokeTypeName(String revokeTypeName) {
        this.revokeTypeName = revokeTypeName;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getVerifyDescription() {
        return verifyDescription;
    }

    public void setVerifyDescription(String verifyDescription) {
        this.verifyDescription = verifyDescription;
    }

    public Long getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Long verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyStatusName() {
        return verifyStatusName;
    }

    public void setVerifyStatusName(String verifyStatusName) {
        this.verifyStatusName = verifyStatusName;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}

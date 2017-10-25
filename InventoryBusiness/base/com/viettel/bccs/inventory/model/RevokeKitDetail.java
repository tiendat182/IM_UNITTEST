/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ThanhNT77
 */
@Entity
@Table(name = "REVOKE_KIT_DETAIL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "RevokeKitDetail.findAll", query = "SELECT r FROM RevokeKitDetail r")})
public class RevokeKitDetail implements Serializable {
    public static enum COLUMNS {ACCOUNTBOOKBANKPLUSID, ACTSTATUS, ADDMONEYRESULT, ADDMONEYSTATUS, CREATEDATE, DATAPRODUCTCODE, DATAREGISTERDATE, ID, ISDN, MAINBALANCE, ORGOWNERCODE, ORGOWNERID, ORGOWNERTYPE, PRICE, PRODOFFERID, PRODUCTCODE, PROMOTIONBALANCE, REGISTERSTATUS, REVOKEDATE, REVOKEDESCRIPTION, STADATETIME, REVOKEKITTRANSID, REVOKESTATUS, REVOKETYPE, SALEDATE, SALEDISCOUNT, SALEPRICE, SALETRANSID, SERIAL, STOCKTRANSID, VERIFYDESCRIPTION, VERIFYSTATUS, VERIFYTYPE, REASONCODE, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "REVOKE_KIT_DETAIL_SEQ_ID_GENERATOR", sequenceName = "REVOKE_KIT_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVOKE_KIT_DETAIL_SEQ_ID_GENERATOR")
    private Long id;
    @Column(name = "REVOKE_KIT_TRANS_ID")
    private Long revokeKitTransId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "REVOKE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date revokeDate;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "REVOKE_TYPE")
    private Long revokeType;
    @Column(name = "ORG_OWNER_ID")
    private Long orgOwnerId;
    @Column(name = "ORG_OWNER_TYPE")
    private Long orgOwnerType;
    @Column(name = "ORG_OWNER_CODE")
    private String orgOwnerCode;
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    @Column(name = "DATA_PRODUCT_CODE")
    private String dataProductCode;
    @Column(name = "ACT_STATUS")
    private String actStatus;
    @Column(name = "REGISTER_STATUS")
    private Long registerStatus;
    @Column(name = "SALE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Column(name = "SALE_TRANS_ID")
    private Long saleTransId;
    @Column(name = "SALE_PRICE")
    private Long salePrice;
    @Column(name = "SALE_DISCOUNT")
    private Long saleDiscount;
    @Column(name = "MAIN_BALANCE")
    private Long mainBalance;
    @Column(name = "PROMOTION_BALANCE")
    private Long promotionBalance;
    @Column(name = "DATA_REGISTER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegisterDate;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "ADD_MONEY_STATUS")
    private Long addMoneyStatus;
    @Column(name = "ADD_MONEY_RESULT")
    private String addMoneyResult;
    @Column(name = "ACCOUNT_BOOK_BANKPLUS_ID")
    private Long accountBookBankplusId;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Column(name = "VERIFY_STATUS")
    private Long verifyStatus;
    @Column(name = "VERIFY_DESCRIPTION")
    private String verifyDescription;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "REVOKE_STATUS")
    private Long revokeStatus;
    @Column(name = "REVOKE_DESCRIPTION")
    private String revokeDescription;
    @Column(name = "STA_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date staDatetime;
    @Column(name = "TK_DATA")
    private Long tkData;
    @Column(name = "TK_10")
    private Long tk10;
    @Column(name = "TK_34")
    private Long tk34;
    @Column(name = "TK_27")
    private Long tk27;
    @Column(name = "TK_46")
    private Long tk46;
    @Column(name = "LAST_MODIFY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModify;
    @Column(name = "SALE_TRANS_REVOKE_ID")
    private Long saleTransRevokeId;
    @Column(name = "SUB_ID")
    private Long subId;
    @Column(name = "VERIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifyDate;
    @Column(name = "SALE_TRANS_DETAIL_ID")
    private Long saleTransDetailId;
    @Column(name = "SUB_STATUS")
    private String subStatus;
    @Column(name = "STAGE")
    private Long stage;
    @Column(name = "VERIFY_TYPE")
    private Long verifyType;
    @Column(name = "REASON_CODE")
    private String reasonCode;

    public RevokeKitDetail() {
    }

    public RevokeKitDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRevokeKitTransId() {
        return revokeKitTransId;
    }

    public void setRevokeKitTransId(Long revokeKitTransId) {
        this.revokeKitTransId = revokeKitTransId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getRevokeDate() {
        return revokeDate;
    }

    public void setRevokeDate(Date revokeDate) {
        this.revokeDate = revokeDate;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }


    public Long getOrgOwnerId() {
        return orgOwnerId;
    }

    public void setOrgOwnerId(Long orgOwnerId) {
        this.orgOwnerId = orgOwnerId;
    }

    public String getOrgOwnerCode() {
        return orgOwnerCode;
    }

    public void setOrgOwnerCode(String orgOwnerCode) {
        this.orgOwnerCode = orgOwnerCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDataProductCode() {
        return dataProductCode;
    }

    public void setDataProductCode(String dataProductCode) {
        this.dataProductCode = dataProductCode;
    }

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Long getSaleTransId() {
        return saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
    }

    public Long getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Long salePrice) {
        this.salePrice = salePrice;
    }

    public Long getSaleDiscount() {
        return saleDiscount;
    }

    public void setSaleDiscount(Long saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public Long getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(Long mainBalance) {
        this.mainBalance = mainBalance;
    }

    public Long getPromotionBalance() {
        return promotionBalance;
    }

    public void setPromotionBalance(Long promotionBalance) {
        this.promotionBalance = promotionBalance;
    }

    public Date getDataRegisterDate() {
        return dataRegisterDate;
    }

    public void setDataRegisterDate(Date dataRegisterDate) {
        this.dataRegisterDate = dataRegisterDate;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getAddMoneyResult() {
        return addMoneyResult;
    }

    public void setAddMoneyResult(String addMoneyResult) {
        this.addMoneyResult = addMoneyResult;
    }

    public Long getAccountBookBankplusId() {
        return accountBookBankplusId;
    }

    public void setAccountBookBankplusId(Long accountBookBankplusId) {
        this.accountBookBankplusId = accountBookBankplusId;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public String getVerifyDescription() {
        return verifyDescription;
    }

    public void setVerifyDescription(String verifyDescription) {
        this.verifyDescription = verifyDescription;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getRevokeDescription() {
        return revokeDescription;
    }

    public void setRevokeDescription(String revokeDescription) {
        this.revokeDescription = revokeDescription;
    }

    public Date getStaDatetime() {
        return staDatetime;
    }

    public void setStaDatetime(Date staDatetime) {
        this.staDatetime = staDatetime;
    }

    public Long getTkData() {
        return tkData;
    }

    public void setTkData(Long tkData) {
        this.tkData = tkData;
    }

    public Long getTk10() {
        return tk10;
    }

    public void setTk10(Long tk10) {
        this.tk10 = tk10;
    }

    public Long getTk34() {
        return tk34;
    }

    public void setTk34(Long tk34) {
        this.tk34 = tk34;
    }

    public Long getTk27() {
        return tk27;
    }

    public void setTk27(Long tk27) {
        this.tk27 = tk27;
    }

    public Long getTk46() {
        return tk46;
    }

    public void setTk46(Long tk46) {
        this.tk46 = tk46;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public Long getSaleTransRevokeId() {
        return saleTransRevokeId;
    }

    public void setSaleTransRevokeId(Long saleTransRevokeId) {
        this.saleTransRevokeId = saleTransRevokeId;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Long getSaleTransDetailId() {
        return saleTransDetailId;
    }

    public void setSaleTransDetailId(Long saleTransDetailId) {
        this.saleTransDetailId = saleTransDetailId;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public Long getRevokeType() {
        return revokeType;
    }

    public void setRevokeType(Long revokeType) {
        this.revokeType = revokeType;
    }

    public Long getOrgOwnerType() {
        return orgOwnerType;
    }

    public void setOrgOwnerType(Long orgOwnerType) {
        this.orgOwnerType = orgOwnerType;
    }

    public Long getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(Long registerStatus) {
        this.registerStatus = registerStatus;
    }

    public Long getAddMoneyStatus() {
        return addMoneyStatus;
    }

    public void setAddMoneyStatus(Long addMoneyStatus) {
        this.addMoneyStatus = addMoneyStatus;
    }

    public Long getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Long verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Long getRevokeStatus() {
        return revokeStatus;
    }

    public void setRevokeStatus(Long revokeStatus) {
        this.revokeStatus = revokeStatus;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public Long getStage() {
        return stage;
    }

    public void setStage(Long stage) {
        this.stage = stage;
    }

    public Long getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Long verifyType) {
        this.verifyType = verifyType;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RevokeKitDetail)) {
            return false;
        }
        RevokeKitDetail other = (RevokeKitDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RevokeKitDetail[ id=" + id + " ]";
    }

}

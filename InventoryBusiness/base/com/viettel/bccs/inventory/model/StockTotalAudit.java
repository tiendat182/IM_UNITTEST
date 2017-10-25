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
 *
 * @author DungPT16
 */
@Entity
@Table(name = "STOCK_TOTAL_AUDIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StockTotalAudit.findAll", query = "SELECT s FROM StockTotalAudit s")})
public class StockTotalAudit implements Serializable {
public static enum COLUMNS {AVAILABLEQUANTITY, AVAILABLEQUANTITYAF, AVAILABLEQUANTITYBF, CREATEDATE, CURRENTQUANTITY, CURRENTQUANTITYAF, CURRENTQUANTITYBF, DESCRIPTION, HANGQUANTITY, HANGQUANTITYAF, HANGQUANTITYBF, ID, OWNERCODE, OWNERID, OWNERNAME, OWNERTYPE, PRODOFFERCODE, PRODOFFERID, PRODOFFERNAME, PRODOFFERTYPEID, REASONID, REASONNAME, SHOPCODE, SHOPCODELV1, SHOPCODELV2, SHOPCODELV3, SHOPCODELV4, SHOPCODELV5, SHOPCODELV6, SHOPID, SHOPIDLV1, SHOPIDLV2, SHOPIDLV3, SHOPIDLV4, SHOPIDLV5, SHOPIDLV6, SHOPNAME, SHOPNAMELV1, SHOPNAMELV2, SHOPNAMELV3, SHOPNAMELV4, SHOPNAMELV5, SHOPNAMELV6, SOURCETYPE, STATEID, STATUS, STICKCODE, SYNSTATUS, TRANSCODE, TRANSID, TRANSTYPE, USERCODE, USERID, USERNAME, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_CODE")
    private String ownerCode;
    @Column(name = "OWNER_NAME")
    private String ownerName;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "USER_CODE")
    private String userCode;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "PROD_OFFER_NAME")
    private String prodOfferName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CURRENT_QUANTITY")
    private Long currentQuantity;
    @Column(name = "AVAILABLE_QUANTITY")
    private Long availableQuantity;
    @Column(name = "HANG_QUANTITY")
    private Long hangQuantity;
    @Column(name = "CURRENT_QUANTITY_BF")
    private Long currentQuantityBf;
    @Column(name = "AVAILABLE_QUANTITY_BF")
    private Long availableQuantityBf;
    @Column(name = "HANG_QUANTITY_BF")
    private Long hangQuantityBf;
    @Column(name = "CURRENT_QUANTITY_AF")
    private Long currentQuantityAf;
    @Column(name = "AVAILABLE_QUANTITY_AF")
    private Long availableQuantityAf;
    @Column(name = "HANG_QUANTITY_AF")
    private Long hangQuantityAf;
    @Column(name = "TRANS_ID")
    private Long transId;
    @Column(name = "TRANS_CODE")
    private String transCode;
    @Column(name = "TRANS_TYPE")
    private Long transType;
    @Column(name = "SOURCE_TYPE")
    private Long sourceType;
    @Column(name = "STICK_CODE")
    private String stickCode;
    @Column(name = "REASON_NAME")
    private String reasonName;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SHOP_ID_LV1")
    private Long shopIdLv1;
    @Column(name = "SHOP_CODE_LV1")
    private String shopCodeLv1;
    @Column(name = "SHOP_NAME_LV1")
    private String shopNameLv1;
    @Column(name = "SHOP_ID_LV2")
    private Long shopIdLv2;
    @Column(name = "SHOP_CODE_LV2")
    private String shopCodeLv2;
    @Column(name = "SHOP_NAME_LV2")
    private String shopNameLv2;
    @Column(name = "SHOP_ID_LV3")
    private Long shopIdLv3;
    @Column(name = "SHOP_CODE_LV3")
    private String shopCodeLv3;
    @Column(name = "SHOP_NAME_LV3")
    private String shopNameLv3;
    @Column(name = "SHOP_ID_LV4")
    private Long shopIdLv4;
    @Column(name = "SHOP_CODE_LV4")
    private String shopCodeLv4;
    @Column(name = "SHOP_NAME_LV4")
    private String shopNameLv4;
    @Column(name = "SHOP_ID_LV5")
    private Long shopIdLv5;
    @Column(name = "SHOP_CODE_LV5")
    private String shopCodeLv5;
    @Column(name = "SHOP_NAME_LV5")
    private String shopNameLv5;
    @Column(name = "SHOP_ID")
    private Long shopId;
    @Column(name = "SHOP_CODE")
    private String shopCode;
    @Column(name = "SHOP_NAME")
    private String shopName;
    @Column(name = "SHOP_ID_LV6")
    private Long shopIdLv6;
    @Column(name = "SHOP_CODE_LV6")
    private String shopCodeLv6;
    @Column(name = "SHOP_NAME_LV6")
    private String shopNameLv6;
    @Column(name = "SYN_STATUS")
    private Long synStatus;
    @Column(name = "PROD_OFFER_CODE")
    private String prodOfferCode;
    @Column(name = "PROD_OFFER_TYPE_ID")
    private Long prodOfferTypeId;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "ID_GENERATOR", sequenceName = "STOCK_TOTAL_AUDIT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GENERATOR")
    private Long id;

    public StockTotalAudit() {
    }

    public StockTotalAudit(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getHangQuantity() {
        return hangQuantity;
    }

    public void setHangQuantity(Long hangQuantity) {
        this.hangQuantity = hangQuantity;
    }

    public Long getCurrentQuantityBf() {
        return currentQuantityBf;
    }

    public void setCurrentQuantityBf(Long currentQuantityBf) {
        this.currentQuantityBf = currentQuantityBf;
    }

    public Long getAvailableQuantityBf() {
        return availableQuantityBf;
    }

    public void setAvailableQuantityBf(Long availableQuantityBf) {
        this.availableQuantityBf = availableQuantityBf;
    }

    public Long getHangQuantityBf() {
        return hangQuantityBf;
    }

    public void setHangQuantityBf(Long hangQuantityBf) {
        this.hangQuantityBf = hangQuantityBf;
    }

    public Long getCurrentQuantityAf() {
        return currentQuantityAf;
    }

    public void setCurrentQuantityAf(Long currentQuantityAf) {
        this.currentQuantityAf = currentQuantityAf;
    }


    public Long getHangQuantityAf() {
        return hangQuantityAf;
    }

    public void setHangQuantityAf(Long hangQuantityAf) {
        this.hangQuantityAf = hangQuantityAf;
    }

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }

    public Long getSourceType() {
        return sourceType;
    }

    public void setSourceType(Long sourceType) {
        this.sourceType = sourceType;
    }

    public String getStickCode() {
        return stickCode;
    }

    public void setStickCode(String stickCode) {
        this.stickCode = stickCode;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getShopIdLv1() {
        return shopIdLv1;
    }

    public void setShopIdLv1(Long shopIdLv1) {
        this.shopIdLv1 = shopIdLv1;
    }

    public String getShopCodeLv1() {
        return shopCodeLv1;
    }

    public void setShopCodeLv1(String shopCodeLv1) {
        this.shopCodeLv1 = shopCodeLv1;
    }

    public String getShopNameLv1() {
        return shopNameLv1;
    }

    public void setShopNameLv1(String shopNameLv1) {
        this.shopNameLv1 = shopNameLv1;
    }

    public Long getShopIdLv2() {
        return shopIdLv2;
    }

    public void setShopIdLv2(Long shopIdLv2) {
        this.shopIdLv2 = shopIdLv2;
    }

    public String getShopCodeLv2() {
        return shopCodeLv2;
    }

    public void setShopCodeLv2(String shopCodeLv2) {
        this.shopCodeLv2 = shopCodeLv2;
    }

    public String getShopNameLv2() {
        return shopNameLv2;
    }

    public void setShopNameLv2(String shopNameLv2) {
        this.shopNameLv2 = shopNameLv2;
    }

    public Long getShopIdLv3() {
        return shopIdLv3;
    }

    public void setShopIdLv3(Long shopIdLv3) {
        this.shopIdLv3 = shopIdLv3;
    }

    public String getShopCodeLv3() {
        return shopCodeLv3;
    }

    public void setShopCodeLv3(String shopCodeLv3) {
        this.shopCodeLv3 = shopCodeLv3;
    }

    public String getShopNameLv3() {
        return shopNameLv3;
    }

    public void setShopNameLv3(String shopNameLv3) {
        this.shopNameLv3 = shopNameLv3;
    }

    public Long getShopIdLv4() {
        return shopIdLv4;
    }

    public void setShopIdLv4(Long shopIdLv4) {
        this.shopIdLv4 = shopIdLv4;
    }

    public String getShopCodeLv4() {
        return shopCodeLv4;
    }

    public void setShopCodeLv4(String shopCodeLv4) {
        this.shopCodeLv4 = shopCodeLv4;
    }

    public String getShopNameLv4() {
        return shopNameLv4;
    }

    public void setShopNameLv4(String shopNameLv4) {
        this.shopNameLv4 = shopNameLv4;
    }

    public Long getShopIdLv5() {
        return shopIdLv5;
    }

    public void setShopIdLv5(Long shopIdLv5) {
        this.shopIdLv5 = shopIdLv5;
    }

    public String getShopCodeLv5() {
        return shopCodeLv5;
    }

    public void setShopCodeLv5(String shopCodeLv5) {
        this.shopCodeLv5 = shopCodeLv5;
    }

    public String getShopNameLv5() {
        return shopNameLv5;
    }

    public void setShopNameLv5(String shopNameLv5) {
        this.shopNameLv5 = shopNameLv5;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopIdLv6() {
        return shopIdLv6;
    }

    public void setShopIdLv6(Long shopIdLv6) {
        this.shopIdLv6 = shopIdLv6;
    }

    public String getShopCodeLv6() {
        return shopCodeLv6;
    }

    public void setShopCodeLv6(String shopCodeLv6) {
        this.shopCodeLv6 = shopCodeLv6;
    }

    public String getShopNameLv6() {
        return shopNameLv6;
    }

    public void setShopNameLv6(String shopNameLv6) {
        this.shopNameLv6 = shopNameLv6;
    }

    public Long getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Long synStatus) {
        this.synStatus = synStatus;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAvailableQuantityAf() {
        return availableQuantityAf;
    }

    public void setAvailableQuantityAf(Long availableQuantityAf) {
        this.availableQuantityAf = availableQuantityAf;
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
        if (!(object instanceof StockTotalAudit)) {
            return false;
        }
        StockTotalAudit other = (StockTotalAudit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTotalAudit[ id=" + id + " ]";
    }
    
}

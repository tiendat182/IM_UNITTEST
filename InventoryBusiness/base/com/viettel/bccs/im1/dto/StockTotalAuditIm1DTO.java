package com.viettel.bccs.im1.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class StockTotalAuditIm1DTO extends BaseDTO implements Serializable {
   private Long ownerId;
    private String ownerCode;
    private String ownerName;
    private Long ownerType;
    private Long userId;
    private String userCode;
    private String userName;
    private Long stateId;
    private Long stockModelId;
    private String stockModelName;
    private Date createDate;
    private Long status;
    private Long qty;
    private Long qtyIssue;
    private Long qtyHang;
    private Long qtyBf;
    private Long qtyIssueBf;
    private Long qtyHangBf;
    private Long qtyAf;
    private Long qtyIssueAf;
    private Long qtyHangAf;
    private Long transId;
    private String transCode;
    private Long transType;
    private Long sourceType;
    private String stickCode;
    private String reasonName;
    private Long reasonId;
    private String description;
    private Long shopIdLv1;
    private String shopCodeLv1;
    private String shopNameLv1;
    private Long shopIdLv2;
    private String shopCodeLv2;
    private String shopNameLv2;
    private Long shopIdLv3;
    private String shopCodeLv3;
    private String shopNameLv3;
    private Long shopIdLv4;
    private String shopCodeLv4;
    private String shopNameLv4;
    private Long shopIdLv5;
    private String shopCodeLv5;
    private String shopNameLv5;
    private Long shopId;
    private String shopCode;
    private String shopName;
    private Long shopIdLv6;
    private String shopCodeLv6;
    private String shopNameLv6;
    private Long synStatus;
    private String stockModelCode;
    private Long stockTypeId;
    private Long valid;
    public String getKeySet() {
        return keySet;
    }
    public Long getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerCode() {
        return this.ownerCode;
    }
    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
    public String getOwnerName() {
        return this.ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public Long getOwnerType() {
        return this.ownerType;
    }
    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserCode() {
        return this.userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public Long getStockModelId() {
        return this.stockModelId;
    }
    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }
    public String getStockModelName() {
        return this.stockModelName;
    }
    public void setStockModelName(String stockModelName) {
        this.stockModelName = stockModelName;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getQty() {
        return this.qty;
    }
    public void setQty(Long qty) {
        this.qty = qty;
    }
    public Long getQtyIssue() {
        return this.qtyIssue;
    }
    public void setQtyIssue(Long qtyIssue) {
        this.qtyIssue = qtyIssue;
    }
    public Long getQtyHang() {
        return this.qtyHang;
    }
    public void setQtyHang(Long qtyHang) {
        this.qtyHang = qtyHang;
    }
    public Long getQtyBf() {
        return this.qtyBf;
    }
    public void setQtyBf(Long qtyBf) {
        this.qtyBf = qtyBf;
    }
    public Long getQtyIssueBf() {
        return this.qtyIssueBf;
    }
    public void setQtyIssueBf(Long qtyIssueBf) {
        this.qtyIssueBf = qtyIssueBf;
    }
    public Long getQtyHangBf() {
        return this.qtyHangBf;
    }
    public void setQtyHangBf(Long qtyHangBf) {
        this.qtyHangBf = qtyHangBf;
    }
    public Long getQtyAf() {
        return this.qtyAf;
    }
    public void setQtyAf(Long qtyAf) {
        this.qtyAf = qtyAf;
    }
    public Long getQtyIssueAf() {
        return this.qtyIssueAf;
    }
    public void setQtyIssueAf(Long qtyIssueAf) {
        this.qtyIssueAf = qtyIssueAf;
    }
    public Long getQtyHangAf() {
        return this.qtyHangAf;
    }
    public void setQtyHangAf(Long qtyHangAf) {
        this.qtyHangAf = qtyHangAf;
    }
    public Long getTransId() {
        return this.transId;
    }
    public void setTransId(Long transId) {
        this.transId = transId;
    }
    public String getTransCode() {
        return this.transCode;
    }
    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }
    public Long getTransType() {
        return this.transType;
    }
    public void setTransType(Long transType) {
        this.transType = transType;
    }
    public Long getSourceType() {
        return this.sourceType;
    }
    public void setSourceType(Long sourceType) {
        this.sourceType = sourceType;
    }
    public String getStickCode() {
        return this.stickCode;
    }
    public void setStickCode(String stickCode) {
        this.stickCode = stickCode;
    }
    public String getReasonName() {
        return this.reasonName;
    }
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
    public Long getReasonId() {
        return this.reasonId;
    }
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getShopIdLv1() {
        return this.shopIdLv1;
    }
    public void setShopIdLv1(Long shopIdLv1) {
        this.shopIdLv1 = shopIdLv1;
    }
    public String getShopCodeLv1() {
        return this.shopCodeLv1;
    }
    public void setShopCodeLv1(String shopCodeLv1) {
        this.shopCodeLv1 = shopCodeLv1;
    }
    public String getShopNameLv1() {
        return this.shopNameLv1;
    }
    public void setShopNameLv1(String shopNameLv1) {
        this.shopNameLv1 = shopNameLv1;
    }
    public Long getShopIdLv2() {
        return this.shopIdLv2;
    }
    public void setShopIdLv2(Long shopIdLv2) {
        this.shopIdLv2 = shopIdLv2;
    }
    public String getShopCodeLv2() {
        return this.shopCodeLv2;
    }
    public void setShopCodeLv2(String shopCodeLv2) {
        this.shopCodeLv2 = shopCodeLv2;
    }
    public String getShopNameLv2() {
        return this.shopNameLv2;
    }
    public void setShopNameLv2(String shopNameLv2) {
        this.shopNameLv2 = shopNameLv2;
    }
    public Long getShopIdLv3() {
        return this.shopIdLv3;
    }
    public void setShopIdLv3(Long shopIdLv3) {
        this.shopIdLv3 = shopIdLv3;
    }
    public String getShopCodeLv3() {
        return this.shopCodeLv3;
    }
    public void setShopCodeLv3(String shopCodeLv3) {
        this.shopCodeLv3 = shopCodeLv3;
    }
    public String getShopNameLv3() {
        return this.shopNameLv3;
    }
    public void setShopNameLv3(String shopNameLv3) {
        this.shopNameLv3 = shopNameLv3;
    }
    public Long getShopIdLv4() {
        return this.shopIdLv4;
    }
    public void setShopIdLv4(Long shopIdLv4) {
        this.shopIdLv4 = shopIdLv4;
    }
    public String getShopCodeLv4() {
        return this.shopCodeLv4;
    }
    public void setShopCodeLv4(String shopCodeLv4) {
        this.shopCodeLv4 = shopCodeLv4;
    }
    public String getShopNameLv4() {
        return this.shopNameLv4;
    }
    public void setShopNameLv4(String shopNameLv4) {
        this.shopNameLv4 = shopNameLv4;
    }
    public Long getShopIdLv5() {
        return this.shopIdLv5;
    }
    public void setShopIdLv5(Long shopIdLv5) {
        this.shopIdLv5 = shopIdLv5;
    }
    public String getShopCodeLv5() {
        return this.shopCodeLv5;
    }
    public void setShopCodeLv5(String shopCodeLv5) {
        this.shopCodeLv5 = shopCodeLv5;
    }
    public String getShopNameLv5() {
        return this.shopNameLv5;
    }
    public void setShopNameLv5(String shopNameLv5) {
        this.shopNameLv5 = shopNameLv5;
    }
    public Long getShopId() {
        return this.shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public String getShopCode() {
        return this.shopCode;
    }
    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
    public String getShopName() {
        return this.shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public Long getShopIdLv6() {
        return this.shopIdLv6;
    }
    public void setShopIdLv6(Long shopIdLv6) {
        this.shopIdLv6 = shopIdLv6;
    }
    public String getShopCodeLv6() {
        return this.shopCodeLv6;
    }
    public void setShopCodeLv6(String shopCodeLv6) {
        this.shopCodeLv6 = shopCodeLv6;
    }
    public String getShopNameLv6() {
        return this.shopNameLv6;
    }
    public void setShopNameLv6(String shopNameLv6) {
        this.shopNameLv6 = shopNameLv6;
    }
    public Long getSynStatus() {
        return this.synStatus;
    }
    public void setSynStatus(Long synStatus) {
        this.synStatus = synStatus;
    }
    public String getStockModelCode() {
        return this.stockModelCode;
    }
    public void setStockModelCode(String stockModelCode) {
        this.stockModelCode = stockModelCode;
    }
    public Long getStockTypeId() {
        return this.stockTypeId;
    }
    public void setStockTypeId(Long stockTypeId) {
        this.stockTypeId = stockTypeId;
    }
    public Long getValid() {
        return this.valid;
    }
    public void setValid(Long valid) {
        this.valid = valid;
    }
}

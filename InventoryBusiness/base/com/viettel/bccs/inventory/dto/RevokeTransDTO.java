package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class RevokeTransDTO extends BaseDTO implements Serializable {

    private String accountId;
    private Long amountNeedRevoke;
    private Long amountRevoked;
    private String cmRequest;
    private String contractNo;
    private Date createDate;
    private Long posId;
    private Long revokeTransId;
    private Date saleTransDate;
    private Long saleTransId;
    private Long shopId;
    private Long staffId;
    private String status;
    private Long stockTransId;
    private Long telecomServiceId;

    public String getKeySet() {
        return keySet;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getAmountNeedRevoke() {
        return this.amountNeedRevoke;
    }

    public void setAmountNeedRevoke(Long amountNeedRevoke) {
        this.amountNeedRevoke = amountNeedRevoke;
    }

    public Long getAmountRevoked() {
        return this.amountRevoked;
    }

    public void setAmountRevoked(Long amountRevoked) {
        this.amountRevoked = amountRevoked;
    }

    public String getCmRequest() {
        return this.cmRequest;
    }

    public void setCmRequest(String cmRequest) {
        this.cmRequest = cmRequest;
    }

    public String getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getPosId() {
        return this.posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public Long getRevokeTransId() {
        return this.revokeTransId;
    }

    public void setRevokeTransId(Long revokeTransId) {
        this.revokeTransId = revokeTransId;
    }

    public Date getSaleTransDate() {
        return this.saleTransDate;
    }

    public void setSaleTransDate(Date saleTransDate) {
        this.saleTransDate = saleTransDate;
    }

    public Long getSaleTransId() {
        return this.saleTransId;
    }

    public void setSaleTransId(Long saleTransId) {
        this.saleTransId = saleTransId;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockTransId() {
        return this.stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }
}

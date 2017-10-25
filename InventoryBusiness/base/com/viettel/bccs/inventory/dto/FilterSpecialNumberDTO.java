package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class FilterSpecialNumberDTO extends BaseDTO implements Serializable {
    private Date createDatetime;
    private Long filterRuleId;
    private Long filterSpecialNumberId;
    private String isdn;
    private String status;
    private Long prodOfferId;
    private String productName;
    private String productCode;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getFilterRuleId() {
        return this.filterRuleId;
    }

    public void setFilterRuleId(Long filterRuleId) {
        this.filterRuleId = filterRuleId;
    }

    public Long getFilterSpecialNumberId() {
        return this.filterSpecialNumberId;
    }

    public void setFilterSpecialNumberId(Long filterSpecialNumberId) {
        this.filterSpecialNumberId = filterSpecialNumberId;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FilterSpecialNumberDTO) {
            if (filterSpecialNumberId != null && ((FilterSpecialNumberDTO) obj).filterSpecialNumberId != null && filterSpecialNumberId.equals(((FilterSpecialNumberDTO) obj).filterSpecialNumberId)
                    && filterRuleId != null && ((FilterSpecialNumberDTO) obj).filterRuleId != null && filterRuleId.equals(((FilterSpecialNumberDTO) obj).filterRuleId)
                    && isdn != null && ((FilterSpecialNumberDTO) obj).isdn != null && isdn.equals(((FilterSpecialNumberDTO) obj).isdn)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * dto hung data tu webservice cua KTTS
 * @author thanhnt77
 */
public class PartnerContractWsDTO extends BaseDTO {

    private Date fromDate;
    private Date toDate;
    private Date cntSignDate;
    private String fromDateStr;
    private String toDateStr;
    private String contractCode;
    private String currencyCode;
    private String partnerName;

    public PartnerContractWsDTO() {
    }

    public PartnerContractWsDTO(Date fromDate, Date toDate, String contractCode) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromDateStr = DateUtil.date2ddMMyyyyString(fromDate);
        this.toDateStr = DateUtil.date2ddMMyyyyString(toDate);
        this.contractCode = contractCode;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCntSignDate() {
        return cntSignDate;
    }

    public void setCntSignDate(Date cntSignDate) {
        this.cntSignDate = cntSignDate;
    }

    public String getFromDateStr() {
        return fromDateStr;
    }

    public void setFromDateStr(String fromDateStr) {
        this.fromDateStr = fromDateStr;
    }

    public String getToDateStr() {
        return toDateStr;
    }

    public void setToDateStr(String toDateStr) {
        this.toDateStr = toDateStr;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}

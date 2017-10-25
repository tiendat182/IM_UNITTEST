package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class PstnIsdnExchangeDTO extends BaseDTO implements Serializable {

    private Date createdate;
    private Long exchangeId;
    private String poCode;
    private String prefix;
    private Long pstnIsdnExchangeId;
    private String type;

    public String getKeySet() {
        return keySet; }
    public Date getCreatedate() {
        return this.createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
    public Long getExchangeId() {
        return this.exchangeId;
    }
    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
    public String getPoCode() {
        return this.poCode;
    }
    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }
    public String getPrefix() {
        return this.prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public Long getPstnIsdnExchangeId() {
        return this.pstnIsdnExchangeId;
    }
    public void setPstnIsdnExchangeId(Long pstnIsdnExchangeId) {
        this.pstnIsdnExchangeId = pstnIsdnExchangeId;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
}

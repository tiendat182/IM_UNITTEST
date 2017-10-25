package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RequestLiquidateDetailDTO extends BaseDTO implements Serializable {

    private Date createDatetime;
    private Long prodOfferId;
    private Long quantity;
    private Long requestLiquidateDetailId;
    private Long requestLiquidateId;
    private Long stateId;
    private String checkSerial;
    private List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO;

    public String getKeySet() {
        return keySet;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getRequestLiquidateDetailId() {
        return this.requestLiquidateDetailId;
    }

    public void setRequestLiquidateDetailId(Long requestLiquidateDetailId) {
        this.requestLiquidateDetailId = requestLiquidateDetailId;
    }

    public Long getRequestLiquidateId() {
        return requestLiquidateId;
    }

    public void setRequestLiquidateId(Long requestLiquidateId) {
        this.requestLiquidateId = requestLiquidateId;
    }

    public Long getStateId() {
        return this.stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public List<RequestLiquidateSerialDTO> getLstRequestLiquidateSerialDTO() {
        return lstRequestLiquidateSerialDTO;
    }

    public void setLstRequestLiquidateSerialDTO(List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO) {
        this.lstRequestLiquidateSerialDTO = lstRequestLiquidateSerialDTO;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }
}

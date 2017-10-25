package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * dto hung data tu webservice cua KTTS
 * @author thanhnt77
 */
public class PartnerShipmentResultWsDTO extends BaseDTO {

    private String reason;
    private String status;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

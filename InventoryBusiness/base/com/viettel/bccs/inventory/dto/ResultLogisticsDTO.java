package com.viettel.bccs.inventory.dto;

/**
 * Created by hoangnt14 on 7/15/2016.
 */
public class ResultLogisticsDTO {
    private String responseCode;
    private String description;
    private String actionCode;
    private String status;
    private String lstTransId;
    private String orderAction;

    public ResultLogisticsDTO() {
    }

    public ResultLogisticsDTO(String responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public String getOrderAction() {
        return orderAction;
    }

    public void setOrderAction(String orderAction) {
        this.orderAction = orderAction;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLstTransId() {
        return lstTransId;
    }

    public void setLstTransId(String lstTransId) {
        this.lstTransId = lstTransId;
    }
}

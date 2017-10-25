package com.viettel.bccs.anypay.dto;

/**
 * Created by PM2-LAMNV5 on 2/16/2016.
 */
public class AgentDTO {
    private Long agentId;
    private Long staffStkId;
    private String msisdn;
    private String iccid;
    private String tradeName;
    private Long status;

    public Long getAgentId() {
        return agentId;
    }

    public void  setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStaffStkId() {
        return staffStkId;
    }

    public void setStaffStkId(Long staffStkId) {
        this.staffStkId = staffStkId;
    }
}

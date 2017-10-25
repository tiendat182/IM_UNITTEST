package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class SignFlowDetailDTO extends BaseDTO implements Serializable {

    private String email;
    private Long signFlowDetailId;
    private Long signFlowId;
    private Long signOrder;
    private String status;
    private Long vofficeRoleId;

    public String getKeySet() {
        return keySet; }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getSignFlowDetailId() {
        return this.signFlowDetailId;
    }
    public void setSignFlowDetailId(Long signFlowDetailId) {
        this.signFlowDetailId = signFlowDetailId;
    }
    public Long getSignFlowId() {
        return this.signFlowId;
    }
    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }
    public Long getSignOrder() {
        return this.signOrder;
    }
    public void setSignOrder(Long signOrder) {
        this.signOrder = signOrder;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getVofficeRoleId() {
        return this.vofficeRoleId;
    }
    public void setVofficeRoleId(Long vofficeRoleId) {
        this.vofficeRoleId = vofficeRoleId;
    }
}

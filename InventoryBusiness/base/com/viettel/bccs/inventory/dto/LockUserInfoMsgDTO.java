/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.dto;

/**
 *
 * @author thanhnt77
 */
public class LockUserInfoMsgDTO {

    private Long num;
    private String transLst;
    private String warningContent;
    private String redirectUrl;
    private String actionUrl;
    private Long lockTypeId;
    private String transCodeLst;
    private String transMessList;

    public String getTransMessList() {
        return transMessList;
    }

    public void setTransMessList(String transMessList) {
        this.transMessList = transMessList;
    }

    public String getTransCodeLst() {
        return transCodeLst;
    }

    public void setTransCodeLst(String transCodeLst) {
        this.transCodeLst = transCodeLst;
    }

    public Long getLockTypeId() {
        return lockTypeId;
    }

    public void setLockTypeId(Long lockTypeId) {
        this.lockTypeId = lockTypeId;
    }

    public LockUserInfoMsgDTO() {
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getTransLst() {
        return transLst;
    }

    public void setTransLst(String transLst) {
        this.transLst = transLst;
    }

    public String getWarningContent() {
        return warningContent;
    }

    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }
}

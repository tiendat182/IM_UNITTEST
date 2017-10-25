package com.viettel.bccs.inventory.dto;
import java.lang.Long;
import java.util.Date;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class ActiveKitVofficeDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private String accountName;
    private String accountPass;
    private String actionCode;
    private Long activeKitVofficeId;
    private Long changeModelTransId;
    private Date createDate;
    private Long createUserId;
    private Date lastModify;
    private String note;
    private String prefixTemplate;
    private String signUserList;
    private String status;
    private Long stockTransActionId;
    public String getAccountName() {
        return this.accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountPass() {
        return this.accountPass;
    }
    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }
    public String getActionCode() {
        return this.actionCode;
    }
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    public Long getActiveKitVofficeId() {
        return this.activeKitVofficeId;
    }
    public void setActiveKitVofficeId(Long activeKitVofficeId) {
        this.activeKitVofficeId = activeKitVofficeId;
    }
    public Long getChangeModelTransId() {
        return this.changeModelTransId;
    }
    public void setChangeModelTransId(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getCreateUserId() {
        return this.createUserId;
    }
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public Date getLastModify() {
        return this.lastModify;
    }
    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getPrefixTemplate() {
        return this.prefixTemplate;
    }
    public void setPrefixTemplate(String prefixTemplate) {
        this.prefixTemplate = prefixTemplate;
    }
    public String getSignUserList() {
        return this.signUserList;
    }
    public void setSignUserList(String signUserList) {
        this.signUserList = signUserList;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}

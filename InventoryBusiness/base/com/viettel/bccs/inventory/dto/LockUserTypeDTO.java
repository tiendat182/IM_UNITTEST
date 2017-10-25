package com.viettel.bccs.inventory.dto;
import java.lang.Character;
import java.lang.Long;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
public class LockUserTypeDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private String actionUrl;
    private Character auto;
    private Long checkRange;
    private String description;
    private String effectRole;
    private Long id;
    private String name;
    private String params;
    private String redirectUrl;
    private String sqlCheckTrans;
    private String sqlCmd;
    private Character status;
    private Long validRange;
    private String warningContent;
    public String getActionUrl() {
        return this.actionUrl;
    }
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    public Character getAuto() {
        return this.auto;
    }
    public void setAuto(Character auto) {
        this.auto = auto;
    }
    public Long getCheckRange() {
        return this.checkRange;
    }
    public void setCheckRange(Long checkRange) {
        this.checkRange = checkRange;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getEffectRole() {
        return this.effectRole;
    }
    public void setEffectRole(String effectRole) {
        this.effectRole = effectRole;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getParams() {
        return this.params;
    }
    public void setParams(String params) {
        this.params = params;
    }
    public String getRedirectUrl() {
        return this.redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public String getSqlCheckTrans() {
        return this.sqlCheckTrans;
    }
    public void setSqlCheckTrans(String sqlCheckTrans) {
        this.sqlCheckTrans = sqlCheckTrans;
    }
    public String getSqlCmd() {
        return this.sqlCmd;
    }
    public void setSqlCmd(String sqlCmd) {
        this.sqlCmd = sqlCmd;
    }
    public Character getStatus() {
        return this.status;
    }
    public void setStatus(Character status) {
        this.status = status;
    }
    public Long getValidRange() {
        return this.validRange;
    }
    public void setValidRange(Long validRange) {
        this.validRange = validRange;
    }
    public String getWarningContent() {
        return this.warningContent;
    }
    public void setWarningContent(String warningContent) {
        this.warningContent = warningContent;
    }
}

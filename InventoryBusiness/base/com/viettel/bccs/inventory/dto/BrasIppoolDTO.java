package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class BrasIppoolDTO extends BaseDTO implements Serializable {

    private Long brasIp;
    private String center;
    private Long domain;
    private String ipLabel;
    private String ipMask;
    private String ipPool;
    private String ipType;
    private Long poolId;
    private String poolName;
    private String poolUniq;
    private String province;
    private String status;
    private Date createDatetime;
    private String createUser;
    private Date updateDatetime;
    private String updateUser;
    private Long rowIndex;
    private Long brasId;
    private Long domainId;
    private String provinceName;
    private boolean specificIp;
    private String msgError;
    private String strDateUpdate;
    private String strBrasIp;
    private String strDomain;
    private String updateDatetimeStr;
    private String statusStr;
    private boolean exportExcel;
    private String isdn;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public boolean isExportExcel() {
        return exportExcel;
    }

    public void setExportExcel(boolean exportExcel) {
        this.exportExcel = exportExcel;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getUpdateDatetimeStr() {
        return updateDatetimeStr;
    }

    public void setUpdateDatetimeStr(String updateDatetimeStr) {
        this.updateDatetimeStr = updateDatetimeStr;
    }

    public BrasIppoolDTO() {
    }

    public String getKeySet() {
        return keySet;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public BrasIppoolDTO(boolean specificIp) {
        this.specificIp = specificIp;
    }

    public boolean isSpecificIp() {
        return specificIp;
    }

    public void setSpecificIp(boolean specificIp) {
        this.specificIp = specificIp;
    }

    public Long getBrasId() {
        return brasId;
    }

    public void setBrasId(Long brasId) {
        this.brasId = brasId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getBrasIp() {
        return this.brasIp;
    }

    public void setBrasIp(Long brasIp) {
        this.brasIp = brasIp;
    }

    public String getCenter() {
        return this.center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getIpLabel() {
        return this.ipLabel;
    }

    public void setIpLabel(String ipLabel) {
        this.ipLabel = ipLabel;
    }

    public String getIpMask() {
        return this.ipMask;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public String getIpPool() {
        return this.ipPool;
    }

    public void setIpPool(String ipPool) {
        this.ipPool = ipPool;
    }

    public String getIpType() {
        return this.ipType;
    }

    public void setIpType(String ipType) {
        this.ipType = ipType;
    }

    public Long getPoolId() {
        return this.poolId;
    }

    public void setPoolId(Long poolId) {
        this.poolId = poolId;
    }

    public String getPoolName() {
        return this.poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolUniq() {
        return this.poolUniq;
    }

    public void setPoolUniq(String poolUniq) {
        this.poolUniq = poolUniq;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Long rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getStrDateUpdate() {
        return strDateUpdate;
    }

    public void setStrDateUpdate(String strDateUpdate) {
        this.strDateUpdate = strDateUpdate;
    }

    public Long getDomain() {
        return domain;
    }

    public void setDomain(Long domain) {
        this.domain = domain;
    }

    public String getStrBrasIp() {
        return strBrasIp;
    }

    public void setStrBrasIp(String strBrasIp) {
        this.strBrasIp = strBrasIp;
    }

    public String getStrDomain() {
        return strDomain;
    }

    public void setStrDomain(String strDomain) {
        this.strDomain = strDomain;
    }
}

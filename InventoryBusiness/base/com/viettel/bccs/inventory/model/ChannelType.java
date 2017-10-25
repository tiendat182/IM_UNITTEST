/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CHANNEL_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChannelType.findAll", query = "SELECT c FROM ChannelType c")})
public class ChannelType implements Serializable {
public static enum COLUMNS {ALLOWADDBATCH, CHANNELTYPEID, CHECKCOMM, CODE, CREATEDATETIME, CREATEUSER, DISCOUNTPOLICYDEFAUT, GROUPCHANNELID, GROUPCHANNELTYPEID, ISCOLLCHANNEL, ISNOTBLANKCODE, ISVHRCHANNEL, ISVTUNIT, NAME, OBJECTTYPE, PRICEPOLICYDEFAUT, STATUS, STOCKREPORTTEMPLATE, STOCKTYPE, SUFFIXOBJECTCODE, TOTALDEBIT, UPDATEBLANKCODEROLE, UPDATEDATETIME, UPDATEOBJECTINFOROLE, UPDATESHOPROLE, UPDATESTAFFOWNERROLE, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private Long status = 0L;
    @Column(name = "OBJECT_TYPE")
    private String objectType;
    @Column(name = "IS_VT_UNIT")
    private String isVtUnit;
    @Column(name = "CHECK_COMM")
    private String checkComm;
    @Column(name = "STOCK_TYPE")
    private Long stockType;
    @Column(name = "STOCK_REPORT_TEMPLATE")
    private String stockReportTemplate;
    @Column(name = "TOTAL_DEBIT")
    private Long totalDebit;
    @Column(name = "ALLOW_ADD_BATCH")
    private Long allowAddBatch;
    @Column(name = "SUFFIX_OBJECT_CODE")
    private String suffixObjectCode;
    @Column(name = "UPDATE_STAFF_OWNER_ROLE")
    private String updateStaffOwnerRole;
    @Column(name = "DISCOUNT_POLICY_DEFAUT")
    private String discountPolicyDefaut;
    @Column(name = "PRICE_POLICY_DEFAUT")
    private String pricePolicyDefaut;
    @Column(name = "UPDATE_BLANK_CODE_ROLE")
    private String updateBlankCodeRole;
    @Column(name = "UPDATE_OBJECT_INFO_ROLE")
    private String updateObjectInfoRole;
    @Column(name = "UPDATE_SHOP_ROLE")
    private String updateShopRole;
    @Column(name = "CODE")
    private String code;
    @Column(name = "GROUP_CHANNEL_TYPE_ID")
    private Long groupChannelTypeId;
    @Column(name = "GROUP_CHANNEL_ID")
    private Long groupChannelId;
    @Column(name = "IS_COLL_CHANNEL")
    private Long isCollChannel;
    @Column(name = "IS_NOT_BLANK_CODE")
    private Long isNotBlankCode;
    @Column(name = "IS_VHR_CHANNEL")
    private Long isVhrChannel;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;

    public ChannelType() {
    }

    public ChannelType(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public ChannelType(Long channelTypeId, String name, Long status) {
        this.channelTypeId = channelTypeId;
        this.name = name;
        this.status = status;
    }

    public Long getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(Long channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getIsVtUnit() {
        return isVtUnit;
    }

    public void setIsVtUnit(String isVtUnit) {
        this.isVtUnit = isVtUnit;
    }

    public String getCheckComm() {
        return checkComm;
    }

    public void setCheckComm(String checkComm) {
        this.checkComm = checkComm;
    }

    public Long getStockType() {
        return stockType;
    }

    public void setStockType(Long stockType) {
        this.stockType = stockType;
    }

    public String getStockReportTemplate() {
        return stockReportTemplate;
    }

    public void setStockReportTemplate(String stockReportTemplate) {
        this.stockReportTemplate = stockReportTemplate;
    }

    public Long getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Long totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Long getAllowAddBatch() {
        return allowAddBatch;
    }

    public void setAllowAddBatch(Long allowAddBatch) {
        this.allowAddBatch = allowAddBatch;
    }

    public String getSuffixObjectCode() {
        return suffixObjectCode;
    }

    public void setSuffixObjectCode(String suffixObjectCode) {
        this.suffixObjectCode = suffixObjectCode;
    }

    public String getUpdateStaffOwnerRole() {
        return updateStaffOwnerRole;
    }

    public void setUpdateStaffOwnerRole(String updateStaffOwnerRole) {
        this.updateStaffOwnerRole = updateStaffOwnerRole;
    }

    public String getDiscountPolicyDefaut() {
        return discountPolicyDefaut;
    }

    public void setDiscountPolicyDefaut(String discountPolicyDefaut) {
        this.discountPolicyDefaut = discountPolicyDefaut;
    }

    public String getPricePolicyDefaut() {
        return pricePolicyDefaut;
    }

    public void setPricePolicyDefaut(String pricePolicyDefaut) {
        this.pricePolicyDefaut = pricePolicyDefaut;
    }

    public String getUpdateBlankCodeRole() {
        return updateBlankCodeRole;
    }

    public void setUpdateBlankCodeRole(String updateBlankCodeRole) {
        this.updateBlankCodeRole = updateBlankCodeRole;
    }

    public String getUpdateObjectInfoRole() {
        return updateObjectInfoRole;
    }

    public void setUpdateObjectInfoRole(String updateObjectInfoRole) {
        this.updateObjectInfoRole = updateObjectInfoRole;
    }

    public String getUpdateShopRole() {
        return updateShopRole;
    }

    public void setUpdateShopRole(String updateShopRole) {
        this.updateShopRole = updateShopRole;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getGroupChannelTypeId() {
        return groupChannelTypeId;
    }

    public void setGroupChannelTypeId(Long groupChannelTypeId) {
        this.groupChannelTypeId = groupChannelTypeId;
    }

    public Long getGroupChannelId() {
        return groupChannelId;
    }

    public void setGroupChannelId(Long groupChannelId) {
        this.groupChannelId = groupChannelId;
    }

    public Long getIsCollChannel() {
        return isCollChannel;
    }

    public void setIsCollChannel(Long isCollChannel) {
        this.isCollChannel = isCollChannel;
    }

    public Long getIsNotBlankCode() {
        return isNotBlankCode;
    }

    public void setIsNotBlankCode(Long isNotBlankCode) {
        this.isNotBlankCode = isNotBlankCode;
    }

    public Long getIsVhrChannel() {
        return isVhrChannel;
    }

    public void setIsVhrChannel(Long isVhrChannel) {
        this.isVhrChannel = isVhrChannel;
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (channelTypeId != null ? channelTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChannelType)) {
            return false;
        }
        ChannelType other = (ChannelType) object;
        if ((this.channelTypeId == null && other.channelTypeId != null) || (this.channelTypeId != null && !this.channelTypeId.equals(other.channelTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ChannelType[ channelTypeId=" + channelTypeId + " ]";
    }
    
}

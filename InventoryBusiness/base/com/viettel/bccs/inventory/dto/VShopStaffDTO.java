package com.viettel.bccs.inventory.dto;

import com.google.common.base.Joiner;
import com.viettel.fw.common.util.DataUtil;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by minhvh1 on 2015-05-08.
 */
public class VShopStaffDTO implements java.io.Serializable {
    public static final String TYPE_SHOP = "1";
    public static final String TYPE_STAFF = "2";
    @JsonProperty("owner_id")
    private String ownerId;
    @JsonProperty("owner_type")
    private String ownerType;
    @JsonProperty("owner_name")
    private String ownerName;
    @JsonProperty("owner_address")
    private String ownerAddress;
    @JsonProperty("channel_type_id")
    private String channelTypeId;
    @JsonProperty("parent_shop_id")
    private String parentShopId;
    @JsonProperty("owner_code")
    private String ownerCode;
    @JsonProperty("status")
    private String status;
    @JsonProperty("shop_path")
    private String shopPath;
    @JsonProperty("table_pk")
    private String tablePk;
    @JsonProperty("staff_owner_id")
    private String staffOwnerId;

    public VShopStaffDTO() {
    }

    public VShopStaffDTO(StaffDTO staffDTO) {
        if (staffDTO != null) {
            ownerId = DataUtil.safeToString(staffDTO.getStaffId());
            ownerCode = staffDTO.getStaffCode();
            ownerName = staffDTO.getName();
            ownerType = TYPE_STAFF;
            channelTypeId = DataUtil.safeToString(staffDTO.getChannelTypeId());
            status = staffDTO.getStatus();
            shopPath = null;
            parentShopId = DataUtil.safeToString(staffDTO.getShopId());
            staffOwnerId = DataUtil.safeToString(staffDTO.getStaffOwnerId());
        }
    }

    public String getItemLabel() {
        String result = "";
        if (!DataUtil.isNullOrEmpty(ownerCode) && !DataUtil.isNullOrEmpty(ownerName)) {
            result = ownerCode + "-" + ownerName;
        }
        return result;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(String channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getParentShopId() {
        return parentShopId;
    }

    public void setParentShopId(String parentShopId) {
        this.parentShopId = parentShopId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopPath() {
        return shopPath;
    }

    public void setShopPath(String shopPath) {
        this.shopPath = shopPath;
    }

    public String getTablePk() {
        return tablePk;
    }

    public void setTablePk(String tablePk) {
        this.tablePk = tablePk;
    }


    public String getStaffOwnerId() {
        return staffOwnerId;
    }

    public void setStaffOwnerId(String staffOwnerId) {
        this.staffOwnerId = staffOwnerId;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    @Override
    public String toString() {
        /*String infor = "";
        if (VShopStaffDTO.TYPE_SHOP.equals(ownerType)) {
            infor = "Đại lý:"; // "common.sale.shop";
        } else if(VShopStaffDTO.TYPE_STAFF.equals(ownerType)){
            infor = "Nhân viên:"; // "common.sale.staff";
        }*/
        String ret = Joiner.on(" - ").skipNulls().join(ownerCode, ownerName);
        return ret;
    }

    public String getCustomLabel() {
        return Joiner.on(" - ").skipNulls().join(ownerCode, ownerName);
    }

}

package com.viettel.bccs.inventory.dto;

import java.lang.Long;
import java.util.Date;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class StockRequestOrderDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private Date createDatetime;
    private String createUser;
    private String description;
    private String orderCode;
    private Long orderType;
    private Long ownerId;
    private Long ownerType;
    private String ownerFullName;
    private String status;
    private String statusName;
    private Long stockRequestOrderId;
    private Date updateDatetime;
    private String updateUser;
    private Date fromDate;
    private Date endDate;
    private List<StockRequestOrderDetailDTO> lsRequestOrderDetailDTO;
    private SignOfficeDTO signOfficeDTO;
    private Long actionStaffId;
    private Long retry;
    private String errorCode;
    private String errorCodeDescription;

    public Long getRetry() {
        return retry;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDescription() {
        return errorCodeDescription;
    }

    public void setErrorCodeDescription(String errorCodeDescription) {
        this.errorCodeDescription = errorCodeDescription;
    }

    public void createOrderFromImport(List<ProductOfferingTotalDTO> lsImport, StaffDTO staffDTO) {
        ownerType = Const.OWNER_TYPE.SHOP_LONG;
        ownerId = staffDTO.getShopId();
        createUser = staffDTO.getStaffCode();
        updateUser = staffDTO.getStaffCode();
        lsRequestOrderDetailDTO = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lsImport)) {
            for (ProductOfferingTotalDTO offeringDTO : lsImport) {
                StockRequestOrderDetailDTO orderDetailDTO = new StockRequestOrderDetailDTO();
                orderDetailDTO.setFromOwnerId(offeringDTO.getOwnerId());
                orderDetailDTO.setFromOwnerType(DataUtil.safeToLong(offeringDTO.getOwnerType()));
                orderDetailDTO.setToOwnerId(staffDTO.getShopId());
                orderDetailDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                orderDetailDTO.setProdOfferId(offeringDTO.getProductOfferingId());
                orderDetailDTO.setStateId(offeringDTO.getStateId());
                orderDetailDTO.setRequestQuantity(offeringDTO.getRequestQuantityInput());
                orderDetailDTO.setProdOfferTypeId(offeringDTO.getProductOfferTypeId());
                lsRequestOrderDetailDTO.add(orderDetailDTO);
            }
        }
    }

    public String getStrCreateDatetime() {
        return createDatetime != null ? DateUtil.date2ddMMyyyyString(createDatetime) : "";
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getOrderType() {
        return this.orderType;
    }

    public void setOrderType(Long orderType) {
        this.orderType = orderType;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockRequestOrderId() {
        return this.stockRequestOrderId;
    }

    public void setStockRequestOrderId(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public List<StockRequestOrderDetailDTO> getLsRequestOrderDetailDTO() {
        return lsRequestOrderDetailDTO;
    }

    public void setLsRequestOrderDetailDTO(List<StockRequestOrderDetailDTO> lsRequestOrderDetailDTO) {
        this.lsRequestOrderDetailDTO = lsRequestOrderDetailDTO;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public SignOfficeDTO getSignOfficeDTO() {
        return signOfficeDTO;
    }

    public void setSignOfficeDTO(SignOfficeDTO signOfficeDTO) {
        this.signOfficeDTO = signOfficeDTO;
    }

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }
}

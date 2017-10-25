package com.viettel.bccs.inventory.dto;
import java.lang.Long;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockRequestOrderDetailDTO extends BaseDTO implements Serializable {
public String getKeySet() {
 return keySet; }
    private Long approvedQuantity;
    private Long fromOwnerId;
    private Long fromOwnerType;
    private String fromOwnerName;
    private Long fromStockTransId;
    private String note;
    private Long prodOfferId;
    private String prodOfferName;
    private Long requestQuantity;
    private Long stateId;
    private String stateName;
    private Long status;
    private Long stockRequestOrderDetailId;
    private Long stockRequestOrderId;
    private Long toOwnerId;
    private Long toOwnerType;
    private String toOwnerName;
    private Date createDatetime;
    private Date lastDatetime;
    private Long exportTransId;
    private Long importTransId;
    private Long prodOfferTypeId;
    private String prodOfferTypeName;
    private String prodOfferCode;
    private String typeTransfer;
    private String unit;
	private boolean editApproveQuantity;

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getTypeTransfer() {
        return typeTransfer;
    }

    public void setTypeTransfer(String typeTransfer) {
        this.typeTransfer = typeTransfer;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStrCreateDateTime() {
        return createDatetime != null ? DateUtil.date2ddMMyyyyString(createDatetime) : "";
    }

    public Long getApprovedQuantity() {
        return this.approvedQuantity;
    }
    public void setApprovedQuantity(Long approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }
    public Long getFromOwnerId() {
        return this.fromOwnerId;
    }
    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }
    public Long getFromOwnerType() {
        return this.fromOwnerType;
    }
    public void setFromOwnerType(Long fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }
    public Long getFromStockTransId() {
        return this.fromStockTransId;
    }
    public void setFromStockTransId(Long fromStockTransId) {
        this.fromStockTransId = fromStockTransId;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Long getProdOfferId() {
        return this.prodOfferId;
    }
    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }
    public Long getRequestQuantity() {
        return this.requestQuantity;
    }
    public void setRequestQuantity(Long requestQuantity) {
        this.requestQuantity = requestQuantity;
    }
    public Long getStateId() {
        return this.stateId;
    }
    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }
    public Long getStatus() {
        return this.status;
    }
    public void setStatus(Long status) {
        this.status = status;
    }
    public Long getStockRequestOrderDetailId() {
        return this.stockRequestOrderDetailId;
    }
    public void setStockRequestOrderDetailId(Long stockRequestOrderDetailId) {
        this.stockRequestOrderDetailId = stockRequestOrderDetailId;
    }
    public Long getStockRequestOrderId() {
        return this.stockRequestOrderId;
    }
    public void setStockRequestOrderId(Long stockRequestOrderId) {
        this.stockRequestOrderId = stockRequestOrderId;
    }
    public Long getToOwnerId() {
        return this.toOwnerId;
    }
    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }
    public Long getToOwnerType() {
        return this.toOwnerType;
    }
    public void setToOwnerType(Long toOwnerType) {
        this.toOwnerType = toOwnerType;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public String getFromOwnerName() {
        return fromOwnerName;
    }

    public void setFromOwnerName(String fromOwnerName) {
        this.fromOwnerName = fromOwnerName;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public String getProdOfferTypeName() {
        return prodOfferTypeName;
    }

    public void setProdOfferTypeName(String prodOfferTypeName) {
        this.prodOfferTypeName = prodOfferTypeName;
    }

    public Date getLastDatetime() {
        return lastDatetime;
    }

    public void setLastDatetime(Date lastDatetime) {
        this.lastDatetime = lastDatetime;
    }

    public Long getExportTransId() {
        return exportTransId;
    }

    public void setExportTransId(Long exportTransId) {
        this.exportTransId = exportTransId;
    }

    public Long getImportTransId() {
        return importTransId;
    }

    public void setImportTransId(Long importTransId) {
        this.importTransId = importTransId;
    }

    public boolean isEditApproveQuantity() {
        return editApproveQuantity;
    }

    public void setEditApproveQuantity(boolean editApproveQuantity) {
        this.editApproveQuantity = editApproveQuantity;
    }
}

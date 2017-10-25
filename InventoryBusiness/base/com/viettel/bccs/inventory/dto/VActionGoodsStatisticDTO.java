package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

/**
 * @author luannt23 on 12/16/2015.
 */
public class VActionGoodsStatisticDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Long fromOwnerId;
    private Long toOwnerId;
    private String actionCode;
    private String prodOfferCode;
    private String prodOfferName;
    private String toOwnerCode;
    private String toOwnerName;
    private Long quantity;
    private Double volume;
    private Double weigth;
    private String status;
    private String statusName;
    private String actionStatus;
    private String actionStatusName;

    public String getFullProdOffer() {
        return !DataUtil.isNullOrEmpty(this.prodOfferCode) && !DataUtil.isNullOrEmpty(this.prodOfferName) ? prodOfferCode + "-"+ prodOfferName : "";
    }

    public String getFullToOwner() {
        return !DataUtil.isNullOrEmpty(this.toOwnerCode) && !DataUtil.isNullOrEmpty(this.toOwnerName) ? toOwnerCode + "-"+ toOwnerName : "";
    }

    public VActionGoodsStatisticDTO() {
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getProdOfferCode() {
        return prodOfferCode;
    }

    public void setProdOfferCode(String prodOfferCode) {
        this.prodOfferCode = prodOfferCode;
    }

    public String getProdOfferName() {
        return prodOfferName;
    }

    public void setProdOfferName(String prodOfferName) {
        this.prodOfferName = prodOfferName;
    }

    public String getToOwnerCode() {
        return toOwnerCode;
    }

    public void setToOwnerCode(String toOwnerCode) {
        this.toOwnerCode = toOwnerCode;
    }

    public String getToOwnerName() {
        return toOwnerName;
    }

    public void setToOwnerName(String toOwnerName) {
        this.toOwnerName = toOwnerName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getWeigth() {
        return weigth;
    }

    public void setWeigth(Double weigth) {
        this.weigth = weigth;
    }

    public Long getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(Long fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public Long getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(Long toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getActionStatusName() {
        return actionStatusName;
    }

    public void setActionStatusName(String actionStatusName) {
        this.actionStatusName = actionStatusName;
    }
}


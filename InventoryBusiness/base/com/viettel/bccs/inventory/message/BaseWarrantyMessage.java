package com.viettel.bccs.inventory.message;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * @author thanhnt77
 */
public class BaseWarrantyMessage extends BaseMessage {


    private Long stockTransId;
    private List<StockHandsetDTO> handsetDTOList = Lists.newArrayList();
    private List<ProductOfferTypeDTO> lsProductTypeDTO = Lists.newArrayList();
    private List<ProductOfferingDTO> lsProductOfferingDTO = Lists.newArrayList();
    private List<WarrantyStockLog> listWarrantyStockLog = Lists.newArrayList();
    private List<StockManagementForWarranty> lstStockModelWarranty = Lists.newArrayList();

    public BaseWarrantyMessage() {
    }

    public List<StockHandsetDTO> getHandsetDTOList() {
        return handsetDTOList;
    }

    public void setHandsetDTOList(List<StockHandsetDTO> handsetDTOList) {
        this.handsetDTOList = handsetDTOList;
    }

    public List<ProductOfferTypeDTO> getLsProductTypeDTO() {
        return lsProductTypeDTO;
    }

    public void setLsProductTypeDTO(List<ProductOfferTypeDTO> lsProductTypeDTO) {
        this.lsProductTypeDTO = lsProductTypeDTO;
    }

    public List<WarrantyStockLog> getListWarrantyStockLog() {
        return listWarrantyStockLog;
    }

    public void setListWarrantyStockLog(List<WarrantyStockLog> listWarrantyStockLog) {
        this.listWarrantyStockLog = listWarrantyStockLog;
    }

    public List<StockManagementForWarranty> getLstStockModelWarranty() {
        return lstStockModelWarranty;
    }

    public void setLstStockModelWarranty(List<StockManagementForWarranty> lstStockModelWarranty) {
        this.lstStockModelWarranty = lstStockModelWarranty;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public List<ProductOfferingDTO> getLsProductOfferingDTO() {
        return lsProductOfferingDTO;
    }

    public void setLsProductOfferingDTO(List<ProductOfferingDTO> lsProductOfferingDTO) {
        this.lsProductOfferingDTO = lsProductOfferingDTO;
    }
}

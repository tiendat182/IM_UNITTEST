package com.viettel.bccs.inventory.mapper;

import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockOrderDetailDTO;
import com.viettel.bccs.inventory.model.StockOrderDetail;

public class StockOrderDetailMapper extends BaseMapper<StockOrderDetail, StockOrderDetailDTO> {
    @Override
    public StockOrderDetailDTO toDtoBean(StockOrderDetail model) {
        StockOrderDetailDTO obj = null;
        if (model != null) {
            obj = new StockOrderDetailDTO();
            obj.setQuantityOrder(model.getQuantityOrder());
            obj.setQuantityReal(model.getQuantityReal());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setStockOrderDetailId(model.getStockOrderDetailId());
            obj.setStockOrderId(model.getStockOrderId());
        }
        return obj;
    }

    @Override
    public StockOrderDetail toPersistenceBean(StockOrderDetailDTO dtoBean) {
        StockOrderDetail obj = null;
        if (dtoBean != null) {
            obj = new StockOrderDetail();
            obj.setQuantityOrder(dtoBean.getQuantityOrder());
            obj.setQuantityReal(dtoBean.getQuantityReal());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setStockOrderDetailId(dtoBean.getStockOrderDetailId());
            obj.setStockOrderId(dtoBean.getStockOrderId());
        }
        return obj;
    }
}

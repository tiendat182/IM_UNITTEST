package com.viettel.bccs.inventory.mapper;

import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.bccs.inventory.model.StockBalanceDetail;

public class StockBalanceDetailMapper extends BaseMapper<StockBalanceDetail, StockBalanceDetailDTO> {
    @Override
    public StockBalanceDetailDTO toDtoBean(StockBalanceDetail model) {
        StockBalanceDetailDTO obj = null;
        if (model != null) {
            obj = new StockBalanceDetailDTO();
            obj.setCreateDatetime(model.getCreateDatetime());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setQuantity(model.getQuantity());
            obj.setQuantityBccs(model.getQuantityBccs());
            obj.setQuantityErp(model.getQuantityErp());
            obj.setQuantityReal(model.getQuantityReal());
            obj.setStockBalanceDetailId(model.getStockBalanceDetailId());
            obj.setStockRequestId(model.getStockRequestId());
        }
        return obj;
    }

    @Override
    public StockBalanceDetail toPersistenceBean(StockBalanceDetailDTO dtoBean) {
        StockBalanceDetail obj = null;
        if (dtoBean != null) {
            obj = new StockBalanceDetail();
            obj.setCreateDatetime(dtoBean.getCreateDatetime());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setQuantityBccs(dtoBean.getQuantityBccs());
            obj.setQuantityErp(dtoBean.getQuantityErp());
            obj.setQuantityReal(dtoBean.getQuantityReal());
            obj.setStockBalanceDetailId(dtoBean.getStockBalanceDetailId());
            obj.setStockRequestId(dtoBean.getStockRequestId());
        }
        return obj;
    }
}

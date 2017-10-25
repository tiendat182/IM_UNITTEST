package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.bccs.inventory.model.StockDeliverDetail;
public class StockDeliverDetailMapper extends BaseMapper<StockDeliverDetail, StockDeliverDetailDTO> {
    @Override
    public StockDeliverDetailDTO toDtoBean(StockDeliverDetail model) {
        StockDeliverDetailDTO obj = null;
        if (model != null) {
           obj =   new StockDeliverDetailDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setNote(model.getNote());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setProdOfferTypeId(model.getProdOfferTypeId());
            obj.setQuantityReal(model.getQuantityReal());
            obj.setQuantitySys(model.getQuantitySys());
            obj.setStateId(model.getStateId());
            obj.setStockDeliverDetailId(model.getStockDeliverDetailId());
            obj.setStockDeliverId(model.getStockDeliverId());
        }
        return obj;
    }
    @Override
    public StockDeliverDetail toPersistenceBean(StockDeliverDetailDTO dtoBean) {
        StockDeliverDetail obj = null;
        if (dtoBean != null) {
           obj =   new StockDeliverDetail(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setNote(dtoBean.getNote());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setProdOfferTypeId(dtoBean.getProdOfferTypeId());
            obj.setQuantityReal(dtoBean.getQuantityReal());
            obj.setQuantitySys(dtoBean.getQuantitySys());
            obj.setStateId(dtoBean.getStateId());
            obj.setStockDeliverDetailId(dtoBean.getStockDeliverDetailId());
            obj.setStockDeliverId(dtoBean.getStockDeliverId());
        }
        return obj;
    }
}

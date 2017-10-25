package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockTransDetailRescueDTO;
import com.viettel.bccs.inventory.model.StockTransDetailRescue;
public class StockTransDetailRescueMapper extends BaseMapper<StockTransDetailRescue, StockTransDetailRescueDTO> {
    @Override
    public StockTransDetailRescueDTO toDtoBean(StockTransDetailRescue model) {
        StockTransDetailRescueDTO obj = null;
        if (model != null) {
           obj =   new StockTransDetailRescueDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setQuantity(model.getQuantity());
            obj.setStateId(model.getStateId());
            obj.setStockTransDetailId(model.getStockTransDetailId());
            obj.setStockTransId(model.getStockTransId());
        }
        return obj;
    }
    @Override
    public StockTransDetailRescue toPersistenceBean(StockTransDetailRescueDTO dtoBean) {
        StockTransDetailRescue obj = null;
        if (dtoBean != null) {
           obj =   new StockTransDetailRescue(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setStateId(dtoBean.getStateId());
            obj.setStockTransDetailId(dtoBean.getStockTransDetailId());
            obj.setStockTransId(dtoBean.getStockTransId());
        }
        return obj;
    }
}

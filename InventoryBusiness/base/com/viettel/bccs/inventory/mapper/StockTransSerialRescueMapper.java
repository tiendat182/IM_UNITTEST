package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.model.StockTransSerialRescue;
public class StockTransSerialRescueMapper extends BaseMapper<StockTransSerialRescue, StockTransSerialRescueDTO> {
    @Override
    public StockTransSerialRescueDTO toDtoBean(StockTransSerialRescue model) {
        StockTransSerialRescueDTO obj = null;
        if (model != null) {
           obj =   new StockTransSerialRescueDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setFromSerial(model.getFromSerial());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setQuantity(model.getQuantity());
            obj.setStateId(model.getStateId());
            obj.setStockTransId(model.getStockTransId());
            obj.setStockTransSerialId(model.getStockTransSerialId());
            obj.setToSerial(model.getToSerial());
        }
        return obj;
    }
    @Override
    public StockTransSerialRescue toPersistenceBean(StockTransSerialRescueDTO dtoBean) {
        StockTransSerialRescue obj = null;
        if (dtoBean != null) {
           obj =   new StockTransSerialRescue(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setFromSerial(dtoBean.getFromSerial());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setStateId(dtoBean.getStateId());
            obj.setStockTransId(dtoBean.getStockTransId());
            obj.setStockTransSerialId(dtoBean.getStockTransSerialId());
            obj.setToSerial(dtoBean.getToSerial());
        }
        return obj;
    }
}

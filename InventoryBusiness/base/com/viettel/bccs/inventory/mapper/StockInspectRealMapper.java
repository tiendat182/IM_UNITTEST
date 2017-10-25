package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockInspectRealDTO;
import com.viettel.bccs.inventory.model.StockInspectReal;
public class StockInspectRealMapper extends BaseMapper<StockInspectReal, StockInspectRealDTO> {
    @Override
    public StockInspectRealDTO toDtoBean(StockInspectReal model) {
        StockInspectRealDTO obj = null;
        if (model != null) {
           obj =   new StockInspectRealDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setFromSerial(model.getFromSerial());
            obj.setQuantity(model.getQuantity());
            obj.setStockInspectId(model.getStockInspectId());
            obj.setStockInspectRealId(model.getStockInspectRealId());
            obj.setToSerial(model.getToSerial());
        }
        return obj;
    }
    @Override
    public StockInspectReal toPersistenceBean(StockInspectRealDTO dtoBean) {
        StockInspectReal obj = null;
        if (dtoBean != null) {
           obj =   new StockInspectReal(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setFromSerial(dtoBean.getFromSerial());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setStockInspectId(dtoBean.getStockInspectId());
            obj.setStockInspectRealId(dtoBean.getStockInspectRealId());
            obj.setToSerial(dtoBean.getToSerial());
        }
        return obj;
    }
}

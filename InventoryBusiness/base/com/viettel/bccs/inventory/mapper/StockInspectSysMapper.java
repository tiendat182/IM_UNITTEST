package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockInspectSysDTO;
import com.viettel.bccs.inventory.model.StockInspectSys;
public class StockInspectSysMapper extends BaseMapper<StockInspectSys, StockInspectSysDTO> {
    @Override
    public StockInspectSysDTO toDtoBean(StockInspectSys model) {
        StockInspectSysDTO obj = null;
        if (model != null) {
           obj =   new StockInspectSysDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setFromSerial(model.getFromSerial());
            obj.setQuantity(model.getQuantity());
            obj.setStockInspectId(model.getStockInspectId());
            obj.setStockInspectSysId(model.getStockInspectSysId());
            obj.setToSerial(model.getToSerial());
        }
        return obj;
    }
    @Override
    public StockInspectSys toPersistenceBean(StockInspectSysDTO dtoBean) {
        StockInspectSys obj = null;
        if (dtoBean != null) {
           obj =   new StockInspectSys(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setFromSerial(dtoBean.getFromSerial());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setStockInspectId(dtoBean.getStockInspectId());
            obj.setStockInspectSysId(dtoBean.getStockInspectSysId());
            obj.setToSerial(dtoBean.getToSerial());
        }
        return obj;
    }
}

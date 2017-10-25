package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockTransActionRescueDTO;
import com.viettel.bccs.inventory.model.StockTransActionRescue;
public class StockTransActionRescueMapper extends BaseMapper<StockTransActionRescue, StockTransActionRescueDTO> {
    @Override
    public StockTransActionRescueDTO toDtoBean(StockTransActionRescue model) {
        StockTransActionRescueDTO obj = null;
        if (model != null) {
           obj =   new StockTransActionRescueDTO(); 
            obj.setActionId(model.getActionId());
            obj.setActionStaffId(model.getActionStaffId());
            obj.setActionType(model.getActionType());
            obj.setCreateDate(model.getCreateDate());
            obj.setDescription(model.getDescription());
            obj.setStockTransId(model.getStockTransId());
        }
        return obj;
    }
    @Override
    public StockTransActionRescue toPersistenceBean(StockTransActionRescueDTO dtoBean) {
        StockTransActionRescue obj = null;
        if (dtoBean != null) {
           obj =   new StockTransActionRescue(); 
            obj.setActionId(dtoBean.getActionId());
            obj.setActionStaffId(dtoBean.getActionStaffId());
            obj.setActionType(dtoBean.getActionType());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setDescription(dtoBean.getDescription());
            obj.setStockTransId(dtoBean.getStockTransId());
        }
        return obj;
    }
}

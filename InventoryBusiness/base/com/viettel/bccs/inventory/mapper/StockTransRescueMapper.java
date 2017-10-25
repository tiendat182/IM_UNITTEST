package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockTransRescueDTO;
import com.viettel.bccs.inventory.model.StockTransRescue;
public class StockTransRescueMapper extends BaseMapper<StockTransRescue, StockTransRescueDTO> {
    @Override
    public StockTransRescueDTO toDtoBean(StockTransRescue model) {
        StockTransRescueDTO obj = null;
        if (model != null) {
           obj =   new StockTransRescueDTO(); 
            obj.setApproveStaffId(model.getApproveStaffId());
            obj.setCreateDate(model.getCreateDate());
            obj.setFromOwnerId(model.getFromOwnerId());
            obj.setFromOwnerType(model.getFromOwnerType());
            obj.setReasonId(model.getReasonId());
            obj.setRequestCode(model.getRequestCode());
            obj.setStatus(model.getStatus());
            obj.setStockTransId(model.getStockTransId());
            obj.setToOwnerId(model.getToOwnerId());
            obj.setToOwnerType(model.getToOwnerType());
        }
        return obj;
    }
    @Override
    public StockTransRescue toPersistenceBean(StockTransRescueDTO dtoBean) {
        StockTransRescue obj = null;
        if (dtoBean != null) {
           obj =   new StockTransRescue(); 
            obj.setApproveStaffId(dtoBean.getApproveStaffId());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setFromOwnerId(dtoBean.getFromOwnerId());
            obj.setFromOwnerType(dtoBean.getFromOwnerType());
            obj.setReasonId(dtoBean.getReasonId());
            obj.setRequestCode(dtoBean.getRequestCode());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockTransId(dtoBean.getStockTransId());
            obj.setToOwnerId(dtoBean.getToOwnerId());
            obj.setToOwnerType(dtoBean.getToOwnerType());
        }
        return obj;
    }
}

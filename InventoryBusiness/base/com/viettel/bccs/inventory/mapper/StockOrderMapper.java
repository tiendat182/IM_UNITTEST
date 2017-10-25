package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockOrderDTO;
import com.viettel.bccs.inventory.model.StockOrder;
public class StockOrderMapper extends BaseMapper<StockOrder, StockOrderDTO> {
    @Override
    public StockOrderDTO toDtoBean(StockOrder model) {
        StockOrderDTO obj = null;
        if (model != null) {
           obj =   new StockOrderDTO(); 
            obj.setApproveDate(model.getApproveDate());
            obj.setApproveStaffId(model.getApproveStaffId());
            obj.setCancelDate(model.getCancelDate());
            obj.setCreateDate(model.getCreateDate());
            obj.setRefuseDate(model.getRefuseDate());
            obj.setRefuseStaffId(model.getRefuseStaffId());
            obj.setShopId(model.getShopId());
            obj.setStaffId(model.getStaffId());
            obj.setStatus(model.getStatus());
            obj.setStockOrderCode(model.getStockOrderCode());
            obj.setStockOrderId(model.getStockOrderId());
        }
        return obj;
    }
    @Override
    public StockOrder toPersistenceBean(StockOrderDTO dtoBean) {
        StockOrder obj = null;
        if (dtoBean != null) {
           obj =   new StockOrder(); 
            obj.setApproveDate(dtoBean.getApproveDate());
            obj.setApproveStaffId(dtoBean.getApproveStaffId());
            obj.setCancelDate(dtoBean.getCancelDate());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setRefuseDate(dtoBean.getRefuseDate());
            obj.setRefuseStaffId(dtoBean.getRefuseStaffId());
            obj.setShopId(dtoBean.getShopId());
            obj.setStaffId(dtoBean.getStaffId());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockOrderCode(dtoBean.getStockOrderCode());
            obj.setStockOrderId(dtoBean.getStockOrderId());
        }
        return obj;
    }
}

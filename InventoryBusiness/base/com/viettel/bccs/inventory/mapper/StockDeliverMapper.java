package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockDeliverDTO;
import com.viettel.bccs.inventory.model.StockDeliver;
public class StockDeliverMapper extends BaseMapper<StockDeliver, StockDeliverDTO> {
    @Override
    public StockDeliverDTO toDtoBean(StockDeliver model) {
        StockDeliverDTO obj = null;
        if (model != null) {
           obj =   new StockDeliverDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateUser(model.getCreateUser());
            obj.setCreateUserId(model.getCreateUserId());
            obj.setCreateUserName(model.getCreateUserName());
            obj.setNewStaffId(model.getNewStaffId());
            obj.setNewStaffOwnerId(model.getNewStaffOwnerId());
            obj.setNote(model.getNote());
            obj.setOldStaffId(model.getOldStaffId());
            obj.setOldStaffOwnerId(model.getOldStaffOwnerId());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerType(model.getOwnerType());
            obj.setStatus(model.getStatus());
            obj.setStockDeliverId(model.getStockDeliverId());
            obj.setUpdateDate(model.getUpdateDate());
        }
        return obj;
    }
    @Override
    public StockDeliver toPersistenceBean(StockDeliverDTO dtoBean) {
        StockDeliver obj = null;
        if (dtoBean != null) {
           obj =   new StockDeliver(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateUser(dtoBean.getCreateUser());
            obj.setCreateUserId(dtoBean.getCreateUserId());
            obj.setCreateUserName(dtoBean.getCreateUserName());
            obj.setNewStaffId(dtoBean.getNewStaffId());
            obj.setNewStaffOwnerId(dtoBean.getNewStaffOwnerId());
            obj.setNote(dtoBean.getNote());
            obj.setOldStaffId(dtoBean.getOldStaffId());
            obj.setOldStaffOwnerId(dtoBean.getOldStaffOwnerId());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockDeliverId(dtoBean.getStockDeliverId());
            obj.setUpdateDate(dtoBean.getUpdateDate());
        }
        return obj;
    }
}

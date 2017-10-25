package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockHandsetRescueDTO;
import com.viettel.bccs.inventory.model.StockHandsetRescue;
public class StockHandsetRescueMapper extends BaseMapper<StockHandsetRescue, StockHandsetRescueDTO> {
    @Override
    public StockHandsetRescueDTO toDtoBean(StockHandsetRescue model) {
        StockHandsetRescueDTO obj = null;
        if (model != null) {
           obj =   new StockHandsetRescueDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setId(model.getId());
            obj.setLastModify(model.getLastModify());
            obj.setNewProdOfferlId(model.getNewProdOfferlId());
            obj.setOldProdOfferId(model.getOldProdOfferId());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerType(model.getOwnerType());
            obj.setSerial(model.getSerial());
            obj.setStatus(model.getStatus());
        }
        return obj;
    }
    @Override
    public StockHandsetRescue toPersistenceBean(StockHandsetRescueDTO dtoBean) {
        StockHandsetRescue obj = null;
        if (dtoBean != null) {
           obj =   new StockHandsetRescue(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setId(dtoBean.getId());
            obj.setLastModify(dtoBean.getLastModify());
            obj.setNewProdOfferlId(dtoBean.getNewProdOfferlId());
            obj.setOldProdOfferId(dtoBean.getOldProdOfferId());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setSerial(dtoBean.getSerial());
            obj.setStatus(dtoBean.getStatus());
        }
        return obj;
    }
}

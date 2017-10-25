package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockHandsetLendDTO;
import com.viettel.bccs.inventory.model.StockHandsetLend;
public class StockHandsetLendMapper extends BaseMapper<StockHandsetLend, StockHandsetLendDTO> {
    @Override
    public StockHandsetLendDTO toDtoBean(StockHandsetLend model) {
        StockHandsetLendDTO obj = null;
        if (model != null) {
           obj =   new StockHandsetLendDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateStaffId(model.getCreateStaffId());
            obj.setId(model.getId());
            obj.setLastModify(model.getLastModify());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerType(model.getOwnerType());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setSerial(model.getSerial());
            obj.setStatus(model.getStatus());
        }
        return obj;
    }
    @Override
    public StockHandsetLend toPersistenceBean(StockHandsetLendDTO dtoBean) {
        StockHandsetLend obj = null;
        if (dtoBean != null) {
           obj =   new StockHandsetLend(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateStaffId(dtoBean.getCreateStaffId());
            obj.setId(dtoBean.getId());
            obj.setLastModify(dtoBean.getLastModify());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setSerial(dtoBean.getSerial());
            obj.setStatus(dtoBean.getStatus());
        }
        return obj;
    }
}

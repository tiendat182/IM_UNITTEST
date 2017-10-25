package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.DivideSerialActionDTO;
import com.viettel.bccs.inventory.model.DivideSerialAction;
public class DivideSerialActionMapper extends BaseMapper<DivideSerialAction, DivideSerialActionDTO> {
    @Override
    public DivideSerialActionDTO toDtoBean(DivideSerialAction model) {
        DivideSerialActionDTO obj = null;
        if (model != null) {
           obj =   new DivideSerialActionDTO(); 
            obj.setCreateDatetime(model.getCreateDatetime());
            obj.setCreateUser(model.getCreateUser());
            obj.setId(model.getId());
            obj.setSerialContent(model.getSerialContent());
            obj.setStockTransActionId(model.getStockTransActionId());
        }
        return obj;
    }
    @Override
    public DivideSerialAction toPersistenceBean(DivideSerialActionDTO dtoBean) {
        DivideSerialAction obj = null;
        if (dtoBean != null) {
           obj =   new DivideSerialAction(); 
            obj.setCreateDatetime(dtoBean.getCreateDatetime());
            obj.setCreateUser(dtoBean.getCreateUser());
            obj.setId(dtoBean.getId());
            obj.setSerialContent(dtoBean.getSerialContent());
            obj.setStockTransActionId(dtoBean.getStockTransActionId());
        }
        return obj;
    }
}

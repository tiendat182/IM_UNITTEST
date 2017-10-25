package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.bccs.inventory.model.ActionLogDetail;
public class ActionLogDetailMapper extends BaseMapper<ActionLogDetail, ActionLogDetailDTO> {
    @Override
    public ActionLogDetailDTO toDtoBean(ActionLogDetail model) {
        ActionLogDetailDTO obj = null;
        if (model != null) {
           obj =   new ActionLogDetailDTO(); 
            obj.setActionDate(model.getActionDate());
            obj.setActionDetailId(model.getActionDetailId());
            obj.setActionId(model.getActionId());
            obj.setColumnName(model.getColumnName());
            obj.setDescription(model.getDescription());
            obj.setNewValue(model.getNewValue());
            obj.setOldValue(model.getOldValue());
            obj.setTableName(model.getTableName());
        }
        return obj;
    }
    @Override
    public ActionLogDetail toPersistenceBean(ActionLogDetailDTO dtoBean) {
        ActionLogDetail obj = null;
        if (dtoBean != null) {
           obj =   new ActionLogDetail(); 
            obj.setActionDate(dtoBean.getActionDate());
            obj.setActionDetailId(dtoBean.getActionDetailId());
            obj.setActionId(dtoBean.getActionId());
            obj.setColumnName(dtoBean.getColumnName());
            obj.setDescription(dtoBean.getDescription());
            obj.setNewValue(dtoBean.getNewValue());
            obj.setOldValue(dtoBean.getOldValue());
            obj.setTableName(dtoBean.getTableName());
        }
        return obj;
    }
}

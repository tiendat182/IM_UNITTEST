package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ActionLogDTO;
import com.viettel.bccs.inventory.model.ActionLog;
public class ActionLogMapper extends BaseMapper<ActionLog, ActionLogDTO> {
    @Override
    public ActionLogDTO toDtoBean(ActionLog model) {
        ActionLogDTO obj = null;
        if (model != null) {
           obj =   new ActionLogDTO(); 
            obj.setActionDate(model.getActionDate());
            obj.setActionId(model.getActionId());
            obj.setActionIp(model.getActionIp());
            obj.setActionType(model.getActionType());
            obj.setActionUser(model.getActionUser());
            obj.setDescription(model.getDescription());
            obj.setObjectId(model.getObjectId());
        }
        return obj;
    }
    @Override
    public ActionLog toPersistenceBean(ActionLogDTO dtoBean) {
        ActionLog obj = null;
        if (dtoBean != null) {
           obj =   new ActionLog(); 
            obj.setActionDate(dtoBean.getActionDate());
            obj.setActionId(dtoBean.getActionId());
            obj.setActionIp(dtoBean.getActionIp());
            obj.setActionType(dtoBean.getActionType());
            obj.setActionUser(dtoBean.getActionUser());
            obj.setDescription(dtoBean.getDescription());
            obj.setObjectId(dtoBean.getObjectId());
        }
        return obj;
    }
}

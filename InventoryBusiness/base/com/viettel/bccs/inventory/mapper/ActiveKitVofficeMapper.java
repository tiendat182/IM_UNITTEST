package com.viettel.bccs.inventory.mapper;

import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ActiveKitVofficeDTO;
import com.viettel.bccs.inventory.model.ActiveKitVoffice;

public class ActiveKitVofficeMapper extends BaseMapper<ActiveKitVoffice, ActiveKitVofficeDTO> {
    @Override
    public ActiveKitVofficeDTO toDtoBean(ActiveKitVoffice model) {
        ActiveKitVofficeDTO obj = null;
        if (model != null) {
            obj = new ActiveKitVofficeDTO();
            obj.setAccountName(model.getAccountName());
            obj.setAccountPass(model.getAccountPass());
            obj.setActionCode(model.getActionCode());
            obj.setActiveKitVofficeId(model.getActiveKitVofficeId());
            obj.setChangeModelTransId(model.getChangeModelTransId());
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateUserId(model.getCreateUserId());
            obj.setLastModify(model.getLastModify());
            obj.setNote(model.getNote());
            obj.setPrefixTemplate(model.getPrefixTemplate());
            obj.setSignUserList(model.getSignUserList());
            obj.setStatus(model.getStatus());
            obj.setStockTransActionId(model.getStockTransActionId());
        }
        return obj;
    }

    @Override
    public ActiveKitVoffice toPersistenceBean(ActiveKitVofficeDTO dtoBean) {
        ActiveKitVoffice obj = null;
        if (dtoBean != null) {
            obj = new ActiveKitVoffice();
            obj.setAccountName(dtoBean.getAccountName());
            obj.setAccountPass(dtoBean.getAccountPass());
            obj.setActionCode(dtoBean.getActionCode());
            obj.setActiveKitVofficeId(dtoBean.getActiveKitVofficeId());
            obj.setChangeModelTransId(dtoBean.getChangeModelTransId());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateUserId(dtoBean.getCreateUserId());
            obj.setLastModify(dtoBean.getLastModify());
            obj.setNote(dtoBean.getNote());
            obj.setPrefixTemplate(dtoBean.getPrefixTemplate());
            obj.setSignUserList(dtoBean.getSignUserList());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockTransActionId(dtoBean.getStockTransActionId());
        }
        return obj;
    }
}

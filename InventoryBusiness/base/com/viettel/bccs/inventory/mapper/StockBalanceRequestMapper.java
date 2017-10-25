package com.viettel.bccs.inventory.mapper;

import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockBalanceRequestDTO;
import com.viettel.bccs.inventory.model.StockBalanceRequest;

public class StockBalanceRequestMapper extends BaseMapper<StockBalanceRequest, StockBalanceRequestDTO> {
    @Override
    public StockBalanceRequestDTO toDtoBean(StockBalanceRequest model) {
        StockBalanceRequestDTO obj = null;
        if (model != null) {
            obj = new StockBalanceRequestDTO();
            obj.setCreateDatetime(model.getCreateDatetime());
            obj.setCreateUser(model.getCreateUser());
            obj.setListEmailSign(model.getListEmailSign());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerType(model.getOwnerType());
            obj.setStatus(model.getStatus());
            obj.setStockRequestId(model.getStockRequestId());
            obj.setUpdateDatetime(model.getUpdateDatetime());
            obj.setUpdateUser(model.getUpdateUser());
            obj.setType(model.getType());
            obj.setStockTransActionId(model.getStockTransActionId());
            obj.setRequestCode(model.getRequestCode());
        }
        return obj;
    }

    @Override
    public StockBalanceRequest toPersistenceBean(StockBalanceRequestDTO dtoBean) {
        StockBalanceRequest obj = null;
        if (dtoBean != null) {
            obj = new StockBalanceRequest();
            obj.setCreateDatetime(dtoBean.getCreateDatetime());
            obj.setCreateUser(dtoBean.getCreateUser());
            obj.setListEmailSign(dtoBean.getListEmailSign());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockRequestId(dtoBean.getStockRequestId());
            obj.setUpdateDatetime(dtoBean.getUpdateDatetime());
            obj.setUpdateUser(dtoBean.getUpdateUser());
            obj.setType(dtoBean.getType());
            obj.setStockTransActionId(dtoBean.getStockTransActionId());
            obj.setRequestCode(dtoBean.getRequestCode());
        }
        return obj;
    }
}

package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockInspectDTO;
import com.viettel.bccs.inventory.model.StockInspect;
public class StockInspectMapper extends BaseMapper<StockInspect, StockInspectDTO> {
    @Override
    public StockInspectDTO toDtoBean(StockInspect model) {
        StockInspectDTO obj = null;
        if (model != null) {
           obj =   new StockInspectDTO(); 
            obj.setApproveDate(model.getApproveDate());
            obj.setApproveNote(model.getApproveNote());
            obj.setApproveStatus(model.getApproveStatus());
            obj.setApproveUser(model.getApproveUser());
            obj.setApproveUserId(model.getApproveUserId());
            obj.setApproveUserName(model.getApproveUserName());
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateUser(model.getCreateUser());
            obj.setCreateUserId(model.getCreateUserId());
            obj.setCreateUserName(model.getCreateUserName());
            obj.setIsFinished(model.getIsFinished());
            obj.setIsFinishedAll(model.getIsFinishedAll());
            obj.setIsScan(model.getIsScan());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerType(model.getOwnerType());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setProdOfferTypeId(model.getProdOfferTypeId());
            obj.setQuantityReal(model.getQuantityReal());
            obj.setQuantitySys(model.getQuantitySys());
            obj.setStateId(model.getStateId());
            obj.setStockInspectId(model.getStockInspectId());
            obj.setUpdateDate(model.getUpdateDate());
        }
        return obj;
    }
    @Override
    public StockInspect toPersistenceBean(StockInspectDTO dtoBean) {
        StockInspect obj = null;
        if (dtoBean != null) {
           obj =   new StockInspect(); 
            obj.setApproveDate(dtoBean.getApproveDate());
            obj.setApproveNote(dtoBean.getApproveNote());
            obj.setApproveStatus(dtoBean.getApproveStatus());
            obj.setApproveUser(dtoBean.getApproveUser());
            obj.setApproveUserId(dtoBean.getApproveUserId());
            obj.setApproveUserName(dtoBean.getApproveUserName());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateUser(dtoBean.getCreateUser());
            obj.setCreateUserId(dtoBean.getCreateUserId());
            obj.setCreateUserName(dtoBean.getCreateUserName());
            obj.setIsFinished(dtoBean.getIsFinished());
            obj.setIsFinishedAll(dtoBean.getIsFinishedAll());
            obj.setIsScan(dtoBean.getIsScan());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setProdOfferTypeId(dtoBean.getProdOfferTypeId());
            obj.setQuantityReal(dtoBean.getQuantityReal());
            obj.setQuantitySys(dtoBean.getQuantitySys());
            obj.setStateId(dtoBean.getStateId());
            obj.setStockInspectId(dtoBean.getStockInspectId());
            obj.setUpdateDate(dtoBean.getUpdateDate());
        }
        return obj;
    }
}

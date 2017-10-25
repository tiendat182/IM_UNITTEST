package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockKitDTO;
import com.viettel.bccs.inventory.model.StockKit;
public class StockKitMapper extends BaseMapper<StockKit, StockKitDTO> {
    @Override
    public StockKitDTO toDtoBean(StockKit model) {
        StockKitDTO obj = null;
        if (model != null) {
           obj =   new StockKitDTO(); 
            obj.setAucRegDate(model.getAucRegDate());
            obj.setAucRemoveDate(model.getAucRemoveDate());
            obj.setAucStatus(model.getAucStatus());
            obj.setBankplusStatus(model.getBankplusStatus());
            obj.setCheckDial(model.getCheckDial());
            obj.setConnectionDate(model.getConnectionDate());
            obj.setConnectionStatus(model.getConnectionStatus());
            obj.setConnectionType(model.getConnectionType());
            obj.setContractCode(model.getContractCode());
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateUser(model.getCreateUser());
            obj.setDepositPrice(model.getDepositPrice());
            obj.setDialStatus(model.getDialStatus());
            obj.setHlrId(model.getHlrId());
            obj.setHlrRegDate(model.getHlrRegDate());
            obj.setHlrRemoveDate(model.getHlrRemoveDate());
            obj.setHlrStatus(model.getHlrStatus());
            obj.setIccid(model.getIccid());
            obj.setId(model.getId());
            obj.setImsi(model.getImsi());
            obj.setIsdn(model.getIsdn());
            obj.setOldOwnerId(model.getOldOwnerId());
            obj.setOldOwnerType(model.getOldOwnerType());
            obj.setOrderCode(model.getOrderCode());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerReceiverId(model.getOwnerReceiverId());
            obj.setOwnerReceiverType(model.getOwnerReceiverType());
            obj.setOwnerType(model.getOwnerType());
            obj.setPin(model.getPin());
            obj.setPin2(model.getPin2());
            obj.setPoCode(model.getPoCode());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setPuk(model.getPuk());
            obj.setPuk2(model.getPuk2());
            obj.setReceiverName(model.getReceiverName());
            obj.setSaleDate(model.getSaleDate());
            obj.setSerial(model.getSerial());
            obj.setSerialReal(model.getSerialReal());
            obj.setSimType(model.getSimType());
            obj.setStartDateWarranty(model.getStartDateWarranty());
            obj.setStateId(model.getStateId());
            obj.setStatus(model.getStatus());
            obj.setStockTransId(model.getStockTransId());
            obj.setTelecomServiceId(model.getTelecomServiceId());
            obj.setUpdateDatetime(model.getUpdateDatetime());
            obj.setUpdateNumber(model.getUpdateNumber());
            obj.setUserSessionId(model.getUserSessionId());
        }
        return obj;
    }
    @Override
    public StockKit toPersistenceBean(StockKitDTO dtoBean) {
        StockKit obj = null;
        if (dtoBean != null) {
           obj =   new StockKit(); 
            obj.setAucRegDate(dtoBean.getAucRegDate());
            obj.setAucRemoveDate(dtoBean.getAucRemoveDate());
            obj.setAucStatus(dtoBean.getAucStatus());
            obj.setBankplusStatus(dtoBean.getBankplusStatus());
            obj.setCheckDial(dtoBean.getCheckDial());
            obj.setConnectionDate(dtoBean.getConnectionDate());
            obj.setConnectionStatus(dtoBean.getConnectionStatus());
            obj.setConnectionType(dtoBean.getConnectionType());
            obj.setContractCode(dtoBean.getContractCode());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateUser(dtoBean.getCreateUser());
            obj.setDepositPrice(dtoBean.getDepositPrice());
            obj.setDialStatus(dtoBean.getDialStatus());
            obj.setHlrId(dtoBean.getHlrId());
            obj.setHlrRegDate(dtoBean.getHlrRegDate());
            obj.setHlrRemoveDate(dtoBean.getHlrRemoveDate());
            obj.setHlrStatus(dtoBean.getHlrStatus());
            obj.setIccid(dtoBean.getIccid());
            obj.setId(dtoBean.getId());
            obj.setImsi(dtoBean.getImsi());
            obj.setIsdn(dtoBean.getIsdn());
            obj.setOldOwnerId(dtoBean.getOldOwnerId());
            obj.setOldOwnerType(dtoBean.getOldOwnerType());
            obj.setOrderCode(dtoBean.getOrderCode());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerReceiverId(dtoBean.getOwnerReceiverId());
            obj.setOwnerReceiverType(dtoBean.getOwnerReceiverType());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setPin(dtoBean.getPin());
            obj.setPin2(dtoBean.getPin2());
            obj.setPoCode(dtoBean.getPoCode());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setPuk(dtoBean.getPuk());
            obj.setPuk2(dtoBean.getPuk2());
            obj.setReceiverName(dtoBean.getReceiverName());
            obj.setSaleDate(dtoBean.getSaleDate());
            obj.setSerial(dtoBean.getSerial());
            obj.setSerialReal(dtoBean.getSerialReal());
            obj.setSimType(dtoBean.getSimType());
            obj.setStartDateWarranty(dtoBean.getStartDateWarranty());
            obj.setStateId(dtoBean.getStateId());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockTransId(dtoBean.getStockTransId());
            obj.setTelecomServiceId(dtoBean.getTelecomServiceId());
            obj.setUpdateDatetime(dtoBean.getUpdateDatetime());
            obj.setUpdateNumber(dtoBean.getUpdateNumber());
            obj.setUserSessionId(dtoBean.getUserSessionId());
        }
        return obj;
    }
}

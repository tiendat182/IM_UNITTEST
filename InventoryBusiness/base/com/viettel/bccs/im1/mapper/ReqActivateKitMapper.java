package com.viettel.bccs.im1.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.im1.dto.ReqActivateKitDTO;
import com.viettel.bccs.im1.model.ReqActivateKit;
public class ReqActivateKitMapper extends BaseMapper<ReqActivateKit, ReqActivateKitDTO> {
    @Override
    public ReqActivateKitDTO toDtoBean(ReqActivateKit model) {
        ReqActivateKitDTO obj = null;
        if (model != null) {
           obj =   new ReqActivateKitDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setErrorDescription(model.getErrorDescription());
            obj.setFromSerial(model.getFromSerial());
            obj.setProcessCount(model.getProcessCount());
            obj.setProcessDate(model.getProcessDate());
            obj.setProcessStatus(model.getProcessStatus());
            obj.setReqId(model.getReqId());
            obj.setSaleTransDate(model.getSaleTransDate());
            obj.setSaleTransId(model.getSaleTransId());
            obj.setSaleTransType(model.getSaleTransType());
            obj.setSaleType(model.getSaleType());
            obj.setShopId(model.getShopId());
            obj.setStaffId(model.getStaffId());
            obj.setToSerial(model.getToSerial());
        }
        return obj;
    }
    @Override
    public ReqActivateKit toPersistenceBean(ReqActivateKitDTO dtoBean) {
        ReqActivateKit obj = null;
        if (dtoBean != null) {
           obj =   new ReqActivateKit(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setErrorDescription(dtoBean.getErrorDescription());
            obj.setFromSerial(dtoBean.getFromSerial());
            obj.setProcessCount(dtoBean.getProcessCount());
            obj.setProcessDate(dtoBean.getProcessDate());
            obj.setProcessStatus(dtoBean.getProcessStatus());
            obj.setReqId(dtoBean.getReqId());
            obj.setSaleTransDate(dtoBean.getSaleTransDate());
            obj.setSaleTransId(dtoBean.getSaleTransId());
            obj.setSaleTransType(dtoBean.getSaleTransType());
            obj.setSaleType(dtoBean.getSaleType());
            obj.setShopId(dtoBean.getShopId());
            obj.setStaffId(dtoBean.getStaffId());
            obj.setToSerial(dtoBean.getToSerial());
        }
        return obj;
    }
}

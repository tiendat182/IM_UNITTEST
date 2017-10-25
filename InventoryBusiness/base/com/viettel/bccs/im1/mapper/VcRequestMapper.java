package com.viettel.bccs.im1.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.im1.dto.VcRequestDTO;
import com.viettel.bccs.im1.model.VcRequest;
public class VcRequestMapper extends BaseMapper<VcRequest, VcRequestDTO> {
    @Override
    public VcRequestDTO toDtoBean(VcRequest model) {
        VcRequestDTO obj = null;
        if (model != null) {
           obj =   new VcRequestDTO(); 
            obj.setCreateTime(model.getCreateTime());
            obj.setFromSerial(model.getFromSerial());
            obj.setLastProcessTime(model.getLastProcessTime());
            obj.setRangeId(model.getRangeId());
            obj.setRequestId(model.getRequestId());
            obj.setRequestType(model.getRequestType());
            obj.setShopId(model.getShopId());
            obj.setStaffId(model.getStaffId());
            obj.setStartProcessTime(model.getStartProcessTime());
            obj.setStatus(model.getStatus());
            obj.setSysCreateTime(model.getSysCreateTime());
            obj.setSysProcessTime(model.getSysProcessTime());
            obj.setToSerial(model.getToSerial());
            obj.setTransId(model.getTransId());
            obj.setUserId(model.getUserId());
        }
        return obj;
    }
    @Override
    public VcRequest toPersistenceBean(VcRequestDTO dtoBean) {
        VcRequest obj = null;
        if (dtoBean != null) {
           obj =   new VcRequest(); 
            obj.setCreateTime(dtoBean.getCreateTime());
            obj.setFromSerial(dtoBean.getFromSerial());
            obj.setLastProcessTime(dtoBean.getLastProcessTime());
            obj.setRangeId(dtoBean.getRangeId());
            obj.setRequestId(dtoBean.getRequestId());
            obj.setRequestType(dtoBean.getRequestType());
            obj.setShopId(dtoBean.getShopId());
            obj.setStaffId(dtoBean.getStaffId());
            obj.setStartProcessTime(dtoBean.getStartProcessTime());
            obj.setStatus(dtoBean.getStatus());
            obj.setSysCreateTime(dtoBean.getSysCreateTime());
            obj.setSysProcessTime(dtoBean.getSysProcessTime());
            obj.setToSerial(dtoBean.getToSerial());
            obj.setTransId(dtoBean.getTransId());
            obj.setUserId(dtoBean.getUserId());
        }
        return obj;
    }
}

package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.MtDTO;
import com.viettel.bccs.inventory.model.Mt;
public class MtMapper extends BaseMapper<Mt, MtDTO> {
    @Override
    public MtDTO toDtoBean(Mt model) {
        MtDTO obj = null;
        if (model != null) {
           obj =   new MtDTO(); 
            obj.setAppId(model.getAppId());
            obj.setAppName(model.getAppName());
            obj.setChannel(model.getChannel());
            obj.setMessage(model.getMessage());
            obj.setMoHisId(model.getMoHisId());
            obj.setMsisdn(model.getMsisdn());
            obj.setMtId(model.getMtId());
            obj.setReceiveTime(model.getReceiveTime());
            obj.setRetryNum(model.getRetryNum());
        }
        return obj;
    }
    @Override
    public Mt toPersistenceBean(MtDTO dtoBean) {
        Mt obj = null;
        if (dtoBean != null) {
           obj =   new Mt(); 
            obj.setAppId(dtoBean.getAppId());
            obj.setAppName(dtoBean.getAppName());
            obj.setChannel(dtoBean.getChannel());
            obj.setMessage(dtoBean.getMessage());
            obj.setMoHisId(dtoBean.getMoHisId());
            obj.setMsisdn(dtoBean.getMsisdn());
            obj.setMtId(dtoBean.getMtId());
            obj.setReceiveTime(dtoBean.getReceiveTime());
            obj.setRetryNum(dtoBean.getRetryNum());
        }
        return obj;
    }
}

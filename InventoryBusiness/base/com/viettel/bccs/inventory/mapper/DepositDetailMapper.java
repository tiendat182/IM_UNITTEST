package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.DepositDetailDTO;
import com.viettel.bccs.inventory.model.DepositDetail;
public class DepositDetailMapper extends BaseMapper<DepositDetail, DepositDetailDTO> {
    @Override
    public DepositDetailDTO toDtoBean(DepositDetail model) {
        DepositDetailDTO obj = null;
        if (model != null) {
           obj =   new DepositDetailDTO(); 
            obj.setAmount(model.getAmount());
            obj.setCreateDate(model.getCreateDate());
            obj.setDepositDetailId(model.getDepositDetailId());
            obj.setDepositId(model.getDepositId());
            obj.setDepositType(model.getDepositType());
            obj.setExpiredDate(model.getExpiredDate());
            obj.setPosId(model.getPosId());
            obj.setPrice(model.getPrice());
            obj.setPriceId(model.getPriceId());
            obj.setQuantity(model.getQuantity());
            obj.setSerial(model.getSerial());
            obj.setStockModelId(model.getStockModelId());
            obj.setSupplyMonth(model.getSupplyMonth());
            obj.setSupplyProgram(model.getSupplyProgram());
        }
        return obj;
    }
    @Override
    public DepositDetail toPersistenceBean(DepositDetailDTO dtoBean) {
        DepositDetail obj = null;
        if (dtoBean != null) {
           obj =   new DepositDetail(); 
            obj.setAmount(dtoBean.getAmount());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setDepositDetailId(dtoBean.getDepositDetailId());
            obj.setDepositId(dtoBean.getDepositId());
            obj.setDepositType(dtoBean.getDepositType());
            obj.setExpiredDate(dtoBean.getExpiredDate());
            obj.setPosId(dtoBean.getPosId());
            obj.setPrice(dtoBean.getPrice());
            obj.setPriceId(dtoBean.getPriceId());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setSerial(dtoBean.getSerial());
            obj.setStockModelId(dtoBean.getStockModelId());
            obj.setSupplyMonth(dtoBean.getSupplyMonth());
            obj.setSupplyProgram(dtoBean.getSupplyProgram());
        }
        return obj;
    }
}

package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.DepositDTO;
import com.viettel.bccs.inventory.model.Deposit;
public class DepositMapper extends BaseMapper<Deposit, DepositDTO> {
    @Override
    public DepositDTO toDtoBean(Deposit model) {
        DepositDTO obj = null;
        if (model != null) {
           obj =   new DepositDTO(); 
            obj.setAddress(model.getAddress());
            obj.setAmount(model.getAmount());
            obj.setBranchId(model.getBranchId());
            obj.setContractNo(model.getContractNo());
            obj.setCreateBy(model.getCreateBy());
            obj.setCreateDate(model.getCreateDate());
            obj.setCustName(model.getCustName());
            obj.setDeliverId(model.getDeliverId());
            obj.setDepositId(model.getDepositId());
            obj.setDepositTypeId(model.getDepositTypeId());
            obj.setDescription(model.getDescription());
            obj.setIdNo(model.getIdNo());
            obj.setIsdn(model.getIsdn());
            obj.setPrimaryAccount(model.getPrimaryAccount());
            obj.setReasonId(model.getReasonId());
            obj.setReceiptId(model.getReceiptId());
            obj.setRequestId(model.getRequestId());
            obj.setShopId(model.getShopId());
            obj.setStaffId(model.getStaffId());
            obj.setStatus(model.getStatus());
            obj.setStockTransId(model.getStockTransId());
            obj.setSubId(model.getSubId());
            obj.setTelecomServiceId(model.getTelecomServiceId());
            obj.setTin(model.getTin());
            obj.setType(model.getType());
        }
        return obj;
    }
    @Override
    public Deposit toPersistenceBean(DepositDTO dtoBean) {
        Deposit obj = null;
        if (dtoBean != null) {
           obj =   new Deposit(); 
            obj.setAddress(dtoBean.getAddress());
            obj.setAmount(dtoBean.getAmount());
            obj.setBranchId(dtoBean.getBranchId());
            obj.setContractNo(dtoBean.getContractNo());
            obj.setCreateBy(dtoBean.getCreateBy());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCustName(dtoBean.getCustName());
            obj.setDeliverId(dtoBean.getDeliverId());
            obj.setDepositId(dtoBean.getDepositId());
            obj.setDepositTypeId(dtoBean.getDepositTypeId());
            obj.setDescription(dtoBean.getDescription());
            obj.setIdNo(dtoBean.getIdNo());
            obj.setIsdn(dtoBean.getIsdn());
            obj.setPrimaryAccount(dtoBean.getPrimaryAccount());
            obj.setReasonId(dtoBean.getReasonId());
            obj.setReceiptId(dtoBean.getReceiptId());
            obj.setRequestId(dtoBean.getRequestId());
            obj.setShopId(dtoBean.getShopId());
            obj.setStaffId(dtoBean.getStaffId());
            obj.setStatus(dtoBean.getStatus());
            obj.setStockTransId(dtoBean.getStockTransId());
            obj.setSubId(dtoBean.getSubId());
            obj.setTelecomServiceId(dtoBean.getTelecomServiceId());
            obj.setTin(dtoBean.getTin());
            obj.setType(dtoBean.getType());
        }
        return obj;
    }
}

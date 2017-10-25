package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ReceiptExpenseDTO;
import com.viettel.bccs.inventory.model.ReceiptExpense;
public class ReceiptExpenseMapper extends BaseMapper<ReceiptExpense, ReceiptExpenseDTO> {
    @Override
    public ReceiptExpenseDTO toDtoBean(ReceiptExpense model) {
        ReceiptExpenseDTO obj = null;
        if (model != null) {
           obj =   new ReceiptExpenseDTO(); 
            obj.setAddress(model.getAddress());
            obj.setAmount(model.getAmount());
            obj.setApproverDate(model.getApproverDate());
            obj.setApproverStaffId(model.getApproverStaffId());
            obj.setCreateDatetime(model.getCreateDatetime());
            obj.setDebt(model.getDebt());
            obj.setDeliverer(model.getDeliverer());
            obj.setDelivererShopId(model.getDelivererShopId());
            obj.setDelivererStaffId(model.getDelivererStaffId());
            obj.setDestroyDate(model.getDestroyDate());
            obj.setDestroyStaffId(model.getDestroyStaffId());
            obj.setNotes(model.getNotes());
            obj.setOwn(model.getOwn());
            obj.setPayMethod(model.getPayMethod());
            obj.setReasonId(model.getReasonId());
            obj.setReceiptDate(model.getReceiptDate());
            obj.setReceiptId(model.getReceiptId());
            obj.setReceiptNo(model.getReceiptNo());
            obj.setReceiptTypeId(model.getReceiptTypeId());
            obj.setShopId(model.getShopId());
            obj.setStaffId(model.getStaffId());
            obj.setStatus(model.getStatus());
            obj.setTelecomServiceId(model.getTelecomServiceId());
            obj.setType(model.getType());
            obj.setUsername(model.getUsername());
        }
        return obj;
    }
    @Override
    public ReceiptExpense toPersistenceBean(ReceiptExpenseDTO dtoBean) {
        ReceiptExpense obj = null;
        if (dtoBean != null) {
           obj =   new ReceiptExpense(); 
            obj.setAddress(dtoBean.getAddress());
            obj.setAmount(dtoBean.getAmount());
            obj.setApproverDate(dtoBean.getApproverDate());
            obj.setApproverStaffId(dtoBean.getApproverStaffId());
            obj.setCreateDatetime(dtoBean.getCreateDatetime());
            obj.setDebt(dtoBean.getDebt());
            obj.setDeliverer(dtoBean.getDeliverer());
            obj.setDelivererShopId(dtoBean.getDelivererShopId());
            obj.setDelivererStaffId(dtoBean.getDelivererStaffId());
            obj.setDestroyDate(dtoBean.getDestroyDate());
            obj.setDestroyStaffId(dtoBean.getDestroyStaffId());
            obj.setNotes(dtoBean.getNotes());
            obj.setOwn(dtoBean.getOwn());
            obj.setPayMethod(dtoBean.getPayMethod());
            obj.setReasonId(dtoBean.getReasonId());
            obj.setReceiptDate(dtoBean.getReceiptDate());
            obj.setReceiptId(dtoBean.getReceiptId());
            obj.setReceiptNo(dtoBean.getReceiptNo());
            obj.setReceiptTypeId(dtoBean.getReceiptTypeId());
            obj.setShopId(dtoBean.getShopId());
            obj.setStaffId(dtoBean.getStaffId());
            obj.setStatus(dtoBean.getStatus());
            obj.setTelecomServiceId(dtoBean.getTelecomServiceId());
            obj.setType(dtoBean.getType());
            obj.setUsername(dtoBean.getUsername());
        }
        return obj;
    }
}

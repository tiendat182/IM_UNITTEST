package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ReportChangeHandsetDTO;
import com.viettel.bccs.inventory.model.ReportChangeHandset;
public class ReportChangeHandsetMapper extends BaseMapper<ReportChangeHandset, ReportChangeHandsetDTO> {
    @Override
    public ReportChangeHandsetDTO toDtoBean(ReportChangeHandset model) {
        ReportChangeHandsetDTO obj = null;
        if (model != null) {
           obj =   new ReportChangeHandsetDTO(); 
            obj.setAdjustAmount(model.getAdjustAmount());
            obj.setChangeType(model.getChangeType());
            obj.setCreateDate(model.getCreateDate());
            obj.setCustName(model.getCustName());
            obj.setCustTel(model.getCustTel());
            obj.setDamageGoodStatus(model.getDamageGoodStatus());
            obj.setDevStaffId(model.getDevStaffId());
            obj.setProdOfferIdNew(model.getProdOfferIdNew());
            obj.setProdOfferIdOld(model.getProdOfferIdOld());
            obj.setReportChangeHandsetId(model.getReportChangeHandsetId());
            obj.setSerialNew(model.getSerialNew());
            obj.setSerialOld(model.getSerialOld());
            obj.setShopId(model.getShopId());
            obj.setStaffId(model.getStaffId());
            obj.setStockTransId(model.getStockTransId());
        }
        return obj;
    }
    @Override
    public ReportChangeHandset toPersistenceBean(ReportChangeHandsetDTO dtoBean) {
        ReportChangeHandset obj = null;
        if (dtoBean != null) {
           obj =   new ReportChangeHandset(); 
            obj.setAdjustAmount(dtoBean.getAdjustAmount());
            obj.setChangeType(dtoBean.getChangeType());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCustName(dtoBean.getCustName());
            obj.setCustTel(dtoBean.getCustTel());
            obj.setDamageGoodStatus(dtoBean.getDamageGoodStatus());
            obj.setDevStaffId(dtoBean.getDevStaffId());
            obj.setProdOfferIdNew(dtoBean.getProdOfferIdNew());
            obj.setProdOfferIdOld(dtoBean.getProdOfferIdOld());
            obj.setReportChangeHandsetId(dtoBean.getReportChangeHandsetId());
            obj.setSerialNew(dtoBean.getSerialNew());
            obj.setSerialOld(dtoBean.getSerialOld());
            obj.setShopId(dtoBean.getShopId());
            obj.setStaffId(dtoBean.getStaffId());
            obj.setStockTransId(dtoBean.getStockTransId());
        }
        return obj;
    }
}

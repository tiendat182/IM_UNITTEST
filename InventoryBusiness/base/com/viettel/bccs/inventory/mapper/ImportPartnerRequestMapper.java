package com.viettel.bccs.inventory.mapper;

import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.model.ImportPartnerRequest;

public class ImportPartnerRequestMapper extends BaseMapper<ImportPartnerRequest, ImportPartnerRequestDTO> {
    @Override
    public ImportPartnerRequestDTO toDtoBean(ImportPartnerRequest model) {
        ImportPartnerRequestDTO obj = null;
        if (model != null) {
            obj = new ImportPartnerRequestDTO();
            obj.setApproveStaffId(model.getApproveStaffId());
            obj.setContractCode(model.getContractCode());
            obj.setContractDate(model.getContractDate());
            obj.setContractImportDate(model.getContractImportDate());
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateShopId(model.getCreateShopId());
            obj.setCreateStaffId(model.getCreateStaffId());
            obj.setCurrencyType(model.getCurrencyType());
            obj.setDeliveryLocation(model.getDeliveryLocation());
            //obj.setDocumentContent(model.getDocumentContent()); -- mang byte lay rieng
            obj.setDocumentName(model.getDocumentName());
            obj.setImportDate(model.getImportDate());
            obj.setImportPartnerRequestId(model.getImportPartnerRequestId());
            obj.setImportStaffId(model.getImportStaffId());
            obj.setLastModify(model.getLastModify());
            obj.setPartnerId(model.getPartnerId());
            obj.setPoCode(model.getPoCode());
            obj.setPoDate(model.getPoDate());
            obj.setReason(model.getReason());
            obj.setRequestCode(model.getRequestCode());
            obj.setRequestDate(model.getRequestDate());
            obj.setStatus(model.getStatus());
            obj.setToOwnerId(model.getToOwnerId());
            obj.setToOwnerType(model.getToOwnerType());
            obj.setUnitPrice(model.getUnitPrice());
        }
        return obj;
    }

    @Override
    public ImportPartnerRequest toPersistenceBean(ImportPartnerRequestDTO dtoBean) {
        ImportPartnerRequest obj = null;
        if (dtoBean != null) {
            obj = new ImportPartnerRequest();
            obj.setApproveStaffId(dtoBean.getApproveStaffId());
            obj.setContractCode(dtoBean.getContractCode());
            obj.setContractDate(dtoBean.getContractDate());
            obj.setContractImportDate(dtoBean.getContractImportDate());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateShopId(dtoBean.getCreateShopId());
            obj.setCreateStaffId(dtoBean.getCreateStaffId());
            obj.setCurrencyType(dtoBean.getCurrencyType());
            obj.setDeliveryLocation(dtoBean.getDeliveryLocation());
            obj.setDocumentContent(dtoBean.getDocumentContent());
            obj.setDocumentName(dtoBean.getDocumentName());
            obj.setImportDate(dtoBean.getImportDate());
            obj.setImportPartnerRequestId(dtoBean.getImportPartnerRequestId());
            obj.setImportStaffId(dtoBean.getImportStaffId());
            obj.setLastModify(dtoBean.getLastModify());
            obj.setPartnerId(dtoBean.getPartnerId());
            obj.setPoCode(dtoBean.getPoCode());
            obj.setPoDate(dtoBean.getPoDate());
            obj.setReason(dtoBean.getReason());
            obj.setRequestCode(dtoBean.getRequestCode());
            obj.setRequestDate(dtoBean.getRequestDate());
            obj.setStatus(dtoBean.getStatus());
            obj.setToOwnerId(dtoBean.getToOwnerId());
            obj.setToOwnerType(dtoBean.getToOwnerType());
            obj.setUnitPrice(dtoBean.getUnitPrice());
        }
        return obj;
    }
}

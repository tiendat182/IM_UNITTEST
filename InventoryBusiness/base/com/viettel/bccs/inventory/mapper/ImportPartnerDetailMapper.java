package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.model.ImportPartnerDetail;
public class ImportPartnerDetailMapper extends BaseMapper<ImportPartnerDetail, ImportPartnerDetailDTO> {
    @Override
    public ImportPartnerDetailDTO toDtoBean(ImportPartnerDetail model) {
        ImportPartnerDetailDTO obj = null;
        if (model != null) {
           obj =   new ImportPartnerDetailDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setImportPartnerDetailId(model.getImportPartnerDetailId());
            obj.setImportPartnerRequestId(model.getImportPartnerRequestId());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setQuantity(model.getQuantity());
            obj.setStateId(model.getStateId());
        }
        return obj;
    }
    @Override
    public ImportPartnerDetail toPersistenceBean(ImportPartnerDetailDTO dtoBean) {
        ImportPartnerDetail obj = null;
        if (dtoBean != null) {
           obj =   new ImportPartnerDetail(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setImportPartnerDetailId(dtoBean.getImportPartnerDetailId());
            obj.setImportPartnerRequestId(dtoBean.getImportPartnerRequestId());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setStateId(dtoBean.getStateId());
        }
        return obj;
    }
}

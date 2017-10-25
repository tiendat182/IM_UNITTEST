package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.model.Partner;
public class PartnerMapper extends BaseMapper<Partner, PartnerDTO> {
    @Override
    public PartnerDTO toDtoBean(Partner model) {
        PartnerDTO obj = null;
        if (model != null) {
           obj =   new PartnerDTO(); 
            obj.setAddress(model.getAddress());
            obj.setContactName(model.getContactName());
            obj.setEndDate(model.getEndDate());
            obj.setFax(model.getFax());
            obj.setPartnerCode(model.getPartnerCode());
            obj.setPartnerId(model.getPartnerId());
            obj.setPartnerName(model.getPartnerName());
            obj.setPartnerType(model.getPartnerType());
            obj.setPhone(model.getPhone());
            obj.setStaDate(model.getStaDate());
            obj.setStatus(model.getStatus());
        }
        return obj;
    }
    @Override
    public Partner toPersistenceBean(PartnerDTO dtoBean) {
        Partner obj = null;
        if (dtoBean != null) {
           obj =   new Partner(); 
            obj.setAddress(dtoBean.getAddress());
            obj.setContactName(dtoBean.getContactName());
            obj.setEndDate(dtoBean.getEndDate());
            obj.setFax(dtoBean.getFax());
            obj.setPartnerCode(dtoBean.getPartnerCode());
            obj.setPartnerId(dtoBean.getPartnerId());
            obj.setPartnerName(dtoBean.getPartnerName());
            obj.setPartnerType(dtoBean.getPartnerType());
            obj.setPhone(dtoBean.getPhone());
            obj.setStaDate(dtoBean.getStaDate());
            obj.setStatus(dtoBean.getStatus());
        }
        return obj;
    }
}

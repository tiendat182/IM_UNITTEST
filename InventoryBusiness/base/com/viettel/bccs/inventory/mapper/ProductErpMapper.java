package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.ProductErpDTO;
import com.viettel.bccs.inventory.model.ProductErp;
public class ProductErpMapper extends BaseMapper<ProductErp, ProductErpDTO> {
    @Override
    public ProductErpDTO toDtoBean(ProductErp model) {
        ProductErpDTO obj = null;
        if (model != null) {
           obj =   new ProductErpDTO(); 
            obj.setCreateDate(model.getCreateDate());
            obj.setCreateUser(model.getCreateUser());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerType(model.getOwnerType());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setProductErpId(model.getProductErpId());
            obj.setQuantity(model.getQuantity());
            obj.setUpdateDatetime(model.getUpdateDatetime());
            obj.setUpdateUser(model.getUpdateUser());
        }
        return obj;
    }
    @Override
    public ProductErp toPersistenceBean(ProductErpDTO dtoBean) {
        ProductErp obj = null;
        if (dtoBean != null) {
           obj =   new ProductErp(); 
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCreateUser(dtoBean.getCreateUser());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setProductErpId(dtoBean.getProductErpId());
            obj.setQuantity(dtoBean.getQuantity());
            obj.setUpdateDatetime(dtoBean.getUpdateDatetime());
            obj.setUpdateUser(dtoBean.getUpdateUser());
        }
        return obj;
    }
}

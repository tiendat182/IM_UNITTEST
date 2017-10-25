package com.viettel.bccs.inventory.mapper;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.bccs.inventory.dto.StockTotalAuditDTO;
import com.viettel.bccs.inventory.model.StockTotalAudit;
public class StockTotalAuditMapper extends BaseMapper<StockTotalAudit, StockTotalAuditDTO> {
    @Override
    public StockTotalAuditDTO toDtoBean(StockTotalAudit model) {
        StockTotalAuditDTO obj = null;
        if (model != null) {
           obj =   new StockTotalAuditDTO(); 
            obj.setAvailableQuantity(model.getAvailableQuantity());
            obj.setAvailableQuantityAf(model.getAvailableQuantityAf());
            obj.setAvailableQuantityBf(model.getAvailableQuantityBf());
            obj.setCreateDate(model.getCreateDate());
            obj.setCurrentQuantity(model.getCurrentQuantity());
            obj.setCurrentQuantityAf(model.getCurrentQuantityAf());
            obj.setCurrentQuantityBf(model.getCurrentQuantityBf());
            obj.setDescription(model.getDescription());
            obj.setHangQuantity(model.getHangQuantity());
            obj.setHangQuantityAf(model.getHangQuantityAf());
            obj.setHangQuantityBf(model.getHangQuantityBf());
            obj.setId(model.getId());
            obj.setOwnerCode(model.getOwnerCode());
            obj.setOwnerId(model.getOwnerId());
            obj.setOwnerName(model.getOwnerName());
            obj.setOwnerType(model.getOwnerType());
            obj.setProdOfferCode(model.getProdOfferCode());
            obj.setProdOfferId(model.getProdOfferId());
            obj.setProdOfferName(model.getProdOfferName());
            obj.setProdOfferTypeId(model.getProdOfferTypeId());
            obj.setReasonId(model.getReasonId());
            obj.setReasonName(model.getReasonName());
            obj.setShopCode(model.getShopCode());
            obj.setShopCodeLv1(model.getShopCodeLv1());
            obj.setShopCodeLv2(model.getShopCodeLv2());
            obj.setShopCodeLv3(model.getShopCodeLv3());
            obj.setShopCodeLv4(model.getShopCodeLv4());
            obj.setShopCodeLv5(model.getShopCodeLv5());
            obj.setShopCodeLv6(model.getShopCodeLv6());
            obj.setShopId(model.getShopId());
            obj.setShopIdLv1(model.getShopIdLv1());
            obj.setShopIdLv2(model.getShopIdLv2());
            obj.setShopIdLv3(model.getShopIdLv3());
            obj.setShopIdLv4(model.getShopIdLv4());
            obj.setShopIdLv5(model.getShopIdLv5());
            obj.setShopIdLv6(model.getShopIdLv6());
            obj.setShopName(model.getShopName());
            obj.setShopNameLv1(model.getShopNameLv1());
            obj.setShopNameLv2(model.getShopNameLv2());
            obj.setShopNameLv3(model.getShopNameLv3());
            obj.setShopNameLv4(model.getShopNameLv4());
            obj.setShopNameLv5(model.getShopNameLv5());
            obj.setShopNameLv6(model.getShopNameLv6());
            obj.setSourceType(model.getSourceType());
            obj.setStateId(model.getStateId());
            obj.setStatus(model.getStatus());
            obj.setStickCode(model.getStickCode());
            obj.setSynStatus(model.getSynStatus());
            obj.setTransCode(model.getTransCode());
            obj.setTransId(model.getTransId());
            obj.setTransType(model.getTransType());
            obj.setUserCode(model.getUserCode());
            obj.setUserId(model.getUserId());
            obj.setUserName(model.getUserName());
        }
        return obj;
    }
    @Override
    public StockTotalAudit toPersistenceBean(StockTotalAuditDTO dtoBean) {
        StockTotalAudit obj = null;
        if (dtoBean != null) {
           obj =   new StockTotalAudit(); 
            obj.setAvailableQuantity(dtoBean.getAvailableQuantity());
            obj.setAvailableQuantityAf(dtoBean.getAvailableQuantityAf());
            obj.setAvailableQuantityBf(dtoBean.getAvailableQuantityBf());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setCurrentQuantity(dtoBean.getCurrentQuantity());
            obj.setCurrentQuantityAf(dtoBean.getCurrentQuantityAf());
            obj.setCurrentQuantityBf(dtoBean.getCurrentQuantityBf());
            obj.setDescription(dtoBean.getDescription());
            obj.setHangQuantity(dtoBean.getHangQuantity());
            obj.setHangQuantityAf(dtoBean.getHangQuantityAf());
            obj.setHangQuantityBf(dtoBean.getHangQuantityBf());
            obj.setId(dtoBean.getId());
            obj.setOwnerCode(dtoBean.getOwnerCode());
            obj.setOwnerId(dtoBean.getOwnerId());
            obj.setOwnerName(dtoBean.getOwnerName());
            obj.setOwnerType(dtoBean.getOwnerType());
            obj.setProdOfferCode(dtoBean.getProdOfferCode());
            obj.setProdOfferId(dtoBean.getProdOfferId());
            obj.setProdOfferName(dtoBean.getProdOfferName());
            obj.setProdOfferTypeId(dtoBean.getProdOfferTypeId());
            obj.setReasonId(dtoBean.getReasonId());
            obj.setReasonName(dtoBean.getReasonName());
            obj.setShopCode(dtoBean.getShopCode());
            obj.setShopCodeLv1(dtoBean.getShopCodeLv1());
            obj.setShopCodeLv2(dtoBean.getShopCodeLv2());
            obj.setShopCodeLv3(dtoBean.getShopCodeLv3());
            obj.setShopCodeLv4(dtoBean.getShopCodeLv4());
            obj.setShopCodeLv5(dtoBean.getShopCodeLv5());
            obj.setShopCodeLv6(dtoBean.getShopCodeLv6());
            obj.setShopId(dtoBean.getShopId());
            obj.setShopIdLv1(dtoBean.getShopIdLv1());
            obj.setShopIdLv2(dtoBean.getShopIdLv2());
            obj.setShopIdLv3(dtoBean.getShopIdLv3());
            obj.setShopIdLv4(dtoBean.getShopIdLv4());
            obj.setShopIdLv5(dtoBean.getShopIdLv5());
            obj.setShopIdLv6(dtoBean.getShopIdLv6());
            obj.setShopName(dtoBean.getShopName());
            obj.setShopNameLv1(dtoBean.getShopNameLv1());
            obj.setShopNameLv2(dtoBean.getShopNameLv2());
            obj.setShopNameLv3(dtoBean.getShopNameLv3());
            obj.setShopNameLv4(dtoBean.getShopNameLv4());
            obj.setShopNameLv5(dtoBean.getShopNameLv5());
            obj.setShopNameLv6(dtoBean.getShopNameLv6());
            obj.setSourceType(dtoBean.getSourceType());
            obj.setStateId(dtoBean.getStateId());
            obj.setStatus(dtoBean.getStatus());
            obj.setStickCode(dtoBean.getStickCode());
            obj.setSynStatus(dtoBean.getSynStatus());
            obj.setTransCode(dtoBean.getTransCode());
            obj.setTransId(dtoBean.getTransId());
            obj.setTransType(dtoBean.getTransType());
            obj.setUserCode(dtoBean.getUserCode());
            obj.setUserId(dtoBean.getUserId());
            obj.setUserName(dtoBean.getUserName());
        }
        return obj;
    }
}

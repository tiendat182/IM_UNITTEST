package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;

/**
 * dto hung data tu webservice cua KTTS
 * @author thanhnt77
 */
public class PartnerShipmentListWsDTO extends BaseDTO {

    private List<PartnerShipmentWsDTO> lsPartnerDetail = Lists.newArrayList();

    public PartnerShipmentListWsDTO() {
    }

    public List<PartnerShipmentWsDTO> getLsPartnerDetail() {
        return lsPartnerDetail;
    }

    public void setLsPartnerDetail(List<PartnerShipmentWsDTO> lsPartnerDetail) {
        this.lsPartnerDetail = lsPartnerDetail;
    }
}

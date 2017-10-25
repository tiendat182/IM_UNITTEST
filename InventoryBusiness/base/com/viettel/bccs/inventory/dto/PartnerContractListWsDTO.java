package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;

/**
 * dto hung data tu webservice cua KTTS
 * @author thanhnt77
 */
public class PartnerContractListWsDTO extends BaseDTO {

    List<PartnerContractWsDTO> list = Lists.newArrayList();

    public List<PartnerContractWsDTO> getList() {
        return list;
    }

    public void setList(List<PartnerContractWsDTO> list) {
        this.list = list;
    }
}

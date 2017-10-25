package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by hoangnt14 on 12/26/2016.
 */
public class SearchIsdnDTO extends BaseMessage {

    public SearchIsdnDTO() {
    }

    private List<VStockNumberDTO> lstIsdn;

    public List<VStockNumberDTO> getLstIsdn() {
        return lstIsdn;
    }

    public void setLstIsdn(List<VStockNumberDTO> lstIsdn) {
        this.lstIsdn = lstIsdn;
    }

    public SearchIsdnDTO(List<VStockNumberDTO> lstIsdn, boolean success, String errCode, String description, String... paramsMsg) {
        super(success, errCode, description, paramsMsg);
        this.lstIsdn = lstIsdn;
    }
}

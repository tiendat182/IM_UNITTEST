package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by DungPT16 on 3/19/2016.
 */
public class LookupIsdnDTO extends BaseMessage {

    public LookupIsdnDTO() {
    }

    private List<String> lstIsdn;

    public List<String> getLstIsdn() {
        return lstIsdn;
    }

    public void setLstIsdn(List<String> lstIsdn) {
        this.lstIsdn = lstIsdn;
    }

    public LookupIsdnDTO(List<String> lstIsdn, boolean success, String errCode, String description, String... paramsMsg) {
        super(success, errCode, description, paramsMsg);
        this.lstIsdn = lstIsdn;
    }
}

package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by hoangnt14 on 12/27/2016.
 */
public class StampInforDTO extends BaseMessage {

    private List<StampDTO> lstStamps;

    public List<StampDTO> getLstStamps() {
        return lstStamps;
    }

    public void setLstStamps(List<StampDTO> lstStamps) {
        this.lstStamps = lstStamps;
    }

    public StampInforDTO() {

    }

    public StampInforDTO(List<StampDTO> lstStamps, boolean success, String errCode, String description, String... paramsMsg) {
        super(success, errCode, description, paramsMsg);
        this.lstStamps = lstStamps;
    }
}

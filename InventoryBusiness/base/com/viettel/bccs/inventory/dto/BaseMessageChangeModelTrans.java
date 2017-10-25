package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by DungHa7 on 14/10/2016.
 */
public class BaseMessageChangeModelTrans extends BaseMessage {
    private List<ChangeModelTransDTO> lstChangeModelTransDTOs;

    public List<ChangeModelTransDTO> getLstChangeModelTransDTOs() {
        return lstChangeModelTransDTOs;
    }

    public void setLstChangeModelTransDTOs(List<ChangeModelTransDTO> lstChangeModelTransDTOs) {
        this.lstChangeModelTransDTOs = lstChangeModelTransDTOs;
    }
}

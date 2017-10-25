package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by DungHa7 on 14/10/2016.
 */
public class BaseMessageChangeModelTransDetail extends BaseMessage {
    private List<ChangeModelTransDetailDTO> lstChangeModelTransDetailDTOs;

    public List<ChangeModelTransDetailDTO> getLstChangeModelTransDetailDTOs() {
        return lstChangeModelTransDetailDTOs;
    }

    public void setLstChangeModelTransDetailDTOs(List<ChangeModelTransDetailDTO> lstChangeModelTransDetailDTOs) {
        this.lstChangeModelTransDetailDTOs = lstChangeModelTransDetailDTOs;
    }
}

package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by DungHa7 on 14/10/2016.
 */
public class BaseMessageChangeModelTransSerial extends BaseMessage {
    private List<ChangeModelTransSerialDTO> lstChangeModelTransSerialDTOs;

    public List<ChangeModelTransSerialDTO> getLstChangeModelTransSerialDTOs() {
        return lstChangeModelTransSerialDTOs;
    }

    public void setLstChangeModelTransSerialDTOs(List<ChangeModelTransSerialDTO> lstChangeModelTransSerialDTOs) {
        this.lstChangeModelTransSerialDTOs = lstChangeModelTransSerialDTOs;
    }
}

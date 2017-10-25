package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by DungHa7 on 14/10/2016.
 */
public class BaseMessageProductExchange extends BaseMessage {
    private List<ProductExchangeDTO> lstProductExchangeDTO;

    public List<ProductExchangeDTO> getLstProductExchangeDTO() {
        return lstProductExchangeDTO;
    }

    public void setLstProductExchangeDTO(List<ProductExchangeDTO> lstProductExchangeDTO) {
        this.lstProductExchangeDTO = lstProductExchangeDTO;
    }
}

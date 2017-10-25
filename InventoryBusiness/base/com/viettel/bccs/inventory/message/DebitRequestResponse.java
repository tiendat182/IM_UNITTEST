package com.viettel.bccs.inventory.message;

import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.fw.dto.BaseMessage;

/**
 * @author luannt23 on 12/12/2015.
 */
public class DebitRequestResponse extends BaseMessage {
    private DebitRequestDTO debitRequestDTO;

    public DebitRequestDTO getDebitRequestDTO() {
        return debitRequestDTO;
    }

    public void setDebitRequestDTO(DebitRequestDTO debitRequestDTO) {
        this.debitRequestDTO = debitRequestDTO;
    }
}

package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ImResult extends BaseDTO implements Serializable {
    private boolean result;
    private String description;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

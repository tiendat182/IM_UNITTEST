package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hellpoethero on 15/09/2016.
 */
public class KcsDTO  extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String code;
    private List<String> serialList;
    private List<Long> stateIdList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getSerialList() {
        return serialList;
    }

    public void setSerialList(List<String> serialList) {
        this.serialList = serialList;
    }

    public List<Long> getStateIdList() {
        return stateIdList;
    }

    public void setStateIdList(List<Long> stateIdList) {
        this.stateIdList = stateIdList;
    }
}

package com.viettel.bccs.inventory.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author thanhnt77
 * 
 */
public class LookupSerialByFileValidDTO implements Serializable{

    private String serial;
    private String errMsg;
    private int index;

    public LookupSerialByFileValidDTO() {
    }

    public LookupSerialByFileValidDTO(String serial, int index) {
        this.serial = serial;
        this.index = index;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

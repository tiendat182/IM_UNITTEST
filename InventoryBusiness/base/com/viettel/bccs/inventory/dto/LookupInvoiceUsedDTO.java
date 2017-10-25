package com.viettel.bccs.inventory.dto;

import com.google.common.base.Joiner;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;

/**
 * Created by hellp on 9/27/2016.
 */
public class LookupInvoiceUsedDTO extends BaseDTO implements Serializable {

    public LookupInvoiceUsedDTO() {
    }

    public LookupInvoiceUsedDTO(String serial) {
        this.serial = serial;
    }

    public String getKeySet() {
        return keySet;
    }

    private String fromInvoice;
    private String toInvoice;
    private String serial;
    private String name;

    public LookupInvoiceUsedDTO(String serial, String name) {
        this.serial = serial;
        this.name = name;
    }
    public String getLabel() {
        return Joiner.on(" - ").skipNulls().join(serial, name);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getFromInvoice() {
        return fromInvoice;
    }

    public void setFromInvoice(String fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    public String getToInvoice() {
        return toInvoice;
    }

    public void setToInvoice(String toInvoice) {
        this.toInvoice = toInvoice;
    }
}

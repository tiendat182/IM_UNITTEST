package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

/**
 * Created by ChungNV7 on 12/31/2015.
 */
public class DeleteNumberDTO extends BaseDTO {

    private Long serviceType;
    private Long fromISDN;
    private Long toISDN;
    private Long total;
    private String result = "0";
    private String resultDesc;
    private String rowKey;
    private String note;

    public DeleteNumberDTO() {
        //
    }

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
    }

    public Long getFromISDN() {
        return fromISDN;
    }

    public void setFromISDN(Long fromISDN) {
        this.fromISDN = fromISDN;
    }

    public Long getToISDN() {
        return toISDN;
    }

    public void setToISDN(Long toISDN) {
        this.toISDN = toISDN;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return rowKey + "";
    }
}

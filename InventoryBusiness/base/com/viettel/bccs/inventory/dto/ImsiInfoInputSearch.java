package com.viettel.bccs.inventory.dto;

import java.util.Date;

/**
 * Created by HoangAnh on 5/29/2017.
 */
public class ImsiInfoInputSearch {
    private String imsiFrom;
    private String imsiTo;
    private Date fromDate;
    private Date toDate;
    private String docNo;
    private String status;

    public String getImsiFrom() {
        return imsiFrom;
    }

    public void setImsiFrom(String imsiFrom) {
        this.imsiFrom = imsiFrom;
    }

    public String getImsiTo() {
        return imsiTo;
    }

    public void setImsiTo(String imsiTo) {
        this.imsiTo = imsiTo;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

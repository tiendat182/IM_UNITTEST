package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vanho on 29/05/2017.
 */
public class OutputImsiProduceDTO extends BaseDTO implements Serializable {

    private String startImsi;
    private String endImsi;
    private Long quatity;
    private Date startDate;
    private Date endDate;
    private Long status;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStartImsi() {
        return startImsi;
    }

    public void setStartImsi(String startImsi) {
        this.startImsi = startImsi;
    }

    public String getEndImsi() {
        return endImsi;
    }

    public void setEndImsi(String endImsi) {
        this.endImsi = endImsi;
    }

    public Long getQuatity() {
        return quatity;
    }

    public void setQuatity(Long quatity) {
        this.quatity = quatity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

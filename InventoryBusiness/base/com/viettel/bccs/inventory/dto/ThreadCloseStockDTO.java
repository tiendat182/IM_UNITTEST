package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;

public class ThreadCloseStockDTO extends BaseDTO implements Serializable {
    public String getKeySet() {
        return keySet;
    }

    private String description;
    private Date endActionTime;
    private Long id;
    private Date processDate;
    private Date startActionTime;
    private String startHour;
    private String startMinute;
    private String startSecond;
    private Long status;
    private String threadName;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndActionTime() {
        return this.endActionTime;
    }

    public void setEndActionTime(Date endActionTime) {
        this.endActionTime = endActionTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getProcessDate() {
        return this.processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getStartActionTime() {
        return this.startActionTime;
    }

    public void setStartActionTime(Date startActionTime) {
        this.startActionTime = startActionTime;
    }

    public Long getStatus() {
        return this.status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(String startSecond) {
        this.startSecond = startSecond;
    }
}

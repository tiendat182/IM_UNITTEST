/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "THREAD_CLOSE_STOCK")
@NamedQueries({
        @NamedQuery(name = "ThreadCloseStock.findAll", query = "SELECT t FROM ThreadCloseStock t")})
public class ThreadCloseStock implements Serializable {
    public static enum COLUMNS {DESCRIPTION, ENDACTIONTIME, ID, PROCESSDATE, STARTACTIONTIME, STARTHOUR, STARTMINUTE, STARTSECOND, STATUS, THREADNAME, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "THREAD_CLOSE_STOCK_ID_GENERATOR", sequenceName = "THREAD_CLOSE_STOCK_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "THREAD_CLOSE_STOCK_ID_GENERATOR")
    private Long id;
    @Column(name = "THREAD_NAME")
    private String threadName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PROCESS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processDate;
    @Column(name = "START_HOUR")
    private String startHour;
    @Column(name = "START_MINUTE")
    private String startMinute;
    @Column(name = "START_SECOND")
    private String startSecond;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "START_ACTION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startActionTime;
    @Column(name = "END_ACTION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endActionTime;

    public ThreadCloseStock() {
    }

    public ThreadCloseStock(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getStartActionTime() {
        return startActionTime;
    }

    public void setStartActionTime(Date startActionTime) {
        this.startActionTime = startActionTime;
    }

    public Date getEndActionTime() {
        return endActionTime;
    }

    public void setEndActionTime(Date endActionTime) {
        this.endActionTime = endActionTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThreadCloseStock)) {
            return false;
        }
        ThreadCloseStock other = (ThreadCloseStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ThreadCloseStock[ id=" + id + " ]";
    }

}

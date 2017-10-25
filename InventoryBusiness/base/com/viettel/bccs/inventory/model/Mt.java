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
 * @author sinhhv
 */
@Entity
@Table(name = "MT")
public class Mt implements Serializable {
    public static enum COLUMNS {APPID, APPNAME, CHANNEL, MESSAGE, MOHISID, MSISDN, MTID, RECEIVETIME, RETRYNUM, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "MT_ID")
    @SequenceGenerator(name = "MT_ID_GENERATOR", sequenceName = "MT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MT_ID_GENERATOR")
    private Long mtId;
    @Column(name = "MSISDN")
    private String msisdn;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "MO_HIS_ID")
    private Long moHisId;
    @Column(name = "RETRY_NUM")
    private Long retryNum;
    @Column(name = "APP_ID")
    private String appId;
    @Column(name = "RECEIVE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTime;
    @Column(name = "CHANNEL")
    private String channel;
    @Column(name = "APP_NAME")
    private String appName;

    public Mt() {
    }

    public Mt(Long mtId) {
        this.mtId = mtId;
    }

    public Long getMtId() {
        return mtId;
    }

    public void setMtId(Long mtId) {
        this.mtId = mtId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMoHisId() {
        return moHisId;
    }

    public void setMoHisId(Long moHisId) {
        this.moHisId = moHisId;
    }

    public Long getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(Long retryNum) {
        this.retryNum = retryNum;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mtId != null ? mtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mt)) {
            return false;
        }
        Mt other = (Mt) object;
        if ((this.mtId == null && other.mtId != null) || (this.mtId != null && !this.mtId.equals(other.mtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.Mt[ mtId=" + mtId + " ]";
    }

}

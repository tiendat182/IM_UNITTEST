/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anhvv4
 */
@Entity
@Table(name = "REASON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reason.findAll", query = "SELECT r FROM Reason r")})
public class Reason implements Serializable {
public static enum COLUMNS {HAVEMONEY, REASONCODE, REASONDESCRIPTION, REASONID, REASONNAME, REASONSTATUS, REASONTYPE, SERVICE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REASON_ID")
    @SequenceGenerator(name = "REASON_ID_GENERATOR", sequenceName = "REASON_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REASON_ID_GENERATOR")
    private Long reasonId;
    @Basic(optional = false)
    @Column(name = "REASON_CODE")
    private String reasonCode;
    @Basic(optional = false)
    @Column(name = "REASON_NAME")
    private String reasonName;
    @Basic(optional = false)
    @Column(name = "REASON_STATUS")
    private String reasonStatus;
    @Column(name = "REASON_DESCRIPTION")
    private String reasonDescription;
    @Column(name = "REASON_TYPE")
    private String reasonType;
    @Column(name = "SERVICE")
    private String service;
    @Column(name = "HAVE_MONEY")
    private Long haveMoney;

    public Reason() {
    }

    public Reason(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Reason(Long reasonId, String reasonCode, String reasonName, String reasonStatus) {
        this.reasonId = reasonId;
        this.reasonCode = reasonCode;
        this.reasonName = reasonName;
        this.reasonStatus = reasonStatus;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public String getReasonStatus() {
        return reasonStatus;
    }

    public void setReasonStatus(String reasonStatus) {
        this.reasonStatus = reasonStatus;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getHaveMoney() {
        return haveMoney;
    }

    public void setHaveMoney(Long haveMoney) {
        this.haveMoney = haveMoney;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reasonId != null ? reasonId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reason)) {
            return false;
        }
        Reason other = (Reason) object;
        if ((this.reasonId == null && other.reasonId != null) || (this.reasonId != null && !this.reasonId.equals(other.reasonId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "base.com.viettel.bccs.inventory.model.Reason[ reasonId=" + reasonId + " ]";
    }
    
}

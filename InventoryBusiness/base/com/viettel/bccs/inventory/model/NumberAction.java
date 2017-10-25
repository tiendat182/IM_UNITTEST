/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sinhhv
 */
@Entity
@Table(name = "NUMBER_ACTION")
@XmlRootElement
public class NumberAction implements Serializable {
    public static enum COLUMNS {FROMTOISDN,STATUS,SERVICETYPE,ACTIONTYPE, CREATEDATE, FROMISDN, NOTE, NUMBERACTIONID, REASONID, TOISDN, USERCREATE, USERIPADDRESS, EXCLUSE_ID_LIST,TELECOMSERVICEID}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "NUMBER_ACTION_ID_GENERATOR", sequenceName = "NUMBER_ACTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NUMBER_ACTION_ID_GENERATOR")
    @Column(name = "NUMBER_ACTION_ID")
    private Long numberActionId;
    @Column(name = "ACTION_TYPE")
    private String actionType;
    @Column(name = "FROM_ISDN")
    private String fromIsdn;
    @Column(name = "TO_ISDN")
    private String toIsdn;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "REASON_ID")
    private Long reasonId;
    @Column(name = "USER_CREATE")
    private String userCreate;
    @Column(name = "USER_IP_ADDRESS")
    private String userIpAddress;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "STATUS")
    private String status;

    public NumberAction() {
    }

    public NumberAction(Long numberActionId) {
        this.numberActionId = numberActionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNumberActionId() {
        return numberActionId;
    }

    public void setNumberActionId(Long numberActionId) {
        this.numberActionId = numberActionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getFromIsdn() {
        return fromIsdn;
    }

    public void setFromIsdn(String fromIsdn) {
        this.fromIsdn = fromIsdn;
    }

    public String getToIsdn() {
        return toIsdn;
    }

    public void setToIsdn(String toIsdn) {
        this.toIsdn = toIsdn;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numberActionId != null ? numberActionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NumberAction)) {
            return false;
        }
        NumberAction other = (NumberAction) object;
        if ((this.numberActionId == null && other.numberActionId != null) || (this.numberActionId != null && !this.numberActionId.equals(other.numberActionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.NumberAction[ numberActionId=" + numberActionId + " ]";
    }

}

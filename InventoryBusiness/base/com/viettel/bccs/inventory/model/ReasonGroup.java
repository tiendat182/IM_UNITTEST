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
@Table(name = "REASON_GROUP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReasonGroup.findAll", query = "SELECT r FROM ReasonGroup r")})
public class ReasonGroup implements Serializable {
public static enum COLUMNS {DESCRIPTION, REASONGROUPCODE, REASONGROUPID, REASONGROUPNAME, STATUS, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REASON_GROUP_ID")
    @SequenceGenerator(name = "REASON_GROUP_ID_GENERATOR", sequenceName = "REASON_GROUP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REASON_GROUP_ID_GENERATOR")
    private Long reasonGroupId;
    @Basic(optional = false)
    @Column(name = "REASON_GROUP_CODE")
    private String reasonGroupCode;
    @Basic(optional = false)
    @Column(name = "REASON_GROUP_NAME")
    private String reasonGroupName;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DESCRIPTION")
    private String description;

    public ReasonGroup() {
    }

    public ReasonGroup(Long reasonGroupId) {
        this.reasonGroupId = reasonGroupId;
    }

    public ReasonGroup(Long reasonGroupId, String reasonGroupCode, String reasonGroupName, String status) {
        this.reasonGroupId = reasonGroupId;
        this.reasonGroupCode = reasonGroupCode;
        this.reasonGroupName = reasonGroupName;
        this.status = status;
    }

    public Long getReasonGroupId() {
        return reasonGroupId;
    }

    public void setReasonGroupId(Long reasonGroupId) {
        this.reasonGroupId = reasonGroupId;
    }

    public String getReasonGroupCode() {
        return reasonGroupCode;
    }

    public void setReasonGroupCode(String reasonGroupCode) {
        this.reasonGroupCode = reasonGroupCode;
    }

    public String getReasonGroupName() {
        return reasonGroupName;
    }

    public void setReasonGroupName(String reasonGroupName) {
        this.reasonGroupName = reasonGroupName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reasonGroupId != null ? reasonGroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReasonGroup)) {
            return false;
        }
        ReasonGroup other = (ReasonGroup) object;
        if ((this.reasonGroupId == null && other.reasonGroupId != null) || (this.reasonGroupId != null && !this.reasonGroupId.equals(other.reasonGroupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "base.com.viettel.bccs.inventory.model.ReasonGroup[ reasonGroupId=" + reasonGroupId + " ]";
    }
    
}

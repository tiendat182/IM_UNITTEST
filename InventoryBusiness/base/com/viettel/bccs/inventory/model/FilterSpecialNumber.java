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
 *
 * @author sinhhv
 */
@Entity
@Table(name = "FILTER_SPECIAL_NUMBER")
public class FilterSpecialNumber implements Serializable {
public static enum COLUMNS {CREATEDATETIME, FILTERRULEID, FILTERSPECIALNUMBERID, ISDN, STATUS, PRODOFFERID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "FILTER_SPECIAL_NUMBER_ID")
    @SequenceGenerator(name = "FILTER_SPECIAL_NUMBER_GENERATOR", sequenceName = "FILTER_SPECIAL_NUMBER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILTER_SPECIAL_NUMBER_GENERATOR")
    private Long filterSpecialNumberId;
    @Column(name = "ISDN")
    private String isdn;
    @Column(name = "FILTER_RULE_ID")
    private Long filterRuleId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;

    public FilterSpecialNumber() {
    }

    public FilterSpecialNumber(Long filterSpecialNumberId) {
        this.filterSpecialNumberId = filterSpecialNumberId;
    }

    public Long getFilterSpecialNumberId() {
        return filterSpecialNumberId;
    }

    public void setFilterSpecialNumberId(Long filterSpecialNumberId) {
        this.filterSpecialNumberId = filterSpecialNumberId;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Long getFilterRuleId() {
        return filterRuleId;
    }

    public void setFilterRuleId(Long filterRuleId) {
        this.filterRuleId = filterRuleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filterSpecialNumberId != null ? filterSpecialNumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FilterSpecialNumber)) {
            return false;
        }
        FilterSpecialNumber other = (FilterSpecialNumber) object;
        if ((this.filterSpecialNumberId == null && other.filterSpecialNumberId != null) || (this.filterSpecialNumberId != null && !this.filterSpecialNumberId.equals(other.filterSpecialNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.FilterSpecialNumber[ filterSpecialNumberId=" + filterSpecialNumberId + " ]";
    }
    
}

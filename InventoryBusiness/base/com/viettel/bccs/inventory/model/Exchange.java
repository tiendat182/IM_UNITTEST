/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sinhhv
 */
@Entity
@Table(name = "EXCHANGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exchange.findAll", query = "SELECT e FROM Exchange e")})
public class Exchange implements Serializable {
public static enum COLUMNS {DESCRIPTION, EXCHANGEID, MAXGROUP, NAME, SERVERID, STATUS, TYPEID, EXCLUSE_ID_LIST}
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EXCHANGE_ID")
    private Long exchangeId;
    @Basic(optional = false)
    @Column(name = "TYPE_ID")
    private Long typeId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "MAX_GROUP")
    private Long maxGroup;
    @Column(name = "SERVER_ID")
    private Long serverId;

    public Exchange() {
    }

    public Exchange(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Exchange(Long exchangeId, Long typeId, String name) {
        this.exchangeId = exchangeId;
        this.typeId = typeId;
        this.name = name;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMaxGroup() {
        return maxGroup;
    }

    public void setMaxGroup(Long maxGroup) {
        this.maxGroup = maxGroup;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exchangeId != null ? exchangeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exchange)) {
            return false;
        }
        Exchange other = (Exchange) object;
        if ((this.exchangeId == null && other.exchangeId != null) || (this.exchangeId != null && !this.exchangeId.equals(other.exchangeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.Exchange[ exchangeId=" + exchangeId + " ]";
    }
    
}

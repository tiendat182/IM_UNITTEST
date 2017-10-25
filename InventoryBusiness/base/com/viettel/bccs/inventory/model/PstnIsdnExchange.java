/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sinhhv
 */
@Entity
@Table(name = "PSTN_ISDN_EXCHANGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PstnIsdnExchange.findAll", query = "SELECT p FROM PstnIsdnExchange p")})
public class PstnIsdnExchange implements Serializable {
public static enum COLUMNS {CREATEDATE, EXCHANGEID, POCODE, PREFIX, PSTNISDNEXCHANGEID, TYPE, EXCLUSE_ID_LIST}
    private static final long serialVersionUID = 1L;
    @Column(name = "PREFIX")
    private String prefix;
    @Column(name = "PO_CODE")
    private String poCode;
    @Column(name = "EXCHANGE_ID")
    private Long exchangeId;
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;
    @Id
    @Basic(optional = false)
    @Column(name = "PSTN_ISDN_EXCHANGE_ID")
    private Long pstnIsdnExchangeId;
    @Column(name = "TYPE")
    private String type;

    public PstnIsdnExchange() {
    }

    public PstnIsdnExchange(Long pstnIsdnExchangeId) {
        this.pstnIsdnExchangeId = pstnIsdnExchangeId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Long getPstnIsdnExchangeId() {
        return pstnIsdnExchangeId;
    }

    public void setPstnIsdnExchangeId(Long pstnIsdnExchangeId) {
        this.pstnIsdnExchangeId = pstnIsdnExchangeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pstnIsdnExchangeId != null ? pstnIsdnExchangeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PstnIsdnExchange)) {
            return false;
        }
        PstnIsdnExchange other = (PstnIsdnExchange) object;
        if ((this.pstnIsdnExchangeId == null && other.pstnIsdnExchangeId != null) || (this.pstnIsdnExchangeId != null && !this.pstnIsdnExchangeId.equals(other.pstnIsdnExchangeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.PstnIsdnExchange[ pstnIsdnExchangeId=" + pstnIsdnExchangeId + " ]";
    }
    
}

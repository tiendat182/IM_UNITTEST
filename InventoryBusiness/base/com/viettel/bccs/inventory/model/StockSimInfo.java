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
 * @author anhvv4
 */
@Entity
@Table(name = "STOCK_SIM_INFO")
@NamedQueries({
    @NamedQuery(name = "StockSimInfo.findAll", query = "SELECT s FROM StockSimInfo s")})
public class StockSimInfo implements Serializable {
public static enum COLUMNS {A3A8, ADM1, AUCREGDATE, AUCREMOVEDATE, AUCSTATUS, CONNECTIONDATE, CONNECTIONSTATUS, CONNECTIONTYPE, EKI, HLRID, HLRREGDATE, HLRREMOVEDATE, HLRSTATUS, ICCID, ID, IMSI, KIND, PIN, PIN2, PUK, PUK2, SIMTYPE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_SIM_INFO_ID_GENERATOR", sequenceName = "STOCK_SIM_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_SIM_INFO_ID_GENERATOR")
    private Long id;
    @Basic(optional = false)
    @Column(name = "IMSI")
    private String imsi;
    @Column(name = "ICCID")
    private String iccid;
    @Column(name = "PIN")
    private String pin;
    @Column(name = "PUK")
    private String puk;
    @Column(name = "PIN2")
    private String pin2;
    @Column(name = "PUK2")
    private String puk2;
    @Column(name = "HLR_ID")
    private String hlrId;
    @Column(name = "SIM_TYPE")
    private String simType;
    @Column(name = "AUC_REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aucRegDate;
    @Column(name = "AUC_STATUS")
    private Long aucStatus;
    @Column(name = "AUC_REMOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aucRemoveDate;
    @Column(name = "HLR_STATUS")
    private Long hlrStatus;
    @Column(name = "HLR_REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hlrRegDate;
    @Column(name = "HLR_REMOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hlrRemoveDate;
    @Column(name = "ADM1")
    private String adm1;
    @Column(name = "EKI")
    private String eki;
    @Column(name = "KIND")
    private String kind;
    @Column(name = "A3A8")
    private String a3a8;
    @Column(name = "CONNECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date connectionDate;
    @Column(name = "CONNECTION_TYPE")
    private String connectionType;
    @Column(name = "CONNECTION_STATUS")
    private Long connectionStatus;

    public StockSimInfo() {
    }

    public StockSimInfo(Long id) {
        this.id = id;
    }

    public StockSimInfo(Long id, String imsi) {
        this.id = id;
        this.imsi = imsi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getPin2() {
        return pin2;
    }

    public void setPin2(String pin2) {
        this.pin2 = pin2;
    }

    public String getPuk2() {
        return puk2;
    }

    public void setPuk2(String puk2) {
        this.puk2 = puk2;
    }

    public String getHlrId() {
        return hlrId;
    }

    public void setHlrId(String hlrId) {
        this.hlrId = hlrId;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public Date getAucRegDate() {
        return aucRegDate;
    }

    public void setAucRegDate(Date aucRegDate) {
        this.aucRegDate = aucRegDate;
    }

    public Long getAucStatus() {
        return aucStatus;
    }

    public void setAucStatus(Long aucStatus) {
        this.aucStatus = aucStatus;
    }

    public Date getAucRemoveDate() {
        return aucRemoveDate;
    }

    public void setAucRemoveDate(Date aucRemoveDate) {
        this.aucRemoveDate = aucRemoveDate;
    }

    public Long getHlrStatus() {
        return hlrStatus;
    }

    public void setHlrStatus(Long hlrStatus) {
        this.hlrStatus = hlrStatus;
    }

    public Date getHlrRegDate() {
        return hlrRegDate;
    }

    public void setHlrRegDate(Date hlrRegDate) {
        this.hlrRegDate = hlrRegDate;
    }

    public Date getHlrRemoveDate() {
        return hlrRemoveDate;
    }

    public void setHlrRemoveDate(Date hlrRemoveDate) {
        this.hlrRemoveDate = hlrRemoveDate;
    }

    public String getAdm1() {
        return adm1;
    }

    public void setAdm1(String adm1) {
        this.adm1 = adm1;
    }

    public String getEki() {
        return eki;
    }

    public void setEki(String eki) {
        this.eki = eki;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getA3a8() {
        return a3a8;
    }

    public void setA3a8(String a3a8) {
        this.a3a8 = a3a8;
    }

    public Date getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Long getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
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
        if (!(object instanceof StockSimInfo)) {
            return false;
        }
        StockSimInfo other = (StockSimInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gencode.StockSimInfo[ id=" + id + " ]";
    }
    
}

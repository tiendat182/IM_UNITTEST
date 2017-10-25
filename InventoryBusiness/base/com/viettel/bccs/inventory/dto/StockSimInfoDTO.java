package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;

public class StockSimInfoDTO extends BaseDTO implements Serializable {

    private String a3a8;
    private String adm1;
    private Date aucRegDate;
    private Date aucRemoveDate;
    private Long aucStatus;
    private Date connectionDate;
    private Long connectionStatus;
    private String connectionType;
    private String eki;
    private String hlrId;
    private Date hlrRegDate;
    private Date hlrRemoveDate;
    private Long hlrStatus;
    private String iccid;
    private Long id;
    private String imsi;
    private String kind;
    private String pin;
    private String pin2;
    private String puk;
    private String puk2;
    private String simType;

    public String getKeySet() {
        return keySet; }
    public String getA3a8() {
        return this.a3a8;
    }
    public void setA3a8(String a3a8) {
        this.a3a8 = a3a8;
    }
    public String getAdm1() {
        return this.adm1;
    }
    public void setAdm1(String adm1) {
        this.adm1 = adm1;
    }
    public Date getAucRegDate() {
        return this.aucRegDate;
    }
    public void setAucRegDate(Date aucRegDate) {
        this.aucRegDate = aucRegDate;
    }
    public Date getAucRemoveDate() {
        return this.aucRemoveDate;
    }
    public void setAucRemoveDate(Date aucRemoveDate) {
        this.aucRemoveDate = aucRemoveDate;
    }
    public Long getAucStatus() {
        return this.aucStatus;
    }
    public void setAucStatus(Long aucStatus) {
        this.aucStatus = aucStatus;
    }
    public Date getConnectionDate() {
        return this.connectionDate;
    }
    public void setConnectionDate(Date connectionDate) {
        this.connectionDate = connectionDate;
    }
    public Long getConnectionStatus() {
        return this.connectionStatus;
    }
    public void setConnectionStatus(Long connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public String getConnectionType() {
        return this.connectionType;
    }
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    public String getEki() {
        return this.eki;
    }
    public void setEki(String eki) {
        this.eki = eki;
    }
    public String getHlrId() {
        return this.hlrId;
    }
    public void setHlrId(String hlrId) {
        this.hlrId = hlrId;
    }
    public Date getHlrRegDate() {
        return this.hlrRegDate;
    }
    public void setHlrRegDate(Date hlrRegDate) {
        this.hlrRegDate = hlrRegDate;
    }
    public Date getHlrRemoveDate() {
        return this.hlrRemoveDate;
    }
    public void setHlrRemoveDate(Date hlrRemoveDate) {
        this.hlrRemoveDate = hlrRemoveDate;
    }
    public Long getHlrStatus() {
        return this.hlrStatus;
    }
    public void setHlrStatus(Long hlrStatus) {
        this.hlrStatus = hlrStatus;
    }
    public String getIccid() {
        return this.iccid;
    }
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getImsi() {
        return this.imsi;
    }
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getKind() {
        return this.kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getPin() {
        return this.pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getPin2() {
        return this.pin2;
    }
    public void setPin2(String pin2) {
        this.pin2 = pin2;
    }
    public String getPuk() {
        return this.puk;
    }
    public void setPuk(String puk) {
        this.puk = puk;
    }
    public String getPuk2() {
        return this.puk2;
    }
    public void setPuk2(String puk2) {
        this.puk2 = puk2;
    }
    public String getSimType() {
        return this.simType;
    }
    public void setSimType(String simType) {
        this.simType = simType;
    }
}

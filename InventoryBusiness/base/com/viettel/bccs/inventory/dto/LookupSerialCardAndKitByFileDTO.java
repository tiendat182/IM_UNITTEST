package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author viet
 */
public class LookupSerialCardAndKitByFileDTO extends BaseDTO implements Serializable {
    private String stt;
    private String id;
    private String isdn;
    private String date;
    private String time;
    private String serialKit;
    private String serialCard;
    private String serial;
    private Date dateExp;
    private String userExp;
    private String price;
    private String provinceName;
    private String provinceCode;
    private String note;
    private String staffCode;
    private String staffName;
    private String clientFileName;
    private String serverFileName;
    private Long kitOrCard;
    private String message;
    private String saleTranDate;

    public String getSaleTranDate() {
        return saleTranDate;
    }

    public void setSaleTranDate(String saleTranDate) {
        this.saleTranDate = saleTranDate;
    }

    public LookupSerialCardAndKitByFileDTO() {
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSerialKit() {
        return serialKit;
    }

    public void setSerialKit(String serialKit) {
        this.serialKit = serialKit;
    }

    public String getSerialCard() {
        return serialCard;
    }

    public void setSerialCard(String serialCard) {
        this.serialCard = serialCard;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getDateExp() {
        return dateExp;
    }

    public void setDateExp(Date dateExp) {
        this.dateExp = dateExp;
    }

    public String getUserExp() {
        return userExp;
    }

    public void setUserExp(String userExp) {
        this.userExp = userExp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public Long getKitOrCard() {
        return kitOrCard;
    }

    public void setKitOrCard(Long kitOrCard) {
        this.kitOrCard = kitOrCard;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
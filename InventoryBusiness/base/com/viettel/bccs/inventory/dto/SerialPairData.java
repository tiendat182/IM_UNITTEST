package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.Date;

/**
 * Created by PM2-LAMNV5 on 11/10/2016.
 */
public class SerialPairData implements SQLData {
    private String sqlType = "SERIAL_PAIR";
    private Long sStatus;
    private Date sSaleDate;
    private Long sOwnerId;
    private Long sOwnerType;
    private Long sBankplusStatus;
    private Long sDepositPrice;
    private Long sStateId;
    private Long sProdOfferId;
    private Long wProdOfferId;
    private Long wOwnerType;
    private Long wOwnerId;
    private Long wStatus;
    private Long wStateId;
    private String wFromSerial;
    private String wToSerial;
    private String wFunc;
    private Long quantity;

    public SerialPairData() {
    }

    public SerialPairData(Long sStatus, Date sSaleDate, Long sOwnerId, Long sOwnerType, Long sBankplusStatus, Long sDepositPrice, Long sStateId, Long sProdOfferId, Long wProdOfferId, Long wOwnerType, Long wOwnerId, Long wStatus, Long wStateId, String wFromSerial, String wToSerial, String wFunc, Long quantity) {
        this.sStatus = sStatus;
        this.sSaleDate = sSaleDate;
        this.sOwnerId = sOwnerId;
        this.sOwnerType = sOwnerType;
        this.sBankplusStatus = sBankplusStatus;
        this.sDepositPrice = sDepositPrice;
        this.sStateId = sStateId;
        this.sProdOfferId = sProdOfferId;
        this.wProdOfferId = wProdOfferId;
        this.wOwnerType = wOwnerType;
        this.wOwnerId = wOwnerId;
        this.wStatus = wStatus;
        this.wStateId = wStateId;
        this.wFromSerial = wFromSerial;
        this.wToSerial = wToSerial;
        this.wFunc = wFunc;
        this.quantity = quantity;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return sqlType;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.sqlType = typeName;
        this.sStatus = stream.readLong();
        this.sSaleDate = stream.readDate();
        this.sOwnerId = stream.readLong();
        this.sOwnerType = stream.readLong();
        this.sBankplusStatus = stream.readLong();
        this.sDepositPrice = stream.readLong();
        this.sStateId = stream.readLong();
        this.sProdOfferId = stream.readLong();
        this.wProdOfferId = stream.readLong();
        this.wOwnerType = stream.readLong();
        this.wOwnerId = stream.readLong();
        this.wStatus = stream.readLong();
        this.wStateId = stream.readLong();
        this.wFromSerial = stream.readString();
        this.wToSerial = stream.readString();
        this.wFunc = stream.readString();
        this.quantity = stream.readLong();
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeLong(DataUtil.defaultIfNull(sStatus, -1L));
        stream.writeDate(sSaleDate == null ? null : new java.sql.Date(sSaleDate.getTime()));
        stream.writeLong(DataUtil.defaultIfNull(sOwnerId, -1L));
        stream.writeLong(DataUtil.defaultIfNull(sOwnerType, -1L));
        stream.writeLong(DataUtil.defaultIfNull(sBankplusStatus, -1L));
        stream.writeLong(DataUtil.defaultIfNull(sDepositPrice, -1L));
        stream.writeLong(DataUtil.defaultIfNull(sStateId, -1L));
        stream.writeLong(DataUtil.defaultIfNull(sProdOfferId, -1L));
        stream.writeLong(DataUtil.defaultIfNull(wProdOfferId, -1L));
        stream.writeLong(DataUtil.defaultIfNull(wOwnerType, -1L));
        stream.writeLong(DataUtil.defaultIfNull(wOwnerId, -1L));
        stream.writeLong(DataUtil.defaultIfNull(wStatus, -1L));
        stream.writeLong(DataUtil.defaultIfNull(wStateId, -1L));
        stream.writeString(wFromSerial);
        stream.writeString(wToSerial);
        stream.writeString(wFunc);
        stream.writeLong(DataUtil.defaultIfNull(quantity, 0L));
    }

    public Long getsStatus() {
        return sStatus;
    }

    public void setsStatus(Long sStatus) {
        this.sStatus = sStatus;
    }

    public Date getsSaleDate() {
        return sSaleDate;
    }

    public void setsSaleDate(Date sSaleDate) {
        this.sSaleDate = sSaleDate;
    }

    public Long getsOwnerId() {
        return sOwnerId;
    }

    public void setsOwnerId(Long sOwnerId) {
        this.sOwnerId = sOwnerId;
    }

    public Long getsOwnerType() {
        return sOwnerType;
    }

    public void setsOwnerType(Long sOwnerType) {
        this.sOwnerType = sOwnerType;
    }

    public Long getsBankplusStatus() {
        return sBankplusStatus;
    }

    public void setsBankplusStatus(Long sBankplusStatus) {
        this.sBankplusStatus = sBankplusStatus;
    }

    public Long getsDepositPrice() {
        return sDepositPrice;
    }

    public void setsDepositPrice(Long sDepositPrice) {
        this.sDepositPrice = sDepositPrice;
    }

    public Long getsStateId() {
        return sStateId;
    }

    public void setsStateId(Long sStateId) {
        this.sStateId = sStateId;
    }

    public Long getsProdOfferId() {
        return sProdOfferId;
    }

    public void setsProdOfferId(Long sProdOfferId) {
        this.sProdOfferId = sProdOfferId;
    }

    public Long getwProdOfferId() {
        return wProdOfferId;
    }

    public void setwProdOfferId(Long wProdOfferId) {
        this.wProdOfferId = wProdOfferId;
    }

    public Long getwOwnerType() {
        return wOwnerType;
    }

    public void setwOwnerType(Long wOwnerType) {
        this.wOwnerType = wOwnerType;
    }

    public Long getwOwnerId() {
        return wOwnerId;
    }

    public void setwOwnerId(Long wOwnerId) {
        this.wOwnerId = wOwnerId;
    }

    public Long getwStatus() {
        return wStatus;
    }

    public void setwStatus(Long wStatus) {
        this.wStatus = wStatus;
    }

    public Long getwStateId() {
        return wStateId;
    }

    public void setwStateId(Long wStateId) {
        this.wStateId = wStateId;
    }

    public String getwFromSerial() {
        return wFromSerial;
    }

    public void setwFromSerial(String wFromSerial) {
        this.wFromSerial = wFromSerial;
    }

    public String getwToSerial() {
        return wToSerial;
    }

    public void setwToSerial(String wToSerial) {
        this.wToSerial = wToSerial;
    }

    public String getwFunc() {
        return wFunc;
    }

    public void setwFunc(String wFunc) {
        this.wFunc = wFunc;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}

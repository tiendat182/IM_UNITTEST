package com.viettel.bccs.inventory.dto;

import java.io.Serializable;

/**
 * Created by duongtv10 on 10/31/2015.
 */
public class SimKITDTO implements Serializable {
    private String errorCode;
    //0 Thành công
    //1 Sai User Pass
    //2 Lỗi input
    //3 Lỗi kết nối DB
    //4 Lỗi hệ thống
    private String errorDescription;
    private String numberIMSI;
    private String numberSerial;
    private String numberPUK1;
    private String numberPUK2;
    private String numberPIN1;
    private String numberPIN2;
    private String startHLR;
    private String endHLR;
    private String startAuC;
    private String endAuC;
    private String nameOrder;
    private String statusOrder;
    private String idOrder;
    private String typeWarehouse;
    //1 Kho đơn vị
    //2 Kho nhân viên
    private String idWarehouse;
    //Mã kho
    private String nameWarehouse;
    //Tên kho
    private String status;
    //1 Trong kho
    //0 Đã bán
    //Khóa
    private String statusPointToPoint;
    //1 Đấu nối
    //Chưa đấu nối
    private boolean result;

    private String staffName;
    private String staffMobile;

    private String provinceName;
    private String provinceCode;
    private String districtName;
    private String districtCode;
    private String simModelType;
    private Long stockModelId;
    private Long ownerType;
    private Long ownerId;
    private Long stateId;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    //True có dữ liệu trả về
    //False không có dữ liệu trả về
    public SimKITDTO() {
        //SimKITDTO
    }

    public String getNumberIMSI() {
        return numberIMSI;
    }

    public void setNumberIMSI(String numberIMSI) {
        this.numberIMSI = numberIMSI;
    }

    public String getNumberSerial() {
        return numberSerial;
    }

    public void setNumberSerial(String numberSerial) {
        this.numberSerial = numberSerial;
    }

    public String getNumberPUK1() {
        return numberPUK1;
    }

    public void setNumberPUK1(String numberPUK1) {
        this.numberPUK1 = numberPUK1;
    }

    public String getNumberPUK2() {
        return numberPUK2;
    }

    public void setNumberPUK2(String numberPUK2) {
        this.numberPUK2 = numberPUK2;
    }

    public String getNumberPIN1() {
        return numberPIN1;
    }

    public void setNumberPIN1(String numberPIN1) {
        this.numberPIN1 = numberPIN1;
    }

    public String getNumberPIN2() {
        return numberPIN2;
    }

    public void setNumberPIN2(String numberPIN2) {
        this.numberPIN2 = numberPIN2;
    }

    public String getStartHLR() {
        return startHLR;
    }

    public void setStartHLR(String startHLR) {
        this.startHLR = startHLR;
    }

    public String getEndHLR() {
        return endHLR;
    }

    public void setEndHLR(String endHLR) {
        this.endHLR = endHLR;
    }

    public String getStartAuC() {
        return startAuC;
    }

    public void setStartAuC(String startAuC) {
        this.startAuC = startAuC;
    }

    public String getEndAuC() {
        return endAuC;
    }

    public void setEndAuC(String endAuC) {
        this.endAuC = endAuC;
    }

    public String getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder = nameOrder;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getTypeWarehouse() {
        return typeWarehouse;
    }

    public void setTypeWarehouse(String typeWarehouse) {
        this.typeWarehouse = typeWarehouse;
    }

    public String getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(String idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public String getNameWarehouse() {
        return nameWarehouse;
    }

    public void setNameWarehouse(String nameWarehouse) {
        this.nameWarehouse = nameWarehouse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusPointToPoint() {
        return statusPointToPoint;
    }

    public void setStatusPointToPoint(String statusPointToPoint) {
        this.statusPointToPoint = statusPointToPoint;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getSimModelType() {
        return simModelType;
    }

    public void setSimModelType(String simModelType) {
        this.simModelType = simModelType;
    }
}

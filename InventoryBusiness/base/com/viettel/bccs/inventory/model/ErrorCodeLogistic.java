package com.viettel.bccs.inventory.model;

public enum ErrorCodeLogistic {

    SUCCESS("00","Thanh cong"),
    STOCK_TRANS_ID_NOT_EXISTED("01", "Ma stockTrans khong ton tai"),
    PRODUCT_OFFERING_NOT_EXISTED("02","Danh sach mat hang khong ton tai"),
    EXCEPTION("03","Co loi xay ra");


    ErrorCodeLogistic(String errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }
    private String errorCode;
    private String description;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
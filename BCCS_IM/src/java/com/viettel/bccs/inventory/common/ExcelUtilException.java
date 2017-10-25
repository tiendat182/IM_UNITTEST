package com.viettel.bccs.inventory.common;

/**
 * Created by ChungNV7 on 12/23/2015.
 */
public class ExcelUtilException extends Exception {

    private String message;
    private int errorCode;

    public ExcelUtilException(String message) {
        this.message = message;
    }

}

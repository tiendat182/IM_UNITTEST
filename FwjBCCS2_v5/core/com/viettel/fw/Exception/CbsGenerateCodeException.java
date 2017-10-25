/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.Exception;

/**
 *
 * @author lamnv5
 */
public class CbsGenerateCodeException extends Exception {

    private String errorCode;
    private String description;

    public CbsGenerateCodeException(String errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

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
    
    @Override
    public String toString() {
        return "CbsGenerateCodeException{" + "errorCode=" + errorCode + ", description=" + description + '}';
    }

}

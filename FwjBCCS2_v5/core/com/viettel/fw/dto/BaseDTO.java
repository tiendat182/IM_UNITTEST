/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.fw.dto;

import java.io.Serializable;

/**
 * @author lamnv5
 */
public class BaseDTO implements Serializable {

    // Dung de dai dien cho key cua doi tuong (hay dung trong truong hop doi tuong co composite key, se duoc dung
    // cho thuoc tinh rowKey trong datatable)
    protected String keySet;

    public String getKeySet() {
        return keySet;
    }

    public void setKeySet(String keySet) {
        this.keySet = keySet;
    }

}

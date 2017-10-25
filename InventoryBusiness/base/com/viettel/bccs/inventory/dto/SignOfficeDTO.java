package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;

public class SignOfficeDTO extends BaseDTO implements Serializable {

    private String userName;
    private String passWord;
    private Long signFlowId;

    public SignOfficeDTO() {
    }

    public SignOfficeDTO(String userName, String passWord, Long signFlowId) {
        this.userName = userName;
        this.passWord = passWord;
        this.signFlowId = signFlowId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Long getSignFlowId() {
        return signFlowId;
    }

    public void setSignFlowId(Long signFlowId) {
        this.signFlowId = signFlowId;
    }
}

package com.viettel.fw.dto;

import java.io.Serializable;

/**
 * Created by tuyennt17 on 6/15/2016.
 */
public class SessionDTO extends BaseDTO implements Serializable{

    protected String sessionId;
    protected String clientNode;
    protected String isdnOrAccount;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClientNode() {
        return clientNode;
    }

    public void setClientNode(String clientNode) {
        this.clientNode = clientNode;
    }

    public String getIsdnOrAccount() {
        return isdnOrAccount;
    }

    public void setIsdnOrAccount(String isdnOrAccount) {
        this.isdnOrAccount = isdnOrAccount;
    }
}

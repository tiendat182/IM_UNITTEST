package com.viettel.akka.beans;

import java.io.Serializable;

/**
 * Created by thiendn1 on 9/7/2015.
 */
public class AkkaResponse implements Serializable{
    private Object response;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}

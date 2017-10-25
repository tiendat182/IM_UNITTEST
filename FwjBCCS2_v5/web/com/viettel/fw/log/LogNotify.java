package com.viettel.fw.log;

/**
 * Created by tuyennt17 on 07/11/2015.
 */
public class LogNotify {

    private String msg;
    private Exception exc;

    public LogNotify(String msg, Exception exc) {
        this.msg = msg;
        this.exc = exc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Exception getExc() {
        return exc;
    }

    public void setExc(Exception exc) {
        this.exc = exc;
    }
}

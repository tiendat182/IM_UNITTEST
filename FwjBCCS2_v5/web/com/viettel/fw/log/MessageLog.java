package com.viettel.fw.log;

import com.viettel.fw.common.data.ErrorType;
import com.viettel.fw.common.data.LogLevel;
import com.viettel.fw.common.data.MsgType;
import com.viettel.fw.common.util.DataUtil;

import java.util.Date;

/**
 * Created by TuyenNT17 on 9/8/2015.
 */
public class MessageLog {
    private String callId;
    private MsgType msgType;
    private ErrorType errorType;
    private String rootCause;
    private String idError;
    private String description;
    private LogLevel logLevel = null;
    private Date createDate;


    public MessageLog() {
    }

    public MessageLog(MsgType msgType, ErrorType errorType, String rootCause, String idError) {
        this.msgType = msgType;
        this.errorType = errorType;
        this.rootCause = rootCause;
        this.idError = idError;
    }


    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getIdError() {
        return idError;
    }

    public void setIdError(String idError) {
        this.idError = idError;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String messageToString() {
        if (DataUtil.isNullObject(this.msgType) || DataUtil.isNullObject(this.errorType) || DataUtil.isNullObject(this.rootCause) || DataUtil.isNullObject(this.idError)) {
            return "";
        }
        return this.msgType.toString() + this.errorType.toString() + this.rootCause + this.idError;
    }

    @Override
    public String toString() {
        return this.msgType.toString() + this.errorType.toString() + this.rootCause + this.idError;
    }
}

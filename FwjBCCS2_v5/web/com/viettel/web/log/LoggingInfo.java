package com.viettel.web.log;

import org.omnifaces.util.Faces;

import java.text.MessageFormat;

/**
 * Created by tuyennt17 on 6/10/2016.
 */
public class LoggingInfo {
    private String actionLog;
    private String appCode;
    private String timer;
    private String username;
    private String ipaddress;
    private String path;
    private String functionName;
    private String params;
    private String className;
    private String durtation;
    private String desc;
    private String iPPortParentNode;
    private String errorCode;
    private String errorDescription;
    private String transactionStatus;
    private String account;
    private String requestContent;
    private String responseContent;
    private String sessionId;


    public LoggingInfo() {
    }

    public LoggingInfo(String actionLog) {
        this.actionLog = actionLog;
    }


    public LoggingInfo(String sessionId,String actionLog, String appCode, String timer, String username, String ipaddress, String path, String functionName, String params, String className, String durtation, String desc, String iPPortParentNode, String errorCode, String errorDescription, String transactionStatus, String account, String requestContent, String responseContent) {
        this.actionLog = actionLog;
        this.appCode = appCode;
        this.timer = timer;
        this.username = username;
        this.ipaddress = ipaddress;
        this.path = path;
        this.functionName = functionName;
        this.params = params;
        this.className = className;
        this.durtation = durtation;
        this.desc = desc;
        this.iPPortParentNode = iPPortParentNode;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.transactionStatus = transactionStatus;
        this.account = account;
        this.requestContent = requestContent;
        this.responseContent = responseContent;
        this.sessionId = sessionId;
    }

    public String getActionLog() {
        return actionLog;
    }

    public void setActionLog(String actionLog) {
        this.actionLog = actionLog;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDurtation() {
        return durtation;
    }

    public void setDurtation(String durtation) {
        this.durtation = durtation;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getiPPortParentNode() {
        return iPPortParentNode;
    }

    public void setiPPortParentNode(String iPPortParentNode) {
        this.iPPortParentNode = iPPortParentNode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        String addition = MessageFormat.format("[SessionId={0}][IP_Port_ParentNode={1}][RequestContent={2}][ResponseContent={3}][ErrorCode={4}][ErrorDescription={5}][TransactionStatus={6}][Account={7}]",this.sessionId,this.iPPortParentNode,this.requestContent,this.responseContent,
                this.errorCode,this.errorDescription,this.transactionStatus,this.account);
        return  MessageFormat.format(("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9}|{10}"), this.actionLog,
                this.appCode, this.timer, this.username, this.ipaddress, this.path, this.functionName, this.params, this.className, this.durtation, this.desc+addition);
    }
}

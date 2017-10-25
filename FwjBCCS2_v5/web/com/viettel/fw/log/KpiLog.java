package com.viettel.fw.log;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thiendn1 on 5/1/2016.
 */
public class KpiLog {
    private int index;
    private String callId;
    private String kpiHeader;
    private String user;
    private String path;
    private String ipRemote;
    private String uri;
    private String params;
    private String className;
    private String methodName;
    private long duration;
    private Date endTime;
    private Date startTime;
    private String defaultSys;

    public KpiLog(){

    }
    public KpiLog(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getKpiHeader() {
        return kpiHeader;
    }

    public void setKpiHeader(String kpiHeader) {
        this.kpiHeader = kpiHeader;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getIpRemote() {
        return ipRemote;
    }

    public void setIpRemote(String ipRemote) {
        this.ipRemote = ipRemote;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDefaultSys() {
        return defaultSys;
    }

    public void setDefaultSys(String defaultSys) {
        this.defaultSys = defaultSys;
    }

    public String getSubMethodDescription(){
        return className+"."+methodName+":"+duration;
    }
    @Override
    public String toString() {
        String endTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(endTime);
        String log = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9,number,#}|{10}|{11}", new Object[]{"action",
                defaultSys, endTimeStr, user, ipRemote, path, uri, params, className, duration, methodName+"-"+callId, kpiHeader});
        return log;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}

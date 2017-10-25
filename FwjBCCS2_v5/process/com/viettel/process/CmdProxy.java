package com.viettel.process;

import java.util.Date;

/**
 * Created by Thien on 03/01/2016.
 */
public class CmdProxy {

    public static final int STATUS_INIT = 0;
    public static final int STATUS_BEGIN = 1;
    public static final int STATUS_EXECUTE = 2;
    public static final int STATUS_FINISH = 3;

    private String cmdId;
    private Date createTime;
    private Date beginExecuteTime;
    private int status;
    private int numActorLimit = 3;
    private Command command;

    public CmdProxy(Command command) {
        this.command = command;
        this.numActorLimit = command.getNumActorLimit();
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBeginExecuteTime() {
        return beginExecuteTime;
    }

    public void setBeginExecuteTime(Date beginExecuteTime) {
        this.beginExecuteTime = beginExecuteTime;
    }

    public int getStatus() {
        return status;
    }

    public String getStringStatus() {
        switch (status) {
            case STATUS_INIT:
                return "INIT";
            case STATUS_BEGIN:
                return "BEGIN";
            case STATUS_EXECUTE:
                return "EXECUTE";
            case STATUS_FINISH:
                return "FINISH";
        }
        return null;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumActorLimit() {
        return numActorLimit;
    }

    public void setNumActorLimit(int numActorLimit) {
        this.numActorLimit = numActorLimit;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getCmdName() {
        return command.getName();
    }

    @Override
    public String toString() {
        String initTime = null;
        String executeTime = null;
        if(beginExecuteTime!=null){
            initTime = ""+(beginExecuteTime.getTime()-createTime.getTime());
            executeTime = ""+(System.currentTimeMillis()-beginExecuteTime.getTime());
        }
        else{
            initTime = "N/A";
            executeTime = "N/A";
        }
        return "Id: " + cmdId + " | createdTime: " + createTime + " | status: " + getStringStatus() + "| initTime: "+initTime +" ms | activeTime: " + executeTime + " ms.";
    }
}

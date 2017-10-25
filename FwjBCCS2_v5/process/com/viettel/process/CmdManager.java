package com.viettel.process;

import com.viettel.mmserver.base.ProcessThreadMX;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Thien on 03/01/2016.
 */
public class CmdManager extends ProcessThreadMX {
    private int numActorLimit = 3;
    private String cmdName;
    private Command command;
    AtomicLong cmdIndex = new AtomicLong();
    List<CmdProxy> cmdProxies = new ArrayList<>();
    private Date stoppedTime;
    private Date startTime;

    public CmdManager(Command command) throws MalformedObjectNameException, InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
        super(command.getName());
        this.command = command;
        this.cmdName = command.getName();
        this.numActorLimit = command.getNumActorLimit();
        registerAgent(cmdName);
        start();
    }


    public boolean addCmd(CmdProxy cmdProxy) {
        if (getCurrentActorSize() < numActorLimit) {
            cmdProxies.add(cmdProxy);
            return true;
        }
        return false;
    }

    public void setCmd(CmdProxy cmdProxy) {
        int index = getCmdProxy(cmdProxy);
        if (index < 0) {
            addCmd(cmdProxy);
        } else {
            cmdProxies.set(index, cmdProxy);
        }
    }

    public void removeCmd(CmdProxy cmdProxy) {
        cmdProxies.remove(cmdProxy);
    }

    public int getCmdProxy(CmdProxy cmdProxy) {
        for (int i = 0; i < cmdProxies.size(); i++) {
            CmdProxy proxy = cmdProxies.get(i);
            if (proxy.getCmdId().equals(cmdProxy.getCmdId())) {
                return i;
            }

        }
        return -1;
    }

    public int getCurrentActorSize() {
        return cmdProxies.size();
    }

    public int getNumActorLimit() {
        return numActorLimit;
    }

    public boolean isOverNumActorLimit() {
        return getCurrentActorSize() >= getNumActorLimit();
    }

    public long getCmdIndex() {
        return cmdIndex.incrementAndGet();
    }

    public String getCmdName() {
        return cmdName;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "Name: " + cmdName + " | numActorLimit: " + numActorLimit + " | currentSize: " + getCurrentActorSize() + " |currentIndex: " + cmdIndex.get();
    }

    @Override
    public void start() {
        super.start();
        this.running = true;
        startTime = new Date();
    }

    public void run() {
        this.logger.info(this.cmdName + " is running");

    }

    @Override
    protected void process() {

    }

    public void info(CmdProxy cmdProxy) {
        logger.info(toString());
        logger.info(cmdProxy);
    }

    @Override
    public void stop() {
        super.stop();
        this.running = false;
        this.logger.info(this.cmdName + " is stopped");
        stoppedTime = new Date();
    }

    public Date getStoppedTime() {
        return stoppedTime;
    }

    public Date getStartTime() {
        return startTime;
    }
}

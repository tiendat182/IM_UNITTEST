package com.viettel.process;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Thien on 03/01/2016.
 */
public abstract class Command {
    public static final int CMD_TYPE_LOOP = 0; //loop
    public static final int CMD_TYPE_1TIME = 1; //run 1 time

    protected String cmdName;
    protected int numActorLimit = 1;
    protected int cmdType = 0;

    protected String schedule;

    protected static Logger logger;

    @Autowired
    @Qualifier(value = SystemConfig.CMD_BUS_ACTOR)
    ActorRef busActor;

    @Autowired
    ApplicationContext context;

    public Command(String name) {
        this.cmdName = name;
        if (cmdType == CMD_TYPE_1TIME) {
            numActorLimit = 1;
            cmdName += "_" + RandomStringUtils.randomNumeric(15);
        }
        logger = Logger.getLogger(cmdName);
    }


    public Command(String name, int numActorLimit, int type) {
        this.cmdName = name;
        this.numActorLimit = numActorLimit;
        this.cmdType = type;
        if (cmdType == CMD_TYPE_1TIME) {
            this.numActorLimit = 1;
//            if (DataUtil.isNullOrEmpty(this.cmdName)) {
            this.cmdName += "_" + RandomStringUtils.randomNumeric(15);
//            }
        }
        logger = Logger.getLogger(cmdName);
    }

    public abstract void execute() throws Exception;

    public String getName() {
        return cmdName;
    }

    public void setName(String cmdName) {
        this.cmdName = cmdName;
    }

    public int getNumActorLimit() {
        return numActorLimit;
    }

    public void setNumActorLimit(int numActorLimit) throws Exception {
        this.numActorLimit = numActorLimit;
        if (cmdType == CMD_TYPE_1TIME) {
            this.numActorLimit = 1;
        }
    }

    public <T> T getCommand(Class<T> commandClass, Object... constructorParams) {
        return context.getBean(commandClass, constructorParams);
    }

    public void addCommandToBaseThread(Command command) {
        busActor.tell(new CmdBusActor.ADD_COMMAND(command), null);
    }

    public CmdManager getCommandManager(String cmdName, long duration) throws Exception {
        FiniteDuration finiteDuration = FiniteDuration.create(duration, TimeUnit.MILLISECONDS);
        Future<Object> result = Patterns.ask(busActor, new CmdBusActor.GET_COMMAND((cmdName)), Timeout.durationToTimeout(finiteDuration));
        CmdManager object = (CmdManager) Await.result(result, finiteDuration);
        return object;
    }

    public int getType() {
        return cmdType;
    }

    @Override
    public String toString() {
        return "Name: " + cmdName + " | numActorLimit: " + numActorLimit + " | type: " + cmdType;
    }

    public String getText(String key) {
        return BundleUtil.getText(GetTextFromBundleHelper.getLocate(), key);
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

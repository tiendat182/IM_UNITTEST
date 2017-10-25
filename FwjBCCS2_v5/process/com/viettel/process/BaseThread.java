package com.viettel.process;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.mmserver.base.ProcessThreadMX;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thien on 03/01/2016.
 */

public class BaseThread extends ProcessThreadMX {

    //    public static final Logger logger = Logger.getLogger("ProcessLog");
    @Autowired
    @Qualifier(value = SystemConfig.CMD_BUS_ACTOR)
    ActorRef busActor;

    @Autowired
    SystemConfig systemConfig;

    public BaseThread(String threadName) {
        super(threadName);
        this.registerAgent();
    }

    public BaseThread(String threadName, String description) {
        super(threadName, description);
        this.registerAgent();
    }

    private void registerAgent() {
        try {
            registerAgent(threadName);
        } catch (MalformedObjectNameException e) {
            logger.error(e);
        } catch (InstanceAlreadyExistsException e) {
            logger.error(e);
        } catch (MBeanRegistrationException e) {
            logger.error(e);
        } catch (NotCompliantMBeanException e) {
            logger.error(e);

        }
    }

    @Override
    public void process() {
        FiniteDuration finiteDuration = FiniteDuration.create(1000L, TimeUnit.MILLISECONDS);
        Future<Object> result = Patterns.ask(busActor, new CmdBusActor.MANAGER_MAP(),
                Timeout.durationToTimeout(finiteDuration));
        try {
            Map<String, CmdManager> cmdManagerMap = (Map<String, CmdManager>) Await.result(result, finiteDuration);
//            int currentSize = getThreadSize(cmdManagerMap);
//            if (systemConfig.MAX_NUM_ACTOR <= currentSize) {
//                logger.info("The application thread number is already over the limit: " + systemConfig.MAX_NUM_ACTOR);
//                return;
//            }
            List<CmdProxy> initCmds = new ArrayList<>();
            Date currentTime = new Date();
            for (String cmdName : cmdManagerMap.keySet()) {
                CmdManager cmdManager = cmdManagerMap.get(cmdName);
                if (!cmdManager.isRunning()) {
                    logger.info("The command " + cmdManager.getCmdName() + " is already stopped at " + cmdManager.getStoppedTime());
                } else {
                    if (!cmdManager.isOverNumActorLimit()) {
                        Command command = cmdManager.getCommand();
                        boolean isValid = true;
                        if (!DataUtil.isNullOrEmpty(command.getSchedule())) {
                            if (CronExpression.isValidExpression(command.getSchedule())) {
                                CronExpression schedule = new CronExpression(command.getSchedule());
                                if (!schedule.isSatisfiedBy(currentTime)) {
                                    isValid = false;
                                }
                            } else {
                                isValid = false;
                                logger.info("The schedule " + command.getSchedule() + " of command " + cmdManager.getCmdName() + " is not valid");
                            }
                        }

                        if (isValid) {
                            CmdProxy cmdProxy = new CmdProxy(command);
                            cmdProxy.setCreateTime(currentTime);
                            initCmds.add(cmdProxy);
                            logger.info("Begin: " + cmdManager.getCmdName());
                        }
                    } else {
                        logger.debug("The command " + cmdManager.getCmdName() + " is already over the limit: " + cmdManager.getNumActorLimit());
                    }
                }
            }
            busActor.tell(new CmdBusActor.EXE_INIT(initCmds), null);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private int getThreadSize(Map<String, CmdManager> cmdManagerMap) {
        int size = 0;
        for (CmdManager cmdManager : cmdManagerMap.values()) {
            size += cmdManager.getCurrentActorSize();
        }
        return size;
    }

}
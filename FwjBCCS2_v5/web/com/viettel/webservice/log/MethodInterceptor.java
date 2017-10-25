package com.viettel.webservice.log;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.log.LogAnalyticActor;
import com.viettel.ws.common.utils.GenericWebInfo;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * tuyennt17
 * thiendn1: optimize
 */
@Aspect
public class MethodInterceptor {

    @Autowired
    protected ActorSystem actorSystem;
    private final String LOG_TYPE_START_ACTION = "start_action";
    private final String LOG_TYPE_END_ACTION = "end_action";
    private final Logger devLog = Logger.getLogger("DevLog");

    @Autowired(required = false)
    @Qualifier(SystemConfig.LOG_ANALYTIC_ACTOR)
    ActorRef logAnalyticActor;
    @Autowired
    SystemConfig systemConfig;


    public void logDev(String callId, Exception e) {
        if (systemConfig.IS_RUN_DEV_LOG) {
            if (callId == null) {
                callId = "undefine";
            }
            logAnalyticActor.tell(new LogAnalyticActor.DEV(callId, e), null);
        }

    }

    public void logDev(String callId, String log) {
        if (systemConfig.IS_RUN_DEV_LOG) {
            if (callId == null) {
                callId = "undefine";
            }
            logAnalyticActor.tell(new LogAnalyticActor.DEV(callId, log), null);
        }
    }


    private Object proceed(ProceedingJoinPoint jp) throws Throwable {
        Object value = null;
        String callId = null;
        Date startTime = new Date();
        String startTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(startTime);
        String className = jp.getTarget().getClass().getSimpleName();
        String methodName = jp.getSignature().getName();
        String temUser = null;
        String ipRemote = null;
        // Bat generic info de test
        try {
            GenericWebInfo genericWebInfo = GetTextFromBundleHelper.getGenericWebInfo();
            callId = genericWebInfo.getReqId();
            ipRemote = genericWebInfo.getIpAddress();
            temUser = genericWebInfo.getStaffCode();
        } catch (Exception e) {
            //log de xac dinh khi nao genericWebInfo null
            logDev(jp.toString(), new Exception(e));
            logDev("undefine", new Exception(e));
        }
        String logStart = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9,number,#}|{10}|{11}", new Object[]{LOG_TYPE_START_ACTION,
                systemConfig.DEFAULT_SYS, startTimeStr, temUser, ipRemote, null, null, null, className, null, methodName, null});
        logDev(callId, logStart);
        try {
            value = jp.proceed();
        } catch (Throwable e) {
            logDev(callId, new Exception(e));
            throw e;
        }
        Date endTime = new Date();
        String endTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(endTime);
        Long duration = Long.valueOf(endTime.getTime() - startTime.getTime());
        String logEnd = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9,number,#}|{10}|{11}", new Object[]{LOG_TYPE_END_ACTION,
                systemConfig.DEFAULT_SYS, endTimeStr, temUser, ipRemote, null, null, null, className, duration, methodName, null});
        logDev(callId, logEnd);
        return value;
    }

    public Object loggingTraceAction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceed(proceedingJoinPoint);
    }
}


package com.viettel.web.log;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ocpsoft.pretty.PrettyContext;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.data.ErrorType;
import com.viettel.fw.common.data.LogLevel;
import com.viettel.fw.common.data.MsgType;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.log.LogAnalyticActor;
import com.viettel.fw.log.LogCollectorActor;
import com.viettel.fw.log.LogNotify;
import com.viettel.fw.log.MessageLog;
import com.viettel.fw.passport.CustomConnector;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import viettel.passport.client.UserToken;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * tuyennt17
 * thiendn1: optimize
 */
@Aspect
public class MethodInterceptor {

    private AtomicLong idError = new AtomicLong(0L);
    public static final Logger logger = Logger.getLogger("aop");
    @Autowired
    protected ActorSystem actorSystem;

    @Autowired(required = false)
    @Qualifier(SystemConfig.LOG_ANALYTIC_ACTOR)
    ActorRef logAnalyticActor;


    @Autowired
    SystemConfig systemConfig;

    private final String LOG_TYPE_START_ACTION = "start_action";
    private final String LOG_TYPE_END_ACTION = "end_action";

    public void logKpi(String params, Date startTime, Date endTime, ProceedingJoinPoint jp) {
        if (!systemConfig.IS_RUN_KPI_LOG) {
            return;
        }
        String kpiHeader = Faces.getRequestHeader("VTS-KPIID");
        if (DataUtil.isNullOrEmpty(kpiHeader)) {
            return;
        }
        UserToken userToken = Faces.getSessionAttribute(CustomConnector.VSA_USER_TOKEN);
        String temUser = userToken != null ? userToken.getUserName() : null;
        String startTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(startTime);
        String endTimeStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS").format(endTime);
        Long duration = Long.valueOf(endTime.getTime() - startTime.getTime());
        String ipRemote = Faces.getRemoteAddr();
        String path = Faces.getRequest().getServerName() + ":" + Faces.getRequest().getServerPort();
        String uri = PrettyContext.getCurrentInstance().getRequestURL().toURL();
        String[] tmp = uri.split("\\/");
        if (tmp.length > 2) {
            uri = "/" + tmp[2];
        }
        String className = jp.getTarget().getClass().getSimpleName();
        String methodName = jp.getSignature().getName();
        String logStart = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9,number,#}|{10}|{11}", new Object[]{LOG_TYPE_START_ACTION,
                systemConfig.DEFAULT_SYS, startTimeStr, temUser, ipRemote, path, uri, params, className, duration, methodName, kpiHeader});
        String logEnd = MessageFormat.format("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}|{9,number,#}|{10}|{11}", new Object[]{LOG_TYPE_END_ACTION,
                systemConfig.DEFAULT_SYS, endTimeStr, temUser, ipRemote, path, uri, params, className, duration, methodName, kpiHeader});
        actorSystem.actorSelection("user/" + Const.LOGGING_COLLECTOR_ACTOR + Faces.getSessionId()).tell(new LogCollectorActor.KPI(kpiHeader,new String[]{logStart, logEnd}), null);

    }


    public void logDev(String callId, Exception e) {
        logAnalyticActor.tell(new LogAnalyticActor.DEV(callId, e), null);
    }

    private Object proceed(ProceedingJoinPoint jp) throws Throwable {
        MessageLog loggingMessageDTO = null;
        String callId = getAppCallId();
        String idError = getIncremntIdError();
        String description = jp.toShortString();
        LogLevel logLevel = null;
        Object value = null;
        Date startDate = new Date();
        try {
            logLevel = LogLevel.INFO;
            value = jp.proceed();
            //thiendn1: xu ly trong truong hop reportError hoac reportMsg
            if (value instanceof LogNotify) {
                LogNotify logNotifyDTO = (LogNotify) value;
                //ghi log
                logDev(callId, logNotifyDTO.getExc());
                description += " - Message: " + logNotifyDTO.getMsg();
                loggingMessageDTO = LoggingUtils.getMsgType(logNotifyDTO.getExc(), idError);
                if (DataUtil.isNullObject(logNotifyDTO.getExc())) {
                    loggingMessageDTO = new MessageLog(MsgType.S, ErrorType.N, Const.ERROR_CODE_NUMBER.SUCCESSFUL, idError);
                } else {
                    logLevel = logNotifyDTO.getExc() instanceof LogicException ? LogLevel.WARN : LogLevel.ERROR;
                }
            } else {
                //thiendn1: ham xu ly thanh cong (khong throw exception)
                loggingMessageDTO = new MessageLog(MsgType.S, ErrorType.N, Const.ERROR_CODE_NUMBER.SUCCESSFUL, idError);
            }
            loggingMessageDTO.setLogLevel(logLevel);
            loggingMessageDTO.setCallId(callId);
            loggingMessageDTO.setCreateDate(startDate);
            loggingMessageDTO.setDescription(description);
            writeLog(loggingMessageDTO);
        } catch (Throwable e) {
            //thiendn1: catch exception, thuong gap trong truong hop khong chu dong su dung reportError tren web
            loggingMessageDTO = LoggingUtils.getMsgType(e, idError);
            loggingMessageDTO.setLogLevel(e instanceof LogicException ? LogLevel.WARN : LogLevel.ERROR);
            loggingMessageDTO.setCallId(callId);
            loggingMessageDTO.setCreateDate(startDate);
            loggingMessageDTO.setDescription(description);
            writeLog(loggingMessageDTO);
            throw e;
        }
        return value;
    }

    public Object loggingTraceAction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object value = null ;
        try {
            Object[] params = proceedingJoinPoint.getArgs();
            value = proceedingJoinPoint.proceed();
        }catch (Exception e) {
            throw  e;
        }
        return value;
    }

    private void setInfoLog(Object[] params) {
        try {
            Arrays.stream(params).forEach(o -> {

            });
        }catch (Exception e ){
            logger.error("Exception when try set info sessionDTO");
        }
    }


    public Object methodTraceException(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        Object value = null;
        try {
            value = proceedingJoinPoint.proceed();
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return value;
    }



    private String getAppCallId() {
        return Faces.getRequestAttribute(Const.CALL_ID_TOKEN);
    }

    private int getKpiId() {
        return Integer.valueOf(Faces.getRequestAttribute(Const.KPI_ID_TOKEN).toString());
    }

    private int getAndIncreaseKpiId() {
        int currentKpiId = getKpiId();
        Faces.setRequestAttribute(Const.KPI_ID_TOKEN, currentKpiId + 1);
        return currentKpiId;
    }

    private int getAndDecreaseKpiId() {
        int currentKpiId = getKpiId();
        Faces.setRequestAttribute(Const.KPI_ID_TOKEN, currentKpiId - 1);
        return currentKpiId;
    }

    private String getIncremntIdError() throws Exception {
        return String.valueOf(idError.incrementAndGet());
    }

    private void writeLog(MessageLog loggingMessageDTO) {
        if (!systemConfig.IS_RUN_SYS_LOG) {
            return;
        }
        actorSystem.actorSelection("user/" + Const.LOGGING_COLLECTOR_ACTOR + Faces.getSessionId()).tell(loggingMessageDTO, null);
    }


    private String getAllParameter() {
        StringBuilder params = new StringBuilder();
        Map<String, String[]> requestParameterMap = Faces.getRequestParameterValuesMap();
        for (Map.Entry<String, String[]> entry : requestParameterMap.entrySet()) {
            String paramName = entry.getKey();
            params.append(paramName).append(":");
            String[] paramValues = entry.getValue();
            for (String paramValue : paramValues) {
                params.append(paramValue).append(";");
            }
        }
        if (params.length() > 1) {
            params.deleteCharAt(params.length() - 1);
        }
        return params.toString();
    }


}


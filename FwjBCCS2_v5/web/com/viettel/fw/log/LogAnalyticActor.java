package com.viettel.fw.log;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.data.LogLevel;
import com.viettel.fw.common.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Thien on 22/11/2015.
 */
@Service(value = SystemConfig.LOG_ANALYTIC_ACTOR_BEAN)
@Scope("prototype")
@Lazy
public class LogAnalyticActor extends UntypedActor {
    private final Logger sysLog = Logger.getLogger("SysLog");
    private final Logger devLog = Logger.getLogger("DevLog");

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DEV) {
            if (((DEV) message).e instanceof Exception) {
                devLog.error(((DEV) message).callId, ((DEV) message).e);
            } else {
                devLog.info(((DEV) message).callId +"|" +((DEV) message).log);
            }
        } else if (message instanceof ACTION){
            List<String> logs = ((ACTION) message).logContents;
            String st = logs.get(0);
            String end = logs.get(1);
            devLog.info(st);
            devLog.info(end);
        } else if (message instanceof UPDATE) {
            String key = ((UPDATE) message).key;
            List<MessageLog> list = (List<MessageLog>) ((UPDATE) message).map.get(key);
            if (list == null || list.isEmpty()) {
                return;
            }
            if (list.size() == 1) {
                //neu chi co 1 item, thuc hien ghi log luon
                writeLog(list.get(0));
            } else {
                //thuc hien nhom cac logMessage co cung callId
                String callId = key;
                Date date = list.get(list.size() - 1).getCreateDate();
                LogLevel logLevel = LogLevel.INFO;
                String messageLog = null;
                StringBuilder description = new StringBuilder(list.get(list.size() - 1).getDescription());
                // xac dinh log level va description
                for (int i = list.size() - 1; i >= 0; i--) {
                    MessageLog log = list.get(i);
                    if (i == list.size() - 1) {
                        messageLog = log.messageToString();
                    }
                    if (!log.getLogLevel().equals(LogLevel.INFO)) {
                        if (logLevel.equals(LogLevel.INFO)) {
                            logLevel = log.getLogLevel();
                            messageLog = log.messageToString();
                        }
                        //xac dinh nguyen nhan gay loi
                        // lay ra message
                        description.append("cause: " + log.getDescription() + "; ");
                    }

                }
                writeLog(callId, date, logLevel, messageLog, description.toString());
                ((UPDATE) message).actorRef.tell(new LogCollectorActor.FLUSH(key), self());
            }
        }else if (message instanceof EXIT) {
            context().stop(self());
        } else {
            unhandled(message);
        }
    }

    public static StringBuilder getKpiMethodDescription(KpiLog log, List<KpiLog> kpiLogs) {
        StringBuilder descriptionBuilder = new StringBuilder();
        if (kpiLogs.isEmpty()) {
            return descriptionBuilder;
        }
        descriptionBuilder.append(" { ").append(log.getSubMethodDescription());
        KpiLog nextLog = kpiLogs.get(0);
        if (nextLog.getIndex() == log.getIndex()) {
            descriptionBuilder.append(" } ");
        } else if (nextLog.getIndex() < log.getIndex()) {
            descriptionBuilder.append(" } } ");
        }
        descriptionBuilder.append(getKpiMethodDescription(nextLog, kpiLogs.subList(1, kpiLogs.size())));
        return descriptionBuilder;
    }


    private void writeLog(String callId, Date date, LogLevel logLevel, String messageLog, String description) throws Exception {
        if (logLevel.equals(LogLevel.INFO)) {
            sysLog.info(writeTraceActionLog(callId, date, logLevel, messageLog, description));
        } else if (logLevel.equals(LogLevel.WARN)) {
            sysLog.warn(writeTraceActionLog(callId, date, logLevel, messageLog, description));

        } else if (logLevel.equals(LogLevel.ERROR)) {
            sysLog.error(writeTraceActionLog(callId, date, logLevel, messageLog, description));
        }
    }

    private String writeTraceActionLog(String callId, Date date, LogLevel logLevel, String messageLog, String description) throws Exception {
        String mess = callId +
                "||" + DateUtil.dateTime2StringNoSlash(date) +
                "||" + messageLog +
                "||" + description + "||" + logLevel.toString();
        return mess;
    }


    private void writeLog(MessageLog loggingMessageDTO) throws Exception {
        writeLog(loggingMessageDTO.getCallId(), loggingMessageDTO.getCreateDate(), loggingMessageDTO.getLogLevel(), loggingMessageDTO.messageToString(), loggingMessageDTO.getDescription());
    }

    public static void main(String[] args) {
        // {0 {1} {1 {2}} {1 {2} {2}} {1} }
        List<KpiLog> list = new ArrayList<>();
        list.add(new KpiLog(0));
        System.out.println(getKpiMethodDescription(new KpiLog(0), list));
    }


    public static class UPDATE {
        String key;
        ConcurrentHashMap map;
        ActorRef actorRef;

        public UPDATE(ActorRef actorRef, String key, ConcurrentHashMap map) {
            this.actorRef = actorRef;
            this.key = key;
            this.map = map;
        }
    }

    public static class ACTION {
        List<String> logContents;
        public ACTION(List<String> logContents) {
            this.logContents = logContents;
        }
    }


    public static class EXIT {

    }


    public static class DEV {
        String callId;
        String log;
        Exception e;

        public DEV(String messages, Exception e) {
            this.callId = messages;
            this.e = e;
        }

        public DEV(String messages, String log) {
            this.callId = messages;
            this.log = log;
        }
    }
}

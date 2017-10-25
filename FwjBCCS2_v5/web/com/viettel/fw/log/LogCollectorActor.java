package com.viettel.fw.log;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import com.google.common.collect.Lists;
import com.viettel.fw.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Thien on 22/11/2015.
 */
@Component(value = SystemConfig.LOG_COLLECTOR_ACTOR_BEAN)
@Scope("prototype")
@Lazy
public class LogCollectorActor extends UntypedActor {

    @Autowired(required = false)
    ActorSystem actorSystem;

    @Autowired(required = false)
    @Qualifier(SystemConfig.LOG_ANALYTIC_ACTOR)
    ActorRef logAnalyticActor;

    ConcurrentHashMap<String, List<MessageLog>> map = new ConcurrentHashMap<>();

    public static class FLUSH {
        String key;

        public FLUSH(String mapKey) {
            this.key = mapKey;
        }
    }

//    public static class FLUSH_KPI extends FLUSH {
//        public FLUSH_KPI(String mapKey) {
//            super(mapKey);
//            this.key = mapKey;
//        }
//    }

    public static class EXIT {

    }

    public static class ANALYTIC {
        String callId;

        public ANALYTIC(String callId) {
            this.callId = callId;
        }
    }

    public static class ACTION {
        List<String> logContent;
        public ACTION(List<String> logContent) {
            this.logContent = logContent;
        }
    }

    public static class KPI {
        String kpiRequestHeader;
        String[] logContents;

        public KPI(String kpiRequestHeader, String[] logContents) {
            this.kpiRequestHeader = kpiRequestHeader;
            this.logContents = logContents;
        }
    }


    public static class KPI_HEADER {
        KpiLog kpiLog;

        public KPI_HEADER(KpiLog kpiLog) {
            this.kpiLog = kpiLog;
        }
    }


    public static class ANALYTIC_KPI {
        String callId;

        public ANALYTIC_KPI(String callId) {
            this.callId = callId;
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MessageLog) {
            String callId = ((MessageLog) message).getCallId();
            List<MessageLog> list = map.get(callId);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add((MessageLog) message);
            map.put(callId, list);
        }else if (message instanceof ANALYTIC) {
            logAnalyticActor.tell(new LogAnalyticActor.UPDATE(self(), ((ANALYTIC) message).callId, map), null);
        }else if (message instanceof FLUSH) {
            map.remove(((FLUSH) message).key);
        }else if (message instanceof EXIT) {
            context().stop(self());
        }else if(message instanceof ACTION){
            logAnalyticActor.tell(new LogAnalyticActor.ACTION(((ACTION)message).logContent),null);
        }else {
            unhandled(message);
        }
    }


}

package com.viettel.web.log;

import com.viettel.akka.SpringExtension;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.log.LogCollectorActor;
import com.viettel.web.common.ActorSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by tuyennt17 on 6/13/2016.
 */
@Service
@Lazy
public class LoggingWithoutSession extends ActorSession {

    @PostConstruct
    public void init(){
        actor = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props(SystemConfig.LOG_COLLECTOR_ACTOR_BEAN), "ServiceLogger");
    }

    @PreDestroy
    public void destroy(){
        actor.tell(new LogCollectorActor.EXIT(), null);
    }
}

package com.viettel.web.log;

import com.viettel.akka.SpringExtension;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.log.LogCollectorActor;
import com.viettel.web.common.ActorSession;
import org.omnifaces.util.Faces;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by thiendn1 on 3/12/2015.
 */

@Service
@Scope("session")
@Lazy
public class LoggingSession extends ActorSession {

    private String sessionId;

    @PostConstruct
    protected void init() {
        sessionId = Faces.getSessionId();
        //tao logging collector actor
        actor = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props(SystemConfig.LOG_COLLECTOR_ACTOR_BEAN), Const.LOGGING_COLLECTOR_ACTOR + sessionId);
    }

    @PreDestroy
    protected void destroy() {
        actor.tell(new LogCollectorActor.EXIT(), null);
    }

}

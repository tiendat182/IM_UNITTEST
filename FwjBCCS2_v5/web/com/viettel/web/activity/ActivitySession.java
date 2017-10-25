package com.viettel.web.activity;

import akka.pattern.Patterns;
import akka.util.Timeout;
import com.viettel.akka.SpringExtension;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.web.common.ActorSession;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thien on 22/11/2015.
 */
//session scope
@Component
@Scope("session")
@Lazy
public class ActivitySession extends ActorSession {

    public static final Logger logger = Logger.getLogger(ActivitySession.class);
    private boolean init;

    @Autowired
    SystemConfig systemConfig;

    @PostConstruct
    protected void init() {
        if(systemConfig.IS_RUN_DASHBOARD){
            actor = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props(SystemConfig.ACTIVITI_ACTOR_BEAN), Const.ACTIVITY_ACTOR + Faces.getSessionId());
            init = true;
        }
    }

    @PreDestroy
    protected void destroy() {
        if(systemConfig.IS_RUN_DASHBOARD){
            actor.tell(new ActivityActor.Save(), null);
            actor.tell(new ActivityActor.Exit(), null);
        }
    }

    public void postAction(List<Object> objects) {
        actor.tell(new ActivityActor.Init(objects), null);
    }

    public List<Object> getActions() {
        FiniteDuration finiteDuration = FiniteDuration.create(10000l, TimeUnit.MILLISECONDS);
        Future<Object> result = Patterns.ask(actor, new ActivityActor.Get(),
                Timeout.durationToTimeout(finiteDuration));
        List<Object> objects = null;
        try {
            objects = (List<Object>) Await.result(result, finiteDuration);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
        return objects;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}

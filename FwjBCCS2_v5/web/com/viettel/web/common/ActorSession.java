package com.viettel.web.common;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by thiendn1 on 3/12/2015.
 */
public abstract class ActorSession {

    @Autowired
    protected ActorSystem actorSystem;


    protected ActorRef actor;

    protected abstract void init();

    protected abstract void destroy();
}

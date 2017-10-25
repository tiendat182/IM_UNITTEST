package com.viettel.web;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import com.viettel.akka.SpringExtension;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import com.viettel.web.activity.UserActivityController;
import com.viettel.web.common.LocateBean;
import com.viettel.web.common.LoginController;
import com.viettel.web.common.MenuBean;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

/**
 * Created by thiendn1 on 4/12/2015.
 */

@Configuration
@ComponentScan(value = {"com.viettel.web", "com.viettel.fw"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {LoginController.class, UserActivityController.class})})
//@ComponentScan(value = {"com.viettel.web", "com.viettel.fw"})
public class WebConfiguration {
    @Autowired(required = false)
    ActorSystem actorSystem;

    @Autowired
    SystemConfig systemConfig;

    @Bean(name = SystemConfig.LOG_ANALYTIC_ACTOR)
    @Scope("singleton")
    @Lazy
    public ActorRef depLogAnalyticActor() {
        return actorSystem.actorOf(new RoundRobinPool(systemConfig.LOG_NUM_ACTOR).props(SpringExtension.SpringExtProvider.get(actorSystem).props(SystemConfig.LOG_ANALYTIC_ACTOR_BEAN)),
                Const.LOGGING_ANALYTIC_ACTOR);
    }

    @Bean(name = SystemConfig.FAVORITE_ACTOR)
    @Scope("singleton")
    @Lazy
    public ActorRef favoriteActor() {
        return actorSystem.actorOf(new RoundRobinPool(systemConfig.FAVORITE_NUM_ACTOR).props(SpringExtension.SpringExtProvider.get(actorSystem).props(SystemConfig.FAVORITE_ACTOR_BEAN)),
                Const.FAVORITE_ACTOR);
    }

}
package com.viettel.process;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import com.viettel.akka.SpringExtension;
import com.viettel.fw.SystemConfig;
import com.viettel.fw.common.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

/**
 * Created by Thien on 03/01/2016.
 */
@Configuration
@ComponentScan({"com.viettel.process", "com.viettel.fw"})
public class ProcessConfiguration {

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    SystemConfig systemConfig;

    @Bean(name = SystemConfig.CMD_BUS_ACTOR)
    @Scope("singleton")
    @Lazy
    public ActorRef cmdBusActor() {
        return actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props(SystemConfig.CMD_BUS_BEAN),
                SystemConfig.CMD_BUS_ACTOR);

    }


    @Bean(name = SystemConfig.CMD_EXECUTE_ACTOR)
    @Scope("singleton")
    @Lazy
    public ActorRef cmdExecuteActor() {
        return actorSystem.actorOf(new RoundRobinPool(systemConfig.MAX_NUM_ACTOR).props(SpringExtension.SpringExtProvider.get(actorSystem)
                        .props(SystemConfig.CMD_EXECUTE_BEAN)),
                SystemConfig.CMD_EXECUTE_ACTOR);
    }

}

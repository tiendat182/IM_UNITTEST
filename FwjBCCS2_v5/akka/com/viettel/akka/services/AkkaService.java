package com.viettel.akka.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.viettel.akka.SpringExtension;
import com.viettel.akka.actors.TaskActor;
import com.viettel.akka.beans.AkkaResponse;
import com.viettel.akka.beans.AkkaTask;
import com.viettel.fw.Exception.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by thiendn1 on 9/7/2015.
 */

@Service
public class AkkaService {

    // throw exception and stop all actor if one actor fails
    private final int ALL_FOR_ONE = 0;
    private final int ONE_fOR_ONE = 1;
    private int mode = ALL_FOR_ONE;



    @Autowired
    ActorSystem system;

    private long duration = 30000;

    public List<AkkaResponse> doTask(List<AkkaTask> akkaTasks) throws Exception {
        List<ActorRef> actorRefs = new ArrayList<>();
        for (AkkaTask akkaTask : akkaTasks) {
            ActorRef supervisor = system.actorOf(
                    SpringExtension.SpringExtProvider.get(system).props("taskActor"));
            // send request to actor
            supervisor.tell(akkaTask, null);
            actorRefs.add(supervisor);
        }
        // get response from actor
        List<AkkaResponse> responses = new ArrayList<>();

        FiniteDuration finiteDuration = FiniteDuration.create(this.duration, TimeUnit.MILLISECONDS);

        for (ActorRef actorRef : actorRefs) {
            Future<Object> result = Patterns.ask(actorRef, new TaskActor.Get(),
                    Timeout.durationToTimeout(finiteDuration));
            Object object = Await.result(result, finiteDuration);
            AkkaResponse akkaResponse = new AkkaResponse();
            // convert NULL_VALUE to null object
            if (object instanceof TaskActor.NULL) {
                akkaResponse.setResponse(null);
            } else {
                if (mode == ALL_FOR_ONE) {
                    if (object instanceof LogicException) {
                        stopAllActor(actorRefs);
                        throw (LogicException) object;
                    }
                    if (object instanceof Exception) {
                        stopAllActor(actorRefs);
                        throw (Exception) object;
                    }
                }
                akkaResponse.setResponse(object);
            }
            responses.add(akkaResponse);
            system.stop(actorRef);
        }
        return responses;
    }

    public void shutDownAkka() {
        system.shutdown();
        system.awaitTermination();
    }

    private void stopAllActor(List<ActorRef> actorRefs) {
        for (ActorRef actorRef : actorRefs) {
            system.stop(actorRef);
        }
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}


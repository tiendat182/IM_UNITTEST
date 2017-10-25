package com.viettel.process;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.viettel.fw.SystemConfig;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Thien on 03/01/2016.
 */
@Service(value = SystemConfig.CMD_EXECUTE_BEAN)
@Scope("prototype")
public class CmdExecuteAtor extends UntypedActor {
    public static final Logger logger = Logger.getLogger("ProcessLog");

    @Override
    public void onReceive(Object o){
        if (o instanceof CmdBusActor.EXE_BEGIN) {
            ActorRef sender = ((CmdBusActor.EXE_BEGIN) o).actorRef;
            CmdProxy cmd = ((CmdBusActor.EXE_BEGIN) o).cmd;
            //send init message to manager
            sender.tell(new CmdBusActor.EXE_EXECUTE(cmd), null);
            //execute action
            try {
                cmd.getCommand().execute();
            } catch (Exception e) {
                logger.error(e);
            }
            finally {
                //send finish message to manager
                sender.tell(new CmdBusActor.EXE_FINISH(cmd), null);
            }
        }
    }
}

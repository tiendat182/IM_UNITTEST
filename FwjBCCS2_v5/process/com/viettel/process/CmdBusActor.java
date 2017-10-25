package com.viettel.process;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.viettel.fw.SystemConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thien on 03/01/2016.
 */


public class CmdBusActor extends UntypedActor {

    public static final Logger logger = Logger.getLogger("ProcessLog");

    private Map<String, CmdManager> cmdCentreMap;
    private List<Command> commands;

    @Autowired
    @Qualifier(value = SystemConfig.CMD_EXECUTE_ACTOR)
    ActorRef executeActor;

    public void init() {
        cmdCentreMap = new HashMap<>();
        for (Command cmd : commands) {
            try {
                addCommand(cmd);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof EXE_INIT) {
            List<CmdProxy> initCmdList = ((EXE_INIT) o).commandList;
            for (CmdProxy cmdProxy : initCmdList) {
                CmdManager cmdManager = cmdCentreMap.get(cmdProxy.getCmdName());
                cmdProxy.setCmdId(cmdProxy.getCmdName() + "-" + (cmdManager.getCmdIndex()));
                cmdProxy.setStatus(CmdProxy.STATUS_BEGIN);
                if (cmdManager.addCmd(cmdProxy)) {
                    cmdCentreMap.put(cmdProxy.getCmdName(), cmdManager);
                    //send command to execute actor
                    executeActor.tell(new EXE_BEGIN(getSelf(), cmdProxy), null);
                    cmdManager.info(cmdProxy);
                }
            }
        } else if (o instanceof EXE_EXECUTE) {
            CmdProxy cmdProxy = ((EXE_EXECUTE) o).cmdProxy;
            cmdProxy.setStatus(CmdProxy.STATUS_EXECUTE);
            cmdProxy.setBeginExecuteTime(new Date());
            CmdManager cmdManager = cmdCentreMap.get(cmdProxy.getCmdName());
            if (cmdManager != null) {
                cmdManager.setCmd(cmdProxy);
                cmdManager.info(cmdProxy);
            }
        } else if (o instanceof EXE_FINISH) {
            CmdProxy cmdProxy = ((EXE_FINISH) o).cmdProxy;
            cmdProxy.setStatus(CmdProxy.STATUS_FINISH);
            CmdManager cmdManager = cmdCentreMap.get(cmdProxy.getCmdName());
            if (cmdManager != null) {
                cmdManager.removeCmd(cmdProxy);
                cmdManager.info(cmdProxy);
            }
            if (cmdProxy.getCommand().getType() == Command.CMD_TYPE_1TIME) {
                cmdCentreMap.remove(cmdProxy.getCmdName());
            }
        } else if (o instanceof MANAGER_MAP) {
            getSender().tell(cmdCentreMap, getSelf());
        } else if (o instanceof ADD_COMMAND) {
            addCommand(((ADD_COMMAND) o).command);
        } else if (o instanceof GET_COMMAND) {
            getSender().tell(getCommand(((GET_COMMAND) o).cmdName), getSelf());
        }
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public CmdManager getCommand(String cmdName) {
        return cmdCentreMap.get(cmdName);
    }

    public void addCommand(Command cmd) throws Exception {
        if (cmd == null) {
            throw new Exception("The command must not be null!");
        }
        if (cmd.getName() == null) {
            throw new Exception("The command name must not be null or empty!");
        }
        CmdManager cmdManager = cmdCentreMap.get(cmd.getName());
        if (cmdManager != null && cmd.cmdType == Command.CMD_TYPE_LOOP) {
            throw new Exception("The command manager is already existed!");
        }
        if (cmdManager != null && cmdManager.isOverNumActorLimit()) {
            throw new Exception("The command manager is already existed!");
        }
        try {
            cmdManager = new CmdManager(cmd);
            cmdCentreMap.put(cmdManager.getCmdName(), cmdManager);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public static class MANAGER_MAP {

    }

    public static class ADD_COMMAND {
        Command command;

        public ADD_COMMAND(Command command) {
            this.command = command;
        }
    }

    public static class GET_COMMAND {
        String cmdName;

        public GET_COMMAND(String cmdName) {
            this.cmdName = cmdName;
        }
    }

    public static class EXE {
        CmdProxy cmdProxy;
        ActorRef actorRef;

    }

    public static class EXE_INIT extends EXE {
        List<CmdProxy> commandList;

        public EXE_INIT(List<CmdProxy> commandList) {
            this.commandList = commandList;
        }
    }

    public static class EXE_BEGIN extends EXE {
        CmdProxy cmd;

        public EXE_BEGIN(ActorRef actorRef, CmdProxy command) {
            this.actorRef = actorRef;
            this.cmd = command;
        }
    }

    public static class EXE_EXECUTE extends EXE {
        public EXE_EXECUTE(CmdProxy cmdProxy) {
            this.cmdProxy = cmdProxy;
        }
    }

    public static class EXE_FINISH extends EXE {
        public EXE_FINISH(CmdProxy cmdProxy) {
            this.cmdProxy = cmdProxy;
        }
    }

}

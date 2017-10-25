package com.viettel.bccs.processIM.common;

import com.viettel.fw.Exception.LogicException;
import com.viettel.process.Command;

/**
 * @author luannt23.
 * @comment
 * @date 3/5/2016.
 */
public abstract class InventoryThread extends Command {

    public InventoryThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }



    protected void reportError(Object ex) {
        if (ex instanceof Exception) {
            Exception e = (Exception) ex;
            logger.error(e.getMessage(), e);
        } else {
            logger.error(ex);
        }
    }

}

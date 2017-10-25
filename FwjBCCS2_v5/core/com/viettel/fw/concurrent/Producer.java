package com.viettel.fw.concurrent;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public abstract class Producer<T extends ConcurrentMessage> extends Worker {
    public Producer() {
        super();
    }

    public boolean enqueue(T msg) {
        msg.setProducerName(getWorkerName());
        return queue.enqueue(msg);
    }

    @Override
    public void logEnd() {

    }

    @Override
    public void logStart() {

    }
}

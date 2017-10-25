package com.viettel.fw.concurrent;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public abstract class Consumer<T extends ConcurrentMessage> extends Worker {
    private static final Logger logger = Logger.getLogger(Consumer.class);

    private T message;

    public Consumer() {
    }

    @Override
    public void process() throws Exception {
        message = (T) queue.dequeue();
        logStart();
        message.setConsumerName(getWorkerName());
        try {
            process(message);
        } finally {
            logEnd();
        }
    }

    public abstract void process(T message) throws Exception;

    /**
     * Day lai queue neu khong xu ly duoc
     *
     * @param message
     * @return
     */
    public boolean pushBack(T message) {
        return queue.enqueue(message);
    }

    public List<T> drain(int maxElement) {
        return queue.drain(maxElement);
    }

    @Override
    public void logStart() {
        try {
            if (message != null) {
                message.setInWorkerTime(new Date(), getWorkerName());
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void logEnd() {
        try {
            if (message != null) {
                message.setOutWorkerTime(new Date(), getWorkerName());
            }
        } catch (Exception ex) {
        }
    }
}

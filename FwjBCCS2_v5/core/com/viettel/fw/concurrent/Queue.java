package com.viettel.fw.concurrent;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public class Queue<T extends ConcurrentMessage> {
    private static Logger logger;
    private BlockingQueue<T> queue;
    private String uniqueName;

    public Queue(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }

    @PostConstruct
    public void init() {
        logger = Logger.getLogger(uniqueName);
    }

    public boolean enqueue(T message) {
        message.setInTime(new Date(), uniqueName);
        boolean result = queue.offer(message);
        if (result) {
            logger.info("enqueue:" + queue.size() + "|" + message);
        }

        return result;
    }

    public boolean enqueue(T message, long timeout, TimeUnit unit) throws InterruptedException {
        message.setInTime(new Date(), uniqueName);
        boolean result = queue.offer(message, timeout, unit);
        if (result) {
            logger.info(message);
        }

        return result;
    }

    public T dequeue() throws InterruptedException {
        T message = queue.take();
        message.setOutTime(new Date(), uniqueName);
        logger.info("dequeue:" + queue.size() + "|" + message);
        return message;
    }

    public List<T> drain(int maxElement) {
        List<T> lst = new ArrayList<>();
        queue.drainTo(lst, maxElement);
        for (T msg : lst) {
            msg.setOutTime(new Date(), uniqueName);
        }

        return lst;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}

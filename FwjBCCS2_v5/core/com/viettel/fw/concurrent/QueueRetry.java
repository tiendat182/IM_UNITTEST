package com.viettel.fw.concurrent;

import com.viettel.fw.Exception.MaxRetryException;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.concurrent.DelayQueue;


/**
 * Created by PM2-LAMNV5 on 6/14/2016.
 */
public class QueueRetry<T extends ConcurrentMessage> {
    private static final Logger logger = Logger.getLogger(QueueRetry.class);
    private DelayQueue<DelayMessage> queue;
    private String uniqueName;

    //Khoang thoi gian giua cac lan retry
    private long delayInterval;
    //So lan retry toi da
    private int maxRetry;

    public QueueRetry() {
        queue = new DelayQueue<>();
    }

    public boolean enqueue(Queue originQueue, T message) throws MaxRetryException {
        if (message.getRetryCount() >= maxRetry) {
            throw new MaxRetryException("exceed.max.retry");
        }

        message.setRetryCount(message.getRetryCount() + 1);
        message.setInTime(new Date(), uniqueName);
        boolean result = queue.offer(new DelayMessage(originQueue, message, delayInterval));

        return result;
    }

    public void start() {
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Take elements out from the DelayQueue object.
                        DelayMessage delayMsg = queue.take();
                        ConcurrentMessage concurrentMessage = delayMsg.getData();
                        Queue orginQueue = delayMsg.getOriginQueue();

                        concurrentMessage.setOutTime(new Date(), uniqueName);
                        concurrentMessage.setConsumerName("RetryConsumer");
                        if (!orginQueue.enqueue(concurrentMessage)) {
                            queue.offer(delayMsg);
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        });
        consumerThread.start();
    }


    public void setDelayInterval(long delayInterval) {
        this.delayInterval = delayInterval;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}

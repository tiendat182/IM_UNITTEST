package com.viettel.fw.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by PM2-LAMNV5 on 6/14/2016.
 */
public class DelayMessage implements Delayed {
    private Queue originQueue;
    private ConcurrentMessage data;
    private long startTime;

    public DelayMessage(Queue originQueue, ConcurrentMessage data, long delay) {
        this.originQueue = originQueue;
        this.data = data;
        this.startTime = System.currentTimeMillis() + delay;
    }

    public Queue getOriginQueue() {
        return originQueue;
    }

    public ConcurrentMessage getData() {
        return data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((DelayMessage) o).startTime) {
            return -1;
        }
        if (this.startTime > ((DelayMessage) o).startTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "{" +
                "data='" + data + '\'' +
                ", startTime=" + startTime +
                '}';
    }

}

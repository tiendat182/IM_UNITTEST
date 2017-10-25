package com.viettel.fw.concurrent;

import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.dto.BaseMessage;

import java.io.Serializable;
import java.util.*;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public class ConcurrentMessage extends BaseMessage implements Serializable {
    private String kpiId;
    private Date inTime;
    private Date outTime;
    private String producerName;
    private String consumerName;
    private int retryCount; //so lan retry

    private HashMap<String, Date> inQueueHistory = new HashMap<>();
    private HashMap<String, Date> outQueueHistory = new HashMap<>();
    private HashMap<String, Long> durationHistory = new HashMap<>();
    private HashMap<String, Date> inWHistory = new HashMap<>();
    private HashMap<String, Date> outWHistory = new HashMap<>();

    public void setInWorkerTime(Date date, String workerName) {
        inWHistory.put(workerName, date);
    }

    public void setOutWorkerTime(Date outTime, String workerName) {
        outWHistory.put(workerName, outTime);
        Long duration = durationHistory.get(workerName);
        if (inWHistory.containsKey(workerName)) {
            if (duration == null) {
                duration = outTime.getTime() - inWHistory.get(workerName).getTime();
            } else {
                duration += outTime.getTime() - inWHistory.get(workerName).getTime();
            }
            durationHistory.put(workerName, duration);
        }
    }

    public void setInTime(Date inTime, String queueName) {
        inQueueHistory.put(queueName, inTime);
        this.inTime = inTime;
    }

    public void setOutTime(Date outTime, String queueName) {
        outQueueHistory.put(queueName, outTime);
        Long duration = durationHistory.get(queueName);
        if (duration == null) {
            duration = outTime.getTime() - inQueueHistory.get(queueName).getTime();
        } else {
            duration += outTime.getTime() - inQueueHistory.get(queueName).getTime();
        }
        durationHistory.put(queueName, duration);

        this.outTime = outTime;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    @Override
    public String toString() {
        long processTime = (inTime != null && outTime != null) ? (outTime.getTime() - inTime.getTime()) : 0L;
        return "Message[" +
                queueHistory() +
                ", producerName='" + producerName + '\'' +
                ", consumerName='" + consumerName + '\'' +
                ", processTime=" + processTime +
                ", retryCount=" + retryCount +
                ", " + super.toString() +
                ']';
    }

    private String queueHistory() {
        String info = "queues[";
        for (HashMap.Entry<String, Date> entry : inQueueHistory.entrySet()) {
            Date inTime = entry.getValue();
            Date outTime = outQueueHistory.get(entry.getKey());
            info += "[queueName=" + entry.getKey() + ",inTime=" + DateUtil.date2ddMMyyyyHHMMss(inTime) + ",outTime=" + DateUtil.date2ddMMyyyyHHMMss(outTime) + ",duration=" + durationHistory.get(entry.getKey()) + "],";
        }

        info += "]";

        info += ",pc[";
        for (HashMap.Entry<String, Date> entry : inWHistory.entrySet()) {
            Date inTime = entry.getValue();
            Date outTime = outWHistory.get(entry.getKey());
            info += "[workerName=" + entry.getKey() + ",inTime=" + DateUtil.date2ddMMyyyyHHMMss(inTime) + ",outTime=" + DateUtil.date2ddMMyyyyHHMMss(outTime) + ",duration=" + durationHistory.get(entry.getKey()) + "],";
        }

        info += "]";

        return info;
    }
}

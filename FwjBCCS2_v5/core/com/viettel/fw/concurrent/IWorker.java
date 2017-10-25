package com.viettel.fw.concurrent;

import java.util.Map;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public interface IWorker {
    void setWorkerName(String name);
    String getWorkerName();

    /**
     * Override ham nay de cai dat nghiep vu
     * @throws Exception
     */
    void process() throws Exception;

    /**
     * Ham boc cua Thread.start
     */
    void start();
    void setQueue(Queue queue);
    void setSleepInterval(long sleepInterval);
    void setBatchSize(int size);
    void setContextParamMap(Map<String, Object> contextParamMap);
    Map<String, Object> getContextParamMap();

    /**
     * Ham init duoc goi truoc khi goi ham Thread.start
     * @throws Exception
     */
    void init() throws Exception;
}

package com.viettel.fw.concurrent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public abstract class Worker extends Thread implements IWorker {
    private static final Logger logger = Logger.getLogger(Worker.class);
    private String workerName;
    protected ApplicationContext context;
    protected Queue queue;
    private long sleepInterval;
    private int batchSize;
    private Map<String, Object> contextParamMap;

    public Worker() {
        sleepInterval = 0L;
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void run() {
        while (true) {
            try {
                process();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (sleepInterval > 0L) {
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        }
    }

    public abstract void logStart();

    public abstract void logEnd();

    @Override
    public void setSleepInterval(long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    @Override
    public Map<String, Object> getContextParamMap() {
        return contextParamMap;
    }

    @Override
    public void setContextParamMap(Map<String, Object> contextParamMap) {
        this.contextParamMap = contextParamMap;
    }

    @Override
    public String getWorkerName() {
        return workerName;
    }

    @Override
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
}

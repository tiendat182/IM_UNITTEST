package com.viettel.fw.concurrent;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PM2-LAMNV5 on 6/10/2016.
 */
public class JobGroup implements ApplicationContextAware {
    private static final Logger logger = Logger.getLogger(JobGroup.class);

    private int numOfInstance;
    private long sleepInterval;
    private Map<String, Object> contextParamMap;

    private Class clazzJob;

    private List<IWorker> jobLst;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public JobGroup() {
    }

    public void init() throws Exception {
        try {
            jobLst = new ArrayList<>();

            for (int i = 0; i < numOfInstance; i++) {
                IWorker producer = (IWorker) context.getBean(clazzJob);
                producer.setWorkerName(clazzJob.getSimpleName() + "-" + (i + 1));
                producer.setSleepInterval(sleepInterval);
                producer.setContextParamMap(contextParamMap);
                producer.init();
                producer.start();
                jobLst.add(producer);
            }


        } catch (Exception ex) {
            logger.fatal(ex);
            System.exit(-1);
        }
    }


    public void setClazzJob(Class clazzJob) {
        this.clazzJob = clazzJob;
    }

    public void setNumOfInstance(int numOfInstance) {
        this.numOfInstance = numOfInstance;
    }

    public void setSleepInterval(long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    public void setContextParamMap(Map<String, Object> contextParamMap) {
        this.contextParamMap = contextParamMap;
    }
}

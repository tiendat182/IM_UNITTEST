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
public class PCGroup<T extends ConcurrentMessage> implements ApplicationContextAware {
    private static final Logger logger = Logger.getLogger(PCGroup.class);

    private int numOfProducer;
    private int numOfConsumer;
    private long producerSleepInterval;
    private int producerBatchSize;
    private int consumerBatchSize;
    private Map<String, Object> contextParamMap;

    private Queue<T> queue;

    private Class clazzProducer;
    private Class clazzConsumer;

    private List<IWorker> producerLst;
    private List<IWorker> consumerLst;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public PCGroup() {
    }

    public void init() throws Exception {
        try {
            producerLst = new ArrayList<>();
            consumerLst = new ArrayList<>();

            for (int i = 0; i < numOfProducer; i++) {
                IWorker producer = (IWorker) context.getBean(clazzProducer);
                producer.setWorkerName(clazzProducer.getSimpleName() + "-" + (i + 1));
                producer.setSleepInterval(producerSleepInterval);
                producer.setBatchSize(producerBatchSize);
                producer.setQueue(queue);
                producer.setContextParamMap(contextParamMap);
                producer.init();
                producer.start();
                producerLst.add(producer);
            }

            for (int i = 0; i < numOfConsumer; i++) {
                IWorker consumer = (IWorker) context.getBean(clazzConsumer);
                consumer.setWorkerName(clazzConsumer.getSimpleName() + "-" + (i + 1));
                consumer.setQueue(queue);
                consumer.setContextParamMap(contextParamMap);
                consumer.setBatchSize(consumerBatchSize);
                consumer.init();
                consumer.start();
                consumerLst.add(consumer);
            }
        } catch (Exception ex) {
            logger.fatal(ex);
            System.exit(-1);
        }
    }

    public void setProducerSleepInterval(long producerSleepInterval) {
        this.producerSleepInterval = producerSleepInterval;
    }

    public void setNumOfProducer(int numOfProducer) {
        this.numOfProducer = numOfProducer;
    }

    public void setNumOfConsumer(int numOfConsumer) {
        this.numOfConsumer = numOfConsumer;
    }

    public void setProducerBatchSize(int producerBatchSize) {
        this.producerBatchSize = producerBatchSize;
    }

    public void setConsumerBatchSize(int consumerBatchSize) {
        this.consumerBatchSize = consumerBatchSize;
    }

    public void setClazzProducer(Class clazzProducer) {
        this.clazzProducer = clazzProducer;
    }

    public void setClazzConsumer(Class clazzConsumer) {
        this.clazzConsumer = clazzConsumer;
    }

    public void setQueue(Queue<T> queue) {
        this.queue = queue;
    }

    public void setContextParamMap(Map<String, Object> contextParamMap) {
        this.contextParamMap = contextParamMap;
    }
}

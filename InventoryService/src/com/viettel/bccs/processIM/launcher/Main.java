package com.viettel.bccs.processIM.launcher;

import com.viettel.fw.SystemConfig;
import com.viettel.process.BaseThread;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by thiendn1 on 30/12/2015.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContextProcess.xml"); //local
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:../conf/applicationContextProcess.xml");//server
//        BaseThread baseThread = (BaseThread) context.getBean("baseThread");
//        baseThread.start();
        BaseThread baseThread = context.getBean(BaseThread.class);
//        SystemConfig config = context.getBean(SystemConfig.class);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = baseThread::process;

        // Main coi nhu la scheduler nen phai fix cung interval scan la 1s
        executor.scheduleAtFixedRate(task, 0, 1000, TimeUnit.MILLISECONDS);

    }
}

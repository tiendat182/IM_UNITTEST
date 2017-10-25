package com.viettel.bccs.processIM.process;

import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Created by hoangnt14 on 7/29/2016.
 */

public class LogisticManager extends InventoryThread {

    private Long maxRetry;
    private Long maxDay;
    private Long sleepTime;
    private Long numberThread;
    private Long id;

    public LogisticManager(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            String excecutorName ;
            if (DataUtil.isNullOrZero(numberThread) && numberThread <= 0) {
                return;
            }
            for (long i = 0; i < numberThread; i++) {
                excecutorName = "Warning:type=Logistic,name=LogisticCommand_" + i;
                LogisticsThreadExecutor logisticsThreadExecutor = (LogisticsThreadExecutor) getCommand(LogisticsThreadExecutor.class, excecutorName, 1, 0, i, maxRetry, maxDay, sleepTime, numberThread);
                addCommandToBaseThread(logisticsThreadExecutor);
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public Long getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Long maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Long getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(Long maxDay) {
        this.maxDay = maxDay;
    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Long getNumberThread() {
        return numberThread;
    }

    public void setNumberThread(Long numberThread) {
        this.numberThread = numberThread;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
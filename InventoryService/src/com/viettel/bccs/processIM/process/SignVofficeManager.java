package com.viettel.bccs.processIM.process;

import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by hoangnt14 on 6/13/2016.
 */
public class SignVofficeManager extends InventoryThread {

    private Long maxRetry;
    private Long maxDay;
    private Long numberThread;
    private Long id;
    private List<Long> allowList;

    public SignVofficeManager(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            String excecutorName;
            if (DataUtil.isNullOrZero(numberThread) && numberThread <= 0) {
                return;
            }
            for (long i = 0; i < numberThread; i++) {
                excecutorName = "Warning:type=SignVOffice,name=SignVofficeCommand_" + new Random().nextInt(100000);
                SignVofficeThreadExecutor signVofficeThreadExecutor = (SignVofficeThreadExecutor) getCommand(SignVofficeThreadExecutor.class, excecutorName, 1, 0, maxRetry, maxDay, numberThread, allowList);
                addCommandToBaseThread(signVofficeThreadExecutor);
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

    public List<Long> getAllowList() {
        return allowList;
    }

    public void setAllowList(List<Long> allowList) {
        this.allowList = allowList;
    }
}

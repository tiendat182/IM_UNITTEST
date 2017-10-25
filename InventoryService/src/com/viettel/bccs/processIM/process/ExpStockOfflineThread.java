package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.repo.StockTransOfflineRepo;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.bccs.inventory.service.StockTransOfflineService;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt on 10/19/2016.
 */
public class ExpStockOfflineThread extends InventoryThread {

    public int processTotal;
    public int jobNumber;

    @Autowired
    StockTransOfflineService stockTransOfflineService;
    @Autowired
    StockTransOfflineRepo stockTransOfflineRepo;

    public ExpStockOfflineThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() throws Exception {
        try {
            reportError("Bat dau tien trinh xuat kho Offline");
            List<StockTrans> lstStockTransOffline = stockTransOfflineRepo.getListStockTransOffline(processTotal, jobNumber);
            if (!DataUtil.isNullOrEmpty(lstStockTransOffline)) {
                for (StockTrans stockTrans : lstStockTransOffline) {
                    try {

                    } catch (Exception e) {
                        // rollback ban ghi xu ly loi
                    }
                }
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public int getProcessTotal() {
        return processTotal;
    }

    public void setProcessTotal(int processTotal) {
        this.processTotal = processTotal;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }
}

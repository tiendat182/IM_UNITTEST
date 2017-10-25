package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.processIM.common.InventoryThread;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 7/23/2016.
 */
public class CancelRequestVTShopThread extends InventoryThread {
    private Long batch;

    @Autowired
    private StockNumberService stockNumberService;

    public CancelRequestVTShopThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau huy yeu cau VTShop ");
            stockNumberService.cancelRequestVTShop();
            reportError("------ket thuc huy yeu cau ");
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

}

package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.service.LockUserInfoService;
import com.viettel.bccs.processIM.common.InventoryThread;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 3/1/2017.
 */
public class UnlockUserStockInspectThread extends InventoryThread {
    @Autowired
    private LockUserInfoService lockUserInfoService;

    public UnlockUserStockInspectThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("Bat dau thuc hien unlock kiem ke");
            lockUserInfoService.unlockUserStockInspect();
            reportError("Ket thuc unlock kiem ke");
        } catch (Exception ex) {
            reportError(ex);
        }

    }
}

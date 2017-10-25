package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.service.LockUserInfoService;
import com.viettel.bccs.processIM.common.InventoryThread;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hoangnt14 on 12/2/2016.
 */
public class UnLockUserDamageThread extends InventoryThread {

    @Autowired
    private LockUserInfoService lockUserInfoService;


    public UnLockUserDamageThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("Bat dau chay thu tuc mo khoa user khong xuat tra hang hong : rp_recheck_damaged_serial ");
            //
            String prodName = "bccs_im.rp_recheck_damaged_serial";
            lockUserInfoService.processProcedureLockUserDamage(prodName);
            //
            reportError("Ket thuc thu tuc : rp_recheck_damaged_serial");
        } catch (Exception ex) {
            reportError(ex);
        }
    }

}

package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.bccs.inventory.service.LockUserInfoService;
import com.viettel.bccs.inventory.service.LockUserTypeService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 12/2/2016.
 */
public class LockUserDamageThread extends InventoryThread {

    @Autowired
    private LockUserInfoService lockUserInfoService;


    public LockUserDamageThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("Bat dau chay thu tuc khoa user khong xuat tra hang hong : pr_check_damaged_good ");
            //
            String prodName = "bccs_im.pr_check_damaged_good";
            lockUserInfoService.processProcedureLockUserDamage(prodName);
            //
            reportError("Ket thuc thu tuc : pr_check_damaged_good");
        } catch (Exception ex) {
            reportError(ex);
        }
    }

}

package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.service.LockUserInfoService;
import com.viettel.bccs.processIM.common.InventoryThread;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 8/4/2016.
 */
public class  UnlockUserThread extends InventoryThread {

    @Autowired
    private LockUserInfoService lockUserInfoService;

    public UnlockUserThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
     public void execute() {
        try {
            List<Long> lstDelete = Lists.newArrayList();
            List<LockUserInfoDTO> lstLockUserInfoDTOs = lockUserInfoService.getLstLockUserInfo();
            reportError("Tong so ban ghi lay duoc: " + lstLockUserInfoDTOs != null ? lstLockUserInfoDTOs.size() : "null");
            for (LockUserInfoDTO lockUserInfoDTO : lstLockUserInfoDTOs) {
                    reportError("Bat dau kiem tra unlock ID : " + lockUserInfoDTO.getId() + "|SHOP_ID: " + lockUserInfoDTO.getShopId() + "|STAFF_ID" + lockUserInfoDTO.getStaffId());
                    if (lockUserInfoService.canUnlock(lockUserInfoDTO.getSqlCheckTrans(), lockUserInfoDTO.getTransId(), lockUserInfoDTO.getTransDate())) {
                        lstDelete.add(lockUserInfoDTO.getId());
                    } else {
                        reportError("Unlock that bai ID : " + lockUserInfoDTO.getId() + "|SHOP_ID: " + lockUserInfoDTO.getShopId() + "|STAFF_ID" + lockUserInfoDTO.getStaffId());
                    }
            }
            try {
                reportError("Bat dau thuc hien unlock : " + lstDelete.size() + "ban ghi ");
                lockUserInfoService.deleteLockUser(lstDelete);
                reportError("Ket thuc unlock : " + lstDelete.size() + "ban ghi");
            } catch (Exception e) {
                reportError(e);
            }
        } catch (Exception ex) {
            reportError(ex);
        }

    }

}

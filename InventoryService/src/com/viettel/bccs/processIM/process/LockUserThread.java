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
 * Created by hoangnt14 on 8/3/2016.
 */
public class LockUserThread extends InventoryThread {

    @Autowired
    private LockUserInfoService lockUserInfoService;

    @Autowired
    private LockUserTypeService lockUserTypeService;

    public LockUserThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            List<LockUserTypeDTO> lstLockUserTypeDTOs = lockUserTypeService.getAllUserLockType();
            if (!DataUtil.isNullOrEmpty(lstLockUserTypeDTOs)) {
                reportError("Bat dau khoa :" + lstLockUserTypeDTOs.size() + " user");
                for (LockUserTypeDTO lockUserTypeDTO : lstLockUserTypeDTOs) {
                    List<LockUserInfoDTO> lstUserInfo = lockUserInfoService.getLockUserInfo(lockUserTypeDTO.getSqlCmd(), lockUserTypeDTO.getId(),
                            lockUserTypeDTO.getValidRange(), lockUserTypeDTO.getCheckRange());
                    for (LockUserInfoDTO lockUserInfoDTO : lstUserInfo) {
                        if (lockUserInfoService.checkExistLockUserInfo(lockUserInfoDTO.getTransId(),
                                lockUserInfoDTO.getTransType(), lockUserInfoDTO.getTransStatus())) {
                            continue;
                        }
                        lockUserInfoService.create(lockUserInfoDTO);
                        reportError("Thuc hien khoa user voi transId :" + lockUserInfoDTO.getTransId());
                    }
                }
                reportError("Ket thuc khoa :" + lstLockUserTypeDTOs.size() + " user");
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }
}

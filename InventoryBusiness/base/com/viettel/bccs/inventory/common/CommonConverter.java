package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.fw.dto.UserDTO;


/**
 * Created by thanhnt on 5/25/2015.
 */
public  class CommonConverter {
    public static StaffDTO convertUserToStaff(UserDTO userDTO) {
        StaffDTO staffDTO = new StaffDTO();
        if (userDTO != null) {
            staffDTO.setStaffCode(userDTO.getStaffCode());
            staffDTO.setStaffId(userDTO.getStaffId());
            staffDTO.setShopCode(userDTO.getShopCode());
            staffDTO.setShopId(userDTO.getShopId());
        }
        return staffDTO;
    }
}

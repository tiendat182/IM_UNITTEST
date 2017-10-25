package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.fw.common.util.DataUtil;
import org.apache.log4j.Logger;

/**
 * Created by ChungNV7 on 1/8/2016.
 */
public class IsdnUtil {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 11;

    public static final Logger logger = Logger.getLogger(IsdnUtil.class);

    public static boolean validateNumberAndLength(String str, int min, int max) {
        if (DataUtil.isNullOrEmpty(str)) {
            return false;
        }
        while (str.startsWith("0")) {
            str = str.substring(1);
        }
        if ((str.length() < min) || str.length() > max) {
            return false;
        }
        try {
            Long.parseLong(str);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }



    public static boolean validExcelFilename(String filename) {
        if (DataUtil.isNullOrEmpty(filename)) {
            return false;
        }
        String[] arr = IsdnConst.EXCEL_TYPE.split("\\|");
        for (int i = 0; i < arr.length; i++) {
            if (filename.endsWith(arr[i].trim())) {
                return true;
            }
        }
        return false;
    }

    public static String getTelecomService(Long serviceId) {
        if (DataUtil.isNullOrZero(serviceId)) {
            return "";
        }
        if (serviceId >= 3) {
            return "3";
        } else {
            return serviceId.toString();
        }
    }

    public static String getTelecomService(String serviceId) {
        if (DataUtil.isNullOrEmpty(serviceId) || !DataUtil.isNumber(serviceId)) {
            return "";
        }
        Long id = DataUtil.safeToLong(serviceId);
        if (id >= 3) {
            return "3";
        } else {
            return id.toString();
        }
    }

    public static boolean checkTelecomService(String telecomService, GroupFilterRuleDTO group) {
        if (!DataUtil.isNumber(telecomService)) {
            return false;
        }
        Long telecomServiceId = DataUtil.safeToLong(telecomService);
        if (DataUtil.isNullObject(group) || DataUtil.isNullOrZero(group.getTelecomServiceId())) {
            return false;
        }
        if (group.getTelecomServiceId() >= 3) {
            if (telecomServiceId != 3) {
                return false;
            }
        } else {
            if (telecomServiceId.longValue() != group.getTelecomServiceId().longValue()) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkTelecomService(String telecomService, String service) {
        if (!DataUtil.isNumber(telecomService)) {
            return false;
        }
        Long telecomServiceId = DataUtil.safeToLong(telecomService);
        if (DataUtil.isNullOrEmpty(service) || !DataUtil.isNumber(service)) {
            return false;
        }
        Long id = DataUtil.safeToLong(service);
        if (id >= 3) {
            if (telecomServiceId != 3) {
                return false;
            }
        } else {
            if (telecomServiceId.longValue() != id.longValue()) {
                return false;
            }
        }
        return true;
    }

}

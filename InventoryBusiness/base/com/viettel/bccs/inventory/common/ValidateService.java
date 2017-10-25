package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.fw.common.util.DataUtil;

import java.util.List;

/**
 * Created by hoangnt14 on 12/10/2015.
 */
public class ValidateService {
    public static boolean checkValueContainOptionSet(String value, List<OptionSetValueDTO> lst) {
        boolean ok = false;
        if (DataUtil.isNullObject(lst)) {
            return false;
        }
        for (OptionSetValueDTO optionSetValueDTO : lst) {
            if (optionSetValueDTO.getValue().equals(value)) {
                return true;
            }
        }
        return ok;
    }

    public static ProductOfferingTotalDTO getProductOfferingByCode(String productCode, List<ProductOfferingTotalDTO> lst) {

        if (DataUtil.isNullObject(lst)) {
            return null;
        }
        for (ProductOfferingTotalDTO productOfferingTotalDTO : lst) {
            if (productOfferingTotalDTO.getCode().equals(productCode)) {
                return productOfferingTotalDTO;
            }
        }
        return null;
    }

    public static boolean checkValueContainShopById(Long shopId, List<ShopDTO> lst) {
        boolean ok = false;
        if (DataUtil.isNullObject(lst)) {
            return false;
        }
        for (ShopDTO shopDTO : lst) {
            if (DataUtil.safeEqual(shopDTO.getShopId(), shopId)) {
                return true;
            }
        }
        return ok;
    }

    public static long countDigitOfNumber(Long number) {
        String s = new Long(number).toString();
        return (long) s.length();

    }
}

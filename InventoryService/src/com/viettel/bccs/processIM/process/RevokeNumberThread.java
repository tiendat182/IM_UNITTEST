package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 6/15/2016.
 */
public class RevokeNumberThread extends InventoryThread {
    private Long day;

    @Autowired
    private StockNumberService stockNumberService;

    @Autowired
    private OptionSetValueService optionSetValueService;

    public RevokeNumberThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau thu hoi so ");
            day = DataUtil.safeToLong(optionSetValueService.getValueByTwoCodeOption(Const.REVOKE_NUMBER.DAY_MOBILE_REUSE, Const.REVOKE_NUMBER.DAY_MOBILE_REUSE));
            List<Long> lstRevokeShopReuseId = stockNumberService.getLstShopIdFromOptionSet(Const.REVOKE_NUMBER.REVOKE_SHOP_MOBILE_REUSE);
            List<Long> lstRevokeShopId = stockNumberService.getLstShopIdFromOptionSet(Const.REVOKE_NUMBER.REVOKE_SHOP_MOBILE);
            if (!DataUtil.isNullOrEmpty(lstRevokeShopReuseId) && !DataUtil.isNullOrEmpty(lstRevokeShopId)) {
                int result = stockNumberService.revokeNumber(lstRevokeShopId, day, lstRevokeShopReuseId, "SYS", "SYS");
                reportError("------thu hoi thanh cong " + result);
            }
            reportError("------Ket thuc thu hoi so ");
        } catch (Exception ex) {
            reportError(ex);

        }
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }
}

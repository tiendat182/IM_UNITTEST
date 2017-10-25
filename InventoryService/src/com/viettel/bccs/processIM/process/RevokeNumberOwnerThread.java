package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 7/22/2016.
 */
public class RevokeNumberOwnerThread extends InventoryThread {
    private Long sleepTime;
    private Long batch;
    private Long day;
    private List<OptionSetValueDTO> lstDay = Lists.newArrayList();
    private List<OptionSetValueDTO> lstShop = Lists.newArrayList();

    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopService;

    public RevokeNumberOwnerThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau thu hoi so ");
            lstDay = optionSetValueService.getByOptionSetCode(Const.REVOKE_NUMBER.DAY_REVOKE_OWNER);
            if (!DataUtil.isNullOrEmpty(lstDay)) {
                day = DataUtil.safeToLong(lstDay.get(0).getValue());
            }
            lstShop = optionSetValueService.getByOptionSetCode(Const.REVOKE_NUMBER.SHOP_REVOKE_OWNER);
            String shopCode = "";
            if (!DataUtil.isNullOrEmpty(lstShop)) {
                shopCode = DataUtil.safeToString(lstShop.get(0).getValue());
            }
            ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(shopCode);
            int result = 0;
            if (!DataUtil.isNullObject(shopDTO)) {
                result = stockNumberService.revokeNumberOwner(shopDTO.getShopId(), day, Const.SHOP.SHOP_VTT_ID, "SYS");
            }
            reportError("------thu hoi thanh cong " + result);
        } catch (Exception ex) {
            reportError(ex);
        } finally {
            try {
                Thread.sleep(sleepTime);
            } catch (Exception ex) {
                reportError(ex);
            }
        }

    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }
}

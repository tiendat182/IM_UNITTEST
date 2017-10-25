package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.StockNumberDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 6/15/2016.
 */
public class ReleaseNiceNumberThread extends InventoryThread {
    private Long batch;
    private Double keepTimeHour;

    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopService;

    public ReleaseNiceNumberThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau tien trinh giai phong so dep ");
            List<Long> lstReleaseShopId = Lists.newArrayList();
            keepTimeHour = DataUtil.safeToDouble(optionSetValueService.getValueByTwoCodeOption(Const.REVOKE_NUMBER.TIME_LOCK_NICE_NUMBER, Const.REVOKE_NUMBER.TIME_LOCK_NICE_NUMBER));
            if (DataUtil.isNullObject(keepTimeHour) || DataUtil.safeEqual(keepTimeHour, 0.0)) {
                reportError("------Chua cau hinh thoi gian giai phong so dep --- ");
                return;
            }
            String lstShopCode = optionSetValueService.getValueByTwoCodeOption(Const.REVOKE_NUMBER.STOCK_LOCK_NICE_NUMBER_10, Const.REVOKE_NUMBER.STOCK_LOCK_NICE_NUMBER_10);
            if (DataUtil.isNullOrEmpty(lstShopCode)) {
                reportError("------Chua cau hinh kho giai phong so dep 10 so --- ");
                return;
            }
            String[] shopSplit = lstShopCode.split(",");
            for (String shopCode : shopSplit) {
                ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(shopCode);
                if (!DataUtil.isNullObject(shopDTO)) {
                    lstReleaseShopId.add(shopDTO.getShopId());
                }
            }

            String lstShopCode11 = optionSetValueService.getValueByTwoCodeOption(Const.REVOKE_NUMBER.STOCK_LOCK_NICE_NUMBER_11, Const.REVOKE_NUMBER.STOCK_LOCK_NICE_NUMBER_11);
            if (DataUtil.isNullOrEmpty(lstShopCode11)) {
                reportError("------Chua cau hinh kho giai phong so dep 11 so --- ");
                return;
            }
            String[] shopSplit11 = lstShopCode11.split(",");
            for (String shopCode : shopSplit11) {
                ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(shopCode);
                if (!DataUtil.isNullObject(shopDTO)) {
                    lstReleaseShopId.add(shopDTO.getShopId());
                }
            }

            List<StockNumberDTO> lstRelease = stockNumberService.getAllListReleaseNumber(keepTimeHour, lstReleaseShopId);
            if (!DataUtil.isNullOrEmpty(lstRelease)) {
                for (StockNumberDTO stockNumberDTO : lstRelease) {
                    try {
                        stockNumberDTO.setLastUpdateUser(null);
                        stockNumberService.releaseNumber(stockNumberDTO);
                    } catch (Exception ex) {
                        reportError(ex);
                    }
                }
            }
//            reportError("------Giai phong thanh cong " + result + " so");
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public Long getBatch() {
        return batch;
    }

    public void setBatch(Long batch) {
        this.batch = batch;
    }


}

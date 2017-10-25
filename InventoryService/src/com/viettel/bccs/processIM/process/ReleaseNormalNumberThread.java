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
public class ReleaseNormalNumberThread extends InventoryThread {
    private Long batch;
    private Double keepTimeHour;

    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopService;

    public ReleaseNormalNumberThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------[GPST]Bat dau tien trinh giai phong so thuong ");
            List<Long> lstReleaseShopId = Lists.newArrayList();
            keepTimeHour = DataUtil.safeToDouble(optionSetValueService.getValueByTwoCodeOption(Const.REVOKE_NUMBER.TIME_LOCK_NORMAL_NUMBER, Const.REVOKE_NUMBER.TIME_LOCK_NORMAL_NUMBER));
            if (DataUtil.isNullObject(keepTimeHour) || DataUtil.safeEqual(keepTimeHour, 0.0)) {
                reportError("------[GPST]Chua cau hinh thoi gian giai phong so thuong --- ");
                return;
            }
            String lstShopCode = optionSetValueService.getValueByTwoCodeOption(Const.REVOKE_NUMBER.STOCK_LOCK_NORMAL_NUMBER, Const.REVOKE_NUMBER.STOCK_LOCK_NORMAL_NUMBER);
            if (DataUtil.isNullOrEmpty(lstShopCode)) {
                reportError("------[GPST]Chua cau hinh kho giai phong so thuong --- ");
                return;
            }
            String[] shopSplit = lstShopCode.split(",");
            for (String shopCode : shopSplit) {
                ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(shopCode);
                if (!DataUtil.isNullObject(shopDTO)) {
                    lstReleaseShopId.add(shopDTO.getShopId());
                }
            }
            Long count = 0L;
            List<StockNumberDTO> lstRelease = stockNumberService.getAllListReleaseNumber(keepTimeHour, lstReleaseShopId);
            if (!DataUtil.isNullOrEmpty(lstRelease)) {
                for (StockNumberDTO stockNumberDTO : lstRelease) {
                    try {
                        stockNumberDTO.setLastUpdateUser(null);
                        stockNumberDTO.setLastUpdateIpAddress("SYS");
                        stockNumberService.releaseNumber(stockNumberDTO);
                        count++;
                    } catch (Exception ex) {
                        reportError(ex);
                    }
                }
            }
            reportError("------[GPST]Giai phong thanh cong " + count + "/" + lstRelease.size() + " so thuong");
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

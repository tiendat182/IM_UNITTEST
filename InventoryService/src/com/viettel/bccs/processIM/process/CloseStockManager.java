package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;

import java.util.List;
import java.util.Random;

/**
 */
public class CloseStockManager extends InventoryThread {

    private Long sleepTime;
    private Long numberThread = 1L;

    public CloseStockManager(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            String excecutorName;
            if (DataUtil.isNullOrZero(numberThread) && numberThread <= 0) {
                return;
            }
            List<Long> ownerTypes = getOwnerTypes();
            List<Long> productOfferTypes = getProductOfferTypes();
            for (Long ownerType : ownerTypes) {
                for (Long productOfferTypeId : productOfferTypes) {
                    for (long i = 0; i < numberThread; i++) {
                        excecutorName = getThreadName(ownerType, productOfferTypeId);
                        excecutorName += "_" + i;
                        CloseStockExecutor closeStockExecutor = (CloseStockExecutor) getCommand(CloseStockExecutor.class, excecutorName, 1, 0, sleepTime, productOfferTypeId, ownerType, i, numberThread);
                        addCommandToBaseThread(closeStockExecutor);
                    }
                }
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    private List<Long> getOwnerTypes() {
        List<Long> ownerType = Lists.newArrayList();
        ownerType.add(Const.OWNER_TYPE.SHOP_LONG);
        ownerType.add(Const.OWNER_TYPE.STAFF_LONG);
        return ownerType;
    }

    private String getThreadName(Long ownerType, Long productOfferType) {
        String excecutorName = "process:name=CloseStockExecutor";
        if (DataUtil.safeEqual(Const.OWNER_TYPE.SHOP_LONG, ownerType)) {
            excecutorName += "_shop";
        } else {
            excecutorName += "_staff";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOfferType)) {
            excecutorName += "_handset";
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ACCESSORIES, productOfferType)) {
            excecutorName += "_accessories";
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT, productOfferType)) {
            excecutorName += "_kit";
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID, productOfferType)) {
            excecutorName += "_simPre";
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_POST_PAID, productOfferType)) {
            excecutorName += "_simPost";
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD, productOfferType)) {
            excecutorName += "_card";
        }
        return excecutorName;
    }

    private List<Long> getProductOfferTypes() {
        List<Long> productOfferType = Lists.newArrayList();
        productOfferType.add(Const.STOCK_TYPE.STOCK_HANDSET);
        productOfferType.add(Const.STOCK_TYPE.STOCK_ACCESSORIES);
//        productOfferType.add(Const.STOCK_TYPE.STOCK_KIT);
//        productOfferType.add(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID);
//        productOfferType.add(Const.STOCK_TYPE.STOCK_SIM_POST_PAID);
//        productOfferType.add(Const.STOCK_TYPE.STOCK_CARD);
        return productOfferType;
    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Long getNumberThread() {
        return numberThread;
    }

    public void setNumberThread(Long numberThread) {
        this.numberThread = numberThread;
    }
}

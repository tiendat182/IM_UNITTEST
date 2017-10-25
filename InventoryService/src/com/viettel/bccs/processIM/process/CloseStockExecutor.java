package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.StockTotalDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.bccs.inventory.model.ThreadCloseStock;
import com.viettel.bccs.inventory.repo.StockTotalRepo;
import com.viettel.bccs.inventory.repo.ThreadCloseStockRepo;
import com.viettel.bccs.inventory.service.StockTotalCycleService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.bccs.inventory.service.ThreadCloseStockService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author luannt23.
 * @comment
 * @date 3/4/2016.
 */
@Service
@Scope("prototype")
@Lazy
public class CloseStockExecutor extends InventoryThread {
    private static final Logger logger = Logger.getLogger(CloseStockExecutor.class);
    private Long sleepTime;
    private Long productOfferTypeId;
    private Long ownerType;
    private Long numberThread;
    private Long number;
    private String threadName;
    private String syncType;
    @Autowired
    private ThreadCloseStockRepo threadCloseStockRepo;
    @Autowired
    private ThreadCloseStockService threadCloseStockService;
    @Autowired
    private StockTotalCycleService stockTotalCycleService;

    public CloseStockExecutor(String name, int numActorLimit, int type, String syncType) {
        super(name, numActorLimit, type);
        this.syncType = syncType;
    }

    public CloseStockExecutor(String name, int numActorLimit, int type, Long sleepTime, Long productOfferTypeId, Long ownerType, Long number, Long numberThread) {
        super(name, numActorLimit, type);
        this.sleepTime = sleepTime;
        this.productOfferTypeId = productOfferTypeId;
        this.ownerType = ownerType;
        this.number = number;
        this.numberThread = numberThread;
        this.threadName = name;
    }

    @Override
    public void execute() {
        try {
            // Kiem tra dieu kien xu ly
            List<Long> ownerTypes = getOwnerTypes();
            List<Long> productOfferTypes = getProductOfferTypes();
            List<Long> productOfferTypeSolr = getProductOfferTypeSolr();

            // Thuc hien chot ton Handset
            for (Long ownerType : ownerTypes) {
                for (Long productOfferTypeId : productOfferTypes) {
                    try {
                        reportError("-----Bat dau chay Thread: " + getThreadName(ownerType, productOfferTypeId));

                        ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
                        threadCloseStockDTO.setThreadName(DateUtil.date2ddMMyyyyString(new Date()) + "_" + getThreadName(ownerType, productOfferTypeId));
                        stockTotalCycleService.doStockTotalCycle(logger, ownerType, productOfferTypeId, threadCloseStockDTO);
                        threadCloseStockService.create(threadCloseStockDTO);
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                    reportError("-----Ket thuc tien trinh: " + getThreadName(ownerType, productOfferTypeId));
                }
            }

            //Thuc hien chot ton tren SOLR
            for (Long productOfferTypeId : productOfferTypeSolr) {
                try {
                    reportError("-----Bat dau chay Thread: " + getThreadName(null, productOfferTypeId));

                    ThreadCloseStockDTO threadCloseStockDTO = new ThreadCloseStockDTO();
                    threadCloseStockDTO.setThreadName(DateUtil.date2ddMMyyyyString(new Date()) + "_" + getThreadName(null, productOfferTypeId));
                    stockTotalCycleService.doStockTotalCycle(logger, null, productOfferTypeId, threadCloseStockDTO);
                    threadCloseStockService.create(threadCloseStockDTO);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
                reportError("-----Ket thuc tien trinh: " + getThreadName(ownerType, productOfferTypeId));
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private String getThreadName(Long ownerType, Long productOfferType) {
        String excecutorName = "process:name=CloseStockExecutor";
        if (DataUtil.safeEqual(Const.OWNER_TYPE.SHOP_LONG, ownerType)) {
            excecutorName += "_shop";
        } else if (DataUtil.safeEqual(Const.OWNER_TYPE.STAFF_LONG, ownerType)) {
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

    private List<Long> getOwnerTypes() {
        List<Long> ownerType = Lists.newArrayList();
        ownerType.add(Const.OWNER_TYPE.SHOP_LONG);
        ownerType.add(Const.OWNER_TYPE.STAFF_LONG);
        return ownerType;
    }

    private List<Long> getProductOfferTypes() {
        List<Long> productOfferType = Lists.newArrayList();
        if ("DB".equals(syncType)) {
            productOfferType.add(Const.STOCK_TYPE.STOCK_HANDSET);
            productOfferType.add(Const.STOCK_TYPE.STOCK_ACCESSORIES);
        }

        return productOfferType;
    }

    private List<Long> getProductOfferTypeSolr() {
        List<Long> productOfferType = Lists.newArrayList();
        if ("SOLR".equals(syncType)) {
            productOfferType.add(Const.STOCK_TYPE.STOCK_KIT);
            productOfferType.add(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID);
            productOfferType.add(Const.STOCK_TYPE.STOCK_CARD);
        }
        return productOfferType;
    }

    private void insertThreadCloseStock(String threadName) throws Exception {
        ThreadCloseStockDTO threadCloseStock = new ThreadCloseStockDTO();
        threadCloseStock.setThreadName(threadName);
        threadCloseStock.setStartHour("00");
        threadCloseStock.setStartMinute("00");
        threadCloseStock.setStartSecond("00");
        threadCloseStock.setStatus(1L);
        threadCloseStockService.create(threadCloseStock);
    }

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }
}

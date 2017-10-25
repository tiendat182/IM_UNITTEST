package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.LogisticsWSUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.ErrorCodeLogistic;
import com.viettel.bccs.inventory.model.StockTransLogistic;
import com.viettel.bccs.inventory.service.LogisticBaseService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockTransLogisticService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hoangnt14 on 7/27/2016.
 */
@Service
@Scope("prototype")
@Lazy
public class LogisticsThreadExecutor extends InventoryThread {
    private Long sleepTime;
    private Long batch;
    private Long maxDay;
    private Long maxRetry;
    private Long numThread;
    private Long id;

    @Autowired
    private StockTransLogisticService stockTransLogisticService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private LogisticBaseService logisticBaseService;

    public LogisticsThreadExecutor(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    public LogisticsThreadExecutor(String name, int numActorLimit, int type, Long id, Long maxRetry, Long maxDay, Long sleepTime, Long numberThread) {
        super(name, numActorLimit, type);
        this.id = id;
        this.maxRetry = maxRetry;
        this.maxDay = maxDay;
        this.sleepTime = sleepTime;
        this.numThread = numberThread;
    }

    @Override
    public void execute() {
        try {
            List<StockTransLogisticDTO> lstStockTransLogisticDTOs =
                    DataUtil.defaultIfNull(stockTransLogisticService.getLstOrderLogistics(id, maxRetry, maxDay, numThread), Lists.newArrayList());
            reportError("-----bat dau chay Thread thu " + id + "");
            reportError("-----bat dau dong bo " + lstStockTransLogisticDTOs.size() + " ban ghi");
            BaseMessage baseMessage = new BaseMessage();
            for (StockTransLogisticDTO stockTransLogisticDTO : lstStockTransLogisticDTOs) {
                try {
                    reportError("trinh ky stock_trans_logistics_id : " + stockTransLogisticDTO.getStockTransLogisticId());
                    StockTransDTO stockTransDTO = stockTransService.getStockTransForLogistics(stockTransLogisticDTO.getStockTransId());
                    if (DataUtil.isNullObject(stockTransDTO)) {
                        reportError("-----stockTrans khong ton tai voi id " + stockTransLogisticDTO.getStockTransId() + "");
                        baseMessage.setErrorCode(ErrorCodeLogistic.STOCK_TRANS_ID_NOT_EXISTED.getErrorCode());
                        baseMessage.setDescription(ErrorCodeLogistic.STOCK_TRANS_ID_NOT_EXISTED.getDescription());
                        stockTransLogisticService.updateStockTransLogistics(stockTransLogisticDTO.getStockTransLogisticId(), Const.LOGISTICS_STATUS.FAIL, baseMessage);
                    } else {
                        //truong hop xuat ban dut cho dai ly
                        //kiem tra giao dich da duoc ban hang va lap hoa don chua
                        if (!DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus())) {
                            if (!stockTransService.checkSaleTrans(stockTransDTO.getStockTransId(), stockTransDTO.getCreateDatetime())) {
                                reportError("Giao dich xuat ban dut cho dai ly stockTransId = " + stockTransDTO.getStockTransId() + " chua duoc ban hang va lap hoa don.....");
                                continue;
                            }
                        }
                        //truong hop ban dat coc cho dai ly
                        //nhung chua lap phieu thu
                        if (!DataUtil.isNullOrEmpty(stockTransDTO.getDepositStatus()) && !DataUtil.safeEqual(stockTransDTO.getDepositStatus(), Const.DEPOSIT_STATUS.DEPOSIT_HAVE)) {
                            reportError("Giao dich xuat dat coc cho dai ly stockTransId = " + stockTransDTO.getStockTransId() + " chua lap phieu thu chi dat coc.....");
                            continue;
                        }
                        //Lay thong tin danh sach mat hang
                        List<ProductOfferingLogisticDTO> lstProductOfferings = productOfferingService.getProductOfferingLogistic(stockTransDTO.getStockTransId());
                        if (DataUtil.isNullObject(lstProductOfferings)) {
                            reportError("-----Khong ton tai mat hang voi stockTransid " + stockTransDTO.getStockTransId() + "");
                            baseMessage.setErrorCode(ErrorCodeLogistic.PRODUCT_OFFERING_NOT_EXISTED.getErrorCode());
                            baseMessage.setDescription(ErrorCodeLogistic.PRODUCT_OFFERING_NOT_EXISTED.getDescription());
                            stockTransLogisticService.updateStockTransLogistics(stockTransLogisticDTO.getStockTransLogisticId(), Const.LOGISTICS_STATUS.FAIL, baseMessage);
                        } else {
                            //goi webservice lap yeu cau tao phieu sang logistic
                            baseMessage = LogisticsWSUtils.createOrder(stockTransDTO, lstProductOfferings);
                            //truong hop goi webserice loi, cap nhat ket qua thuc hien retry lan sau
                            if (DataUtil.safeEqual(Const.LOGISTIC_WS_STATUS.CALL_WS, baseMessage.getErrorCode())) {
                                if (stockTransLogisticDTO.getLogisticRetry() < maxRetry) {
                                    stockTransLogisticService.updateStockTransLogisticRetry(maxRetry, stockTransLogisticDTO.getStockTransLogisticId(), baseMessage);
                                } else {
                                    //Cap nhat trang thai that bai
                                    stockTransLogisticService.updateStockTransLogistics(stockTransLogisticDTO.getStockTransLogisticId(), Const.LOGISTICS_STATUS.FAIL, baseMessage);
                                }
                            } else {
                                if (DataUtil.safeEqual(Const.LOGISTIC_WS_STATUS.SUCCESS, baseMessage.getErrorCode())) {
                                    //cap nhat trang thai gui yeu cau sang logistic thanh cong
                                    stockTransLogisticService.updateStockTransLogistics(stockTransLogisticDTO.getStockTransLogisticId(), Const.LOGISTICS_STATUS.SUCCESS, baseMessage);
                                } else {
                                    //cap nhat trang thai gui yeu cau sang logistic bi tu choi
                                    stockTransLogisticService.updateStockTransLogistics(stockTransLogisticDTO.getStockTransLogisticId(), Const.LOGISTICS_STATUS.REJECT, baseMessage);
                                    //Neu chua thanh toan hoac dat coc thi huy lenh
                                    if (DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.isNullOrEmpty(stockTransDTO.getDepositStatus())) {
                                        OrderObjectDTO orderObjectDTO = new OrderObjectDTO();
                                        orderObjectDTO.setOrderCode(stockTransDTO.getStockTransId().toString());
                                        ResultLogisticsDTO result = logisticBaseService.cancelOrderOrBill(orderObjectDTO);
                                        if (DataUtil.safeEqual(result.getResponseCode(), com.viettel.bccs.inventory.common.Const.LOGISTICS.SUCCESS)) {
                                            reportError("-----Huy lenh thanh cong " + stockTransDTO.getStockTransId() + "");
                                            continue;
                                        }
                                    }
                                }
                            }

                        }
                    }
                    reportError("------ket thuc dong bo Logistics ");
                } catch (Exception e) {
                    reportError(e);
                    if (stockTransLogisticDTO.getLogisticRetry() < maxRetry) {
                        stockTransLogisticService.updateStockTransLogisticRetry(maxRetry, stockTransLogisticDTO.getStockTransLogisticId(), baseMessage);
                    } else {
                        //Cap nhat trang thai that bai
                        stockTransLogisticService.updateStockTransLogistics(stockTransLogisticDTO.getStockTransLogisticId(), Const.LOGISTICS_STATUS.FAIL, baseMessage);
                    }
                }
            }
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

    public Long getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(Long maxDay) {
        this.maxDay = maxDay;
    }

    public Long getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Long maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Long getNumThread() {
        return numThread;
    }

    public void setNumThread(Long numThread) {
        this.numThread = numThread;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

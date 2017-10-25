package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.dto.StockRequestOrderTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.service.StockRequestOrderService;
import com.viettel.bccs.inventory.service.StockRequestOrderTransService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 10/27/2016.
 */
public class GoodRevokeImportThread extends InventoryThread {

    @Autowired
    private StockRequestOrderService stockRequestOrderService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockRequestOrderTransService stockRequestOrderTransService;


    public GoodRevokeImportThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau tien trinh sinh phieu nhap sau khi CN xuat kho ");

            List<StockTransDTO> lstStockTrans = stockTransService.getLstRequestOrderExported();
            if (!DataUtil.isNullOrEmpty(lstStockTrans)) {
                for (StockTransDTO stockTransDTO : lstStockTrans) {
                    try {
                        stockRequestOrderService.processImportStock(stockTransDTO.getStockTransId());
                    } catch (LogicException ex) {
                        StockRequestOrderTransDTO stockRequestOrderTransError = stockRequestOrderTransService.getOrderTransByStockTransId(stockTransDTO.getStockTransId());
                        if (DataUtil.isNullObject(stockRequestOrderTransError)) {
                            stockRequestOrderTransError = new StockRequestOrderTransDTO();
                            stockRequestOrderTransError.setRetry(1L);
                            stockRequestOrderTransError.setStockTransType(2L);
                            stockRequestOrderTransError.setStockTransId(stockTransDTO.getStockTransId());
                            stockRequestOrderTransError.setCreateDatetime(new Date());
                        } else {
                            stockRequestOrderTransError.setRetry(stockRequestOrderTransError.getRetry() + 1L);
                        }
                        stockRequestOrderTransError.setErrorCode(ex.getErrorCode());
                        stockRequestOrderTransError.setErrorCodeDescription(ex.getDescription());
                        stockRequestOrderTransService.save(stockRequestOrderTransError);
                        reportError(ex);
                    } catch (Exception e) {
                        reportError(e);
                    }
                }
            }
            reportError("------Ket thuc tien trinh sinh phieu nhap sau khi CN xuat kho");
        } catch (Exception ex) {
            reportError(ex);
        }
    }


}

package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.bccs.inventory.dto.StockRequestOrderTransDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.StockRequestOrderDetail;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 10/27/2016.
 */
public class GoodRevokeCreateNoteThread extends InventoryThread {

    @Autowired
    private StockRequestOrderService stockRequestOrderService;

    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @Autowired
    private StockRequestOrderTransService stockRequestOrderTransService;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;


    public GoodRevokeCreateNoteThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau tien trinh lap phieu hang thu hoi ");
            List<StockRequestOrderDetailDTO> lstDetail = stockRequestOrderDetailService.getLstDetailToExport();
            for (StockRequestOrderDetailDTO stockRequestOrderDetailDTO : lstDetail) {
                StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByActionId(stockRequestOrderDetailDTO.getStockRequestOrderId());
                if (DataUtil.safeEqual(stockTransVofficeDTO.getStatus(), Const.VOFFICE_STATUS.SIGNED)) {
                    try {
                        stockRequestOrderService.processCreateExpNote(stockRequestOrderDetailDTO.getStockRequestOrderId()
                                , stockRequestOrderDetailDTO.getFromOwnerId(), stockRequestOrderDetailDTO.getToOwnerId());
                    } catch (LogicException ex) {
                        StockRequestOrderTransDTO stockRequestOrderTransError = stockRequestOrderTransService.getOrderTrans(stockRequestOrderDetailDTO.getStockRequestOrderId()
                                , stockRequestOrderDetailDTO.getFromOwnerId(), stockRequestOrderDetailDTO.getToOwnerId(), 1L);
                        if (DataUtil.isNullObject(stockRequestOrderTransError)) {
                            stockRequestOrderTransError = new StockRequestOrderTransDTO();
                            stockRequestOrderTransError.setRetry(1L);
                            stockRequestOrderTransError.setStockRequestOrderId(stockRequestOrderDetailDTO.getStockRequestOrderId());
                            stockRequestOrderTransError.setFromOwnerId(stockRequestOrderDetailDTO.getFromOwnerId());
                            stockRequestOrderTransError.setToOwnerId(stockRequestOrderDetailDTO.getToOwnerId());
                            stockRequestOrderTransError.setStockTransType(1L);
                            stockRequestOrderTransError.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                            stockRequestOrderTransError.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
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
                } else {
                    try {
                        StockRequestOrderDTO stockRequestOrderDTO = stockRequestOrderService.findOne(stockRequestOrderDetailDTO.getStockRequestOrderId());
                        stockRequestOrderDTO.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_2_REJECT);
                        stockRequestOrderDTO.setUpdateUser("SYS");
                        stockRequestOrderDTO.setUpdateDatetime(new Date());
                        stockRequestOrderService.save(stockRequestOrderDTO);
                        stockRequestOrderDetailService.updateCancelNote(stockRequestOrderDTO.getStockRequestOrderId());
                    } catch (Exception ex) {
                        reportError(ex);
                    }
                }
                reportError("------Ket thuc tien trinh lap phieu hang thu hoi");
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }


}

package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockTransLogisticService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HungPM6 on 1/19/2016.
 */
@Service
public class BaseStockCancelTransAgentService extends BaseStockService {
    public static final Logger logger = Logger.getLogger(BaseStockImpNoteService.class);
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransLogisticService stockTransLogisticService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        if (!DataUtil.isNullObject(stockTransDTO.getRegionStockTransId())) {
            StockTransDTO regionStockTransDTO = stockTransService.findOne(stockTransDTO.getRegionStockTransId());
            if (!DataUtil.isNullObject(regionStockTransDTO)) {
                stockTransDTO.setRegionStockId(regionStockTransDTO.getFromOwnerId());
                flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.CANCEL);
            }
        }
        //set lai actionCode
        stockTransActionDTO.setActionCode(null);
        //Cong so luong kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        //set status cho kho 3 mien
        flagStockDTO.setStatusForAgent(Const.STOCK_TRANS_STATUS.EXPORTED);
        // Luu stock_total_audit
        flagStockDTO.setInsertStockTotalAudit(true);

        //Cap nhat lai trang thai serial kho xuat ve trang thai new
//        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
//        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        StockTransDTO transDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(transDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.trans.not.found");
        }


        //Giao dich phai o trang thai da lap lenh/phieu
        if (!(DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, transDTO.getStatus())
                || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, transDTO.getStatus())
                || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_ORDER, transDTO.getStatus())
                || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_NOTE, transDTO.getStatus()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.cancel.trans.status.invalid");
        }

        // Check trang thai dat coc
//        if (!DataUtil.isNullOrEmpty(transDTO.getDepositStatus()) && DataUtil.safeEqual(Const.DEPOSIT_STATUS.DEPOSIT_HAVE, transDTO.getDepositStatus())) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.cancel.error.deposit");
//        }

        // Check trang thai thanh toan, lap hoa don cua giao dich
        if (!DataUtil.isNullOrEmpty(transDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, transDTO.getPayStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.cancel.error.pay");
        }

        //Khong duoc huy giao dich kho 3 mien
        if (!DataUtil.isNullObject(stockTransActionDTO.getRegionOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.three.region.effectInvalid");
        }

        //Khong duoc phep huy giao dich dieu chuyen hang chi nhanh : isAutoGen = 3
        if (DataUtil.safeEqual(Const.STOCK_TRANS.IS_TRANSFER, stockTransDTO.getIsAutoGen())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.cancel.trans.transfer.invalid");
        }
        //Khong duoc phep huy cac giao dich logistic
        StockTransLogisticDTO stockTransLogisticDTO = stockTransLogisticService.findByStockTransId(transDTO.getStockTransId());
        if (!DataUtil.isNullObject(stockTransLogisticDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.cancel.trans.logistic.invalid");
        }
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        //do nothing
    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) {
            return;
        }

        StockTransDTO exportDTO = new StockTransDTO();
        exportDTO.setStockTransId(stockTransDTO.getRegionStockTransId());
        exportDTO.setStatus(flagStockDTO.getRegionImportStatus());
        exportDTO = doSaveStockTrans(exportDTO);
        exportDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());//Set lai cho truong hop update

        StockTransActionDTO exportActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        exportActionDTO.setActionCode(null);
        exportActionDTO.setRegionOwnerId(stockTransDTO.getRegionStockId());
        exportActionDTO.setNote(flagStockDTO.getNote());
        exportActionDTO.setIsRegionExchange(true);
        doSaveStockTransAction(exportDTO, exportActionDTO);

        return;
    }

    /**
     * Goi ws huy giao dich logistic
     *
     * @param stockTransDTO
     * @param lstStockTransDetail
     * @throws Exception
     */
    @Override
    public void doSyncLogistic(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        if (Const.STOCK_TRANS.IS_LOGISTIC.equals(stockTransDTO.getLogicstic())) {
            if (DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_ORDER, oldStockTransStatus)
                    || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, oldStockTransStatus)) {
                //Goi ham ws huy giao dich logistic khi lap lenh
                return;
            }
            if (DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORT_NOTE, oldStockTransStatus)
                    || DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, oldStockTransStatus)) {
                //Goi ham ws huy giao dich logistic khi lap phieu
                return;
            }
        }
    }
}

package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thetm1 on 9/12/2015.
 * Description Xuat kho (da lap phieu xuat)
 * Detail + Update stock_trans
 * + Khong luu stock_trans_detail (-)
 * + Insert stock_trans_action
 * + Luu stock_trans_serial
 * + Cap nhat stock_total
 * + Cap nhat chi tiet serial (doSaveStockGoods)
 */

@Service
public class BaseStockExpNoteService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockExpNoteService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);

        //Tru so luong thuc te kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

        //cap nhat trang thai stock_x ve cho nhap (status=3)
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_CONFIRM);

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        List<StockTransActionDTO> lstTransAction = stockTransActionService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransAction.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                        stockTransDTO.getStockTransId())));
        if(!DataUtil.isNullOrEmpty(lstTransAction)){
            stockTransDTO.setRegionStockId(lstTransAction.get(0).getRegionOwnerId());
        }

        //set null
        stockTransActionDTO.setActionCode(null);
        stockTransActionDTO.setNote(null);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        baseValidateService.doOrderValidate(stockTransDTO);
        //Validate trang thai giao dich phai la da lap phieu
        StockTransDTO stockTrans = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(stockTrans)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stocktrans.empty");
        }

        //Validate trang thai phai la da lap lenh
        if (!DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, stockTrans.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.status.invalid");
        }

        //Kiem tra stockTransAction
        StockTransActionDTO oldStockTransActionDTO = stockTransActionService.findOne(stockTransActionDTO.getStockTransActionId());
        if (DataUtil.isNullObject(oldStockTransActionDTO) || !DataUtil.safeEqual(oldStockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.stock.code.export.not.found");
        }

        //Check trang thai ki voffice cua giao dich cu
        if (DataUtil.safeEqual(Const.SIGN_VOFFICE, oldStockTransActionDTO.getSignCaStatus())) {
            stockTransVofficeService.doSignedVofficeValidate(oldStockTransActionDTO);
        }

        baseValidateService.doDebitValidate(stockTransDTO, new ArrayList());
        // Validate kho xuat con so luong xuat khong.
        baseValidateService.doCurrentQuantityValidate(stockTransDTO, lstStockTransDetail);

        //Neu la lap lenh xuat cho nhan vien : Validate ngay nop tien cua nhan vien
        if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            baseValidateService.findTransExpiredPay(stockTransDTO.getToOwnerId());
        }
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }
}

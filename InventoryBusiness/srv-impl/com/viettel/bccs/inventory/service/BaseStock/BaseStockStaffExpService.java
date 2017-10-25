package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ReasonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HaiNT41 on 1/11/2016.
 * Service nhan vien xuat tra hang
 */
@Service
public class BaseStockStaffExpService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockStaffExpService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReasonService reasonService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);

        //Cap nhat trang thai giao dich la da nhap kho
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);

        //Tru so luong kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

        //cap nhat trang thai stock_x tu hang moi ve cho nhap kho
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_CONFIRM);

        // Luu thong tin stock_total_audit
        flagStockDTO.setInsertStockTotalAudit(true);

        //Increase stock num
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac thong tin giao dich phai ton tai
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullObject(stockTransActionDTO)
                || DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //1. Validate kho xuat
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> exportStaffList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(exportStaffList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }

        //2. Validate kho nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> importShopList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(importShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }

        //3. Kho nhan la shop cua kho xuat
        if (!DataUtil.safeEqual(exportStaffList.get(0).getParentShopId(), importShopList.get(0).getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stock.notConsistent");
        }

        //4. kiem tra ly do co ton tai khong
        if (DataUtil.isNullOrZero(stockTransDTO.getReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.note.reason.export.require.msg");
        }

        ReasonDTO reason = reasonService.findOne(stockTransDTO.getReasonId());
        if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId()) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, reason.getReasonStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
        }

        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().getBytes("UTF-8").length > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }

        //validate thong tin serial
        baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
        //validate so luong thuc te
        baseValidateService.doCurrentQuantityValidate(stockTransDTO, lstStockTransDetail);
        //validate so luong dap ung
        baseValidateService.doQuantityAvailableValidate(stockTransDTO, lstStockTransDetail);

        //Validate ngay nop tien cua nhan vien
        baseValidateService.findTransExpiredPay(stockTransDTO.getFromOwnerId());

    }

    /**
     * @param stockTransDTO
     * @param stockTransActionDTO
     * @return Insert 2 ban ghi trang thai status = 2, 3 vao bang stockTransAction
     * @throws Exception
     */
    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {

        //insert ban ghi 1
        StockTransActionDTO exportTransActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        exportTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        exportTransActionDTO.setStockTransActionId(null);//luon insert
        exportTransActionDTO.setActionCode(null);
        exportTransActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        exportTransActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        exportTransActionDTO.setNote(null);
        if (stockTransDTO.isSignVoffice()) {
            exportTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        stockTransActionService.save(exportTransActionDTO);

        //insert ban ghi 2
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        transActionDTO.setStockTransActionId(null);//luon insert
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        transActionDTO.setNote(stockTransDTO.getNote());
        if (stockTransDTO.isSignVoffice()) {
            transActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        return stockTransActionService.save(transActionDTO);
    }

}


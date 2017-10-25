package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HaiNT41 on 1/13/2016.
 * Ham tu choi giao dich
 */

@Service
public class BaseStockRejectTransService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockOutOrderService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransIm1Service stockTransServiceIm1;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        //Cap nhat trang thai giao dich la tu choi
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.REJECT);

        //Cong lai so luong dap ung va thuc te cua kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

        //cap nhat trang thai stock_x ve cho nhap (status=1)
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac thong tin giao dich phai ton tai
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }
        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Trang thai truoc khi tu choi phai = 3
        if (!DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORTED, oldStockTransDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.transStatus.exported");
        }

        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.stockTransDetail.empty");
        }

        //Kiem tra stockTransAction
        if (DataUtil.isNullObject(stockTransActionService.findOne(stockTransActionDTO.getStockTransActionId()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Nhap ly do tu choi
        if (DataUtil.isNullOrZero(stockTransDTO.getRejectReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.note.reason.export.require.msg");
        }

        ReasonDTO reason = reasonService.findOne(stockTransDTO.getRejectReasonId());
        if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId()) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, reason.getReasonStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
        }

        //ghi chu
        if (stockTransActionDTO.getNote() != null && stockTransActionDTO.getNote().length() > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.note.note.over.maxlength");
        }

        List<StockTransDetailDTO> oldLstStockTransDetailDTO = stockTransDetailService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                        stockTransDTO.getStockTransId())));
        //Kiem tra stockTransDetail
        if (DataUtil.isNullOrEmpty(oldLstStockTransDetailDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }
        if (oldLstStockTransDetailDTO.size() != lstStockTransDetail.size()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Check han muc
        //validate han muc tu kho cap tren tra lai kho cap duoi
        StockTransDTO rejectTransDTO = DataUtil.cloneBean(stockTransDTO);
        rejectTransDTO.setFromOwnerId(stockTransDTO.getToOwnerId());
        rejectTransDTO.setFromOwnerType(stockTransDTO.getToOwnerType());
        rejectTransDTO.setToOwnerId(stockTransDTO.getFromOwnerId());
        rejectTransDTO.setToOwnerType(stockTransDTO.getFromOwnerType());
        rejectTransDTO.setStockTransId(null);
        baseValidateService.doDebitValidate(rejectTransDTO, lstStockTransDetail);

    }

    @Override
    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        if (transDTOImport.getStockTransId() != null) {
            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
            }
            //validate tu choi tren bccs1
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    StockTransIm1DTO stockTransToUpdateIM1;
                    if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.REJECT)) {
                        //Lay thong tin IM1
                        stockTransToUpdateIM1 = stockTransServiceIm1.findOneLock(stockTransDTO.getStockTransId());
                        if (!DataUtil.isNullObject(stockTransToUpdateIM1)) {
                            //check da xuat kho tren im1 chua
                            if (!DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORTED))) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.cancel.import.im1");
                            }
                            stockTransToUpdateIM1.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.CANCEL_IM1));
                            stockTransServiceIm1.updateStatusStockTrans(stockTransToUpdateIM1);
                        }
                    }
                }
            }
            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
            stockTransToUpdate.setRejectNote(stockTransDTO.getRejectNote());
            stockTransToUpdate.setRejectReasonId(stockTransDTO.getRejectReasonId());
            stockTransService.save(stockTransToUpdate);
            transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
        }
        return transDTOImport;

    }

    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return stockTransActionDTO;
        }
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setStockTransActionId(null);//luon insert
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        transActionDTO.setNote(stockTransDTO.getRejectNote());
        if (stockTransDTO.isSignVoffice()) {
            transActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        createUser = transActionDTO.getCreateUser();
        createDate = transActionDTO.getCreateDatetime();
        return stockTransActionService.save(transActionDTO);

    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }
}

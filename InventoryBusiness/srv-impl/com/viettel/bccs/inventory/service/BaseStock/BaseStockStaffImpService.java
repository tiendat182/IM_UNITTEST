package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.provider.WsCallerFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HaiNT41 on 1/11/2016.
 * Service nhan vien xac nhan nhap hang
 */
@Service
public class BaseStockStaffImpService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockStaffImpService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIM;

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private WsCallerFactory wsCallerFactory;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransIm1Service stockTransServiceIm1;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);

        //Cap nhat trang thai giao dich la da nhap kho
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);

        //Phieu nhap kho
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);

        //Cong so luong kho nhap
        flagStockDTO.setImportStock(true);
        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

        //cap nhat trang thai stock_x tu cho nhap ve da nhap kho
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);

        //Cap nhat serial ve kho nhan
        flagStockDTO.setUpdateOwnerId(true);

        // Luu thong tin stock_total_audit
        flagStockDTO.setInsertStockTotalAudit(true);

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        //Validate cac thong tin giao dich phai ton tai
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.stockTransDetail.empty");
        }

        //Kiem tra giao dich stockTransDTO co ton tai trong DB khong
        StockTransDTO oldStockTransDTO = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Kiem tra stockTransAction
        if (DataUtil.isNullObject(stockTransActionService.findOne(stockTransDTO.getStockTransActionId()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Kiem tra stockTransDetail
        List<StockTransDetailDTO> oldLstStockTransDetail = stockTransDetailService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                        stockTransDTO.getStockTransId())));
        if (DataUtil.isNullOrEmpty(oldLstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        //Validate trang thai giao dich phai o trang thai da xuat kho
        if (!DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.EXPORTED, oldStockTransDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.transStatus.exported");
        }

        //Validate cac truong trong giao dich
        //1. Validate kho xuat
        if (DataUtil.isNullOrZero(stockTransDTO.getFromOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getFromOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(exportShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }

        //2. Validate kho nhan vien nhan
        if (DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) || DataUtil.isNullOrZero(stockTransDTO.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.stock.require.msg");
        }

        List<VShopStaffDTO> importStaffList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(importStaffList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }

        //3. Kho xuat la cap tren lien ke kho nhan
        if (!DataUtil.safeEqual(exportShopList.get(0).getOwnerId(), importStaffList.get(0).getParentShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stock.notConsistent");
        }

        //4. kiem tra ly do nhap co ton tai khong
        if (DataUtil.isNullOrZero(stockTransDTO.getImportReasonId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.note.reason.export.require.msg");
        }

        ReasonDTO reason = reasonService.findOne(stockTransDTO.getImportReasonId());
        if (DataUtil.isNullObject(reason) || DataUtil.isNullOrZero(reason.getReasonId()) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, reason.getReasonStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.reason.notExists");
        }

        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getImportNote()) && stockTransDTO.getImportNote().getBytes("UTF-8").length > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, new ArrayList());

        //Validate ngay nop tien cua nhan vien
        baseValidateService.findTransExpiredPay(stockTransDTO.getToOwnerId());

    }

    @Override
    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        //Truong hop update
        StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

        if (DataUtil.isNullObject(stockTransToUpdate)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists");
        }

        //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
        if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
        }
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {

                    StockTransIm1DTO stockTransToUpdateIM1 = stockTransServiceIm1.findOneLock(stockTransDTO.getStockTransId());
                    if (!DataUtil.isNullObject(stockTransToUpdateIM1)) {
                        //Da huy hoac tu choi tren IM1
                        if (DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.DESTROY_IM1))
                                || DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.CANCEL_IM1))) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.destroy.import.im1");
                        }
                        if (DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.PROCESSING))) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.offline.import.im1");
                        }
                        StockTransIm1DTO stockTransImport = stockTransServiceIm1.findFromStockTransIdLock(stockTransToUpdateIM1.getStockTransId());
                        if (DataUtil.isNullObject(stockTransImport)) {
                            //Cap nhat stock_trans_status = 4
                            stockTransToUpdateIM1.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.IMPORT_ORDER));
                            stockTransServiceIm1.save(stockTransToUpdateIM1);
                            //Neu chua xac nhan nhap hang, them moi ban ghi tren IM1
                            StockTransIm1DTO stockTransIm1Save = DataUtil.cloneBean(stockTransToUpdateIM1);
                            stockTransIm1Save.setStockTransId(null);
                            stockTransIm1Save.setCreateDatetime(stockTransDTO.getCreateDatetime());
                            stockTransIm1Save.setRealTransDate(stockTransDTO.getCreateDatetime());
                            stockTransIm1Save.setRealTransUserId(null);
                            stockTransIm1Save.setValid(3000L);
                            stockTransIm1Save.setReasonId(stockTransDTO.getImportReasonId());
                            stockTransIm1Save.setStockTransType(DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.IMPORT));
                            stockTransIm1Save.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORTED));
                            stockTransIm1Save.setFromStockTransId(stockTransToUpdateIM1.getStockTransId());
                            stockTransServiceIm1.save(stockTransIm1Save);
                        } else {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.duplicate.import.im1");
                        }
                    }
                }
            }
        }
        stockTransToUpdate.setStatus(stockTransDTO.getStatus());
        //Neu process offline: status = 0
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            stockTransToUpdate.setStatus(Const.STOCK_TRANS_STATUS.PROCESSING);
        }
        if (!DataUtil.isNullObject(stockTransDTO.getDepositStatus())) {
            stockTransToUpdate.setDepositStatus(stockTransDTO.getDepositStatus());
        }
        if (!DataUtil.isNullObject(stockTransDTO.getPayStatus())) {
            stockTransToUpdate.setPayStatus(stockTransDTO.getPayStatus());
        }
        if (!DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
            stockTransToUpdate.setBankplusStatus(stockTransDTO.getBankplusStatus());
        }
        if (!DataUtil.isNullObject(stockTransDTO.getCreateUserIpAdress())) {
            stockTransToUpdate.setCreateUserIpAdress(stockTransDTO.getCreateUserIpAdress());
        }
        if (!DataUtil.isNullObject(stockTransDTO.getDepositPrice())) {
            stockTransToUpdate.setDepositPrice(stockTransDTO.getDepositPrice());
        }
        if (!DataUtil.isNullOrZero(stockTransDTO.getImportReasonId())) {
            stockTransToUpdate.setImportReasonId(stockTransDTO.getImportReasonId());
        }
        stockTransToUpdate.setImportNote(stockTransDTO.getImportNote());
        stockTransToUpdate.setRejectReasonId(stockTransDTO.getRejectReasonId());
        stockTransToUpdate.setRejectNote(stockTransDTO.getRejectNote());
        stockTransService.save(stockTransToUpdate);
        transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
        transDTOImport.setStockTransDate(stockTransToUpdate.getCreateDatetime());
        return transDTOImport;
    }


    /**
     * @param stockTransDTO
     * @param stockTransActionDTO
     * @return Insert 2 ban ghi trang thai status = 5, 6 vao bang stockTransAction
     * @throws Exception
     */
    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        //insert ban ghi 1
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        transActionDTO.setStockTransActionId(null);//luon insert
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
//        transActionDTO.setNote(stockTransDTO.getNote());
        if (stockTransDTO.isSignVoffice()) {
            transActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        StockTransActionDTO stockTransActionNote = stockTransActionService.save(transActionDTO);

        //insert ban ghi 2
        StockTransActionDTO newTransActionDTO;
        newTransActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        newTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        newTransActionDTO.setStockTransActionId(null);//luon insert
        newTransActionDTO.setActionCode(null);
        newTransActionDTO.setNote(null);
        newTransActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        newTransActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        if (stockTransDTO.isSignVoffice()) {
            newTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        stockTransActionService.save(newTransActionDTO);

        return stockTransActionNote;
    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
    }

    @Override
    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
        //Xoa ban ghi trong bang lock_user_info theo action_id
        stockTransRepo.unlockUserInfo(stockTransDTO.getStockTransId());
    }

}

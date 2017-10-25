package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by hoangnt14 on 1/19/2016.* Description Nhap kho (da lap phieu nhap)
 * <p>
 * + doSaveStockTrans
 * + doSaveStockTransAction
 * + doSaveStockGoods
 * + doSaveStockTransSerial
 * + doSaveStockTotal
 */

@Service
public class BaseStockAgentRetrieveImportService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockImpNoteService.class);
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransIm1Service stockTransServiceIm1;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws Exception {
//        stockTransDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_IMPORT);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockAgent(Const.STOCK_TRANS_STATUS.IMPORTED);

        //Cong so luong kho nhan
        flagStockDTO.setImportStock(true);
        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        //Tru so luong kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

        //cap nhat trang thai stock_x ve da nhap (status=1 --> status=1)
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);

        //Cap nhat serial ve kho nhan
        flagStockDTO.setUpdateOwnerId(true);
        flagStockDTO.setStatusForAgent(Const.STOCK_TRANS_STATUS.IMPORTED);
        // bankplus
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setRequestType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TRANS_TYPE_MINUS_EXPORT);
        stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE);
        // Set lai state hang theo ly do
        if (!DataUtil.isNullObject(stockTransDTO) && !DataUtil.isNullObject(stockTransDTO.getReasonId())) {
            ReasonDTO reasonDTO = reasonService.findOne(stockTransDTO.getReasonId());
            if (!DataUtil.isNullObject(reasonDTO) && !DataUtil.isNullObject(reasonDTO.getHaveMoney())) {
                flagStockDTO.setStateIdForReasonId(reasonDTO.getHaveMoney());
            }
        }
    }

    //hoangnt : check thuc hien thu hoi dai ly tren 2 he thong IM1 va IM2
    @Override
    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        //Truong hop update
        if (transDTOImport.getStockTransId() != null) {
            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

            if (DataUtil.isNullObject(stockTransToUpdate)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists");
            }

            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
            }
            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                        StockTransIm1DTO stockTransToUpdateIM1 = stockTransServiceIm1.findOneLock(stockTransDTO.getStockTransId());
                        if (!DataUtil.isNullObject(stockTransToUpdateIM1)) {
                            //check da xuat kho tren im1 chua
                            if (!DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))
                                    && !DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_NOTE))) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.duplicate.export.im1");
                            }
                            stockTransToUpdateIM1.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORTED));
                            stockTransServiceIm1.updateStatusStockTrans(stockTransToUpdateIM1);
                        }
                    }
                }
            }
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
            stockTransToUpdate.setImportReasonId(stockTransDTO.getImportReasonId());
            stockTransToUpdate.setImportNote(stockTransDTO.getImportNote());
            stockTransToUpdate.setRejectReasonId(stockTransDTO.getRejectReasonId());
            stockTransToUpdate.setRejectNote(stockTransDTO.getRejectNote());
            stockTransService.save(stockTransToUpdate);
            transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
            transDTOImport.setStockTransDate(stockTransToUpdate.getCreateDatetime());
            return transDTOImport;
        }
        //Truong hop insert
        if (DataUtil.isNullObject(stockTransDTO.getStockTransType())) {
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
        }
        StockTransDTO stockTrans = stockTransService.save(stockTransDTO);
        transDTOImport.setStockTransId(stockTrans.getStockTransId());
        return transDTOImport;
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.AGENT);
        if (DataUtil.isNullOrEmpty(exportShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }
        List<VShopStaffDTO> importShopList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(importShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }
        //validate hang hoa ko vuot qua tong han muc kho xuat--thaont19
        baseValidateService.doCurrentQuantityValidate(stockTransDTO, lstStockTransDetail);
        //tru Bankplus
        Long deposit;
        Long depositTotal = 0L;
        if (!DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
                ProductOfferingDTO productOffering = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
                String tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
                List<StockTransSerialDTO> lstStockTransSerial = stockTransDetailDTO.getLstStockTransSerial();
                if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                    for (StockTransSerialDTO stockTransSerialDTO : lstStockTransSerial) {
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                            deposit = stockTransSerialService.getDepostiPriceForRetrieve(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType(),
                                    stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId(), stockTransSerialDTO.getFromSerial());
                            depositTotal += deposit;
                        } else {
                            deposit = stockTransSerialService.getDepostiPriceByRangeSerial(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType(),
                                    stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId(), stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial());
                            depositTotal += deposit;
                        }
                    }
                }
            }
        }
        stockTransDTO.setDepositTotal(-depositTotal);

        if (depositTotal != null && depositTotal.compareTo(0L) > 0) {
            boolean isAccountBankplus = baseValidateService.doAccountBankplusRetrieveValidate(stockTransDTO);
            if (!isAccountBankplus) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.retrieve.bank.plus.not.balance");
            }
        }
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
    }

    @Override
    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

    }

    @Override
    public void doSaveStockTransDetail(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        //do nothing
    }

    @Override
    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setActionCode(null);
        transActionDTO.setNote(null);
        return super.doSaveStockTransAction(stockTransDTO, transActionDTO);
    }
}
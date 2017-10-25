package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
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
 * Created by hoangnt14 on 1/22/2016.
 */
@Service
public class BaseStockCollaboratorRetrieveService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockCollaboratorExpService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransSerialService stockTransSerialService;


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_STAFF);

        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_ORDER);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);

        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        // total audit
        flagStockDTO.setInsertStockTotalAudit(true);
        flagStockDTO.setStatusForAgent(Const.STOCK_TRANS_STATUS.IMPORTED);
        //Cap nhat serial ve kho nhan
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setUpdateOwnerId(true);
        // bankplus
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setRequestType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TRANS_TYPE_MINUS_EXPORT);
        stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE);

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        // validate cho NVDB/DB
        validateColl(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
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
            stockTransDTO.setDepositTotal(-depositTotal);
            if (depositTotal != null && depositTotal.compareTo(0L) > 0) {
                boolean isAccountBankplus = baseValidateService.doAccountBankplusRetrieveValidate(stockTransDTO);
                if (!isAccountBankplus) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.retrieve.bank.plus.not.balance");
                }
            }
            //Validate han muc kho nhan
            baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
            // Check serial;
            baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);

        }
    }

    private void validateColl(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType().toString(), Const.VT_UNIT.AGENT);
        if (DataUtil.isNullOrEmpty(exportShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.fromStock.notExists");
        }
        List<VShopStaffDTO> importShopList = shopService.getListShopStaff(stockTransDTO.getToOwnerId(), stockTransDTO.getToOwnerType().toString(), Const.VT_UNIT.VT);
        if (DataUtil.isNullOrEmpty(importShopList)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }

        // Validate length theo utf-8
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode()) && stockTransActionDTO.getActionCode().getBytes("UTF-8").length > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.actionCode.overLength");
        }
//        if (DataUtil.isNullOrZero(stockTransDTO.getChannelTypeId())) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.valid.ownerType");
//        }

        StaffDTO staffDTO = staffService.findOne(stockTransDTO.getFromOwnerId());
        if (!shopService.checkChannelIsCollAndPointOfSale(staffDTO.getChannelTypeId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.retrieve.order.notCollaborator");
        }
        // Kiem tra cac danh sach mat hang xuat phai cung dich vu VAT

        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.retrieve.order.product");
        }
        List<String> lstProductOfferingCode = Lists.newArrayList();
        Long totalAmount = 0L;
        Long totalDepositPrice = 0L;
        List<ProductOfferingDTO> listProduct = Lists.newArrayList();
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            if (DataUtil.isNullObject(stockTransDetailDTO.getProdOfferId())) {
                continue;
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
            if (lstProductOfferingCode.contains(productOfferingDTO.getCode())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.validate.list.duplicate");
            }
            if (DataUtil.isNullObject(productOfferingDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
            }
            List<ProductOfferPriceDTO> lstPrice = productOfferPriceService.getPriceDepositAmount(productOfferingDTO.getProductOfferingId(),
                    null, Const.PRODUCT_OFFERING.PRICE_POLICY_VT, null, null, null);
            if (DataUtil.isNullOrEmpty(lstPrice) || DataUtil.isNullObject(lstPrice.get(0))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.price.not.define");
            }
            if (DataUtil.safeEqual(stockTransDetailDTO.getStateId(), Const.STATE_STATUS.NEW)) {
                Long rootPrice = productOfferPriceService.getPriceAmount(productOfferingDTO.getProductOfferingId(),
                        Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, DataUtil.safeToLong(staffDTO.getPricePolicy()));
                stockTransDetailDTO.setPrice(rootPrice);
                stockTransDetailDTO.setAmount(DataUtil.safeToLong(stockTransDetailDTO.getQuantity()) * DataUtil.safeToLong(rootPrice));
                totalAmount += DataUtil.safeToLong(stockTransDetailDTO.getQuantity()) * DataUtil.safeToLong(rootPrice);
            }
            ProductOfferPriceDTO productOfferPriceDTO = lstPrice.get(0);
            // start comment theo y/c thetm1. Luu gia goc
            stockTransDetailDTO.setDepositPrice(productOfferPriceDTO.getPrice());
            totalDepositPrice += DataUtil.safeToLong(stockTransDetailDTO.getQuantity()) * DataUtil.safeToLong(productOfferPriceDTO.getPrice());
//            stockTransDetailDTO.setPrice(productOfferPriceDTO.getPrice());
//            stockTransDetailDTO.setAmount(DataUtil.safeToLong(stockTransDetailDTO.getQuantity()) * DataUtil.safeToLong(productOfferPriceDTO.getPrice()));
//            stockTransDetailDTO.setProductOfferPriceId(productOfferPriceDTO.getProductOfferPriceId());
            // end
            listProduct.add(productOfferingDTO);
        }
        if (DataUtil.isNullOrEmpty(listProduct)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.lstStockTransDetail.empty");
        }
        stockTransDTO.setTotalAmount(totalAmount);
        stockTransDTO.setDepositPrice(totalDepositPrice);
    }

    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
    }

    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO) throws Exception {
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        StockTransActionDTO result = super.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
        // Save tiep 1 ban ghi khac
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setStockTransActionId(null);//luon insert
        transActionDTO.setActionCode(null);
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        transActionDTO.setNote(null);
        if (stockTransDTO.isSignVoffice()) {
            transActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        stockTransActionService.save(transActionDTO);
        return result;
    }
}

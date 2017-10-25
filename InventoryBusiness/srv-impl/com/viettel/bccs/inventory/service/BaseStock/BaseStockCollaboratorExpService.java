package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dungpt16 on 13/01/2016.
 */

@Service
public class BaseStockCollaboratorExpService extends BaseStockService {

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


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_STAFF);
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);

        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
        // Cap nhat trang thai stock_x
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setUpdateOwnerId(true);
        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        // Luu thong tin dat coc
//        flagStockDTO.setInsertReceiptExpense(true);
//        flagStockDTO.setReceiptExpenseType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
//        flagStockDTO.setReceiptExpenseTypeId(Const.RECEIPT_EXPENSE.RECEIPT_TYPE_EXPORT_CTV);
//        flagStockDTO.setReceiptExpensePayMethod(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
//        flagStockDTO.setReceiptExpenseStatus(Const.STOCK_TRANS.STOCK_TRANS_STATUS_DEPOSIT);
//        flagStockDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
//        flagStockDTO.setDepositType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        // Luu thong tin stock_total_audit
        flagStockDTO.setInsertStockTotalAudit(true);
        // set trang thai lay serial list tu giao dien
        flagStockDTO.setSerialListFromTransDetail(true);
        // bankplus
        flagStockDTO.setUpdateAccountBalance(true);
        flagStockDTO.setRequestType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TRANS_TYPE_MINUS_EXPORT);
        stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE);
        //set deposit_price
        flagStockDTO.setUpdateDepositPrice(true);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        // validate cho NVDB/DB
        validateColl(stockTransDTO, stockTransActionDTO, lstStockTransDetail);

        //Validate han muc kho nhan
        boolean isAccountBankplus = baseValidateService.doAccountBankplusValidate(stockTransDTO);
        if (!isAccountBankplus) {
            baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
        }
        // Check serial;
        baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);
    }

    private void validateColl(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        // Validate kho nhan hang co phai la dai ly khong
        baseValidateService.doOrderValidateCommon(stockTransDTO, Const.VT_UNIT.AGENT);

        // Validate length theo utf-8
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode()) && stockTransActionDTO.getActionCode().getBytes("UTF-8").length > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.actionCode.overLength");
        }
//        if (DataUtil.isNullOrZero(stockTransDTO.getChannelTypeId())) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.valid.ownerType");
//        }

        StaffDTO staffDTO = staffService.findOne(stockTransDTO.getToOwnerId());
        if (!shopService.checkChannelIsCollAndPointOfSale(staffDTO.getChannelTypeId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.export.order.notCollaborator");
        }
        Long channelTypeId = staffDTO.getChannelTypeId();
        //Neu la diem ban
        if (DataUtil.safeEqual(staffDTO.getPointOfSale(), Const.CHANNEL_TYPE.POINT_OF_SALE_DB)) {
            channelTypeId = Const.CHANNEL_TYPE.CHANNEL_TYPE_POINT_OF_SALE;
        }

        //Lay thong tin chi nhanh cua user xuat hang
        Long branchId = shopService.findBranchId(staffDTO.getShopId());

        List<String> lstProductOfferingCode = Lists.newArrayList();
        Long totalAmount = 0L;
        Long totalDepositPrice = 0L;
        List<ProductOfferingDTO> listProduct = Lists.newArrayList();
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            if (DataUtil.isNullObject(stockTransDetailDTO.getProdOfferId())) {
                continue;
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTO) || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS.ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.product.offering.not.exist", stockTransDetailDTO.getProdOfferName());
            }
            if (lstProductOfferingCode.contains(productOfferingDTO.getCode() + "_" + stockTransDetailDTO.getStateId())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.validate.list.duplicate");
            }
            lstProductOfferingCode.add(productOfferingDTO.getCode() + "_" + stockTransDetailDTO.getStateId());
            if (DataUtil.isNullObject(productOfferingDTO)) { //TODO code thừa
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
            }
            if (DataUtil.safeEqual(stockTransDetailDTO.getStateId(), Const.STATE_STATUS.NEW)) {
                Long rootPrice = productOfferPriceService.getPriceAmount(productOfferingDTO.getProductOfferingId(),
                        Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, DataUtil.safeToLong(staffDTO.getPricePolicy()));
                stockTransDetailDTO.setPrice(rootPrice);
                stockTransDetailDTO.setAmount(DataUtil.safeToLong(stockTransDetailDTO.getQuantity()) * DataUtil.safeToLong(rootPrice));
                totalAmount += DataUtil.safeToLong(stockTransDetailDTO.getQuantity()) * DataUtil.safeToLong(rootPrice);
            }
            List<ProductOfferPriceDTO> lstPrice = productOfferPriceService.getPriceDepositAmount(productOfferingDTO.getProductOfferingId(),
                    branchId, Const.PRODUCT_OFFERING.PRICE_POLICY_VT, channelTypeId, null, null);
            if (DataUtil.isNullOrEmpty(lstPrice) || DataUtil.isNullObject(lstPrice.get(0))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.coll.price.not.define");
            }
            if (lstPrice.size() > 1) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "sp.exportStockCollaborator.stockTransDetail.deposit.price.duplicate");
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

    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        // Lap phieu xuat
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        StockTransActionDTO result = super.doSaveStockTransAction(stockTransDTO, stockTransActionDTO);
        // Xuat kho
        StockTransDTO stockTransDTOData = DataUtil.cloneBean(stockTransDTO);
        stockTransDTOData.setNote(null);
        StockTransActionDTO transActionDTOExported = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTOExported.setActionCode(null);
        transActionDTOExported.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        super.doSaveStockTransAction(stockTransDTOData, transActionDTOExported);
        // nhap kho
        StockTransActionDTO transActionDTOImported = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTOImported.setActionCode(null);
        transActionDTOImported.setNote(null);
        transActionDTOImported.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        super.doSaveStockTransAction(stockTransDTOData, transActionDTOImported);
        return result;
    }
}

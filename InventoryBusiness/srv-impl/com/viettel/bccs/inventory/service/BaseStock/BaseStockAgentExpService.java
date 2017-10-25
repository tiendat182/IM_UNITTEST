package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dungpt16 on 13/01/2016.
 */

@Service
public class BaseStockAgentExpService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockAgentExpService.class);

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ProductOfferingService productOfferingService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setStockBase(stockTransDTO.getStockBase() != null ? stockTransDTO.getStockBase().trim() : null);
        String noteRegion = getText("export.order.three.region.note");
        //Cap nhat trang thai giao dich la lap lenh xuat
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        if (DataUtil.safeEqual(stockTransDTO.getStockAgent(), Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)) {
            stockTransDTO.setDepositStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
        }
        if (DataUtil.safeEqual(stockTransDTO.getStockAgent(), Const.STOCK_TRANS.STOCK_TRANS_PAY)) {
            stockTransDTO.setPayStatus(Const.STOCK_STRANS_PAY.PAY_ORDER);
        }
        if (!DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) {
            String strNote = noteRegion;
            if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote())) {
                strNote = stockTransDTO.getNote() + ". " + noteRegion;
            }
            stockTransDTO.setNote(strNote);
        }
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        if (DataUtil.safeEqual(stockTransDTO.getLogicstic(), Const.STOCK_TRANS.IS_LOGISTIC)) {
            stockTransDTO.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
        }

        //Kho 3 mien
        flagStockDTO.setRegionExportStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        flagStockDTO.setRegionImportStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        flagStockDTO.setNote(noteRegion);

        //Prefix ma phieu xuat, nhap kho 3 mien
        flagStockDTO.setPrefixExportActionCode(Const.PREFIX_REGION.LX);
        flagStockDTO.setPrefixImportActionCode(Const.PREFIX_REGION.LN);
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_LX);
        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        //Validate cac truong trong giao dich
        baseValidateService.doOrderValidateAgent(stockTransDTO, stockTransActionDTO);
        //Validate ky voffice
        baseValidateService.doSignVofficeValidate(stockTransDTO);

        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);

        //validate hang hoa ko vuot qua tong han muc kho xuat--thaont19
        baseValidateService.doQuantityAvailableValidate(stockTransDTO, lstStockTransDetail);

        // validate cho dai ly
        validateAgentExp(stockTransDTO, lstStockTransDetail);
    }

    private void validateAgentExp(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        // Validate kho nhan hang co phai la dai ly khong
        ShopDTO shop = shopService.findOne(stockTransDTO.getToOwnerId());
        if (!shopService.checkChannelIsAgent(shop.getChannelTypeId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.export.order.notAgent");
        }
        // Kiem tra cac danh sach mat hang xuat phai cung dich vu VAT
//        Long vat = null;
//        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
//            List<ProductOfferPriceDTO> lstPrice = productOfferPriceService.getProductOfferPrice(stockTransDetailDTO.getProdOfferId(), Const.PRODUCT_OFFER_PRICE.PRICE_TYPE_SALE_AGENT, pricePolicyId);
//            if (!DataUtil.isNullOrEmpty(lstPrice) && !DataUtil.isNullObject(lstPrice.get(0))) {
//                if (DataUtil.isNullObject(vat)) {
//                    vat = lstPrice.get(0).getVat();
//                } else {
//                    if (!DataUtil.safeEqual(vat, lstPrice.get(0).getVat())) {
//                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.export.order.notVat");
//                    }
//                }
//            }
//        }
//        Long telecomServiceId = null;
        List<String> lstProductOfferingCode = Lists.newArrayList();
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
            }
            if (DataUtil.safeEqual(stockTransDTO.getStockAgent(), Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)) {
                if (!DataUtil.safeEqual(Const.PRODUCT_OFFERING.CHECK_SERIAL, productOfferingDTO.getCheckSerial())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "sp.exportStockCollaborator.noSerial.notExport");
                }
            }
            if (lstProductOfferingCode.contains(productOfferingDTO.getCode() + "_" + stockTransDetailDTO.getStateId())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.validate.list.duplicate");
            }
            lstProductOfferingCode.add(productOfferingDTO.getCode() + "_" + stockTransDetailDTO.getStateId());
//            if (!DataUtil.isNullObject(productOfferingDTO)) {
//                if (DataUtil.isNullObject(telecomServiceId)) {
//                    telecomServiceId = productOfferingDTO.getTelecomServiceId();
//                } else {
//                    if (!DataUtil.safeEqual(telecomServiceId, productOfferingDTO.getTelecomServiceId())) {
//                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.export.order.notTelecomServiceId");
//                    }
//                }
//            } else {
//                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
//            }
        }
    }

    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {
    }

    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
    }

    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
    }
}

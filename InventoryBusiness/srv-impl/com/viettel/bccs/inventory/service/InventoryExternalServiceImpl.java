package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.repo.InvoiceListRepo;
import com.viettel.bccs.inventory.repo.InvoiceTemplateRepo;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.sale.dto.SaleTransBankplusDTO;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebParam;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryExternalServiceImpl extends BaseServiceImpl implements InventoryExternalService {

    public static final Logger logger = Logger.getLogger(InventoryExternalServiceImpl.class);

    @Autowired
    private StockSimService stockSimService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private InvoiceTemplateService invoiceTemplateService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockHandsetRepo stockHandsetRepo;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockDeliverService stockDeliverService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private InvoiceListRepo invoiceListRepo;
    @Autowired
    private InvoiceTemplateRepo invoiceTemplateRepo;
    @Autowired
    private SaleWs saleWs;
    @Autowired
    private SaleTransRepo saleTransRepo;


    @Override
    public StockSimMessage getSimInfor(String msisdn) {
        StockSimMessage stockSimMessage = new StockSimMessage();

        try {

            StockSimDTO stocksim = stockSimService.getSimInfor(msisdn);
            stockSimMessage.setStockSimDTO(stocksim);
            stockSimMessage.setSuccess(true);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            stockSimMessage.setErrorCode(ex.getErrorCode());
            stockSimMessage.setDescription(ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            stockSimMessage.setDescription(ex.getMessage());
        }
        return stockSimMessage;
    }

    @Override
    public boolean isCaSim(String serial) {
        return stockSimService.isCaSim(serial);
    }

    public StockHandsetDTO getStockModelSoPin(String serial) {
        return stockHandsetService.getStockModelSoPin(serial);
    }

    public StockTotalResultDTO getQuantityInEcomStock(String prodOfferCode) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(prodOfferCode)) {
            return new StockTotalResultDTO("-1", getText("g6.quantity.ecom.stock.prod.code.not.null"));
        }
        ProductOfferingDTO productOffering = productOfferingService.findByProductOfferCode(prodOfferCode.trim(), Const.STATUS_ACTIVE);
        if (DataUtil.isNullObject(productOffering)) {
            return new StockTotalResultDTO("-1", getText("mn.stock.status.isdn.update.file.isdn.product.not.contains"));
        }

        return stockCardService.getQuantityInEcomStock(prodOfferCode.trim());
    }

    public BaseMessage verifyUnlockG6(String imei) throws LogicException, Exception {

        BaseMessage baseMessage = new BaseMessage(true);
        if (DataUtil.isNullOrEmpty(imei)) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
            baseMessage.setDescription(getText("mn.stock.utilities.look.serial.require.msg"));
            return baseMessage;
        }
        boolean checkExist = stockHandsetService.checkExistSerial(imei.trim());
        if (!checkExist) {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
            baseMessage.setDescription(getText("g6.quantity.verify.unlock.not.exsit"));
            return baseMessage;
        }
        boolean checkUnlock = stockHandsetService.checkUnlockSerial(imei.trim());
        if (checkUnlock) {
            baseMessage.setErrorCode(Const.UNLOCK_G6.SUCCESS);
            baseMessage.setDescription(getText("g6.quantity.verify.unlock.yes"));
        } else {
            baseMessage.setSuccess(false);
            baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
            baseMessage.setDescription(getText("g6.quantity.verify.unlock.no"));
            baseMessage.setKeyMsg("1");
        }
        return baseMessage;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage unlockG6(String imei) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        try {
            baseMessage = stockCardService.unlockG6(imei);
            return baseMessage;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        baseMessage.setSuccess(false);
        baseMessage.setErrorCode(Const.UNLOCK_G6.FAILED);
        return baseMessage;
    }

    public StockDeliveringResultDTO getLstStockDeliveringBill(Date startTime, Date endTime) throws LogicException, Exception {
        try {
            if (startTime == null || endTime == null) {
                return new StockDeliveringResultDTO(Const.DELIVERING_STOCK.FAILED, getText("stock.delivery.date.not.null"), Lists.newArrayList());
            }
            List<StockDeliveringBillDetailDTO> lstStockDeliveringBill = stockTransActionService.getStockDeliveringResult(startTime, endTime);
            if (DataUtil.isNullOrEmpty(lstStockDeliveringBill)) {
                return new StockDeliveringResultDTO(Const.DELIVERING_STOCK.FAILED, getText("stock.delivery.not.found"), Lists.newArrayList());
            }
            for (int i = 0; i < lstStockDeliveringBill.size(); i++) {
                //Duyet tung phieu
                StockDeliveringBillDetailDTO stockDeliveringBillDetail = lstStockDeliveringBill.get(i);
                //Lay ra danh sach thong tin hang hoa
                stockDeliveringBillDetail.setLstProductInfos(productOfferingService.getLstProductInfo(stockDeliveringBillDetail.getStockTransId()));
            }
            return new StockDeliveringResultDTO(Const.DELIVERING_STOCK.SUCCESS, getText("stock.delivery.success"), lstStockDeliveringBill);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new StockDeliveringResultDTO(Const.DELIVERING_STOCK.FAILED, getText("stock.delivery.failed"), Lists.newArrayList());

    }

    public BaseMessage serialCardIsSaledOnBCCS(@WebParam(name = "serial") String serial) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage(false);
        baseMessage.setErrorCode(Const.DELIVERING_STOCK.FAILED);
        try {
            if (DataUtil.isNullOrEmpty(serial) || !DataUtil.checkDigit(serial.trim())) {
                baseMessage.setDescription(getText("stock.serial.card.sale.serial.not.null"));
                return baseMessage;
            }
            List<Long> lstStatus = stockCardService.getStatusSerialCardSale(serial.trim());
            if (!DataUtil.isNullOrEmpty(lstStatus)) {
                if (lstStatus.size() > 1) {
                    baseMessage.setDescription(getTextParam("stock.serial.card.sale.serial.too.match", DataUtil.safeToString(lstStatus.size()), serial));
                    return baseMessage;
                }
                Long status = lstStatus.get(0);
                if (DataUtil.safeEqual(status, 0L)) {
                    baseMessage.setSuccess(true);
                    baseMessage.setErrorCode(Const.DELIVERING_STOCK.SUCCESS);
                    baseMessage.setDescription(getTextParam("stock.serial.card.sale.have.sale", serial.trim()));
                    return baseMessage;
                } else {
                    baseMessage.setDescription(getTextParam("stock.serial.card.sale.not.have.sale", serial.trim(), status.toString()));
                    return baseMessage;
                }
            } else {
                baseMessage.setDescription(getTextParam("stock.serial.card.sale.not.found", serial.trim()));
                return baseMessage;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        baseMessage.setDescription(getTextParam("stock.serial.card.sale.failed", serial));
        return baseMessage;
    }

    public BaseMessage checkTransferCondition(Long staffId, boolean checkCollaborator) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage(true);
        StaffDTO staffDTO = staffService.findOne(staffId);
        if (staffDTO == null) {
            baseMessage.setSuccess(false);
            baseMessage.setDescription(getText("validate.stock.inspect.staff.notFound"));
            return baseMessage;
        }
        //check ton phoi hoa don
        List<InvoiceTemplateDTO> lst = invoiceTemplateService.getInvoiceTemplateExsitByOwnerType(staffId, null, Const.OWNER_TYPE.STAFF);
        if (!DataUtil.isNullOrEmpty(lst) && lst.get(0).getAmount() > 0) {
            baseMessage.setSuccess(false);
            baseMessage.setDescription(getTextParam("ws.product.check.transfer.invoice.template", lst.get(0).getAmount().toString()));
            return baseMessage;
        }
//        check con hang hay khong
        List<StockTotalDTO> lstStockTotal = stockTransService.checkHaveProductOffering(staffId, Const.OWNER_TYPE.STAFF_LONG, checkCollaborator);
        if (!DataUtil.isNullOrEmpty(lstStockTotal)) {
            baseMessage.setSuccess(false);
            baseMessage.setDescription(getTextParam("ws.product.check.transfer.not.have.product",
                    lstStockTotal.stream().map(x -> x.getOwnerCode() + ": " + x.getCode()).collect(Collectors.joining(","))));
            return baseMessage;
        }
        //check con giao dich treo hay khong
        List<String> lstSuspend = stockTransService.checkStockTransSuppend(staffId, Const.OWNER_TYPE.STAFF_LONG);
        if (!DataUtil.isNullOrEmpty(lstSuspend)) {
            baseMessage.setSuccess(false);
            baseMessage.setDescription(getTextParam("ws.product.check.transfer.not.trans.suppend",
                    lstSuspend.stream().map(x -> x.toString()).collect(Collectors.joining(","))));
            return baseMessage;
        }
        baseMessage.setDescription(getText("ws.product.check.transfer.success"));
        return baseMessage;
    }

    public StampInforDTO getStampInformation(List<StampListDTO> lstStampInput) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstStampInput)) {
            return new StampInforDTO(null, false, "ERR_IM", "ext.stamp.lst.stamp.is.null");
        }
        List<StampDTO> lstStampResult = Lists.newArrayList();

        for (StampListDTO stampListDTO : lstStampInput) {
            StampDTO stampDTO = null;
            try {
                if (DataUtil.isNullOrEmpty(stampListDTO.getStockModelCode())) {
                    stampDTO = new StampDTO(stampListDTO.getStockModelCode(), false, "ERR_IM", "ext.stamp.stock.code.is.null");
                    lstStampResult.add(stampDTO);
                    continue;
                }
                if (DataUtil.isNullOrEmpty(stampListDTO.getFromSerial())) {
                    stampDTO = new StampDTO(stampListDTO.getStockModelCode(), false, "ERR_IM", "ext.stamp.from.serial.is.null");
                    lstStampResult.add(stampDTO);
                    continue;
                }
                String lstOptionSetModelCode = optionSetValueService.getValueByTwoCodeOption(Const.STAMP.STAMP_STOCK_MODEL_CODE, Const.STAMP.STAMP_STOCK_MODEL_CODE);

                if (!lstOptionSetModelCode.contains(stampListDTO.getStockModelCode().trim())) {
                    stampDTO = new StampDTO(stampListDTO.getStockModelCode(), false, "ERR_IM", "ext.stamp.stock.code.is.not.cofig", stampListDTO.getStockModelCode());
                    lstStampResult.add(stampDTO);
                    continue;
                }

                ProductOfferingDTO productOfferingDTO = productOfferingService.findByProductOfferCode(stampListDTO.getStockModelCode().trim(), Const.STATUS_ACTIVE);
                if (DataUtil.isNullObject(productOfferingDTO)) {
                    stampDTO = new StampDTO(stampListDTO.getStockModelCode(), true, "00", "ext.stamp.stock.code.is.not.found", stampListDTO.getStockModelCode());
                    stampDTO.setFromSerial(stampListDTO.getFromSerial());
                    stampDTO.setStockModelCode(stampListDTO.getStockModelCode());
                    stampDTO.setStatus(-1L); //chua nhap kho
                    lstStampResult.add(stampDTO);
                    continue;
                }
                stampDTO = stockHandsetRepo.getStampInformation(productOfferingDTO.getProductOfferingId(), stampListDTO.getFromSerial());
                if (DataUtil.isNullObject(stampDTO)) {
                    stampDTO = new StampDTO(stampListDTO.getStockModelCode(), true, "00", "ext.stamp.serial.result.not.found", stampListDTO.getFromSerial());
                    stampDTO.setFromSerial(stampListDTO.getFromSerial());
                    stampDTO.setStockModelCode(stampListDTO.getStockModelCode());
                    stampDTO.setStatus(-1L); //chua nhap kho
                    lstStampResult.add(stampDTO);
                    continue;
                }
                stampDTO.setSuccess(true);
                lstStampResult.add(stampDTO);
            } catch (Exception ex) {
                logger.error("Error :", ex);
                stampDTO = new StampDTO(stampListDTO.getStockModelCode(), false, "ERR_IM", "ext.stamp.error");
                lstStampResult.add(stampDTO);
            }
        }
        return new StampInforDTO(lstStampResult, true, "00", "ext.stamp.success");
    }

    public StockDeliverResultDTO checkStaffHaveStockDeliver(List<String> lstStaffCode) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstStaffCode)) {
            return new StockDeliverResultDTO(null, false, "ERR_IM", "ext.stock.deliver.staff.null");
        }
        List<StockDeliverInforDTO> lstStockResult = Lists.newArrayList();
        for (String staffCode : lstStaffCode) {
            StockDeliverInforDTO stockDeliverInforDTO = null;
            try {

                stockDeliverInforDTO = new StockDeliverInforDTO(staffCode.trim());
                stockDeliverInforDTO.setErrorCode("ERR_IM");

                //check nhan vien ton tai
                StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode.trim());
                if (DataUtil.isNullObject(staffDTO)) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.not.found");
                    stockDeliverInforDTO.setErrorCode("ERR_IM_STAFF_NOT_FOUND");
                    lstStockResult.add(stockDeliverInforDTO);
                    continue;
                }

                boolean checkStaffHaveStockTotal = stockTotalService.checkHaveProductInStockTotal(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG);
                if (checkStaffHaveStockTotal) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.have.inventory");
                }

                //check giao dich treo nhan vien
                boolean checkSuspend = stockTransRepo.checkSuspendStock(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId());
                if (checkSuspend) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.suspend");
                }

                //check hoa don
                boolean checkStaffHaveInvoiceList = invoiceListRepo.checkStaffHaveInvoiceList(staffDTO.getStaffId());
                if (checkStaffHaveInvoiceList) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.have.invoice.List");
                }

                //check phoi hoa don
                boolean checkObjectHaveInvoiceTemplate = invoiceTemplateRepo.checkObjectHaveInvoiceTemplate(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG);
                if (checkObjectHaveInvoiceTemplate) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.have.invoice.Template");
                }

                // Kiem tra nhan vien co quan ly kenh nao khong
                List<Staff> listStaff = staffService.getStaffList(staffDTO.getStaffId());
                if (!DataUtil.isNullOrEmpty(listStaff)) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.have.manage");
                }

                List<SaleTransDTO> listSaleTransDTO = saleTransRepo.getListSaleTransByStaffId(staffDTO.getStaffId());
                if (!DataUtil.isNullOrEmpty(listSaleTransDTO)) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.have.sale_trans", DataUtil.safeToString(listSaleTransDTO.size()));
                }

                List<SaleTransBankplusDTO> listSaleTransBankplusDTO = saleTransRepo.getListSaleTransBankplusByStaffId(staffDTO.getStaffId());
                if (!DataUtil.isNullOrEmpty(listSaleTransBankplusDTO)) {
                    stockDeliverInforDTO.addError("ext.stock.deliver.staff.have.sale_trans_bankplus", DataUtil.safeToString(listSaleTransBankplusDTO.size()));
                }

                //Kiem tra nhan vien co thu kho hay khong
                ShopDTO shopDTO = shopService.getShopByShopKeeper(staffDTO.getStaffId());
                if (DataUtil.isNullObject(shopDTO)) {
                    if (stockDeliverInforDTO.getListError().isEmpty()) {
                        stockDeliverInforDTO.setHaveTransfer(true);
                        stockDeliverInforDTO.setSuccess(true);
                        stockDeliverInforDTO.setDescriptionResult("ext.stock.deliver.shop.not.found");
                    }
                    lstStockResult.add(stockDeliverInforDTO);
                    continue;
                }

                //kiem tra con ton kho
                boolean haveStockTotal = stockTotalService.checkHaveProductInStockTotal(shopDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG);
                if (!haveStockTotal) {
                    //da het hang --> cho phep dieu chuyen
                    if (stockDeliverInforDTO.getListError().isEmpty()) {
                        stockDeliverInforDTO.setHaveTransfer(true);
                        stockDeliverInforDTO.setSuccess(true);
                        stockDeliverInforDTO.setDescriptionResult("ext.stock.deliver.have.not.product.stock.total");
                    }
                    lstStockResult.add(stockDeliverInforDTO);
                    continue;
                }

                StockDeliverDTO stockDeliverDTO = stockDeliverService.getStockDeliverByOwnerIdAndStatus(shopDTO.getShopId(), Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_SIGN);
                if (!DataUtil.isNullObject(stockDeliverDTO)) {
                    //da ban giao
                    if (stockDeliverInforDTO.getListError().isEmpty()) {
                        stockDeliverInforDTO.setHaveTransfer(true);
                        stockDeliverInforDTO.setSuccess(true);
                        stockDeliverInforDTO.setDescriptionResult("ext.stock.delivered");
                    }
                } else {
                    stockDeliverInforDTO.addError("ext.stock.deliver.false");
                }

                lstStockResult.add(stockDeliverInforDTO);

            } catch (Exception ex) {
                logger.error("Error :", ex);
                stockDeliverInforDTO = new StockDeliverInforDTO(staffCode, false, false, "ERR_IM", "ext.stock.deliver.staff.error");
                lstStockResult.add(stockDeliverInforDTO);
            }
        }
        return new StockDeliverResultDTO(lstStockResult, true, null, "ext.stock.deliver.success");
    }
}

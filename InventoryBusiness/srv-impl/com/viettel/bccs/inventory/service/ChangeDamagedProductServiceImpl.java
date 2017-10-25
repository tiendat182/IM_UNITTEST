package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.cc.dto.CardInfoProvisioningDTO;
import com.viettel.bccs.cc.dto.HistoryInforCardNumberDTO;
import com.viettel.bccs.im1.dto.ReqActivateKitDTO;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.dto.VcRequestDTO;
import com.viettel.bccs.im1.service.ReqActivateKitService;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.im1.service.VcRequestService;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Service
public class ChangeDamagedProductServiceImpl extends BaseStockService implements ChangeDamagedProductService {
    public static final Logger logger = Logger.getLogger(ChangeDamagedProductService.class);
    private final int EVN_SERIAL_LENGTH = 15;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    // DB IM1
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ReportChangeGoodsService reportChangeGoodsService;
    @Autowired
    private VcRequestService vcRequestService;
    @Autowired
    private ReqActivateKitService reqActivateKitService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private CustomerCareWs customerCareWs;

    @Override
    public List<ChangeDamagedProductDTO> checkNewSerialInput(String newSerial, Long staffId) throws LogicException, Exception {
        try {
            StaffDTO staffDTO = staffService.findOne(staffId);
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(Const.STATUS_ACTIVE, staffDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "balance.vali.approveUser");
            }
            List<ChangeDamagedProductDTO> listSerial = stockTransSerialService.getListSerialProduct(newSerial, Const.OWNER_TYPE.STAFF_LONG, staffId);
            if (DataUtil.isNullOrEmpty(listSerial)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.newSerial.invalid");
            }
            List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CHANGE_DAMAGE_PRODUCT_OFFER_TYPE);
            List<ChangeDamagedProductDTO> lstResult = Lists.newArrayList();
            for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                if (!DataUtil.isNullObject(optionSetValueDTO.getValue()) && !DataUtil.isNullObject(optionSetValueDTO.getValue().trim())) {
                    for (ChangeDamagedProductDTO changeDamagedProductDTO : listSerial) {
                        if (DataUtil.safeEqual(DataUtil.safeToLong(optionSetValueDTO.getValue()), changeDamagedProductDTO.getProductOfferTypeId())) {
                            lstResult.add(changeDamagedProductDTO);
                        }
                    }
                }
            }
            if (DataUtil.isNullOrEmpty(lstResult)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.newSerial.invalid");
            }
            return lstResult;
        } catch (Exception ex) {
            logger.error("error ", ex);
            throw ex;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage saveChangeDamagedProduct(Long productOfferTypeId, Long prodOfferId, Long reasonId, List<ChangeDamagedProductDTO> lstChangeProduct, Long staffId) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        try {
            StaffDTO staffDTO = staffService.findOne(staffId);
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(staffDTO.getStatus(), Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "balance.vali.approveUser");
            }
            if (DataUtil.isNullOrEmpty(lstChangeProduct) || DataUtil.isNullObject(lstChangeProduct.get(0))) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.data.isNull");
            }
            if (DataUtil.isNullOrZero(productOfferTypeId)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.product.offerType.isNull");
            }
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
            if (DataUtil.isNullObject(productOfferTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.stock.inspect.productOfferType.notFound");
            }
            if (DataUtil.isNullOrZero(prodOfferId)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.inspect.not.choose.product.offer");
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
            if (DataUtil.isNullObject(productOfferingDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
            }
            validateChangeProduct(productOfferTypeDTO, productOfferingDTO, reasonId, lstChangeProduct, staffDTO, null);
            // Save data
            saveData(staffDTO, productOfferTypeDTO, productOfferingDTO, lstChangeProduct);

        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setKeyMsg(le.getKeyMsg());
            baseMessage.setParamsMsg(le.getParamsMsg());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
            throw le;
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setKeyMsg("common.error.happen");
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
        return baseMessage;
    }

    @Transactional(rollbackFor = Exception.class)
    private void saveData(StaffDTO staffDTO, ProductOfferTypeDTO productOfferTypeDTO, ProductOfferingDTO productOfferingDTO, List<ChangeDamagedProductDTO> lstChangeProduct) throws Exception, LogicException {
        // Xu ly du lieu
        String strReasonCodeExp = getText("REASON_EXP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeExp)) {
            strReasonCodeExp = "XKH";
        }
        List<ReasonDTO> lstReasonExp = reasonService.getLsReasonByCode(strReasonCodeExp);
        if (DataUtil.isNullOrEmpty(lstReasonExp) || DataUtil.isNullObject(lstReasonExp.get(0))) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.reason.export.not.found");
        }
        ReasonDTO reasonExp = lstReasonExp.get(0);
        String strReasonCodeImp = getText("REASON_IMP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeImp)) {
            strReasonCodeImp = "NKH";
        }
        List<ReasonDTO> lstReasonImp = reasonService.getLsReasonByCode(strReasonCodeImp);
        if (DataUtil.isNullOrEmpty(lstReasonImp) || DataUtil.isNullObject(lstReasonImp.get(0))) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.reason.import.not.found");
        }
        ReasonDTO reasonImp = lstReasonImp.get(0);
        // Giao dich xuat kho
        List<StockTransDetailDTO> stockTransDetail = getStockTransDetail(productOfferTypeDTO, productOfferingDTO, lstChangeProduct, staffDTO, false);
        Date currentDate = getSysDate(em);

        StockTransDTO stockTransExport = getStockTransExport(staffDTO.getStaffId(), reasonExp.getReasonId(), currentDate);
        StockTransDTO stockTransExportNew = doSaveStockTrans(stockTransExport);
        StockTransActionDTO stockTransActionExpNote = getStockTransActionExpNote(staffDTO);
        StockTransActionDTO stockTransActionExp = DataUtil.cloneBean(stockTransActionExpNote);
        StockTransActionDTO stockTransActionExped = DataUtil.cloneBean(stockTransActionExpNote);
        // Luu stock_trans_action
        stockTransActionExpNote = doSaveStockTransAction(stockTransExportNew, stockTransActionExpNote);
        stockTransActionExp.setStockTransActionId(null);
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        // Luu stock_trans_action
        doSaveStockTransAction(stockTransExportNew, stockTransActionExp);

        stockTransActionExped.setStockTransActionId(null);
        stockTransActionExped.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        // Luu stock_trans_action
        doSaveStockTransAction(stockTransExportNew, stockTransActionExped);

        // Luu stock_trans_detail
        List<StockTransDetailDTO> stockTransDetailExp = DataUtil.cloneBean(stockTransDetail);
        doSaveStockTransDetail(stockTransExportNew, stockTransDetailExp);
        // Luu stock_trans_serial
        doSaveStockTransSerial(stockTransExportNew, stockTransDetailExp);
        // Cap nhat trang thai hang moi ve da su dung
        FlagStockDTO flagStockExp = new FlagStockDTO();
        flagStockExp.setNewStatus(Const.STATE_STATUS.SALE);
        flagStockExp.setOldStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
        doSaveStockGoods(stockTransExportNew, stockTransDetailExp, flagStockExp);
        // Tru kho
        flagStockExp.setExportStock(true);
        flagStockExp.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockExp.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        doSaveStockTotal(stockTransExportNew, stockTransDetailExp, flagStockExp, stockTransActionExpNote);

        // Luu thong tin nhap hang hong
        StockTransDTO stockTransImport = getStockTransImport(staffDTO.getStaffId(), reasonImp.getReasonId(), currentDate);
        stockTransImport.setFromStockTransId(stockTransExportNew.getStockTransId());
        StockTransDTO stockTransImportNew = doSaveStockTrans(stockTransImport);
        StockTransActionDTO stockTransActionImpNote = getStockTransActionImpNote(staffDTO);
        StockTransActionDTO stockTransActionImp = DataUtil.cloneBean(stockTransActionImpNote);
        // Luu stock_trans_action
        stockTransActionImpNote = doSaveStockTransAction(stockTransImportNew, stockTransActionImpNote);
        stockTransActionImp.setStockTransActionId(null);
        stockTransActionImp.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        // Luu stock_trans_action
        doSaveStockTransAction(stockTransImportNew, stockTransActionImp);
        // Luu stock_trans_detail
        List<StockTransDetailDTO> stockTransDetailImp = getStockTransDetail(productOfferTypeDTO, productOfferingDTO, lstChangeProduct, staffDTO, true);
        doSaveStockTransDetail(stockTransImportNew, stockTransDetailImp);
        // Luu stock_trans_serial
        doSaveStockTransSerial(stockTransImportNew, stockTransDetailImp);
        // Cap nhat trang thai hang cu ve da su dung
        FlagStockDTO flagStockImp = new FlagStockDTO();
        flagStockImp.setNewStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
        flagStockImp.setOldStatus(Const.STATE_STATUS.SALE);
        flagStockImp.setStateIdForReasonId(Const.STATE_DAMAGE);
        flagStockImp.setUpdateDamageProduct(true);
        flagStockImp.setUpdateOwnerId(true);
        doSaveStockGoods(stockTransImportNew, stockTransDetailImp, flagStockImp);
        // Cong kho
        flagStockImp.setImportStock(true);
        flagStockImp.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockImp.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        doSaveStockTotal(stockTransImportNew, stockTransDetailImp, flagStockImp, stockTransActionImpNote);

        ShopDTO userShop = shopService.findOne(staffDTO.getShopId());
        for (ChangeDamagedProductDTO changeProduct : lstChangeProduct) {
            // Neu mat hang the cao.
            if (!DataUtil.isNullObject(productOfferingDTO)
                    && !DataUtil.isNullObject(productOfferingDTO.getProductOfferTypeId())
                    && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
                // Neu mat hang truyen vao la the cao data thi khong active tong dai.
                List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_MODEL_CARD_DATA);
                boolean isCardData = false;
                if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
                    for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                        if (DataUtil.safeEqual(optionSetValueDTO.getValue(), productOfferingDTO.getCode())) {
                            isCardData = true;
                            break;
                        }
                    }
                }
                if (!isCardData) {
                    VcRequestDTO vcRequestDTO = new VcRequestDTO();
                    vcRequestDTO.setCreateTime(getSysDate(emIm1));
                    vcRequestDTO.setUserId(staffDTO.getStaffCode());
                    vcRequestDTO.setFromSerial(normalSerialCard(changeProduct.getNewSerial()));
                    vcRequestDTO.setToSerial(normalSerialCard(changeProduct.getNewSerial()));
                    vcRequestDTO.setStaffId(staffDTO.getStaffId());
                    vcRequestDTO.setShopId(staffDTO.getShopId());
                    if (!DataUtil.isNullObject(stockTransExportNew.getStockTransId())) {
                        vcRequestDTO.setTransId(stockTransExportNew.getStockTransId());
                    }
                    vcRequestDTO.setRequestType(Const.SALE_TRANS.REQUEST_TYPE_CHANGE_GOODS);
                    vcRequestDTO.setStatus(0L);
                    VcRequestDTO result = vcRequestService.create(vcRequestDTO);
                    if (DataUtil.isNullObject(result)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.create.vc.request.error");
                    }
                }
            }
            // Luu thong tin kit vao bang vc_active_kit.
            if (!DataUtil.isNullObject(productOfferingDTO)
                    && !DataUtil.isNullObject(productOfferingDTO.getProductOfferTypeId())
                    && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_KIT)) {
                ReqActivateKitDTO reqActivateKitDTO = new ReqActivateKitDTO();
//                Long reqId = getSequence(emIm1, "BCCS_IM.REQ_ID_SEQ");
//                reqActivateKitDTO.setReqId(reqId);
                reqActivateKitDTO.setSaleTransId(stockTransExportNew.getStockTransId());
                reqActivateKitDTO.setFromSerial(changeProduct.getNewSerial());
                reqActivateKitDTO.setToSerial(changeProduct.getNewSerial());
                reqActivateKitDTO.setShopId(staffDTO.getShopId());
                reqActivateKitDTO.setStaffId(staffDTO.getStaffId());
                reqActivateKitDTO.setSaleTransDate(currentDate);
                reqActivateKitDTO.setCreateDate(getSysDate(emIm1));
                reqActivateKitDTO.setSaleType(Const.SALE_TRANS.SALE_TYPE);
                reqActivateKitDTO.setSaleTransType(Const.SALE_TRANS.SALE_TRANS_TYPE_RETAIL);
                reqActivateKitDTO.setProcessStatus(0L);
                ReqActivateKitDTO result = reqActivateKitService.create(reqActivateKitDTO);
                if (DataUtil.isNullObject(result)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.agent.create.req_activate_kit.error");
                }
            }

            // insert du lieu vao bang report_change_goods
            ReportChangeGoodsDTO report = new ReportChangeGoodsDTO();
            report.setId(getSequence(em, "REPORT_CHANGE_GOODS_SEQ"));
            report.setShopId(staffDTO.getShopId());
            report.setShopCode(userShop.getShopCode());
            report.setShopName(userShop.getName());
            report.setStaffId(staffDTO.getStaffId());
            report.setStaffCode(staffDTO.getStaffCode());
            report.setStaffName(staffDTO.getName());
            report.setProdOfferId(productOfferingDTO.getProductOfferingId());
            report.setProdOfferCode(productOfferingDTO.getCode());
            report.setProdOfferName(productOfferingDTO.getName());
            report.setSerialNew(changeProduct.getNewSerial());
            report.setSerialOld(normalSerialCard(changeProduct.getOldSerial()));
            report.setCreateDate(currentDate);
            report.setTelecomServiceId(productOfferingDTO.getTelecomServiceId());
            report.setStockTransId(stockTransExportNew.getStockTransId());
            if (!DataUtil.isNullObject(productOfferingDTO)
                    && !DataUtil.isNullObject(productOfferingDTO.getProductOfferTypeId())
                    && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
                report.setReasonId(changeProduct.getReasonId());
            }
            reportChangeGoodsService.create(report);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ErrorChangeProductDTO> saveChangeDamagedProductFile(Long productOfferTypeId, Long prodOfferId, Long reasonId, List<ChangeDamagedProductDTO> lstChangeProduct, Long staffId) throws LogicException, Exception {
        List<ErrorChangeProductDTO> lstResult = Lists.newArrayList();
        try {
            StaffDTO staffDTO = staffService.findOne(staffId);
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(staffDTO.getStatus(), Const.STATUS_ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "balance.vali.approveUser");
            }
            if (DataUtil.isNullOrEmpty(lstChangeProduct) || DataUtil.isNullObject(lstChangeProduct.get(0))) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.data.isNull");
            }
            if (DataUtil.isNullOrZero(productOfferTypeId)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.product.offerType.isNull");
            }
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
            if (DataUtil.isNullObject(productOfferTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.stock.inspect.productOfferType.notFound");
            }
            if (DataUtil.isNullOrZero(prodOfferId)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.inspect.not.choose.product.offer");
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
            if (DataUtil.isNullObject(productOfferingDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
            }
            List<ChangeDamagedProductDTO> lstSuccess = validateChangeProduct(productOfferTypeDTO, productOfferingDTO, reasonId, lstChangeProduct, staffDTO, lstResult);
            // Save data
            if (!DataUtil.isNullOrEmpty(lstSuccess) && DataUtil.isNullOrEmpty(lstResult)) {
                saveData(staffDTO, productOfferTypeDTO, productOfferingDTO, lstSuccess);
            }
            return lstResult;
        } catch (LogicException le) {
            logger.error(le.getMessage(), le);
            throw le;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private List<ChangeDamagedProductDTO> validateChangeProduct(ProductOfferTypeDTO productOfferTypeDTO,
                                                                ProductOfferingDTO productOfferingDTO,
                                                                Long reasonId,
                                                                List<ChangeDamagedProductDTO> lstChangeProduct,
                                                                StaffDTO staffDTO, List<ErrorChangeProductDTO> lstError) throws LogicException, Exception {
        if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD) &&
                DataUtil.isNullOrZero(reasonId)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.change.damaged.choose.reason.require");
        }
        // Check so luong xuat hang.
        int totalSerialNew = lstChangeProduct.size();
        StockTotalDTO stockTotal = new StockTotalDTO();
        stockTotal.setOwnerId(staffDTO.getStaffId());
        stockTotal.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTotal.setProdOfferId(productOfferingDTO.getProductOfferingId());
        stockTotal.setStateId(Const.STATE_STATUS.NEW);
        List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(stockTotal);
        if (DataUtil.isNullOrEmpty(lstStockTotal)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.empty", productOfferingDTO.getName());
        }
        if (lstStockTotal.get(0).getCurrentQuantity() < totalSerialNew) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.stock.total.not.enought");
        }
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            StockTotalIm1DTO stockTotalIm1DTO = new StockTotalIm1DTO();
            stockTotalIm1DTO.setOwnerId(staffDTO.getStaffId());
            stockTotalIm1DTO.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            stockTotalIm1DTO.setStockModelId(productOfferingDTO.getProductOfferingId());
            stockTotalIm1DTO.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            stockTotalIm1DTO.setStateId(Const.STATE_STATUS.NEW);
            List<StockTotalIm1DTO> lstStockTotalIm1 = stockTotalIm1Service.findStockTotal(stockTotalIm1DTO);
            if (DataUtil.isNullOrEmpty(lstStockTotalIm1)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTotal.im1.empty", productOfferingDTO.getName());
            }
            if (lstStockTotalIm1.get(0).getQuantity() < totalSerialNew) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.stock.total.im1.not.enought");
            }
        }
        List<ChangeDamagedProductDTO> lstSuccess = Lists.newArrayList();
        for (ChangeDamagedProductDTO changeProductDTO : lstChangeProduct) {
            ErrorChangeProductDTO errorChangeProductDTO = new ErrorChangeProductDTO();
            errorChangeProductDTO.setOldNumber(changeProductDTO.getOldSerial());
            errorChangeProductDTO.setNewNumber(changeProductDTO.getNewSerial());
            if (DataUtil.isNullOrEmpty(changeProductDTO.getOldSerial())) {
                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.oldSerial.isNull");
                } else {
                    errorChangeProductDTO.setError(getText("stock.validate.change.damaged.oldSerial.isNull"));
                    lstError.add(errorChangeProductDTO);
                    continue;
                }
            }
            if (DataUtil.isNullOrEmpty(changeProductDTO.getNewSerial())) {
                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.newSerial.isNull");
                } else {
                    errorChangeProductDTO.setError(getText("stock.validate.change.damaged.newSerial.isNull"));
                    lstError.add(errorChangeProductDTO);
                    continue;
                }
            }
            if (!DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_HANDSET)) {
                if (!DataUtil.isNumber(changeProductDTO.getOldSerial())) {
                    if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.file.oldSerial.invalid");
                    } else {
                        errorChangeProductDTO.setError(getText("stock.validate.stock.file.oldSerial.invalid"));
                        lstError.add(errorChangeProductDTO);
                        continue;
                    }
                }
                if (!DataUtil.isNumber(changeProductDTO.getNewSerial())) {
                    if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.file.newSerial.invalid");
                    } else {
                        errorChangeProductDTO.setError(getText("stock.validate.stock.file.newSerial.invalid"));
                        lstError.add(errorChangeProductDTO);
                        continue;
                    }
                }
            }
            // Validate serial nhap vao
            List<StockTransSerialDTO> lstStockSerial = stockTransSerialService.getListBySerial(productOfferingDTO.getProductOfferingId(), changeProductDTO.getOldSerial(), null, null, null, productOfferTypeDTO.getTableName());
            boolean isFoundInOldStockCard = false;
            if (DataUtil.isNullOrEmpty(lstStockSerial)) {
                // Kiem tra xem view cu
                if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.CARD)) {
                    lstStockSerial = stockTransSerialService.getListStockCardSaledBySerial(productOfferingDTO.getProductOfferingId(), changeProductDTO.getOldSerial());
                }
                if (DataUtil.isNullOrEmpty(lstStockSerial)) {
                    if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.oldSerial.notExistence");
                    } else {
                        errorChangeProductDTO.setError(getText("stock.validate.change.damaged.oldSerial.notExistence"));
                        lstError.add(errorChangeProductDTO);
                        continue;
                    }
                } else {
                    isFoundInOldStockCard = true;
                }
            }
            StockTransSerialDTO stockTransSerialDTO = lstStockSerial.get(0);
            Long statusOldSerial = stockTransSerialDTO.getStatus();
            Long ownerTypeOldSerial = stockTransSerialDTO.getOwnerType();
            Long ownerIdOldSerial = stockTransSerialDTO.getOwnerId();

            if (DataUtil.isNullObject(statusOldSerial) || DataUtil.isNullObject(ownerTypeOldSerial) || DataUtil.isNullObject(ownerIdOldSerial)) {
                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.oldSerial.not.information");
                } else {
                    errorChangeProductDTO.setError(getText("stock.validate.change.damaged.oldSerial.not.information"));
                    lstError.add(errorChangeProductDTO);
                    continue;
                }
            }
            // chi cho phep doi hang hong neu serial hang hong da ban hoac trong kho doi tuong ngoai Viettel.
            if (!DataUtil.safeEqual(statusOldSerial, Const.STOCK_GOODS.STATUS_SALE) && !DataUtil.safeEqual(statusOldSerial, Const.STOCK_GOODS.STATUS_NEW)) {
                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.oldSerial.not.sale");
                } else {
                    errorChangeProductDTO.setError(getText("stock.validate.change.damaged.oldSerial.not.sale"));
                    lstError.add(errorChangeProductDTO);
                    continue;
                }
            }
            // Neu trang thai chua ban thi kiem tra hang hoa phai nam trong kho doi tuong ngoai Viettel
            if (DataUtil.safeEqual(statusOldSerial, Const.STOCK_GOODS.STATUS_NEW)) {
                Long channelTypeId;
                if (DataUtil.safeEqual(ownerTypeOldSerial, Const.OWNER_TYPE.STAFF_LONG)) {
                    StaffDTO staffOld = staffService.findOne(ownerIdOldSerial);
                    channelTypeId = staffOld.getChannelTypeId();
                } else {
                    ShopDTO shopDTO = shopService.findOne(ownerIdOldSerial);
                    channelTypeId = shopDTO.getChannelTypeId();
                }
                ChannelTypeDTO channelTypeDTO = shopService.getChannelTypeById(channelTypeId);
                if (!DataUtil.isNullObject(channelTypeDTO) && DataUtil.safeEqual(channelTypeDTO.getIsVtUnit(), Const.VT_UNIT.VT)) {
                    if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.oldSerial.not.sale");
                    } else {
                        errorChangeProductDTO.setError(getText("stock.validate.change.damaged.oldSerial.not.sale"));
                        lstError.add(errorChangeProductDTO);
                        continue;
                    }
                }
            }
            //hoangnt: neu mat hang the cao data thi ko check lock tong dai
            boolean isCardData = false;
            if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
                // Neu mat hang truyen vao la the cao data thi khong active tong dai.
                List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_MODEL_CARD_DATA);
                if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
                    for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                        if (DataUtil.safeEqual(optionSetValueDTO.getValue(), productOfferingDTO.getCode())) {
                            isCardData = true;
                            break;
                        }
                    }
                }
            }
            // Check the cao hong da bi khoa chua. Neu chua bi khoa thi thong bao khoa the truoc khi
            if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)
                    && changeProductDTO.getOldSerial().length() < this.EVN_SERIAL_LENGTH && !isCardData) {
                // Thuc hien khoa the tren CC
                boolean lockCardSucess = true;
                // Lay thong tin the cao
                // Kiem tra xem lock tren CC chua.
                CardNumberLockDTO cardNumberLockDTO = customerCareWs.getHistoryCardNumber(changeProductDTO.getOldSerial());
                if (DataUtil.isNullObject(cardNumberLockDTO) || !DataUtil.safeEqual(cardNumberLockDTO.getStatus(), Const.STATUS_ACTIVE)) {
                    lockCardSucess = false;
                    HistoryInforCardNumberDTO historyInforCardNumberDTO = customerCareWs.getInforCardNumber(changeProductDTO.getOldSerial(),
                            Const.CC_WS.REG_TYPE_LOCK_CARD_OCS, false, null, null);
                    if (historyInforCardNumberDTO != null && "0".equals(historyInforCardNumberDTO.getErrorCode())) {
                        CardInfoProvisioningDTO cardInfoProvisioningDTO = historyInforCardNumberDTO.getCardInfoProvisioningDTO();
                        // Goi tong dai thanh cong.
                        if (cardInfoProvisioningDTO != null && cardInfoProvisioningDTO.isResult() &&
                                DataUtil.safeEqual(cardInfoProvisioningDTO.getResponeCode(), Const.CC_WS.RESPONSE_CODE_SUCCESS)) {
                            String valueCard = historyInforCardNumberDTO.getCardInfoProvisioningDTO().getCardValue();
                            String dateUsed = historyInforCardNumberDTO.getCardInfoProvisioningDTO().getDateUsed();

                            if (DataUtil.isNullObject(valueCard)) {
                                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "lockcard.error.no.value.provisioning");
                                } else {
                                    errorChangeProductDTO.setError(getText("lockcard.error.no.value.provisioning"));
                                    lstError.add(errorChangeProductDTO);
                                    continue;
                                }
                            }
                            if (!DataUtil.isNullObject(dateUsed)) {
                                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "lockcard.error.used.in.provisioning");
                                } else {
                                    errorChangeProductDTO.setError(getText("lockcard.error.used.in.provisioning"));
                                    lstError.add(errorChangeProductDTO);
                                    continue;
                                }
                            }
                            // Thuc hien khoa the
                            ShopDTO userShop = shopService.findOne(staffDTO.getShopId());
                            ReasonDTO reason = reasonService.findOne(reasonId);
                            BaseMessage baseMessage = customerCareWs.lockedCard(staffDTO.getName(), changeProductDTO.getOldSerial(), userShop.getShopCode(),
                                    reason.getReasonName(), staffDTO.getTel(), changeProductDTO.getIpAddress(), valueCard);
                            if (baseMessage != null && DataUtil.safeEqual(baseMessage.getErrorCode(), Const.CC_WS.RESPONSE_CODE_SUCCESS)) {
                                lockCardSucess = true;
                            } else {
                                if (baseMessage != null) {
                                    logger.error("error lock cc: " + baseMessage.getDescription());
                                }
                                if (baseMessage != null && !DataUtil.isNullObject(baseMessage.getDescription())) {
                                    if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "CC: " + baseMessage.getDescription());
                                    } else {
                                        errorChangeProductDTO.setError("CC: " + baseMessage.getDescription());
                                        lstError.add(errorChangeProductDTO);
                                        continue;
                                    }
                                }
                            }
                        } else {
                            if (cardInfoProvisioningDTO != null && !cardInfoProvisioningDTO.isResult() &&
                                    !DataUtil.safeEqual(cardInfoProvisioningDTO.getResponeCode(), Const.CC_WS.RESPONSE_CODE_SUCCESS)) {
                                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "lockcard.error.in.provisioning");
                                } else {
                                    errorChangeProductDTO.setError(getText("lockcard.error.in.provisioning"));
                                    lstError.add(errorChangeProductDTO);
                                    continue;
                                }
                            }
                        }
                    } else {
                        if (historyInforCardNumberDTO != null && !"0".equals(historyInforCardNumberDTO.getErrorCode())
                                && !DataUtil.isNullObject(historyInforCardNumberDTO.getErrorDescription())) {
                            if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "CC: " + historyInforCardNumberDTO.getErrorDescription());
                            } else {
                                errorChangeProductDTO.setError("CC: " + historyInforCardNumberDTO.getErrorDescription());
                                lstError.add(errorChangeProductDTO);
                                continue;
                            }
                        }
                    }
                }
                if (!lockCardSucess) {
                    if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.card.not.block", changeProductDTO.getOldSerial());
                    } else {
                        errorChangeProductDTO.setError(getTextParam("stock.validate.stock.card.not.block", changeProductDTO.getOldSerial()));
                        lstError.add(errorChangeProductDTO);
                        continue;
                    }
                }
            }
            // Kiem tra serial moi.
            List<StockTransSerialDTO> lstStockSerialNew = stockTransSerialService.getListBySerial(productOfferingDTO.getProductOfferingId(), changeProductDTO.getNewSerial(),
                    Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId(), DataUtil.safeToLong(Const.STATUS_ACTIVE), productOfferTypeDTO.getTableName());
            if (DataUtil.isNullOrEmpty(lstStockSerialNew) || DataUtil.isNullObject(lstStockSerialNew.get(0))) {
                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.newSerial.notActive");
                } else {
                    errorChangeProductDTO.setError(getText("stock.validate.change.damaged.newSerial.notActive"));
                    lstError.add(errorChangeProductDTO);
                    continue;
                }
            }
            StockTransSerialDTO stockTransSerialDTONew = lstStockSerialNew.get(0);
            if (!DataUtil.safeEqual(stockTransSerialDTONew.getStateId(), Const.STATE_STATUS.NEW)) {
                if (DataUtil.safeEqual(changeProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.change.damaged.newSerial.state.not.new");
                } else {
                    errorChangeProductDTO.setError(getText("stock.validate.change.damaged.newSerial.state.not.new"));
                    lstError.add(errorChangeProductDTO);
                    continue;
                }
            }
            // set lai serial
            changeProductDTO.setNewSerial(stockTransSerialDTONew.getFromSerial());
            lstSuccess.add(changeProductDTO);
            if (isFoundInOldStockCard) {
                // insert du lieu
                if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.CARD)) {
                    stockCardService.insertStockCardFromSaled(productOfferingDTO.getProductOfferingId(), changeProductDTO.getOldSerial());
                }
            } else { // Kiem tra tren IM1 neu khong ton tai trong bang stock_card thi move tu stock_card_saled -> stock_card
                if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.CARD)) {
                    if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                            && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                            && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                        boolean isExistStockCardIm1 = stockCardService.checkStockCardIm1(productOfferingDTO.getProductOfferingId(), changeProductDTO.getOldSerial());
                        if (!isExistStockCardIm1) {
                            stockCardService.insertStockCardFromSaledIm1(productOfferingDTO.getProductOfferingId(), changeProductDTO.getOldSerial());
                        }
                    }
                }
            }
        }
        return lstSuccess;
    }

    private StockTransDTO getStockTransExport(Long staffId, Long reasionId, Date currentDate) {
        StockTransDTO stockTransExport = new StockTransDTO();
        stockTransExport.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransExport.setFromOwnerId(staffId);
        stockTransExport.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransExport.setToOwnerId(0L);
        stockTransExport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransExport.setReasonId(reasionId);
        stockTransExport.setCreateDatetime(currentDate);
        stockTransExport.setNote(getText("stock.change.damaged.stock.trans.note.export"));
        return stockTransExport;
    }

    private StockTransDTO getStockTransImport(Long staffId, Long reasionId, Date currentDate) {
        StockTransDTO stockTransImport = new StockTransDTO();
        stockTransImport.setFromOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransImport.setFromOwnerId(0L);
        stockTransImport.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransImport.setToOwnerId(staffId);
        stockTransImport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransImport.setReasonId(reasionId);
        stockTransImport.setCreateDatetime(currentDate);
        stockTransImport.setNote(getText("stock.change.damaged.stock.trans.note.import"));
        return stockTransImport;
    }

    private StockTransActionDTO getStockTransActionExpNote(StaffDTO staffDTO) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        Long stockTransActionExpId = getSequence(em, "STOCK_TRANS_ACTION_SEQ");
        stockTransActionExp.setActionCode("XDHH00" + stockTransActionExpId);
        stockTransActionExp.setStockTransActionId(stockTransActionExpId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionExp.setNote(getText("stock.change.damaged.stock.trans.note.export"));
        return stockTransActionExp;
    }

    private StockTransActionDTO getStockTransActionImpNote(StaffDTO staffDTO) throws Exception {
        StockTransActionDTO stockTransActionImp = new StockTransActionDTO();
        Long stockTransActionExpId = getSequence(em, "STOCK_TRANS_ACTION_SEQ");
        stockTransActionImp.setActionCode("NDHH00" + stockTransActionExpId);
        stockTransActionImp.setStockTransActionId(stockTransActionExpId);
        stockTransActionImp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionImp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionImp.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionImp.setNote(getText("stock.change.damaged.stock.trans.note.import"));
        return stockTransActionImp;
    }

    private List<StockTransDetailDTO> getStockTransDetail(ProductOfferTypeDTO productOfferTypeDTOs, ProductOfferingDTO productOfferingDTO, List<ChangeDamagedProductDTO> lstChangeProduct,
                                                          StaffDTO staffDTO, boolean isDamageProduct) throws Exception {
        // Lay thong tin gia
        Long prodOfferId = productOfferingDTO.getProductOfferingId();
        ShopDTO userShop = shopService.findOne(staffDTO.getShopId());
        Long amount = productOfferPriceService.getPriceAmount(prodOfferId, Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, DataUtil.safeToLong(userShop.getPricePolicy()));
        if (DataUtil.isNullOrZero(amount)) {
            amount = 0L;
        }
        Long quantity = DataUtil.safeToLong(lstChangeProduct.size());
        List<StockTransDetailDTO> stockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(prodOfferId);
        stockTransDetailDTO.setProdOfferTypeId(productOfferTypeDTOs.getProductOfferTypeId());
        if (isDamageProduct) {
            stockTransDetailDTO.setStateId(Const.STATE_DAMAGE);
        } else {
            stockTransDetailDTO.setStateId(Const.STATE_STATUS.NEW);
        }
        stockTransDetailDTO.setQuantity(quantity);
        stockTransDetailDTO.setPrice(amount);
        stockTransDetailDTO.setAmount(DataUtil.safeToLong(quantity) * DataUtil.safeToLong(amount));
        List<StockTransSerialDTO> stockTransSerial = Lists.newArrayList();
        for (ChangeDamagedProductDTO productDTO : lstChangeProduct) {
            // stock_trans_serial.
            StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
            stockTransSerialDTO.setProdOfferId(prodOfferId);
            if (isDamageProduct) {
                stockTransSerialDTO.setStateId(Const.STATE_DAMAGE);
                stockTransSerialDTO.setFromSerial(productDTO.getOldSerial());
                stockTransSerialDTO.setToSerial(productDTO.getOldSerial());
            } else {
                stockTransSerialDTO.setStateId(Const.STATE_STATUS.NEW);
                stockTransSerialDTO.setFromSerial(productDTO.getNewSerial());
                stockTransSerialDTO.setToSerial(productDTO.getNewSerial());
            }
            stockTransSerialDTO.setQuantity(1L);
            stockTransSerial.add(stockTransSerialDTO);
        }
        stockTransDetailDTO.setLstStockTransSerial(stockTransSerial);
        stockTransDetailDTO.setTableName(productOfferTypeDTOs.getTableName());
        stockTransDetail.add(stockTransDetailDTO);
        return stockTransDetail;
    }

    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO) throws Exception {
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        transActionDTO.setNote(stockTransDTO.getNote());
        //giao dich xuat hoac nhap khong set note va action_code
        if (Const.ConfigIDCheck.danh_sach_action_khong_can_actioncode.sValidate(transActionDTO.getStatus())) {
            transActionDTO.setNote(null);
            transActionDTO.setActionCode(null);
        }
        stockTransActionService.insertStockTransActionNative(transActionDTO);
        return stockTransActionDTO;
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

    @Transactional(rollbackFor = Exception.class)
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                 List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        // khong xu ly gi
        return stockTransActionDTO;
    }

    public String normalSerialCard(String serial) {
        Long lSerial = null;
        try {
            lSerial = Long.parseLong(serial);
            serial = String.valueOf(lSerial);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (serial != null && !"".equals(serial.trim())) {
            while (serial.length() < 11) {
                serial = "0" + serial;
            }
        }
        return serial;
    }
}

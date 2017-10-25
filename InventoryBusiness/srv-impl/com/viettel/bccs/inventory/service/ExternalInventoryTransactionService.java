package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockHandset;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockOrderRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.partner.dto.AccountAgentDTO;
import com.viettel.bccs.partner.service.AccountAgentService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExternalInventoryTransactionService extends BaseStockService {
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private ReportChangeHandsetService reportChangeHandsetService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockHandsetRepo stockHandsetRepo;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @Transactional(rollbackFor = Exception.class)
    public ChangeResultDTO saveChange2gTo3g(StaffDTO staffDTO, String serial2G, String serial3G, ProductOfferTypeDTO productOfferingType2G,
                                            ProductOfferTypeDTO productOfferingType3G, ProductOfferingDTO productOffering2G, ProductOfferingDTO productOffering3G,
                                            Long collStaffId, String custName, String custTel) throws LogicException, Exception {
        ChangeResultDTO result;

        Date currentDate = getSysDate(em);
        StockHandsetDTO stockHandset2G = new StockHandsetDTO();
        stockHandset2G.setOwnerId(staffDTO.getStaffId());
        stockHandset2G.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockHandset2G.setCreateDate(currentDate);
        stockHandset2G.setSerial(serial2G);
        stockHandset2G.setProdOfferId(productOffering2G.getProductOfferingId());
        stockHandset2G.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
        stockHandset2G.setStateId(Const.STATE_STATUS.NEW);
        StockHandsetDTO stockHandsetDTO = stockHandsetService.create(stockHandset2G);
        // Luu thong tin IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            stockHandsetRepo.insertStockHandset(stockHandsetDTO);
        }
        // </editor-fold>

        String strReasonCodeImp = getText("REASON_IMP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeImp)) {
            strReasonCodeImp = "NKH";
        }
        List<ReasonDTO> lstReasonImp = reasonService.getLsReasonByCode(strReasonCodeImp);
        if (DataUtil.isNullOrEmpty(lstReasonImp) || DataUtil.isNullObject(lstReasonImp.get(0))) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.reason.import.not.found");
        }
        ReasonDTO reasonImp = lstReasonImp.get(0);
        StockTransDTO stockTransImp = new StockTransDTO();
        stockTransImp.setFromOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransImp.setFromOwnerId(Const.OWNER_CUST_ID);
        stockTransImp.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransImp.setToOwnerId(staffDTO.getStaffId());
        stockTransImp.setCreateDatetime(currentDate);
        stockTransImp.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransImp.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransImp.setReasonId(reasonImp.getReasonId());
        stockTransImp.setNote(getText("stock.change.damaged.stock.trans.note.import"));
        StockTransDTO stockTransNewImp = doSaveStockTrans(stockTransImp);
        // Luu stock_trans_action
        StockTransActionDTO stockTransActionImpNote = getStockTransActionImpNote(staffDTO);
        StockTransActionDTO stockTransActionImp = DataUtil.cloneBean(stockTransActionImpNote);
        // Luu stock_trans_action
        stockTransActionImpNote = doSaveStockTransAction(stockTransNewImp, stockTransActionImpNote);
        stockTransActionImp.setStockTransActionId(null);
        stockTransActionImp.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        // Luu stock_trans_action
        doSaveStockTransAction(stockTransNewImp, stockTransActionImp);

        // Luu stock_trans_detail
        List<StockTransDetailDTO> stockTransDetailImp = getStockTransDetail(productOfferingType2G, productOffering2G, serial2G, staffDTO, Const.STATE_STATUS.NEW);
        doSaveStockTransDetail(stockTransNewImp, stockTransDetailImp);
        // Luu stock_trans_serial
        doSaveStockTransSerial(stockTransNewImp, stockTransDetailImp);

        FlagStockDTO flagStockImp = new FlagStockDTO();
        flagStockImp.setImportStock(true);
        flagStockImp.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockImp.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        doSaveStockTotal(stockTransNewImp, stockTransDetailImp, flagStockImp, stockTransActionImpNote);
        // </editor-fold>

        String strReasonCodeExp = getText("REASON_EXP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeExp)) {
            strReasonCodeExp = "XKH";
        }
        List<ReasonDTO> lstReasonExp = reasonService.getLsReasonByCode(strReasonCodeExp);
        if (DataUtil.isNullOrEmpty(lstReasonExp) || DataUtil.isNullObject(lstReasonExp.get(0))) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.reason.import.not.found");
        }
        ReasonDTO reasonExp = lstReasonExp.get(0);
        StockTransDTO stockTransExp = new StockTransDTO();
        stockTransExp.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransExp.setToOwnerId(Const.OWNER_CUST_ID);
        stockTransExp.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransExp.setFromOwnerId(staffDTO.getStaffId());
        stockTransExp.setCreateDatetime(currentDate);
        stockTransExp.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransExp.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransExp.setFromStockTransId(stockTransNewImp.getStockTransId());
        stockTransExp.setReasonId(reasonExp.getReasonId());
        stockTransExp.setNote(getText("change2gTo3g.stockTrans.note"));
        StockTransDTO stockTransExpNew = doSaveStockTrans(stockTransExp);

        StockTransActionDTO stockTransActionExpNote = getStockTransActionExpNote(staffDTO);
        StockTransActionDTO stockTransActionExp = DataUtil.cloneBean(stockTransActionExpNote);
        StockTransActionDTO stockTransActionExped = DataUtil.cloneBean(stockTransActionExpNote);
        // Luu stock_trans_action
        stockTransActionExpNote = doSaveStockTransAction(stockTransExpNew, stockTransActionExpNote);
        stockTransActionExp.setStockTransActionId(null);
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        // Luu stock_trans_action
        doSaveStockTransAction(stockTransExpNew, stockTransActionExp);

        stockTransActionExped.setStockTransActionId(null);
        stockTransActionExped.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        // Luu stock_trans_action
        doSaveStockTransAction(stockTransExpNew, stockTransActionExped);

        // Luu stock_trans_detail
        List<StockTransDetailDTO> stockTransDetail = getStockTransDetail(productOfferingType3G, productOffering3G, serial3G, staffDTO, Const.STATE_STATUS.NEW);
        List<StockTransDetailDTO> stockTransDetailExp = DataUtil.cloneBean(stockTransDetail);
        doSaveStockTransDetail(stockTransExpNew, stockTransDetailExp);
        // Luu stock_trans_serial
        doSaveStockTransSerial(stockTransExpNew, stockTransDetailExp);
        // Cap nhat trang thai hang moi ve da su dung
        FlagStockDTO flagStockExp = new FlagStockDTO();
        flagStockExp.setNewStatus(Const.STATE_STATUS.SALE);
        flagStockExp.setOldStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
        flagStockExp.setUpdateSaleDate(true);
        doSaveStockGoods(stockTransExpNew, stockTransDetailExp, flagStockExp);
        // Tru kho
        flagStockExp.setExportStock(true);
        flagStockExp.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockExp.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        doSaveStockTotal(stockTransExpNew, stockTransDetailExp, flagStockExp, stockTransActionExpNote);
        // </editor-fold>

        // Chuan hoa lai cust_name, cust_tel
        if (!DataUtil.isNullOrEmpty(custName) && custName.getBytes("UTF-8").length > 300) {
            custName = custName.substring(0, 300);
        }
        if (!DataUtil.isNullOrEmpty(custTel) && custTel.getBytes("UTF-8").length > 40) {
            custTel = custTel.substring(0, 300);
        }

        ReportChangeHandsetDTO reportChangeHandsetDTO = new ReportChangeHandsetDTO();
        Long reportChangeHandsetId = getSequence(em, "report_change_handset_seq");
        reportChangeHandsetDTO.setReportChangeHandsetId(reportChangeHandsetId);
        reportChangeHandsetDTO.setStockTransId(stockTransNewImp.getStockTransId());
        reportChangeHandsetDTO.setShopId(staffDTO.getShopId());
        reportChangeHandsetDTO.setStaffId(staffDTO.getStaffId());
        reportChangeHandsetDTO.setCreateDate(currentDate);
        reportChangeHandsetDTO.setProdOfferIdOld(productOffering2G.getProductOfferingId());
        reportChangeHandsetDTO.setProdOfferIdNew(productOffering3G.getProductOfferingId());
        reportChangeHandsetDTO.setSerialNew(serial3G);
        reportChangeHandsetDTO.setSerialOld(serial2G);
        reportChangeHandsetDTO.setChangeType(Const.TYPE_OF_CHANGE_2G_TO_3G);
        reportChangeHandsetDTO.setCustName(custName);
        reportChangeHandsetDTO.setCustTel(custTel);
        if (!DataUtil.isNullOrZero(collStaffId)) {
            reportChangeHandsetDTO.setDevStaffId(collStaffId);
        }
        reportChangeHandsetService.create(reportChangeHandsetDTO);
        result = new ChangeResultDTO(stockTransNewImp.getStockTransId(), stockTransExpNew.getStockTransId(), true, "", "change2gTo3g.change.success");
        // </editor-fold>
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage cancelChange2gTo3g(StockTransDTO stockTrans2GDTO, StockTransDTO stockTrans3GDTO) throws LogicException, Exception {
        BaseMessage result ;
        Date currentDate = getSysDate(em);
        //huy giao dich nhap 2G
        stockTrans2GDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        stockTransService.save(stockTrans2GDTO);
        //Them moi stock_trans_action = 7
        StockTransActionDTO stockTransActionCancel = new StockTransActionDTO();
        stockTransActionCancel.setStockTransId(stockTrans2GDTO.getStockTransId());
        stockTransActionCancel.setCreateDatetime(currentDate);
        stockTransActionCancel.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        stockTransActionService.save(stockTransActionCancel);
        //Lay thong tin giao dich
        StockTransActionDTO stockTransActionNoteDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTrans2GDTO.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.IMPORT_NOTE));
        List<StockTransDetailDTO> lstStockTransDetailDTOs = stockTransDetailService.findByStockTransId(stockTrans2GDTO.getStockTransId());
        if (DataUtil.isNullOrEmpty(lstStockTransDetailDTOs)) {
            throw new LogicException("", "cancel.change.2g.3g.detail.2g.not.found");
        }
        List<StockTransSerialDTO> lstStockTransSerialDTOs = stockTransSerialService.findByStockTransDetailId(lstStockTransDetailDTOs.get(0).getStockTransDetailId());
        if (DataUtil.isNullOrEmpty(lstStockTransSerialDTOs)) {
            throw new LogicException("", "cancel.change.2g.3g.serial.2g.not.found");
        }
        StockTransSerialDTO stockTransSerial2GDTO = lstStockTransSerialDTOs.get(0);
        //Xoa ban ghi trong stock_handset
        stockHandsetRepo.deleteStockHandset(stockTransSerial2GDTO.getProdOfferId(), stockTransSerial2GDTO.getFromSerial(),
                Const.OWNER_TYPE.STAFF_LONG, stockTrans2GDTO.getToOwnerId(), DataUtil.safeToLong(Const.STATUS_ACTIVE), Const.STATE_STATUS.NEW);
        // Xoa thong tin IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            stockHandsetRepo.deleteStockHandsetIM1(stockTransSerial2GDTO.getProdOfferId(), stockTransSerial2GDTO.getFromSerial(),
                    Const.OWNER_TYPE.STAFF_LONG, stockTrans2GDTO.getToOwnerId(), DataUtil.safeToLong(Const.STATUS_ACTIVE), Const.STATE_STATUS.NEW);
        }
        //Tru Stock_total
        FlagStockDTO flagStockImp = new FlagStockDTO();
        flagStockImp.setImportStock(true);
        flagStockImp.setImpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockImp.setImpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        doSaveStockTotal(stockTrans2GDTO, lstStockTransDetailDTOs, flagStockImp, stockTransActionNoteDTO);
        //Giao dich xuat 3G
        //Huy giao dich 3g
        stockTrans3GDTO.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        stockTransService.save(stockTrans3GDTO);
        //Them moi stock_trans_action = 7
        stockTransActionCancel = new StockTransActionDTO();
        stockTransActionCancel.setStockTransId(stockTrans3GDTO.getStockTransId());
        stockTransActionCancel.setCreateDatetime(currentDate);
        stockTransActionCancel.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
        stockTransActionService.save(stockTransActionCancel);

        lstStockTransDetailDTOs = stockTransDetailService.findByStockTransId(stockTrans3GDTO.getStockTransId());
        if (DataUtil.isNullOrEmpty(lstStockTransDetailDTOs)) {
            throw new LogicException("", "cancel.change.2g.3g.detail.3g.not.found");
        }
        lstStockTransSerialDTOs = stockTransSerialService.findByStockTransDetailId(lstStockTransDetailDTOs.get(0).getStockTransDetailId());
        if (DataUtil.isNullOrEmpty(lstStockTransSerialDTOs)) {
            throw new LogicException("", "cancel.change.2g.3g.serial.3g.not.found");
        }
        List<StockTransDetailDTO> lstDetailExp = getLstStockTransDetail(lstStockTransDetailDTOs.get(0), lstStockTransSerialDTOs);
        StockTransActionDTO stockTransActionExpDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTrans3GDTO.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        // Cap nhat trang thai da su dung ve hang moi
        FlagStockDTO flagStockExp = new FlagStockDTO();
        flagStockExp.setNewStatus(Const.STATE_STATUS.NEW);
        flagStockExp.setOldStatus(DataUtil.safeToLong(Const.STATUS_INACTIVE));
        flagStockExp.setUpdateSaleDate(true);
        doSaveStockGoods(stockTrans3GDTO, lstDetailExp, flagStockExp);
        // Cong kho
        flagStockExp.setExportStock(true);
        flagStockExp.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockExp.setExpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        doSaveStockTotal(stockTrans3GDTO, lstStockTransDetailDTOs, flagStockExp, stockTransActionExpDTO);
        //xoa ban ghi report_change_handset

        result = new BaseMessage(true, "", getText("cancel.change.2g.3g.success"));
        return result;
    }

    private List<StockTransDetailDTO> getLstStockTransDetail(StockTransDetailDTO stockTransDetailDTO, List<StockTransSerialDTO> lstStockTransSerialDTOs) throws Exception {
        List<StockTransDetailDTO> lst = Lists.newArrayList();
        stockTransDetailDTO.setLstStockTransSerial(lstStockTransSerialDTOs);
        ProductOfferTypeDTO productOfferingType3G = productOfferTypeService.findOne(Const.PRODUCT_OFFER_TYPE.PHONE);
        stockTransDetailDTO.setTableName(productOfferingType3G.getTableName());
        stockTransDetailDTO.setProdOfferTypeId(Const.PRODUCT_OFFER_TYPE.PHONE);
        lst.add(stockTransDetailDTO);
        return lst;
    }

    private StockTransActionDTO getStockTransActionImpNote(StaffDTO staffDTO) throws Exception {
        StockTransActionDTO stockTransActionImp = new StockTransActionDTO();
        Long stockTransActionImpId = getSequence(em, "STOCK_TRANS_ACTION_SEQ");
        stockTransActionImp.setActionCode("NDHH00" + stockTransActionImpId);
        stockTransActionImp.setStockTransActionId(stockTransActionImpId);
        stockTransActionImp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionImp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionImp.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionImp.setNote(getText("title.change2GTo3G.page"));
        return stockTransActionImp;
    }

    private StockTransActionDTO getStockTransActionExpNote(StaffDTO staffDTO) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        Long stockTransActionExpId = getSequence(em, "STOCK_TRANS_ACTION_SEQ");
        stockTransActionExp.setActionCode("XDHH00" + stockTransActionExpId);
        stockTransActionExp.setStockTransActionId(stockTransActionExpId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionExp.setNote(getText("title.change2GTo3G.page"));
        return stockTransActionExp;
    }

    private List<StockTransDetailDTO> getStockTransDetail(ProductOfferTypeDTO productOfferTypeDTOs, ProductOfferingDTO productOfferingDTO, String serial,
                                                          StaffDTO staffDTO, Long stateId) throws Exception {
        // Lay thong tin gia
        Long prodOfferId = productOfferingDTO.getProductOfferingId();
        ShopDTO userShop = shopService.findOne(staffDTO.getShopId());
        Long amount = productOfferPriceService.getPriceAmount(prodOfferId, Const.PRODUCT_OFFERING.PRICE_TYPE_ROOT, DataUtil.safeToLong(userShop.getPricePolicy()));
        if (DataUtil.isNullOrZero(amount)) {
            amount = 0L;
        }
        List<StockTransDetailDTO> stockTransDetail = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(prodOfferId);
        stockTransDetailDTO.setProdOfferTypeId(productOfferTypeDTOs.getProductOfferTypeId());
        stockTransDetailDTO.setStateId(stateId);
        stockTransDetailDTO.setQuantity(1L);
        stockTransDetailDTO.setPrice(amount);
        stockTransDetailDTO.setAmount(1L * DataUtil.safeToLong(amount));
        List<StockTransSerialDTO> stockTransSerial = Lists.newArrayList();
        // stock_trans_serial.
        StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
        stockTransSerialDTO.setProdOfferId(prodOfferId);
        stockTransSerialDTO.setStateId(stateId);
        stockTransSerialDTO.setFromSerial(serial);
        stockTransSerialDTO.setToSerial(serial);
        stockTransSerialDTO.setQuantity(1L);
        stockTransSerial.add(stockTransSerialDTO);

        stockTransDetailDTO.setLstStockTransSerial(stockTransSerial);
        stockTransDetailDTO.setTableName(productOfferTypeDTOs.getTableName());
        stockTransDetail.add(stockTransDetailDTO);
        return stockTransDetail;
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}

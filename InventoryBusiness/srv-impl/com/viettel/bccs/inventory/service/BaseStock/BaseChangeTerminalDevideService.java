package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by thanhnt77
 * lap lenh , lap phieu xuat tu dong
 */

@Service
public class BaseChangeTerminalDevideService extends BaseStockService {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private ReportChangeHandsetService reportChangeHandsetService;
    @Autowired
    private ProductWs productWs;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferingRepo productOfferingRepo;
    @Autowired
    private SaleWs saleWs;
    @Autowired
    private OptionSetValueRepo optionSetValueRepo;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {

//        boolean isSameModel = stockTransDTO.getSourceType().compareTo(2L) == 0;// 2 la doi cung model, 1 la doi khac model

        //1. valdiate + lay ve danh sach mat hang
        List<ReportChangeHandsetDTO> lstFindSaleTrans = reportChangeHandsetService.getLsChangeHandsetTerminalDevide(stockTransDTO, stockTransActionDTO, lstStockTransDetail);

        if (DataUtil.isNullOrEmpty(lstFindSaleTrans)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.sale.change.terminal.changeTerminalDevice.notFound");
        }
        //Gia ban model cu
        Long priceSaled = lstFindSaleTrans.get(0).getPriceSaled();

        if (priceSaled == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.ModelOldNotPrice");
        }

        StaffDTO staffDTO = staffService.findOne(stockTransActionDTO.getActionStaffId());

        StockTransDetailDTO damageDetailDTO = lstStockTransDetail.get(0);
        StockTransDetailDTO changeDetailDTO = lstStockTransDetail.get(1);

        StockTransSerialDTO damageSerialDTO = damageDetailDTO.getLstSerial().get(0);
        StockTransSerialDTO changeSerialDTO = changeDetailDTO.getLstSerial().get(0);

        Long damageProdOfferId = damageDetailDTO.getProdOfferId();//id mat hang hong
        Long changeProdOfferId = changeDetailDTO.getProdOfferId();//id mat hang doi lai

        String damageSerial = damageSerialDTO.getFromSerial();
        String changeSerial = changeSerialDTO.getFromSerial();

        Date sysdate = getSysDate(em);

        //3.Luu giao dich xuat
        //3.1 luu stock_trans
        StockTransDTO stockTransExport = new StockTransDTO();
        stockTransExport.setFromOwnerId(stockTransActionDTO.getActionStaffId());
        stockTransExport.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransExport.setToOwnerId(null);
        stockTransExport.setToOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransExport.setCreateDatetime(sysdate);
        stockTransExport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransExport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        String strReasonCodeExp = getText("REASON_EXP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeExp)) {
            strReasonCodeExp = "XKH";
        }

        List<ReasonDTO> lsReasonExpDTO = reasonService.getLsReasonByCode(strReasonCodeExp);
        if (DataUtil.isNullOrEmpty(lsReasonExpDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.validate.stock.reason.export.not.found");
        }
        stockTransExport.setReasonId(lsReasonExpDTO.get(0).getReasonId());
        stockTransExport.setNote(getText("stock.sale.change.terminal.price.change.damage.product.note.exp"));
        StockTransDTO stockTransSaveExport = stockTransService.save(stockTransExport);
        //3.2 luu stock_trans_action
        //tao moi action status = 2
        String actionCode = "XDHH00" + stockTransSaveExport.getStockTransId();
        StockTransActionDTO actionExport1 = new StockTransActionDTO();
        actionExport1.setActionCode(actionCode);
        actionExport1.setStockTransId(stockTransSaveExport.getStockTransId());
        actionExport1.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        actionExport1.setCreateUser(staffDTO.getStaffCode());
        actionExport1.setCreateDatetime(sysdate);
        actionExport1.setActionStaffId(staffDTO.getStaffId());
        actionExport1.setNote(getText("stock.sale.change.terminal.price.change.damage.product.note.exp"));
        StockTransActionDTO actionExportSave = stockTransActionService.save(actionExport1);

        //tao moi action status = 3
        StockTransActionDTO actionExport2 = DataUtil.cloneBean(actionExport1);
        actionExport2.setActionCode(null);
        actionExport2.setNote(null);
        actionExport2.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionService.save(actionExport2);

        //tao moi action status = 6
        StockTransActionDTO actionExport3 = DataUtil.cloneBean(actionExport2);
        actionExport3.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(actionExport3);

        //3.3 luu stock_trans_detail
        StockTransDetailDTO detailExport = new StockTransDetailDTO();
        detailExport.setStockTransId(stockTransSaveExport.getStockTransId());
        detailExport.setProdOfferId(changeProdOfferId);
        detailExport.setStateId(Const.GOODS_STATE.NEW);
        detailExport.setQuantity(1L);
        detailExport.setPrice(null);
        detailExport.setAmount(null);
        detailExport.setCreateDatetime(sysdate);
        StockTransDetailDTO detailSaveExport = stockTransDetailService.save(detailExport);

        detailSaveExport.setTableName(changeDetailDTO.getTableName());
        detailSaveExport.setProdOfferTypeId(changeDetailDTO.getProdOfferTypeId());

        //3.4 luu stock_trans_serial
        StockTransSerialDTO serialExport = new StockTransSerialDTO();
        serialExport.setFromSerial(changeSerial);
        serialExport.setToSerial(changeSerial);
        serialExport.setQuantity(1L);
        serialExport.setStateId(Const.GOODS_STATE.NEW);
        serialExport.setStockTransId(stockTransSaveExport.getStockTransId());
        serialExport.setCreateDatetime(sysdate);
        serialExport.setProdOfferId(changeProdOfferId);
        serialExport.setStockTransDetailId(detailSaveExport.getStockTransDetailId());
        stockTransSerialService.save(serialExport);

        //3.5 thuc  hien tru kho xuat
        //3.5.1 check so luong dap ung kho xuat
        StockTotalDTO stockTotalDTOExport = getStockTotalDTOByOwnerId(staffDTO.getStaffId(), Const.OWNER_TYPE.STAFF_LONG, changeProdOfferId, Const.GOODS_STATE.NEW);

        Long quantityExport = changeDetailDTO.getQuantity();
        if (stockTotalDTOExport.getAvailableQuantity().compareTo(quantityExport) < 0
                || stockTotalDTOExport.getCurrentQuantity().compareTo(quantityExport) < 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.price.ModelIsNotAvailableInStockOwner", quantityExport);
        }

        FlagStockDTO flagStockExp = new FlagStockDTO();
        flagStockExp.setExportStock(true);
        flagStockExp.setInsertStockTotalAudit(true);
        flagStockExp.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);//-1
        flagStockExp.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);//-1
        //flagStockExp.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
        //3.5.2 tru kho xuat stock_total
        doSaveStockTotal(stockTransSaveExport, Lists.newArrayList(detailSaveExport), flagStockExp, actionExportSave);

        //4.Luu giao dich nhap
        //4.1 luu stock_trans
        StockTransDTO stockTransImport = new StockTransDTO();
        stockTransImport.setFromOwnerId(null);
        stockTransImport.setFromOwnerType(Const.OWNER_TYPE.CUSTOMER_LONG);
        stockTransImport.setToOwnerId(stockTransActionDTO.getActionStaffId());
        stockTransImport.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTransImport.setCreateDatetime(sysdate);
        stockTransImport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransImport.setFromStockTransId(stockTransSaveExport.getStockTransId());

        String strReasonCodeImp = getText("REASON_IMP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeImp)) {
            strReasonCodeImp = "NKH";
        }

        List<ReasonDTO> lsReasonImpDTO = reasonService.getLsReasonByCode(strReasonCodeImp);
        if (DataUtil.isNullOrEmpty(lsReasonImpDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.validate.stock.reason.import.not.found");
        }

        stockTransImport.setReasonId(lsReasonImpDTO.get(0).getReasonId());
        stockTransImport.setNote(getText("stock.sale.change.terminal.price.change.damage.product.note.imp"));
        StockTransDTO stockTransSaveImport = stockTransService.save(stockTransImport);
        //4.2 luu stock_trans_action
        //tao moi action status =5
        String actionCodeImport = "NDHH00" + stockTransSaveImport.getStockTransId();
        StockTransActionDTO actionImport1 = new StockTransActionDTO();
        actionImport1.setActionCode(actionCodeImport);
        actionImport1.setStockTransId(stockTransSaveImport.getStockTransId());
        actionImport1.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        actionImport1.setCreateUser(staffDTO.getStaffCode());
        actionImport1.setCreateDatetime(sysdate);
        actionImport1.setActionStaffId(staffDTO.getStaffId());
        actionImport1.setNote(getText("stock.sale.change.terminal.price.change.damage.product.note.imp"));
        StockTransActionDTO actionImportSave = stockTransActionService.save(actionImport1);

        //tao moi action status = 6
        StockTransActionDTO actionImport3 = DataUtil.cloneBean(actionImport1);
        actionImport3.setNote(null);
        actionImport3.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(actionImport3);

        //4.3 luu stock_trans_detail
        StockTransDetailDTO detailImport = new StockTransDetailDTO();
        detailImport.setStockTransId(stockTransSaveImport.getStockTransId());
        detailImport.setProdOfferId(damageProdOfferId);
        detailImport.setStateId(Const.GOODS_STATE.BROKEN_15_DAY_CUSTOMER);
        detailImport.setQuantity(1L);
        detailImport.setPrice(null);
        detailImport.setAmount(null);
        detailImport.setCreateDatetime(sysdate);
        StockTransDetailDTO detailSaveImport = stockTransDetailService.save(detailImport);

        detailSaveImport.setTableName(damageDetailDTO.getTableName());
        detailSaveImport.setProdOfferTypeId(damageDetailDTO.getProdOfferTypeId());

        //4.4 luu stock_trans_serial
        StockTransSerialDTO serialImport = new StockTransSerialDTO();
        serialImport.setFromSerial(damageSerial);
        serialImport.setToSerial(damageSerial);
        serialImport.setQuantity(1L);
        serialImport.setStateId(Const.GOODS_STATE.BROKEN_15_DAY_CUSTOMER);
        serialImport.setStockTransId(stockTransSaveImport.getStockTransId());
        serialImport.setCreateDatetime(sysdate);
        serialImport.setProdOfferId(damageProdOfferId);
        serialImport.setStockTransDetailId(detailSaveImport.getStockTransDetailId());
        stockTransSerialService.save(serialImport);

        //4.5 cong kho nhap stock_total
        FlagStockDTO flagStockImport = new FlagStockDTO();
        flagStockImport.setImportStock(true);
        flagStockImport.setInsertStockTotalAudit(true);
        flagStockImport.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockImport.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

        doSaveStockTotal(stockTransSaveImport, Lists.newArrayList(detailSaveImport), flagStockImport, actionImportSave);

        //5. thuc hien update stock_x
        //5.1 nhap serial hang hong ve kho nhan vien
        FlagStockDTO flagStockGood = new FlagStockDTO();
        flagStockGood.setStateIdForReasonId(Const.GOODS_STATE.BROKEN_15_DAY_CUSTOMER);
        flagStockGood.setUpdateOwnerId(true);
        flagStockGood.setOldStatus(Const.STATE_STATUS.SALE);
        flagStockGood.setNewStatus(Const.STATE_STATUS.NEW);
        flagStockGood.setUpdateDamageProduct(true);
        //chuyen trang thai cua hang doi từ hàng moi => hang hong, da ban => moi
        doSaveStockGoods(stockTransSaveImport, Lists.newArrayList(detailSaveImport), flagStockGood);
        //5.2 nhap serial, chuyen trang thai serial chuyen doi tu,   moi => da ban
        flagStockGood = new FlagStockDTO();
        flagStockGood.setUpdateSaleDate(true);
        flagStockGood.setOldStatus(Const.STATE_STATUS.NEW);
        flagStockGood.setNewStatus(Const.STATE_STATUS.SALE);
        flagStockGood.setUpdateDamageProduct(false);
        doSaveStockGoods(stockTransSaveExport, Lists.newArrayList(detailSaveExport), flagStockGood);

        Long ajustAmount = 0L;
        //5.2. them data vao bang bao cao thay doi thiet bi dau cuoi
        //them vao bang REPORT_CHANGE_HANDSET
        ReportChangeHandsetDTO reportChangeHandset = new ReportChangeHandsetDTO();
        reportChangeHandset.setStockTransId(stockTransSaveExport.getStockTransId());
        reportChangeHandset.setShopId(staffDTO.getShopId());
        reportChangeHandset.setStaffId(staffDTO.getStaffId());
        reportChangeHandset.setCreateDate(sysdate);
        reportChangeHandset.setProdOfferIdOld(damageProdOfferId);
        reportChangeHandset.setProdOfferIdNew(changeProdOfferId);
        reportChangeHandset.setSerialNew(changeSerial);
        reportChangeHandset.setSerialOld(damageSerial);
        reportChangeHandset.setDamageGoodStatus(stockTransDTO.getNote());
        reportChangeHandset.setChangeType(1L);


        //6. Giao dich dieu chinh doanh thu ban hang
        //neu la giao dich doi khac model, gia tien chenh lech thi moi thuc hien giao dich
        int numberDate = Const.AMOUNT_DAY_TO_CHANGE_HANDSET_DEFAULT;//so ngay duoc phep doi thiet bi 3
        String strNumberDate = optionSetValueRepo.getValueByOptionSetIdAndCode(Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET, Const.OPTION_SET.AMOUNT_DAY_TO_CHANGE_HANDSET);
        if (!DataUtil.isNullOrEmpty(strNumberDate)) {
            numberDate = DataUtil.safeToInt(strNumberDate);
        }

        //R765639: Bo sung tham so xac dinh co sinh GD dieu chinh sang Sale
        boolean isSaveSaleTransForChangeGood = true;

        //Thong tin mat hang hong
        ProductOfferingDTO damageProductOfferingDTO = productOfferingService.findOne(damageProdOfferId);

        //Thong tin mat hang doi lai
        ProductOfferingDTO changeProductOfferingDTO = productOfferingService.findOne(changeProdOfferId);

        //Neu 2 mat hang cung prodOfferId
        if (DataUtil.safeEqual(damageProductOfferingDTO.getProductOfferingId(), changeProductOfferingDTO.getProductOfferingId())) {

            //Khong sinh giao dich tang/giam doanh thu
            isSaveSaleTransForChangeGood = false;
            //Nguoc lai
        } else {

            //Neu cung ma hang hach toan
            if (DataUtil.safeEqual(damageProductOfferingDTO.getAccountingModelCode(), changeProductOfferingDTO.getAccountingModelCode())) {

                //Khong sinh giao dich tang/giam doanh thu
                isSaveSaleTransForChangeGood = false;
            } else {

                //Lay thong tin dinh dang ma hang truoc dau gach duoi ("_") cuoi cung
                //Thong tin mat hang hong
                String damageProdOfferCodePrefix = productOfferingRepo.getStockModelPrefixById(damageProdOfferId);

                //Thong tin mat hang doi lai
                String changeProdOfferCodePrefix = productOfferingRepo.getStockModelPrefixById(changeProdOfferId);

                //Neu ma mat hang trung dinh dang truoc dau gach duoi cuoi cung
                if (DataUtil.safeEqual(damageProdOfferCodePrefix, changeProdOfferCodePrefix)) {

                    //Khong sinh giao dich tang/giam doanh thu
                    isSaveSaleTransForChangeGood = false;
                }
            }
        }

        //Neu sinh giao dich dieu chinh tang/giam tren Sale
        if (isSaveSaleTransForChangeGood) {
            ChangeGoodMessage message = saleWs.changeGood(damageProdOfferId, damageSerial, changeProdOfferId, changeSerial, numberDate, staffDTO.getStaffId(), stockTransActionDTO.getFromDate(), stockTransActionDTO.getToDate());
            if (message == null) {
                throw new LogicException(ErrorCode.ERROR_CONNNECT.ERROR_SYSTEM_SALE, "error.connect.sale.service");
            }
            if (!"0".equals(message.getResponseCode())) {
                LogicException ex = new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "");
                ex.setDescription(message.getDescription());
                throw ex;
            }
            ajustAmount = message.getAdjustAmount();
        }
        reportChangeHandset.setAdjustAmount(DataUtil.safeToLong(ajustAmount));
        ReportChangeHandsetDTO reportChangeHandsetDTOSave = reportChangeHandsetService.create(reportChangeHandset);
        actionExportSave.setStockTransActionId(reportChangeHandsetDTOSave.getReportChangeHandsetId());
        return actionExportSave;
    }

    //get Sale_Trans_Code from Sale_Trans_Id
    private static String formatTransCode(Long transId) {
        return "GD0000" + String.format("%09d", transId);
    }

    /**
     * ham tra ve tong so luong dap ung
     *
     * @param ownerId
     * @param ownerType
     * @param prodOfferId
     * @param stateId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    private StockTotalDTO getStockTotalDTOByOwnerId(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws LogicException, Exception {
        StockTotalDTO stockTotalDTOs = stockTotalService.getStockTotalForProcessStock(ownerId, ownerType, prodOfferId, stateId);
        String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}",
                ownerId, ownerType, prodOfferId, stateId);
        if (stockTotalDTOs == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "sell.store.no.info", params);
        }
        if (DataUtil.safeToLong(stockTotalDTOs.getAvailableQuantity()).compareTo(0L) <= 0 || DataUtil.safeToLong(stockTotalDTOs.getCurrentQuantity()).compareTo(0L) <= 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.sale.change.terminal.not.enough.currentQuantity");
        }
        return stockTotalDTOs;

    }


}

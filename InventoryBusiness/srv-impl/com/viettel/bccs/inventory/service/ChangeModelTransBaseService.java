package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 8/26/2016.
 */
@Service
public class ChangeModelTransBaseService extends BaseStockService {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ChangeModelTransService changeModelTransService;
    @Autowired
    private ChangeModelTransDetailService changeModelTransDetailService;
    @Autowired
    private ChangeModelTransSerialService changeModelTransSerialService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ActiveKitVofficeService activeKitVofficeService;
    @Autowired
    private SignFlowDetailService signFlowDetailService;


    public void approveChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO) || !DataUtil.safeEqual(changeModelTransDTO.getStatus(), Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_NEW)) {
            throw new LogicException("", "mn.stock.change.product.offering.approve.not.found.transaction");
        }
        StaffDTO createStaff = staffService.findOne(changeModelTransDTO.getCreateUserId());

        //Lay thong tin giao dich stock_Trans

        StockTransDTO originalStockTrans = stockTransService.findOne(changeModelTransDTO.getStockTransId());
        if (DataUtil.isNullObject(originalStockTrans)) {
            throw new LogicException("", "utilities.exchange.card.bankplus.not.transaction");
        }
        //Kiem tra da ky voffice chua
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(originalStockTrans.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionDTO)
                || DataUtil.isNullOrZero(stockTransActionDTO.getStockTransActionId())) {
            throw new LogicException("", "utilities.exchange.card.bankplus.not.transaction");
        }
        stockTransVofficeService.doSignedVofficeValidate(stockTransActionDTO);
        //Lay cac giao dich chi tiet
        List<StockTransDetailDTO> lstDetail = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());

        Date currentDate = getSysDate(em);
        String listStockTransId = "";
        //Them moi cac giao dich phat sinh
        if (!DataUtil.safeEqual(changeModelTransDTO.getFromOwnerId(), Const.L_VT_SHOP_ID)) {
            // B1: VT nhap hang tu chi nhanh
            originalStockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransService.save(originalStockTrans);
            listStockTransId += (originalStockTrans.getStockTransId() + ",");
            //Them moi stock_trans_action voi status 5,6
            StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(stockTransActionDTO);
            stockTransActionNew.setStockTransActionId(null);
            stockTransActionNew.setCreateDatetime(currentDate);
            stockTransActionNew.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionNew.setSignCaStatus(Const.SIGN_VOFFICE);
            stockTransActionNew.setActionStaffId(approveStaffDTO.getStaffId());
            stockTransActionNew.setCreateUser(approveStaffDTO.getStaffCode());
            stockTransActionNew.setActionCodeShop(null);
            StockTransActionDTO stockTransActionNewSave = stockTransActionService.save(stockTransActionNew);
            stockTransActionNew.setSignCaStatus(null);
            stockTransActionNew.setActionCode(null);
            stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionNew);
            //Tang so phieu nhap
            doIncreaseStockNum(stockTransActionNewSave, Const.STOCK_TRANS.TRANS_CODE_PN, requiredRoleMap);
            // ky voffice
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            originalStockTrans.setSignVoffice(stockTransVoficeDTO.isSignVoffice());
            originalStockTrans.setUserName(stockTransVoficeDTO.getUserName());
            originalStockTrans.setPassWord(stockTransVoficeDTO.getPassWord());
            originalStockTrans.setSignFlowId(stockTransVoficeDTO.getSignFlowId());
            doSignVoffice(originalStockTrans, stockTransActionNewSave, requiredRoleMap, flagStockDTO);
            // B2: Tao giao dich VT xuat tra doi tac
            StockTransDTO transVTExpToPartner = getStockTransVT(stockTransVoficeDTO, Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG,
                    Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG, originalStockTrans.getReasonId());
            transVTExpToPartner.setCreateDatetime(currentDate);
            StockTransDTO transVTExpToPartnerSave = stockTransService.save(transVTExpToPartner);
            listStockTransId += (transVTExpToPartnerSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVTExpToPartner = getStockTransActionVT(approveStaffDTO, currentDate, transVTExpToPartnerSave.getStockTransId());
            stockTransActionVTExpToPartner.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, approveStaffDTO));
            stockTransActionVTExpToPartner.setSignCaStatus(Const.SIGN_VOFFICE);
            StockTransActionDTO stockTransActionVTExpToPartnerSave = stockTransActionService.save(stockTransActionVTExpToPartner);
            stockTransActionVTExpToPartner.setSignCaStatus(null);
            stockTransActionVTExpToPartner.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionVTExpToPartner.setActionCode(null);
            stockTransActionService.save(stockTransActionVTExpToPartner);
            stockTransActionVTExpToPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionVTExpToPartner);
            //stock_trans_detail va serial
            saveStockTransDetailAndSerial(lstDetail, transVTExpToPartnerSave.getStockTransId(), currentDate, null);
            //Tang so phieu xuat
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
            doIncreaseStockNum(stockTransActionVTExpToPartnerSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            // ky voffice
            doSignVoffice(transVTExpToPartner, stockTransActionVTExpToPartnerSave, requiredRoleMap, flagStockDTO);

            // B3: Tao giao dich VT nhap hang tu doi tac
            StockTransDTO transVtImpFromPartner = getStockTransVT(stockTransVoficeDTO, Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG,
                    Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG, originalStockTrans.getReasonId());
            transVtImpFromPartner.setCreateDatetime(currentDate);
            StockTransDTO transVtImpFromPartnerSave = stockTransService.save(transVtImpFromPartner);
            listStockTransId += (transVtImpFromPartnerSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVtImpFromPartner = getStockTransActionVT(approveStaffDTO, currentDate, transVtImpFromPartnerSave.getStockTransId());
            stockTransActionVtImpFromPartner.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
//            stockTransActionVtImpFromPartner.setSignCaStatus(Const.SIGN_VOFFICE);
            StockTransActionDTO stockTransActionVtImpFromPartnerSave = stockTransActionService.save(stockTransActionVtImpFromPartner);
            stockTransActionVtImpFromPartner.setSignCaStatus(null);
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionVtImpFromPartner.setActionCode(null);
            stockTransActionService.save(stockTransActionVtImpFromPartner);
            //stock_trans_detail va serial cua mat hang moi
            saveStockTransDetailAndSerial(lstDetail, transVtImpFromPartnerSave.getStockTransId(), currentDate, changeModelTransId);
            //Tang so phieu nhap
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            doIncreaseStockNum(stockTransActionVtImpFromPartnerSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            // ky voffice
//            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PARTNER);
//            doSignVoffice(transVtImpFromPartner, stockTransActionVtImpFromPartnerSave, requiredRoleMap, flagStockDTO);

            //Lay danh sach serial theo mat hang moi
            lstDetail = stockTransDetailService.findByStockTransId(transVtImpFromPartnerSave.getStockTransId());

            // B4: Tao giao dich VT xuat hang cho chi nhanh
            StockTransDTO transVtExpToBranch = getStockTransVT(stockTransVoficeDTO, Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG,
                    createStaff.getShopId(), Const.OWNER_TYPE.SHOP_LONG, originalStockTrans.getReasonId());
            transVtExpToBranch.setCreateDatetime(currentDate);
            StockTransDTO transVtExpToBranchSave = stockTransService.save(transVtExpToBranch);
            listStockTransId += (transVtExpToBranchSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVtExpToBranch = getStockTransActionVT(approveStaffDTO, currentDate, transVtExpToBranchSave.getStockTransId());
            stockTransActionVtExpToBranch.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, approveStaffDTO));
            stockTransActionVtExpToBranch.setSignCaStatus(Const.SIGN_VOFFICE);
            StockTransActionDTO stockTransActionVtExpToBranchSave = stockTransActionService.save(stockTransActionVtExpToBranch);
            stockTransActionVtExpToBranch.setSignCaStatus(null);
            stockTransActionVtExpToBranch.setActionCode(null);
            stockTransActionVtExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionService.save(stockTransActionVtExpToBranch);
            stockTransActionVtExpToBranch = getStockTransActionVT(approveStaffDTO, currentDate, transVtExpToBranchSave.getStockTransId());
            stockTransActionVtExpToBranch.setSignCaStatus(Const.SIGN_VOFFICE);
            stockTransActionVtExpToBranch.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionVtExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO stockTransActionBrandImpVTSave = stockTransActionService.save(stockTransActionVtExpToBranch);
            stockTransActionVtExpToBranch.setSignCaStatus(null);
            stockTransActionVtExpToBranch.setActionCode(null);
            stockTransActionVtExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionVtExpToBranch);
            //stock_trans_detail va serial cua mat hang moi
            saveStockTransDetailAndSerial(lstDetail, transVtExpToBranchSave.getStockTransId(), currentDate, null);
            flagStockDTO = new FlagStockDTO();
            //tang so phieu nhap cua user cap chi nhanh
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            doIncreaseStockNum(stockTransActionBrandImpVTSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            // ky voffice
            doSignVoffice(transVtExpToBranch, stockTransActionBrandImpVTSave, requiredRoleMap, flagStockDTO);
            //Tang so phieu xuat cua user duyet
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
            doIncreaseStockNum(stockTransActionVtExpToBranchSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            // ky voffice
            doSignVoffice(transVtExpToBranch, stockTransActionVtExpToBranchSave, requiredRoleMap, flagStockDTO);
            // B5: unlock serial
            //Lay cac giao dich chi tiet
            List<StockTransDetailDTO> lstDetailOld = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
            for (StockTransDetailDTO transDetailDTO : lstDetailOld) {
                transDetailDTO.setProdOfferIdChange(changeModelTransDetailService.getNewProdOfferId(transDetailDTO.getProdOfferId(), changeModelTransId, transDetailDTO.getStateId()));
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
                ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
                transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
            }
            flagStockDTO = new FlagStockDTO();
            //cap nhat serial voi status t? 4-->1 va cap nhat lai prod_offer_id
            flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_LOCK);
            flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
            flagStockDTO.setUpdateProdOfferId(true);
            doSaveStockGoods(originalStockTrans, lstDetailOld, flagStockDTO);
            //cong so luong thuc te va SL dap ung
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            doSaveStockTotal(transVtExpToBranchSave, lstDetail, flagStockDTO, stockTransActionBrandImpVTSave);
        } else {
            //Neu la cap VT lap yeu cau
            // B1: VT nhap hang tu chi nhanh
            originalStockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransService.save(originalStockTrans);
            listStockTransId += (originalStockTrans.getStockTransId() + ",");
            //Them moi stock_trans_action voi status 6
            StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(stockTransActionDTO);
            stockTransActionNew.setStockTransActionId(null);
            stockTransActionNew.setSignCaStatus(null);
            stockTransActionNew.setCreateDatetime(currentDate);
            stockTransActionNew.setCreateUser(approveStaffDTO.getStaffCode());
            stockTransActionNew.setActionStaffId(approveStaffDTO.getStaffId());
            stockTransActionNew.setActionCodeShop(null);
            stockTransActionNew.setNote(null);
            stockTransActionNew.setActionCode(null);
            stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionNew);
            // B2: Tao giao dich VT nhap hang tu doi tac
            StockTransDTO transVtImpFromPartner = getStockTransVT(stockTransVoficeDTO, Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG,
                    Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG, originalStockTrans.getReasonId());
            transVtImpFromPartner.setCreateDatetime(currentDate);
            StockTransDTO transVtImpFromPartnerSave = stockTransService.save(transVtImpFromPartner);
            listStockTransId += (transVtImpFromPartnerSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVtImpFromPartner = getStockTransActionVT(approveStaffDTO, currentDate, transVtImpFromPartnerSave.getStockTransId());
            stockTransActionVtImpFromPartner.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
//            stockTransActionVtImpFromPartner.setSignCaStatus(Const.SIGN_VOFFICE);
            StockTransActionDTO stockTransActionVtImpFromPartnerSave = stockTransActionService.save(stockTransActionVtImpFromPartner);
            stockTransActionVtImpFromPartner.setSignCaStatus(null);
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionVtImpFromPartner.setActionCode(null);
            stockTransActionService.save(stockTransActionVtImpFromPartner);
            //stock_trans_detail va serial cua mat hang moi
            saveStockTransDetailAndSerial(lstDetail, transVtImpFromPartnerSave.getStockTransId(), currentDate, changeModelTransId);
            //Tang so phieu nhap
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            doIncreaseStockNum(stockTransActionVtImpFromPartnerSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            lstDetail = stockTransDetailService.findByStockTransId(transVtImpFromPartnerSave.getStockTransId());
            // ky voffice
//            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PARTNER);
//            doSignVoffice(transVtImpFromPartner, stockTransActionVtImpFromPartnerSave, requiredRoleMap, flagStockDTO);
            // B5: unlock serial
            //Lay cac giao dich chi tiet
            List<StockTransDetailDTO> lstDetailOld = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
            for (StockTransDetailDTO transDetailDTO : lstDetailOld) {
                transDetailDTO.setProdOfferIdChange(changeModelTransDetailService.getNewProdOfferId(transDetailDTO.getProdOfferId(), changeModelTransId, transDetailDTO.getStateId()));
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
                ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
                transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
            }
            flagStockDTO = new FlagStockDTO();
            //cap nhat serial voi status tu 4-->1 va cap nhat lai prod_offer_id
            flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_LOCK);
            flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
            flagStockDTO.setUpdateProdOfferId(true);
            doSaveStockGoods(originalStockTrans, lstDetailOld, flagStockDTO);
            //cong so luong thuc te va SL dap ung
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            doSaveStockTotal(transVtImpFromPartnerSave, lstDetail, flagStockDTO, stockTransActionVtImpFromPartnerSave);
        }
        //cap nhat trang thai yeu cau thanh da duyet
        changeModelTransDTO.setStatus(Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_APPROVE);
        changeModelTransDTO.setApproveUserId(approveStaffDTO.getStaffId());
        changeModelTransDTO.setApproveDate(currentDate);
        listStockTransId = listStockTransId.substring(0, listStockTransId.length() - 1);
        changeModelTransDTO.setListStockTransId(listStockTransId);
        changeModelTransService.save(changeModelTransDTO);
    }

    public void cancelChangeProduct(Long changeModelTransId, StaffDTO approveStaffDTO) throws LogicException, Exception {
        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO) || !DataUtil.safeEqual(changeModelTransDTO.getStatus(), Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_NEW)) {
            throw new LogicException("", "mn.stock.change.product.offering.approve.not.found.transaction");
        }
        Date currentDate = getSysDate(em);
        //cap nhat trang thai yeu cau thanh da huy
        changeModelTransDTO.setStatus(Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_CANCEL);
        changeModelTransDTO.setApproveUserId(approveStaffDTO.getStaffId());
        changeModelTransDTO.setApproveDate(currentDate);
        changeModelTransService.save(changeModelTransDTO);
        //Lay thong tin giao dich stock_Trans
        StockTransDTO originalStockTrans = stockTransService.findOne(changeModelTransDTO.getStockTransId());
        if (DataUtil.isNullObject(originalStockTrans)) {
            throw new LogicException("", "utilities.exchange.card.bankplus.not.transaction");
        }
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(originalStockTrans.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException("", "utilities.exchange.card.bankplus.not.transaction");
        }
        //cap nhat thanh da huy status = 8
        originalStockTrans.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
        stockTransService.save(originalStockTrans);
        //Them moi stock_trans_action voi status 8
        StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(stockTransActionDTO);
        stockTransActionNew.setStockTransActionId(null);
        stockTransActionNew.setCreateDatetime(currentDate);
        stockTransActionNew.setActionCode(null);
        stockTransActionNew.setActionCodeShop(null);
        stockTransActionNew.setCreateUser(approveStaffDTO.getStaffCode());
        stockTransActionNew.setNote(null);
        stockTransActionNew.setActionStaffId(approveStaffDTO.getStaffId());
        stockTransActionNew.setSignCaStatus(null);
        stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
        StockTransActionDTO stockTransActionSave = stockTransActionService.save(stockTransActionNew);
        //Cong lai SL dap ung va thuc te
        //Lay cac giao dich chi tiet
        List<StockTransDetailDTO> lstDetail = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        doSaveStockTotal(originalStockTrans, lstDetail, flagStockDTO, stockTransActionSave);
        //Mo cac trang thai serial tu 1-->4
        for (StockTransDetailDTO transDetailDTO : lstDetail) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
            transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
            transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
        }
        flagStockDTO = new FlagStockDTO();
        //cap nhat serial voi status tu 4-->1 va cap nhat lai prod_offer_id
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_LOCK);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        doSaveStockGoods(originalStockTrans, lstDetail, flagStockDTO);

    }

    StockTransDTO getStockTransVT(StockTransDTO stockTransVoffice, Long fromOwnerId, Long fromOwnerType, Long toOwnerId, Long toOwnerType, Long reasonId) {
        StockTransDTO stockTransDTO = DataUtil.cloneBean(stockTransVoffice);
        stockTransDTO.setFromOwnerId(fromOwnerId);
        stockTransDTO.setFromOwnerType(fromOwnerType);
        stockTransDTO.setToOwnerId(toOwnerId);
        stockTransDTO.setToOwnerType(toOwnerType);
        stockTransDTO.setReasonId(reasonId);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        return stockTransDTO;
    }

    private StockTransActionDTO getStockTransActionVT(StaffDTO staffDTO, Date currentDate, Long stockTransId) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        stockTransActionExp.setCreateDatetime(currentDate);
        stockTransActionExp.setStockTransId(stockTransId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        return stockTransActionExp;
    }

    public boolean saveStockTransDetailAndSerial(List<StockTransDetailDTO> lstTransDetail, Long stockTransId,
                                                 Date createDate, Long changeModelTransId) throws Exception {
        try {
            for (StockTransDetailDTO transDetail : lstTransDetail) {
                StockTransDetailDTO tmp = DataUtil.cloneBean(transDetail);
                List<StockTransSerialDTO> lstSerial = stockTransSerialService.findByStockTransDetailId(transDetail.getStockTransDetailId());
                tmp.setStockTransDetailId(null);
                tmp.setStockTransId(stockTransId);
                tmp.setCreateDatetime(createDate);
                if (changeModelTransId != null) {
                    // Lay ra ma mat hang moi
                    tmp.setProdOfferId(changeModelTransDetailService.getNewProdOfferId(tmp.getProdOfferId(), changeModelTransId, tmp.getStateId()));
                }
                StockTransDetailDTO stockTransDetailSave = stockTransDetailService.save(tmp);
                for (StockTransSerialDTO stockTransSerialDTO : lstSerial) {
                    stockTransSerialDTO.setStockTransSerialId(null);
                    stockTransSerialDTO.setStockTransId(stockTransId);
                    stockTransSerialDTO.setCreateDatetime(createDate);
                    stockTransSerialDTO.setStockTransDetailId(stockTransDetailSave.getStockTransDetailId());
                    stockTransSerialDTO.setProdOfferId(stockTransDetailSave.getProdOfferId());
                    stockTransSerialService.save(stockTransSerialDTO);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public void processApproveRequest(Long changeModelTransId) throws LogicException, Exception {
        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO) || !DataUtil.safeEqual(changeModelTransDTO.getStatus(), Const.PROCESS_TOOLKIT.PRE_APPROVE_STATUS)) {
            throw new LogicException("", "thread.toolkit.cancel.request.not.found", changeModelTransId);
        }
        StaffDTO createStaff = staffService.findOne(changeModelTransDTO.getCreateUserId());
        Date currentDate = getSysDate(em);

        StockTransDTO originalStockTrans = stockTransService.findOne(changeModelTransDTO.getStockTransId());
        if (DataUtil.isNullObject(originalStockTrans)) {
            throw new LogicException("", "thread.toolkit.cancel.request.stock.trans", changeModelTransDTO.getStockTransId());
        }
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(originalStockTrans.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException("", "thread.toolkit.cancel.request.stock.trans.action", changeModelTransDTO.getStockTransId());
        }
        Long vOfficeStockTransActionId;
        //Lay cac giao dich chi tiet
        List<StockTransDetailDTO> lstDetail = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
        StaffDTO approveStaffDTO = staffService.findOne(changeModelTransDTO.getApproveUserId());
        ShopDTO approveStaffShopDTO = shopService.findOne(approveStaffDTO.getShopId());
        approveStaffDTO.setShopCode(approveStaffShopDTO.getShopCode());
        RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.add(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        //Them moi cac giao dich phat sinh
        String listStockTransId = "";
        if (!DataUtil.safeEqual(changeModelTransDTO.getFromOwnerId(), Const.L_VT_SHOP_ID)) {
            // B1: VT nhap hang tu chi nhanh
            originalStockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransService.save(originalStockTrans);
            listStockTransId += (originalStockTrans.getStockTransId() + ",");
            //Them moi stock_trans_action voi status 5,6
            StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(stockTransActionDTO);
            stockTransActionNew.setStockTransActionId(null);
            stockTransActionNew.setCreateDatetime(currentDate);
            stockTransActionNew.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionNew.setActionStaffId(approveStaffDTO.getStaffId());
            stockTransActionNew.setCreateUser(approveStaffDTO.getStaffCode());
            stockTransActionNew.setActionCodeShop(null);
            StockTransActionDTO stockTransActionNewSave = stockTransActionService.save(stockTransActionNew);
            stockTransActionNew.setActionCode(null);
            stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionNew);
            //Tang so phieu nhap
            doIncreaseStockNum(stockTransActionNewSave, Const.STOCK_TRANS.TRANS_CODE_PN, requiredRoleMap);

            // B2: Tao giao dich VT xuat tra doi tac
            StockTransDTO transVTExpToPartner = getStockTransVT(new StockTransDTO(), Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG,
                    Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG, originalStockTrans.getReasonId());
            transVTExpToPartner.setCreateDatetime(currentDate);
            StockTransDTO transVTExpToPartnerSave = stockTransService.save(transVTExpToPartner);
            listStockTransId += (transVTExpToPartnerSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVTExpToPartner = getStockTransActionVT(approveStaffDTO, currentDate, transVTExpToPartnerSave.getStockTransId());
            stockTransActionVTExpToPartner.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, approveStaffDTO));
            StockTransActionDTO stockTransActionVTExpToPartnerSave = stockTransActionService.save(stockTransActionVTExpToPartner);
            stockTransActionVTExpToPartner.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionVTExpToPartner.setActionCode(null);
            stockTransActionService.save(stockTransActionVTExpToPartner);
            stockTransActionVTExpToPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionVTExpToPartner);
            //stock_trans_detail va serial
            saveStockTransDetailAndSerial(lstDetail, transVTExpToPartnerSave.getStockTransId(), currentDate, null);
            //Tang so phieu xuat
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
            doIncreaseStockNum(stockTransActionVTExpToPartnerSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);

            // B3: Tao giao dich VT nhap hang tu doi tac
            StockTransDTO transVtImpFromPartner = getStockTransVT(new StockTransDTO(), Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG,
                    Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG, originalStockTrans.getReasonId());
            transVtImpFromPartner.setCreateDatetime(currentDate);
            StockTransDTO transVtImpFromPartnerSave = stockTransService.save(transVtImpFromPartner);
            listStockTransId += (transVtImpFromPartnerSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVtImpFromPartner = getStockTransActionVT(approveStaffDTO, currentDate, transVtImpFromPartnerSave.getStockTransId());
            stockTransActionVtImpFromPartner.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO stockTransActionVtImpFromPartnerSave = stockTransActionService.save(stockTransActionVtImpFromPartner);
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionVtImpFromPartner.setActionCode(null);
            stockTransActionService.save(stockTransActionVtImpFromPartner);
            //stock_trans_detail va serial cua mat hang moi
            saveStockTransDetailAndSerial(lstDetail, transVtImpFromPartnerSave.getStockTransId(), currentDate, changeModelTransId);
            //Tang so phieu nhap
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            doIncreaseStockNum(stockTransActionVtImpFromPartnerSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);

            //Lay danh sach serial theo mat hang moi
            lstDetail = stockTransDetailService.findByStockTransId(transVtImpFromPartnerSave.getStockTransId());

            // B4: Tao giao dich VT xuat hang cho chi nhanh
            StockTransDTO transVtExpToBranch = getStockTransVT(new StockTransDTO(), Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG,
                    createStaff.getShopId(), Const.OWNER_TYPE.SHOP_LONG, originalStockTrans.getReasonId());
            transVtExpToBranch.setCreateDatetime(currentDate);
            StockTransDTO transVtExpToBranchSave = stockTransService.save(transVtExpToBranch);
            listStockTransId += (transVtExpToBranchSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVtExpToBranch = getStockTransActionVT(approveStaffDTO, currentDate, transVtExpToBranchSave.getStockTransId());
            stockTransActionVtExpToBranch.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, approveStaffDTO));
            StockTransActionDTO stockTransActionVtExpToBranchSave = stockTransActionService.save(stockTransActionVtExpToBranch);
            stockTransActionVtExpToBranch.setActionCode(null);
            stockTransActionVtExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            stockTransActionService.save(stockTransActionVtExpToBranch);
            stockTransActionVtExpToBranch = getStockTransActionVT(approveStaffDTO, currentDate, transVtExpToBranchSave.getStockTransId());
            stockTransActionVtExpToBranch.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionVtExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO stockTransActionBrandImpVTSave = stockTransActionService.save(stockTransActionVtExpToBranch);
            stockTransActionVtExpToBranch.setActionCode(null);
            stockTransActionVtExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionVtExpToBranch);
            //stock_trans_detail va serial cua mat hang moi
            saveStockTransDetailAndSerial(lstDetail, transVtExpToBranchSave.getStockTransId(), currentDate, null);
            flagStockDTO = new FlagStockDTO();
            //tang so phieu nhap cua user cap chi nhanh
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            doIncreaseStockNum(stockTransActionBrandImpVTSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            //Tang so phieu xuat cua user duyet
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
            doIncreaseStockNum(stockTransActionVtExpToBranchSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            vOfficeStockTransActionId = stockTransActionVtExpToBranchSave.getStockTransActionId();
            // B5: unlock serial
            //Lay cac giao dich chi tiet
            List<StockTransDetailDTO> lstDetailOld = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
            for (StockTransDetailDTO transDetailDTO : lstDetailOld) {
                transDetailDTO.setProdOfferIdChange(changeModelTransDetailService.getNewProdOfferId(transDetailDTO.getProdOfferId(), changeModelTransId, transDetailDTO.getStateId()));
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
                ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
                transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
            }
            flagStockDTO = new FlagStockDTO();
            //cap nhat serial voi status t? 3-->1 va cap nhat lai prod_offer_id
            flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
            flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
            flagStockDTO.setUpdateProdOfferId(true);
            doSaveStockGoods(originalStockTrans, lstDetailOld, flagStockDTO);
            //cong so luong thuc te va SL dap ung
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            doSaveStockTotal(transVtExpToBranchSave, lstDetail, flagStockDTO, stockTransActionBrandImpVTSave);
        } else {
            //Neu la cap VT lap yeu cau
            // B1: VT nhap hang tu chi nhanh
            originalStockTrans.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransService.save(originalStockTrans);
            listStockTransId += (originalStockTrans.getStockTransId() + ",");
            //Them moi stock_trans_action voi status 6
            StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(stockTransActionDTO);
            stockTransActionNew.setStockTransActionId(null);
            stockTransActionNew.setActionCode(null);
            stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionService.save(stockTransActionNew);
            // B2: Tao giao dich VT nhap hang tu doi tac
            StockTransDTO transVtImpFromPartner = getStockTransVT(new StockTransDTO(), Const.TD_PARTNER_ID, Const.OWNER_TYPE.PARTNER_LONG,
                    Const.L_VT_SHOP_ID, Const.OWNER_TYPE.SHOP_LONG, originalStockTrans.getReasonId());
            transVtImpFromPartner.setCreateDatetime(currentDate);
            StockTransDTO transVtImpFromPartnerSave = stockTransService.save(transVtImpFromPartner);
            listStockTransId += (transVtImpFromPartnerSave.getStockTransId() + ",");
            //stock_trans_action
            StockTransActionDTO stockTransActionVtImpFromPartner = getStockTransActionVT(approveStaffDTO, currentDate, transVtImpFromPartnerSave.getStockTransId());
            stockTransActionVtImpFromPartner.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, approveStaffDTO));
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            StockTransActionDTO stockTransActionVtImpFromPartnerSave = stockTransActionService.save(stockTransActionVtImpFromPartner);
            stockTransActionVtImpFromPartner.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionVtImpFromPartner.setActionCode(null);
            stockTransActionService.save(stockTransActionVtImpFromPartner);
            //stock_trans_detail va serial cua mat hang moi
            saveStockTransDetailAndSerial(lstDetail, transVtImpFromPartnerSave.getStockTransId(), currentDate, changeModelTransId);
            //Tang so phieu nhap
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
            doIncreaseStockNum(stockTransActionVtImpFromPartnerSave, flagStockDTO.getPrefixActionCode(), requiredRoleMap);

            lstDetail = stockTransDetailService.findByStockTransId(transVtImpFromPartnerSave.getStockTransId());

            vOfficeStockTransActionId = stockTransActionVtImpFromPartnerSave.getStockTransActionId();
            // B5: unlock serial
            //Lay cac giao dich chi tiet
            List<StockTransDetailDTO> lstDetailOld = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
            for (StockTransDetailDTO transDetailDTO : lstDetailOld) {
                transDetailDTO.setProdOfferIdChange(changeModelTransDetailService.getNewProdOfferId(transDetailDTO.getProdOfferId(), changeModelTransId, transDetailDTO.getStateId()));
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
                ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
                transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
                transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
            }
            flagStockDTO = new FlagStockDTO();
            //cap nhat serial voi status tu 3-->1 va cap nhat lai prod_offer_id
            flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
            flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
            flagStockDTO.setUpdateProdOfferId(true);
            doSaveStockGoods(originalStockTrans, lstDetailOld, flagStockDTO);
            //cong so luong thuc te va SL dap ung
            flagStockDTO = new FlagStockDTO();
            flagStockDTO.setImportStock(true);
            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
            doSaveStockTotal(transVtImpFromPartnerSave, lstDetail, flagStockDTO, stockTransActionVtImpFromPartnerSave);
        }
        //cap nhat trang thai yeu cau thanh da duyet
        changeModelTransDTO.setStatus(Const.PROCESS_TOOLKIT.APPROVE_STATUS);
        changeModelTransDTO.setApproveDate(currentDate);
        listStockTransId = listStockTransId.substring(0, listStockTransId.length() - 1);
        changeModelTransDTO.setListStockTransId(listStockTransId);
        changeModelTransService.save(changeModelTransDTO);
        //check ky vOffice
        ActiveKitVofficeDTO activeKitVofficeDTO = activeKitVofficeService.findByChangeModelTransId(changeModelTransDTO.getChangeModelTransId());
        if (!DataUtil.isNullObject(activeKitVofficeDTO)) {
            StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
            String stVofficeId = DateUtil.dateToStringWithPattern(originalStockTrans.getCreateDatetime(), "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            stockTransVoffice.setStockTransVofficeId(stVofficeId);
            stockTransVoffice.setAccountName(activeKitVofficeDTO.getAccountName());
            stockTransVoffice.setAccountPass(activeKitVofficeDTO.getAccountPass());
            stockTransVoffice.setSignUserList(activeKitVofficeDTO.getSignUserList());
            stockTransVoffice.setCreateDate(activeKitVofficeDTO.getCreateDate());
            stockTransVoffice.setLastModify(activeKitVofficeDTO.getLastModify());
            stockTransVoffice.setCreateUserId(activeKitVofficeDTO.getCreateUserId());
            stockTransVoffice.setStatus(Const.STATUS.NO_ACTIVE);
            stockTransVoffice.setPrefixTemplate(activeKitVofficeDTO.getPrefixTemplate());
            stockTransVoffice.setStockTransActionId(vOfficeStockTransActionId);
            String actionCode = activeKitVofficeDTO.getActionCode().replace("PX", "PN");
            stockTransVoffice.setActionCode(actionCode);
            stockTransVofficeService.save(stockTransVoffice);
            //Cap nhat lai active_kit_voffice
            activeKitVofficeDTO.setStockTransActionId(vOfficeStockTransActionId);
            activeKitVofficeDTO.setStatus(Const.STATUS.ACTIVE);
            activeKitVofficeDTO.setLastModify(currentDate);
            activeKitVofficeService.save(activeKitVofficeDTO);
        }

    }

    public void processCancelRequest(Long changeModelTransId) throws LogicException, Exception {
        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO) || !DataUtil.safeEqual(changeModelTransDTO.getStatus(), Const.PROCESS_TOOLKIT.PRE_CANCEL_STATUS)) {
            throw new LogicException("", "thread.toolkit.cancel.request.not.found", changeModelTransId);
        }
        Date currentDate = getSysDate(em);
        changeModelTransDTO.setStatus(Const.PROCESS_TOOLKIT.CANCEL_STATUS);
        changeModelTransDTO.setApproveDate(currentDate);
        changeModelTransService.save(changeModelTransDTO);
        StockTransDTO originalStockTrans = stockTransService.findOne(changeModelTransDTO.getStockTransId());
        if (DataUtil.isNullObject(originalStockTrans)) {
            throw new LogicException("", "thread.toolkit.cancel.request.stock.trans", changeModelTransDTO.getStockTransId());
        }
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(originalStockTrans.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException("", "thread.toolkit.cancel.request.stock.trans.action", changeModelTransDTO.getStockTransId());
        }
        //cap nhat thanh da huy status = 8
        originalStockTrans.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
        stockTransService.save(originalStockTrans);
        //Them moi stock_trans_action voi status 8
        StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(stockTransActionDTO);
        stockTransActionNew.setStockTransActionId(null);
        stockTransActionNew.setCreateDatetime(currentDate);
        stockTransActionNew.setActionCode(null);
        stockTransActionNew.setActionCodeShop(null);
        stockTransActionNew.setCreateUser("SYS");
        stockTransActionNew.setNote(null);
        stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.REJECT);
        StockTransActionDTO stockTransActionSave = stockTransActionService.save(stockTransActionNew);
        //Cong lai SL dap ung va thuc te
        //Lay cac giao dich chi tiet
        List<StockTransDetailDTO> lstDetail = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
        doSaveStockTotal(originalStockTrans, lstDetail, flagStockDTO, stockTransActionSave);
        for (StockTransDetailDTO transDetailDTO : lstDetail) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(transDetailDTO.getProdOfferId());
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
            transDetailDTO.setTableName(productOfferTypeDTO.getTableName());
            transDetailDTO.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
        }
        flagStockDTO = new FlagStockDTO();
        //cap nhat serial voi status tu 3-->1
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_NEW);
        doSaveStockGoods(originalStockTrans, lstDetail, flagStockDTO);
    }

    public void approveChangeProductKit(Long changeModelTransId, Long staffId, StockTransDTO stockTransVoficeDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        Date currentDate = getSysDate(em);

        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO)
                || DataUtil.isNullOrZero(changeModelTransDTO.getChangeModelTransId())
                || !Const.CHANGE_MODEL_TRANS_REQUEST_TYPE.CREATE_REQUEST_KIT.equals(changeModelTransDTO.getRequestType())
                || !Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_NEW.equals(changeModelTransDTO.getStatus())) {
            throw new LogicException("", getText("mn.approve.change.model.trans.validate.err"));
        }
        changeModelTransDTO.setStatus(Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_STATUS_WAIT_TOOLKIT);
        changeModelTransDTO.setApproveUserId(staffId);
        changeModelTransDTO.setApproveDate(currentDate);
        changeModelTransService.save(changeModelTransDTO);

//        StaffDTO createStaff = staffService.findOne(changeModelTransDTO.getCreateUserId());
//        if (DataUtil.isNullObject(createStaff)
//                || DataUtil.isNullOrZero(createStaff.getStaffId())){
//            throw new LogicException("", getText("utilities.exchange.card.bankplus.not.transaction"));
//        }

        //Lay thong tin giao dich stock_Trans

        StockTransDTO originalStockTrans = stockTransService.findOne(changeModelTransDTO.getStockTransId());
        if (DataUtil.isNullObject(originalStockTrans)) {
            throw new LogicException("", "utilities.exchange.card.bankplus.not.transaction");
        }
        originalStockTrans.setPassWord(stockTransVoficeDTO.getPassWord());
        originalStockTrans.setUserName(stockTransVoficeDTO.getUserName());
        originalStockTrans.setSignFlowId(stockTransVoficeDTO.getSignFlowId());
        originalStockTrans.setSignVoffice(stockTransVoficeDTO.isSignVoffice());
        //Kiem tra da ky voffice chua
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(originalStockTrans.getStockTransId(), Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
        if (DataUtil.isNullObject(stockTransActionDTO)
                || DataUtil.isNullOrZero(stockTransActionDTO.getStockTransActionId())) {
            throw new LogicException("", "utilities.exchange.card.bankplus.not.transaction");
        }
        stockTransVofficeService.doSignedVofficeValidate(stockTransActionDTO);
        //Lay cac giao dich chi tiet
//        List<StockTransDetailDTO> lstDetail = stockTransDetailService.findByStockTransId(changeModelTransDTO.getStockTransId());

//        String listStockTransId = "";
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
        // ky voffice
        doSignVoffice(originalStockTrans, stockTransActionDTO, requiredRoleMap, flagStockDTO);
        //Luu ActiveKitOffive
        doInsertVoffice(originalStockTrans, stockTransActionDTO, requiredRoleMap, flagStockDTO, changeModelTransDTO);
    }

    public void doInsertVoffice(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, RequiredRoleMap requiredRoleMap,
                                FlagStockDTO flagStockDTO, ChangeModelTransDTO changeModelTransDTO) throws Exception {

        if (!stockTransDTO.isSignVoffice()) {
            return;
        }

        List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(stockTransDTO.getSignFlowId());

        if (DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyDetail");
        }
        StringBuilder lstUser = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            for (SignFlowDetailDTO signFlowDetailDTO : lstSignFlowDetail) {
                if (DataUtil.isNullOrEmpty(signFlowDetailDTO.getEmail())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyEmail");
                }
                if (!DataUtil.isNullOrEmpty(lstUser.toString())) {
                    lstUser.append(",");
                }
                lstUser.append(signFlowDetailDTO.getEmail().trim().substring(0, signFlowDetailDTO.getEmail().lastIndexOf("@")));
            }

            ActiveKitVofficeDTO activeKitVofficeDTO = new ActiveKitVofficeDTO();
            String stVofficeId = DateUtil.dateToStringWithPattern(stockTransDTO.getCreateDatetime(), "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            activeKitVofficeDTO.setActiveKitVofficeId(Long.valueOf(stVofficeId));
            activeKitVofficeDTO.setAccountName(stockTransDTO.getUserName().trim());
            activeKitVofficeDTO.setAccountPass(stockTransDTO.getPassWord());
            activeKitVofficeDTO.setSignUserList(lstUser.toString());
            activeKitVofficeDTO.setCreateDate(stockTransDTO.getCreateDatetime());
            activeKitVofficeDTO.setLastModify(stockTransDTO.getCreateDatetime());
            activeKitVofficeDTO.setCreateUserId(stockTransActionDTO.getActionStaffId());
            activeKitVofficeDTO.setStatus(Const.STATUS.NO_ACTIVE);
            activeKitVofficeDTO.setChangeModelTransId(changeModelTransDTO.getChangeModelTransId());
            //check template
            StaffDTO staff = staffService.findOne(activeKitVofficeDTO.getCreateUserId());
            String prefixTemplatePath = shopService.isCenterOrBranch(staff.getShopId()) ? flagStockDTO.getPrefixActionCode() + "_TTH_CN" : flagStockDTO.getPrefixActionCode();
            activeKitVofficeDTO.setPrefixTemplate(prefixTemplatePath);
            //check phan quyen receiptNo
//            String receiptNo = stockTransActionDTO.getActionCode();
//            if (Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(stockTransActionDTO.getStatus()) || Const.STOCK_TRANS_STATUS.IMPORT_NOTE.equals(stockTransActionDTO.getStatus())) {
//                receiptNo = receiptNo.substring(receiptNo.lastIndexOf("_") + 1, receiptNo.length());
//                if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
//                    receiptNo = DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop()) ? receiptNo : stockTransActionDTO.getActionCodeShop();
//                }
//            }
//            activeKitVofficeDTO.setReceiptNo(receiptNo);
            activeKitVofficeDTO.setActionCode(stockTransActionDTO.getActionCode());

            activeKitVofficeService.create(activeKitVofficeDTO);
        }
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

}

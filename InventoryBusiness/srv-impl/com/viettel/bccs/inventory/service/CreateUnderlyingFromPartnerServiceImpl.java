package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.ShopIm1DTO;
import com.viettel.bccs.im1.model.StaffIm1;
import com.viettel.bccs.im1.repo.ShopIm1Repo;
import com.viettel.bccs.im1.repo.StaffIm1Repo;
import com.viettel.bccs.im1.service.ShopIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.repo.ShopRepo;
import com.viettel.bccs.inventory.repo.StaffRepo;
import com.viettel.bccs.inventory.repo.StockTransActionRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockPartnerService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thanhnt77 on 29/05/2016.
 */
@Service
public class CreateUnderlyingFromPartnerServiceImpl extends BaseStockPartnerService implements CreateUnderlyingFromPartnerService {

    Logger logger = Logger.getLogger(CreateUnderlyingFromPartnerServiceImpl.class);

    private final BaseMapper<StockTrans, StockTransDTO> stockMapper = new BaseMapper(StockTrans.class, StockTransDTO.class);
    private final BaseMapper<StockTransDetail, StockTransDetailDTO> detailMapper = new BaseMapper<>(StockTransDetail.class, StockTransDetailDTO.class);
    private final BaseMapper<StockTransSerial, StockTransSerialDTO> serialMapper = new BaseMapper<>(StockTransSerial.class, StockTransSerialDTO.class);
    private final BaseMapper<StockTransAction, StockTransActionDTO> actionMapper = new BaseMapper<>(StockTransAction.class, StockTransActionDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private PartnerContractService partnerContractService;
    @Autowired
    private PartnerContractDetailService partnerContractDetailService;
    @Autowired
    private KttsService kttsService;
    @Autowired
    private StockTransDetailKcsService stockTransDetailKcsService;
    @Autowired
    private StockTransLogisticService stockTransLogisticService;
    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private StaffIm1Repo staffIm1Repo;
    @Autowired
    private ShopIm1Repo shopIm1Repo;
    @Autowired
    private ShopIm1Service shopIm1Service;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransActionRepo stockTransActionRepo;
    @Autowired
    private ShopRepo shopRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        Connection conn = IMCommonUtils.getDBIMConnection();
        conn.setAutoCommit(false);
        try {
            Date sysdate = DbUtil.getSysDate(em);
            //1. thuc hien validate du lieu nhap
            doValidate(importPartnerRequestDTO);

            //2. Xu ly insert du lieu
            //2.1 Insert bang stock_trans
            StockTransDTO stockTransImpVT = new StockTransDTO();
            StockTransActionDTO stockTransActionDTOImpVT = new StockTransActionDTO();
            stockTransImpVT.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
            stockTransImpVT.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransImpVT.setFromOwnerId(importPartnerRequestDTO.getPartnerId());
            stockTransImpVT.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransImpVT.setToOwnerId(importPartnerRequestDTO.getToOwnerId());
            stockTransImpVT.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransImpVT.setReasonId(importPartnerRequestDTO.getReasonId());
            stockTransImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            stockTransImpVT.setNote(importPartnerRequestDTO.getNote());
            stockTransImpVT.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
            stockTransImpVT.setCreateDatetime(sysdate);
            stockTransImpVT.setLogicstic(importPartnerRequestDTO.isLogistic() ? Const.STOCK_TRANS.IS_LOGISTIC : null);
            stockTransImpVT.setStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransImpVT.setActionCode(importPartnerRequestDTO.getActionCode());
            stockTransImpVT.setUserCreate(importPartnerRequestDTO.getImportStaffCode());
            SignOfficeDTO signOfficeDTO = importPartnerRequestDTO.getSignOfficeDTO();
            if (importPartnerRequestDTO.getSignOfficeDTO() != null) {
                stockTransImpVT.setUserName(signOfficeDTO.getUserName());
                stockTransImpVT.setPassWord(signOfficeDTO.getPassWord());
                stockTransImpVT.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransImpVT.setSignVoffice(true);
                stockTransActionDTOImpVT.setSignCaStatus(Const.SIGN_VOFFICE);
            }
            insertStockTrans(stockTransImpVT, conn);
            //2.2 Insert bang stock_trans_action
            stockTransActionDTOImpVT.setStockTransId(stockTransImpVT.getStockTransId());
            stockTransActionDTOImpVT.setActionCode(importPartnerRequestDTO.getActionCode());
            stockTransActionDTOImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER_PARTNER);
            stockTransActionDTOImpVT.setNote(importPartnerRequestDTO.getNote());
            stockTransActionDTOImpVT.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
            stockTransActionDTOImpVT.setCreateDatetime(sysdate);
            stockTransActionDTOImpVT.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransActionDTOImpVT.setSignCaStatus(importPartnerRequestDTO.isSignVoffice() && importPartnerRequestDTO.getSignOfficeDTO() != null ? Const.SIGN_VOFFICE : null);
            insertStockTransAction(stockTransImpVT, stockTransActionDTOImpVT, conn);

            //2.3 thuc hien ky vOffice
            FlagStockDTO flagStockDTO = new FlagStockDTO();
            flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_LN);


            if (importPartnerRequestDTO.isSignVoffice() && importPartnerRequestDTO.getSignOfficeDTO() != null) {
                // doSignVoffice(stockTransImpVT, stockTransActionDTOImpVT, requiredRoleMap, flagStockDTO);
                StockTransVofficeDTO vofficeDTO = getStockTransVoffice(stockTransImpVT, stockTransActionDTOImpVT, requiredRoleMap, flagStockDTO);
                importPartnerRequestDTO.setStockTransVofficeDTO(vofficeDTO);
                insertVoffice(conn, importPartnerRequestDTO, stockTransImpVT, Const.ConfigStockTrans.LN.toString());
            }

            //2.4 thuc hien dong bo du lieu logistic
            if (importPartnerRequestDTO.isLogistic()) {
                StockTransLogisticDTO stockTransLogistic = new StockTransLogisticDTO();
                stockTransLogistic.setRequestTypeLogistic(Const.LOGISTIC.REQ_TYPE_NEW);
                stockTransLogistic.setCreateDate(stockTransImpVT.getCreateDatetime());
                stockTransLogistic.setStockTransId(stockTransImpVT.getStockTransId());
                stockTransLogistic.setStatus(Const.LOGISTIC.STATUS_NEW);
                insertStockTransLogistic(stockTransLogistic, conn);
                //stockTransLogisticService.save(stockTransLogistic);
            }

            //11. Thuc hien thay doi so phieu
            //doIncreaseStockNum(stockTransActionDTOImpVT, flagStockDTO.getPrefixActionCode(), requiredRoleMap);
            doIncreaseStockNumNew(stockTransActionDTOImpVT, flagStockDTO.getPrefixActionCode(), requiredRoleMap, conn);

            StockTransDetailDTO stockTransDetailDTOImpVT;
            StockTransDetailKcsDTO stockTransDetailKcsDTO;
            List<ProductOfferingDTO> lsProdOfferDto = importPartnerRequestDTO.getLsProductOfferDto();

            //2.6 insert bang partner_contract
            PartnerContractDTO partnerContractDTO = new PartnerContractDTO();
            partnerContractDTO.setStockTransId(stockTransImpVT.getStockTransId());
            partnerContractDTO.setActionCode(importPartnerRequestDTO.getActionCode());
            partnerContractDTO.setCreateDate(sysdate);
            partnerContractDTO.setLastModified(sysdate);
            partnerContractDTO.setPoCode(importPartnerRequestDTO.getPoCode());
            partnerContractDTO.setPoDate(importPartnerRequestDTO.getPoDate());
            partnerContractDTO.setContractCode(importPartnerRequestDTO.getContractCode());
            partnerContractDTO.setContractDate(importPartnerRequestDTO.getContractDate());
            partnerContractDTO.setImportDate(importPartnerRequestDTO.getImportDate());
            partnerContractDTO.setDeliveryLocation(importPartnerRequestDTO.getDeliveryLocation());
            partnerContractDTO.setRequestDate(importPartnerRequestDTO.getRequestDate());
            partnerContractDTO.setPartnerId(importPartnerRequestDTO.getPartnerId());
            partnerContractDTO.setCurrencyType(importPartnerRequestDTO.getCurrencyType());
            partnerContractDTO.setCurrencyRate(importPartnerRequestDTO.getCurrencyRate());
            partnerContractDTO.setKcsNo(importPartnerRequestDTO.getCodeKCS());
            partnerContractDTO.setKcsDate(importPartnerRequestDTO.getDateKCS());
            partnerContractDTO.setOrderCode(importPartnerRequestDTO.getCodePackage());
            partnerContractDTO.setStartDateWarranty(importPartnerRequestDTO.getEndDate());
            partnerContractDTO.setContractStatus(Const.STATE_STATUS.NEW.shortValue());
            List<PartnerContractDetailDTO> lsPartnerContractDetail = Lists.newArrayList();
            for (ProductOfferingDTO offeringDTO : lsProdOfferDto) {
                //2.7 insert partner contract detail
                PartnerContractDetailDTO partnerContractDetailDTO = new PartnerContractDetailDTO();
                partnerContractDetailDTO.setCreateDate(sysdate);
                partnerContractDetailDTO.setProdOfferId(offeringDTO.getProductOfferingId());
                partnerContractDetailDTO.setStateId(offeringDTO.getStateId());
                partnerContractDetailDTO.setQuantity(DataUtil.safeToLong(offeringDTO.getAvailableQuantity()));
                partnerContractDetailDTO.setCurrencyType(importPartnerRequestDTO.getCurrencyType());
                partnerContractDetailDTO.setUnitPrice(offeringDTO.getPrice());
                partnerContractDetailDTO.setOrderCode(importPartnerRequestDTO.getCodePackage());
                partnerContractDetailDTO.setStartDateWarranty(importPartnerRequestDTO.getEndDate());
                partnerContractDetailDTO.setUnitPrice(offeringDTO.getPrice());
                lsPartnerContractDetail.add(partnerContractDetailDTO);
            }

            doSavePartnerContractAndDetail(partnerContractDTO, lsPartnerContractDetail, conn, stockTransImpVT, sysdate);
            //2.6 insert bang stock_trans_detail, neu la mat hang kit thi sinh them giao dich rieng cho mat hang sim
            Long stockTransInsert = stockTransImpVT.getStockTransId();
            for (ProductOfferingDTO offeringDTO : lsProdOfferDto) {
                offeringDTO.setQuantityKtts(DataUtil.safeToLong(offeringDTO.getStrQuantityKcs()));
                //2.5.1 insert stockTrans
                stockTransDetailDTOImpVT = new StockTransDetailDTO();
                stockTransDetailDTOImpVT.setStockTransId(stockTransInsert);
                stockTransDetailDTOImpVT.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
                stockTransDetailDTOImpVT.setProdOfferId(offeringDTO.getProductOfferingId());
                stockTransDetailDTOImpVT.setProdOfferIdSwap(importPartnerRequestDTO.isLogistic() ? offeringDTO.getProdOfferSimId() : null);
                stockTransDetailDTOImpVT.setStateId(Const.GOODS_STATE.NEW);
                stockTransDetailDTOImpVT.setQuantity(offeringDTO.getQuantity());
                stockTransDetailDTOImpVT.setAmount(0L);
                stockTransDetailDTOImpVT.setPrice(offeringDTO.getPrice());
                stockTransDetailDTOImpVT.setCreateDatetime(sysdate);
                insertStockTransDetail(stockTransImpVT, stockTransDetailDTOImpVT, conn);
                stockTransDetailDTOImpVT.setProdOfferIdSwap(offeringDTO.getProdOfferSimId());
                //2.5.2 tao giao dich xuat sim neu la mat hang kit
                StockTransActionDTO stockTransActionExpSim = null;
                StockTransDTO stockTransExpSim = null;
                StockTransDetailDTO stockTransDetailExpSim = null;
                if (!importPartnerRequestDTO.isLogistic() && Const.STOCK_TYPE.STOCK_KIT.equals(offeringDTO.getProductOfferTypeId())) {
                    //2.5.2.1 tao moi giao dich stock_trans cho gd sim
                    stockTransExpSim = new StockTransDTO();
                    stockTransExpSim.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
                    stockTransExpSim.setFromOwnerId(importPartnerRequestDTO.getToOwnerId());
                    stockTransExpSim.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                    stockTransExpSim.setToOwnerId(importPartnerRequestDTO.getToOwnerId());
                    stockTransExpSim.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                    stockTransExpSim.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
                    stockTransExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                    stockTransExpSim.setCreateDatetime(sysdate);
                    stockTransExpSim.setReasonId(Const.REASON_ID.EXP_SIM_IMP_FROM_PARTNER);
                    stockTransExpSim.setNote(GetTextFromBundleHelper.getText("import.partner.exportNote"));
                    stockTransExpSim.setStaffId(importPartnerRequestDTO.getImportStaffId());
                    stockTransExpSim.setUserCreate(importPartnerRequestDTO.getImportStaffCode());
                    insertStockTrans(stockTransExpSim, conn);
                    //2.5.2.2 tao moi 3 stock_trans_action cho gd sim
                    stockTransActionExpSim = new StockTransActionDTO();
                    stockTransActionExpSim.setActionCode(importPartnerRequestDTO.getActionCode());
                    stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                    stockTransActionExpSim.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
                    stockTransActionExpSim.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
                    stockTransActionExpSim.setCreateDatetime(sysdate);
                    stockTransActionExpSim.setStockTransId(stockTransExpSim.getStockTransId());
                    insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);

                    stockTransActionExpSim.setActionCode(null);
                    stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                    stockTransExpSim.setNote(null);
                    insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);

                    stockTransActionExpSim.setActionCode(null);
                    stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                    stockTransExpSim.setNote(null);
                    insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);
                    //2.5.2.2 tao moi 3 stock_trans_detail cho gd sim
                    stockTransDetailExpSim = new StockTransDetailDTO();
                    stockTransDetailExpSim.setStockTransId(stockTransExpSim.getStockTransId());
                    stockTransDetailExpSim.setProdOfferId(offeringDTO.getProdOfferSimId());
                    stockTransDetailExpSim.setStateId(Const.GOODS_STATE.NEW);
                    stockTransDetailExpSim.setQuantity(0L);
                    stockTransDetailExpSim.setAmount(0L);
                    stockTransDetailExpSim.setPrice(0L);
                    stockTransDetailExpSim.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
                    insertStockTransDetail(stockTransExpSim, stockTransDetailExpSim, conn);
                }

                if (!importPartnerRequestDTO.isLogistic()) {
                    //Cong so luong STOCK_TOTAL cho kho nhan
                    flagStockDTO.setImportStock(true);
                    flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                    flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                    Long quantity = DataUtil.safeToLong(offeringDTO.getStrQuantityKcs()).compareTo(0L) == 0 ? offeringDTO.getQuantity() : offeringDTO.getAvailableQuantity();
                    stockTransDetailDTOImpVT.setQuantity(quantity);
                    //2.5.3 INSERT du lieu cac bang STOCK_X
                    if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_QUANTITY, offeringDTO.getImpType())) {
                        //luong nay luon chay cap nhap stock_total
                        doSaveStockTotal(stockTransImpVT, Lists.newArrayList(stockTransDetailDTOImpVT), flagStockDTO, stockTransActionDTOImpVT, conn);
                    } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_FILE, offeringDTO.getImpType())) {
                        importSerialByFile(importPartnerRequestDTO, stockTransImpVT, stockTransActionDTOImpVT, stockTransDetailDTOImpVT,
                                stockTransExpSim, stockTransActionExpSim, stockTransDetailExpSim, offeringDTO, conn, sysdate);
                    } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE, offeringDTO.getImpType())) {
                        importSerialByRange(importPartnerRequestDTO, stockTransImpVT, stockTransActionDTOImpVT, stockTransDetailDTOImpVT,
                                stockTransExpSim, stockTransActionExpSim, stockTransDetailExpSim, offeringDTO, conn, sysdate);
                    }
                }

                //2.5.6 insert stockTransDetailKcs
                if (!DataUtil.isNullOrZero(offeringDTO.getQuantityKtts())) {
                    stockTransDetailKcsDTO = new StockTransDetailKcsDTO();
                    stockTransDetailKcsDTO.setQuantity(offeringDTO.getQuantityKtts());
                    stockTransDetailKcsDTO.setCreateDatetime(sysdate);
                    stockTransDetailKcsDTO.setStockTransId(stockTransInsert);
                    stockTransDetailKcsDTO.setProdOfferId(offeringDTO.getProductOfferingId());
                    stockTransDetailKcsDTO.setStockTransDetailKcsId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_KCS_SEQ"));
                    stockTransDetailKcsDTO.setStockTransDetailId(stockTransDetailDTOImpVT.getStockTransDetailId());
                    stockTransDetailKcsDTO.setStateId(Const.STATE_STATUS.NEW);
                    insertStockTransDetailKcs(stockTransDetailKcsDTO, conn);
                }
            }
            conn.commit();

            BaseMessage messageKtts = kttsService.getResultImpShipment(importPartnerRequestDTO.getCodeKCS(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            if (!messageKtts.isSuccess()) {
                LogicException ex = new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "");
                ex.setDescription(messageKtts.getDescription());
                throw ex;
            }
        } catch (LogicException ex) {
            conn.rollback();
            throw ex;
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.close();
        }
    }

    public void insertStockTransDetailKcs(StockTransDetailKcsDTO stockTransDetailKcsDTO, Connection conn) throws Exception {
        PreparedStatement insertContractDetailKCS = null;
        try {
            StringBuilder strQueryInsertContractDetailKCS = new StringBuilder("");
            strQueryInsertContractDetailKCS.append("  insert into stock_trans_detail_kcs (stock_trans_detail_kcs_id,stock_trans_id,prod_offer_id,quantity,create_datetime,stock_trans_detail_id,state_id) values(?,?,?,?,?,?,?) ");
            insertContractDetailKCS = conn.prepareStatement(strQueryInsertContractDetailKCS.toString());
            insertContractDetailKCS.setLong(1, stockTransDetailKcsDTO.getStockTransDetailKcsId());
            insertContractDetailKCS.setLong(2, stockTransDetailKcsDTO.getStockTransId());
            insertContractDetailKCS.setLong(3, stockTransDetailKcsDTO.getProdOfferId());
            insertContractDetailKCS.setLong(4, stockTransDetailKcsDTO.getQuantity());
            insertContractDetailKCS.setDate(5, new java.sql.Date(stockTransDetailKcsDTO.getCreateDatetime().getTime()));
            insertContractDetailKCS.setLong(6, stockTransDetailKcsDTO.getStockTransDetailId());
            insertContractDetailKCS.setLong(7, stockTransDetailKcsDTO.getStateId());
            insertContractDetailKCS.executeUpdate();
        } catch (Exception ex) {
            conn.rollback();
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        } finally {
            if (insertContractDetailKCS != null) {
                insertContractDetailKCS.close();
            }
        }
    }

    public void insertStockTransDetail(StockTransDTO stockTransDTO, StockTransDetailDTO stockTransDetailDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement insertStock = null;
        try {
            StringBuilder strInsert = new StringBuilder();
            strInsert.append(" INSERT INTO STOCK_TRANS_DETAIL (STOCK_TRANS_DETAIL_ID,STOCK_TRANS_ID,PROD_OFFER_ID,STATE_ID,QUANTITY,PRICE, AMOUNT, CREATE_DATETIME) ");
            strInsert.append(" VALUES ");
            strInsert.append(" (?,?,?,?,?,?,?,?) ");
            insertStock = conn.prepareStatement(strInsert.toString());
            insertStock.setLong(1, stockTransDetailDTO.getStockTransDetailId());
            insertStock.setLong(2, stockTransDTO.getStockTransId());
            insertStock.setLong(3, stockTransDetailDTO.getProdOfferId());
            insertStock.setLong(4, stockTransDetailDTO.getStateId());
            if (stockTransDetailDTO.getQuantity() != null) {
                insertStock.setLong(5, stockTransDetailDTO.getQuantity());
            } else {
                insertStock.setNull(5, java.sql.Types.INTEGER);
            }
            if (stockTransDetailDTO.getPrice() != null) {
                insertStock.setLong(6, stockTransDetailDTO.getPrice());
            } else {
                insertStock.setNull(6, java.sql.Types.INTEGER);
            }
            if (stockTransDetailDTO.getAmount() != null) {
                insertStock.setLong(7, stockTransDetailDTO.getAmount());
            } else {
                insertStock.setNull(7, java.sql.Types.INTEGER);
            }
            insertStock.setDate(8, new java.sql.Date(stockTransDTO.getCreateDatetime().getTime()));
            insertStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.stock.trans.detail.err");
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
        }
    }

    public void doSavePartnerContractAndDetail(PartnerContractDTO partnerContractDTO, List<PartnerContractDetailDTO> lsPartnerDetailDto, Connection conn,
                                               StockTransDTO stockTransImpVT, Date sysDate) throws LogicException, Exception {

        Long partnerContractId = DbUtil.getSequence(em, "partner_contract_seq");
        partnerContractDTO.setPartnerContractId(partnerContractId);
        int resultSavePartnerContract = doSavePartnerContract(partnerContractDTO, conn, stockTransImpVT);
        if (resultSavePartnerContract <= 0) {
            //Co loi xay ra khi insert bang PARTNER_CONTRACT
            if (conn != null) {
                conn.rollback();
            }
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        }
        for (PartnerContractDetailDTO partnerDettail : lsPartnerDetailDto) {
            // Save partner_contract_detail
            partnerDettail.setPartnerContractId(partnerContractId);
            insertPartnerContractDetail(partnerDettail, conn);

        }
    }

    public void insertPartnerContractDetail(PartnerContractDetailDTO partnerDettail, Connection conn) throws Exception {
        PreparedStatement insertPartnerContractDetail = null;
        try {
            StringBuilder strQueryInsertPartnerContract = new StringBuilder("");
            strQueryInsertPartnerContract.append(" INSERT INTO partner_contract_detail(partner_contract_detail_id, partner_contract_id, create_date, prod_offer_id, state_id, quantity, currency_type, unit_price, order_code, start_date_warranty) ");
            strQueryInsertPartnerContract.append("             VALUES (partner_contract_detail_seq.NEXTVAL,?,?,?,?,?,?,?,?,?) ");
            insertPartnerContractDetail = conn.prepareStatement(strQueryInsertPartnerContract.toString());
            insertPartnerContractDetail.setLong(1, partnerDettail.getPartnerContractId());
            insertPartnerContractDetail.setDate(2, partnerDettail.getCreateDate() == null ? null : new java.sql.Date(partnerDettail.getCreateDate().getTime()));
            insertPartnerContractDetail.setLong(3, partnerDettail.getProdOfferId());
            insertPartnerContractDetail.setLong(4, partnerDettail.getStateId());
            insertPartnerContractDetail.setLong(5, DataUtil.safeToLong(partnerDettail.getQuantity()));
            insertPartnerContractDetail.setString(6, (partnerDettail.getCurrencyType() == null) ? "" : partnerDettail.getCurrencyType().trim());
            insertPartnerContractDetail.setLong(7, DataUtil.safeToLong(partnerDettail.getUnitPrice()));
            insertPartnerContractDetail.setString(8, (partnerDettail.getOrderCode() == null) ? "" : partnerDettail.getOrderCode().trim());
            insertPartnerContractDetail.setDate(9, (partnerDettail.getStartDateWarranty() == null) ? null : new java.sql.Date(partnerDettail.getStartDateWarranty().getTime()));
            insertPartnerContractDetail.executeUpdate();
        } catch (Exception ex) {
            conn.rollback();
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        } finally {
            if (insertPartnerContractDetail != null) {
                insertPartnerContractDetail.close();
            }
        }
    }

    public int doSavePartnerContract(PartnerContractDTO partnerContractDTO, Connection conn, StockTransDTO stockTransImpVT) throws LogicException, Exception {
        PreparedStatement insertPartnerContract = null;
        try {
            StringBuilder strQueryInsertPartnerContract = new StringBuilder("");
            strQueryInsertPartnerContract.append(" INSERT INTO partner_contract(partner_contract_id, stock_trans_id, action_code, create_date, ");
            strQueryInsertPartnerContract.append("             last_modified, contract_code, po_code, contract_date, po_date, import_date, ");
            strQueryInsertPartnerContract.append("             delivery_location, request_date, partner_id, currency_type, unit_price, contract_status, currency_rate, kcs_no, kcs_date) ");
            strQueryInsertPartnerContract.append("             VALUES (?,?,?, sysdate, sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            insertPartnerContract = conn.prepareStatement(strQueryInsertPartnerContract.toString());
            insertPartnerContract.setLong(1, partnerContractDTO.getPartnerContractId());
            insertPartnerContract.setLong(2, stockTransImpVT.getStockTransId());
            insertPartnerContract.setString(3, partnerContractDTO.getActionCode());
            insertPartnerContract.setString(4, partnerContractDTO.getContractCode());
            insertPartnerContract.setString(5, partnerContractDTO.getPoCode());
            insertPartnerContract.setDate(6, (partnerContractDTO.getContractDate() == null) ? null : new java.sql.Date(partnerContractDTO.getContractDate().getTime()));
            insertPartnerContract.setDate(7, (partnerContractDTO.getPoDate() == null) ? null : new java.sql.Date(partnerContractDTO.getPoDate().getTime()));
            insertPartnerContract.setDate(8, (partnerContractDTO.getImportDate() == null) ? null : new java.sql.Date(partnerContractDTO.getImportDate().getTime()));
            insertPartnerContract.setString(9, (partnerContractDTO.getDeliveryLocation() == null) ? "" : partnerContractDTO.getDeliveryLocation().trim());
            insertPartnerContract.setDate(10, (partnerContractDTO.getRequestDate() == null) ? null : new java.sql.Date(partnerContractDTO.getRequestDate().getTime()));
            insertPartnerContract.setLong(11, partnerContractDTO.getPartnerId());
            insertPartnerContract.setString(12, (partnerContractDTO.getCurrencyType() == null) ? "" : partnerContractDTO.getCurrencyType());
            if (partnerContractDTO.getUnitPrice() == null) {
                insertPartnerContract.setNull(13, java.sql.Types.INTEGER);
            } else {
                insertPartnerContract.setDouble(13, partnerContractDTO.getUnitPrice());
            }
            insertPartnerContract.setLong(14, partnerContractDTO.getContractStatus());

            insertPartnerContract.setNull(15, Types.DOUBLE);
            insertPartnerContract.setString(16, DataUtil.safeToString(partnerContractDTO.getKcsNo()).trim());
            insertPartnerContract.setDate(17, (partnerContractDTO.getKcsDate() == null) ? null : new java.sql.Date(partnerContractDTO.getKcsDate().getTime()));

            return insertPartnerContract.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
        } finally {
            if (insertPartnerContract != null) {
                insertPartnerContract.close();
            }
        }
    }

    private void doIncreaseStockNumNew(StockTransActionDTO stockTransActionDTO, String prefixActionCode, RequiredRoleMap requiredRoleMap, Connection conn) throws Exception {
        try {
            if (DataUtil.isNullOrEmpty(prefixActionCode)) {
                return;
            }
            // Cap nhat du lieu sang IM1.
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                staffIm1Repo.increaseStockNum(conn, stockTransActionDTO.getActionStaffId(), "STOCK_NUM_IMP");
                if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
                    Long num = 0L;
                    String actionCodeShop = "";
                    StaffIm1 staff = staffIm1Repo.findOneStaff(stockTransActionDTO.getActionStaffId());
                    if (staff != null) {
                        //tang stock_num_imp  + 1
                        shopIm1Repo.increaseStockNum(conn, staff.getShopId());

                        ShopIm1DTO shopDTO = shopIm1Service.findOne(staff.getShopId());
                        if (shopDTO != null) {
                            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                                num = shopDTO.getStockNum() != null ? shopDTO.getStockNum() : num;
                                actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num);

                            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)) {
                                num = shopDTO.getStockNumImp() != null ? shopDTO.getStockNumImp() : num;
                                actionCodeShop = prefixActionCode + DataUtil.customFormat("000000", num);
                            }
                            //ShopIm1DTO shopDTONew = shopIm1Service.findOne(shopDTO.getShopId());
                            stockTransActionRepo.updateActionCodeShop(conn, actionCodeShop, stockTransActionDTO.getStockTransActionId());
                        }
                    }
                }
                return;
            }

            staffRepo.increaseStockNum(conn, stockTransActionDTO.getActionStaffId(), "STOCK_NUM_IMP");

            if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
                Long num = 0L;
                String actionCodeShop = "";
                StaffDTO staff = staffService.findOne(stockTransActionDTO.getActionStaffId());
                if (staff != null) {
                    //tang stock_num_imp  + 1
                    shopRepo.increaseStockNum(conn, staff.getShopId());

                    ShopDTO shopDTO = shopService.findOne(staff.getShopId());
                    if (shopDTO != null) {
                        if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                            num = shopDTO.getStockNum() != null ? shopDTO.getStockNum() : num;
                            actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num);

                        } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)) {
                            num = shopDTO.getStockNumImp() != null ? shopDTO.getStockNumImp() : num;
                            actionCodeShop = prefixActionCode + DataUtil.customFormat("000000", num);
                        }
                        stockTransActionRepo.updateActionCodeShop(conn, actionCodeShop, stockTransActionDTO.getStockTransActionId());
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            conn.close();
            conn.rollback();
            throw ex;
        }
    }

    private void insertStockTransLogistic(StockTransLogisticDTO stockTransLogisticDTO, Connection conn) throws Exception {
        PreparedStatement ps = null;
        try {
            stockTransLogisticDTO.setStockTransLogisticId(DbUtil.getSequence(em, "STOCK_TRANS_LOGISTIC_SEQ"));
            StringBuilder builder = new StringBuilder("");
            builder.append(" insert into stock_trans_logistic (stock_trans_logistic_id,stock_trans_id,request_type_logistic,create_date,status) ");
            builder.append(" values (?,?,?,?,?) ");
            ps = conn.prepareStatement(builder.toString());
            ps.setLong(1, stockTransLogisticDTO.getStockTransLogisticId());
            ps.setLong(2, stockTransLogisticDTO.getStockTransId());
            ps.setLong(3, stockTransLogisticDTO.getRequestTypeLogistic());
            ps.setDate(4, new java.sql.Date(stockTransLogisticDTO.getCreateDate().getTime()));
            ps.setLong(5, stockTransLogisticDTO.getStatus());
            ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public StockTransVofficeDTO getStockTransVoffice(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, RequiredRoleMap requiredRoleMap,
                                                     FlagStockDTO flagStockDTO) throws Exception {

        if (!stockTransDTO.isSignVoffice()) {
            return null;
        }

        List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(stockTransDTO.getSignFlowId());

        if (DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyDetail");
        }
        StringBuilder lstUser = new StringBuilder("");
        StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
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


            String stVofficeId = DateUtil.dateToStringWithPattern(stockTransDTO.getCreateDatetime(), "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            stockTransVoffice.setStockTransVofficeId(stVofficeId);
            stockTransVoffice.setAccountName(stockTransDTO.getUserName().trim());
            stockTransVoffice.setAccountPass(stockTransDTO.getPassWord());
            stockTransVoffice.setSignUserList(lstUser.toString());
            stockTransVoffice.setCreateDate(stockTransDTO.getCreateDatetime());
            stockTransVoffice.setLastModify(stockTransDTO.getCreateDatetime());
            stockTransVoffice.setCreateUserId(stockTransActionDTO.getActionStaffId());
            stockTransVoffice.setStatus(Const.STATUS.NO_ACTIVE);
            stockTransVoffice.setStockTransActionId(stockTransActionDTO.getStockTransActionId());
            stockTransVoffice.setFindSerial(flagStockDTO.getFindSerial());
            //check template
            StaffDTO staff = staffService.findOne(stockTransVoffice.getCreateUserId());
            String prefixTemplatePath = shopService.isCenterOrBranch(staff.getShopId()) ? flagStockDTO.getPrefixActionCode() + "_TTH_CN" : flagStockDTO.getPrefixActionCode();
            stockTransVoffice.setPrefixTemplate(prefixTemplatePath);
            //check phan quyen receiptNo
            String receiptNo = stockTransActionDTO.getActionCode();
            if (Const.STOCK_TRANS_STATUS.EXPORT_NOTE.equals(stockTransActionDTO.getStatus()) || Const.STOCK_TRANS_STATUS.IMPORT_NOTE.equals(stockTransActionDTO.getStatus())) {
                receiptNo = receiptNo.substring(receiptNo.lastIndexOf("_") + 1, receiptNo.length());
                if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
                    receiptNo = DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCodeShop()) ? receiptNo : stockTransActionDTO.getActionCodeShop();
                }
            }
            stockTransVoffice.setReceiptNo(receiptNo);
            stockTransVoffice.setActionCode(stockTransActionDTO.getActionCode());

        }
        return stockTransVoffice;
    }


    public void executeStockTransForPartnerBackUp(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        Date sysdate = DbUtil.getSysDate(em);
        //1. thuc hien validate du lieu nhap
        doValidate(importPartnerRequestDTO);

        Connection conn = IMCommonUtils.getDBIMConnection();
        conn.setAutoCommit(false);

        //2. Xu ly insert du lieu
        //2.1 Insert bang stock_trans
        StockTransDTO stockTransDTO = new StockTransDTO();
        StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();

        stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransDTO.setFromOwnerId(importPartnerRequestDTO.getPartnerId());
        stockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransDTO.setToOwnerId(importPartnerRequestDTO.getToOwnerId());

        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransDTO.setReasonId(importPartnerRequestDTO.getReasonId());
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER_PARTNER);
        stockTransDTO.setNote(importPartnerRequestDTO.getNote());
        stockTransDTO.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setLogicstic(importPartnerRequestDTO.isLogistic() ? Const.STOCK_TRANS.IS_LOGISTIC : null);
        SignOfficeDTO signOfficeDTO = importPartnerRequestDTO.getSignOfficeDTO();
        if (importPartnerRequestDTO.getSignOfficeDTO() != null) {
            stockTransDTO.setUserName(signOfficeDTO.getUserName());
            stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
            stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
            stockTransDTO.setSignVoffice(true);
            stockTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        StockTransDTO stockTransImpVT = stockTransService.save(stockTransDTO);

        //2.2 Insert bang stock_trans_action
        stockTransActionDTO.setStockTransId(stockTransImpVT.getStockTransId());
        stockTransActionDTO.setActionCode(importPartnerRequestDTO.getActionCode());
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER_PARTNER);
        stockTransActionDTO.setNote(importPartnerRequestDTO.getNote());
        stockTransActionDTO.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
        stockTransActionDTO.setCreateDatetime(sysdate);
        stockTransActionDTO.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
        StockTransActionDTO stockTransActionDTOImpVT = stockTransActionService.save(stockTransActionDTO);

        //2.3 thuc hien ky vOffice
        FlagStockDTO flagStockDTO = new FlagStockDTO();
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
        if (importPartnerRequestDTO.getSignOfficeDTO() != null) {
            doSignVoffice(stockTransDTO, stockTransActionDTOImpVT, requiredRoleMap, flagStockDTO);
        }
        //2.4 thuc hien dong bo du lieu logistic
        if (importPartnerRequestDTO.isLogistic()) {
            StockTransLogisticDTO stockTransLogistic = new StockTransLogisticDTO();
            stockTransLogistic.setRequestTypeLogistic(Const.LOGISTIC.REQ_TYPE_NEW);
            stockTransLogistic.setCreateDate(stockTransImpVT.getCreateDatetime());
            stockTransLogistic.setStockTransId(stockTransImpVT.getStockTransId());
            stockTransLogistic.setStatus(Const.LOGISTIC.STATUS_NEW);
            stockTransLogisticService.save(stockTransLogistic);
        }


        //11. Thuc hien thay doi so phieu
        doIncreaseStockNum(stockTransActionDTOImpVT, flagStockDTO.getPrefixActionCode(), requiredRoleMap);

        StockTransDetailDTO stockTransDetailDTO;
        StockTransDetailKcsDTO stockTransDetailKcsDTO;
        List<ProductOfferingDTO> lsProdOfferDto = importPartnerRequestDTO.getLsProductOfferDto();

        //2.6 insert bang partner_contract
        PartnerContractDTO partnerContractDTO = new PartnerContractDTO();
        partnerContractDTO.setStockTransId(stockTransImpVT.getStockTransId());
        partnerContractDTO.setActionCode(importPartnerRequestDTO.getActionCode());
        partnerContractDTO.setCreateDate(sysdate);
        partnerContractDTO.setLastModified(sysdate);
        partnerContractDTO.setPoCode(importPartnerRequestDTO.getPoCode());
        partnerContractDTO.setPoDate(importPartnerRequestDTO.getPoDate());
        partnerContractDTO.setContractCode(importPartnerRequestDTO.getContractCode());
        partnerContractDTO.setContractDate(importPartnerRequestDTO.getContractDate());
        partnerContractDTO.setImportDate(importPartnerRequestDTO.getImportDate());
        partnerContractDTO.setDeliveryLocation(importPartnerRequestDTO.getDeliveryLocation());
        partnerContractDTO.setRequestDate(importPartnerRequestDTO.getRequestDate());
        partnerContractDTO.setPartnerId(importPartnerRequestDTO.getPartnerId());
        partnerContractDTO.setCurrencyType(importPartnerRequestDTO.getCurrencyType());
        partnerContractDTO.setCurrencyRate(importPartnerRequestDTO.getCurrencyRate());
        partnerContractDTO.setKcsNo(importPartnerRequestDTO.getCodeKCS());
        partnerContractDTO.setKcsDate(importPartnerRequestDTO.getDateKCS());
        partnerContractDTO.setOrderCode(importPartnerRequestDTO.getCodePackage());
        partnerContractDTO.setStartDateWarranty(importPartnerRequestDTO.getEndDate());
        PartnerContractDTO partnerContractDTOSave = partnerContractService.save(partnerContractDTO);
        for (ProductOfferingDTO offeringDTO : lsProdOfferDto) {
            //2.7 insert partner contract detail
            PartnerContractDetailDTO partnerContractDetailDTO = new PartnerContractDetailDTO();
            partnerContractDetailDTO.setPartnerContractId(partnerContractDTOSave.getPartnerContractId());
            partnerContractDetailDTO.setCreateDate(sysdate);
            partnerContractDetailDTO.setProdOfferId(offeringDTO.getProductOfferingId());
            partnerContractDetailDTO.setStateId(offeringDTO.getStateId());
            partnerContractDetailDTO.setQuantity(offeringDTO.getQuantityKtts());
            partnerContractDetailDTO.setCurrencyType(importPartnerRequestDTO.getCurrencyType());
            partnerContractDetailDTO.setUnitPrice(offeringDTO.getPrice());
            partnerContractDetailDTO.setOrderCode(importPartnerRequestDTO.getCodePackage());
            partnerContractDetailDTO.setStartDateWarranty(importPartnerRequestDTO.getEndDate());
            partnerContractDetailService.save(partnerContractDetailDTO);
        }
        //2.6 insert bang stock_trans_detail, neu la mat hang kit thi sinh them giao dich rieng cho mat hang sim
        Long stockTransInsert = stockTransImpVT.getStockTransId();
        for (ProductOfferingDTO offeringDTO : lsProdOfferDto) {
            offeringDTO.setQuantityKtts(DataUtil.safeToLong(offeringDTO.getStrQuantityKcs()));
            //2.5.1 insert stockTrans
            stockTransDetailDTO = new StockTransDetailDTO();
            stockTransDetailDTO.setStockTransId(stockTransInsert);
            stockTransDetailDTO.setProdOfferId(offeringDTO.getProductOfferingId());
            stockTransDetailDTO.setProdOfferIdSwap(offeringDTO.isLogistic() ? offeringDTO.getProdOfferSimId() : null);
            stockTransDetailDTO.setStateId(Const.GOODS_STATE.NEW);
            stockTransDetailDTO.setQuantity(offeringDTO.getQuantity());
            stockTransDetailDTO.setAmount(0L);
            stockTransDetailDTO.setPrice(offeringDTO.getPrice());
            stockTransDetailDTO.setCreateDatetime(sysdate);
            StockTransDetailDTO stockTransDetailDTOImpVT = stockTransDetailService.save(stockTransDetailDTO);

            //2.5.2 tao giao dich xuat sim neu la mat hang kit

            StockTransActionDTO stockTransActionExpSim = null;
            StockTransDTO stockTransExpSim = null;
            StockTransDetailDTO stockTransDetailExpSim = null;
            if (Const.STOCK_TYPE.STOCK_KIT.equals(offeringDTO.getProductOfferTypeId())) {
                //2.5.2.1 tao moi giao dich stock_trans cho gd sim
                stockTransExpSim = new StockTransDTO();
                stockTransExpSim.setFromOwnerId(importPartnerRequestDTO.getToOwnerId());
                stockTransExpSim.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockTransExpSim.setToOwnerId(importPartnerRequestDTO.getToOwnerId());
                stockTransExpSim.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockTransExpSim.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
                stockTransExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransExpSim.setCreateDatetime(sysdate);
                stockTransExpSim.setReasonId(Const.REASON_ID.EXP_SIM_IMP_FROM_PARTNER);
                stockTransExpSim.setNote(GetTextFromBundleHelper.getText("import.partner.exportNote"));
                stockTransExpSim.setStaffId(importPartnerRequestDTO.getImportStaffId());
                stockTransExpSim.setUserCreate(importPartnerRequestDTO.getImportStaffCode());
                StockTransDTO stockTransExpSimInsert = stockTransService.save(stockTransDTO);
                //2.5.2.2 tao moi 3 stock_trans_action cho gd sim
                stockTransActionExpSim = new StockTransActionDTO();
                stockTransActionExpSim.setActionCode(importPartnerRequestDTO.getActionCode());
                stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                stockTransActionExpSim.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
                stockTransActionExpSim.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
                stockTransActionExpSim.setCreateDatetime(sysdate);
                stockTransActionExpSim.setStockTransId(stockTransExpSimInsert.getStockTransId());
                stockTransActionService.save(stockTransActionExpSim);

                stockTransActionExpSim.setActionCode(null);
                stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                stockTransExpSim.setNote(null);
                stockTransActionService.save(stockTransActionExpSim);

                stockTransActionExpSim.setActionCode(null);
                stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransExpSim.setNote(null);
                stockTransActionService.save(stockTransActionExpSim);
                //2.5.2.2 tao moi 3 stock_trans_detail cho gd sim
                stockTransDetailExpSim = new StockTransDetailDTO();
                stockTransDetailExpSim.setStockTransId(stockTransExpSim.getStockTransId());
                stockTransDetailExpSim.setProdOfferId(offeringDTO.getProdOfferSimId());
                stockTransDetailExpSim.setStateId(Const.GOODS_STATE.NEW);
                stockTransDetailExpSim.setQuantity(0L);
                stockTransDetailExpSim.setAmount(0L);
                stockTransDetailExpSim.setPrice(0L);
                stockTransDetailService.save(stockTransDetailExpSim);
            }
            if (!importPartnerRequestDTO.isLogistic()) {
                //Cong so luong STOCK_TOTAL cho kho nhan
                flagStockDTO.setImportStock(true);
                flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                Long quantity = DataUtil.isNullOrZero(DataUtil.safeToLong(offeringDTO.getStrQuantityKcs())) ? offeringDTO.getQuantity() : offeringDTO.getAvailableQuantity();
                stockTransDetailDTOImpVT.setQuantity(quantity);
                //2.5.3 INSERT du lieu cac bang STOCK_X
                if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_QUANTITY, offeringDTO.getImpType())) {
                    //luong nay luon chay cap nhap stock_total
                    doSaveStockTotal(stockTransImpVT, Lists.newArrayList(stockTransDetailDTOImpVT), flagStockDTO, stockTransActionDTOImpVT, conn);
                } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_FILE, offeringDTO.getImpType())) {
                    importSerialByFile(importPartnerRequestDTO, stockTransImpVT, stockTransActionDTOImpVT, stockTransDetailDTOImpVT,
                            stockTransExpSim, stockTransActionExpSim, stockTransDetailExpSim, offeringDTO, conn, sysdate);
                } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE, offeringDTO.getImpType())) {
                    importSerialByRange(importPartnerRequestDTO, stockTransImpVT, stockTransActionDTOImpVT, stockTransDetailDTOImpVT,
                            stockTransExpSim, stockTransActionExpSim, stockTransDetailExpSim, offeringDTO, conn, sysdate);
                }
            }

            //2.5.6 insert stockTransDetailKcs
            if (!DataUtil.isNullOrZero(offeringDTO.getQuantityKtts())) {
                stockTransDetailKcsDTO = new StockTransDetailKcsDTO();
                stockTransDetailKcsDTO.setQuantity(offeringDTO.getQuantityKtts());
                stockTransDetailKcsDTO.setCreateDatetime(sysdate);
                stockTransDetailKcsDTO.setStockTransId(stockTransInsert);
                stockTransDetailKcsDTO.setProdOfferId(offeringDTO.getProductOfferingId());
                stockTransDetailKcsService.save(stockTransDetailKcsDTO);
            }
        }


        //2.8 xu ly insert so luong detail

        /*BaseMessage messageKtts = kttsService.getResultImpShipment(importPartnerRequestDTO.getCodeKCS(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        if (!messageKtts.isSuccess()) {
            LogicException ex = new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "");
            ex.setDescription(messageKtts.getDescription());
            throw ex;
        }*/
    }

    /**
     * ham xu ly insert serial theo file
     *
     * @param importPartnerRequestDTO
     * @param stockTransImpVT
     * @param stockTransActionDTOImpVT
     * @param stockTransDetailDTOImpVT
     * @param stockTransExpSim
     * @param stockTransActionExpSim
     * @param stockTransDetailExpSim
     * @param offeringDTO
     * @param conn
     * @param sysDate
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    public Long importSerialByFile(ImportPartnerRequestDTO importPartnerRequestDTO, StockTransDTO stockTransImpVT, StockTransActionDTO stockTransActionDTOImpVT,
                                   StockTransDetailDTO stockTransDetailDTOImpVT, StockTransDTO stockTransExpSim, StockTransActionDTO stockTransActionExpSim,
                                   StockTransDetailDTO stockTransDetailExpSim, ProductOfferingDTO offeringDTO, Connection conn, Date sysDate) throws Exception {
        String tableName = IMServiceUtil.getTableNameByOfferType(offeringDTO.getProductOfferTypeId());
        List<HashMap<String, String>> lstMapParam = getMapProfileSerial(offeringDTO.getLstParam(), offeringDTO.getListProfile());

        PreparedStatement insertStock = null;
        PreparedStatement insertStockTransSerial = null;
        Long numberSuccessRecord = 0L;
        Long numberErrorRecord = 0L;
        try {

            StringBuilder fieldNameList = new StringBuilder();
            StringBuilder fieldDataList = new StringBuilder();
            List<String> lstProfile = new ArrayList();
            ProfileDTO profileDTO = productOfferingService.getProfileByProductId(offeringDTO.getProductOfferingId());
            if (profileDTO != null) {
                lstProfile = profileDTO.getLstColumnName();
            }

            StringBuilder strInsert = new StringBuilder();
            strInsert.append(" INSERT INTO ");
            strInsert.append(tableName);
            strInsert.append("(ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, PROD_OFFER_ID, CREATE_DATE, CREATE_USER, TELECOM_SERVICE_ID,UPDATE_DATETIME ");

            // Chi them thong tin hop dong cho 1 so bang nay thoi: STOCK_SIM, STOCK_CARD, STOCK_HANDSET, STOCK_KIT
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                    || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                fieldNameList.append(",STOCK_TRANS_ID, CONTRACT_CODE, PO_CODE");
                fieldDataList.append(",?, ?, ?");
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                    fieldNameList.append(",A3A8, KIND, HLR_STATUS, AUC_STATUS, SIM_TYPE, SIM_MODEL_TYPE");
                    fieldDataList.append(",?, ?, ?, ?, ?, ?");
                }
            }

            //Them thong tin profile
            for (int i = 0; i < lstProfile.size(); i++) {
                fieldNameList.append(",");
                fieldNameList.append(lstProfile.get(i));
                fieldDataList.append(",?");
            }

            strInsert.append(fieldNameList);
            strInsert.append(")");
            strInsert.append(" VALUES (");
            strInsert.append(tableName + "_SEQ.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ? ");
            strInsert.append(fieldDataList);
            strInsert.append(")");
            strInsert.append(" LOG ERRORS INTO ERR$_" + tableName + " ('INSERT') REJECT LIMIT UNLIMITED ");

            insertStock = conn.prepareStatement(strInsert.toString());

            boolean isCard = false;
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)) {
                isCard = true;
            }

            Long numberBatch = 0L;
            StringBuilder strQueryInsertStockTransSerial = new StringBuilder();
            strQueryInsertStockTransSerial.append("insert into STOCK_TRANS_SERIAL (STOCK_TRANS_SERIAL_ID, STOCK_TRANS_DETAIL_ID, FROM_SERIAL, TO_SERIAL, QUANTITY, CREATE_DATETIME, STOCK_TRANS_ID, PROD_OFFER_ID, STATE_ID) ");
            strQueryInsertStockTransSerial.append("values (STOCK_TRANS_SERIAL_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?) ");
            insertStockTransSerial = conn.prepareStatement(strQueryInsertStockTransSerial.toString());
            for (int i = 0; i < lstMapParam.size(); i++) {
                HashMap<String, String> mapParam = lstMapParam.get(i);
                insertStock.setLong(1, Const.OWNER_TYPE.SHOP_LONG);
                insertStock.setLong(2, stockTransImpVT.getToOwnerId());
                insertStock.setLong(3, Long.valueOf(Const.STATUS_ACTIVE));
                insertStock.setLong(4, stockTransDetailDTOImpVT.getStateId());
                insertStock.setLong(5, stockTransDetailDTOImpVT.getProdOfferId());
                insertStock.setDate(6, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                insertStock.setString(7, stockTransImpVT.getUserCreate() == null ? "" : stockTransImpVT.getUserCreate());
                insertStock.setLong(8, offeringDTO.getTelecomServiceId());
                insertStock.setDate(9, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                        || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                    insertStock.setLong(10, stockTransImpVT.getStockTransId());
                    insertStock.setString(11, importPartnerRequestDTO.getContractCode());
                    insertStock.setString(12, importPartnerRequestDTO.getPoCode() == null ? "" : importPartnerRequestDTO.getPoCode());
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                        insertStock.setString(13, null);
                        insertStock.setString(14, null);
                        insertStock.setString(15, Const.HLR_STATUS_DEFAULT);
                        insertStock.setString(16, Const.AUC_STATUS_DEFAULT);
                        insertStock.setLong(17, offeringDTO.getProductOfferTypeId());
                        if (stockTransDetailDTOImpVT.getProdOfferIdSwap() != null) {
                            insertStock.setLong(18, stockTransDetailDTOImpVT.getProdOfferIdSwap());
                        } else {
                            insertStock.setNull(18, Types.LONGVARCHAR);
                        }
                    }
                }

                for (int j = 0; j < lstProfile.size(); j++) {
                    if (DataUtil.safeEqual(lstProfile.get(j), "SERIAL")) {
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)
                                || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                            insertStock.setString(j + 13, formatSerial(mapParam.get(lstProfile.get(j).trim()), isCard));
                        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(j + 19, formatSerial(mapParam.get(lstProfile.get(j).trim()), isCard));
                        } else {
                            insertStock.setString(j + 10, formatSerial(mapParam.get(lstProfile.get(j).trim()), isCard));
                        }
                    } else {
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)
                                || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                            insertStock.setString(j + 13, mapParam.get(lstProfile.get(j).trim()));
                        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(j + 19, mapParam.get(lstProfile.get(j).trim()));
                        } else {
                            insertStock.setString(j + 10, mapParam.get(lstProfile.get(j).trim()));
                        }
                    }
                }


                insertStock.addBatch();

                //insert bang stock_trans_serial
                strQueryInsertStockTransSerial.append("insert into STOCK_TRANS_SERIAL (STOCK_TRANS_SERIAL_ID," +
                        " STOCK_TRANS_DETAIL_ID, FROM_SERIAL, TO_SERIAL, QUANTITY, CREATE_DATETIME, STOCK_TRANS_ID, PROD_OFFER_ID, STATE_ID) ");
                insertStockTransSerial.setLong(1, stockTransDetailDTOImpVT.getStockTransDetailId());
                insertStockTransSerial.setString(2, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setString(3, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setLong(4, 1L);
                insertStockTransSerial.setDate(5, new java.sql.Date(sysDate.getTime()));
                insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                insertStockTransSerial.setLong(7, offeringDTO.getProductOfferingId());
                insertStockTransSerial.setLong(8, Const.GOODS_STATE.NEW);
                insertStockTransSerial.addBatch();

                //Xuat SIM neu la giao dich import KIT
                if (offeringDTO.isLogistic() && Const.STOCK_TYPE.STOCK_KIT.equals(offeringDTO.getProductOfferTypeId())) {
                    insertStockTransSerial.setLong(1, stockTransDetailDTOImpVT.getStockTransDetailId());
                    insertStockTransSerial.setString(2, mapParam.get("SERIAL"));
                    insertStockTransSerial.setString(3, mapParam.get("SERIAL"));
                    insertStockTransSerial.setLong(4, 1L);
                    insertStockTransSerial.setDate(5, new java.sql.Date(sysDate.getTime()));
                    insertStockTransSerial.setLong(6, stockTransExpSim.getStockTransId());
                    insertStockTransSerial.setLong(7, offeringDTO.getProductOfferingId());
                    insertStockTransSerial.setLong(8, Const.GOODS_STATE.NEW);
                    insertStockTransSerial.addBatch();
                }

                //Khi so luong du 1 batch, commit batch do
                if (i % Const.DEFAULT_BATCH_SIZE == 0 && i > 0) {
                    boolean hasErrorInBatch = false; //truong hop co loi xay ra
                    Long tmpErrorRecordInBatch = 0L;
                    Long tmpSuccessRecordInBatch = 0L;
                    try {
                        Long startStockKitId = DbUtil.getSequence(em, "STOCK_KIT_SEQ");
                        int[] resultInsertStock = insertStock.executeBatch();
                        //so ban ghi insert thanh cong
                        int tmpErrorRecord = countNumberError(tableName, conn, stockTransImpVT.getStockTransId());
                        tmpErrorRecordInBatch = tmpErrorRecord - numberErrorRecord;
                        tmpSuccessRecordInBatch = Const.DEFAULT_BATCH_SIZE - tmpErrorRecord;

                        if (tmpSuccessRecordInBatch <= 0) {
                            continue;
                        }

                        //Neu la giao dich import KIT va co thong tin serial, isdn, thuc hien update bang stock_sim, stock_number
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {

                            //update trang thai SIM thanh da ban
                            int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), stockTransImpVT);
                            if (resultUpdateStockSim > 0) {
                                //Cap nhat tru kho stock_total
                                FlagStockDTO flagStockDTO = new FlagStockDTO();
                                flagStockDTO.setExportStock(true);
                                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), flagStockDTO);
                                StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpSim, stockTransActionExpSim);
                                changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);
                            }

                            //update thong tin bang stock_number
                            updateStockNumber(startStockKitId, conn, stockTransImpVT);
                        }

                        FlagStockDTO flagStockDTO = new FlagStockDTO();
                        flagStockDTO.setImportStock(true);
                        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

                        //Cong so luong vao kho nhan
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, Lists.newArrayList(stockTransDetailDTOImpVT), flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionDTOImpVT);
                        changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);

                        //Update bang stockTransDetail nhu the nao ????

                        int[] resultInsertStockTransSerial = insertStockTransSerial.executeBatch();
                        for (int j = 0; j < resultInsertStockTransSerial.length; j++) {
                            if (resultInsertStockTransSerial[j] == PreparedStatement.EXECUTE_FAILED) {
                                hasErrorInBatch = true;
                                break;
                            }
                        }

                        if (hasErrorInBatch) {
                            conn.rollback();
                        } else {
                            conn.commit();
                        }
                        ++numberBatch;
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        hasErrorInBatch = true;
                        conn.rollback();
                    }
                    if (hasErrorInBatch) {
                        tmpSuccessRecordInBatch = 0L;
                        tmpErrorRecordInBatch = Const.DEFAULT_BATCH_SIZE;
                    }
                    numberSuccessRecord += tmpSuccessRecordInBatch;
                    numberErrorRecord += tmpErrorRecordInBatch;
                }
            }

            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = lstMapParam.size() - numberBatch * Const.DEFAULT_BATCH_SIZE;
            if (numberRemainRecord > 0) {
                boolean hasErrorInBach = false; //truong hop co loi xay ra
                Long tmpErrorRecordInBatch = 0L;
                Long tmpSuccessRecordInBatch = 0L;
                try {
                    Long startStockKitId = DbUtil.getSequence(em, "STOCK_KIT_SEQ");
                    insertStock.executeBatch();
                    //so ban ghi insert thanh cong
                    int tmpErrorRecord = countNumberError(tableName, conn, stockTransImpVT.getStockTransId());
                    tmpErrorRecordInBatch = tmpErrorRecord - numberErrorRecord;
                    tmpSuccessRecordInBatch = numberRemainRecord - tmpErrorRecordInBatch;
                    if (tmpSuccessRecordInBatch > 0) {
                        //Neu la giao dich import KIT va co thong tin serial, isdn, thuc hien update bang stock_sim, stock_number
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                            //update trang thai SIM thanh da ban
                            int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), stockTransImpVT);

                            if (resultUpdateStockSim > 0) {
                                //Tru StockTotal cho kho nhan
                                FlagStockDTO flagStockDTO = new FlagStockDTO();
                                flagStockDTO.setExportStock(true);
                                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), flagStockDTO);
                                StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpSim, stockTransActionExpSim);
                                changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);
                            }

                            //update thong tin bang stock_number
                            updateStockNumber(startStockKitId, conn, stockTransImpVT);
                        }

                        FlagStockDTO flagStockDTO = new FlagStockDTO();
                        flagStockDTO.setImportStock(true);
                        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

                        //Cong so luong vao kho nhan
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, Lists.newArrayList(stockTransDetailDTOImpVT), flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionDTOImpVT);
                        changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);

                        //Update bang stockTransDetail nhu the nao ????

                        int[] resultInsertStockTransSerial = insertStockTransSerial.executeBatch();
                        for (int j = 0; j < resultInsertStockTransSerial.length; j++) {
                            if (resultInsertStockTransSerial[j] == PreparedStatement.EXECUTE_FAILED) {
                                hasErrorInBach = true;
                                break;
                            }
                        }
                    }

                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    hasErrorInBach = true;
                    conn.rollback();
                }
                if (hasErrorInBach) {
                    tmpSuccessRecordInBatch = 0L;
                    tmpErrorRecordInBatch = numberRemainRecord;
                }
                numberSuccessRecord += tmpSuccessRecordInBatch;
                numberErrorRecord += tmpErrorRecordInBatch;
            }
            return numberSuccessRecord;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
            if (insertStockTransSerial != null) {
                insertStockTransSerial.close();
            }
        }

    }


    /**
     * ham xu ly insert serial theo dai
     *
     * @param importPartnerRequestDTO
     * @param stockTransImpVT
     * @param stockTransActionDTOImpVT
     * @param stockTransDetailDTOImpVT
     * @param stockTransExpSim
     * @param stockTransActionExpSim
     * @param stockTransDetailExpSim
     * @param offeringDTO
     * @param conn
     * @param sysDate
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    @Transactional(rollbackFor = Exception.class)
    public Long importSerialByRange(ImportPartnerRequestDTO importPartnerRequestDTO, StockTransDTO stockTransImpVT, StockTransActionDTO stockTransActionDTOImpVT,
                                    StockTransDetailDTO stockTransDetailDTOImpVT, StockTransDTO stockTransExpSim, StockTransActionDTO stockTransActionExpSim,
                                    StockTransDetailDTO stockTransDetailExpSim, ProductOfferingDTO offeringDTO, Connection conn, Date sysDate) throws Exception {
        String tableName = IMServiceUtil.getTableNameByOfferType(offeringDTO.getProductOfferTypeId());
        PreparedStatement insertStock = null;
        PreparedStatement insertStockTransSerial = null;
        try {
            List<StockTransSerialDTO> lstStockTransSerialDTO = offeringDTO.getListStockTransSerialDTOs();
            if (DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
                return 0L;
            }

            //Tao cau insert stock_x
            StringBuilder fieldNameList = new StringBuilder();
            StringBuilder fieldDataList = new StringBuilder();
            StringBuilder strInsert = new StringBuilder();
            strInsert.append(" INSERT INTO ");
            strInsert.append(tableName);
            strInsert.append("(ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, PROD_OFFER_ID, CREATE_DATE, CREATE_USER, TELECOM_SERVICE_ID, SERIAL, UPDATE_DATETIME ");

            // Chi them thong tin hop dong cho 1 so bang nay thoi: STOCK_SIM, STOCK_CARD, STOCK_HANDSET, STOCK_KIT
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)
                    || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)
                    || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)
                    || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                fieldNameList.append(",STOCK_TRANS_ID, CONTRACT_CODE, PO_CODE");
                fieldDataList.append(",?, ?, ?");
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                    fieldNameList.append(",A3A8, KIND, HLR_STATUS, AUC_STATUS, SIM_TYPE, SIM_MODEL_TYPE");
                    fieldDataList.append(",?, ?, ?, ?, ?, ?");
                }
            }

            strInsert.append(fieldNameList);

            strInsert.append(")");
            strInsert.append(" VALUES (");
            strInsert.append(tableName + "_SEQ.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
            strInsert.append(fieldDataList);
            strInsert.append(")");
            //strInsert.append(" log errors reject limit unlimited ");
            strInsert.append(" LOG ERRORS INTO ERR$_" + tableName + " ('INSERT') REJECT LIMIT UNLIMITED ");

            insertStock = conn.prepareStatement(strInsert.toString());
            ProductOfferingDTO productOffering = productOfferingService.findOne(offeringDTO.getProductOfferingId());

            boolean isCard = false;
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)) {
                isCard = true;
            }

            Long numberBatch = 0L;
            int numberSerial = 0;
            Long numberSuccessRecord = 0L;
            Long numberErrorRecord = 0L;
            String fromSerialInBatch = "";
            String toSerialInBatch = "";
            StringBuilder strQueryInsertStockTransSerial = new StringBuilder();
            strQueryInsertStockTransSerial.append("insert into STOCK_TRANS_SERIAL (STOCK_TRANS_SERIAL_ID, STOCK_TRANS_DETAIL_ID, FROM_SERIAL, TO_SERIAL, QUANTITY, CREATE_DATETIME, STOCK_TRANS_ID, PROD_OFFER_ID, STATE_ID) ");
            strQueryInsertStockTransSerial.append("values (STOCK_TRANS_SERIAL_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?) ");
            insertStockTransSerial = conn.prepareStatement(strQueryInsertStockTransSerial.toString());

            for (int i = 0; i < lstStockTransSerialDTO.size(); i++) {
                StockTransSerialDTO stockTransSerialDTO = lstStockTransSerialDTO.get(i);
                BigInteger fromSerial = new BigInteger(stockTransSerialDTO.getFromSerial());
                BigInteger toSerial = new BigInteger(stockTransSerialDTO.getToSerial());
                BigInteger currentSerial = fromSerial;
                while (currentSerial.compareTo(toSerial) <= 0) {
                    numberSerial += 1;
                    if ("".equals(fromSerialInBatch)) {
                        fromSerialInBatch = formatSerial(currentSerial.toString(), isCard);
                    }
                    toSerialInBatch = formatSerial(currentSerial.toString(), isCard);

                    insertStock.setLong(1, Const.OWNER_TYPE.SHOP_LONG);
                    insertStock.setLong(2, stockTransImpVT.getToOwnerId());
                    insertStock.setString(3, Const.STATUS_ACTIVE);
                    insertStock.setLong(4, stockTransDetailDTOImpVT.getStateId());
                    insertStock.setLong(5, stockTransDetailDTOImpVT.getProdOfferId());
                    insertStock.setDate(6, new java.sql.Date(sysDate.getTime()));
                    insertStock.setString(7, stockTransImpVT.getUserCreate());
                    insertStock.setLong(8, productOffering.getTelecomServiceId());
                    insertStock.setString(9, formatSerial(currentSerial.toString(), isCard));
                    insertStock.setDate(10, new java.sql.Date(sysDate.getTime()));
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                            || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                        insertStock.setLong(11, stockTransImpVT.getStockTransId());
                        insertStock.setString(12, importPartnerRequestDTO.getContractCode());
                        insertStock.setString(13, importPartnerRequestDTO.getPoCode());
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(14, null);
                            insertStock.setString(15, null);
                            insertStock.setString(16, Const.HLR_STATUS_DEFAULT);
                            insertStock.setString(17, Const.AUC_STATUS_DEFAULT);
                            insertStock.setLong(18, offeringDTO.getProductOfferTypeId());
                            insertStock.setLong(19, stockTransDetailDTOImpVT.getProdOfferIdSwap());
                        }

                    }

                    insertStock.addBatch();

                    //Khi so luong du 1 batch, commit batch do
                    if (numberSerial % Const.DEFAULT_BATCH_SIZE == 0) {
                        boolean hasErrorInBach = false; //truong hop co loi xay ra
                        Long tmpErrorRecordInBatch = 0L;
                        Long tmpSuccessRecordInBatch = 0L;
                        try {
                            Long startStockKitId = DbUtil.getSequence(em, "STOCK_KIT_SEQ");
                            insertStock.executeBatch();
                            //so ban ghi insert thanh cong
                            int tmpErrorRecord = countNumberError(tableName, conn, stockTransImpVT.getStockTransId());
                            tmpErrorRecordInBatch = tmpErrorRecord - numberErrorRecord;
                            tmpSuccessRecordInBatch = Const.DEFAULT_BATCH_SIZE - tmpErrorRecord;
                            if (tmpSuccessRecordInBatch <= 0) {
                                continue;
                            }
                            //Nhap hang kho nhan
                            insertStockTransSerial.setLong(1, stockTransDetailDTOImpVT.getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                            insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                            insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                            insertStockTransSerial.setLong(7, stockTransDetailDTOImpVT.getProdOfferId());
                            insertStockTransSerial.setLong(8, stockTransDetailDTOImpVT.getStateId());
                            insertStockTransSerial.addBatch();

                            //Xuat SIM neu la giao dich import KIT
                            if (offeringDTO.isLogistic() && DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                                insertStockTransSerial.setLong(1, stockTransDetailExpSim.getStockTransDetailId());
                                insertStockTransSerial.setString(2, fromSerialInBatch);
                                insertStockTransSerial.setString(3, toSerialInBatch);
                                insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                                insertStockTransSerial.setDate(5, new java.sql.Date(sysDate.getTime()));
                                insertStockTransSerial.setLong(6, stockTransExpSim.getStockTransId());
                                insertStockTransSerial.setLong(7, stockTransDetailExpSim.getProdOfferId());
                                insertStockTransSerial.setLong(8, stockTransDetailExpSim.getStateId());
                                insertStockTransSerial.addBatch();
                            }

                            //Neu la giao dich import KIT va co thong tin serial, isdn, thuc hien update bang stock_sim, stock_number
                            if (offeringDTO.isLogistic() && DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                                //update trang thai SIM thanh da ban
                                int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), stockTransImpVT);

                                if (resultUpdateStockSim > 0) {
                                    //Tru StockTotal cho kho nhan
                                    FlagStockDTO flagStockDTO = new FlagStockDTO();
                                    flagStockDTO.setExportStock(true);
                                    flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                    flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                    StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), flagStockDTO);
                                    StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpSim, stockTransActionExpSim);
                                    changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);
                                }

                                //update thong tin bang stock_number
                                updateStockNumber(startStockKitId, conn, stockTransImpVT);
                            }

                            FlagStockDTO flagStockDTO = new FlagStockDTO();
                            flagStockDTO.setImportStock(true);
                            flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                            flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

                            //Cong so luong vao kho nhan
                            StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, Lists.newArrayList(stockTransDetailDTOImpVT), flagStockDTO);
                            StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionDTOImpVT);
                            changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);
                            //Update bang stockTransDetail nhu the nao ????

                            int[] resultInsertStockTransSerial = insertStockTransSerial.executeBatch();
                            for (int j = 0; j < resultInsertStockTransSerial.length; j++) {
                                if (resultInsertStockTransSerial[j] == PreparedStatement.EXECUTE_FAILED) {
                                    hasErrorInBach = true;
                                    break;
                                }
                            }
                            if (hasErrorInBach) {
                                conn.rollback();
                            } else {
                                conn.commit();
                            }
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                            hasErrorInBach = true;
                            conn.rollback();
                        }

                        ++numberBatch;
                        if (hasErrorInBach) {
                            tmpSuccessRecordInBatch = 0L;
                            tmpErrorRecordInBatch = Const.DEFAULT_BATCH_SIZE;
                        }
                        numberSuccessRecord += tmpSuccessRecordInBatch;
                        numberErrorRecord += tmpErrorRecordInBatch;
                        fromSerialInBatch = "";
                        toSerialInBatch = "";
                    }
                    currentSerial = currentSerial.add(BigInteger.ONE);
                }


            }
            //Xu ly voi so ban ghi con lai
            Long numberRemainRecord = DataUtil.safeToLong(offeringDTO.getAvailableQuantity()) - numberBatch * Const.DEFAULT_BATCH_SIZE;
            if (numberRemainRecord > 0) {
                boolean hasErrorInBach = false; //truong hop co loi xay ra
                Long tmpErrorRecordInBatch = 0L;
                Long tmpSuccessRecordInBatch = 0L;
                try {
                    Long startStockKitId = DbUtil.getSequence(em, "STOCK_KIT_SEQ");
                    insertStock.executeBatch();
                    //so ban ghi insert thanh cong
                    int tmpErrorRecord = countNumberError(tableName, conn, stockTransImpVT.getStockTransId());
                    tmpErrorRecordInBatch = tmpErrorRecord - numberErrorRecord;
                    tmpSuccessRecordInBatch = numberRemainRecord - tmpErrorRecordInBatch;
                    //Insert thong tin serial voi cac ban ghi con lai
                    if (tmpSuccessRecordInBatch > 0) {
                        //Nhap hang kho nhan
                        insertStockTransSerial.setLong(1, stockTransDetailDTOImpVT.getStockTransDetailId());
                        insertStockTransSerial.setString(2, fromSerialInBatch);
                        insertStockTransSerial.setString(3, toSerialInBatch);
                        insertStockTransSerial.setLong(4, numberRemainRecord);
                        insertStockTransSerial.setDate(5, new java.sql.Date(sysDate.getTime()));
                        insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                        insertStockTransSerial.setLong(7, stockTransDetailDTOImpVT.getProdOfferId());
                        insertStockTransSerial.setLong(8, stockTransDetailDTOImpVT.getStateId());
                        insertStockTransSerial.addBatch();

                        //Xuat SIM neu la giao dich import KIT
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                            insertStockTransSerial.setLong(1, stockTransDetailExpSim.getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, numberRemainRecord);
                            insertStockTransSerial.setDate(5, new java.sql.Date(sysDate.getTime()));
                            insertStockTransSerial.setLong(6, stockTransExpSim.getStockTransId());
                            insertStockTransSerial.setLong(7, stockTransDetailExpSim.getProdOfferId());
                            insertStockTransSerial.setLong(8, stockTransDetailExpSim.getStateId());
                            insertStockTransSerial.addBatch();
                        }


                        //Neu la giao dich import KIT va co thong tin serial, isdn, thuc hien update bang stock_sim, stock_number
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                            //update trang thai SIM thanh da ban
                            int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), stockTransImpVT);

                            if (resultUpdateStockSim > 0) {
                                //Tru StockTotal cho kho nhan
                                FlagStockDTO flagStockDTO = new FlagStockDTO();
                                flagStockDTO.setExportStock(true);
                                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, Lists.newArrayList(stockTransDetailExpSim), flagStockDTO);
                                StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpSim, stockTransActionExpSim);
                                changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);
                            }

                            //update thong tin bang stock_number
                            updateStockNumber(startStockKitId, conn, stockTransImpVT);
                        }

                        FlagStockDTO flagStockDTO = new FlagStockDTO();
                        flagStockDTO.setImportStock(true);
                        flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                        flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);

                        //Cong so luong vao kho nhan
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, Lists.newArrayList(stockTransDetailDTOImpVT), flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionDTOImpVT);
                        changeStockTotal(initStockTotalDTO, stockTotalAuditDTO, conn);

                        //Update bang stockTransDetail nhu the nao ????

                        int[] resultInsertStockTransSerial = insertStockTransSerial.executeBatch();
                        for (int j = 0; j < resultInsertStockTransSerial.length; j++) {
                            if (resultInsertStockTransSerial[j] == PreparedStatement.EXECUTE_FAILED) {
                                hasErrorInBach = true;
                                break;
                            }
                        }
                    }

                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    hasErrorInBach = true;
                    conn.rollback();
                }
                if (hasErrorInBach) {
                    tmpSuccessRecordInBatch = 0L;
                    tmpErrorRecordInBatch = numberRemainRecord;
                }
                numberSuccessRecord += tmpSuccessRecordInBatch;
                numberErrorRecord += tmpErrorRecordInBatch;
            }

            return numberSuccessRecord;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (insertStock != null) {
                insertStock.close();
            }
            if (insertStockTransSerial != null) {
                insertStockTransSerial.close();
            }
        }
    }

    /**
     * ham xu ly lay map profile serial
     *
     * @param lstParam
     * @param lstProfile
     * @return
     * @author thanhnt77
     */
    private List<HashMap<String, String>> getMapProfileSerial(List<String> lstParam, List<String> lstProfile) {
        List<HashMap<String, String>> lstMapParam = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lstParam)) {
            for (int i = 0; i < lstParam.size(); i++) {
                HashMap<String, String> hashMap = new HashMap();
                String param = lstParam.get(i);
                String[] arrParam = param.split(Const.COMMA_SEPARATE);
                for (int j = 0; j < lstProfile.size(); j++) {
                    hashMap.put(lstProfile.get(j), arrParam[j]);
                }
                lstMapParam.add(hashMap);
            }
        }
        return lstMapParam;
    }


    public void doValidate(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        //Cac truong bat buoc nhap
        if (DataUtil.isNullObject(importPartnerRequestDTO) || DataUtil.isNullOrEmpty(importPartnerRequestDTO.getLsProductOfferDto())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getPartnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.partner.msg.require");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.shop");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getActionCode())
                || importPartnerRequestDTO.getActionCode().length() > 50
                || !DataUtil.validateStringByPattern(importPartnerRequestDTO.getActionCode(), Const.REGEX.CODE_REGEX)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "partner.create.underlying.report.ktts.validate.actionCode.msg");
        }
        if (!DataUtil.isNullObject(importPartnerRequestDTO.getPoCode())
                && (importPartnerRequestDTO.getPoCode().length() > 50 || !DataUtil.validateStringByPattern(importPartnerRequestDTO.getPoCode(), Const.REGEX.CODE_REGEX))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "partner.create.underlying.report.ktts.validate.pocode.msg");
        }

        //Cac truong khong hop le
        //Lay id mat hang
        for (ProductOfferingDTO offeringDTO : importPartnerRequestDTO.getLsProductOfferDto()) {
            if (DataUtil.isNullOrZero(offeringDTO.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.update.file.isdn.product.not.contains");
            }
            if (!DataUtil.isNullOrEmpty(offeringDTO.getStrQuantityKcs())) {
                if (!DataUtil.validateStringByPattern(offeringDTO.getStrQuantityKcs(), Const.REGEX.NUMBER_REGEX)
                        || DataUtil.safeToLong(offeringDTO.getStrQuantityKcs()).compareTo(0L) < 0
                        || DataUtil.safeToLong(offeringDTO.getStrQuantityKcs()).compareTo(offeringDTO.getQuantity()) >= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "partner.create.underlying.report.ktts.validate.quantity.kcs", offeringDTO.getName());
                }
            }
            ProductOfferingDTO productOffering = productOfferingService.findOne(offeringDTO.getProductOfferingId());
            if (DataUtil.isNullObject(productOffering)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.update.file.isdn.product.not.contains");
            }
            if (DataUtil.isNullObject(productOffering)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.update.file.isdn.product.not.contains");
            }
            Long prodOfferTypeId = productOffering.getProductOfferTypeId();
            //Neu mat hang la kit, phai chon loai sim
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT, prodOfferTypeId)) {
                if (DataUtil.isNullOrZero(offeringDTO.getProdOfferSimId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.sim.type.require");
                }
            }
        }
    }

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }
}

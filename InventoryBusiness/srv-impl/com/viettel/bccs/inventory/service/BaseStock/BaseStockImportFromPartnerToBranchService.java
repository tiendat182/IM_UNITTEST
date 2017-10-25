package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.repo.StaffRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HaiNT41 on 2/18/2016.
 */
@Service
public class BaseStockImportFromPartnerToBranchService extends BaseStockPartnerService {
    public static final Logger logger = Logger.getLogger(BaseStockImportFromPartnerToBranchService.class);
    @Autowired
    private ImportPartnerRequestService importPartnerRequestService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StaffService staffService;
    @Autowired
    BaseValidateService baseValidateService;

    @Autowired
    ProductWs productWs;

    @Autowired
    StaffRepo staffRepo;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    private StockTransDTO stockTransExpToBranch;
    private StockTransDTO stockTransRegion;
    private StockTransDTO stockTransImpVT;
    private StockTransDTO stockTransExpSim;
    private List<StockTransDetailDTO> lstStockTransDetailExpToBranch;
    private List<StockTransDetailDTO> lstStockTransDetailImpVT;
    private List<StockTransDetailDTO> lstStockTransDetailExpSim;
    private List<StockTransDetailDTO> lstStockTransDetailExpSimRegion;
    private List<StockTransDetailDTO> lstStockTransDetailPartnerRegion;
    private List<HashMap<String, String>> lstMapParam;

    private String tableName;
    Date sysdate;
//    boolean hasResultSuccess;

    @Transactional(rollbackFor = Exception.class)
    public Long executeStockTrans(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        //1.validate
        doValidate(importPartnerRequestDTO);

        //2.prepare data
        doPrepareData(importPartnerRequestDTO);

        //Check han muc cua giao dich nhap kho chi nhanh
        //set so luong = so luong xuat
        lstStockTransDetailExpToBranch.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());
        baseValidateService.doDebitValidate(stockTransExpToBranch, lstStockTransDetailExpToBranch);
        //set lai ve = 0
        lstStockTransDetailExpToBranch.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());

        Connection conn = null;
        try {
            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            //3.Insert cac bang giao dich
            //3.1.Tao giao dich xuat hang cho chi nhanh
            //3.1.1. Insert bang stock_trans, status = 5 : CN da lap phieu nhap
            insertStockTrans(stockTransExpToBranch, conn);
            //3.1.2. Insert 3 ban ghi vao bang stock_trans_action, status = 1,2,5
            StockTransActionDTO stockTransActionExpToBranch = new StockTransActionDTO();
            stockTransActionExpToBranch.setActionCode(Const.STOCK_TRANS.TRANS_CODE_LX + "_" + importPartnerRequestDTO.getActionCode());
            stockTransActionExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            stockTransActionExpToBranch.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
            stockTransActionExpToBranch.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransActionExpToBranch.setCreateDatetime(stockTransExpToBranch.getCreateDatetime());
            insertStockTransAction(stockTransExpToBranch, stockTransActionExpToBranch, conn);

            stockTransActionExpToBranch.setActionCode(Const.STOCK_TRANS.TRANS_CODE_PX + "_" + importPartnerRequestDTO.getActionCode());
            stockTransActionExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            insertStockTransAction(stockTransExpToBranch, stockTransActionExpToBranch, conn);

            stockTransActionExpToBranch.setActionCode(Const.STOCK_TRANS.TRANS_CODE_PN + "_" + importPartnerRequestDTO.getActionCode());
            stockTransActionExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            insertStockTransAction(stockTransExpToBranch, stockTransActionExpToBranch, conn);

            //3.1.3. Insert bang stock_trans_detail, quantity = 0 ???
            insertStockTransDetail(stockTransExpToBranch, lstStockTransDetailExpToBranch, conn);

            //3.2.Tao giao dich nhap hang tu doi tac cho VT
            //3.2.1. Insert bang stock_trans, status = 5 : VT da lap phieu nhap
            insertStockTrans(stockTransImpVT, conn);

            //3.2.2. Insert bang stock_trans_action, status = 5
            StockTransActionDTO stockTransActionImpVT = new StockTransActionDTO();
            stockTransActionImpVT.setActionCode(importPartnerRequestDTO.getActionCode());
            stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionImpVT.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
            stockTransActionImpVT.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransActionImpVT.setCreateDatetime(stockTransImpVT.getCreateDatetime());
            if (importPartnerRequestDTO.isSignVoffice()) {
                stockTransActionImpVT.setSignCaStatus(Const.SIGN_VOFFICE);
            }
            //
            insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
            //3.2.2.1 insert vao bang stock trans voffice
            if (importPartnerRequestDTO.isSignVoffice()) {
                insertVoffice(conn, importPartnerRequestDTO, stockTransImpVT, null);
            }

            //3.2.3. Insert bang stock_trans_detail, quantity = 0 ???
            insertStockTransDetail(stockTransImpVT, lstStockTransDetailImpVT, conn);

            //3.3. Tao giao dich xuat sim tra hang doi tac (neu product_offer_type_id = 8 : STOCK_KIT)
            StockTransActionDTO stockTransActionExpSim = new StockTransActionDTO();
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                //StockTrans
                insertStockTrans(stockTransExpSim, conn);
                //StockTransAction
                stockTransActionExpSim.setActionCode(importPartnerRequestDTO.getActionCode());
                stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                stockTransActionExpSim.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
                stockTransActionExpSim.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
                stockTransActionExpSim.setCreateDatetime(stockTransExpSim.getCreateDatetime());
                insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);
                //StockTransDetail
                insertStockTransDetail(stockTransExpSim, lstStockTransDetailExpSim, conn);
            }

            //5.INSERT du lieu cac bang STOCK_X
            List<StockTransSerialDTO> lstStockTransSerialDTO = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO();
            Long resultUpdate = 0L;
            if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_QUANTITY, importPartnerRequestDTO.getImpType())) {
                //Truong hop nhap theo so luong : list<StockTransSerial> null
                //Cong so luong STOCK_TOTAL cho chi nhanh
                FlagStockDTO flagStockDTO = new FlagStockDTO();
                flagStockDTO.setImportStock(true);
                flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                lstStockTransDetailExpToBranch.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());
                doSaveStockTotal(stockTransExpToBranch, lstStockTransDetailExpToBranch, flagStockDTO, stockTransActionExpToBranch, conn);
                resultUpdate = 1L;
                //Update bang stockTransDetail cho 3 giao dich --> QUANTITY_RES, QUANTITY_REAL ???

            } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_FILE, importPartnerRequestDTO.getImpType())) {
                resultUpdate = importSerialByFile(stockTransActionExpToBranch, stockTransActionExpSim, importPartnerRequestDTO, conn);
            } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE, importPartnerRequestDTO.getImpType())) {
                resultUpdate = importSerialByRange(importPartnerRequestDTO, stockTransActionExpSim, stockTransActionExpToBranch, conn);
            }

            //7.Update trang thai STOCK_TRANS_DETAIL, STOCK_TRANS
            //update trang thai giao dich xuat doi tac cho chi nhanh ve bang 6, stock_trans_action_status = 3,6
            if (resultUpdate <= 0) {
                stockTransExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                updateStockTrans(stockTransExpToBranch, conn);
                stockTransActionExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                insertStockTransAction(stockTransExpToBranch, stockTransActionExpToBranch, conn);
                //update trang thai giao dich nhap hang DT -> VT ve bang 6, stock_trans_action_status = 6
                stockTransImpVT.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                updateStockTrans(stockTransImpVT, conn);
                stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
                //update trang thai giao dich xuat sim cho DT, stock_trans_action_status = 3,6
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                    stockTransExpSim.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                    updateStockTrans(stockTransExpSim, conn);
                    stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.CANCEL);
                    insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);
                }
                conn.commit();
            } else {
                //4.Insert bang PARTNER_CONTRACT
                int resultSavePartnerContract = doSavePartnerContract(importPartnerRequestDTO, conn, stockTransImpVT);
                if (resultSavePartnerContract <= 0) {
                    //Co loi xay ra khi insert bang PARTNER_CONTRACT
                    if (!DataUtil.isNullObject(conn)) {
                        conn.rollback();
                    }
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
                }
                stockTransExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                updateStockTrans(stockTransExpToBranch, conn);
                stockTransActionExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                insertStockTransAction(stockTransExpToBranch, stockTransActionExpToBranch, conn);
                stockTransActionExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                insertStockTransAction(stockTransExpToBranch, stockTransActionExpToBranch, conn);
                //update trang thai giao dich nhap hang DT -> VT ve bang 6, stock_trans_action_status = 6
                stockTransImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                updateStockTrans(stockTransImpVT, conn);
                stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
                //update trang thai giao dich xuat sim cho DT, stock_trans_action_status = 3,6
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                    stockTransExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                    updateStockTrans(stockTransExpSim, conn);
                    stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                    insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);
                    stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                    insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);
                }
                //Cap nhat trang thai yeu cau ve da nhap kho ( = 3)
                updateImportPartnerRequest(importPartnerRequestDTO, conn);
                //tang stock_num
                staffRepo.increaseStockNum(conn, importPartnerRequestDTO.getImportStaffId(), "STOCK_NUM_IMP");
                conn.commit();
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }

        return stockTransImpVT.getStockTransId();
    }


    public void doPrepareData(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        stockTransExpToBranch = new StockTransDTO();
        stockTransImpVT = new StockTransDTO();
        stockTransExpSim = new StockTransDTO();
        lstStockTransDetailExpToBranch = new ArrayList();
        lstStockTransDetailImpVT = new ArrayList();
        lstStockTransDetailExpSim = new ArrayList();
        sysdate = DbUtil.getSysDate(em);
        //Khoi tao doi tuong xuat kho cho chi nhanh
        stockTransExpToBranch.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
        stockTransExpToBranch.setFromOwnerId(Const.SHOP.SHOP_VTT_ID);
        stockTransExpToBranch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransExpToBranch.setToOwnerId(importPartnerRequestDTO.getToOwnerId());
        stockTransExpToBranch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransExpToBranch.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
        stockTransExpToBranch.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransExpToBranch.setCreateDatetime(sysdate);
        stockTransExpToBranch.setTotalAmount(0L);
        stockTransExpToBranch.setReasonId(Const.REASON_ID.XUAT_KHO_CAP_DUOI);
        stockTransExpToBranch.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.branch"));
        stockTransExpToBranch.setStaffId(importPartnerRequestDTO.getImportStaffId());
        stockTransExpToBranch.setUserCreate(importPartnerRequestDTO.getImportStaffCode());

        //setFromStockTransId

        //Neu la giao dich import KIT, thuc hien tao giao dich xuat SIM cho doi tac
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
            stockTransExpSim.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
            stockTransExpSim.setFromOwnerId(importPartnerRequestDTO.getToOwnerId());
            stockTransExpSim.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransExpSim.setToOwnerId(importPartnerRequestDTO.getPartnerId());
            stockTransExpSim.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransExpSim.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
            stockTransExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            stockTransExpSim.setCreateDatetime(sysdate);
            stockTransExpSim.setTotalAmount(0L);
            stockTransExpSim.setReasonId(Const.REASON_ID.EXP_SIM_IMP_FROM_PARTNER);
            stockTransExpSim.setNote(GetTextFromBundleHelper.getText("import.partner.exportNote"));
            stockTransExpSim.setStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransExpSim.setUserCreate(importPartnerRequestDTO.getImportStaffCode());

            StockTransDetailDTO stockTransDetailExpSim = new StockTransDetailDTO();
            stockTransDetailExpSim.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
            stockTransDetailExpSim.setProdOfferId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferSimId());
            stockTransDetailExpSim.setStateId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
            stockTransDetailExpSim.setQuantity(0L);
            stockTransDetailExpSim.setAmount(0L);
            stockTransDetailExpSim.setPrice(0L);
            stockTransDetailExpSim.setLstStockTransSerial(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO());
            lstStockTransDetailExpSim.add(stockTransDetailExpSim);
        }

        //Khoi tao doi tuong nhap hang tu doi tac
        stockTransImpVT.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
        stockTransImpVT.setFromOwnerId(importPartnerRequestDTO.getPartnerId());
        stockTransImpVT.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransImpVT.setToOwnerId(Const.SHOP.SHOP_VTT_ID);
        stockTransImpVT.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransImpVT.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
        stockTransImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransImpVT.setCreateDatetime(sysdate);
        stockTransImpVT.setTotalAmount(0L);
        stockTransImpVT.setReasonId(Const.REASON_ID.IMPORT_FROM_PARTNER);
        stockTransImpVT.setNote(GetTextFromBundleHelper.getText("mn.stock.partner.import.stock"));
        stockTransImpVT.setStaffId(importPartnerRequestDTO.getImportStaffId());
        //check erp=0(chua dong bo) neu dong bo erp, nguoc lai de = null(khong dong bo erp)
        stockTransImpVT.setCheckErp(importPartnerRequestDTO.isSyncERP() ? null : Const.STOCK_TRANS.IS_NOT_ERP);
        stockTransImpVT.setUserCreate(importPartnerRequestDTO.getImportStaffCode());
        stockTransImpVT.setActionCode(importPartnerRequestDTO.getActionCode());

        //Khoi tao stockTransDetail
        StockTransDetailDTO stockTransDetailExpToBranch = new StockTransDetailDTO();
        stockTransDetailExpToBranch.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
        stockTransDetailExpToBranch.setProdOfferId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
        stockTransDetailExpToBranch.setStateId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
        stockTransDetailExpToBranch.setQuantity(0L);
        stockTransDetailExpToBranch.setAmount(0L);
        stockTransDetailExpToBranch.setPrice(0L);
        stockTransDetailExpToBranch.setLstStockTransSerial(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO());
        lstStockTransDetailExpToBranch.add(stockTransDetailExpToBranch);
        //Nhap tu doi tac giong xuat chi nhanh
        StockTransDetailDTO stockTransDetailImpVT = DataUtil.cloneBean(stockTransDetailExpToBranch);
        stockTransDetailImpVT.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
        lstStockTransDetailImpVT.add(stockTransDetailImpVT);

        //Truyen gia tri profile vao list trong truong hop xuat theo file
        lstMapParam = new ArrayList();
        List<String> lstProfile = importPartnerRequestDTO.getListProfile();
        List<String> lstParam = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstParam();
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
    }

    public void doValidate(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        //Cac truong bat buoc nhap
        if (DataUtil.isNullObject(importPartnerRequestDTO) || DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListImportPartnerDetailDTOs())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getRequestCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.request.import.request.code.require.msg");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getPartnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.partner.msg.require");
        }
        if (DataUtil.isNullObject(importPartnerRequestDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.shop");
        }

        //Cac truong khong hop le
        //Lay id mat hang
        ImportPartnerDetailDTO importPartnerDetail = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0);
        if (DataUtil.isNullOrOneNavigate(importPartnerDetail.getProdOfferId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.update.file.isdn.product.not.contains");
        }
        ProductOfferingDTO productOffering = productOfferingService.findOne(importPartnerDetail.getProdOfferId());
        if (DataUtil.isNullObject(productOffering)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.update.file.isdn.product.not.contains");
        }
        tableName = IMServiceUtil.getTableNameByOfferType(productOffering.getProductOfferTypeId());
        //Neu mat hang la kit, phai chon loai sim
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) && DataUtil.isNullObject(importPartnerDetail.getProdOfferSimId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.sim.type.require");
        }
        //Neu mat hang la sim, phai nhap cac thong tin KIND, A3A8
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_NUMBER_NAME, tableName)) {
            if (DataUtil.isNullObject(importPartnerDetail.getA3a8())) {
                if (DataUtil.isNullObject(importPartnerDetail.getA3a8())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.required.a3a8");
                }
                if (DataUtil.isNullObject(importPartnerDetail.getKind())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.required.kind");
                }
            }
            //Don vi nhan phai la shop cua user dang nhap
            if (DataUtil.isNullObject(importPartnerRequestDTO.getImportStaffId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.stock.import.stock.valid");
            }
            StaffDTO staffDTO = staffService.findOne(importPartnerRequestDTO.getImportStaffId());
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(importPartnerRequestDTO.getToOwnerId(), staffDTO.getShopId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.stock.import.stock.valid");
            }

            //Validate voi truong hop nhap theo file thi file nhap vao phai co du lieu, profile khac null
        }
    }

    public Long importSerialByRange(ImportPartnerRequestDTO importPartnerRequestDTO, StockTransActionDTO stockTransActionExpSim,
                                    StockTransActionDTO stockTransActionExpToBranch, Connection conn) throws Exception {
        PreparedStatement insertStock = null;
        PreparedStatement insertStockTransSerial = null;
        try {
            List<StockTransSerialDTO> lstStockTransSerialDTO = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO();
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
                    fieldNameList.append(",A3A8, KIND");
                    fieldDataList.append(",?, ?");
                }
            }


            strInsert.append(fieldNameList);

            strInsert.append(")");
            strInsert.append(" VALUES (");
            strInsert.append(tableName + "_SEQ.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ?, ? ");
            strInsert.append(fieldDataList);
            strInsert.append(")");
            strInsert.append(" log errors reject limit unlimited ");

            insertStock = conn.prepareStatement(strInsert.toString());
            ProductOfferingDTO productOffering = productOfferingService.findOne(lstStockTransDetailExpToBranch.get(0).getProdOfferId());

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
                    insertStock.setLong(2, stockTransExpToBranch.getToOwnerId());
                    insertStock.setString(3, Const.STATUS_ACTIVE);
                    insertStock.setLong(4, lstStockTransDetailExpToBranch.get(0).getStateId());
                    insertStock.setLong(5, lstStockTransDetailExpToBranch.get(0).getProdOfferId());
                    insertStock.setDate(6, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                    insertStock.setString(7, stockTransImpVT.getUserCreate());
                    insertStock.setLong(8, productOffering.getTelecomServiceId());
                    insertStock.setString(9, formatSerial(currentSerial.toString(), isCard));
                    insertStock.setDate(10, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                            || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                        insertStock.setLong(11, stockTransImpVT.getStockTransId());
                        insertStock.setString(12, importPartnerRequestDTO.getContractCode());
                        insertStock.setString(13, importPartnerRequestDTO.getPoCode());
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(14, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getA3a8());
                            insertStock.setString(15, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getKind());
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
                            //Xuat chi nhanh
                            insertStockTransSerial.setLong(1, lstStockTransDetailExpToBranch.get(0).getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                            insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                            insertStockTransSerial.setLong(6, stockTransExpToBranch.getStockTransId());
                            insertStockTransSerial.setLong(7, lstStockTransDetailExpToBranch.get(0).getProdOfferId());
                            insertStockTransSerial.setLong(8, lstStockTransDetailExpToBranch.get(0).getStateId());
                            insertStockTransSerial.addBatch();
                            //Nhap hang VT
                            insertStockTransSerial.setLong(1, lstStockTransDetailImpVT.get(0).getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                            insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                            insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                            insertStockTransSerial.setLong(7, lstStockTransDetailImpVT.get(0).getProdOfferId());
                            insertStockTransSerial.setLong(8, lstStockTransDetailImpVT.get(0).getStateId());
                            insertStockTransSerial.addBatch();
                            //Xuat SIM neu la giao dich import KIT
                            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                                insertStockTransSerial.setLong(1, lstStockTransDetailExpSim.get(0).getStockTransDetailId());
                                insertStockTransSerial.setString(2, fromSerialInBatch);
                                insertStockTransSerial.setString(3, toSerialInBatch);
                                insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                                insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpSim.getCreateDatetime().getTime()));
                                insertStockTransSerial.setLong(6, stockTransExpSim.getStockTransId());
                                insertStockTransSerial.setLong(7, lstStockTransDetailExpSim.get(0).getProdOfferId());
                                insertStockTransSerial.setLong(8, lstStockTransDetailExpSim.get(0).getStateId());
                                insertStockTransSerial.addBatch();
                            }

                            //Neu la giao dich import KIT va co thong tin serial, isdn, thuc hien update bang stock_sim, stock_number
                            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                                //update trang thai SIM thanh da ban
                                int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, lstStockTransDetailExpSim, stockTransImpVT);

                                if (resultUpdateStockSim > 0) {
                                    //Tru StockTotal cho kho chi nhanh
                                    FlagStockDTO flagStockDTO = new FlagStockDTO();
                                    flagStockDTO.setExportStock(true);
                                    flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                    flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                    StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, lstStockTransDetailExpSim, flagStockDTO);
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

                            //Cong so luong vao kho chi nhanh
                            StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransExpToBranch, lstStockTransDetailExpToBranch, flagStockDTO);
                            StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpToBranch, stockTransActionExpToBranch);

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
            Long numberRemainRecord = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity() - numberBatch * Const.DEFAULT_BATCH_SIZE;
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
                    //Xuat chi nhanh
                    if (tmpSuccessRecordInBatch > 0) {
                        insertStockTransSerial.setLong(1, lstStockTransDetailExpToBranch.get(0).getStockTransDetailId());
                        insertStockTransSerial.setString(2, fromSerialInBatch);
                        insertStockTransSerial.setString(3, toSerialInBatch);
                        insertStockTransSerial.setLong(4, numberRemainRecord);
                        insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                        insertStockTransSerial.setLong(6, stockTransExpToBranch.getStockTransId());
                        insertStockTransSerial.setLong(7, lstStockTransDetailExpToBranch.get(0).getProdOfferId());
                        insertStockTransSerial.setLong(8, lstStockTransDetailExpToBranch.get(0).getStateId());
                        insertStockTransSerial.addBatch();
                        //Nhap hang VT
                        insertStockTransSerial.setLong(1, lstStockTransDetailImpVT.get(0).getStockTransDetailId());
                        insertStockTransSerial.setString(2, fromSerialInBatch);
                        insertStockTransSerial.setString(3, toSerialInBatch);
                        insertStockTransSerial.setLong(4, numberRemainRecord);
                        insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                        insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                        insertStockTransSerial.setLong(7, lstStockTransDetailImpVT.get(0).getProdOfferId());
                        insertStockTransSerial.setLong(8, lstStockTransDetailImpVT.get(0).getStateId());
                        insertStockTransSerial.addBatch();
                        //Xuat SIM neu la giao dich import KIT
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                            insertStockTransSerial.setLong(1, lstStockTransDetailExpSim.get(0).getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, numberRemainRecord);
                            insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpSim.getCreateDatetime().getTime()));
                            insertStockTransSerial.setLong(6, stockTransExpSim.getStockTransId());
                            insertStockTransSerial.setLong(7, lstStockTransDetailExpSim.get(0).getProdOfferId());
                            insertStockTransSerial.setLong(8, lstStockTransDetailExpSim.get(0).getStateId());
                            insertStockTransSerial.addBatch();
                        }

                        //Neu la giao dich import KIT va co thong tin serial, isdn, thuc hien update bang stock_sim, stock_number
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                            //update trang thai SIM thanh da ban
                            int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, lstStockTransDetailExpSim, stockTransImpVT);

                            if (resultUpdateStockSim > 0) {
                                //Tru StockTotal cho kho chi nhanh
                                FlagStockDTO flagStockDTO = new FlagStockDTO();
                                flagStockDTO.setExportStock(true);
                                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, lstStockTransDetailExpSim, flagStockDTO);
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

                        //Cong so luong vao kho chi nhanh
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransExpToBranch, lstStockTransDetailExpToBranch, flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpToBranch, stockTransActionExpToBranch);

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

    public Long importSerialByFile(StockTransActionDTO stockTransActionExpToBranch, StockTransActionDTO stockTransActionExpSim,
                                   ImportPartnerRequestDTO importPartnerRequestDTO, Connection conn) throws Exception {
        PreparedStatement insertStock = null;
        PreparedStatement insertStockTransSerial = null;
        Long numberSuccessRecord = 0L;
        Long numberErrorRecord = 0L;
        try {

            StringBuilder fieldNameList = new StringBuilder();
            StringBuilder fieldDataList = new StringBuilder();
//            List<String> lstProfile = importPartnerRequestDTO.getListProfile();
            List<String> lstProfile = new ArrayList();
            ProfileDTO profileDTO = productOfferingService.getProfileByProductId(lstStockTransDetailExpToBranch.get(0).getProdOfferId());
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
                    fieldNameList.append(",A3A8, KIND");
                    fieldDataList.append(",?, ?");
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
            strInsert.append(" log errors reject limit unlimited ");

            insertStock = conn.prepareStatement(strInsert.toString());
//        insertStock.clearBatch();

            ProductOfferingDTO productOffering = productOfferingService.findOne(lstStockTransDetailExpToBranch.get(0).getProdOfferId());

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
                insertStock.setLong(2, stockTransExpToBranch.getToOwnerId());
                insertStock.setLong(3, Long.valueOf(Const.STATUS_ACTIVE));
                insertStock.setLong(4, lstStockTransDetailExpToBranch.get(0).getStateId());
                insertStock.setLong(5, lstStockTransDetailExpToBranch.get(0).getProdOfferId());
                insertStock.setDate(6, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                insertStock.setString(7, stockTransImpVT.getUserCreate() == null ? "" : stockTransImpVT.getUserCreate());
                insertStock.setLong(8, productOffering.getTelecomServiceId());
                insertStock.setDate(9, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                        || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                    insertStock.setLong(10, stockTransImpVT.getStockTransId());
                    insertStock.setString(11, importPartnerRequestDTO.getContractCode());
                    insertStock.setString(12, importPartnerRequestDTO.getPoCode() == null ? "" : importPartnerRequestDTO.getPoCode());
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                        insertStock.setString(13, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getA3a8());
                        insertStock.setString(14, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getKind());
                    }
                }

                for (int j = 0; j < lstProfile.size(); j++) {
                    if (DataUtil.safeEqual(lstProfile.get(j), "SERIAL")) {
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)
                                || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                            insertStock.setString(j + 13, formatSerial(mapParam.get(lstProfile.get(j).trim()), isCard));
                        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(j + 15, formatSerial(mapParam.get(lstProfile.get(j).trim()), isCard));
                        } else {
                            insertStock.setString(j + 10, formatSerial(mapParam.get(lstProfile.get(j).trim()), isCard));
                        }
                    } else {
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName)
                                || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                            insertStock.setString(j + 13, mapParam.get(lstProfile.get(j).trim()));
                        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(j + 15, mapParam.get(lstProfile.get(j).trim()));
                        } else {
                            insertStock.setString(j + 10, mapParam.get(lstProfile.get(j).trim()));
                        }
                    }
                }


                insertStock.addBatch();

                //insert bang stock_trans_serial
                //Xuat chi nhanh
                insertStockTransSerial.setLong(1, lstStockTransDetailExpToBranch.get(0).getStockTransDetailId());
                insertStockTransSerial.setString(2, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setString(3, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setLong(4, 1L);
                insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpToBranch.getCreateDatetime().getTime()));
                insertStockTransSerial.setLong(6, stockTransExpToBranch.getStockTransId());
                insertStockTransSerial.setLong(7, lstStockTransDetailExpToBranch.get(0).getProdOfferId());
                insertStockTransSerial.setLong(8, lstStockTransDetailExpToBranch.get(0).getStateId());
                insertStockTransSerial.addBatch();
                //Nhap hang VT
                insertStockTransSerial.setLong(1, lstStockTransDetailImpVT.get(0).getStockTransDetailId());
                insertStockTransSerial.setString(2, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setString(3, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setLong(4, 1L);
                insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                insertStockTransSerial.setLong(7, lstStockTransDetailImpVT.get(0).getProdOfferId());
                insertStockTransSerial.setLong(8, lstStockTransDetailImpVT.get(0).getStateId());
                insertStockTransSerial.addBatch();
                //Xuat SIM neu la giao dich import KIT
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
                    insertStockTransSerial.setLong(1, lstStockTransDetailExpSim.get(0).getStockTransDetailId());
                    insertStockTransSerial.setString(2, mapParam.get("SERIAL"));
                    insertStockTransSerial.setString(3, mapParam.get("SERIAL"));
                    insertStockTransSerial.setLong(4, 1L);
                    insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpSim.getCreateDatetime().getTime()));
                    insertStockTransSerial.setLong(6, stockTransExpSim.getStockTransId());
                    insertStockTransSerial.setLong(7, lstStockTransDetailExpSim.get(0).getProdOfferId());
                    insertStockTransSerial.setLong(8, lstStockTransDetailExpSim.get(0).getStateId());
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
                            int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, lstStockTransDetailExpSim, stockTransImpVT);
                            if (resultUpdateStockSim > 0) {
                                //Tru StockTotal cho kho chi nhanh
                                FlagStockDTO flagStockDTO = new FlagStockDTO();
                                flagStockDTO.setExportStock(true);
                                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, lstStockTransDetailExpSim, flagStockDTO);
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

                        //Cong so luong vao kho chi nhanh
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransExpToBranch, lstStockTransDetailExpToBranch, flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpToBranch, stockTransActionExpToBranch);
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
                            int resultUpdateStockSim = updateStockSim(startStockKitId, conn, stockTransExpSim, lstStockTransDetailExpSim, stockTransImpVT);

                            if (resultUpdateStockSim > 0) {
                                //Tru StockTotal cho kho chi nhanh
                                FlagStockDTO flagStockDTO = new FlagStockDTO();
                                flagStockDTO.setExportStock(true);
                                flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
                                flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

                                StockTotalDTO initStockTotalDTO = initStockTotal(Long.valueOf(resultUpdateStockSim), stockTransExpSim, lstStockTransDetailExpSim, flagStockDTO);
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

                        //Cong so luong vao kho chi nhanh
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransExpToBranch, lstStockTransDetailExpToBranch, flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransExpToBranch, stockTransActionExpToBranch);
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

    private int updateStockTrans(StockTransDTO stockTransDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement updateStock = null;
        try {
            StringBuilder updateStockTrans = new StringBuilder();
            updateStockTrans.append(" update STOCK_TRANS ");
            updateStockTrans.append(" set status = ? ");
            updateStockTrans.append(" where stock_trans_id = ? ");
            updateStock = conn.prepareCall(updateStockTrans.toString());
            updateStock.setString(1, stockTransDTO.getStatus());
            updateStock.setLong(2, stockTransDTO.getStockTransId());
            return updateStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.update.stock.trans.err");
        } finally {
            if (updateStock != null) {
                updateStock.close();
            }
        }
    }

    private int updateStockTransDetail(StockTransDetailDTO stockTransDetailDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement updateStock = null;
        try {
            StringBuilder updateStockTrans = new StringBuilder();
            updateStockTrans.append(" update STOCK_TRANS_DETAIL ");
            updateStockTrans.append(" set quantity = ? ");
            updateStockTrans.append(" where stock_trans_id = ? ");
            updateStock = conn.prepareCall(updateStockTrans.toString());
            updateStock.setLong(1, stockTransDetailDTO.getQuantity());
            updateStock.setLong(2, stockTransDetailDTO.getStockTransDetailId());
            return updateStock.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.update.");
        } finally {
            if (updateStock != null) {
                updateStock.close();
            }

        }
    }


    private int updateImportPartnerRequest(ImportPartnerRequestDTO importPartnerRequestDTO, Connection conn) throws LogicException, Exception {
        PreparedStatement ps = null;
        try {
            StringBuilder updateImportPartnerRequest = new StringBuilder();
            updateImportPartnerRequest.append(" update IMPORT_PARTNER_REQUEST ");
            updateImportPartnerRequest.append(" set status = ?, import_staff_id = ?, import_date = sysdate ");
            updateImportPartnerRequest.append(" where import_partner_request_id = ? ");
            ps = conn.prepareStatement(updateImportPartnerRequest.toString());
            ps.setLong(1, Const.LongSimpleItem.importPartnerRequestStatusImported.getValue());
            ps.setLong(2, importPartnerRequestDTO.getImportStaffId());
            ps.setLong(3, importPartnerRequestDTO.getImportPartnerRequestId());
            return ps.executeUpdate();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.update.stock.number.err");
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<String> getErrorDetails(Long transID) throws LogicException, Exception {
        List<String> listError = Lists.newArrayList();
        try {
            StringBuilder builder = new StringBuilder("select product_offer_type_id, prod_offer_id ")
                    .append(" from product_offering")
                    .append(" where prod_offer_id in(select prod_offer_id  ")
                    .append(" from stock_trans_detail where stock_trans_id= #transID)");
            Query qProdType = em.createNativeQuery(builder.toString());
            qProdType.setParameter("transID", transID);
            List rs = qProdType.getResultList();
            if (rs == null || rs.isEmpty()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.prodType.valid");
            }
            //
            Object[] objects = (Object[]) rs.get(0);
            Long productOfferId = DataUtil.safeToLong(objects[1]);
            Long productOfferTypeId = DataUtil.safeToLong(objects[0]);
            //
            ProfileDTO profileDTO = productWs.getProfileByProductOfferId(productOfferId);
            //
            builder = new StringBuilder("select ");
            if (!DataUtil.isNullObject(profileDTO)) {
                for (String build : profileDTO.getLstColumnName()) {
                    builder.append(build);
                    builder.append(", ");
                }
            }
            builder.append("ora_err_mesg$");
            builder.append(" from ")
                    .append(Const.AppProperties4Sms.prefixTable.stringValue())
                    .append(Const.MapTableProdType.mapper.getTableName(productOfferTypeId));
            builder.append(" where stock_trans_id= #transID");

            Query dQuery = em.createNativeQuery(builder.toString());
            dQuery.setParameter("transID", transID);
            List lstObject = dQuery.getResultList();
            if (lstObject != null && !lstObject.isEmpty()) {
                for (Object object : lstObject) {
                    Object[] xyz = (Object[]) object;
                    StringBuilder sub = new StringBuilder("");
                    for (Object obj : xyz) {
                        if (!DataUtil.isNullOrEmpty(sub.toString())) {
                            sub.append(", ");
                        }
                        sub.append(DataUtil.safeToString(obj)).append("");
                    }
                    listError.add(sub.toString());
                }
            }
            return listError;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}

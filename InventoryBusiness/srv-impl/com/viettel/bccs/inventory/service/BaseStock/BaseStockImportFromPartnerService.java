package com.viettel.bccs.inventory.service.BaseStock;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StaffRepo;
import com.viettel.bccs.inventory.service.PartnerService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hoangnt14 on 5/10/2016.
 */
@Service
public class BaseStockImportFromPartnerService extends BaseStockPartnerService {
    public static final Logger logger = Logger.getLogger(BaseStockImportFromPartnerService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    StaffRepo staffRepo;

    @Autowired
    StaffService staffService;
    @Autowired
    ShopService shopService;
    @Autowired
    PartnerService partnerService;

    private StockTransDTO stockTransRegion;
    private StockTransDTO stockTransImpVT;
    private StockTransDTO stockTransExpSim;
    private List<StockTransDetailDTO> lstStockTransDetailImpVT;
    private List<StockTransDetailDTO> lstStockTransDetailExpSim;
    private List<StockTransDetailDTO> lstStockTransDetailExpSimRegion;
    private List<StockTransDetailDTO> lstStockTransDetailPartnerRegion;
    private List<HashMap<String, String>> lstMapParam;

    private String tableName;
    private Long prodOfferTypeId;
    Date sysdate;

    @Transactional(rollbackFor = Exception.class)
    public Long executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        Connection conn = null;
        try {
            //validate
            doValidate(importPartnerRequestDTO);
            //prepare data
            doPrepareDataForPartner(importPartnerRequestDTO);

            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            //1.Neu giao dich ve kho 3 mien
            StockTransActionDTO stockTransActionRegion = new StockTransActionDTO();
            if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                //1.1. Insert bang stock_trans, status = 6 : Da nhap hang
                insertStockTrans(stockTransRegion, conn);
                //1.2. Insert stock_trans_action status 1->6
                stockTransActionRegion.setActionCode(Const.STOCK_TRANS.TRANS_CODE_LX + "_" + importPartnerRequestDTO.getActionCode());
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region.order"));
                stockTransActionRegion.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
                stockTransActionRegion.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
                stockTransActionRegion.setCreateDatetime(stockTransRegion.getCreateDatetime());
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(Const.STOCK_TRANS.TRANS_CODE_PX + "_" + importPartnerRequestDTO.getActionCode());
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region.note"));
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(null);
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(null);
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                stockTransRegion.setNote(null);
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(Const.STOCK_TRANS.TRANS_CODE_PN + "_" + importPartnerRequestDTO.getActionCode());
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region.import.note"));
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(null);
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransRegion.setNote(null);
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);
            }
            //2.Neu mat hang KIT, thuc hien giao dich xuat sim product_offer_type_id = 8 : STOCK_KIT
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
                stockTransActionExpSim.setRegionOwnerId(importPartnerRequestDTO.getRegionStockId());
                insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);

                stockTransActionExpSim.setActionCode(null);
                stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                stockTransExpSim.setNote(null);
                insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);

                stockTransActionExpSim.setActionCode(null);
                stockTransActionExpSim.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransExpSim.setNote(null);
                insertStockTransAction(stockTransExpSim, stockTransActionExpSim, conn);

                //StockTransDetail
                insertStockTransDetail(stockTransExpSim, lstStockTransDetailExpSim, conn);
                //2.1 Neu co giao dich nhap ve kho 3 mien
                if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                    insertStockTransDetail(stockTransRegion, lstStockTransDetailExpSimRegion, conn);
                }
            }
            //3.Luu thong tin giao dich nhap kho tu doi tac
            // 3.1 Stocktran
            insertStockTrans(stockTransImpVT, conn);
            //3.2 Insert bang stock_trans_action, status = 2
            StockTransActionDTO stockTransActionImpVT = new StockTransActionDTO();
            stockTransActionImpVT.setActionCode(importPartnerRequestDTO.getActionCode());
            stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionImpVT.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
            stockTransActionImpVT.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransActionImpVT.setCreateDatetime(stockTransImpVT.getCreateDatetime());
            stockTransActionImpVT.setRegionOwnerId(importPartnerRequestDTO.getRegionStockId());

            if (importPartnerRequestDTO.isSignVoffice()) {
                stockTransActionImpVT.setSignCaStatus(Const.SIGN_VOFFICE);
            }
            insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
            //status = 6
            stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionImpVT.setActionCode(null);
            stockTransImpVT.setNote(null);
            insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
            //3.2. insert vao bang stock trans voffice
            if (importPartnerRequestDTO.isSignVoffice()) {
                insertVoffice(conn, importPartnerRequestDTO, stockTransImpVT, null);
            }
            //3.3. Insert bang stock_trans_detail
            if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_QUANTITY, importPartnerRequestDTO.getImpType())) {
                lstStockTransDetailImpVT.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());
                if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                    lstStockTransDetailPartnerRegion.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());
                }
            } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE, importPartnerRequestDTO.getImpType())) {
                Long totalQuantity = 0L;
                List<StockTransSerialDTO> lstStockTransSerialDTO = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO();
                if (!DataUtil.isNullOrEmpty(lstStockTransSerialDTO)) {
                    for (StockTransSerialDTO stockTransSerialDTO : lstStockTransSerialDTO) {
                        totalQuantity += stockTransSerialDTO.getQuantity();
                    }
                }
                lstStockTransDetailImpVT.get(0).setQuantity(totalQuantity);
            }
            insertStockTransDetail(stockTransImpVT, lstStockTransDetailImpVT, conn);
            //3.4 Neu co giao dich ve kho 3 mien
            if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                insertStockTransDetail(stockTransRegion, lstStockTransDetailPartnerRegion, conn);
            }
            //4.Luu thong tin hop dong PARTNER_CONTRACT
            int resultSavePartnerContract = doSavePartnerContractAndDetail(importPartnerRequestDTO, conn, stockTransImpVT);
            if (resultSavePartnerContract <= 0) {
                //Co loi xay ra khi insert bang PARTNER_CONTRACT
                if (conn != null) {
                    conn.rollback();
                }
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.branch.insert.partner.contract");
            }
            //5.INSERT du lieu cac bang STOCK_X
            if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_QUANTITY, importPartnerRequestDTO.getImpType())) {
                //Truong hop nhap theo so luong : list<StockTransSerial> null
                //Cong so luong STOCK_TOTAL cho kho nhan
                FlagStockDTO flagStockDTO = new FlagStockDTO();
                flagStockDTO.setImportStock(true);
                flagStockDTO.setImpAvailableQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                flagStockDTO.setImpCurrentQuantity(Const.STOCK_TOTAL.PLUS_QUANTITY);
                lstStockTransDetailImpVT.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());
                doSaveStockTotal(stockTransImpVT, lstStockTransDetailImpVT, flagStockDTO, stockTransActionImpVT, conn);
                if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                    lstStockTransDetailPartnerRegion.get(0).setQuantity(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity());
                    doSaveStockTotal(stockTransRegion, lstStockTransDetailPartnerRegion, flagStockDTO, stockTransActionRegion, conn);
                }
            } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_FILE, importPartnerRequestDTO.getImpType())) {
                importSerialByFile(stockTransActionImpVT, stockTransActionExpSim, importPartnerRequestDTO, conn);
            } else if (DataUtil.safeEqual(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE, importPartnerRequestDTO.getImpType())) {
                importSerialByRange(importPartnerRequestDTO, stockTransActionExpSim, stockTransActionImpVT, conn);
            }
            //cap nhat stock_num_imp cua staff
            staffRepo.increaseStockNum(conn, importPartnerRequestDTO.getImportStaffId(), "STOCK_NUM_IMP");
            conn.commit();
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

    public void doPrepareDataForPartner(ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        sysdate = DbUtil.getSysDate(em);
        stockTransRegion = new StockTransDTO();
        stockTransExpSim = new StockTransDTO();
        lstStockTransDetailExpSim = new ArrayList();
        lstStockTransDetailExpSimRegion = new ArrayList();
        stockTransImpVT = new StockTransDTO();
        lstStockTransDetailImpVT = new ArrayList();
        lstStockTransDetailPartnerRegion = new ArrayList();
        //Khoi tao nhap kho 3 mien
        if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
            stockTransRegion.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
            stockTransRegion.setFromOwnerId(Const.SHOP.SHOP_VTT_ID);
            stockTransRegion.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransRegion.setToOwnerId(importPartnerRequestDTO.getRegionStockId());
            stockTransRegion.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransRegion.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
            stockTransRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region"));
            stockTransRegion.setCreateDatetime(sysdate);
            stockTransRegion.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION);
            stockTransRegion.setCheckErp(Const.STOCK_TRANS.IS_NOT_ERP);
            stockTransRegion.setStaffId(importPartnerRequestDTO.getImportStaffId());
            StockTransDetailDTO stockTransDetailPartner = new StockTransDetailDTO();
            stockTransDetailPartner.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
            stockTransDetailPartner.setProdOfferId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
            stockTransDetailPartner.setStateId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
            lstStockTransDetailPartnerRegion.add(stockTransDetailPartner);
        }
        //Neu la giao dich import KIT, thuc hien tao giao dich xuat SIM cho doi tac
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName)) {
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
            StockTransDetailDTO stockTransDetailExpSim = new StockTransDetailDTO();
            stockTransDetailExpSim.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
            stockTransDetailExpSim.setProdOfferId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferSimId());
            stockTransDetailExpSim.setStateId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
            stockTransDetailExpSim.setQuantity(0L);
            stockTransDetailExpSim.setAmount(0L);
            stockTransDetailExpSim.setPrice(0L);
            stockTransDetailExpSim.setLstStockTransSerial(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO());
            lstStockTransDetailExpSim.add(stockTransDetailExpSim);

            if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                stockTransDetailExpSim = new StockTransDetailDTO();
                stockTransDetailExpSim.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
                stockTransDetailExpSim.setProdOfferId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferSimId());
                stockTransDetailExpSim.setStateId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
                lstStockTransDetailExpSimRegion.add(stockTransDetailExpSim);
            }
        }
        //Khoi tao doi tuong nhap hang tu doi tac
        stockTransImpVT.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
        stockTransImpVT.setFromOwnerId(importPartnerRequestDTO.getPartnerId());
        stockTransImpVT.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        stockTransImpVT.setToOwnerId(importPartnerRequestDTO.getToOwnerId());
        stockTransImpVT.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockTransImpVT.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
        stockTransImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransImpVT.setCreateDatetime(sysdate);
        stockTransImpVT.setReasonId(Const.REASON_ID.IMPORT_PARTNER);
        stockTransImpVT.setNote(GetTextFromBundleHelper.getText("mn.stock.partner.import.stock"));
        stockTransImpVT.setStaffId(importPartnerRequestDTO.getImportStaffId());
        //check erp=0(chua dong bo) neu dong bo erp, nguoc lai de = null(khong dong bo erp)
        stockTransImpVT.setCheckErp(importPartnerRequestDTO.isSyncERP() ? null : Const.STOCK_TRANS.IS_NOT_ERP);
        stockTransImpVT.setUserCreate(importPartnerRequestDTO.getImportStaffCode());
        stockTransImpVT.setRegionStockTransId(stockTransRegion.getStockTransId());
        //Khoi tao stockTransDetail
        StockTransDetailDTO stockTransDetailImp = new StockTransDetailDTO();
        stockTransDetailImp.setStockTransDetailId(DbUtil.getSequence(em, "STOCK_TRANS_DETAIL_SEQ"));
        stockTransDetailImp.setProdOfferId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferId());
        stockTransDetailImp.setStateId(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getStateId());
        stockTransDetailImp.setQuantity(0L);
        stockTransDetailImp.setAmount(0L);
        stockTransDetailImp.setPrice(0L);
        stockTransDetailImp.setLstStockTransSerial(importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getLstStockTransSerialDTO());
        lstStockTransDetailImpVT.add(stockTransDetailImp);
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
        PartnerDTO partnerDTO = partnerService.findOne(importPartnerRequestDTO.getPartnerId());
        if (DataUtil.isNullObject(partnerDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.partner");
        }
        ShopDTO shopDTO = shopService.findOne(importPartnerRequestDTO.getToOwnerId());
        if (DataUtil.isNullObject(shopDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.toStock.notExists");
        }
        StaffDTO staffDTO = staffService.findOne(importPartnerRequestDTO.getImportStaffId());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
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
        prodOfferTypeId = productOffering.getProductOfferTypeId();
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
            //Validate voi truong hop nhap theo file thi file nhap vao phai co du lieu, profile khac null
        }
    }

    public Long importSerialByFile(StockTransActionDTO stockTransActionExp, StockTransActionDTO stockTransActionExpSim,
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
            ProfileDTO profileDTO = productOfferingService.getProfileByProductId(lstStockTransDetailImpVT.get(0).getProdOfferId());
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
            strInsert.append(" log errors reject limit unlimited ");

            insertStock = conn.prepareStatement(strInsert.toString());
//        insertStock.clearBatch();

            ProductOfferingDTO productOffering = productOfferingService.findOne(lstStockTransDetailImpVT.get(0).getProdOfferId());

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
                insertStock.setLong(4, lstStockTransDetailImpVT.get(0).getStateId());
                insertStock.setLong(5, lstStockTransDetailImpVT.get(0).getProdOfferId());
                insertStock.setDate(6, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                insertStock.setString(7, stockTransImpVT.getUserCreate() == null ? "" : stockTransImpVT.getUserCreate());
                insertStock.setLong(8, productOffering.getTelecomServiceId());
                insertStock.setDate(9, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                        || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                    insertStock.setLong(10, stockTransImpVT.getStockTransId());
                    insertStock.setString(11, importPartnerRequestDTO.getContractCode());
                    insertStock.setString(12, importPartnerRequestDTO.getPoCode() == null ? "" : importPartnerRequestDTO.getPoCode());
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                        insertStock.setString(13, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getA3a8());
                        insertStock.setString(14, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getKind());
                        insertStock.setString(15, Const.HLR_STATUS_DEFAULT);
                        insertStock.setString(16, Const.AUC_STATUS_DEFAULT);
                        insertStock.setLong(17, prodOfferTypeId);
                        insertStock.setLong(18, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferSimId());
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
                insertStockTransSerial.setLong(1, lstStockTransDetailImpVT.get(0).getStockTransDetailId());
                insertStockTransSerial.setString(2, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setString(3, formatSerial(mapParam.get("SERIAL"), isCard));
                insertStockTransSerial.setLong(4, 1L);
                insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                insertStockTransSerial.setLong(7, lstStockTransDetailImpVT.get(0).getProdOfferId());
                insertStockTransSerial.setLong(8, lstStockTransDetailImpVT.get(0).getStateId());
                insertStockTransSerial.addBatch();
                //Luu thong tin giao dich 3 mien
                if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                    insertStockTransSerial.setLong(1, lstStockTransDetailPartnerRegion.get(0).getStockTransDetailId());
                    insertStockTransSerial.setString(2, formatSerial(mapParam.get("SERIAL"), isCard));
                    insertStockTransSerial.setString(3, formatSerial(mapParam.get("SERIAL"), isCard));
                    insertStockTransSerial.setLong(4, 1L);
                    insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                    insertStockTransSerial.setLong(6, stockTransRegion.getStockTransId());
                    insertStockTransSerial.setLong(7, lstStockTransDetailPartnerRegion.get(0).getProdOfferId());
                    insertStockTransSerial.setLong(8, lstStockTransDetailPartnerRegion.get(0).getStateId());
                    insertStockTransSerial.addBatch();
                }
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
                if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) && !DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                    insertStockTransSerial.setLong(1, lstStockTransDetailExpSim.get(0).getStockTransDetailId());
                    insertStockTransSerial.setString(2, mapParam.get("SERIAL"));
                    insertStockTransSerial.setString(3, mapParam.get("SERIAL"));
                    insertStockTransSerial.setLong(4, 1L);
                    insertStockTransSerial.setDate(5, new java.sql.Date(stockTransExpSim.getCreateDatetime().getTime()));
                    insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
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
                                //Cap nhat tru kho stock_total
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

                        //Cong so luong vao kho nhan
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, lstStockTransDetailImpVT, flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionExp);
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
                                //Tru StockTotal cho kho nhan
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

                        //Cong so luong vao kho nhan
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, lstStockTransDetailImpVT, flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionExp);
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

    public Long importSerialByRange(ImportPartnerRequestDTO importPartnerRequestDTO, StockTransActionDTO stockTransActionExpSim,
                                    StockTransActionDTO stockTransActionExp, Connection conn) throws Exception {
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
            strInsert.append(" log errors reject limit unlimited ");

            insertStock = conn.prepareStatement(strInsert.toString());
            ProductOfferingDTO productOffering = productOfferingService.findOne(lstStockTransDetailImpVT.get(0).getProdOfferId());

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
                    insertStock.setLong(4, lstStockTransDetailImpVT.get(0).getStateId());
                    insertStock.setLong(5, lstStockTransDetailImpVT.get(0).getProdOfferId());
                    insertStock.setDate(6, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                    insertStock.setString(7, stockTransImpVT.getUserCreate());
                    insertStock.setLong(8, productOffering.getTelecomServiceId());
                    insertStock.setString(9, formatSerial(currentSerial.toString(), isCard));
                    insertStock.setDate(10, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                    if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)
                            || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT_NAME, tableName) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                        insertStock.setLong(11, stockTransImpVT.getStockTransId());
                        insertStock.setString(12, importPartnerRequestDTO.getContractCode());
                        insertStock.setString(13, importPartnerRequestDTO.getPoCode());
                        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_NAME, tableName)) {
                            insertStock.setString(14, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getA3a8());
                            insertStock.setString(15, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getKind());
                            insertStock.setString(16, Const.HLR_STATUS_DEFAULT);
                            insertStock.setString(17, Const.AUC_STATUS_DEFAULT);
                            insertStock.setLong(18, prodOfferTypeId);
                            insertStock.setLong(19, importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferSimId());
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
                            insertStockTransSerial.setLong(1, lstStockTransDetailImpVT.get(0).getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                            insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                            insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                            insertStockTransSerial.setLong(7, lstStockTransDetailImpVT.get(0).getProdOfferId());
                            insertStockTransSerial.setLong(8, lstStockTransDetailImpVT.get(0).getStateId());
                            insertStockTransSerial.addBatch();
                            //Luu thong tin giao dich 3 mien
                            if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                                insertStockTransSerial.setLong(1, lstStockTransDetailPartnerRegion.get(0).getStockTransDetailId());
                                insertStockTransSerial.setString(2, fromSerialInBatch);
                                insertStockTransSerial.setString(3, toSerialInBatch);
                                insertStockTransSerial.setLong(4, Const.DEFAULT_BATCH_SIZE);
                                insertStockTransSerial.setDate(5, new java.sql.Date(stockTransRegion.getCreateDatetime().getTime()));
                                insertStockTransSerial.setLong(6, stockTransRegion.getStockTransId());
                                insertStockTransSerial.setLong(7, lstStockTransDetailPartnerRegion.get(0).getProdOfferId());
                                insertStockTransSerial.setLong(8, lstStockTransDetailPartnerRegion.get(0).getStateId());
                                insertStockTransSerial.addBatch();
                            }
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
                                    //Tru StockTotal cho kho nhan
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

                            //Cong so luong vao kho nhan
                            StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, lstStockTransDetailImpVT, flagStockDTO);
                            StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionExp);
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
                    if (tmpSuccessRecordInBatch > 0) {
                        //Nhap hang kho nhan
                        insertStockTransSerial.setLong(1, lstStockTransDetailImpVT.get(0).getStockTransDetailId());
                        insertStockTransSerial.setString(2, fromSerialInBatch);
                        insertStockTransSerial.setString(3, toSerialInBatch);
                        insertStockTransSerial.setLong(4, numberRemainRecord);
                        insertStockTransSerial.setDate(5, new java.sql.Date(stockTransImpVT.getCreateDatetime().getTime()));
                        insertStockTransSerial.setLong(6, stockTransImpVT.getStockTransId());
                        insertStockTransSerial.setLong(7, lstStockTransDetailImpVT.get(0).getProdOfferId());
                        insertStockTransSerial.setLong(8, lstStockTransDetailImpVT.get(0).getStateId());
                        insertStockTransSerial.addBatch();
                        //Luu thong tin giao dich 3 mien
                        if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                            insertStockTransSerial.setLong(1, lstStockTransDetailPartnerRegion.get(0).getStockTransDetailId());
                            insertStockTransSerial.setString(2, fromSerialInBatch);
                            insertStockTransSerial.setString(3, toSerialInBatch);
                            insertStockTransSerial.setLong(4, numberRemainRecord);
                            insertStockTransSerial.setDate(5, new java.sql.Date(stockTransRegion.getCreateDatetime().getTime()));
                            insertStockTransSerial.setLong(6, stockTransRegion.getStockTransId());
                            insertStockTransSerial.setLong(7, lstStockTransDetailPartnerRegion.get(0).getProdOfferId());
                            insertStockTransSerial.setLong(8, lstStockTransDetailPartnerRegion.get(0).getStateId());
                            insertStockTransSerial.addBatch();
                        }
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
                                //Tru StockTotal cho kho nhan
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

                        //Cong so luong vao kho nhan
                        StockTotalDTO initStockTotalDTO = initStockTotal(tmpSuccessRecordInBatch, stockTransImpVT, lstStockTransDetailImpVT, flagStockDTO);
                        StockTotalAuditDTO stockTotalAuditDTO = initStockTotalAudit(stockTransImpVT, stockTransActionExp);
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
}

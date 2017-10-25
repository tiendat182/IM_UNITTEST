package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockTransIm1DTO;
import com.viettel.bccs.im1.service.BaseStockIm1Service;
import com.viettel.bccs.im1.service.StockTransIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.model.Deposit;
import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.partner.dto.AccountBalanceDTO;
import com.viettel.bccs.partner.dto.AccountBookBankplusDTO;
import com.viettel.bccs.partner.service.AccountBalanceService;
import com.viettel.bccs.partner.service.AccountBookBankplusService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;

/**
 * Created by thetm1 on 09/12/2015.
 */

@Service
public abstract class BaseStockService extends BaseServiceImpl {
    private enum UpdateSystem {BCCS1, BCCS2}

    public static final Logger logger = Logger.getLogger(BaseStockService.class);
    public static final String STOCK_TRANS_VOFFICE_SEQ = "STOCK_TRANS_VOFFICE_SEQ";
    public static final String TEN_ZEZO = "0000000000";
    protected String oldStockTransStatus;
    protected String vtUnit;
    protected String fromStock;
    protected String createUser;
    protected Date createDate;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager emIm1;

    private final BaseMapper<StockTotal, StockTotalDTO> totalMapper = new BaseMapper(StockTotal.class, StockTotalDTO.class);

    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransIm1Service stockTransServiceIm1;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransLogisticService stockTransLogisticService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    @Autowired
    private StockTransRepo stockTransRepo;

    @Autowired
    private StockTransOfflineService stockTransOfflineService;
    @Autowired
    private StockTransSerialOfflineService stockTransSerialOfflineService;

    @Autowired
    private StockTransDetailOfflineService stockTransDetailOfflineService;

    @Autowired
    private ReceiptExpenseService receiptExpenseService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private DepositDetailService depositDetailService;
    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private AccountBookBankplusService accountBookBankplusService;
    @Autowired
    private AccountBalanceService accountBalanceService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private BaseStockIm1Service baseStockIm1Service;


    public BasePartnerMessage createTransToPartner(StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO, String typeExport, String serialList, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return null;
    }

    @Transactional(rollbackFor = Exception.class, timeout = 3600)
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                 List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        List<StockTransDetailDTO> lstStockTransDetailDTO = Lists.newArrayList();
        if (lstStockTransDetail != null) {
            lstStockTransDetailDTO.addAll(lstStockTransDetail);
        }


//        Date sysdate = DbUtil.getSysDate(em);
//        stockTransDTO.setCreateDatetime(sysdate);
        stockTransDTO.setStockTransDate(stockTransDTO.getCreateDatetime());
        FlagStockDTO flagStockDTO = new FlagStockDTO();

        //0. chuan bi du lieu
        doPrepareData(stockTransDTO, stockTransActionDTO, flagStockDTO);

        //0.1. chuan bi list detail do ko truyen tu giao dien xuong
        if (DataUtil.safeEqual(Const.STOCK_TRANS_STATUS.IMPORTED, stockTransDTO.getStatus())
                && DataUtil.isNullOrEmpty(lstStockTransDetailDTO)) {
            lstStockTransDetailDTO = stockTransDetailService.findByFilter(Lists.newArrayList(
                    new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                            stockTransDTO.getStockTransId())));
        }


        //0.2. gioi han so luong 1 lan xuat
        Long quantity = 0L;
        Long maxQty = 300000L;
        if (!DataUtil.isNullOrEmpty(lstStockTransDetailDTO)) {
            for (StockTransDetailDTO dto : lstStockTransDetailDTO) {
                if (!DataUtil.safeEqual(dto.getProdOfferId(), 1076L)) {
                    quantity += DataUtil.safeToLong(dto.getQuantity(), 0L);
                }
            }

            List<OptionSetValueDTO> valueLst = optionSetValueService.getByOptionSetCode("MAX_QTY_TRANS");
            if (!DataUtil.isNullOrEmpty(valueLst)) {
                maxQty = DataUtil.safeToLong(valueLst.get(0).getValue(), maxQty);
            }

            if (quantity.compareTo(maxQty) > 0) {
                throw new LogicException("ESDSSTD0001", "Dang trong thoi gian KM de tranh cao tai, SL khong duoc vuot qua " + maxQty.toString());
            }
        }

        //1. Thuc hien validate
        doValidate(stockTransDTO, stockTransActionDTO, lstStockTransDetailDTO);

        //2.1 Giao dich xuat kho 3 mien
        doSaveRegionStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetailDTO);

        //2.2 Giao dich dieu chuyen
        doSaveExchangeStockTrans(stockTransDTO, stockTransActionDTO, flagStockDTO, lstStockTransDetailDTO);

        //2.3 Thuc hien luu thong tin giao dich stock_trans
        StockTransDTO stockTransNew = doSaveStockTrans(stockTransDTO);

        //2.3. Neu xu ly offline thuc hien insert bang stockTransOffline
        doSaveStockOffline(stockTransDTO, lstStockTransDetailDTO);

        //3. Thuc hien luu thong tin log giao dich, save
        stockTransActionDTO = doSaveStockTransAction(stockTransNew, stockTransActionDTO);

        //5. Thuc hien luu chi tiet mat hang trong giao dich
        doSaveStockTransDetail(stockTransNew, lstStockTransDetailDTO);

        //6. Thuc hien luu chi tiet serial tung mat hang trong giao dich
        doSaveStockTransSerial(stockTransNew, lstStockTransDetailDTO);

        // 6.0 Luu tai khoan bankplus
        doUpdateBankplusBCCS(flagStockDTO, stockTransNew);

        // 6.1 Luu giao dich xuat kho truong hop dat coc
        doSaveReceiptExpense(flagStockDTO, stockTransNew, stockTransActionDTO, lstStockTransDetailDTO);

        // 7.0 Lay thong tin kho cua user dang nhap
        StaffDTO staff = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (DataUtil.isNullObject(staff)
                || DataUtil.isNullObject(staff.getStaffId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("crt.err.staff.not.found"));
        }
        flagStockDTO.setShopId(staff.getShopId());
        //7.1 Thuc hien cap nhat chi tiet serial trong kho
        doSaveStockGoods(stockTransNew, lstStockTransDetailDTO, flagStockDTO);

        //8. Thuc hien cap nhat so luong kho xuat va kho nhan
        doSaveStockTotal(stockTransNew, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);

        //9. Thuc hien ky Voffice
        doSignVoffice(stockTransNew, stockTransActionDTO, requiredRoleMap, flagStockDTO);

        //10. Thuc hien luu du lieu dong bo logistic
        doSyncLogistic(stockTransNew, lstStockTransDetailDTO, requiredRoleMap);

        //11. Thuc hien thay doi so phieu
        doIncreaseStockNum(stockTransActionDTO, flagStockDTO.getPrefixActionCode(), requiredRoleMap);

        //12. Thuc hien xoa bang lockUserInfo
        doUnlockUser(stockTransDTO);
        stockTransDTO.setStockTransDate(stockTransNew.getStockTransDate());
        return stockTransActionDTO;
    }

    public abstract void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception;

    public abstract void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception;

    public void doSaveRegionStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {

        if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) {
            return;
        }

        //Chi ap dung muc VT, neu khong phai VT, khong luu Kho 3 mien
        StaffDTO staff = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (!Const.L_VT_SHOP_ID.equals(staff.getShopId())) {
            return;
        }


        //Tao giao dich xuat kho 3 mien
        StockTransDTO regionTransDTO = new StockTransDTO();
        if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockTransId())) {
            regionTransDTO = DataUtil.cloneBean(stockTransDTO);
            regionTransDTO.setStockTransId(null);
            regionTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            regionTransDTO.setImportReasonId(null);
            regionTransDTO.setIsAutoGen(1L);
            regionTransDTO.setNote(flagStockDTO.getNote());
            regionTransDTO.setCheckErp("0");
            regionTransDTO.setTransport(null);

            //Neu la lap lenh nhap: chuyen hang VT --> kho 3 mien
            if (Const.STOCK_TRANS_STATUS.IMPORT_ORDER.equals(stockTransDTO.getStatus())) {
                regionTransDTO.setFromOwnerId(stockTransDTO.getToOwnerId());
                regionTransDTO.setToOwnerId(stockTransDTO.getRegionStockId());
            } else {
                //Neu la lap lenh xuat: chuyen hang kho 3 mien --> VT
                regionTransDTO.setFromOwnerId(stockTransDTO.getRegionStockId());
                regionTransDTO.setToOwnerId(stockTransDTO.getFromOwnerId());
            }

        } else {
            regionTransDTO.setStockTransId(stockTransDTO.getRegionStockTransId());
        }
        regionTransDTO.setStatus(flagStockDTO.getRegionImportStatus());
        regionTransDTO = doSaveStockTrans(regionTransDTO);
        regionTransDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());//Set lai cho truong hop update
        regionTransDTO.setRegionStockId(stockTransDTO.getRegionStockId());//kho 3 mien

        //Luu chi tiet mat hang
        List<StockTransDetailDTO> stockTransDetailRegion = DataUtil.cloneBean(lstStockTransDetail);
        doSaveStockTransDetail(regionTransDTO, stockTransDetailRegion);

        //Ghi log giao dich xuat
        StockTransActionDTO exportActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        exportActionDTO.setStatus(flagStockDTO.getRegionExportStatus());
        exportActionDTO.setActionCode(DataUtil.safeToString(flagStockDTO.getPrefixExportActionCode()) + DataUtil.safeToString(exportActionDTO.getActionCode()));
//        exportActionDTO.setRegionOwnerId(stockTransDTO.getRegionStockId());
        exportActionDTO.setNote(flagStockDTO.getNote());
        exportActionDTO.setIsRegionExchange(true);
        doSaveStockTransAction(regionTransDTO, exportActionDTO);

        //Ghi log giao dich nhap
        StockTransActionDTO importActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        importActionDTO.setStatus(flagStockDTO.getRegionImportStatus());
        importActionDTO.setActionCode(DataUtil.safeToString(flagStockDTO.getPrefixImportActionCode()) + DataUtil.safeToString(importActionDTO.getActionCode()));
//        importActionDTO.setRegionOwnerId(stockTransDTO.getRegionStockId());
        importActionDTO.setNote(flagStockDTO.getNote());
        importActionDTO.setIsRegionExchange(true);
        doSaveStockTransAction(regionTransDTO, importActionDTO);

        stockTransDTO.setRegionStockTransId(regionTransDTO.getStockTransId());

        return;
    }

    public StockTransDTO doSaveStockTrans(StockTransDTO stockTransDTO) throws Exception {
        StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
        //Truong hop update
        if (transDTOImport.getStockTransId() != null) {
            StockTransDTO stockTransToUpdate = stockTransService.findOneLock(stockTransDTO.getStockTransId());

            if (DataUtil.isNullObject(stockTransToUpdate)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.notExists");
            }

            //Neu trang thai stockTrans da duoc cap nhat thi thong bao loi
            if (DataUtil.safeEqual(stockTransDTO.getStatus(), stockTransToUpdate.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.updated");
            }
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORTED) ||
                            DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                        StockTransIm1DTO stockTransToUpdateIM1;
                        if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORTED)) {
                            //Lay thong tin IM1
                            stockTransToUpdateIM1 = stockTransServiceIm1.findOneLock(stockTransDTO.getStockTransId());
                            if (!DataUtil.isNullObject(stockTransToUpdateIM1)) {
                                //check da xuat kho tren im1 chua
                                if (!DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))
                                        && !DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_NOTE))) {
                                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.duplicate.export.im1");
                                }
                                stockTransToUpdateIM1.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORTED));
                                stockTransServiceIm1.updateStatusStockTrans(stockTransToUpdateIM1);
                            }
                        } else {
                            stockTransToUpdateIM1 = stockTransServiceIm1.findOne(stockTransDTO.getStockTransId());
                            if (!DataUtil.isNullObject(stockTransToUpdateIM1)) {
                                //Da huy hoac tu choi tren IM1
                                if (DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.DESTROY_IM1))
                                        || DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.CANCEL_IM1))) {
                                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.destroy.import.im1");
                                }
                                if (DataUtil.safeEqual(stockTransToUpdateIM1.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.PROCESSING))) {
                                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.offline.import.im1");
                                }
                                StockTransIm1DTO stockTransImport = stockTransServiceIm1.findFromStockTransIdLock(stockTransToUpdateIM1.getStockTransId());
                                if (!DataUtil.isNullObject(stockTransImport)) {
                                    //check da xuat kho tren im1 chua
                                    if (!DataUtil.safeEqual(stockTransImport.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_ORDER))
                                            && !DataUtil.safeEqual(stockTransImport.getStockTransStatus(), DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORT_NOTE))) {
                                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.stockTrans.duplicate.import.im1");
                                    }
                                    stockTransImport.setStockTransStatus(DataUtil.safeToLong(Const.STOCK_TRANS_STATUS.EXPORTED));
                                    stockTransServiceIm1.updateStatusStockTrans(stockTransImport);
                                }
                            }
                        }
                    }
                }
            }
            stockTransToUpdate.setStatus(stockTransDTO.getStatus());
            //Neu process offline: status = 0
            if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
                stockTransToUpdate.setStatus(Const.STOCK_TRANS_STATUS.PROCESSING);
            }
            if (!DataUtil.isNullObject(stockTransDTO.getDepositStatus())) {
                stockTransToUpdate.setDepositStatus(stockTransDTO.getDepositStatus());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getPayStatus())) {
                stockTransToUpdate.setPayStatus(stockTransDTO.getPayStatus());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
                stockTransToUpdate.setBankplusStatus(stockTransDTO.getBankplusStatus());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getCreateUserIpAdress())) {
                stockTransToUpdate.setCreateUserIpAdress(stockTransDTO.getCreateUserIpAdress());
            }
            if (!DataUtil.isNullObject(stockTransDTO.getDepositPrice())) {
                stockTransToUpdate.setDepositPrice(stockTransDTO.getDepositPrice());
            }
            if (!DataUtil.isNullOrZero(stockTransDTO.getImportReasonId())) {
                stockTransToUpdate.setImportReasonId(stockTransDTO.getImportReasonId());
            }
            stockTransToUpdate.setImportNote(stockTransDTO.getImportNote());
            stockTransToUpdate.setRejectReasonId(stockTransDTO.getRejectReasonId());
            stockTransToUpdate.setRejectNote(stockTransDTO.getRejectNote());
            stockTransService.save(stockTransToUpdate);
            transDTOImport.setCreateDatetime(stockTransDTO.getCreateDatetime());//Lay lai de ghi log stock_trans_action
            transDTOImport.setStockTransDate(stockTransToUpdate.getCreateDatetime());
            return transDTOImport;
        }
        //Truong hop insert
        if (DataUtil.isNullObject(stockTransDTO.getStockTransType())) {
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
        }
        StockTransDTO stockTrans = stockTransService.save(stockTransDTO);
        transDTOImport.setStockTransId(stockTrans.getStockTransId());
        return transDTOImport;

    }

    public StockTransActionDTO doSaveStockTransAction(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO) throws Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return stockTransActionDTO;
        }
        StockTransActionDTO transActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        transActionDTO.setStockTransActionId(null);//luon insert
        transActionDTO.setStockTransId(stockTransDTO.getStockTransId());
        transActionDTO.setRegionOwnerId(stockTransDTO.getRegionStockId());
        transActionDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
        transActionDTO.setNote(stockTransDTO.getNote());
        if (stockTransDTO.isSignVoffice() && !stockTransActionDTO.getIsRegionExchange()) {//log giao dich dieu chuyen, 3 mien thi ko ky voffice
            transActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
        }
        //giao dich xuat hoac nhap khong set note va action_code
        if (Const.ConfigIDCheck.danh_sach_action_khong_can_actioncode.sValidate(transActionDTO.getStatus())) {
            transActionDTO.setNote(null);
            transActionDTO.setActionCode(null);
        }
        createUser = transActionDTO.getCreateUser();
        createDate = transActionDTO.getCreateDatetime();
        return stockTransActionService.save(transActionDTO);

    }

    public void doSaveStockTransDetail(StockTransDTO
                                               stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {


        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            stockTransDetail.setStockTransId(stockTransDTO.getStockTransId());
            stockTransDetail.setCreateDatetime(stockTransDTO.getCreateDatetime());
            StockTransDetailDTO stockTransDetailDTO = stockTransDetailService.save(stockTransDetail);
            stockTransDetail.setStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
        }


    }

    public void doSaveStockTransSerial(StockTransDTO
                                               stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {

            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                    stockTransSerial.setStockTransSerialId(null); // luon insert
                    stockTransSerial.setStockTransDetailId(stockTransDetail.getStockTransDetailId());
                    stockTransSerial.setCreateDatetime(stockTransDTO.getCreateDatetime());
                    stockTransSerial.setStockTransId(stockTransDTO.getStockTransId());
                    stockTransSerial.setProdOfferId(stockTransDetail.getProdOfferId());
                    stockTransSerial.setStateId(stockTransDetail.getStateId());
                    stockTransSerialService.save(stockTransSerial);
                }
            }
        }
    }

    public void doSaveStockTotal(StockTransDTO
                                         stockTransDTO, List<StockTransDetailDTO> lstStockTransDetailDTO, FlagStockDTO
                                         flagStockDTO, StockTransActionDTO stockTransActionDTO) throws LogicException, Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        if (!flagStockDTO.isExportStock() && !flagStockDTO.isImportStock()) {
            return;
        }


        //Neu la giao dich xuat dieu chuyen --> chi cong so luong thuc te, va cap nhap serial ve =3
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            flagStockDTO.setImpAvailableQuantity(0L);
        }

        List<StockTotalDTO> lstExpStockTotalDTO = new ArrayList<>();
        List<StockTotalDTO> lstImpStockTotalDTO = new ArrayList<>();

        if (DataUtil.isNullOrEmpty(lstStockTransDetailDTO)) {

            lstStockTransDetailDTO = stockTransDetailService.findByFilter(Lists.newArrayList(
                    new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                            stockTransDTO.getStockTransId())));
        }

        //Cap nhat kho xuat
        Long fromOwnerId = stockTransDTO.getFromOwnerId();
        Long fromOwnerType = stockTransDTO.getFromOwnerType();
        if ((!DataUtil.isNullOrZero(stockTransDTO.getRegionStockId()))
                && (DataUtil.isNullOrEmpty(flagStockDTO.getStatusForAgent()) || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED))
                && (Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId()))) {
            //Neu o cap VT thi thao tac voi kho 3 mien: Xuat hang tu kho 3 mien
            fromOwnerId = stockTransDTO.getRegionStockId();
        }

        //Cap nhat kho nhan
        Long toOwnerId = stockTransDTO.getToOwnerId();
        Long toOwnerType = stockTransDTO.getToOwnerType();
        if ((!DataUtil.isNullOrZero(stockTransDTO.getRegionStockId()))
                && (DataUtil.isNullOrEmpty(flagStockDTO.getStatusForAgent()) || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED))
                && (Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId()))) {
            //Neu o cap VT thi thao tac voi kho 3 mien: Nhap hang ve kho 3 mien
            toOwnerId = stockTransDTO.getRegionStockId();
        }

        for (StockTransDetailDTO stockTransDetail : lstStockTransDetailDTO) {

            StockTotalDTO stockTotal;
            if (flagStockDTO.isExportStock()) {
                stockTotal = new StockTotalDTO();
                stockTotal.setOwnerId(fromOwnerId);
                stockTotal.setOwnerType(fromOwnerType);
                stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
                stockTotal.setStateId(stockTransDetail.getStateId());
                stockTotal.setAvailableQuantity(flagStockDTO.getExpAvailableQuantity() * stockTransDetail.getQuantity());
                stockTotal.setCurrentQuantity(flagStockDTO.getExpCurrentQuantity() * stockTransDetail.getQuantity());
                stockTotal.setHangQuantity(flagStockDTO.getExpHangQuantity() * stockTransDetail.getQuantity());
                stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());

                lstExpStockTotalDTO.add(stockTotal);
            }

            if (flagStockDTO.isImportStock()) {
                // Neu xuat kho ban hang dai ly thi khong cong vao stock_total dai ly.
                if (!DataUtil.isNullObject(flagStockDTO.getObjectType()) && DataUtil.safeEqual(flagStockDTO.getObjectType(), Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT)
                        && !DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransDTO.getPayStatus())) {
                    continue;
                }
                stockTotal = new StockTotalDTO();
                stockTotal.setOwnerId(toOwnerId);
                stockTotal.setOwnerType(toOwnerType);
                stockTotal.setProdOfferId(stockTransDetail.getProdOfferId());
                if (!DataUtil.isNullOrZero(flagStockDTO.getStateIdForReasonId())) {
                    stockTotal.setStateId(flagStockDTO.getStateIdForReasonId());
                } else {
                    stockTotal.setStateId(stockTransDetail.getStateId());
                }
                stockTotal.setAvailableQuantity(flagStockDTO.getImpAvailableQuantity() * stockTransDetail.getQuantity());
                stockTotal.setCurrentQuantity(flagStockDTO.getImpCurrentQuantity() * stockTransDetail.getQuantity());
                stockTotal.setHangQuantity(flagStockDTO.getImpHangQuantity() * stockTransDetail.getQuantity());
                stockTotal.setModifiedDate(stockTransDTO.getCreateDatetime());
                stockTotal.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                lstImpStockTotalDTO.add(stockTotal);
            }
        }
        // Luu stock_total_audit
        StockTotalAuditDTO stockTotalAuditDTO = null;
        if (flagStockDTO.isInsertStockTotalAudit()) {
            stockTotalAuditDTO = new StockTotalAuditDTO();
            stockTotalAuditDTO.setUserId(stockTransActionDTO.getActionStaffId());
            stockTotalAuditDTO.setCreateDate(stockTransDTO.getCreateDatetime());
            if (!DataUtil.isNullObject(stockTransDTO.getReasonId())) {
                stockTotalAuditDTO.setReasonId(stockTransDTO.getReasonId());
            } else {
                stockTotalAuditDTO.setReasonId(stockTransDTO.getImportReasonId());
            }
            stockTotalAuditDTO.setTransId(stockTransDTO.getStockTransId());
            stockTotalAuditDTO.setTransCode(stockTransActionDTO.getActionCode());
            stockTotalAuditDTO.setTransType(DataUtil.safeToLong(stockTransDTO.getStockTransType()));
            stockTotalAuditDTO.setUserCode(stockTransActionDTO.getCreateUser());
            stockTotalAuditDTO.setStatus(Long.valueOf(Const.STATUS_ACTIVE));
//            stockTotalAuditDTO.setUserName(stockTransActionDTO.getCreateUser());
//            if (flagStockDTO.isExportStock()) {
//                stockTotalAuditDTO.setOwnerCode(stockTransDTO.getFromOwnerCode());
//                stockTotalAuditDTO.setOwnerName(stockTransDTO.getFromOwnerName());
//            } else {
//                stockTotalAuditDTO.setOwnerCode(stockTransDTO.getToOwnerCode());
//                stockTotalAuditDTO.setOwnerName(stockTransDTO.getToOwnerName());
//            }
        }

        //Cap nhat so luong kho xuat
        doSaveStockTotal(stockTotalAuditDTO, lstExpStockTotalDTO);

        //Cap nhat so luong kho nhap
        doSaveStockTotal(stockTotalAuditDTO, lstImpStockTotalDTO);

        //thaont19: luu thong tin IM1
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                baseStockIm1Service.doSaveStockTotal(stockTransDTO, lstStockTransDetailDTO, flagStockDTO, stockTransActionDTO);
            }
        }
    }

    protected void doSaveStockTotal(StockTotalAuditDTO
                                            stockTotalAuditDTO, List<StockTotalDTO> lstStockTotalDTO) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(lstStockTotalDTO)) {
            return;
        }

        for (StockTotalDTO stockTotalDTO : lstStockTotalDTO) {

            List<StockTotalDTO> lstStockTotal = stockTotalService.searchStockTotal(stockTotalDTO);
            if (lstStockTotal != null && lstStockTotal.size() > 1) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(lstStockTotal.get(0).getProdOfferId());
                if (offeringDTO != null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.limit.product.exist", offeringDTO.getName());
                }
            }

            //Truong hop update
            if (!DataUtil.isNullOrEmpty(lstStockTotal)) {
                StockTotalDTO stockTotal = lstStockTotal.get(0);
                //StockTotalDTO stockTotalToUpdate = stockTotalService.findOne(stockTotal.getStockTotalId());
                StockTotalDTO stockTotalToUpdate;
                try {
                    StockTotal stockTotalUpdate = em.find(StockTotal.class, stockTotal.getStockTotalId(), LockModeType.PESSIMISTIC_READ);
                    stockTotalToUpdate = totalMapper.toDtoBean(stockTotalUpdate);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    List<VShopStaffDTO> exportShopList = shopService.getListShopStaff(stockTotal.getOwnerId(), DataUtil.safeToString(stockTotal.getOwnerType()), null);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "basestock.validate.invalidate.stocktotal.updating", exportShopList.get(0).getOwnerCode() + "-" + exportShopList.get(0).getOwnerName());
                }
                stockTotalToUpdate.setAvailableQuantity(DataUtil.safeToLong(stockTotal.getAvailableQuantity()) + stockTotalDTO.getAvailableQuantity());
                stockTotalToUpdate.setCurrentQuantity(DataUtil.safeToLong(stockTotal.getCurrentQuantity()) + stockTotalDTO.getCurrentQuantity());
                stockTotalToUpdate.setHangQuantity(DataUtil.safeToLong(stockTotal.getHangQuantity()) + stockTotalDTO.getHangQuantity());
                stockTotalToUpdate.setModifiedDate(stockTotalDTO.getModifiedDate());
                stockTotalService.save(stockTotalToUpdate);
                // Luu stock_total_audit
                if (!DataUtil.isNullObject(stockTotalAuditDTO)) {
                    StockTotalAuditDTO stockTotalAuditInsert = DataUtil.cloneBean(stockTotalAuditDTO);
                    stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
                    stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
                    stockTotalAuditInsert.setProdOfferId(stockTotalToUpdate.getProdOfferId());
                    stockTotalAuditInsert.setStateId(stockTotalToUpdate.getStateId());
                    stockTotalAuditInsert.setCurrentQuantityBf(DataUtil.safeToLong(stockTotal.getCurrentQuantity()));
                    stockTotalAuditInsert.setCurrentQuantity(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
                    stockTotalAuditInsert.setCurrentQuantityAf(DataUtil.safeToLong(stockTotal.getCurrentQuantity()) + stockTotalDTO.getCurrentQuantity());
                    // available quantity
                    stockTotalAuditInsert.setAvailableQuantityBf(DataUtil.safeToLong(stockTotal.getAvailableQuantity()));
                    stockTotalAuditInsert.setAvailableQuantity(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
                    stockTotalAuditInsert.setAvailableQuantityAf(DataUtil.safeToLong(stockTotal.getAvailableQuantity()) + stockTotalDTO.getAvailableQuantity());
                    doSaveStockTotalAudit(stockTotalAuditInsert);
                }
                continue;
            }
            //Truong hop them moi
            stockTotalService.save(stockTotalDTO);
            // Luu stock_total_audit
            if (!DataUtil.isNullObject(stockTotalAuditDTO)) {
                StockTotalAuditDTO stockTotalAuditInsert = DataUtil.cloneBean(stockTotalAuditDTO);
                stockTotalAuditInsert.setOwnerId(stockTotalDTO.getOwnerId());
                stockTotalAuditInsert.setOwnerType(stockTotalDTO.getOwnerType());
                stockTotalAuditInsert.setProdOfferId(stockTotalDTO.getProdOfferId());
                stockTotalAuditInsert.setStateId(stockTotalDTO.getStateId());
                stockTotalAuditInsert.setCurrentQuantityBf(0L);
                stockTotalAuditInsert.setCurrentQuantity(DataUtil.safeToLong(stockTotalDTO.getCurrentQuantity()));
                stockTotalAuditInsert.setCurrentQuantityAf(stockTotalDTO.getCurrentQuantity());
                // available quantity
                stockTotalAuditInsert.setAvailableQuantityBf(0L);
                stockTotalAuditInsert.setAvailableQuantity(DataUtil.safeToLong(stockTotalDTO.getAvailableQuantity()));
                stockTotalAuditInsert.setAvailableQuantityAf(stockTotalDTO.getAvailableQuantity());
                doSaveStockTotalAudit(stockTotalAuditInsert);
            }
        }
    }


    private String getTableNameBCCS1(Long stockTypeId) {
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ACCESSORIES, stockTypeId)) {
            return "STOCK_ACCESSORIES";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD, stockTypeId)) {
            return "STOCK_CARD";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, stockTypeId)) {
            return "STOCK_HANDSET";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ISDN_HOMEPHONE, stockTypeId)) {
            return "STOCK_ISDN_HOMEPHONE";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ISDN_MOBILE, stockTypeId)) {
            return "STOCK_ISDN_MOBILE";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ISDN_PSTN, stockTypeId)) {
            return "STOCK_ISDN_PSTN";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT, stockTypeId)) {
            return "STOCK_KIT";
        }

        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID, stockTypeId)) {
            return "STOCK_SIM";
        }
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_POST_PAID, stockTypeId)) {
            return "STOCK_SIM";
        }

        return "";
    }

    private String getUpdateSerialFunc(String tableName, boolean wOSS) throws LogicException {
        if ("STOCK_HANDSET".equals(tableName)) {
            return wOSS ? "F1" : "F2";
        } else if ("STOCK_SIM".equals(tableName)) {
            return wOSS ? "F3" : "F4";
        } else if ("STOCK_CARD".equals(tableName)) {
            return wOSS ? "F5" : "F6";
        } else if ("STOCK_KIT".equals(tableName)) {
            return wOSS ? "F7" : "F8";
        } else if ("STOCK_ACCESSORIES".equals(tableName)) {
            return wOSS ? "F9" : "F10";
        }

        throw new LogicException("ESGUSF_001", "err.update.serial.func.not.specified");
    }


    @Transactional(timeout = 3600)
    public Long updateSerialPairCollection(UpdateSystem system, List<SerialPairData> pairs) throws Exception {
        CallableStatement stmt = null;
        try {
            Connection conn;
            if (system == UpdateSystem.BCCS2) {
                conn = em.unwrap(Connection.class);
            } else {
                conn = emIm1.unwrap(Connection.class);
            }

            Map map = conn.getTypeMap();
            map.put("SERIAL_PAIR", SerialPairData.class);
            conn.setTypeMap(map);

            OracleConnection oraConn = conn.unwrap(OracleConnection.class);

            ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("SERIAL_PAIR_TABLE", oraConn);

            ARRAY array = new ARRAY(descriptor, oraConn, pairs.toArray());

            stmt = conn.prepareCall("begin ? := pkg_update_serial.update_pair_collection(?, ?); end;");

            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.setArray(2, array);
            stmt.registerOutParameter(3, Types.STRUCT, "SERIAL_PAIR");

            stmt.execute();

            long result = stmt.getLong(1);
            SerialPairData errPair = (SerialPairData) stmt.getObject(3);
            if (errPair != null) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(errPair.getwProdOfferId());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText(
                        system == UpdateSystem.BCCS2 ? "stockTrans.validate.quantity.serial" : "stockTrans.validate.quantity.serial.bccs1"),
                        productOfferingDTO.getCode() + " - " + productOfferingDTO.getName(),
                        errPair.getwStateId() == null ? "" : productOfferingService.getStockStateName(errPair.getwStateId()),
                        errPair.getwFromSerial(),
                        errPair.getwToSerial());
            }

            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (stmt != null) stmt.close();
        }
    }

    public void doSaveStockGoods(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail,
                                 FlagStockDTO flagStockDTO) throws LogicException, Exception {
        if (DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        if (DataUtil.isNullObject(flagStockDTO.getOldStatus()) || DataUtil.isNullObject(flagStockDTO.getNewStatus())) {
            return;
        }

        //Neu la giao dich xuat dieu chuyen --> cap nhap serial ve =3
        if (!DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        }


        Long oldOwnerType, oldOwnerId;
        Long newOwnerType = null, newOwnerId = null;

        if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerType()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId()) && flagStockDTO.isUpdateOwnerId()) {
            if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())
                    || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED)
                    || (!Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId()))) {
                newOwnerType = stockTransDTO.getToOwnerType();
                newOwnerId = stockTransDTO.getToOwnerId();
            } else {
                //chon kho 3 mien
                newOwnerType = stockTransDTO.getToOwnerType();
                newOwnerId = stockTransDTO.getRegionStockId();
            }
        }

        if ((Const.STOCK_TRANS_STATUS.EXPORTED.equals(stockTransDTO.getStatus())
                || DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED)
                || Const.STOCK_TRANS_STATUS.IMPORTED.equals(stockTransDTO.getStatus()))
                && !DataUtil.isNullOrZero(stockTransDTO.getRegionStockId())) { // case5
            //neu xuat kho va chon kho 3 mien o lap lenh
            oldOwnerType = stockTransDTO.getFromOwnerType();
            oldOwnerId = stockTransDTO.getRegionStockId();

            //Neu VT Nhap hang tu chi nhanh, dai ly
            if (Const.STOCK_TRANS_STATUS.IMPORTED.equals(stockTransDTO.getStatus()) && Const.L_VT_SHOP_ID.equals(flagStockDTO.getShopId())
                    && !DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.EXPORTED)) {
                oldOwnerType = stockTransDTO.getFromOwnerType();
                oldOwnerId = stockTransDTO.getFromOwnerId();
            }
        } else {
            oldOwnerType = stockTransDTO.getFromOwnerType();
            oldOwnerId = stockTransDTO.getFromOwnerId();
        }

        BigInteger fromSerial;
        BigInteger toSerial;
        Date sysDate = DbUtil.getSysDate(em);
        boolean updateBCCS1 = false;

        ProductOfferingDTO productOfferingDTO;
        ProductOfferTypeDTO productOfferTypeDTO = null;

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                updateBCCS1 = true;
            }
        }

        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            //check trang thai hang hoa
            if (DataUtil.isNullOrZero(stockTransDetail.getProdOfferId())
                    || DataUtil.isNullObject(productOfferingService.findOne(stockTransDetail.getProdOfferId()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.detail"));
            }

            productOfferingDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
            if (productOfferingDTO != null) {
                productOfferTypeDTO = productOfferTypeService.findOne(productOfferingDTO.getProductOfferTypeId());
            }

            if (DataUtil.isNullOrEmpty(stockTransDetail.getTableName())) {
                if (productOfferTypeDTO != null) {
                    stockTransDetail.setTableName(productOfferTypeDTO.getTableName());
                    stockTransDetail.setProdOfferTypeId(productOfferTypeDTO.getProductOfferTypeId());
                }
            }

            String tableName = stockTransDetail.getTableName();
            if (DataUtil.isNullOrEmpty(tableName)) {
                continue;
            }

            Long configStockCard = 0L;
            List<OptionSetValueDTO> lstConfigStockCard = optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP");
            if (!DataUtil.isNullOrEmpty(lstConfigStockCard) && !DataUtil.isNullObject(lstConfigStockCard.get(0).getValue())) {
                configStockCard = DataUtil.safeToLong(lstConfigStockCard.get(0).getValue());
            }
            if (Const.STOCK_TYPE.STOCK_CARD_NAME.equalsIgnoreCase(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
                StockTransActionDTO actionDTO = new StockTransActionDTO();
                actionDTO.setCreateUser(createUser);
                actionDTO.setCreateDatetime(createDate);

                stockCardService.doSaveStockCard(flagStockDTO, stockTransDTO, actionDTO, stockTransDetail, flagStockDTO.getNewStatus(), flagStockDTO.getOldStatus(),
                        newOwnerType, oldOwnerType, newOwnerId, oldOwnerId);
                continue;
            }

            SerialPairData pair = new SerialPairData();

//            StringBuilder sqlUpdate = new StringBuilder("");
//            sqlUpdate.append("UPDATE    ").append(tableName).append(" ");
//            sqlUpdate.append("   SET    update_datetime = sysdate ");

//            HashMap<Integer, Object> params = new HashMap<>();
//            int indx = 1;

            if (!DataUtil.isNullObject(flagStockDTO.getObjectType()) && DataUtil.safeEqual(flagStockDTO.getObjectType(), Const.STOCK_TRANS.OBJECT_TYPE_AGENT_EXPORT)
                    && !DataUtil.isNullOrEmpty(stockTransDTO.getPayStatus()) && DataUtil.safeEqual(Const.PAY_STATUS.PAY_HAVE, stockTransDTO.getPayStatus())) {
//                sqlUpdate.append("      ,status = ? ");
//                params.put(indx++, Const.STOCK_GOODS.STATUS_SALE);
                pair.setsStatus(Const.STOCK_GOODS.STATUS_SALE);
            } else {
//                sqlUpdate.append("      ,status = ? ");
//                params.put(indx++, flagStockDTO.getNewStatus());
                pair.setsStatus(flagStockDTO.getNewStatus());
            }

            if (flagStockDTO.isUpdateSaleDate()) {
//                sqlUpdate.append("      ,sale_date = sysdate ");
                pair.setsSaleDate(sysDate);
            }

            if (flagStockDTO.isGpon()) {//SERIAL_GPON, TVMS_CAD_ID, TVMS_MAC_ID
//                sqlUpdate.append("      ,sale_date = sysdate ");
                pair.setsSaleDate(sysDate);
            }

            if (!DataUtil.isNullOrZero(stockTransDTO.getToOwnerType()) && !DataUtil.isNullOrZero(stockTransDTO.getToOwnerId())
                    && flagStockDTO.isUpdateOwnerId()) {
//                sqlUpdate.append("     ,owner_type = ? ");
//                sqlUpdate.append("     ,owner_id = ? ");
//                params.put(indx++, newOwnerType);
//                params.put(indx++, newOwnerId);

                pair.setsOwnerType(newOwnerType);
                pair.setsOwnerId(newOwnerId);
            }


            boolean isUpdateDepositStatus = false;
            if (flagStockDTO.isUpdateAccountBalance() && !DataUtil.isNullObject(stockTransDTO.getBankplusStatus())) {
//                sqlUpdate.append("     ,bankplus_status = ").append(stockTransDTO.getBankplusStatus());
                pair.setsBankplusStatus(stockTransDTO.getBankplusStatus());
                if (DataUtil.safeEqual(stockTransDTO.getBankplusStatus(), Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE)) {
//                    sqlUpdate.append("     ,deposit_price = 0 ");
                    pair.setsDepositPrice(0L);
                    isUpdateDepositStatus = true;
                }
            }

            if (flagStockDTO.isUpdateDepositPrice() && !isUpdateDepositStatus) {
//                sqlUpdate.append("     ,deposit_price = ").append(stockTransDetail.getDepositPrice());
                pair.setsDepositPrice(DataUtil.safeToLong(stockTransDetail.getDepositPrice(), 0L));
            }
            if (!DataUtil.isNullOrZero(flagStockDTO.getStateIdForReasonId())) {
//                sqlUpdate.append("     ,state_id = ").append(flagStockDTO.getStateIdForReasonId());
                pair.setsStateId(flagStockDTO.getStateIdForReasonId());
            }
            if (flagStockDTO.isUpdateStateDemo()) {
//                sqlUpdate.append("     ,state_id = ").append(flagStockDTO.getStateIdForReasonId());
                pair.setsStateId(DataUtil.safeToLong(stockTransDetail.getStateId()));
                pair.setsSaleDate(null);
            }
            if (flagStockDTO.isUpdateProdOfferId()) {
//                sqlUpdate.append("     ,prod_offer_id = ").append(stockTransDetail.getProdOfferIdChange());
                pair.setsProdOfferId(stockTransDetail.getProdOfferIdChange());
            }

//            sqlUpdate.append(" WHERE    prod_offer_id = ").append(stockTransDetail.getProdOfferId());
            boolean wOSS = false;
            pair.setwProdOfferId(stockTransDetail.getProdOfferId());
            if (!flagStockDTO.isUpdateDamageProduct()) {
                if (!flagStockDTO.isUpdateStateDemo()) {
                    wOSS = true;
                }
//                sqlUpdate.append("   AND owner_type = ").append(oldOwnerType);
//                sqlUpdate.append("   AND owner_id = ").append(oldOwnerId);
//                sqlUpdate.append("   AND status =  ").append(flagStockDTO.getOldStatus());
//                sqlUpdate.append("   AND state_id = ").append(stockTransDetail.getStateId());
                pair.setwOwnerType(oldOwnerType);
                pair.setwOwnerId(oldOwnerId);
                pair.setwStatus(flagStockDTO.getOldStatus());
                pair.setwStateId(stockTransDetail.getStateId());
            }

//            if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getProdOfferTypeId())
//                    || Const.PRODUCT_OFFER_TYPE.ACCESSORIES.equals(stockTransDetail.getProdOfferTypeId())) {
//                sqlUpdate.append("AND   serial >= #fromSerial ");
//                sqlUpdate.append("AND   serial <= #toSerial ");
//            } else {
//                sqlUpdate.append("AND   to_number(serial) >= #fromSerial ");
//                sqlUpdate.append("AND   to_number(serial) <= #toSerial ");
//            }

            List<StockTransSerialDTO> lstStockTransSerial;
            if (flagStockDTO.isSerialListFromTransDetail()) {
                lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
            } else {
                lstStockTransSerial = stockTransSerialService.findByStockTransDetailId(stockTransDetail.getStockTransDetailId());
            }

            if (DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                //bao loi ko co serial
                if (DataUtil.safeEqual(stockTransDetail.getCheckSerial(), 1L)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stock.import.stock.error.no.serial"));
                }
            }

            //set gia tri vao listDetail
            stockTransDetail.setLstStockTransSerial(lstStockTransSerial);

            List<SerialPairData> pairs = new ArrayList<>();
            //Xac dinh ham cap nhat serial
            pair.setwFunc(getUpdateSerialFunc(tableName, wOSS));

            for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                SerialPairData npair = (SerialPairData) BeanUtils.cloneBean(pair);
                pairs.add(npair);

                npair.setwFromSerial(stockTransSerial.getFromSerial());
                npair.setwToSerial(stockTransSerial.getToSerial());


//                String[] params = new String[4];
                //hoangnt: bo phu kien
                if (Const.PRODUCT_OFFER_TYPE.PHONE.equals(stockTransDetail.getProdOfferTypeId())) {
                    npair.setQuantity(1L);
//                    if (count != 1) {
//                        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
//                        params[0] = productOfferingDTO.getName();
//                        params[1] = productOfferingService.getStockStateName(stockTransDetail.getStateId());
//                        params[2] = stockTransSerial.getFromSerial();
//                        params[3] = stockTransSerial.getToSerial();
//                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial"), params);
//                    }
                } else {
                    fromSerial = new BigInteger(stockTransSerial.getFromSerial());
                    toSerial = new BigInteger(stockTransSerial.getToSerial());
                    npair.setQuantity(toSerial.subtract(fromSerial).longValue() + 1);

//                    if (count != (toSerial.subtract(fromSerial).intValue() + 1)) {
//                        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetail.getProdOfferId());
//                        params[0] = productOfferingDTO.getName();
//                        params[1] = productOfferingService.getStockStateName(stockTransDetail.getStateId());
//                        params[2] = stockTransSerial.getFromSerial();
//                        params[3] = stockTransSerial.getToSerial();
//                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial"), params);
//                    }
                }
//                total += count;
            }

            Long total = updateSerialPairCollection(UpdateSystem.BCCS2, pairs);
            if (!DataUtil.safeEqual(stockTransDetail.getQuantity(), total)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.stockDetail"));
            }

            if (updateBCCS1) {
                //Xac dinh ham cap nhat serial
                String tableNameBccs1 = getTableNameBCCS1(productOfferingDTO.getProductOfferTypeId());
                String wFuncBccs1 = getUpdateSerialFunc(tableNameBccs1, wOSS);
                pairs.stream().forEach(x -> x.setwFunc(wFuncBccs1));

                Long totalBccs1 = updateSerialPairCollection(UpdateSystem.BCCS1, pairs);
                if (!DataUtil.safeEqual(stockTransDetail.getQuantity(), totalBccs1)) {
                    //TODO k vao duoc vi thằng if (!DataUtil.safeEqual(stockTransDetail.getQuantity(), total))  nó đã bắt exception rồi
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.stockDetail"));
                }
            }
        }

//        //thaont19: luu thong tin IM1
//        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
//        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
//            if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
//                baseStockIm1Service.doSaveStockGoods(stockTransDTO, lstStockTransDetail, flagStockDTO);
//            }
//        }
    }


    public void doSignVoffice(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, RequiredRoleMap
            requiredRoleMap,
                              FlagStockDTO flagStockDTO) throws Exception {

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

            StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
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

            stockTransVofficeService.save(stockTransVoffice);

        }
    }

    public void doSyncLogistic(StockTransDTO
                                       stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws
            Exception {
        if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_SYNC_LOGISTIC) && Const.STOCK_TRANS.IS_LOGISTIC.equals(stockTransDTO.getLogicstic())) {
            StockTransLogisticDTO stockTransLogistic = new StockTransLogisticDTO();
            stockTransLogistic.setRequestTypeLogistic(Const.LOGISTIC.REQ_TYPE_NEW);
            stockTransLogistic.setCreateDate(stockTransDTO.getCreateDatetime());
            stockTransLogistic.setStockTransId(stockTransDTO.getStockTransId());
            stockTransLogistic.setStatus(Const.LOGISTIC.STATUS_NEW);
            stockTransLogisticService.save(stockTransLogistic);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doIncreaseStockNum(StockTransActionDTO stockTransActionDTO, String
            prefixActionCode, RequiredRoleMap requiredRoleMap) throws Exception {

        if (DataUtil.isNullOrEmpty(prefixActionCode)) {
            return;
        }
        // Cap nhat du lieu sang IM1.
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            baseStockIm1Service.doIncreaseStockNum(stockTransActionDTO, prefixActionCode, requiredRoleMap);
            return;
        }
        StaffDTO staff = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (staff != null) {
            // Bo sung action_code_shop theo nhan vien neu ko co quyen ROLE_STOCK_NUM_SHOP 09/03/2016
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                staff.setStockNum(DataUtil.safeToLong(staff.getStockNum()) + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)
                    || Const.STOCK_TRANS.TRANS_CODE_LN.equals(prefixActionCode)) {
                staff.setStockNumImp(DataUtil.safeToLong(staff.getStockNumImp()) + 1);
            }
            staffService.save(staff);
            //lock ban ghi
            //em.refresh(staffMapper.toPersistenceBean(staff), LockModeType.PESSIMISTIC_WRITE);
            if (requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_STOCK_NUM_SHOP)) {
                updateStockNumShop(staff.getShopId(), prefixActionCode, stockTransActionDTO.getStockTransActionId());
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStockNumShop(Long shopId, String prefixActionCode, Long stockTransActionId) throws Exception {
        ShopDTO shopDTO = shopService.findOne(shopId);
        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
        if (shopDTO != null) {
            //check action Type
            Long num = 0L;
            String actionCodeShop = "";
            //Thuc hien lock ban ghi stockTrans
            //em.refresh(shopMapper.toPersistenceBean(shopDTO), LockModeType.PESSIMISTIC_WRITE);
            if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(prefixActionCode)) {
                num = shopDTO.getStockNum() != null ? shopDTO.getStockNum() : num;
                actionCodeShop = Const.STOCK_TRANS.TRANS_CODE_PX + DataUtil.customFormat("000000", num + 1);
                shopDTO.setStockNum(num + 1);
            } else if (Const.STOCK_TRANS.TRANS_CODE_PN.equals(prefixActionCode)) {
                num = shopDTO.getStockNumImp() != null ? shopDTO.getStockNumImp() : num;
                actionCodeShop = prefixActionCode + DataUtil.customFormat("000000", num + 1);
                shopDTO.setStockNumImp(num + 1);
            }
            if (stockTransActionDTO != null) {
                stockTransActionDTO.setActionCodeShop(actionCodeShop);
                stockTransActionService.save(stockTransActionDTO);
            }
            shopService.save(shopDTO);
        }
    }

    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
        //Xoa ban ghi trong bang lock_user_info
        stockTransRepo.unlockUserInfo(stockTransDTO.getStockTransActionId());
    }

    public void doSaveStockOffline(StockTransDTO
                                           stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        if (!DataUtil.safeEqual(Const.PROCESS_OFFLINE, stockTransDTO.getProcessOffline())) {
            return;
        }
        // TODO không fix được vì trên nó return rồi
        if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        StockTransDTO oldStockTrans = stockTransService.findOne(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(oldStockTrans)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        doSaveStockTransOffline(oldStockTrans);
        List<StockTransDetailDTO> lstOldStockTransDetail = stockTransDetailService.findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                        oldStockTrans.getStockTransId())));
        if (DataUtil.isNullOrEmpty(lstOldStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }
        doSaveStockTransDetailOffline(oldStockTrans, lstOldStockTransDetail);
        doSaveStockTransSerialOffline(oldStockTrans, lstStockTransDetail);
    }

    public void doSaveStockTransOffline(StockTransDTO stockTransDTO) throws Exception {
        StockTransOfflineDTO stockTransOfflineDTO = copyStockTransDTO(stockTransDTO);
        stockTransOfflineDTO.setStatus("1");//Tien trinh chua xu ly
        stockTransOfflineService.save(stockTransOfflineDTO);
        return;
    }

    public void doSaveStockTransDetailOffline(StockTransDTO
                                                      stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            StockTransDetailOfflineDTO stockTransDetailOfflineDTO = copyStockTransDetailDTO(stockTransDetail);
            stockTransDetailOfflineDTO.setStockTransId(stockTransDTO.getStockTransId());
            stockTransDetailOfflineDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            stockTransDetailOfflineService.save(stockTransDetailOfflineDTO);
        }
    }

    public void doSaveStockTransSerialOffline(StockTransDTO
                                                      stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        for (StockTransDetailDTO stockTransDetail : lstStockTransDetail) {
            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetail.getLstStockTransSerial();
            if (DataUtil.safeEqual(stockTransDetail.getCheckSerial(), 1L) && DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
            }
            if (!DataUtil.isNullOrEmpty(lstStockTransSerial)) {
                for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                    StockTransSerialOfflineDTO stockTransSerialOfflineDTO = new StockTransSerialOfflineDTO();
                    stockTransSerialOfflineDTO.setStockTransSerialId(stockTransSerial.getStockTransSerialId());
                    stockTransSerialOfflineDTO.setStockTransDetailId(stockTransDetail.getStockTransDetailId());
                    stockTransSerialOfflineDTO.setFromSerial(stockTransSerial.getFromSerial());
                    stockTransSerialOfflineDTO.setToSerial(stockTransSerial.getToSerial());
                    stockTransSerialOfflineDTO.setQuantity(stockTransSerial.getQuantity());
                    stockTransSerialOfflineDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
                    stockTransSerialOfflineDTO.setStockTransId(stockTransDTO.getStockTransId());
                    stockTransSerialOfflineDTO.setProdOfferId(stockTransDetail.getProdOfferId());
                    stockTransSerialOfflineDTO.setStateId(stockTransDetail.getStateId());
                    stockTransSerialOfflineService.save(stockTransSerialOfflineDTO);
                }
            }
        }
    }

    public StockTransOfflineDTO copyStockTransDTO(StockTransDTO stockTransDTO) {
        StockTransOfflineDTO stockTransOfflineDTO = new StockTransOfflineDTO();
        if (!DataUtil.isNullObject(stockTransDTO)) {
            stockTransOfflineDTO.setStockTransId(stockTransDTO.getStockTransId());
            stockTransOfflineDTO.setFromOwnerId(stockTransDTO.getFromOwnerId());
            stockTransOfflineDTO.setFromOwnerType(stockTransDTO.getFromOwnerType());
            stockTransOfflineDTO.setToOwnerId(stockTransDTO.getToOwnerId());
            stockTransOfflineDTO.setToOwnerType(stockTransDTO.getToOwnerType());
            stockTransOfflineDTO.setStockTransType(stockTransDTO.getStockTransType());
            stockTransOfflineDTO.setStatus(stockTransDTO.getStatus());
            stockTransOfflineDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            stockTransOfflineDTO.setTotalAmount(stockTransDTO.getTotalAmount());
            stockTransOfflineDTO.setReasonId(stockTransDTO.getReasonId());
            stockTransOfflineDTO.setNote(stockTransDTO.getNote());
            stockTransOfflineDTO.setStockBase(stockTransDTO.getStockBase());
            stockTransOfflineDTO.setCheckErp(stockTransDTO.getCheckErp());
            stockTransOfflineDTO.setPayStatus(stockTransDTO.getPayStatus());
            stockTransOfflineDTO.setDepositStatus(stockTransDTO.getDepositStatus());
            stockTransOfflineDTO.setRequestId(stockTransDTO.getRequestId());
            stockTransOfflineDTO.setRegionStockTransId(stockTransDTO.getRegionStockTransId());
            stockTransOfflineDTO.setIsAutoGen(stockTransDTO.getIsAutoGen());
            stockTransOfflineDTO.setTransport(stockTransDTO.getTransport());
            stockTransOfflineDTO.setProcessOffline(stockTransDTO.getProcessOffline());
            stockTransOfflineDTO.setProcessStartDate(stockTransDTO.getProcessStartDate());
            stockTransOfflineDTO.setProcessEndDate(stockTransDTO.getProcessEndDate());
            //stockTransOfflineDTO.setInvoiceStatus(stockTransDTO.getInvoiceStatus());
            stockTransOfflineDTO.setImportReasonId(stockTransDTO.getImportReasonId());
            stockTransOfflineDTO.setImportNote(stockTransDTO.getImportNote());
            stockTransOfflineDTO.setRejectReasonId(stockTransDTO.getRejectReasonId());
            stockTransOfflineDTO.setRejectNote(stockTransDTO.getRejectNote());
        }
        return stockTransOfflineDTO;
    }

    public StockTransDetailOfflineDTO copyStockTransDetailDTO(StockTransDetailDTO stockTransDetailDTO) {
        StockTransDetailOfflineDTO stockTransDetailOfflineDTO = new StockTransDetailOfflineDTO();
        if (!DataUtil.isNullObject(stockTransDetailDTO)) {
            stockTransDetailOfflineDTO.setStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
            stockTransDetailOfflineDTO.setStockTransId(stockTransDetailDTO.getStockTransId());
            stockTransDetailOfflineDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
            stockTransDetailOfflineDTO.setStateId(stockTransDetailDTO.getStateId());
            stockTransDetailOfflineDTO.setQuantity(stockTransDetailDTO.getQuantity());
            stockTransDetailOfflineDTO.setPrice(stockTransDetailDTO.getPrice());
            stockTransDetailOfflineDTO.setAmount(stockTransDetailDTO.getAmount());
            stockTransDetailOfflineDTO.setCreateDatetime(stockTransDetailDTO.getCreateDatetime());
        }

        return stockTransDetailOfflineDTO;
    }

    public void doSaveReceiptExpense(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        if (!flagStockDTO.isInsertReceiptExpense()) {
            return;
        }

        if (DataUtil.safeEqual(flagStockDTO.getDepositStatus(), Const.STOCK_STRANS_DEPOSIT.DEPOSIT_CANCEL)) {
            doCancelReceiptExpense(flagStockDTO, stockTransDTO, stockTransActionDTO, lstStockTransDetail);
        } else {
            ReceiptExpenseDTO receiptExpenseDTO = new ReceiptExpenseDTO();
            receiptExpenseDTO.setReceiptNo(stockTransActionDTO.getActionCode());
            receiptExpenseDTO.setShopId(stockTransDTO.getShopId());
            receiptExpenseDTO.setStaffId(stockTransDTO.getToOwnerId());
            receiptExpenseDTO.setType(flagStockDTO.getReceiptExpenseType());
            receiptExpenseDTO.setReceiptTypeId(flagStockDTO.getReceiptExpenseTypeId());
            receiptExpenseDTO.setReceiptDate(stockTransDTO.getCreateDatetime());
            receiptExpenseDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());
            receiptExpenseDTO.setPayMethod(flagStockDTO.getReceiptExpensePayMethod());
            receiptExpenseDTO.setAmount(stockTransDTO.getTotalAmount());
            receiptExpenseDTO.setUsername(stockTransDTO.getUserCreate());
            receiptExpenseDTO.setStatus(flagStockDTO.getReceiptExpenseStatus());
            receiptExpenseDTO.setDeliverer(stockTransDTO.getDevliver());
            receiptExpenseDTO.setReasonId(stockTransDTO.getReasonId());
            receiptExpenseDTO = receiptExpenseService.create(receiptExpenseDTO, stockTransDTO.getUserCreate());
            // Luu vao bang deposit
            DepositDTO depositDTO = new DepositDTO();
            depositDTO.setAmount(stockTransDTO.getTotalAmount());
            depositDTO.setReceiptId(receiptExpenseDTO.getReceiptId());
            depositDTO.setReasonId(stockTransDTO.getReasonId());
            depositDTO.setShopId(stockTransDTO.getShopId());
            depositDTO.setStaffId(stockTransDTO.getToOwnerId());
            depositDTO.setStockTransId(stockTransDTO.getStockTransId());
            depositDTO.setType(flagStockDTO.getDepositType());
            depositDTO.setStatus(flagStockDTO.getDepositStatus());
            depositDTO.setDepositTypeId(DataUtil.safeToLong(flagStockDTO.getDepositTypeId()));
            depositDTO.setCreateBy(stockTransDTO.getUserCreate());
            depositDTO = depositService.create(depositDTO);
            //save deposit_detail
            if (!DataUtil.isNullObject(lstStockTransDetail)) {
                for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
                    DepositDetailDTO depositDetailDTO = new DepositDetailDTO();
                    depositDetailDTO.setAmount(stockTransDetailDTO.getAmount());
                    depositDetailDTO.setDepositId(depositDTO.getDepositId());
                    depositDetailDTO.setDepositType(DataUtil.safeToString(depositDTO.getDepositTypeId()));
                    depositDetailDTO.setQuantity(stockTransDetailDTO.getQuantity());
                    depositDetailDTO.setPrice(stockTransDetailDTO.getPrice());
                    depositDetailDTO.setDepositType(flagStockDTO.getDepositType());
                    depositDetailDTO.setStockModelId(stockTransDetailDTO.getProdOfferId());
                    depositDetailDTO.setPriceId(DataUtil.safeToLong(stockTransDetailDTO.getProductOfferPriceId()));
                    depositDetailService.create(depositDetailDTO);
                }
            }
        }
    }

    public void doSaveStockTotalAudit(StockTotalAuditDTO stockTotalAuditDTO) throws Exception {
        if (DataUtil.isNullObject(stockTotalAuditDTO)) {
            return;
        }
        stockTotalAuditDTO.setSourceType(Const.STOCK_TOTAL_AUDIT.SOURCE_TYPE_STOCK_TRANS);
        stockTotalAuditService.save(stockTotalAuditDTO);
    }

    public void doSaveExchangeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, FlagStockDTO flagStockDTO, List<StockTransDetailDTO> lstStockTransDetail) throws
            Exception {

        if (DataUtil.isNullOrZero(stockTransDTO.getExchangeStockId())) {
            return;
        }

        //Tao giao dich xuat kho dieu chuyen
        StockTransDTO exportDTO = new StockTransDTO();
        if (DataUtil.isNullOrZero(stockTransDTO.getRegionStockTransId())) {
            exportDTO = DataUtil.cloneBean(stockTransDTO);
            exportDTO.setStockTransId(null);
            exportDTO.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
            exportDTO.setImportReasonId(null);
            exportDTO.setReasonId(stockTransDTO.getImportReasonId());
            exportDTO.setIsAutoGen(3L);
            exportDTO.setNote(flagStockDTO.getNote());
            exportDTO.setFromOwnerId(stockTransDTO.getToOwnerId());
            exportDTO.setToOwnerId(stockTransDTO.getExchangeStockId());
        } else {
            exportDTO.setStockTransId(stockTransDTO.getRegionStockTransId());
        }
        exportDTO.setStatus(flagStockDTO.getRegionExportStatus());
        exportDTO = doSaveStockTrans(exportDTO);
        exportDTO.setCreateDatetime(stockTransDTO.getCreateDatetime());//Set lai cho truong hop update

        //Luu chi tiet mat hang
        doSaveStockTransDetail(exportDTO, lstStockTransDetail);

        //Ghi log giao dich xuat
        StockTransActionDTO exportActionDTO = DataUtil.cloneBean(stockTransActionDTO);
        exportActionDTO.setStatus(flagStockDTO.getRegionExportStatus());
        exportActionDTO.setActionCode(DataUtil.safeToString(flagStockDTO.getPrefixExportActionCode()) + DataUtil.safeToString(exportActionDTO.getActionCode()));
        exportActionDTO.setRegionOwnerId(stockTransDTO.getExchangeStockId());
        exportActionDTO.setNote(flagStockDTO.getNote());
//        exportActionDTO.setIsRegionExchange(true);
        StockTransActionDTO stockTransActionSave = doSaveStockTransAction(exportDTO, exportActionDTO);

        stockTransDTO.setRegionStockTransId(exportDTO.getStockTransId());
        stockTransDTO.setExchangeStockTransActionId(stockTransActionSave.getStockTransActionId());

        return;
    }

    public void doCancelReceiptExpense(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
        if (!flagStockDTO.isInsertReceiptExpense()) {
            return;
        }

        List<FilterRequest> filters = new ArrayList<>();
        FilterRequest filterRequestStockTransId = new FilterRequest(Deposit.COLUMNS.STOCKTRANSID.name(), stockTransDTO.getStockTransId());
        FilterRequest filterRequestStatus = new FilterRequest(Deposit.COLUMNS.STATUS.name(), Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
        filters.add(filterRequestStockTransId);
        filters.add(filterRequestStatus);
        List<DepositDTO> depositDTOs = depositService.findByFilter(filters);

        if (depositDTOs != null && !depositDTOs.isEmpty()) {
            DepositDTO depositDTO = depositDTOs.get(0);
            depositDTO.setStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_CANCEL);
            ReceiptExpenseDTO receiptExpenseDTO = receiptExpenseService.findOne(depositDTO.getReceiptId());
            receiptExpenseDTO.setStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
            depositService.update(depositDTO.getDepositId(), depositDTO.getStatus());
            receiptExpenseService.update(receiptExpenseDTO, receiptExpenseDTO.getStatus());
        }
        return;

    }

    @Transactional(rollbackFor = Exception.class)
    public void doUpdateBankplusBCCS(FlagStockDTO flagStockDTO, StockTransDTO stockTransDTO) throws Exception {
        if (!flagStockDTO.isUpdateAccountBalance()
                || DataUtil.isNullObject(stockTransDTO)
                || DataUtil.isNullOrZero(stockTransDTO.getBankplusStatus())
                || DataUtil.isNullOrZero(stockTransDTO.getAccountBalanceId())
                || !DataUtil.safeEqual(stockTransDTO.getBankplusStatus(), Const.STOCK_TRANS.BANKPLUS_STATUS_PENDING_1)) {
            return;
        }
        // Lay thong tin va lock tai khoan bankplus
        Long totalAmount = stockTransDTO.getTotalAmount();
        if (DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
            totalAmount = stockTransDTO.getDepositTotal();
        }

        if (!DataUtil.isNullObject(stockTransDTO.getDepositPrice()) && !DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
            totalAmount = stockTransDTO.getDepositPrice();
        }
        AccountBalanceDTO accountBalanceDTO = accountBalanceService.findLock(stockTransDTO.getAccountBalanceId());
//        if (((DataUtil.isNullObject(accountBalanceDTO) || accountBalanceDTO.getRealBalance() < totalAmount)
//                && (!DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED)))
//                || ((DataUtil.isNullObject(accountBalanceDTO) || totalAmount > 0) && DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED))) {
//            // Cap nhat lai trang thai bankplus_status trong bang stock_trans
//            stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE);
//            stockTransService.save(stockTransDTO);
//            return;
//        }
        if ((DataUtil.isNullObject(accountBalanceDTO) || accountBalanceDTO.getRealBalance() < totalAmount)
                && !DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.bankplus.not.enough");
        }
        if ((DataUtil.isNullObject(accountBalanceDTO) || totalAmount > 0) && DataUtil.safeEqual(flagStockDTO.getStatusForAgent(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.bankplus.change.account.invalid");
        }

        // Luu log vao bang account_book_bankplus
        Date sysdate = DbUtil.getSysDate(em);
        AccountBookBankplusDTO accountBookBankplusDTO = new AccountBookBankplusDTO();
        accountBookBankplusDTO.setAccountId(accountBalanceDTO.getAccountId()); // account_balance.account_id
        accountBookBankplusDTO.setAmountRequest(-totalAmount);
        accountBookBankplusDTO.setDebitRequest(0L);
        accountBookBankplusDTO.setCreateDate(sysdate);
        accountBookBankplusDTO.setRequestType(flagStockDTO.getRequestType());
        accountBookBankplusDTO.setReturnDate(stockTransDTO.getCreateDatetime());
        accountBookBankplusDTO.setRealTransDate(stockTransDTO.getCreateDatetime());
        accountBookBankplusDTO.setStatus(2L);
        accountBookBankplusDTO.setStockTransId(stockTransDTO.getStockTransId());
        accountBookBankplusDTO.setUserRequest(stockTransDTO.getUserCreate());
        accountBookBankplusDTO.setOpeningBalance(accountBalanceDTO.getRealBalance());
        accountBookBankplusDTO.setClosingBalance(accountBalanceDTO.getRealBalance() - totalAmount);
        accountBookBankplusDTO.setIpAddress(stockTransDTO.getCreateUserIpAdress());
        accountBookBankplusService.create(accountBookBankplusDTO);
        // Tru tien trong tai khoan bankplus bccs.
        accountBalanceDTO.setRealBalance(accountBalanceDTO.getRealBalance() - totalAmount);
        accountBalanceDTO.setLastUpdateTime(sysdate);
        accountBalanceService.create(accountBalanceDTO);
    }

}


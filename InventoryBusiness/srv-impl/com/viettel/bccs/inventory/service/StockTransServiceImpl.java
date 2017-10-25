package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.BaseStockIm1Service;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.service.SaleTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;

import javax.jws.WebMethod;
import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;

@Service
public class StockTransServiceImpl extends BaseServiceImpl implements StockTransService {

    private final BaseMapper<StockTrans, StockTransDTO> mapper = new BaseMapper(StockTrans.class, StockTransDTO.class);
    private final BaseMapper<StockTransDetail, StockTransDetailDTO> detailMapper = new BaseMapper<>(StockTransDetail.class, StockTransDetailDTO.class);
    private final BaseMapper<StockTransSerial, StockTransSerialDTO> serialMapper = new BaseMapper<>(StockTransSerial.class, StockTransSerialDTO.class);
    private final BaseMapper<StockTransAction, StockTransActionDTO> actionMapper = new BaseMapper<>(StockTransAction.class, StockTransActionDTO.class);
    private final BaseMapper<ProductOffering, ProductOfferingDTO> offerMapper = new BaseMapper<>(ProductOffering.class, ProductOfferingDTO.class);
    private final BaseMapper<StockTransExt, StockTransExtDTO> extMapper = new BaseMapper<>(StockTransExt.class, StockTransExtDTO.class);
    private static final String ERR_NO_REVOKE_SERIAL = "ERR_NO_REVOKE_SERIAL";

    @Autowired
    private StockTransRepo repository;
    @Autowired
    private StockTransActionRepo actionRepo;
    @Autowired
    private StockTransDetailRepo stockTransDetailRepo;
    @Autowired
    private StockTransActionRepo stockTransActionRepo;
    @Autowired
    private StockTransSerialRepo stockTransSerialRepo;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockTransOfflineService stockTransOfflineService;
    @Autowired
    private StockTransDetailOfflineService stockTransDetailOfflineService;
    @Autowired
    private StockTransSerialOfflineService stockTransSerialOfflineService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;
    @Autowired
    private BaseStockIm1Service baseStockIm1Service;
    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private StockTransExtRepo stockTransExtRepo;

    @Autowired
    private SaleTransService saleTransService;

    @Autowired
    private ReasonService reasonService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(StockTransService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public StockTransDTO findOneLock(Long id) throws Exception {
        try {
            StockTrans stockTrans = em.find(StockTrans.class, id, LockModeType.PESSIMISTIC_READ);
            em.refresh(stockTrans);

            return mapper.toDtoBean(stockTrans);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "sell.store.busy");
        }
    }

    @WebMethod
    public List<StockTransDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTransDTO save(StockTransDTO stockTransDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDTO)));
    }

    @WebMethod
    public List<VStockTransDTO> searchVStockTrans(VStockTransDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto)) {
            return new ArrayList<VStockTransDTO>();
        }
        if (DataUtil.isNullObject(dto.getStartDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.from.date.not.blank");
        }
        if (DataUtil.isNullObject(dto.getEndDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.to.date.not.blank");
        }
        long day = DateUtil.daysBetween2Dates(dto.getEndDate(), dto.getStartDate());
        if (day < 0 || day > 30) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.from.to.valid");
        }
        return repository.searchVStockTrans(dto);
    }

    @Override
    public List<VStockTransDTO> searchMaterialVStockTrans(VStockTransDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto)) {
            return new ArrayList<VStockTransDTO>();
        }
        if (DataUtil.isNullObject(dto.getStartDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.from.date.not.blank");
        }
        if (DataUtil.isNullObject(dto.getEndDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.to.date.not.blank");
        }
        long day = DateUtil.daysBetween2Dates(dto.getEndDate(), dto.getStartDate());
        if (day < 0 || day > 30) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.from.to.valid");
        }

        ReasonDTO reasonDTO = reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_DEPLOY, Const.REASON_CODE.EXP_MATERIAL_DEPLOY);

        dto.setReasonID(reasonDTO.getReasonId());

        return repository.searchMaterialVStockTrans(dto);
    }

    @Override
    public List<StockTransFullDTO> searchStockTransDetail(List<Long> stockTransActionIds) throws LogicException, Exception {
        List<StockTransFullDTO> stockTransFullDTOs = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(stockTransActionIds)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.searchObject.valid");
        }
        for (Long stockTransActionId : stockTransActionIds) {
            if (DataUtil.isNullOrZero(stockTransActionId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.searchObject.valid");
            }
            StockTransAction stockTransAction = actionRepo.findOne(stockTransActionId);
            if (DataUtil.isNullObject(stockTransAction)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.searchObject.valid");
            }
            List<StockTransFullDTO> querryResult = repository.searchStockTransDetail(stockTransActionId);
            stockTransFullDTOs.addAll(querryResult);
        }
        return stockTransFullDTOs;
    }

    @Override
    public Long getTotalValueStockSusbpendByOwnerId(Long ownerId, String ownerType) throws LogicException, Exception {
        return repository.getTotalValueStockSusbpendByOwnerId(ownerId, ownerType);
    }

    @Override
    public StockTransDTO findStockTransByActionId(Long actionId) throws LogicException, Exception {
        return repository.findStockTransByActionId(actionId);
    }

    @Override
    public List<VStockTransDTO> findStockSuspendDetail(Long ownerId, String ownerType) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(ownerType)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "limit.stock.lookup.invalid.ownerType");
        }
        if (DataUtil.isNullOrZero(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "limit.stock.lookup.invalid.ownerId");
        }
        return repository.findStockSuspendDetail(ownerId, ownerType);
    }

    @Override
    public List<ManageTransStockViewDto> findStockTrans(ManageTransStockDto transStockDto) throws LogicException, Exception {
        return repository.findStockTrans(transStockDto);
    }

    @Override
    public List<ManageStockTransViewDetailDto> viewDetailStockTrans(Long stockTransId) throws Exception {
        return repository.viewDetailStockTrans(stockTransId);
    }

    @Transactional(rollbackFor = Exception.class)
    public StockTransDTO checkAndLockReceipt(Long stockTransId) throws Exception {
        return repository.checkAndLockReceipt(stockTransId);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public int updateDepositStatus(Long stockTransId, String status) throws Exception {
        try {
            StockTransDTO dto = findOne(stockTransId);
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "");
            }
            dto.setDepositStatus(status);
            repository.save(mapper.toPersistenceBean(dto));
            return 1;
        } catch (LogicException e) {
            throw e;

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceType.update");
        }
    }

    @WebMethod
    public List<VStockTransDTO> searchVStockTransAgent(VStockTransDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto)) {
            return new ArrayList<VStockTransDTO>();
        }
        if (DataUtil.isNullObject(dto.getStartDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.from.date.not.blank");
        }
        if (DataUtil.isNullObject(dto.getEndDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.to.date.not.blank");
        }
        long day = DateUtil.daysBetween2Dates(dto.getEndDate(), dto.getStartDate());
        if (day < 0 || day > 30) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.from.to.valid");
        }
        if (DataUtil.isNullObject(dto.getToOwnerID())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "agent.sale.shopCode.require.msg");
        }
        dto.setPayStatus(Const.STOCK_STRANS_PAY.PAY_ORDER);
        dto.setStockTransType(DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.EXPORT));
        dto.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        dto.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        return repository.searchVStockTransAgent(dto);
    }

    @Override
    public Long getSequence() throws Exception {
        return repository.getSequence();
    }

    @Override
    public List<ManageTransStockViewDto> searchStockTransForTransfer(TranferIsdnInfoSearchDto transStockDto) throws LogicException, Exception {
        return repository.searchStockTransForTransfer(transStockDto);
    }

    @Override
    public boolean checkStatusRetrieveExpense(Long stockTransId, String status, String depositStatus) throws LogicException, Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(StockTrans.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
            if (!DataUtil.isNullObject(status)) {
                lst.add(new FilterRequest(StockTrans.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, status));
            }
            if (!DataUtil.isNullObject(depositStatus)) {
                lst.add(new FilterRequest(StockTrans.COLUMNS.DEPOSITSTATUS.name(), FilterRequest.Operator.NE, depositStatus));
            }
            Date sysdate = getSysDate(em);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sysdate);
            calendar.add(Calendar.DATE, -30);
            lst.add(new FilterRequest(StockTrans.COLUMNS.CREATEDATETIME.name(), FilterRequest.Operator.GT, calendar.getTime()));
            return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public List<VStockTransDTO> mmSearchVStockTrans(VStockTransDTO vStockTransDTO) throws LogicException, Exception {
        return repository.mmSearchVStockTrans(vStockTransDTO);
    }

    @Override
    public boolean checkExistTransByfieldExportIsdnDTO(FieldExportFileDTO fieldExportFileDTO) throws LogicException, Exception {
        return repository.checkExistTransByfieldExportIsdnDTO(fieldExportFileDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createStockTrans(Date createDate, Long stockTransId, ShopDTO pos, List<StockWarrantyDTO> stockList, StaffDTO staff, Long revokeType) throws LogicException, Exception {

        //Buoc 3.1: tao gd kho
        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setStockTransId(stockTransId);
        stockTrans.setCreateDatetime(createDate);

        //Neu to doi thuc hien thu hoi
        if (revokeType.compareTo(1L) == 0) {
            stockTrans.setToOwnerId(pos.getShopId());
            stockTrans.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        } else {
            //Nhan vien cua hang thuc hien thu hoi
            stockTrans.setToOwnerId(staff.getStaffId());
            stockTrans.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        }

        stockTrans.setFromOwnerId(Const.OWNER_CUST_ID);
        stockTrans.setFromOwnerType(Const.OWNER_TYPE_REVOKE);
        stockTrans.setStockTransType(Const.IMPORT);
        stockTrans.setStatus(Const.TRANS_IMPORT);

        StockTransDTO stockTransToSave = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTrans)));

        //Luu stock_trans_action: Lap phieu
        StockTransActionDTO transAction = new StockTransActionDTO();
        transAction.setStockTransId(stockTransToSave.getStockTransId());
        transAction.setActionCode("MPN" + stockTransToSave.getStockTransId()); //Ma phieu xuat
        transAction.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE); //action = lap phieu
        transAction.setCreateDatetime(createDate);
        transAction.setActionStaffId(staff == null ? null : staff.getStaffId());
        stockTransActionRepo.save(actionMapper.toPersistenceBean(transAction));

        //Luu stock_trans_action: Nhap kho
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        transActionImport.setStatus(Const.TRANS_IMPORT); //action = nhap kho
        transActionImport.setStockTransId(stockTransToSave.getStockTransId());
        transActionImport.setCreateDatetime(createDate);
        transActionImport.setActionStaffId(staff == null ? null : staff.getStaffId());
        stockTransActionRepo.save(actionMapper.toPersistenceBean(transActionImport));

        //Buoc 3.2: luu chi tiet gd
        for (StockWarrantyDTO sw : stockList) {
            StockTransDetailDTO std = new StockTransDetailDTO();
            std.setStockTransId(stockTransId);
            std.setProdOfferId(sw.getProdOfferId());
            std.setStateId(Const.STATE_STATUS.RETRIEVE);
            std.setQuantity(1L);
            std.setCreateDatetime(createDate);

            StockTransDetailDTO stockTransDetailToSave = detailMapper.toDtoBean(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std)));

            if (!DataUtil.isNullOrEmpty(sw.getRevokedSerial())) {
                StockTransSerialDTO sts = new StockTransSerialDTO();
                sts.setStockTransId(stockTransId);
                sts.setStockTransDetailId(stockTransDetailToSave.getStockTransDetailId());
                sts.setProdOfferId(sw.getProdOfferId());
                sts.setStateId(Const.STATE_STATUS.RETRIEVE);
                sts.setFromSerial(sw.getRevokedSerial());
                sts.setToSerial(sw.getRevokedSerial());
                sts.setQuantity(1L);
                sts.setCreateDatetime(createDate);
                stockTransSerialRepo.save(serialMapper.toPersistenceBean(sts));
            }

            //Cong so luong trong bang stockTotal


            Long ownerType;
            Long ownerId;
            if (revokeType.compareTo(1L) == 0) {
                //Neu to doi thuc hien thu hoi
                ownerType = Const.OWNER_TYPE.SHOP_LONG;
                ownerId = pos.getShopId();
            } else {
                //Nhan vien cua hang thuc hien thu hoi
                ownerType = Const.OWNER_TYPE.STAFF_LONG;
                ownerId = staff == null ? null : staff.getStaffId();
            }

            Long stockState = Const.STATE_STATUS.RETRIEVE;

            //Check serial trong kho ao
            boolean isShopVirtual = inventoryService.checkExistVirtualShop(sw.getOldOwnerId());
            if (isShopVirtual) {
                ownerType = Const.OWNER_TYPE.SHOP_LONG;
                ownerId = sw.getOldOwnerId();
                stockState = Const.GOODS_STATE.NEW;
                revokeType = 1L;
            }

            //luu thay doi BCCS2.0
            StockTotalMessage msg = stockTotalService.changeStockTotal(
                    ownerId,
                    ownerType,
                    sw.getProdOfferId(),
                    stockState,
                    1L, //qty
                    1L, //qtyIssue
                    0L, //qtyHang
                    staff == null ? null : staff.getStaffId(), //userId
                    null, //reasonId
                    stockTransId,
                    createDate,
                    stockTransId.toString(),
                    "2",//giao dich nhap
                    Const.SourceType.STOCK_TRANS);
            if (!msg.isSuccess()) {
                throw new LogicException("", msg.getKeyMsg());
            }

            /**
             * Cap nhat chi tiet serial
             */
            //Ket qua cap nhat serial ve kho cua doi tuong thu hoi tren BCCS2.0
            boolean updateResult = repository.updateSerialForRevoke(sw.getProdOfferTypeId(), sw.getProdOfferId(), sw.getRevokedSerial(), ownerId, stockState, sw.getOldOwnerType(), sw.getOldOwnerId(), revokeType);
            if (!updateResult) {
                sw.setErrCode(ERR_NO_REVOKE_SERIAL);
                if (revokeType.compareTo(1L) == 0 && !isShopVirtual) {
                    sw.setDescription(getText("crt.err.revoke.serial.team.fail"));
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "crt.err.update.serial.team.fail", sw.getProdOfferId(), sw.getRevokedSerial());
                } else {
                    sw.setDescription(getText("crt.err.revoke.serial.staff.fail"));
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "crt.err.update.serial.staff.fail", sw.getProdOfferId(), sw.getRevokedSerial());
                }
            }
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
                if (Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    //Ket qua cap nhat serial ve kho cua doi tuong thu hoi tren BCCS1.0
                    updateResult = baseStockIm1Service.updateSerialForRevoke(sw.getProdOfferTypeId(), sw.getProdOfferId(), sw.getRevokedSerial(), ownerId, stockState, sw.getOldOwnerType(), sw.getOldOwnerId(), revokeType);
                    if (!updateResult) {
                        sw.setErrCode(ERR_NO_REVOKE_SERIAL);
                        if (revokeType.compareTo(1L) == 0 && !isShopVirtual) {
                            sw.setDescription(getText("crt.err.revoke.serial.team.fail.bccs1"));
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "crt.err.update.serial.team.fail.bccs1", sw.getProdOfferId(), sw.getRevokedSerial());
                        } else {
                            sw.setDescription(getText("crt.err.revoke.serial.staff.fail.bccs1"));
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "crt.err.update.serial.staff.fail.bccs1", sw.getProdOfferId(), sw.getRevokedSerial());
                        }
                    }
                }
            }


        }
    }

    @Override
    public Long getStockTransIdByCodeExist(String cmdCode, String noteCode, Long fromShopId, Long toShopId) throws Exception {
        return repository.getStockTransIdByCodeExist(cmdCode, noteCode, fromShopId, toShopId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStockTransOffline(List<StockTransDTO> lsStockTransDTOs) throws LogicException, Exception {
        if (!DataUtil.isNullOrEmpty(lsStockTransDTOs)) {
            Date sysDate = getSysDate(em);
            for (StockTransDTO stockTransDTO : lsStockTransDTOs) {
                StockTransDTO transDTO = findOne(stockTransDTO.getStockTransId());
                if (transDTO != null) {
                    //update thong tin bang StockTransDTO voi trang thai offline
                    transDTO.setStatus(Const.STOCK_TRANS.STATUS_OFFLINE);
                    transDTO.setProcessStartDate(sysDate);
                    save(transDTO);
                    //tao moi data bang StockTransOffline
                    StockTransOfflineDTO transOfflineDTO = new StockTransOfflineDTO(transDTO);
                    transOfflineDTO.setStatus(Const.STATUS.ACTIVE);
                    transOfflineDTO.setTotalAmount(transDTO.getTotalAmount());
                    stockTransOfflineService.save(transOfflineDTO);
                    List<StockTransDetailDTO> lsStockTransDetail = stockTransDTO.getLsStockTransDetailDTOList();

                    if (!DataUtil.isNullOrEmpty(lsStockTransDetail)) {
                        for (StockTransDetailDTO stockTransDetailDTO : lsStockTransDetail) {
                            //tao moi thong tin bang  StockTransDetailOffline
                            StockTransDetailOfflineDTO detailOfflineDTO = new StockTransDetailOfflineDTO(stockTransDetailDTO);
                            if (Const.GOODS_STATE.BROKEN.equals(detailOfflineDTO.getStateId()) || Const.GOODS_STATE.RETRIVE.equals(detailOfflineDTO.getStateId())) {
                                detailOfflineDTO.setAmount(null);
                                detailOfflineDTO.setPrice(null);
                            }
                            detailOfflineDTO.setCreateDatetime(sysDate);
                            stockTransDetailOfflineService.save(detailOfflineDTO);
                            List<StockTransSerialDTO> lstSerial = stockTransDetailDTO.getLstSerial();
                            if (!DataUtil.isNullOrEmpty(lstSerial)) {
                                for (StockTransSerialDTO stockTransSerialDTO : lstSerial) {
                                    // tao moi data bang StockTransSerialOffline
                                    StockTransSerialOfflineDTO stso = new StockTransSerialOfflineDTO(stockTransSerialDTO);
                                    stso.setCreateDatetime(sysDate);
                                    stso.setStockTransDetailId(stockTransDetailDTO.getStockTransDetailId());
                                    stso.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                                    stso.setStockTransId(detailOfflineDTO.getStockTransId());
                                    stso.setStateId(stockTransDetailDTO.getStateId());
                                    stockTransSerialOfflineService.save(stso);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<StockTransFullDTO> getListStockFullDTOByTransIdAndStatus(List<Long> lsStockTransId, List<Long> lsStatusAction) throws Exception {
        return repository.getListStockFullDTOByTransIdAndStatus(lsStockTransId, lsStatusAction);
    }

    public boolean checkStockSusbpendForProcessStock(Long ownerId, String ownerType, Long productOfferId) throws LogicException, Exception {

        return repository.checkStockSusbpendForProcessStock(ownerId, ownerType, productOfferId);
    }

    @Override
    public List<SaleTransDTO> getSaleTransFromStockTrans(Long stockTransId) throws LogicException, Exception {
        List<SaleTransDTO> lstSaleTrans = saleTransService.findSaleTransByStockTransId(stockTransId, null);// sale: goi ham ben sale
        return lstSaleTrans;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAPDeployment(StaffDTO staffDTO, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception {

        //Buoc 1: tao gd kho
        Date createDate = getSysDate(em);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTrans.setFromOwnerId(staffDTO.getStaffId());
        stockTrans.setToOwnerType(Const.OWNER_TYPE_CUST);
        stockTrans.setToOwnerId(Const.OWNER_CUST_ID);
        stockTrans.setStockTransType(Const.IMPORT);
        stockTrans.setStatus(Const.TRANS_IMPORT);
        stockTrans.setCreateDatetime(createDate);
        stockTrans.setAccType(Const.ACC_TYPE_TROUBLE);
        stockTrans.setAccount(account);
        stockTrans.setTroubleType(troubleType);
        stockTrans.setReasonId(Const.REASON_EXP_SUPPLIES);

        StockTransDTO stockTransToSave = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTrans)));

        //Luu transId check trung lap
        StockTransExtDTO stockTransExt = new StockTransExtDTO();
        stockTransExt.setStockTransId(stockTransToSave.getStockTransId());
        stockTransExt.setExtKey("TROUBLE_ID");
        stockTransExt.setExtValue(transId);
        stockTransExt.setStatus(Const.STATUS_ACTIVE);

        stockTransExtRepo.save(extMapper.toPersistenceBean(stockTransExt));

        //Luu stock_trans_action
        StockTransActionDTO transAction = new StockTransActionDTO();
        transAction.setStockTransId(stockTransToSave.getStockTransId());
        transAction.setActionCode("MPX" + stockTransToSave.getStockTransId().toString()); //Ma phieu xuat
        transAction.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE); //action = lap phieu
        transAction.setCreateDatetime(createDate);
        if (!DataUtil.isNullObject(staffDTO)) {
            transAction.setActionStaffId(staffDTO.getStaffId());
        }
        stockTransActionRepo.save(actionMapper.toPersistenceBean(transAction));

        //Luu stock_trans_action: Nhap kho
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        transActionImport.setStockTransId(stockTransToSave.getStockTransId());
        transActionImport.setStatus(Const.TRANS_IMPORT); //action = nhap kho
        transActionImport.setCreateDatetime(createDate);
        if (!DataUtil.isNullObject(staffDTO)) {
            transActionImport.setActionStaffId(staffDTO.getStaffId());
        }
        stockTransActionRepo.save(actionMapper.toPersistenceBean(transActionImport));

        //Buoc 3.2: luu chi tiet gd
            for (StockTransDetailDTO stockTransDetailBean : lstStockTransDetail) {
                StockTransDetailDTO std = new StockTransDetailDTO();
                std.setStockTransId(stockTransToSave.getStockTransId());
                std.setProdOfferId(stockTransDetailBean.getProdOfferId());
                std.setStateId(Const.STATE_STATUS.NEW);
                std.setQuantity(stockTransDetailBean.getQuantity());
                std.setCreateDatetime(createDate);

                detailMapper.toDtoBean(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std)));

                //Cong so luong trong bang stockTotal

                //luu thay doi BCCS2.0
                StockTotalMessage msg = stockTotalService.changeStockTotal(
                        staffDTO.getStaffId(),
                        Const.OWNER_TYPE.STAFF_LONG,
                        stockTransDetailBean.getProdOfferId(),
                        Const.StockStateType.USED.getValue(),
                        -stockTransDetailBean.getQuantity(), //qty
                        -stockTransDetailBean.getQuantity(), //qtyIssue
                        0L, //qtyHang
                        staffDTO.getStaffId(), //userId
                        Const.REASON_EXP_SUPPLIES, //reasonId
                        stockTransToSave.getStockTransId(),
                        createDate,
                        stockTransToSave.getStockTransId().toString(),
                        "1",//giao dich xuat
                        Const.SourceType.STOCK_TRANS);
                if (!msg.isSuccess()) {
                    throw new LogicException("", msg.getKeyMsg(), msg.getParamsMsg());
                }
            }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreAPDeployment(StaffDTO staffDTO, String account, String troubleType, List<StockTransDetailDTO> lstStockTransDetail, String transId) throws LogicException, Exception {

        //hoangnt: Tim stock_trans_ext
        StockTransExtDTO stockTransExtDTO = stockTransExtRepo.getStockTransId("TROUBLE_ID", transId);
        //Buoc 1: tao gd kho
        Date createDate = getSysDate(em);

        StockTransDTO stockTrans = new StockTransDTO();
        stockTrans.setFromOwnerType(Const.OWNER_TYPE_CUST);
        stockTrans.setFromOwnerId(Const.OWNER_CUST_ID);
        stockTrans.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        stockTrans.setToOwnerId(staffDTO.getStaffId());
        stockTrans.setStockTransType(Const.IMPORT);
        stockTrans.setStatus(Const.TRANS_IMPORT);
        stockTrans.setCreateDatetime(createDate);
        stockTrans.setAccType(Const.ACC_TYPE_TROUBLE);
        stockTrans.setAccount(account);
        stockTrans.setTroubleType(troubleType);
        stockTrans.setReasonId(Const.REASON_EXP_SUPPLIES);

        if (!DataUtil.isNullObject(stockTransExtDTO)) {
            stockTrans.setFromStockTransId(stockTransExtDTO.getStockTransId());
            stockTransExtDTO.setStatus(Const.STATUS_INACTIVE);
            stockTransExtRepo.save(extMapper.toPersistenceBean(stockTransExtDTO));
        }

        StockTransDTO stockTransToSave = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTrans)));

        //Luu transId check trung lap
        StockTransExtDTO stockTransExt = new StockTransExtDTO();
        stockTransExt.setStockTransId(stockTransToSave.getStockTransId());
        stockTransExt.setExtKey("CANCEL_TROUBLE_ID");
        stockTransExt.setExtValue(transId);
        stockTransExt.setStatus(Const.STATUS_INACTIVE);

        stockTransExtRepo.save(extMapper.toPersistenceBean(stockTransExt));

        //Luu stock_trans_action
        StockTransActionDTO transAction = new StockTransActionDTO();
        transAction.setStockTransId(stockTransToSave.getStockTransId());
        transAction.setActionCode("MPN" + stockTransToSave.getStockTransId().toString()); //Ma phieu nhap
        transAction.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE); //action = lap phieu nhap
        transAction.setCreateDatetime(createDate);
        if (!DataUtil.isNullObject(staffDTO)) {
            transAction.setActionStaffId(staffDTO.getStaffId());
        }
        stockTransActionRepo.save(actionMapper.toPersistenceBean(transAction));

        //Luu stock_trans_action: Nhap kho
        StockTransActionDTO transActionImport = new StockTransActionDTO();
        transActionImport.setStockTransId(stockTransToSave.getStockTransId());
        transActionImport.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED); //action = nhap kho
        transActionImport.setCreateDatetime(createDate);
        if (!DataUtil.isNullObject(staffDTO)) {
            transActionImport.setActionStaffId(staffDTO.getStaffId());
        }
        stockTransActionRepo.save(actionMapper.toPersistenceBean(transActionImport));

        //Buoc 3.2: luu chi tiet gd
        for (StockTransDetailDTO stockTransDetailBean : lstStockTransDetail) {
            StockTransDetailDTO std = new StockTransDetailDTO();
            std.setStockTransId(stockTransToSave.getStockTransId());
            std.setProdOfferId(stockTransDetailBean.getProdOfferId());
            std.setStateId(Const.STATE_STATUS.NEW);
            std.setQuantity(stockTransDetailBean.getQuantity());
            std.setCreateDatetime(createDate);

            detailMapper.toDtoBean(stockTransDetailRepo.save(detailMapper.toPersistenceBean(std)));

            //Cong so luong trong bang stockTotal
            //luu thay doi BCCS2.0
            StockTotalMessage msg = stockTotalService.changeStockTotal(
                    staffDTO.getStaffId(),
                    Const.OWNER_TYPE.STAFF_LONG,
                    stockTransDetailBean.getProdOfferId(),
                    Const.StockStateType.USED.getValue(),
                    stockTransDetailBean.getQuantity(), //qty
                    stockTransDetailBean.getQuantity(), //qtyIssue
                    0L, //qtyHang
                    staffDTO.getStaffId(), //userId
                    Const.REASON_EXP_SUPPLIES, //reasonId
                    stockTransToSave.getStockTransId(),
                    createDate,
                    stockTransToSave.getStockTransId().toString(),
                    "1",//giao dich xuat
                    Const.SourceType.STOCK_TRANS);
            if (!msg.isSuccess()) {
                throw new LogicException("", msg.getKeyMsg(), msg.getParamsMsg());
            }
        }
    }

    public List<StockTotalDTO> checkHaveProductOffering(Long ownerId, Long ownerType, boolean checkCollaborator) throws Exception {
        return repository.checkHaveProductOffering(ownerId, ownerType, checkCollaborator);
    }

    public List<String> checkStockTransSuppend(Long ownerId, Long ownerType) throws Exception {
        return repository.checkStockTransSuppend(ownerId, ownerType);
    }

    @Override
    public Long getTransAmount(Long transID) throws Exception {
        return repository.getTransAmount(transID);
    }


    public StockTransDTO getStockTransForLogistics(Long stockTransId) throws Exception {
        return repository.getStockTransForLogistics(stockTransId);
    }

    public boolean checkSaleTrans(Long stockTransId, Date saleTransDate) throws Exception {
        return repository.checkSaleTrans(stockTransId, saleTransDate);
    }

    public List<DOATransferDTO> getContentFileDOA(Long stockTransId) throws Exception {
        return repository.getContentFileDOA(stockTransId);
    }

    @Override
    public VStockTransDTO findVStockTransDTO(Long stockTransID, Long actionID) throws LogicException, Exception {
        return repository.findVStockTransDTO(stockTransID, actionID);
    }

    public List<StockTransDTO> getLstRequestOrderExported() throws Exception {
        return repository.getLstRequestOrderExported();
    }
}

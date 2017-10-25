package com.viettel.bccs.inventory.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.model.StockTransRescue;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.repo.StockTransRescueRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class StockTransRescueServiceImpl extends BaseServiceImpl implements StockTransRescueService {

    private final BaseMapper<StockTransRescue, StockTransRescueDTO> mapper = new BaseMapper<>(StockTransRescue.class, StockTransRescueDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockHandsetRescueService stockHandsetRescueService;
    @Autowired
    private StockTransDetailRescueService stockTransDetailRescueService;
    @Autowired
    private StockTransSerialRescueService stockTransSerialRescueService;
    @Autowired
    private StockTransActionRescueService stockTransActionRescueService;
    @Autowired
    private StockHandsetService stockHandsetService;
    @Autowired
    private StockHandsetRepo stockHandsetRepo;
    @Autowired
    private StockTransRescueRepo repository;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    public static final Logger logger = Logger.getLogger(StockTransRescueService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransRescueDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransRescueDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public List<StockTransRescueDTO> searchStockRescue(StockTransRescueDTO stockTransRescueDTO) throws LogicException, Exception {

        if (DataUtil.isNullObject(stockTransRescueDTO.getFromDate()) || DataUtil.isNullObject(stockTransRescueDTO.getToDate())) {
            throw new LogicException("", "stock.rescue.warranty.date.require");
        }
        if (DateUtil.compareDateToDate(stockTransRescueDTO.getFromDate(), stockTransRescueDTO.getToDate()) > 0) {
            throw new LogicException("", "stock.trans.from.than.to");
        }
        return repository.searchStockRescue(stockTransRescueDTO);
    }

    @WebMethod
    public Long getMaxId() throws LogicException, Exception {
        return repository.getMaxId();
    }

    @WebMethod
    public Long getReasonId() throws LogicException, Exception {
        return repository.getReasonId();
    }


    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage createRequest(StockTransRescueDTO stockTransRescueAdd, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        Date currentDate = getSysDate(em);
        //validate
        if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY)) {
            throw new LogicException("", "stock.rescue.warranty.not.permission");
        }
        if (DataUtil.isNullOrEmpty(stockTransRescueAdd.getRequestCode())) {
            throw new LogicException("", "stock.rescue.warranty.vaildate.request.code");
        }
        if (DataUtil.isNullOrEmpty(stockTransRescueAdd.getStaffRequest())) {
            throw new LogicException("", "stock.rescue.warranty.vaildate.staff");
        }
        if (DataUtil.isNullOrZero(stockTransRescueAdd.getToOwnerId())) {
            throw new LogicException("", "stock.rescue.warranty.vaildate.shop");
        }
        ShopDTO shopDTO = shopService.findOne(stockTransRescueAdd.getToOwnerId());
        if (DataUtil.isNullObject(shopDTO)) {
            throw new LogicException("", "stock.rescue.warranty.vaildate.shop.not.exsit");
        }
        // save stock_Trans_Rescue
        if (!repository.isDulicateRequestCode(stockTransRescueAdd.getRequestCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.balance.stock.request.validate.requestCode");
        }
        stockTransRescueAdd.setCreateDate(currentDate);
        StockTransRescue stockTransRescue = repository.save(mapper.toPersistenceBean(stockTransRescueAdd));
        // Lay danh sach mat hang, so luong can chuyen bao hanh
        List<String> lstSerial = Lists.newArrayList();
        for (StockHandsetRescueDTO tmp : stockTransRescueAdd.getLstSelection()) {
            lstSerial.add(tmp.getSerial());
        }
        List<StockHandsetRescueDTO> lstStockHandset = stockHandsetRescueService.getListProductForRescue(stockTransRescueAdd.getFromOwnerId(), lstSerial);
        if (DataUtil.isNullOrEmpty(lstStockHandset)) {
            throw new LogicException("", "stock.rescue.warranty.vaildate.lst.product");
        }
        //save stock_Trans_DetaiL_Rescue
        for (StockHandsetRescueDTO stockHandsetRescueDTO : lstStockHandset) {
            StockTransDetailRescueDTO stockTransDetailRescueDTO = new StockTransDetailRescueDTO();
            stockTransDetailRescueDTO.setStockTransId(stockTransRescue.getStockTransId());
            stockTransDetailRescueDTO.setStateId(Const.GOODS_STATE.BROKEN);
            stockTransDetailRescueDTO.setCreateDate(currentDate);
            stockTransDetailRescueDTO.setProdOfferId(stockHandsetRescueDTO.getNewProdOfferlId());
            stockTransDetailRescueDTO.setQuantity(stockHandsetRescueDTO.getQuantity());
            stockTransDetailRescueService.save(stockTransDetailRescueDTO);
        }
        //Lay danh sach serial ung cuu thong tin
        List<StockHandsetRescueDTO> lstStockSerial = stockHandsetRescueService.getListSerialForRescue(stockTransRescueAdd.getFromOwnerId(), lstSerial);
        if (DataUtil.isNullOrEmpty(lstStockSerial)) {
            throw new LogicException("", "stock.rescue.warranty.vaildate.lst.product");
        }
        //save stock_trans_serial_rescue
        for (StockHandsetRescueDTO stockHandsetRescueDTO : lstStockSerial) {
            StockTransSerialRescueDTO stockTransSerialRescueDTO = new StockTransSerialRescueDTO();
            stockTransSerialRescueDTO.setStockTransId(stockTransRescue.getStockTransId());
            stockTransSerialRescueDTO.setStateId(Const.GOODS_STATE.BROKEN);
            stockTransSerialRescueDTO.setQuantity(1L);
            stockTransSerialRescueDTO.setCreateDate(currentDate);
            stockTransSerialRescueDTO.setFromSerial(stockHandsetRescueDTO.getFromSerial());
            stockTransSerialRescueDTO.setToSerial(stockHandsetRescueDTO.getToSerial());
            stockTransSerialRescueDTO.setProdOfferId(stockHandsetRescueDTO.getNewProdOfferlId());
            stockTransSerialRescueService.save(stockTransSerialRescueDTO);
            //cap nhat trang thai da chuyen bao hanh
            stockHandsetRescueService.updateStatusSerialForRescue(Const.STOCK_TRANS_RESCUE.STATUS_HAVE_RESCUE, Const.STOCK_TRANS_RESCUE.STATUS_NOT_RESCUE, stockTransRescueAdd.getFromOwnerId(), stockHandsetRescueDTO.getFromSerial(), stockHandsetRescueDTO.getNewProdOfferlId());
        }
        //save stock_trans_Action_rescue
        StockTransActionRescueDTO stockTransActionRescueDTO = new StockTransActionRescueDTO();
        stockTransActionRescueDTO.setStockTransId(stockTransRescue.getStockTransId());
        stockTransActionRescueDTO.setActionType(1L);
        stockTransActionRescueDTO.setCreateDate(currentDate);
        stockTransActionRescueDTO.setActionStaffId(stockTransRescueAdd.getFromOwnerId());
        stockTransActionRescueDTO.setDescription(getText("stock.rescue.warranty.description.create"));
        stockTransActionRescueService.save(stockTransActionRescueDTO);
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    private List<StockTransSerialRescueDTO> actionStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap, Long statusBefor, Long statusAffer) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        //validate
        if (!requiredRoleMap.hasRole(stockTransRescueAction.getRole())) {
            throw new LogicException("", "stock.rescue.warranty.not.permission");
        }
        StockTransRescueDTO stockTransRescueDTO = findOne(stockTransRescueAction.getStockTransId());
        if (DataUtil.isNullObject(stockTransRescueDTO)) {
            throw new LogicException("", "stock.rescue.warranty.view.detail.not.esxit");
        }
        if (!DataUtil.safeEqual(stockTransRescueDTO.getStatus(), statusBefor)) {
            throw new LogicException("", stockTransRescueAction.getErrorStatusMgs());
        }
        //cap nhat status huy bao hanh
        stockTransRescueDTO.setStatus(statusAffer);
        repository.save(mapper.toPersistenceBean(stockTransRescueDTO));
        //luu stock_trans_action_rescue
        StockTransActionRescueDTO stockTransActionRescueDTO = new StockTransActionRescueDTO();
        stockTransActionRescueDTO.setStockTransId(stockTransRescueAction.getStockTransId());
        stockTransActionRescueDTO.setActionType(2L);
        stockTransActionRescueDTO.setCreateDate(currentDate);
        stockTransActionRescueDTO.setActionStaffId(stockTransRescueAction.getFromOwnerId());
        stockTransActionRescueDTO.setDescription(getText(stockTransRescueAction.getDescription()));
        stockTransActionRescueService.save(stockTransActionRescueDTO);
        //lay danh sach serial
        List<StockTransSerialRescueDTO> lstSerial = stockTransSerialRescueService.viewDetailSerailByStockTransId(stockTransRescueAction.getStockTransId());
        if (DataUtil.isNullOrEmpty(lstSerial)) {
            throw new LogicException("", "stock.rescue.warranty.validate.delete.no.serial");
        }
        return lstSerial;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        stockTransRescueAction.setRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAction.setErrorStatusMgs("stock.rescue.warranty.validate.delete");
        stockTransRescueAction.setDescription("stock.rescue.warranty.description.delete");
        //lay danh sach serial
        List<StockTransSerialRescueDTO> lstSerial = actionStockRescue(stockTransRescueAction, requiredRoleMap, Const.STOCK_TRANS_RESCUE.STATUS_HAVE_RESCUE, Const.STOCK_TRANS_RESCUE.STATUS_DELETE);
        if (DataUtil.isNullOrEmpty(lstSerial)) {
            throw new LogicException("", "stock.rescue.warranty.validate.delete.no.serial");
        }
        // cap nhat status = 0
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerial) {
            //cap nhat trang thai da chuyen bao hanh
            stockHandsetRescueService.updateStatusSerialForRescue(Const.STOCK_TRANS_RESCUE.STATUS_NOT_RESCUE, null, stockTransRescueAction.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
        }
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage receiveStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        stockTransRescueAction.setRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY);
        stockTransRescueAction.setErrorStatusMgs("stock.rescue.warranty.validate.receive");
        stockTransRescueAction.setDescription("stock.rescue.warranty.description.receive");
        List<StockTransSerialRescueDTO> lstSerial = actionStockRescue(stockTransRescueAction, requiredRoleMap, Const.STOCK_TRANS_RESCUE.STATUS_RETURN, Const.STOCK_TRANS_RESCUE.STATUS_RECEIVE);
        // cap nhat status = 6
        Date currentDate = getSysDate(em);
        StaffDTO staffDTO = staffService.findOne(stockTransRescueAction.getFromOwnerId());
        StockTransRescueDTO stockTransRescueDTO = findOne(stockTransRescueAction.getStockTransId());
        String[] params = new String[4];
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "staff.code.invalid");
        }
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerial) {
            if (DataUtil.isNullOrZero(stockTransSerialRescueDTO.getProdOfferIdReturn()) || DataUtil.isNullOrEmpty(stockTransSerialRescueDTO.getSerialReturn())) {
                throw new LogicException("", "stock.rescue.warranty.stock.handset.serial.null");
            }
            Long prodOfferId = stockTransSerialRescueDTO.getProdOfferId();
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
//            ProductOfferingDTO productOfferingReturnDTO = productOfferingService.findOne(stockTransSerialRescueDTO.getProdOfferIdReturn());
//            if (!DataUtil.safeEqual(productOfferingDTO.getAccountingModelCode(), productOfferingReturnDTO.getAccountingModelCode())) {
//                throw new LogicException("", "create.note.change.product.not.match.accounting.model.code", productOfferingDTO.getCode(), productOfferingReturnDTO.getCode());
//            }
            //cap nhat trang thai da chuyen bao hanh
            stockHandsetRescueService.updateStatusSerialForRescue(Const.STOCK_TRANS_RESCUE.STATUS_RECEIVE, null, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
            //Neu mat hang trung nhau va serial trung nhau
            if (DataUtil.safeEqual(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getProdOfferIdReturn())
                    && DataUtil.safeEqual(stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getSerialReturn())) {
                //cap nhat stock_handset
                int result = stockHandsetService.updateForStockRescue(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
                if (DataUtil.safeEqual(result, 0)) {
                    params[0] = productOfferingDTO.getName();
                    params[1] = "";
                    params[2] = stockTransSerialRescueDTO.getFromSerial();
                    params[3] = stockTransSerialRescueDTO.getFromSerial();
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial"), params);
                }
                if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                        && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    result = stockHandsetService.updateForStockRescueIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
                    if (DataUtil.safeEqual(result, 0)) {
                        params[0] = productOfferingDTO.getName();
                        params[1] = "";
                        params[2] = stockTransSerialRescueDTO.getFromSerial();
                        params[3] = stockTransSerialRescueDTO.getFromSerial();
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.bccs1"), params);
                    }
                }

            } else {
                //cap nhat status = 0 voi serial cu
                StockHandsetDTO stockHandsetDTO = stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(),
                        Const.OWNER_TYPE.STAFF_LONG, stockTransRescueDTO.getFromOwnerId(), null);
                if (DataUtil.isNullObject(stockHandsetDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescue.warranty.stock.handset", productOfferingDTO.getCode(), stockTransSerialRescueDTO.getFromSerial());
                }
                stockHandsetDTO.setStatus(DataUtil.safeToLong(Const.STATUS_INACTIVE));
                stockHandsetDTO.setUpdateDatetime(currentDate);
                stockHandsetService.create(stockHandsetDTO);
                if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                        && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                    int result = stockHandsetRepo.updateStockHandsetIM1(null, null, null, DataUtil.safeToLong(Const.STATUS_INACTIVE), null, stockTransRescueDTO.getFromOwnerId()
                            , Const.OWNER_TYPE.STAFF_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
                    if (DataUtil.safeEqual(result, 0)) {
                        params[0] = productOfferingDTO.getName();
                        params[1] = "";
                        params[2] = stockTransSerialRescueDTO.getFromSerial();
                        params[3] = stockTransSerialRescueDTO.getFromSerial();
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.bccs1"), params);
                    }
                }

                StockHandsetDTO stockHandsetOld ;
                //Neu mat hang cu va mat hang moi khac nhau
                if (!DataUtil.safeEqual(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getProdOfferIdReturn())) {
                    stockHandsetOld = stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferIdReturn(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null);
                    prodOfferId = stockTransSerialRescueDTO.getProdOfferIdReturn();
                    productOfferingDTO = productOfferingService.findOne(prodOfferId);
                } else {
                    //Neu serial cu va serial moi khac nhau
                    stockHandsetOld = stockHandsetRepo.getStockHandset(stockTransSerialRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), null, null, null);
                }
                // Serial moi tuong ung mat hang chua co tren he thong --> Them moi ban ghi
                if (DataUtil.isNullObject(stockHandsetOld)) {
                    StockHandsetDTO stockHandsetNew = new StockHandsetDTO();
                    stockHandsetNew.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
                    stockHandsetNew.setStateId(Const.STATE_STATUS.RETRIEVE);
                    stockHandsetNew.setOwnerId(stockTransRescueDTO.getFromOwnerId());
                    stockHandsetNew.setSerial(stockTransSerialRescueDTO.getSerialReturn());
                    stockHandsetNew.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
                    stockHandsetNew.setCreateDate(currentDate);
                    stockHandsetNew.setUpdateDatetime(currentDate);
                    stockHandsetNew.setCreateUser(staffDTO.getStaffCode());
                    stockHandsetNew.setProdOfferId(prodOfferId);
                    StockHandsetDTO stockHandsetSave = stockHandsetService.create(stockHandsetNew);
                    //IM1
                    if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                            && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                            && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                        try {
                            stockHandsetRepo.insertStockHandset(stockHandsetSave);
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                            params[0] = productOfferingDTO.getName();
                            params[1] = productOfferingService.getStockStateName(stockHandsetSave.getStateId());
                            params[2] = stockHandsetSave.getSerial();
                            params[3] = stockHandsetSave.getSerial();
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.bccs1"), params);
                        }
                    }
                } else {
                    //Neu serial moi da co tren he thong, neu khong thuoc kho TTBT --> Bao loi
                    stockHandsetOld = stockHandsetRepo.getStockHandset(prodOfferId, stockTransSerialRescueDTO.getSerialReturn(),
                            Const.OWNER_TYPE.SHOP_LONG, stockTransRescueDTO.getToOwnerId(), Const.STATE_STATUS.NEW);
                    if (DataUtil.isNullObject(stockHandsetOld)) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescue.warranty.stock.handset.serial.return.not.found", productOfferingDTO.getCode(), stockTransSerialRescueDTO.getSerialReturn());
                    }
                    stockHandsetOld.setStateId(Const.STATE_STATUS.RETRIEVE);
                    stockHandsetOld.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
                    stockHandsetOld.setOwnerId(stockTransRescueDTO.getFromOwnerId());
                    stockHandsetOld.setUpdateDatetime(currentDate);
                    stockHandsetService.create(stockHandsetOld);
                    //IM1
                    if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                            && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                        int result = stockHandsetRepo.updateStockHandsetIM1(Const.STATE_STATUS.RETRIEVE, stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, null, Const.STATE_STATUS.NEW,
                                stockTransRescueDTO.getToOwnerId()
                                , Const.OWNER_TYPE.SHOP_LONG, DataUtil.safeToLong(Const.STATUS_ACTIVE), stockTransSerialRescueDTO.getSerialReturn(), prodOfferId);
                        if (DataUtil.safeEqual(result, 0)) {
                            params[0] = productOfferingDTO.getName();
                            params[1] = productOfferingService.getStockStateName(Const.STATE_STATUS.NEW);
                            params[2] = stockTransSerialRescueDTO.getSerialReturn();
                            params[3] = stockTransSerialRescueDTO.getSerialReturn();
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getText("stockTrans.validate.quantity.serial.bccs1"), params);
                        }
                    }
                }
            }
            //Cong stock_toal voi state_id = 4
            StockTotalMessage resultStockTotal = stockTotalService.changeStockTotal(stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, prodOfferId,
                    Const.STATE_STATUS.RETRIEVE, 1L, 1L, 0L, staffDTO.getStaffId(), stockTransRescueDTO.getReasonId(), stockTransRescueDTO.getStockTransId(), currentDate, "", "1", Const.SourceType.STOCK_TRANS);
            if (!resultStockTotal.isSuccess()) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, resultStockTotal.getKeyMsg(), resultStockTotal.getParamsMsg());
            }
            //Tru stock_total voi state_id = 3
            resultStockTotal = stockTotalService.changeStockTotal(stockTransRescueDTO.getFromOwnerId(), Const.OWNER_TYPE.STAFF_LONG, stockTransSerialRescueDTO.getProdOfferId(),
                    Const.STATE_STATUS.DAMAGE, -1L, -1L, 0L, staffDTO.getStaffId(), stockTransRescueDTO.getReasonId(), stockTransRescueDTO.getStockTransId(), currentDate, "", "1", Const.SourceType.STOCK_TRANS);
            if (!resultStockTotal.isSuccess()) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, resultStockTotal.getKeyMsg(), resultStockTotal.getParamsMsg());
            }
        }

        //Thuc hien giao dich xuat nhap tu nhan vien cho khach hang
        String strReasonCodeExp = getText("REASON_EXP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeExp))

        {
            strReasonCodeExp = "XKH";
        }

        List<ReasonDTO> lstReasonExp = reasonService.getLsReasonByCode(strReasonCodeExp);
        if (DataUtil.isNullOrEmpty(lstReasonExp) || DataUtil.isNullObject(lstReasonExp.get(0)))

        {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.reason.export.not.found");
        }

        ReasonDTO reasonExp = lstReasonExp.get(0);
        String strReasonCodeImp = getText("REASON_IMP_CHANGE_DEMAGED_GOODS");
        if (DataUtil.isNullOrEmpty(strReasonCodeImp))

        {
            strReasonCodeImp = "NKH";
        }

        List<ReasonDTO> lstReasonImp = reasonService.getLsReasonByCode(strReasonCodeImp);
        if (DataUtil.isNullOrEmpty(lstReasonImp) || DataUtil.isNullObject(lstReasonImp.get(0)))

        {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.validate.stock.reason.import.not.found");
        }

        ReasonDTO reasonImp = lstReasonImp.get(0);

        //stock_trans_exp
        StockTransDTO stockTransExport = getStockTransExport(staffDTO.getStaffId(), reasonExp.getReasonId(), currentDate);
        StockTransDTO stockTransExpSave = stockTransService.save(stockTransExport);
        //stock_trans_action 2,3,6
        StockTransActionDTO stockTransActionExport = getStockTransActionExp(staffDTO, currentDate, stockTransExpSave.getStockTransId());
        StockTransActionDTO stockTransActionExportSave = stockTransActionService.save(stockTransActionExport);
        stockTransActionExportSave.setActionCode("XDHH00" + stockTransActionExportSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionExportSave);
        stockTransActionExportSave.setStockTransActionId(null);
        stockTransActionExportSave.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionService.save(stockTransActionExportSave);
        stockTransActionExportSave.setStockTransActionId(null);
        stockTransActionExportSave.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionExportSave);
        //stock_Trans_detail
        List<StockTransDetailRescueDTO> lstDetailRescue = stockTransDetailRescueService.viewDetail(stockTransRescueDTO.getStockTransId());
        for (StockTransDetailRescueDTO stockTransDetailRescueDTO : lstDetailRescue) {
            StockTransDetailDTO stockTransDetailExp = getStockTransDetailExp(stockTransExpSave.getStockTransId(), stockTransDetailRescueDTO.getProdOfferId(), currentDate, stockTransDetailRescueDTO.getQuantity());
            StockTransDetailDTO stockTransDetailSave = stockTransDetailService.save(stockTransDetailExp);
            List<StockTransSerialRescueDTO> lstSerialRescue = stockTransSerialRescueService.viewDetailSerail(stockTransRescueDTO.getStockTransId(), stockTransDetailRescueDTO.getProdOfferId(), null);
            for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerialRescue) {
                StockTransSerialDTO stockTransSerialDTO = getStockTransSerialExp(stockTransExpSave.getStockTransId(), stockTransDetailSave.getStockTransDetailId(),
                        stockTransDetailRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getFromSerial(), currentDate);
                stockTransSerialService.save(stockTransSerialDTO);
            }
        }

        //stock_Trans_imp
        StockTransDTO stockTransImp = getStockTransImport(staffDTO.getStaffId(), reasonImp.getReasonId(), currentDate);
        StockTransDTO stockTransImpSave = stockTransService.save(stockTransImp);
        //stock_trans_action 5,6
        StockTransActionDTO stockTransActionImp = getStockTransActionImp(staffDTO, currentDate, stockTransImpSave.getStockTransId());
        StockTransActionDTO stockTransActionImpSave = stockTransActionService.save(stockTransActionImp);
        stockTransActionImpSave.setActionCode("NDHH00" + stockTransActionImpSave.getStockTransActionId());
        stockTransActionService.save(stockTransActionImpSave);
        stockTransActionImpSave.setStockTransActionId(null);
        stockTransActionImpSave.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        stockTransActionService.save(stockTransActionImpSave);
        //stock_trans_detail
        List<StockTransDetailRescueDTO> lstReturnSerial = stockTransDetailRescueService.getCountLstDetail(stockTransRescueDTO.getStockTransId());
        for (StockTransDetailRescueDTO stockTransDetailRescueDTO : lstReturnSerial) {
            StockTransDetailDTO stockTransDetailImp = getStockTransDetailImp(stockTransImpSave.getStockTransId(), stockTransDetailRescueDTO.getProdOfferId(), currentDate, stockTransDetailRescueDTO.getQuantity());
            StockTransDetailDTO stockTransDetailSave = stockTransDetailService.save(stockTransDetailImp);
            List<StockTransSerialRescueDTO> lstSerialRescue = stockTransSerialRescueService.viewDetailSerail(stockTransRescueDTO.getStockTransId(), null, stockTransDetailRescueDTO.getProdOfferId());
            for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerialRescue) {
                StockTransSerialDTO stockTransSerialDTO = getStockTransSerialImp(stockTransImpSave.getStockTransId(), stockTransDetailSave.getStockTransDetailId(),
                        stockTransDetailRescueDTO.getProdOfferId(), stockTransSerialRescueDTO.getSerialReturn(), currentDate);
                stockTransSerialService.save(stockTransSerialDTO);
            }
        }

        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage acceptStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap
            requiredRoleMap) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        stockTransRescueAction.setRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY);
        stockTransRescueAction.setErrorStatusMgs("stock.rescue.warranty.validate.accept");
        stockTransRescueAction.setDescription("stock.rescue.warranty.description.accept");
        List<StockTransSerialRescueDTO> lstSerial = actionStockRescue(stockTransRescueAction, requiredRoleMap, Const.STOCK_TRANS_RESCUE.STATUS_HAVE_RESCUE, Const.STOCK_TRANS_RESCUE.STATUS_ACCEPT);
        // cap nhat status = 3
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerial) {
            //cap nhat trang thai da chuyen bao hanh
            stockHandsetRescueService.updateStatusSerialForRescue(Const.STOCK_TRANS_RESCUE.STATUS_ACCEPT, null, stockTransRescueAction.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
        }
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage cancelStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap
            requiredRoleMap) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        stockTransRescueAction.setRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY);
        stockTransRescueAction.setErrorStatusMgs("stock.rescue.warranty.validate.cancel");
        stockTransRescueAction.setDescription("stock.rescue.warranty.description.cancel");
        List<StockTransSerialRescueDTO> lstSerial = actionStockRescue(stockTransRescueAction, requiredRoleMap, Const.STOCK_TRANS_RESCUE.STATUS_HAVE_RESCUE, Const.STOCK_TRANS_RESCUE.STATUS_CANCEL);
        // cap nhat status = 3
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerial) {
            //cap nhat trang thai da chuyen bao hanh
            stockHandsetRescueService.updateStatusSerialForRescue(Const.STOCK_TRANS_RESCUE.STATUS_NOT_RESCUE, null, stockTransRescueAction.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
        }
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage returnStockRescue(StockTransRescueDTO stockTransRescueAction, RequiredRoleMap
            requiredRoleMap, List<StockTransSerialRescueDTO> lstSerial) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        stockTransRescueAction.setRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY);
        stockTransRescueAction.setErrorStatusMgs("stock.rescue.warranty.validate.give");
        stockTransRescueAction.setDescription("stock.rescue.warranty.description.give");
        List<StockTransSerialRescueDTO> lstSerialRescue = actionStockRescue(stockTransRescueAction, requiredRoleMap, Const.STOCK_TRANS_RESCUE.STATUS_ACCEPT, Const.STOCK_TRANS_RESCUE.STATUS_RETURN);
        // cap nhat status = 3
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerialRescue) {
            //cap nhat trang thai da chuyen bao hanh
            stockHandsetRescueService.updateStatusSerialForRescue(Const.STOCK_TRANS_RESCUE.STATUS_RETURN, null, stockTransRescueAction.getFromOwnerId(), stockTransSerialRescueDTO.getFromSerial(), stockTransSerialRescueDTO.getProdOfferId());
        }
        //check trung
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerial) {
            if (!DataUtil.validateStringByPattern(stockTransSerialRescueDTO.getSerialReturn(), Const.REGEX.SERIAL_REGEX) || stockTransSerialRescueDTO.getSerialReturn().getBytes("UTF-8").length > 20) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescue.warranty.serial.return.serial.not.match", stockTransSerialRescueDTO.getSerialReturn());
            }
            Long prodOfferId = stockTransSerialRescueDTO.getProductOfferingTotalDTO().getProductOfferingId();
            if (!DataUtil.isNullOrZero(prodOfferId)) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
                if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescue.warranty.serial.return.product.not.esxit", stockTransSerialRescueDTO.getProductOfferingTotalDTO().getCode());
                }
            }
            for (StockTransSerialRescueDTO stockTransSerialRescueDTO1 : lstSerial) {
                if (!DataUtil.safeEqual(stockTransSerialRescueDTO.getStockTransSerialId(), stockTransSerialRescueDTO1.getStockTransSerialId())
                        && DataUtil.safeEqual(stockTransSerialRescueDTO.getSerialReturn(), stockTransSerialRescueDTO1.getSerialReturn())
                        && DataUtil.safeEqual(stockTransSerialRescueDTO.getProductOfferingTotalDTO().getProductOfferingId(), stockTransSerialRescueDTO1.getProductOfferingTotalDTO().getProductOfferingId())) {
                    throw new LogicException("", "stock.rescue.warranty.serial.return.duplicate", stockTransSerialRescueDTO.getProductOfferingTotalDTO().getCode(), stockTransSerialRescueDTO.getSerialReturn());
                }
            }
        }
        for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstSerial) {
            StockTransSerialRescueDTO stockTransSerialRescue = stockTransSerialRescueService.findOne(stockTransSerialRescueDTO.getStockTransSerialId());
            stockTransSerialRescue.setSerialReturn(stockTransSerialRescueDTO.getSerialReturn());
            stockTransSerialRescue.setProdOfferIdReturn(stockTransSerialRescueDTO.getProductOfferingTotalDTO().getProductOfferingId());
            stockTransSerialRescueService.save(stockTransSerialRescue);
        }
        baseMessage.setSuccess(true);
        return baseMessage;
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


    private StockTransActionDTO getStockTransActionExp(StaffDTO staffDTO, Date currentDate, Long stockTransId) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        stockTransActionExp.setCreateDatetime(currentDate);
        stockTransActionExp.setStockTransId(stockTransId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        stockTransActionExp.setNote(getText("stock.change.damaged.stock.trans.note.export"));
        return stockTransActionExp;
    }

    private StockTransDetailDTO getStockTransDetailExp(Long stockTransId, Long prodOfferId, Date currentDate, Long quantity) {
        StockTransDetailDTO expTransDetailDTO = new StockTransDetailDTO();
        expTransDetailDTO.setStockTransId(stockTransId);
        expTransDetailDTO.setProdOfferId(prodOfferId);
        expTransDetailDTO.setStateId(Const.STATE_STATUS.DAMAGE);
        expTransDetailDTO.setQuantity(quantity);
        expTransDetailDTO.setCreateDatetime(currentDate);
        return expTransDetailDTO;
    }

    private StockTransSerialDTO getStockTransSerialExp(Long stockTransId, Long stockTransDetailId, Long prodOfferId, String fromSerial, Date currentDate) {
        StockTransSerialDTO expStockSerialDTO = new StockTransSerialDTO();
        expStockSerialDTO.setProdOfferId(prodOfferId);
        expStockSerialDTO.setStateId(Const.STATE_STATUS.DAMAGE);
        expStockSerialDTO.setStockTransId(stockTransId);
        expStockSerialDTO.setStockTransDetailId(stockTransDetailId);
        expStockSerialDTO.setFromSerial(fromSerial);
        expStockSerialDTO.setToSerial(fromSerial);
        expStockSerialDTO.setQuantity(1L);
        expStockSerialDTO.setCreateDatetime(currentDate);
        return expStockSerialDTO;
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

    private StockTransActionDTO getStockTransActionImp(StaffDTO staffDTO, Date currentDate, Long stockTransId) throws Exception {
        StockTransActionDTO stockTransActionExp = new StockTransActionDTO();
        stockTransActionExp.setCreateDatetime(currentDate);
        stockTransActionExp.setStockTransId(stockTransId);
        stockTransActionExp.setActionStaffId(staffDTO.getStaffId());
        stockTransActionExp.setCreateUser(staffDTO.getStaffCode());
        stockTransActionExp.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransActionExp.setNote(getText("stock.change.damaged.stock.trans.note.import"));
        return stockTransActionExp;
    }


    private StockTransDetailDTO getStockTransDetailImp(Long stockTransId, Long prodOfferId, Date currentDate, Long quantity) {
        StockTransDetailDTO expTransDetailDTO = new StockTransDetailDTO();
        expTransDetailDTO.setStockTransId(stockTransId);
        expTransDetailDTO.setProdOfferId(prodOfferId);
        expTransDetailDTO.setStateId(Const.STATE_STATUS.RETRIEVE);
        expTransDetailDTO.setQuantity(quantity);
        expTransDetailDTO.setCreateDatetime(currentDate);
        return expTransDetailDTO;
    }

    private StockTransSerialDTO getStockTransSerialImp(Long stockTransId, Long stockTransDetailId, Long prodOfferId, String serialReturn, Date currentDate) {
        StockTransSerialDTO expStockSerialDTO = new StockTransSerialDTO();
        expStockSerialDTO.setProdOfferId(prodOfferId);
        expStockSerialDTO.setStateId(Const.STATE_STATUS.RETRIEVE);
        expStockSerialDTO.setStockTransId(stockTransId);
        expStockSerialDTO.setStockTransDetailId(stockTransDetailId);
        expStockSerialDTO.setFromSerial(serialReturn);
        expStockSerialDTO.setToSerial(serialReturn);
        expStockSerialDTO.setQuantity(1L);
        expStockSerialDTO.setCreateDatetime(currentDate);
        return expStockSerialDTO;
    }
}

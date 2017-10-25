package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.ChangeModelTransDetail;
import com.viettel.bccs.inventory.repo.ChangeModelTransDetailRepo;
import com.viettel.bccs.inventory.repo.ChangeModelTransRepo;
import com.viettel.bccs.inventory.repo.ChangeModelTransSerialRepo;
import com.viettel.bccs.inventory.repo.ProductExchangeRepo;
import com.viettel.bccs.inventory.model.ProductExchange;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.BaseValidateService;
import com.viettel.bccs.inventory.service.ProductExchangeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Sort;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

@Service
public class ProductExchangeServiceImpl extends BaseStockService implements ProductExchangeService {

    private final BaseMapper<ProductExchange, ProductExchangeDTO> mapper = new BaseMapper<>(ProductExchange.class, ProductExchangeDTO.class);
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductExchangeRepo repository;
    @Autowired
    private StaffService staffService;
    @Autowired
    ExecuteStockTransService executeStockTransService;
    @Autowired
    StockTransActionService stockTransActionService;
    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    ChangeModelTransService changeModelTransService;
    @Autowired
    ChangeModelTransDetailService changeModelTransDetailService;
    @Autowired
    ChangeModelTransSerialService changeModelTransSerialService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ChangeModelTransRepo repoChangeModelTrans;
    @Autowired
    private ChangeModelTransDetailRepo repoChangeModelTransDetail;
    @Autowired
    private ChangeModelTransSerialRepo repoChangeModelTransSerial;
    @Autowired
    private StockSimService stockSimService;
    @Autowired
    private SaleWs saleWs;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;


    public static final Logger logger = Logger.getLogger(ProductExchangeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ProductExchangeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ProductExchangeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ProductExchangeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ProductExchangeDTO create(ProductExchangeDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ProductExchangeDTO update(ProductExchangeDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductExchange(ProductExchangeDTO productExchangeDTO, Long staffId) throws Exception, LogicException {
        validateProductExchange(productExchangeDTO, staffId);
        productExchangeDTO.setStatus(Const.STATUS.ACTIVE);
        Date sysdate = getSysDate(em);
        StaffDTO staffDTO = staffService.findOne(staffId);
        if (DataUtil.isNullObject(staffDTO) || DataUtil.isNullOrZero(staffDTO.getStaffId())
                || (!DataUtil.isNullObject(staffDTO) && !DataUtil.isNullOrZero(staffDTO.getStaffId()) && Const.STATUS.NO_ACTIVE.equals(staffDTO.getStatus()))) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.staff"));
        }
        productExchangeDTO.setCreateDatetime(sysdate);
        productExchangeDTO.setCreateUser(staffDTO.getStaffCode());
        productExchangeDTO.setUpdateDatetime(sysdate);
        productExchangeDTO.setUpdateUser(staffDTO.getStaffCode());
        productExchangeDTO.setProdOfferTypeId(Const.PRODUCT_OFFER_TYPE.KIT);
        create(productExchangeDTO);
    }

    public void validateProductExchange(ProductExchangeDTO productExchangeDTO, Long staffId) throws Exception, LogicException {
        if (DataUtil.isNullOrZero(staffId)) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.staff"));
        }
        if (DataUtil.isNullOrZero(productExchangeDTO.getProdOfferId())) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.prodOfferId"));
        }
        if (DataUtil.isNullOrZero(productExchangeDTO.getNewProdOfferId())) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.prodOfferId"));
        }
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(productExchangeDTO.getProdOfferId());
        if (!DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())
                && (!Const.STATUS_ACTIVE.equals(productOfferingDTO.getStatus())
                || !Const.PRODUCT_OFFER_TYPE.KIT.equals(productOfferingDTO.getProductOfferTypeId()))) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.prodOfferId.err"));
        }
        ProductOfferingDTO productOfferingNewDTO = productOfferingService.findOne(productExchangeDTO.getNewProdOfferId());
        if (!DataUtil.isNullOrZero(productOfferingNewDTO.getProductOfferingId())
                && (!Const.STATUS_ACTIVE.equals(productOfferingNewDTO.getStatus())
                || !Const.PRODUCT_OFFER_TYPE.KIT.equals(productOfferingNewDTO.getProductOfferTypeId()))) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.prodOfferId.err"));
        }

        validateExistsProdExchange(productOfferingDTO.getProductOfferingId(), productOfferingNewDTO.getProductOfferingId(), productExchangeDTO.getStartDatetime(), productExchangeDTO.getEndDatetime());

    }

    @Override
    public List<ProductExchangeDTO> searchProductExchange(ProductExchangeDTO productExchangeDTO) throws Exception, LogicException {
        return repository.searchProductExchange(productExchangeDTO);
    }

    private boolean checkEligibleProExchange(Long prodOfferId, Long prodOfferIdNew) throws Exception, LogicException {
        Date sysdate = getSysDate(em);
        List<ProductExchangeDTO> listCheck = mapper.toDtoBean(repository.checkProductExchange(prodOfferId, prodOfferIdNew, sysdate, sysdate));
        if (DataUtil.isNullOrEmpty(listCheck)) {
            return false;
        }
        return true;
    }

    @Override
    public void validateCreateTallyOut(List<StockTransDetailDTO> lsDetailDTOs) throws Exception, LogicException {
        if (DataUtil.isNullOrEmpty(lsDetailDTOs)) {
            throw new LogicException("", "", getText("mn.list.create.tally.out.validate.input"));
        }
        for (StockTransDetailDTO valdiateDetail : lsDetailDTOs) {
            if (DataUtil.isNullOrEmpty(valdiateDetail.getProdOfferCodeNew())) {
                throw new LogicException("", "", getText("mn.list.create.tally.out.validate.input.prodOfferCodeNew"));
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findByProductOfferCode(valdiateDetail.getProdOfferCode(), Const.STATUS_ACTIVE);
            if (!DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())
                    && (!Const.STATUS_ACTIVE.equals(productOfferingDTO.getStatus())
                    || !Const.PRODUCT_OFFER_TYPE.KIT.equals(productOfferingDTO.getProductOfferTypeId()))) {
                throw new LogicException("", getText("mn.list.product.exchange.validate.prodOfferId.err"));
            }
            ProductOfferingDTO productOfferingNewDTO = productOfferingService.findByProductOfferCode(valdiateDetail.getProdOfferCodeNew(), Const.STATUS_ACTIVE);
            if (!DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())
                    && (!Const.STATUS_ACTIVE.equals(productOfferingDTO.getStatus())
                    || !Const.PRODUCT_OFFER_TYPE.KIT.equals(productOfferingDTO.getProductOfferTypeId()))) {
                throw new LogicException("", getText("mn.list.product.exchange.validate.prodOfferId.err"));
            }
            valdiateDetail.setProdOfferIdChange(productOfferingNewDTO.getProductOfferingId());
            boolean check = checkEligibleProExchange(productOfferingDTO.getProductOfferingId(), productOfferingNewDTO.getProductOfferingId());
            if (!check) {
                throw new LogicException("", getText("mn.list.create.tally.out.validate.input.productExchang"));
            }
            List<StockTransSerialDTO> lstSerial = valdiateDetail.getLstStockTransSerial();
            for (StockTransSerialDTO serial : lstSerial) {
                boolean checkSerial = stockSimService.checkSimElegibleExists(serial.getFromSerial(), serial.getToSerial());
                if (!checkSerial) {
                    throw new LogicException("", getText("mn.list.create.tally.out.kit.file.result.sim"));
                }
                BaseMessage baseMess = saleWs.checkActiveSerial(serial.getFromSerial(), serial.getToSerial());
                if (!baseMess.isSuccess()) {
                    throw new LogicException("", getText("mn.list.create.tally.out.kit.file.result.sim.isdn"));
                }
            }
        }
    }

    @WebMethod
    @Override
    public BaseMessageStockTrans createTallyOut(String mode, String type, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                List<StockTransDetailDTO> lsDetailDTOs, RequiredRoleMap requiredRoleMap) throws Exception, LogicException {
        validateCreateTallyOut(lsDetailDTOs);
        BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.PRODUCT_EXCHANGE_KIT,
                Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
        return message;
    }


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
//        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
//        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
//        stockTransDTO.setStockAgent(Const.STOCK_TRANS_STATUS.IMPORTED);

        //tru so luong kho nhan
        flagStockDTO.setImportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        flagStockDTO.setExpCurrentQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);

//        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);

        //Tru so luong kho xuat
        flagStockDTO.setExportStock(true);

        //cap nhat trang thai stock_x ve da nhap (status=1 --> status=1)
        flagStockDTO.setOldStatus(Const.STOCK_GOODS.STATUS_NEW);
        flagStockDTO.setNewStatus(Const.STOCK_GOODS.STATUS_CONFIRM);
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PX);
        //Cap nhat serial ve kho nhan
//        flagStockDTO.setUpdateOwnerId(true);
//        flagStockDTO.setStatusForAgent(Const.STOCK_TRANS_STATUS.IMPORTED);
        // bankplus
//        flagStockDTO.setUpdateAccountBalance(true);
//        flagStockDTO.setRequestType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TRANS_TYPE_MINUS_EXPORT);
//        stockTransDTO.setBankplusStatus(Const.STOCK_TRANS.BANKPLUS_STATUS_NOT_APPROVE);
        // Set lai state hang theo ly do
        if (!DataUtil.isNullObject(stockTransDTO) && !DataUtil.isNullObject(stockTransDTO.getReasonId())) {
            ReasonDTO reasonDTO = reasonService.findOne(stockTransDTO.getReasonId());
            if (!DataUtil.isNullObject(reasonDTO) && !DataUtil.isNullObject(reasonDTO.getHaveMoney())) {
                flagStockDTO.setStateIdForReasonId(reasonDTO.getHaveMoney());
            }
        }
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        //validate ma phieu xuat
        StaffDTO staffDTO = staffService.findOne(stockTransActionDTO.getActionStaffId());
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staff.code.invalid.name");
        }
        // Validate so luong serial thuc te trong kho voi so luong hang xuat.
        baseValidateService.doInputSerialValidate(stockTransDTO, lstStockTransDetail);

        String actionNoteCode = stockTransActionDTO.getActionCode();
        if (DataUtil.isNullOrEmpty(actionNoteCode)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.transCode.require.msg");
        } else if (actionNoteCode.trim().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.transCode.error.format.msg.product");
        } else if (DataUtil.safeEqual(staffDTO.getShopId(), Const.VT_SHOP_ID)
                && !staffService.validateTransCode(actionNoteCode, stockTransActionDTO.getActionStaffId(), Const.STOCK_TRANS_TYPE.EXPORT)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.expNote.invalid");
        }
        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().getBytes("UTF-8").length > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }
        //check trung
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.retrieve.order.product");
        }
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            ProductOfferingDTO productOfferingDTOOld = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTOOld) || DataUtil.safeEqual(productOfferingDTOOld.getStatus(), Const.STATUS_INACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.old.product.esxit", stockTransDetailDTO.getProdOfferName());
            }
            ProductOfferingDTO productOfferingDTONew = productOfferingService.findOne(stockTransDetailDTO.getProdOfferIdChange());
            if (DataUtil.isNullObject(productOfferingDTONew) || DataUtil.safeEqual(productOfferingDTONew.getStatus(), Const.STATUS_INACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.new.product.esxit", productOfferingDTONew.getName());
            }
            //check trung ma tai chinh
//            if (!DataUtil.safeEqual(productOfferingDTONew.getAccountingModelCode(), productOfferingDTOOld.getAccountingModelCode())) {
//                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.not.match.accounting.model.code", productOfferingDTONew.getCode(), productOfferingDTOOld.getCode());
//            }
            if (DataUtil.safeEqual(productOfferingDTONew.getProductOfferingId(), productOfferingDTOOld.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.duplicate", productOfferingDTONew.getCode(), productOfferingDTOOld.getCode());
            }
        }
        //check trung mat hang va state_id
        if (lstStockTransDetail.size() >= 2) {
            for (int i = 0; i < lstStockTransDetail.size() - 1; i++) {
                for (int j = i + 1; j < lstStockTransDetail.size(); j++) {
                    if (DataUtil.safeEqual(lstStockTransDetail.get(i).getProdOfferId(), lstStockTransDetail.get(j).getProdOfferId())
                            && DataUtil.safeEqual(lstStockTransDetail.get(i).getStateId(), lstStockTransDetail.get(j).getStateId())) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "create.note.change.product.duplicate.state", lstStockTransDetail.get(i).getProdOfferName(), productOfferingService.getStockStateName(lstStockTransDetail.get(i).getStateId()));
                    }
                }
            }
        }
        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
        //validate vuot qua han muc kho xuat
        baseValidateService.doQuantityAvailableValidate(stockTransDTO, lstStockTransDetail);
        //validate so luong cho phep xuat kho
        baseValidateService.doCurrentQuantityValidate(stockTransDTO, lstStockTransDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StockTransActionDTO executeStockTrans(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) throws Exception {
        StockTransActionDTO transActionDTO = super.executeStockTrans(stockTransDTO, stockTransActionDTO,
                lstStockTransDetail, requiredRoleMap);
        StockTransActionDTO stockTransActionNew = DataUtil.cloneBean(transActionDTO);
        stockTransActionNew.setStockTransActionId(null);
        stockTransActionNew.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransActionNew.setSignCaStatus(null);
        stockTransActionNew.setActionCode(null);
        StockTransActionDTO stockTransActionSaveDTO = stockTransActionService.save(stockTransActionNew);
        //
        changeProductTrans(stockTransDTO, transActionDTO, lstStockTransDetail);
        doIncreaseStockNum(stockTransActionSaveDTO, Const.STOCK_TRANS.TRANS_CODE_PX, requiredRoleMap);
        return transActionDTO;

    }

    private void changeProductTrans(StockTransDTO stockTransDTO, StockTransActionDTO
            stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception, LogicException {
        //Thong tin giao dich chuyen doi mat hang
        ChangeModelTransDTO changeModelTransDTO = new ChangeModelTransDTO();
        changeModelTransDTO.setStockTransId(stockTransActionDTO.getStockTransId());
        changeModelTransDTO.setFromOwnerId(stockTransDTO.getFromOwnerId());
        changeModelTransDTO.setFromOwnerType(stockTransDTO.getFromOwnerType());
        if (Const.L_VT_SHOP_ID.equals(stockTransDTO.getFromOwnerId())) {
            changeModelTransDTO.setToOwnerId(DataUtil.safeToLong(Const.TD_PARTNER_ID));
            changeModelTransDTO.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
        } else {
            changeModelTransDTO.setToOwnerId(DataUtil.safeToLong(Const.VT_SHOP_ID));
            changeModelTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        }
        changeModelTransDTO.setCreateUserId(stockTransActionDTO.getActionStaffId());
        changeModelTransDTO.setStatus(0L);
        changeModelTransDTO.setCreateDate(stockTransDTO.getCreateDatetime());
        changeModelTransDTO.setRequestType(Const.CHANGE_MODEL_TRANS_REQUEST_TYPE.CREATE_REQUEST_KIT);
        ChangeModelTransDTO changeModelTransSave = changeModelTransService.save(changeModelTransDTO);
        //detail
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            ChangeModelTransDetailDTO changeModelTransDetailDTO = new ChangeModelTransDetailDTO();
            changeModelTransDetailDTO.setChangeModelTransId(changeModelTransSave.getChangeModelTransId());
            changeModelTransDetailDTO.setNewProdOfferId(stockTransDetailDTO.getProdOfferIdChange());
            changeModelTransDetailDTO.setOldProdOfferId(stockTransDetailDTO.getProdOfferId());
            changeModelTransDetailDTO.setStateId(stockTransDetailDTO.getStateId());
            changeModelTransDetailDTO.setQuantity(stockTransDetailDTO.getQuantity());
            changeModelTransDetailDTO.setNote(stockTransDTO.getNote());
            changeModelTransDetailDTO.setCreateDate(stockTransDTO.getCreateDatetime());
            ChangeModelTransDetailDTO modelTransDetailSave = changeModelTransDetailService.save(changeModelTransDetailDTO);
            List<StockTransSerialDTO> lstStockTransSerial = stockTransDetailDTO.getLstStockTransSerial();
            for (StockTransSerialDTO stockTransSerial : lstStockTransSerial) {
                ChangeModelTransSerialDTO changeModelTransSerialDTO = new ChangeModelTransSerialDTO();
                changeModelTransSerialDTO.setChangeModelTransDetailId(modelTransDetailSave.getChangeModelTransDetailId());
                changeModelTransSerialDTO.setFromSerial(stockTransSerial.getFromSerial());
                changeModelTransSerialDTO.setToSerial(stockTransSerial.getToSerial());
                changeModelTransSerialDTO.setQuantity(stockTransSerial.getQuantity());
                changeModelTransSerialDTO.setCreateDate(stockTransDTO.getCreateDatetime());
                changeModelTransSerialService.save(changeModelTransSerialDTO);
            }
        }
    }

    @Override
    public void validateExistsProdExchange(Long prodOfferId, Long prodOfferIdNew, Date fromDate, Date toDate) throws Exception, LogicException {
        Date sysdate = getSysDate(em);
        if (fromDate == null)
            fromDate = sysdate;
        if (toDate == null)
            toDate = sysdate;
        List<ProductExchangeDTO> listCheck = mapper.toDtoBean(repository.checkProductExchange(prodOfferId, prodOfferIdNew, fromDate, toDate));
        if (!DataUtil.isNullOrEmpty(listCheck)) {
            throw new LogicException("", getText("mn.list.product.exchange.validate.exists"));
        }
    }

    public ShopDTO validateShopExists(Long shopId) throws Exception, LogicException {
        if (DataUtil.isNullOrZero(shopId)) {
            throw new LogicException("", getTextParam("mn.approve.product.exchange.validate.shopId"));
        }
        if (String.valueOf(shopId).length() > 20) {
            throw new LogicException("", getTextParam("mn.approve.product.exchange.validate.shopId.length"));
        }

        ShopDTO shopDTO = shopService.findOne(shopId);
        if (DataUtil.isNullObject(shopDTO)
                || DataUtil.isNullOrZero(shopDTO.getShopId())
                || !Const.STATUS.ACTIVE.equals(shopDTO.getStatus())) {
            throw new LogicException("", getTextParam("mn.approve.product.exchange.validate.shopId.err"));
        }
        return shopDTO;
    }

    @Override
    @WebMethod
    public BaseMessageChangeModelTrans findApprovedRequestProductExchangeKit(Long shopId, Date fromDate, Date toDate) throws Exception, LogicException {
        BaseMessageChangeModelTrans messOut = new BaseMessageChangeModelTrans();
        ShopDTO shopDTO = validateShopExists(shopId);
        if (!DataUtil.isNullObject(fromDate)
                && !DataUtil.isNullObject(toDate)
                && (fromDate.compareTo(toDate) > 0)) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.list.product.exchange.required.date.todate.fromdate"));
            return messOut;
        }
        if (!DataUtil.isNullObject(fromDate)
                && !DataUtil.isNullObject(toDate)
                && (fromDate.compareTo(toDate) > 0)) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.list.product.exchange.required.date.todate.fromdate"));
            return messOut;
        }
        List<ChangeModelTransDTO> lstChangeModelTransDTOs = repoChangeModelTrans.findApprovedRequestProductExchangeKit(shopDTO.getShopId(), fromDate, toDate);
        messOut.setLstChangeModelTransDTOs(DataUtil.defaultIfNull(lstChangeModelTransDTOs, new ArrayList<>()));
        messOut.setSuccess(true);
        return messOut;
    }

    @Override
    @WebMethod
    public BaseMessageChangeModelTransDetail findModelTransDetailByChangeModelTransId(Long changeModelTransId) throws Exception, LogicException {
        BaseMessageChangeModelTransDetail messOut = new BaseMessageChangeModelTransDetail();

        if (DataUtil.isNullOrZero(changeModelTransId)) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.changeModelTransDetail.Id.err"));
            return messOut;
        }
        if (String.valueOf(changeModelTransId).length() > 20) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.validate.changeModelTransId.length"));
            return messOut;
        }
        messOut.setLstChangeModelTransDetailDTOs(repoChangeModelTransDetail.findModelTransDetailByChangeModelTransId(changeModelTransId));
        messOut.setSuccess(true);
        return messOut;
    }

    @Override
    @WebMethod
    public BaseMessageChangeModelTransSerial findModelTransSerialByChangeModelTransDetailId(Long changeModelTransDetailId) throws Exception, LogicException {
        BaseMessageChangeModelTransSerial messOut = new BaseMessageChangeModelTransSerial();
        if (DataUtil.isNullOrZero(changeModelTransDetailId)) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.changeModelTransDetail.Id.err"));
            return messOut;
        }
        if (String.valueOf(changeModelTransDetailId).length() > 20) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.validate.changeModelTransId.length"));
            return messOut;
        }
        List<ChangeModelTransSerialDTO> lstChangeModelTransSerialDTOs = repoChangeModelTransSerial.findModelTransSerialByChangeModelTransDetailId(changeModelTransDetailId);
        messOut.setLstChangeModelTransSerialDTOs(lstChangeModelTransSerialDTOs);
        messOut.setSuccess(true);
        return messOut;
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateChangeModelTransStatusDestroy(Long changeModelTransId) throws Exception, LogicException {
        BaseMessage messOut = new BaseMessage();
        if (DataUtil.isNullOrZero(changeModelTransId)) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.changeModelTransDetail.Id.err"));
            throw new LogicException("", getText("mn.approve.product.exchange.changeModelTrans.status.err"));
        }
        if (String.valueOf(changeModelTransId).length() > 20) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.validate.changeModelTransId.length"));
            return messOut;
        }
        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO)
                || DataUtil.isNullOrZero(changeModelTransDTO.getChangeModelTransId())) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.change.model.trans.validate.err"));
            throw new LogicException("", getText("mn.approve.change.model.trans.validate.err"));
        }
        if (!Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_STATUS_WAIT_TOOLKIT.equals(changeModelTransDTO.getStatus())) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.changeModelTrans.status.err"));
            throw new LogicException("", getText("mn.approve.product.exchange.changeModelTrans.status.err"));
        }
        ChangeModelTransDTO changeModelTransSaveDTO = changeModelTransService.updateChangeModelTransStatus(changeModelTransId, Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_STATUS_WAIT_DESTROY);
        messOut.setSuccess(true);
        messOut.setKeyMsg(getTextParam("mn.approve.product.exchange.changeModelTrans.status.success", String.valueOf(changeModelTransSaveDTO.getChangeModelTransId())));
        return messOut;
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateChangeModelTransStatusSuccess(Long changeModelTransId) throws Exception, LogicException {
        BaseMessage messOut = new BaseMessage();

        if (DataUtil.isNullOrZero(changeModelTransId)) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.changeModelTransDetail.Id.err"));
            throw new LogicException("", getText("mn.approve.product.exchange.changeModelTransDetail.Id.err"));
        }
        if (String.valueOf(changeModelTransId).length() > 20) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.validate.changeModelTransId.length"));
            return messOut;
        }
        ChangeModelTransDTO changeModelTransDTO = changeModelTransService.findOne(changeModelTransId);
        if (DataUtil.isNullObject(changeModelTransDTO)
                || DataUtil.isNullOrZero(changeModelTransDTO.getChangeModelTransId())) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.change.model.trans.validate.err"));
            throw new LogicException("", getText("mn.approve.change.model.trans.validate.err"));
        }
        if (!Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_STATUS_WAIT_TOOLKIT.equals(changeModelTransDTO.getStatus())) {
            messOut.setSuccess(false);
            messOut.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            messOut.setKeyMsg(getText("mn.approve.product.exchange.changeModelTrans.status.err"));
            throw new LogicException("", getText("mn.approve.product.exchange.changeModelTrans.status.err"));
        }
        ChangeModelTransDTO changeModelTransSaveDTO = changeModelTransService.updateChangeModelTransStatus(changeModelTransId, Const.CHANGE_PRODUCT.CHANGE_MODEL_TRANS_STATUS_SUCCESS);
        messOut.setSuccess(true);
        messOut.setKeyMsg(getTextParam("mn.approve.product.exchange.changeModelTrans.status.success", String.valueOf(changeModelTransSaveDTO.getChangeModelTransId())));
        return messOut;
    }
}

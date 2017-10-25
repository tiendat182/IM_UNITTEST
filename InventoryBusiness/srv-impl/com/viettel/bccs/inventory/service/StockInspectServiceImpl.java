package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockInspect;
import com.viettel.bccs.inventory.repo.StockInspectRepo;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockInspectServiceImpl extends BaseServiceImpl implements StockInspectService {

    private final BaseMapper<StockInspect, StockInspectDTO> mapper = new BaseMapper<>(StockInspect.class, StockInspectDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private StockInspectSysService stockInspectSysService;
    @Autowired
    private StockInspectRealService stockInspectRealService;
    @Autowired
    private StockTransRepo stockTransRepo;
    @Autowired
    private StockInspectRepo repository;
    public static final Logger logger = Logger.getLogger(StockInspectService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockInspectDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockInspectDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockInspectDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockInspectDTO save(StockInspectDTO dto) throws Exception {
        try {
            StockInspect stockInspect = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockInspect);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockInspectDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<ApproveStockInspectDTO> searchApproveInspect(Long approveUserId, Long ownerId, Long ownerType, Long approveStatus, Long prodOfferTypeId, String code, Date fromDate, Date toDate) throws Exception {
        return repository.searchApproveInspect(approveUserId, ownerId, ownerType, approveStatus, prodOfferTypeId, code, fromDate, toDate);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateApprove(List<StockInspectDTO> lsStockInspectDTO) throws LogicException, Exception {
        return repository.updateApprove(lsStockInspectDTO);
    }

    @Override
    public List<StockInspectCheckDTO> checkStockInspect(StockInspectDTO stockInspectDTO, StaffDTO staffDTO, boolean isInspectQuantity) throws LogicException, Exception {
        List<StockInspectCheckDTO> stockInspectCheckDTOs = Lists.newArrayList();
        try {
            List<Date> fromToDate = getFromDateToDateCheck(isInspectQuantity);
            Date fromDate = null;
            Date toDate = null;
            if (!DataUtil.isNullOrEmpty(fromToDate) && fromToDate.size() > 1) {
                fromDate = fromToDate.get(0);
                toDate = fromToDate.get(1);
            }
            // Validate
            validateStockInspect(stockInspectDTO, staffDTO, fromDate, toDate, isInspectQuantity);
            // Lay danh sach kiem ke.
            if (isInspectQuantity) {
                stockInspectCheckDTOs = repository.getStockInspectForQuantity(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate);
            } else if (DataUtil.safeEqual(stockInspectDTO.getProdOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                stockInspectCheckDTOs = repository.getStockInspectForNoSerial(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate);
            } else {
                stockInspectCheckDTOs = repository.getStockInspectForSerial(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate);
            }
        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("StockInspectService.checkStockInspect", ex);
            throw ex;
        }
        return stockInspectCheckDTOs;
    }

    public void validateConfigDate(boolean isInspectQuantity) throws LogicException, Exception {

        Date currentDate = getSysDate(em);
        List<Date> fromToDate = getFromDateToDateCheck(isInspectQuantity);
        Date fromDate = currentDate;
        Date toDate = currentDate;
        if (!DataUtil.isNullOrEmpty(fromToDate) && fromToDate.size() > 1) {
            fromDate = fromToDate.get(0);
            toDate = fromToDate.get(1);
        }

        if (DateUtil.compareDateToDate(currentDate, fromDate) < 0 || DateUtil.compareDateToDate(currentDate, toDate) > 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,
                    isInspectQuantity ? "stock.inspect.time.checkStock.notPermission.quantity" : "stock.inspect.time.checkStock.notPermission.serial"
                    , DateUtil.date2ddMMyyyyString(fromDate), DateUtil.date2ddMMyyyyString(toDate));
        }
    }

    @Override
    public void validateStockInspect(StockInspectDTO stockInspectDTO, StaffDTO staffDTO, Date fromDate, Date toDate, boolean isInspectQuantity) throws LogicException, Exception {
        // Validate du lieu
        if (DataUtil.isNullObject(stockInspectDTO) || DataUtil.isNullOrZero(stockInspectDTO.getProdOfferTypeId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.type.product.not.blank");
        }
        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(stockInspectDTO.getProdOfferTypeId());
        if (DataUtil.isNullObject(productOfferTypeDTO) || DataUtil.isNullOrZero(productOfferTypeDTO.getProductOfferTypeId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOfferType.notFound");
        }

        //Neu kiem ke theo so luong hoac no-serial bat buoc chon trang thai
        if ((isInspectQuantity || DataUtil.safeEqual(stockInspectDTO.getProdOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL))
                && DataUtil.isNullOrZero(stockInspectDTO.getStateId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.track.state.stock.require.msg");
        }
        if (DataUtil.safeEqual(stockInspectDTO.getOwnerType(), Const.OWNER_TYPE.STAFF)) {
            StaffDTO staff = staffService.findOne(stockInspectDTO.getOwnerId());
            if (DataUtil.isNullObject(staff) || DataUtil.isNullOrZero(staff.getStaffId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.staff.notFound");
            }
//            if (!DataUtil.safeEqual(staff.getShopId(), staffDTO.getShopId())) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.staff.not.shop.userLogin");
//            }
        }
        if (!DataUtil.isNullOrZero(stockInspectDTO.getProdOfferId())) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockInspectDTO.getProdOfferId());
            if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
            }
        }
        //validate ngay kiem ke
        validateConfigDate(isInspectQuantity);
        // Neu mat hang noserial
//        if (isInspectQuantity) {
//            if (DataUtil.isNullOrZero(stockInspectDTO.getProdOfferId())) {
//                if (isFinishStockInspectNotStockTotal(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
//                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate)) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finished");
//                }
//            } else {
//                if (isFinishNotCheckStockTotal(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
//                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate, null)) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finish.in.month");
//                }
//            }
//        } else if (DataUtil.safeEqual(stockInspectDTO.getProdOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
//            if (DataUtil.isNullOrZero(stockInspectDTO.getProdOfferId())) {
//                if (isFinishStockInspect(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
//                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate)) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finished");
//                }
//            } else {
//                if (isFinishNotCheckStockTotal(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
//                        stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate, null)) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finish.in.month");
//                }
//            }
//        } else {
//            // neu mat hang check serial
//            if (isFinishStockInspect(stockInspectDTO.getOwnerType(), stockInspectDTO.getOwnerId(),
//                    stockInspectDTO.getStateId(), stockInspectDTO.getProdOfferTypeId(), stockInspectDTO.getProdOfferId(), fromDate, toDate)) {
//                if (DataUtil.isNullOrZero(stockInspectDTO.getProdOfferId())) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finished");
//                } else {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.product.offer.finished");
//                }
//            }
//        }
    }

    @Override
    public boolean isFinishStockInspect(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws LogicException, Exception {
        List<Long> prodOfferIds = repository.getStockTotalMinusStockInspect(ownerType, ownerId, stateId, productOfferTypeId, prodOfferId, fromDate, toDate, true);
        return DataUtil.isNullOrEmpty(prodOfferIds);
    }

    @Override
    public boolean isFinishStockInspectNotStockTotal(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate) throws LogicException, Exception {
        List<Long> prodOfferIds = repository.getStockTotalMinusStockInspect(ownerType, ownerId, stateId, productOfferTypeId, prodOfferId, fromDate, toDate, false);
        return DataUtil.isNullOrEmpty(prodOfferIds);
    }

    @Override
    public boolean isFinishNotCheckStockTotal(Long ownerType, Long ownerId, Long stateId, Long productOfferTypeId, Long prodOfferId, Date fromDate, Date toDate, Long approveStatus) throws LogicException, Exception {
        List<StockInspect> stockInspects = repository.getStockInspect(ownerType, ownerId, stateId, productOfferTypeId, prodOfferId, fromDate, toDate, Const.STOCK_INSPECT.IS_FINISHED, approveStatus);
        return !DataUtil.isNullOrEmpty(stockInspects);
    }

    @Override
    public Long getQuantityStockTotal(Long ownerType, Long ownerId, Long productOfferTypeId, Long prodOfferId, Long stateId) throws LogicException, Exception {
        return repository.getQuantityStockTotal(ownerType, ownerId, productOfferTypeId, prodOfferId, stateId);
    }

    @Override
    public void validateSaveStockInspect(Long prodOfferId, Long productOfferTypeId, Long stateId, Long ownerType, Long ownerId,
                                         List<StockInspectCheckDTO> stockInspectList, StaffDTO userLogin, StaffDTO approveStaff,
                                         Date fromDate, Date toDate, boolean isInspectQuantity, boolean isApprover) throws LogicException, Exception {
//        if (DataUtil.isNullOrEmpty(stockInspectList)) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.empty.error");
//        }

        if (DataUtil.isNullObject(ownerType) || DataUtil.isNullObject(ownerId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.stock.not.exist");
        } else {
            if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF_LONG)) {
                StaffDTO staffDTO = staffService.findOne(ownerId);
                if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(staffDTO.getStatus(), Const.STATUS.ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.view.approve.stock.inspect.staff.status");
                }
            }
            if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(ownerId);
                if (DataUtil.isNullObject(shopDTO) || !DataUtil.safeEqual(shopDTO.getStatus(), Const.STATUS.ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.view.approve.stock.inspect.shop.status");
                }
            }
        }

        if (DataUtil.isNullObject(userLogin)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
        } else {
            StaffDTO staffDTO = staffService.findOne(DataUtil.safeToLong(userLogin.getStaffId()));
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(staffDTO.getStatus(), Const.STATUS.ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
            }
        }

        if (!DataUtil.isNullObject(approveStaff)) {
            StaffDTO staffDTO = staffService.findOne(DataUtil.safeToLong(approveStaff.getStaffId()));
            if (DataUtil.isNullObject(staffDTO) || !DataUtil.safeEqual(staffDTO.getStatus(), Const.STATUS.ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.not.found");
            }
            if (DataUtil.safeEqual(approveStaff.getStaffId(), userLogin.getStaffId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.user.login.error");
            }
        }

        //validate ngay kiem ke
        validateConfigDate(isInspectQuantity);

        if (isInspectQuantity) {
            if (DataUtil.isNullOrEmpty(stockInspectList)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.empty.error");
            }
            for (StockInspectCheckDTO stockInspectCheckDTO : stockInspectList) {
                stockInspectCheckDTO.setNote(DataUtil.safeToString(stockInspectCheckDTO.getNote()).trim());
                if (stockInspectCheckDTO.getNote().length() > 500) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.isdn.manage.create.field.validate.note", 500);
                }
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockInspectCheckDTO.getProdOfferId());
                if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferTypeId())
                        || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS.ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.not.exist", stockInspectCheckDTO.getProductName());

                }
                if (stockInspectCheckDTO.isDisableQuantityPoor()) {
                    stockInspectCheckDTO.setQuantityPoor(stockInspectCheckDTO.getQuanittyReal());
                }
                if (stockInspectCheckDTO.getCurrentQuantity() == null || stockInspectCheckDTO.getQuanittyReal() == null || stockInspectCheckDTO.getQuantityPoor() == null) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.empty.error", productOfferingDTO.getName());
                }
                Long quantity = DataUtil.safeToLong(stockInspectCheckDTO.getCurrentQuantity());
                if (quantity.compareTo(0L) < 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.check.error", productOfferingDTO.getName());
                }
                quantity = DataUtil.safeToLong(stockInspectCheckDTO.getQuanittyReal());
                if (quantity.compareTo(0L) < 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.check.error", productOfferingDTO.getName());
                }

                quantity = DataUtil.safeToLong(stockInspectCheckDTO.getQuantityPoor());
                if (quantity.compareTo(0L) < 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.poor.error", productOfferingDTO.getName());
                }
                if (DataUtil.safeToLong(stockInspectCheckDTO.getQuantityPoor()).compareTo(stockInspectCheckDTO.getQuanittyReal()) > 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.valid.real.and.poor.error", productOfferingDTO.getName());
                }
//                if (isFinishNotCheckStockTotal(ownerType, ownerId,
//                        stockInspectCheckDTO.getStateId(), productOfferTypeId, stockInspectCheckDTO.getProdOfferId(), fromDate, toDate, null)) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finish.in.month");
//                }
        }
        } else if (DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
            if (DataUtil.isNullOrEmpty(stockInspectList)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.empty.error");
            }
            for (StockInspectCheckDTO stockInspectCheckDTO : stockInspectList) {
                Long quantity = stockInspectCheckDTO.getQuantity();
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockInspectCheckDTO.getProdOfferId());
                if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferTypeId())
                        || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS.ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.not.exist", stockInspectCheckDTO.getProductName());
                }
                if (DataUtil.isNullObject(quantity)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.empty.error", productOfferingDTO.getName());
                }
                if (quantity < 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.quantity.error", productOfferingDTO.getName());
                }
//                if (isFinishNotCheckStockTotal(ownerType, ownerId, stockInspectCheckDTO.getStateId(),
//                        productOfferTypeId, stockInspectCheckDTO.getProdOfferId(), fromDate, toDate, null)) {
//                    throw new LogicException(ErrorCode.ERROR_STANDARD.SUCCESS, "validate.stock.inspect.is.finish.in.month");
//                }
            }
        } else {
            // Kiem tra mat hang da chot chua.
            if (DataUtil.isNullOrEmpty(stockInspectList) && DataUtil.isNullOrZero(prodOfferId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.list.empty.error");
            }
            for (StockInspectCheckDTO stockInspectCheckDTO : stockInspectList) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockInspectCheckDTO.getProdOfferId());
                if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferTypeId())
                        || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS.ACTIVE)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.not.exist", stockInspectCheckDTO.getProductName());
                }
//                if (isFinishStockInspect(ownerType, ownerId, stockInspectCheckDTO.getStateId(), productOfferTypeId, stockInspectCheckDTO.getProdOfferId(), fromDate, toDate)) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.is.finish.in.month");
//                }
            }
        }

        //Validate phieu xuat chua lap phieu
//        if (isApprover) {
//            Map<String, StockInspectCheckDTO> collect = stockInspectList.stream().collect(Collectors.toMap(x -> x.getProdOfferId() + "|" + x.getStateId(), x -> x, (first, second) -> first));
//            List<StockInspectCheckDTO> deduped = new ArrayList<>(collect.values());
//            for (StockInspectCheckDTO stockInspectCheckDTO : deduped) {
//                if (stockTransRepo.checkSuspendInspect(ownerType, ownerId, stockInspectCheckDTO.getProdOfferId(), stockInspectCheckDTO.getStateId())) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.suspend.note", stockInspectCheckDTO.getProductName(), stockInspectCheckDTO.getStateName());
//                }
//
//            }
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStockInspect(Long prodOfferId, Long productOfferTypeId, Long stateId, Long ownerType, Long ownerId,
                                 List<StockInspectCheckDTO> stockInspectList, boolean isApprover,
                                 StaffDTO staffDTO, StaffDTO approveStaff, boolean isInspectQuantity) throws LogicException, Exception {
        try {
            // Validate
            if (DataUtil.isNullOrZero(productOfferTypeId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.type.product.not.blank");
            }
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
            if (DataUtil.isNullObject(productOfferTypeDTO) || DataUtil.isNullOrZero(productOfferTypeDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOfferType.notFound");
            }
            List<Date> fromToDate = getFromDateToDateCheck(false);
            Date fromDate = null;
            Date toDate = null;
            if (!DataUtil.isNullOrEmpty(fromToDate) && fromToDate.size() > 1) {
                fromDate = fromToDate.get(0);
                toDate = fromToDate.get(1);
            }
            if (DataUtil.isNullOrEmpty(stockInspectList)) {
                stockInspectList = Lists.newArrayList();
            }
            validateSaveStockInspect(prodOfferId, productOfferTypeId, stateId, ownerType, ownerId,
                    stockInspectList, staffDTO, approveStaff, fromDate, toDate, isInspectQuantity, isApprover);
            // Luu DB
            // Chuan hoa lai du lieu truoc khi insert vao DB.
            List<StockInspectDTO> stockInspectStandard = Lists.newArrayList();
            if (isInspectQuantity) {
                for (StockInspectCheckDTO stockInspectCheckDTO : stockInspectList) {
                    stockInspectCheckDTO.setOwnerId(ownerId);
                    stockInspectCheckDTO.setOwnerType(ownerType);
                    StockInspectDTO stockInspectDTO = copyFromStockInspectCheck(stockInspectCheckDTO);
                    stockInspectDTO.setProdOfferId(stockInspectCheckDTO.getProdOfferId());
                    stockInspectDTO.setQuantityReal(stockInspectCheckDTO.getQuanittyReal());
                    stockInspectDTO.setQuantitySys(stockInspectCheckDTO.getCurrentQuantity());
                    stockInspectDTO.setQuantityPoor(stockInspectCheckDTO.getQuantityPoor());
                    stockInspectDTO.setNote(stockInspectCheckDTO.getNote());
                    stockInspectDTO.setStateId(stockInspectCheckDTO.getStateId());
                    stockInspectStandard.add(stockInspectDTO);
                }
            } else if (DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                for (StockInspectCheckDTO stockInspectCheckDTO : stockInspectList) {
                    if (!DataUtil.safeEqual(stockInspectCheckDTO.getQuantity(), 0L) || !DataUtil.safeEqual(stockInspectCheckDTO.getCurrentQuantity(), 0L)) {
                        stockInspectCheckDTO.setOwnerId(ownerId);
                        stockInspectCheckDTO.setOwnerType(ownerType);
                        StockInspectDTO stockInspectDTO = copyFromStockInspectCheck(stockInspectCheckDTO);
                        stockInspectDTO.setProdOfferId(stockInspectCheckDTO.getProdOfferId());
                        stockInspectDTO.setQuantityReal(stockInspectCheckDTO.getQuantity());
                        stockInspectDTO.setQuantitySys(stockInspectCheckDTO.getCurrentQuantity());
                        stockInspectDTO.setStateId(stockInspectCheckDTO.getStateId());
                        stockInspectStandard.add(stockInspectDTO);
                    }
                }
            } else {
                for (StockInspectCheckDTO stockInspectCheckDTO : stockInspectList) {
                    stockInspectCheckDTO.setOwnerId(ownerId);
                    stockInspectCheckDTO.setOwnerType(ownerType);
                    addSerialToStockInspect(stockInspectCheckDTO, stockInspectStandard);
                }
                // Truong hop chot kiem ke neu chon mat hang ma khong nhap serial vao van cho thuc hien
                if (DataUtil.isNullOrEmpty(stockInspectList) && !DataUtil.isNullOrZero(prodOfferId)) {
                    StockInspectDTO stockInspectDTO = new StockInspectDTO();
                    stockInspectDTO.setOwnerId(ownerId);
                    stockInspectDTO.setOwnerType(ownerType);
                    stockInspectDTO.setProdOfferId(prodOfferId);
                    stockInspectDTO.setStateId(stateId);
                    stockInspectDTO.setQuantityReal(0L);
                    stockInspectStandard.add(stockInspectDTO);
                }
            }
            for (StockInspectDTO stockInspectTemp : stockInspectStandard) {
                List<StockInspectDTO> stockInspectDTOs = mapper.toDtoBean(repository.getStockInspect(stockInspectTemp.getOwnerType(),
                        stockInspectTemp.getOwnerId(), stockInspectTemp.getStateId(), productOfferTypeId,
                        stockInspectTemp.getProdOfferId(), fromDate, toDate, null, null));
                StockInspectDTO stockInspectDTO = null;
                if (!DataUtil.isNullOrEmpty(stockInspectDTOs)) {
                    stockInspectDTO = stockInspectDTOs.get(0);
                }
                Date currentDate = getSysDate(em);
                boolean newInspect = false;
                if (DataUtil.isNullObject(stockInspectDTO)) {
                    stockInspectDTO = new StockInspectDTO();
                    Long stockInspectId = getSequence(em, "STOCK_INSPECT_SEQ");
                    stockInspectDTO.setStockInspectId(stockInspectId);
                    stockInspectDTO.setCreateDate(currentDate);
                    newInspect = true;
                }
                stockInspectDTO.setOwnerType(stockInspectTemp.getOwnerType());
                stockInspectDTO.setOwnerId(stockInspectTemp.getOwnerId());
                stockInspectDTO.setProdOfferTypeId(productOfferTypeId);
                stockInspectDTO.setProdOfferId(stockInspectTemp.getProdOfferId());
                stockInspectDTO.setStateId(stockInspectTemp.getStateId());
                stockInspectDTO.setUpdateDate(currentDate);
                stockInspectDTO.setQuantityReal(stockInspectTemp.getQuantityReal());
                stockInspectDTO.setQuantityPoor(stockInspectTemp.getQuantityPoor());
                stockInspectDTO.setNote(stockInspectTemp.getNote());
                stockInspectDTO.setCreateUserId(staffDTO.getStaffId());
                stockInspectDTO.setCreateUserName(staffDTO.getName());
                stockInspectDTO.setCreateUser(staffDTO.getStaffCode());
                stockInspectDTO.setIsFinishedAll(Const.STOCK_INSPECT.IS_NOT_FINISH);
                stockInspectDTO.setApproveStatus(Const.STOCK_INSPECT.STATUS_NOT_APPROVE);

                //neu thuc hien kiem ke lai, xoa cac ban ghi cu trong stock_inspect_real va stock_inspect_sys
                if (!newInspect) {
                    stockInspectRealService.deleteStockInspect(stockInspectDTO.getStockInspectId());
                    stockInspectSysService.deleteStockInspectSys(stockInspectDTO.getStockInspectId());
                }

                // Neu chon xac nhan kiem ke
                if (isApprover) {
                    stockInspectDTO.setIsFinished(Const.STOCK_INSPECT.IS_FINISHED);
                    if (DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL) || isInspectQuantity) {
                        stockInspectDTO.setQuantitySys(stockInspectTemp.getQuantitySys());
                    } else {
                        // Luu thong tin serial thuc te
                        List<StockTransSerialDTO> stockTransSerialDTOs = stockTransSerialService.getListSerialFromTable(stockInspectTemp.getProdOfferId(),
                                stockInspectTemp.getOwnerId(), stockInspectTemp.getOwnerType(), stockInspectTemp.getStateId(), productOfferTypeDTO.getTableName());
                        Long quantitySys = 0L;
                        if (!DataUtil.isNullOrEmpty(stockTransSerialDTOs)) {
                            for (StockTransSerialDTO stockTransSerialDTO : stockTransSerialDTOs) {
                                StockInspectSysDTO stockInspectSysDTO = new StockInspectSysDTO();
                                stockInspectSysDTO.setCreateDate(currentDate);
                                stockInspectSysDTO.setStockInspectId(stockInspectDTO.getStockInspectId());
                                stockInspectSysDTO.setFromSerial(stockTransSerialDTO.getFromSerial());
                                stockInspectSysDTO.setToSerial(stockTransSerialDTO.getToSerial());
                                stockInspectSysDTO.setQuantity(stockTransSerialDTO.getQuantity());
                                quantitySys += stockTransSerialDTO.getQuantity();
                                stockInspectSysService.save(stockInspectSysDTO);
                            }
                        }
                        stockInspectDTO.setQuantitySys(quantitySys);
                    }
                } else {
                    stockInspectDTO.setIsFinished(Const.STOCK_INSPECT.IS_NOT_FINISH);
                    stockInspectDTO.setQuantitySys(Const.STOCK_INSPECT.QUANTITY_ZERO);

                    if (DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL) || isInspectQuantity) {
                        stockInspectDTO.setQuantitySys(stockInspectTemp.getQuantitySys());
                    }
                }
                stockInspectDTO = save(stockInspectDTO);

                // B2. insert du lieu moi
                if (!DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL) && !isInspectQuantity) {
                    List<StockInspectRealDTO> stockInspectRealList = stockInspectTemp.getStockInspectRealDTOs();
                    if (!DataUtil.isNullOrEmpty(stockInspectRealList)) {
                        for (StockInspectRealDTO stockInspectRealDTO : stockInspectRealList) {
                            stockInspectRealDTO.setStockInspectId(stockInspectDTO.getStockInspectId());
                            stockInspectRealDTO.setCreateDate(currentDate);
                            stockInspectRealService.save(stockInspectRealDTO);
                        }
                    }
                }
            }
            // Update all.

            boolean isUpdateAllFinish;
            if (isInspectQuantity) {
                isUpdateAllFinish = isFinishStockInspectNotStockTotal(ownerType, ownerId, null, null, null, fromDate, toDate);
                if (isUpdateAllFinish) {
                    isUpdateAllFinish = !repository.checkExistSpecNotFinish(ownerType, ownerId, null, null, null, fromDate, toDate);
                }
            } else {
                isUpdateAllFinish = isFinishStockInspect(ownerType, ownerId, null, null, null, fromDate, toDate);
            }

            if (isUpdateAllFinish) {
                repository.updateStockInspectAllFinish(ownerType, ownerId, fromDate, toDate, Const.STOCK_INSPECT.IS_FINISHED);
            } else {
                repository.updateStockInspectAllFinish(ownerType, ownerId, fromDate, toDate, Const.STOCK_INSPECT.IS_NOT_FINISH);
            }
        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("StockInspectService.checkStockInspect", ex);
            throw ex;
        }
    }

    @Override
    public List<StockInspectCheckDTO> addSerialToListManual(Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId, Long stateId,
                                                            String singleSerial, String fromSerial, String toSerial, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception {
        try {
            // Validate du lieu
            if (DataUtil.safeEqual(productOfferTypeId, Const.PRODUCT_OFFER_TYPE.CARD)) {
                singleSerial = normalSerialCard(singleSerial);
                fromSerial = normalSerialCard(fromSerial);
                toSerial = normalSerialCard(toSerial);
            }
            // Lay danh sach serial
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
            if (DataUtil.isNullObject(productOfferTypeDTO) || DataUtil.isNullOrZero(productOfferTypeDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOfferType.notFound");
            }
            String tableName = productOfferTypeDTO.getTableName();
            validateAddSerialToList(singleSerial, fromSerial, toSerial, lstSerial, tableName);
            if (!DataUtil.isNullOrEmpty(singleSerial)) {
                fromSerial = singleSerial;
                toSerial = singleSerial;
            }
            return stockTransSerialService.getListGatherSerial(productOfferTypeId, prodOfferId, ownerId, ownerType, stateId, tableName, fromSerial, toSerial);
        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("StockInspectService.checkStockInspect", ex);
            throw ex;
        }
    }

    @Override
    public void validateAddSerialToList(String singleSerial, String fromSerial, String toSerial, List<StockInspectCheckDTO> lstSerial, String tableName) throws LogicException, Exception {
        // Validate du lieu
        if (DataUtil.isNullOrEmpty(singleSerial) && DataUtil.isNullOrEmpty(fromSerial) && DataUtil.isNullOrEmpty(toSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.add.list.serial.empty");
        }
        if (!DataUtil.isNullOrEmpty(singleSerial) && (!DataUtil.isNullOrEmpty(fromSerial) || !DataUtil.isNullOrEmpty(toSerial))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.add.list.serial.input.single.from.to");
        }
        String formalRegex = getText("FORMAL_REGEX");
//        if ((!DataUtil.isNullOrEmpty(singleSerial) && !DataUtil.validateStringByPattern(singleSerial, formalRegex)) ||
//                (!DataUtil.isNullOrEmpty(fromSerial) && !DataUtil.validateStringByPattern(fromSerial, formalRegex)) ||
//                (!DataUtil.isNullOrEmpty(toSerial) && !DataUtil.validateStringByPattern(toSerial, formalRegex))) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.character.special");
//        }
        if ((!DataUtil.isNullOrEmpty(singleSerial) && singleSerial.trim().length() > 30) ||
                (!DataUtil.isNullOrEmpty(fromSerial) && fromSerial.trim().length() > 30) ||
                (!DataUtil.isNullOrEmpty(toSerial) && toSerial.trim().length() > 30)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.maxlength");
        }
        if ((!DataUtil.isNullOrEmpty(fromSerial) && DataUtil.isNullOrEmpty(toSerial)) ||
                (!DataUtil.isNullOrEmpty(toSerial) && DataUtil.isNullOrEmpty(fromSerial))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.toSerial.not.empty");
        }
        if (!DataUtil.safeEqual(tableName, Const.STOCK_TYPE.STOCK_HANDSET_NAME)
                && !DataUtil.isNullOrEmpty(singleSerial) &&
                (!DataUtil.validateStringByPattern(singleSerial, Const.REGEX.NUMBER_REGEX_INTEGER) || DataUtil.safeToLong(singleSerial) < 0)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.negative");
        }
        if (!DataUtil.isNullOrEmpty(fromSerial) &&
                (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX_INTEGER) || DataUtil.safeToLong(fromSerial) < 0)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.negative");
        }
        if (!DataUtil.isNullOrEmpty(toSerial) &&
                (!DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX_INTEGER) || DataUtil.safeToLong(toSerial) < 0)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.toSerial.negative");
        }
        if (!DataUtil.isNullOrEmpty(fromSerial) && !DataUtil.isNullOrEmpty(toSerial)
                && DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX_INTEGER) && DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX_INTEGER)
                && DataUtil.safeToLong(fromSerial.trim()) > DataUtil.safeToLong(toSerial.trim())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.less.toSerial");
        }
        if (!DataUtil.isNullOrEmpty(fromSerial) && !DataUtil.isNullOrEmpty(toSerial)) {
            Long fromSerialLong = DataUtil.safeToLong(fromSerial);
            Long toSerialLong = DataUtil.safeToLong(toSerial);
            if (toSerialLong - fromSerialLong + 1 > Const.STOCK_INSPECT.MAX_QUANTITY_STOCK_INSPECT) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.max.serial.exceed", Const.STOCK_INSPECT.MAX_QUANTITY_STOCK_INSPECT);
            }
        }
        // Check dai serial co bi trung khong.
        checkSerialExistence(fromSerial, toSerial, singleSerial, lstSerial);
    }

    @Override
    public List<StockInspectCheckDTO> addSerialToList2D(Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId, Long stateId,
                                                        String serial2D, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception {
        try {
            if (DataUtil.isNullOrEmpty(serial2D) || DataUtil.isNullOrEmpty(serial2D.trim())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.add.list.serial.2d.empty");
            }
            if (!DataUtil.isNullOrZero(prodOfferId)) {
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(prodOfferId);
                if (DataUtil.isNullObject(productOfferingDTO) || DataUtil.isNullOrZero(productOfferingDTO.getProductOfferingId())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOffering.notFound");
                }
            }
            List<String> lstSerialInput = splitSerial(serial2D);
            ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
            if (DataUtil.isNullObject(productOfferTypeDTO) || DataUtil.isNullOrZero(productOfferTypeDTO.getProductOfferTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.stock.inspect.productOfferType.notFound");
            }
            String tableName = productOfferTypeDTO.getTableName();
            validateAddSerialToList2D(lstSerialInput, lstSerial, tableName);
            return stockTransSerialService.getListSerialFromList(productOfferTypeId, prodOfferId, ownerId, ownerType, stateId, tableName, lstSerialInput);
        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("StockInspectService.checkStockInspect", ex);
            throw ex;
        }
    }

    @Override
    public void validateAddSerialToList2D(List<String> lstSerialInput, List<StockInspectCheckDTO> lstSerialTable, String tableName) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstSerialInput)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.add.list.serial.2d.empty");
        }
        for (String serial : lstSerialInput) {
//            if (!DataUtil.isNullOrEmpty(serial) && serial.trim().length() > 30) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.maxlength");
//            }
            if (!DataUtil.safeEqual(tableName, Const.STOCK_TYPE.STOCK_HANDSET_NAME)) {
                if (!DataUtil.isNullOrEmpty(serial) &&
                        (!DataUtil.validateStringByPattern(serial, Const.REGEX.NUMBER_REGEX_INTEGER) || DataUtil.safeToLong(serial) < 0)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.2d.negative");
                }
            }
            // Check dai serial co bi trung khong.
            checkSerialExistence(null, null, serial, lstSerialTable);
        }
    }

    @Override
    public List<Date> getFromDateToDateCheck(boolean isSpecConfirmQuantity) throws Exception {
        Date currentDate = getSysDate(em);
        String currentDateStr = DateUtil.date2ddMMyyyyString(currentDate);
        Date currDate = DateUtil.string2Date(currentDateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String fromDateStr = optionSetValueService.getValueByTwoCodeOption(Const.STOCK_INSPECT.CHECK_STOCK_DAY_IN_MOTH, isSpecConfirmQuantity ? Const.STOCK_INSPECT.BELLOW_QUANTITY : Const.STOCK_INSPECT.BELLOW);
        int fromValue = DataUtil.safeToInt(fromDateStr);
        String toDateStr = optionSetValueService.getValueByTwoCodeOption(Const.STOCK_INSPECT.CHECK_STOCK_DAY_IN_MOTH, isSpecConfirmQuantity ? Const.STOCK_INSPECT.ABOVE_QUANTITY : Const.STOCK_INSPECT.ABOVE);
        int toValue = DataUtil.safeToInt(toDateStr);
        Date fromDate;
        Date toDate;
        if (isSpecConfirmQuantity) {
            calendar.set(Calendar.DATE, fromValue);
            fromDate = calendar.getTime();
            calendar.set(Calendar.DATE, toValue);
            toDate = calendar.getTime();
        } else {
            if (day >= fromValue) {
                calendar.set(Calendar.DATE, fromValue);
                fromDate = calendar.getTime();
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, toValue);
//                calendar.add(Calendar.DATE, 1);
                toDate = calendar.getTime();
            } else {
                calendar.set(Calendar.DATE, toValue);
//                calendar.add(Calendar.DATE, 1);
                toDate = calendar.getTime();
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DATE, fromValue);
                fromDate = calendar.getTime();
            }
        }
        List<Date> result = Lists.newArrayList();
        result.add(fromDate);
        result.add(toDate);
        return result;
    }

    private List<StockInspectDTO> addSerialToStockInspect(StockInspectCheckDTO stockInspectCheckDTO, List<StockInspectDTO> listStock) {
        for (StockInspectDTO stockInspect : listStock) {
            if (DataUtil.safeEqual(stockInspect.getProdOfferId(), stockInspectCheckDTO.getProdOfferId())
                    && DataUtil.safeEqual(stockInspect.getStateId(), stockInspectCheckDTO.getStateId())) {
                StockInspectRealDTO stockInspectRealDTO = new StockInspectRealDTO();
                stockInspectRealDTO.setFromSerial(stockInspectCheckDTO.getFromSerial());
                stockInspectRealDTO.setToSerial(stockInspectCheckDTO.getToSerial());
                stockInspectRealDTO.setQuantity(stockInspectCheckDTO.getQuantity());
                List<StockInspectRealDTO> lstStockReal = stockInspect.getStockInspectRealDTOs();
                if (DataUtil.isNullOrEmpty(lstStockReal)) {
                    lstStockReal = Lists.newArrayList();
                }
                lstStockReal.add(stockInspectRealDTO);
                Long totalQuantity = DataUtil.safeToLong(stockInspect.getQuantityReal());
                totalQuantity += stockInspectCheckDTO.getQuantity();
                stockInspect.setQuantityReal(totalQuantity);
                stockInspect.setStockInspectRealDTOs(lstStockReal);
                return listStock;
            }
        }
        StockInspectDTO stockInspect = copyFromStockInspectCheck(stockInspectCheckDTO);
        stockInspect.setProdOfferId(stockInspectCheckDTO.getProdOfferId());
        stockInspect.setStateId(stockInspectCheckDTO.getStateId());
        stockInspect.setQuantityReal(stockInspectCheckDTO.getQuantity());
        // Luu serial
        List<StockInspectRealDTO> lstStockReal = Lists.newArrayList();
        StockInspectRealDTO stockInspectRealDTO = new StockInspectRealDTO();
        stockInspectRealDTO.setFromSerial(stockInspectCheckDTO.getFromSerial());
        stockInspectRealDTO.setToSerial(stockInspectCheckDTO.getToSerial());
        stockInspectRealDTO.setQuantity(stockInspectCheckDTO.getQuantity());
        lstStockReal.add(stockInspectRealDTO);
        stockInspect.setStockInspectRealDTOs(lstStockReal);
        listStock.add(stockInspect);
        return listStock;
    }

    private StockInspectDTO copyFromStockInspectCheck(StockInspectCheckDTO stockInspectCheckDTO) {
        StockInspectDTO stockInspectDTO = new StockInspectDTO();
        stockInspectDTO.setOwnerId(stockInspectCheckDTO.getOwnerId());
        stockInspectDTO.setOwnerType(stockInspectCheckDTO.getOwnerType());
        stockInspectDTO.setProdOfferId(stockInspectCheckDTO.getProdOfferId());
        return stockInspectDTO;
    }

    private String normalSerialCard(String serial) {
        if (!DataUtil.isNullOrEmpty(serial)) {
            while (serial.length() < 11) {
                serial = "0" + serial;
            }
        }
        return serial;
    }

    @Override
    public StockInspectExportDTO exportStockInspect(Long ownerType, Long ownerId, Long prodOfferTypeId, Long stateId, boolean isSpecConfirmQuantity) throws LogicException, Exception {
        List<Date> lstDate = getFromDateToDateCheck(false);

        //validate ngay kiem ke
        validateConfigDate(isSpecConfirmQuantity);
        return repository.exportStockInspect(lstDate.get(0), lstDate.get(1), ownerType, ownerId, prodOfferTypeId, stateId, isSpecConfirmQuantity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApproveStaffNew(Long productOfferTypeId, Long prodOfferId, Long stateId, Long ownerType, Long ownerId, StaffDTO staffDTO, StaffDTO approveStaff, boolean isSpecConfirmQuantity) throws LogicException, Exception {
        try {
            if (DataUtil.isNullObject(approveStaff) || !DataUtil.safeEqual(approveStaff.getStatus(), Const.STATUS.ACTIVE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.not.found");
            }
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
            }
            if (DataUtil.safeEqual(approveStaff.getStaffId(), staffDTO.getStaffId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.inspect.staff.approver.user.login.error");
            }
            List<Date> fromToDate = getFromDateToDateCheck(isSpecConfirmQuantity);
            Date fromDate;
            Date toDate;
            if (!DataUtil.isNullOrEmpty(fromToDate) && fromToDate.size() > 1) {
                fromDate = fromToDate.get(0);
                toDate = fromToDate.get(1);
                int update = repository.updateApproveStaffNew(ownerType, ownerId, productOfferTypeId, prodOfferId, stateId, fromDate, toDate, approveStaff);
                if (update < 1) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "message.stock.inspect.update.approve.new.fail");
                }
            }
        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    private List<String> splitSerial(String serial) {
        List<String> result = Lists.newArrayList();
//        serial = serial.replaceAll("(\\r|\\n|\\t)", "");
        // serial = serial.replaceAll("(\\r\\n\\t)", ";").replaceAll("(\\r\\n)", ";");
        serial = serial.replaceAll("(\\r\\n\\t)", ";").replaceAll("(\\r\\n)", ";");
        List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getLsOptionSetValueByCode(Const.STOCK_INSPECT.SEPARATE_CHAR_STOCK_INSPECT);
        if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
            for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                String value = optionSetValueDTO.getValue();
                if (serial.contains(value.replace("\\", ""))) {
                    String[] lstSerialSplit = serial.split("\\" + value.replace("\\", ""));
                    result.addAll(Arrays.asList(lstSerialSplit));
                    return result;
                }
            }
        }
        result.add(serial);
        return result;
    }

    private void checkSerialExistence(String fromSerial, String toSerial, String singleSerial, List<StockInspectCheckDTO> lstSerial) throws LogicException, Exception {
        String fromSerialCheck = fromSerial;
        String toSerialCheck = toSerial;
        // Neu singleSerial not null
        if (!DataUtil.isNullOrEmpty(singleSerial)) {
            fromSerialCheck = singleSerial;
            toSerialCheck = singleSerial;
        }
        if (!DataUtil.isNullOrEmpty(lstSerial)) {
            Long fromSerialCheckLong = null;
            Long toSerialCheckLong = null;
            if (DataUtil.isNumber(fromSerialCheck)) {
                fromSerialCheckLong = DataUtil.safeToLong(fromSerialCheck);
                toSerialCheckLong = DataUtil.safeToLong(toSerialCheck);
            }
            for (StockInspectCheckDTO stockSerial : lstSerial) {
                String fromSerialList = stockSerial.getFromSerial();
                String toSerialList = stockSerial.getToSerial();
                Long fromSerialLong = null;
                Long toSerialLong = null;
                if (DataUtil.isNumber(fromSerialList)) {
                    fromSerialLong = DataUtil.safeToLong(fromSerialList);
                    toSerialLong = DataUtil.safeToLong(toSerialList);
                }
                if (DataUtil.isNumber(fromSerialCheck) && DataUtil.isNumber(fromSerialList)) {
                    if (fromSerialCheckLong.compareTo(fromSerialLong) >= 0 && fromSerialCheckLong.compareTo(toSerialLong) <= 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.duplicate", fromSerialCheck, toSerialCheck, fromSerialList, toSerialList);
                    }
                    if (toSerialCheckLong.compareTo(fromSerialLong) >= 0 && toSerialCheckLong.compareTo(toSerialLong) <= 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.duplicate", fromSerialCheck, toSerialCheck, fromSerialList, toSerialList);
                    }
                    if (fromSerialCheckLong.compareTo(fromSerialLong) <= 0 && toSerialCheckLong.compareTo(toSerialLong) >= 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.duplicate", fromSerialCheck, toSerialCheck, fromSerialList, toSerialList);
                    }
                } else {
                    if (DataUtil.safeEqual(fromSerialCheck, fromSerialList)) {
                        if (DataUtil.isNullOrEmpty(singleSerial)) {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.serial.duplicate", fromSerialCheck, toSerialCheck, fromSerialList, toSerialList);
                        } else {
                            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.singleSerial.duplicate", fromSerialCheck);
                        }
                    }
                }
            }
        }
    }
}

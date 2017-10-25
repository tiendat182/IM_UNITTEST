package com.viettel.bccs.inventory.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.im1.service.StockIsdnIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.NumberActionDetailRepo;
import com.viettel.bccs.inventory.repo.StockNumberRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.web.notify.NotifyService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockNumberServiceImpl extends BaseServiceImpl implements StockNumberService {

    private final BaseMapper<StockNumber, StockNumberDTO> mapper = new BaseMapper(StockNumber.class, StockNumberDTO.class);
    private final BaseMapper<NumberActionDetail, NumberActionDetailDTO> mapperNumberActionDetail = new BaseMapper(NumberActionDetail.class, NumberActionDetailDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StockNumberRepo repository;
    @Autowired
    private AreaService areaService;
    @Autowired
    NumberActionService numberActionService;
    @Autowired
    OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopSv;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private NumberActionDetailService numberActionDetailService;
    @Autowired
    private StockIsdnIm1Service stockIsdnIm1Service;
    @Autowired
    private NumberActionDetailRepo numberActionDetailRepo;


    private ShopDTO shopDTO;
    private List<OptionSetValueDTO> listMobile = Lists.newArrayList();
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 11;

    public static final Logger logger = Logger.getLogger(StockNumberService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockNumberDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockNumberDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockNumberDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockNumberDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        BaseMessage result = new BaseMessage();
        result.setSuccess(true);
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockNumberDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        BaseMessage result = new BaseMessage();
        result.setSuccess(true);
        return result;
    }

    @WebMethod
    @Transactional(propagation = Propagation.REQUIRED)
    public NumberActionDTO insertBatch(final SearchNumberRangeDTO searchDTO) throws LogicException, Exception {
        final Long start;
        final SearchNumberRangeDTO saveObject = searchDTO;
        if (searchDTO.getServiceType() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "ws.service.type.not.null");
        }
        List<OptionSetValueDTO> listService = optionSetValueService.getByOptionSetCode("TELCO_FOR_NUMBER");
        boolean checkService = false;
        for (OptionSetValueDTO option : listService) {
            if (option.getValue().equals(searchDTO.getServiceType().trim())) {
                checkService = true;
                break;
            }
        }
        if (!checkService) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.service.type.invalid");
        }

        if (searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "")) {
            searchDTO.setPstnCode(null);
            searchDTO.setAreaCode(null);
            searchDTO.setAreaName(null);
        }

        if (DataUtil.isNullObject(searchDTO.getPstnCode()) && !searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "")) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.range.pstn.code.not.null");
        }

        if (!DataUtil.isNullObject(searchDTO.getPstnCode()) && !searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "")) {
            if (DataUtil.isNullOrEmpty(areaService.findByFilter(Lists.newArrayList(
                    new FilterRequest(Area.COLUMNS.PSTNCODE.name(), FilterRequest.Operator.EQ, searchDTO.getPstnCode().trim()),
                    new FilterRequest(Area.COLUMNS.DISTRICT.name(), FilterRequest.Operator.IS_NULL),
                    new FilterRequest(Area.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE)
            )))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.range.pstn.code.invalid");
            }
        }

        if (DataUtil.isNullOrEmpty(searchDTO.getStartRange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.range.start.range.not.null");
        }

        if (DataUtil.isNullOrEmpty(searchDTO.getEndRange())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "create.range.end.range.not.null");
        }

        try {
            start = Long.valueOf((((!DataUtil.isNullObject(searchDTO.getPstnCode()) && !searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "")) ? searchDTO.getPstnCode().trim() : "") + searchDTO.getStartRange().trim()).replace("^0+", ""));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.start.range.error");
        }
        final Long end;
        try {
            end = Long.valueOf((((!DataUtil.isNullObject(searchDTO.getPstnCode()) && !searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "")) ? searchDTO.getPstnCode().trim() : "") + searchDTO.getEndRange().trim()).replace("^0+", ""));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.end.range.error");
        }
        if (!(end >= start && start >= 0)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.error.range");
        }
        if (searchDTO.getStartRange().length() != searchDTO.getEndRange().length()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.error.range.length");
        }
        if (searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "")) {
            if ((start + "").length() < 9) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.range.start.mobile.invalid");
            }
            if ((end + "").length() > 11) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.range.end.mobile.invalid");
            }
        } else {
            if ((start + "").length() < 8) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.range.start.homephone.pstn.invalid");
            }
            if ((end + "").length() > 11) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.range.end.homephone.pstn.invalid");
            }
            if (DataUtil.isNullObject(searchDTO.getAreaCode()) || DataUtil.isNullObject(searchDTO.getAreaName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.locality.require");
            }
        }
        if (end - start + 1 > 1000000) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.number.per.range.over");
        }
        if (DataUtil.isNullObject(searchDTO.getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "owner.id.not.blank");
        }

        if (DataUtil.isNullOrEmpty(shopSv.findByFilter(Lists.newArrayList(new FilterRequest(Shop.COLUMNS.SHOPID.name(), FilterRequest.Operator.EQ, searchDTO.getOwnerId()),
                new FilterRequest(Shop.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE)
        )))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "create.range.owner.id.invalid");
        }

        if (!searchDTO.getServiceType().equals(Const.PRODUCT_OFFER_TYPE.MOBILE + "") && !DataUtil.isNullObject(searchDTO.getAreaCode())) {
            List<FilterRequest> filterRequestsArea = Lists.newArrayList();
            FilterRequest area1 = new FilterRequest(Area.COLUMNS.AREACODE.name(), FilterRequest.Operator.EQ, searchDTO.getAreaCode());
            filterRequestsArea.add(area1);
            FilterRequest area2 = new FilterRequest(Area.COLUMNS.NAME.name(), FilterRequest.Operator.EQ, searchDTO.getAreaName());
            filterRequestsArea.add(area2);
            if (DataUtil.isNullOrEmpty(areaService.findByFilter(filterRequestsArea))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.area.invalid");
            }
        }
//        if (searchDTO.getServiceType().equals("3")) {
//            if (searchDTO.getPlanningType() == null) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "ws.planning.type.not.null");
//            }
//
//            if (!listPlanningType.contains(searchDTO.getPlanningType())) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.planning.type.invalid");
//            }
//        }
        if (!numberActionService.checkOverlap(start, end, searchDTO.getServiceType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "ws.range.number.overlap");
        }

        if (DataUtil.isNullObject(searchDTO.getUserCreate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "create.range.create.user.not.null");
        }

        if (DataUtil.isNullOrEmpty(staffService.findByFilter(Lists.newArrayList(new FilterRequest(Staff.COLUMNS.STAFFCODE.name(), FilterRequest.Operator.EQ, searchDTO.getUserCreate().trim()),
                new FilterRequest(Staff.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE))))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "create.range.create.user.invalid");
        }

        NumberActionDTO logAction = new NumberActionDTO();
        logAction.setTelecomServiceId(Long.valueOf(searchDTO.getServiceType()));
        logAction.setFromIsdn(start + "");
        logAction.setToIsdn(end + "");
        logAction.setUserCreate(searchDTO.getUserCreate().trim());
        logAction.setUserIpAddress(searchDTO.getIpAddress().trim());
        logAction.setCreateDate(new Date());
        logAction.setServiceType(searchDTO.getPlanningType());
        logAction.setActionType("1");
        logAction.setStatus("0");
        final NumberActionDTO numberActionRs = numberActionService.create(logAction);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection con;
                    PreparedStatement ps;
                    PreparedStatement psUpdate;
                    con = IMCommonUtils.getDBIMConnection();
                    ps = con.prepareStatement("INSERT INTO STOCK_NUMBER (id,isdn,status,area_code,user_create,create_date,service_type,telecom_service_id,owner_id,owner_type,last_update_time,last_update_user,last_update_ip_address) values(stock_number_seq.nextval,?,'1',?,?,sysdate,?,?,?,'1',sysdate,?,?)");
                    psUpdate = con.prepareStatement("UPDATE NUMBER_ACTION SET STATUS=? WHERE NUMBER_ACTION_ID=?");
                    boolean error = false;
                    for (long i = start; i <= end; i++) {
                        ps.setString(1, i + "");
                        ps.setString(2, saveObject.getAreaCode());
                        ps.setString(3, saveObject.getUserCreate());
                        ps.setString(4, saveObject.getPlanningType());
                        ps.setLong(5, Long.valueOf(saveObject.getServiceType()));
                        ps.setLong(6, saveObject.getOwnerId());
                        ps.setString(7, saveObject.getUserCreate());
                        ps.setString(8, saveObject.getIpAddress());
                        ps.addBatch();
                        if (i % 1000 == 0) {
                            try {
                                ps.executeBatch();
                            } catch (Exception e) {
                                error = true;
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                    try {
                        ps.executeBatch();
                    } catch (Exception e) {
                        error = true;
                        logger.error(e.getMessage(), e);
                    }
                    psUpdate.setString(1, error ? "2" : "1");
                    psUpdate.setLong(2, numberActionRs.getNumberActionId());
                    psUpdate.execute();
                    try {
                        if (!error) {
                            notifyService.makePush(searchDTO.getUserCreate(), getText("mn.isdn.create"), getText("create.number.ranges.success"));
                        } else {
                            notifyService.makePush(searchDTO.getUserCreate(), getText("mn.isdn.create"), getText("create.number.ranges.fail"));
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                    psUpdate.close();
                    ps.close();
                    con.close();
                    //notify here
                } catch (Exception ex) {
                    try {
                        numberActionRs.setStatus("2");
                        numberActionService.update(numberActionRs);
                        try {
                            notifyService.makePush(searchDTO.getUserCreate(), getText("mn.isdn.create"), getText("create.number.ranges.fail"));
                        } catch (Exception exx) {
                            logger.error(exx.getMessage(), exx);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    logger.error(ex.getMessage(), ex);
                }
            }
        });
        try {
            t.start();
        } catch (Exception ex) {
            try {
                numberActionRs.setStatus("2");
                numberActionService.update(numberActionRs);
                try {
                    notifyService.makePush(searchDTO.getUserCreate(), getText("mn.isdn.create"), getText("create.number.ranges.fail"));
                } catch (Exception exx) {
                    logger.error(exx.getMessage(), exx);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            logger.error(ex.getMessage(), ex);
        }
//            StockNumber stock = new StockNumber();
//            stock.setIsdn(i + "");
//            stock.setStatus("1");
//            stock.setUserCreate(searchDTO.getUserCreate());
//            stock.setAreaCode(searchDTO.getAreaCode());
//            stock.setCreateDate(new Date());
//            stock.setServiceType(searchDTO.getPlanningType());
//            stock.setTelecomServiceId(Long.valueOf(searchDTO.getServiceType()));
//            lst.add(stock);
//            em.persist(stock);
//            if (i % 10 == 0) {
//                transaction.commit();
//                em.close();
//                transaction.begin();
//                em = emf.createEntityManager();
//            }

//        transaction.commit();
//        em.close();
        return logAction;
    }

    public boolean validateNumberAndLength(String str, int min, int max) {
        if (str == null || (str.length() < min) || str.length() > max) {
            return false;
        }
        try {
            Long.parseLong(str);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    private void checkValidate(SearchNumberRangeDTO dto) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "search.number.range.validate.null");
            }
            //require
            if (DataUtil.isNullObject(dto.getServiceType())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "search.number.range.validate.require.startRange");
            }
            if (DataUtil.isNullObject(dto.getStartRange())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "search.number.range.validate.require.endRange");
            }
            // contains in database - selectItems
            listMobile = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            if (!ValidateService.checkValueContainOptionSet(dto.getServiceType(), listMobile)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "search.number.range.validate.contains.serviceType");
            }
            //compare
            if (DataUtil.safeToLong(dto.getEndRange()).longValue() < DataUtil.safeToLong(dto.getStartRange()).longValue()) {
                throw new LogicException("", "search.number.range.start.compare.end");
            }
            if (DataUtil.safeToLong(dto.getEndRange()).longValue() - 100000L + 1L > DataUtil.safeToLong(dto.getStartRange()).longValue()) {
                throw new LogicException("", "search.number.range.start.too.large");
            }
            // length range
            if (!validateNumberAndLength(dto.getStartRange(), MIN_LENGTH, MAX_LENGTH)) {
                throw new LogicException("", "mn.stock.status.isdn.from.length");
            }
            if (!validateNumberAndLength(dto.getEndRange(), MIN_LENGTH, MAX_LENGTH)) {
                throw new LogicException("", "mn.stock.status.isdn.to.length");
            }
            if (dto.getStartRange().length() != dto.getEndRange().length()) {
                throw new LogicException("", "mn.stock.status.isdn.range.matchlength");
            }
//            if (DataUtil.isNullObject(dto.getProductNewId())) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "search.number.range.validate.require.productNewId");
//            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    @Override
    public boolean checkExistNumberByListFilterRuleId(List<Long> listFilterRuleId) throws Exception {
        List<FilterRequest> lsRequest = Lists.newArrayList(new FilterRequest(StockNumber.COLUMNS.FILTERRULEIDLIST.name(), FilterRequest.Operator.IN, listFilterRuleId));
        return findByFilter(lsRequest).size() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRuleForRefilter(BigDecimal startNumber, BigDecimal endNumber, List<Long> lstSelectedRule) throws Exception {
        List<FilterRequest> lstReques = Lists.newArrayList();
        lstReques.add(new FilterRequest(StockNumber.COLUMNS.ISDN.name(), FilterRequest.Operator.GOE, startNumber));
        lstReques.add(new FilterRequest(StockNumber.COLUMNS.ISDN.name(), FilterRequest.Operator.LOE, endNumber));
        lstReques.add(new FilterRequest(StockNumber.COLUMNS.FILTERRULEID.name(), FilterRequest.Operator.IN, lstSelectedRule));
        List<StockNumberDTO> lstStockNumber = findByFilter(lstReques);
        for (StockNumberDTO dto : lstStockNumber) {
            dto.setFilterRuleId(null);
            dto.setLastUpdateTime(optionSetValueService.getSysdateFromDB(false));
        }
        return repository.save(mapper.toPersistenceBean(lstStockNumber)).size();
    }

    @Override
    public List<StockNumberDTO> getListNumberFilter(Long telecomServiceId, BigDecimal startNumber, BigDecimal endNumber, Long minNumber, Long ownerId, List<String> lstStatus) throws Exception {
        return repository.getListNumberFilter(telecomServiceId, startNumber, endNumber, minNumber, ownerId, lstStatus);
    }

    //chungnv len danh sach cap nhat trang thai theo dai
    @Override
    public List<TableNumberRangeDTO> getListRangeGrant(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        return repository.getListRangeGrant(searchNumberRangeDTO);
    }

    //chungnv len danh sach cap nhat trang thai tu file
    @Override
    public List<TableNumberRangeDTO> getListRangeGrantFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        return repository.getListRangeGrantFromFile(searchNumberRangeDTO);
    }

    //chungnv cap nhat trang thai cho so
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateRangeGrant(List<TableNumberRangeDTO> listRangeUpdate, StaffDTO staff, String ip, SearchNumberRangeDTO searchNumberRangeDTO) throws LogicException, Exception {
        return repository.updateRangeGrant(listRangeUpdate, staff, ip, searchNumberRangeDTO);
    }

    @WebMethod
    public List<String> checkListStockNumber(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception {
        return repository.checkListStockNumber(searchNumberRangeDTO);
    }

    @Override
    public List<TableNumberRangeDTO> previewFromFile(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception, LogicException {
        return repository.previewFromFile(searchNumberRangeDTO);
    }

    @Override
    public List<TableNumberRangeDTO> previewFromRange(SearchNumberRangeDTO searchNumberRangeDTO) throws Exception, LogicException {
        checkValidate(searchNumberRangeDTO);
        return repository.previewFromRange(searchNumberRangeDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long updateStockNumberByProOfferId(List<TableNumberRangeDTO> listRangeUpdate, String staffCode, String ip, ProductOfferingTotalDTO productOfferingTotalDTO, boolean isFromFile) throws LogicException, Exception {
        return repository.updateStockNumberByProOfferId(listRangeUpdate, staffCode, ip, getSysDate(em), productOfferingTotalDTO, isFromFile);
    }

    @Override
    public List<Object[]> previewDistriButeNumber(InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return repository.previewDistriButeNumber(inputByRange, requiredRoleMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String distriButeNumber(List<Object[]> listRangePreviewed, InputDistributeByRangeDTO inputByRange, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        try {
            return repository.distriButeNumber(listRangePreviewed, inputByRange, requiredRoleMap);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }
//
//    //    @Override
//    public HashMap<String, String> previewDistriButeNumberByFile(String telecomServiceId, byte[] byteContent, String fromOwnerId, String fromOwnerType, String stockType, String fromOwnerPath, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
//        return repository.previewDistriButeNumberByFile(telecomServiceId, byteContent, fromOwnerId, fromOwnerType, stockType, fromOwnerPath, requiredRoleMap);
//    }
//
//    //    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public HashMap<String, String> distriButeNumberByFile(String telecomServiceId, byte[] byteContent, String stockType, Long reasonId, String notes, String userCreate, String userIpCreate, String fromOwnerId, String fromOwnerType, String fromOwnerCode, String fromOwnerPath) throws LogicException, Exception {
//        return repository.distriButeNumberByFile(telecomServiceId, byteContent, stockType, reasonId, notes, userCreate, userIpCreate, fromOwnerId, fromOwnerType, fromOwnerCode, fromOwnerPath);
//    }

    @Override
    public List<DeleteNumberDTO> previewDeleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO deleteStockNumberDTO) throws LogicException, Exception {
        return repository.previewDeleteNumber(lstDelete, deleteStockNumberDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DeleteNumberDTO> deleteNumber(List<DeleteNumberDTO> lstDelete, DeleteNumberDTO dto, String user, String ip) throws LogicException, Exception {
        return repository.deleteNumber(lstDelete, dto, user, ip);
    }

    @Override
    public List<TransferNGNDTO> previewTransfer(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto) throws LogicException, Exception {
        return repository.previewTransfer(lstDelete, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TransferNGNDTO> transferNGN(List<TransferNGNDTO> lstDelete, TransferNGNDTO dto, String userCreate, String userIpCreate) throws LogicException, Exception {
        return repository.transferNGN(lstDelete, dto, userCreate, userIpCreate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StockTransSerialDTO> findRangeForExportNote(String fromIsdn, String toIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        return repository.findRangeForExportNote(fromIsdn, toIsdn, ownerType, ownerId, prodOfferId);
    }

    @Override
    public HashMap<String, String> checkIsdnBeforeDistribute(List<String[]> listIsdn, String currentOwnerId, String currentShopPath, String telecomServiceId, RequiredRoleMap requiredRoleMap) {
        return repository.checkIsdnBeforeDistribute(listIsdn, currentOwnerId, currentShopPath, telecomServiceId, requiredRoleMap);
    }

    @Override
    public List<Object[]> previewDistriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return repository.previewDistriButeNumberByFile(listIsdn, inputByFile, requiredRoleMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, String> distriButeNumberByFile(List<Object[]> listIsdn, InputDistributeByFileDTO inputByFile, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        return repository.distriButeNumberByFile(listIsdn, inputByFile, requiredRoleMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StockTransSerialDTO> findByListIsdn(List<String> lstIsdn, String ownerType, Long ownerId, Long prodOfferId) throws LogicException, Exception {
        return repository.findByListIsdn(lstIsdn, ownerType, ownerId, prodOfferId);
    }

    @Override
    public List<IsdnViewDetailDTO> searchIsdn(InfoSearchIsdnDTO infoSearch, int rowNum, int firstRow) throws LogicException, Exception {
        return repository.searchIsdn(infoSearch, rowNum, firstRow);
    }

    @Override
    public List<InfoStockIsdnDto> viewInfoStockIsdn(Long ownerId, Long ownerType) throws Exception {
        return repository.viewInfoStockIsdn(ownerId, ownerType);
    }

    public int revokeNumber(List<Long> lstRevokeShopId, Long day, List<Long> lstRevokeShopReuseId, String updateUser, String updateIp) throws LogicException, Exception {
        Long result;
        List<Long> lstNumberId = repository.getListNumberFromRevokeShop(lstRevokeShopId, day);
        if (!DataUtil.isNullOrEmpty(lstNumberId)) {
            try {
                Connection conn = IMCommonUtils.getDBIMConnection();
                conn.setAutoCommit(false);
                result = repository.updateNumberForReuse(lstNumberId, lstRevokeShopReuseId, updateUser, updateIp, conn);
                conn.commit();
                conn.close();
                return result.intValue();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return 0;
    }

    public int revokeNumberOwner(Long shopId, Long day, Long revokeShopId, String updateUser) throws LogicException, Exception {
        Long result;
        List<Long> lstNumberId = repository.getListNumberOwnerTDN(shopId, day);
        if (!DataUtil.isNullOrEmpty(lstNumberId)) {
            try {
                Connection conn = IMCommonUtils.getDBIMConnection();
                conn.setAutoCommit(false);
                result = repository.updateNumberRevokeOwner(lstNumberId, revokeShopId, updateUser, conn);
                conn.commit();
                conn.close();
                return result.intValue();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int cancelRequestVTShop() throws LogicException, Exception {
        List<StockIsdnVtShopDTO> lstIsdnExpriedVtShop = repository.getLstIsdnExpriedVtShop();
        List<StockIsdnVtShopDTO> lstRequest = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lstIsdnExpriedVtShop)) {
            for (StockIsdnVtShopDTO stockIsdnVTShopDTO : lstIsdnExpriedVtShop) {
                if (DataUtil.isNullOrZero(stockIsdnVTShopDTO.getRequestId())) {
                    lstRequest.add(stockIsdnVTShopDTO);
                }
            }
            repository.updateIsdnExpriedVtShop(lstIsdnExpriedVtShop);
            repository.unlockIsdnExpriedVtShop(lstRequest);
            // Thuc hien luu tren DB IM1
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                    && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                repository.unlockIsdnExpriedVtShopIM1(lstRequest);
            }
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void releaseNumber(StockNumberDTO stockNumberDTO) throws Exception {
        //cap nhat stock_number
        Long lockStaffUser = stockNumberDTO.getLockByStaff();

        List<StockIsdnVtShopDTO> lsVTStockIsdn = DataUtil.isNullOrEmpty(stockNumberDTO.getLastUpdateUser())
                ? Lists.newArrayList(new StockIsdnVtShopDTO(stockNumberDTO.getIsdn()))
                : Lists.newArrayList(new StockIsdnVtShopDTO(stockNumberDTO.getIsdn(), stockNumberDTO.getLastUpdateUser()));

        repository.unlockIsdnExpriedVtShop(lsVTStockIsdn);
        //cap nhat stock_isdn_mobile
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            Long result = repository.unlockIsdnExpriedVtShopIM1(lsVTStockIsdn);
            if (result.intValue() == 0) {
                throw new LogicException("", "hotline1.update.isdn.fail");
            }
        }
        // them moi number_action
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        numberActionDTO.setTelecomServiceId(1L);
        numberActionDTO.setActionType("9");
        numberActionDTO.setNote(getText("common.unlock.isdn"));
        numberActionDTO.setFromIsdn(stockNumberDTO.getIsdn());
        numberActionDTO.setToIsdn(stockNumberDTO.getIsdn());
        numberActionDTO.setUserCreate(DataUtil.isNullOrEmpty(stockNumberDTO.getLastUpdateUser()) ? "SYS" : stockNumberDTO.getLastUpdateUser());
        numberActionDTO.setCreateDate(getSysDate(em));
        numberActionDTO.setUserIpAddress(stockNumberDTO.getLastUpdateIpAddress());
        NumberActionDTO numberActionSave = numberActionService.create(numberActionDTO);
        //Them moi number_action_detail
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        numberActionDetailDTO.setNumberActionId(numberActionSave.getNumberActionId());
        numberActionDetailDTO.setColumnName("STATUS");
        numberActionDetailDTO.setOldValue(Const.STOCK_NUMBER_STATUS.LOCK);
        numberActionDetailDTO.setNewValue(Const.STOCK_NUMBER_STATUS.NEW);
        numberActionDetailService.create(numberActionDetailDTO);
        numberActionDetailDTO.setColumnName("LOCK_BY_STAFF");
        numberActionDetailDTO.setOldValue(DataUtil.safeToString(lockStaffUser));
        numberActionDetailDTO.setNewValue(null);
        numberActionDetailService.create(numberActionDetailDTO);
    }

    public List<StockNumberDTO> getAllListReleaseNumber(Double keepTimeHour, List<Long> lstShopId) throws Exception {
        return repository.getAllReleaseNumber(keepTimeHour, lstShopId);
    }

    public List<Long> getLstShopIdFromOptionSet(String code) throws Exception {
        return repository.getLstShopIdFromOptionSet(code);
    }

    @Override
    public Long countLockIsdnByStaffId(Long staffId, List<Long> lstShopId) throws Exception {
        return repository.countLockIsdnByStaffId(staffId, lstShopId);
    }

    @Transactional(rollbackFor = Exception.class)
    public String unlockIsdnByStaff(Long staffId, String isdn, String ipAddress) throws LogicException, Exception {
        StaffDTO staffDTO = staffService.findOne(staffId);
        if (DataUtil.isNullObject(staffDTO) || Const.STATUS.NO_ACTIVE.equals(staffDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
        }
        String isdnStr = DataUtil.safeToString(isdn).trim();
        // check dinh dang so thue bao, do dai phai 8 - 11
        if (!DataUtil.isNumber(isdnStr) || !validateNumberAndLength(isdnStr, MIN_LENGTH, MAX_LENGTH)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.file.isdn.number");
        }

        StockNumber stockNumber = findStockNumberByIsdn(isdnStr);
        if (stockNumber == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.notfound", isdnStr);
        }
        //chi check voi so thue bao la mobile
        if (!Const.STOCK_TYPE.STOCK_ISDN_MOBILE.equals(stockNumber.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.mobile");

        }
        //thue bao o trang thai so moi, or so ngung su dung
        if (!Const.ISDN.LOCK.equals(stockNumber.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.unlock.isdn.option.invalid.status.isdn");
        }
        if (!staffDTO.getStaffId().equals(stockNumber.getLockByStaff())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.number.isdn.unlock.isdn.invalid.staff.login", staffDTO.getStaffCode(), isdnStr);
        }
        StockNumberDTO stockNumberDTO = new StockNumberDTO();
        BeanUtils.copyProperties(stockNumber, stockNumberDTO);
        stockNumberDTO.setLastUpdateUser(staffDTO.getStaffCode());
        stockNumberDTO.setLastUpdateIpAddress(ipAddress);
        releaseNumber(stockNumberDTO);
        return isdnStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String lockIsdnByStaff(Long staffId, String isdn, String ipAddress, RequiredRoleMap requiredRoleMap) throws LogicException, Exception {
        StaffDTO staffDTO = staffService.findOne(staffId);
        boolean roleLockNumber = (requiredRoleMap == null || !requiredRoleMap.hasRole(Const.PERMISION.BCCS2_IM_QLSO_TRACUUSO_GIUSO)) ? false : true;
        if (DataUtil.isNullObject(staffDTO) || Const.STATUS.NO_ACTIVE.equals(staffDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
        }
        String isdnStr = DataUtil.safeToString(isdn).trim();
        // check dinh dang so thue bao, do dai phai 8 - 11
        if (!DataUtil.isNumber(isdnStr) || !validateNumberAndLength(isdnStr, MIN_LENGTH, MAX_LENGTH)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.file.isdn.number");
        }

        //Kho cau hinh lock so
        List<Long> lsIdStock = getLsIdKeepper(Const.OPTION_SET.STOCK_LOCK_NUMBER);
        List<Long> lsIdNormalStock = getLsCommonStock(Const.OPTION_SET.STOCK_LOCK_NORMAL_NUMBER);
        List<Long> lsIdTotalStock = Lists.newArrayList();
        lsIdTotalStock.addAll(lsIdStock);
        lsIdTotalStock.addAll(lsIdNormalStock);

        String strStockLock = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_NUMBER, Const.OPTION_SET.STOCK_LOCK_NUMBER);
        String strStockNormalLock = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_NORMAL_NUMBER, Const.OPTION_SET.STOCK_LOCK_NORMAL_NUMBER);
        String strStockTotalLock = strStockLock;
        if (!DataUtil.isNullOrEmpty(strStockNormalLock)) {
            strStockTotalLock += "," + strStockNormalLock;
        }

        //so luong isdn cau hinh lock so
        String totalConfigISDN = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.QUANTITY_LOCK_NUMBER, Const.OPTION_SET.QUANTITY_LOCK_NUMBER);
        String totalConfigNormalISDN = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.QUANTITY_LOCK_NORMAL_NUMBER, Const.OPTION_SET.QUANTITY_LOCK_NORMAL_NUMBER);

        //check phan quyen
        if ((requiredRoleMap == null || !requiredRoleMap.hasRole(Const.PERMISION.BCCS2_IM_QLSO_TRACUUSO_GIUSO)) && DataUtil.isNullOrEmpty(lsIdNormalStock)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.number.isdn.lock.not.role");
        }
        //tim so thue bao
        StockNumber stockNumberDTO = repository.findStockNumberByIsdn(isdnStr);
        if (stockNumberDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.notfound", isdnStr);
        }
        //chi check voi so thue bao la mobile
        if (!Const.STOCK_TYPE.STOCK_ISDN_MOBILE.equals(stockNumberDTO.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.mobile");

        }
        //thue bao o trang thai so moi, or so ngung su dung
        if (!(Const.ISDN.NEW_NUMBER.equals(stockNumberDTO.getStatus()) || Const.ISDN.STOP_USE.equals(stockNumberDTO.getStatus()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.option.invalid.status.isdn");
        }

        //so ko nam trong kho chung thi bao loi
        if (!lsIdTotalStock.contains(stockNumberDTO.getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.status", strStockTotalLock);
        }

        //so nam trong kho TD2016 va khong co quyen
        if (lsIdStock.contains(stockNumberDTO.getOwnerId()) && !roleLockNumber) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.notRole", strStockLock);
        }

        //Validate so luong so TD2016 da lock
        if (!DataUtil.isNullOrEmpty(lsIdStock) && lsIdStock.contains(stockNumberDTO.getOwnerId())) {
            Long totalISDNStaff = repository.countLockIsdnByStaffId(staffId, lsIdStock);

            if (totalISDNStaff.compareTo(DataUtil.safeToLong(totalConfigISDN)) >= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.nice.isdn.option.over.config.isdn", staffDTO.getStaffCode(), totalConfigISDN, strStockLock);
            }
        }

        //Validate so luong so thuong da lock
        if (!DataUtil.isNullOrEmpty(lsIdNormalStock) && lsIdNormalStock.contains(stockNumberDTO.getOwnerId())) {
            Long totalISDNStaff = repository.countLockIsdnByStaffId(staffId, lsIdNormalStock);

            if (totalISDNStaff.compareTo(DataUtil.safeToLong(totalConfigNormalISDN)) >= 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.nice.isdn.option.over.config.isdn", staffDTO.getStaffCode(), totalConfigNormalISDN, strStockNormalLock);
            }
        }

        //Kiem tra nhan vien da giu so nay lan nao chua, neu da giu --> Thong bao loi
        if (repository.checkIsdnHaveLockedByStaffId(isdnStr, staffDTO.getStaffCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.have.locked.isdn", staffDTO.getStaffCode(), isdnStr);
        }

        Date sysDate = getSysDate(em);

        //cap nhap stock_number
        String oldStatus = stockNumberDTO.getStatus();
        Long oldLockStockStaff = stockNumberDTO.getLockByStaff();

        stockNumberDTO.setStatus(Const.ISDN.LOCK);
        stockNumberDTO.setLockByStaff(staffId);
        stockNumberDTO.setLastUpdateTime(sysDate);
        stockNumberDTO.setLastUpdateUser(staffDTO.getStaffCode());
        stockNumberDTO.setLastUpdateIpAddress(ipAddress);
        repository.save(stockNumberDTO);
        //cap nhap bccs1 : stock_isdn_mobile
        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }
        if (isCheckIm1) {
            List<String> lst = Lists.newArrayList(Const.ISDN.NEW_NUMBER, Const.ISDN.STOP_USE);
            List<Long> lsOldStatus = lst.stream().map(x -> DataUtil.safeToLong(x)).collect(Collectors.toList());
            stockIsdnIm1Service.lockOrUnlockIsdn(isdnStr, DataUtil.safeToLong(Const.ISDN.LOCK), lsOldStatus, staffDTO);
        }
        //ghi log giu so
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        numberActionDTO.setFromIsdn(isdnStr);
        numberActionDTO.setToIsdn(isdnStr);
        numberActionDTO.setActionType(Const.NUMBER_ACTION_TYPE.KEEP_ISDN);
        numberActionDTO.setTelecomServiceId(Const.STOCK_TYPE.STOCK_ISDN_MOBILE);
        numberActionDTO.setNote(getText("common.keep.isdn"));
        numberActionDTO.setUserCreate(staffDTO.getStaffCode());
        numberActionDTO.setUserIpAddress(ipAddress);
        numberActionDTO.setCreateDate(sysDate);
        NumberActionDTO numberActionDTOSave = numberActionService.create(numberActionDTO);
        //cap nhap log detail
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        numberActionDetailDTO.setNumberActionId(numberActionDTOSave.getNumberActionId());
        numberActionDetailDTO.setColumnName("STATUS");
        numberActionDetailDTO.setOldValue(oldStatus);
        numberActionDetailDTO.setNewValue(stockNumberDTO.getStatus());
        numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(numberActionDetailDTO));
        //cap nhap log detail
        numberActionDetailDTO = new NumberActionDetailDTO();
        numberActionDetailDTO.setNumberActionId(numberActionDTOSave.getNumberActionId());
        numberActionDetailDTO.setColumnName("LOCK_BY_STAFF");
        numberActionDetailDTO.setOldValue(DataUtil.safeToString(oldLockStockStaff));
        numberActionDetailDTO.setNewValue(DataUtil.safeToString(stockNumberDTO.getLockByStaff()));
        numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(numberActionDetailDTO));

        //STOCK_ISDN_MOBILE
        return isdnStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String lockNiceIsdnByStaff(Long staffId, String isdn, String ipAddress) throws LogicException, Exception {
        StaffDTO staffDTO = staffService.findOne(staffId);
        if (DataUtil.isNullObject(staffDTO) || Const.STATUS.NO_ACTIVE.equals(staffDTO.getStatus())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid.name");
        }
        String isdnStr = DataUtil.safeToString(isdn).trim();
        // check dinh dang so thue bao, do dai phai 8 - 11
        if (!DataUtil.isNumber(isdnStr) || !validateNumberAndLength(isdnStr, MIN_LENGTH, MAX_LENGTH)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.file.isdn.number");
        }
        //tim so thue bao
        StockNumber stockNumberDTO = repository.findStockNumberByIsdn(isdnStr);
        if (stockNumberDTO == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.notfound", isdnStr);
        }
        //chi check voi so thue bao la mobile
        if (!Const.STOCK_TYPE.STOCK_ISDN_MOBILE.equals(stockNumberDTO.getTelecomServiceId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.mobile");

        }
        //thue bao o trang thai so moi, or so ngung su dung
        if (!(Const.ISDN.NEW_NUMBER.equals(stockNumberDTO.getStatus()) || Const.ISDN.STOP_USE.equals(stockNumberDTO.getStatus()))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.option.invalid.status.isdn");
        }
        //tim danh so kho chung cau cau hinh 10 so dep
        List<Long> lsIdShopKeeper10 = getLsIdKeepper(Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_10);
        List<Long> lsIdShopKeeper11 = getLsIdKeepper(Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_11);

        //so ko nam trong kho chung 10 or 11 so thi bao loi
        if (!(lsIdShopKeeper10.contains(stockNumberDTO.getOwnerId()) || lsIdShopKeeper11.contains(stockNumberDTO.getOwnerId()))) {
            String strStock = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_10, Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_10) + ","
                    + optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_11, Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_11);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.status", strStock);
        }

        String typeConfig = lsIdShopKeeper10.contains(stockNumberDTO.getOwnerId()) ? "1" : (lsIdShopKeeper11.contains(stockNumberDTO.getOwnerId()) ? "2" : "");

        // lay so luong trong bang config so dep
        Long configNiceNumber = repository.getConfigQuantityNiceNumberByStaff(staffDTO.getStaffCode(), typeConfig);
        if (DataUtil.safeEqual(-1L, configNiceNumber)) {
            String strconfigNiceNumber = optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_QUANTITY_NICE_NUMBER, Const.OPTION_SET.STOCK_LOCK_QUANTITY_NICE_NUMBER);
            if (DataUtil.isNullOrEmpty(strconfigNiceNumber)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.nice.isdn.option.shopid.notfound");
            }
            configNiceNumber = DataUtil.safeToLong(strconfigNiceNumber);
        }
        Long totalISDNStaff = repository.countLockIsdnByStaffId(staffId, "1".equals(typeConfig) ? lsIdShopKeeper10 : lsIdShopKeeper11);
        if (totalISDNStaff.compareTo(DataUtil.safeToLong(configNiceNumber)) >= 0) {
            String strStock = "1".equals(typeConfig) ? optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_10, Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_10) :
                    optionSetValueService.getValueByTwoCodeOption(Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_11, Const.OPTION_SET.STOCK_LOCK_NICE_NUMBER_11);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.nice.isdn.option.over.config.isdn", staffDTO.getStaffCode(), totalISDNStaff, strStock);
        }

        //Kiem tra nhan vien da giu so nay lan nao chua, neu da giu --> Thong bao loi
        if (repository.checkIsdnHaveLockedByStaffId(isdnStr, staffDTO.getStaffCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.have.locked.isdn", staffDTO.getStaffCode(), isdnStr);
        }


        Date sysDate = getSysDate(em);

        //cap nhap stock_number
        String oldStatus = stockNumberDTO.getStatus();
        Long oldLockStockStaff = stockNumberDTO.getLockByStaff();

        stockNumberDTO.setStatus(Const.ISDN.LOCK);
        stockNumberDTO.setLockByStaff(staffId);
        stockNumberDTO.setLastUpdateTime(sysDate);
        stockNumberDTO.setLastUpdateUser(staffDTO.getStaffCode());
        stockNumberDTO.setLastUpdateIpAddress(ipAddress);
        repository.save(stockNumberDTO);
        //cap nhap bccs1 : stock_isdn_mobile
        boolean isCheckIm1 = false;
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())) {
            isCheckIm1 = Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()));
        }
        if (isCheckIm1) {
            List<Long> lsOldStatus = Lists.newArrayList(Const.ISDN.NEW_NUMBER, Const.ISDN.STOP_USE).stream().map(x -> DataUtil.safeToLong(x)).collect(Collectors.toList());
            stockIsdnIm1Service.lockOrUnlockIsdn(isdnStr, DataUtil.safeToLong(Const.ISDN.LOCK), lsOldStatus, staffDTO);
        }
        //ghi log giu so
        NumberActionDTO numberActionDTO = new NumberActionDTO();
        numberActionDTO.setFromIsdn(isdnStr);
        numberActionDTO.setToIsdn(isdnStr);
        numberActionDTO.setActionType(Const.NUMBER_ACTION_TYPE.KEEP_ISDN);
        numberActionDTO.setTelecomServiceId(Const.STOCK_TYPE.STOCK_ISDN_MOBILE);
        numberActionDTO.setNote(getText("common.keep.nice.isdn"));
        numberActionDTO.setUserCreate(staffDTO.getStaffCode());
        numberActionDTO.setUserIpAddress(ipAddress);
        numberActionDTO.setCreateDate(sysDate);
        NumberActionDTO numberActionDTOSave = numberActionService.create(numberActionDTO);
        //cap nhap log detail
        NumberActionDetailDTO numberActionDetailDTO = new NumberActionDetailDTO();
        numberActionDetailDTO.setNumberActionId(numberActionDTOSave.getNumberActionId());
        numberActionDetailDTO.setColumnName("STATUS");
        numberActionDetailDTO.setOldValue(oldStatus);
        numberActionDetailDTO.setNewValue(stockNumberDTO.getStatus());
        numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(numberActionDetailDTO));
        //cap nhap log detail
        numberActionDetailDTO = new NumberActionDetailDTO();
        numberActionDetailDTO.setNumberActionId(numberActionDTOSave.getNumberActionId());
        numberActionDetailDTO.setColumnName("LOCK_BY_STAFF");
        numberActionDetailDTO.setOldValue(DataUtil.safeToString(oldLockStockStaff));
        numberActionDetailDTO.setNewValue(DataUtil.safeToString(stockNumberDTO.getLockByStaff()));
        numberActionDetailRepo.save(mapperNumberActionDetail.toPersistenceBean(numberActionDetailDTO));

        //STOCK_ISDN_MOBILE
        return isdnStr;
    }

    private List<Long> getLsIdKeepper(String optionCode) throws LogicException, Exception {
        String strShopIdKeeper11 = optionSetValueService.getValueByTwoCodeOption(optionCode, optionCode);
        if (DataUtil.isNullOrEmpty(strShopIdKeeper11)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.option.shopid.notfound");
        }
        List<String> lsIdShopShopCode11 = Arrays.asList(strShopIdKeeper11.split(","));
        if (DataUtil.isNullOrEmpty(lsIdShopShopCode11)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.number.isdn.lock.isdn.option.shopid.notfound");
        }
        List<Long> lsIdShopKeeper11 = Lists.newArrayList();//getShopByShopCode
        for (String s : lsIdShopShopCode11) {
            ShopDTO shopDTO = shopSv.getShopByShopCode(s);
            if (shopDTO == null || !Const.STATUS.ACTIVE.equals(shopDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "sell.store.no.info", s);
            }
            lsIdShopKeeper11.add(shopDTO.getShopId());
        }
        return lsIdShopKeeper11;
    }

    private List<Long> getLsCommonStock(String optionCode) throws LogicException, Exception {
        List<Long> lsIdShopKeeper = Lists.newArrayList();
        String strShopIdKeeper = optionSetValueService.getValueByTwoCodeOption(optionCode, optionCode);
        if (!DataUtil.isNullOrEmpty(strShopIdKeeper)) {
            List<String> lsIdShopShopCode = Arrays.asList(strShopIdKeeper.split(","));
            for (String s : lsIdShopShopCode) {
                ShopDTO shopDTO = shopSv.getShopByShopCode(s);
                if (shopDTO == null || !Const.STATUS.ACTIVE.equals(shopDTO.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "sell.store.no.info", s);
                }
                lsIdShopKeeper.add(shopDTO.getShopId());
            }
        }
        return lsIdShopKeeper;
    }

    @Override
    public StockNumber findStockNumberByIsdn(String isdn) throws Exception {
        return repository.findStockNumberByIsdn(isdn);
    }
}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.googlecode.ehcache.annotations.Cacheable;
import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.repo.StockTotalAuditIm1Repo;
import com.viettel.bccs.im1.repo.StockTotalIm1Repo;
import com.viettel.bccs.im1.model.StockTotalIm1;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.bccs.inventory.model.StockTotalAudit;
import com.viettel.bccs.inventory.repo.InventoryWsRepo;
import com.viettel.bccs.inventory.repo.StockTotalAuditRepo;
import com.viettel.bccs.inventory.repo.StockTotalRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockTotalServiceImpl extends BaseServiceImpl implements StockTotalService {

    private final BaseMapper<StockTotal, StockTotalDTO> mapper = new BaseMapper(StockTotal.class, StockTotalDTO.class);
    private final BaseMapper<StockTotalAudit, StockTotalAuditDTO> mapperStockTotalAudit = new BaseMapper(StockTotalAudit.class, StockTotalAuditDTO.class);

    @Autowired
    private StockTotalRepo repository;
    @Autowired
    private InventoryWsRepo repositoryInventory;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StockTotalAuditService stockTotalAuditService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ActionLogService actionLogService;
    @Autowired
    private StockTotalIm1Service stockTotalIm1Service;

    public static final Logger logger = Logger.getLogger(StockTotalService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTotalDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTotalDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTotalDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod

    public StockTotalDTO save(StockTotalDTO stockTotalDTO) throws Exception {
        stockTotalDTO.setModifiedDate(getSysDate(em));
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTotalDTO)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTotalDTO saveForProcessStockSerial(StockTotalDTO stockTotalDTO, ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception {
        try {
            stockTotalDTO.setModifiedDate(getSysDate(em));
            StockTotalDTO result = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTotalDTO)));

            //save log action
            String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}", result.getOwnerType(), result.getOwnerId(), result.getStateId(), result.getProdOfferId());
            dto.setDescription(getTextParam("stock.process.description", params));
            actionLogService.saveForProcessStock(dto, lstDetail);
            return result;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTotalMessage saveForProcessStockNoSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
                                                         Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                                         Long reasonId, Long transId, Date transDate,
                                                         String transCode, String transType, Const.SourceType sourceType, ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception {
        try {
            StockTotalMessage stockTotalMessage = changeStockTotal(ownerId, ownerType, prodOfferId, stateId,
                    qty, qtyIssue, qtyHang, userId, reasonId, transId, transDate, transCode, transType, sourceType);
            //save log action
            StockTotalDTO stockTotalDTO = stockTotalMessage.getStockTotalDTO();
            lstDetail.get(0).setNewValue(DataUtil.safeToString(stockTotalDTO.getCurrentQuantity()));
            lstDetail.get(1).setNewValue(DataUtil.safeToString(stockTotalDTO.getAvailableQuantity()));
            actionLogService.saveForProcessStock(dto, lstDetail);
            return stockTotalMessage;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Override
    public List<StockTotalDTO> searchStockTotal(StockTotalDTO stockTotalDTO) throws LogicException, Exception {
        if (stockTotalDTO != null) {
            List<FilterRequest> lsRequests = Lists.newArrayList();

            if (!DataUtil.isNullOrZero(stockTotalDTO.getOwnerId())) {
                lsRequests.add(new FilterRequest(StockTotal.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, stockTotalDTO.getOwnerId()));
            }
            if (!DataUtil.isNullOrZero(stockTotalDTO.getOwnerType())) {
                lsRequests.add(new FilterRequest(StockTotal.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, stockTotalDTO.getOwnerType()));
            }

            if (!DataUtil.isNullOrZero(stockTotalDTO.getProdOfferId())) {
                lsRequests.add(new FilterRequest(StockTotal.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, stockTotalDTO.getProdOfferId()));
            }

            if (!DataUtil.isNullOrZero(stockTotalDTO.getStateId())) {
                lsRequests.add(new FilterRequest(StockTotal.COLUMNS.STATEID.name(), FilterRequest.Operator.EQ, stockTotalDTO.getStateId()));
            }

            if (!DataUtil.isNullOrZero(stockTotalDTO.getStatus())) {
                lsRequests.add(new FilterRequest(StockTotal.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, stockTotalDTO.getStatus()));
            }

            return mapper.toDtoBean(repository.findAll(repository.toPredicate(lsRequests)));
        }
        return new ArrayList<StockTotalDTO>();
    }

    @Override
    public Long getTotalValueStock(Long ownerId, String ownerType) throws LogicException, Exception {
        return repository.getTotalValueStock(ownerId, ownerType);
    }

    @Override
    @WebMethod
    public Long checkAction(StockTotalDTO stockTotalDTO) throws Exception, LogicException {
        return repository.checkAction(stockTotalDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTotalMessage changeStockTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                              Long reasonId, Long transId, Date transDate,
                                              String transCode, String transType, Const.SourceType sourceType) throws Exception {

        StockTotalMessage msg = changeStockTotalIm2(
                ownerId,
                ownerType,
                prodOfferId,
                stateId,
                qty,
                qtyIssue,
                qtyHang,
                userId,
                reasonId,
                transId,
                transDate,
                transCode,
                transType,
                sourceType);
        if (!msg.isSuccess()) return msg;

        //luu thay doi BCCS1.0
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            msg = stockTotalIm1Service.changeStockTotal(
                    ownerId,
                    ownerType,
                    prodOfferId,
                    stateId,
                    qty,
                    qtyIssue,
                    qtyHang,
                    userId,
                    reasonId,
                    transId,
                    transDate,
                    transCode,
                    transType,
                    sourceType);
        }

        return msg;
    }

    private StockTotalMessage changeStockTotalIm2(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
                                                  Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                                  Long reasonId, Long transId, Date transDate,
                                                  String transCode, String transType, Const.SourceType sourceType) throws Exception {
        String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}", DataUtil.safeToString(ownerId), DataUtil.safeToString(ownerType), stateId, DataUtil.safeToString(prodOfferId));
        Long tQty = DataUtil.safeToLong(qty, 0L);
        Long tQtyIssue = DataUtil.safeToLong(qtyIssue, 0L);
        Long tQtyHang = DataUtil.safeToLong(qtyHang, 0L);

        StockTotalMessage result = new StockTotalMessage();
        StockTotalDTO stockTotal = null;

        try {
            StockTotalDTO stockTotalSearch = new StockTotalDTO();
            stockTotalSearch.setOwnerId(ownerId);
            stockTotalSearch.setOwnerType(ownerType);
            stockTotalSearch.setProdOfferId(prodOfferId);
            stockTotalSearch.setStateId(stateId);
            stockTotalSearch.setStatus(Const.NEW);
            List<StockTotalDTO> lsStockTotalDto = repository.findOneStockTotal(stockTotalSearch);
            stockTotal = !DataUtil.isNullOrEmpty(lsStockTotalDto) ? lsStockTotalDto.get(0) : null;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setKeyMsg("sell.store.busy");
            return result;
        }

        if (stockTotal == null && (tQty < 0L || tQtyIssue < 0L || tQtyHang < 0L)) {
            result.setKeyMsg("sell.store.no.info", params);
            return result;
        }

        if (tQty == 0L && tQtyIssue == 0L && tQtyHang == 0L) {
            result.setStockTotalDTO(stockTotal);
            result.setSuccess(true);
            return result;
        }

        //Ghi log audit
        StockTotalAudit sta = new StockTotalAudit();
        sta.setCreateDate(transDate);
        sta.setOwnerId(ownerId);
        sta.setOwnerType(ownerType);
        sta.setUserId(userId);
        sta.setReasonId(reasonId);
        sta.setTransId(transId);
        sta.setTransCode(transCode);
        sta.setTransType(Long.valueOf(transType));
        sta.setSourceType(sourceType.getValue());
        sta.setProdOfferId(prodOfferId);
        sta.setStateId(stateId);
        sta.setCurrentQuantity(tQty);
        sta.setAvailableQuantity(tQtyIssue);
        sta.setHangQuantity(tQtyHang);

        if (stockTotal == null) {
            stockTotal = new StockTotalDTO();
            stockTotal.setOwnerType(ownerType);
            stockTotal.setOwnerId(ownerId);
            stockTotal.setStateId(stateId);
            stockTotal.setProdOfferId(prodOfferId);
            stockTotal.setModifiedDate(transDate);
            stockTotal.setCurrentQuantity(qty);
            stockTotal.setAvailableQuantity(qtyIssue);
            stockTotal.setHangQuantity(qtyHang);
            stockTotal.setStatus(1L);
            repository.save(mapper.toPersistenceBean(stockTotal));
            sta.setCurrentQuantityBf(0L);
            sta.setAvailableQuantityBf(0L);
            sta.setHangQuantityBf(0L);
        } else {
            stockTotal.setCurrentQuantity(DataUtil.safeToLong(stockTotal.getCurrentQuantity(), 0L));
            stockTotal.setAvailableQuantity(DataUtil.safeToLong(stockTotal.getAvailableQuantity(), 0L));
            stockTotal.setHangQuantity(DataUtil.safeToLong(stockTotal.getHangQuantity(), 0L));

            if (stockTotal.getCurrentQuantity() + tQty < 0L) {
                result.setKeyMsg("sell.store.good.not.enough");
                return result;
            }
            if (stockTotal.getAvailableQuantity() + tQtyIssue < 0) {
                result.setKeyMsg("sell.store.good.avail.not.enough");
                return result;
            }

            sta.setCurrentQuantityBf(stockTotal.getCurrentQuantity());
            sta.setAvailableQuantityBf(stockTotal.getAvailableQuantity());
            sta.setHangQuantityBf(stockTotal.getHangQuantity());
            //Cap nhat lai StockTotal
            stockTotal.setCurrentQuantity(stockTotal.getCurrentQuantity() + tQty);
            stockTotal.setAvailableQuantity(stockTotal.getAvailableQuantity() + tQtyIssue);
            stockTotal.setHangQuantity(stockTotal.getHangQuantity() + tQtyHang);
            stockTotal.setModifiedDate(transDate);
            repository.save(mapper.toPersistenceBean(stockTotal));
        }

        //Ghi log audit sau khi giam tru so luong
        sta.setCurrentQuantityAf(stockTotal.getCurrentQuantity());
        sta.setAvailableQuantityAf(stockTotal.getAvailableQuantity());
        sta.setHangQuantityAf(stockTotal.getHangQuantity());
        stockTotalAuditService.save(mapperStockTotalAudit.toDtoBean(sta));
        result.setStockTotalDTO(stockTotal);
        result.setSuccess(true);
        return result;
    }

//    private StockTotalMessage changeStockTotalIm1(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
//                                                  Long qty, Long qtyIssue, Long qtyHang, Long userId,
//                                                  Long reasonId, Long transId, Date transDate,
//                                                  String transCode, String transType, Const.SourceType sourceType) throws Exception {
//        String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}", DataUtil.safeToString(ownerId), DataUtil.safeToString(ownerType), stateId, DataUtil.safeToString(prodOfferId));
//        Long tQty = DataUtil.safeToLong(qty, 0L);
//        Long tQtyIssue = DataUtil.safeToLong(qtyIssue, 0L);
//        Long tQtyHang = DataUtil.safeToLong(qtyHang, 0L);
//
//        StockTotalMessage result = new StockTotalMessage();
//        StockTotalIm1DTO stockTotalIm1 = null;
//
//        try {
//            StockTotalIm1DTO stockTotalIm1Search = new StockTotalIm1DTO();
//            stockTotalIm1Search.setOwnerId(ownerId);
//            stockTotalIm1Search.setOwnerType(ownerType);
//            stockTotalIm1Search.setStockModelId(prodOfferId);
//            stockTotalIm1Search.setStateId(stateId);
//            stockTotalIm1Search.setStatus(Const.NEW);
//            List<StockTotalIm1DTO> lsStockTotalIm1 = stockTotalIm1Repo.findOneStockTotal(stockTotalIm1Search);
//            stockTotalIm1 = !DataUtil.isNullOrEmpty(lsStockTotalIm1) ? lsStockTotalIm1.get(0) : null;
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            result.setErrorCode("ES102100105");
//            result.setKeyMsg("sell.store.busy");
//            return result;
//        }
//
//        if (stockTotalIm1 == null && (tQty < 0L || tQtyIssue < 0L || tQtyHang < 0L)) {
//            result.setKeyMsg("sell.store.no.info", params);
//            return result;
//        }
//
//        if (tQty == 0L && tQtyIssue == 0L && tQtyHang == 0L) {
//            result.setSuccess(true);
//            return result;
//        }
//
//        //Ghi log audit
//        StockTotalAuditIm1DTO sta = new StockTotalAuditIm1DTO();
//        sta.setCreateDate(transDate);
//        sta.setOwnerId(ownerId);
//        sta.setOwnerType(ownerType);
//        sta.setUserId(userId);
//        sta.setReasonId(reasonId);
//        sta.setTransId(transId);
//        sta.setTransCode(transCode);
//        sta.setTransType(Long.valueOf(transType));
//        sta.setSourceType(sourceType.getValue());
//        sta.setStockModelId(prodOfferId);
//        sta.setStateId(stateId);
//        sta.setQty(tQty);
//        sta.setQtyIssue(tQtyIssue);
//        sta.setQtyHang(tQtyHang);
//
//        if (stockTotalIm1 == null) {
//            stockTotalIm1 = new StockTotalIm1DTO();
//            stockTotalIm1.setOwnerType(ownerType);
//            stockTotalIm1.setOwnerId(ownerId);
//            stockTotalIm1.setStateId(stateId);
//            stockTotalIm1.setStockModelId(prodOfferId);
//            stockTotalIm1.setModifiedDate(transDate);
//            stockTotalIm1.setQuantity(qty);
//            stockTotalIm1.setQuantityIssue(qtyIssue);
//            stockTotalIm1.setQuantityHang(qtyHang);
//            stockTotalIm1.setStatus(1L);
//            stockTotalIm1Repo.createStockTotal(stockTotalIm1);
//            sta.setQtyBf(0L);
//            sta.setQtyIssueBf(0L);
//            sta.setQtyHangBf(0L);
//        } else {
//            stockTotalIm1.setQuantity(DataUtil.safeToLong(stockTotalIm1.getQuantity(), 0L));
//            stockTotalIm1.setQuantityIssue(DataUtil.safeToLong(stockTotalIm1.getQuantityIssue(), 0L));
//            stockTotalIm1.setQuantityHang(DataUtil.safeToLong(stockTotalIm1.getQuantityHang(), 0L));
//
//            if (stockTotalIm1.getQuantity() + tQty < 0L) {
//                result.setKeyMsg("sell.store.good.not.enough");
//                return result;
//            }
//            if (stockTotalIm1.getQuantityIssue() + tQtyIssue < 0) {
//                result.setKeyMsg("sell.store.good.avail.not.enough");
//                return result;
//            }
//
//            sta.setQtyBf(stockTotalIm1.getQuantity());
//            sta.setQtyIssueBf(stockTotalIm1.getQuantityIssue());
//            sta.setQtyHangBf(stockTotalIm1.getQuantityHang());
//            //Cap nhat lai StockTotal
//            stockTotalIm1.setQuantity(stockTotalIm1.getQuantity() + tQty);
//            stockTotalIm1.setQuantityIssue(stockTotalIm1.getQuantityIssue() + tQtyIssue);
//            stockTotalIm1.setQuantityHang(stockTotalIm1.getQuantityHang() + tQtyHang);
//            stockTotalIm1.setModifiedDate(transDate);
//
//            stockTotalIm1Repo.updateStockTotal(stockTotalIm1);
//        }
//
//        //Ghi log audit sau khi giam tru so luong
//        sta.setQtyAf(stockTotalIm1.getQuantity());
//        sta.setQtyIssueAf(stockTotalIm1.getQuantityIssue());
//        sta.setQtyHangAf(stockTotalIm1.getQuantityHang());
//        stockTotalAuditIm1Repo.createStockTotalAudit(sta);
//
//        result.setSuccess(true);
//        return result;
//    }

    @Override
    public List<StockTotalWsDTO> getStockTotalDetail(Long ownerId, Long ownerType, String prodOfferName) throws Exception {
        List<StockTotalWsDTO> stockTotalDTOList = Lists.newArrayList();
        try {
            if (DataUtil.isNullOrZero(ownerId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.lookup.invalid.ownerId");
            }
            //set listDetailSerial
            List<StockTransSerialDTO> lstSerial;
            int maxValue = 100;
            int tmp = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.MAX_ROW_SERIAL));
            maxValue = tmp == 0 ? maxValue : tmp;
            stockTotalDTOList = repositoryInventory.getStockTotalShop(ownerId, ownerType, null, prodOfferName);

            List<OptionSetValueDTO> lsOptionState = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.GOODS_STATE);
            Map<String, String> mapState = lsOptionState.stream().collect(Collectors.toMap(OptionSetValueDTO::getValue, OptionSetValueDTO::getName));
            Long index = 1L;
            for (StockTotalWsDTO stockTotal : stockTotalDTOList) {
                stockTotal.setIndex(index);
                stockTotal.setStateName(DataUtil.safeToString(mapState.get(DataUtil.safeToString(stockTotal.getStateId()))));
                lstSerial = repositoryInventory.getStockTransSerial(stockTotal, maxValue);
                stockTotal.setLstSerial(lstSerial);
                stockTotal.setTotalSerial(getTotalSerial(lstSerial));
                index++;
            }
        } catch (Exception ex) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "Have some error!", ex);
        }
        return stockTotalDTOList;
    }

    private Long getTotalSerial(List<StockTransSerialDTO> lstSerial) {
        Long totalSerial = 0L;
        if (!DataUtil.isNullOrEmpty(lstSerial)) {
            for (StockTransSerialDTO serialDTO : lstSerial) {
                if (!DataUtil.isNullObject(serialDTO.getQuantity())) {
                    totalSerial += serialDTO.getQuantity();
                }
            }
        }

        return totalSerial;
    }

    @Override
//    @Cacheable(cacheName = "stockTotalServiceImpl.getStockTotalForProcessStock")
    public StockTotalDTO getStockTotalForProcessStock(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockTotal.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, ownerId));
        lst.add(new FilterRequest(StockTotal.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, ownerType));
        lst.add(new FilterRequest(StockTotal.COLUMNS.PRODOFFERID.name(), FilterRequest.Operator.EQ, prodOfferId));
        lst.add(new FilterRequest(StockTotal.COLUMNS.STATEID.name(), FilterRequest.Operator.EQ, stateId));
        lst.add(new FilterRequest(StockTotal.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<StockTotalDTO> lstResult = findByFilter(lst);
        if (!DataUtil.isNullObject(lstResult) && lstResult.size() > 0) {
            return lstResult.get(0);
        }
        return null;
    }

    @Override
    public List<StockTotalDTO> getStockTotalAvailable(Long ownerType, Long productOfferTypeId) throws Exception {
        List<StockTotalDTO> result = Lists.newArrayList();
        if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF_LONG)) {
            result = repository.getStockTotalAvailableStaff(productOfferTypeId);
        } else if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.SHOP_LONG)) {
            result = repository.getStockTotalAvailableShop(productOfferTypeId);
        } else {
            result = repository.getStockTotalAvailable(productOfferTypeId);
        }
        return result;
    }

    @Override
    public List<Long> getTotalQuantityStockTotal(Long prodOfferId, Long ownerId) throws Exception {
        return repository.getTotalQuantityStockTotal(prodOfferId, ownerId);
    }

    @Override
    @WebMethod
    public boolean checkStockTotal(Long ownerId, Long prodOfferId, Long qty) throws Exception {
        StockTotalDTO stockTotalDTO = getStockTotalForProcessStock(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_EXPIRE);
        if (stockTotalDTO == null) {
            return false;
        }
        if (qty > stockTotalDTO.getAvailableQuantity() || qty > stockTotalDTO.getCurrentQuantity()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkStockTotalIm1(Long ownerId, Long prodOfferId, Long qty) throws Exception {
        StockTotalIm1DTO stockTotalDTO = getStockTotalForProcessStockIm1(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_EXPIRE);
        if (stockTotalDTO == null) {
            return false;
        }
        if (qty > stockTotalDTO.getQuantity() || qty > stockTotalDTO.getQuantityIssue()) {
            return false;
        }
        return true;
    }

    @Override
    @WebMethod
    public StockTotalIm1DTO getStockTotalForProcessStockIm1(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        return stockTotalIm1Service.getStockTotalForProcessStock(ownerId, ownerType, prodOfferId, stateId);
    }

    public boolean checkHaveProductInStockTotal(Long ownerId, Long ownerType) throws Exception {
        return repository.checkHaveProductInStockTotal(ownerId, ownerType);
    }

}

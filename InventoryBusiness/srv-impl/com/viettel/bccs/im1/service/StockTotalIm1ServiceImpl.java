package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.*;
import com.viettel.bccs.im1.model.StockTotalIm1;
import com.viettel.bccs.im1.repo.StockTotalIm1Repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTotalMessage;
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
import java.util.Date;
import java.util.List;

@Service
public class StockTotalIm1ServiceImpl extends BaseServiceImpl implements StockTotalIm1Service {

    private final BaseMapper<StockTotalIm1, StockTotalIm1DTO> mapper = new BaseMapper<>(StockTotalIm1.class, StockTotalIm1DTO.class);

    @Autowired
    private StockTotalIm1Repo repository;

    @Autowired
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(StockTotalIm1Service.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public List<StockTotalIm1DTO> findStockTotal(StockTotalIm1DTO stockTotalDTO) throws Exception {
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        try {
            if (DataUtil.isNullObject(stockTotalDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.lookup.invalid.ownerId");
            }
            stockTotalIm1DTOList = repository.findOneStockTotal(stockTotalDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "basestock1.validate.invalidate.stocktotal.updating");
        }
        return stockTotalIm1DTOList;
    }

    @WebMethod
    public List<StockTotalIm1DTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTotalIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    public int create(StockTotalIm1DTO dto) throws Exception {
        return repository.createStockTotal(dto);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public int update(StockTotalIm1DTO dto) throws Exception {
        return repository.updateStockTotal(dto);
    }

    @WebMethod
    public StockTotalIm1DTO save(StockTotalIm1DTO stockTotalDTO) throws Exception {

        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTotalDTO)));
    }

    @WebMethod
    public List<StockTransDetailIm1DTO> findStockTransDetail(Long stockTransId) throws LogicException, Exception {
        List<StockTransDetailIm1DTO> listStockTransDetail = null;
        try {
            if (DataUtil.isNullOrZero(stockTransId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.lookup.invalid.ownerId");
            }
            listStockTransDetail = repository.findStockTransDetail(stockTransId);
        } catch (Exception ex) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "Have some error!", ex);
        }
        return listStockTransDetail;
    }

    @WebMethod
    public StockModelIm1DTO findOneStockModel(Long stockModelId) throws LogicException, Exception {
        StockModelIm1DTO stockModelIm1DTO = null;
        try {
            if (DataUtil.isNullOrZero(stockModelId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.lookup.invalid.ownerId");
            }
            stockModelIm1DTO = repository.findOneStockModel(stockModelId);
        } catch (Exception ex) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "Have some error!", ex);
        }
        return stockModelIm1DTO;
    }

    @WebMethod
    public StockTypeIm1DTO findOneStockType(Long stockModelId) throws LogicException, Exception {
        StockTypeIm1DTO stockTypeIm1DTO = null;
        try {
            if (DataUtil.isNullOrZero(stockModelId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.lookup.invalid.ownerId");
            }
            stockTypeIm1DTO = repository.findOneStockType(stockModelId);
        } catch (Exception ex) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "Have some error!", ex);
        }
        return stockTypeIm1DTO;
    }

    @WebMethod
    public List<StockTransSerialIm1DTO> findSerialByStockTransDetail(StockTransDetailIm1DTO stockTransDetail) throws LogicException, Exception {
        List<StockTransSerialIm1DTO> listStockTransSerial = null;
        try {
            if (DataUtil.isNullObject(stockTransDetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.lookup.invalid.ownerId");
            }
            listStockTransSerial = repository.findSerialByStockTransDetail(stockTransDetail);
        } catch (Exception ex) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "Have some error!", ex);
        }
        return listStockTransSerial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTotalMessage changeStockTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
                                              Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                              Long reasonId, Long transId, Date transDate,
                                              String transCode, String transType, Const.SourceType sourceType) throws Exception {
        String params = MessageFormat.format("ownerId:{0}, ownerType:{1}, stateId:{2}, prodOfferId:{3}", DataUtil.safeToString(ownerId), DataUtil.safeToString(ownerType), stateId, DataUtil.safeToString(prodOfferId));
        Long tQty = DataUtil.safeToLong(qty, 0L);
        Long tQtyIssue = DataUtil.safeToLong(qtyIssue, 0L);
        Long tQtyHang = DataUtil.safeToLong(qtyHang, 0L);

        StockTotalMessage result = new StockTotalMessage();
        StockTotalIm1DTO stockTotal = null;
        try {
            StockTotalIm1DTO stockTotalDTOFilter = new StockTotalIm1DTO();
            stockTotalDTOFilter.setOwnerId(ownerId);
            stockTotalDTOFilter.setOwnerType(ownerType);
            stockTotalDTOFilter.setStockModelId(prodOfferId);
            stockTotalDTOFilter.setStateId(stateId);
            stockTotalDTOFilter.setStatus(DataUtil.safeToLong(Const.STATUS.ACTIVE));
            List<StockTotalIm1DTO> lsStockTotalDto = findStockTotal(stockTotalDTOFilter);

            stockTotal = !DataUtil.isNullOrEmpty(lsStockTotalDto) ? lsStockTotalDto.get(0) : null;
            // xu ly lock doi tuong stock_total
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setKeyMsg("sell.store.busy.bccs1");
            return result;
        }

        if (stockTotal == null && (tQty < 0L || tQtyIssue < 0L || tQtyHang < 0L)) {
            result.setKeyMsg("sell.store.no.info.bccs1", params);
            return result;
        }

        if (tQty == 0L && tQtyIssue == 0L && tQtyHang == 0L) {
            result.setStockTotalIm1DTO(stockTotal);
            result.setSuccess(true);
            return result;
        }

        //Ghi log audit
        StockTotalAuditIm1DTO sta = new StockTotalAuditIm1DTO();
        sta.setCreateDate(transDate);
        sta.setOwnerId(ownerId);
        sta.setOwnerType(ownerType);
        sta.setUserId(userId);
        sta.setReasonId(reasonId);
        sta.setTransId(transId);
        sta.setTransCode(transCode);
        sta.setTransType(Long.valueOf(transType));
        sta.setSourceType(sourceType.getValue());
        sta.setStockModelId(prodOfferId);
        sta.setStateId(stateId);
        sta.setQty(tQty);
        sta.setQtyIssue(tQtyIssue);
        sta.setQtyHang(tQtyHang);
        sta.setReasonId(0L);

        if (stockTotal == null) {
            stockTotal = new StockTotalIm1DTO();
            stockTotal.setOwnerType(ownerType);
            stockTotal.setOwnerId(ownerId);
            stockTotal.setStateId(stateId);
            stockTotal.setStockModelId(prodOfferId);
            stockTotal.setModifiedDate(transDate);
            stockTotal.setQuantity(qty);
            stockTotal.setQuantityIssue(qtyIssue);
            stockTotal.setQuantityHang(qtyHang);
            stockTotal.setStatus(1L);
            create(stockTotal);

            sta.setQtyBf(0L);
            sta.setQtyIssueBf(0L);
            sta.setQtyHangBf(0L);
        } else {
            stockTotal.setQuantity(DataUtil.safeToLong(stockTotal.getQuantity(), 0L));
            stockTotal.setQuantityIssue(DataUtil.safeToLong(stockTotal.getQuantityIssue(), 0L));
            stockTotal.setQuantityHang(DataUtil.safeToLong(stockTotal.getQuantityHang(), 0L));

            if (stockTotal.getQuantity() + tQty < 0L) {
                result.setKeyMsg("sell.store.good.not.enough.bccs1");
                return result;
            }
            if (stockTotal.getQuantityIssue() + tQtyIssue < 0) {
                result.setKeyMsg("sell.store.good.avail.not.enough.bccs1");
                return result;
            }

            sta.setQtyBf(stockTotal.getQuantity());
            sta.setQtyIssueBf(stockTotal.getQuantityIssue());
            sta.setQtyHangBf(stockTotal.getQuantityHang());
            //Cap nhat lai StockTotal
            stockTotal.setQuantity(stockTotal.getQuantity() + tQty);
            stockTotal.setQuantityIssue(stockTotal.getQuantityIssue() + tQtyIssue);
            stockTotal.setQuantityHang(stockTotal.getQuantityHang() + tQtyHang);
            stockTotal.setModifiedDate(transDate);

            update(stockTotal);
        }

        //Ghi log audit sau khi giam tru so luong
        sta.setQtyAf(stockTotal.getQuantity());
        sta.setQtyIssueAf(stockTotal.getQuantityIssue());
        sta.setQtyHangAf(stockTotal.getQuantityHang());

        stockTotalAuditIm1Service.create(sta);

        result.setStockTotalIm1DTO(stockTotal);
        result.setSuccess(true);
        return result;
    }

    @Override
    @WebMethod
    public StockTotalIm1DTO getStockTotalForProcessStock(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception {
        List<StockTotalIm1DTO> stockTotalIm1DTOList = Lists.newArrayList();
        StockTotalIm1DTO stockTotalDTO = new StockTotalIm1DTO();
        stockTotalDTO.setOwnerId(ownerId);
        stockTotalDTO.setOwnerType(ownerType);
        stockTotalDTO.setStockModelId(prodOfferId);
        stockTotalDTO.setStateId(stateId);
        stockTotalDTO.setStatus(Long.valueOf(Const.STATUS_ACTIVE));
        try {
            stockTotalIm1DTOList = repository.findOneStockTotal(stockTotalDTO);
            if (!DataUtil.isNullOrEmpty(stockTotalIm1DTOList))
                return stockTotalIm1DTOList.get(0);
        } catch (Exception ex) {
        }
        return null;
    }

}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTotalCycle;
import com.viettel.bccs.inventory.repo.StockTotalCycleRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
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
import java.util.Date;
import java.util.List;

@Service
public class StockTotalCycleServiceImpl extends BaseServiceImpl implements StockTotalCycleService {

    private final BaseMapper<StockTotalCycle, StockTotalCycleDTO> mapper = new BaseMapper<>(StockTotalCycle.class, StockTotalCycleDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTotalCycleRepo repository;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private ThreadCloseStockService threadCloseStockService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;

    public static final Logger logger = Logger.getLogger(StockTotalCycleService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTotalCycleDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTotalCycleDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTotalCycleDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTotalCycleDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTotalCycleDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<StockTotalCycleReportDTO> getStockCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        return repository.getStockCycle(fromDate, toDate, stateId, productOfferTypeId, prodOfferId, ownerType, ownerId);
    }

    @Override
    public List<StockTotalCycleDTO> getStockCycleExport(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception {
        return repository.getStockCycleExport(fromDate, toDate, stateId, productOfferTypeId, prodOfferId, ownerType, ownerId);
    }

    @Override
    public List<String> getListSerialStockXByDayRemain(Long prodOfferId, Long ownerId, Long ownerType, Long typeCycle, String tableName, Long dayStockRemain, Long stateId) throws Exception {
        return repository.getListSerialStockXByDayRemain(prodOfferId, ownerId, ownerType, typeCycle, tableName, dayStockRemain, stateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doStockTotalCycle(Logger logger, Long ownerType, Long productOfferTypeId, ThreadCloseStockDTO threadCloseStock) throws Exception, LogicException {
        Date startDate = getSysDate(em);
        Date currentDate = getTruncSysdate(em);
        threadCloseStock.setStartActionTime(startDate);
        Connection conn = null;

        // Xu ly theo tung mat hang
        try {
            conn = IMCommonUtils.getDBIMConnection();
            conn.setAutoCommit(false);
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET, productOfferTypeId) || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_ACCESSORIES, productOfferTypeId)) {
                List<StockTotalCycleDTO> stockTotalCycleDTOs = repository.getStockTotalCycleHandset(productOfferTypeId);
                int countNumber = insertStockTotalCycle(productOfferTypeId, currentDate, stockTotalCycleDTOs, conn);
                logger.error("-----Xu ly cho kho ownerType = " + ownerType + ", So ban ghi: " + countNumber);

                //insert chi tiet serial
                repository.insertSerialCycle(stockTotalCycleDTOs, getTableName(productOfferTypeId), currentDate, conn);

            } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID, productOfferTypeId)
                    || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT, productOfferTypeId)
                    || DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD, productOfferTypeId)) {

                List<StockTotalCycleDTO> stockTotalCycleDTOs = repository.getStockTotalCycleSolr(null, 210L, getCoreSolr(productOfferTypeId));
//                    logger.warn("-----Tong ton kho ownerType = " + ownerType + ", ownerId = " + stockTotalDTO.getOwnerId() + ", So mat hang: " + stockTotalCycleDTOs.size());
                int countNumber = insertStockTotalCycle(productOfferTypeId, currentDate, stockTotalCycleDTOs, conn);
//                    logger.warn("-----Xu ly cho kho ownerType = " + ownerType + ", ownerId = " + stockTotalDTO.getOwnerId() + ", So ban ghi: " + countNumber);

                //Insert chi tiet Serial (Khong phai the cao)
                if (!DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD, productOfferTypeId)) {
                    repository.insertSerialCycle(stockTotalCycleDTOs, getTableName(productOfferTypeId), currentDate, conn);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        // Cap nhat thoi gian xu ly cua tien trinh
        Date endDate = getSysDate(em);
        threadCloseStock.setProcessDate(endDate);
        threadCloseStock.setStatus(0L);
        threadCloseStock.setEndActionTime(endDate);
    }

    private int insertStockTotalCycle(Long productOfferTypeId, Date currentDate, List<StockTotalCycleDTO> stockTotalCycleDTOs, Connection conn) throws Exception {
        int result = 0;
        if (!DataUtil.isNullOrEmpty(stockTotalCycleDTOs)) {
            result = repository.insertStockTotalCycle(productOfferTypeId, currentDate, stockTotalCycleDTOs, conn);
        }
        return result;
    }

    private String getTableName(Long productOfferTypeId) throws Exception {
        ProductOfferTypeDTO productOfferTypeDTO = productOfferTypeService.findOne(productOfferTypeId);
        if (!DataUtil.isNullObject(productOfferTypeDTO)) {
            return productOfferTypeDTO.getTableName();
        }
        return null;
    }

    @Override
    public List<StockTotalCycleDTO> findStockOver(Long ownerId, Long ownerType) throws Exception {
        return repository.findStockOver(ownerId, ownerType);
    }

    @Override
    public List<StockTotalCycleDTO> findStockOverByStateId(Long ownerId, Long ownerType) throws Exception {
        return repository.findStockOverByStateId(ownerId, ownerType);
    }

    @Override
    public List<StockTotalCycleDTO> findStockOverByTypeId(Long ownerId, Long ownerType) throws Exception {
        return repository.findStockOverByTypeId(ownerId, ownerType);
    }

    private Const.SOLR_CORE getCoreSolr(Long productOfferTypeId) throws Exception {
        if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_SIM_PRE_PAID, productOfferTypeId)) {
            return Const.SOLR_CORE.DB_SIM;
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_KIT, productOfferTypeId)) {
            return Const.SOLR_CORE.DB_KIT;
        } else if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_CARD, productOfferTypeId)) {
            return Const.SOLR_CORE.DB_CARD;
        }

        return null;
    }
}

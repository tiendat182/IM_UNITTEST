package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockCycleDTO;
import com.viettel.bccs.inventory.dto.StockTotalCycleDTO;
import com.viettel.bccs.inventory.dto.StockTotalCycleReportDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.mysema.query.types.Predicate;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.model.StockTotalCycle;

public interface StockTotalCycleRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<StockTotalCycleReportDTO> getStockCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception;

    /**
     * ham tim kiem danh sach hang ton kho
     *
     * @param fromDate
     * @param toDate
     * @param stateId
     * @param productOfferTypeId
     * @param prodOfferId
     * @param ownerType
     * @param ownerId
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<StockTotalCycleDTO> getStockCycleExport(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId,
                                                        Long prodOfferId, Long ownerType, Long ownerId) throws Exception;

    /**
     * ham tra ve danh sach serial ton trong kho tuonng ung
     *
     * @param prodOfferId
     * @param ownerId
     * @param ownerType
     * @param typeCycle
     * @param tableName
     * @param dayStockRemain
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    public List<String> getListSerialStockXByDayRemain(Long prodOfferId, Long ownerId, Long ownerType, Long typeCycle,
                                                       String tableName, Long dayStockRemain, Long stateId) throws Exception;

    public List<StockTotalCycleDTO> getStockTotalCycleHandset(Long ownerType, Long prodOfferId, Long dayStockRemain) throws Exception;

    public List<StockTotalCycleDTO> getStockTotalCycleHandset(Long productOfferTypeId) throws Exception;

    public List<StockTotalCycleDTO> getStockTotalCycle(Long ownerType, Long ownerId, Long prodOfferId, Long dayStockRemain, String tableName, Long dayStockCycle) throws Exception;

    public int insertStockTotalCycle(Long productOfferTypeId, Date currentDate, List<StockTotalCycleDTO> stockTotalCycleDTOs, Connection conn) throws Exception;

    public List<StockTotalCycleDTO> findStockOver(Long ownerId, Long ownerType) throws Exception;

    public List<StockTotalCycleDTO> findStockOverByStateId(Long ownerId, Long ownerType) throws Exception;

    public List<StockTotalCycleDTO> findStockOverByTypeId(Long ownerId, Long ownerType) throws Exception;

    public List<StockTotalCycleDTO> getStockTotalCycleSolr(Long prodOfferId, Long dayStockRemain, Const.SOLR_CORE core) throws Exception;

    public List<StockCycleDTO> getSerialDB(Long ownerType, Long ownerId, Long prodOfferId, Long stateId, String tableName, Long dayStockRemain) throws Exception;

    public void insertSerialCycle(List<StockTotalCycleDTO> lstStockTotalCycle, String tableName, Date currentDate, Connection conn) throws Exception;
}

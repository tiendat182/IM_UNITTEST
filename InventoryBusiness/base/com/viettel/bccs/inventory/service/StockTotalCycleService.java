package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTotalCycleDTO;
import com.viettel.bccs.inventory.dto.StockTotalCycleReportDTO;
import com.viettel.bccs.inventory.dto.ThreadCloseStockDTO;
import com.viettel.bccs.inventory.model.StockTotalCycle;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface StockTotalCycleService {

    @WebMethod
    public StockTotalCycleDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTotalCycleDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTotalCycleDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTotalCycleDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTotalCycleDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public List<StockTotalCycleReportDTO> getStockCycle(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId, Long prodOfferId, Long ownerType, Long ownerId) throws Exception;

    /**
     * ham tim kiem danh sach hang ton kho
     * @author thanhnt77
     * @param fromDate
     * @param toDate
     * @param stateId
     * @param productOfferTypeId
     * @param prodOfferId
     * @param ownerType
     * @param ownerId
     * @return
     * @throws Exception
     */
    public List<StockTotalCycleDTO> getStockCycleExport(Date fromDate, Date toDate, Long stateId, Long productOfferTypeId,
                                                        Long prodOfferId, Long ownerType, Long ownerId) throws Exception;

    /**
     * ham tra ve danh sach serial ton trong kho tuonng ung
     * @author thanhnt77
     * @param prodOfferId
     * @param ownerId
     * @param ownerType
     * @param typeCycle
     * @param tableName
     * @param dayStockRemain
     * @return
     * @throws Exception
     */
    public List<String> getListSerialStockXByDayRemain(Long prodOfferId, Long ownerId, Long ownerType, Long typeCycle, String tableName, Long dayStockRemain, Long stateId) throws Exception;

    public void doStockTotalCycle(Logger logger, Long ownerType, Long productOfferTypeId, ThreadCloseStockDTO threadCloseStockDTO) throws Exception;

    public List<StockTotalCycleDTO> findStockOver(Long ownerId, Long ownerType) throws Exception;

    public List<StockTotalCycleDTO> findStockOverByStateId(Long ownerId, Long ownerType) throws Exception;

    public List<StockTotalCycleDTO> findStockOverByTypeId(Long ownerId, Long ownerType) throws Exception;
}
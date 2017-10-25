package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.*;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTotalMessage;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

public interface StockTotalIm1Service {

    @WebMethod
    public List<StockTotalIm1DTO> findStockTotal(StockTotalIm1DTO stockTotalDTO) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTotalIm1DTO> findAll() throws Exception;

    @WebMethod
    public List<StockTotalIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception;
    @WebMethod
    public int create(StockTotalIm1DTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public int update(StockTotalIm1DTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalIm1DTO save(StockTotalIm1DTO stockTotalDTO) throws Exception;

    @WebMethod
    public List<StockTransDetailIm1DTO> findStockTransDetail(Long stockTransId) throws LogicException,Exception;

    @WebMethod
    public StockModelIm1DTO findOneStockModel(Long stockModelId) throws LogicException,Exception;

    @WebMethod
    public StockTypeIm1DTO findOneStockType(Long stockModelId) throws LogicException,Exception;

    @WebMethod
    public List<StockTransSerialIm1DTO> findSerialByStockTransDetail(StockTransDetailIm1DTO stockTransDetail) throws LogicException,Exception;

    public StockTotalMessage changeStockTotal(Long ownerId, Long ownerType, Long prodOfferId, Long stateId,
                                              Long qty, Long qtyIssue, Long qtyHang, Long userId,
                                              Long reasonId, Long transId, Date transDate,
                                              String transCode, String transType, Const.SourceType sourceType) throws LogicException, Exception;

    @WebMethod
    public StockTotalIm1DTO getStockTotalForProcessStock(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws Exception;
}
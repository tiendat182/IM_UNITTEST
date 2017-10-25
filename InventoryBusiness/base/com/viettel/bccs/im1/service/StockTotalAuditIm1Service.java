package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.List;

public interface StockTotalAuditIm1Service {

    @WebMethod
    public StockTotalAuditIm1DTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTotalAuditIm1DTO> findAll() throws Exception;

    @WebMethod
    public List<StockTotalAuditIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public int create(StockTotalAuditIm1DTO productSpecCharacterDTO) throws LogicException,Exception;

    @WebMethod
    public BaseMessage update(StockTotalAuditIm1DTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTotalAuditIm1DTO save(StockTotalAuditIm1DTO stockTotalDTO) throws Exception;

}
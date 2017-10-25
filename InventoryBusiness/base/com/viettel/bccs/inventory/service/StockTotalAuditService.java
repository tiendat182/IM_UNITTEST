package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTotalAuditDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTotalAuditService {

    @WebMethod
    public StockTotalAuditDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTotalAuditDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTotalAuditDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTotalAuditDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(StockTotalAuditDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public StockTotalAuditDTO save(StockTotalAuditDTO stockTotalAuditDTO) throws Exception;
}
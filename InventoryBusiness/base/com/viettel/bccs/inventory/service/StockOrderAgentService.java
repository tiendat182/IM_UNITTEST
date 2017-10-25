package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockOrderAgentService {

    @WebMethod
    public StockOrderAgentDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockOrderAgentDTO> findAll() throws Exception;

    @WebMethod
    public List<StockOrderAgentDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public void createRequestStockOrder(StockOrderAgentDTO dto, List<StockOrderAgentDetailDTO> listDTODetail, StaffDTO currentStaff) throws Exception, LogicException;

    @WebMethod
    public StockOrderAgentDTO update(StockOrderAgentDTO dto) throws Exception, LogicException;

    @WebMethod
    public Long getMaxStockOrderAgentId() throws Exception;

    @WebMethod
    public List<StockOrderAgentDTO> search(StockOrderAgentDTO dto) throws Exception, LogicException;

    @WebMethod
    public List<StockOrderAgentDTO> getStockOrderAgent(Long shopId, Long stockTransId) throws Exception, LogicException;

    @WebMethod
    public List<StockOrderAgentForm> getStockOrderAgentForm(StockOrderAgentDTO searchForm, StaffDTO currentStaff);

    @WebMethod
    public List checkShopAgent(Long shopId);
}
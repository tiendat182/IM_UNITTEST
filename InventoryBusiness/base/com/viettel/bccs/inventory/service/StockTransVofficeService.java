package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockTransVofficeService {

    @WebMethod
    public StockTransVofficeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockTransVofficeDTO> findAll() throws Exception;

    @WebMethod
    public List<StockTransVofficeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockTransVofficeDTO stockTransVofficeDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockTransVofficeDTO stockTransVofficeDTO) throws Exception;

    @WebMethod
    public StockTransVofficeDTO save(StockTransVofficeDTO stockTransVofficeDTO) throws Exception;

    @WebMethod
    public void doSignedVofficeValidate(StockTransActionDTO stockTransActionDTO) throws Exception, LogicException;

    @WebMethod
    public StockTransVofficeDTO findStockTransVofficeByRequestId(String requestId) throws LogicException, Exception;

    @WebMethod
    public StockTransVofficeDTO findStockTransVofficeByActionId(Long actionId) throws LogicException, Exception;

    public List<StockTransVofficeDTO> searchStockTransVoffice(StockTransVofficeDTO stockTransVofficeDTO) throws Exception;

    public void updateVofficeDOA(StockTransVoffice stockTransVoffice) throws LogicException, Exception;

    public List<StockTransVoffice> getLstVofficeSigned(Long maxDay, String prefixTemplate) throws Exception;

    public void doSignVOffice(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception;

    public void updateVofficeDeliver(StockTransVoffice stockTransVoffice) throws LogicException, Exception;

    public void updateVofficeDevice(StockTransVoffice stockTransVoffice) throws LogicException, Exception;

    public void updateVofficeDebit(StockTransVoffice stockTransVoffice) throws LogicException, Exception;
}
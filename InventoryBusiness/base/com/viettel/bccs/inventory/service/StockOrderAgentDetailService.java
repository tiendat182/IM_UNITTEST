package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockOrderAgentDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
@WebService
public interface StockOrderAgentDetailService {

    @WebMethod
    public StockOrderAgentDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockOrderAgentDetailDTO> findAll() throws Exception;

    @WebMethod
    public List<StockOrderAgentDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockOrderAgentDetailDTO create(StockOrderAgentDetailDTO dto) throws Exception;
    @WebMethod
    public BaseMessage update(StockOrderAgentDetailDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public List<StockOrderAgentDetailDTO> getByStockOrderAgentId(Long id) throws Exception;

    @WebMethod
    public List<StockTransFullDTO> getListGood(Long orderAgentId);

}
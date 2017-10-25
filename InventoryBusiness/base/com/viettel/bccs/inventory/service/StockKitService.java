package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.LookupSerialCardAndKitByFileDTO;
import com.viettel.bccs.inventory.dto.StockKitDTO;
import com.viettel.bccs.inventory.model.StockKit;

import java.util.List;

import com.viettel.fw.Exception.LogicException;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface StockKitService {

    @WebMethod
    public StockKitDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockKitDTO> findAll() throws Exception;

    @WebMethod
    public List<StockKitDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(StockKitDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public BaseMessage update(StockKitDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    StockKitDTO getStockKitBySerial(String serial) throws Exception;

    @WebMethod
    List<StockKitDTO> getStockKitBySerialAndProdOfferId(Long staffId, String fromSerial, String toSerial, Long prodOfferId) throws Exception, LogicException;

    @WebMethod
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serial) throws Exception;
}
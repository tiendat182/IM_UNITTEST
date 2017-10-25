package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.ProductExchange;

import java.util.Date;
import java.util.List;

import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import org.primefaces.model.LazyDataModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.transaction.annotation.Transactional;
import com.viettel.fw.dto.BaseMessage;

@WebService
public interface ProductExchangeService {

    @WebMethod
    public ProductExchangeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ProductExchangeDTO> findAll() throws Exception;

    @WebMethod
    public List<ProductExchangeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public ProductExchangeDTO create(ProductExchangeDTO productSpecCharacterDTO) throws Exception;

    @WebMethod
    public ProductExchangeDTO update(ProductExchangeDTO productSpecCharacterDTO) throws Exception;

    @Transactional(rollbackFor = Exception.class)
    void createProductExchange(ProductExchangeDTO productExchangeDTO, Long staffId) throws Exception, LogicException;

    List<ProductExchangeDTO> searchProductExchange(ProductExchangeDTO productExchangeDTO) throws Exception, LogicException;

    void validateCreateTallyOut(List<StockTransDetailDTO> lsDetailDTOs) throws Exception, LogicException;

    @WebMethod
    BaseMessageStockTrans createTallyOut(String mode, String type, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                         List<StockTransDetailDTO> lsDetailDTOs, RequiredRoleMap requiredRoleMap) throws Exception, LogicException;

    void validateExistsProdExchange(Long prodOfferId, Long prodOfferIdNew, Date fromDate, Date toDate) throws Exception, LogicException;

    @WebMethod
    BaseMessageChangeModelTrans findApprovedRequestProductExchangeKit(Long shopId, Date fromDate, Date toDate) throws Exception, LogicException;

    @WebMethod
    BaseMessageChangeModelTransDetail findModelTransDetailByChangeModelTransId(Long changeModelTransId) throws Exception, LogicException;

    @WebMethod
    BaseMessageChangeModelTransSerial findModelTransSerialByChangeModelTransDetailId(Long changeModelTransDetailId) throws Exception, LogicException;

    @WebMethod
    BaseMessage updateChangeModelTransStatusDestroy(Long changeModelTransId) throws Exception, LogicException;

    @WebMethod
    BaseMessage updateChangeModelTransStatusSuccess(Long changeModelTransId) throws Exception, LogicException;
}
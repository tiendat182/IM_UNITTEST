package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ProductErpDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ProductErpService {

    @WebMethod
    public ProductErpDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ProductErpDTO> findAll() throws Exception;

    @WebMethod
    public List<ProductErpDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public BaseMessage create(ProductErpDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public BaseMessage update(ProductErpDTO productSpecCharacterDTO) throws Exception;

}
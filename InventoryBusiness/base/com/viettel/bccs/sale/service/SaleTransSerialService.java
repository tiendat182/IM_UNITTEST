package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.SaleTransSerialDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 12/17/2015.
 */
public interface SaleTransSerialService {
    void save(SaleTransSerialDTO saleTransSerial);

    @WebMethod
    public SaleTransSerialDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<SaleTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public SaleTransSerialDTO createNewSaleTransSerialDTO(SaleTransSerialDTO saleTransDetailDTO) throws Exception;

    @WebMethod
    public List<SaleTransSerialDTO> findByDetailId(Long saleTransDetailId, Date saleTransDate);

    @WebMethod
    public SaleTransSerialDTO update(SaleTransSerialDTO saleTransDetailDTO) throws Exception;
}

package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.SaleTransDetailDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface SaleTransDetailService {

    @WebMethod
    public SaleTransDetailDTO findOne(Long id) throws Exception;

    @WebMethod
    public void save(SaleTransDetailDTO saleTransDetailDTO) throws LogicException, Exception;

    @WebMethod
    public SaleTransDetailDTO update(SaleTransDetailDTO saleTransDetailDTO) throws Exception;

    @WebMethod
    public List<SaleTransDetailDTO> findBySaleTransId(Long saleTransId) throws Exception;

    @WebMethod
    public List<SaleTransDetailDTO> findSaleTransDetails(Long saleTransId, Long prodOfferId, Date saleTransDate) throws Exception;
}
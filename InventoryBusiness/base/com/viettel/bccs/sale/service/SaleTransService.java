package com.viettel.bccs.sale.service;

import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.fw.Exception.LogicException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface SaleTransService {
    @WebMethod
    SaleTransDTO findOne(Long id);

    public SaleTransDTO save(SaleTransDTO saleTransDTO) throws Exception, LogicException;

    public SaleTransDTO update(SaleTransDTO saleTransDTO) throws Exception, LogicException;

    @WebMethod
    public List<SaleTransDTO> findSaleTransByStockTransId(Long stockTransId, Date saleTransDate) throws Exception, LogicException;

    @WebMethod
    public boolean updateInTransIdById(Long saleTransId, Long inTransId) throws Exception, LogicException;

}

package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockDeviceTransferDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface StockDeviceTransferService {

    @WebMethod
    public StockDeviceTransferDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockDeviceTransferDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public String getDeviceConfigStateStrBy(Long probOfferId, Short probOfferStatus, Long probNewOfferId);
}
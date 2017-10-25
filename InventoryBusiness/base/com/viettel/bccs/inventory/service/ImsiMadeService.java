package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImsiMadeDTO;
import com.viettel.bccs.inventory.dto.OutputImsiProduceDTO;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface ImsiMadeService {

    @WebMethod
    public ImsiMadeDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ImsiMadeDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<OutputImsiProduceDTO> getListImsiRange(String startImsi, String endImsi, String docCode, Date fromDate, Date toDate, Long status) throws Exception;

    @WebMethod
    public List<ImsiMadeDTO> getImsiRangeByDate(Date fromDate, Date toDate) throws Exception;

    @WebMethod
    public void doCreate(ImsiMadeDTO imsiMadeDTO, String username) throws LogicException, Exception;

    @WebMethod
    public Long checkImsiRange(String fromImsi, String toImsi) throws Exception;

    @WebMethod
    public List<PartnerDTO> getListPartnerA4keyNotNull() throws Exception;

    @WebMethod
    public boolean validateImsiRange(String fromImsi, String toImsi) throws Exception;

    @WebMethod
    public Long getQuantityByImsi(String fromImsi, String toImsi) throws Exception;
}
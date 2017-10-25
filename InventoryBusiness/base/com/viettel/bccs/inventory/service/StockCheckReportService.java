package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockCheckReportDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface StockCheckReportService {

    @WebMethod
    public StockCheckReportDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<StockCheckReportDTO> findAll() throws Exception;

    @WebMethod
    public List<StockCheckReportDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public StockCheckReportDTO uploadFileStockCheckReport(StockCheckReportDTO stockCheckReportDTO, String staffCode) throws Exception, LogicException;

    @WebMethod
    public List<StockCheckReportDTO> onSearch(Long shopId, Date checkDate) throws Exception, LogicException;

    @WebMethod
    public byte[] getFileStockCheckReport(Long stockCheckReportId) throws Exception, LogicException;

}
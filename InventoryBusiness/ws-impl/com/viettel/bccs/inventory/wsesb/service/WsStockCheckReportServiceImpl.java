package com.viettel.bccs.inventory.wsesb.service;

import java.util.ArrayList;

import com.viettel.bccs.inventory.dto.StockCheckReportDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.data.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.dto.BaseDTO;
import com.viettel.ws.common.utils.*;

import com.viettel.bccs.inventory.service.StockCheckReportService;

import java.util.Date;
import java.util.List;
import java.lang.Long;

import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

import org.springframework.beans.factory.annotation.Qualifier;

@Service("WsStockCheckReportServiceImpl")
public class WsStockCheckReportServiceImpl implements StockCheckReportService {

    public static final Logger logger = Logger.getLogger(WsStockCheckReportServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockCheckReportService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockCheckReportService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockCheckReportDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockCheckReportDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockCheckReportDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    @WebMethod
    public StockCheckReportDTO uploadFileStockCheckReport(StockCheckReportDTO stockCheckReportDTO, String staffCode) throws Exception, LogicException {
        return client.uploadFileStockCheckReport(stockCheckReportDTO, staffCode);
    }

    @Override
    @WebMethod
    public List<StockCheckReportDTO> onSearch(Long shopId, Date checkDate) throws Exception, LogicException {
        return client.onSearch(shopId, checkDate);
    }

    @Override
    @WebMethod
    public byte[] getFileStockCheckReport(Long stockCheckReportId) throws Exception, LogicException {
        return client.getFileStockCheckReport(stockCheckReportId);
    }
}
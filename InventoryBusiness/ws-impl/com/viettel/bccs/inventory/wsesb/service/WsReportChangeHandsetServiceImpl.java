package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseWarrantyMessage;
import com.viettel.bccs.inventory.service.ReportChangeHandsetService;
import com.viettel.bccs.inventory.service.WarrantyService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service("WsReportChangeHandsetServiceImpl")
public class WsReportChangeHandsetServiceImpl implements ReportChangeHandsetService {

    public static final Logger logger = Logger.getLogger(WsReportChangeHandsetServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ReportChangeHandsetService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ReportChangeHandsetService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ReportChangeHandsetDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ReportChangeHandsetDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    @WebMethod
    public List<ReportChangeHandsetDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public ReportChangeHandsetDTO create(ReportChangeHandsetDTO reportChangeHandsetDTO) throws Exception {
        return client.create(reportChangeHandsetDTO);
    }

    @Override
    @WebMethod
    public BaseMessage update(ReportChangeHandsetDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public List<ReportChangeHandsetDTO> getLsReportChangeHandsetTerminal(Long prodOfferId, String serial, String shopPath) throws Exception {
        return client.getLsReportChangeHandsetTerminal(prodOfferId, serial, shopPath);
    }

    @Override
    public List<ReportChangeHandsetDTO> getLsChangeHandsetTerminalDevide(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        return client.getLsChangeHandsetTerminalDevide(stockTransDTO, stockTransActionDTO, lstStockTransDetail);
    }
}
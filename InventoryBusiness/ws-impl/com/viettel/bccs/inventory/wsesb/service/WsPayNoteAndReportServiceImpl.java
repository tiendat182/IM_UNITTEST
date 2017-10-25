package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockDocumentDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.dto.ws.PayNoteAndReportDTO;
import com.viettel.bccs.inventory.service.PayNoteAndReportService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsPayNoteAndReportServiceImpl")
public class WsPayNoteAndReportServiceImpl implements PayNoteAndReportService {

    public static final Logger logger = Logger.getLogger(WsShopServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PayNoteAndReportService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PayNoteAndReportService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }


    @Override
    @WebMethod
    public List<VStockTransDTO> getListStockTransForPay(PayNoteAndReportDTO payNoteAndReportDTO,
                                                 String exportOwnerId,  String importOwnerId, boolean flag) {
        return client.getListStockTransForPay(payNoteAndReportDTO, exportOwnerId, importOwnerId, flag);
    }
    @Override
    @WebMethod
    public BaseMessage createStockDocumentAndUpdateAction(StockDocumentDTO stockDocumentDTO,Long actionId ) throws Exception,LogicException{
        return client.createStockDocumentAndUpdateAction(stockDocumentDTO, actionId);
    }
    @Override
    @WebMethod
    public BaseMessage cancelStockDocument(List<VStockTransDTO> selectForCancel, String strCancelPay, String user) throws Exception, LogicException {
        return client.cancelStockDocument(selectForCancel, strCancelPay,user);
    }
}
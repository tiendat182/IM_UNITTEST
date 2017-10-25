package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransInputDTO;
import com.viettel.bccs.inventory.service.RescueInformationExportStockService;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

/**
 * Created by tuydv1 on 1/13/2016.
 */
@Service("WsRescueInformationExportStockServiceImpl")
public class WsRescueInformationExportStockServiceImpl implements RescueInformationExportStockService {
    public static final Logger logger = Logger.getLogger(WsRescueInformationExportStockServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private RescueInformationExportStockService client;

    @PostConstruct
    public void init() {
        try {
            client = wsClientFactory.createWsClient(RescueInformationExportStockService.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    @WebMethod
    public BaseMessage validateStockTransForExport(Long productOfferingId, String state, String unit, Long avaiableQuantity, String quantity, String serialRecover) throws Exception, LogicException {
        return client.validateStockTransForExport(productOfferingId, state, unit, avaiableQuantity, quantity, serialRecover);
    }

    @Override
    @WebMethod
    public BaseMessage executeStockTransForExport(StockTransInputDTO transInputDTO, String shopCode, ProductOfferingDTO stockRecoverCode, String strSerialRecover,
                                                  List<StockTransDetailDTO> lsStockTransDetailDTO, boolean render) throws Exception, LogicException {
        return client.executeStockTransForExport(transInputDTO, shopCode, stockRecoverCode, strSerialRecover, lsStockTransDetailDTO, render);
    }

}

package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.BillStockDTO;
import com.viettel.bccs.inventory.dto.BillStockResultDTO;
import com.viettel.bccs.inventory.dto.OrderObjectDTO;
import com.viettel.bccs.inventory.service.LogisticService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
@Service("WsLogisticServiceImpl")
public class WsLogisticServiceImpl implements LogisticService {
    public static final Logger logger = Logger.getLogger(WsLogisticServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private LogisticService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(LogisticService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BillStockResultDTO createBill(List<BillStockDTO> lstBillStockDTO) throws LogicException, Exception {
        return client.createBill(lstBillStockDTO);
    }

    @Override
    @WebMethod
    public BillStockResultDTO transStock(BillStockDTO billStockDTO) throws LogicException, Exception {
        return client.transStock(billStockDTO);
    }

    @Override
    @WebMethod
    public BillStockResultDTO cancelOrderOrBill(OrderObjectDTO orderObjectDTO) throws LogicException, Exception {
        return client.cancelOrderOrBill(orderObjectDTO);
    }
}

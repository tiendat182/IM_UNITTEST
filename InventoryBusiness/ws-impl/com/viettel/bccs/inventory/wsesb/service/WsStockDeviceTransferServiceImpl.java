package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.StockDeviceTransferDTO;
import com.viettel.bccs.inventory.service.ApproachOrderDivideDeviceService;
import com.viettel.bccs.inventory.service.StockDeviceTransferService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

/**
 * Created by hoangnt14 on 5/4/2017.
 */
@Service("WsStockDeviceTransferServiceImpl")
public class WsStockDeviceTransferServiceImpl implements StockDeviceTransferService {

    public static final Logger logger = Logger.getLogger(WsStockDeviceTransferServiceImpl.class);
    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockDeviceTransferService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockDeviceTransferService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public StockDeviceTransferDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<StockDeviceTransferDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public String getDeviceConfigStateStrBy(Long probOfferId, Short probOfferStatus, Long probNewOfferId) {
        return client.getDeviceConfigStateStrBy(probOfferId, probOfferStatus, probNewOfferId);
    }
}

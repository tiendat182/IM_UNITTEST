package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.LookupInvoiceUsedDTO;
import com.viettel.bccs.inventory.service.InvoiceUsedService;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by hellp on 9/26/2016.
 */
@Service("WsInvoiceUsedServiceImpl")
public class WsInvoiceUsedServiceImpl implements InvoiceUsedService {
    public static final Logger logger = Logger.getLogger(WsInvoiceUsedServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private InvoiceUsedService client;

    @PostConstruct
    public void init() {
        try {
            client = wsClientFactory.createWsClient(InvoiceUsedService.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    public LookupInvoiceUsedDTO getInvoiceUsed(String serial, Date fromDate, Date toDate) throws Exception {
        return client.getInvoiceUsed(serial,fromDate,toDate);
    }

    @Override
    public List<LookupInvoiceUsedDTO> getAllSerial() throws Exception {
        return client.getAllSerial();
    }
}

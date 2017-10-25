package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.LookupSerialCardAndKitByFileDTO;
import com.viettel.bccs.inventory.dto.StockKitDTO;
import com.viettel.bccs.inventory.service.StockKitService;
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
import java.util.List;

@Service("WsStockKitServiceImpl")
public class WsStockKitServiceImpl implements StockKitService {

    public static final Logger logger = Logger.getLogger(WsStockKitServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockKitService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockKitService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    public StockKitDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    public List<StockKitDTO> findAll() throws Exception {
        return client.findAll();
    }

    @Override
    public List<StockKitDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @Override
    public BaseMessage create(StockKitDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    public BaseMessage update(StockKitDTO dto) throws Exception {
        return client.update(dto);
    }

    @Override
    public StockKitDTO getStockKitBySerial(String serial) throws Exception {
        return client.getStockKitBySerial(serial);
    }

    @Override
    public List<StockKitDTO> getStockKitBySerialAndProdOfferId(Long staffId, String fromSerial, String toSerial, Long prodOfferId) throws Exception, LogicException {
        return client.getStockKitBySerialAndProdOfferId(staffId, fromSerial, toSerial, prodOfferId);
    }

    @Override
    @WebMethod
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serial) throws Exception {
        return client.getSerialList(serial);
    }
}
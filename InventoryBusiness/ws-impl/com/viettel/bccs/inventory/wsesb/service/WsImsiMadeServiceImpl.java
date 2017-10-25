package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImsiMadeDTO;
import com.viettel.bccs.inventory.dto.OutputImsiProduceDTO;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.service.ActionLogService;
import com.viettel.bccs.inventory.service.ImsiMadeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

/**
 * Created by vanho on 02/06/2017.
 */
@Service("WsImsiMadeServiceImpl")
public class WsImsiMadeServiceImpl implements ImsiMadeService {

    public static final Logger logger = Logger.getLogger(WsImsiMadeServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ImsiMadeService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ImsiMadeService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ImsiMadeDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ImsiMadeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public List<OutputImsiProduceDTO> getListImsiRange(String startImsi, String endImsi, String docCode, Date fromDate, Date toDate, Long status) throws Exception {
        return client.getListImsiRange(startImsi, endImsi, docCode, fromDate, toDate, status);
    }

    @Override
    @WebMethod
    public List<ImsiMadeDTO> getImsiRangeByDate(Date fromDate, Date toDate) throws Exception {
        return client.getImsiRangeByDate(fromDate, toDate);
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void doCreate(ImsiMadeDTO imsiMadeDTO, String username) throws LogicException, Exception {
        client.doCreate(imsiMadeDTO, username);
    }

    @Override
    @WebMethod
    public Long checkImsiRange(String fromImsi, String toImsi) throws Exception {
        return client.checkImsiRange(fromImsi, toImsi);
    }

    @Override
    @WebMethod
    public List<PartnerDTO> getListPartnerA4keyNotNull() throws Exception {
        return client.getListPartnerA4keyNotNull();
    }

    @Override
    @WebMethod
    public Long getQuantityByImsi(String fromImsi, String toImsi) throws Exception {
        return client.getQuantityByImsi(fromImsi, toImsi);
    }

    @Override
    @WebMethod
    public boolean validateImsiRange(String fromImsi, String toImsi) throws Exception {
        return client.validateImsiRange(fromImsi, toImsi);
    }
}

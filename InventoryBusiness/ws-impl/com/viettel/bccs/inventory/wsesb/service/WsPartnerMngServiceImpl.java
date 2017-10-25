package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.dto.PartnerInputSearch;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.service.ImsiInfoService;
import com.viettel.bccs.inventory.service.PartnerMngService;
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
 * Created by vanho on 06/06/2017.
 */
@Service("WsPartnerMngServiceImpl")
public class WsPartnerMngServiceImpl implements PartnerMngService {
    public static final Logger logger = Logger.getLogger(WsPartnerMngServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private PartnerMngService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(PartnerMngService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public PartnerDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<PartnerDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public List<Partner> searchPartner(PartnerInputSearch partnerInputSearch) throws Exception {
        return client.searchPartner(partnerInputSearch);
    }

    @Override
    @WebMethod
    public Long countPartnerByPartnerCode(String partnerCode) throws Exception {
        return client.countPartnerByPartnerCode(partnerCode);
    }

    @Override
    @WebMethod
    public Partner saveOrUpdate(Partner Partner) throws Exception {
        return client.saveOrUpdate(Partner);
    }

    @Override
    @WebMethod
    public Long countPartnerByA4Key(String a4Key) throws Exception {
        return client.countPartnerByA4Key(a4Key);
    }

    @Override
    @WebMethod
    public void saveOrUpdateList(List<Partner> partnerLits) throws Exception {
        client.saveOrUpdateList(partnerLits);
    }
}

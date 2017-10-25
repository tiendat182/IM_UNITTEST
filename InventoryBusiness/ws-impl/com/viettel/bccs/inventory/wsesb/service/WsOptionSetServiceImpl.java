package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.OptionSetDTO;
import com.viettel.bccs.inventory.service.OptionSetService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;


@Service("WsOptionSetServiceImpl")
public class WsOptionSetServiceImpl implements OptionSetService {

    public static final Logger logger = Logger.getLogger(WsOptionSetServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private OptionSetService client;

    @PostConstruct
    public void init() {
        try {
            client = wsClientFactory.createWsClient(OptionSetService.class);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    @WebMethod
    public List<OptionSetDTO> findByFilter(List<FilterRequest> filters) throws LogicException, Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public List<FilterRequest> getRequestListByCondition(OptionSetDTO optionSetDTO) throws LogicException, Exception {
        return client.getRequestListByCondition(optionSetDTO);
    }

    @Override
    @WebMethod
    public OptionSetDTO findById(Long optionSetId) {
        return client.findById(optionSetId);
    }

}
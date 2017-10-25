package com.viettel.bccs.product.wsesb.service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.viettel.bccs.product.dto.SaleProgramDTO;
import com.viettel.bccs.product.service.SaleProgramService;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 10/20/2016.
 */
@Service("wsSaleProgramServiceImpl")
@DependsOn(value = "cxfWsClientFactory")
public class WsSaleProgramServiceImpl implements SaleProgramService {
    public static final Logger logger = Logger.getLogger(WsSaleProgramServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private SaleProgramService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(SaleProgramService.class);
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }

    @Override
    @WebMethod
    public SaleProgramDTO findByCode(String code) throws Exception {
        return client.findByCode(code);
    }

    @Override
    @WebMethod
    public SaleProgramDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<SaleProgramDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    @Cacheable(cacheName = "WsSaleProgramService.findAllSalesProgramActive")
    public List<SaleProgramDTO> findAllSalesProgramActive() throws Exception {
        return client.findAllSalesProgramActive();
    }
}

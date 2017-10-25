package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.service.HandleVOfficeResultService;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.voffice.vo.ResultObj;
import com.viettel.voffice.vo.ResultObjList;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;

@Service("WsHandleVOfficeResultServiceImpl")
public class WsHandleVOfficeResultServiceImpl implements HandleVOfficeResultService {

    public static final Logger logger = Logger.getLogger(WsHandleVOfficeResultServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private HandleVOfficeResultService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(HandleVOfficeResultService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public BaseMessage returnSignResult(ResultObj resultObj) throws Exception {
        return client.returnSignResult(resultObj);
    }

}
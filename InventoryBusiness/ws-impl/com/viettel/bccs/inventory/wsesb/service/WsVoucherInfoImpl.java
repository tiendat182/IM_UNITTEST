package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.VoucherInfoDTO;
import com.viettel.bccs.inventory.service.VoucherInfoService;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsVoucherInfoImpl")
public class WsVoucherInfoImpl implements VoucherInfoService {

    public static final Logger logger = Logger.getLogger(WsVoucherInfoImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private VoucherInfoService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(VoucherInfoService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }


    @Override
    @WebMethod
    public List<VoucherInfoDTO> findAll() {
        return client.findAll();
    }

    @Override
    @WebMethod
    public BaseMessage insertListVoucherInfo(List<VoucherInfoDTO> voucherInfoDTOs) {
        return client.insertListVoucherInfo(voucherInfoDTOs);
    }

    @Override
    @WebMethod
    public VoucherInfoDTO findBySerial(String serial) {
        return client.findBySerial(serial);
    }

    @Override
    public BaseMessage deleteVoucherInfo(Long id) {
        return client.deleteVoucherInfo(id);
    }
}
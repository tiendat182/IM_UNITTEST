package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ImsiInfoDTO;
import com.viettel.bccs.inventory.dto.ImsiInfoInputSearch;
import com.viettel.bccs.inventory.dto.UpdateImsiInfoDTO;
import com.viettel.bccs.inventory.service.ImsiInfoService;
import com.viettel.bccs.inventory.service.ImsiMadeService;
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

/**
 * Created by vanho on 02/06/2017.
 */
@Service("WsImsiInfoServiceImpl")
public class WsImsiInfoServiceImpl implements ImsiInfoService {

    public static final Logger logger = Logger.getLogger(WsImsiInfoServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private ImsiInfoService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(ImsiInfoService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @Override
    @WebMethod
    public ImsiInfoDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @Override
    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @Override
    @WebMethod
    public List<ImsiInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @Override
    @WebMethod
    public List<ImsiInfoDTO> search(ImsiInfoInputSearch imsiInfoInputSearch) throws Exception {
        return client.search(imsiInfoInputSearch);
    }

    @Override
    @WebMethod
    public Long countImsiRange(String fromImsi, String toImsi) throws Exception {
        return client.countImsiRange(fromImsi, toImsi);
    }

    @Override
    @WebMethod
    public Long countImsiRangeToCheckDelete(String fromImsi, String toImsi) throws Exception {
        return client.countImsiRangeToCheckDelete(fromImsi, toImsi);
    }

    @Override
    @WebMethod
    public BaseMessage insertBatch(ImsiInfoDTO imsiInfoDTO) throws Exception {
        return client.insertBatch(imsiInfoDTO);
    }

    @Override
    @WebMethod
    public BaseMessage updateBatch(ImsiInfoDTO imsiInfoDTO) throws Exception {
        return client.updateBatch(imsiInfoDTO);
    }

    @Override
    @WebMethod
    public BaseMessage deleteImsi(String fromImsi, String toImsi) throws Exception {
        return client.deleteImsi(fromImsi, toImsi);
    }

    @Override
    @WebMethod
    public boolean checkImsiInfo(String imsi) throws Exception {
        return client.checkImsiInfo(imsi);
    }

    @Override
    @WebMethod
    public List<UpdateImsiInfoDTO> getTransactionToUpdateImsi() throws Exception {
        return client.getTransactionToUpdateImsi();
    }

    @Override
    @WebMethod
    public void updateImsiInfoToHasUsedSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception {
        client.updateImsiInfoToHasUsedSim(updateImsiInfoDTOS);
    }

    @Override
    @WebMethod
    public void updateImsiInfoHasConnectSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception {
        client.updateImsiInfoHasConnectSim(updateImsiInfoDTOS);
    }

    @Override
    @WebMethod
    public List<UpdateImsiInfoDTO> getImsiHasFillPartner() throws Exception {
        return client.getImsiHasFillPartner();
    }
}

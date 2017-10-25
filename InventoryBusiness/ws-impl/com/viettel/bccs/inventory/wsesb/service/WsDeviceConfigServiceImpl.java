package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.DeviceConfigDTO;
import com.viettel.bccs.inventory.dto.DeviceConfigDetailDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.service.DeviceConfigService;
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

/**
 * Created by hoangnt14 on 5/4/2017.
 */
@Service("WsDeviceConfigServiceImpl")
public class WsDeviceConfigServiceImpl implements DeviceConfigService{

    public static final Logger logger = Logger.getLogger(WsDeviceConfigServiceImpl.class);
    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private DeviceConfigService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(DeviceConfigService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @WebMethod
    public DeviceConfigDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return client.count(filters);
    }

    @WebMethod
    public List<DeviceConfigDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return client.findByFilter(filters);
    }

    @WebMethod
    public List<DeviceConfigDTO> searchDeviceByProductOfferIdAndStateId(Long productOfferId, Long stateId) throws Exception {
        return client.searchDeviceByProductOfferIdAndStateId(productOfferId, stateId);
    }

    @WebMethod
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductTypeAndState(String input, Long prodTypeId, Long stateId) throws Exception {
        return client.getLsProductOfferingByProductTypeAndState(input, prodTypeId, stateId);
    }

    @WebMethod
    public BaseMessage createDeviceConfig(String username, DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException {
        return client.createDeviceConfig(username, addDeviceConfig, listAddDeviceConfigDetail);
    }

    @WebMethod
    public BaseMessage createDeviceConfigImport(String username, List<DeviceConfigDTO> addDeviceConfig) throws Exception, LogicException {
        return client.createDeviceConfigImport(username, addDeviceConfig);
    }

    @WebMethod
    public BaseMessage updateDeviceConfig(String username, DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException {
        return client.updateDeviceConfig(username, addDeviceConfig, listAddDeviceConfigDetail);
    }

    @WebMethod
    public BaseMessage checkProductIdConfigIsConfig(Long probOfferId, Long stateId, Long newProbOfferId) throws Exception, LogicException {
        return client.checkProductIdConfigIsConfig(probOfferId, stateId, newProbOfferId);
    }

    @WebMethod
    public BaseMessage checkProductIsConfigWithState(Long probOfferId, Long stateId) throws Exception, LogicException {
        return client.checkProductIsConfigWithState(probOfferId, stateId);
    }

    @WebMethod
    public BaseMessage checkProductIsConfigWithStateForUpdate(Long probOfferId, Long stateId) throws Exception, LogicException {
        return client.checkProductIsConfigWithStateForUpdate(probOfferId, stateId);
    }

    @WebMethod
    public BaseMessage checkProductIdConfig(DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException {
        return client.checkProductIdConfig(addDeviceConfig, listAddDeviceConfigDetail);
    }

    @WebMethod
    public String getStateStr(Long stateId) {
        return client.getStateStr(stateId);
    }

    @WebMethod
    public void delete(List<DeviceConfigDTO> listIds, String staffCode) throws Exception, LogicException {
        client.delete(listIds, staffCode);
    }

    @WebMethod
    public List<DeviceConfigDTO> getDeviceConfigByProductAndState(Long probOfferId, Long stateId) {
        return client.getDeviceConfigByProductAndState(probOfferId, stateId);
    }

    @WebMethod
    public List<DeviceConfigDTO> getDeviceConfigInfo() {
        return client.getDeviceConfigInfo();
    }
}

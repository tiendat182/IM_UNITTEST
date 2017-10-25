package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.DeviceConfigDTO;
import com.viettel.bccs.inventory.dto.DeviceConfigDetailDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface DeviceConfigService {

    @WebMethod
    public DeviceConfigDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DeviceConfigDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<DeviceConfigDTO> searchDeviceByProductOfferIdAndStateId(Long productOfferId, Long stateId) throws Exception;

    @WebMethod
    public List<ProductOfferingTotalDTO> getLsProductOfferingByProductTypeAndState(String input, Long prodTypeId, Long stateId) throws Exception;

    @WebMethod
    public BaseMessage createDeviceConfig(String username, DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException;

    @WebMethod
    public BaseMessage createDeviceConfigImport(String username, List<DeviceConfigDTO> addDeviceConfig) throws Exception, LogicException;

    @WebMethod
    public BaseMessage updateDeviceConfig(String username, DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException;


    @WebMethod
    public BaseMessage checkProductIdConfigIsConfig(Long probOfferId, Long stateId, Long newProbOfferId) throws Exception, LogicException;

    @WebMethod
    public BaseMessage checkProductIsConfigWithState(Long probOfferId, Long stateId) throws Exception, LogicException;

    @WebMethod
    public BaseMessage checkProductIsConfigWithStateForUpdate(Long probOfferId, Long stateId) throws Exception, LogicException;

    @WebMethod
    public BaseMessage checkProductIdConfig(DeviceConfigDTO addDeviceConfig, List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) throws Exception, LogicException;

    @WebMethod
    public String getStateStr(Long stateId);

    @WebMethod
    public void delete(List<DeviceConfigDTO> listIds, String staffCode)  throws Exception, LogicException;

    @WebMethod
    public List<DeviceConfigDTO> getDeviceConfigByProductAndState(Long probOfferId, Long stateId);

    @WebMethod
    List<DeviceConfigDTO> getDeviceConfigInfo();

}
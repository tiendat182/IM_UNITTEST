package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ImsiInfoDTO;
import com.viettel.bccs.inventory.dto.ImsiInfoInputSearch;
import com.viettel.bccs.inventory.dto.UpdateImsiInfoDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ImsiInfoService {

    @WebMethod
    public ImsiInfoDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ImsiInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<ImsiInfoDTO> search(ImsiInfoInputSearch imsiInfoInputSearch) throws Exception;

    @WebMethod
    public Long countImsiRange(String fromImsi, String toImsi) throws Exception;

    @WebMethod
    public Long countImsiRangeToCheckDelete(String fromImsi, String toImsi) throws Exception;

    @WebMethod
    public BaseMessage insertBatch(ImsiInfoDTO imsiInfoDTO) throws Exception;

    @WebMethod
    public BaseMessage updateBatch(ImsiInfoDTO imsiInfoDTO) throws Exception;

    @WebMethod
    public BaseMessage deleteImsi(String fromImsi, String toImsi) throws Exception;

    @WebMethod
    public boolean checkImsiInfo(String imsi) throws Exception;

    @WebMethod
    public List<UpdateImsiInfoDTO> getTransactionToUpdateImsi() throws Exception;

    @WebMethod
    public void updateImsiInfoToHasUsedSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception;

    @WebMethod
    public void updateImsiInfoHasConnectSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception;

    @WebMethod
    public List<UpdateImsiInfoDTO> getImsiHasFillPartner() throws Exception;

}
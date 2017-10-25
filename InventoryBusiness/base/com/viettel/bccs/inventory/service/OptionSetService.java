package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.OptionSetDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface OptionSetService {

    @WebMethod
    public List<OptionSetDTO> findByFilter(List<FilterRequest> filters) throws LogicException, Exception;

    /**
     * get list Filter request by condition findByDebitRequestDetail
     * @param optionSetDTO dto optionSetDTO
     * @return list object OptionSetDTO
     */

    @WebMethod
    public List<FilterRequest> getRequestListByCondition(OptionSetDTO optionSetDTO) throws LogicException, Exception;
    
    /**
     * get object OptionSetDTO by id
     * @param optionSetId internal id of  OptionSet
     * @return list object TelecomServiceTypeDTO
     */
    @WebMethod
    public OptionSetDTO findById(Long optionSetId);



}
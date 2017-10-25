package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.dto.PartnerInputSearch;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PartnerMngService {

    @WebMethod
    public PartnerDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<PartnerDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<Partner> searchPartner(PartnerInputSearch partnerInputSearch) throws Exception;

    @WebMethod
    public Long countPartnerByPartnerCode(String partnerCode) throws Exception;

    @WebMethod
    public Partner saveOrUpdate(Partner Partner) throws Exception;

    @WebMethod
    public Long countPartnerByA4Key(String a4Key) throws Exception;

    @WebMethod
    public void saveOrUpdateList(List<Partner> partnerLits) throws Exception;
}
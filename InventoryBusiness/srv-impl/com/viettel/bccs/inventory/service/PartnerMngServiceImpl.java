package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.dto.PartnerInputSearch;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.repo.PartnerMngRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class PartnerMngServiceImpl extends BaseServiceImpl implements PartnerMngService {

    private final BaseMapper<Partner,PartnerDTO> mapper = new BaseMapper<>(Partner.class,PartnerDTO.class);

    @Autowired
    private PartnerMngRepo repo;
    public static final Logger logger = Logger.getLogger(PartnerMngService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repo.count(repo.toPredicate(filters));
    }

    @WebMethod
    public PartnerDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repo.findOne(id));
    }

    @WebMethod
    public List<PartnerDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));
    }

    @WebMethod
    public List<Partner> searchPartner(PartnerInputSearch partnerInputSearch) throws Exception {
        return repo.searchPartner(partnerInputSearch);
    }

    @WebMethod
    public Long countPartnerByPartnerCode(String partnerCode) throws Exception {
        return repo.countPartnerByPartnerCode(partnerCode);
    }


    @WebMethod
    public Partner saveOrUpdate(Partner partner) throws Exception {
        return repo.save(partner);
    }

    @WebMethod
    public Long countPartnerByA4Key(String a4Key) throws Exception {
        return repo.countPartnerByA4Key(a4Key);
    }

    @WebMethod
    public void saveOrUpdateList(List<Partner> partnerLits) throws Exception {
        repo.save(partnerLits);
    }


}

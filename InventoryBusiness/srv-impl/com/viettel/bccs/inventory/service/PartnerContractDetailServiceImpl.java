package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PartnerContractDetailDTO;
import com.viettel.bccs.inventory.model.PartnerContractDetail;
import com.viettel.bccs.inventory.repo.PartnerContractDetailRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class PartnerContractDetailServiceImpl extends BaseServiceImpl implements PartnerContractDetailService {

    private final BaseMapper<PartnerContractDetail, PartnerContractDetailDTO> mapper = new BaseMapper<>(PartnerContractDetail.class, PartnerContractDetailDTO.class);

    @Autowired
    private PartnerContractDetailRepo repository;
    public static final Logger logger = Logger.getLogger(PartnerContractDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PartnerContractDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PartnerContractDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PartnerContractDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PartnerContractDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PartnerContractDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public PartnerContractDetailDTO save(PartnerContractDetailDTO partnerContractDetailDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(partnerContractDetailDTO)));
    }
}

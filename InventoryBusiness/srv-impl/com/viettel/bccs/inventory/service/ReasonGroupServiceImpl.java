package com.viettel.bccs.inventory.service;
import com.viettel.bccs.inventory.dto.ReasonGroupDTO;
import com.viettel.bccs.inventory.model.ReasonGroup;
import com.viettel.bccs.inventory.repo.ReasonGroupRepo;
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
public class ReasonGroupServiceImpl extends BaseServiceImpl implements ReasonGroupService {

    private final BaseMapper<ReasonGroup, ReasonGroupDTO> mapper = new BaseMapper(ReasonGroup.class, ReasonGroupDTO.class);

    @Autowired
    private ReasonGroupRepo repository;
    public static final Logger logger = Logger.getLogger(ReasonGroupService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ReasonGroupDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ReasonGroupDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ReasonGroupDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ReasonGroupDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ReasonGroupDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

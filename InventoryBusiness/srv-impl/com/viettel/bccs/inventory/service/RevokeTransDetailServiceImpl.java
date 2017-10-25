package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RevokeTransDetailDTO;
import com.viettel.bccs.inventory.model.RevokeTransDetail;
import com.viettel.bccs.inventory.repo.RevokeTransDetailRepo;
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
public class RevokeTransDetailServiceImpl extends BaseServiceImpl implements RevokeTransDetailService {

    private final BaseMapper<RevokeTransDetail, RevokeTransDetailDTO> mapper = new BaseMapper<>(RevokeTransDetail.class, RevokeTransDetailDTO.class);

    @Autowired
    private RevokeTransDetailRepo repository;
    public static final Logger logger = Logger.getLogger(RevokeTransDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RevokeTransDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<RevokeTransDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<RevokeTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(RevokeTransDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(RevokeTransDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Transactional(rollbackFor = Exception.class)
    public RevokeTransDetailDTO save(RevokeTransDetailDTO revokeTransDetailDTO) throws Exception {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(revokeTransDetailDTO)));
    }
}

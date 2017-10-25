package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApLockTransDetailDTO;
import com.viettel.bccs.inventory.model.ApLockTransDetail;
import com.viettel.bccs.inventory.repo.ApLockTransDetailRepo;
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
public class ApLockTransDetailServiceImpl extends BaseServiceImpl implements ApLockTransDetailService {

    private final BaseMapper<ApLockTransDetail, ApLockTransDetailDTO> mapper = new BaseMapper<>(ApLockTransDetail.class, ApLockTransDetailDTO.class);

    @Autowired
    private ApLockTransDetailRepo repository;
    public static final Logger logger = Logger.getLogger(ApLockTransDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ApLockTransDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ApLockTransDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ApLockTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ApLockTransDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ApLockTransDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

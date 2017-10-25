package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.LockUserInfoMsgDTO;
import com.viettel.bccs.inventory.dto.LockUserTypeDTO;
import com.viettel.bccs.inventory.model.LockUserType;
import com.viettel.bccs.inventory.repo.LockUserTypeRepo;
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
public class LockUserTypeServiceImpl extends BaseServiceImpl implements LockUserTypeService {

    private final BaseMapper<LockUserType, LockUserTypeDTO> mapper = new BaseMapper<>(LockUserType.class, LockUserTypeDTO.class);

    @Autowired
    private LockUserTypeRepo repository;
    public static final Logger logger = Logger.getLogger(LockUserTypeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public LockUserTypeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<LockUserTypeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<LockUserTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(LockUserTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(LockUserTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<LockUserTypeDTO> getAllUserLockType() throws Exception {
        return repository.getAllUserLockType();
    }

    @Override
    public List<LockUserInfoMsgDTO> getLockUserInfo(Long staffId) throws Exception {
        return repository.getLockUserInfo(staffId);
    }
}

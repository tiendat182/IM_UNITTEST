package com.viettel.bccs.partner.service;

import com.viettel.bccs.partner.dto.AccountAgentDTO;
import com.viettel.bccs.im1.model.AccountAgent;
import com.viettel.bccs.partner.repo.AccountAgentRepo;
import com.viettel.fw.Exception.LogicException;
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
public class AccountAgentServiceImpl extends BaseServiceImpl implements AccountAgentService {

    private final BaseMapper<AccountAgent, AccountAgentDTO> mapper = new BaseMapper<>(AccountAgent.class, AccountAgentDTO.class);

    @Autowired
    private AccountAgentRepo repository;
    public static final Logger logger = Logger.getLogger(AccountAgentService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public AccountAgentDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<AccountAgentDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<AccountAgentDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(AccountAgentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(AccountAgentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<AccountAgentDTO> getAccountAgentFromOwnerId(Long ownerId, Long ownerType) throws LogicException, Exception {
        List<AccountAgent> lstAccountAgent = repository.getAccountAgentFromOwnerId(ownerId, ownerType);
        return mapper.toDtoBean(lstAccountAgent);
    }
}

package com.viettel.bccs.partner.service;

import com.viettel.bccs.partner.dto.AccountBookBankplusDTO;
import com.viettel.bccs.partner.model.AccountBookBankplus;
import com.viettel.bccs.partner.repo.AccountBookBankplusRepo;
import com.viettel.bccs.partner.service.AccountBookBankplusService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class AccountBookBankplusServiceImpl extends BaseServiceImpl implements AccountBookBankplusService {

    private final BaseMapper<AccountBookBankplus, AccountBookBankplusDTO> mapper = new BaseMapper(AccountBookBankplus.class, AccountBookBankplusDTO.class);
    @Autowired
    private AccountBookBankplusRepo repository;
    public static final Logger logger = Logger.getLogger(AccountBookBankplusService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public AccountBookBankplusDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<AccountBookBankplusDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<AccountBookBankplusDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public AccountBookBankplusDTO create(AccountBookBankplusDTO dto) throws Exception {
        try {
            AccountBookBankplus accountBookBankplus = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(accountBookBankplus);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public AccountBookBankplusDTO update(AccountBookBankplusDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

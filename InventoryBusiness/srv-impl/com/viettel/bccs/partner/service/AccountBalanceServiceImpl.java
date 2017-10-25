package com.viettel.bccs.partner.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.partner.dto.AccountBalanceDTO;
import com.viettel.bccs.partner.model.AccountBalance;
import com.viettel.bccs.partner.repo.AccountBalanceRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class AccountBalanceServiceImpl extends BaseServiceImpl implements AccountBalanceService {

    private final BaseMapper<AccountBalance, AccountBalanceDTO> mapper = new BaseMapper<>(AccountBalance.class, AccountBalanceDTO.class);

    @Autowired
    private AccountBalanceRepo repository;
    public static final Logger logger = Logger.getLogger(AccountBalanceService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public AccountBalanceDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<AccountBalanceDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<AccountBalanceDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public AccountBalanceDTO create(AccountBalanceDTO dto) throws Exception {
        try {
            AccountBalance accountBalance = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(accountBalance);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public AccountBalanceDTO update(AccountBalanceDTO dto) throws Exception {
        try {
            AccountBalance accountBalance = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(accountBalance);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public AccountBalanceDTO getAccountBalanceBankplus(Long ownerType, Long ownerId) throws LogicException, Exception {
        List<AccountBalance> accountBalances = repository.getAccountBalance(ownerType, ownerId, Const.ACCOUNT_BALANCE.BALANCE_TYPE_BANKPLUS);
        List<AccountBalanceDTO> accountBalanceDTOs = mapper.toDtoBean(accountBalances);
        if (!DataUtil.isNullOrEmpty(accountBalanceDTOs)) {
            return accountBalanceDTOs.get(0);
        }
        return null;
    }

    @Override
    public AccountBalanceDTO findLock(Long accountBalanceId) throws LogicException, Exception {
        return mapper.toDtoBean(repository.findLock(accountBalanceId));
    }
}

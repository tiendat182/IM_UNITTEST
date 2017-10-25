package com.viettel.bccs.partner.service;

import com.viettel.bccs.partner.dto.AccountBalanceDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface AccountBalanceService {

    @WebMethod
    public AccountBalanceDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<AccountBalanceDTO> findAll() throws Exception;

    @WebMethod
    public List<AccountBalanceDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public AccountBalanceDTO create(AccountBalanceDTO dto) throws Exception;
    @WebMethod
    public AccountBalanceDTO update(AccountBalanceDTO dto) throws Exception;

    /**
     * Lay thong tin tai khoan bankplus
     * @param ownerType
     * @param ownerId
     * @return
     * @throws LogicException
     * @throws Exception
     */
    @WebMethod
    public AccountBalanceDTO getAccountBalanceBankplus(Long ownerType, Long ownerId) throws LogicException, Exception;
    @WebMethod
    public AccountBalanceDTO findLock(Long accountBalanceId) throws LogicException, Exception;
}
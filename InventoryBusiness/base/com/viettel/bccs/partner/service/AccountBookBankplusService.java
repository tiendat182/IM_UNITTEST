package com.viettel.bccs.partner.service;
import com.viettel.bccs.partner.dto.AccountBookBankplusDTO;

import java.util.List;

import com.viettel.fw.common.util.extjs.FilterRequest;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface AccountBookBankplusService {

    @WebMethod
    public AccountBookBankplusDTO findOne(Long id) throws Exception;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public List<AccountBookBankplusDTO> findAll() throws Exception;

    @WebMethod
    public List<AccountBookBankplusDTO> findByFilter(List<FilterRequest> filters) throws Exception;

    @WebMethod
    public AccountBookBankplusDTO create(AccountBookBankplusDTO productSpecCharacterDTO) throws Exception;
    @WebMethod
    public AccountBookBankplusDTO update(AccountBookBankplusDTO productSpecCharacterDTO) throws Exception;

}
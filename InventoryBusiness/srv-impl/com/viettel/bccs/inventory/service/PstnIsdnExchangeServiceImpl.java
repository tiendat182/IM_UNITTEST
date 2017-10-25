package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.PstnIsdnExchangeDTO;
import com.viettel.bccs.inventory.model.PstnIsdnExchange;
import com.viettel.bccs.inventory.repo.PstnIsdnExchangeRepo;
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
public class PstnIsdnExchangeServiceImpl extends BaseServiceImpl implements PstnIsdnExchangeService {

    private final BaseMapper<PstnIsdnExchange, PstnIsdnExchangeDTO> mapper = new BaseMapper<>(PstnIsdnExchange.class, PstnIsdnExchangeDTO.class);

    @Autowired
    private PstnIsdnExchangeRepo repository;
    public static final Logger logger = Logger.getLogger(PstnIsdnExchangeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public PstnIsdnExchangeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<PstnIsdnExchangeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<PstnIsdnExchangeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(PstnIsdnExchangeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(PstnIsdnExchangeDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

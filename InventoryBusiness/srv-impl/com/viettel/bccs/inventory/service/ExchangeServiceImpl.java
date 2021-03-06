package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ExchangeDTO;
import com.viettel.bccs.inventory.model.Exchange;
import com.viettel.bccs.inventory.repo.ExchangeRepo;
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
public class ExchangeServiceImpl extends BaseServiceImpl implements ExchangeService {

    private final BaseMapper<Exchange, ExchangeDTO> mapper = new BaseMapper<>(Exchange.class, ExchangeDTO.class);

    @Autowired
    private ExchangeRepo repository;
    public static final Logger logger = Logger.getLogger(ExchangeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ExchangeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ExchangeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ExchangeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ExchangeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ExchangeDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

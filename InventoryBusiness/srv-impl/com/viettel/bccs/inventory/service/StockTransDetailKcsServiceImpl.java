package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailKcsDTO;
import com.viettel.bccs.inventory.model.StockTransDetailKcs;
import com.viettel.bccs.inventory.repo.StockTransDetailKcsRepo;
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
public class StockTransDetailKcsServiceImpl extends BaseServiceImpl implements StockTransDetailKcsService {

    private final BaseMapper<StockTransDetailKcs, StockTransDetailKcsDTO> mapper = new BaseMapper<>(StockTransDetailKcs.class, StockTransDetailKcsDTO.class);

    @Autowired
    private StockTransDetailKcsRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransDetailKcsService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransDetailKcsDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransDetailKcsDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransDetailKcsDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransDetailKcsDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransDetailKcsDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockTransDetailKcsDTO save(StockTransDetailKcsDTO stockTransDetailKcsDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDetailKcsDTO)));
    }
}

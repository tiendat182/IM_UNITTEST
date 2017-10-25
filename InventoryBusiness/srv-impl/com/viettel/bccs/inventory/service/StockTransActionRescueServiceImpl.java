package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransActionRescueDTO;
import com.viettel.bccs.inventory.model.StockTransActionRescue;
import com.viettel.bccs.inventory.repo.StockTransActionRescueRepo;
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
public class StockTransActionRescueServiceImpl extends BaseServiceImpl implements StockTransActionRescueService {

    private final BaseMapper<StockTransActionRescue, StockTransActionRescueDTO> mapper = new BaseMapper<>(StockTransActionRescue.class, StockTransActionRescueDTO.class);

    @Autowired
    private StockTransActionRescueRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransActionRescueService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransActionRescueDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransActionRescueDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransActionRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransActionRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransActionRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public StockTransActionRescueDTO save(StockTransActionRescueDTO stockTransActionRescueDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransActionRescueDTO)));
    }
}

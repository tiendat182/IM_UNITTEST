package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransExtDTO;
import com.viettel.bccs.inventory.model.StockTransExt;
import com.viettel.bccs.inventory.repo.StockTransExtRepo;
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
public class StockTransExtServiceImpl extends BaseServiceImpl implements StockTransExtService {

    private final BaseMapper<StockTransExt, StockTransExtDTO> mapper = new BaseMapper<>(StockTransExt.class, StockTransExtDTO.class);

    @Autowired
    private StockTransExtRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransExtService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransExtDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransExtDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransExtDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransExtDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransExtDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

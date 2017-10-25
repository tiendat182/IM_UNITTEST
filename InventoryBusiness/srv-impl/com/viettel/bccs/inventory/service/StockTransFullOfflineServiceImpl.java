package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransFullOfflineDTO;
import com.viettel.bccs.inventory.model.StockTransFullOffline;
import com.viettel.bccs.inventory.repo.StockTransFullOfflineRepo;
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
public class StockTransFullOfflineServiceImpl extends BaseServiceImpl implements StockTransFullOfflineService {

    private final BaseMapper<StockTransFullOffline, StockTransFullOfflineDTO> mapper = new BaseMapper<>(StockTransFullOffline.class, StockTransFullOfflineDTO.class);

    @Autowired
    private StockTransFullOfflineRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransFullOfflineService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransFullOfflineDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransFullOfflineDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransFullOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransFullOfflineDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransFullOfflineDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailOfflineDTO;
import com.viettel.bccs.inventory.model.StockTransDetailOffline;
import com.viettel.bccs.inventory.repo.StockTransDetailOfflineRepo;
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
public class StockTransDetailOfflineServiceImpl extends BaseServiceImpl implements StockTransDetailOfflineService {

    private final BaseMapper<StockTransDetailOffline, StockTransDetailOfflineDTO> mapper = new BaseMapper<>(StockTransDetailOffline.class, StockTransDetailOfflineDTO.class);

    @Autowired
    private StockTransDetailOfflineRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransDetailOfflineService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransDetailOfflineDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransDetailOfflineDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransDetailOfflineDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransDetailOfflineDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransDetailOfflineDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockTransDetailOfflineDTO save(StockTransDetailOfflineDTO stockTransDetailOfflineDTO) throws Exception {

        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDetailOfflineDTO)));
    }
}

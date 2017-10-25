package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopLockDTO;
import com.viettel.bccs.inventory.model.StockIsdnVtShopLock;
import com.viettel.bccs.inventory.repo.StockIsdnVtShopLockRepo;
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
public class StockIsdnVtShopLockServiceImpl extends BaseServiceImpl implements StockIsdnVtShopLockService {

    private final BaseMapper<StockIsdnVtShopLock, StockIsdnVtShopLockDTO> mapper = new BaseMapper<>(StockIsdnVtShopLock.class, StockIsdnVtShopLockDTO.class);

    @Autowired
    private StockIsdnVtShopLockRepo repository;
    public static final Logger logger = Logger.getLogger(StockIsdnVtShopLockService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockIsdnVtShopLockDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockIsdnVtShopLockDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockIsdnVtShopLockDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockIsdnVtShopLockDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockIsdnVtShopLockDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockIsdnVtShopLockDTO save(StockIsdnVtShopLockDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void deleteShopLock(String isdn) throws Exception {
        repository.deleteShopLock(isdn);
    }
}

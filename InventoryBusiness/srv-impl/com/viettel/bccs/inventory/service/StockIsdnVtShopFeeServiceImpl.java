package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockIsdnVtShopFeeDTO;
import com.viettel.bccs.inventory.model.StockIsdnVtShopFee;
import com.viettel.bccs.inventory.repo.StockIsdnVtShopFeeRepo;
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
public class StockIsdnVtShopFeeServiceImpl extends BaseServiceImpl implements StockIsdnVtShopFeeService {

    private final BaseMapper<StockIsdnVtShopFee, StockIsdnVtShopFeeDTO> mapper = new BaseMapper<>(StockIsdnVtShopFee.class, StockIsdnVtShopFeeDTO.class);

    @Autowired
    private StockIsdnVtShopFeeRepo repository;
    public static final Logger logger = Logger.getLogger(StockIsdnVtShopFeeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockIsdnVtShopFeeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockIsdnVtShopFeeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockIsdnVtShopFeeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockIsdnVtShopFeeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockIsdnVtShopFeeDTO save(StockIsdnVtShopFeeDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void deleteFee(String isdn) throws Exception {
        repository.deleteFee(isdn);
    }
}

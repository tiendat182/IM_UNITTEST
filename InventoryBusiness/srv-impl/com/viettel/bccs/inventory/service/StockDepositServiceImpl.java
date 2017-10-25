package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.repo.StockDepositRepo;
import com.viettel.bccs.inventory.dto.StockDepositDTO;
import com.viettel.bccs.inventory.model.StockDeposit;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;

import org.springframework.transaction.annotation.Transactional;


@Service
public class StockDepositServiceImpl extends BaseServiceImpl implements StockDepositService {

    private final BaseMapper<StockDeposit, StockDepositDTO> mapper = new BaseMapper<>(StockDeposit.class, StockDepositDTO.class);

    @Autowired
    private StockDepositRepo repository;
    public static final Logger logger = Logger.getLogger(StockDepositService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockDepositDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockDepositDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockDepositDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockDepositDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockDepositDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public StockDepositDTO getStockDeposit(Long prodOfferId, Long channelTypeId, Long transType) throws Exception {
        return repository.getStockDeposit(prodOfferId, channelTypeId, transType);
    }
}

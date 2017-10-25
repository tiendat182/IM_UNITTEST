package com.viettel.bccs.partner.service;

import com.viettel.bccs.partner.dto.StockOwnerTmpDTO;
import com.viettel.bccs.partner.model.StockOwnerTmp;
import com.viettel.bccs.partner.repo.StockOwnerTmpRepo;
import com.viettel.fw.Exception.LogicException;
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
public class StockOwnerTmpServiceImpl extends BaseServiceImpl implements StockOwnerTmpService {

    private final BaseMapper<StockOwnerTmp, StockOwnerTmpDTO> mapper = new BaseMapper<>(StockOwnerTmp.class, StockOwnerTmpDTO.class);

    @Autowired
    private StockOwnerTmpRepo repository;
    public static final Logger logger = Logger.getLogger(StockOwnerTmpService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockOwnerTmpDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockOwnerTmpDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockOwnerTmpDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockOwnerTmpDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockOwnerTmpDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<StockOwnerTmpDTO> getStockOwnerTmpByOwnerId(Long ownerId, Long ownerType) throws Exception, LogicException {
        return mapper.toDtoBean(repository.getStockOwnerTmpByOwnerId(ownerId, ownerType));
    }
}

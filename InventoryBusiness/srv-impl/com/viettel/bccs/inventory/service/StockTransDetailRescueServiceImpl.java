package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransDetailRescueDTO;
import com.viettel.bccs.inventory.model.StockTransDetailRescue;
import com.viettel.bccs.inventory.repo.StockTransDetailRescueRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
public class StockTransDetailRescueServiceImpl extends BaseServiceImpl implements StockTransDetailRescueService {

    private final BaseMapper<StockTransDetailRescue, StockTransDetailRescueDTO> mapper = new BaseMapper<>(StockTransDetailRescue.class, StockTransDetailRescueDTO.class);

    @Autowired
    private StockTransDetailRescueRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransDetailRescueService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransDetailRescueDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransDetailRescueDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransDetailRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransDetailRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransDetailRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public StockTransDetailRescueDTO save(StockTransDetailRescueDTO stockTransDetailRescueDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransDetailRescueDTO)));
    }

    public List<StockTransDetailRescueDTO> viewDetail(Long stockTransId) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(stockTransId)) {
            throw new LogicException("", "stock.rescue.warranty.view.detail.not.esxit");
        }
        return repository.viewDetail(stockTransId);
    }

    public List<StockTransDetailRescueDTO> getCountLstDetail(Long stockTransId) throws LogicException, Exception {
        return repository.getCountLstDetail(stockTransId);
    }
}

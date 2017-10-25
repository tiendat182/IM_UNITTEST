package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockTransSerialRescueDTO;
import com.viettel.bccs.inventory.model.StockTransSerialRescue;
import com.viettel.bccs.inventory.repo.StockTransSerialRescueRepo;
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
public class StockTransSerialRescueServiceImpl extends BaseServiceImpl implements StockTransSerialRescueService {

    private final BaseMapper<StockTransSerialRescue, StockTransSerialRescueDTO> mapper = new BaseMapper<>(StockTransSerialRescue.class, StockTransSerialRescueDTO.class);

    @Autowired
    private StockTransSerialRescueRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransSerialRescueService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransSerialRescueDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransSerialRescueDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransSerialRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransSerialRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransSerialRescueDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public StockTransSerialRescueDTO save(StockTransSerialRescueDTO stockTransSerialRescueDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransSerialRescueDTO)));
    }

    @WebMethod
    public List<StockTransSerialRescueDTO> viewDetailSerail(Long stockTransId, Long prodOfferId, Long prodOfferIdReturn) throws LogicException, Exception {
        return repository.viewDetailSerail(stockTransId, prodOfferId, prodOfferIdReturn);
    }

    @WebMethod
    public List<StockTransSerialRescueDTO> viewDetailSerailByStockTransId(Long stockTransId) throws LogicException, Exception {
        return repository.viewDetailSerail(stockTransId, null, null);
    }

    @WebMethod
    public List<StockTransSerialRescueDTO> getListDetailSerial(Long stockTransId) throws LogicException, Exception {
        return repository.getListDetailSerial(stockTransId);
    }
}

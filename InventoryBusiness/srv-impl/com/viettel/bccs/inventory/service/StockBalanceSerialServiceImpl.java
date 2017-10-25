package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockBalanceSerialDTO;
import com.viettel.bccs.inventory.model.StockBalanceSerial;
import com.viettel.bccs.inventory.repo.StockBalanceSerialRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
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
public class StockBalanceSerialServiceImpl extends BaseServiceImpl implements StockBalanceSerialService {

    private final BaseMapper<StockBalanceSerial, StockBalanceSerialDTO> mapper = new BaseMapper<>(StockBalanceSerial.class, StockBalanceSerialDTO.class);

    @Autowired
    private StockBalanceSerialRepo repository;
    public static final Logger logger = Logger.getLogger(StockBalanceSerialService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockBalanceSerialDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockBalanceSerialDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockBalanceSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockBalanceSerialDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockBalanceSerialDTO save(StockBalanceSerialDTO dto) throws Exception {
        try {
            StockBalanceSerial stockBalanceSerial = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockBalanceSerial);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public List<StockBalanceSerialDTO> getListStockBalanceSerialDTO(Long stockBalanceDetailID) throws Exception {
        return mapper.toDtoBean(repository.getListStockBalanceSerial(stockBalanceDetailID));
    }
}

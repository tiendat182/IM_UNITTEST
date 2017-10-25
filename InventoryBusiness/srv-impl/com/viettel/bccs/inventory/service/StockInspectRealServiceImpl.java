package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockInspectRealDTO;
import com.viettel.bccs.inventory.model.StockInspectReal;
import com.viettel.bccs.inventory.repo.StockInspectRealRepo;
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
public class StockInspectRealServiceImpl extends BaseServiceImpl implements StockInspectRealService {

    private final BaseMapper<StockInspectReal, StockInspectRealDTO> mapper = new BaseMapper<>(StockInspectReal.class, StockInspectRealDTO.class);

    @Autowired
    private StockInspectRealRepo repository;
    public static final Logger logger = Logger.getLogger(StockInspectRealService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockInspectRealDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockInspectRealDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockInspectRealDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockInspectRealDTO save(StockInspectRealDTO dto) throws Exception {
        try {
            StockInspectReal stockInspectReal = repository.saveAndFlush(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockInspectReal);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockInspectRealDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public BaseMessage delete(StockInspectRealDTO stockInspectRealDTO) throws Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            repository.delete(mapper.toPersistenceBean(stockInspectRealDTO));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "brasIpPool.delete");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    public List<StockInspectRealDTO> getStockInspectReal(Long stockInspectId) throws LogicException, Exception {
        return mapper.toDtoBean(repository.getStockInspectReal(stockInspectId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteStockInspect(Long stockInspectId) throws LogicException, Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            repository.deleteStockInspectReal(stockInspectId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happen");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }
}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockInspectSysDTO;
import com.viettel.bccs.inventory.model.StockInspectSys;
import com.viettel.bccs.inventory.repo.StockInspectSysRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
public class StockInspectSysServiceImpl extends BaseServiceImpl implements StockInspectSysService {

    private final BaseMapper<StockInspectSys, StockInspectSysDTO> mapper = new BaseMapper<>(StockInspectSys.class, StockInspectSysDTO.class);

    @Autowired
    private StockInspectSysRepo repository;
    public static final Logger logger = Logger.getLogger(StockInspectSysService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockInspectSysDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockInspectSysDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockInspectSysDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockInspectSysDTO save(StockInspectSysDTO dto) throws Exception {
        try {
            StockInspectSys stockInspectSys = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockInspectSys);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockInspectSysDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(Long stockInspectId) throws Exception {
        BaseMessage result = new BaseMessage(true);
        if (DataUtil.isNullObject(stockInspectId)) {
            return null;
        }
        try {
            StockInspectSysDTO stockInspectSysDTO = findByStockInspectId(stockInspectId);
            repository.delete(mapper.toPersistenceBean(stockInspectSysDTO));
        } catch (LogicException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @WebMethod
    public StockInspectSysDTO findByStockInspectId(Long stockInspectId) throws Exception {
        List<FilterRequest> lst ;
        List<StockInspectSysDTO> lstResult ;
        lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockInspectSys.COLUMNS.STOCKINSPECTID.name(), FilterRequest.Operator.EQ, stockInspectId));
        lstResult = findByFilter(lst);
        StockInspectSysDTO stockInspectSysDTO = findOne(lstResult.get(0).getStockInspectSysId());
        return stockInspectSysDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteStockInspectSys(Long stockInspectId) throws Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            repository.deleteStockInspectSys(stockInspectId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happen");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }
}

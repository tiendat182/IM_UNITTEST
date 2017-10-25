package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockHandsetLendDTO;
import com.viettel.bccs.inventory.model.StockHandsetLend;
import com.viettel.bccs.inventory.repo.StockHandsetLendRepo;
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

import javax.jws.WebMethod;
import java.util.List;

@Service
public class StockHandsetLendServiceImpl extends BaseServiceImpl implements StockHandsetLendService {

    private final BaseMapper<StockHandsetLend, StockHandsetLendDTO> mapper = new BaseMapper(StockHandsetLend.class, StockHandsetLendDTO.class);

    @Autowired
    private StockHandsetLendRepo repository;
    public static final Logger logger = Logger.getLogger(StockHandsetLendService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockHandsetLendDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockHandsetLendDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockHandsetLendDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockHandsetLendDTO dto) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {

            Long stockHandsetLendId = repository.getSequence();
            dto.setId(stockHandsetLendId);
            StockHandsetLend stockHandsetLend = mapper.toPersistenceBean(dto);
            repository.save(stockHandsetLend);
            result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        }catch (Exception ex) {
            logger.error("Error: ", ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stockHandSetLend.error.insert");
        }
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockHandsetLendDTO dto) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {
            StockHandsetLend stockHandsetLend = mapper.toPersistenceBean(dto);
            repository.save(stockHandsetLend);
            result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        }catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stockHandSetLend.error.insert");
        }
        return result;
    }
}

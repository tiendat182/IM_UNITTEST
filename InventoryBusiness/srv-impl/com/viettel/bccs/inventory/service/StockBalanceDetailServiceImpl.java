package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.bccs.inventory.model.StockBalanceDetail;
import com.viettel.bccs.inventory.repo.StockBalanceDetailRepo;
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
public class StockBalanceDetailServiceImpl extends BaseServiceImpl implements StockBalanceDetailService {

    private final BaseMapper<StockBalanceDetail, StockBalanceDetailDTO> mapper = new BaseMapper<>(StockBalanceDetail.class, StockBalanceDetailDTO.class);

    @Autowired
    private StockBalanceDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockBalanceDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockBalanceDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockBalanceDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockBalanceDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockBalanceDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockBalanceDetailDTO save(StockBalanceDetailDTO dto) throws Exception {
        try {
            StockBalanceDetail stockBalanceDetail = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockBalanceDetail);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public List<StockBalanceDetailDTO> getListStockBalanceDetail(Long requestID) throws Exception {
        return repository.getListStockBalanceDetailDTO(requestID);
    }
}

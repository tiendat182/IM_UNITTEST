package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransLogisticDTO;
import com.viettel.bccs.inventory.model.StockTransLogistic;
import com.viettel.bccs.inventory.repo.StockTransLogisticRepo;
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
public class StockTransLogisticServiceImpl extends BaseServiceImpl implements StockTransLogisticService {

    private final BaseMapper<StockTransLogistic, StockTransLogisticDTO> mapper = new BaseMapper(StockTransLogistic.class, StockTransLogisticDTO.class);

    @Autowired
    private StockTransLogisticRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransLogisticService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransLogisticDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransLogisticDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransLogisticDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransLogisticDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransLogisticDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public StockTransLogisticDTO save(StockTransLogisticDTO stockTransLogisticDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransLogisticDTO)));
    }

    @Override
    public StockTransLogisticDTO findByStockTransId(Long stockTransId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockTransLogistic.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ, stockTransId));
        List<StockTransLogisticDTO> result = findByFilter(lst);
        if (!DataUtil.isNullObject(result) && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public List<StockTransLogisticDTO> getLstOrderLogistics(Long id, Long maxRetry, Long maxDay, Long numberThread) throws Exception {
        return repository.getLstOrderLogistics(id, maxRetry, maxDay, numberThread);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStockTransLogistics(Long stockTransLogistic, Long status, BaseMessage baseMessage) throws Exception {
        StockTransLogisticDTO stockTransLogisticDTO = findOne(stockTransLogistic);
        if (!DataUtil.isNullObject(stockTransLogisticDTO)) {
            stockTransLogisticDTO.setStatus(status);
            stockTransLogisticDTO.setLogisticResponseCode(baseMessage.getErrorCode());
            stockTransLogisticDTO.setLogisticDescription(baseMessage.getDescription());
            repository.save(mapper.toPersistenceBean(stockTransLogisticDTO));
        }
    }

    public void updateStockTransLogisticRetry(Long maxRetry, Long stockTransLogistic, BaseMessage baseMessage) throws Exception {
        StockTransLogisticDTO stockTransLogisticDTO = findOne(stockTransLogistic);
        if (!DataUtil.isNullObject(stockTransLogisticDTO)) {
            if (!DataUtil.isNullOrZero(stockTransLogisticDTO.getLogisticRetry())) {
                if (!DataUtil.safeEqual(stockTransLogisticDTO.getLogisticRetry(), maxRetry - 1)) {
                    stockTransLogisticDTO.setLogisticRetry(stockTransLogisticDTO.getLogisticRetry() + 1);
                } else {
                    stockTransLogisticDTO.setLogisticRetry(stockTransLogisticDTO.getLogisticRetry() + 1);
                    stockTransLogisticDTO.setStatus(com.viettel.fw.common.util.Const.LOGISTICS_STATUS.FAIL);
                }
            } else {
                stockTransLogisticDTO.setLogisticRetry(1L);
            }
            stockTransLogisticDTO.setLogisticResponseCode(baseMessage.getErrorCode());
            stockTransLogisticDTO.setLogisticDescription(baseMessage.getDescription());
            repository.save(mapper.toPersistenceBean(stockTransLogisticDTO));
        }
    }
}

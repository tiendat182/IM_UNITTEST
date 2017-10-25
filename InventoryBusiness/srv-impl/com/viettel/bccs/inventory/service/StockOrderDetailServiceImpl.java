package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockOrderDetailDTO;
import com.viettel.bccs.inventory.model.StockOrderDetail;
import com.viettel.bccs.inventory.repo.StockOrderDetailRepo;
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
public class StockOrderDetailServiceImpl extends BaseServiceImpl implements StockOrderDetailService {

    private final BaseMapper<StockOrderDetail, StockOrderDetailDTO> mapper = new BaseMapper<>(StockOrderDetail.class, StockOrderDetailDTO.class);

    @Autowired
    private StockOrderDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockOrderDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockOrderDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockOrderDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockOrderDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockOrderDetailDTO save(StockOrderDetailDTO dto) throws Exception {
        try {
            StockOrderDetail stockOrderDetail = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockOrderDetail);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockOrderDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

}

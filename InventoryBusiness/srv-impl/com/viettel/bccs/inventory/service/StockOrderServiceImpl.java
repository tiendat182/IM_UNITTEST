package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockOrderDTO;
import com.viettel.bccs.inventory.model.StockOrder;
import com.viettel.bccs.inventory.repo.StockOrderRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

@Service
public class StockOrderServiceImpl extends BaseServiceImpl implements StockOrderService {

    private final BaseMapper<StockOrder, StockOrderDTO> mapper = new BaseMapper<>(StockOrder.class, StockOrderDTO.class);

    @Autowired
    private StockOrderRepo repository;
    public static final Logger logger = Logger.getLogger(StockOrderService.class);

    @WebMethod
    public StockOrderDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockOrderDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockOrderDTO saveStockOrder(StockOrderDTO dto) throws Exception {
        try {
            StockOrder stockOrderDetail = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(stockOrderDetail);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockOrderDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

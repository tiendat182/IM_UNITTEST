package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockOrderAgentDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.bccs.inventory.model.StockOrderAgentDetail;
import com.viettel.bccs.inventory.repo.StockOrderAgentDetailRepo;
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
public class StockOrderAgentDetailServiceImpl extends BaseServiceImpl implements StockOrderAgentDetailService {

    private final BaseMapper<StockOrderAgentDetail, StockOrderAgentDetailDTO> mapper = new BaseMapper<>(StockOrderAgentDetail.class, StockOrderAgentDetailDTO.class);

    @Autowired
    private StockOrderAgentDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockOrderAgentDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockOrderAgentDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockOrderAgentDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockOrderAgentDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockOrderAgentDetailDTO create(StockOrderAgentDetailDTO dto) throws Exception {
        try {
            return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockOrderAgentDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public List<StockOrderAgentDetailDTO> getByStockOrderAgentId(Long id) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(StockOrderAgentDetail.COLUMNS.STOCKORDERAGENTID.name(), FilterRequest.Operator.EQ, id));
        return findByFilter(lst);
    }
	
	 @Override
    public List<StockTransFullDTO> getListGood(Long orderAgentId) {
        return repository.getListGood(orderAgentId);
    }
}

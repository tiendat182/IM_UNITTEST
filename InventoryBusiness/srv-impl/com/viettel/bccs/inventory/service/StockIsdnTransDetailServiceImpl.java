package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockIsdnTransDetailDTO;
import com.viettel.bccs.inventory.model.StockIsdnTransDetail;
import com.viettel.bccs.inventory.repo.StockIsdnTransDetailRepo;
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
public class StockIsdnTransDetailServiceImpl extends BaseServiceImpl implements StockIsdnTransDetailService {

    private final BaseMapper<StockIsdnTransDetail, StockIsdnTransDetailDTO> mapper = new BaseMapper<>(StockIsdnTransDetail.class, StockIsdnTransDetailDTO.class);

    @Autowired
    private StockIsdnTransDetailRepo repository;
    public static final Logger logger = Logger.getLogger(StockIsdnTransDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockIsdnTransDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockIsdnTransDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockIsdnTransDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockIsdnTransDetailDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        BaseMessage msg = new BaseMessage();
        msg.setSuccess(true);
        return msg;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockIsdnTransDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<StockIsdnTransDetailDTO> searchByStockIsdnTransId(Long stockIsdnTransId) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList(new FilterRequest(StockIsdnTransDetail.COLUMNS.STOCKISDNTRANSID.name(), FilterRequest.Operator.EQ, stockIsdnTransId));
        return findByFilter(filters);
    }
}

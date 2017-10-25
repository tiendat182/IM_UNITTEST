package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.StockTotalAuditIm1DTO;
import com.viettel.bccs.im1.model.StockTotalAuditIm1;
import com.viettel.bccs.im1.repo.StockTotalAuditIm1Repo;
import com.viettel.fw.Exception.LogicException;
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
public class StockTotalAuditIm1ServiceImpl extends BaseServiceImpl implements StockTotalAuditIm1Service {

    private final BaseMapper<StockTotalAuditIm1, StockTotalAuditIm1DTO> mapper = new BaseMapper<>(StockTotalAuditIm1.class, StockTotalAuditIm1DTO.class);

    @Autowired
    private StockTotalAuditIm1Repo repository;
    public static final Logger logger = Logger.getLogger(StockTotalAuditIm1Service.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTotalAuditIm1DTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTotalAuditIm1DTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTotalAuditIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }


    @WebMethod
    public int create(StockTotalAuditIm1DTO dto) throws LogicException,Exception {
        return repository.createStockTotalAudit(dto);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTotalAuditIm1DTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public StockTotalAuditIm1DTO save(StockTotalAuditIm1DTO stockTotalDTO) throws Exception {

        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTotalDTO)));
    }



}

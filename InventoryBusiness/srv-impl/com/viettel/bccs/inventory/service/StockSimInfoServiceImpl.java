package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockSimInfoDTO;
import com.viettel.bccs.inventory.model.StockSimInfo;
import com.viettel.bccs.inventory.repo.StockSimInfoRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class StockSimInfoServiceImpl extends BaseServiceImpl implements StockSimInfoService {

    private final BaseMapper<StockSimInfo,StockSimInfoDTO> mapper = new BaseMapper(StockSimInfo.class,StockSimInfoDTO.class);

    @Autowired
    private StockSimInfoRepo repository;
    public static final Logger logger = Logger.getLogger(StockSimInfoService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockSimInfoDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockSimInfoDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockSimInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

}

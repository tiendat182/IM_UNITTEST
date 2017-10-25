package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.RequestLiquidateSerialDTO;
import com.viettel.bccs.inventory.model.RequestLiquidateSerial;
import com.viettel.bccs.inventory.repo.RequestLiquidateSerialRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class RequestLiquidateSerialServiceImpl extends BaseServiceImpl implements RequestLiquidateSerialService {

    private final BaseMapper<RequestLiquidateSerial, RequestLiquidateSerialDTO> mapper = new BaseMapper<>(RequestLiquidateSerial.class, RequestLiquidateSerialDTO.class);

    @Autowired
    private RequestLiquidateSerialRepo repository;
    public static final Logger logger = Logger.getLogger(RequestLiquidateSerialService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RequestLiquidateSerialDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<RequestLiquidateSerialDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<RequestLiquidateSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RequestLiquidateSerialDTO save(RequestLiquidateSerialDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    public List<RequestLiquidateSerialDTO> getLstRequestLiquidateSerialDTO(Long requestLiquidateDetailId) throws Exception {
        List<FilterRequest> requests = Lists.newArrayList(new FilterRequest(RequestLiquidateSerial.COLUMNS.REQUESTLIQUIDATEDETAILID.name(),
                FilterRequest.Operator.EQ, requestLiquidateDetailId));
        List<RequestLiquidateSerialDTO> lstResult = findByFilter(requests);
        return lstResult;
    }
}

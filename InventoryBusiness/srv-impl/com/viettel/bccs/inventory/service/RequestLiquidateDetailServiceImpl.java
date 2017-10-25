package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RequestLiquidateDetailDTO;
import com.viettel.bccs.inventory.model.RequestLiquidateDetail;
import com.viettel.bccs.inventory.repo.RequestLiquidateDetailRepo;
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
public class RequestLiquidateDetailServiceImpl extends BaseServiceImpl implements RequestLiquidateDetailService {

    private final BaseMapper<RequestLiquidateDetail, RequestLiquidateDetailDTO> mapper = new BaseMapper<>(RequestLiquidateDetail.class, RequestLiquidateDetailDTO.class);

    @Autowired
    private RequestLiquidateDetailRepo repository;
    public static final Logger logger = Logger.getLogger(RequestLiquidateDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RequestLiquidateDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<RequestLiquidateDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<RequestLiquidateDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RequestLiquidateDetailDTO save(RequestLiquidateDetailDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @Override
    public List<RequestLiquidateDetailDTO> getLstRequestLiquidateDetailDTO(Long requestLiquidateId) throws Exception {
        return repository.getLstRequestLiquidateDetailDTO(requestLiquidateId);
    }

}

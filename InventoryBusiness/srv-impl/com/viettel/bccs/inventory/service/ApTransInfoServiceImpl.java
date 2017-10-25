package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ApTransInfoDTO;
import com.viettel.bccs.inventory.model.ApTransInfo;
import com.viettel.bccs.inventory.repo.ApTransInfoRepo;
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
public class ApTransInfoServiceImpl extends BaseServiceImpl implements ApTransInfoService {

    private final BaseMapper<ApTransInfo, ApTransInfoDTO> mapper = new BaseMapper<>(ApTransInfo.class, ApTransInfoDTO.class);

    @Autowired
    private ApTransInfoRepo repository;
    public static final Logger logger = Logger.getLogger(ApTransInfoService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ApTransInfoDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ApTransInfoDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ApTransInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ApTransInfoDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ApTransInfoDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

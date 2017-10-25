package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.VcRequestDTO;
import com.viettel.bccs.im1.model.VcRequest;
import com.viettel.bccs.im1.repo.VcRequestRepo;
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
public class VcRequestServiceImpl extends BaseServiceImpl implements VcRequestService {

    private final BaseMapper<VcRequest, VcRequestDTO> mapper = new BaseMapper<>(VcRequest.class, VcRequestDTO.class);

    @Autowired
    private VcRequestRepo repository;
    public static final Logger logger = Logger.getLogger(VcRequestService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public VcRequestDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<VcRequestDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<VcRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public VcRequestDTO create(VcRequestDTO dto) throws Exception {
        try {
            VcRequest vcRequest = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(vcRequest);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(VcRequestDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.ReqActivateKitDTO;
import com.viettel.bccs.im1.model.ReqActivateKit;
import com.viettel.bccs.im1.repo.ReqActivateKitRepo;
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
public class ReqActivateKitServiceImpl extends BaseServiceImpl implements ReqActivateKitService {

    private final BaseMapper<ReqActivateKit, ReqActivateKitDTO> mapper = new BaseMapper<>(ReqActivateKit.class, ReqActivateKitDTO.class);

    @Autowired
    private ReqActivateKitRepo repository;
    public static final Logger logger = Logger.getLogger(ReqActivateKitService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ReqActivateKitDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ReqActivateKitDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ReqActivateKitDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ReqActivateKitDTO create(ReqActivateKitDTO dto) throws Exception {
        try {
            ReqActivateKit reqActivateKit = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(reqActivateKit);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ReqActivateKitDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

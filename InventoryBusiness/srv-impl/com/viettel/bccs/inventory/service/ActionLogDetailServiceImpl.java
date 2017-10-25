package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.bccs.inventory.model.ActionLogDetail;
import com.viettel.bccs.inventory.repo.ActionLogDetailRepo;
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
public class ActionLogDetailServiceImpl extends BaseServiceImpl implements ActionLogDetailService {

    private final BaseMapper<ActionLogDetail, ActionLogDetailDTO> mapper = new BaseMapper<>(ActionLogDetail.class, ActionLogDetailDTO.class);

    @Autowired
    private ActionLogDetailRepo repository;
    public static final Logger logger = Logger.getLogger(ActionLogDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ActionLogDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ActionLogDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ActionLogDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ActionLogDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ActionLogDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ActionLogDetailDTO save(ActionLogDetailDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }
}

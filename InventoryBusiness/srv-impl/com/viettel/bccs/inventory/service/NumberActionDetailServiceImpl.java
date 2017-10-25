package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.NumberActionDetailDTO;
import com.viettel.bccs.inventory.model.NumberActionDetail;
import com.viettel.bccs.inventory.repo.NumberActionDetailRepo;
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
public class NumberActionDetailServiceImpl extends BaseServiceImpl implements NumberActionDetailService {

    private final BaseMapper<NumberActionDetail, NumberActionDetailDTO> mapper = new BaseMapper(NumberActionDetail.class, NumberActionDetailDTO.class);

    @Autowired
    private NumberActionDetailRepo repository;
    public static final Logger logger = Logger.getLogger(NumberActionDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public NumberActionDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<NumberActionDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<NumberActionDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(NumberActionDetailDTO dto) throws Exception {
        repository.save(mapper.toPersistenceBean(dto));
        BaseMessage msg = new BaseMessage();
        msg.setSuccess(true);
        return msg;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(NumberActionDetailDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

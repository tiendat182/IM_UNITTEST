package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.repo.ChangeModelTransSerialRepo;
import com.viettel.bccs.inventory.dto.ChangeModelTransSerialDTO;
import com.viettel.bccs.inventory.model.ChangeModelTransSerial;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeModelTransSerialServiceImpl extends BaseServiceImpl implements ChangeModelTransSerialService {

    private final BaseMapper<ChangeModelTransSerial, ChangeModelTransSerialDTO> mapper = new BaseMapper<>(ChangeModelTransSerial.class, ChangeModelTransSerialDTO.class);

    @Autowired
    private ChangeModelTransSerialRepo repository;
    public static final Logger logger = Logger.getLogger(ChangeModelTransSerialService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ChangeModelTransSerialDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ChangeModelTransSerialDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ChangeModelTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ChangeModelTransSerialDTO save(ChangeModelTransSerialDTO dto) throws Exception {
        ChangeModelTransSerial changeModelTransSerial = repository.save(mapper.toPersistenceBean(dto));
        return mapper.toDtoBean(changeModelTransSerial);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ChangeModelTransSerialDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public List<ChangeModelTransSerialDTO> viewDetailSerial(Long changeModelTransDetailId) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        filters.add(new FilterRequest(ChangeModelTransSerial.COLUMNS.CHANGEMODELTRANSDETAILID.name(), FilterRequest.Operator.EQ, changeModelTransDetailId));
        return findByFilter(filters);
    }
}

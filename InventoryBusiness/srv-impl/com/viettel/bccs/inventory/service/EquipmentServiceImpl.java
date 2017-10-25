package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.EquipmentDTO;
import com.viettel.bccs.inventory.model.Equipment;
import com.viettel.bccs.inventory.repo.EquipmentRepo;
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
public class EquipmentServiceImpl extends BaseServiceImpl implements EquipmentService {

//    private final EquipmentMapper mapper = new EquipmentMapper();
    private final BaseMapper<Equipment, EquipmentDTO> mapper = new BaseMapper(Equipment.class, EquipmentDTO.class);

    @Autowired
    private EquipmentRepo repository;
    public static final Logger logger = Logger.getLogger(EquipmentService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public EquipmentDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<EquipmentDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<EquipmentDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(EquipmentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(EquipmentDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<EquipmentDTO> getEquipmentBras() throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Equipment.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        lst.add(new FilterRequest(Equipment.COLUMNS.EQUIPMENTTYPE.name(), FilterRequest.Operator.EQ, Const.BRAS.EQUIPMENT_TYPE));
        return findByFilter(lst);
    }
}

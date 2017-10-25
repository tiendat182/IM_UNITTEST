package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.EquipmentVendorDTO;
import com.viettel.bccs.inventory.model.EquipmentVendor;
import com.viettel.bccs.inventory.repo.EquipmentVendorRepo;
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
public class EquipmentVendorServiceImpl extends BaseServiceImpl implements EquipmentVendorService {

//    private final EquipmentVendorMapper mapper = new EquipmentVendorMapper();
    private final BaseMapper<EquipmentVendor, EquipmentVendorDTO> mapper = new BaseMapper(EquipmentVendor.class, EquipmentVendorDTO.class);

    @Autowired
    private EquipmentVendorRepo repository;
    public static final Logger logger = Logger.getLogger(EquipmentVendorService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public EquipmentVendorDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<EquipmentVendorDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<EquipmentVendorDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(EquipmentVendorDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(EquipmentVendorDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

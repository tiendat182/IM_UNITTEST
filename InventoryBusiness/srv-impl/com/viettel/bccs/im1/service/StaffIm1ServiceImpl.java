package com.viettel.bccs.im1.service;


import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.bccs.im1.model.StaffIm1;
import com.viettel.bccs.im1.repo.StaffIm1Repo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;

@Service
public class StaffIm1ServiceImpl extends BaseServiceImpl implements StaffIm1Service {

    private final BaseMapper<StaffIm1, StaffIm1DTO> mapper = new BaseMapper(StaffIm1.class, StaffIm1DTO.class);

    @Autowired
    private StaffIm1Repo repository;

    public static final Logger logger = Logger.getLogger(StaffIm1Service.class);

    @WebMethod
    public StaffIm1DTO findOne(Long id) throws Exception, LogicException {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @Override
    public StaffIm1DTO findOneStaff(Long id) throws Exception, LogicException {
        return mapper.toDtoBean(repository.findOneStaff(id));
    }

    @WebMethod
    public StaffIm1DTO save(StaffIm1DTO staffIm1DTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(staffIm1DTO)));
    }
}

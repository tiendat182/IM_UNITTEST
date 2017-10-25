package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.RevokeKitTransDTO;
import com.viettel.bccs.inventory.model.RevokeKitTrans;
import com.viettel.bccs.inventory.repo.RevokeKitTransRepo;
import com.viettel.fw.Exception.LogicException;
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
public class RevokeKitTransServiceImpl extends BaseServiceImpl implements RevokeKitTransService {

    private static final BaseMapper<RevokeKitTrans, RevokeKitTransDTO> mapper = new BaseMapper<>(RevokeKitTrans.class, RevokeKitTransDTO.class);

    @Autowired
    private RevokeKitTransRepo repository;
    public static final Logger logger = Logger.getLogger(RevokeKitTransService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public RevokeKitTransDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public RevokeKitTransDTO save(RevokeKitTransDTO dto) throws LogicException, Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }
}

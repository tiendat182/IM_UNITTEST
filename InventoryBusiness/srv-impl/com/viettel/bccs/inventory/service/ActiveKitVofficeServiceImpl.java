package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.repo.ActiveKitVofficeRepo;
import com.viettel.bccs.inventory.dto.ActiveKitVofficeDTO;
import com.viettel.bccs.inventory.model.ActiveKitVoffice;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
public class ActiveKitVofficeServiceImpl extends BaseServiceImpl implements ActiveKitVofficeService {

    private final BaseMapper<ActiveKitVoffice, ActiveKitVofficeDTO> mapper = new BaseMapper<>(ActiveKitVoffice.class, ActiveKitVofficeDTO.class);

    @Autowired
    private ActiveKitVofficeRepo repository;
    public static final Logger logger = Logger.getLogger(ActiveKitVofficeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ActiveKitVofficeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ActiveKitVofficeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ActiveKitVofficeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ActiveKitVofficeDTO save(ActiveKitVofficeDTO dto) throws Exception {
        ActiveKitVoffice activeKitVoffice = repository.save(mapper.toPersistenceBean(dto));
        return mapper.toDtoBean(activeKitVoffice);
    }

    @WebMethod
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ActiveKitVofficeDTO dto) throws Exception, LogicException {
        if (DataUtil.isNullObject(dto)) {
            throw new LogicException("", getText("mn.active.kit.voffice.validate.save"));
        }
        BaseMessage messOut = new BaseMessage();
        save(mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto))));
        messOut.setSuccess(true);
        return messOut;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ActiveKitVofficeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    public ActiveKitVofficeDTO findByChangeModelTransId(Long changeModelTransId) throws Exception {
        return mapper.toDtoBean(repository.findByChangeModelTransId(changeModelTransId));
    }
}

package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.StockHandsetRescueDTO;
import com.viettel.bccs.inventory.model.StockHandsetRescue;
import com.viettel.bccs.inventory.repo.StockHandsetRescueRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class StockHandsetRescueServiceImpl extends BaseServiceImpl implements StockHandsetRescueService {

    private final BaseMapper<StockHandsetRescue, StockHandsetRescueDTO> mapper = new BaseMapper(StockHandsetRescue.class, StockHandsetRescueDTO.class);

    @Autowired
    private StockHandsetRescueRepo repository;
    public static final Logger logger = Logger.getLogger(StockHandsetRescueService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockHandsetRescueDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockHandsetRescueDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockHandsetRescueDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockHandsetRescueDTO dto) throws Exception {
        BaseMessage result = new BaseMessage();
        mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        result.setSuccess(true);
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockHandsetRescueDTO dto) throws Exception {
        BaseMessage result = new BaseMessage();
        mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        result.setSuccess(true);
        return result;
    }

    @WebMethod
    public List<StockHandsetRescueDTO> getListHansetRescue(Long ownerId) throws LogicException, Exception {
        return repository.getListHansetRescue(ownerId);
    }

    @WebMethod
    public List<StockHandsetRescueDTO> getListProductForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception {
        return repository.getListProductForRescue(ownerId, lstSerial);
    }

    @WebMethod
    public List<StockHandsetRescueDTO> getListSerialForRescue(Long ownerId, List<String> lstSerial) throws LogicException, Exception {
        return repository.getListSerialForRescue(ownerId, lstSerial);
    }

    public int updateStatusSerialForRescue(Long statusAffer, Long statusBefor, Long ownerId, String serial, Long prodOfferId) throws LogicException, Exception {
        return repository.updateStatusSerialForRescue(statusAffer, statusBefor, ownerId, serial, prodOfferId);
    }
}

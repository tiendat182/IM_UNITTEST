package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DepositDTO;
import com.viettel.bccs.inventory.model.Deposit;
import com.viettel.bccs.inventory.repo.DepositRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class DepositServiceImpl extends BaseServiceImpl implements DepositService {

    private final BaseMapper<Deposit, DepositDTO> mapper = new BaseMapper(Deposit.class, DepositDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private DepositRepo repository;
    public static final Logger logger = Logger.getLogger(DepositService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DepositDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DepositDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DepositDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public DepositDTO create(DepositDTO dto) throws Exception {
        try {
            Date currentDate = getSysDate(em);
            dto.setStatus(Const.STATUS_ACTIVE);
            dto.setCreateDate(currentDate);
            Deposit deposit = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(deposit);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public DepositDTO update(Long depositId, String status) throws Exception {
        try {
            DepositDTO depositDTO = findOne(depositId);
            depositDTO.setStatus(status);
            Deposit deposit = repository.save(mapper.toPersistenceBean(depositDTO));
            return mapper.toDtoBean(deposit);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

}

package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceActionLogDTO;
import com.viettel.bccs.inventory.model.InvoiceActionLog;
import com.viettel.bccs.inventory.repo.InvoiceActionLogRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class InvoiceActionLogServiceImpl extends BaseServiceImpl implements InvoiceActionLogService {

    //    private final InvoiceActionLogMapper mapper = new InvoiceActionLogMapper();
    private final BaseMapper<InvoiceActionLog, InvoiceActionLogDTO> mapper = new BaseMapper(InvoiceActionLog.class, InvoiceActionLogDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private InvoiceActionLogRepo repository;
    public static final Logger logger = Logger.getLogger(InvoiceActionLogService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceActionLogDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceActionLogDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceActionLogDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceActionLogDTO dto) throws Exception, LogicException {
        BaseMessage baseMessage = new BaseMessage(false);
        try {
            repository.save(mapper.toPersistenceBean(dto));
            //Luwu log vao bang action_log
            baseMessage.setSuccess(true);
            return baseMessage;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceActionLogDTO dto) throws Exception {
        throw new NotImplementedException();
    }
}

package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ActionLogDetailDTO;
import com.viettel.bccs.inventory.repo.ActionLogRepo;
import com.viettel.bccs.inventory.dto.ActionLogDTO;
import com.viettel.bccs.inventory.model.ActionLog;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;


@Service
public class ActionLogServiceImpl extends BaseServiceImpl implements ActionLogService {

    private final BaseMapper<ActionLog, ActionLogDTO> mapper = new BaseMapper<>(ActionLog.class, ActionLogDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ActionLogRepo repository;
    @Autowired
    private ActionLogDetailService actionLogDetailService;

    public static final Logger logger = Logger.getLogger(ActionLogService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ActionLogDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ActionLogDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ActionLogDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ActionLogDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ActionLogDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ActionLogDTO saveForProcessStock(ActionLogDTO dto, List<ActionLogDetailDTO> lstDetail) throws Exception {
        try {
            Date currentDate = getSysDate(em);
            dto.setActionDate(currentDate);
            ActionLog actionLog = repository.save(mapper.toPersistenceBean(dto));
            //Luu action_log_detail
            for (ActionLogDetailDTO actionLogDetailDTO : lstDetail) {
                actionLogDetailDTO.setActionId(actionLog.getActionId());
                actionLogDetailDTO.setActionDate(currentDate);
                actionLogDetailService.save(actionLogDetailDTO);
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
        return null;
    }
}

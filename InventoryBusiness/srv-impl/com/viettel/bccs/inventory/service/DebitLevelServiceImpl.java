package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.DebitLevel;
import com.viettel.bccs.inventory.repo.DebitLevelRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class DebitLevelServiceImpl extends BaseServiceImpl implements DebitLevelService {

    //private final DebitLevelMapper mapper = new DebitLevelMapper();
    private final BaseMapper<DebitLevel, DebitLevelDTO> mapper = new BaseMapper(DebitLevel.class, DebitLevelDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;
    @Autowired
    private DebitLevelRepo repository;
    @Autowired
    private OptionSetValueService optionSetValueService;
    public static final Logger logger = Logger.getLogger(DebitLevelService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DebitLevelDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DebitLevelDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DebitLevelDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(DebitLevelDTO dto) throws Exception {
        BaseMessage message = validateDebitlelvel(dto, true);
        if (DataUtil.isNullOrEmpty(message.getErrorCode())) {
            dto.setCreateDate(DbUtil.getSysDate(emIM));
            dto.setLastUpdateTime(DbUtil.getSysDate(emIM));
            mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(dto)));
            message.setSuccess(true);
        }
        return message;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(DebitLevelDTO dto) throws Exception {
        BaseMessage message = validateDebitlelvel(dto, false);
        if (DataUtil.isNullOrEmpty(message.getErrorCode())) {
            dto.setLastUpdateTime(DbUtil.getSysDate(emIM));
            mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(dto)));
            message.setSuccess(true);
        }
        return message;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public List<DebitLevelDTO> searchDebitlevel(DebitLevelDTO dto) throws Exception {
        if (DataUtil.isNullObject(dto)) {
            return new ArrayList<DebitLevelDTO>();
        }
        return repository.searchDebitlevel(dto);
    }

    /**
     * ham check debitlevel
     *
     * @return true neu trung lap
     * @throws com.viettel.fw.Exception.LogicException
     * @throws Exception
     * @author ngocdm4
     */
    private BaseMessage validateDebitlelvel(DebitLevelDTO debitLevelDTO, Boolean insert) throws LogicException, Exception {
        BaseMessage message = new BaseMessage();
        List<OptionSetValueDTO> optionSetValueDTOstype = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
        List<OptionSetValueDTO> optionSetValueDTOslevel = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_LEVEL);
        // check null
        if (DataUtil.isNullObject(debitLevelDTO)) {
            message.setKeyMsg("validate.server.null.object");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        }
        if (DataUtil.isNullOrEmpty(debitLevelDTO.getLastUpdateUser()) || DataUtil.isNullOrEmpty(debitLevelDTO.getCreateUser())
                || DataUtil.isNullOrEmpty(debitLevelDTO.getStatus()) || DataUtil.isNullOrEmpty(debitLevelDTO.getDebitLevel())
                || DataUtil.isNullOrEmpty(debitLevelDTO.getDebitDayType()) || debitLevelDTO.getDebitAmount() == null) {
            message.setKeyMsg("quota.esblish.validate.empty");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        } else if (!(debitLevelDTO.getDebitAmount().toString().length() < 20)) {
            message.setKeyMsg("quota.establish.validate.amout");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        } else if (!DataUtil.isNumber(debitLevelDTO.getStatus()) || Integer.parseInt(debitLevelDTO.getStatus()) > 1) {
            message.setKeyMsg("quota.esblish.validate.service.status");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        } else if (!DataUtil.isNumber(debitLevelDTO.getDebitDayType())) {
            message.setKeyMsg("quota.esblish.validate.service.debitdaytype");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        } else if (!DataUtil.isNumber(debitLevelDTO.getDebitLevel())) {
            message.setKeyMsg("quota.esblish.validate.service.debitlevel");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        }
        // validate duplicate insert = true : chuc nang create , false : chuc nang update
        if (insert) {
            boolean Isdebitlevel = false;
            boolean Isdebitdaytype = false;
            for (OptionSetValueDTO lstdebitlevel : optionSetValueDTOslevel) {
                if (lstdebitlevel.getValue().equals(debitLevelDTO.getDebitLevel()))
                    Isdebitlevel = true;
            }
            for (OptionSetValueDTO lstdebitdaytype : optionSetValueDTOstype) {
                if (lstdebitdaytype.getValue().equals(debitLevelDTO.getDebitDayType()))
                    Isdebitdaytype = true;
            }
            if (!Isdebitdaytype) {
                message.setKeyMsg("quota.establish.validate.debitdaytype");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return message;
            }
            if (!Isdebitlevel) {
                message.setKeyMsg("quota.establish.validate.debitlevel");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return message;
            }
            List<FilterRequest> lsRequests = Lists.newArrayList();
            lsRequests.add(new FilterRequest(DebitLevel.COLUMNS.DEBITLEVEL.name(), FilterRequest.Operator.EQ, debitLevelDTO.getDebitLevel()));
            lsRequests.add(new FilterRequest(DebitLevel.COLUMNS.DEBITDAYTYPE.name(), FilterRequest.Operator.EQ, debitLevelDTO.getDebitDayType()));
            lsRequests.add(new FilterRequest(DebitLevel.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, debitLevelDTO.getStatus()));
            List<DebitLevelDTO> lsData = findByFilter(lsRequests);
            if (lsData != null && lsData.size() > 0) {
                message.setKeyMsg("quota.establish.validate.duplicate");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT);
                return message;
            }
        } else {
            DebitLevelDTO checkupdate = findOne(debitLevelDTO.getId());
            if (DataUtil.isNullObject(checkupdate)) {
                message.setKeyMsg("quota.establish.validate.empty");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
                return message;
            } else if (!checkupdate.getDebitLevel().equals(debitLevelDTO.getDebitLevel()) || !checkupdate.getDebitDayType().equals(debitLevelDTO.getDebitDayType())) {
                message.setKeyMsg("quota.establish.validate.not.edit");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
                return message;
            }
        }
        return message;
    }

    public List<DebitLevelDTO> getLstDebitByLevel(Long level) throws Exception {
        List<FilterRequest> lsRequests = Lists.newArrayList();
        lsRequests.add(new FilterRequest(DebitLevel.COLUMNS.DEBITLEVEL.name(), FilterRequest.Operator.EQ, level));
        lsRequests.add(new FilterRequest(DebitLevel.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        return findByFilter(lsRequests);
    }

}

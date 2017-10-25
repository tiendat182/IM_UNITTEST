package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.NumberFilterRule;
import com.viettel.bccs.inventory.repo.NumberFilterRuleRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class NumberFilterRuleServiceImpl extends BaseServiceImpl implements NumberFilterRuleService {


    private final BaseMapper<NumberFilterRule, NumberFilterRuleDTO> mapper = new BaseMapper(NumberFilterRule.class, NumberFilterRuleDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private NumberFilterRuleRepo repository;
    @Autowired
    private StockNumberService stockNumberService;
    public static final Logger logger = Logger.getLogger(NumberFilterRuleService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public NumberFilterRuleDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<NumberFilterRuleDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<NumberFilterRuleDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Override
    public Boolean checkExistNameNumberFilter(String name, Long filterId) throws Exception {
        List<FilterRequest> lsRequest = Lists.newArrayList();
        lsRequest.add(new FilterRequest(NumberFilterRule.COLUMNS.NAME.name(), FilterRequest.Operator.EQ, name.toLowerCase(), false));
        lsRequest.add(new FilterRequest(NumberFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        if (filterId != null) {
            lsRequest.add(new FilterRequest(NumberFilterRule.COLUMNS.FILTERRULEID.name(), FilterRequest.Operator.NE, filterId)); //Not equal
        }
        return !DataUtil.isNullOrEmpty(findByFilter(lsRequest));
    }

    @WebMethod
    public Long checkInsertConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        return repository.checkInsertConditionTelecomServiceId(numberFilterRuleDTO);
    }

    @WebMethod
    public Long checkEditConditionTelecomServiceId(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        return repository.checkEditConditionTelecomServiceId(numberFilterRuleDTO);
    }


    @Override
    public List<NumberFilterRuleDTO> findRuleAggregate(NumberFilterRuleDTO numberFilterRule, boolean extract) throws Exception {
        return repository.findRuleAggregate(numberFilterRule, extract);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(NumberFilterRuleDTO dto, StaffDTO staffDTO) throws Exception {
        // Edit
        if (DataUtil.isNullObject(dto)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "mn.isdn.rule.nice.digit.invalid");
        }
        BaseMessage result = new BaseMessage(true);
        Date sysDate = getSysDate(em);
        dto.setStatus(Const.STATUS.ACTIVE);
        dto.setCreateDate(sysDate);
        dto.setCreateUser(staffDTO.getStaffCode());
        dto.setLastUpdateTime(sysDate);
        dto.setLastUpdateUser(staffDTO.getStaffCode());
        repository.save(mapper.toPersistenceBean(dto));
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(NumberFilterRuleDTO dto, StaffDTO staffDTO) throws Exception {
        if (DataUtil.isNullObject(dto)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "mn.isdn.rule.nice.digit.invalid");
        }
        NumberFilterRuleDTO numberFilterRuleDTO = findOne(dto.getFilterRuleId());
        if (DataUtil.isNullObject(numberFilterRuleDTO)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "mn.isdn.rule.nice.digit.update.fail");
        }
        BaseMessage result = new BaseMessage(true);
        BeanUtils.copyProperties(dto, numberFilterRuleDTO);
        numberFilterRuleDTO.setLastUpdateTime(getSysDate(em));
        numberFilterRuleDTO.setLastUpdateUser(staffDTO.getStaffCode());
        repository.save(mapper.toPersistenceBean(numberFilterRuleDTO));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteListNumberFiler(List<NumberFilterRuleDTO> lst, StaffDTO staffDTO) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        List<Long> lsFilterRuleId = DataUtil.collectProperty(lst, "filterRuleId", Long.class);
        boolean isExist = stockNumberService.checkExistNumberByListFilterRuleId(lsFilterRuleId);
        if (isExist) {
            baseMessage.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            baseMessage.setKeyMsg("mn.isdn.rule.nice.digit.deletelist.fail");
            return baseMessage;
        }
        for (NumberFilterRuleDTO numberFilterRuleDTODTO : lst) {
            numberFilterRuleDTODTO.setStatus(Const.STATUS.NO_ACTIVE);
            numberFilterRuleDTODTO.setLastUpdateTime(getSysDate(em));
            numberFilterRuleDTODTO.setLastUpdateUser(staffDTO.getStaffCode());
            repository.save(mapper.toPersistenceBean(numberFilterRuleDTODTO));
        }
        return baseMessage;
    }

    @Override
    public List<NumberFilterRuleDTO> searchHightOrderRule(Long telecomServiceId, Long minNumber) throws Exception {
        return repository.searchHightOrderRule(telecomServiceId, minNumber);
    }

    public List<NumberFilterRuleDTO> getListNumerFilterRule(NumberFilterRuleDTO numberFilterRuleDTO) throws Exception {
        List<FilterRequest> lstRq = Lists.newArrayList();
        if (!DataUtil.isNullObject(numberFilterRuleDTO.getGroupFilterRuleId())) {
            FilterRequest rq1 = new FilterRequest(NumberFilterRule.COLUMNS.GROUPFILTERRULEID.name(), FilterRequest.Operator.EQ, numberFilterRuleDTO.getGroupFilterRuleId());
            lstRq.add(rq1);
        }
        lstRq.add(new FilterRequest(NumberFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return findByFilter(lstRq);
    }

    @Override
    public boolean checkBeforeDelete(Long groupFilterId) throws Exception {
        List<FilterRequest> listRq = Lists.newArrayList();
        FilterRequest rq1 = new FilterRequest(NumberFilterRule.COLUMNS.GROUPFILTERRULEID.name(), FilterRequest.Operator.EQ, groupFilterId);
        listRq.add(rq1);
        FilterRequest rq2 = new FilterRequest(NumberFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE);
        listRq.add(rq2);
        return DataUtil.isNullOrEmpty(findByFilter(listRq));
    }
}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.GroupFilterRuleDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.GroupFilterRule;
import com.viettel.bccs.inventory.model.QGroupFilterRule;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.GroupFilterRuleRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GroupFilterRuleServiceImpl extends BaseServiceImpl implements GroupFilterRuleService {

    //    private final GroupFilterRuleMapper mapper = new GroupFilterRuleMapper();
    private final BaseMapper<GroupFilterRule, GroupFilterRuleDTO> mapper = new BaseMapper(GroupFilterRule.class, GroupFilterRuleDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private GroupFilterRuleRepo repository;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StaffService staffService;

    public static final Logger logger = Logger.getLogger(GroupFilterRuleService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public GroupFilterRuleDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<GroupFilterRuleDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<GroupFilterRuleDTO> findByFilter(List<FilterRequest> filters) throws LogicException, Exception {
        OrderSpecifier<Date> sortByUpdateTime = QGroupFilterRule.groupFilterRule.lastUpdateTime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByUpdateTime));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public GroupFilterRuleDTO save(GroupFilterRuleDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto.getGroupFilterRuleCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "ws.group.code.not.null");
        } else if (dto.getGroupFilterRuleCode().length() > 20) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.group.code.over.maxlength");
        }

        if (!dto.getGroupFilterRuleCode().matches("[a-zA-Z0-9_]*")) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "rule.group.code.invalid");
        }

        if (DataUtil.isNullObject(dto.getName())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "ws.group.name.not.null");
        } else if (dto.getName().length() > 100) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.group.name.over.maxlength");
        }
//        if (!DataUtil.validateStringByPattern(dto.getName(), "^[" + BundleUtil.getText(new Locate("vi", "VN"), "input.alpha.num.vn") + "]+$")) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "rule.group.name");
//        }

        if (DataUtil.isNullObject(dto.getParentId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "ws.rule.not.null");
        } else {
            GroupFilterRule rule = repository.findOne(dto.getParentId());
            if (DataUtil.isNullObject(rule)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "ws.rule.notexist");
            }
        }
        if (DataUtil.isNullObject(dto.getGroupType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "ws.group.type.not.null");
        }
        if (!DataUtil.isNullObject(dto.getNotes()) && dto.getNotes().length() > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "ws.notes.over.maxlength");
        }
        List<FilterRequest> lstRQ = Lists.newArrayList();
        FilterRequest rq2 = new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULECODE.name(), FilterRequest.Operator.EQ, dto.getGroupFilterRuleCode().trim().toLowerCase());
        rq2.setExtract(false);
        lstRQ.add(rq2);
        FilterRequest rq5 = new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE);
        lstRQ.add(rq5);
        FilterRequest rq6 = new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NOT_NULL);
        lstRQ.add(rq6);
        if (!DataUtil.isNullObject(dto.getGroupFilterRuleId())) {
            FilterRequest rq3 = new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULEID.name(), FilterRequest.Operator.NE, dto.getGroupFilterRuleId());
            lstRQ.add(rq3);
        }
        if (!DataUtil.isNullOrEmpty(findByFilter(lstRQ))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "ws.group.code.existed");
        }
        lstRQ.clear();
        FilterRequest rq1 = new FilterRequest(GroupFilterRule.COLUMNS.NAME.name(), FilterRequest.Operator.EQ, dto.getName().trim().toLowerCase());
        rq1.setExtract(false);
        lstRQ.add(rq1);
        FilterRequest rq4 = new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE);
        lstRQ.add(rq4);
        FilterRequest rq7 = new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NOT_NULL);
        lstRQ.add(rq7);
        if (!DataUtil.isNullObject(dto.getGroupFilterRuleId())) {
            FilterRequest rq3 = new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULEID.name(), FilterRequest.Operator.NE, dto.getGroupFilterRuleId());
            lstRQ.add(rq3);
        }
        if (!DataUtil.isNullOrEmpty(findByFilter(lstRQ))) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "ws.group.name.existed");
        }

        if (!DataUtil.isNullObject(dto.getGroupFilterRuleId())) {
            dto.setLastUpdateTime(getSysDate(em));
        } else {
            dto.setCreateDate(getSysDate(em));
            dto.setLastUpdateTime(getSysDate(em));
        }
        GroupFilterRuleDTO _return = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        return _return;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(GroupFilterRuleDTO dto, String userName) throws LogicException, Exception {
        GroupFilterRuleDTO dtoToSave = DataUtil.cloneBean(dto);
        dtoToSave.setName(dto.getName().trim());
        dtoToSave.setNotes(dto.getNotes().trim());
        dtoToSave.setStatus(dto.getStatus().trim());
        userName = userName.trim();
        if (DataUtil.isNullObject(dtoToSave)) {
            throw new LogicException("", "mn.isdn.rule.data.invalid");
        }
        if(DataUtil.isNullObject(userName)){
            throw new LogicException("", "mn.isdn.rule.user.require");
        }
        if(userName.length() > 20){
            throw new LogicException("", "mn.isdn.rule.user.maxLength");
        }
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Staff.COLUMNS.STAFFCODE.name(), FilterRequest.Operator.EQ, userName));
        lst.add(new FilterRequest(Staff.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        if(DataUtil.isNullOrEmpty(staffService.findByFilter(lst))){
            throw new LogicException("", "mn.isdn.rule.user.invalid");
        }
        //validate du lieu
        validate(dtoToSave);
        //Check da ton tai chua
        if (!DataUtil.isNullObject(dtoToSave.getGroupFilterRuleCode())) {
            List<FilterRequest> requests = Lists.newArrayList();
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NULL));
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULECODE.name(), FilterRequest.Operator.EQ, dtoToSave.getGroupFilterRuleCode().toLowerCase(), false));
            List<GroupFilterRuleDTO> code = findByFilter(requests);
            if (!DataUtil.isNullOrEmpty(code)) {
                throw new LogicException("", "mn.isdn.rule.add.findOne.exist.code");
            }
        }
        if (!DataUtil.isNullObject(dtoToSave.getName())) {
            List<FilterRequest> requests = Lists.newArrayList();
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NULL));
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.NAME.name(), FilterRequest.Operator.EQ, dtoToSave.getName().toLowerCase(), false));
            List<GroupFilterRuleDTO> name = findByFilter(requests);
            if (!DataUtil.isNullOrEmpty(name)) {
                throw new LogicException("", "mn.isdn.rule.add.findOne.exist.name");
            }
        }

        BaseMessage result = new BaseMessage(true);

        try {
            dtoToSave.setLastUpdateTime(optionSetValueService.getSysdateFromDB(false));
            dtoToSave.setCreateDate(optionSetValueService.getSysdateFromDB(false));
            dtoToSave.setStatus(Const.STATUS.ACTIVE);
            dtoToSave.setLastUpdateUser(userName);
            dtoToSave.setCreateUser(userName);
            GroupFilterRule add = repository.save(mapper.toPersistenceBean(dtoToSave));
            if (DataUtil.isNullObject(add)) {
                throw new LogicException("", "mn.isdn.rule.add.fail");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
        }
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(GroupFilterRuleDTO dto, String userName) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto)) {
            throw new LogicException("", "mn.isdn.rule.data.invalid");
        }
        GroupFilterRuleDTO groupFilterRuleDTO = findOne(dto.getGroupFilterRuleId());
        if (DataUtil.isNullObject(groupFilterRuleDTO)) {
            throw new LogicException("", "mn.isdn.rule.update.findOne");
        }

        //validate du lieu
        validate(dto);
        //Check da ton tai chua
        if (!DataUtil.isNullObject(dto.getGroupFilterRuleCode())) {
            List<FilterRequest> requestsCode = Lists.newArrayList();
            requestsCode.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NULL));
            requestsCode.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            requestsCode.add(new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULEID.name(), FilterRequest.Operator.NE, dto.getGroupFilterRuleId()));
            requestsCode.add(new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULECODE.name(), FilterRequest.Operator.EQ, dto.getGroupFilterRuleCode().toLowerCase(), false));
            List<GroupFilterRuleDTO> code = findByFilter(requestsCode);
            if (!DataUtil.isNullOrEmpty(code)) {
                throw new LogicException("", "mn.isdn.rule.add.findOne.exist.code");
            }
        }
        if (!DataUtil.isNullObject(dto.getName())) {
            List<FilterRequest> requestsName = Lists.newArrayList();
            requestsName.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NULL));
            requestsName.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            requestsName.add(new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULEID.name(), FilterRequest.Operator.NE, dto.getGroupFilterRuleId()));
            requestsName.add(new FilterRequest(GroupFilterRule.COLUMNS.NAME.name(), FilterRequest.Operator.EQ, dto.getName().toLowerCase(), false));
            List<GroupFilterRuleDTO> name = findByFilter(requestsName);
            if (!DataUtil.isNullOrEmpty(name)) {
                throw new LogicException("", "mn.isdn.rule.add.findOne.exist.name");
            }
        }

        BaseMessage result = new BaseMessage(true);

        // set lai cac gia tri update.
        if (!DataUtil.isNullOrEmpty(dto.getGroupFilterRuleCode())) {
            groupFilterRuleDTO.setGroupFilterRuleCode(dto.getGroupFilterRuleCode());
        }
        if (!DataUtil.isNullOrEmpty(dto.getName())) {
            groupFilterRuleDTO.setName(dto.getName());
        }
        if (!DataUtil.isNullOrZero(dto.getTelecomServiceId())) {
            groupFilterRuleDTO.setTelecomServiceId(dto.getTelecomServiceId());
        }
        if (!DataUtil.isNullOrEmpty(dto.getNotes())) {
            groupFilterRuleDTO.setNotes(dto.getNotes());
        }
        if (!DataUtil.isNullObject(dto.getStatus())) {
            groupFilterRuleDTO.setStatus(dto.getStatus());
        }
        groupFilterRuleDTO.setLastUpdateUser(userName);
        groupFilterRuleDTO.setLastUpdateTime(optionSetValueService.getSysdateFromDB(false));
        try {
            GroupFilterRule update = repository.save(mapper.toPersistenceBean(groupFilterRuleDTO));
            if (DataUtil.isNullObject(update)) {
                throw new LogicException("", "mn.isdn.rule.update.findOne");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
        }
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(Long id) throws LogicException, Exception {
        if (repository.findOne(id) == null) {
            throw new LogicException("", "ws.rule.notexist");
        }
        repository.delete(id);
        List<FilterRequest> filters = Lists.newArrayList();
        FilterRequest filterRequest1 = new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.EQ, id);
        filters.add(filterRequest1);
        List<GroupFilterRuleDTO> lst = findByFilter(filters);
        for (GroupFilterRuleDTO dto : lst) {
            dto.setStatus(Const.STATUS.NO_ACTIVE);
            repository.save(mapper.toPersistenceBean(dto));
        }
        BaseMessage mg = new BaseMessage();
        mg.setSuccess(true);
        return mg;
    }

    @Override
    public List<GroupFilterRuleDTO> searchByDto(GroupFilterRuleDTO searchDto) throws LogicException, Exception {
        //validate du lieu
//        validate(searchDto);
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NULL));
        requests.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        if (!DataUtil.isNullObject(searchDto.getName()))
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.NAME.name(), FilterRequest.Operator.LIKE, searchDto.getName().toLowerCase(), false));
        if (!DataUtil.isNullObject(searchDto.getGroupFilterRuleCode()))
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULECODE.name(), FilterRequest.Operator.LIKE, searchDto.getGroupFilterRuleCode().toLowerCase(), false));
        if (!DataUtil.isNullObject(searchDto.getNotes()))
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.NOTES.name(), FilterRequest.Operator.LIKE, searchDto.getNotes().toLowerCase(), false));
        if (!DataUtil.isNullOrZero(searchDto.getTelecomServiceId()))
            requests.add(new FilterRequest(GroupFilterRule.COLUMNS.TELECOMSERVICEID.name(), FilterRequest.Operator.EQ, searchDto.getTelecomServiceId()));
        return findByFilter(requests);
    }

    @Override
    public BaseMessage deleteByDto(GroupFilterRuleDTO dto, String userName) throws LogicException, Exception {
        if(DataUtil.isNullObject(dto.getGroupFilterRuleId())){
            throw new LogicException("", "mn.isdn.rule.err.empty.code");
        }
        if(DataUtil.safeToString(dto.getGroupFilterRuleId()).length() > 20){
            throw new LogicException("", "mn.isdn.rule.update.findOne");
        }
        BaseMessage result = new BaseMessage(true);
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        requests.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.EQ, dto.getGroupFilterRuleId()));
        List<GroupFilterRuleDTO> groupFilterRuleList = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(groupFilterRuleList)) {
            throw new LogicException("", "mn.isdn.rule.err.exist.child");
        } else {
            dto.setStatus(Const.STATUS.NO_ACTIVE);
            update(dto, userName);
        }
        return result;
    }

    @Override
    public BaseMessage deleteByListDto(List<GroupFilterRuleDTO> listDto, String userName) throws LogicException, Exception {
        List<Long> listID = Lists.newArrayList();
        for (GroupFilterRuleDTO dto : listDto) {
            listID.add(dto.getGroupFilterRuleId());
            dto.setLastUpdateTime(optionSetValueService.getSysdateFromDB(false));
            dto.setStatus(Const.STATUS.NO_ACTIVE);
            dto.setLastUpdateUser(userName);
        }
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        requests.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IN, listID));
        List<GroupFilterRuleDTO> groupFilterRuleList = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(groupFilterRuleList)) {
            throw new LogicException("", "mn.isdn.rule.err.exist.child");
        } else {
            List<GroupFilterRuleDTO> groupFilterRuleDTOList = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(listDto)));
            if (DataUtil.isNullOrEmpty(groupFilterRuleDTOList)) {
                throw new LogicException("", "mn.isdn.rule.delete.fail");
            }
        }
        BaseMessage result = new BaseMessage(true);
        return result;
    }

    private void validate(GroupFilterRuleDTO dto) throws LogicException, Exception {
        if (DataUtil.isNullObject(dto.getGroupFilterRuleCode())) {
            throw new LogicException("", "mn.isdn.rule.msg.empty.code");
        } else if (dto.getGroupFilterRuleCode().length() > 20) {
            throw new LogicException("", "mn.isdn.rule.msg.length.code");
        } else {
            Pattern p = Pattern.compile("[^a-z0-9_]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(dto.getGroupFilterRuleCode());
            if (m.find())
                throw new LogicException("", "mn.isdn.rule.msg.special.code");
        }

        if (DataUtil.isNullObject(dto.getName())) {
            throw new LogicException("", "mn.isdn.rule.msg.empty.name");
        } else if (dto.getGroupFilterRuleCode().length() > 100) {
            throw new LogicException("", "mn.isdn.rule.msg.length.name");
        }

        if (!DataUtil.isNullObject(dto.getNotes()) && dto.getGroupFilterRuleCode().length() > 500) {
            throw new LogicException("", "mn.isdn.rule.msg.length.note");
        }

//        if (DataUtil.isNullOrZero(dto.getTelecomServiceId())) {
//            throw new LogicException("", "mn.isdn.rule.msg.empty.source");
//        }

        if (DataUtil.isNullObject(dto.getStatus()) || !(dto.getStatus().equals(Const.STATUS.ACTIVE) || dto.getStatus().equals(Const.STATUS.NO_ACTIVE))) {
            throw new LogicException("", "mn.isdn.rule.msg.status");
        }

        List<OptionSetValueDTO> listTelco = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
        for(int i = 0; i < listTelco.size(); i++){
            OptionSetValueDTO optionSetValueDTO = listTelco.get(i);
            if(DataUtil.safeEqual(optionSetValueDTO.getValue(), DataUtil.safeToString(dto.getTelecomServiceId()))){
                break;
            }
            if(i == listTelco.size() - 1){
                throw new LogicException("", "mn.isdn.rule.telecomService.invalid");
            }
        }
    }

    @Override
    public List<GroupFilterRuleDTO> getListNumberFiler(Long groupFilterRuleId, boolean isParent) throws Exception {
        List<FilterRequest> request = Lists.newArrayList();
        if (groupFilterRuleId != null) {
            request.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.EQ, groupFilterRuleId));
        } else {
            request.add(new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), isParent ? FilterRequest.Operator.IS_NULL : FilterRequest.Operator.IS_NOT_NULL));
        }
        request.add(new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        List<GroupFilterRuleDTO> lsResult = findByFilter(request);
        Collections.sort(lsResult, new Comparator<GroupFilterRuleDTO>() {
            @Override
            public int compare(GroupFilterRuleDTO o1, GroupFilterRuleDTO o2) {
                if (o1 != null && o2 != null && o1.getName().toLowerCase() != null && o2.getName().toLowerCase() != null) {
                    return o1.getName().compareTo(o2.getName());
                }
                return 0;
            }
        });
        return lsResult;
    }

    @Override
    public List<GroupFilterRuleDTO> search(GroupFilterRuleDTO currentGroup, boolean isGroup) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        if (!DataUtil.isNullObject(currentGroup.getGroupFilterRuleCode())) {
            FilterRequest filterRequest0 = new FilterRequest(GroupFilterRule.COLUMNS.GROUPFILTERRULECODE.name(), FilterRequest.Operator.LIKE, currentGroup.getGroupFilterRuleCode().toLowerCase(), false);
            filters.add(filterRequest0);
        }
        if (!DataUtil.isNullObject(currentGroup.getName())) {
            FilterRequest filterRequest1 = new FilterRequest(GroupFilterRule.COLUMNS.NAME.name(), FilterRequest.Operator.LIKE, currentGroup.getName().toLowerCase(), false);
            filters.add(filterRequest1);
        }
        if (!DataUtil.isNullObject(currentGroup.getParentId())) {
            FilterRequest filterRequest2 = new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.EQ, currentGroup.getParentId());
            filters.add(filterRequest2);
        }
        if (!DataUtil.isNullObject(currentGroup.getGroupType())) {
            FilterRequest filterRequest2 = new FilterRequest(GroupFilterRule.COLUMNS.GROUPTYPE.name(), FilterRequest.Operator.EQ, currentGroup.getGroupType());
            filters.add(filterRequest2);
        }
        if (!DataUtil.isNullObject(currentGroup.getNotes())) {
            FilterRequest filterRequest3 = new FilterRequest(GroupFilterRule.COLUMNS.NOTES.name(), FilterRequest.Operator.LIKE, currentGroup.getNotes().toLowerCase());
            filterRequest3.setExtract(false);
            filters.add(filterRequest3);
        }
        if (!DataUtil.isNullObject(currentGroup.getTelecomServiceId())) {
            FilterRequest filterRequest5 = new FilterRequest(GroupFilterRule.COLUMNS.TELECOMSERVICEID.name(), FilterRequest.Operator.EQ, currentGroup.getTelecomServiceId());
            filters.add(filterRequest5);
        }
        if (!DataUtil.isNullObject(isGroup) && isGroup) {
            FilterRequest filterRequest4 = new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NOT_NULL);
            filters.add(filterRequest4);
        } else {
            FilterRequest filterRequest4 = new FilterRequest(GroupFilterRule.COLUMNS.PARENTID.name(), FilterRequest.Operator.IS_NULL);
            filters.add(filterRequest4);
        }
        FilterRequest filterRequest5 = new FilterRequest(GroupFilterRule.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE);
        filters.add(filterRequest5);
        return findByFilterOrderByName(filters);
    }


    private List<GroupFilterRuleDTO> findByFilterOrderByName(List<FilterRequest> filters) throws LogicException, Exception {
        OrderSpecifier<String> sortByName = QGroupFilterRule.groupFilterRule.name.asc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }
}

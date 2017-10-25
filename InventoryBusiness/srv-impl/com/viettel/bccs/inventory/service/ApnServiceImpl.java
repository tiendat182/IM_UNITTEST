package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.Apn;
import com.viettel.bccs.inventory.model.QApn;
import com.viettel.bccs.inventory.repo.ApnRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApnServiceImpl extends BaseServiceImpl implements ApnService {

    private final BaseMapper<Apn, ApnDTO> mapper = new BaseMapper<>(Apn.class, ApnDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ApnRepo repository;
    @Autowired
    private StaffService staffService;

    public static final Logger logger = Logger.getLogger(ApnService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ApnDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ApnDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ApnDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ApnDTO dto) throws LogicException, Exception {
        BaseMessage msg = new BaseMessage();
        StaffDTO staffDTO = staffService.getStaffByStaffCode(dto.getLastUpdateUser());
        if (DataUtil.isNullObject(staffDTO)
                || DataUtil.isNullOrZero(staffDTO.getStaffId())) {
            throw new LogicException("", "staff.code.invalid");
        }
        if (DataUtil.isNullObject(dto.getApnCode())) {
            msg.setKeyMsg("mn.import.apn.code.require");
            msg.setSuccess(false);
            return msg;
        }
        if (dto.getApnCode().length() > 50) {
            msg.setKeyMsg("mn.import.apn.code.over");
            msg.setSuccess(false);
            return msg;
        }
        if (DataUtil.isNullObject(dto.getApnName())) {
            msg.setKeyMsg("mn.import.apn.name.require");
            msg.setSuccess(false);
            return msg;
        }
        if (!DataUtil.isNullOrEmpty(dto.getApnName()) && dto.getApnName().length() > 100) {
            msg.setKeyMsg("mn.import.apn.name.over");
            msg.setSuccess(false);
            return msg;
        }
        if (!DataUtil.isNullObject(dto.getCenterCode())
                && dto.getCenterCode().length() > 50) {
            msg.setKeyMsg("mn.import.apn.center.require");
            msg.setSuccess(false);
            return msg;
        }
        if (!DataUtil.isNullOrEmpty(dto.getDescription())
                && dto.getCenterCode().length() > 50) {
            msg.setKeyMsg("mn.import.apn.description.require");
            msg.setSuccess(false);
            return msg;
        }
        if (!DataUtil.isNullOrEmpty(dto.getDescription())
                && dto.getDescription().length() > 500) {
            msg.setKeyMsg("mn.import.apn.description.over");
            msg.setSuccess(false);
            return msg;
        }
        if (!DataUtil.isNullOrZero(dto.getApnId())) {
            ApnDTO apn = findOne(dto.getApnId());
            if (!DataUtil.isNullObject(apn)
                    && !DataUtil.isNullOrZero(apn.getApnId())
                    && Const.LONG_OBJECT_TWO.equals(apn.getStatus())) {
                throw new LogicException("", getText("apn.error.data.not.found"));
            }
        }
        repository.save(mapper.toPersistenceBean(dto));
        msg.setSuccess(true);
        return msg;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ApnDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<ApnDTO> checkDuplicateApn(List<ApnDTO> listApn, String typeCheck) {
        try {
            if (DataUtil.safeEqual(typeCheck, "APN_ID")) {
                List<Long> lstApnId = Lists.newArrayList();
                for (ApnDTO dto : listApn) {
                    lstApnId.add(dto.getApnId());
                }
                return findByFilter(Lists.newArrayList(new FilterRequest(Apn.COLUMNS.APNID.name(), FilterRequest.Operator.IN, lstApnId)));
            } else if (DataUtil.safeEqual(typeCheck,"APN_CODE")) {
                List<String> lstApnCode = Lists.newArrayList();
                for (ApnDTO dto : listApn) {
                    lstApnCode.add(dto.getApnCode().toUpperCase());
                }
                return findByFilter(Lists.newArrayList(new FilterRequest(Apn.COLUMNS.APNCODE.name().toUpperCase(), FilterRequest.Operator.IN, lstApnCode)));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage insertListAPN(List<ApnDTO> listApn) throws Exception {
        BaseMessage msg = new BaseMessage();
        try {
            for (ApnDTO dto : listApn) {
                if (DataUtil.isNullObject(dto.getApnId())) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.apn_id.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getApnCode()) || dto.getApnCode().trim().length() > 50 || !dto.getApnCode().matches("^([\\d\\w])+$")) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.apn_code.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getApnName()) || dto.getApnName().trim().length() > 50) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.apn_name.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getCenterCode()) || dto.getCenterCode().trim().length() > 50) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.center_code.error");
                    return msg;
                }
                Date date = getSysDate(em);
                dto.setCreateTime(date);
                dto.setLastUpdateTime(date);
            }
            repository.save(mapper.toPersistenceBean(listApn));
            msg.setSuccess(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            msg.setSuccess(false);
            msg.setKeyMsg(ex.getMessage());
        }
        return msg;
    }

    @Override
    public List<ApnDTO> searchApn(ApnDTO apnDTO) throws Exception {
//        List<FilterRequest> filters = Lists.newArrayList();
//        filters.add(new FilterRequest(Apn.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, 2L));
//        if (!DataUtil.isNullObject(apnDTO.getApnCode())) {
//            filters.add(new FilterRequest(Apn.COLUMNS.APNCODE.name(), FilterRequest.Operator.LIKE, apnDTO.getApnCode(), false));
//        }
//        if (!DataUtil.isNullObject(apnDTO.getApnName())) {
//            filters.add(new FilterRequest(Apn.COLUMNS.APNNAME.name(), FilterRequest.Operator.LIKE, apnDTO.getApnName(), false));
//        }
//        if (!DataUtil.isNullObject(apnDTO.getStatus())) {
//            filters.add(new FilterRequest(Apn.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, apnDTO.getStatus()));
//        }
//        if (!DataUtil.isNullObject(apnDTO.getCenterCode())) {
//            filters.add(new FilterRequest(Apn.COLUMNS.CENTERCODE.name(), FilterRequest.Operator.LIKE, apnDTO.getCenterCode(), false));
//        }
//        if (!DataUtil.isNullObject(apnDTO.getDescription())) {
//            filters.add(new FilterRequest(Apn.COLUMNS.DESCRIPTION.name(), FilterRequest.Operator.LIKE, apnDTO.getDescription(), false));
//        }
//        OrderSpecifier<Date> sortLastUpdateTime = QApn.apn.lastUpdateTime.desc();
//        return DataUtil.defaultIfNull(mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortLastUpdateTime)), Lists.newArrayList());
        return DataUtil.defaultIfNull(mapper.toDtoBean(repository.searchApn(apnDTO)), new ArrayList<>());
    }

    @Override
    public List<ApnDTO> searchApnCorrect(ApnDTO apnDTO) throws Exception {
        return DataUtil.defaultIfNull(mapper.toDtoBean(repository.searchApnCorrect(apnDTO)), new ArrayList<>());
    }

    @Override
    public List<ApnDTO> searchApnAutoComplete(String input) throws Exception {
        return mapper.toDtoBean(repository.searchApnAutoComplete(input));
    }

    @Override
    @WebMethod
    public ApnDTO findApnById(Long apnId) throws Exception {
        if (DataUtil.isNullOrZero(apnId)) {
            return null;
        }
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Apn.COLUMNS.APNID.name(), FilterRequest.Operator.EQ, apnId));
        requests.add(new FilterRequest(Apn.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, Const.STATUS_DELETE));
        List<ApnDTO> lits = findByFilter(requests);
        if (DataUtil.isNullOrEmpty(lits)) {
            return null;
        }
        return lits.get(0);
    }
}

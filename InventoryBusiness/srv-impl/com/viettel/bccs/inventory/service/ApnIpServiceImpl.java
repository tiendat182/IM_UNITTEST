package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ApnIpDTO;
import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.ApnIp;
import com.viettel.bccs.inventory.model.Area;
import com.viettel.bccs.inventory.model.QApnIp;
import com.viettel.bccs.inventory.repo.ApnIpRepo;
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
import java.util.Date;
import java.util.List;

@Service
public class ApnIpServiceImpl extends BaseServiceImpl implements ApnIpService {

    private final BaseMapper<ApnIp, ApnIpDTO> mapper = new BaseMapper<>(ApnIp.class, ApnIpDTO.class);

    @Autowired
    private ApnIpRepo repository;
    @Autowired
    private StaffService staffService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(ApnIpService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ApnIpDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ApnIpDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ApnIpDTO> searchApnIp(ApnIpDTO apnIp) throws Exception {
//        List<FilterRequest> filters = Lists.newArrayList();
//        if (!DataUtil.isNullObject(apnIp.getApnId())) {
//            filters.add(new FilterRequest(ApnIp.COLUMNS.APNID.name(), FilterRequest.Operator.EQ, apnIp.getApnId()));
//        }
//        if (!DataUtil.isNullObject(apnIp.getIp())) {
//            filters.add(new FilterRequest(ApnIp.COLUMNS.IP.name(), FilterRequest.Operator.LIKE, apnIp.getIp(), false));
//        }
//        if (!DataUtil.isNullObject(apnIp.getStatus())) {
//            filters.add(new FilterRequest(ApnIp.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, apnIp.getStatus()));
//        }
//        if (!DataUtil.isNullObject(apnIp.getCenterCode())) {
//            filters.add(new FilterRequest(ApnIp.COLUMNS.CENTERCODE.name(), FilterRequest.Operator.LIKE, apnIp.getCenterCode(), false));
//        }
//        if (!DataUtil.isNullObject(apnIp.getSubNet())) {
//            filters.add(new FilterRequest(ApnIp.COLUMNS.SUBNET.name(), FilterRequest.Operator.LIKE, apnIp.getSubNet(), false));
//        }
//        OrderSpecifier<Date> sortLastUpdateTime = QApnIp.apnIp.lastUpdateTime.desc();
//        return DataUtil.defaultIfNull(mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortLastUpdateTime)), Lists.newArrayList());
        return mapper.toDtoBean(repository.search(mapper.toPersistenceBean(apnIp)));

    }

    @WebMethod
    public List<ApnIpDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(ApnIpDTO dto) throws Exception {
        StaffDTO staffDTO = staffService.getStaffByStaffCode(dto.getLastUpdateUser());
        if (DataUtil.isNullObject(staffDTO)
                || DataUtil.isNullOrZero(staffDTO.getStaffId())) {
            throw new LogicException("", "staff.code.invalid");
        }
        if (!DataUtil.isNullOrZero(dto.getApnIpId())) {
            ApnIpDTO apnIp = findOne(dto.getApnIpId());
            if (!DataUtil.isNullObject(apnIp)
                    && !DataUtil.isNullOrZero(apnIp.getApnIpId())
                    && Const.LONG_OBJECT_TWO.equals(apnIp.getStatus())) {
                throw new LogicException("", getText("apnIp.error.data.not.found"));
            }
        }
        BaseMessage msg = new BaseMessage();
        msg.setSuccess(true);
        repository.save(mapper.toPersistenceBean(dto));
        return msg;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(ApnIpDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage insertListAPNIP(List<ApnIpDTO> listApnIp) throws Exception {
        BaseMessage msg = new BaseMessage();
        try {
            for (ApnIpDTO dto : listApnIp) {
                if (DataUtil.isNullObject(dto.getApnId())) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.apn_id.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getIp()) || dto.getIp().trim().length() > 50 || !dto.getIp().matches("^([\\d\\w\\.\\:])+$")) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.ip.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getCenterCode()) || dto.getCenterCode().trim().length() > 50) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.center_code.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getSubNet()) || dto.getSubNet().trim().length() > 50) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.subnet.error");
                    return msg;
                }
                if (DataUtil.isNullObject(dto.getStatus()) || (!dto.getStatus().equals(0L) && !dto.getStatus().equals(1L))) {
                    msg.setSuccess(false);
                    msg.setKeyMsg("import.apn.status.error");
                    return msg;
                }
                Date date = getSysDate(em);
                dto.setCreateTime(date);
                dto.setLastUpdateTime(date);
            }
            repository.save(mapper.toPersistenceBean(listApnIp));
            msg.setSuccess(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            msg.setSuccess(false);
            msg.setKeyMsg(ex.getMessage());
        }
        return msg;
    }

    @Override
    public List<ApnIpDTO> checkDuplicateApnIp(List<ApnIpDTO> listApn) {
        return mapper.toDtoBean(repository.checkDuplicateApnIp(listApn));
    }

    @Override
    @WebMethod
    public ApnIpDTO findApnIpById(Long apnIpID) throws Exception {
        if (DataUtil.isNullOrZero(apnIpID)) {
            return null;
        }
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(ApnIp.COLUMNS.APNIPID.name(), FilterRequest.Operator.EQ, apnIpID));
        requests.add(new FilterRequest(ApnIp.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, Const.STATUS_DELETE));
        List<ApnIpDTO> lits = findByFilter(requests);
        if (DataUtil.isNullOrEmpty(lits)) {
            return null;
        }
        return lits.get(0);
    }
}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FinanceTypeDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.FinanceType;
import com.viettel.bccs.inventory.model.QFinanceType;
import com.viettel.bccs.inventory.repo.FinanceTypeRepo;
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
import java.util.Date;
import java.util.List;

@Service
public class FinanceTypeServiceImpl extends BaseServiceImpl implements FinanceTypeService {

//    private final FinanceTypeMapper mapper = new FinanceTypeMapper();

    private final BaseMapper<FinanceType, FinanceTypeDTO> mapper = new BaseMapper(FinanceType.class, FinanceTypeDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private FinanceTypeRepo repository;
    public static final Logger logger = Logger.getLogger(FinanceTypeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public FinanceTypeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<FinanceTypeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<FinanceTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(FinanceTypeDTO financeTypeDTO, StaffDTO staffDTO) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        if (financeTypeDTO != null) {
            baseMessage = validateFinanceType(financeTypeDTO);
            if (!DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                return baseMessage;
            }
            Date sysDate = DbUtil.getSysDate(em);
            financeTypeDTO.setLastUpdateTime(sysDate);
            financeTypeDTO.setCreateDate(sysDate);
            financeTypeDTO.setLastUpdateUser(staffDTO.getStaffCode());
            financeTypeDTO.setCreateUser(staffDTO.getStaffCode());
            financeTypeDTO.setStatus(Const.STATUS.ACTIVE);
            repository.save(mapper.toPersistenceBean(financeTypeDTO));
        }
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(FinanceTypeDTO financeTypeDTO, StaffDTO staffDTO) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        if (financeTypeDTO != null) {
            baseMessage = validateFinanceType(financeTypeDTO);
            if (!DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                return baseMessage;
            }
            Date sysDate = DbUtil.getSysDate(em);
            financeTypeDTO.setLastUpdateTime(sysDate);
            financeTypeDTO.setLastUpdateUser(staffDTO.getStaffCode());
            repository.save(mapper.toPersistenceBean(financeTypeDTO));
        }
        return baseMessage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteListFinance(List<FinanceTypeDTO> lsFinanceTypeDTOs, StaffDTO staffDTO) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        if (lsFinanceTypeDTOs != null) {
            Date sysDate = DbUtil.getSysDate(em);
            for (FinanceTypeDTO financeTypeDTO : lsFinanceTypeDTOs) {
                financeTypeDTO.setLastUpdateTime(sysDate);
                financeTypeDTO.setLastUpdateUser(staffDTO.getStaffCode());
                financeTypeDTO.setStatus(Const.STATUS.NO_ACTIVE);
                repository.save(mapper.toPersistenceBean(financeTypeDTO));
            }
        }
        return baseMessage;
    }

    @WebMethod
    @Override
    public List<FinanceTypeDTO> searchLsFinanceTypeDto(FinanceTypeDTO searchFinanceTypeDTO) throws LogicException, Exception {
        if (searchFinanceTypeDTO != null) {
            List<FilterRequest> lsRequests = Lists.newArrayList();
            if (!DataUtil.isNullOrEmpty(searchFinanceTypeDTO.getFinanceType())) {
                lsRequests.add(new FilterRequest(FinanceType.COLUMNS.FINANCETYPE.name(), FilterRequest.Operator.EQ, searchFinanceTypeDTO.getFinanceType()));
            }
            if (searchFinanceTypeDTO.getDayNum() != null) {
                lsRequests.add(new FilterRequest(FinanceType.COLUMNS.DAYNUM.name(), FilterRequest.Operator.EQ, searchFinanceTypeDTO.getDayNum()));
            }
            lsRequests.add(new FilterRequest(FinanceType.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            OrderSpecifier<Date> order = QFinanceType.financeType1.lastUpdateTime.desc();
            return mapper.toDtoBean(repository.findAll(repository.toPredicate(lsRequests), order));
        }
        return new ArrayList<FinanceTypeDTO>();
    }

    /**
     * ham check ton tai financeType (trong financeType ko dc trung lap)
     *
     * @return true neu trung lap
     * @throws com.viettel.fw.Exception.LogicException
     * @throws Exception
     * @author ThanhNT
     */
    private BaseMessage validateFinanceType(FinanceTypeDTO financeTypeDTO) throws LogicException, Exception {
        BaseMessage message = new BaseMessage();
        //validate check empty
        List<String> lsValueFinance = DataUtil.collectProperty(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.FINANCE_TYPE), "value", String.class);
        if (DataUtil.isNullOrEmpty(financeTypeDTO.getFinanceType())) {
            message.setKeyMsg("mn.stock.limit.men.order.require.msg");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            return message;
        } else if (!lsValueFinance.contains(financeTypeDTO.getFinanceType())) {
            message.setKeyMsg("mn.stock.limit.men.order.require.wrong.fomat");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        } else if (financeTypeDTO.getDayNum() == null) {
            message.setKeyMsg("mn.stock.limit.financeType.day.offer.require.msg");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            return message;
        } else if (financeTypeDTO.getId() != null) {
            if (DataUtil.isNullOrEmpty(financeTypeDTO.getStatus())) {
                message.setKeyMsg("mn.stock.require.status.msg");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                return message;
            } else if (!(Const.STATUS.ACTIVE.equals(financeTypeDTO.getStatus()) || Const.STATUS.NO_ACTIVE.equals(financeTypeDTO.getStatus()))) {
                message.setKeyMsg("mn.stock.require.status.notExist");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                return message;
            }
        }
        //validate trung lap
        List<FilterRequest> lsRequests = Lists.newArrayList();
        if (financeTypeDTO.getId() != null && financeTypeDTO.getId() > 0L) {
            FinanceTypeDTO typeDTO = findOne(financeTypeDTO.getId());
            if (typeDTO == null) {
                message.setKeyMsg("mn.stock.require.financeTypeDTO.notFound");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                return message;
            }
            lsRequests.add(new FilterRequest(FinanceType.COLUMNS.ID.name(), FilterRequest.Operator.NE, financeTypeDTO.getId()));
        }
        lsRequests.add(new FilterRequest(FinanceType.COLUMNS.FINANCETYPE.name(), FilterRequest.Operator.EQ, financeTypeDTO.getFinanceType()));
        lsRequests.add(new FilterRequest(FinanceType.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        List<FinanceTypeDTO> lsData = findByFilter(lsRequests);
        if (lsData != null && lsData.size() > 0) {
            message.setKeyMsg("mn.stock.limit.financeType.duplicate");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        }
        return message;
    }
}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.BrasDTO;
import com.viettel.bccs.inventory.dto.EquipmentDTO;
import com.viettel.bccs.inventory.model.Bras;
import com.viettel.bccs.inventory.model.QBras;
import com.viettel.bccs.inventory.repo.BrasRepo;
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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class BrasServiceImpl extends BaseServiceImpl implements BrasService {

    private final BaseMapper<Bras, BrasDTO> mapper = new BaseMapper(Bras.class, BrasDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private BrasRepo repository;

    @Autowired
    private EquipmentService equipmentService;

    private EquipmentDTO equipmentDTO;

    public static final Logger logger = Logger.getLogger(BrasService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public BrasDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<BrasDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<BrasDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByName = QBras.bras.updateDatetime.desc();
        OrderSpecifier<String> sortByCode = QBras.bras.code.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName, sortByCode));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(BrasDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    private void checkValidate(BrasDTO dto, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "bras.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getBrasId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "bras.validate.require.brasId");
            }
            if (DataUtil.isNullOrEmpty(dto.getCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "bras.validate.require.code");
            }
            if (DataUtil.isNullOrEmpty(dto.getName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "bras.validate.require.name");
            }
            if (DataUtil.isNullOrZero(dto.getEquipmentId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "bras.validate.require.equipmentId");
            }
            if (DataUtil.isNullOrEmpty(dto.getIp())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "bras.validate.require.ip");
            }

            //maxlength
            if (dto.getCode().length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.code");
            }
            if (dto.getName().length() > 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.name");
            }
            if (dto.getIp().length() > 30) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.ip");
            }
            if (!DataUtil.isNullOrEmpty(dto.getAaaIp()) && dto.getAaaIp().length() > 30) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.aaaIp");
            }
            if (!DataUtil.isNullOrEmpty(dto.getSlot()) && dto.getSlot().length() > 20) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.slot");
            }
            if (!DataUtil.isNullOrEmpty(dto.getPort()) && dto.getPort().length() > 20) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.port");
            }
            if (!DataUtil.isNullOrEmpty(dto.getDescription()) && dto.getDescription().length() > 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.maxlength.desc");
            }
            // contains in database - selectItems
            equipmentDTO = equipmentService.findOne(dto.getEquipmentId());
            if (DataUtil.isNullObject(equipmentDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.not.contains.equipmentId");
            }
            //trim space
            dto.setCode(dto.getCode().trim());
            dto.setName(dto.getName().trim());
            dto.setIp(dto.getIp().trim());
            dto.setAaaIp(dto.getAaaIp().trim());
            dto.setSlot(dto.getSlot().trim());
            dto.setPort(dto.getPort().trim());
            dto.setDescription(dto.getDescription().trim());
            //bras code
            if (!dto.getCode().matches(Const.REGEX.CODE_REGEX)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "bras.validate.not.matches.code");
            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(BrasDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, true);
        // Validate {Ma Bras} la duy nhat
        if (isDulicateBrasCode(dto.getCode(), dto.getBrasId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "bras.duplicate.code", dto.getCode());
        }
        // Validate {Ip} la duy nhat
        if (isDulicateBrasIP(dto.getIp(), dto.getBrasId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "bras.duplicate.ip", dto.getIp());
        }
        try {
            BrasDTO brasDTO = findOne(dto.getBrasId());
            if (DataUtil.isNullObject(brasDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "bras.update.error", dto.getBrasId());
            }
            if (!DataUtil.isNullOrEmpty(dto.getCode())) {
                brasDTO.setCode(dto.getCode());
            }
            if (!DataUtil.isNullOrEmpty(dto.getName())) {
                brasDTO.setName(dto.getName());
            }
            if (!DataUtil.isNullOrZero(dto.getEquipmentId())) {
                brasDTO.setEquipmentId(dto.getEquipmentId());
            }
            if (!DataUtil.isNullOrEmpty(dto.getIp())) {
                brasDTO.setIp(dto.getIp());
            }
            if (!DataUtil.isNullOrEmpty(dto.getAaaIp())) {
                brasDTO.setAaaIp(dto.getAaaIp());
            }
            if (!DataUtil.isNullOrEmpty(dto.getSlot())) {
                brasDTO.setSlot(dto.getSlot());
            }
            if (!DataUtil.isNullOrEmpty(dto.getPort())) {
                brasDTO.setPort(dto.getPort());
            }
            if (!DataUtil.isNullOrEmpty(dto.getDescription())) {
                brasDTO.setDescription(dto.getDescription());
            }
            brasDTO.setUpdateUser(staffCode);
            brasDTO.setUpdateDatetime(getSysDate(em));
            repository.save(mapper.toPersistenceBean(brasDTO));
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private boolean isDulicateBrasCode(String brasCode, Long brasId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Bras.COLUMNS.CODE.name(), FilterRequest.Operator.EQ, brasCode));
        lst.add(new FilterRequest(Bras.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        if (!DataUtil.isNullOrZero(brasId)) {
            lst.add(new FilterRequest(Bras.COLUMNS.BRASID.name(), FilterRequest.Operator.NE, brasId));
        }
        return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
    }

    private boolean isDulicateBrasIP(String ip, Long brasId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Bras.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        lst.add(new FilterRequest(Bras.COLUMNS.IP.name(), FilterRequest.Operator.EQ, ip));
        if (!DataUtil.isNullOrZero(brasId)) {
            lst.add(new FilterRequest(Bras.COLUMNS.BRASID.name(), FilterRequest.Operator.NE, brasId));
        }
        return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BrasDTO createNewBras(BrasDTO brasDTO, String staffCode) throws Exception, LogicException {
        checkValidate(brasDTO, false);
        // Validate {Ma Bras} la duy nhat
        if (isDulicateBrasCode(brasDTO.getCode(), brasDTO.getBrasId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "bras.duplicate.code", brasDTO.getCode());
        }
        // Validate {Ip} la duy nhat
        if (isDulicateBrasIP(brasDTO.getIp(), brasDTO.getBrasId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "bras.duplicate.ip", brasDTO.getIp());
        }
        try {
            Date currentDate = getSysDate(em);
            brasDTO.setCreateUser(staffCode);
            brasDTO.setCreateDatetime(currentDate);
            brasDTO.setUpdateUser(staffCode);
            brasDTO.setUpdateDatetime(currentDate);
            brasDTO.setStatus(Const.STATUS_ACTIVE);
            Bras bras = mapper.toPersistenceBean(brasDTO);
            bras = repository.save(bras);
            return mapper.toDtoBean(bras);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(List<Long> lstId) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        if (DataUtil.isNullObject(lstId)) {
            return null;
        }
        for (Long dtoId : lstId) {
            try {
                BrasDTO brasDTO = findOne(dtoId);
                if (DataUtil.isNullObject(brasDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "bras.delete.error", dtoId);
                }
                repository.delete(mapper.toPersistenceBean(brasDTO));
            } catch (LogicException e) {
                throw e;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "bras.delete");
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    public List<BrasDTO> search(BrasDTO brasDTO) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            if (!DataUtil.isNullOrEmpty(brasDTO.getCode())) {
                lst.add(new FilterRequest(Bras.COLUMNS.CODE.name(), FilterRequest.Operator.LIKE, brasDTO.getCode().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrZero(brasDTO.getEquipmentId())) {
                lst.add(new FilterRequest(Bras.COLUMNS.EQUIPMENTID.name(), FilterRequest.Operator.EQ, brasDTO.getEquipmentId().toString().trim()));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getName())) {
                lst.add(new FilterRequest(Bras.COLUMNS.NAME.name(), FilterRequest.Operator.LIKE, brasDTO.getName().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getIp())) {
                lst.add(new FilterRequest(Bras.COLUMNS.IP.name(), FilterRequest.Operator.LIKE, brasDTO.getIp().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getAaaIp())) {
                lst.add(new FilterRequest(Bras.COLUMNS.AAAIP.name(), FilterRequest.Operator.EQ, brasDTO.getAaaIp().trim()));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getStatus())) {
                lst.add(new FilterRequest(Bras.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, brasDTO.getStatus().trim()));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getDescription())) {
                lst.add(new FilterRequest(Bras.COLUMNS.DESCRIPTION.name(), FilterRequest.Operator.LIKE, brasDTO.getDescription().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getSlot())) {
                lst.add(new FilterRequest(Bras.COLUMNS.SLOT.name(), FilterRequest.Operator.LIKE, brasDTO.getSlot().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrEmpty(brasDTO.getPort())) {
                lst.add(new FilterRequest(Bras.COLUMNS.PORT.name(), FilterRequest.Operator.LIKE, brasDTO.getPort().toLowerCase().trim(), false));
            }

            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public BrasDTO findByBrasIp(String ipBras) throws Exception, LogicException {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Bras.COLUMNS.IP.name(), FilterRequest.Operator.EQ, ipBras));
        requests.add(new FilterRequest(Bras.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<BrasDTO> list = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<BrasDTO> findAllAction() throws Exception, LogicException {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Bras.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        return findByFilter(requests);

    }

    @Override
    public BrasDTO findByBrasId(Long brasId) throws Exception, LogicException {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(Bras.COLUMNS.BRASID.name(), FilterRequest.Operator.EQ, brasId));
        requests.add(new FilterRequest(Bras.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<BrasDTO> list = findByFilter(requests);
        if (!DataUtil.isNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * author Tuydv1
     * add new bras
     *
     * @param dto
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BrasDTO createBras(BrasDTO dto, String staffCode) throws Exception, LogicException {
        checkValidate(dto, false);
        // Validate {Ma Bras} la duy nhat
        if (isDulicateBrasCode(dto.getCode(), dto.getBrasId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "bras.duplicate.code", dto.getCode());
        }
        // Validate {Ip} la duy nhat
        if (isDulicateBrasIP(dto.getIp(), dto.getBrasId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "bras.duplicate.ip", dto.getIp());
        }
        try {
            Date currentDate = getSysDate(em);
            dto.setCreateUser(staffCode);
            dto.setCreateDatetime(currentDate);
            dto.setUpdateUser(staffCode);
            dto.setUpdateDatetime(currentDate);
            Bras bras = mapper.toPersistenceBean(dto);
            bras = repository.save(bras);
            return mapper.toDtoBean(bras);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

}

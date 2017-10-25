package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.VofficeRoleDTO;
import com.viettel.bccs.inventory.model.VofficeRole;
import com.viettel.bccs.inventory.repo.VofficeRoleRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
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
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class VofficeRoleServiceImpl extends BaseServiceImpl implements VofficeRoleService {

    private final BaseMapper<VofficeRole, VofficeRoleDTO> mapper = new BaseMapper(VofficeRole.class, VofficeRoleDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private SignFlowDetailService signFlowDetailService;

    @Autowired
    private StaffService staffService;
    private StaffDTO staffDTO;
    private static final Pattern PATTERN_NAME = Pattern.compile(GetTextFromBundleHelper.getText("NAME_REGEX"));
    private static final Pattern PATTERN_CODE = Pattern.compile(Const.REGEX.CODE_REGEX);
    @Autowired
    private VofficeRoleRepo repository;
    public static final Logger logger = Logger.getLogger(VofficeRoleService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public VofficeRoleDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<VofficeRoleDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<VofficeRoleDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(VofficeRoleDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        if (DataUtil.isNullObject(lstId)) {
            return null;
        }
        for (Long vofficeRoleId : lstId) {
            try {
                VofficeRoleDTO vofficeRoleDTO = findOne(vofficeRoleId);
                if (DataUtil.isNullObject(vofficeRoleDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "signRoleVoffice.delete.error", vofficeRoleId);
                }
                boolean isExistSignFlowDetail = isExistSignFlowDetail(vofficeRoleId);
                if (isExistSignFlowDetail) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "signRoleVoffice.delete.exist.list", vofficeRoleDTO.getRoleName());
                }
                vofficeRoleDTO.setStatus(Const.STATUS.NO_ACTIVE);
                vofficeRoleDTO.setLastUpdateUser(staffCode);
                vofficeRoleDTO.setLastUpdateTime(getSysDate(em));
                repository.save(mapper.toPersistenceBean(vofficeRoleDTO));
            } catch (LogicException ex) {
                throw ex;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "signRoleVoffice.delete");
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private boolean isExistSignFlowDetail(Long vofficeRoleId) throws Exception {
        List<SignFlowDetailDTO> lstSignFlowDetai = signFlowDetailService.findByVofficeRoleId(vofficeRoleId);
        if (!DataUtil.isNullOrEmpty(lstSignFlowDetai)) {
            return true;
        }
        return false;
    }

    @WebMethod
    public List<VofficeRoleDTO> search(VofficeRoleDTO vofficeRoleDTO, boolean flag) throws Exception, LogicException {
        List<VofficeRoleDTO> result = Lists.newArrayList();
        try {
            List lstParam = new ArrayList();
            StringBuilder strQuery = new StringBuilder();
            strQuery.append("SELECT * From voffice_role where status =1 ");
            if (!DataUtil.isNullOrEmpty(vofficeRoleDTO.getRoleCode())) {
                strQuery.append(" and upper(role_code) like ? ");
                lstParam.add("%" + vofficeRoleDTO.getRoleCode().trim().toUpperCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\\\!") + "%");
                strQuery.append("  escape '\\' ");
            }
            if (!DataUtil.isNullOrEmpty(vofficeRoleDTO.getRoleName())) {
                strQuery.append(" and upper(role_name) like ? ");
                lstParam.add("%" + vofficeRoleDTO.getRoleName().trim().toUpperCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\\\!") + "%");
                strQuery.append("  escape '\\' ");
            }
            if (flag) {
                strQuery.append("  order by last_update_time desc ");
            } else {
                strQuery.append(" order by role_name ");
            }
            Query query = em.createNativeQuery(strQuery.toString(), VofficeRole.class);
            if (query != null) {
                for (int i = 0; i < lstParam.size(); i++) {
                    query.setParameter(i + 1, lstParam.get(i));
                }
                List<VofficeRole> list = query.getResultList();
                if (!DataUtil.isNullOrEmpty(list)) {
                    result = mapper.toDtoBean(list);
                }
            }

            return result;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
    }

    /**
     * author tuydv1
     * add new VofficeRole
     *
     * @param dto
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public VofficeRoleDTO createNewVofficeRole(VofficeRoleDTO dto, String staffCode) throws Exception, LogicException {
        checkValidate(dto, staffCode, false);
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getRoleCode())) {
                dto.setRoleCode(dto.getRoleCode().trim());
            }
            if (!DataUtil.isNullObject(dto.getRoleName())) {
                dto.setRoleName(dto.getRoleName().trim());
            }
        }
        // Validate ma la duy nhat
        if (isDulicateVofficeRole(dto, dto.getVofficeRoleId(), true)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "signRoleVoffice.duplicate.roleCode", dto.getRoleCode());
        }
        // Validate ten la duy nhat
        if (isDulicateVofficeRole(dto, dto.getVofficeRoleId(), false)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "signRoleVoffice.duplicate.roleName", dto.getRoleName());
        }
        try {
            Date currentDate = getSysDate(em);
            dto.setCreateUser(staffCode);
            dto.setCreateDate(currentDate);
            dto.setLastUpdateUser(staffCode);
            dto.setLastUpdateTime(currentDate);
            dto.setStatus(Const.STATUS_ACTIVE);
            VofficeRole VofficeRole = mapper.toPersistenceBean(dto);
            VofficeRole = repository.save(VofficeRole);
            return mapper.toDtoBean(VofficeRole);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    /**
     * Update
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(VofficeRoleDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, staffCode, true);
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getRoleCode())) {
                dto.setRoleCode(dto.getRoleCode().trim());
            }
            if (!DataUtil.isNullObject(dto.getRoleName())) {
                dto.setRoleName(dto.getRoleName().trim());
            }
        }
        // Validate ma la duy nhat
        if (isDulicateVofficeRole(dto, dto.getVofficeRoleId(), true)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "signRoleVoffice.duplicate.roleCode", dto.getRoleCode());
        }
        // Validate ten la duy nhat
        if (isDulicateVofficeRole(dto, dto.getVofficeRoleId(), false)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "signRoleVoffice.duplicate.roleName", dto.getRoleName());
        }
        try {
            VofficeRoleDTO vofficeRoleDTO = findOne(dto.getVofficeRoleId());
            if (DataUtil.isNullObject(vofficeRoleDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "signRoleVoffice.update.error", dto.getVofficeRoleId());
            }
            dto.setLastUpdateUser(staffCode);
            dto.setLastUpdateTime(getSysDate(em));
            repository.save(mapper.toPersistenceBean(dto));
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.vofficeRole.update");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private void checkValidate(VofficeRoleDTO dto, String staffCode, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signRoleVoffice.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getVofficeRoleId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signRoleVoffice.require.vofficeRoleId");
            }
            if (DataUtil.isNullOrEmpty(dto.getRoleCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signRoleVoffice.require.roleCode");
            }
            if (DataUtil.isNullOrEmpty(dto.getRoleName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signRoleVoffice.require.roleName");
            }

            //maxlength
            if (dto.getRoleCode().getBytes("UTF-8").length > 20 ||
                    !PATTERN_CODE.matcher(dto.getRoleCode().trim()).matches()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signRoleVoffice.validate.maxlength.roleCode");
            }
            if (dto.getRoleName().getBytes("UTF-8").length > 100 ||
                    !DataUtil.validateStringByPattern(dto.getRoleName().trim(), getText("NAME_REGEX"))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signRoleVoffice.validate.maxlength.roleName");
            }

            // contains in database - selectItems
            staffDTO = staffService.getStaffByStaffCode(staffCode);
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    private boolean isDulicateVofficeRole(VofficeRoleDTO dto, Long vofficeRoleId, boolean flag) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            if (flag) {
                lst.add(new FilterRequest(VofficeRole.COLUMNS.ROLECODE.name(), FilterRequest.Operator.EQ, dto.getRoleCode(), false));
            } else {
                lst.add(new FilterRequest(VofficeRole.COLUMNS.ROLENAME.name(), FilterRequest.Operator.EQ, dto.getRoleName(), false));
            }
            lst.add(new FilterRequest(VofficeRole.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            if (!DataUtil.isNullOrZero(vofficeRoleId)) {
                lst.add(new FilterRequest(VofficeRole.COLUMNS.VOFFICEROLEID.name(), FilterRequest.Operator.NE, vofficeRoleId));
            }
            return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

}

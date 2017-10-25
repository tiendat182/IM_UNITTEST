package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignFlowDTO;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.model.SignFlow;
import com.viettel.bccs.inventory.repo.SignFlowRepo;
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
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SignFlowServiceImpl extends BaseServiceImpl implements SignFlowService {

    private final BaseMapper<SignFlow, SignFlowDTO> mapper = new BaseMapper(SignFlow.class, SignFlowDTO.class);

    @Autowired
    private SignFlowRepo repository;
    public static final Logger logger = Logger.getLogger(SignFlowService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StaffService staffService;
    private StaffDTO staffDTO;
    @Autowired
    private ShopService shopService;
    private ShopDTO shopDTO;
    private static final Pattern PATTERN = Pattern.compile(Const.REGEX.CODE_REGEX);
    private static final Pattern PATTERN_NAME = Pattern.compile(GetTextFromBundleHelper.getText("NAME_REGEX"));

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public SignFlowDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<SignFlowDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<SignFlowDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(SignFlowDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(SignFlowDTO dto, List<SignFlowDetailDTO> list, String staffCode) throws Exception {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, staffCode, true);
        checkValidateDetail(list);
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getName())) {
                dto.setName(dto.getName().trim());
            }
        }
        // Validate ma la duy nhat
        if (isDulicateSignFlow(dto, dto.getSignFlowId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "signFlow.duplicate.name", dto.getName());
        }
        try {
            SignFlowDTO signFlowDTO = findOne(dto.getSignFlowId());
            if (DataUtil.isNullObject(signFlowDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "signRoleVoffice.update.error", dto.getName());
            }
            dto.setShopCode(shopDTO.getShopCode());
            dto.setLastUpdateUser(staffCode);
            dto.setLastUpdateTime(getSysDate(em));
            repository.save(mapper.toPersistenceBean(dto));
            for (SignFlowDetailDTO detailDTO : list) {
                detailDTO.setSignFlowId(dto.getSignFlowId());
                SignFlowDetailDTO signFlowDetailDTO = new SignFlowDetailDTO();
                if (!DataUtil.isNullOrZero(detailDTO.getSignFlowDetailId())) {
                    signFlowDetailDTO = signFlowDetailService.findOne(detailDTO.getSignFlowDetailId());
                }
                if (DataUtil.isNullObject(signFlowDetailDTO)) {
                    signFlowDetailService.create(detailDTO);
                } else {
                    signFlowDetailService.update(detailDTO);
                }
            }
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "signFlow.update.error");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    public List<SignFlowDTO> getSignFlowByShop(Long shopId) throws Exception {
        List<FilterRequest> lsRequest = Lists.newArrayList(new FilterRequest(SignFlow.COLUMNS.SHOPID.name(), FilterRequest.Operator.EQ, shopId),
                new FilterRequest(SignFlow.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return findByFilter(lsRequest);
    }

    @WebMethod
    public List<SignFlowDTO> search(SignFlowDTO signFlowDTO) throws Exception, LogicException {
        List<SignFlowDTO> result = Lists.newArrayList();
        try {
            List lstParam = new ArrayList();
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(" select a.* ");
            strQuery.append("   from sign_flow a  ");
            strQuery.append("  where 1=1 ");
            strQuery.append("    and a.status = 1 ");

            if (!DataUtil.isNullObject(signFlowDTO.getCurrentShopId())) {
                strQuery.append("    and exists (select 1 from shop s ");
                strQuery.append("                 where s.shop_id = a.shop_id ");
                strQuery.append("                   and s.shop_path like ? )");

                lstParam.add("%_" + signFlowDTO.getCurrentShopId() + "%");
            }
            if (!DataUtil.isNullOrEmpty(signFlowDTO.getName())) {
                strQuery.append(" and upper(a.name) like ?  escape '\\'  ");
                lstParam.add("%" + signFlowDTO.getName().trim().toUpperCase().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").replaceAll("!", "\\!") + "%");

            }
            if (!DataUtil.isNullObject(signFlowDTO.getShopId())) {
                strQuery.append(" and a.shop_id =? ");
                lstParam.add(signFlowDTO.getShopId());
            }
            if (!DataUtil.isNullObject(signFlowDTO.getVofficeRoleId())) {
                strQuery.append("    and exists (select 1 from sign_flow_detail s ");
                strQuery.append("                 where s.sign_flow_id = a.sign_flow_id ");
                strQuery.append("                   and s.voffice_role_id = ? )");
                
                lstParam.add(signFlowDTO.getVofficeRoleId());
            }
            if (!DataUtil.isNullOrEmpty(signFlowDTO.getEmail())) {
                strQuery.append("    and exists (select 1 from sign_flow_detail s ");
                strQuery.append("                 where s.sign_flow_id = a.sign_flow_id ");
                strQuery.append("                   and upper(s.email) like ? escape '\\' )");

                lstParam.add("%" + signFlowDTO.getEmail().trim().toUpperCase().replace("\\", "\\\\").replaceAll("%", "\\\\%").replaceAll("_", "\\\\_").replaceAll("!", "\\\\!") + "%");
            }

            strQuery.append(" and rownum <= 100 ");
            strQuery.append(" order by a.last_update_time desc ");
            Query query = em.createNativeQuery(strQuery.toString(), SignFlow.class);
            if (query != null) {
                for (int i = 0; i < lstParam.size(); i++) {
                    query.setParameter(i + 1, lstParam.get(i));
                }
                List<SignFlow> list = query.getResultList();
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
     * add new SignFlow
     *
     * @param dto
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public SignFlowDTO createNewSignFlow(SignFlowDTO dto, List<SignFlowDetailDTO> listDtoDetail, String staffCode) throws Exception, LogicException {
        checkValidate(dto, staffCode, false);
        checkValidateDetail(listDtoDetail);
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getName())) {
                dto.setName(dto.getName().trim());
            }
        }
        // Validate ma la duy nhat
        if (isDulicateSignFlow(dto, dto.getSignFlowId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "signFlow.duplicate.name", dto.getName());
        }
        try {
            Date currentDate = getSysDate(em);
            dto.setCreateUser(staffCode);
            dto.setCreateDate(currentDate);
            dto.setLastUpdateUser(staffCode);
            dto.setLastUpdateTime(currentDate);
            dto.setStatus(Const.STATUS_ACTIVE);
            dto.setShopCode(shopDTO.getShopCode());
            SignFlow signFlow = mapper.toPersistenceBean(dto);
            signFlow = repository.save(signFlow);
            Long signFlowId = signFlow.getSignFlowId();
            for (SignFlowDetailDTO detailDTO : listDtoDetail) {
                convert(detailDTO);
                detailDTO.setSignFlowId(signFlowId);
                detailDTO.setStatus(Const.STATUS_ACTIVE);
                signFlowDetailService.create(detailDTO);
            }
            return mapper.toDtoBean(signFlow);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    private void checkValidate(SignFlowDTO dto, String staffCode, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getSignFlowId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.signFlowId");
            }
            if (DataUtil.isNullOrEmpty(dto.getName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.name");
            }
            if (DataUtil.isNullObject(dto.getShopId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.shopId");
            }

            //maxlength
            if (dto.getName().trim().getBytes("UTF-8").length > 100 ||
                    !DataUtil.validateStringByPattern(dto.getName().trim(), getText("NAME_REGEX"))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.maxlength.name");
            }

            // contains in database - selectItems
            shopDTO = shopService.findOne(dto.getShopId());
            if (DataUtil.isNullObject(shopDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "shop.validate");
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

    private void checkValidateDetail(List<SignFlowDetailDTO> listDtoDetail) throws Exception {
        try {

            if (DataUtil.isNullOrEmpty(listDtoDetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.validate.listSignFlowDetail");
            } else

            {
                int i = 0;
                int k;
                boolean checkExits = true;
                for (SignFlowDetailDTO dtoDetail : listDtoDetail) {
                    if (Const.STATUS_ACTIVE.equals(dtoDetail.getStatus())) {
                        checkExits = false;
                    }
                    if (DataUtil.isNullObject(dtoDetail.getVofficeRoleId())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.name");
                    } else {
                        k = 0;
                        for (SignFlowDetailDTO signFlowDetailDTO : listDtoDetail) {
                            if (i != k && dtoDetail.getVofficeRoleId().equals(signFlowDetailDTO.getVofficeRoleId())
                                    && dtoDetail.getStatus().trim().equals(signFlowDetailDTO.getStatus().trim())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "signFlow.duplicate.roleName");
                            }
                            k++;
                        }
                    }
                    if (DataUtil.isNullObject(dtoDetail.getEmail().trim())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "signFlow.require.email");
                    } else if (dtoDetail.getEmail().trim().getBytes("UTF-8").length > 100) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.maxlength.email");
                    } else if (!dtoDetail.getEmail().trim().endsWith("@viettel.com.vn")) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.validate.email");

                    } else if (!PATTERN.matcher(dtoDetail.getEmail().trim().substring(0,
                            dtoDetail.getEmail().trim().length() - Const.FORMAT_EMAIL.length())).matches()) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.validate.email");
                    }
//                    else{
//                        k=0;
//                        for(SignFlowDetailDTO signFlowDetailDTO :listDtoDetail){
//                            if(i!=k && dtoDetail.getEmail().trim().equals(signFlowDetailDTO.getEmail().trim())
//                                    && dtoDetail.getStatus().trim().equals(signFlowDetailDTO.getStatus().trim())){
//                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "signFlow.duplicate.email");
//                            }
//                            k++;
//                        }
//                    }
                    if (DataUtil.isNullObject(dtoDetail.getSignOrder())) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.require.signOrder");
                    } else if (DataUtil.safeToLong(dtoDetail.getSignOrder()) <= 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.require.signOrder");
                    } else if (dtoDetail.getSignOrder().toString().trim().getBytes("UTF-8").length > 2) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "signFlow.maxlength.signOrder");
                    } else {
                        k = 0;
                        for (SignFlowDetailDTO signFlowDetailDTO : listDtoDetail) {
                            if (i != k && dtoDetail.getSignOrder().equals(signFlowDetailDTO.getSignOrder())
                                    && dtoDetail.getStatus().trim().equals(signFlowDetailDTO.getStatus().trim())) {
                                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "signFlow.duplicate.signOrder");
                            }
                            k++;
                        }
                    }
                    i++;
                }
                if (checkExits) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "signFlow.validate.listSignFlowDetail");
                }
            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    private boolean isDulicateSignFlow(SignFlowDTO dto, Long signFlowId) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(SignFlow.COLUMNS.NAME.name(), FilterRequest.Operator.EQ, dto.getName(), false));
            lst.add(new FilterRequest(SignFlow.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            if (!DataUtil.isNullOrZero(signFlowId)) {
                lst.add(new FilterRequest(SignFlow.COLUMNS.SIGNFLOWID.name(), FilterRequest.Operator.NE, signFlowId));
            }
            return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        if (DataUtil.isNullObject(lstId)) {
            return null;
        }
        for (Long signFlowId : lstId) {
            try {
                SignFlowDTO signFlowDTO = findOne(signFlowId);
                if (DataUtil.isNullObject(signFlowDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "signFlow.delete.error", signFlowId);
                }
                signFlowDTO.setStatus(Const.STATUS.NO_ACTIVE);
                signFlowDTO.setLastUpdateUser(staffCode);
                signFlowDTO.setLastUpdateTime(getSysDate(em));
                repository.save(mapper.toPersistenceBean(signFlowDTO));
                List<SignFlowDetailDTO> listDetailDTO = signFlowDetailService.findBySignFlowId(signFlowDTO.getSignFlowId());
                if (!DataUtil.isNullOrEmpty(listDetailDTO)) {
                    for (SignFlowDetailDTO signFlowDetailDTO : listDetailDTO) {
                        signFlowDetailDTO.setStatus(Const.STATUS.NO_ACTIVE);
                        signFlowDetailService.update(signFlowDetailDTO);
                    }
                }
            } catch (LogicException ex) {
                throw ex;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "signFlow.delete");
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private SignFlowDetailDTO convert(SignFlowDetailDTO dto) throws Exception {
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getEmail())) {
                dto.setEmail(dto.getEmail().trim());
            }
        }
        return dto;
    }
}

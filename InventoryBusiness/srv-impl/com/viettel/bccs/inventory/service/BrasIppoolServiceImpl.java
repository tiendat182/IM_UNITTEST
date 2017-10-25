package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.BrasIppool;
import com.viettel.bccs.inventory.repo.AreaRepo;
import com.viettel.bccs.inventory.repo.BrasIppoolRepo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class BrasIppoolServiceImpl extends BaseServiceImpl implements BrasIppoolService {

    private static final Pattern PATTERN = Pattern.compile(Const.REGEX.IP_REGEX);
    public static final String BRAS_IPPOOL_STATUS_FAIL = "0"; //hong
    public static final String BRAS_IPPOOL_STATUS_FREE = "1"; //chua su dung
    public static final String BRAS_IPPOOL_STATUS_USED = "2"; //dang su dung
    public static final String BRAS_IPPOOL_STATUS_SUPPEND = "3";//tam ngung
    public static final String BRAS_IPPOOL_STATUS_LOCK = "4"; //lock

    private final BaseMapper<BrasIppool, BrasIppoolDTO> mapper = new BaseMapper(BrasIppool.class, BrasIppoolDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private BrasIppoolRepo repository;
    @Autowired
    private DomainService domainService;
    @Autowired
    private BrasService brasService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private AreaRepo areaRepo;
    @Autowired
    private OptionSetValueService optionSetValueService;

    private List<OptionSetValueDTO> listStatus = Lists.newArrayList();
    private List<OptionSetValueDTO> listType = Lists.newArrayList();
    private DomainDTO domainDTO;
    private BrasDTO brasDTO;
    private AreaDTO areaDTO;


    public static final Logger logger = Logger.getLogger(BrasIppoolService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public BrasIppoolDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<BrasIppoolDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<BrasIppoolDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(BrasIppoolDTO dto) throws Exception, LogicException {
        throw new NotImplementedException();
    }

    private void checkValidate(BrasIppoolDTO dto, boolean isUpdate) throws Exception, LogicException {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "brasIpPool.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getPoolId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.poolId");
            }
            if (DataUtil.isNullOrEmpty(dto.getIpPool())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.ipPool");
            } else {
                dto.setIpPool(dto.getIpPool().trim());
            }
            if (DataUtil.isNullOrEmpty(dto.getPoolName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.name");
            } else {
                dto.setPoolName(dto.getPoolName().trim());
            }
            if (DataUtil.isNullOrZero(dto.getDomainId()) && DataUtil.isNullOrZero(dto.getDomain())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.domainId");
            }
            if (DataUtil.isNullOrZero(dto.getBrasId()) && DataUtil.isNullOrZero(dto.getBrasIp())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.brasId");
            }
            if (DataUtil.isNullOrEmpty(dto.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.status");
            } else {
                dto.setStatus(dto.getStatus().trim());
            }
            if (DataUtil.isNullOrEmpty(dto.getProvince())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.province");
            } else {
                dto.setProvince(dto.getProvince().trim());
            }
            //check ip dac biet (anhvv4 161215)
            if (dto.isSpecificIp()) {
                if (DataUtil.isNullOrEmpty(dto.getIpType())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.ipType");
                } else {
                    dto.setIpType(dto.getIpType().trim());
                }
                if (DataUtil.isNullOrEmpty(dto.getIpLabel())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "brasIpPool.validate.require.iplabel");
                } else {
                    dto.setIpLabel(dto.getIpLabel().trim());
                }
            }
            //maxlength
            if (dto.getIpPool().length() > 20) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.ipPool");
            }
            if (dto.getPoolName().length() > 20) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.name");
            }
            if (!DataUtil.isNullOrEmpty(dto.getPoolUniq()) && dto.getPoolUniq().length() > 30) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.poooUniq");
            }
            if (!DataUtil.isNullOrEmpty(dto.getIpMask()) && dto.getIpMask().length() > 200) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.ipMask");
            }
            if (!DataUtil.isNullOrEmpty(dto.getIpLabel()) && dto.getIpLabel().length() > 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.ipLable");
            }
            if (!DataUtil.isNullOrEmpty(dto.getProvince()) && dto.getProvince().length() > 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.province");
            }
            //Regex IpPool
            if (!PATTERN.matcher(dto.getIpPool()).matches()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.matches.ipPool");
            }
            // contains in database - selectItems
            domainDTO = domainService.findOne(dto.getDomainId());
            if (DataUtil.isNullObject(domainDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.domainId");
            }
            listStatus = optionSetValueService.getLsOptionSetValueByCode(Const.BRAS.BRAS_IPPOOL_STATUS);
            if (!ValidateService.checkValueContainOptionSet(dto.getStatus(), listStatus)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.status");
            }
            brasDTO = brasService.findOne(dto.getBrasId());
            if (DataUtil.isNullObject(brasDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.brasId");
            }
            //validate province (anhvv4 161215)
            if (!DataUtil.isNullOrEmpty(dto.getProvince())) {
                List<String> listAreaCode = new ArrayList<>(Arrays.asList(dto.getProvince().split(",")));
                for (String areaCode : listAreaCode) {
                    areaDTO = areaService.findByCode(areaCode);
                    if (DataUtil.isNullObject(areaDTO)) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.province");
                    }
                }
            }
            //anhvv4 161215 check loai ip dac thu
            if (dto.isSpecificIp()) {
                listType = optionSetValueService.getLsOptionSetValueByCode(Const.BRAS.BRAS_IPPOOL_TYPE_SPECIFIC);
                if (!ValidateService.checkValueContainOptionSet(dto.getIpType(), listType)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.ipType");
                }
            }
            //trim space
            dto.setIpPool(DataUtil.trim(dto.getIpPool()));
            dto.setPoolName(DataUtil.trim(dto.getPoolName()));
            dto.setPoolUniq(DataUtil.trim(dto.getPoolUniq()));
            dto.setIpMask(DataUtil.trim(dto.getIpMask()));
            dto.setIpLabel(DataUtil.trim(dto.getIpLabel()));

        } catch (LogicException ex) {
            throw ex;
        }
    }

    private boolean isDulicateBrasIpPool(String ip, Long poolId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(BrasIppool.COLUMNS.IPPOOL.name(), FilterRequest.Operator.EQ, ip));
//        lst.add(new FilterRequest(BrasIppool.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        if (!DataUtil.isNullOrZero(poolId)) {
            lst.add(new FilterRequest(BrasIppool.COLUMNS.POOLID.name(), FilterRequest.Operator.NE, poolId));
        }
        return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
    }

    //tuydv1 phương thức check ip da ton tai
    @WebMethod
    public boolean checkDulicateBrasIpPool(String ip) throws Exception {
        return isDulicateBrasIpPool(ip, null);
    }


    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(BrasIppoolDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, true);
        // Validate Ippool la duy nhat
        if (isDulicateBrasIpPool(dto.getIpPool(), dto.getPoolId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.duplicate", dto.getIpPool());
        }
        if (DataUtil.isNullOrEmpty(dto.getProvince())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.input.provice");
        }
        String[] arrProvince = dto.getProvince().split(",");
        if (DataUtil.isNullOrEmpty(arrProvince)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.input.provice");
        }
        for (String areaCode : arrProvince) {
            AreaDTO area = areaRepo.getProcinve(areaCode);
            if (DataUtil.isNullObject(area)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.province");
            }
        }
        BrasIppoolDTO oldBrasIppool = findOne(dto.getPoolId());
        if (!DataUtil.safeEqual(dto.getIpPool(), oldBrasIppool.getIpPool())) {
            oldBrasIppool.setIpPool(dto.getIpPool());
        }
        if (!DataUtil.safeEqual(dto.getBrasIp(), oldBrasIppool.getBrasIp())) {
            oldBrasIppool.setBrasIp(dto.getBrasIp());
        }
        if (!DataUtil.safeEqual(dto.getPoolName(), oldBrasIppool.getPoolName())) {
            oldBrasIppool.setPoolName(dto.getPoolName());
        }
        if (!DataUtil.safeEqual(dto.getPoolUniq(), oldBrasIppool.getPoolUniq())) {
            oldBrasIppool.setPoolUniq(dto.getPoolUniq());
        }
        if (!DataUtil.safeEqual(dto.getDomain(), oldBrasIppool.getDomain())) {
            oldBrasIppool.setDomain(dto.getDomain());
        }
        if (!DataUtil.safeEqual(dto.getIpPool(), oldBrasIppool.getIpPool())) {
            oldBrasIppool.setIpPool(dto.getIpPool());
        }
        if (!DataUtil.safeEqual(dto.getIpMask(), oldBrasIppool.getIpMask())) {
            oldBrasIppool.setIpMask(dto.getIpMask());
        }
        if (!DataUtil.safeEqual(dto.getProvince(), oldBrasIppool.getProvince())) {
            oldBrasIppool.setProvince(dto.getProvince());
        }
        if (!DataUtil.safeEqual(dto.getStatus(), oldBrasIppool.getStatus())) {
            //hoangnt
            validateUpdateStatus(dto.getStatus(), oldBrasIppool.getIpPool());
            oldBrasIppool.setStatus(dto.getStatus());
        }
        oldBrasIppool.setUpdateUser(staffCode);
        oldBrasIppool.setUpdateDatetime(getSysDate(em));
        oldBrasIppool.setDomain(dto.getDomainId());
        oldBrasIppool.setBrasIp(dto.getBrasId());
        if (dto.isSpecificIp()) {
            if (!DataUtil.safeEqual(dto.getIpType(), oldBrasIppool.getIpType())) {
                oldBrasIppool.setIpType(dto.getIpType());
            }
            if (!DataUtil.safeEqual(dto.getIpLabel(), oldBrasIppool.getIpLabel())) {
                oldBrasIppool.setIpLabel(dto.getIpLabel());
            }
        } else {
            oldBrasIppool.setIpType(null);
            oldBrasIppool.setIpLabel(null);
        }
        repository.save(mapper.toPersistenceBean(oldBrasIppool));
        // Xoa ban ghi cu
        repository.deleteIpPool(oldBrasIppool.getPoolId());
        // insert ban ghi moi
        for (String areaCode : arrProvince) {
            repository.insertIppoolProvince(oldBrasIppool.getPoolId(), areaCode);
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }


    public void validateUpdateStatus(String status, String ipPool) throws LogicException, Exception {
        if (DataUtil.safeEqual(status, Const.BRAS_IPPOOL.BRAS_IPPOOL_STATUS_USE)) {
            //chi chuyen ve trang thai dang su dung khi dang dan voi thue bao ben CM
            if (!repository.checkIpAssignSubsubcriber(ipPool)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.update.status.used", ipPool);
            }
        } else if (DataUtil.safeEqual(status, Const.BRAS_IPPOOL.BRAS_IPPOOL_STATUS_FREE)) {
            //chi chuyen ve trang thai moi khi khong chiem boi thue bao nao
            BrasIppoolDTO brasIppoolDTO = repository.checkIpHaveUsedCM(ipPool);
            if (!DataUtil.isNullObject(brasIppoolDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.update.status.free", ipPool);
            }
        }

    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateStatus(BrasIppoolDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        // Validate Ippool la duy nhat
        if (isDulicateBrasIpPool(dto.getIpPool(), dto.getPoolId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.duplicate", dto.getIpPool());
        }
        if (dto.getIpPool().length() > 20) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.maxlength.ipPool");
        }
        //Regex IpPool
        if (!PATTERN.matcher(dto.getIpPool()).matches()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.matches.ipPool");
        }
        BrasIppoolDTO oldBrasIppool = findOne(dto.getPoolId());
        if (!DataUtil.safeEqual(dto.getStatus(), oldBrasIppool.getStatus())) {
            //hoangnt
            validateUpdateStatus(dto.getStatus(), oldBrasIppool.getIpPool());

            oldBrasIppool.setStatus(dto.getStatus());
            oldBrasIppool.setUpdateUser(staffCode);
            oldBrasIppool.setUpdateDatetime(getSysDate(em));
            repository.save(mapper.toPersistenceBean(oldBrasIppool));
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(List<Long> lstId) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        if (DataUtil.isNullObject(lstId)) {
            return null;
        }
        for (Long dtoId : lstId) {
            BrasIppoolDTO brasDTO = findOne(dtoId);
            if (!DataUtil.isNullObject(brasDTO)) {
                if (!DataUtil.isNullOrEmpty(brasDTO.getStatus())
                        && (DataUtil.safeEqual(brasDTO.getStatus(), Const.BRAS_IPPOOL.BRAS_IPPOOL_STATUS_USE)
                        || DataUtil.safeEqual(brasDTO.getStatus(), Const.BRAS_IPPOOL.BRAS_IPPOOL_STATUS_LOCK))) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "brasIppool.canotDelete");
                }
                //hoangnt: check trang thai ben CM truoc khi delete
                BrasIppoolDTO brasIppoolDTO = repository.checkIpHaveUsedCM(brasDTO.getIpPool());
                if (!DataUtil.isNullObject(brasIppoolDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.have.used.cm", brasDTO.getIpPool());
                }
                repository.delete(mapper.toPersistenceBean(brasDTO));
                // Xoa ippool_province
                int update = repository.deleteIpPool(brasDTO.getPoolId());
                if (update < 1) {
                    throw new LogicException("", "bras.ippool.delete.ippool.province.error");
                }
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    public List<BrasIppoolDTO> search(BrasIppoolDTO brasIppoolDTO) throws Exception {
        try {
            return repository.search(brasIppoolDTO);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public List<BrasIppoolDTO> searchByIp(BrasIppoolDTO brasIppoolDTO) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(brasIppoolDTO.getIpPool())) {
            lst.add(new FilterRequest(BrasIppool.COLUMNS.IPPOOL.name(), FilterRequest.Operator.EQ, brasIppoolDTO.getIpPool().trim().toLowerCase(), false));
        }
        return findByFilter(lst);
    }


    /**
     * author Tuydv1
     * add new bras ippool
     *
     * @param dto
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BrasIppoolDTO createNewBrasIppool(BrasIppoolDTO dto, String staffCode) throws Exception, LogicException {
        checkValidate(dto, false);
        // Validate mau so la duy nhat
        if (isDulicateBrasIpPool(dto.getIpPool(), dto.getPoolId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.duplicate", dto.getIpPool());
        }
        //hoangnt check them dk ben CM
        BrasIppoolDTO brasIppoolDTO = repository.checkIpHaveUsedCM(dto.getIpPool());
        if (!DataUtil.isNullObject(brasIppoolDTO)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.have.used.cm", dto.getIpPool());
        }
        if (DataUtil.isNullOrEmpty(dto.getProvince())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.input.provice");
        }
        String[] arrProvince = dto.getProvince().split(",");
        if (DataUtil.isNullOrEmpty(arrProvince)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "brasIpPool.input.provice");
        }
//            String provinceCode = null;
        for (int i = 0; i < arrProvince.length; i++) {
            String areaCode = arrProvince[i];
            AreaDTO area = areaRepo.getProcinve(areaCode);
            if (DataUtil.isNullObject(area)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "brasIpPool.validate.not.contains.province");
            }
        }

        Date currentDate = getSysDate(em);
        dto.setCreateUser(staffCode);
        dto.setCreateDatetime(currentDate);
        dto.setUpdateUser(staffCode);
        dto.setUpdateDatetime(currentDate);
        dto.setDomain(dto.getDomainId());
//            dto.setProvince(provinceCode);
        dto.setBrasIp(dto.getBrasId());
        BrasIppool brasIppool = mapper.toPersistenceBean(dto);
        brasIppool = repository.save(brasIppool);
        // Luu ippool_province
        for (String areaCode : arrProvince) {
            repository.insertIppoolProvince(brasIppool.getPoolId(), areaCode);
        }
        return mapper.toDtoBean(brasIppool);
    }

    /**
     * start service for Sale
     */
    @WebMethod
    public boolean checkBrasIdExist(Long brasId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(BrasIppool.COLUMNS.BRASIP.name(), FilterRequest.Operator.EQ, brasId));
//        lst.add(new FilterRequest(BrasIppool.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
    }

    @Override
    public BaseMessage lockIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(false);
        if (DataUtil.isNullOrEmpty(ippool) || DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_PARAMS_NULL");
            return result;
        }
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(BrasIppool.COLUMNS.IPPOOL.name(), FilterRequest.Operator.EQ, ippool));
        lst.add(new FilterRequest(BrasIppool.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, BRAS_IPPOOL_STATUS_SUPPEND));
        List<BrasIppoolDTO> brasIppoolDTOList = findByFilter(lst);
        if (DataUtil.isNullOrEmpty(brasIppoolDTOList)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_IPPOOL_NOT_FOUND");
            return result;
        }
        for (BrasIppoolDTO brasIppoolDTO : brasIppoolDTOList) {
            if (!DataUtil.safeEqual(brasIppoolDTO.getStatus(), BRAS_IPPOOL_STATUS_FREE)) {
                result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
                result.setKeyMsg("ERR_IPPOOL_STATUS_INVALID");
                return result;
            }
            brasIppoolDTO.setStatus(BRAS_IPPOOL_STATUS_LOCK);
            repository.save(mapper.toPersistenceBean(brasIppoolDTO));
        }
        result.setSuccess(true);
        result.setKeyMsg("SUCCESS");
        return result;
    }

    @Override
    public BaseMessage unlockIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(false);
        if (DataUtil.isNullOrEmpty(ippool) || DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_PARAMS_NULL");
            return result;
        }
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(BrasIppool.COLUMNS.IPPOOL.name(), FilterRequest.Operator.EQ, ippool));
        lst.add(new FilterRequest(BrasIppool.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, BRAS_IPPOOL_STATUS_SUPPEND));
        List<BrasIppoolDTO> brasIppoolDTOList = findByFilter(lst);
        if (DataUtil.isNullOrEmpty(brasIppoolDTOList)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_IPPOOL_NOT_FOUND");
            return result;
        }
        for (BrasIppoolDTO brasIppoolDTO : brasIppoolDTOList) {
            if (!DataUtil.safeEqual(brasIppoolDTO.getStatus(), BRAS_IPPOOL_STATUS_LOCK)) {
                result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
                result.setKeyMsg("ERR_IPPOOL_STATUS_INVALID");
                return result;
            }
            brasIppoolDTO.setStatus(BRAS_IPPOOL_STATUS_FREE);
            repository.save(mapper.toPersistenceBean(brasIppoolDTO));
        }
        result.setSuccess(true);
        result.setKeyMsg("SUCCESS");
        return result;
    }

    @Override
    public List<String> getListStaticIPProvince(String domain, String province, Long ipType) throws Exception, LogicException {
        List<String> result = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(domain) || DataUtil.isNullOrEmpty(province) || DataUtil.isNullOrZero(ipType)) {
            return result;
        }
        result = repository.getListStaticIpProvince(domain, province, ipType, DataUtil.safeToLong(BRAS_IPPOOL_STATUS_FREE), null);
        return result;
    }

    @Override
    public List<String> getListStaticIpSpecialProvince(String domain, String province, Long ipType, Long specialType) throws Exception, LogicException {
        List<String> result = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(domain) || DataUtil.isNullOrEmpty(province) || DataUtil.isNullOrZero(ipType) || DataUtil.isNullOrZero(specialType)) {
            return result;
        }
        result = repository.getListStaticIpProvince(domain, province, ipType, DataUtil.safeToLong(BRAS_IPPOOL_STATUS_FREE), specialType);
        return result;
    }

    @Override
    public BaseMessage registerIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(false);
        if (DataUtil.isNullOrEmpty(ippool) || DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_PARAMS_NULL");
            return result;
        }
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(BrasIppool.COLUMNS.IPPOOL.name(), FilterRequest.Operator.EQ, ippool));
        lst.add(new FilterRequest(BrasIppool.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, BRAS_IPPOOL_STATUS_SUPPEND));
        List<BrasIppoolDTO> brasIppoolDTOList = findByFilter(lst);
        if (DataUtil.isNullOrEmpty(brasIppoolDTOList)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_IPPOOL_NOT_FOUND");
            return result;
        }
        for (BrasIppoolDTO brasIppoolDTO : brasIppoolDTOList) {
            if (!DataUtil.safeEqual(brasIppoolDTO.getStatus(), BRAS_IPPOOL_STATUS_FREE)) {
                result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
                result.setKeyMsg("ERR_IPPOOL_STATUS_INVALID");
                return result;
            }
            brasIppoolDTO.setStatus(BRAS_IPPOOL_STATUS_USED);
            repository.save(mapper.toPersistenceBean(brasIppoolDTO));
        }
        result.setSuccess(true);
        result.setKeyMsg("SUCCESS");
        return result;
    }

    @Override
    public BaseMessage releaseIppool(String ippool, Long shopId, Long staffId) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(false);
        if (DataUtil.isNullOrEmpty(ippool) || DataUtil.isNullOrZero(shopId) || DataUtil.isNullOrZero(staffId)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_PARAMS_NULL");
            return result;
        }
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(BrasIppool.COLUMNS.IPPOOL.name(), FilterRequest.Operator.EQ, ippool));
        lst.add(new FilterRequest(BrasIppool.COLUMNS.STATUS.name(), FilterRequest.Operator.NE, BRAS_IPPOOL_STATUS_SUPPEND));
        List<BrasIppoolDTO> brasIppoolDTOList = findByFilter(lst);
        if (DataUtil.isNullOrEmpty(brasIppoolDTOList)) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("ERR_IPPOOL_NOT_FOUND");
            return result;
        }
        for (BrasIppoolDTO brasIppoolDTO : brasIppoolDTOList) {
            if (!DataUtil.safeEqual(brasIppoolDTO.getStatus(), BRAS_IPPOOL_STATUS_USED)) {
                result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
                result.setKeyMsg("ERR_IPPOOL_STATUS_INVALID");
                return result;
            }
            brasIppoolDTO.setStatus(BRAS_IPPOOL_STATUS_FREE);
            repository.save(mapper.toPersistenceBean(brasIppoolDTO));
        }
        result.setSuccess(true);
        result.setKeyMsg("SUCCESS");
        return result;
    }

    /**
     * End service for sale
     */
    public BrasIppoolDTO getBrasViewIpFromCM(String ippool) throws Exception, LogicException {
        return repository.checkIpHaveUsedCM(ippool);
    }
}

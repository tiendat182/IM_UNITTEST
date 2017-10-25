package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LockUserInfoDTO;
import com.viettel.bccs.inventory.model.LockUserInfo;
import com.viettel.bccs.inventory.model.LogUnlockUserInfo;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.LockUserInfoRepo;
import com.viettel.bccs.inventory.repo.LogUnlockUserInfoRepo;
import com.viettel.bccs.inventory.repo.ShopRepo;
import com.viettel.bccs.inventory.repo.StaffRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.util.Date;
import java.util.List;

@Service
public class LockUserInfoServiceImpl extends BaseServiceImpl implements LockUserInfoService {

    private final BaseMapper<LockUserInfo, LockUserInfoDTO> mapper = new BaseMapper<>(LockUserInfo.class, LockUserInfoDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private LockUserInfoRepo repository;
    @Autowired
    private StaffRepo staffRepository;

    @Autowired
    private ShopRepo shopRepository;

    @Autowired
    private LockUserInfoRepo lockUserInfoRepo;

    @Autowired
    private LogUnlockUserInfoRepo logUnlockUserInfoRepo;

    public static final Logger logger = Logger.getLogger(LockUserInfoService.class);


    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public LockUserInfoDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<LockUserInfoDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<LockUserInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(LockUserInfoDTO dto) throws Exception {
        dto.setCreateDate(getSysDate(em));
        repository.save(mapper.toPersistenceBean(dto));
        BaseMessage result = new BaseMessage();
        result.setSuccess(true);
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(LockUserInfoDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteLockUser(List<Long> lstId) throws Exception {
        repository.deleteLockUser(lstId);
        BaseMessage result = new BaseMessage();
        result.setSuccess(true);
        return result;
    }

    public List<LockUserInfoDTO> getLockUserInfo(String sql, Long lockTypeId, Long validRange, Long checkRange) throws Exception {
        return repository.getLockUserInfo(sql, lockTypeId, validRange, checkRange);
    }

    public boolean checkExistLockUserInfo(Long transId, Long transType, Long transStatus) throws Exception {
        return repository.checkExistLockUserInfo(transId, transType, transStatus);
    }

    public List<LockUserInfoDTO> getLstLockUserInfo() throws Exception {
        return repository.getLstLockUserInfo();
    }

    public boolean canUnlock(String sql, Long transId, Date transDate) throws Exception {
        return repository.canUnlock(sql, transId, transDate);
    }

    @WebMethod
    @Override
    public List<LockUserInfoDTO> getListLockUser(String staffCode, String lockTypeId, String stockTransType) throws Exception {
        if (!DataUtil.isNullOrEmpty(staffCode)) {
            if (!DataUtil.isNullOrEmpty(lockTypeId)) {
                if (!StringUtils.isNumeric(lockTypeId))
                    return null;
            }
            if (!DataUtil.isNullOrEmpty(stockTransType)) {
                if (!StringUtils.isNumeric(stockTransType))
                    return null;
            }
            try {
                Long staffId = staffRepository.getStaffByStaffCode(staffCode).getStaffId();
                return lockUserInfoRepo.getListLockUserV2(staffId, lockTypeId, stockTransType);
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    @WebMethod
    @Override
    public BaseMessage delete(String deleteId, String userId) throws Exception {
        BaseMessage baseMessage = new BaseMessage(false);
        try {
            baseMessage.setSuccess(deleteLogUserInfo(deleteId, userId));
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            baseMessage.setSuccess(false);
        }
        return baseMessage;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLogUserInfo(String deleteId, String userId) throws Exception {
        if (!DataUtil.isNullOrEmpty(deleteId) && !DataUtil.isNullOrEmpty(userId)
                && StringUtils.isNumeric(deleteId) && StringUtils.isNumeric(userId)) {
            Long deleteIdLong = Long.valueOf(deleteId);
            Long userIdLong = Long.valueOf(userId);
            Long deleteShopId = staffRepository.findOne(deleteIdLong).getShopId();
            Long userShopId = staffRepository.findOne(userIdLong).getShopId();
            String userShopPath = shopRepository.findOne(userShopId).getShopPath();

            StringBuilder strQuery = new StringBuilder();
            strQuery.append(
                    "select SHOP_PATH from shop " +
                            "where shop_id = #deleteShopId" +
                            "   and shop_path like #userShopPath1" +
                            "   and shop_path not like #userShopPath2"
            );
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("deleteShopId", deleteShopId);
            query.setParameter("userShopPath1", "%" + userShopPath + "%");
            query.setParameter("userShopPath2", userShopPath);

            List queryResult = query.getResultList();
            if (!DataUtil.isNullOrEmpty(queryResult) || deleteId.equals(userId)) {//? && or ||
                List<LockUserInfo> lst = lockUserInfoRepo.findByStaffId(deleteIdLong);
                lockUserInfoRepo.delete(lst);
                addLogUnlockUserInfo(deleteIdLong, userIdLong);
                return true;
            }
        }
        return false;
    }

    public void addLogUnlockUserInfo(Long deleteId, Long userId) throws Exception {
        Staff unlockStaff = staffRepository.findOne(userId);
        Shop unlockShop = shopRepository.findOne(unlockStaff.getShopId());
        Staff deleteStaff = staffRepository.findOne(deleteId);
        Shop deleteShop = shopRepository.findOne(deleteStaff.getShopId());

        LogUnlockUserInfo logUnlockUserInfo = new LogUnlockUserInfo();
        logUnlockUserInfo.setUnlockShopId(unlockStaff.getShopId());
        logUnlockUserInfo.setUnlockStaffId(unlockStaff.getStaffId());
        logUnlockUserInfo.setUnlockShopCode(unlockShop.getShopCode());
        logUnlockUserInfo.setUnlockShopName(unlockShop.getName());
        logUnlockUserInfo.setUnlockStaffCode(unlockStaff.getStaffCode());
        logUnlockUserInfo.setUnlockStaffName(unlockStaff.getName());

        logUnlockUserInfo.setShopId(deleteShop.getShopId());
        logUnlockUserInfo.setStaffId(deleteStaff.getStaffId());
        logUnlockUserInfo.setShopCode(deleteShop.getShopCode());
        logUnlockUserInfo.setShopName(deleteShop.getName());
        logUnlockUserInfo.setStaffCode(deleteStaff.getStaffCode());
        logUnlockUserInfo.setStaffName(deleteStaff.getName());
        logUnlockUserInfo.setCreateDate(DbUtil.getSysDate(em));
        logUnlockUserInfo.setStatus(1L);
        logUnlockUserInfo.setUnlockShopPath(unlockShop.getShopPath());
        logUnlockUserInfo.setShopPath(deleteShop.getShopPath());

        logUnlockUserInfoRepo.save(logUnlockUserInfo);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processProcedureLockUserDamage(String procedureName) {
        StoredProcedureQuery procedure = em.createStoredProcedureQuery(procedureName);
        procedure.execute();
    }

    @Transactional(rollbackFor = Exception.class)
    public void unlockUserStockInspect() throws Exception {
        lockUserInfoRepo.unlockUserStockInspect();
    }
}

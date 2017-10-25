package com.viettel.bccs.im1.service;

import com.viettel.bccs.im1.dto.InvoiceTemplateIm1DTO;
import com.viettel.bccs.im1.dto.InvoiceTemplateLogIm1DTO;
import com.viettel.bccs.im1.model.InvoiceTemplateIm1;
import com.viettel.bccs.im1.model.InvoiceTemplateLogIm1;
import com.viettel.bccs.im1.repo.InvoiceTemplateIm1Repo;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.StaffService;
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
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceTemplateIm1ServiceImpl extends BaseServiceImpl implements InvoiceTemplateIm1Service {
    public static final Logger logger = Logger.getLogger(InvoiceTemplateIm1Service.class);
    private final BaseMapper<InvoiceTemplateIm1, InvoiceTemplateIm1DTO> mapper = new BaseMapper<>(InvoiceTemplateIm1.class, InvoiceTemplateIm1DTO.class);
    @Autowired
    private StaffService staffService;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM_LIB)
    private EntityManager em;

    @Autowired
    private InvoiceTemplateIm1Repo repository;
    @Autowired
    private InvoiceTemplateLogIm1Service invoiceTemplateLogIm1Service;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceTemplateIm1DTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceTemplateIm1DTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceTemplateIm1DTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceTemplateIm1DTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceTemplateIm1DTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverInvoice(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode, Long amountDelivery) throws LogicException, Exception {
        // Lay danh sach invoice_template
        InvoiceTemplateIm1 invoiceTemplateIm1 = repository.findOne(invoiceDeliveryId);
        InvoiceTemplateIm1DTO invoiceTemplateDeliver = mapper.toDtoBean(invoiceTemplateIm1);
        if (DataUtil.isNullObject(invoiceTemplateDeliver) || !DataUtil.safeEqual(invoiceTemplateDeliver.getType(), Const.OWNER_TYPE.SHOP_LONG)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceTemplate.validate.not.find.im1");
        }
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
        }
        if (!DataUtil.safeEqual(invoiceTemplateDeliver.getShopId(), staffDTO.getShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.not.match.shop.im1");
        }
        // Check So luong phoi hoa don tren IM1 va IM2 phai trung nhau. Neu khong trung nhau thi bao loi
        if (!DataUtil.safeEqual(amountDelivery, invoiceTemplateIm1.getAmount())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.count");
        }
        // Lock bang
        Map<String, Object> properties = new HashMap();
        properties.put("javax.persistence.lock.timeout", 0);
        try {
            em.lock(invoiceTemplateIm1, LockModeType.PESSIMISTIC_READ);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.delivery.im1");
        }
        Date currentDate = getSysDate(em);
        for (InvoiceTemplateDTO invoiceTemplateDTO : lstInvoiceReceive) {
            Long qty = invoiceTemplateDTO.getAmount();
            Long type = DataUtil.safeToLong(invoiceTemplateDTO.getOwnerType());
            long amount = invoiceTemplateDeliver.getAmount();
            invoiceTemplateDeliver.setAmount(amount - qty);
            // Luu log
            InvoiceTemplateLogIm1DTO log1 = new InvoiceTemplateLogIm1DTO();
            log1.setInvoiceTemplateLogId(getSequence(em, "bccs_im.invoice_template_log_seq"));
            log1.setInvoiceTemplateId(invoiceDeliveryId);
            log1.setShopId(staffDTO.getShopId());
            log1.setOrgAmount(amount);
            log1.setAmountApply(-qty);
            log1.setCreateUser(staffDTO.getStaffCode());
            log1.setCreateDate(currentDate);
            log1.setLogType(2L);
            invoiceTemplateLogIm1Service.create(log1);

            InvoiceTemplateIm1 invoiceTemplateReceiver = null;
            if (DataUtil.safeEqual(type, Const.OWNER_TYPE.SHOP_LONG)) {
                invoiceTemplateReceiver = repository.getInvoiceTemplate(invoiceTemplateDTO.getOwnerId(), 1L);
            } else if (DataUtil.safeEqual(type, Const.OWNER_TYPE.STAFF_LONG)) {
                invoiceTemplateReceiver = repository.getInvoiceTemplate(invoiceTemplateDTO.getOwnerId(), 2L);
            }
            Long amount2 = 0L;
            if (invoiceTemplateReceiver == null) {
                invoiceTemplateReceiver = new InvoiceTemplateIm1();
                invoiceTemplateReceiver.setInvoiceTemplateId(invoiceTemplateDTO.getInvoiceTemplateId());
                if (DataUtil.safeEqual(type, Const.OWNER_TYPE.SHOP_LONG)) {
                    invoiceTemplateReceiver.setShopId(invoiceTemplateDTO.getOwnerId());
                } else if (DataUtil.safeEqual(type, Const.OWNER_TYPE.STAFF_LONG)) {
                    invoiceTemplateReceiver.setShopId(staffDTO.getShopId());
                }
                invoiceTemplateReceiver.setCreateUser(staffDTO.getStaffCode());
                invoiceTemplateReceiver.setCreateDate(currentDate);
                invoiceTemplateReceiver.setLastUpdateUser(staffDTO.getName());
                invoiceTemplateReceiver.setLastUpdateDate(currentDate);
                invoiceTemplateReceiver.setAmount(qty);
                invoiceTemplateReceiver.setPreAmount(0L);
                invoiceTemplateReceiver.setUsedAmount(0L);
                if (DataUtil.safeEqual(type, Const.OWNER_TYPE.STAFF_LONG)) {
                    invoiceTemplateReceiver.setStaffId(invoiceTemplateDTO.getOwnerId());
                }
                invoiceTemplateReceiver.setType(type);
                repository.save(invoiceTemplateReceiver);

            } else {
                try {
                    em.lock(invoiceTemplateReceiver, LockModeType.PESSIMISTIC_READ);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.retrieve.im1");
                }
                // Check So luong phoi hoa don tren IM1 va IM2 phai trung nhau. Neu khong trung nhau thi bao loi
                if (!DataUtil.safeEqual(invoiceTemplateDTO.getAmountNotUsed(), invoiceTemplateReceiver.getAmount())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.count");
                }
                amount2 = invoiceTemplateReceiver.getAmount();
                invoiceTemplateReceiver.setAmount(amount2 + qty);
                invoiceTemplateReceiver.setLastUpdateUser(staffDTO.getStaffCode());
                invoiceTemplateReceiver.setLastUpdateDate(currentDate);
                repository.save(invoiceTemplateReceiver);
            }
            InvoiceTemplateLogIm1DTO log2 = new InvoiceTemplateLogIm1DTO();
            log2.setInvoiceTemplateLogId(getSequence(em, "bccs_im.invoice_template_log_seq"));
            log2.setInvoiceTemplateId(invoiceTemplateReceiver.getInvoiceTemplateId());
            if (type.equals(1L)) {
                log2.setShopId(invoiceTemplateDTO.getOwnerId());
            } else if (type.equals(2L)) {
                log2.setShopId(staffDTO.getShopId());
            }
            log2.setFromShopId(staffDTO.getShopId());
            log2.setOrgAmount(amount2);
            log2.setAmountApply(qty);
            log2.setCreateUser(staffDTO.getName());
            log2.setCreateDate(currentDate);
            if (type.equals(1L)) {
                log2.setLogType(3L);
            } else if (type.equals(2L)) {
                log2.setLogType(4L);
            }

            if (type.equals(2L)) {
                log2.setStaffId(invoiceTemplateDTO.getOwnerId());
            }
            invoiceTemplateLogIm1Service.create(log2);
            // Luu log xac nhan phoi hoa don
            if (type.equals(1L)) {
                InvoiceTemplateLogIm1DTO log3 = new InvoiceTemplateLogIm1DTO();
                log3.setInvoiceTemplateLogId(getSequence(em, "bccs_im.invoice_template_log_seq"));
                log3.setInvoiceTemplateId(invoiceTemplateReceiver.getInvoiceTemplateId());
                log3.setShopId(invoiceTemplateDTO.getOwnerId());
                log3.setOrgAmount(amount2);
                log3.setAmountApply(qty);
                log3.setCreateUser(staffDTO.getName());
                log3.setCreateDate(currentDate);
                log3.setLogType(4L);
                invoiceTemplateLogIm1Service.create(log3);
            }
        }
        invoiceTemplateDeliver.setLastUpdateUser(staffDTO.getStaffCode());
        invoiceTemplateDeliver.setLastUpdateDate(currentDate);
        repository.save(mapper.toPersistenceBean(invoiceTemplateDeliver));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeInvoice(Long invoiceReceive, List<InvoiceTemplateDTO> lstInvoiceRevoke, String staffCode, Long amountReceive) throws LogicException, Exception {
        // Lay danh sach invoice_template
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        InvoiceTemplateIm1 invoiceTemplateReceiver = repository.getInvoiceTemplate(staffDTO.getShopId(), 1L);
        if (invoiceTemplateReceiver == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceTemplate.validate.not.find.revoke.im1");
        }
        if (DataUtil.isNullObject(invoiceTemplateReceiver) || !DataUtil.safeEqual(invoiceTemplateReceiver.getInvoiceTemplateId(), invoiceReceive)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceTemplate.validate.not.find.revoke.im1");
        }
        if (DataUtil.isNullObject(staffDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
        }
        if (!DataUtil.safeEqual(invoiceTemplateReceiver.getShopId(), staffDTO.getShopId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.not.match.shop.revoke.im1");
        }
        // Check So luong phoi hoa don tren IM1 va IM2 phai trung nhau. Neu khong trung nhau thi bao loi
        if (!DataUtil.safeEqual(amountReceive, invoiceTemplateReceiver.getAmount())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.count");
        }
        // Lock bang
        Map<String, Object> properties = new HashMap();
        properties.put("javax.persistence.lock.timeout", 0);
        try {
            em.lock(invoiceTemplateReceiver, LockModeType.PESSIMISTIC_READ);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.retrieve.im1");
        }
        Date currentDate = getSysDate(em);
        for (InvoiceTemplateDTO invoiceTemplateRevokeDTO : lstInvoiceRevoke) {
            Long qty = invoiceTemplateRevokeDTO.getAmount();
            Long type = DataUtil.safeToLong(invoiceTemplateRevokeDTO.getOwnerType());
            long amount = invoiceTemplateReceiver.getAmount();
            invoiceTemplateReceiver.setAmount(amount + qty);

            // Luu log
            InvoiceTemplateLogIm1DTO log2 = new InvoiceTemplateLogIm1DTO();
            log2.setInvoiceTemplateLogId(getSequence(em, "bccs_im.invoice_template_log_seq"));
            log2.setInvoiceTemplateId(invoiceTemplateReceiver.getInvoiceTemplateId());
            log2.setShopId(staffDTO.getShopId());
            if (type.equals(1L)) {
                log2.setFromShopId(invoiceTemplateRevokeDTO.getOwnerId());
            } else if (type.equals(2L)) {
                log2.setFromShopId(staffDTO.getShopId());
            }
            log2.setOrgAmount(amount);
            log2.setAmountApply(qty);
            log2.setCreateUser(staffDTO.getName());
            log2.setCreateDate(currentDate);
            log2.setLogType(6L);
            invoiceTemplateLogIm1Service.create(log2);

            // Tru so luong phoi
            InvoiceTemplateIm1 invoiceTemplateRevoke = repository.findOne(invoiceTemplateRevokeDTO.getInvoiceTemplateId());
            if (DataUtil.isNullObject(invoiceTemplateRevoke)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.not.find.im1");
            }
            try {
                em.lock(invoiceTemplateRevoke, LockModeType.PESSIMISTIC_READ);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.delivery.im1");
            }
            // Check So luong phoi hoa don tren IM1 va IM2 phai trung nhau. Neu khong trung nhau thi bao loi
            if (!DataUtil.safeEqual(invoiceTemplateRevokeDTO.getAmountNotUsed(), invoiceTemplateRevoke.getAmount())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.count");
            }
            long amount2 = invoiceTemplateRevoke.getAmount();
            invoiceTemplateRevoke.setAmount(amount2 - qty);
            invoiceTemplateRevoke.setLastUpdateUser(staffDTO.getName());
            invoiceTemplateRevoke.setLastUpdateDate(currentDate);
            repository.save(invoiceTemplateRevoke);
            // Luu log
            InvoiceTemplateLogIm1DTO log1 = new InvoiceTemplateLogIm1DTO();
            log1.setInvoiceTemplateLogId(getSequence(em, "bccs_im.invoice_template_log_seq"));
            log1.setInvoiceTemplateId(invoiceTemplateRevoke.getInvoiceTemplateId());
            if (type.equals(1L)) {
                log1.setShopId(invoiceTemplateRevokeDTO.getOwnerId());
            } else if (type.equals(2L)) {
                log1.setShopId(staffDTO.getShopId());
            }
            log1.setOrgAmount(amount2);
            log1.setAmountApply(-qty);
            log1.setCreateUser(staffDTO.getName());
            log1.setCreateDate(currentDate);
            log1.setLogType(5L);
            if (type.equals(2L)) {
                log1.setStaffId(invoiceTemplateRevokeDTO.getOwnerId());
            }
            invoiceTemplateLogIm1Service.create(log1);
        }
        invoiceTemplateReceiver.setLastUpdateUser(staffDTO.getStaffCode());
        invoiceTemplateReceiver.setLastUpdateDate(currentDate);
        repository.save(invoiceTemplateReceiver);
    }
}

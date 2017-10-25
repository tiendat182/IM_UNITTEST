package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.im1.service.InvoiceTemplateIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.InvoiceTemplate;
import com.viettel.bccs.inventory.model.QInvoiceTemplate;
import com.viettel.bccs.inventory.repo.InvoiceTemplateRepo;
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
import java.util.List;

@Service
public class InvoiceTemplateServiceImpl extends BaseServiceImpl implements InvoiceTemplateService {

    //    private final InvoiceTemplateMapper mapper = new InvoiceTemplateMapper();
    private final BaseMapper<InvoiceTemplate, InvoiceTemplateDTO> mapper = new BaseMapper(InvoiceTemplate.class, InvoiceTemplateDTO.class);

    private static final Long TEMPLATE_SHOP_TYPE = 1L;
    private static final String TEMPLATE_SHOP_TYPE_STR = "1";
    private static final String TEMPLATE_STAFF_TYPE_STR = "2";
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private InvoiceTemplateRepo repository;

    @Autowired
    private InvoiceTemplateLogService invoiceTemplateLogService;
    @Autowired
    private InvoiceTemplateTypeService invoiceTemplateTypeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private InvoiceTemplateIm1Service invoiceTemplateIm1Service;

    private StaffDTO staffDTO;
    private InvoiceTemplateTypeDTO invoiceTemplateTypeDTO;


    public static final Logger logger = Logger.getLogger(InvoiceTemplateService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceTemplateDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceTemplateDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceTemplateDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByName = QInvoiceTemplate.invoiceTemplate.createDatetime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceTemplateDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    private void checkValidateTemplate(InvoiceTemplateDTO dto, String staffCode, boolean isCreate, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceTemplate.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getInvoiceTemplateId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceTemplate.validate.require.invoiceTemplateId");
            }
            if (DataUtil.isNullOrZero(dto.getInvoiceTemplateTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceTemplate.validate.require.invoiceTemplateTypeId");
            }
            if (DataUtil.isNullOrZero(dto.getOwnerId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceTemplate.validate.require.ownerId");
            }
            if (DataUtil.isNullOrZero(dto.getAmount())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceTemplate.validate.require.amount");
            }
            if (DataUtil.isNullOrEmpty(staffCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staffCode.validate.require");
            }
            if (!isCreate) {
                if (DataUtil.isNullOrEmpty(dto.getOwnerType())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceTemplate.validate.require.ownerType");
                }
                if (!dto.getOwnerType().equals(TEMPLATE_SHOP_TYPE_STR) && !dto.getOwnerType().equals(TEMPLATE_STAFF_TYPE_STR)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.not.contains.ownerType");
                }
            }
            //maxlength
            if (ValidateService.countDigitOfNumber(dto.getAmount()) > 10l) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.maxlength.amount");
            }
            if (staffCode.length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate.maxlength");
            }
            // contains in database - selectItems
            staffDTO = staffService.getStaffByStaffCode(staffCode);
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
            }
            invoiceTemplateTypeDTO = invoiceTemplateTypeService.findOne(dto.getInvoiceTemplateTypeId());
            if (DataUtil.isNullObject(invoiceTemplateTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.not.contains.invoiceTemplateTypeId");
            }
//            //compare Id cua user dang nhap
//            if (isCreate && isupdate) {
//                if (!dto.getOwnerId().toString().equals(staffDTO.getShopId().toString())) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.not.contains.shopId");
//                }
//            }

        } catch (LogicException ex) {
            throw ex;
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceTemplateDTO invoiceTemplateDTO, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidateTemplate(invoiceTemplateDTO, staffCode, true, true);
        try {
            InvoiceTemplateDTO dto = findOne(invoiceTemplateDTO.getInvoiceTemplateId());
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "invoice.template.update.error", invoiceTemplateDTO.getInvoiceTemplateId());
            }
            if (invoiceTemplateDTO.getAmount() > dto.getAmount()) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceTemplate.update.amount");
            }
            dto.setUpdateUser(staffCode);
            dto.setUpdateDatetime(getSysDate(em));
            dto.setAmount(dto.getAmount() - invoiceTemplateDTO.getAmount());
            repository.save(mapper.toPersistenceBean(dto));
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceTemplate.update");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private List<InvoiceTemplateDTO> getInvoiceTemplateExsit(Long ownerId, Long invoiceTemplateTypeId) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, ownerId));
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.INVOICETEMPLATETYPEID.name(), FilterRequest.Operator.EQ, invoiceTemplateTypeId));
            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    public List<InvoiceTemplateDTO> getInvoiceTemplateExsitByOwnerType(Long ownerId, Long invoiceTemplateTypeId, String ownerType) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, ownerId));
            if (!DataUtil.isNullOrZero(invoiceTemplateTypeId)) {
                lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.INVOICETEMPLATETYPEID.name(), FilterRequest.Operator.EQ, invoiceTemplateTypeId));
            }
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, ownerType));
            if (DataUtil.safeEqual(ownerType, Const.OWNER_TYPE.STAFF)) {
                StaffDTO staffDTO = staffService.findOne(ownerId);
                lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.FROMSHOPID.name(), FilterRequest.Operator.EQ, staffDTO.getShopId()));
            }
            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public List<InvoiceTemplateDTO> search(InvoiceTemplateDTO invoiceTemplateDTO) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            if (!DataUtil.isNullOrZero(invoiceTemplateDTO.getOwnerId())) {
                lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, invoiceTemplateDTO.getOwnerId().toString().trim()));
            }
            if (!DataUtil.isNullOrZero(invoiceTemplateDTO.getAmount())) {
                lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.AMOUNT.name(), FilterRequest.Operator.EQ, invoiceTemplateDTO.getAmount().toString().trim()));
            }
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, TEMPLATE_SHOP_TYPE));
            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public InvoiceTemplateDTO createNewInvoiceTemplate(InvoiceTemplateDTO invoiceTemplateDTO, String staffCode, String typeAddNew) throws Exception, LogicException {
        checkValidateTemplate(invoiceTemplateDTO, staffCode, true, false);
        try {
            Date currentDate = getSysDate(em);
            List<InvoiceTemplateDTO> lst = getInvoiceTemplateExsit(invoiceTemplateDTO.getOwnerId(), invoiceTemplateDTO.getInvoiceTemplateTypeId());
            if (DataUtil.isNullOrEmpty(lst)) {
                invoiceTemplateDTO.setCreateUser(staffCode);
                invoiceTemplateDTO.setCreateDatetime(currentDate);
                invoiceTemplateDTO.setUpdateUser(staffCode);
                invoiceTemplateDTO.setUpdateDatetime(currentDate);
                invoiceTemplateDTO.setUsedAmount(0L);
                InvoiceTemplate invoiceTemplate = mapper.toPersistenceBean(invoiceTemplateDTO);
                invoiceTemplate = repository.save(invoiceTemplate);
                //ghi log
                if (DataUtil.safeEqual(typeAddNew, Const.INVOICE_TYPE.TEMPLATE_TYPE_ADD_NEW)) {
                    InvoiceTemplateLogDTO invoiceTemplateLogDTO = saveLogDelivery(invoiceTemplate, 0L, Const.INVOICE_TYPE.TEMPLATE_LOG_ADD_NEW);
                    invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
                }
                return mapper.toDtoBean(invoiceTemplate);
            } else {
                InvoiceTemplateDTO dto = findOne(lst.get(0).getInvoiceTemplateId());
                if (!DataUtil.isNullObject(dto)) {
                    Long beforAmount = dto.getAmount();
                    dto.setUpdateUser(staffCode);
                    dto.setUpdateDatetime(currentDate);
                    Long total = dto.getAmount() + invoiceTemplateDTO.getAmount();
                    if (ValidateService.countDigitOfNumber(total) > 10l) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.maxlength.amount", total);
                    }
                    dto.setAmount(dto.getAmount() + invoiceTemplateDTO.getAmount());
                    InvoiceTemplate invoiceTemplate = mapper.toPersistenceBean(dto);
                    invoiceTemplate = repository.save(invoiceTemplate);
                    //ghi log
                    if (DataUtil.safeEqual(typeAddNew, Const.INVOICE_TYPE.TEMPLATE_TYPE_ADD_NEW)) {
                        InvoiceTemplateLogDTO invoiceTemplateLogDTO = saveLogDelivery(invoiceTemplate, beforAmount, Const.INVOICE_TYPE.TEMPLATE_LOG_ADD_NEW);
                        invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
                    }
                    return mapper.toDtoBean(invoiceTemplate);
                }
            }
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
        return null;
    }

    private InvoiceTemplateLogDTO saveLogDelivery(InvoiceTemplate invoiceReceive, Long orgAmount, String logType) throws Exception, LogicException {
        try {
            InvoiceTemplateLogDTO invoiceTemplateLog = new InvoiceTemplateLogDTO();
            invoiceTemplateLog.setInvoiceTemplateId(invoiceReceive.getInvoiceTemplateId());
            if (DataUtil.safeEqual(logType, Const.INVOICE_TYPE.TEMPLATE_LOG_REVEIVE) || DataUtil.safeEqual(logType, Const.INVOICE_TYPE.TEMPLATE_LOG_BE_RETRIEVE)) {
                invoiceTemplateLog.setFromShopId(invoiceReceive.getFromShopId());
            }
            invoiceTemplateLog.setOwnerId(invoiceReceive.getOwnerId());
            invoiceTemplateLog.setOwnerType(invoiceReceive.getOwnerType());
            invoiceTemplateLog.setOrgAmount(orgAmount);
            invoiceTemplateLog.setAfterAmount(invoiceReceive.getAmount().toString());
            invoiceTemplateLog.setLogType(logType);
            return invoiceTemplateLog;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deliveryInvoiceTempalte(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {
            InvoiceTemplate invoiceTemplate = repository.findOne(invoiceDeliveryId);
            //lock ban ghi giao phoi
            try {
                em.lock(invoiceTemplate, LockModeType.PESSIMISTIC_READ);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.delivery");
            }
            InvoiceTemplateDTO dtoDelivery = mapper.toDtoBean(invoiceTemplate);
            Long amount = dtoDelivery.getAmount();
            Date currentDate = getSysDate(em);
            for (InvoiceTemplateDTO invoiceTemplateDTO : lstInvoiceReceive) {
                checkValidateTemplate(invoiceTemplateDTO, staffCode, false, false);
                List<InvoiceTemplateDTO> lst = getInvoiceTemplateExsitByOwnerType(invoiceTemplateDTO.getOwnerId(), invoiceTemplateDTO.getInvoiceTemplateTypeId(), invoiceTemplateDTO.getOwnerType());
                Long amountDelivery = dtoDelivery.getAmount();
                dtoDelivery.setAmount(dtoDelivery.getAmount() - invoiceTemplateDTO.getAmount());
                dtoDelivery.setUsedAmount(dtoDelivery.getUsedAmount() + invoiceTemplateDTO.getAmount());
                //ghi log assign
                InvoiceTemplateLogDTO invoiceTemplateLogDTO = saveLogDelivery(mapper.toPersistenceBean(dtoDelivery), amountDelivery, Const.INVOICE_TYPE.TEMPLATE_LOG_ASSIGN);
                invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
                if (DataUtil.isNullOrEmpty(lst)) {
                    invoiceTemplateDTO.setFromShopId(dtoDelivery.getOwnerId());
                    InvoiceTemplateDTO invoiceReceive = createNewInvoiceTemplate(invoiceTemplateDTO, staffCode, Const.INVOICE_TYPE.TEMPLATE_TYPE_ASSIGN);
                    invoiceTemplateLogDTO = saveLogDelivery(mapper.toPersistenceBean(invoiceReceive), 0L, Const.INVOICE_TYPE.TEMPLATE_LOG_REVEIVE);
                    invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
                    invoiceTemplateDTO.setInvoiceTemplateId(invoiceReceive.getInvoiceTemplateId());
                } else {

                    //lock ban ghi duoc giao
                    InvoiceTemplate receive = repository.findOne(lst.get(0).getInvoiceTemplateId());
                    try {
                        em.lock(receive, LockModeType.PESSIMISTIC_READ);
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.retrieve");
                    }
                    InvoiceTemplateDTO dtoReceive = mapper.toDtoBean(receive);
                    Long orgAmount = dtoReceive.getAmount();
                    Long total = dtoReceive.getAmount() + invoiceTemplateDTO.getAmount();
                    if (ValidateService.countDigitOfNumber(total) > 10l) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.maxlength.amount");
                    }
                    dtoReceive.setAmount(dtoReceive.getAmount() + invoiceTemplateDTO.getAmount());
                    dtoReceive.setFromShopId(dtoDelivery.getOwnerId());
                    dtoReceive.setUpdateDatetime(currentDate);
                    dtoReceive.setUpdateUser(staffCode);
                    InvoiceTemplate invoiceReceive = repository.save(mapper.toPersistenceBean(dtoReceive));
                    //ghi log receive
                    invoiceReceive.setFromShopId(dtoDelivery.getOwnerId());
                    invoiceTemplateLogDTO = saveLogDelivery(invoiceReceive, orgAmount, Const.INVOICE_TYPE.TEMPLATE_LOG_REVEIVE);
                    invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
                }
                dtoDelivery.setUpdateDatetime(currentDate);
                dtoDelivery.setUpdateUser(staffCode);
                repository.save(mapper.toPersistenceBean(dtoDelivery));
                //
            }
            // Thuc hien luu tren DB IM1
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                    && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                invoiceTemplateIm1Service.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amount);
            }
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage retrieveInvoiceTempalte(Long invoiceDeliveryId, List<InvoiceTemplateDTO> lstInvoiceReceive, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {
            Date currentDate = getSysDate(em);
            //lock ban ghi nhan phoi
            InvoiceTemplate invoice = repository.findOne(invoiceDeliveryId);
            try {
                em.lock(invoice, LockModeType.PESSIMISTIC_READ);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.retrieve");
            }
            InvoiceTemplateDTO dtoRetrieve = mapper.toDtoBean(invoice);
            Long amountRetrieve = dtoRetrieve.getAmount();
            for (InvoiceTemplateDTO invoiceTemplateDTO : lstInvoiceReceive) {
                checkValidateTemplate(invoiceTemplateDTO, staffCode, false, false);
                InvoiceTemplate receive = repository.findOne(invoiceTemplateDTO.getInvoiceTemplateId());
                //lock ban ghi bi thu hoi
                try {
                    em.lock(receive, LockModeType.PESSIMISTIC_READ);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceTemplate.validate.lock.revoke");
                }
                InvoiceTemplateDTO dtoReceive = mapper.toDtoBean(receive);
                dtoRetrieve.setAmount(dtoRetrieve.getAmount() + invoiceTemplateDTO.getAmount());
                Long orgAmount = dtoReceive.getAmount();
                if (invoiceTemplateDTO.getAmount() > dtoReceive.getAmount()) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "mn.invoice.info.revoke.invoice.template");
                }
                dtoReceive.setAmount(dtoReceive.getAmount() - invoiceTemplateDTO.getAmount());
                dtoReceive.setUpdateDatetime(currentDate);
                dtoReceive.setUpdateUser(staffCode);
                InvoiceTemplate invoiceTemplate = repository.save(mapper.toPersistenceBean(dtoReceive));
                invoiceTemplate.setFromShopId(dtoRetrieve.getOwnerId());
                dtoRetrieve.setUpdateDatetime(currentDate);
                dtoRetrieve.setUpdateUser(staffCode);
                InvoiceTemplateLogDTO invoiceTemplateLogDTO = saveLogDelivery(invoiceTemplate, orgAmount, Const.INVOICE_TYPE.TEMPLATE_LOG_BE_RETRIEVE);
                invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
                repository.save(mapper.toPersistenceBean(dtoRetrieve));
            }
            //ghi log thu hoi
            InvoiceTemplateLogDTO invoiceTemplateLogDTO = saveLogDelivery(mapper.toPersistenceBean(dtoRetrieve), amountRetrieve, Const.INVOICE_TYPE.TEMPLATE_LOG_RETRIEVE);
            invoiceTemplateLogService.create(invoiceTemplateLogDTO, staffCode, currentDate);
            // Thuc hien luu tren DB IM1
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                    && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                invoiceTemplateIm1Service.revokeInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountRetrieve);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @WebMethod
    public List<InvoiceTemplateDTO> getAllReceiveInvoiceTemplateByShopId(Long shopId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.FROMSHOPID.name(), FilterRequest.Operator.EQ, shopId));
            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    public List<InvoiceTemplateDTO> getInvoiceTemplateList(Long shopId) throws Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, TEMPLATE_SHOP_TYPE));
            lst.add(new FilterRequest(InvoiceTemplate.COLUMNS.OWNERID.name(), FilterRequest.Operator.EQ, shopId));
            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.InvoiceSerialDTO;
import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.InvoiceType;
import com.viettel.bccs.inventory.model.QInvoiceType;
import com.viettel.bccs.inventory.repo.InvoiceTypeRepo;
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
public class InvoiceTypeServiceImpl extends BaseServiceImpl implements InvoiceTypeService {

    private final BaseMapper<InvoiceType, InvoiceTypeDTO> mapper = new BaseMapper(InvoiceType.class, InvoiceTypeDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private InvoiceTypeRepo repository;
    @Autowired
    private OptionSetValueService optionSetService;
    @Autowired
    private InvoiceSerialService invoiceSerialService;
    @Autowired
    private StaffService staffService;

    private List<OptionSetValueDTO> listInvoiceType = Lists.newArrayList();
    private List<OptionSetValueDTO> listType = Lists.newArrayList();
    private StaffDTO staffDTO;

    public static final Logger logger = Logger.getLogger(InvoiceTypeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceTypeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceTypeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByName = QInvoiceType.invoiceType1.createDatetime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    private void checkValidate(InvoiceTypeDTO dto, String staffCode, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceType.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getInvoiceTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceType.validate.require.invoiceTypeId");
            }
            if (DataUtil.isNullOrEmpty(dto.getInvoiceName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceType.validate.require.invoiceName");
            }
            if (DataUtil.isNullOrEmpty(dto.getInvoiceType())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceType.validate.require.invoiceType");
            }
            if (DataUtil.isNullOrEmpty(dto.getTypeInv())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceType.validate.require.typeInv");
            }
            if (DataUtil.isNullOrEmpty(dto.getType())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceType.validate.require.type");
            }
            if (DataUtil.isNullOrZero(dto.getInvoiceNoLength())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceType.validate.require.invoiceNoLength");
            }
            if (DataUtil.isNullOrEmpty(staffCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staffCode.validate.require");
            }

            //maxlength
            if (dto.getInvoiceName().length() > 100) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceType.validate.maxlength.invoiceName");
            }
            if (dto.getTypeInv().length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceType.validate.maxlength.typeInv");
            }
            if (ValidateService.countDigitOfNumber(dto.getInvoiceNoLength()) > 10l) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceType.validate.maxlength.invoiceNoLength");
            }
            if (staffCode.length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate.maxlength");
            }
            // contains in database - selectItems
            staffDTO = staffService.getStaffByStaffCode(staffCode);
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
            }
            listInvoiceType = optionSetService.getLsOptionSetValueByCode(Const.INVOICE_SERIAL.INVOICE_TYPE);
            listType = optionSetService.getLsOptionSetValueByCode(Const.INVOICE_TYPE.TYPE);
            if (!ValidateService.checkValueContainOptionSet(dto.getInvoiceType(), listInvoiceType)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceType.validate.not.contains.invoiceType");
            }
            if (!ValidateService.checkValueContainOptionSet(dto.getType(), listType)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceType.validate.not.contains.type");
            }
            //trim space
            dto.setInvoiceName(dto.getInvoiceName().trim());
            dto.setTypeInv(dto.getTypeInv().trim());
        } catch (LogicException ex) {
            throw ex;
        }
    }

    /**
     * author Hoangnt
     * search InvoiceType
     *
     * @param invoiceTypeDTO
     * @return
     * @throws Exception
     */
    @WebMethod
    public List<InvoiceTypeDTO> search(InvoiceTypeDTO invoiceTypeDTO) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            if (!DataUtil.isNullOrEmpty(invoiceTypeDTO.getInvoiceType())) {
                lst.add(new FilterRequest(InvoiceType.COLUMNS.INVOICETYPE.name(), FilterRequest.Operator.EQ, invoiceTypeDTO.getInvoiceType().trim()));
            }
            if (!DataUtil.isNullOrEmpty(invoiceTypeDTO.getTypeInv())) {
                lst.add(new FilterRequest(InvoiceType.COLUMNS.TYPEINV.name(), FilterRequest.Operator.LIKE, invoiceTypeDTO.getTypeInv().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrEmpty(invoiceTypeDTO.getInvoiceName())) {
                lst.add(new FilterRequest(InvoiceType.COLUMNS.INVOICENAME.name(), FilterRequest.Operator.LIKE, invoiceTypeDTO.getInvoiceName().toLowerCase().trim(), false));
            }
            if (!DataUtil.isNullOrZero(invoiceTypeDTO.getInvoiceNoLength())) {
                lst.add(new FilterRequest(InvoiceType.COLUMNS.INVOICENOLENGTH.name(), FilterRequest.Operator.EQ, invoiceTypeDTO.getInvoiceNoLength().toString().trim()));
            }
            if (!DataUtil.isNullOrEmpty(invoiceTypeDTO.getType())) {
                lst.add(new FilterRequest(InvoiceType.COLUMNS.TYPE.name(), FilterRequest.Operator.EQ, invoiceTypeDTO.getType().trim()));
            }
            lst.add(new FilterRequest(InvoiceType.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
            return findByFilter(lst);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "Error Exception");
        }
    }

    /**
     * @param type
     * @return
     * @throws Exception
     */
    public List<InvoiceTypeDTO> getInvoiceType(Long type) throws Exception, LogicException {
        return repository.getInvoiceType(type);
    }


    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceTypeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    /**
     * author HoangNT
     * add new invoiceType
     *
     * @param dto
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public InvoiceTypeDTO createNewInvoiceType(InvoiceTypeDTO dto, String staffCode) throws Exception, LogicException {
        checkValidate(dto, staffCode, false);
        // Validate mau so la duy nhat
        if (isDulicateInvoiceType(dto.getTypeInv(), dto.getInvoiceTypeId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceType.duplicate.type", dto.getTypeInv());
        }
        try {
            Date currentDate = getSysDate(em);
            dto.setCreateUser(staffCode);
            dto.setCreateDatetime(currentDate);
            dto.setUpdateUser(staffCode);
            dto.setUpdateDatetime(currentDate);
            dto.setStatus(Const.STATUS_ACTIVE);
            InvoiceType invoiceType = mapper.toPersistenceBean(dto);
            invoiceType = repository.save(invoiceType);
            return mapper.toDtoBean(invoiceType);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    private boolean isDulicateInvoiceType(String invoiceType, Long invoiceTypeId) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(InvoiceType.COLUMNS.TYPEINV.name(), FilterRequest.Operator.EQ, invoiceType));
            lst.add(new FilterRequest(InvoiceType.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            if (!DataUtil.isNullOrZero(invoiceTypeId)) {
                lst.add(new FilterRequest(InvoiceType.COLUMNS.INVOICETYPEID.name(), FilterRequest.Operator.NE, invoiceTypeId));
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
        for (Long invoiceTypeId : lstId) {
            try {
                InvoiceTypeDTO invoiceTypeDTO = findOne(invoiceTypeId);
                if (DataUtil.isNullObject(invoiceTypeDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "invoice.invoiceType.delete.error", invoiceTypeId);
                }
                boolean isExistInvoiceSerial = isExistInvoiceSerial(invoiceTypeId);
                if (isExistInvoiceSerial) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "invoice.invoiceType.delete.exist.list", invoiceTypeDTO.getInvoiceName());
                }
                invoiceTypeDTO.setStatus(Const.STATUS.NO_ACTIVE);
                invoiceTypeDTO.setUpdateUser(staffCode);
                invoiceTypeDTO.setUpdateDatetime(getSysDate(em));
                repository.save(mapper.toPersistenceBean(invoiceTypeDTO));
            } catch (LogicException ex) {
                throw ex;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceType.delete");
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private boolean isExistInvoiceSerial(Long invoiceTypeId) throws Exception {
        List<InvoiceSerialDTO> lstInvoiceSerial = invoiceSerialService.getInvoiceSerialFromInvoiceType(invoiceTypeId);
        if (!DataUtil.isNullOrEmpty(lstInvoiceSerial)) {
            return true;
        }
        return false;
    }

    /**
     * Update invoice_type
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceTypeDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, staffCode, true);
        // Validate mau so la duy nhat
        if (isDulicateInvoiceType(dto.getTypeInv(), dto.getInvoiceTypeId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceType.duplicate.type", dto.getTypeInv());
        }
        try {
            InvoiceTypeDTO invoiceTypeDTO = findOne(dto.getInvoiceTypeId());
            if (DataUtil.isNullObject(invoiceTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "invoice.invoiceType.update.error", dto.getInvoiceTypeId());
            }
            // set lai cac gia tri update.
            if (!DataUtil.isNullOrEmpty(dto.getInvoiceType())) {
                invoiceTypeDTO.setInvoiceType(dto.getInvoiceType());
            }
            if (!DataUtil.isNullOrEmpty(dto.getInvoiceName())) {
                invoiceTypeDTO.setInvoiceName(dto.getInvoiceName());
            }
            if (!DataUtil.isNullOrEmpty(dto.getType())) {
                invoiceTypeDTO.setType(dto.getType());
            }
            if (!DataUtil.isNullOrEmpty(dto.getTypeInv())) {
                invoiceTypeDTO.setTypeInv(dto.getTypeInv());
            }
            if (!DataUtil.isNullOrZero(dto.getInvoiceNoLength())) {
                invoiceTypeDTO.setInvoiceNoLength(dto.getInvoiceNoLength());
            }
            invoiceTypeDTO.setUpdateUser(staffCode);
            invoiceTypeDTO.setUpdateDatetime(getSysDate(em));
            repository.save(mapper.toPersistenceBean(invoiceTypeDTO));
        }catch (LogicException e){
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceType.update");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }
}

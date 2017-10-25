package com.viettel.bccs.inventory.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.InvoiceList;
import com.viettel.bccs.inventory.model.QInvoiceList;
import com.viettel.bccs.inventory.repo.InvoiceListRepo;
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
public class InvoiceListServiceImpl extends BaseServiceImpl implements InvoiceListService {

    //    private final InvoiceListMapper mapper = new InvoiceListMapper();
    private final BaseMapper<InvoiceList, InvoiceListDTO> mapper = new BaseMapper(InvoiceList.class, InvoiceListDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private InvoiceListRepo repository;

    @Autowired
    private InvoiceActionLogService invoiceActionLogService;
    @Autowired
    private InvoiceTypeService invoiceTypeService;

    @Autowired
    private InvoiceSerialService invoiceSerialService;

    @Autowired
    private StaffService staffService;

    private StaffDTO staffDTO;
    private InvoiceSerialDTO invoiceSerialDTO;

    public static final Logger logger = Logger.getLogger(InvoiceListService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceListDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceListDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceListDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByName = QInvoiceList.invoiceList.createDatetime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceListDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    private void checkValidate(InvoiceListDTO dto, String staffCode, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceList.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getInvoiceListId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceList.validate.require.invoiceListId");
            }
            if (!isUpdate && DataUtil.isNullOrZero(dto.getInvoiceSerialId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceList.validate.require.invoiceSerialId");
            }
            if (DataUtil.isNullOrZero(dto.getFromInvoice())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceList.validate.require.fromInvocie");
            }
            if (DataUtil.isNullOrZero(dto.getToInvoice())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceList.validate.require.toInvoice");
            }
            if (DataUtil.isNullOrEmpty(staffCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staffCode.validate.require");
            }
            if (staffCode.length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate.maxlength");
            }
            // contains in database - selectItems
            staffDTO = staffService.getStaffByStaffCode(staffCode);
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
            }
            if (!isUpdate) {
                invoiceSerialDTO = invoiceSerialService.findOne(dto.getInvoiceSerialId());
                if (DataUtil.isNullObject(invoiceSerialDTO)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceList.validate.not.contains.invoiceSerialId");
                }
                dto.setSerialNo(invoiceSerialDTO.getSerialNo());
                dto.setShopId(staffDTO.getShopId());
            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    /**
     * author HoangNT
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceListDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, staffCode, true);
        // Validate bo {invoice_serial_id + serialNo + dai ap dung} la duy nhat
        if (!isDulicateInvoiceList(dto.getInvoiceListId(), dto.getSerialNo(), dto.getFromInvoice(), dto.getToInvoice(), dto.getInvoiceSerialId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceList.duplicate", dto.getSerialNo(), dto.getFromInvoice(), dto.getToInvoice());
        }
        try {
            InvoiceListDTO invoiceListDTO = findOne(dto.getInvoiceListId());
            if (DataUtil.isNullObject(invoiceListDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "invoice.list.update.error", dto.getInvoiceListId());
            }
            // set lai cac gia tri update.
            if (!DataUtil.isNullOrZero(dto.getFromInvoice())) {
                invoiceListDTO.setFromInvoice(dto.getFromInvoice());
            }
            if (!DataUtil.isNullOrZero(dto.getToInvoice())) {
                invoiceListDTO.setToInvoice(dto.getToInvoice());
            }
            if (!DataUtil.isNullOrZero(dto.getFromInvoice()) && !DataUtil.isNullOrZero(dto.getToInvoice())) {
                if (dto.getCurrInvoice() < dto.getFromInvoice()) {
                    invoiceListDTO.setCurrInvoice(dto.getFromInvoice() - 1L);
                }
            }
            invoiceListDTO.setUpdateDatetime(getSysDate(em));
            invoiceListDTO.setUpdateUser(staffCode);
            InvoiceList invoiceList = repository.save(mapper.toPersistenceBean(invoiceListDTO));
            //Luwu log vao bang action_log
            InvoiceActionLogDTO invoiceAction = saveActionLog(invoiceList, Const.INVOICE_TYPE.ACTION_LOG_INVOICE_LIST_UPDATE);
            invoiceAction.setCreateUser(staffCode);
            invoiceAction.setCreateDatetime(getSysDate(em));
            invoiceActionLogService.create(invoiceAction);
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceList.update");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    private InvoiceActionLogDTO saveActionLog(InvoiceList invoiceList, String type) {
        InvoiceActionLogDTO invoiceAction = new InvoiceActionLogDTO();
        invoiceAction.setActionType(type);
        invoiceAction.setInvoiceListId(invoiceList.getInvoiceListId());
        invoiceAction.setSerialNo(invoiceList.getSerialNo());
        invoiceAction.setFromInvoice(invoiceList.getFromInvoice());
        invoiceAction.setToInvoice(invoiceList.getToInvoice());
        invoiceAction.setCurInvoice(invoiceList.getCurrInvoice());
        return invoiceAction;
    }

    /**
     * author HoangNT
     *
     * @param dto
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvoiceListDTO createNewInvoiceList(InvoiceListDTO dto, String staffCode) throws Exception, LogicException {
        checkValidate(dto, staffCode, false);
        // Validate {SerialNo + dai ap dung} la duy nhat
        if (!isDulicateInvoiceList(dto.getInvoiceListId(), dto.getSerialNo(), dto.getFromInvoice(), dto.getToInvoice(), dto.getInvoiceSerialId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceList.duplicate", dto.getSerialNo(), dto.getFromInvoice(), dto.getToInvoice());
        }
        try {
            Date currentDate = getSysDate(em);
            dto.setCreateUser(staffCode);
            dto.setCreateDatetime(currentDate);
            dto.setUpdateUser(staffCode);
            dto.setUpdateDatetime(currentDate);
            dto.setStatus(Const.STATUS_ACTIVE);
            dto.setCurrInvoice(dto.getFromInvoice() - 1);
            InvoiceList invoiceList = mapper.toPersistenceBean(dto);
            invoiceList = repository.save(invoiceList);

            //Luwu log vao bang action_log
            InvoiceActionLogDTO invoiceAction = saveActionLog(invoiceList, Const.INVOICE_TYPE.ACTION_LOG_INVOICE_LIST_ADD);
            invoiceAction.setCreateUser(staffCode);
            invoiceAction.setCreateDatetime(currentDate);
            invoiceActionLogService.create(invoiceAction);
            return mapper.toDtoBean(invoiceList);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    private boolean checkLengthInvocie(Long endNo, Long serialId) throws Exception {
        boolean ok = true;
        try {
            InvoiceSerialDTO invoiceSerialDTO = invoiceSerialService.findOne(serialId);
            InvoiceTypeDTO invoiceTypeDTO = invoiceTypeService.findOne(invoiceSerialDTO.getInvoiceTypeId());
            if (ValidateService.countDigitOfNumber(endNo) > invoiceTypeDTO.getInvoiceNoLength().longValue()) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceList.error.add.length", invoiceTypeDTO.getInvoiceNoLength());
            }
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
        return ok;
    }


    /**
     * author HoangNT
     *
     * @param serialNo
     * @param startNo
     * @param endNo
     * @return
     * @throws Exception
     */
    private boolean isDulicateInvoiceList(Long invoiceListId, String serialNo, Long startNo, Long endNo, Long serialId) throws Exception {
        boolean ok = true;
        if (startNo > endNo) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceList.validateFromTo");
        }
        checkLengthInvocie(endNo, serialId);
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(InvoiceList.COLUMNS.INVOICESERIALID.name(), FilterRequest.Operator.EQ, serialId));
            lst.add(new FilterRequest(InvoiceList.COLUMNS.SERIALNO.name(), FilterRequest.Operator.EQ, serialNo));
            lst.add(new FilterRequest(InvoiceList.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            if (!DataUtil.isNullOrZero(invoiceListId)) {
                lst.add(new FilterRequest(InvoiceList.COLUMNS.INVOICELISTID.name(), FilterRequest.Operator.NE, invoiceListId));
            }
            Multimap<String, Long> map = ArrayListMultimap.create();
            List<InvoiceListDTO> lstResult = findByFilter(lst);
            if (!DataUtil.isNullObject(lstResult)) {
                for (InvoiceListDTO dto : lstResult) {
                    for (long i = dto.getFromInvoice(); i <= dto.getToInvoice(); i++) {
                        map.put(dto.getSerialNo(), i);
                    }
                }
            }
            for (Long i = startNo; i <= endNo; i++) {
                if (map.containsEntry(serialNo, i)) {
                    ok = false;
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
        return ok;
    }

    /**
     * author HoangNT
     *
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage delete(List<Long> lstId, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        if (DataUtil.isNullObject(lstId)) {
            return null;
        }
        Date curDate = getSysDate(em);
        for (Long invoiceId : lstId) {
            try {
                InvoiceListDTO invoiceListDTO = findOne(invoiceId);
                if (DataUtil.isNullObject(invoiceListDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "invoice.list.update.error", invoiceId);
                }
                if (invoiceListDTO.getCurrInvoice() >= invoiceListDTO.getFromInvoice()) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceList.not.delete", invoiceListDTO.getInvoiceListId());
                }
                invoiceListDTO.setStatus(Const.STATUS.NO_ACTIVE);
                invoiceListDTO.setUpdateUser(staffCode);
                invoiceListDTO.setUpdateDatetime(curDate);
                repository.save(mapper.toPersistenceBean(invoiceListDTO));
            } catch (LogicException e) {
                throw e;
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceList.delete");
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    @Override
    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException {
        return invoiceSerialService.getAllSerialByInvoiceType(shopId, invoiceTypeId);
    }

    @Override

    public List<InvoiceTypeDTO> getAllInvoiceTypeByType(Long type) throws Exception, LogicException {
        return invoiceTypeService.getInvoiceType(type);
    }

    @Override
    public List<InvoiceListDTO> getAllInvoiceListByShopId(Long shopId) throws Exception, LogicException {
        return repository.getAllInvoiceListByShopId(shopId);
    }

    @Override
    public List<InvoiceListDTO> getAllInvoiceListBySerialCode(String serialCode) throws Exception, LogicException {
        return repository.getAllInvoiceListBySerialCode(serialCode);
    }

    @Override
    public List<InvoiceListDTO> getAllInvoiceListByInvoiceListId(Long invoiceListId) throws Exception, LogicException {
        return repository.getAllInvoiceListByInvoiceListId(invoiceListId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage updateInvoiceListByInvoiceListId(Long invoiceListId) throws Exception, LogicException {
        return repository.updateInvoiceListByInvoiceListId(invoiceListId);
    }

    @Override
    public List<InvoiceListDTO> searchInvoiceList(InvoiceListDTO invoiceListDTO) throws Exception, LogicException {
        return repository.searchInvoiceList(invoiceListDTO);
    }

    @Override
    @WebMethod
    public List<InvoiceListDTO> getInvoiceListFromSerial(Long invoiceSerialId) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        if (!DataUtil.isNullObject(invoiceSerialId)) {
            lst.add(new FilterRequest(InvoiceList.COLUMNS.INVOICESERIALID.name(), FilterRequest.Operator.EQ, invoiceSerialId));
        }
        lst.add(new FilterRequest(InvoiceList.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS.ACTIVE));
        return findByFilter(lst);
    }
}
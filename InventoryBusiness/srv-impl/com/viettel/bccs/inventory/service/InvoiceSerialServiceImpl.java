package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.OrderSpecifier;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ValidateService;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.InvoiceSerial;
import com.viettel.bccs.inventory.model.QInvoiceSerial;
import com.viettel.bccs.inventory.repo.InvoiceSerialRepo;
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
public class InvoiceSerialServiceImpl extends BaseServiceImpl implements InvoiceSerialService {

    //    private final InvoiceSerialMapper mapper = new InvoiceSerialMapper();
    private final BaseMapper<InvoiceSerial, InvoiceSerialDTO> mapper = new BaseMapper(InvoiceSerial.class, InvoiceSerialDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private InvoiceSerialRepo repository;
    @Autowired
    private InvoiceTypeService invoiceTypeSv;
    @Autowired
    private InvoiceListService invoiceListService;
    @Autowired
    private ShopService shopSv;
    @Autowired
    private StaffService staffService;

    private List<ShopDTO> listBranch = Lists.newArrayList();
    private List<ShopDTO> listSubShop = Lists.newArrayList();
    private StaffDTO staffDTO;
    private InvoiceTypeDTO invoiceTypeDTO;

    public static final Logger logger = Logger.getLogger(InvoiceSerialService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public InvoiceSerialDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<InvoiceSerialDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<InvoiceSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        OrderSpecifier<Date> sortByName = QInvoiceSerial.invoiceSerial.createDatetime.desc();
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters), sortByName));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(InvoiceSerialDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    private void checkValidate(InvoiceSerialDTO dto, String staffCode, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "invoiceSerial.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getInvoiceSerialId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceSerial.validate.require.invoiceSerialId");
            }
            if (DataUtil.isNullOrZero(dto.getInvoiceTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceSerial.validate.require.invoiceTypeId");
            }
            if (DataUtil.isNullOrEmpty(dto.getSerialNo())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceSerial.validate.require.serialNo");
            }
            if (DataUtil.isNullOrZero(dto.getShopUsedId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "invoiceSerial.validate.require.shopId");
            }
            if (DataUtil.isNullOrEmpty(staffCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staffCode.validate.require");
            }
            //maxlength
            if (dto.getSerialNo().length() > 10) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceSerial.validate.maxlength.serialNo");
            }
            if (staffCode.length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate.maxlength");
            }
            // contains in database - selectItems
            listBranch = shopSv.getListAllBranch();
            staffDTO = staffService.getStaffByStaffCode(staffCode);
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
            }
            listSubShop = shopSv.getListShopByParentShopId(staffDTO.getShopId());
            listBranch.addAll(listSubShop);
            if (!ValidateService.checkValueContainShopById(dto.getShopUsedId(), listBranch)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceSerial.validate.not.contains.shopId");
            }
            invoiceTypeDTO = invoiceTypeSv.findOne(dto.getInvoiceTypeId());
            if (DataUtil.isNullObject(invoiceTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "invoiceSerial.validate.not.contains.invoiceTypeId");
            }
            //trim space
            dto.setSerialNo(dto.getSerialNo().trim());
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
     * @throws LogicException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(InvoiceSerialDTO dto, String staffCode) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        checkValidate(dto, staffCode, true);
        // Validate serial_no va shop la duy nhat
        if (isDulicateInvoiceSerial(dto.getSerialNo(), dto.getShopUsedId(), dto.getInvoiceSerialId(), dto.getInvoiceTypeId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceSerial.duplicate", dto.getSerialNo());
        }
        try {
            InvoiceSerialDTO invoiceSerialDTO = findOne(dto.getInvoiceSerialId());
            if (DataUtil.isNullObject(invoiceSerialDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_UPDATE, "invoice.serial.update.error", dto.getInvoiceSerialId());
            }
            // set lai cac gia tri update.
            if (!DataUtil.isNullOrZero(dto.getInvoiceTypeId())) {
                invoiceSerialDTO.setInvoiceTypeId(dto.getInvoiceTypeId());
            }
            if (!DataUtil.isNullOrEmpty(dto.getSerialNo())) {
                invoiceSerialDTO.setSerialNo(dto.getSerialNo());
            }
            if (!DataUtil.isNullOrZero(dto.getShopUsedId())) {
                invoiceSerialDTO.setShopUsedId(dto.getShopUsedId());
            }
            if (!DataUtil.isNullOrEmpty(dto.getDescription())) {
                invoiceSerialDTO.setDescription(dto.getDescription());
            }
            invoiceSerialDTO.setInvoiceTrans(dto.getInvoiceTrans());
            invoiceSerialDTO.setUpdateDatetime(getSysDate(em));
            invoiceSerialDTO.setUpdateUser(staffCode);
            repository.save(mapper.toPersistenceBean(invoiceSerialDTO));
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceSerial.update");
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    /**
     * Author HoangNT
     *
     * @param serialNo
     * @param shopId
     * @param invoiceSerialId
     * @return
     * @throws Exception
     */
    private boolean isDulicateInvoiceSerial(String serialNo, Long shopId, Long invoiceSerialId, Long invoiceTypeId) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.SHOPUSEDID.name(), FilterRequest.Operator.EQ, shopId));
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.INVOICETYPEID.name(), FilterRequest.Operator.EQ, invoiceTypeId));
            if (!DataUtil.isNullOrZero(invoiceSerialId)) {
                lst.add(new FilterRequest(InvoiceSerial.COLUMNS.INVOICESERIALID.name(), FilterRequest.Operator.NE, invoiceSerialId));
            }
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.SERIALNO.name(), FilterRequest.Operator.EQ, serialNo));

            return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    /**
     * author HoangNT
     *
     * @return
     */
    public List<InvoiceSerialDTO> getAllSerialByInvoiceType(Long shopId, Long invoiceTypeId) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        try {
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.INVOICETYPEID.name(), FilterRequest.Operator.EQ, invoiceTypeId));
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.SHOPUSEDID.name(), FilterRequest.Operator.EQ, shopId));
            lst.add(new FilterRequest(InvoiceSerial.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            OrderSpecifier<String> sortByName = QInvoiceSerial.invoiceSerial.serialNo.asc();
            return mapper.toDtoBean(repository.findAll(repository.toPredicate(lst), sortByName));
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    /**
     * author HoangNT
     *
     * @param lst
     * @return
     * @throws Exception
     * @throws LogicException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InvoiceSerialDTO> createNewInvoiceSerial(List<InvoiceSerialDTO> lst, String staffCode) throws Exception, LogicException {
        if (DataUtil.isNullObject(lst)) {
            return null;
        }
        List<InvoiceSerialDTO> listCloneBean = DataUtil.cloneBean(lst);
        List<InvoiceSerialDTO> lstResult = Lists.newArrayList();
        for (InvoiceSerialDTO invoiceSerialDTO : listCloneBean) {
            checkValidate(invoiceSerialDTO, staffCode, false);
            if (isDulicateInvoiceSerial(invoiceSerialDTO.getSerialNo(), invoiceSerialDTO.getShopUsedId(), invoiceSerialDTO.getInvoiceSerialId(), invoiceSerialDTO.getInvoiceTypeId())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "invoce.invoiceSerial.duplicate", invoiceSerialDTO.getSerialNo());
            }
            try {
                Date currentDate = getSysDate(em);
                invoiceSerialDTO.setCreateUser(staffCode);
                invoiceSerialDTO.setCreateDatetime(currentDate);
                invoiceSerialDTO.setUpdateUser(staffCode);
                invoiceSerialDTO.setUpdateDatetime(currentDate);
                invoiceSerialDTO.setStatus(Const.STATUS_ACTIVE);
                InvoiceSerial invoiceSerial = mapper.toPersistenceBean(invoiceSerialDTO);
                invoiceSerial = repository.save(invoiceSerial);
                invoiceSerialDTO.setInvoiceSerialId(invoiceSerial.getInvoiceSerialId());
                lstResult.add(invoiceSerialDTO);
            } catch (Exception ex) {
                logger.error("Error: ", ex);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
            }
        }
        return lstResult;
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
        Date currentDate = getSysDate(em);
        for (Long invocieSerialId : lstId) {
            try {
                InvoiceSerialDTO invoiceSerialDTO = findOne(invocieSerialId);
                if (DataUtil.isNullObject(invoiceSerialDTO)) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "invoice.serial.delete.error", invocieSerialId);
                }
                boolean isExistInvoiceList = isExistInvoiceList(invocieSerialId);
                if (isExistInvoiceList) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DELETE, "invoice.serial.delete.exist.list", invoiceSerialDTO.getSerialNo());
                }
                invoiceSerialDTO.setStatus(Const.STATUS.NO_ACTIVE);
                invoiceSerialDTO.setUpdateDatetime(currentDate);
                invoiceSerialDTO.setUpdateUser(staffCode);
                repository.save(mapper.toPersistenceBean(invoiceSerialDTO));
            } catch (LogicException ex) {
                throw ex;
            } catch (Exception ex) {
                logger.error("Error: ", ex);
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "invoce.invoiceSerial.delete");
            }
        }
        result.setErrorCode(ErrorCode.ERROR_STANDARD.SUCCESS);
        return result;
    }

    /**
     * DungPT16
     * Kiem tra co dai hoa don nao dang ton tai khong
     *
     * @param invoiceSerialId
     * @return
     */
    private boolean isExistInvoiceList(Long invoiceSerialId) throws Exception {
        List<InvoiceListDTO> lstInvoiceList = invoiceListService.getInvoiceListFromSerial(invoiceSerialId);
        if (!DataUtil.isNullOrEmpty(lstInvoiceList)) {
            return true;
        }
        return false;
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> searchInvoiceSerial(InvoiceSerialDTO invoiceSerialDTO) throws Exception, LogicException {
        return repository.searchInvoiceSerial(invoiceSerialDTO);
    }

    @Override
    @WebMethod
    public List<InvoiceSerialDTO> getInvoiceSerialFromInvoiceType(Long invoiceTypeId) throws Exception, LogicException {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(InvoiceSerial.COLUMNS.INVOICETYPEID.name(), FilterRequest.Operator.EQ, invoiceTypeId));
        lst.add(new FilterRequest(InvoiceSerial.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        return findByFilter(lst);
    }
}

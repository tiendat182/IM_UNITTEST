package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitDayTypeDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.model.DebitDayType;
import com.viettel.bccs.inventory.repo.DebitDayTypeRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DebitDayTypeServiceImpl extends BaseServiceImpl implements DebitDayTypeService {

    private final BaseMapper<DebitDayType, DebitDayTypeDTO> mapper = new BaseMapper(DebitDayType.class, DebitDayTypeDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;
    @Autowired
    private DebitDayTypeRepo repository;
    @Autowired
    OptionSetValueService optionSetValueService;
    public static final Logger logger = Logger.getLogger(DebitDayTypeService.class);
    private String[] fileType = {"doc", "docx", "pdf", "xls", "xlsx", "png", "jpg", "jpeg", "bmp", "gif", "txt"};

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DebitDayTypeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DebitDayTypeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DebitDayTypeDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(DebitDayTypeDTO dto, StaffDTO staff) throws Exception {
        BaseMessage message = new BaseMessage();
        if (DataUtil.isNullObject(dto)) {
            message.setKeyMsg("validate.server.null.object");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
        }
        //check du lieu nhap vao
        if (DataUtil.isNullOrEmpty(dto.getDebitDayType())) {
            message.setKeyMsg("require.messenge.type.limit");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            return message;
        }
        if (DataUtil.isNullObject(dto.getStartDate())) {
            message.setKeyMsg("require.messenge.start.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            return message;
        }
        if (DataUtil.isNullObject(dto.getEndDate())) {
            message.setKeyMsg("require.messenge.end.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            return message;
        }
//        if (DataUtil.isNullOrEmpty(dto.getFileName())) {
//            message.setKeyMsg("range.time.promotion.dispatch.require");
//            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
//            return message;
//        }
        //check tinh hop le cac truong
        message = validateValueField(dto);
        if (!DataUtil.isNullOrEmpty(message.getKeyMsg())) {
            return message;
        }
        //check ngay bat dau ngay ket thuc
        if (dto.getStartDate().compareTo(dto.getEndDate()) > 0) {
            message.setKeyMsg("validate.start.end.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        }
        //check ngay bat dau phai sau ngay hien tai
        if (parseToSimpleDate(optionSetValueService.getSysdateFromDB(true)).compareTo(parseToSimpleDate(dto.getStartDate())) > 0) {
            message.setKeyMsg("validate.enddate.current.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        }
        //validate ngay sua da ton tai chua
        if (validateExistRange(dto)) {
            message.setKeyMsg("validate.input.for.add.new");
            message.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            return message;
        }
        String createUser = staff.getStaffCode();
        dto.setCreateUser(createUser);
        dto.setLastUpdateUser(createUser);
        Date dateDb = DbUtil.getSysDate(emIM);
        dto.setCreateDate(dateDb);
        dto.setLastUpdateTime(dateDb);
        dto.setStatus(Const.STATUS.ACTIVE);
        mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        message.setSuccess(true);
        return message;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(DebitDayTypeDTO dto, StaffDTO staffDTO) throws Exception {
        BaseMessage message = new BaseMessage();
        if (DataUtil.isNullObject(dto)) {
            message.setKeyMsg("validate.server.null.object");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        }
        if (DataUtil.isNullOrEmpty(dto.getDebitDayType())) {
            message.setKeyMsg("require.messenge.type.limit");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        }
        if (DataUtil.isNullObject(dto.getStartDate())) {
            message.setKeyMsg("require.messenge.start.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        }
        if (DataUtil.isNullObject(dto.getEndDate())) {
            message.setKeyMsg("require.messenge.end.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        }
//        if (DataUtil.isNullOrEmpty(dto.getFileName())) {
//            message.setKeyMsg("range.time.promotion.dispatch.require");
//            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
//            return message;
//        }
        DebitDayTypeDTO typeDTO = findOne(dto.getId());
        if (DataUtil.isNullObject(typeDTO)) {
            message.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_UPDATE);
            message.setKeyMsg("validate.object.null.server");
            return message;
        }
        //check k cho sua debitdaytype
        if (!DataUtil.safeEqual(typeDTO.getDebitDayType(), dto.getDebitDayType())) {
            message.setKeyMsg("validate.debitdaytype.not.accept.edit");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION);
            return message;
        }
        //check tinh hop le cac truong
        message = validateValueField(dto);
        if (!DataUtil.isNullOrEmpty(message.getKeyMsg())) {
            return message;
        }
        //check ngay sua doi co hop le k
        BaseMessage mess = validateCommon(dto);
        if (!DataUtil.isNullOrEmpty(mess.getKeyMsg())) {
            return mess;
        }
        //validate ngay sua da ton tai chua
        if (validateExistRange(dto)) {
            message.setKeyMsg("validate.input.for.add.new");
            message.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            return message;
        }
        typeDTO.setLastUpdateUser(staffDTO.getStaffCode());
        typeDTO.setLastUpdateTime(DbUtil.getSysDate(emIM));
        typeDTO.setStatus(dto.getStatus());

        typeDTO.setStartDate(dto.getStartDate());
        typeDTO.setEndDate(dto.getEndDate());

        if (!DataUtil.isNullOrEmpty(dto.getFileName()) && dto.getFileContent() != null) {
            typeDTO.setFileName(dto.getFileName());
            typeDTO.setFileContent(dto.getFileContent());
        }

        mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(typeDTO)));
        message.setSuccess(true);
        return message;
    }

    @Override
    public BaseMessage delete(List<DebitDayTypeDTO> lstDebitDayTypeDTOs, StaffDTO staff) throws Exception {
        BaseMessage message = new BaseMessage();
        if (DataUtil.isNullOrEmpty(lstDebitDayTypeDTOs)) {
            message.setKeyMsg("validate.server.null.lst");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return message;
        }
        for (DebitDayTypeDTO dto : lstDebitDayTypeDTOs) {
            if (DataUtil.isNullObject(dto)) {
                message.setKeyMsg("validate.server.null.object.get");
                message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
                return message;
            }
        }
        for (DebitDayTypeDTO dto : lstDebitDayTypeDTOs) {
            DebitDayTypeDTO typeDTO = findOne(dto.getId());
            if (DataUtil.isNullObject(typeDTO)) {
                message.setKeyMsg("validate.object.null.server");
                message.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_DELETE);
                return message;
            }
            dto.setLastUpdateTime(DbUtil.getSysDate(emIM));
            dto.setLastUpdateUser(staff.getStaffCode());
            dto.setStatus(Const.STATUS.NO_ACTIVE);
            mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(dto)));
        }
        message.setSuccess(true);
        return message;
    }

    public BaseMessage validateCommon(DebitDayTypeDTO dto) throws Exception {
        BaseMessage message = new BaseMessage();
        if (dto.getStartDate().compareTo((parseToSimpleDate(DbUtil.getSysDate(emIM)))) > 0
                && (DataUtil.isNullObject(dto.getEndDate()))) {
            message.setKeyMsg("mn.stock.limit.currentdate.startdate");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        } else if (dto.getStartDate().compareTo((parseToSimpleDate(DbUtil.getSysDate(emIM)))) < 0) {
            message.setKeyMsg("validate.enddate.current.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        } else if (dto.getStartDate().compareTo(dto.getEndDate()) > 0) {
            message.setKeyMsg("validate.start.end.date");
            message.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return message;
        }
        return message;
    }

    public BaseMessage validateValueField(DebitDayTypeDTO dto) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        //check debitDayType
        List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
        if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
            Boolean valid = false;
            for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                if (DataUtil.safeEqual(dto.getDebitDayType(), optionSetValueDTO.getValue())) {
                    valid = true;
                }
            }
            if (!valid) {
                baseMessage.setKeyMsg("validate.value.field.debit.day.type");
                baseMessage.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return baseMessage;
            }
        }
        //check type file
        baseMessage = validatePreImport(dto);
        if (!DataUtil.isNullOrEmpty(baseMessage.getKeyMsg())) {
            return baseMessage;
        }
        //check ngay nhap vao
        return baseMessage;
    }

    public BaseMessage validatePreImport(DebitDayTypeDTO dayTypeDTO) throws IOException, Exception {
        BaseMessage baseMessage = new BaseMessage();
//        if (DataUtil.isNullOrEmpty(dayTypeDTO.getFileName())) {
//            baseMessage.setKeyMsg("range.time.promotion.dispatch.require");
//            baseMessage.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
//            return baseMessage;
//        }

        if (!DataUtil.isNullOrEmpty(dayTypeDTO.getFileName())) {
            String fileExtension = StringUtils.substringAfterLast(dayTypeDTO.getFileName(), ".");
            if (!DataUtil.isNullOrEmpty(fileExtension)) {
                if (!isCorrectExtension(fileExtension)) {
                    baseMessage.setKeyMsg("validate.format.file.dont.accept");
                    baseMessage.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                    return baseMessage;
                }

            } else {
                baseMessage.setKeyMsg("common.error.happened");
                baseMessage.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return baseMessage;
            }
        }
        return baseMessage;
    }

    /**
     * kiem tra dinh dang file
     *
     * @param extension
     * @author
     */
    public boolean isCorrectExtension(String extension) {
        boolean correct = false;
        for (int i = 0; i < fileType.length; i++) {
            if (DataUtil.safeEqual(fileType[i], extension)) {
                correct = true;
                break;
            }
        }
        return correct;
    }

    public boolean validateExistRange(DebitDayTypeDTO dtoUpdate) throws Exception {
        //validate khoang cau hinh them co bi trung khoang cu k
        boolean exist = false;
        List<DebitDayTypeDTO> lst;
        lst = findAll();
        for (DebitDayTypeDTO dto : lst) {
            if (dtoUpdate.getId() != null && dto.getId().compareTo(dtoUpdate.getId()) == 0) {
                break;
            }
            if (DataUtil.safeEqual(dto.getStatus(), Const.STATUS.ACTIVE) && parseToSimpleDate(dto.getStartDate()).compareTo(parseToSimpleDate(dtoUpdate.getEndDate())) <= 0 &&
                    parseToSimpleDate(dto.getEndDate()).compareTo(parseToSimpleDate(dtoUpdate.getStartDate())) >= 0) {
                return true;
            }
        }
        return exist;
    }

    public Date parseToSimpleDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date dateFormat = new Date();
        try {
            dateFormat = format.parse(format.format(date));
        } catch (ParseException e) {
            e.getMessage();
        }
        return dateFormat;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public List<DebitDayTypeDTO> searchDebitDayType(DebitDayTypeDTO dto) throws Exception {
        if (!DataUtil.isNullObject(dto)) {
            return repository.searchDebitDayType(dto);
        }
        return new ArrayList<DebitDayTypeDTO>();
    }

    @Override
    public byte[] getAttachFileContent(Long requestId) throws Exception {
        return repository.getAttachFileContent(requestId);
    }

}

package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitLevelDTO;
import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.dto.DebitRequestReportDTO;
import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.bccs.sale.service.PaymentGroupService;
import com.viettel.bccs.sale.service.PaymentGroupServiceService;
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
import java.util.List;

@Service
public class DebitRequestDetailServiceImpl extends BaseServiceImpl implements DebitRequestDetailService {

    //    private final DebitRequestDetailMapper mapper = new DebitRequestDetailMapper();
    private final BaseMapper<DebitRequestDetail, DebitRequestDetailDTO> mapper = new BaseMapper(DebitRequestDetail.class, DebitRequestDetailDTO.class);

    @Autowired
    private DebitRequestDetailRepo repository;

    @Autowired
    OptionSetValueService optionSetValueService;

    @Autowired
    DebitRequestRepo debitRequestRepo;

    @Autowired
    private StockDebitRepo stockDebitRepo;

    @Autowired
    private ShopRepo shopRepo;

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    private OptionSetValueRepo optionSetValueRepo;

    @Autowired
    PaymentGroupService paymentGroupService;

    @Autowired
    PaymentGroupServiceService paymentGroupServiceService;

    @Autowired
    DebitLevelService debitLevelService;

    public static final Logger logger = Logger.getLogger(DebitRequestDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DebitRequestDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DebitRequestDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DebitRequestDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(DebitRequestDetailDTO dto) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        repository.save(mapper.toPersistenceBean(dto));
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(DebitRequestDetailDTO dto) throws Exception {
        BaseMessage result = new BaseMessage();
        try {
            repository.save(mapper.toPersistenceBean(dto));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_UPDATE);
        }
        return result;
    }

    public BaseMessage validateInternal(DebitRequestDetailDTO checked, List<DebitRequestDetailDTO> lstChecked, BaseMessage msg) {
        int dupl = 0;
        for (DebitRequestDetailDTO detailDTO : lstChecked) {
            if (!DataUtil.isNullOrEmpty(detailDTO.getDebitDayType())
                    && !DataUtil.isNullObject(detailDTO.getOwnerId())
                    && !DataUtil.isNullOrEmpty(detailDTO.getOwnerType())
                    && !DataUtil.isNullOrEmpty(checked.getDebitDayType())
                    && !DataUtil.isNullObject(checked.getOwnerId())
                    && !DataUtil.isNullOrEmpty(checked.getOwnerType())) {
                if (detailDTO.getDebitDayType().equals(checked.getDebitDayType())
                        && detailDTO.getOwnerType().equals(checked.getOwnerType())
                        && detailDTO.getOwnerId().equals(checked.getOwnerId())) {
                    dupl++;
                }
            }
            if (dupl > 1) {
                msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT);
                msg.setKeyMsg("limit.stock.result.duplicate");
                return msg;
            } // ngu code :(
        }

        return msg;
    }

    @Override
    public BaseMessage validate(DebitRequestDetailDTO debitRequestDetailDTO) throws LogicException, Exception {
        BaseMessage result = new BaseMessage();
        if (debitRequestDetailDTO.getDebitValue() == null && DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupId())) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            result.setKeyMsg("mn.stock.limit.stock.require");
            return result;
        }
        //Neu la han muc cong no thi bat buoc nhap nhom nop tien
        if (!DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupId()) && DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupServiceId())) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            result.setKeyMsg("mn.stock.limit.group.offerMoney.require.msg");
            return result;
        }
        //Neu la han muc kho cho don vi thi ngay ap dung bat buoc nhap
        if (DataUtil.safeEqual(debitRequestDetailDTO.getOwnerType(), Const.OWNER_TYPE.SHOP)) {
            if (!DataUtil.isNullOrZero(debitRequestDetailDTO.getDebitValue()) && DataUtil.isNullOrEmpty(debitRequestDetailDTO.getDebitDayType())) {
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                result.setKeyMsg("mn.stock.limit.require.day.use.msg");
                return result;
            }
        }

        if (DataUtil.isNullOrEmpty(debitRequestDetailDTO.getOwnerType())) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            result.setKeyMsg("limit.stock.valid.ownerType");
            return result;
        } else {
            if (!Const.OWNER_TYPE.SHOP.equals(debitRequestDetailDTO.getOwnerType())
                    && !Const.OWNER_TYPE.STAFF.equals(debitRequestDetailDTO.getOwnerType())) {
                result.setKeyMsg("limit.stock.valid.ownerTypeOut");
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return result;
            }
        }

        if (DataUtil.isNullObject(debitRequestDetailDTO.getOwnerId())) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            result.setKeyMsg("limit.stock.owner.required");
            return result;
        } else {
            if (Const.OWNER_TYPE.SHOP.equals(debitRequestDetailDTO.getOwnerType())) {
                Shop objects = shopRepo.findOne(debitRequestDetailDTO.getOwnerId());
                if (DataUtil.isNullObject(objects)) {
                    result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                    result.setKeyMsg("limit.stock.owner.invalid");
                    return result;
                }
            } else {
                Staff objects = staffRepo.findOne(debitRequestDetailDTO.getOwnerId());
                if (DataUtil.isNullObject(objects)) {
                    result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                    result.setKeyMsg("limit.stock.owner.invalid");
                    return result;
                }
            }
        }

        if (debitRequestDetailDTO.getDebitValue() != null) {
            if (Const.OWNER_TYPE.STAFF.equals(debitRequestDetailDTO.getOwnerType())) {
                List<DebitLevelDTO> lstLevel = debitLevelService.getLstDebitByLevel(debitRequestDetailDTO.getDebitValue());
                if (DataUtil.isNullOrEmpty(lstLevel)) {
                    result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                    result.setKeyMsg("mn.stock.limit.require.valid");
                    return result;
                }
            } else {
                Long debitValue = DataUtil.safeToLong(debitRequestDetailDTO.getDebitValue());
                if (debitValue <= 0L) {
                    result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                    result.setKeyMsg("limit.stock.debitLevel.invalid");
                    return result;
                }
                if (DataUtil.safeToString(debitRequestDetailDTO.getDebitValue()).length() > 20) {
                    result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                    result.setKeyMsg("limit.stock.debitLevel.maxlength");
                    return result;
                }
                if (DataUtil.isNullOrEmpty(debitRequestDetailDTO.getDebitDayType())) {
                    result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                    result.setKeyMsg("limit.stock.debitDayType.required");
                    return result;
                } else {
                    List objects = optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.DEBIT_DAY_TYPE, debitRequestDetailDTO.getDebitDayType());
                    if (DataUtil.isNullOrEmpty(objects)) {
                        result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                        result.setKeyMsg("limit.stock.debitDayType.required");
                        return result;
                    }
                }
            }

        }
        if (!DataUtil.isNullOrEmpty(debitRequestDetailDTO.getNote())) {
            if (debitRequestDetailDTO.getNote().length() > 200) {
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                result.setKeyMsg("limit.stock.note.max");
                return result;
            }
        }
        //Neu la han muc nhan vien thi bat buoc nhap khoang cach
        if (DataUtil.safeEqual(debitRequestDetailDTO.getOwnerType(), Const.OWNER_TYPE.STAFF)) {
            if (debitRequestDetailDTO.getDistance() == null || debitRequestDetailDTO.getDistance() <= 0) {
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                result.setKeyMsg("limit.stock.distance.invalid");
                return result;
            }
        }
        if (debitRequestDetailDTO.getDistance() != null && debitRequestDetailDTO.getDistance() <= 0) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            result.setKeyMsg("limit.stock.distance.shop.invalid");
            return result;
        }

        Staff createUser;
        if (DataUtil.isNullOrZero(debitRequestDetailDTO.getStaffId())) {
            result.setKeyMsg("limit.stock.valid.user");
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return result;
        } else {
            createUser = staffRepo.findOne(debitRequestDetailDTO.getStaffId());
            if (DataUtil.isNullObject(createUser)) {
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                result.setKeyMsg("limit.stock.owner.required");
                return result;
            }
        }
        if (Const.OWNER_TYPE.SHOP.equals(debitRequestDetailDTO.getOwnerType())) {
            if (!shopRepo.validateConstraints(debitRequestDetailDTO.getOwnerId(), createUser.getShopId())) {
                result.setKeyMsg("limit.stock.invalid.ownerChildren");
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION);
                return result;
            }
        } else {
            if (!staffRepo.validateConstraints(debitRequestDetailDTO.getOwnerId(), createUser.getShopId())) {
                result.setKeyMsg("limit.stock.invalid.ownerChildren");
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION);
                return result;
            }
        }
        // validate han muc cong no
        if (!DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupId())) {
            PaymentGroupDTO paymentGroupDTO = paymentGroupService.findOne(DataUtil.safeToLong(debitRequestDetailDTO.getPaymentGroupId()));
            if (DataUtil.isNullObject(paymentGroupDTO) || DataUtil.safeEqual(paymentGroupDTO.getStatus(), DataUtil.safeToLong(Const.STATUS_INACTIVE))) {
                result.setKeyMsg("payment.debit.payment.group.invalid");
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return result;
            }
        }
        if (!DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupId())) {
            if (DataUtil.isNullOrZero(debitRequestDetailDTO.getPaymentGroupServiceId())) {
                result.setKeyMsg("payment.debit.payment.group.service.null");
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return result;
            }
            PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.findOne(DataUtil.safeToLong(debitRequestDetailDTO.getPaymentGroupServiceId()));
            if (DataUtil.isNullObject(paymentGroupServiceDTO) || DataUtil.safeEqual(paymentGroupServiceDTO.getStatus(), DataUtil.safeToLong(Const.STATUS_INACTIVE))) {
                result.setKeyMsg("payment.debit.payment.group.service.invalid");
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return result;
            }
        }

        return isDuplicate(debitRequestDetailDTO, result);
    }

    private BaseMessage isDuplicate(DebitRequestDetailDTO debitRequestDetailDTO, BaseMessage msg) throws LogicException, Exception {
        Long requestId = repository.checkExsitRequestDetail(debitRequestDetailDTO);
        if (!DataUtil.isNullOrZero(requestId)) {
            DebitRequest request = debitRequestRepo.findOne(requestId);
            msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT);
            msg.setKeyMsg("limit.stock.result.duplicate", request.getRequestCode());
        }
        return msg;
    }

    @Override
    public List<DebitRequestDetailDTO> findByDebitRequestDetail(DebitRequestDetailDTO searcher) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(searcher.getStatus())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, searcher.getStatus()));
        }
        if (!DataUtil.isNullOrEmpty(searcher.getDebitDayType())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.DEBITDAYTYPE.name(), FilterRequest.Operator.EQ, searcher.getDebitDayType()));
        }
        if (!DataUtil.isNullOrEmpty(searcher.getFinanceType())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.FINANCETYPE.name(), FilterRequest.Operator.EQ, searcher.getFinanceType()));
        }
        if (!DataUtil.isNullOrEmpty(searcher.getOwnerType())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.OWNERTYPE.name(), FilterRequest.Operator.EQ, searcher.getOwnerType()));
        }
        if (!DataUtil.isNullOrEmpty(searcher.getRequestType())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.REQUESTTYPE.name(), FilterRequest.Operator.EQ, searcher.getRequestType()));
        }
        if (!DataUtil.isNullObject(searcher.getDebitValue())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.DEBITVALUE.name(), FilterRequest.Operator.EQ, searcher.getDebitValue()));
        }
        if (!DataUtil.isNullObject(searcher.getRequestDate())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.REQUESTDATE.name(), FilterRequest.Operator.EQ, searcher.getRequestDate()));
        }
        if (!DataUtil.isNullObject(searcher.getRequestDate())) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.REQUESTDATE.name(), FilterRequest.Operator.EQ, searcher.getRequestDate()));
        }

        List<DebitRequestDetailDTO> result = findByFilter(filters);
        return result;
    }

    public List<DebitRequestDetailDTO> getLstDetailByRequestId(Long requestId) throws Exception {
        List<FilterRequest> filters = Lists.newArrayList();
        if (!DataUtil.isNullOrZero(requestId)) {
            filters.add(new FilterRequest(DebitRequestDetail.COLUMNS.REQUESTID.name(), FilterRequest.Operator.EQ, requestId));
        }
        return findByFilter(filters);
    }

    public List<DebitRequestReportDTO> getLstDetailForReport(Long requestId) throws Exception {
        return repository.getLstDetailForReport(requestId);
    }
}

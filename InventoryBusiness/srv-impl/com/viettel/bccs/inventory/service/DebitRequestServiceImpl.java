package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.DebitRequestResponse;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.model.StockDebit;
import com.viettel.bccs.inventory.repo.*;
import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

@Service
public class DebitRequestServiceImpl extends BaseServiceImpl implements DebitRequestService {

    public static final String STOCK_TRANS_VOFFICE_SEQ = "STOCK_TRANS_VOFFICE_SEQ";
    public static final String TEN_ZEZO = "0000000000";

    private final BaseMapper<DebitRequestDetail, DebitRequestDetailDTO> detailMapper = new BaseMapper(DebitRequestDetail.class, DebitRequestDetailDTO.class);

    private final BaseMapper<DebitRequest, DebitRequestDTO> mapper = new BaseMapper(DebitRequest.class, DebitRequestDTO.class);

    @Autowired
    private DebitRequestRepo repository;

    @Autowired
    private DebitRequestDetailRepo debitRequestDetailRepo;

    @Autowired
    DebitRequestDetailService debitRequestDetailService;

    @Autowired
    private StockDebitRepo stockDebitRepo;

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    ShopRepo shopRepo;

    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(DebitRequestService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DebitRequestDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DebitRequestDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DebitRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage save(DebitRequestDTO debitRequestDTO) throws LogicException, Exception {
        BaseMessage baseMessage = new BaseMessage();
        repository.save(mapper.toPersistenceBean(debitRequestDTO));
        baseMessage.setSuccess(true);
        return baseMessage;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(DebitRequestDTO dto) throws LogicException, Exception {
        BaseMessage message;
        message = validate(dto);
        if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
            return message;
        }
        DebitRequest request = mapper.toPersistenceBean(dto);
        List<DebitRequestDetailDTO> requestDetails = dto.getDebitDebitRequestDetails();
        Date sys = getSysDate(em);
//        Long debitRequestId = repository.getSequence();
        request.setCreateDate(sys);
//        request.setRequestId(debitRequestId);
        request.setLastUpdateUser(dto.getCreateUser());
        request.setLastUpdateTime(sys);
        request.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED);
//        request.setRequestCode(Const.REQUEST_CODE + debitRequestId);
        DebitRequest debitRequestSave = repository.save(request);
        debitRequestSave.setRequestCode(Const.REQUEST_CODE + debitRequestSave.getRequestId());
        debitRequestSave = repository.save(request);
        for (DebitRequestDetailDTO detailDTO : requestDetails) {
            detailDTO.setRequestId(debitRequestSave.getRequestId());
            detailDTO.setRequestDate(sys);
            detailDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED);
            debitRequestDetailService.create(detailDTO);
        }
        //sign voffice
        List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(dto.getSignFlowId());
        if (DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyDetail");
        }
        StringBuilder lstUser = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            for (SignFlowDetailDTO signFlowDetailDTO : lstSignFlowDetail) {
                if (DataUtil.isNullOrEmpty(signFlowDetailDTO.getEmail())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyEmail");
                }
                if (!DataUtil.isNullOrEmpty(lstUser.toString())) {
                    lstUser.append(",");
                }
                lstUser.append(signFlowDetailDTO.getEmail().trim().substring(0, signFlowDetailDTO.getEmail().lastIndexOf("@")));
            }

            StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
            String stVofficeId = DateUtil.dateToStringWithPattern(sys, "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            stockTransVoffice.setStockTransVofficeId(stVofficeId);
            stockTransVoffice.setAccountName(dto.getUserName().trim());
            stockTransVoffice.setAccountPass(dto.getPassWord());
            stockTransVoffice.setSignUserList(lstUser.toString());
            stockTransVoffice.setCreateDate(sys);
            stockTransVoffice.setLastModify(sys);
            stockTransVoffice.setStatus(Const.STATUS.NO_ACTIVE);
            stockTransVoffice.setStockTransActionId(debitRequestSave.getRequestId());
            //check template
            stockTransVoffice.setPrefixTemplate(Const.DEBIT_REQUEST_PREFIX_TEMPLATE);
            stockTransVoffice.setReceiptNo(debitRequestSave.getRequestCode());
            stockTransVoffice.setActionCode(debitRequestSave.getRequestCode());

            stockTransVofficeService.save(stockTransVoffice);

        }
        message.setSuccess(true);
        message.setKeyMsg("mn.stock.limit.insert.success", request.getRequestCode());
        return message;
    }

    private BaseMessage validate(DebitRequestDTO dto) throws LogicException, Exception {
        BaseMessage msg = new BaseMessage(false);

        if (!DataUtil.isNullOrEmpty(dto.getDescription())) {
            if (dto.getDescription().length() > 100) {
                msg.setKeyMsg("limit.stock.description.max");
                msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return msg;
            }
        }
        if (DataUtil.isNullOrEmpty(dto.getRequestObjectType())) {
            msg.setKeyMsg("limit.stock.valid.object");
            msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return msg;
        } else {
            if (!Const.OWNER_TYPE.SHOP.equals(dto.getRequestObjectType())
                    && !Const.OWNER_TYPE.STAFF.equals(dto.getRequestObjectType())) {
                msg.setKeyMsg("limit.stock.valid.object");
                msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
                return msg;
            }
        }

        if (DataUtil.isNullOrEmpty(dto.getCreateUser())) {
            msg.setKeyMsg("limit.stock.valid.user");
            msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            return msg;
        } else {
            List<Staff> objects = staffRepo.findByStaffCodeAndStatusActive(dto.getCreateUser());
            if (DataUtil.isNullOrEmpty(objects)) {
                msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                msg.setKeyMsg("limit.stock.owner.required");
                return msg;
            }
        }
//        if (DataUtil.isNullOrEmpty(dto.getFileName())
//                || DataUtil.isNullObject(dto.getFileContent())
//                || dto.getFileName().length() > 200
//                || dto.getFileContent().length == 0
//                || dto.getFileContent().length > Const.FILE_MAX) {
//            msg.setKeyMsg("limit.stock.valid.file");
//            msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
//            return msg;
//        }

        if (DataUtil.isNullOrEmpty(dto.getDebitDebitRequestDetails())) {
            msg.setKeyMsg("mn.stock.limit.view.list.order.required");
            msg.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY);
            return msg;
        }

        for (DebitRequestDetailDTO requestDetail : dto.getDebitDebitRequestDetails()) {
            msg = debitRequestDetailService.validate(requestDetail);
            //debitRequestDetailService.validateInternal(requestDetail, dto.getDebitDebitRequestDetails(), msg);
            if (!DataUtil.isNullOrEmpty(msg.getErrorCode())) {
                break;
            }
        }
        return msg;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(DebitRequestDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public byte[] getAttachFileContent(Long requestId) throws Exception {
        return repository.getAttachFileContent(requestId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DebitRequestResponse approveDebitRequest(DebitRequestDTO debitRequest) throws LogicException, Exception {
        DebitRequestResponse result = new DebitRequestResponse();
        DebitRequestDTO debitRequestDTO = findOne(debitRequest.getRequestId());
        if (!Const.DEBIT_REQUEST_STATUS.STATUS_CREATED.equals(debitRequestDTO.getStatus())) {
            result.setErrorCode(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT);
            result.setKeyMsg("limit.stock.approveResult.conflict");
            return result;
        }
        if (DataUtil.isNullOrEmpty(debitRequest.getCurrentStaff())) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            result.setKeyMsg("limit.stock.lastUser.required");
            return result;
        } else {
            List<Staff> objects = staffRepo.findByStaffCodeAndStatusActive(debitRequest.getCurrentStaff());
            if (DataUtil.isNullOrEmpty(objects)) {
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                result.setKeyMsg("limit.stock.lastUser.required");
                return result;
            }
        }

        List<DebitRequestDetailDTO> details = debitRequest.getDebitDebitRequestDetails();
        String status = Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED;
        for (DebitRequestDetailDTO detail : details) {

            if (Const.DEBIT_REQUEST_STATUS.STATUS_REJECT.equals(detail.getStatus())) {
                status = Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED_UNIT;
            } else {
                if (!Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED.equals(detail.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.valid.status");
                }
            }
            DebitRequestDetailDTO detailDTO = debitRequestDetailService.findOne(detail.getRequestDetailId());
            if (DataUtil.isNullObject(detailDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.approveResult.conflict");
            }
            DebitRequestDetail bean = detailMapper.toPersistenceBean(detailDTO);
            bean.setStatus(detail.getStatus());
            debitRequestDetailRepo.save(bean);
            if (Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED.equals(detail.getStatus())) {
                StockDebit stockDebit = stockDebitRepo.buildStockDebitFromDebitRequestDetail(bean, debitRequest.getCurrentStaff());
                stockDebitRepo.save(stockDebit);
                //Neu la han muc nhan vien, cap nhat lai ngay nop tien cho tat ca loai ngay ap dung
                if (DataUtil.safeEqual(Const.OWNER_TYPE.STAFF, stockDebit.getOwnerType())) {
                    stockDebitRepo.updateFinanceType(stockDebit.getOwnerId(), stockDebit.getFinanceType());
                }
            }
        }
        debitRequestDTO.setStatus(status);
        debitRequestDTO.setLastUpdateTime(getSysDate(em));
        debitRequestDTO.setLastUpdateUser(debitRequest.getCurrentStaff());

        result.setDebitRequestDTO(debitRequestDTO);
        DebitRequest bean = mapper.toPersistenceBean(debitRequestDTO);
        if (bean.getFileContent() == null || bean.getFileContent().length == 0)
            bean.setFileContent(getAttachFileContent(bean.getRequestId()));
        repository.save(bean);
        return result;
    }

    @Override
    public List<DebitRequestDetailDTO> findDebitRequestDetailByDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        return debitRequestDetailRepo.findDebitRequestDetailByDebitRequset(mapper.toPersistenceBean(debitRequestDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage deleteDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        BaseMessage result = new BaseMessage();
        if (DataUtil.isNullOrEmpty(debitRequestDTO.getCurrentStaff())) {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
            result.setKeyMsg("limit.stock.lastUser.required");
            return result;
        } else {
            List<Staff> objects = staffRepo.findByStaffCodeAndStatusActive(debitRequestDTO.getCurrentStaff());
            if (DataUtil.isNullOrEmpty(objects)) {
                result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE);
                result.setKeyMsg("limit.stock.lastUser.required");
                return result;
            }
        }
        int delDetail = debitRequestDetailRepo.deleteDebitRequestDetailByDebitRequest(debitRequestDTO);
        if (delDetail == Const.RESULT_CODE.SUCCESS) {
            repository.deleteDebitRequest(debitRequestDTO);
        } else {
            result.setErrorCode(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT);
            result.setKeyMsg("limit.stock.delete.notFound");
        }
        return result;
    }

    @Override
    public List<DebitRequestDTO> findDebitRequest(DebitRequestDTO debitRequestDTO) throws Exception {
        if (debitRequestDTO.getFromDate() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.from.date.not.blank");
        }
        if (debitRequestDTO.getToDate() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.to.date.not.blank");
        }
        if (debitRequestDTO.getToDate().getTime() - debitRequestDTO.getFromDate().getTime() > Const.MONTH_IN_MILLISECOND) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.from.to.valid");
        }
        return mapper.toDtoBean(repository.findDebitRequest(debitRequestDTO));
    }

    public Long getRevenueInMonth(Long ownerId, String ownerType) throws Exception {
        return repository.getRevenueInMonth(ownerId, ownerType);
    }
}

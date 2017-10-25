package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.*;
import com.viettel.bccs.inventory.repo.ImportPartnerDetailRepo;
import com.viettel.bccs.inventory.repo.ImportPartnerRequestRepo;
import com.viettel.bccs.inventory.repo.MtRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.jws.WebMethod;
import java.util.List;


@Service
public class ImportPartnerRequestServiceImpl extends BaseServiceImpl implements ImportPartnerRequestService {

    private final BaseMapper<ImportPartnerRequest, ImportPartnerRequestDTO> mapper = new BaseMapper<>(ImportPartnerRequest.class, ImportPartnerRequestDTO.class);
    private final BaseMapper<ImportPartnerDetail, ImportPartnerDetailDTO> detailMapper = new BaseMapper<>(ImportPartnerDetail.class, ImportPartnerDetailDTO.class);

    private static enum ModeMessage {
        create(0), approve(1), reject(2);

        ModeMessage(int val) {
            value = val;
        }

        int value;

        public boolean validate(int test) {
            return test == value;
        }
    }

    @Autowired
    private OptionSetValueService optionSetService;
    @Autowired
    private ImportPartnerDetailRepo importPartnerDetailRepo;
    @Autowired
    private ImportPartnerDetailService importPartnerDetailService;
    @Autowired
    private MtRepo mtRepo;

    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    private ImportPartnerRequestRepo repository;
    public static final Logger logger = Logger.getLogger(ImportPartnerRequestService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ImportPartnerRequestDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ImportPartnerRequestDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ImportPartnerRequestDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage saveImportPartnerRequest(ImportPartnerRequestDTO dto) throws LogicException, Exception {
        BaseMessage msg = new BaseMessage();
        if (!DataUtil.isNullOrZero(dto.getImportPartnerRequestId())) {
            //phe duyet/ tu choi
            ImportPartnerRequest request = repository.findOne(dto.getImportPartnerRequestId());
            if (dto.getStatus() == null || !Const.ConfigIDCheck.importPartnerUpdateStatus.validate(dto.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.status");
            }
            if (DataUtil.isNullObject(request)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.none", dto.getRequestCode());
            } else if (DataUtil.isNullObject(request.getStatus()) || request.getStatus().compareTo(0L) != 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.status");
            } else if (DataUtil.isNullOrZero(dto.getApproveStaffId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.status");
            } else {
                repository.approve(dto);
                int mode = ModeMessage.reject.value;
                if (Const.ConfigIDCheck.importPartnerApproveStatus.validate(dto.getStatus())) {
                    mode = ModeMessage.approve.value;
                }
                createMessage(mode, dto);
            }
            return msg;
        }
        // them moi
        validate(dto);
        Long requestID = repository.getSequence();
        dto.setImportPartnerRequestId(requestID);
        dto.setCreateDate(optionSetService.getSysdateFromDB(false));
        dto.setLastModify(optionSetService.getSysdateFromDB(false));
        repository.saveAndFlush(mapper.toPersistenceBean(dto));
        for (ImportPartnerDetailDTO detailDTO : dto.getListImportPartnerDetailDTOs()) {
            ImportPartnerDetail detail = detailMapper.toPersistenceBean(detailDTO);
            detail.setImportPartnerRequestId(requestID);
            detail.setCreateDate(optionSetService.getSysdateFromDB(false));
            importPartnerDetailRepo.save(detail);
        }
        createMessage(ModeMessage.create.value, dto);
        return msg;
    }

    private void validate(ImportPartnerRequestDTO dto) throws Exception {
        if (DataUtil.isNullOrZero(dto.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.warehouse.not.emty");
        }
        if (DataUtil.isNullOrZero(dto.getToOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "receive.stock.require.msg");
        }
        if (DataUtil.isNullOrEmpty(dto.getRequestCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "create.request.import.request.code.require.msg");
        }
        String test = repository.getRequestCode();
        if (DataUtil.isNullObject(test) || !test.equals(dto.getRequestCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.code");
        }

        if (dto.getRequestCode().length() > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.maxCode");
        }

        if (DataUtil.isNullOrEmpty(dto.getDocumentName()) || dto.getDocumentName().length() > 100) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.maxDocument");
        }

        if (dto.getDocumentContent() == null || dto.getDocumentContent().length < 10 || dto.getDocumentContent().length > Const.FILE_MAX) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.maxFileSize");
        }

        if (DataUtil.isNullOrEmpty(dto.getContractCode()) || dto.getContractCode().length() > 100) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.contractCode");
        }

        if (dto.getContractImportDate() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.importDate");
        }
        if (DataUtil.isNullObject(dto.getCurrencyType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.currencyType");
        }
        if (DataUtil.isNullOrZero(dto.getUnitPrice()) || dto.getUnitPrice().compareTo(0L) <= 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.unitPrice");
        }
        //check shop, staff
        if (DataUtil.isNullOrZero(dto.getCreateStaffId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.staff");
        }
        if (DataUtil.isNullOrZero(dto.getCreateStaffId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.shop");
        }

        if (DataUtil.isNullObject(dto.getPartnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.partner");
        }
        PartnerDTO search = new PartnerDTO();
        search.setPartnerId(dto.getPartnerId());
        List<PartnerDTO> partner = partnerService.findPartner(search);
        if (DataUtil.isNullOrEmpty(partner)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.partner");
        }

        repository.validateStock(dto.getCreateStaffId(), dto.getToOwnerId());

        //validate detail
        if (DataUtil.isNullOrEmpty(dto.getListImportPartnerDetailDTOs()) || dto.getListImportPartnerDetailDTOs().size() > 1) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.request.valid.detail");
        }
        importPartnerDetailService.doValidateImportPartnerDetail(dto.getListImportPartnerDetailDTOs().get(0));
    }

    @Override
    public List<ImportPartnerRequestDTO> findImportPartnerRequest(ImportPartnerRequestDTO importPartnerRequestDTO) throws Exception {
        return mapper.toDtoBean(repository.findImportPartnerRequest(importPartnerRequestDTO));
    }

    @Override
    public Long getSequence() throws Exception {
        return repository.getSequence();
    }

    @Override
    public String getRequestCode() throws Exception {
        return repository.getRequestCode();
    }

    private void createMessage(int mode, ImportPartnerRequestDTO importPartnerRequestDTO) throws LogicException, Exception {
        StaffDTO staff = staffService.findOne(importPartnerRequestDTO.getCreateStaffId());
        if (staff == null || staff.getStaffId() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.staff");
        }

        Mt mtBean = new Mt();
        mtBean.setAppId(Const.AppProperties4Sms.appID.getValue().toString());
        mtBean.setAppName(Const.AppProperties4Sms.appName.getValue().toString());
        mtBean.setMsisdn(staff.getTel());
        mtBean.setChannel(Const.AppProperties4Sms.channel.getValue().toString());
        mtBean.setRetryNum(Const.AppProperties4Sms.retryNum.getLongValue());
        mtBean.setMoHisId(Const.AppProperties4Sms.moHisID.getLongValue());
        mtBean.setReceiveTime(optionSetService.getSysdateFromDB(false));
        String message = "";
        if (ModeMessage.create.validate(mode)) {
            List<OptionSetValueDTO> values = optionSetService.getByOptionSetCode(Const.OPTION_SET.ISDN_VTT);
            if (values == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.mo");
            }
            message = "import.partner.mt.contentCreate";
            String[] params = new String[4];
            ShopDTO shopDTO = shopService.findOne(importPartnerRequestDTO.getToOwnerId());
            params[0] = shopDTO.getShopCode() + "-" + shopDTO.getName();
            params[1] = importPartnerRequestDTO.getRequestCode();
            params[2] = importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getProdOfferName();
            params[3] = "" + importPartnerRequestDTO.getListImportPartnerDetailDTOs().get(0).getQuantity();
            message = GetTextFromBundleHelper.getTextParam(message, params);
            mtBean.setMessage(message);
            for (OptionSetValueDTO value : values) {
                Mt mt = DataUtil.cloneBean(mtBean);
                mt.setMsisdn(value.getValue());
                mtRepo.save(mt);
            }
            return;
        }
        if (ModeMessage.approve.validate(mode)) {
            message = "import.partner.mt.contentApprove";
            message = GetTextFromBundleHelper.getTextParam(message, importPartnerRequestDTO.getRequestCode());
        } else if (ModeMessage.reject.validate(mode)) {
            message = "import.partner.mt.contentReject";
            message = GetTextFromBundleHelper.getTextParam(message, importPartnerRequestDTO.getRequestCode(), importPartnerRequestDTO.getReason());
        }
        mtBean.setMessage(message);
        mtRepo.save(mtBean);
    }

    @Override
    public byte[] getAttachFileContent(Long requestLiquidateID) throws LogicException, Exception {
        return repository.getFileContent(requestLiquidateID);
    }
}

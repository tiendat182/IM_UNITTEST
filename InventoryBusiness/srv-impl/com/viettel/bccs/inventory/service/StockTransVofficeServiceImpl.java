package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.repo.StockTransVofficeRepo;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockTransVofficeService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.voffice.autosign.Ver3AutoSign;
import com.viettel.voffice.autosign.ws.FileAttachTranfer;
import com.viettel.voffice.autosign.ws.InforStaff;
import com.viettel.voffice.autosign.ws.KttsVofficeCommInpuParam;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class StockTransVofficeServiceImpl extends BaseServiceImpl implements StockTransVofficeService {

    private final BaseMapper<StockTransVoffice, StockTransVofficeDTO> mapper = new BaseMapper(StockTransVoffice.class, StockTransVofficeDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private CommonService commonService;

    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private BaseReportService baseReportService;

    @Autowired
    private Ver3AutoSign ver3AutoSign;

    @Autowired
    private PartnerContractService partnerContractService;

    @Autowired
    private StockBalanceSerialService stockBalanceSerialService;

    @Autowired
    private StockBalanceDetailService stockBalanceDetailService;

    @Autowired
    private StockBalanceRequestService stockBalanceRequestService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ReportUtil fileUtil;

    @Autowired
    private BaseStockTransVofficeService baseStockTransVofficeService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StockRequestOrderService stockRequestOrderService;

    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @Autowired
    private StockDeliverService stockDeliverService;

    @Autowired
    private StockDeliverDetailService stockDeliverDetailService;

    @Autowired
    private StockTransSerialService stockTransSerialService;

    @Autowired
    private DebitRequestService debitRequestService;

    @Autowired
    private DebitRequestDetailService debitRequestDetailService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private StockTransVofficeRepo repository;
    public static final Logger logger = Logger.getLogger(StockTransVofficeService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransVofficeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransVofficeDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransVofficeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockTransVofficeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockTransVofficeDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    public StockTransVofficeDTO save(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransVofficeDTO)));
    }

    public void doSignedVofficeValidate(StockTransActionDTO stockTransActionDTO) throws Exception, LogicException {

        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.search.validate.validStockTrans");
        }

        if (!DataUtil.safeEqual(Const.SIGN_VOFFICE, stockTransActionDTO.getSignCaStatus())) {
            return;
        }

        List<StockTransVofficeDTO> lstStockTransVoffice = findByFilter(Lists.newArrayList(
                new FilterRequest(StockTransVoffice.COLUMNS.STOCKTRANSACTIONID.name(), FilterRequest.Operator.EQ,
                        stockTransActionDTO.getStockTransActionId())));
        if (DataUtil.isNullOrEmpty(lstStockTransVoffice)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.voffice.empty");
        }

        if (!DataUtil.safeEqual(Const.VOFFICE_STATUS.SIGNED, lstStockTransVoffice.get(0).getStatus())) {
            if (DataUtil.safeEqual(Const.VOFFICE_STATUS.SIGN_ERROR, lstStockTransVoffice.get(0).getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.voffice.sign.signError", lstStockTransVoffice.get(0).getActionCode());
            } else if (DataUtil.safeEqual(Const.VOFFICE_STATUS.NOT_SIGNED, lstStockTransVoffice.get(0).getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.voffice.sign.error", lstStockTransVoffice.get(0).getActionCode());
            } else if (DataUtil.safeEqual(Const.VOFFICE_STATUS.REJECT, lstStockTransVoffice.get(0).getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.voffice.sign.reject", lstStockTransVoffice.get(0).getActionCode());
            } else if (DataUtil.safeEqual(Const.VOFFICE_STATUS.PROCESSING, lstStockTransVoffice.get(0).getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.voffice.sign.processing", lstStockTransVoffice.get(0).getActionCode());
            } else if (DataUtil.safeEqual(Const.VOFFICE_STATUS.FAILED, lstStockTransVoffice.get(0).getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.voffice.sign.failed", lstStockTransVoffice.get(0).getActionCode());
            }
        }
    }

    @Override
    public StockTransVofficeDTO findStockTransVofficeByRequestId(String requestId) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(requestId)) {
            return null;
        }
        return mapper.toDtoBean(repository.findStockTransVofficeByRequestId(requestId));
    }

    @Override
    public StockTransVofficeDTO findStockTransVofficeByActionId(Long actionId) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(actionId)) {
            return null;
        }
        return mapper.toDtoBean(repository.findStockTransVofficeByActionId(actionId));
    }

    @Override
    public List<StockTransVofficeDTO> searchStockTransVoffice(StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        return mapper.toDtoBean(repository.searchStockTransVoffice(stockTransVofficeDTO));
    }

    public List<StockTransVoffice> getLstVofficeSigned(Long maxDay, String prefixTemplate) throws Exception {
        return repository.getLstVofficeSigned(maxDay, prefixTemplate);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateVofficeDOA(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        baseStockTransVofficeService.updateVofficeDOA(stockTransVoffice);
    }

    @Override
    public void doSignVOffice(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        VStockTransDTO vStockTransDTO = null; //stockTransService.findVStockTransDTO(0L, stockTransVofficeDTO.getStockTransActionId());
        doValidate(vStockTransDTO, stockTransVofficeDTO);
        if (DataUtil.isNullOrEmpty(stockTransVofficeDTO.getErrorCode())) {
            try {
                List<String> lstEmail = Lists.newArrayList();
                String[] emailList = stockTransVofficeDTO.getSignUserList().split(",");
                for (int i = 0; i < emailList.length; i++) {
                    lstEmail.add(emailList[i] + "@viettel.com.vn");
                }
                List<FileAttachTranfer> listFile;
                //trinh ky
                Long result = null;
                if (Const.STOCK_BALANCE_PREFIX_TEMPLATE.equals(stockTransVofficeDTO.getPrefixTemplate())) {
                    //trinh ky de xuat can kho
                    listFile = doCreateStockBalanceFileAttach(stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky de xuat: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenStockBalance(stockTransVofficeDTO, listFile, lstEmail, GetTextFromBundleHelper.getText("voffice.doctitle.request"));
                } else if (DataUtil.safeEqual(stockTransVofficeDTO.getPrefixTemplate(), Const.DOA_TRANSFER_PREFIX_TEMPLATE)) {
                    //trinh ky chuyen hang DOA
                    listFile = doCreateDOATransferFileAttach(stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky de xuat: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenStockBalance(stockTransVofficeDTO, listFile, lstEmail, GetTextFromBundleHelper.getText("voffice.doctitle.request.DOA"));
                } else if (DataUtil.safeEqual(stockTransVofficeDTO.getPrefixTemplate(), Const.GOODS_REVOKE_PREFIX_TEMPLATE)) {
                    //trinh ky chuyen hang DOA
                    listFile = doCreateGoodRevokeFileAttach(stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky de xuat: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenStockBalance(stockTransVofficeDTO, listFile, lstEmail, GetTextFromBundleHelper.getText("voffice.doctitle.request.good.revoke"));
                } else if (DataUtil.safeEqual(stockTransVofficeDTO.getPrefixTemplate(), Const.STOCK_DELIVER_PREFIX_TEMPLATE)) {
                    //trinh ky ban giao kho
                    listFile = doCreateStockDeliver(stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky de xuat: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenStockBalance(stockTransVofficeDTO, listFile, lstEmail, GetTextFromBundleHelper.getText("voffice.doctitle.request.stock.deliver"));
                } else if (DataUtil.safeEqual(stockTransVofficeDTO.getPrefixTemplate(), Const.STOCK_DEVICE_TEMPLATE)) {
                    //trinh ky phan ra thiet bi
                    listFile = doCreateStockDeviceTemplate(stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky phan ra thiet bi: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenStockBalance(stockTransVofficeDTO, listFile, lstEmail, GetTextFromBundleHelper.getText("voffice.doctitle.request.stock.device"));
                } else if (DataUtil.safeEqual(stockTransVofficeDTO.getPrefixTemplate(), Const.DEBIT_REQUEST_PREFIX_TEMPLATE)) {
                    //trinh ky han muc
                    listFile = doCreateStockDebitRequestTemplate(stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky han muc: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenStockBalance(stockTransVofficeDTO, listFile, lstEmail, GetTextFromBundleHelper.getText("voffice.doctitle.request.debit.request"));
                } else {
                    //trinh ky giao dich
                    listFile = doCreateFileAttach(vStockTransDTO, stockTransVofficeDTO, lstEmail);
                    if (DataUtil.isNullOrEmpty(listFile)) {
                        throw new Exception();
                    }
                    logger.error("trinh ky de xuat: " + stockTransVofficeDTO.getStockTransActionId() + " - " + listFile.size());
                    result = doAuthenticate(stockTransVofficeDTO, vStockTransDTO, listFile, lstEmail);
                }
                if (DataUtil.safeEqual(result, 1L)) {
                    stockTransVofficeDTO.setStatus(Const.STOCK_TRANS_VOFFICE.V_SIGNING.toString());
                    stockTransVofficeDTO.setRetry(null);
                } else {
                    stockTransVofficeDTO.setRetry(stockTransVofficeDTO.getRetry() == null ? 0L : stockTransVofficeDTO.getRetry() + 1L);
                    stockTransVofficeDTO.setStatus(Const.STOCK_TRANS_VOFFICE.V_VALIDATE_ERROR.toString());
                    stockTransVofficeDTO.setResponseCode(DataUtil.safeToString(result));
                    stockTransVofficeDTO.setResponseCodeDescription(GetTextFromBundleHelper.getText("sign.state" + result));
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                System.out.println(ex.getMessage());
                stockTransVofficeDTO.setErrorCode("999");
                stockTransVofficeDTO.setErrorCodeDescription(GetTextFromBundleHelper.getText("sign.state.error"));
                stockTransVofficeDTO.setStatus(Const.STOCK_TRANS_VOFFICE.V_ERROR.toString());
                stockTransVofficeDTO.setRetry(stockTransVofficeDTO.getRetry() == null ? 0L : stockTransVofficeDTO.getRetry() + 1L);
            }
        }
        stockTransVofficeDTO.setLastModify(getSysDate(em));
        repository.save(mapper.toPersistenceBean(stockTransVofficeDTO));
    }

    private Long doAuthenStockBalance(StockTransVofficeDTO stockTransVofficeDTO, List<FileAttachTranfer> listFileAttach,
                                      List<String> listSign, String tittle) throws Exception {
        KttsVofficeCommInpuParam params = new KttsVofficeCommInpuParam();
        params.getLstFileAttach().addAll(listFileAttach);
        params.setAccountName(stockTransVofficeDTO.getAccountName());
        params.setAccountPass(stockTransVofficeDTO.getAccountPass());
        params.setTransCode(stockTransVofficeDTO.getStockTransVofficeId());

        params.setIsCanVanthuXetduyet(false);

        params.setDocTitle(tittle);
        params.setRegisterNumber(fileUtil.getImCode() + stockTransVofficeDTO.getStockTransVofficeId().toString());
        params.getLstEmail().addAll(listSign);
        params.setCreateDate(DateUtil.date2yyMMddString(stockTransVofficeDTO.getCreateDate()));
        return ver3AutoSign.kttsAuthenticate(params);
    }

    private Long doAuthenticate(StockTransVofficeDTO stockTransVofficeDTO,
                                VStockTransDTO vStockTransDTO,
                                List<FileAttachTranfer> listFileAttach,
                                List<String> listSign) throws Exception {
        KttsVofficeCommInpuParam params = new KttsVofficeCommInpuParam();
        params.getLstFileAttach().addAll(listFileAttach);
        params.setMoneyTransfer(stockTransService.getTransAmount(vStockTransDTO.getStockTransID()));
        params.setMoneyUnitID(Const.STOCK_TRANS_VOFFICE.VND);
        /// /
        params.setAccountName(stockTransVofficeDTO.getAccountName());
        params.setAccountPass(stockTransVofficeDTO.getAccountPass());
        //
        params.setIsCanVanthuXetduyet(false);
        params.setDocTitle(GetTextFromBundleHelper.getText("voffice.doctitle.trans"));
        params.setTransCode(stockTransVofficeDTO.getStockTransVofficeId());
        params.setRegisterNumber(fileUtil.getImCode() + stockTransVofficeDTO.getStockTransVofficeId());
        params.getLstEmail().addAll(listSign);
        params.setCreateDate(DateUtil.date2yyMMddString(vStockTransDTO.getCreateDateTime()));
        return ver3AutoSign.kttsAuthenticate(params);
    }

    private List<InforStaff> getListInfoStaff(String arrEmail) throws Exception {
        return ver3AutoSign.getListInfoStaff(arrEmail);
    }

    private void doValidate(VStockTransDTO vStockTransDTO, StockTransVofficeDTO stockTransVofficeDTO) throws Exception {
        try {
            stockTransVofficeDTO.setResponseCodeDescription(null);
            stockTransVofficeDTO.setResponseCode(null);
            stockTransVofficeDTO.setErrorCodeDescription(null);
            stockTransVofficeDTO.setErrorCode(null);
            if (Const.GOODS_REVOKE_PREFIX_TEMPLATE.equals(stockTransVofficeDTO.getPrefixTemplate())
                    || Const.STOCK_DELIVER_PREFIX_TEMPLATE.equals(stockTransVofficeDTO.getPrefixTemplate())
                    || Const.DEBIT_REQUEST_PREFIX_TEMPLATE.equals(stockTransVofficeDTO.getPrefixTemplate())) {
                return;
            }
            if (DataUtil.isNullOrEmpty(stockTransVofficeDTO.getPrefixTemplate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.voffice.valid.prefix");
            }
            if (Const.STOCK_BALANCE_PREFIX_TEMPLATE.equals(stockTransVofficeDTO.getPrefixTemplate())) {
                List<StockBalanceRequestDTO> lstStockBalanceRequest = stockBalanceRequestService.searchStockBalanceRequest(new StockBalanceRequestDTO(stockTransVofficeDTO.getStockTransActionId()));
                if (DataUtil.isNullOrEmpty(lstStockBalanceRequest)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.balance.valid.object");
                }
                if (!Const.BalanceRequestStatus.approved.toLong().equals(lstStockBalanceRequest.get(0).getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.balance.valid.status");
                }

                List<StockBalanceDetailDTO> stockBalanceDetailDTOs = getListStockBalanceDetail(lstStockBalanceRequest.get(0).getStockRequestId());
                if (DataUtil.isNullOrEmpty(stockBalanceDetailDTOs)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.balance.valid.detail");
                }
                return;
            }
            if (vStockTransDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.voffice.valid.trans");
            }
            if (DataUtil.isNullOrEmpty(vStockTransDTO.getActionCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.voffice.valid.actionCode");
            }
            if (DataUtil.isNullOrEmpty(vStockTransDTO.getActionType())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.voffice.valid.actionType");
            }
            if (DataUtil.isNullOrZero(vStockTransDTO.getActionID())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.voffice.valid.action");
            }
            if (vStockTransDTO.getFromOwnerType() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.voffice.valid.fromOwnerType");
            }
            if (!Const.ConfigListIDCheck.SUPORT_OWNER_TYPE_LIST.validate(vStockTransDTO.getFromOwnerType())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PERMISSION, "stock.trans.voffice.valid.supportOwnerType");
            }
        } catch (LogicException ex) {
            logger.error("Error: ", ex);
            stockTransVofficeDTO.setErrorCode(ex.getErrorCode());
            stockTransVofficeDTO.setErrorCodeDescription(ex.getMessage());
            stockTransVofficeDTO.setStatus(Const.STOCK_TRANS_VOFFICE.V_VALIDATE_ERROR.toString());
        }
    }

    private List<StockBalanceDetailDTO> getListStockBalanceDetail(Long stockBalanceRequestId) throws Exception {
        List<StockBalanceDetailDTO> stockBalanceDetailDTOs = Lists.newArrayList();
        if (stockBalanceRequestId != null) {
            stockBalanceDetailDTOs = stockBalanceDetailService.getListStockBalanceDetail(stockBalanceRequestId);
        }
        return stockBalanceDetailDTOs;
    }

    private List<FileAttachTranfer> doCreateGoodRevokeFileAttach(StockTransVofficeDTO stockTransVofficeDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            StockRequestOrderDTO stockRequestOrderDTO = stockRequestOrderService.findOne(stockTransVofficeDTO.getStockTransActionId());
            if (DataUtil.isNullObject(stockRequestOrderDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong ton tai giao dich stock_request_order");
            }
            List<StockRequestOrderDetailDTO> lstDetail = stockRequestOrderDetailService.getLstDetailTemplate(stockTransVofficeDTO.getStockTransActionId());
            if (DataUtil.isNullObject(lstDetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong ton tai giao dich stock_request_order_detail");
            }
            for (StockRequestOrderDetailDTO stockRequestOrderDetailDTO : lstDetail) {
                stockRequestOrderDetailDTO.setTypeTransfer(getText("good.revoke.type.transfer"));
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "good.revoke.msg.signDate";
            String vOfficeDate = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(day), "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstDetail", lstDetail);
            bean.put("ownerName", lstDetail.get(0).getToOwnerName());
            bean.put("vOfficeNumber", stockRequestOrderDTO.getOrderCode());
            bean.put("vOfficeDate", vOfficeDate);
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.GOOD_REVOKE;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.GOOD_REVOKE_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }

    private List<FileAttachTranfer> doCreateStockDebitRequestTemplate(StockTransVofficeDTO stockTransVofficeDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            DebitRequestDTO debitRequestDTO = debitRequestService.findOne(stockTransVofficeDTO.getStockTransActionId());
            List<DebitRequestReportDTO> lstDetail = debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId());

            for (DebitRequestReportDTO debitRequestReportDTO : lstDetail) {
                debitRequestReportDTO.setRevenueInMonth(debitRequestService.getRevenueInMonth(debitRequestReportDTO.getOwnerId(), debitRequestReportDTO.getOwnerType()));
            }
            // Lay thong tin nhan vien tao
            StaffDTO staffDTO = staffService.getStaffByStaffCode(debitRequestDTO.getCreateUser());
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.vali.createUser");
            }

            ShopDTO shopDTO = shopService.findOne(staffDTO.getShopId());
            AreaDTO province = areaService.findByCode(shopDTO.getProvince());
            AreaDTO district = areaService.findByCode(shopDTO.getProvince() + shopDTO.getDistrict());

//            MvShopStaffDTO mvShopStaffDTO = shopService.getMViewShopStaff(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "good.revoke.msg.signDate";
            String vOfficeDate = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(day), "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstDetail", lstDetail);
            bean.put("districtName", district == null ? "" : district.getName());
            bean.put("provinceName", province == null ? "" : province.getName());
            bean.put("vOfficeDate", vOfficeDate);
            if (Const.OWNER_TYPE.SHOP.equals(debitRequestDTO.getRequestObjectType())) {
                bean.put("userName", GetTextFromBundleHelper.getText("vofice.debit.request.template.userNameShop"));
                bean.put("department", GetTextFromBundleHelper.getText("vofice.debit.request.template.departmentShop"));
            } else {
                bean.put("userName", GetTextFromBundleHelper.getText("vofice.debit.request.template.userName"));
                bean.put("department", GetTextFromBundleHelper.getText("vofice.debit.request.template.department"));
            }
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.DEBIT_REQUEST;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.DEBIT_REQUEST_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }

    private List<FileAttachTranfer> doCreateStockDeviceTemplate(StockTransVofficeDTO stockTransVofficeDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            List<StockDeviceDTO> lstDetail = stockTransSerialService.getLstStockDevice(stockTransVofficeDTO.getStockTransActionId());

            if (DataUtil.isNullOrEmpty(lstDetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong tim thay thong tin phan ra thiet bi");
            }
            StockDeviceDTO stockDeviceDTO = lstDetail.get(0);
            String province = commonService.getProvince(stockDeviceDTO.getRequestShopId());
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "stock.balance.msg.signDate";
            if (DataUtil.isNullOrEmpty(province)) {
                province = GetTextFromBundleHelper.getText("stock.balance.msg.defaultParam");
            }
            String vofficeDate = GetTextFromBundleHelper.getTextParam(key, province, "" + day, "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstDetail", lstDetail);
            bean.put("requestShop", stockDeviceDTO.getRequestShopName());
            bean.put("vOfficeDate", vofficeDate);

            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.STOCK_DEVICE;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.STOCK_DEVICE_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }

    private List<FileAttachTranfer> doCreateStockDeliver(StockTransVofficeDTO stockTransVofficeDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            StockDeliverDTO stockDeliverDTO = stockDeliverService.findOne(stockTransVofficeDTO.getStockTransActionId());
            if (DataUtil.isNullObject(stockDeliverDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong ton tai giao dich stock_deliver");
            }
            List<StockDeliverDetailDTO> lstDetail = stockDeliverDetailService.getLstDetailByStockDeliverId(stockTransVofficeDTO.getStockTransActionId());
            if (DataUtil.isNullOrEmpty(lstDetail)) {
                lstDetail = null;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            //
            ShopDTO shopDTO = shopService.findOne(stockDeliverDTO.getOwnerId());
            String key = "stock.deliver.sign.vofice.signDate";
            String vOfficeDate = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(day), "" + month, "" + year, "" + shopDTO.getAddress());
            //vStaffOld
            StaffDTO staffDTO = staffService.findOne(stockDeliverDTO.getOldStaffId());
            key = "stock.deliver.sign.vofice.vtaffOld";
            String vStaffOld = GetTextFromBundleHelper.getTextParam(key, staffDTO.getName(), "" + staffDTO.getTtnsCode() == null ? "" : staffDTO.getTtnsCode(), "" + staffDTO.getEmail(), "" + staffDTO.getTel());
            //vStaffOwnerOld
            staffDTO = staffService.findOne(stockDeliverDTO.getOldStaffOwnerId());
            key = "stock.deliver.sign.vofice.vtaffOwnerOld";
            String vStaffOwnerOld = GetTextFromBundleHelper.getTextParam(key, staffDTO.getName(), "" + staffDTO.getTtnsCode() == null ? "" : staffDTO.getTtnsCode(), "" + staffDTO.getEmail(), "" + staffDTO.getTel());
            //vStaffNew
            staffDTO = staffService.findOne(stockDeliverDTO.getNewStaffId());
            key = "stock.deliver.sign.vofice.vtaffNew";
            String vStaffNew = GetTextFromBundleHelper.getTextParam(key, staffDTO.getName(), "" + staffDTO.getTtnsCode() == null ? "" : staffDTO.getTtnsCode(), "" + staffDTO.getEmail(), "" + staffDTO.getTel());
            //vStaffOwnerNew
            staffDTO = staffService.findOne(stockDeliverDTO.getNewStaffOwnerId());
            key = "stock.deliver.sign.vofice.vtaffOwnerNew";
            String vStaffOwnerNew = GetTextFromBundleHelper.getTextParam(key, staffDTO.getName(), "" + staffDTO.getTtnsCode() == null ? "" : staffDTO.getTtnsCode(), "" + staffDTO.getEmail(), "" + staffDTO.getTel());
            //vDatetime
            key = "stock.deliver.sign.vofice.signDateTime";
            String vOfficeDateTime = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(hour), "" + minute, "" + day, "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstDetail", lstDetail);
            bean.put("vOfficeNumber", stockDeliverDTO.getCode());
            bean.put("vOfficeDate", vOfficeDate);
            bean.put("vStaffOld", vStaffOld);
            bean.put("vStaffOwnerOld", vStaffOwnerOld);
            bean.put("vStaffNew", vStaffNew);
            bean.put("vStaffOwnerNew", vStaffOwnerNew);
            bean.put("shopCode", shopDTO.getShopCode());
            bean.put("shopName", shopDTO.getName());
            bean.put("vOfficeDateTime", vOfficeDateTime);


            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.STOCK_DELIVER;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.STOCK_DELIVER_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }


    private List<FileAttachTranfer> doCreateDOATransferFileAttach(StockTransVofficeDTO stockTransVofficeDTO, List<String> lstStaff) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        String shopName = "";
        Long shopId = null;
        try {
            StockTransDTO stockTransDTO = stockTransService.findStockTransByActionId(stockTransVofficeDTO.getStockTransActionId());
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong ton tai giao dich stock_trans DOA");
            }
            List<DOATransferDTO> lstContent = stockTransService.getContentFileDOA(stockTransDTO.getStockTransId());
            if (DataUtil.isNullObject(lstContent)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong ton tai giao dich stock_trans_action DOA");
            }
            DOATransferDTO doaTransferDTO = lstContent.get(0);
            if (DataUtil.safeEqual(doaTransferDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                shopName = doaTransferDTO.getOwnerName();
                shopId = doaTransferDTO.getOwnerId();
            } else {
                ShopDTO shopDTO = shopService.findOne(doaTransferDTO.getParrentShopId());
                if (DataUtil.isNullObject(shopDTO)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "Khong ton tai giao dich kho don vi cua nhan vien");
                }
                shopName = shopDTO.getName();
                shopId = shopDTO.getShopId();
            }
            String province = commonService.getProvince(shopId);
            Date createDate = doaTransferDTO.getCreateDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(createDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "stock.balance.msg.signDate";
            if (DataUtil.isNullOrEmpty(province)) {
                province = GetTextFromBundleHelper.getText("stock.balance.msg.defaultParam");
            }
            String vofficeDate = GetTextFromBundleHelper.getTextParam(key, province, "" + day, "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstStockModel", lstContent);
            bean.put("shopName", shopName);
            bean.put("ownerName", doaTransferDTO.getOwnerName());
            bean.put("ownerCode", doaTransferDTO.getOwnerCode());
            bean.put("vOfficeDate", vofficeDate);
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.DOA_TRANSFER;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstStaff);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.STOCK_DOA_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }

    private List<FileAttachTranfer> doCreateStockBalanceFileAttach(StockTransVofficeDTO stockTransVofficeDTO, List<String> lstStaff) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            List<StockBalanceRequestDTO> lstStockBalanceRequest = stockBalanceRequestService.searchStockBalanceRequest(new StockBalanceRequestDTO(stockTransVofficeDTO.getStockTransActionId()));
            StockBalanceRequestDTO selectedStockBalanceRequestDTO = lstStockBalanceRequest.get(0);
            List<StockBalanceDetailDTO> listStockBalanceDetailDTO = getListStockBalanceDetail(selectedStockBalanceRequestDTO.getStockRequestId());
            String ownerName = getOwnerName(selectedStockBalanceRequestDTO.getOwnerId(), Const.STOCK_TRANS_VOFFICE.OWNERTYPE_SHOP);
            for (StockBalanceDetailDTO stockBalanceDetailDTO : listStockBalanceDetailDTO) {
                stockBalanceDetailDTO.setType(DataUtil.safeToString(selectedStockBalanceRequestDTO.getType()));
                stockBalanceDetailDTO.setOwnerName(ownerName);
                Long bccs = stockBalanceDetailDTO.getQuantityBccs();
                bccs = bccs == null ? 0L : bccs;
                Long real = stockBalanceDetailDTO.getQuantityReal();
                real = real == null ? 0L : real;
                Long erp = stockBalanceDetailDTO.getQuantityErp();
                erp = erp == null ? 0L : erp;
                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                stockBalanceDetailDTO.setDivBCCSFN(decimalFormat.format(bccs - erp));
                stockBalanceDetailDTO.setDivInspectFN(decimalFormat.format(real - erp));
            }
            StaffDTO staffDTO = staffService.getStaffByStaffCode(selectedStockBalanceRequestDTO.getCreateUser());
            if (staffDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.valid.staff", selectedStockBalanceRequestDTO.getCreateUser());
            }
            Map bean = new HashMap<>();
            bean.put("lstStockModel", listStockBalanceDetailDTO);
            bean.put("createUser", staffDTO.getStaffCode());
            bean.put("ownerName", ownerName);
            bean.put("createIsdn", DataUtil.isNullObject(staffDTO.getIsdn()) ? "" : staffDTO.getIsdn());
            buildVofficeDate(bean, selectedStockBalanceRequestDTO);
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.STOCK_BALANCE_REPORT;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstStaff);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.STOCK_BALANCE.STOCK_BALANCE_FILE_NAME);
            lstFile.add(fileAttachTranfer);
            //lay file phu luc
            byte[] phuluc = layPhuLucSerial(selectedStockBalanceRequestDTO);
            if (phuluc != null && phuluc.length > 0) {
                FileAttachTranfer filePhuLuc = new FileAttachTranfer();
                filePhuLuc.setAttachBytes(bytes);
                filePhuLuc.setFileSign(0L);
                filePhuLuc.setFileName(Const.STOCK_BALANCE.STOCK_BALANCE_SERIAL_FILE_NAME);
                lstFile.add(filePhuLuc);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return lstFile;
    }

    private void buildVofficeDate(Map bean, StockBalanceRequestDTO stockBalanceRequestDTO) throws Exception {
        Date sysdate = getSysDate(em);
        String key = "stock.balance.msg.signDate";
        String provinve = commonService.getProvince(stockBalanceRequestDTO.getOwnerId());
        if (DataUtil.isNullOrEmpty(provinve)) {
            provinve = GetTextFromBundleHelper.getText("stock.balance.msg.defaultParam");
        }
        String header = GetTextFromBundleHelper.getTextParam(key, provinve, "" + sysdate.getDay(), "" + sysdate.getMonth(), "" + sysdate.getYear());
        bean.put("vOfficeDate", header);
    }

    private byte[] layPhuLucSerial(StockBalanceRequestDTO selectedStockBalanceRequestDTO) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String ownerName = getOwnerName(selectedStockBalanceRequestDTO.getOwnerId(), Const.STOCK_TRANS_VOFFICE.OWNERTYPE_SHOP);

        List<StockBalanceDetailDTO> listStockBalanceDetailDTO = getListStockBalanceDetail(selectedStockBalanceRequestDTO.getStockRequestId());
        List<StockBalanceSerialDTO> lstBalanceSerialDTO = Lists.newArrayList();
        for (StockBalanceDetailDTO stockBalanceDetailDTO : listStockBalanceDetailDTO) {
            List<StockBalanceSerialDTO> subs = DataUtil.defaultIfNull(stockBalanceSerialService.getListStockBalanceSerialDTO(stockBalanceDetailDTO.getStockBalanceDetailId()), Lists.newArrayList());
            for (StockBalanceSerialDTO stockBalanceSerialDTO : subs) {
                stockBalanceSerialDTO.setOwnerName(ownerName);
                stockBalanceSerialDTO.setProdOfferName(stockBalanceDetailDTO.getProdOfferName());
                Long quantity = stockBalanceDetailDTO.getQuantity();
                quantity = quantity == null ? 0L : quantity;
                stockBalanceSerialDTO.setQuantity(decimalFormat.format(quantity));
            }
            lstBalanceSerialDTO.addAll(subs);
        }
        if (DataUtil.isNullOrEmpty(lstBalanceSerialDTO)) {
            return new byte[]{};
        }
        Map bean = new HashMap<>();
        bean.put("lstStockModel", lstBalanceSerialDTO);
        String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.STOCK_BALANCE_ATTACH;
        return JasperReportUtils.exportPdfByte(bean, templateName);
    }

    private List<FileAttachTranfer> doCreateFileAttach(VStockTransDTO vStockTransDTO,
                                                       StockTransVofficeDTO stockTransVofficeDTO,
                                                       List<String> lstStaff) throws LogicException, Exception {
        ReportDTO reportDTO = createReportConfig(vStockTransDTO, stockTransVofficeDTO.getPrefixTemplate());
        //
        List<StockTransFullDTO> data = stockTransService.searchStockTransDetail(Lists.newArrayList(vStockTransDTO.getActionID()));
        List<StockTransDetailDTO> lstStockTransDetailDTOs = getData(data, vStockTransDTO);
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        byte[] fileContent = baseReportService.exportWithDataSource(reportDTO, new JRBeanCollectionDataSource(lstStockTransDetailDTOs));
        //insert comment
        if (fileContent == null || fileContent.length == 0) {
            return lstFile;
        }
        fileContent = fileUtil.insertComment(fileContent, lstStaff);

        FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
        fileAttachTranfer.setAttachBytes(fileContent);
        fileAttachTranfer.setFileSign(1L);
        fileAttachTranfer.setFileName(getFileAttachName(vStockTransDTO));
        lstFile.add(fileAttachTranfer);
        if (DataUtil.safeEqual(stockTransVofficeDTO.getFindSerial(), Const.STOCK_TRANS_VOFFICE.TRANG_THAI_ACTIVE)) {
            //them doan lay serial detail vao day
        }
        return lstFile;
    }

    private ReportDTO createReportConfig(VStockTransDTO vStockTransDTO, String prefixTemplate) throws Exception {
        ReportDTO reportDTO = new ReportDTO();

        HashMap<String, Object> params = new HashMap<>();
        String path = fileUtil.getTemplatePath();
        String name = getTemplateName(vStockTransDTO, prefixTemplate);

        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(vStockTransDTO.getActionID());

        if (stockTransActionDTO != null) {
            String actionCode = stockTransActionDTO.getActionCode();
            params.put(Const.REPORT_PARAMS.ACTIONCODE.toString(), actionCode);
            actionCode = getReceiptNO(stockTransActionDTO);
            params.put(Const.REPORT_PARAMS.RECEIPTNO.toString(), actionCode);
        }

        params.put(Const.REPORT_PARAMS.DATEREQUEST.toString(), DateUtil.date2ddMMyyyyString(getSysDate(em)));

        params.put(Const.REPORT_PARAMS.FROMADDRESS.toString(), getAddress(vStockTransDTO.getFromOwnerID(), vStockTransDTO.getFromOwnerType()));
        params.put(Const.REPORT_PARAMS.FROMOWNER.toString(), getOwnerName(vStockTransDTO.getFromOwnerID(), vStockTransDTO.getFromOwnerType()));

        params.put(Const.REPORT_PARAMS.TOADDRESS.toString(), getAddress(vStockTransDTO.getToOwnerID(), vStockTransDTO.getToOwnerType()));
        params.put(Const.REPORT_PARAMS.TOOWNER.toString(), getOwnerName(vStockTransDTO.getToOwnerID(), vStockTransDTO.getToOwnerType()));
        //
        params.put(Const.REPORT_PARAMS.NOTE.toString(), vStockTransDTO.getNote());
        //
        params.put(Const.REPORT_PARAMS.REASONNAME.toString(), vStockTransDTO.getReasonName());
        if (Const.ConfigListIDCheck.EXPORT.sValidate(vStockTransDTO.getActionType())) {
            params.put(Const.REPORT_PARAMS.STOCKTRANSTYPE.toString(), Const.ConfigListIDCheck.EXPORT.getValue().toString());
        } else {
            params.put(Const.REPORT_PARAMS.STOCKTRANSTYPE.toString(), Const.ConfigListIDCheck.IMPORT.getValue().toString());
        }
        //init partnercontract info
        initContractProperties(params, vStockTransDTO);

        reportDTO.setParams(params);
        reportDTO.setJasperFilePath(path + name);
        return reportDTO;
    }

    private String getAddress(Long ownerID, Long ownerTyp) {
        try {
            return commonService.getOwnerAddress(ownerID.toString(), ownerTyp.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    private String getOwnerName(Long ownerID, Long ownerTyp) {
        try {
            return commonService.getOwnerName(ownerID.toString(), ownerTyp.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    private String getTemplateName(VStockTransDTO vStockTransDTO, String prefixTemplate) throws Exception {
        String preFix = prefixTemplate;
        String stockReportTemplate = "";
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
            if (!DataUtil.isNullOrZero(vStockTransDTO.getFromOwnerID()) && DataUtil.safeEqual(vStockTransDTO.getFromOwnerID(), Const.SHOP.SHOP_VTT_ID)) {
                preFix += "_VT";
            }
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER) && DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
            if (!DataUtil.isNullOrZero(vStockTransDTO.getToOwnerID()) && DataUtil.safeEqual(vStockTransDTO.getToOwnerID(), Const.SHOP.SHOP_VTT_ID)) {
                preFix += "_VT";
            }
        }
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                || DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            stockReportTemplate = commonService.getStockReportTemplate(vStockTransDTO.getFromOwnerID(), DataUtil.safeToString(vStockTransDTO.getFromOwnerType()));
        }
        String templateName = "";
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER) || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
            if (preFix != null && preFix.endsWith("_TTH_CN")) {
                templateName = preFix + "_2007.jasper";
            } else {
                templateName = preFix + "_" + stockReportTemplate + "_2007.jasper";
            }
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER) || DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
            templateName = preFix + "_" + stockReportTemplate + "_2007.jasper";
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransType(), DataUtil.safeToLong(Const.STOCK_TRANS_TYPE.AGENT))) {
            templateName = preFix + "_" + stockReportTemplate + ".jasper";
        }
        if (DataUtil.safeEqual(vStockTransDTO.getStockTransType(), Const.STOCK_TRANS_TYPE.REVOKE_KIT) && DataUtil.safeEqual(vStockTransDTO.getStockTransType(), Const.STOCK_TRANS_TYPE.EXPORT_KIT)) {
            templateName = preFix + "_RC" + ".jasper";
        }

        return templateName;
    }

    private String getFileName(VStockTransDTO vStockTransDTO) throws Exception {
        String prefix = getPrefix(vStockTransDTO);
        String subPrefix = getSubPrefix(vStockTransDTO);
        return prefix + Const.TEMPLATE.SEPARATOR.toString()
                + vStockTransDTO.getActionCode().replaceAll("\\W", "")
                + Const.TEMPLATE.SEPARATOR.toString()
                + subPrefix;
    }

    private String getSubPrefix(VStockTransDTO vStockTransDTO) throws Exception {
        String subPrefix = Const.ConfigStockTrans.FR_PARTNER.toString();
        if (DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)
                || DataUtil.safeEqual(vStockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.STAFF_LONG)) {
            Long ownerId = vStockTransDTO.getFromOwnerID();
            Long ownerType = vStockTransDTO.getFromOwnerType();
            if (Const.ConfigStockTrans.PN.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.NK.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.LN.validate(vStockTransDTO.getActionType())) {
                ownerId = vStockTransDTO.getToOwnerID();
                ownerType = vStockTransDTO.getToOwnerType();
            }
            subPrefix = commonService.getStockReportTemplate(ownerId, DataUtil.safeToString(ownerType));
        }
        return subPrefix;
    }

    private String getPrefix(VStockTransDTO vStockTransDTO) {
        String prefix = Const.ConfigStockTrans.LX.toString();
        if (Const.ConfigStockTrans.PX.validate(vStockTransDTO.getActionType()) &&
                Const.ConfigStockTrans.PXS.lValidate(vStockTransDTO.getStockTransType())) {
            prefix = Const.ConfigStockTrans.PXS.toString();
        } else {
            if (Const.ConfigStockTrans.PX.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.XK.validate(vStockTransDTO.getActionType())) {
                prefix = Const.ConfigStockTrans.PX.toString();
            } else if (Const.ConfigStockTrans.PN.validate(vStockTransDTO.getActionType())
                    || Const.ConfigStockTrans.NK.validate(vStockTransDTO.getActionType())) {
                prefix = Const.ConfigStockTrans.PN.toString();
            } else if (Const.ConfigStockTrans.LN.validate(vStockTransDTO.getActionType())) {
                prefix = Const.ConfigStockTrans.LN.toString();
            }
        }
        return prefix;
    }

    private String getFileAttachName(VStockTransDTO vStockTransDTO) throws Exception {
        String actionCode = vStockTransDTO.getActionCode();
        if (DataUtil.isNullOrEmpty(actionCode)) {
            vStockTransDTO.setActionCode(Const.TEMPLATE.DEFAULT.toString());
        }
        return getFileName(vStockTransDTO) + Const.TEMPLATE.MEXTENSION;
    }

    private String getReceiptNO(StockTransActionDTO stockTransActionDTO) {
        String actionCode = stockTransActionDTO.getActionCodeShop();
        if (DataUtil.isNullOrEmpty(actionCode)) {
            actionCode = stockTransActionDTO.getActionCode();
            if (!DataUtil.isNullOrEmpty(actionCode)) {
                if (Const.ConfigStockTrans.PN.validate(stockTransActionDTO.getStatus())
                        || Const.ConfigStockTrans.PX.validate(stockTransActionDTO.getStatus())) {
                    String[] split = actionCode.split("_");
                    actionCode = split[split.length - 1];
                }
            }
        }
        return actionCode;
    }

    public List<StockTransDetailDTO> getData(List<StockTransFullDTO> stockTransFullDTOs, VStockTransDTO vStockTransDTO) {
        List<StockTransDetailDTO> lstData = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(stockTransFullDTOs)) {
            for (StockTransFullDTO stockTransFullDTO : stockTransFullDTOs) {
                StockTransDetailDTO detailDTO = new StockTransDetailDTO(stockTransFullDTO.getProdOfferName(), stockTransFullDTO.getUnit());
                detailDTO.setTransDetailID(stockTransFullDTO.getStockTransDetailId());
                detailDTO.setQuantity(stockTransFullDTO.getQuantity());
                detailDTO.setPrice(stockTransFullDTO.getPrice());
                detailDTO.setTotalPrice(stockTransFullDTO.getAmount());
                detailDTO.setFromSerial(stockTransFullDTO.getFromSerial());
                detailDTO.setToSerial(stockTransFullDTO.getToSerial());
                detailDTO.setReceivingUnit(vStockTransDTO.getToOwnerName());
                detailDTO.setNote(vStockTransDTO.getNote());
                lstData.add(detailDTO);
            }
        }
        return lstData;
    }

    public void initContractProperties(HashMap bean, VStockTransDTO stockTransDTO) throws Exception {
        if (stockTransDTO != null
                && DataUtil.safeEqual(Const.STOCK_TRANS_VOFFICE.OWNERTYPE_PARTNER, stockTransDTO.getFromOwnerType())) {
            PartnerContractDTO partnerContractDTO = partnerContractService.findByStockTransID(stockTransDTO.getStockTransID());
            bean.put("contractCode", partnerContractDTO.getContractCode());
            bean.put("contractDate", partnerContractDTO.getContractDate());
            bean.put("poCode", partnerContractDTO.getPoCode());
            bean.put("poDate", partnerContractDTO.getPoDate());
            bean.put("requestImportDate", partnerContractDTO.getRequestDate());
            bean.put("importStockDate", partnerContractDTO.getImportDate());
            bean.put("deliveryLocation", partnerContractDTO.getDeliveryLocation());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateVofficeDeliver(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        baseStockTransVofficeService.updateVofficeDeliver(stockTransVoffice);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateVofficeDevice(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        baseStockTransVofficeService.updateVofficeDevice(stockTransVoffice);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateVofficeDebit(StockTransVoffice stockTransVoffice) throws LogicException, Exception {
        baseStockTransVofficeService.updateVofficeDebit(stockTransVoffice);
    }

}

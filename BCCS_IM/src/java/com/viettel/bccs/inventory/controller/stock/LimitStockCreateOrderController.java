package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.bccs.sale.dto.PaymentGroupDTO;
import com.viettel.bccs.sale.dto.PaymentGroupDayTypeDTO;
import com.viettel.bccs.sale.dto.PaymentGroupServiceDTO;
import com.viettel.bccs.sale.service.PaymentGroupService;
import com.viettel.bccs.sale.service.PaymentGroupServiceService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author:thanhnt77
 */
@Component
@Scope("view")
@ManagedBean(name = "limitStockCreateOrderController")
public class LimitStockCreateOrderController extends InventoryController {

    private boolean b = true;
    private String[] fileType = {".doc", ".docx", ".pdf", ".xls", ".xlsx", ".png", ".jpg", ".jpeg", ".bmp"};
    private String[] fileImportType = {".xls", ".xlsx"};
    private StaffDTO currentStaff;
    private List<StaffDTO> lstStaffDTO;
    private DebitRequestDTO debitRequestDTO;
    private DebitRequestDetailDTO debitRequestDtlDTO;
    private List<DebitRequestDetailDTO> lstRequestDtl = Lists.newArrayList();
    private StockDebitDTO stockDebitDTO;
    private List<StockDebitDTO> lstStockDebit = Lists.newArrayList();
    private List<OptionSetValueDTO> lstType, lstDebitDayType, lstDebitLevel, lstDebitLevelConst, lstFinanceType;
    private List<ShopDTO> lstShopDTO;
    private String description = "";
    private String descriptionFile = "";
    private String outPath = "";
    private String importFileName = "";
    private String limitCreateOrderType = "1";
    private String typeInput = "1";
    private String staff;
    private int tabIndex;
    private UploadedFile fileImport;
    private StreamedContent errorFile;
    private boolean showWhenError = false;
    private Workbook bookError;
    private Boolean writeOffice = true;
    private String templateFileName = "";
    private String templatePath = "";
    private String fileNameError = "";
    private List<PaymentGroupDTO> lstPaymentGroup;
    private List<PaymentGroupServiceDTO> lstPaymentGroupService;

    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice

    @Autowired
    private ShopInfoNameable shopInfoTag;

    @Autowired
    private StaffInfoNameable staffInfoTag;

    @Autowired
    private OrderStockTagNameable orderStockTag;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private DebitRequestService debitRequestService;

    @Autowired
    private DebitRequestDetailService debitRequestDetailService;

    @Autowired
    private PaymentGroupService paymentGroupService;

    @Autowired
    private PaymentGroupServiceService paymentGroupServiceService;


    @Secured("@")
    @PostConstruct
    public void init() {
        setDefaultControls();
    }

    private void setDefaultControls() {
        clearCache();
        try {
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(currentStaff.getShopId()), true, Const.OWNER_TYPE.SHOP);
            staff = currentStaff.getStaffCode();
            // Khoi tao Voffice
            writeOfficeTag.init(this, currentStaff.getShopId());
            lstType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.TYPE_DEBIT);
            //
            lstPaymentGroup = paymentGroupService.getLstPaymentGroup();
            lstPaymentGroupService = paymentGroupServiceService.getLstGroupService();
            lstDebitDayType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
            lstDebitLevel = optionSetValueService.getDefaultDebitLevel();
            lstDebitLevelConst = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_LEVEL);
            executeCommand("updateControls()");
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e.getErrorCode(), e.getKeyMsg());
            topPage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    private void clearCache() {
        debitRequestDTO = new DebitRequestDTO();
        debitRequestDtlDTO = new DebitRequestDetailDTO();
        stockDebitDTO = new StockDebitDTO();
        lstDebitDayType = Lists.newArrayList();
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        doRefresh();
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        typeInput = "1";
        if (tabIndex == 0) {
            limitCreateOrderType = "1";
        } else {
            limitCreateOrderType = "2";
            templateFileName = Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_STAFF;
            templatePath = FileUtil.getTemplatePath() + templateFileName;
            fileNameError = currentStaff.getStaffCode().toUpperCase() + "_" + Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_STAFF_ERROR;
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO shopDTO) {
        staffInfoTag.initStaff(this, shopDTO.getOwnerId());
        if (("1").equals(typeInput)) {
            debitRequestDtlDTO.setShopId(shopDTO.getOwnerId());
            debitRequestDtlDTO.setShopCode(shopDTO.getOwnerCode());
        }
    }

    @Secured("@")
    public void receiveClear() {
        debitRequestDtlDTO.setOwnerCode(null);
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO staffDTO) {
        debitRequestDtlDTO.setOwnerId(DataUtil.safeToLong(staffDTO.getOwnerId()));
        debitRequestDtlDTO.setOwnerCode(staffDTO.getOwnerCode());
    }

    @Secured("@")
    public void resetControl() {
        setDefaultControls();
    }

    public void validateAdd() throws LogicException, Exception {
        if (debitRequestDtlDTO.getDebitValue() == null && DataUtil.isNullOrZero(debitRequestDtlDTO.getPaymentGroupId())) {
            throw new LogicException("", "mn.stock.limit.stock.require");
        }

        //Neu la han muc cong no thi bat buoc nhap nhom nop tien
        if (!DataUtil.isNullOrZero(debitRequestDtlDTO.getPaymentGroupId()) && DataUtil.isNullOrZero(debitRequestDtlDTO.getPaymentGroupServiceId())) {
            throw new LogicException("", "mn.stock.limit.group.offerMoney.require.msg");
        }
        //Neu la han muc kho cho don vi thi ngay ap dung bat buoc nhap
        if (("2").equals(typeInput)) {
            if (!DataUtil.isNullOrZero(debitRequestDtlDTO.getDebitValue()) && DataUtil.isNullOrEmpty(debitRequestDtlDTO.getDebitDayType())) {
                throw new LogicException("", "mn.stock.limit.require.day.use.msg");
            }
            if (DataUtil.isNullOrZero(debitRequestDtlDTO.getDebitValue())) {
                debitRequestDtlDTO.setDebitDayType(null);
            }
        }
        if (DataUtil.isNullOrZero(debitRequestDtlDTO.getPaymentGroupId())) {
            debitRequestDtlDTO.setPaymentGroupServiceId(null);
        }

    }

    @Secured("@")
    public void doAddItem() {
        try {
            //validate du lieu dau vao
            validateAdd();

            DebitRequestDetailDTO debitRequestDtlAdd = new DebitRequestDetailDTO();
            if (("1").equals(typeInput)) {
                debitRequestDtlAdd.setShopId(debitRequestDtlDTO.getShopId());
                debitRequestDtlAdd.setShopCode(debitRequestDtlDTO.getShopCode());
                debitRequestDtlAdd.setOwnerId(debitRequestDtlDTO.getOwnerId());
                debitRequestDtlAdd.setOwnerCode(debitRequestDtlDTO.getOwnerCode());
                debitRequestDtlAdd.setDebitValue(debitRequestDtlDTO.getDebitValue());
                debitRequestDtlAdd.setOwnerType(Const.OWNER_TYPE.STAFF);
                //save tam
                debitRequestDtlAdd.setDebitDayType("1");
            }
            if (("2").equals(typeInput)) {
                debitRequestDtlAdd.setOwnerId(DataUtil.safeToLong(shopInfoTag.getvShopStaffDTO().getOwnerId()));
                debitRequestDtlAdd.setOwnerCode(shopInfoTag.getvShopStaffDTO().getOwnerCode());
                debitRequestDtlAdd.setOwnerType(Const.OWNER_TYPE.SHOP);
                debitRequestDtlAdd.setDebitDayType(debitRequestDtlDTO.getDebitDayType());
                debitRequestDtlAdd.setDebitValue(debitRequestDtlDTO.getDebitValue());
            }
            debitRequestDtlAdd.setDistance(debitRequestDtlDTO.getDistance());
            debitRequestDtlAdd.setPaymentGroupId(debitRequestDtlDTO.getPaymentGroupId());
            debitRequestDtlAdd.setPaymentGroupServiceId(debitRequestDtlDTO.getPaymentGroupServiceId());
            //
            debitRequestDtlAdd.setFinanceType(debitRequestDtlDTO.getFinanceType());
            debitRequestDtlAdd.setNote(debitRequestDtlDTO.getNote());
            debitRequestDtlAdd.setStaffId(currentStaff.getStaffId());
            boolean isDuplicate = false;
            if (isDuplicate == false) {
                // Check da ton tai yeu cau chua duoc duyet
                BaseMessage dtlValid = debitRequestDetailService.validate(debitRequestDtlAdd);
                if (dtlValid != null && !DataUtil.isNullOrEmpty(dtlValid.getErrorCode())) {
                    //isDuplicate = true;
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, dtlValid.getKeyMsg(), dtlValid.getParamsMsg());
                    topPage();
                    return;
                }
            }
            // Check duplicate nhan vien
            if (DataUtil.safeEqual(typeInput, "1")) {
                for (int i = 0; i < lstRequestDtl.size(); i++) {
                    DebitRequestDetailDTO rqd = lstRequestDtl.get(i);
                    if (DataUtil.safeEqual(debitRequestDtlAdd.getOwnerId(), rqd.getOwnerId())) {
                        isDuplicate = true;
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "mn.stock.limit.staff.msg.duplicate");
                        topPage();
                        break;
                    }
                }
            }
            // Check duplicate don vi
            if (DataUtil.safeEqual(typeInput, "2")) {
                for (int i = 0; i < lstRequestDtl.size(); i++) {
                    DebitRequestDetailDTO rqd = lstRequestDtl.get(i);
                    if (DataUtil.safeEqual(debitRequestDtlAdd.getOwnerId(), rqd.getOwnerId())
                            && (DataUtil.safeEqual(debitRequestDtlAdd.getDebitDayType(), rqd.getDebitDayType())
                            || (!DataUtil.isNullOrZero(debitRequestDtlAdd.getPaymentGroupId()) && !DataUtil.isNullOrZero(rqd.getPaymentGroupId())))) {
                        isDuplicate = true;
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "mn.stock.limit.shop.msg.duplicate");
                        topPage();
                        break;
                    }
                }
            }
            // Kiem tra so ban ghi khi them moi
            if (!isDuplicate) {
                if (lstRequestDtl.size() < maxRowImport("MAX_ROW_IMPORT_TYPE")) {
                    lstRequestDtl.add(debitRequestDtlAdd);
                } else {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, getTextParam("mn.stock.limit.createOrder.record.msg", "" + maxRowImport("MAX_ROW_IMPORT_TYPE")));
                    topPage();
                    return;
                }
            }
            staffInfoTag.initStaff(null, shopInfoTag.getvShopStaffDTO().getOwnerId());
            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(currentStaff.getShopId()), true, Const.OWNER_TYPE.SHOP);
            debitRequestDtlDTO = new DebitRequestDetailDTO();
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e.getErrorCode(), e.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }

    }

    @Secured("@")
    public String getLimitStockTypeName(String limitStockType) {
        String limitStockTypeConvert = "";
        if (!(limitStockType == null && lstDebitLevelConst.isEmpty())) {
            for (OptionSetValueDTO ops : lstDebitLevelConst) {
                if (DataUtil.safeEqual(ops.getValue(), limitStockType)) {
                    limitStockTypeConvert = ops.getName();
                    break;
                }
            }
        }
        return limitStockTypeConvert;
    }

    @Secured("@")
    public OptionSetValueDTO getDebitType(String debitLevel) {
        if (!(debitLevel == null && lstDebitLevel.isEmpty())) {
            for (OptionSetValueDTO ops : lstDebitLevel) {
                if (DataUtil.safeEqual(ops.getValue(), debitLevel)) {
                    return ops;
                }
            }
        }
        return null;
    }

    @Secured("@")
    public String getDebitAmount(String debitValue, String paymentType, Long paymentGroupId) {
        String debitDayTypeConvert = "";
        if (DataUtil.safeEqual(paymentType, Const.DEBIT_TYPE.DEBIT_STOCK)) {
            if (DataUtil.safeEqual(typeInput, "1")) {
                if (!(DataUtil.isNullOrEmpty(debitValue) && lstDebitLevel.isEmpty())) {
                    for (OptionSetValueDTO ops : lstDebitLevel) {
                        if (DataUtil.safeEqual(ops.getValue(), debitValue)) {
                            debitDayTypeConvert = ops.getName();
                            break;
                        }
                    }
                }
            } else {
                if (!DataUtil.isNullOrEmpty(debitValue)) {
                    String str = String.format("%,d", DataUtil.safeToLong(debitValue));
                    return str;
                }
            }
        } else {
            if (!(debitDayTypeConvert == null && lstPaymentGroup.isEmpty())) {
                for (PaymentGroupDTO paymentGroupDTO : lstPaymentGroup) {
                    if (DataUtil.safeEqual(paymentGroupDTO.getPaymentGroupId(), paymentGroupId)) {
                        debitDayTypeConvert = paymentGroupDTO.getName();
                        break;
                    }
                }
            }
        }
        return debitDayTypeConvert;
    }


    @Secured("@")
    public String getDebitDayTypeName(String debitDayType) {
        String debitDayTypeConvert = "";
        if (!(debitDayType == null && lstDebitDayType.isEmpty())) {
            for (OptionSetValueDTO ops : lstDebitDayType) {
                if (DataUtil.safeEqual(ops.getValue(), debitDayType)) {
                    debitDayTypeConvert = ops.getName();
                    break;
                }
            }
        }
        return debitDayTypeConvert;
    }

    @Secured("@")
    public String getFinanceTypeName(Long paymentGroupServiceId) {
        String financeTypeStr = "";
        if (!(paymentGroupServiceId == null && lstPaymentGroupService.isEmpty())) {
            for (PaymentGroupServiceDTO paymentGroupServiceDTO : lstPaymentGroupService) {
                if (DataUtil.safeEqual(paymentGroupServiceDTO.getId(), paymentGroupServiceId)) {
                    financeTypeStr = paymentGroupServiceDTO.getCode();
                    break;
                }
            }
        }
        return financeTypeStr;
    }


    @Secured("@")
    public void onCreateOrder() {
        try {
            List<DebitRequestDetailDTO> lstClone = Lists.newArrayList();
            debitRequestDTO = new DebitRequestDTO();
            //sign voffice
            SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            debitRequestDTO.setUserName(signOfficeDTO.getUserName());
            debitRequestDTO.setPassWord(signOfficeDTO.getPassWord());
            debitRequestDTO.setSignFlowId(signOfficeDTO.getSignFlowId());

            for (DebitRequestDetailDTO detailDTO : lstRequestDtl) {
                BaseMessage bmValid = debitRequestDetailService.validate(detailDTO);
                if (bmValid != null && !DataUtil.isNullOrEmpty(bmValid.getErrorCode())) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, bmValid.getKeyMsg(), bmValid.getParamsMsg());
                    topPage();
                    return;
                }
            }
            if (("1").equals(typeInput)) {
                debitRequestDTO.setRequestObjectType(Const.OWNER_TYPE.STAFF);
                for (DebitRequestDetailDTO detailDTO : lstRequestDtl) {
                    //hoangnt
                    if (!DataUtil.isNullOrZero(detailDTO.getDebitValue())) {
                        DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                        detail.setPaymentGroupId(null);
                        detail.setPaymentGroupServiceId(null);
                        detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_STOCK);
                        OptionSetValueDTO optionSetValueDebit = getDebitType(DataUtil.safeToString(detail.getDebitValue()));
                        String[] lstDebitDayType = optionSetValueDebit.getDescription().split(",");
                        if (lstDebitDayType.length > 1) {
                            detail.setDebitDayType(lstDebitDayType[0]);
                            DebitRequestDetailDTO detailClone = DataUtil.cloneBean(detail);
                            detailClone.setDebitDayType(lstDebitDayType[1]);
                            lstClone.add(detailClone);
                        } else {
                            detail.setDebitDayType(optionSetValueDebit.getDescription());
                        }
                        lstClone.add(detail);
                    }
                    if (!DataUtil.isNullOrZero(detailDTO.getPaymentGroupId())) {
                        //set name
                        DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                        detail.setDebitValue(null);
                        detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_PAYMENT);
                        PaymentGroupDTO paymentGroupDTO = paymentGroupService.findOne(detail.getPaymentGroupId());
                        detail.setPaymentGroupName(paymentGroupDTO.getName());
                        PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.findOne(detail.getPaymentGroupServiceId());
                        detail.setPaymentGroupServiceName(paymentGroupServiceDTO.getCode());
                        List<PaymentGroupDayTypeDTO> lstDayType = paymentGroupService.getLstDayTypeByPaymentGroupId(detail.getPaymentGroupId());
                        detail.setDebitDayType(DataUtil.safeToString(lstDayType.get(0).getDebitDayType()));
                        detail.setPaymentDebitValue(lstDayType.get(0).getMaxDebitMoney());
                        if (lstDayType.size() > 1) {
                            for (int i = 1; i < lstDayType.size(); i++) {
                                DebitRequestDetailDTO detailClone = DataUtil.cloneBean(detail);
                                detailClone.setDebitDayType(DataUtil.safeToString(lstDayType.get(i).getDebitDayType()));
                                detailClone.setPaymentDebitValue(lstDayType.get(i).getMaxDebitMoney());
                                lstClone.add(detailClone);
                            }
                        }
                        lstClone.add(detail);
                    }
                }
            } else {
                debitRequestDTO.setRequestObjectType(Const.OWNER_TYPE.SHOP);
                for (DebitRequestDetailDTO detailDTO : lstRequestDtl) {
                    if (!DataUtil.isNullOrZero(detailDTO.getDebitValue())) {
                        DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                        detail.setPaymentGroupId(null);
                        detail.setPaymentGroupServiceId(null);
                        detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_STOCK);
                        lstClone.add(detail);
                    }
                    if (!DataUtil.isNullOrZero(detailDTO.getPaymentGroupId())) {
                        //set name
                        DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                        detail.setDebitValue(null);
                        detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_PAYMENT);
                        PaymentGroupDTO paymentGroupDTO = paymentGroupService.findOne(detail.getPaymentGroupId());
                        detail.setPaymentGroupName(paymentGroupDTO.getName());
                        PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.findOne(detail.getPaymentGroupServiceId());
                        detail.setPaymentGroupServiceName(paymentGroupServiceDTO.getCode());
                        List<PaymentGroupDayTypeDTO> lstDayType = paymentGroupService.getLstDayTypeByPaymentGroupId(detail.getPaymentGroupId());
                        detail.setDebitDayType(DataUtil.safeToString(lstDayType.get(0).getDebitDayType()));
                        detail.setPaymentDebitValue(lstDayType.get(0).getMaxDebitMoney());
                        if (lstDayType.size() > 1) {
                            for (int i = 1; i < lstDayType.size(); i++) {
                                DebitRequestDetailDTO detailClone = DataUtil.cloneBean(detail);
                                detailClone.setDebitDayType(DataUtil.safeToString(lstDayType.get(i).getDebitDayType()));
                                detailClone.setPaymentDebitValue(lstDayType.get(i).getMaxDebitMoney());
                                lstClone.add(detailClone);
                            }
                        }
                        lstClone.add(detail);
                    }
                }

            }
//            lstClone.addAll(lstRequestDtl);
            debitRequestDTO.setStatus("1");
            debitRequestDTO.setCreateUserId(currentStaff.getStaffId());
            debitRequestDTO.setCreateUser(currentStaff.getStaffCode());
            debitRequestDTO.setDescription(description);
            debitRequestDTO.setDebitDebitRequestDetails(lstClone);

            BaseMessage bmCreate = debitRequestService.create(debitRequestDTO);
            if (bmCreate != null) {
                if (bmCreate.isSuccess()) {
                    reportSuccess("", bmCreate.getKeyMsg(), bmCreate.getParamsMsg());
                    topPage();
                    doRefresh();
                } else {
                    throw new LogicException(bmCreate.getErrorCode(), bmCreate.getKeyMsg(), bmCreate.getParamsMsg());
                }
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private boolean validateImportFile(UploadedFile fileInput) {
        if (DataUtil.isNullObject(fileInput)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.limit.createFile.require.msg");
            return false;
        }
        File importFile = new File(fileInput.getFileName());
        String fileName = importFile.getName();
        String[] fileNameArr = fileName.split("\\.");
        if (DataUtil.isNullObject(fileNameArr) || fileNameArr.length != 2) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.importFile.msg.invalid.name");
            return false;
        }
        String extensionFileName = fileNameArr[1];
        if (!"xls".equalsIgnoreCase(extensionFileName) && !"xlsx".equalsIgnoreCase(extensionFileName)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.importFile.msg.invalid.extention");
            return false;
        }
        return true;
    }

    /**
     * So ban ghi duoc phep import
     *
     * @param rowCode
     * @return
     * @throws Exception
     */
    private int maxRowImport(String rowCode) throws Exception {
        List<OptionSetValueDTO> lstOpt = optionSetValueService.getByOptionSetCode(rowCode.trim());
        if (DataUtil.isNullOrEmpty(lstOpt)) {
            return 0;
        } else {
            return Integer.parseInt(lstOpt.get(0).getValue());
        }
    }

    /**
     * Lap yeu cau han muc theo file
     *
     * @param event
     */
    @Secured("@")
    public void handleFileImport(FileUploadEvent event) {
        try {
            bookError = null;
            if (!DataUtil.isNullObject(event)) {
                fileImport = event.getFile();
                BaseMessage message = validateFileUploadWhiteList(fileImport, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setParamsMsg(message.getParamsMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                importFileName = new String(event.getFile().getFileName().getBytes(), "UTF-8");
            }
            if (validateImportFile(fileImport)) {
                b = true;
                showWhenError = false;
                boolean isValidTemplate = false;
                lstRequestDtl = Lists.newArrayList();
                InputStream inputStream = event.getFile().getInputstream();
                Cell cellStt;
                Cell cellOwner;
                Cell cellDebitValue;
                Cell cellDebitDayType;
                Cell cellPaymentGroup;
                Cell cellPaymentGroupService;
                Cell cellDistance;
                Cell cellNote;
                Workbook workbook;
                if (StringUtils.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileImport.getContentType())) {
                    workbook = new XSSFWorkbook(inputStream);
                } else {
                    workbook = new HSSFWorkbook(inputStream);
                }
                Sheet firstSheet = workbook.getSheetAt(0);
                // Check dinh dang file
                for (Row rowFirst : firstSheet) {
                    if (("1").equals(typeInput) && !DataUtil.isNullObject(rowFirst.getCell(0))) {
                        cellStt = rowFirst.getCell(0);
                        if (!DataUtil.isNullObject(cellStt)) {
                            cellStt.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellOwner = rowFirst.getCell(1);
                        if (!DataUtil.isNullObject(cellOwner)) {
                            cellOwner.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellDebitValue = rowFirst.getCell(2);
                        if (!DataUtil.isNullObject(cellDebitValue)) {
                            cellDebitValue.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellPaymentGroup = rowFirst.getCell(3);
                        if (!DataUtil.isNullObject(cellPaymentGroup)) {
                            cellPaymentGroup.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellPaymentGroupService = rowFirst.getCell(4);
                        if (!DataUtil.isNullObject(cellPaymentGroupService)) {
                            cellPaymentGroupService.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellDistance = rowFirst.getCell(5);
                        if (!DataUtil.isNullObject(cellDistance)) {
                            cellDistance.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellNote = rowFirst.getCell(6);
                        if (!DataUtil.isNullObject(cellNote)) {
                            cellNote.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        if (getText("mn.stock.limit.template.stt").trim().equals(cellStt.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.staff.code").trim().equals(cellOwner.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.stock.code").trim().equals(cellDebitValue.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.payment.group").trim().equals(cellPaymentGroup.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.payment.service").trim().equals(cellPaymentGroupService.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.distance").trim().equals(cellDistance.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.note").trim().equals(cellNote.getStringCellValue().trim())) {
                            isValidTemplate = true;
                            break;
                        } else {
                            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.importFile.msg.invalid.format");
                            topPage();
                            return;
                        }
                    }
                    if (("2").equals(typeInput) && !DataUtil.isNullObject(rowFirst.getCell(0))) {
                        cellStt = rowFirst.getCell(0);
                        if (!DataUtil.isNullObject(cellStt)) {
                            cellStt.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellOwner = rowFirst.getCell(1);
                        if (!DataUtil.isNullObject(cellOwner)) {
                            cellOwner.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellDebitValue = rowFirst.getCell(2);
                        if (!DataUtil.isNullObject(cellDebitValue)) {
                            cellDebitValue.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellDebitDayType = rowFirst.getCell(3);
                        if (!DataUtil.isNullObject(cellDebitDayType)) {
                            cellDebitDayType.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellPaymentGroup = rowFirst.getCell(4);
                        if (!DataUtil.isNullObject(cellPaymentGroup)) {
                            cellPaymentGroup.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellPaymentGroupService = rowFirst.getCell(5);
                        if (!DataUtil.isNullObject(cellPaymentGroupService)) {
                            cellPaymentGroupService.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellDistance = rowFirst.getCell(6);
                        if (!DataUtil.isNullObject(cellDistance)) {
                            cellDistance.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        cellNote = rowFirst.getCell(7);
                        if (!DataUtil.isNullObject(cellNote)) {
                            cellNote.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        if (getText("mn.stock.limit.template.stt").trim().equals(cellStt.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.shop.code").trim().equals(cellOwner.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.stock").trim().equals(cellDebitValue.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.day.use.code").trim().equals(cellDebitDayType.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.payment.group").trim().equals(cellPaymentGroup.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.payment.service").trim().equals(cellPaymentGroupService.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.limit.distance").trim().equals(cellDistance.getStringCellValue().trim())
                                && getText("mn.stock.limit.template.note").trim().equals(cellNote.getStringCellValue().trim())) {
                            isValidTemplate = true;
                            break;
                        } else {
                            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.importFile.msg.invalid.format");
                            topPage();
                            return;
                        }
                    }
                }

                // Lap yeu cau han muc kho theo file
                if (isValidTemplate) {
                    if (("1").equals(typeInput)) {
                        int count = 0;
                        for (Row row : firstSheet) {
                            count++;
                            if (count == 1) {
                                continue;
                            }
                            cellStt = row.getCell(0);
                            if (!DataUtil.isNullObject(cellStt)) {
                                cellStt.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellOwner = row.getCell(1);
                            if (!DataUtil.isNullObject(cellOwner)) {
                                cellOwner.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellDebitValue = row.getCell(2);
                            if (!DataUtil.isNullObject(cellDebitValue)) {
                                cellDebitValue.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellPaymentGroup = row.getCell(3);
                            if (!DataUtil.isNullObject(cellPaymentGroup)) {
                                cellPaymentGroup.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellPaymentGroupService = row.getCell(4);
                            if (!DataUtil.isNullObject(cellPaymentGroupService)) {
                                cellPaymentGroupService.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellDistance = row.getCell(5);
                            if (!DataUtil.isNullObject(cellDistance)) {
                                cellDistance.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellNote = row.getCell(6);
                            if (!DataUtil.isNullObject(cellNote)) {
                                cellNote.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            if (DataUtil.isNullOrEmpty(cellStt.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellOwner.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellDebitValue.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellPaymentGroup.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellPaymentGroupService.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellDistance.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellNote.getStringCellValue())) {
                                continue;
                            }
                            DebitRequestDetailDTO debitRequestDtlStaff = new DebitRequestDetailDTO();
                            // STT
                            debitRequestDtlStaff.setOwnerType(Const.OWNER_TYPE.STAFF);
                            debitRequestDtlStaff.setStt(cellStt.getStringCellValue().trim());
                            // Ma nhan vien
                            if (DataUtil.isNullOrEmpty(cellOwner.getStringCellValue().trim())) {
                                debitRequestDtlStaff.setOwnerId(null);
                                debitRequestDtlStaff.setOwnerCode(cellOwner.getStringCellValue().trim());
                            } else {
                                StaffDTO staff = staffService.getStaffByStaffCode(cellOwner.getStringCellValue().toUpperCase().trim());
                                if (DataUtil.isNullObject(staff)) {
                                    debitRequestDtlStaff.setOwnerId(-1L);
                                    debitRequestDtlStaff.setOwnerCode(cellOwner.getStringCellValue().trim());
                                } else {
                                    debitRequestDtlStaff.setOwnerId(staff.getStaffId());
                                    debitRequestDtlStaff.setOwnerCode(staff.getStaffCode());
                                }
                            }
                            // Ma han muc kho hang
                            if (!DataUtil.isNullOrEmpty(cellDebitValue.getStringCellValue())) {
                                String value = optionSetValueService.getValueByTwoCodeOption("DEBIT_LEVEL", cellDebitValue.getStringCellValue().trim());
                                if (!DataUtil.isNullOrEmpty(value)) {
                                    debitRequestDtlStaff.setDebitValue(DataUtil.safeToLong(value));
                                } else {
                                    debitRequestDtlStaff.setDebitValue(0L);
                                    debitRequestDtlStaff.setOldDebitValue(cellDebitValue.getStringCellValue().trim());
                                }
                            } else {
                                debitRequestDtlStaff.setDebitValue(null);
                            }

                            //save tam
                            debitRequestDtlStaff.setDebitDayType("1");
                            if (!DataUtil.isNullOrEmpty(cellPaymentGroup.getStringCellValue())) {
                                // Ma han muc cong no
                                debitRequestDtlStaff.setPaymentGroupCode(cellPaymentGroup.getStringCellValue().trim());
                                if (DataUtil.isNullOrEmpty(cellPaymentGroup.getStringCellValue().trim())) {
                                    debitRequestDtlStaff.setPaymentGroupId(null);
                                } else {
                                    PaymentGroupDTO paymentGroupDTO = paymentGroupService.getPaymentGroupByName(cellPaymentGroup.getStringCellValue().trim());
                                    if (DataUtil.isNullObject(paymentGroupDTO)) {
                                        debitRequestDtlStaff.setPaymentGroupId(-1L);
                                    } else {
                                        debitRequestDtlStaff.setPaymentGroupId(paymentGroupDTO.getPaymentGroupId());
                                    }
                                }
                                // Ma nhom nop tien
                                if (DataUtil.isNullObject(cellPaymentGroupService) || DataUtil.isNullOrEmpty(cellPaymentGroupService.getStringCellValue().trim())) {
                                    debitRequestDtlStaff.setPaymentGroupServiceId(null);
                                } else {
                                    debitRequestDtlStaff.setPaymentGroupServiceCode(cellPaymentGroupService.getStringCellValue().trim());
                                    PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.getPaymentGroupServiceByName(cellPaymentGroupService.getStringCellValue().trim());
                                    if (DataUtil.isNullObject(paymentGroupServiceDTO)) {
                                        debitRequestDtlStaff.setPaymentGroupServiceId(-1L);
                                    } else {
                                        debitRequestDtlStaff.setPaymentGroupServiceId(paymentGroupServiceDTO.getId());
                                    }
                                }
                            }
                            // Khoang cach
                            debitRequestDtlStaff.setDistance(DataUtil.safeToDouble(cellDistance.getStringCellValue().trim()));
                            debitRequestDtlStaff.setDistanceStr(cellDistance.getStringCellValue().trim());

                            // Ghi chu
                            debitRequestDtlStaff.setNote(cellNote.getStringCellValue().trim());
                            //
                            debitRequestDtlStaff.setStaffId(currentStaff.getStaffId());
                            debitRequestDtlStaff.setOwnerType(Const.OWNER_TYPE.STAFF);
                            if (!DataUtil.isNullObject(debitRequestDtlStaff)) {
                                lstRequestDtl.add(debitRequestDtlStaff);
                            }
                        }
                    }

                    if (("2").equals(typeInput)) {
                        int count = 0;
                        for (Row row : firstSheet) {
                            count++;
                            if (count == 1) {
                                continue;
                            }
                            cellStt = row.getCell(0);
                            if (!DataUtil.isNullObject(cellStt)) {
                                cellStt.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellOwner = row.getCell(1);
                            if (!DataUtil.isNullObject(cellOwner)) {
                                cellOwner.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellDebitValue = row.getCell(2);
                            if (!DataUtil.isNullObject(cellDebitValue)) {
                                cellDebitValue.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellDebitDayType = row.getCell(3);
                            if (!DataUtil.isNullObject(cellDebitDayType)) {
                                cellDebitDayType.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellPaymentGroup = row.getCell(4);
                            if (!DataUtil.isNullObject(cellPaymentGroup)) {
                                cellPaymentGroup.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellPaymentGroupService = row.getCell(5);
                            if (!DataUtil.isNullObject(cellPaymentGroupService)) {
                                cellPaymentGroupService.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellDistance = row.getCell(6);
                            if (!DataUtil.isNullObject(cellDistance)) {
                                cellDistance.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            cellNote = row.getCell(7);
                            if (!DataUtil.isNullObject(cellNote)) {
                                cellNote.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            if (DataUtil.isNullOrEmpty(cellStt.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellOwner.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellDebitValue.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellDebitDayType.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellPaymentGroup.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellPaymentGroupService.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellDistance.getStringCellValue())
                                    && DataUtil.isNullOrEmpty(cellNote.getStringCellValue())) {
                                continue;
                            }
                            DebitRequestDetailDTO debitRequestDtlShop = new DebitRequestDetailDTO();
                            // STT
                            debitRequestDtlShop.setStt(cellStt.getStringCellValue().trim());
                            debitRequestDtlShop.setOwnerType(Const.OWNER_TYPE.SHOP);
                            // Ma cua hang
                            if (DataUtil.isNullOrEmpty(cellOwner.getStringCellValue().trim())) {
                                debitRequestDtlShop.setOwnerId(null);
                                debitRequestDtlShop.setOwnerCode(cellOwner.getStringCellValue().trim());
                            } else {
                                ShopDTO shop = shopService.getShopByCodeAndActiveStatus(cellOwner.getStringCellValue().toUpperCase().trim());
                                if (DataUtil.isNullObject(shop)) {
                                    debitRequestDtlShop.setOwnerId(-1L);
                                    debitRequestDtlShop.setOwnerCode(cellOwner.getStringCellValue().trim());
                                } else {
                                    debitRequestDtlShop.setOwnerId(shop.getShopId());
                                    debitRequestDtlShop.setOwnerCode(shop.getShopCode());
                                }
                            }
                            // Ma han muc kho hang
                            if (!DataUtil.isNullOrEmpty(cellDebitValue.getStringCellValue())) {
                                if (DataUtil.safeToLong(cellDebitValue.getStringCellValue().trim()) > 0L) {
                                    debitRequestDtlShop.setDebitValue(DataUtil.safeToLong(cellDebitValue.getStringCellValue().trim()));
                                } else {
                                    debitRequestDtlShop.setDebitValue(0L);
                                    debitRequestDtlShop.setOldDebitValue(cellDebitValue.getStringCellValue().trim());
                                }
                            } else {
                                debitRequestDtlShop.setDebitValue(null);
                            }

                            // Ma ngay ap dung
                            debitRequestDtlShop.setDebitDayType(cellDebitDayType.getStringCellValue().trim());
                            //
                            if (!DataUtil.isNullOrEmpty(cellPaymentGroup.getStringCellValue())) {
                                // Ma han muc cong no
                                debitRequestDtlShop.setPaymentGroupCode(cellPaymentGroup.getStringCellValue().trim());
                                if (DataUtil.isNullOrEmpty(cellPaymentGroup.getStringCellValue().trim())) {
                                    debitRequestDtlShop.setPaymentGroupId(null);
                                } else {
                                    PaymentGroupDTO paymentGroupDTO = paymentGroupService.getPaymentGroupByName(cellPaymentGroup.getStringCellValue().trim());
                                    if (DataUtil.isNullObject(paymentGroupDTO)) {
                                        debitRequestDtlShop.setPaymentGroupId(-1L);
                                    } else {
                                        debitRequestDtlShop.setPaymentGroupId(paymentGroupDTO.getPaymentGroupId());
                                    }
                                }
                                // Ma nhom nop tien
                                debitRequestDtlShop.setPaymentGroupServiceCode(cellPaymentGroupService.getStringCellValue().trim());
                                if (DataUtil.isNullOrEmpty(cellPaymentGroupService.getStringCellValue().trim())) {
                                    debitRequestDtlShop.setPaymentGroupServiceId(null);
                                } else {
                                    PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.getPaymentGroupServiceByName(cellPaymentGroupService.getStringCellValue().trim());
                                    if (DataUtil.isNullObject(paymentGroupServiceDTO)) {
                                        debitRequestDtlShop.setPaymentGroupServiceId(-1L);
                                    } else {
                                        debitRequestDtlShop.setPaymentGroupServiceId(paymentGroupServiceDTO.getId());
                                    }
                                }
                            }
                            // Khoang cach
                            debitRequestDtlShop.setDistance(DataUtil.safeToDouble(cellDistance.getStringCellValue().trim()));
                            debitRequestDtlShop.setDistanceStr(cellDistance.getStringCellValue().trim());
                            // Ghi chu
                            debitRequestDtlShop.setNote(cellNote.getStringCellValue().trim());
                            //
                            debitRequestDtlShop.setStaffId(currentStaff.getStaffId());
                            debitRequestDtlShop.setOwnerType(Const.OWNER_TYPE.SHOP);
                            if (!DataUtil.isNullObject(debitRequestDtlShop)) {
                                lstRequestDtl.add(debitRequestDtlShop);
                            }
                        }
                    }
                }
                inputStream.close();
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void onCreateOrderFile() {
        try {
            BaseMessage message = validateFileUploadWhiteList(fileImport, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (DataUtil.isNullObject(fileImport)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.limit.importFile.msg.require");
                topPage();
            }
            showWhenError = false;
            // Check tong so ban ghi
            int maxRow = maxRowImport("MAX_ROW_IMPORT_TYPE");
            if (lstRequestDtl.size() > maxRow) {
                b = false;
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.importFile.msg.record", maxRow);
                topPage();
                return;
            } else {
                // Check null + validate
                BaseMessage bmValid;
                if (DataUtil.isNullOrEmpty(lstRequestDtl)) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.limit.view.list.order.required");
                    topPage();
                    return;
                } else {
                    for (DebitRequestDetailDTO detailDTO : lstRequestDtl) {
                        bmValid = debitRequestDetailService.validate(detailDTO);
                        if (!DataUtil.isNullObject(bmValid) && !DataUtil.isNullOrEmpty(bmValid.getErrorCode())) {
                            detailDTO.setResult(getTextParam(bmValid.getKeyMsg(), bmValid.getParamsMsg()));
                            b = false;
                        }
                    }
                    // Check duplicate nhan vien
                    if (DataUtil.safeEqual(typeInput, "1")) {
                        for (int i = 0; i < lstRequestDtl.size() - 1; i++) {
                            DebitRequestDetailDTO rqd = lstRequestDtl.get(i);
                            for (int j = i + 1; j < lstRequestDtl.size(); j++) {
                                DebitRequestDetailDTO debitRequestDtlAdd = lstRequestDtl.get(j);
                                if (DataUtil.safeEqual(debitRequestDtlAdd.getOwnerId(), rqd.getOwnerId())) {
                                    rqd.setResult(getText("mn.stock.limit.staff.msg.duplicate"));
                                    b = false;
                                    break;
                                }
                            }
                        }
                    }
                    // Check duplicate don vi
                    if (DataUtil.safeEqual(typeInput, "2")) {
                        for (int i = 0; i < lstRequestDtl.size() - 1; i++) {
                            DebitRequestDetailDTO rqd = lstRequestDtl.get(i);
                            for (int j = i + 1; j < lstRequestDtl.size(); j++) {
                                DebitRequestDetailDTO debitRequestDtlAdd = lstRequestDtl.get(j);
                                if (DataUtil.safeEqual(debitRequestDtlAdd.getOwnerId(), rqd.getOwnerId())
                                        && (DataUtil.safeEqual(debitRequestDtlAdd.getDebitDayType(), rqd.getDebitDayType())
                                        || (!DataUtil.isNullOrZero(debitRequestDtlAdd.getPaymentGroupId()) && !DataUtil.isNullOrZero(rqd.getPaymentGroupId())))) {
                                    rqd.setResult(getText("mn.stock.limit.shop.msg.duplicate"));
                                    b = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (b == false) {
                    bookError = exportFileError("mn.stock.limit.msg.invalid.data");
                }
            }
            // Check Debit request

            if (b == true) {
                List<DebitRequestDetailDTO> lstClone = Lists.newArrayList();
                debitRequestDTO = new DebitRequestDTO();
                //sign voffice
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                debitRequestDTO.setUserName(signOfficeDTO.getUserName());
                debitRequestDTO.setPassWord(signOfficeDTO.getPassWord());
                debitRequestDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                //hoangnt
                if (("1").equals(typeInput)) {
                    debitRequestDTO.setRequestObjectType(Const.OWNER_TYPE.STAFF);
                    for (DebitRequestDetailDTO detailDTO : lstRequestDtl) {
                        //hoangnt
                        if (!DataUtil.isNullOrZero(detailDTO.getDebitValue())) {
                            DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                            detail.setPaymentGroupId(null);
                            detail.setPaymentGroupServiceId(null);
                            detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_STOCK);
                            OptionSetValueDTO optionSetValueDebit = getDebitType(DataUtil.safeToString(detail.getDebitValue()));
                            String[] lstDebitDayType = optionSetValueDebit.getDescription().split(",");
                            if (lstDebitDayType.length > 1) {
                                detail.setDebitDayType(lstDebitDayType[0]);
                                DebitRequestDetailDTO detailClone = DataUtil.cloneBean(detail);
                                detailClone.setDebitDayType(lstDebitDayType[1]);
                                lstClone.add(detailClone);
                            } else {
                                detail.setDebitDayType(optionSetValueDebit.getDescription());
                            }
                            lstClone.add(detail);
                        }
                        if (!DataUtil.isNullOrZero(detailDTO.getPaymentGroupId())) {
                            //set name
                            DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                            detail.setDebitValue(null);
                            detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_PAYMENT);
                            PaymentGroupDTO paymentGroupDTO = paymentGroupService.findOne(detail.getPaymentGroupId());
                            detail.setPaymentGroupName(paymentGroupDTO.getName());
                            PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.findOne(detail.getPaymentGroupServiceId());
                            detail.setPaymentGroupServiceName(paymentGroupServiceDTO.getCode());
                            List<PaymentGroupDayTypeDTO> lstDayType = paymentGroupService.getLstDayTypeByPaymentGroupId(detail.getPaymentGroupId());
                            detail.setDebitDayType(DataUtil.safeToString(lstDayType.get(0).getDebitDayType()));
                            detail.setPaymentDebitValue(lstDayType.get(0).getMaxDebitMoney());
                            if (lstDayType.size() > 1) {
                                for (int i = 1; i < lstDayType.size(); i++) {
                                    DebitRequestDetailDTO detailClone = DataUtil.cloneBean(detail);
                                    detailClone.setDebitDayType(DataUtil.safeToString(lstDayType.get(i).getDebitDayType()));
                                    detailClone.setPaymentDebitValue(lstDayType.get(i).getMaxDebitMoney());
                                    lstClone.add(detailClone);
                                }
                            }
                            lstClone.add(detail);
                        }
                    }
                } else {
                    debitRequestDTO.setRequestObjectType(Const.OWNER_TYPE.SHOP);
                    for (DebitRequestDetailDTO detailDTO : lstRequestDtl) {
                        if (!DataUtil.isNullOrZero(detailDTO.getDebitValue())) {
                            DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                            detail.setPaymentGroupId(null);
                            detail.setPaymentGroupServiceId(null);
                            detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_STOCK);
                            lstClone.add(detail);
                        }
                        if (!DataUtil.isNullOrZero(detailDTO.getPaymentGroupId())) {
                            //set name
                            DebitRequestDetailDTO detail = DataUtil.cloneBean(detailDTO);
                            detail.setDebitValue(null);
                            detail.setPaymentType(Const.DEBIT_TYPE.DEBIT_PAYMENT);
                            PaymentGroupDTO paymentGroupDTO = paymentGroupService.findOne(detail.getPaymentGroupId());
                            detail.setPaymentGroupName(paymentGroupDTO.getName());
                            PaymentGroupServiceDTO paymentGroupServiceDTO = paymentGroupServiceService.findOne(detail.getPaymentGroupServiceId());
                            detail.setPaymentGroupServiceName(paymentGroupServiceDTO.getCode());
                            List<PaymentGroupDayTypeDTO> lstDayType = paymentGroupService.getLstDayTypeByPaymentGroupId(detail.getPaymentGroupId());
                            detail.setDebitDayType(DataUtil.safeToString(lstDayType.get(0).getDebitDayType()));
                            detail.setPaymentDebitValue(lstDayType.get(0).getMaxDebitMoney());
                            if (lstDayType.size() > 1) {
                                for (int i = 1; i < lstDayType.size(); i++) {
                                    DebitRequestDetailDTO detailClone = DataUtil.cloneBean(detail);
                                    detailClone.setDebitDayType(DataUtil.safeToString(lstDayType.get(i).getDebitDayType()));
                                    detailClone.setPaymentDebitValue(lstDayType.get(i).getMaxDebitMoney());
                                    lstClone.add(detailClone);
                                }
                            }
                            lstClone.add(detail);
                        }
                    }

                }
//            lstClone.addAll(lstRequestDtl);
//                debitRequestDTO.setFileName(fileUpload.getFileName());
//                debitRequestDTO.setFileContent(byteContent);
                debitRequestDTO.setStatus("1");
                debitRequestDTO.setCreateUser(currentStaff.getStaffCode());
                debitRequestDTO.setDescription(descriptionFile);
                debitRequestDTO.setDebitDebitRequestDetails(lstClone);

                BaseMessage bmCreate = debitRequestService.create(debitRequestDTO);
                if (bmCreate != null) {
                    if (bmCreate.isSuccess()) {
                        reportSuccess("", bmCreate.getKeyMsg(), bmCreate.getParamsMsg());
                        topPage();
                        doRefresh();
                    } else {
                        throw new LogicException(bmCreate.getErrorCode(), bmCreate.getKeyMsg(), bmCreate.getParamsMsg());
                    }
                }
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }

    }

    private Workbook exportExcel(String fileNameTemplate, List<DebitRequestDetailDTO> lstDTO) throws FileNotFoundException {
//        FileExportBean bean = new FileExportBean();
//        bean.setTempalatePath(FileUtil.getTemplatePath());
//        bean.setTemplateName(fileNameTemplate);
//        bean.setOutName(FileUtil.getOutputPath() + currentStaff.getStaffCode().toUpperCase() + "_" + fileNameTemplate);

        FileInputStream fis = new FileInputStream(new File(FileUtil.getTemplatePath() + fileNameTemplate));
        Workbook wb;
        try {
            if (fileNameTemplate.endsWith(".xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                wb = new HSSFWorkbook(new POIFSFileSystem(fis));
            }
//            wb = FileUtil.exportWorkBook(bean);
            Sheet sheet = wb.getSheet("Sheet1");
            for (int i = 0; i < lstDTO.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> lstStr = Lists.newArrayList();
                if (("1").equals(typeInput)) {
                    lstStr.add(lstDTO.get(i).getStt());
                    lstStr.add(lstDTO.get(i).getOwnerCode());
                    lstStr.add(DataUtil.safeToString(lstDTO.get(i).getOldDebitValue()));
                    lstStr.add(lstDTO.get(i).getPaymentGroupCode());
                    lstStr.add(lstDTO.get(i).getPaymentGroupServiceCode());
                    lstStr.add(lstDTO.get(i).getDistanceStr().toString());
                    lstStr.add(lstDTO.get(i).getNote());
                    lstStr.add(lstDTO.get(i).getResult());
                    for (int j = 0; j < lstStr.size(); j++) {
                        row.createCell(j).setCellValue(lstStr.get(j));
                    }
                }
                if (("2").equals(typeInput)) {
                    lstStr.add(lstDTO.get(i).getStt());
                    lstStr.add(lstDTO.get(i).getOwnerCode());
                    lstStr.add(DataUtil.safeToString(lstDTO.get(i).getOldDebitValue()));
                    lstStr.add(lstDTO.get(i).getDebitDayType());
                    lstStr.add(lstDTO.get(i).getPaymentGroupCode());
                    lstStr.add(lstDTO.get(i).getPaymentGroupServiceCode());
                    lstStr.add(lstDTO.get(i).getDistanceStr().toString());
                    lstStr.add(lstDTO.get(i).getNote());
                    lstStr.add(lstDTO.get(i).getResult());
                    for (int j = 0; j < lstStr.size(); j++) {
                        row.createCell(j).setCellValue(lstStr.get(j));
                    }
                }
            }

            return wb;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return null;
    }

    private Workbook exportFileError(String keyMsg) {
        String fileNameTemplate = "";
        Workbook wb;
        try {
            if (("1").equals(typeInput)) {
                fileNameTemplate = Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_STAFF_ERROR;
                if (!DataUtil.isNullOrEmpty(keyMsg)) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "mn.stock.limit.msg.invalid.data");
                    topPage();
                }
            } else {
                fileNameTemplate = Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_SHOP_ERROR;
                if (!DataUtil.isNullOrEmpty(keyMsg)) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "mn.stock.limit.msg.invalid.data");
                    topPage();
                }
            }
            // Tra ve file
            wb = exportExcel(fileNameTemplate, lstRequestDtl);
            if (outPath != null) {
                showWhenError = true;
            }
            return wb;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    @Secured("@")
    public StreamedContent downloadErrorFile() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bookError.write(outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return new DefaultStreamedContent(inputStream, FacesContext.getCurrentInstance().getExternalContext().getMimeType(fileNameError), fileNameError);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }


    @Secured("@")
    public StreamedContent downloadTemplateFile() {
        try {
            InputStream streamTemp = new FileInputStream(new File(templatePath));
            return new DefaultStreamedContent(streamTemp, "application/xls", templateFileName);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    @Secured("@")
    public void doRemoveItem(int index) {
        lstRequestDtl.remove(index);
    }

    @Secured("@")
    public void doRefresh() {
        showWhenError = false;
        fileImport = null;
        importFileName = "";
        description = "";
        descriptionFile = "";
        shopInfoTag.resetShop();
        staffInfoTag.resetProduct();
        debitRequestDTO = new DebitRequestDTO();
        debitRequestDtlDTO = new DebitRequestDetailDTO();
        lstRequestDtl = Lists.newArrayList();
        lstStockDebit = Lists.newArrayList();
        lstDebitDayType = Lists.newArrayList();
        if (("1").equals(typeInput)) {
            templateFileName = Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_STAFF;
            templatePath = FileUtil.getTemplatePath() + templateFileName;
            fileNameError = currentStaff.getStaffCode().toUpperCase() + "_" + Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_STAFF_ERROR;
        }
        if (("2").equals(typeInput)) {
            templateFileName = Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_SHOP;
            templatePath = FileUtil.getTemplatePath() + templateFileName;
            fileNameError = currentStaff.getStaffCode().toUpperCase() + "_" + Const.LIMIT_STOCK.TEMPLATE_CREATE_ORDER_SHOP_ERROR;
        }
        try {
            lstDebitDayType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.DEBIT_DAY_TYPE);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }

    }

    @Secured("@")
    public void onValidateConfirm() {
        if (DataUtil.isNullObject(fileImport)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.limit.importFile.msg.require");
            topPage();
            return;
        }

        if (DataUtil.isNullOrEmpty(lstRequestDtl)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.limit.msg.empty.data");
            topPage();
            return;
        }
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public StaffDTO getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(StaffDTO currentStaff) {
        this.currentStaff = currentStaff;
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }


    public String getTypeInput() {
        return typeInput;
    }

    public void setTypeInput(String typeInput) {
        this.typeInput = typeInput;
    }

    public List<OptionSetValueDTO> getLstDebitDayType() {
        return lstDebitDayType;
    }

    public void setLstDebitDayType(List<OptionSetValueDTO> lstDebitDayType) {
        this.lstDebitDayType = lstDebitDayType;
    }

    public List<OptionSetValueDTO> getLstDebitLevel() {
        return lstDebitLevel;
    }

    public void setLstDebitLevel(List<OptionSetValueDTO> lstDebitLevel) {
        this.lstDebitLevel = lstDebitLevel;
    }

    public List<OptionSetValueDTO> getLstFinanceType() {
        return lstFinanceType;
    }

    public void setLstFinanceType(List<OptionSetValueDTO> lstFinanceType) {
        this.lstFinanceType = lstFinanceType;
    }

    public List<DebitRequestDetailDTO> getLstRequestDtl() {
        return lstRequestDtl;
    }

    public void setLstRequestDtl(List<DebitRequestDetailDTO> lstRequestDtl) {
        this.lstRequestDtl = lstRequestDtl;
    }

    public List<ShopDTO> getLstShopDTO() {
        return lstShopDTO;
    }

    public void setLstShopDTO(List<ShopDTO> lstShopDTO) {
        this.lstShopDTO = lstShopDTO;
    }

    public DebitRequestDTO getDebitRequestDTO() {
        return debitRequestDTO;
    }

    public void setDebitRequestDTO(DebitRequestDTO debitRequestDTO) {
        this.debitRequestDTO = debitRequestDTO;
    }

    public DebitRequestDetailDTO getDebitRequestDtlDTO() {
        return debitRequestDtlDTO;
    }

    public void setDebitRequestDtlDTO(DebitRequestDetailDTO debitRequestDtlDTO) {
        this.debitRequestDtlDTO = debitRequestDtlDTO;
    }

    public List<StaffDTO> getLstStaffDTO() {
        return lstStaffDTO;
    }

    public void setLstStaffDTO(List<StaffDTO> lstStaffDTO) {
        this.lstStaffDTO = lstStaffDTO;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }

    public List<StockDebitDTO> getLstStockDebit() {
        return lstStockDebit;
    }

    public void setLstStockDebit(List<StockDebitDTO> lstStockDebit) {
        this.lstStockDebit = lstStockDebit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UploadedFile getFileImport() {
        return fileImport;
    }

    public void setFileImport(UploadedFile fileImport) {
        this.fileImport = fileImport;
    }

    public StreamedContent getErrorFile() {
        return errorFile;
    }

    public void setErrorFile(StreamedContent errorFile) {
        this.errorFile = errorFile;
    }

    public boolean isShowWhenError() {
        return showWhenError;
    }

    public void setShowWhenError(boolean showWhenError) {
        this.showWhenError = showWhenError;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getLimitCreateOrderType() {
        return limitCreateOrderType;
    }

    public void setLimitCreateOrderType(String limitCreateOrderType) {
        this.limitCreateOrderType = limitCreateOrderType;
    }

    public String getDescriptionFile() {
        return descriptionFile;
    }

    public void setDescriptionFile(String descriptionFile) {
        this.descriptionFile = descriptionFile;
    }

    public Boolean isWriteOffice() {
        return writeOffice;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public List<OptionSetValueDTO> getLstType() {
        return lstType;
    }

    public void setLstType(List<OptionSetValueDTO> lstType) {
        this.lstType = lstType;
    }

    public List<PaymentGroupServiceDTO> getLstPaymentGroupService() {
        return lstPaymentGroupService;
    }

    public void setLstPaymentGroupService(List<PaymentGroupServiceDTO> lstPaymentGroupService) {
        this.lstPaymentGroupService = lstPaymentGroupService;
    }

    public List<PaymentGroupDTO> getLstPaymentGroup() {
        return lstPaymentGroup;
    }

    public void setLstPaymentGroup(List<PaymentGroupDTO> lstPaymentGroup) {
        this.lstPaymentGroup = lstPaymentGroup;
    }
}
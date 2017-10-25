package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockDebitService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author:ThanhNT77
 */
@Component
@Scope("view")
@ManagedBean(name = "underExportOrderController")
public class UnderExportOrderController extends TransCommonController {

    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private SignOfficeTagNameable writeOfficeTagFile;//khai bao tag ky vOffice
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockDebitService stockDebitService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private ProductOfferingService productOfferingService;

    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = true;
    private Boolean writeOfficeFile = true;
    private Boolean isCanPrint = false;
    private boolean autoCreateNoteFile;
    private int limitAutoComplete;
    private int tabIndex;
    private StaffDTO staffDTO;
    private StockDebitDTO stockDebitDTO;
    private RequiredRoleMap requiredRoleMap;
    private String attachFileName;
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private List<FieldExportFileDTO> fieldExportFileDTOs;
    private List<FieldExportFileDTO> lstErrFieldExport;
    private ExcellUtil processingFile;
    private boolean selectedFile;
    private boolean hasFileError;
    private Boolean tranLogistics = true;
    private Boolean tranfer = true;
    private boolean showDownResult;//bien hien thi link tai file ket qua tai nhap theo file
    private boolean showCreateOrder;//bien hien thi button lap lenh tai nhap theo file
    private boolean existLogistic;
    private String actionCodeNotePrint;
    private boolean checkErp;
    private List<OptionSetValueDTO> optionSetValueDTOTransports;
    private String transportTypeImport;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            initTab();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * init cho tab
     *
     * @throws LogicException
     * @throws Exception
     * @author ThanhNT
     */
    private void initTab() throws LogicException, Exception {
        requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK,
                Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP, Const.PERMISION.ROLE_AUTO_ORDER_NOTE, Const.PERMISION.ROLE_DONGBO_ERP);
        checkErp = false;
        if (tabIndex == 0) {
            initTab1();
        } else if (tabIndex == 1) {
            initTab2();
        }
    }

    /**
     * init data cho tab1
     *
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    private void initTab1() throws LogicException, Exception {
        stockDebitDTO = null;
        infoOrderDetail = false;
        isCanPrint = false;
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        //get max gioi han autocomplete
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        //init cho vung thong tin ky vOffice
        writeOfficeTag.init(this, staffDTO.getShopId());
        //init cho vung thong tin lenh xuat
        orderStockTag.init(this, writeOffice);
        //init cho tag list danh sach hang hoa
        listProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP));
    }

    /**
     * init data cho tab2
     *
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    private void initTab2() throws LogicException, Exception {
        //reset file
        attachFileName = null;
        uploadedFile = null;
        byteContent = null;
        fieldExportFileDTOs = Lists.newArrayList();
        lstErrFieldExport = Lists.newArrayList();
        processingFile = null;
        selectedFile = false;
        hasFileError = false;
        tranfer = false;//OUTSOURCE
        tranLogistics = true;
        writeOfficeFile = true;
        //init cho vung thong tin ky vOffice
        writeOfficeTagFile.init(this, staffDTO.getShopId());
        List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST, staffDTO.getShopCode()), new ArrayList<>());
        existLogistic = !DataUtil.isNullOrEmpty(options);
        optionSetValueDTOTransports = optionSetValueService.getByOptionSetCode("TRANSPORT_TYPE");
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        this.showAutoOrderNote = false;
        autoCreateNoteFile = false;
        tranfer = false;//OUTSOURCE
        init();
    }

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @param writeOffice
     * @author ThanhNT77
     */
    @Secured("@")
    public void receiveWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
        updateElemetId("frmExportOrder:numberTabView:pnExportOffice");
    }

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @param autoOrderNote
     * @author ThanhNT77
     */
    @Secured("@")
    public void receiveAutoOrderNote(Boolean autoOrderNote) {
        try {
            this.showAutoOrderNote = autoOrderNote;
            actionCodeNote = "";
            if (showAutoOrderNote) {
                actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            }
            orderStockTag.setTranLogistics(false);
            updateElemetId("frmExportOrder:numberTabView:pnUnderExportOrder");
            updateElemetId("frmExportOrder:numberTabView:pnlDataButton");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }

    }

    /**
     * an hien thi khi click vao checkBox tao phieu tu dong
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangeAutoOrderNotefile() {
        try {
            this.tranLogistics = false;
            updateElemetId("frmExportOrder:numberTabView");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }

    }

    /**
     * ham check hien thi check box lap phieu tu dong
     *
     * @return
     * @author thanhnt77
     */
    public Boolean getRoleCheckShowAutoOrderNote() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
    }

    public Boolean getShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            if (!(Const.SHOP.SHOP_VTT_ID.equals(vShopStaffDTO.getOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(vShopStaffDTO.getOwnerId()))) {
                if (vShopStaffDTO.getOwnerId() != null && vShopStaffDTO.getOwnerType() != null) {
                    stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()), DataUtil.safeToString(vShopStaffDTO.getOwnerType()));
                    updateElemetId("frmExportOrder:numberTabView:pnStockDebit");
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham xu ly xoa shop hien tai
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void clearCurrentShop() {
        stockDebitDTO = null;
        updateElemetId("frmExportOrder:numberTabView:pnStockDebit");
    }

    public boolean isShowStockDebit() {
        return stockDebitDTO != null;
    }

    /**
     * ham nhan gia tri kho 3 mien tu tag tren
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doReceiveThreeStock(Long ownerId, String ownerType) {
        try {
            //init cho tag list danh sach hang hoa
            listProductTag.init(this, new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, ownerId, ownerType));
            updateElemetId("frmExportOrder:numberTabView:listProduct:listProductpnListProduct");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doCreateUnderlyingOrder() {
        try {
            isCanPrint = false;
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            valdiateUserLogin(stockTransDTO.getFromOwnerId(), stockTransDTO.getToOwnerId(), DataUtil.isNullOrZero(stockTransDTO.getRegionStockId()));
            stockTransDTO.setActionCodeNote(actionCodeNote);
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            BaseMessage message = executeStockTransService.executeStockTrans(showAutoOrderNote ? Const.STOCK_TRANS.ORDER_AND_NOTE : Const.STOCK_TRANS.ORDER,
                    Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                LogicException logicException = new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
                throw logicException;
            }
            if (showAutoOrderNote) {
                reportSuccess("frmExportNote:msgExport", "export.order.note.create.success");
            } else {
                reportSuccess("frmExportNote:msgExport", "export.order.create.success");
            }
            isCanPrint = true;
            actionCodeNotePrint = actionCodeNote;
            actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        topPage();
    }

    @Secured("@")
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            List<StockTransDetailDTO> lsStorckTransDetail = listProductTag.getListTransDetailDTOs();
            stockTransDTO.setCreateDatetime(getSysdateFromDB());
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
            return exportStockTransDetail(stockTransDTO, lsStorckTransDetail);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham xu ly in phieu
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent doPrintStockTransDetailNote() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionCode(actionCodeNotePrint);
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTO.setStockTransActionId(null);
            List<StockTransDetailDTO> lsStorckTransDetail = listProductTag.getListTransDetailDTOs();
            return exportStockTransDetail(stockTransDTO, lsStorckTransDetail);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham validate
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidateUnderlyingOrder() {
        //validate thong tin chi tiet
        doValidateListDetail(listProductTag.getListTransDetailDTOs());
    }

    /**
     * ham xu ly upload file
     *
     * @param event
     * @author ThanhNT77
     */
    @Secured("@")
    public void doFileUploadAction(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            lstErrFieldExport = Lists.newArrayList();
            hasFileError = false;
            showDownResult = false;
            showCreateOrder = false;

            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            selectedFile = true;
            byteContent = uploadedFile.getContents();
            attachFileName = event.getFile().getFileName();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    private void clearUpload() {
        uploadedFile = null;
        selectedFile = false;
        attachFileName = "";
        byteContent = null;
        showDownResult = false;
    }


    /**
     * ham dowload file mau lap lenh
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.CREATE_EXP_CMD_FROM_FILE_PATTERN_TEMPLATE);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "createExpCmdFromFilePattern.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doPreviewField() {
        try {
            fieldExportFileDTOs = Lists.newArrayList();
            lstErrFieldExport = Lists.newArrayList();
            showDownResult = false;
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            //validate so dong
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            if (totalRow > 10000) {
                reportErrorValidateFail("", "", "mn.isdn.manage.create.field.validate.maxRow");
                return;
            }
            //validate so cot
            Row test = sheetProcess.getRow(0);
            if (test == null || processingFile.getTotalColumnAtRow(test) != 8) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
            }
            //validate doc file
            validateFileUpload(processingFile);
            if (DataUtil.isNullOrEmpty(fieldExportFileDTOs)) {
                clearUpload();
                reportErrorValidateFail("", "", "mn.isdn.manage.create.field.err.noOrder");
            } else {
                showCreateOrder = true;//hien thi button lap lenh xuat
                if (!DataUtil.isNullOrEmpty(lstErrFieldExport)) {
                    clearUpload();
                    reportSuccess("", "mn.isdn.manage.create.field.result", DataUtil.safeToString(fieldExportFileDTOs.size()),
                            DataUtil.safeToString(fieldExportFileDTOs.size() + lstErrFieldExport.size()));
                }
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * validate lap lenh theo file
     *
     * @author Thanhnt77
     */
    public void doValdiateCreateUnderlyingOrderByFile() {

    }

    /**
     * lap lenh theo file
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doCreateUnderlyingOrderByFile() {
        try {
            SignOfficeDTO signOfficeDTO = null;
            if (writeOfficeFile) {
                signOfficeDTO = writeOfficeTagFile.validateVofficeAccount();
            }

            HashMap<String, List<FieldExportFileDTO>> lstFiel = new HashMap<>();
            for (FieldExportFileDTO dto : fieldExportFileDTOs) {
                String key = dto.getActionCode() + "&" + dto.getFromOwnerCode() + "&" + dto.getToOwnerCode();
                if (lstFiel.containsKey(key)) {
                    List<FieldExportFileDTO> value = lstFiel.get(key);
                    value.add(dto);
                    lstFiel.put(key, value);
                } else {
                    List<FieldExportFileDTO> value = Lists.newArrayList();
                    value.add(dto);
                    lstFiel.put(key, value);
                }
                dto.setKey(key);
            }
            List<StockTransFileDTO> lsStockTransFileDto = Lists.newArrayList();

            String actionCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            int index = actionCode.lastIndexOf("_") + 1;
            String prefix = actionCode.substring(0, index);
            String posfix = actionCode.substring(index);
            Long noteNumber = DataUtil.safeToLong(posfix);

            for (String key : lstFiel.keySet()) {
                List<FieldExportFileDTO> lstDetail = lstFiel.get(key);
                FieldExportFileDTO firstElement = lstDetail.get(0);
                StockTransFileDTO stockTransFileDTO = new StockTransFileDTO();
                stockTransFileDTO.setKey(key);

                StockTransDTO stockTransDTO = new StockTransDTO();
                stockTransDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockTransDTO.setFromOwnerId(DataUtil.safeToLong(firstElement.getExportShop().getShopId()));
                stockTransDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockTransDTO.setToOwnerId(DataUtil.safeToLong(firstElement.getReceiveShop().getShopId()));
                stockTransDTO.setReasonId(Const.REASON_ID.XUAT_KHO_CAP_DUOI);
                stockTransDTO.setNote(firstElement.getNote());
                stockTransDTO.setStockBase(firstElement.getStockBase());
                stockTransDTO.setTransport(!getShowTransfer() || !tranfer ? null : Const.STOCK_TRANS.IS_TRANFER);
                stockTransDTO.setLogicstic(!getShowLogistic() || !tranLogistics ? null : Const.STOCK_TRANS.IS_LOGISTIC);
                stockTransDTO.setIsAutoGen(!getShowLogistic() || !tranLogistics ? null : Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC);
                stockTransDTO.setCheckErp(checkErp ? Const.STOCK_TRANS.IS_NOT_ERP : null);
                if (isThreeRegionProdCode(firstElement.getExportShop().getShopCode())) {
                    stockTransDTO.setRegionStockId(firstElement.getExportShop().getShopId());
                    stockTransDTO.setFromOwnerId(Const.L_VT_SHOP_ID);
                }
                //tao ma phieu cho TH lap lenh lap phieu tu dong
                if (autoCreateNoteFile) {
                    actionCode = prefix + DataUtil.customFormat("000000", DataUtil.safeToDouble(noteNumber));
                    stockTransDTO.setActionCodeNote(actionCode);
                    noteNumber++;
                }
                if (writeOfficeFile && signOfficeDTO != null) {
                    stockTransDTO.setUserName(signOfficeDTO.getUserName());
                    stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                    stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                    stockTransDTO.setSignVoffice(writeOfficeFile);
                }

                //outsource them transporttype
                if (tranfer) {
                    if (DataUtil.isNullOrEmpty(transportTypeImport)) {
                        transportTypeImport = (optionSetValueDTOTransports == null || optionSetValueDTOTransports.isEmpty()) ? null : optionSetValueDTOTransports.get(0).getValue();
                    }
                    stockTransDTO.setTransport(transportTypeImport);
                }

                stockTransFileDTO.setStockTransDTO(stockTransDTO);

                StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
                stockTransActionDTO.setActionCode(firstElement.getActionCode());
                stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
                stockTransActionDTO.setNote(firstElement.getNote());
                stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
                stockTransFileDTO.setStockTransActionDTO(stockTransActionDTO);

                List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList();
                for (FieldExportFileDTO dto : lstDetail) {
                    StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                    stockTransDetailDTO.setProdOfferId(dto.getProductOfferId());
                    stockTransDetailDTO.setStateId(dto.getStateId());
                    stockTransDetailDTO.setQuantity(dto.getQuantity());
                    stockTransDetailDTO.setProdOfferName(dto.getProdOfferName());
                    transDetailDTOs.add(stockTransDetailDTO);
                }
                stockTransFileDTO.setLstStockTransDetail(transDetailDTOs);
                lsStockTransFileDto.add(stockTransFileDTO);
            }
            List<BaseExtMessage> lsMessage = DataUtil.defaultIfNull(executeStockTransService.executeStockTransList(autoCreateNoteFile ? Const.STOCK_TRANS.ORDER_AND_NOTE : Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.EXPORT,
                    lsStockTransFileDto, requiredRoleMap), new ArrayList<>());
            HashMap<String, String> mapMsgValid = new HashMap<>();
            lsMessage.forEach(obj -> mapMsgValid.put(obj.getStockTransFileDTO() != null ? obj.getStockTransFileDTO().getKey() : "", obj.getDescriptionFile()));
            int countCreateOk = 0;
            for (FieldExportFileDTO dto : fieldExportFileDTOs) {
                String msg = mapMsgValid.get(dto.getKey());
                boolean checkMsg = DataUtil.isNullOrEmpty(msg);
                dto.setErrorMsg(checkMsg ? getText("export.order.create.success") : msg);
                if (checkMsg) {
                    countCreateOk++;
                }
            }
            reportSuccess("", "export.order.create.file.success", countCreateOk, fieldExportFileDTOs.size());
            hasFileError = false;//an link tai file loi
            showDownResult = true;//hien thi link tai file ket qua
            showCreateOrder = false;//an button lap lenh xuat
            topPage();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham reset thong tin
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doResetAll() {
    }

    /**
     * ham xu ly download file
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent downloadFileCreateOrder() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("createExpCmdFromFile_" + staffDTO.getName() + "_Result.xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.RESULT_FROM_FILE_TEMPLATE_CREATE_ORDER);
            fileExportBean.putValue("fieldExportFileDTOs", fieldExportFileDTOs);
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    @Secured("@")
    public StreamedContent downloadFileError() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("createExpCmdFromFileErr_" + staffDTO.getName() + ".xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_CREATE_ORDER);
            fileExportBean.putValue("lstErrFieldExport", lstErrFieldExport);
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    /**
     * ham xu ly validate file upload neu co loi
     *
     * @return true neu co loi
     * @author ThanhNT77
     */
    private void validateFileUpload(ExcellUtil processingFile) {
        try {
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            /*if (totalRow > 51) {
                reportErrorValidateFail("", "", "mn.isdn.manage.create.field.validate.maxRow");
            }*/
            for (int i = 1; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    continue;
                }
                FieldExportFileDTO dto = new FieldExportFileDTO();
                String actionCode = processingFile.getStringValue(row.getCell(0)).trim();
                String fromOwnerCode = processingFile.getStringValue(row.getCell(1)).trim();
                String toOwnerCode = processingFile.getStringValue(row.getCell(2)).trim();
                String productOfferCode = processingFile.getStringValue(row.getCell(3)).trim();
                String strState = processingFile.getStringValue(row.getCell(4)).trim();
                String strQuantity = processingFile.getStringValue(row.getCell(5)).trim();
                String note = processingFile.getStringValue(row.getCell(6)).trim();
                String stockBase = processingFile.getStringValue(row.getCell(7)).trim();
                dto.setActionCode(actionCode);
                dto.setFromOwnerCode(fromOwnerCode);
                dto.setToOwnerCode(toOwnerCode);
                dto.setProductOfferCode(productOfferCode);
                dto.setStrState(strState);
                dto.setStateName(productOfferingService.getStockStateName(DataUtil.safeToLong(strState)));
                dto.setStateId(DataUtil.safeToLong(strState));
                dto.setStrQuantity(strQuantity);
                dto.setNote(note);
                dto.setStockBase(stockBase);

                String msg = validateRow(dto, fieldExportFileDTOs);
                if (!DataUtil.isNullObject(msg)) {
                    dto.setErrorMsg(msg);
                    lstErrFieldExport.add(dto);
                } else {
                    fieldExportFileDTOs.add(dto);
                }

            }
            if (!DataUtil.isNullOrEmpty(lstErrFieldExport)) {
                hasFileError = true;
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }


    /**
     * upload file
     *
     * @param fieldExportFileDTO
     * @param fieldExportFileDTOs
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    private String validateRow(FieldExportFileDTO fieldExportFileDTO, List<FieldExportFileDTO> fieldExportFileDTOs) throws Exception {
        String actionCode = fieldExportFileDTO.getActionCode();
        String fromOwnerCode = fieldExportFileDTO.getFromOwnerCode();
        String toOwnerCode = fieldExportFileDTO.getToOwnerCode();
        String productOfferCode = fieldExportFileDTO.getProductOfferCode();
        Long stateId = fieldExportFileDTO.getStateId();
        String strQuantity = fieldExportFileDTO.getStrQuantity();
        String note = fieldExportFileDTO.getNote();
        if (DataUtil.isNullObject(actionCode) || actionCode.length() > 50 || !DataUtil.validateStringByPattern(actionCode, Const.REGEX.CODE_REGEX)) {
            return getText("mn.isdn.manage.create.field.validate.actionCode");
        }
        if (DataUtil.isNullObject(fromOwnerCode)) {
            return getText("mn.isdn.manage.create.field.validate.fromOwner");
        }
        if (DataUtil.isNullObject(toOwnerCode)) {
            return getText("mn.isdn.manage.create.field.validate.toOwner");
        }
        if (DataUtil.safeEqual(fromOwnerCode, toOwnerCode)) {
            return getText("mn.isdn.manage.create.field.validate.toOwnerEqualfromOwner");
        }
        if (DataUtil.isNullOrZero(stateId)) {
            return getText("mn.isdn.manage.create.field.validate.stateId");
        }

        if (DataUtil.isNullObject(productOfferCode)) {
            return getText("mn.isdn.manage.create.field.validate.productOffer");
        }

        if (DataUtil.isNullObject(strQuantity) || !DataUtil.validateStringByPattern(strQuantity, Const.REGEX.CODE_REGEX) || DataUtil.safeToLong(strQuantity) > 100000000000L || DataUtil.safeToLong(strQuantity) <= 0) {
            return getText("mn.isdn.manage.create.field.validate.quantity");
        } else {
            fieldExportFileDTO.setQuantity(DataUtil.safeToLong(strQuantity));
        }

        if (note.length() > 500) {
            return getTextParam("mn.isdn.manage.create.field.validate.note", DataUtil.safeToString(500));
        }

        //neu la kho 3 mien
        if (isThreeRegionProdCode(fromOwnerCode)) {
            // xu ly user dang nhap ko thuoc kho VT
            if (!Const.SHOP.SHOP_VTT_ID.equals(staffDTO.getShopId())) {
                return getText("mn.isdn.manage.creat.field.export.error.vtt");
            }
            //xu ly check quyen dau kho 3 mien cua user dang nhap
            if (!getShowThreeStock()) {
                return getText("mn.isdn.manage.creat.field.export.error.role");
            }

            List<ShopDTO> lstShopFrom = shopService.getListShopByStaffShopId(staffDTO.getShopId(), fromOwnerCode);
            if (DataUtil.isNullOrEmpty(lstShopFrom)) {
                return getText("mn.isdn.manage.creat.field.export.fromShopInvalid");
            } else {
                fieldExportFileDTO.setFromOwnerId(lstShopFrom.get(0).getShopId());
                fieldExportFileDTO.setExportShop(lstShopFrom.get(0));
            }
            //validate ton tai kho nhan
            List<ShopDTO> lstShopTo = shopService.getListShopByStaffShopId(staffDTO.getShopId(), toOwnerCode);
            if (DataUtil.isNullOrEmpty(lstShopTo)) {
                return getText("mn.isdn.manage.creat.field.export.toShopInvalid");
            } else {
                fieldExportFileDTO.setToOwnerId(lstShopTo.get(0).getShopId());
                fieldExportFileDTO.setReceiveShop(lstShopTo.get(0));
            }

        } else {
            // validate ton tai kho xuat
            if (!fromOwnerCode.equals(staffDTO.getShopCode())) {
                return getTextParam("mn.isdn.manage.create.field.validate.fromOwner.exist.staff", fromOwnerCode, staffDTO.getShopCode());
            }
            List<ShopDTO> lstShopFrom = shopService.getListShopByStaffShopId(staffDTO.getShopId(), fromOwnerCode);
            if (DataUtil.isNullOrEmpty(lstShopFrom)) {
                return getText("mn.isdn.manage.creat.field.export.fromShopInvalid");
            } else {
                fieldExportFileDTO.setFromOwnerId(lstShopFrom.get(0).getShopId());
                fieldExportFileDTO.setExportShop(lstShopFrom.get(0));
            }
            //validate ton tai kho nhan
            List<ShopDTO> lstShopTo = shopService.getListShopByStaffShopId(staffDTO.getShopId(), toOwnerCode);
            if (DataUtil.isNullOrEmpty(lstShopTo)) {
                return getText("mn.isdn.manage.creat.field.export.toShopInvalid");
            } else {
                fieldExportFileDTO.setToOwnerId(lstShopTo.get(0).getShopId());
                fieldExportFileDTO.setReceiveShop(lstShopTo.get(0));
            }
            ShopDTO shopDTOFrom = lstShopFrom.get(0);
            ShopDTO shopDTOTo = lstShopTo.get(0);
            //validate kho nhan phai la con lien ke cua kho nhan
            if (!shopDTOFrom.getShopId().equals(shopDTOTo.getParentShopId())) {
                return getTextParam("mn.isdn.manage.creat.field.export.toShopInvalid.child", shopDTOTo.getShopCode(), shopDTOFrom.getShopCode());
            }
        }


        // validate mat hang
        ProductOfferingDTO dto = productOfferingService.getProdOfferDtoByCodeAndStock(productOfferCode, fieldExportFileDTO.getFromOwnerId(), stateId);
        if (DataUtil.isNullObject(dto) || !DataUtil.safeEqual(dto.getStatus(), Const.STATUS.ACTIVE)) {
            return getText("mn.isdn.manage.creat.field.export.productOffer.active");
        } else {
            fieldExportFileDTO.setProductOfferId(dto.getProductOfferingId());
            fieldExportFileDTO.setProductOfferingDTO(dto);
            fieldExportFileDTO.setProdOfferName(dto.getName());
        }

        for (FieldExportFileDTO exportIsdnDTO : fieldExportFileDTOs) {
            if (!DataUtil.safeEqual(exportIsdnDTO.getActionCode(), fieldExportFileDTO.getActionCode())) {
                return getText("mn.isdn.manage.create.field.err.actionCodeNotEqual");
            }
            if (DataUtil.safeEqual(fieldExportFileDTO.getActionCode(), exportIsdnDTO.getActionCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getFromOwnerCode(), exportIsdnDTO.getFromOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getToOwnerCode(), exportIsdnDTO.getToOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getProductOfferCode(), exportIsdnDTO.getProductOfferCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getStateId(), exportIsdnDTO.getStateId())
                    ) {
                return getText("mn.isdn.manage.create.field.err.file.action.exist");
            }
        }
        return "";
    }

    private boolean isThreeRegionProdCode(String prodOfferCode) {
        return Const.THREE_REGION.VT_MB.equals(prodOfferCode) || Const.THREE_REGION.VT_MN.equals(prodOfferCode) || Const.THREE_REGION.VT_MT.equals(prodOfferCode);
    }

    public Boolean getShowThreeStock() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
    }

    public Boolean getShowLogistic() {
        return existLogistic && requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_SYNC_LOGISTIC);
    }

    public Boolean getShowTransfer() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
    }

    //getter and setter
    public Boolean getInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(Boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public StockDebitDTO getStockDebitDTO() {
        return stockDebitDTO;
    }

    public void setStockDebitDTO(StockDebitDTO stockDebitDTO) {
        this.stockDebitDTO = stockDebitDTO;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public Boolean getIsCanPrint() {
        return isCanPrint;
    }

    public void setIsCanPrint(Boolean isCanPrint) {
        this.isCanPrint = isCanPrint;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public List<FieldExportFileDTO> getFieldExportFileDTOs() {
        return fieldExportFileDTOs;
    }

    public void setFieldExportFileDTOs(List<FieldExportFileDTO> fieldExportFileDTOs) {
        this.fieldExportFileDTOs = fieldExportFileDTOs;
    }

    public List<FieldExportFileDTO> getLstErrFieldExport() {
        return lstErrFieldExport;
    }

    public void setLstErrFieldExport(List<FieldExportFileDTO> lstErrFieldExport) {
        this.lstErrFieldExport = lstErrFieldExport;
    }

    public ExcellUtil getProcessingFile() {
        return processingFile;
    }

    public void setProcessingFile(ExcellUtil processingFile) {
        this.processingFile = processingFile;
    }

    public boolean isSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(boolean selectedFile) {
        this.selectedFile = selectedFile;
    }

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public Boolean getTranLogistics() {
        return tranLogistics;
    }

    public void setTranLogistics(Boolean tranLogistics) {
        this.tranLogistics = tranLogistics;
    }

    public Boolean getTranfer() {
        return tranfer;
    }

    public void setTranfer(Boolean tranfer) {
        this.tranfer = tranfer;
    }

    public boolean isShowDownResult() {
        return showDownResult;
    }

    public void setShowDownResult(boolean showDownResult) {
        this.showDownResult = showDownResult;
    }

    public boolean isShowCreateOrder() {
        return showCreateOrder;
    }

    public void setShowCreateOrder(boolean showCreateOrder) {
        this.showCreateOrder = showCreateOrder;
    }

    public Boolean getWriteOfficeFile() {
        return writeOfficeFile;
    }

    public void setWriteOfficeFile(Boolean writeOfficeFile) {
        this.writeOfficeFile = writeOfficeFile;
    }

    public SignOfficeTagNameable getWriteOfficeTagFile() {
        return writeOfficeTagFile;
    }

    public void setWriteOfficeTagFile(SignOfficeTagNameable writeOfficeTagFile) {
        this.writeOfficeTagFile = writeOfficeTagFile;
    }

    public boolean isAutoCreateNoteFile() {
        return autoCreateNoteFile;
    }

    public void setAutoCreateNoteFile(boolean autoCreateNoteFile) {
        this.autoCreateNoteFile = autoCreateNoteFile;
    }

    public boolean isCheckErp() {
        return checkErp;
    }

    public void setCheckErp(boolean checkErp) {
        this.checkErp = checkErp;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOTransports() {
        return optionSetValueDTOTransports;
    }

    public void setOptionSetValueDTOTransports(List<OptionSetValueDTO> optionSetValueDTOTransports) {
        this.optionSetValueDTOTransports = optionSetValueDTOTransports;
    }

    public String getTransportTypeImport() {
        return transportTypeImport;
    }

    public void setTransportTypeImport(String transportTypeImport) {
        this.transportTypeImport = transportTypeImport;
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
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
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author:DungHa7
 */
@Component
@Scope("view")
@ManagedBean(name = "manageCreateStickerForKitController")
public class ManageCreateStickerForKitController extends TransCommonController {

    @Autowired
    private ShopInfoNameable shopInfoExportTag;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat
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
    @Autowired
    private ProductExchangeService productExchangeService;
    @Autowired
    private StockKitService stockKitService;

    private Boolean infoOrderDetail = false;
    private Boolean writeOfficeFile = true;
    private Boolean isCanPrint = false;
    private boolean autoCreateNoteFile;
    private int tabIndex;
    private StockDebitDTO stockDebitDTO;
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
    private StockTransInputDTO transInputDTO = new StockTransInputDTO();
    private List<ReasonDTO> lstReason;
    private int limitAutoComplete;
    private RequiredRoleMap requiredRoleMap;
    private boolean canPrint;
    private StaffDTO staffDTO;
    private boolean writeOffice = true;
    private Long stockTransActionId;
    private List<FieldExportFileRowDataDTO> lstExportFile = new ArrayList<>();
    private boolean preview = false;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            initTab();
            initProduct();
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

    private void initProduct() {
        try {
            if (staffDTO == null) {
                staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            }
            shopInfoExportTag.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), true);
            shopInfoExportTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstReason = reasonService.getLsReasonByType(Const.REASON_CODE.CDKIT);
            transInputDTO = new StockTransInputDTO();
            transInputDTO.setCreateDatetime(new Date());
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO()));
            transInputDTO.setCheckErp(false);
            transInputDTO.setFromOwnerType(DataUtil.safeToLong(Const.OWNER_TYPE.SHOP));
            transInputDTO.setFromOwnerId(staffDTO.getShopId());
            initTagProduct();
            //init cho vung thong tin ky vOffice
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            preview = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
            topPage();
        }

    }


    private void initTagProduct() {
        try {
            ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, DataUtil.safeToLong(staffDTO.getShopId()), Const.OWNER_TYPE.SHOP);
            config.setChangeProduct(true);
            config.setShowTotalPrice(false);
            listProductTag.init(this, config);
            List<String> listValue = Lists.newArrayList();
            listValue.add(DataUtil.safeToString(Const.STATE_STATUS.NEW));
            listValue.add(DataUtil.safeToString(Const.STATE_STATUS.DAMAGE));
            listValue.add(DataUtil.safeToString(Const.STATE_STATUS.RETRIEVE));
            listProductTag.setLsProductStatus(DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_STATE, listValue), new ArrayList<OptionSetValueDTO>()));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
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
                Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP, Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
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
        //init cho vung thong tin lenh xuat
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
        tranfer = true;
        tranLogistics = true;
        writeOfficeFile = true;
        //init cho vung thong tin ky vOffice
        List<OptionSetValueDTO> options = DataUtil.defaultIfNull(optionSetValueService.getByOptionsetCodeAndValue(Const.OPTION_SET.LOGISTIC_SHOP_ID_LIST, staffDTO.getShopCode()), new ArrayList<>());
        existLogistic = !DataUtil.isNullOrEmpty(options);
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        this.showAutoOrderNote = false;
        autoCreateNoteFile = false;
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

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            if (vShopStaffDTO.getOwnerId() != null && !DataUtil.isNullOrEmpty(vShopStaffDTO.getOwnerType())) {
                stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()), vShopStaffDTO.getOwnerType());
            }
            updateElemetId("frmExportOrder:numberTabView:pnStockDebit");
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
            preview = false;
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
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + "stock_trans_template/StickerFilePattern.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "StickerFilePattern_template.xls" + "\"");
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
            lstExportFile = new ArrayList<>();
            showDownResult = false;
            preview = true;
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
            if (totalRow > 51) {
                reportErrorValidateFail("", "", "mn.isdn.manage.create.field.validate.maxRow");
                return;
            }
            //validate so cot
            Row test = sheetProcess.getRow(0);
            if (test == null || processingFile.getTotalColumnAtRow(test) != 5) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
            }
            //validate doc file
            // add DTO fieldExportFileDTOs
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
            List<StockKitDTO> lstStockKit = new ArrayList<>();
            for (FieldExportFileDTO filExport : fieldExportFileDTOs) {
                Long quantity = filExport.getQuantity();
                List<StockKitDTO> lstFindStockKit = stockKitService.getStockKitBySerialAndProdOfferId(filExport.getFromOwnerId(), filExport.getFromSerial(), filExport.getToSerial(), filExport.getProductOfferId());

                if (!DataUtil.isNullOrEmpty(lstFindStockKit)) {
                    int size = lstFindStockKit.size();
                    if (!quantity.equals(DataUtil.safeToLong(size))) {
                        reportErrorValidateFail("", "", "mn.list.create.tally.out.kit.file.fromSerial.toSerial.quantity");
                        return;
                    }
                    lstStockKit.addAll(lstFindStockKit);
                } else {
                    return;
                }
            }
            if (!DataUtil.isNullOrEmpty(lstStockKit)) {
                lstExportFile = changeColumnDataExport(lstStockKit);
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened.retry", ex);
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


    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
//        PdfWriter.getInstance(pdf, (OutputStream) document);
        pdf.open();
//        PdfWriter writer = (PdfWriter) document;
        Element element;
        // step 4

        // step 5

//        pdf.add()
//        String FONT = "resources/sentinel-layout/fonts/times.ttf";
//        Font font = FontFactory.getFont(FONT, "Cp1250", BaseFont.EMBEDDED);
//        Font f=new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,50.0f,Font.UNDERLINE,BaseColor.RED);
//        Paragraph p=new Paragraph(document.getpa,font);
//        pdf.close();
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        pdf.add(p);
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        externalContext.setRequestCharacterEncoding("UTF-8");
//        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
//
//        pdf.add(Image.getInstance(logo));
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
            fileExportBean.setTemplateName("stock_trans_template/KitExchangeFilePattern.xls");
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
            fileExportBean.setOutName("Kit_CreateStickerError_" + staffDTO.getStaffCode() + ".xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName("Kit_CreateSticker.xls");
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
                int j = 0;
                FieldExportFileDTO dto = new FieldExportFileDTO();

                String fromOwnerCode = processingFile.getStringValue(row.getCell(j++)).trim();
                String productOfferCode = processingFile.getStringValue(row.getCell(j++)).trim();
                String fromSerial = processingFile.getStringValue(row.getCell(j++)).trim();
                String toSerial = processingFile.getStringValue(row.getCell(j++)).trim();
                String strQuantity = processingFile.getStringValue(row.getCell(j)).trim();

                dto.setFromOwnerCode(fromOwnerCode);
                dto.setFromSerial(fromSerial);
                dto.setToSerial(toSerial);
                dto.setProductOfferCode(productOfferCode);
                dto.setStrQuantity(strQuantity);
                dto.setStateName(productOfferingService.getStockStateName(DataUtil.safeToLong(Const.STOCK_GOODS.STATUS_NEW)));
                dto.setStateId(DataUtil.safeToLong(Const.STOCK_GOODS.STATUS_NEW));

                if (DataUtil.isNullOrEmpty(fromOwnerCode)
                        && (DataUtil.isNullOrEmpty(fromSerial)
                        || DataUtil.isNullOrEmpty(toSerial))) {
                    continue;
                }
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

        String fromOwnerCode = fieldExportFileDTO.getFromOwnerCode();
        String fromSerial = fieldExportFileDTO.getFromSerial();
        String toSerial = fieldExportFileDTO.getToSerial();
        String productOfferCode = fieldExportFileDTO.getProductOfferCode();
        Long stateId = fieldExportFileDTO.getStateId();
        String strQuantity = fieldExportFileDTO.getStrQuantity();

//        String toOwnerCode = "";
//        if (DataUtil.isNullObject(actionCode) || actionCode.length() > 50 || !DataUtil.validateStringByPattern(actionCode, Const.REGEX.CODE_REGEX)) {
//            return getText("mn.isdn.manage.create.field.validate.actionCode");
//        }
//        if (prodOfferCodeNew.equals(productOfferCode)) {
//            return getText("mn.list.create.tally.out.validate.prodOffer");
//        }
        if (DataUtil.isNullObject(fromOwnerCode)) {
            return getText("mn.isdn.manage.create.field.validate.fromOwner");
        }
        if (DataUtil.isNullObject(fromSerial) || !DataUtil.validateStringByPattern(fromSerial, Const.REGEX.SERIAL_REGEX) || fromSerial.length() >= 20 || fromSerial.length() <= 0) {
            return getTextParam("mn.list.create.tally.out.kit.file.fromSerial.require");
        }
        if (DataUtil.isNullObject(toSerial) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.SERIAL_REGEX) || toSerial.length() >= 20 || toSerial.length() <= 0) {
            return getTextParam("mn.list.create.tally.out.kit.file.toSerial.require");
        }
        if (DataUtil.isNullOrZero(stateId)) {
            return getText("mn.isdn.manage.create.field.validate.stateId");
        }

        if (DataUtil.isNullObject(productOfferCode)) {
            return getText("mn.isdn.manage.create.field.validate.productOffer");
        }

        if (DataUtil.isNullObject(strQuantity) || !DataUtil.validateStringByPattern(strQuantity, Const.REGEX.CODE_REGEX) || DataUtil.safeToLong(strQuantity) > 50000 || DataUtil.safeToLong(strQuantity) <= 0) {
            return getText("mn.isdn.manage.create.field.validate.quantity");
        } else {
            fieldExportFileDTO.setQuantity(DataUtil.safeToLong(strQuantity));
        }

//        if (note.length() > 500) {
//            return getTextParam("mn.isdn.manage.create.field.validate.note", DataUtil.safeToString(500));
//        }

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
//            validate ton tai kho nhan

            ShopDTO shop = shopService.findOne(staffDTO.getShopId());
            if (DataUtil.isNullObject(shop)
                    || DataUtil.isNullOrZero(shop.getShopId())
                    || !Const.STATUS.ACTIVE.equals(shop.getStatus())) {
                return getText("mn.isdn.manage.creat.field.export.toShopInvalid");
            } else {
                fieldExportFileDTO.setToOwnerId(shop.getShopId());
                fieldExportFileDTO.setReceiveShop(shop);
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
//            List<ShopDTO> lstShopTo = shopService.getListShopByStaffShopId(staffDTO.getShopId(), toOwnerCode);
//            if (DataUtil.isNullOrEmpty(lstShopTo)) {
//                return getText("mn.isdn.manage.creat.field.export.toShopInvalid");
//            } else {
//                fieldExportFileDTO.setToOwnerId(lstShopTo.get(0).getShopId());
//                fieldExportFileDTO.setReceiveShop(lstShopTo.get(0));//            }


//            ShopDTO shopDTOFrom = lstShopFrom.get(0);
//            ShopDTO shopDTOTo = lstShopTo.get(0);
            //validate kho nhan phai la con lien ke cua kho nhan
//            if (!shopDTOFrom.getShopId().equals(shopDTOTo.getParentShopId())) {
//                return getTextParam("mn.isdn.manage.creat.field.export.toShopInvalid.child", shopDTOTo.getShopCode(), shopDTOFrom.getShopCode());
//            }
        }


        // validate mat hang
        ProductOfferingDTO dto = productOfferingService.getProdOfferDtoByCodeAndStock(productOfferCode, staffDTO.getShopId(), stateId);
        if (DataUtil.isNullObject(dto) || !DataUtil.safeEqual(dto.getStatus(), Const.STATUS.ACTIVE)) {
            return getText("mn.isdn.manage.creat.field.export.productOffer.active");
        } else {
            fieldExportFileDTO.setProductOfferId(dto.getProductOfferingId());
            fieldExportFileDTO.setProductOfferingDTO(dto);
            fieldExportFileDTO.setProdOfferName(dto.getName());
        }
        BigInteger fromSerialFile = new BigInteger(fromSerial);
        BigInteger toSerialFile = new BigInteger(toSerial);

        Long valueSerial = (DataUtil.safeToLong(toSerial) - DataUtil.safeToLong(fromSerial) + 1);
        if (!valueSerial.equals(DataUtil.safeToLong(strQuantity))) {
            return getText("mn.list.create.tally.out.kit.file.fromSerial.toSerial.quantity");
        }

        Range<BigInteger> ranFile = Range.closed(fromSerialFile, toSerialFile);
        Range<BigInteger> ranSelected;
        for (FieldExportFileDTO exportIsdnDTO : fieldExportFileDTOs) {
            if (!DataUtil.safeEqual(exportIsdnDTO.getActionCode(), fieldExportFileDTO.getActionCode())) {
                return getText("mn.isdn.manage.create.field.err.actionCodeNotEqual");
            }
            //validate Trung: tuy dl ma check trung
            if (DataUtil.safeEqual(fieldExportFileDTO.getFromOwnerCode(), exportIsdnDTO.getFromOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getProductOfferCode(), exportIsdnDTO.getProductOfferCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getProdOfferCodeNew(), exportIsdnDTO.getProdOfferCodeNew())
                    && DataUtil.safeEqual(fieldExportFileDTO.getFromSerial(), exportIsdnDTO.getFromSerial())
                    && DataUtil.safeEqual(fieldExportFileDTO.getToSerial(), exportIsdnDTO.getToSerial())
                    ) {
                return getText("mn.isdn.manage.create.field.err.file.action.exist");
            }
            BigInteger fromSerialSelect = new BigInteger(exportIsdnDTO.getFromSerial());
            BigInteger toSerialSelect = new BigInteger(exportIsdnDTO.getToSerial());
            ranSelected = Range.closed(fromSerialSelect, toSerialSelect);
            if (ranSelected.isConnected(ranFile)) {
                return getText("mn.isdn.manage.create.field.err.file.action.exist");
            }
        }
//        for (FieldExportFileDTO exportIsdnDTO : fieldExportFileDTOs) {
//            if (!DataUtil.safeEqual(exportIsdnDTO.getActionCode(), fieldExportFileDTO.getActionCode())) {
//                return getText("mn.isdn.manage.create.field.err.actionCodeNotEqual");
//            }
//            if (DataUtil.safeEqual(fieldExportFileDTO.getActionCode(), exportIsdnDTO.getActionCode())
//                    && DataUtil.safeEqual(fieldExportFileDTO.getFromOwnerCode(), exportIsdnDTO.getFromOwnerCode())
//                    && DataUtil.safeEqual(fieldExportFileDTO.getProductOfferCode(), exportIsdnDTO.getProductOfferCode())
//                    ) {
//                return getText("mn.isdn.manage.create.field.err.file.action.exist");
//            }
//        }
        return "";
    }


    @Secured("@")
    public void doValidateCreateNote() {
        doPreviewField();
        List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();
        for (StockTransDetailDTO stockTransDetailDTO : lsDetailDTOs) {
            if (DataUtil.safeEqual(stockTransDetailDTO.getCheckSerial(), Const.PRODUCT_OFFERING._CHECK_SERIAL)
                    && DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstSerial())) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransDetailDTO.getProdOfferName());
                break;
            }
        }
    }

    @Secured("@")
    public void doCreateNote() {
        try {
            StockTransDTO stockTransDTO = getStockTransExport();
            StockTransActionDTO stockTransActionDTO = getStockTransActionExport();
            List<StockTransDetailDTO> lsDetailDTOs = new ArrayList<>();
            for (FieldExportFileDTO exportDTO : fieldExportFileDTOs) {
                StockTransDetailDTO stockTransDetailAdd = new StockTransDetailDTO();
                stockTransDetailAdd.setProdOfferId(exportDTO.getProductOfferId());
                stockTransDetailAdd.setProdOfferCode(exportDTO.getProductOfferCode());
                stockTransDetailAdd.setProdOfferCodeNew(exportDTO.getProdOfferCodeNew());
                stockTransDetailAdd.setStateId(exportDTO.getStateId());
                stockTransDetailAdd.setFromSerial(exportDTO.getFromSerial());
                stockTransDetailAdd.setToSerial(exportDTO.getToSerial());
                stockTransDetailAdd.setQuantity(exportDTO.getQuantity());
                stockTransDetailAdd.setNote(transInputDTO.getNote());
                stockTransDetailAdd.setTableName(Const.MapTableProdType.mapper.getTableName(Const.STOCK_TYPE.STOCK_KIT));

                List<StockTransSerialDTO> lstStockTransSerial = new ArrayList<>();
                StockTransSerialDTO serialDTO = new StockTransSerialDTO();
                serialDTO.setFromSerial(stockTransDetailAdd.getFromSerial());
                serialDTO.setToSerial(stockTransDetailAdd.getToSerial());
                serialDTO.setQuantity(stockTransDetailAdd.getQuantity());
                serialDTO.setStateId(stockTransDetailAdd.getStateId());

                lstStockTransSerial.add(serialDTO);
                stockTransDetailAdd.setLstStockTransSerial(lstStockTransSerial);
                lsDetailDTOs.add(stockTransDetailAdd);

            }


//            serialDTO.setStockTransId(stockTransDTOSave.getStockTransId());
//            serialDTO.setCreateDatetime(stockTransDTOSave.getCreateDatetime());
//            serialDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
//            serialDTO.setStockTransDetailId(stockTransDetailDTOSave.getStockTransDetailId());
//            StockTransSerialDTO stockTransSerialDTOSave = stockTransSerialService.save(serialDTO);
//            lstSerial.add(stockTransSerialDTOSave);

            BaseMessageStockTrans message = productExchangeService.createTallyOut(Const.STOCK_TRANS.NOTE_CHANGE_PRODUCT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode()) || !DataUtil.isNullOrEmpty(message.getKeyMsg())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            stockTransActionId = message.getStockTransActionId();
            canPrint = true;
            reportSuccess("", "mn.stock.change.product.offering.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened.retry", ex);
            topPage();

        }
    }

    public StockTransDTO getStockTransDTOForPint() {
        StockTransDTO stockTransDTO = getStockTransExport();
        try {
            stockTransDTO.setActionCode(transInputDTO.getActionCode());
            String fromOwnerName = null;
            String fromOwnerAddress = null;
            String toOwnerName = null;
            String toOwnerAddress = null;
            if (DataUtil.safeEqual(transInputDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(transInputDTO.getFromOwnerId());
                if (shopDTO != null) {
                    fromOwnerName = shopDTO.getName();
                    fromOwnerAddress = shopDTO.getAddress();
                }
            }
            if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                ShopDTO shopDTO = shopService.findOne(stockTransDTO.getToOwnerId());
                if (shopDTO != null) {
                    toOwnerName = shopDTO.getName();
                    toOwnerAddress = shopDTO.getAddress();
                }
            } else if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.PARTNER_LONG)) {
                PartnerDTO partnerDTO = partnerService.findOne(stockTransDTO.getToOwnerId());
                if (partnerDTO != null) {
                    toOwnerName = partnerDTO.getPartnerName();
                    toOwnerAddress = partnerDTO.getAddress();
                }
            }
            stockTransDTO.setFromOwnerName(fromOwnerName);
            stockTransDTO.setFromOwnerAddress(fromOwnerAddress);
            stockTransDTO.setToOwnerName(toOwnerName);
            stockTransDTO.setToOwnerAddress(toOwnerAddress);
            if (!DataUtil.isNullOrZero(transInputDTO.getReasonId())) {
                ReasonDTO reasonDTO = reasonService.findOne(transInputDTO.getReasonId());
                stockTransDTO.setReasonName(reasonDTO.getReasonName());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return stockTransDTO;
    }

    @Secured("@")
    public void doReset() {
        try {
            canPrint = false;
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO()));
            transInputDTO.setReasonId(null);
            transInputDTO.setNote(null);
            initTagProduct();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveWriteOffice() {
        //
    }

    private StockTransDTO getStockTransExport() {
        StockTransDTO stockTransExport = new StockTransDTO();
        stockTransExport.setFromOwnerType(transInputDTO.getFromOwnerType());
        stockTransExport.setFromOwnerId(transInputDTO.getFromOwnerId());
        if (DataUtil.safeEqual(transInputDTO.getFromOwnerId(), DataUtil.safeToLong(Const.VT_SHOP_ID))) {
            stockTransExport.setToOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransExport.setToOwnerId(Const.TD_PARTNER_ID);
        } else {
            stockTransExport.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransExport.setToOwnerId(DataUtil.safeToLong(Const.VT_SHOP_ID));
        }
        stockTransExport.setStockTransType(Const.STOCK_TRANS_TYPE.EXPORT);
        stockTransExport.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        stockTransExport.setReasonId(transInputDTO.getReasonId());
        stockTransExport.setNote(transInputDTO.getNote());
        stockTransExport.setCheckErp(transInputDTO.getCheckErp() ? Const.STOCK_TRANS.IS_NOT_ERP : null);
        return stockTransExport;
    }

    public StockTransActionDTO getStockTransActionExport() {
        StockTransActionDTO transActionDTO = new StockTransActionDTO();
        transActionDTO.setActionCode(transInputDTO.getActionCode());
        transActionDTO.setCreateUser(staffDTO.getStaffCode());
        transActionDTO.setActionStaffId(staffDTO.getStaffId());
        transActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        return transActionDTO;
    }

    @Secured("@")
    public boolean showCheckErp() {
        return true;
        // return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_TRANSPORT_STOCK);
    }

    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                List<Long> lstActionID = Lists.newArrayList();
                lstActionID.add(stockTransActionId);
                List<StockTransFullDTO> lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lstActionID), new ArrayList<StockTransFullDTO>());
                stockTransDTO.setStockTransActionId(stockTransActionId);
                return exportStockTransDetail(stockTransDTO, lsStockTransFull);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
        return null;
    }

    public void prepareExportPDF() throws Exception {
        if (DataUtil.isNullOrEmpty(lstExportFile)) {
            throw new LogicException("", getText("mn.create.sticker.for.kit"));
        }

    }

    public List<FieldExportFileRowDataDTO> changeColumnDataExport(List<StockKitDTO> stockKitDTOs) {
//        StockKitDTO lstExportFileDTO1s = new StockKitDTO();
//        StockKitDTO lstExportFileDTO2s = new StockKitDTO();
//        StockKitDTO lstExportFileDTO3s = new StockKitDTO();
//        StockKitDTO lstExportFileDTO4s = new StockKitDTO();
//        StockKitDTO lstExportFileDTO5s = new StockKitDTO();
//        ProductExchangeDTO lst5Rows = new ArrayList<>();
        FieldExportFileRowDataDTO newExport = new FieldExportFileRowDataDTO();
        List<FieldExportFileRowDataDTO> lstExportFileDTOs = new ArrayList<>();
        int n = 0;
        if (!DataUtil.isNullObject(stockKitDTOs)) {
            n = stockKitDTOs.size();
        }
//        boolean add = false;
        int k;
        if (n < 5) {
            k = (n / 5) + 1;
        } else {
            k = n / 5;
        }
        for (int j = 0; j < k; j++) {
            for (int i = 0; i < n; i++) {
                if (i % 5 == 0) {
                    newExport.setRowStockKitDTO1(stockKitDTOs.get(i));
                }
                if (i % 5 == 1) {
                    newExport.setRowStockKitDTO2(stockKitDTOs.get(i));
                }
                if (i % 5 == 2) {
                    newExport.setRowStockKitDTO3(stockKitDTOs.get(i));
                }
                if (i % 5 == 3) {
                    newExport.setRowStockKitDTO4(stockKitDTOs.get(i));
                }
                if (i % 5 == 4) {
                    newExport.setRowStockKitDTO5(stockKitDTOs.get(i));
                }
            }
            lstExportFileDTOs.add(newExport);
        }
        return lstExportFileDTOs;
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


    public boolean isAutoCreateNoteFile() {
        return autoCreateNoteFile;
    }

    public void setAutoCreateNoteFile(boolean autoCreateNoteFile) {
        this.autoCreateNoteFile = autoCreateNoteFile;
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public List<ReasonDTO> getLstReason() {
        return lstReason;
    }

    public void setLstReason(List<ReasonDTO> lstReason) {
        this.lstReason = lstReason;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }


    public boolean isCanPrint() {
        return canPrint;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }

    public boolean isWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public List<FieldExportFileRowDataDTO> getLstExportFile() {
        return lstExportFile;
    }

    public void setLstExportFile(List<FieldExportFileRowDataDTO> lstExportFile) {
        this.lstExportFile = lstExportFile;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }
}

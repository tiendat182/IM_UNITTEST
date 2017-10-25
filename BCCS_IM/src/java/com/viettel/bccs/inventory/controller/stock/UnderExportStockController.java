package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.fw.logging.Kpi;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListFifoProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
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
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * controller chuc nang xuat kho
 *
 * @author ThanhNT77
 */
@Component
@Scope("view")
@ManagedBean
public class UnderExportStockController extends TransCommonController {

    private static final String FILE_NAME_TEMPLATE = "mau_file_nhap_serial.xls";

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ListFifoProductNameable listProductTag;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockTransDetailService stockTransDetailService;

    private boolean infoOrderDetail;
    private int limitAutoComplete;
    private int limitOffline;
    private VStockTransDTO forSearch;
    private StaffDTO staffDTO;
    private RequiredRoleMap requiredRoleMap;
    private boolean isDisableSucess;
    private boolean showPanelOffline;
    private boolean checkOffline;
    private String attachFileName = "";
    private byte[] byteContent;
    private UploadedFile fileUpload;
    private ExcellUtil processingFile;
    private int tabIndex;
    private boolean selectedFile;
    private boolean previewOrder;
    private boolean hasFileError;
    private Long actionId;


    private List<VStockTransDTO> vStockTransDTOList;
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<StockTransFullDTO> lsStockTransFullError = Lists.newArrayList();
    private List<StockTransFullDTO> lsStockTransFullView = Lists.newArrayList();
    private List<StockTransDTO> lsStockTransValid = Lists.newArrayList();

    @PostConstruct
    public void init() {
        try {
            initTab();
            showDialog("guide");
        } catch (LogicException le) {
            logger.error(le);
            reportError("", "", le);
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
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
        if (tabIndex == 0) {
            initTab1();
        } else if (tabIndex == 1) {
            initTab2();
        }
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        init();
    }

    /**
     * init data cho tab1
     *
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    private void initTab1() throws LogicException, Exception {
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        vStockTransDTOList = Lists.newArrayList();
        List<OptionSetValueDTO> optionSetValueDTOs = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS), new ArrayList<>());
        //loai bo trang thai da lap lenh nhap = 4 va da tu choi nhap = 8, lap lenh xuat = 1
        optionSetValueDTOsList = optionSetValueDTOs.stream().filter(obj -> !(DataUtil.safeEqual(obj.getValue(), "4") || DataUtil.safeEqual(obj.getValue(), "8") || DataUtil.safeEqual(obj.getValue(), "1"))).collect(Collectors.toList());
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        limitOffline = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.MIN_QUANTITY_EXPORT_OFFLINE));
        requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
        orderStockTag.init(this, false);
        String shopId = DataUtil.safeToString(staffDTO.getShopId());
        shopInfoTagExport.initShop(this, shopId, false);
        shopInfoTagReceive.initShop(this, shopId, false);
        shopInfoTagExport.loadShop(shopId, true);
        doResetSearhStockTrans();
        listProductTag.setAddNewProduct(false);
    }

    /**
     * init data cho tab2
     *
     * @throws LogicException
     * @throws Exception
     * @author thanhnt77
     */
    private void initTab2() {
        //reset file
        attachFileName = null;
        fileUpload = null;
        byteContent = null;
        processingFile = null;
        selectedFile = false;
        hasFileError = false;
        previewOrder = false;
        lsStockTransFullError = Lists.newArrayList();
        lsStockTransFullView = Lists.newArrayList();
        lsStockTransValid = Lists.newArrayList();
    }

    /**
     * ham xu ly tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSearchStockTrans() {
        try {
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setVtUnit(Const.VT_UNIT.VT);
            forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
            forSearch.setUserShopId(staffDTO.getShopId());
            forSearch.setStockTransType(null);
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    /**
     * ham xu ly xoa thong tin tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doResetSearhStockTrans() {
        //vStockTransDTOList = Lists.newArrayList();
        shopInfoTagReceive.resetShop();
        //shopInfoTagExport.resetShop();
        Date currentDate = DateUtil.sysDate();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
        forSearch.setFromOwnerID(staffDTO.getShopId());
        infoOrderDetail = false;
        forSearch.setStockTransStatus("2");
    }

    /**
     * ham xu ly xoa thong tin tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    /**
     * ham reset back tro ve man tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doBackPage() {
        infoOrderDetail = false;
        orderStockTag.resetOrderStock();
        doSearchStockTrans();
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidateCreateStock() {
        try {
            List<StockTransDetailDTO> lsStockTransFull = listProductTag.getListTransDetailDTOs();
            for (StockTransDetailDTO stockTransFullDTO : lsStockTransFull) {
                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(stockTransFullDTO.getCheckSerial()) && DataUtil.isNullOrEmpty(stockTransFullDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransFullDTO.getProdOfferName());
                }
            }
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
     * ham xuat kho
     *
     * @author ThanhNT77
     */
    @Kpi("ID_KPI_UNDER_EXPORT")
    @Secured("@")
    public void doCreateUnderlyingStock() {
        try {
            List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();
            if (DataUtil.isNullOrEmpty(lsDetailDTOs)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.stock.goods.not.found.list.prod");
            }
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            //validate thong tin cua user dang nhap
            valdiateUserLogin(stockTransDTO.getFromOwnerId(), stockTransDTO.getToOwnerId(), DataUtil.isNullOrZero(stockTransDTO.getRegionStockId()));
            stockTransDTO.setNote(null);
            stockTransDTO.setProcessOffline(checkOffline ? Const.PROCESS_OFFLINE : "");
            //validate user dang nhap trc khi xuat kho
            StaffDTO staffDTOCurrent = staffService.getStaffByStaffCode(staffDTO.getStaffCode());
            if (staffDTOCurrent == null || !DataUtil.safeEqual(staffDTOCurrent.getShopChanelTypeId(), staffDTO.getShopChanelTypeId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, staffDTO.getName(), staffDTO.getShopName());
            }

            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.atutogen.fail", getText("mn.stock.underlying.export.stock"));
            }

            //validate xuat kho offline
            List<StockTransFullDTO> lstDetailDB = stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId()));
            Long total = 0L;
            for (StockTransFullDTO stock : lstDetailDB) {
                if (!DataUtil.isNullObject(stock.getCheckSerial()) && stock.getCheckSerial().equals(Const.PRODUCT_OFFERING._CHECK_SERIAL)) {
                    total += DataUtil.safeToLong(stock.getQuantity());
                }
            }
            if (total < limitOffline && checkOffline) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.export.stock.offline.invalide");
            }

            //xuat kho
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("", "export.stock.success");
            isDisableSucess = true;
            topPage();
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
     * ham view chi tiet
     *
     * @param stockTransActionId
     * @author ThanhNT77
     */
    @Secured("@")
    public void doViewStockTransDetail(Long stockTransActionId) {
        try {
            //Validate ky voffice
            StockTransActionDTO actionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);

            orderStockTag.loadOrderStock(stockTransActionId, true);
            orderStockTag.setNameThreeRegion("1");
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_SERIAL);
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);
            infoOrderDetail = true;
            isDisableSucess = false;
            List<StockTransDetailDTO> lsDetail = listProductTag.getListTransDetailDTOs();
            Long total = 0L;
            for (StockTransDetailDTO stock : lsDetail) {
                if (!DataUtil.isNullObject(stock.getCheckSerial()) && stock.getCheckSerial().equals(1L)) {
                    total += DataUtil.safeToLong(stock.getQuantity());
                }
            }


            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            showPanelOffline = (total >= limitOffline) && (DataUtil.isNullObject(stockTransDTO.getRegionStockTransId()));

            if (!(Const.SHOP.SHOP_VTT_ID.equals(stockTransDTO.getToOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(stockTransDTO.getToOwnerId()))) {
                if (stockTransDTO.getToOwnerId() != null && stockTransDTO.getToOwnerType() != null) {
                    stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(stockTransDTO.getToOwnerId()), DataUtil.safeToString(stockTransDTO.getToOwnerType()));
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void validateDestroyStock(Long stockActionId) {
        this.actionId = stockActionId;
    }

    /**
     * ham huy giao dich
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doDestroyStock() {
        try {
            if (DataUtil.isNullOrZero(actionId)) {
                return;
            }
            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(actionId);

            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());

            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.destroy.order.atutogen.fail", getText("mn.stock.underlying.export.stock"));
            }

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS, null, stockTransDTO, stockTransActionDTO, new ArrayList<StockTransDetailDTO>(), requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            doSearchStockTrans();
            reportSuccess("frmExportNote:msgExportNote", "export.order.stock.confirm.cancel.ok");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham xu ly in phieu
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            List<StockTransDetailDTO> lsStorckTransDetail = listProductTag.getListTransDetailDTOs();
            return exportStockTransDetail(stockTransDTO, lsStorckTransDetail);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent exportHandOverReport() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttohanover");
            }
            StreamedContent content = exportHandOverReport(stockTransDTO);
            return content;
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    /**
     * ham xu ly upload file xuat kho
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            hasFileError = false;
            fileUpload = event.getFile();
            selectedFile = false;

            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearFileUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = fileUpload.getContents();
            attachFileName = new String(fileUpload.getFileName().getBytes(), "UTF-8");
            processingFile = new ExcellUtil(fileUpload, byteContent);
            selectedFile = true;
        } catch (LogicException ex) {
            topReportError("msgExportFile", ex);
        } catch (Exception e) {
            topReportError("msgExportFile", "common.error.happened", e);
        }
    }

    /**
     * ham dowload file mau lap lenh
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.FROM_FILE_TEMPLATE_CREATE_ORDER_STOCK);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "createExpCmdUnderlyingFromFilePattern.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public StreamedContent downloadFileError() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("createExpCmdStockFromFileErr_" + staffDTO.getName() + ".xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_CREATE_ORDER_STOCK);
            fileExportBean.putValue("lsStockTransFullError", lsStockTransFullError);
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
     * xu ly clear xoa file upload xuat kho
     *
     * @author ThanhNT77
     */
    private void clearFileUpload() {
        attachFileName = null;
        processingFile = null;
        fileUpload = null;
        byteContent = null;
    }

    /**
     * ham xu ly xem file upload xuat kho hang loat
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public void doPreviewFileUpload() {
        try {
            lsStockTransFullView = Lists.newArrayList();
            lsStockTransFullError = Lists.newArrayList();
            lsStockTransValid = Lists.newArrayList();
            if (processingFile != null) {
                Sheet sheetProcess = processingFile.getSheetAt(0);
                Row test = sheetProcess.getRow(0);
                if (test == null || processingFile.getTotalColumnAtRow(test) != 9) {
                    reportError("msgExportFile", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
                } else {
                    if (createUnderStockByFile(processingFile)) {
                        clearFileUpload();
                        selectedFile = false;
                        hasFileError = true;
                        reportError("msgExportFile", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "distribute.number.file.invalid");
                    }
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            selectedFile = false;
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doValdiateCreateUnderlyingOrderByFile() {

    }

    /**
     * ham xuat kho
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doCreateUnderlyingStockByFile() {
        try {
            if (!DataUtil.isNullOrEmpty(lsStockTransValid)) {
                stockTransService.saveStockTransOffline(lsStockTransValid);
                reportSuccess("", "export.stock.success");
                clearFileUpload();
                hasFileError = false;
                selectedFile = false;
                lsStockTransValid = Lists.newArrayList();
                lsStockTransFullError = Lists.newArrayList();
                lsStockTransFullView = Lists.newArrayList();
                topPage();
            }
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
     * ham tao stockDetail theo loai mat hang va serial
     *
     * @param processingFile
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    private boolean createUnderStockByFile(ExcellUtil processingFile) throws LogicException, Exception {
        Sheet sheetProcess = processingFile.getSheetAt(0);

        //hashmap luu thong tin stockTransId
        HashMap<String, Long> hashMapStockTrans = Maps.newHashMap();
        //hashmap luu tong so luong mat hang
        HashMap<String, HashMap<String, Long>> hashMapQuantity = Maps.newHashMap();
        //hashmap luu thong tin dai serial tuong ung voi tung mat hang
        HashMap<String, HashMap<String, List<StockTransSerialDTO>>> hashMapSerial = Maps.newHashMap();
        //hashmap luu thong tin ma lenh, ma phieu, ma don vi
        HashMap<String, String> hashMapCmdCodeNoteCodeUnit = Maps.newHashMap();
        //hasMap luu thong tin index so luong mat hang, so luong serial dung de validate DB
        HashMap<String, List<Integer>> hashMapIndex = Maps.newHashMap();

        List<StockTransFullDTO> lsStockFullFileAll = Lists.newArrayList();//list all chi tiet
        List<StockTransFullDTO> lsStockFullFileError = Lists.newArrayList();//list record loi

        int totalRow = 0;
        int totalCheck = 0;
        boolean isExistError = false;
        for (Row row : sheetProcess) {
            if (totalCheck == 0) {
                totalCheck++;
                continue;
            }
            //doc tat ca cac dong trong sheet
            int col = 1;
            String cmdCode = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col++))).trim(); //ma lenh xuat
            String noteCode = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col++))).trim(); //ma phieu xuat
            String shopCode = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col++))).trim(); //don vi
            String prodOfferCode = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col++))).trim(); //ma mat hang
            Long stateId = DataUtil.safeToLong(processingFile.getStringValue(row.getCell(col++)).trim());//trang thai
            String fromSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col++))).trim(); //tu serial
            String toSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col++))).trim(); //den serial
            String quantity = DataUtil.safeToString(processingFile.getStringValue(row.getCell(col))).trim(); //so luong hang
            if (DataUtil.isNullOrEmpty(cmdCode)
                    && DataUtil.isNullOrEmpty(noteCode)
                    && DataUtil.isNullOrEmpty(shopCode)
                    && DataUtil.isNullOrEmpty(prodOfferCode)
                    && DataUtil.isNullOrEmpty(fromSerial)
                    && DataUtil.isNullOrZero(stateId)
                    && DataUtil.isNullOrEmpty(toSerial)) {
                continue;
            }
            StockTransFullDTO stockFull = new StockTransFullDTO();
            stockFull.setIndex(totalRow);
            totalRow++;
            //----//
            stockFull.setActionCode(cmdCode);
            stockFull.setActionCodeNote(noteCode);
            stockFull.setUnit(shopCode);
            stockFull.setProdOfferCode(prodOfferCode);
            stockFull.setStateId(stateId);
            stockFull.setFromSerial(fromSerial);
            stockFull.setToSerial(toSerial);
            stockFull.setStrQuantity(quantity);

            String msgValid = validateRow(stockFull, lsStockFullFileAll);
            if (!DataUtil.isNullOrEmpty(msgValid)) {
                stockFull.setMsgError(msgValid);
                lsStockFullFileError.add(stockFull);
            } else {
                String key = stockFull.getActionCode() + "&" + stockFull.getActionCodeNote();
                //luu thong tin index validate so luong mat hang, so luong serial trong DB
                List<Integer> lsIndex = hashMapIndex.get(key);
                if (lsIndex == null) {
                    lsIndex = Lists.newArrayList(stockFull.getIndex());
                    hashMapIndex.put(key, lsIndex);
                } else {
                    lsIndex.add(stockFull.getIndex());
                }

                //luu thong tin ma lenh, ma phieu, ma don vi
                if (!hashMapCmdCodeNoteCodeUnit.containsKey(key)) {
                    hashMapCmdCodeNoteCodeUnit.put(key, key + "&" + stockFull.getUnit());
                }

                //luu stock_trans_id vao hashmap
                if (!hashMapStockTrans.containsKey(key)) {
                    hashMapStockTrans.put(key, stockFull.getStockTransId());
                }

                //cong so luong total
                HashMap<String, Long> mapStockModelIdAndQuantiy = hashMapQuantity.get(key);
                String keyProdWithState = key + DataUtil.safeToString(stockFull.getProdOfferId()) + "&" + DataUtil.safeToString(stockFull.getStateId());
                if (mapStockModelIdAndQuantiy == null) {
                    mapStockModelIdAndQuantiy = Maps.newHashMap();
                    mapStockModelIdAndQuantiy.put(keyProdWithState, stockFull.getQuantity());

                    hashMapQuantity.put(key, mapStockModelIdAndQuantiy);
                } else {
                    Long quantityInList = mapStockModelIdAndQuantiy.get(keyProdWithState);
                    if (quantityInList != null) {
                        quantityInList += stockFull.getQuantity();
                    } else {
                        quantityInList = stockFull.getQuantity();
                    }
                    mapStockModelIdAndQuantiy.put(keyProdWithState, quantityInList);
                }

                //them serial truong hop mat hang ban theo dai serial
                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(DataUtil.safeToLong(stockFull.getCheckSerial()))) {
                    StockTransSerialDTO stockTransSerial = new StockTransSerialDTO();
                    stockTransSerial.setFromSerial(stockFull.getFromSerial());
                    stockTransSerial.setToSerial(stockFull.getToSerial());
                    stockTransSerial.setQuantity(stockFull.getQuantity());

                    HashMap<String, List<StockTransSerialDTO>> mapStockModelIdAndSerial = hashMapSerial.get(key);
                    String keyMatHangAndTrangThai = DataUtil.safeToString(stockFull.getProdOfferId()) + "&" + DataUtil.safeToString(stockFull.getStateId());
                    if (mapStockModelIdAndSerial == null) {
                        mapStockModelIdAndSerial = Maps.newHashMap();
                        List<StockTransSerialDTO> lstStockTransSerial = Lists.newArrayList();
                        lstStockTransSerial.add(stockTransSerial);
                        mapStockModelIdAndSerial.put(keyMatHangAndTrangThai, lstStockTransSerial);
                        hashMapSerial.put(key, mapStockModelIdAndSerial);
                    } else {
                        List<StockTransSerialDTO> lstStockTransSerial = mapStockModelIdAndSerial.get(keyMatHangAndTrangThai);
                        if (lstStockTransSerial == null) {
                            lstStockTransSerial = Lists.newArrayList();
                            lstStockTransSerial.add(stockTransSerial);
                        } else {
                            lstStockTransSerial.add(stockTransSerial);
                        }
                        mapStockModelIdAndSerial.put(keyMatHangAndTrangThai, lstStockTransSerial);
                    }
                }
            }
            lsStockFullFileAll.add(stockFull);
        }
        for (String key : hashMapStockTrans.keySet()) {
            String msgValid = createDeliverCmdUnderlying(key, hashMapStockTrans.get(key), hashMapQuantity.get(key), hashMapSerial.get(key), hashMapCmdCodeNoteCodeUnit.get(key).split("&"));
            if (!DataUtil.isNullOrEmpty(msgValid)) {
                List<Integer> lsIndex = hashMapIndex.get(key);
                if (lsIndex != null) {
                    for (Integer index : lsIndex) {
                        StockTransFullDTO stockTransFullDTO = lsStockFullFileAll.get(index);
                        if (stockTransFullDTO != null && DataUtil.isNullOrEmpty(stockTransFullDTO.getMsgError())) {
                            stockTransFullDTO.setMsgError(msgValid);
                        }
                    }
                }
            }
        }

        for (StockTransFullDTO stockTransFullDTO : lsStockFullFileAll) {
            if (DataUtil.isNullOrEmpty(stockTransFullDTO.getMsgError())) {
                lsStockTransFullView.add(stockTransFullDTO);
            } else {
                isExistError = true;
            }
        }
        if (isExistError) {
            lsStockTransFullError.addAll(lsStockFullFileAll);
        }
        return isExistError;
    }

    public boolean isShowCreateStockByFile() {
        return lsStockTransFullView != null && lsStockTransFullView.size() > 0;
    }

    /**
     * ham xu ly validate so luong mat hang, so luong serial trong DB
     *
     * @param stockTransId
     * @param mapStockModelIdAndQuantity
     * @param mapStockModelIdAndSerial
     * @param lstParam
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    private String createDeliverCmdUnderlying(String key, Long stockTransId, HashMap<String, Long> mapStockModelIdAndQuantity,
                                              HashMap<String, List<StockTransSerialDTO>> mapStockModelIdAndSerial, String[] lstParam) throws Exception {
        //10.Lay danh sach mat hang theo stockTransID
        List<StockTransDetailDTO> lsStockTransDetailDTO = stockTransDetailService.findByStockTransId(stockTransId);
        //10.1 neu danh sach trong thi bao loi
        if (DataUtil.isNullOrEmpty(lsStockTransDetailDTO)) {
            return getText("MSG.exp.cmd.underlying.from.file.17");
        }
        //10.2 neu so luong mat hang tra ve khac so luong mat hang trong DB thi bao loi
        if (lsStockTransDetailDTO.size() != mapStockModelIdAndQuantity.size()) {
            return getTextParam("MSG.exp.cmd.underlying.from.file.18", DataUtil.safeToString(lsStockTransDetailDTO.size()), DataUtil.safeToString(mapStockModelIdAndQuantity.size()),
                    lstParam[0], lstParam[1], lstParam[2]);
        }
        //11.Kiem tra so so luong serial cua tung mat hang
        for (StockTransDetailDTO stock : lsStockTransDetailDTO) {
            //11.1 neu tong so serial cua tung mat hang trong file bang 0 or bang rong thi bao loi
            String keyProdWithState = key + DataUtil.safeToString(stock.getProdOfferId()) + "&" + DataUtil.safeToString(stock.getStateId());
            Long quantity = mapStockModelIdAndQuantity.get(keyProdWithState);
            if (DataUtil.isNullOrZero(quantity) || !quantity.equals(stock.getQuantity())) {
//                return getTextParam("MSG.exp.cmd.underlying.from.file.19", stock.getProdOfferCode(), lstParam[0], lstParam[1], lstParam[2]);
                return getTextParam("MSG.exp.cmd.underlying.from.file.20", lstParam[1]);
            }
            String keyMatHangAndTrangThai = DataUtil.safeToString(stock.getProdOfferId()) + "&" + DataUtil.safeToString(stock.getStateId());
            List<StockTransSerialDTO> listSerialFile = mapStockModelIdAndSerial.get(keyMatHangAndTrangThai);

            if (!DataUtil.isNullOrEmpty(listSerialFile)) {
                stock.setLstStockTransSerial(listSerialFile);
            }

        }
        StockTransDTO stockTransDTO = new StockTransDTO();
        stockTransDTO.setStockTransId(stockTransId);
        stockTransDTO.setLsStockTransDetailDTOList(lsStockTransDetailDTO);
        stockTransDTO.setStaffId(staffDTO.getStaffId());
        //neu ko loi lam gi thi luu thong tin vao list stockTransTong quat
        lsStockTransValid.add(stockTransDTO);
        return "";
    }

    /**
     * ham xu ly validate theo row doc tu file
     *
     * @param stockFile
     * @param lsTransFullDTOs
     * @return
     * @author thanhnt77
     */
    private String validateRow(StockTransFullDTO stockFile, List<StockTransFullDTO> lsTransFullDTOs) throws LogicException, Exception {
        //1.validate cac truong bat buoc nhap
        //validate cac truong bat buoc nhap
        if (DataUtil.isNullOrEmpty(stockFile.getActionCode())
                || DataUtil.isNullOrEmpty(stockFile.getActionCodeNote())
                || DataUtil.isNullOrEmpty(stockFile.getUnit())
                || DataUtil.isNullOrEmpty(stockFile.getProdOfferCode())
                || DataUtil.isNullOrEmpty(stockFile.getStrQuantity())
                || stockFile.getStateId() == null) {
            return getText("input.list.product.file.content.empty");
        }
        //2.validate ma lenh phai co dinh dang a-zA-Z0-9
        if (stockFile.getActionCode().length() > 50 || !DataUtil.validateStringByPattern(stockFile.getActionCode(), Const.REGEX.CODE_REGEX)) {
            return getText("export.order.code.format.msg");
        }
        //3.validate ma phieu phai co dinh dang a-zA-Z0-9
        if (stockFile.getActionCodeNote().length() > 50 || !DataUtil.validateStringByPattern(stockFile.getActionCodeNote(), Const.REGEX.CODE_REGEX)) {
            return getText("export.order.code.under.format.msg");
        }
        //4.validate so luong phai la so va khong duoc de trong
        if (!DataUtil.validateStringByPattern(stockFile.getStrQuantity(), Const.REGEX.NUMBER_REGEX)) {
            return getText("export.order.stock.number.format.msg");
        }
        stockFile.setQuantity(DataUtil.safeToLong(stockFile.getStrQuantity()));

        //5.validate ton tai cua don vi xuat
        ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(stockFile.getUnit());
        if (shopDTO == null) {
            return getTextParam("MSG.exp.cmd.underlying.from.file.08", stockFile.getUnit());
        }
        //6.valdiate trong file neu malenh+maphieu da trung nhau thi ma don vi cung phai trung nhau, neu ko thi bao loi
        boolean isValidateDb = true;
        if (!DataUtil.isNullOrEmpty(lsTransFullDTOs)) {
            for (StockTransFullDTO stockTran : lsTransFullDTOs) {
                if (stockFile.getActionCode().equalsIgnoreCase(stockTran.getActionCode()) && stockFile.getActionCodeNote().equalsIgnoreCase(stockTran.getActionCodeNote())) {
                    if (!stockFile.getUnit().equalsIgnoreCase(stockTran.getUnit())) {
                        return getTextParam("MSG.exp.cmd.underlying.from.file.16", stockFile.getActionCode(), stockFile.getActionCodeNote());
                    } else {
                        if (stockTran.getStockTransId() != null) {
                            stockFile.setStockTransId(stockTran.getStockTransId());
                            isValidateDb = false;
                        }
                    }
                }
            }
        }
        //7.check ma phieu ton tai tren he thong lay stocktransId tu map phieu ma lenh
        //xu ly bo qua ban ghi da da check ton tai tren he thong roi
        if (isValidateDb) {
            Long stockTransId = stockTransService.getStockTransIdByCodeExist(stockFile.getActionCode(), stockFile.getActionCodeNote(), staffDTO.getShopId(), shopDTO.getShopId());
            //7.1.neu ko tim thay stockTransID
            if (DataUtil.isNullOrZero(stockTransId)) {
                return getTextParam("MSG.exp.cmd.underlying.from.file.09", stockFile.getActionCode(), stockFile.getActionCodeNote(), stockFile.getUnit());
            }
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransId);
            if (stockTransDTO != null && Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())) {
                return getText("error.cannot.create.receive.note.exchange");
            }
            stockFile.setStockTransId(stockTransId);
        }
        //8.xu ly check ma mat hang ton tai tren he thong
        ProductOfferingDTO offeringDTO = productOfferingService.getProdOfferDtoByCodeAndStock(stockFile.getProdOfferCode(), staffDTO.getShopId(), stockFile.getStateId());
        if (offeringDTO == null) {
            return getTextParam("MSG.exp.cmd.underlying.from.file.10", stockFile.getProdOfferCode(), stockFile.getActionCode(), stockFile.getActionCodeNote());
        }

        if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial()) && (DataUtil.isNullOrEmpty(stockFile.getFromSerial()) || DataUtil.isNullOrEmpty(stockFile.getToSerial()))) {
            return getText("input.list.product.file.content.empty");
        }
        stockFile.setProductOfferTypeId(offeringDTO.getProductOfferTypeId());
        //9. xu ly check serial cua mat hang
        if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(offeringDTO.getCheckSerial())) {
            //9.1.xu ly validate neu from serial to serial trong file ma trong thi bao loi
            String fromSerial = stockFile.getFromSerial();
            String toSerial = stockFile.getToSerial();
            if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
                return getTextParam("MSG.exp.cmd.underlying.from.file.12", stockFile.getProdOfferCode(), stockFile.getActionCode(), stockFile.getActionCodeNote());
            }
            //9.2 new la mat hang handset
            if (Const.STOCK_TYPE.STOCK_HANDSET.equals(offeringDTO.getProductOfferTypeId())) {
                //9.2.1 xu ly neu la mat hang handset thi from serial va toSerial phai giong nhau
                if (!fromSerial.equals(toSerial)) {
                    return getText("mn.stock.partner.serial.valid.hanset.valid.file");
                }
                //9.2.2 xu ly neu la mat hang handset thi so luong bat buoc phai bang 1
                if (!stockFile.getQuantity().equals(1L)) {
                    return getText("mn.stock.partner.serial.valid.hanset.valid.number");
                }
//                //9.2.3 check trung lap dai serial trong file cua mat hang hand set
//                boolean isDuplicate = false;
//                for (StockTransFullDTO stockTmp : lsTransFullDTOs) {
//                    if (DataUtil.safeEqual(stockTmp.getStateId(), stock.getStateId())
//                            && DataUtil.safeEqual(stockTmp.getProdOfferId(), stock.getProdOfferId())) {
//                        if (fromSerial.equals(stockTmp.getFromSerial()) && toSerial.equals(stockTmp.getToSerial())) {
//                            isDuplicate = true;
//                            break;
//                        }
//                    }
//                }
//                if (isDuplicate) {
//                    return getText("mn.stock.partner.range.duplidate.prod");
//                }
            } else { //9.3 voi mat hang khong phai handset
                //9.3.1 validate tu serial den serial phai la kieu so
                if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX)
                        || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                    return getTextParam("mn.stock.partner.serial.valid.format", offeringDTO.getName());
                }
                //9.3.2 validate den serial > tu serial va neu den seri - tu serial > 500.000
                BigInteger fromSerialFile = new BigInteger(fromSerial);
                BigInteger toSerialFile = new BigInteger(toSerial);
                BigInteger result = toSerialFile.subtract(fromSerialFile);
                if (result.compareTo(new BigInteger("0")) < 0 || result.compareTo(new BigInteger("500000")) > 0) {
                    return getMessage("mn.stock.partner.serial.valid.range");
                }
                //9.3.3 valadiate so luong serial, neu toSerial - fromSerial > so luong serial thuc te trong file thi cung bao loi
                result = result.add(BigInteger.ONE);
                if (result.compareTo(new BigInteger(stockFile.getStrQuantity())) != 0) {
                    return getMessage("input.list.product.file.number.valid.serial");
                }
//                //9.3.4 validate check trung lap dai serial trong file
//                boolean isDuplicate = false;
//                for (StockTransFullDTO stockTmp : lsTransFullDTOs) {
//                    if (!Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTmp.getProductOfferTypeId()) && DataUtil.isNullOrEmpty(stockTmp.getMsgError())
//                            && DataUtil.safeEqual(stockTmp.getStateId(), stock.getStateId())
//                            && DataUtil.safeEqual(stockTmp.getProdOfferId(), stock.getProdOfferId())   ) {
//                        Range<BigInteger> ranSelect = Range.closed(new BigInteger(stockTmp.getFromSerial()), new BigInteger(stockTmp.getToSerial()));
//                        Range<BigInteger> ranFile = Range.closed(fromSerialFile, toSerialFile);
//                        if (ranSelect.isConnected(ranFile)) {
//                            isDuplicate = true;
//                            break;
//                        }
//                    }
//                }
//                if (isDuplicate) {
//                    return getText("mn.stock.partner.range.duplidate.prod");
//                }
            }
        }
        //neu ko loi lam gi thi set du lieu vao
        stockFile.setProdOfferId(offeringDTO.getProductOfferingId());
        stockFile.setProductOfferTypeId(offeringDTO.getProductOfferTypeId());
        stockFile.setCheckSerial(DataUtil.safeToString(offeringDTO.getCheckSerial()));
        return "";
    }

    /**
     * ham xu ly in phieu
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent doCreateImportTemplate() {
        try {
            if (DataUtil.isNullOrEmpty(vStockTransDTOList)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.status.isdn.export.nodata");
            }
            List<Long> lsStockTransId = Lists.newArrayList();
            vStockTransDTOList.forEach(obj -> lsStockTransId.add(obj.getStockTransID()));

            List<StockTransFullDTO> listStockTransFullDTO = DataUtil.defaultIfNull(stockTransService.getListStockFullDTOByTransIdAndStatus(lsStockTransId, Lists.newArrayList(1L, 3L, 4L)), Lists.newArrayList());
            List<StockTransFullDTO> lsStockTmp = Lists.newArrayList();
            for (StockTransFullDTO stock : listStockTransFullDTO) {
                stock.setStrQuantity(DataUtil.safeToString(stock.getQuantity()));
                if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stock.getProductOfferTypeId())) {
                    int quantity = DataUtil.safeToInt(stock.getQuantity());
                    if (quantity > 1) {
                        for (int i = 0; i < quantity - 1; i++) {
                            lsStockTmp.add(stock);
                        }
                    }
                    stock.setStrQuantity("1");
                }
            }
            if (lsStockTmp.size() > 0) {
                listStockTransFullDTO.addAll(lsStockTmp);
                // xu ly sort lai
                Collections.sort(listStockTransFullDTO, new Comparator<StockTransFullDTO>() {
                    @Override
                    public int compare(StockTransFullDTO o1, StockTransFullDTO o2) {
                        int result = DataUtil.safeToString(o1.getActionCode()).compareTo(DataUtil.safeToString(o2.getActionCode()));
                        if (result == 0) {
                            result = DataUtil.safeToString(o1.getActionCodeNote()).compareTo(DataUtil.safeToString(o2.getActionCodeNote()));
                            if (result == 0) {
                                result = DataUtil.safeToString(o1.getUnit()).compareTo(DataUtil.safeToString(o2.getUnit()));
                                if (result == 0) {
                                    result = DataUtil.safeToString(o1.getProdOfferCode()).compareTo(DataUtil.safeToString(o2.getProdOfferCode()));
                                    if (result == 0) {
                                        result = DataUtil.safeToLong(o1.getStateId()).compareTo(DataUtil.safeToLong(o2.getStateId()));
                                    }
                                }
                            }
                        }
                        return result;
                    }
                });
            }

            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.EXPORT_TRANS_DETAIL_TEMPLATE);

            bean.putValue("lstStockTransFull", listStockTransFullDTO);
            bean.setOutName("createExpCmdUnderlyingFromFilePattern.xls");
            return FileUtil.exportToStreamed(bean);
//            FileUtil.exportFile(bean);
//            return bean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham cap nhap trang thai vOffice
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSignVoffice() {
        try {
            if (DataUtil.isNullOrZero(this.actionId)) {
                return;
            }
            //goi ham cap nhap thong tin vOffice
            doSaveStatusOffice(this.actionId);
            reportSuccess("", "voffice.sign.office.susscess");
            doSearchStockTrans();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", "common.error.happened", ex);
            topPage();
        }
    }

    //getter and setter
    public List<OptionSetValueDTO> getOptionSetValueDTOsList() {
        return optionSetValueDTOsList;
    }

    public void setOptionSetValueDTOsList(List<OptionSetValueDTO> optionSetValueDTOsList) {
        this.optionSetValueDTOsList = optionSetValueDTOsList;
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public ListFifoProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListFifoProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public ShopInfoNameable getShopInfoTagReceive() {
        return shopInfoTagReceive;
    }

    public void setShopInfoTagReceive(ShopInfoNameable shopInfoTagReceive) {
        this.shopInfoTagReceive = shopInfoTagReceive;
    }

    public ShopInfoNameable getShopInfoTagExport() {
        return shopInfoTagExport;
    }

    public void setShopInfoTagExport(ShopInfoNameable shopInfoTagExport) {
        this.shopInfoTagExport = shopInfoTagExport;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public boolean isDisableSucess() {
        return isDisableSucess;
    }

    public void setDisableSucess(boolean isDisableSucess) {
        this.isDisableSucess = isDisableSucess;
    }

    public boolean isShowPanelOffline() {
        return showPanelOffline;
    }

    public void setShowPanelOffline(boolean showPanelOffline) {
        this.showPanelOffline = showPanelOffline;
    }

    public boolean isCheckOffline() {
        return checkOffline;
    }

    public void setCheckOffline(boolean checkOffline) {
        this.checkOffline = checkOffline;
    }

    public List<StockTransFullDTO> getLsStockTransFullError() {
        return lsStockTransFullError;
    }

    public void setLsStockTransFullError(List<StockTransFullDTO> lsStockTransFullError) {
        this.lsStockTransFullError = lsStockTransFullError;
    }

    public List<StockTransDTO> getLsStockTransValid() {
        return lsStockTransValid;
    }

    public void setLsStockTransValid(List<StockTransDTO> lsStockTransValid) {
        this.lsStockTransValid = lsStockTransValid;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public ExcellUtil getProcessingFile() {
        return processingFile;
    }

    public void setProcessingFile(ExcellUtil processingFile) {
        this.processingFile = processingFile;
    }

    public List<StockTransFullDTO> getLsStockTransFullView() {
        return lsStockTransFullView;
    }

    public void setLsStockTransFullView(List<StockTransFullDTO> lsStockTransFullView) {
        this.lsStockTransFullView = lsStockTransFullView;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(boolean selectedFile) {
        this.selectedFile = selectedFile;
    }

    public boolean isPreviewOrder() {
        return previewOrder;
    }

    public void setPreviewOrder(boolean previewOrder) {
        this.previewOrder = previewOrder;
    }

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }
}

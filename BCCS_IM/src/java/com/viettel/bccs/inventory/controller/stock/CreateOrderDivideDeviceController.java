package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockDeviceTransfer;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by vanho on 23/03/2017.
 */
@Component
@Scope("view")
@ManagedBean
public class CreateOrderDivideDeviceController extends TransCommonController {

    private boolean showSearch = true;
    private boolean disableChangeDivideDevice;
    private boolean viewDetailDivideDevice;
    private boolean showDownResult;//bien hien thi link tai file ket qua tai nhap theo file
    private StreamedContent resultImportFile;
    private File fileResultImport;
    private StockRequestDTO stockRequestDTOSearch;
    private StockRequestDTO stockRequestDTOSelect;
    private StockRequestDTO stockRequestDTOSelectFile;
    private StockRequestDTO stockRequestDTOViewDetail;
    private List<StockRequestDTO> lsRequestSearch;
    private TabView tabView;
    private String docsInput;
    private int limitAutoComplete;
    private List<StockTransFullDTO> lsStockTransFull;
    private StaffDTO staffDTO;
    private int tabIndex;
    private String attachFileName;
    private byte[] byteContent;
    private boolean selectedFile;
    private boolean hasFileError;
    private boolean showPreview;
    private boolean isShowListProduct;
    private String suffix;
    private UploadedFile uploadedFile;
    private UploadedFile uploadedFileDoc;
    private ExcellUtil processingFile;
    private ConfigListProductTagDTO configListProductTagDTO;
    private List<DivideDevicePreview> previewFile = Lists.newArrayList();
    private List<DivideDevicePreview> previewFileClone = Lists.newArrayList();
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();
    private List<String> listStateERP = Lists.newArrayList();
    private List<String> listErrFile;
    private Map<String, StockTransDetailDTO> listProduct;
    private String shopPathSearch;
    public static final int MAX_SIZE_15M = 15 * 1024 * 1024;

    //private List
    @Autowired
    private DeviceConfigService deviceConfigService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTagFile;//khai bao tag ky vOffice
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopInfoNameable shopInfoTagImport;
    @Autowired
    private ShopInfoNameable shopInfoTagSearch;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTagSearch;
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private OrderDivideDeviceService orderDivideDeviceService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTotalService stockTotalService;
    @Autowired
    private StockDeviceTransferService stockDeviceTransferService;

    @Autowired

    @PostConstruct
    public void init() {
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            listStateERP.addAll(DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.WARRANTY_OFFER_STATUS), new ArrayList<OptionSetValueDTO>()).stream().map(s -> s.getValue()).collect(Collectors.toList()));

            shopPathSearch = shopService.getShopByShopId(staffDTO.getShopId()).getShopPath();

            if(tabIndex == 0)
                initData();
            else if (tabIndex == 1)
                initTab2();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        if(this.tabView == null)
            this.tabView = tabView;
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        this.showAutoOrderNote = false;
        init();
    }

    @Secured("@")
    public void receiveSearch(VShopStaffDTO vShopStaffDTO) {
        stockRequestDTOSearch.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockRequestDTOSearch.setOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
    }

    @Secured("@")
    public void receiveNormal(VShopStaffDTO vShopStaffDTO) {
        isShowListProduct = true;
        listProductTag.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockRequestDTOSelect.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:pnCreateRequest");
        RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:msgSearch");
    }

    @Secured("@")
    public void receiveImport(VShopStaffDTO vShopStaffDTO) {
        isShowListProduct = true;
        stockRequestDTOSelectFile.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:pnCreateRequestImport");
        RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:msgExportFile");
    }

    @Secured("@")
    public void doUploadDocs(FileUploadEvent event) {
        try {
            uploadedFileDoc = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFileDoc, ALOW_EXTENSION_PDF_TYPE_LIST, MAX_SIZE_15M);
            if (!message.isSuccess()) {
                clearUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if(tabIndex == 0)
                stockRequestDTOSelect.setFileContent(uploadedFileDoc.getContents());
            else if(tabIndex  == 1)
                stockRequestDTOSelectFile.setFileContent(uploadedFileDoc.getContents());
            docsInput = event.getFile().getFileName();
            if (DataUtil.isNullOrEmpty(docsInput)
                    || DataUtil.isNullObject(uploadedFileDoc.getContents())
                    || docsInput.length() > 200
                    || uploadedFileDoc.getContents().length == 0
                    || uploadedFileDoc.getContents().length > MAX_SIZE_15M) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.valid.file.create.order");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
            clearUpload();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
            clearUpload();
        }
    }

    public void doShowPreview(){
        previewFileClone = Lists.newArrayList(previewFile);
    }


    @Secured("@")
    public void clearSearchShop() {
        stockRequestDTOSearch.setOwnerId(null);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
    }

    @Secured("@")
    public void clearShop() {
        try {
            initData();
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            stockRequestDTOSelect.setOwnerCode(staffDTO.getShopCode());
            isShowListProduct = false;
            shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
            stockRequestDTOSelect.setOwnerId(null);
            RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:pnCreateRequest");
            RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:msgSearch");
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void clearShopImport() {
        try {
            initTab2();
            isShowListProduct = false;
            shopInfoTagImport.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
            stockRequestDTOSelectFile.setOwnerId(null);
            RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:pnCreateRequestImport");
            RequestContext.getCurrentInstance().update("frmChangeDOA:numberTabView:msgExportFile");
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    private void initTab2() throws LogicException, Exception {
        //reset file
        docsInput = "";
        attachFileName = "";
        uploadedFile = null;
        byteContent = null;
        processingFile = null;
        selectedFile = false;
        hasFileError = false;
        showDownResult = false;
        isShowListProduct = true;
        uploadedFileDoc = null;
        previewFile = new ArrayList<>();
        previewFileClone = new ArrayList<>();
        showPreview = false;
        listProduct = new HashMap<>();
        listErrFile = new ArrayList<>();
        //init cho vung thong tin ky vOffice
        writeOfficeTagFile.init(this, staffDTO.getShopId());
        showSearch = false;
        shopInfoTagImport.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        shopInfoTagImport.loadShop(DataUtil.safeToString(staffDTO.getShopId()), false);
        stockRequestDTOSelectFile = new StockRequestDTO();
        stockRequestDTOSelectFile.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockRequestDTOSelectFile.setRequestCode(stockRequestService.getRequest());
        stockRequestDTOSelectFile.setOwnerId(staffDTO.getShopId());
        stockRequestDTOSelectFile.setOwnerCode(staffDTO.getShopCode());
        disableChangeDivideDevice = true;
    }

    @Secured("@")
    public void doSearch() {
        try {
            if (stockRequestDTOSearch.getFromDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (stockRequestDTOSearch.getToDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(stockRequestDTOSearch.getFromDate(), stockRequestDTOSearch.getToDate()) > 0)
                    || DateUtil.daysBetween2Dates(stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getFromDate()) > 30L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "view.stock.offer.cycel.fromDate.endDate", 30);
            }
            lsRequestSearch = orderDivideDeviceService.getListOrderDivideDevice(shopPathSearch, stockRequestDTOSearch.getFromDate(), stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getOwnerId(), stockRequestDTOSearch.getRequestCode(), stockRequestDTOSearch.getStatus());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void doCreateRequest() {
        try {

            if(tabIndex == 0)
                initData();
            else initTab2();

            showSearch = false;
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            stockRequestDTOSelect.setOwnerId(staffDTO.getShopId());

            stockRequestDTOSelect.setOwnerCode(staffDTO.getShopCode());

            configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
            List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
            configListProductTagDTO.setLsProductStatus(lsProductStatus);
            configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DIVIDE);
            listProductTag.init(this, configListProductTagDTO);
            disableChangeDivideDevice = false;

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    public StreamedContent prepareDownloadFileResult() {
        InputStream stream;
        try {
            stream = new FileInputStream(fileResultImport);
            return new DefaultStreamedContent(stream, "application/excel", "resultImportFile." + suffix);
        } catch (FileNotFoundException e) {
            logger.error(e);
            reportError("", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    private void initData() throws LogicException, Exception {
        attachFileName = "";
        uploadedFileDoc = null;
        docsInput = "";
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        Date currentDate = optionSetValueService.getSysdateFromDB(true);
        stockRequestDTOSearch = new StockRequestDTO();
        stockRequestDTOSearch.setFromDate(currentDate);
        stockRequestDTOSearch.setToDate(currentDate);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);

        stockRequestDTOSelect = new StockRequestDTO();
        stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
        stockRequestDTOSelect.setOwnerId(staffDTO.getShopId());
        stockRequestDTOSelect.setOwnerCode(staffDTO.getShopCode());


        isShowListProduct = true;
        shopInfoTagSearch.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        staffInfoTagSearch.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

        shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), false);
        //shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        staffInfoTag.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

        configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
        List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
        configListProductTagDTO.setLsProductStatus(lsProductStatus);
        configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DIVIDE);
        listProductTag.init(this, configListProductTagDTO);
        writeOfficeTag.init(this, staffDTO.getShopId());
        lsRequestSearch = Lists.newArrayList();
        disableChangeDivideDevice = false;
    }

    @Secured("@")
    public void doResetSearch() {
        try {
            initData();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doValidRequest() {

        try {
            boolean isOK = true;
            if (shopInfoTag.getvShopStaffDTO() == null) {
                isOK = false;
                reportError("", "create.request.divide.device.shopMissing");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }
            if(DataUtil.isNullOrEmpty(stockRequestDTOSelect.getNote())){
                reportError("", "order.divide.product.docCode.empty");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            } else if(stockRequestDTOSelect.getNote().length() > 50) {
                reportError("", "order.divide.product.docCode.errorMaxlength");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            } else {
                Pattern pattern = Pattern.compile("^([a-zA-Z0-9_])+$");
                if(!pattern.matcher(stockRequestDTOSelect.getNote()).matches()){
                    reportError("", "order.divide.product.docCode.errorCharacter");
                    FacesContext.getCurrentInstance().validationFailed();
                    topPage();
                }
            }
            if (stockRequestDTOSelect.getFileContent() == null) {
                isOK = false;
                reportError("", "create.request.divide.device.docsMissing");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }
            if (DataUtil.isNullOrEmpty(listProductTag.getListTransDetailDTOs())) {
                isOK = false;
                reportError("", "create.request.divide.device.productIsEmpty");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }
            if (!isOK)
                return;

            //validate status
            boolean hasERP = false;
            boolean conflictERP = false;
            for (StockTransDetailDTO stockTransDetailDTO : listProductTag.getListTransDetailDTOs()) {
                if (listStateERP.contains(String.valueOf(stockTransDetailDTO.getStateId()))) {
                    hasERP = true;
                    continue;
                }

                if (hasERP) {
                    conflictERP = true;
                    break;
                }
            }

            if (conflictERP) {
                reportError("", "create.request.divide.device.conflictERP");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }

            //check trang thai phu kien phan ra
            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            boolean isSelectAllState = true;
            for (ListProductOfferDTO listProductOfferDTO : lsListProductOfferDTO) {

                if(listProductOfferDTO.getProductOfferingTotalDTO() == null || listProductOfferDTO.getProductOfferingTotalDTO().getProductOfferingId() == null)
                    continue;

                if (listProductOfferDTO.getStateId().toString().equals(Const.GOODS_STATE.NEW.toString())) { // neu la hang moi
                    if (DataUtil.isNullOrEmpty(listProductOfferDTO.getLstStockDeviceTransfer())) // chua load danh sach trang thai thi load len
                        listProductOfferDTO.setLstStockDeviceTransfer(getLstDeviceConfigFprStateNew(listProductOfferDTO.getProductOfferingTotalDTO().getProductOfferingId()));
                    continue;
                } else if(DataUtil.isNullOrEmpty(listProductOfferDTO.getLstStockDeviceTransfer())){ // ko phai hang moi va chua nhap tran thai
                    isSelectAllState = false;
                    break;
                }
                for (StockDeviceTransferDTO stockDeviceTransferDTO : listProductOfferDTO.getLstStockDeviceTransfer()) {
                    if (stockDeviceTransferDTO.getNewStateId() == null) {
                        isSelectAllState = false;
                        break;
                    }
                }
                if (!isSelectAllState)
                    break;
            }
            if (!isSelectAllState) {
                reportError("", "order.divide.product.noChooseState");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }
        } catch (Exception e){
            reportError("", "common.error.happened", e);
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        }
    }

    public List<StockDeviceTransferDTO> getLstDeviceConfigFprStateNew(Long probOfferId) throws Exception {
        List<DeviceConfigDTO> deviceConfigByProductAndState = DataUtil.defaultIfNull(deviceConfigService.getDeviceConfigByProductAndState(probOfferId, Const.GOODS_STATE.NEW), new ArrayList<>());
        List<StockDeviceTransferDTO> lstStockDeviceTransfer = new ArrayList<>();
        for (DeviceConfigDTO deviceConfigDTO : deviceConfigByProductAndState) {
            StockDeviceTransferDTO stockDeviceTransferDTO = new StockDeviceTransferDTO();
            stockDeviceTransferDTO.setProdOfferId(deviceConfigDTO.getProdOfferId());
            stockDeviceTransferDTO.setNewProdOfferId(deviceConfigDTO.getNewProdOfferId());
            stockDeviceTransferDTO.setStateId(Const.GOODS_STATE.NEW);
            stockDeviceTransferDTO.setNewStateId(Const.GOODS_STATE.NEW);
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(deviceConfigDTO.getNewProdOfferId());
            stockDeviceTransferDTO.setNewProbOfferCode(productOfferingDTO.getCode());
            stockDeviceTransferDTO.setNewProbOfferName(productOfferingDTO.getName());

            lstStockDeviceTransfer.add(stockDeviceTransferDTO);
        }

        return lstStockDeviceTransfer;
    }


    public void validateBeforeOrder(){

        if(shopInfoTagImport.getvShopStaffDTO() == null){
            reportError("", "create.request.divide.device.shopMissing");
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        }

        if(DataUtil.isNullOrEmpty(stockRequestDTOSelectFile.getNote())){
            reportError("", "order.divide.product.docCode.empty");
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        } else if(stockRequestDTOSelectFile.getNote().length() > 50) {
            reportError("", "order.divide.product.docCode.errorMaxlength");
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        } else {
            Pattern pattern = Pattern.compile("^([a-zA-Z0-9_])+$");
            if(!pattern.matcher(stockRequestDTOSelectFile.getNote()).matches()){
                reportError("", "order.divide.product.docCode.errorCharacter");
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }
        }

        if(stockRequestDTOSelectFile.getFileContent() == null){
            reportError("", "create.request.divide.device.docsMissing");
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        }

        //validate status
        boolean hasERP = false;
        boolean conflictERP = false;
        for(StockTransDetailDTO stockTransDetailDTO : listProduct.values().stream().collect(Collectors.toList())){
            if(listStateERP.contains(String.valueOf(stockTransDetailDTO.getStateId()))) {
                hasERP = true;
                continue;
            }

            if(hasERP) {
                conflictERP = true;
                break;
            }
        }

        if(conflictERP){
            reportError("", "create.request.divide.device.conflictERP");
            FacesContext.getCurrentInstance().validationFailed();
            topPage();
        }
        //List<StockTransDetailDTO> stockTransDetailDTOS = listProduct.values().stream().collect(Collectors.toList());
        for(String key : listProduct.keySet()){
            String [] tokens = key.split(":");
            List<DeviceConfigDTO> deviceConfigByProductAndState = deviceConfigService.getDeviceConfigByProductAndState(listProduct.get(key).getProdOfferId(), Long.valueOf(tokens[1]));
            if(deviceConfigByProductAndState.size() != listProduct.get(key).getLstStockDeviceTransfer().size()){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, getTextParam("create.request.divide.device.notEnough", tokens[0])));
                FacesContext.getCurrentInstance().validationFailed();
                topPage();
            }
        }

    }

    @Secured("@")
    private void doValidRequestImport() {
        try {
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
            if (totalRow > 101) {
                reportErrorValidateFail("", "", "deviceConfig.validate.limit.detail");
                return;
            }
            //validate so cot
            Row test = sheetProcess.getRow(3);
            if (test == null || processingFile.getTotalColumnAtRow(test) != 6) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
            }
            //validate doc file
            validateFileUpload(processingFile);

            List<String> collect = listErrFile.stream().filter(s -> !DataUtil.isNullOrEmpty(s)  && !getText("create.request.divide.device.valid").equalsIgnoreCase(s)).collect(Collectors.toList());
            if(DataUtil.isNullOrEmpty(collect) && !DataUtil.isNullOrEmpty(listProduct)){
                disableChangeDivideDevice = false;
            } else {
                showDownResult = true;
                Workbook workbook = WorkbookFactory.create(uploadedFile.getInputstream());
                Sheet sheet = workbook.getSheetAt(0);
                CellStyle cs = workbook.createCellStyle();
                cs.setBorderRight(CellStyle.BORDER_THIN);
                cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
                cs.setBorderBottom(CellStyle.BORDER_THIN);
                cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                cs.setBorderLeft(CellStyle.BORDER_THIN);
                cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                cs.setBorderTop(CellStyle.BORDER_THIN);
                cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
                cs.setWrapText(true);

                Font font = workbook.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                font.setCharSet(FontCharset.VIETNAMESE.getValue());
                CellStyle csHeader = workbook.createCellStyle();
                csHeader.setBorderRight(CellStyle.BORDER_THIN);
                csHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setBorderBottom(CellStyle.BORDER_THIN);
                csHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setBorderLeft(CellStyle.BORDER_THIN);
                csHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setBorderTop(CellStyle.BORDER_THIN);
                csHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
                csHeader.setFont(font);
                csHeader.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                csHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
                Row rowHeader = sheet.getRow(3);
                Cell cellResult = rowHeader.createCell(6);
                cellResult.setCellValue(getText("create.request.divide.device.result"));
                cellResult.setCellStyle(csHeader);
                rowHeader.getCell(5).setCellStyle(csHeader);
                rowHeader.getCell(4).setCellStyle(csHeader);
                rowHeader.getCell(3).setCellStyle(csHeader);
                rowHeader.getCell(2).setCellStyle(csHeader);
                rowHeader.getCell(1).setCellStyle(csHeader);
                rowHeader.getCell(0).setCellStyle(csHeader);
                int msgErr = 0;
                for (int row = 4; row <= sheet.getLastRowNum(); row++) {
                    Row xRow = sheet.getRow(row);
                    if(xRow == null)
                        continue;
                    if(listErrFile.size() <= msgErr)
                        break;
                    Cell cell = xRow.createCell(6);
                    cell.setCellValue(listErrFile.get(msgErr++));
                    cell.setCellStyle(cs);
                    Cell cell5 = xRow.getCell(5);
                    if(cell5 != null)
                        cell5.setCellStyle(cs);
                    Cell cell4 = xRow.getCell(4);
                    if(cell4 != null)
                        cell4.setCellStyle(cs);
                    Cell cell3 = xRow.getCell(3);
                    if(cell3 != null)
                        cell3.setCellStyle(cs);
                    Cell cell2 = xRow.getCell(2);
                    if(cell2 != null)
                        cell2.setCellStyle(cs);
                    Cell cell1 = xRow.getCell(1);
                    if(cell1 != null)
                        cell1.setCellStyle(cs);
                    Cell cell0 = xRow.getCell(0);
                    if(cell0 != null)
                        cell0.setCellStyle(cs);
                }

                String fileName = uploadedFile.getFileName();
                suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                fileResultImport = File.createTempFile("resultImport", suffix);
                FileOutputStream fileOut = new FileOutputStream(fileResultImport);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
                reportError("", "create.request.divide.device.importErr");
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

    @Secured("@")
    public StreamedContent downloadFileAttach(StockRequestDTO stockRequestDTO) {
        try {
            byte[] content = orderDivideDeviceService.getAttachFileContent(stockRequestDTO.getStockRequestId());
            InputStream is = new ByteArrayInputStream(content);
            return new DefaultStreamedContent(is, "application/xlsx/doc/docx/jpg/gpeg/pdf/jpe?g/png/gif/image/txt",
                    getTextParam("create.request.divide.device.docs.requestCode", stockRequestDTO.getRequestCode())+".pdf");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    public void doImportDivide(){
        try {
            //validate vOffice
            SignOfficeDTO signOfficeDTO = writeOfficeTagFile.validateVofficeAccount();
            stockRequestDTOSelectFile.setSignOfficeDTO(signOfficeDTO);

            //lay thong tin mat hang
            //List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            stockRequestDTOSelectFile.setStockTransDetailDTOs(listProduct.values().stream().collect(Collectors.toList()));

            stockRequestDTOSelectFile.setCreateUser(staffDTO.getStaffCode());
            stockRequestDTOSelectFile.setUpdateUser(staffDTO.getStaffCode());
            stockRequestDTOSelectFile.setActionStaffId(staffDTO.getStaffId());

            //thuc hien tao yeu cau
            orderDivideDeviceService.createOrderDivideDevice(stockRequestDTOSelectFile);

            reportSuccess("", "stockOrderAgent.add.sussces");
            disableChangeDivideDevice = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    public StreamedContent getResultImportFile() {
        return resultImportFile;
    }

    public void setResultImportFile(StreamedContent resultImportFile) {
        this.resultImportFile = resultImportFile;
    }

    @Secured("@")
    private void changeOwnerTypeInput() {
        try {
            shopInfoTag.resetShop();
            staffInfoTag.resetProduct();
            if (tabIndex == 0) {
                shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
                stockRequestDTOSelect.setOwnerId(staffDTO.getShopId());
                stockRequestDTOSelect.setOwnerCode(staffDTO.getShopCode());
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
                List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
                configListProductTagDTO.setLsProductStatus(lsProductStatus);
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DIVIDE);
                listProductTag.init(this, configListProductTagDTO);
            } else if(tabIndex == 1){
                shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
                stockRequestDTOSelectFile.setOwnerId(staffDTO.getShopId());
                stockRequestDTOSelectFile.setOwnerCode(staffDTO.getShopCode());
            }
            updateElemetId("frmChangeDOA:numberTabView:listProduct:listProductpnListProduct");
            updateElemetId("frmChangeDOA:numberTabView:msgSearch");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }
    @Secured("@")
    public void doResetRequestImport(){
        try {
            listErrFile = new ArrayList<>();
            listProduct = new HashMap<>();
            stockRequestDTOSelectFile = new StockRequestDTO();
            stockRequestDTOSelectFile.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelectFile.setRequestCode(stockRequestService.getRequest());
            writeOfficeTagFile.resetOffice();
            showDownResult = false;
            resultImportFile = null;
            fileResultImport = null;
            attachFileName = "";
            uploadedFileDoc = null;
            previewFile = new ArrayList<>();
            showPreview = false;
            previewFileClone = new ArrayList<>();
            docsInput = "";
            changeOwnerTypeInput();
            updateElemetId("frmChangeDOA:numberTabView:pnlOfficeImport");
            disableChangeDivideDevice = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        topPage();
    }

    @Secured("@")
    public void doResetRequest() {
        try {
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            docsInput = "";
            uploadedFileDoc = null;
            writeOfficeTag.resetOffice();
            changeOwnerTypeInput();
            updateElemetId("frmChangeDOA:numberTabView:pnlOffice");
            disableChangeDivideDevice = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
        topPage();
    }


    /**
     * ham chuyen doi yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doChangeRequest() {
        try {
            //validate vOffice
            SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            stockRequestDTOSelect.setSignOfficeDTO(signOfficeDTO);

            //lay thong tin mat hang
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            stockRequestDTOSelect.setStockTransDetailDTOs(stockTransDetailDTOs);

            stockRequestDTOSelect.setCreateUser(staffDTO.getStaffCode());
            stockRequestDTOSelect.setUpdateUser(staffDTO.getStaffCode());
            stockRequestDTOSelect.setActionStaffId(staffDTO.getStaffId());

            //thuc hien tao yeu cau
            orderDivideDeviceService.createOrderDivideDevice(stockRequestDTOSelect);

            reportSuccess("", "stockOrderAgent.add.sussces");
            disableChangeDivideDevice = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
            //reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham xem chi tiet yeu cau
     *
     * @author thanhnt77
     */
    public void doShowInfoOrderDetail(StockRequestDTO stockRequestDTO) {
        try {
            Long stockTransId = stockRequestDTO.getStockTransId();
            StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
            if (stockTransActionDTO != null) {
                lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId())), new ArrayList<>());
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    if ("1".equals(stockTransFullDTO.getCheckSerial())) {
                        stockTransFullDTO.setLstSerial(DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransFullDTO.getStockTransDetailId()), new ArrayList<>()));
                    }
                }
            }
            viewDetailDivideDevice = true;
            stockRequestDTOViewDetail = DataUtil.cloneBean(stockRequestDTO);
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOViewDetail.getOwnerType())) {
                ShopDTO shopDTO = shopService.findOne(stockRequestDTOViewDetail.getOwnerId());
                if (shopDTO != null) {
                    this.stockRequestDTOViewDetail.setOwnerCode(shopDTO.getShopCode());
                    this.stockRequestDTOViewDetail.setOwnerName(shopDTO.getName());
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    /**
     * ham xem serial
     *
     * @param stockTransDetailId
     * @author thanhnt77
     */
    public void doShowViewSerial(Long stockTransDetailId) {
        try {
            lsSerial = DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransDetailId), new ArrayList<>());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public void resetRequestCode() {
        try {
            if(tabIndex == 0)
                stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            else if(tabIndex == 1)
                stockRequestDTOSelectFile.setRequestCode(stockRequestService.getRequest());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    public boolean isDisableChangeDivideDevice() {
        return disableChangeDivideDevice;
    }

    public void setDisableChangeDivideDevice(boolean disabldoSeChangeDivideDevice) {
        this.disableChangeDivideDevice = disableChangeDivideDevice;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ConfigListProductTagDTO getConfigListProductTagDTO() {
        return configListProductTagDTO;
    }

    public void setConfigListProductTagDTO(ConfigListProductTagDTO configListProductTagDTO) {
        this.configListProductTagDTO = configListProductTagDTO;
    }

    public StaffInfoNameable getStaffInfoTagSearch() {
        return staffInfoTagSearch;
    }

    public void setStaffInfoTagSearch(StaffInfoNameable staffInfoTagSearch) {
        this.staffInfoTagSearch = staffInfoTagSearch;
    }

    public ShopInfoNameable getShopInfoTagSearch() {
        return shopInfoTagSearch;
    }

    public void setShopInfoTagSearch(ShopInfoNameable shopInfoTagSearch) {
        this.shopInfoTagSearch = shopInfoTagSearch;
    }

    public List<StockRequestDTO> getLsRequestSearch() {
        return lsRequestSearch;
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

    public void setLsRequestSearch(List<StockRequestDTO> lsRequestSearch) {
        this.lsRequestSearch = lsRequestSearch;
    }

    public StockRequestDTO getStockRequestDTOSearch() {
        return stockRequestDTOSearch;
    }

    public void setStockRequestDTOSearch(StockRequestDTO stockRequestDTOSearch) {
        this.stockRequestDTOSearch = stockRequestDTOSearch;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public boolean isViewDetailDivideDevice() {
        return viewDetailDivideDevice;
    }

    public void setViewDetailDivideDevice(boolean viewDetailDivideDevice) {
        this.viewDetailDivideDevice = viewDetailDivideDevice;
    }

    public StockRequestDTO getStockRequestDTOSelect() {
        return stockRequestDTOSelect;
    }

    public void setStockRequestDTOSelect(StockRequestDTO stockRequestDTOSelect) {
        this.stockRequestDTOSelect = stockRequestDTOSelect;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public StockRequestDTO getStockRequestDTOViewDetail() {
        return stockRequestDTOViewDetail;
    }

    public void setStockRequestDTOViewDetail(StockRequestDTO stockRequestDTOViewDetail) {
        this.stockRequestDTOViewDetail = stockRequestDTOViewDetail;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    @Secured("@")
    public void doBackPage() {
        showSearch = true;
        viewDetailDivideDevice = false;
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.IMPORT_DIVIDE_DEVICE);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            CellStyle cs = workbook.createCellStyle();
            cs.setBorderRight(CellStyle.BORDER_THIN);
            cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cs.setWrapText(true);
            Font font = workbook.createFont();
            font.setCharSet(FontCharset.VIETNAMESE.getValue());
            cs.setFont(font);

            HSSFSheet sheet = workbook.getSheetAt(1);
            //sheet.autoSizeColumn(0);
            //sheet.autoSizeColumn(1);
            //sheet.autoSizeColumn(2);
            //sheet.autoSizeColumn(3);
            List<DeviceConfigDTO> deviceConfigDTOS = deviceConfigService.getDeviceConfigInfo();
            for(int i = 0 ; i < deviceConfigDTOS.size() ; i++){
                DeviceConfigDTO deviceConfigDTO = deviceConfigDTOS.get(i);
                Row xRow = sheet.createRow(i + 1);
                Cell stt = xRow.createCell(0);
                stt.setCellValue(i + 1);
                stt.setCellStyle(cs);

                Cell product = xRow.createCell(1);
                product.setCellValue(deviceConfigDTO.getProductOfferName());
                product.setCellStyle(cs);

                Cell status = xRow.createCell(2);
                status.setCellValue(deviceConfigDTO.getStateName());
                status.setCellStyle(cs);

                Cell item = xRow.createCell(3);
                item.setCellValue(deviceConfigDTO.getNewProdOfferName());
                item.setCellStyle(cs);

                Cell productCode = xRow.createCell(4);
                productCode.setCellValue(deviceConfigDTO.getProductOfferCode());
                productCode.setCellStyle(cs);

                Cell stateCode = xRow.createCell(5);
                stateCode.setCellValue(deviceConfigDTO.getStateId());
                stateCode.setCellStyle(cs);

                Cell itemCode = xRow.createCell(6);
                itemCode.setCellValue(deviceConfigDTO.getNewProdOfferCode());
                itemCode.setCellStyle(cs);
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "template_import_divide_device.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doFileUploadAction(FileUploadEvent event) {
        try {

            uploadedFile = event.getFile();
            listErrFile = Lists.newArrayList();
            listProduct = new HashMap<>();
            showDownResult = false;
            previewFileClone = new ArrayList<>();

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

            doValidRequestImport();
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

    /*@Secured("@")
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
    }*/

    /*@Secured("@")
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
    }*/

    private void validateFileUpload(ExcellUtil processingFile) {
        try {

            previewFile = new ArrayList<>();
            Map<String, List<String>> mapSerialWithItem = new HashMap<>();
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            Row rowTemp = sheetProcess.getRow(3);
            String sttHeader = processingFile.getStringValue(rowTemp.getCell(0)).trim();
            String productHeader = processingFile.getStringValue(rowTemp.getCell(1)).trim();
            String statusProductHeader = processingFile.getStringValue(rowTemp.getCell(2)).trim();
            String serialHeader = processingFile.getStringValue(rowTemp.getCell(3)).trim();
            String itemHeader = processingFile.getStringValue(rowTemp.getCell(4)).trim();
            String itemStatusHeader = processingFile.getStringValue(rowTemp.getCell(5)).trim();
            if(!sttHeader.equalsIgnoreCase(getText("create.request.divide.device.stt")) || !productHeader.equalsIgnoreCase(getText("create.request.divide.device.product"))
                    || !statusProductHeader.equalsIgnoreCase(getText("create.request.divide.device.status")) || !serialHeader.equalsIgnoreCase(getText("create.request.divide.device.serial"))
                    || !itemHeader.equalsIgnoreCase(getText("create.request.divide.device.item")) || !itemStatusHeader.equalsIgnoreCase(getText("create.request.divide.device.item.status"))){
                reportErrorValidateFail("", "", "list.product.validate.fileError");
                return;
            }
            for (int i = 4; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    continue;
                }

                String productCode = processingFile.getStringValue(row.getCell(1)).trim().toUpperCase();
                String status = processingFile.getStringValue(row.getCell(2)).trim();
                String serial = processingFile.getStringValue(row.getCell(3)).trim();
                String item = processingFile.getStringValue(row.getCell(4)).trim();
                String itemStatus = processingFile.getStringValue(row.getCell(5)).trim();


                if(DataUtil.isNullOrEmpty(productCode) && DataUtil.isNullOrEmpty(status) && DataUtil.isNullOrEmpty(serial)
                        && DataUtil.isNullOrEmpty(item) && DataUtil.isNullOrEmpty(itemStatus)){
                    listErrFile.add("");
                    continue;
                }

                DivideDevicePreview devicePreview = new DivideDevicePreview();
                devicePreview.setProductCode(productCode);
                try {
                    devicePreview.setStatus(listProductTag.getLsProductStatus().stream().filter(s -> s.getValue().equals(status)).collect(Collectors.toList()).get(0).getName());
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    devicePreview.setStatus(status);
                }
                devicePreview.setSerial(serial);
                devicePreview.setItemCode(item);

                try {
                    devicePreview.setItemStatus(listProductTag.getLsProductStatus().stream().filter(s -> s.getValue().equals(itemStatus)).collect(Collectors.toList()).get(0).getName());
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                    devicePreview.setItemStatus(itemStatus);
                }

                previewFile.add(devicePreview);

                if(DataUtil.isNullOrEmpty(productCode)){
                    listErrFile.add(getText("create.request.divide.device.productNull"));
                    continue;
                }

                if(DataUtil.isNullOrEmpty(status)){
                    listErrFile.add(getText("create.request.divide.device.statusNull"));
                    continue;
                }

                if(DataUtil.isNullOrEmpty(serial)){
                    listErrFile.add(getText("create.request.divide.device.serialNull"));
                    continue;
                }

                if(DataUtil.isNullOrEmpty(item)){
                    listErrFile.add(getText("create.request.divide.device.itemCodeNull"));
                    continue;
                }

                if(DataUtil.isNullOrEmpty(itemStatus)){
                    listErrFile.add(getText("create.request.divide.device.itemStatusNull"));
                    continue;
                }


                if(listProductTag.getLsProductStatus().stream().noneMatch(s -> s.getValue().equals(status))){
                    listErrFile.add(getText("create.request.divide.device.status.inValid"));
                    continue;
                }

                if(listProductTag.getLsProductStatus().stream().noneMatch(s -> s.getValue().equals(itemStatus))){
                    listErrFile.add(getText("create.request.divide.device.itemStatus.inValid"));
                    continue;
                }


                ProductOfferingDTO productOfferingDTO = orderDivideDeviceService.getProductByCodeAndProbType(productCode, Const.DEVICE_CONFIG.PROB_OFFER_TYPE_ID);
                //List<ProductOfferingTotalDTO> lsProductOfferingDTO = orderDivideDeviceService.getLsProductOfferingDTO(productCode, stockRequestDTOSelectFile.getOwnerId(), Const.OWNER_TYPE.SHOP_LONG.toString(), status);
                /*if(DataUtil.isNullOrEmpty(lsProductOfferingDTO) || !lsProductOfferingDTO.get(0).getCode().equalsIgnoreCase(productCode)){
                    listErrFile.add(getTextParam("create.request.divide.device.productNotFound", productCode, status));
                    continue;
                }*/
                if(productOfferingDTO == null){
                    listErrFile.add(getTextParam("create.request.divide.device.productNotFound", productCode, status));
                    continue;
                }
                StockTransDetailDTO stockTransDetailDTO = listProduct.get(productCode + ":" + status);
                if(stockTransDetailDTO == null){
                    stockTransDetailDTO = new StockTransDetailDTO();
                    stockTransDetailDTO.setProdOfferId(productOfferingDTO.getProductOfferingId());
                    stockTransDetailDTO.setStateId(Long.valueOf(status));
                    stockTransDetailDTO.setOwnerID(staffDTO.getShopId());
                    stockTransDetailDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                    stockTransDetailDTO.setProdOfferTypeId(productOfferingDTO.getProductOfferTypeId());
                    stockTransDetailDTO.setQuantity(0L);
                    stockTransDetailDTO.setCheckSerial(DataUtil.safeToLong(productOfferingDTO.getCheckSerial()));
                    stockTransDetailDTO.setLstStockTransSerial(new ArrayList<>());
                    stockTransDetailDTO.setLstStockDeviceTransfer(new ArrayList<>());
                    StockTotalDTO stockTotalForProcessStock = stockTotalService.getStockTotalForProcessStock(staffDTO.getShopId(), Const.OWNER_TYPE.SHOP_LONG,
                            stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId());
                    stockTransDetailDTO.setAvaiableQuantity(stockTotalForProcessStock == null ? 0L : stockTotalForProcessStock.getAvailableQuantity());
                }
                List<StockTransSerialDTO> listSerialView = DataUtil.defaultIfNull(stockTransSerialService.getRangeSerial(Const.OWNER_TYPE.SHOP_LONG, staffDTO.getShopId(),
                        stockTransDetailDTO.getProdOfferId(), "STOCK_HANDSET", stockTransDetailDTO.getStateId(), "", null), new ArrayList<StockTransSerialDTO>());

                if(listSerialView.stream().noneMatch(s -> s.getFromSerial().equalsIgnoreCase(serial))){
                    listErrFile.add(getTextParam("create.request.divide.device.serialNotFound", serial, productCode, devicePreview.getStatus()));
                    continue;
                }
                /*List<ProductOfferingTotalDTO> listItem = DataUtil.defaultIfNull(deviceConfigService.getLsProductOfferingByProductTypeAndState(item, 11L, null), new ArrayList<>());
                if(DataUtil.isNullOrEmpty(listItem) || !listItem.get(0).getCode().equalsIgnoreCase(item)){
                    listErrFile.add(getTextParam("create.request.divide.device.itemCodeNotFound", item));
                    continue;
                }*/

                ProductOfferingDTO productOfferingItem = orderDivideDeviceService.getProductByCodeAndProbType(item, Const.DEVICE_CONFIG.ITEM_OFFER_TYPE_ID);
                if(productOfferingItem == null){
                    listErrFile.add(getTextParam("create.request.divide.device.itemCodeNotFound", item));
                    continue;
                }

                if(Const.GOODS_STATE.NEW == Long.valueOf(status) && Const.GOODS_STATE.NEW != Long.valueOf(itemStatus)){
                    listErrFile.add(getText("create.request.divide.device.conflictStatsItemWithProduct"));
                    continue;
                }

                List<DeviceConfigDTO> deviceConfigByProductAndState = deviceConfigService.getDeviceConfigByProductAndState(stockTransDetailDTO.getProdOfferId(), stockTransDetailDTO.getStateId());

                if(!deviceConfigByProductAndState.stream().map(s -> s.getNewProdOfferId()).collect(Collectors.toList()).contains(productOfferingItem.getProductOfferingId())){
                    listErrFile.add(getTextParam("create.request.divide.device.itemNotConfig", item, productCode, devicePreview.getItemStatus()));
                    continue;
                }
                List<String> lstItem = mapSerialWithItem.get(productCode + ":" + status + ":" + serial);
                if(!DataUtil.isNullOrEmpty(lstItem) && lstItem.contains(productOfferingItem.getProductOfferingId() + "")){
                    listErrFile.add(getTextParam("create.request.divide.device.itemCodeIsDuplicate", item, productCode));
                    continue;
                } else {
                    if (!stockTransDetailDTO.getLstStockDeviceTransfer().stream().map(s -> s.getNewProdOfferId()).collect(Collectors.toList()).contains(productOfferingItem.getProductOfferingId())){
                        StockDeviceTransferDTO stockDeviceTransferDTO = new StockDeviceTransferDTO();
                        stockDeviceTransferDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                        stockDeviceTransferDTO.setStateId(DataUtil.safeToLong(status));
                        stockDeviceTransferDTO.setNewProbOfferCode(productOfferingItem.getCode());
                        stockDeviceTransferDTO.setNewProdOfferId(productOfferingItem.getProductOfferingId());
                        stockDeviceTransferDTO.setNewStateId(DataUtil.safeToLong(itemStatus));

                        stockTransDetailDTO.getLstStockDeviceTransfer().add(stockDeviceTransferDTO);
                    }
                }

                if(!stockTransDetailDTO.getLstStockTransSerial().stream().map(s -> s.getFromSerial()).collect(Collectors.toList()).contains(serial)) {
                    stockTransDetailDTO.getLstStockTransSerial().add(listSerialView.stream().filter(s -> s.getFromSerial().equalsIgnoreCase(serial)).collect(Collectors.toList()).get(0));
                    stockTransDetailDTO.setQuantity(stockTransDetailDTO.getQuantity() + 1);
                }

                if(stockTransDetailDTO.getAvaiableQuantity() <  stockTransDetailDTO.getQuantity()){
                    listErrFile.add(getTextParam("create.request.divide.device.quantityNotEnough", productCode, status, stockTransDetailDTO.getAvaiableQuantity() + ""));
                    continue;
                }

                listProduct.put(productCode + ":" + status, stockTransDetailDTO);
                if(lstItem == null){
                    lstItem = new ArrayList<>();
                }
                lstItem.add(productOfferingItem.getProductOfferingId() + "");
                mapSerialWithItem.put(productCode + ":" + status + ":" + serial, lstItem);
                listErrFile.add(getText("create.request.divide.device.valid"));
            }

            if(!DataUtil.isNullOrEmpty(previewFile))
                showPreview = true;

        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }


    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
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

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public boolean isShowDownResult() {
        return showDownResult;
    }

    public void setShowDownResult(boolean showDownResult) {
        this.showDownResult = showDownResult;
    }

    public boolean isSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(boolean selectedFile) {
        this.selectedFile = selectedFile;
    }

    public SignOfficeTagNameable getWriteOfficeTagFile() {
        return writeOfficeTagFile;
    }

    public void setWriteOfficeTagFile(SignOfficeTagNameable writeOfficeTagFile) {
        this.writeOfficeTagFile = writeOfficeTagFile;
    }

    public StockRequestDTO getStockRequestDTOSelectFile() {
        return stockRequestDTOSelectFile;
    }

    public void setStockRequestDTOSelectFile(StockRequestDTO stockRequestDTOSelectFile) {
        this.stockRequestDTOSelectFile = stockRequestDTOSelectFile;
    }

    public TabView getTabView() {
        return tabView;
    }

    public void setTabView(TabView tabView) {
        this.tabView = tabView;
    }

    public String getDocsInput() {
        return docsInput;
    }

    public void setDocsInput(String docsInput) {
        this.docsInput = docsInput;
    }

    public UploadedFile getUploadedFileDoc() {
        return uploadedFileDoc;
    }

    public void setUploadedFileDoc(UploadedFile uploadedFileDoc) {
        this.uploadedFileDoc = uploadedFileDoc;
    }

    public boolean isShowPreview() {
        return showPreview;
    }

    public void setShowPreview(boolean showPreview) {
        this.showPreview = showPreview;
    }

    public List<DivideDevicePreview> getPreviewFile() {
        return previewFile;
    }

    public void setPreviewFile(List<DivideDevicePreview> previewFile) {
        this.previewFile = previewFile;
    }

    public List<DivideDevicePreview> getPreviewFileClone() {
        return previewFileClone;
    }

    public void setPreviewFileClone(List<DivideDevicePreview> previewFileClone) {
        this.previewFileClone = previewFileClone;
    }

    private List<DeviceConfigDetailDTO> listAddDeviceConfigDetail;

    public List<DeviceConfigDetailDTO> getListAddDeviceConfigDetail() {
        return listAddDeviceConfigDetail;
    }

    public void setListAddDeviceConfigDetail(List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) {
        this.listAddDeviceConfigDetail = listAddDeviceConfigDetail;
    }

    public void preToShowDeviceConfig(Long probOfferId, Long stateId){
        listAddDeviceConfigDetail = new ArrayList<>();
        try {
            List<DeviceConfigDTO> lstDeviceConfigUpdate = DataUtil.defaultIfNull(deviceConfigService.getDeviceConfigByProductAndState(probOfferId, stateId), new ArrayList<>());
            for (DeviceConfigDTO deviceConfigDTO1 : lstDeviceConfigUpdate) {
                DeviceConfigDetailDTO deviceConfigDetailDTO = new DeviceConfigDetailDTO();
                deviceConfigDetailDTO.setDeviceConfigDTO(new DeviceConfigDTO());
                BeanUtils.copyProperties(deviceConfigDetailDTO.getDeviceConfigDTO(), deviceConfigDTO1);
                deviceConfigDetailDTO.setStatus(stockDeviceTransferService.getDeviceConfigStateStrBy(deviceConfigDTO1.getProdOfferId(), Short.valueOf(deviceConfigDTO1.getStateId().toString()), deviceConfigDTO1.getNewProdOfferId()));
                try {
                    ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(deviceConfigDTO1.getNewProdOfferId());
                    deviceConfigDetailDTO.setProductOfferingTotalDTO(deviceConfigService.getLsProductOfferingByProductTypeAndState(productOfferingDTO.getCode(), Const.DEVICE_CONFIG.ITEM_OFFER_TYPE_ID, null).get(0));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new LogicException("105", "balance.valid.prodInfo");
                }
                listAddDeviceConfigDetail.add(deviceConfigDetailDTO);
            }
        } catch (LogicException ex){
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "", "common.error.happened");
            return;
        }
    }

    private String getKey(String key, String value) {
        String[] subs = key.split("_");
        StringBuilder result = new StringBuilder();
        for (String sub : subs) {
            if (!DataUtil.isNullOrEmpty(sub) && !key.startsWith(sub)) {
                result.append(".");
            }
            result.append(sub.toLowerCase());
        }
        return result.append(value).toString();
    }

    @Secured("@")
    public String getDisplayName(String code, Object value) {
        try {
            if (DataUtil.isNullOrEmpty(code)) {
                return DataUtil.safeToString(value);
            }
            if (DataUtil.isNullOrEmpty(DataUtil.safeToString(value))) {
                return "";
            }
            String val = DataUtil.safeToString(value).trim();
            List<OptionSetValueDTO> options = getOptionValByCode(code);
            for (OptionSetValueDTO optionVal : options) {
                if (optionVal.getValue().equals(val)) {
                    return optionVal.getName();
                }
            }
            String key = getKey(code, DataUtil.safeToString(value));
            return getText(key);
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
        return DataUtil.safeToString(value);
    }

    public boolean isShowListProduct() {
        return isShowListProduct;
    }

    public void setShowListProduct(boolean showListProduct) {
        isShowListProduct = showListProduct;
    }

    public ShopInfoNameable getShopInfoTagImport() {
        return shopInfoTagImport;
    }

    public void setShopInfoTagImport(ShopInfoNameable shopInfoTagImport) {
        this.shopInfoTagImport = shopInfoTagImport;
    }
}

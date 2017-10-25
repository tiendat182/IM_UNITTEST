package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.fw.logging.Kpi;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.ExcelWriterUtils;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.StockTotalWsDTO;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.mmserver.database.Data;
import com.viettel.web.common.annotation.Security;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.primefaces.model.DefaultStreamedContent;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by anhvv4 on 17/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class ViewInforWarehouseController extends InventoryController {

    @Autowired
    private ShopInfoNameable shopInfoTag;

    private VShopStaffDTO curShopStaff = new VShopStaffDTO();
    //bien phuc vu autocomplete staff
    private VShopStaffDTO curStaff = new VShopStaffDTO();
    private List<VShopStaffDTO> lsCurStaff = Lists.newArrayList();

    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();

    private String typeProductOffering;
    private String productOfferingCode;
    private String itemStock;
    private boolean showStaff;
    private WareHouseInfoDTO itemDTO;
    private TreeNode rootStock;
    private String fileNameOutDownload;
    private boolean directLink;
    private boolean disableUnit;
    private StaffDTO currentStaff;

    //bien phuc vu autocomplete productOffering
    private ProductOfferingTotalDTO productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalNewDTO = Lists.newArrayList();

    @Autowired
    ProductOfferingService productOfferingService;
    @Autowired
    StaffService staffService;
    @Autowired
    StockTransSerialService stockTransSerialService;
    @Autowired
    StockTotalService stockTotalService;
    @Autowired
    private ProductWs productWs;
    @Autowired
    KcsService kcsService;
    @Autowired
    private SaleWs saleWs;
    @Autowired
    StockHandsetService stockHandsetService;
    @Autowired
    StockTotalAuditService stockTotalAudit;
    private static final String FILE_KCS_TEMPLATE_PATH = "IMPORT_KCS_INFO_TEMPLATE.xls";
    private static final String FILE_KCS_TEMPLATE_ERROR_PATH = "IMPORT_KCS_INFO_TEMPLATE_ERROR.xls";
    private static final String FILE_KCS_ERROR_NAME = "importKCSInforError.xls";

    private String fileName;
    private UploadedFile uploadedFile;
    private List<String[]> listError = Lists.newArrayList();
    private Workbook bookError;
    private byte[] contentByte;
    private int dataRow = 0;
    HashMap<String, KcsDTO> mapKCS;
    HashMap<Long, Long> mapQuantity;
    private boolean showKcs;

    private static final String SPECIAL_CHAR_SERIAL = "[^A-Za-z0-9_-]+";
    private static final String SPECIAL_CHAR_CODE = "[^A-Za-z0-9_-]+";
    private Long errorCount;

    @Security("@")
    @PostConstruct
    public void init() {

        try {
            errorCount = 1L;
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            helperSystem();
            disableUnit = false;
            fileNameOutDownload = null;
            typeProductOffering = Const.OWNER_TYPE.SHOP;
            lsProductOfferingTotalNewDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOffering(null), new ArrayList<ProductOfferingTotalDTO>());
            //set lai list value cua text nhan vien
            rootStock = new DefaultTreeNode(new WareHouseInfoDTO(), null);
            initShop();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }

    }

    void initShop() {
        try {
            shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(getStaffDTO().getShopId().toString(), false);
            curShopStaff = shopService.getShopByShopId(getStaffDTO().getShopId());
            lsCurStaff = DataUtil.defaultIfNull(staffService.getStaffByShopId(null, DataUtil.safeToString(curShopStaff.getOwnerId()))
                    , Lists.newArrayList());
            curStaff = staffService.findStaffById(getStaffDTO().getStaffId().toString());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_KCS_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + FILE_KCS_TEMPLATE_PATH + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                fileName = null;
                contentByte = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                focusElementByRawCSSSlector(".outputAttachFile");
                throw ex;
            }
//            listError = Lists.newArrayList();
            fileName = uploadedFile.getFileName();
            contentByte = uploadedFile.getContents();

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    public void validateImport() {
        validate();
    }

    public boolean validate() {
        if (DataUtil.isNullObject(contentByte)) {
            reportError("frmImportKcs:messages", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            return false;
        }
        return true;
    }

    @Secured("@")
    public void importData() {
        try {
            listError = Lists.newArrayList();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                contentByte = null;
                fileName = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (validate()) {
                BaseMessage msg = excuteImportKcs();
                if (!DataUtil.isNullOrEmpty(listError)) {
                    try {
                        FileExportBean bean = new FileExportBean();
                        bean.setTempalatePath(FileUtil.getTemplatePath());
                        bean.setTemplateName(FILE_KCS_TEMPLATE_ERROR_PATH);
                        bean.putValue("lstData", listError);
                        bookError = FileUtil.exportWorkBook(bean);
                    } catch (Exception e) {
                        reportError("", "common.error.happen", e);
                        topPage();
                    }
                }
                if (msg.isSuccess()) {
                    reportSuccess("", "view.warehouse.import.kcs.success", dataRow - listError.size(), dataRow);
                } else {
                    reportError("", "", msg.getDescription());
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public BaseMessage excuteImportKcs() throws Exception {
        BaseMessage message = new BaseMessage(false);
        errorCount = 1L;
        try {
            mapKCS = new HashMap<String, KcsDTO>();
            mapQuantity = new HashMap<Long, Long>();
            ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
            Sheet sheet = ex.getSheetAt(0);
            dataRow = 0;
            Row title = sheet.getRow(3);
            if (!ex.getStringValue(title.getCell(1)).equals(getText("view.warehouse.import.kcs.title.code"))
                    || !ex.getStringValue(title.getCell(2)).equals(getText("view.warehouse.import.kcs.title.serial"))
                    || !ex.getStringValue(title.getCell(3)).equals(getText("view.warehouse.import.kcs.title.state"))) {
                message.setDescription("view.warehouse.import.kcs.file.invalid");
                return message;
            }
            for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String code = ex.getStringValue(row.getCell(1));
                String serial = ex.getStringValue(row.getCell(2));
                String state = ex.getStringValue(row.getCell(3));
                if (checkKCS(code, serial, state).isSuccess()) {
                    if (!mapKCS.containsKey(code)) {
                        List<String> serialList = new ArrayList<String>();
                        List<Long> stateIdList = new ArrayList<Long>();
                        serialList.add(serial);
                        stateIdList.add(Long.valueOf(state));

                        KcsDTO kcsDTO = new KcsDTO();
                        kcsDTO.setSerialList(serialList);
                        kcsDTO.setStateIdList(stateIdList);
                        mapKCS.put(code, kcsDTO);
                    } else {
                        List<String> serialList = mapKCS.get(code).getSerialList();
                        List<Long> stateIdList = mapKCS.get(code).getStateIdList();
                        serialList.add(serial);
                        stateIdList.add(Long.valueOf(state));
                    }
                }
                dataRow++;
            }
            if (DataUtil.isNullOrEmpty(mapKCS)) {
                message.setDescription("view.warehouse.import.kcs.all.data.invalid");
                return message;
            }
            Long userId = Long.valueOf(currentStaff.getStaffId());
            Long ownerId = Long.valueOf(curStaff.getOwnerId());
            if (userId == null || ownerId == null) {
                message.setDescription("view.warehouse.import.kcs.null.ownerId");
                return message;
            }
            message = checkStockTotal(ownerId);
            if (!message.isSuccess()) {
                return message;
            }
            try {
                message = kcsService.importKcs(userId, ownerId, mapKCS);
            } catch (Exception e) {
                message.setDescription("view.warehouse.import.kcs.update.error");
                throw e;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (DataUtil.isNullOrEmpty(message.getDescription()))
                message.setDescription("view.warehouse.import.kcs.system.error");
        }
        return message;
    }

    // kiem tra du lieu kcs
    @Secured("@")
    public BaseMessage checkKCS(String code, String serial, String state) {
        BaseMessage message = new BaseMessage(false);
        if (DataUtil.isNullOrEmpty(code) || code.trim().length() > 30 || Pattern.compile(SPECIAL_CHAR_CODE).matcher(code).find()) {
            message.setDescription(getText("view.warehouse.import.kcs.code.invalid.error"));
            String[] error = {(errorCount++).toString(), code, serial, state, message.getDescription()};
            listError.add(error);
            return message;
        }
        if (DataUtil.isNullOrEmpty(serial) || serial.trim().length() > 30 || Pattern.compile(SPECIAL_CHAR_SERIAL).matcher(serial).find()) {
            message.setDescription(getText("view.warehouse.import.kcs.serial.invalid.error"));
            String[] error = {(errorCount++).toString(), code, serial, state, message.getDescription()};
            listError.add(error);
            return message;
        }
        if (DataUtil.isNullOrEmpty(state) || !(state.equals(Const.STATE_DAMAGE.toString()) || state.equals(Const.STATE_GOOD.toString()))) {
            message.setDescription(getText("view.warehouse.import.kcs.state.invalid.error"));
            String[] error = {(errorCount++).toString(), code, serial, state, message.getDescription()};
            listError.add(error);
            return message;
        }
        if (!mapKCS.isEmpty())
            if (mapKCS.containsKey(code)) {
                KcsDTO kcsDTO = mapKCS.get(code);
                List<String> serialList = kcsDTO.getSerialList();
                if (serialList.contains(serial)) {
                    String[] error = {(errorCount++).toString(), code, serial, state, getText("view.warehouse.import.kcs.data.duplicate.error")};
                    listError.add(error);
                    return message;
                }
            }
        try {
            ProductOfferingDTO product = productOfferingService.getProductByCode(code);
            ProductOfferingDTO productIm1 = productOfferingService.getProductByCodeIm1(code);

            Long ownerId = Long.valueOf(curStaff.getOwnerId());

            if (product == null || productIm1 == null) {
                String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.code.not.found.error", (product == null ? "" : "BCCS1_"))};
                listError.add(error);
                return message;
            }

            if (!product.getStatus().equals(Const.STATUS_ACTIVE) || !productIm1.getStatus().equals(Const.STATUS_ACTIVE)) {
                String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.product.not.active.error", (!product.getStatus().equals(Const.STATUS_ACTIVE) ? "" : "BCCS1_"))};
                listError.add(error);
                return message;
            }

            if (stockTotalService.getStockTotalForProcessStock(
                    ownerId, Const.OWNER_TYPE_STAFF, product.getProductOfferingId(), Const.STATE_EXPIRE) == null ||
                    stockTotalService.getStockTotalForProcessStockIm1(
                            ownerId, Const.OWNER_TYPE_STAFF, product.getProductOfferingId(), Const.STATE_EXPIRE) == null) {
                String[] error = {(errorCount++).toString(), code, serial, state,
                        /*getTextParam("view.warehouse.import.kcs.stock.not.exist.error",
                                (stockTotalService.getStockTotalForProcessStock(
                                ownerId, Const.OWNER_TYPE_STAFF, product.getProductOfferingId(), Const.STATE_EXPIRE) == null ? "" : "BCCS1_"))*/
                        getTextParam("view.warehouse.import.kcs.stock.not.exist.error",
                                (stockTotalService.getStockTotalForProcessStock(
                                        ownerId, Const.OWNER_TYPE_STAFF, product.getProductOfferingId(), Const.STATE_EXPIRE) == null ? "" : "BCCS1_"), product.getName())
                };
                listError.add(error);
                return message;
            }
            List<ProductInStockDTO> productInStockDTOs = kcsService.findProductInStock(code, serial);
            List<ProductInStockDTO> productInStockIm1DTOs = kcsService.findProductInStockIm1(code, serial);

            if (DataUtil.isNullOrEmpty(productInStockDTOs) || DataUtil.isNullOrEmpty(productInStockIm1DTOs)) {
                String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.serial.not.found.error", (DataUtil.isNullOrEmpty(productInStockDTOs) ? "" : "BCCS1_"))};
                listError.add(error);
                return message;
            } else {
                ProductInStockDTO productInStockDTO = productInStockDTOs.get(0);
                ProductInStockDTO productInStockIm1DTO = productInStockIm1DTOs.get(0);

                if (productInStockDTO.getOwnerType() == null || productInStockDTO.getOwnerType() != Const.OWNER_TYPE_STAFF ||
                        productInStockIm1DTO.getOwnerType() == null || productInStockIm1DTO.getOwnerType() != Const.OWNER_TYPE_STAFF) {
                    String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.serial.in.shop.error", (productInStockDTO.getOwnerType() == null || productInStockDTO.getOwnerType() != Const.OWNER_TYPE_STAFF ? "" : "BCCS1_"))};
                    listError.add(error);
                    return message;
                }

                //
                if (productInStockDTO.getOwnerId() == null || !productInStockDTO.getOwnerId().equals(ownerId) ||
                        productInStockIm1DTO.getOwnerId() == null || !productInStockIm1DTO.getOwnerId().equals(ownerId)) {
                    String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.serial.not.in.staff.error", (productInStockDTO.getOwnerId() == null || !productInStockDTO.getOwnerId().equals(ownerId) ? "" : "BCCS1_"))};
                    listError.add(error);
                    return message;
                }

                //
                if (productInStockDTO.getStateAfterId() == null || productInStockDTO.getStateAfterId() != Long.valueOf(Const.STATUS_ACTIVE) ||
                        productInStockIm1DTO.getStateAfterId() == null || productInStockIm1DTO.getStateAfterId() != Long.valueOf(Const.STATUS_ACTIVE)) {
                    String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.serial.status.not.active.error", (productInStockDTO.getStateAfterId() == null || productInStockDTO.getStateAfterId() != Long.valueOf(Const.STATUS_ACTIVE) ? "" : "BCCS1_"))};
                    listError.add(error);
                    return message;
                }

                //
                if (productInStockDTO.getStateId() == null || productInStockDTO.getStateId() != Const.STATE_EXPIRE ||
                        productInStockIm1DTO.getStateId() == null || productInStockIm1DTO.getStateId() != Const.STATE_EXPIRE) {
                    String[] error = {(errorCount++).toString(), code, serial, state, getTextParam("view.warehouse.import.kcs.serial.state.not.expire.error", (productInStockDTO.getStateId() == null || productInStockDTO.getStateId() != Const.STATE_EXPIRE ? "" : "BCCS1_"))};
                    listError.add(error);
                    return message;
                }
            }
        } catch (Exception e) {
            String[] error = {(errorCount++).toString(), code, serial, state, getText("view.warehouse.import.kcs.check.error")};
            listError.add(error);
            logger.error(e.getMessage(), e);
        }
        message.setSuccess(true);
        return message;
    }

    public BaseMessage checkStockTotal(Long ownerId) throws Exception {
        BaseMessage message = new BaseMessage(true);
        Iterator<String> iterator = mapKCS.keySet().iterator();
        while (iterator.hasNext()) {
            String code = iterator.next();
            ProductOfferingDTO productOfferingDTO = productOfferingService.getProductByCode(code);
            //Long prodOfferId = productOfferingService.getProductByCode(code).getProductOfferingId();
            Long prodOfferId = productOfferingDTO.getProductOfferingId();
            KcsDTO kcsDTO = mapKCS.get(code);
            Long quantity = Long.valueOf(kcsDTO.getSerialList().size());

            if (!stockTotalService.checkStockTotal(ownerId, prodOfferId, quantity) ||
                    !stockTotalService.checkStockTotalIm1(ownerId, prodOfferId, quantity)) {
                StockTotalDTO stockTotalDTO = stockTotalService.getStockTotalForProcessStock(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_EXPIRE);
                StockTotalIm1DTO stockTotalIm1DTO = stockTotalService.getStockTotalForProcessStockIm1(ownerId, Const.OWNER_TYPE_STAFF, prodOfferId, Const.STATE_EXPIRE);
                if (stockTotalDTO == null || stockTotalIm1DTO == null) {
                    /*message.setDescription(MessageFormat.format(
                            getText("view.warehouse.import.kcs.stock.not.exist.error"),(stockTotalDTO == null ? "" : "BCCS1_"), code));*/
                    message.setDescription(MessageFormat.format(
                            getText("view.warehouse.import.kcs.stock.not.exist.error"), (stockTotalDTO == null ? "" : "BCCS1_"), productOfferingDTO.getName()));
                } else {
                    /*message.setDescription(MessageFormat.format(
                            getText("view.warehouse.import.kcs.stock.not.enough.error")
                            ,(!stockTotalService.checkStockTotal(ownerId, prodOfferId, quantity) ? "" : "BCCS1_")
                            , code
                            , quantity
                            , (!stockTotalService.checkStockTotal(ownerId, prodOfferId, quantity) ? stockTotalDTO.getAvailableQuantity() : stockTotalIm1DTO.getQuantity())));*/
                    message.setDescription(MessageFormat.format(
                            getText("view.warehouse.import.kcs.stock.not.enough.error")
                            , (!stockTotalService.checkStockTotal(ownerId, prodOfferId, quantity) ? "" : "BCCS1_")
                            , code));
                }
                message.setSuccess(false);
                return message;
            }
        }
        return message;
    }

    @Secured("@")
    public void downloadFileError() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + FILE_KCS_ERROR_NAME);
            externalContext.setResponseContentType("application/vnd.ms-excel");
            bookError.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void change() {
        if (DataUtil.safeEqual(typeProductOffering, "1")) {
            showStaff = false;
            showKcs = false;
            initShop();
        } else {
            showStaff = true;
            showKcs = true;
            initShop();
        }
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOfferingNew(String inputProduct) {
        try {
            String input = inputProduct != null ? inputProduct.trim() : "";
            lsProductOfferingTotalNewDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOffering(input), new ArrayList<>());
            return lsProductOfferingTotalNewDTO;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }

    /*private List<ProductOfferingTotalDTO> findByProductByNameOrCode(String input) {
        List<ProductOfferingTotalDTO> listData = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(lsProductOfferingTotalNewDTO)) {
            for (ProductOfferingTotalDTO productOffering : lsProductOfferingTotalNewDTO) {
                if (productOffering.getCode().toLowerCase().contains(input.toLowerCase())
                        || productOffering.getName().toLowerCase().contains(input.toLowerCase())) {
                    listData.add(productOffering);
                }
            }
        }
        return listData;
    }*/

    @Secured("@")
    public List<VShopStaffDTO> doChangeStaffNew(String inputStaff) {
        try {
            String input = DataUtil.isNullOrEmpty(inputStaff) ? "" : inputStaff.trim();
            //lay shopId
            String shopId = String.valueOf(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            if (curShopStaff != null) {
                shopId = curShopStaff.getOwnerId();
            }
            lsCurStaff = DataUtil.defaultIfNull(staffService.getStaffByShopId(input, shopId), new ArrayList<>());
            return lsCurStaff;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<VShopStaffDTO>();
    }

    @Kpi("ID_KPI_VIEW_INFOR_STOCK")
    @Secured("@")
    public void searchStock() {
        try {

            String ownerType = typeProductOffering;
            String ownerId = null;
            String shopName = "";
            String shopCode = "";
            if (ownerType.equals(Const.OWNER_TYPE.SHOP)) {
                if (curShopStaff != null) {
                    ownerId = curShopStaff.getOwnerId();
                    shopName = curShopStaff.getOwnerName();
                    shopCode = curShopStaff.getOwnerCode();
                }
            } else if (ownerType.equals(Const.OWNER_TYPE.STAFF)) {
                if (curStaff != null) {
                    ownerId = curStaff.getOwnerId();
                }
            }
            Long prodOfferId = null;
            if (productOfferingTotalNewDTO != null) {
//                stockCode = productOfferingTotalNewDTO.getCode();
                prodOfferId = productOfferingTotalNewDTO.getProductOfferingId();
            }

            //lay danh sach productOfferType
            List<ProductOfferingDTO> productOfferingList = productOfferingService.getListProductOfferTypeViewer(ownerType, ownerId, prodOfferId, null);
            //lay danh sach tock_total
            Map<Long, List<ProductOfferingTotalDTO>> map = new HashMap<Long, List<ProductOfferingTotalDTO>>();

            List<ProductOfferingTotalDTO> listStock = productOfferingService.getListProductOfferingViewer(ownerType, ownerId, prodOfferId);
            if (listStock != null) {
                for (ProductOfferingTotalDTO stockTotal : listStock) {
                    List<ProductOfferingTotalDTO> listStockChild = map.get(stockTotal.getProductOfferTypeId());
                    if (DataUtil.isNullOrEmpty(listStockChild)) {
                        listStockChild = new ArrayList<ProductOfferingTotalDTO>();
                        listStockChild.add(stockTotal);
                        map.put(stockTotal.getProductOfferTypeId(), listStockChild);
                    } else {
                        listStockChild.add(stockTotal);
                        map.put(stockTotal.getProductOfferTypeId(), listStockChild);
                    }
                }
            }

            //Tao rootStock
            rootStock = new DefaultTreeNode(new WareHouseInfoDTO(), null);
            TreeNode groupStock;
            Long requirementQuanlity;
            if (!DataUtil.isNullOrEmpty(productOfferingList)) {
                for (ProductOfferingDTO product : productOfferingList) {
                    groupStock = new DefaultTreeNode(new WareHouseInfoDTO(product.getName()), rootStock);
                    List<ProductOfferingTotalDTO> listStockChild = map.get(product.getProductOfferTypeId());
                    if (!DataUtil.isNullOrEmpty(listStockChild)) {
                        for (ProductOfferingTotalDTO productTotal : listStockChild) {
                            requirementQuanlity = productTotal.getCurrentQuantity() - productTotal.getAvailableQuantity();
                            WareHouseInfoDTO dto = new WareHouseInfoDTO(productTotal.getCode(), productTotal.getStateName(), productTotal.getUnit(),
                                    requirementQuanlity, productTotal.getAvailableQuantity(), productTotal.getCurrentQuantity(),
                                    String.valueOf(productTotal.getProductOfferTypeId()), String.valueOf(productTotal.getProductOfferingId()),
                                    String.valueOf(productTotal.getOwnerType()), String.valueOf(productTotal.getOwnerId()),
                                    productTotal.getCheckSerial(), DataUtil.safeToString(productTotal.getStateId()));
                            dto.setProductOfferTypeId(DataUtil.safeToString(product.getProductOfferTypeId()));
                            dto.setOwnerType(ownerType);
                            dto.setShopName(shopName);
                            dto.setShopCode(shopCode);
                            dto.setProductCode(productTotal.getCode());
                            dto.setProductName(productTotal.getName());
                            dto.setAccountingModelCode(productTotal.getAccountingModelCode());
                            new DefaultTreeNode(dto, groupStock);
                        }
                    }
                }
            }
            topPage();
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

    public boolean isShowExport() {
        return rootStock != null && rootStock.getChildren() != null && rootStock.getChildren().size() > 0;
    }

    @Secured("@")
    public void showDialogSerial(WareHouseInfoDTO wareHouseInfoDTO) {
        try {
            itemDTO = wareHouseInfoDTO;
            itemDTO.setIsExport(false);
            lsSerial = stockTransSerialService.findStockTransSerialByProductOfferType(itemDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void showDialogViewTransSale(WareHouseInfoDTO wareHouseInfoDTO) {
        try {
            itemDTO = DataUtil.cloneBean(wareHouseInfoDTO);
            String teamCode = productWs.findTeamCodeByShopCode(itemDTO.getShopCode());
            if (DataUtil.isNullOrEmpty(teamCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "view.stock.product.ws.find.teamcode.empty");
            }
            List<SubGoodsDTO> lstSubGood = saleWs.getLsSubGoodsByTeamCodeAndProdOfferId(teamCode, DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId()));
            if (DataUtil.isNullOrEmpty(lstSubGood)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "view.stock.sale.ws.find.subgood.empty");
            }
            List<Long> lsQuantity = stockTotalService.getTotalQuantityStockTotal(DataUtil.safeToLong(itemDTO.getProductOfferingId()), DataUtil.safeToLong(curShopStaff.getOwnerId()));
            if (!DataUtil.isNullOrEmpty(lsQuantity) && lsQuantity.size() > 1) {
                itemDTO.setCurrentQuanlity(lsQuantity.get(0));
                itemDTO.setAvailableQuanlity(lsQuantity.get(1));
            }
            itemDTO.setLstGoods(lstSubGood);
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
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            curShopStaff = vShopStaffDTO;
            //set lai list value cua textComplete hang hoa
            lsProductOfferingTotalNewDTO = productOfferingService.getListProductOfferingTotalDistinct(curShopStaff.getOwnerType(), curShopStaff.getOwnerId(), null);

            //set lai list value cua text nhan vien
            lsCurStaff = DataUtil.defaultIfNull(staffService.getStaffByShopId(null, DataUtil.safeToString(curShopStaff.getOwnerId()))
                    , new ArrayList<VShopStaffDTO>());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveStaff() {
        try {
            //set lai list value cua textComplete hang hoa
            lsProductOfferingTotalNewDTO = productOfferingService.getListProductOfferingTotalDistinct(curStaff.getOwnerType(), curStaff.getOwnerId(), null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearShop() {
        curShopStaff = null;
    }

    @Secured("@")
    public void receiveProduct() {

    }

    @Secured("@")
    public void resetProduct() {
        productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
    }


    @Secured("@")
    public void receiveShop() {
        curShopStaff = null;
        curStaff = null;
        updateElemetId("frmViewInforWarehouse:pnStaff");
    }

    @Secured("@")
    public void clearStaff() {
        curStaff = null;
    }

    @Secured("@")
    public StreamedContent exportFile() {

        try {
            //get data
            String ownerType = typeProductOffering;
            String ownerId = null;
            if (ownerType.equals(Const.OWNER_TYPE.SHOP)) {
                if (curShopStaff != null) {
                    ownerId = curShopStaff.getOwnerId();
                }
            } else if (ownerType.equals(Const.OWNER_TYPE.STAFF)) {
                if (curStaff != null) {
                    ownerId = curStaff.getOwnerId();
                }
            }
            Long prodOfferId = null;
            if (productOfferingTotalNewDTO != null) {
//                stockCode = productOfferingTotalNewDTO.getCode();
                prodOfferId = productOfferingTotalNewDTO.getProductOfferingId();
            }
            List<ProductOfferingTotalDTO> listStock = productOfferingService.getListProductOfferingViewer(ownerType, ownerId, prodOfferId);

            //write data
            String sourceFilePath = FileUtil.getTemplatePath() + "stock_view_template.xlsx";
            String timeTemp = String.valueOf(System.nanoTime());

            String fileNameOut = "stock_view_" + timeTemp + ".xlsx";
            ExcelWriterUtils excelUtil = new ExcelWriterUtils();


            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(sourceFilePath));
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            //kho hang
            if (curShopStaff != null) {
                excelUtil.createCellNoStyle(xssfSheet, 2, 6, curShopStaff.getOwnerName());
            }
            //thoi gian bao cao
            excelUtil.createCellNoStyle(xssfSheet, 2, 7, DateUtil.date2ddMMyyyyString(new Date()));
            //nguoi xuat bao cao
            excelUtil.createCellNoStyle(xssfSheet, 2, 8, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());


            SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWorkbook);
            if (DataUtil.isNullObject(workbook)) {
                logger.info("file khong ton tai");
            }
            Sheet tmpSheet = workbook.getSheetAt(0);

            CellStyle csString = excelUtil.createCellStyle(workbook);
            int row = 11;
            int stt = 1;
            String temp;

            if (listStock != null) {
                Long requirementQuanlity;
                //write data to file
                for (ProductOfferingTotalDTO product : listStock) {
                    int column = 0;
                    excelUtil.createCell(tmpSheet, column, row, String.valueOf(stt++)).setCellStyle(csString);
                    column++;
                    temp = product.getProductOfferTypeName();
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = product.getName();
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = product.getStateName();
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = product.getUnit();
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    requirementQuanlity = product.getCurrentQuantity() - product.getAvailableQuantity();
                    temp = DataUtil.safeToString(requirementQuanlity);
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = String.valueOf(product.getAvailableQuantity());
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = String.valueOf(product.getCurrentQuantity());
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    row++;
                }
            }

//            OutputStream outputStream = new FileOutputStream(fileOut);
//            workbook.write(outputStream);
//            InputStream is = new FileInputStream(fileOut);
//            return new DefaultStreamedContent(is, FacesContext.getCurrentInstance().getExternalContext().getMimeType(fileNameOut), fileNameOut);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            bos.close();
            return new DefaultStreamedContent(new ByteArrayInputStream(bos.toByteArray()), "application/xlsx", fileNameOut);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }
        return null;
    }

    private void helperSystem() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String sys = request.getParameter(Const.SYSTEM_SALE);
        directLink = DataUtil.safeEqual(sys, Const.SYSTEM_SALE);
    }


    @Secured("@")
    public StreamedContent exportFileSerial() {
        try {
            String stockName = itemDTO.getName();
            String stateName = itemDTO.getStateName();
            String unit = itemDTO.getUnit();

            itemDTO.setIsExport(true);
            //get data
            lsSerial = stockTransSerialService.findStockTransSerialByProductOfferType(itemDTO);

            //write data
            String sourceFilePath = FileUtil.getTemplatePath() + "stock_serial_view_template.xlsx";
            String timeTemp = String.valueOf(System.nanoTime());

            String fileNameOut = "stock_serial_view_" + timeTemp + ".xlsx";
            ExcelWriterUtils excelUtil = new ExcelWriterUtils();

            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(sourceFilePath));
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            //kho hang
            excelUtil.createCellNoStyle(xssfSheet, 2, 5, curShopStaff.getOwnerName());
            //ten hang hoa
            excelUtil.createCellNoStyle(xssfSheet, 2, 6, stockName);
            //trang thai
            excelUtil.createCellNoStyle(xssfSheet, 2, 7, stateName);
            //don vi tinh
            excelUtil.createCellNoStyle(xssfSheet, 2, 8, unit);


            SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWorkbook);
            if (DataUtil.isNullObject(workbook)) {
                logger.info("file khong ton tai");
            }
            Sheet tmpSheet = workbook.getSheetAt(0);

            CellStyle csString = excelUtil.createCellStyle(workbook);

            int row = 11;
            int stt = 1;
            String temp;

            if (lsSerial != null) {
                //write data to file
                for (StockTransSerialDTO stockSerial : lsSerial) {
                    int column = 0;
                    excelUtil.createCell(tmpSheet, column, row, String.valueOf(stt++)).setCellStyle(csString);
                    column++;
                    temp = stockSerial.getFromSerial();
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = stockSerial.getToSerial();
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    column++;

                    temp = String.valueOf(stockSerial.getQuantity());
                    if (DataUtil.isNullOrEmpty(temp)) {
                        excelUtil.createCell(tmpSheet, column, row, "").setCellStyle(csString);
                    } else {
                        excelUtil.createCell(tmpSheet, column, row, temp).setCellStyle(csString);
                    }
                    row++;
                }

            }

//            OutputStream outputStream = new FileOutputStream(fileOut);
//            workbook.write(outputStream);
//            InputStream is = new FileInputStream(fileOut);
//            return new DefaultStreamedContent(is, FacesContext.getCurrentInstance().getExternalContext().getMimeType(fileNameOut), fileNameOut);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            bos.close();
            return new DefaultStreamedContent(new ByteArrayInputStream(bos.toByteArray()), "application/xlsx", fileNameOut);
        } catch (Exception ex) {
            reportError("", "common.error.happen", ex);
            topPage();
        }
        return null;
    }


    @Secured("@")
    public StreamedContent exportFileDetail() {
        try {
            //get data
            fileNameOutDownload = null;
            String ownerType = typeProductOffering;
            String ownerId = null;
            String ownerName = "";
            if (ownerType.equals(Const.OWNER_TYPE.SHOP)) {
                if (curShopStaff != null) {
                    ownerId = curShopStaff.getOwnerId();
                    ownerName = curShopStaff.getOwnerName();
                }
            } else if (ownerType.equals(Const.OWNER_TYPE.STAFF)) {
                if (curStaff != null) {
                    ownerId = curStaff.getOwnerId();
                    ownerName = curStaff.getOwnerName();
                }
            }
            String stockCode = null;
            if (productOfferingTotalNewDTO != null) {
                stockCode = productOfferingTotalNewDTO.getCode();
            }
            List<StockTotalWsDTO> lstStockGoods = DataUtil.defaultIfNull(stockTotalService.getStockTotalDetail(DataUtil.safeToLong(ownerId),
                    DataUtil.safeToLong(ownerType), stockCode), new ArrayList<>());

            Date sysDate = getSysdateFromDB();
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("stock_serial_report_" + DateUtil.date2LongUpdateDateTime(sysDate) + ".xls");
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName("stock_serial_report.xls");
            fileExportBean.putValue("dateCreate", DateUtil.date2ddMMyyyyString(sysDate));
            fileExportBean.putValue("ownerName", ownerName);
            fileExportBean.putValue("lstStockGoods", lstStockGoods);
            fileNameOutDownload = fileExportBean.getOutName();
            return FileUtil.exportToStreamed(fileExportBean);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happen", ex);
            topPage();
        }
        return null;
    }


    @Secured("@")
    public boolean getShowDownloadLink() {
        return !DataUtil.isNullObject(fileNameOutDownload);
    }

    //getter and setter
    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public VShopStaffDTO getCurStaff() {
        return curStaff;
    }

    public void setCurStaff(VShopStaffDTO curStaff) {
        this.curStaff = curStaff;
    }

    public List<VShopStaffDTO> getLsCurStaff() {
        return lsCurStaff;
    }

    public void setLsCurStaff(List<VShopStaffDTO> lsCurStaff) {
        this.lsCurStaff = lsCurStaff;
    }

    public String getTypeProductOffering() {
        return typeProductOffering;
    }

    public boolean isShowStaff() {
        return showStaff;
    }

    public void setShowStaff(boolean showStaff) {
        this.showStaff = showStaff;
    }

    public void setTypeProductOffering(String typeProductOffering) {
        this.typeProductOffering = typeProductOffering;
    }

    public String getProductOfferingCode() {
        return productOfferingCode;
    }

    public void setProductOfferingCode(String productOfferingCode) {
        this.productOfferingCode = productOfferingCode;
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalNewDTO() {
        return productOfferingTotalNewDTO;
    }

    public void setProductOfferingTotalNewDTO(ProductOfferingTotalDTO productOfferingTotalNewDTO) {
        this.productOfferingTotalNewDTO = productOfferingTotalNewDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalNewDTO() {
        return lsProductOfferingTotalNewDTO;
    }

    public void setLsProductOfferingTotalNewDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalNewDTO) {
        this.lsProductOfferingTotalNewDTO = lsProductOfferingTotalNewDTO;
    }

    public String getItemStock() {
        return itemStock;
    }

    public void setItemStock(String itemStock) {
        this.itemStock = itemStock;
    }

    public VShopStaffDTO getCurShopStaff() {
        return curShopStaff;
    }

    public void setCurShopStaff(VShopStaffDTO curShopStaff) {
        this.curShopStaff = curShopStaff;
    }

    public TreeNode getRootStock() {
        return rootStock;
    }

    public void setRootStock(TreeNode rootStock) {
        this.rootStock = rootStock;
    }

    public boolean isDirectLink() {
        return directLink;
    }

    public void setDirectLink(boolean directLink) {
        this.directLink = directLink;
    }

    public boolean isDisableUnit() {
        return disableUnit;
    }

    public void setDisableUnit(boolean disableUnit) {
        this.disableUnit = disableUnit;
    }

    public WareHouseInfoDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(WareHouseInfoDTO itemDTO) {
        this.itemDTO = itemDTO;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<String[]> getListError() {
        return listError;
    }

    public void setListError(List<String[]> listError) {
        this.listError = listError;
    }

    public Workbook getBookError() {
        return bookError;
    }

    public void setBookError(Workbook bookError) {
        this.bookError = bookError;
    }

    public byte[] getContentByte() {
        return contentByte;
    }

    public void setContentByte(byte[] contentByte) {
        this.contentByte = contentByte;
    }

    public boolean isShowKcs() {
        return showKcs;
    }

    public void setShowKcs(boolean showKcs) {
        this.showKcs = showKcs;
    }
}

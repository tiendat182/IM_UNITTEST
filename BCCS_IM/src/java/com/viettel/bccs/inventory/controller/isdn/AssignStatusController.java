package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.IsdnUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.impl.ShopInfoTag;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.annotation.Security;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
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
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by ChungNV7 on 12/21/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "assignStatusController")
public class AssignStatusController extends InventoryController {

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private StockNumberService stockNumberService;

    @Autowired
    private ShopInfoNameable shopInfoTag;
    //danh sach loai dich vu
    private List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList();
    //danh sach shopcode
    private List<ShopDTO> listShop = Lists.newArrayList();
    private List<ShopDTO> filterShop = Lists.newArrayList();
    //tim kiem stocknumber
    private SearchNumberRangeDTO searchStockNumberDTO = new SearchNumberRangeDTO();
    //danh sach ket qua
    private List<TableNumberRangeDTO> listRangeGrant = Lists.newArrayList();
    private List<TableNumberRangeDTO> listRangeGrantSelected = Lists.newArrayList();
    //type service
    private String typeService;
    //acttach filename
    private String attachFileName;
    private VShopStaffDTO curShopStaff;
    private boolean renderedFromFile = false;
    private boolean showAttachFile = false;

    private static final String SELECT_FROM_FILE = "selectFromFile";
    private static final String ENTERED_DIGITAL_RANGES = "enteredDigitalRanges";

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 11;
    private static final int INT_NEW = 1;
    private static final int INT_SUSPEND = 3;
    private static final String STR_NEW = "NEW";
    private static final String STR_SUSPEND = "SUSPEND";
    private static final int IN_USE = 2;
    private static final int AAA = 4;
    private static final String MOBILE_TYPE = "1";
    private int tabIndex;
    private ExcellUtil processingFile;

    //    private static final String FILE_ERROR_TEMPLATE_PATH = "D:\\SVN\\BCCS2\\06.SOURCE\\Draft\\BCCS_INVENTORY\\BCCS_IM\\web\\resources\\templates\\assignIsdnStatusPattern_error.xls";
//    private static final String FILE_TEMPLATE_PATH = "D:\\SVN\\BCCS2\\06.SOURCE\\Draft\\BCCS_INVENTORY\\BCCS_IM\\web\\resources\\templates\\assignIsdnStatusPattern.xls";
    private static final String FILE_ERROR_TEMPLATE_PATH = "assignIsdnStatusPattern_error.xls";
    private static final String FILE_TEMPLATE_PATH = "assignIsdnStatusPattern.xls";
    private static final long MAX_NUMBER = 100000;

    //map isdn,status
    private HashMap<String, String> mapISDN = new HashMap<String, String>();
    private HashMap<String, String> mapISDNError = new HashMap<String, String>();
    private List<String> lstError = new ArrayList<String>();

    //file error
    private HSSFWorkbook workbookError = null;
    //upload file
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private boolean hasFileError = false;

    @PostConstruct
    @Security("@")
    public void init() {
        initOptionSet();
        shopInfoTag.initAllShop(this);
        typeService = SELECT_FROM_FILE;
        renderedFromFile = false;
        hasFileError = false;
    }

    public void initOptionSet() {
        try {
//            listShop = shopService.findAll();
            List<OptionSetValueDTO> listOptionSet;
            listOptionSet = optionSetValueService.getByOptionSetCode(Const.TELCO_FOR_NUMBER);
            if (listOptionSet != null && listOptionSet.size() > 1) {
                Collections.sort(listOptionSet, new Comparator<OptionSetValueDTO>() {
                    public int compare(OptionSetValueDTO o1, OptionSetValueDTO o2) {
                        return Long.valueOf(o1.getValue()).compareTo(Long.valueOf(o2.getValue()));
                    }
                });
            }
            optionSetValueDTOs.addAll(listOptionSet);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        curShopStaff = DataUtil.cloneBean(vShopStaffDTO);
    }


    @Secured("@")
    public void changeTypeService() {
        if (DataUtil.safeEqual(typeService, SELECT_FROM_FILE)) {
            renderedFromFile = true;
        } else {
            renderedFromFile = false;
        }
    }

    @Secured("@")
    public void clearCurrentShop() {
        curShopStaff = new VShopStaffDTO();
    }

    @Secured("@")
    public void previewStatus(boolean renderedFromFile) {

        //validate searchStockDTO
        if (searchStockNumberDTO == null) {
            reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
            return;
        }
        if (!renderedFromFile) {
            if (!DataUtil.isNullObject(curShopStaff)) {
                searchStockNumberDTO.setShopId(DataUtil.safeToLong(curShopStaff.getOwnerId()));
            }
        }
        listRangeGrant = Lists.newArrayList();
        if (!renderedFromFile) {
            //start phai la so co do dai thoa man
//            if (MOBILE_TYPE.equals(searchStockNumberDTO.getServiceType())) {
//                if ((searchStockNumberDTO.getStartRange().length() < 9) || searchStockNumberDTO.getStartRange().length() > 10) {
//                    reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.mobile.length");
//                    return;
//                }
//            }
            if (!IsdnUtil.validateNumberAndLength(searchStockNumberDTO.getStartRange(), MIN_LENGTH, MAX_LENGTH)) {
                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.from.length");
                return;
            }
            //to phai la so va co do dai thoa man
            if (!IsdnUtil.validateNumberAndLength(searchStockNumberDTO.getEndRange(), MIN_LENGTH, MAX_LENGTH)) {
                focusElementByRawCSSSlector(".fromNumberTxt");
                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.to.length");
                return;
            }
            //kiem tra length cua so dau dai va so cuoi dai
            if (!DataUtil.isNullOrEmpty(searchStockNumberDTO.getStartRange())
                    && !DataUtil.isNullOrEmpty(searchStockNumberDTO.getEndRange())) {
                if (searchStockNumberDTO.getStartRange().length() != searchStockNumberDTO.getEndRange().length()) {
                    focusElementByRawCSSSlector(".clFromTo");
                    reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.range.matchlength");
                    return;
                }
                long start = Long.parseLong(searchStockNumberDTO.getStartRange());
                long end = Long.parseLong(searchStockNumberDTO.getEndRange());
                if ((end < start) || ((end - start) > MAX_NUMBER)) {
                    reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.start.end.notmatch");
                    return;
                }
            }
            //len danh sach
            try {
                List<TableNumberRangeDTO> listRange = stockNumberService.getListRangeGrant(searchStockNumberDTO);
                if (listRange != null) {
                    for (TableNumberRangeDTO dto : listRange) {
                        dto.setStatusName(BundleUtil.getText(DataUtil.defaultIfNull(getStrStatus(dto.getStatus()), "")));
                        dto.setOldStatusName(BundleUtil.getText(DataUtil.defaultIfNull(getStrStatus(dto.getOldStatus()), "")));
                    }
                    listRangeGrant.addAll(listRange);
                    listRangeGrantSelected = Lists.newArrayList();
                }
//                if (DataUtil.isNullOrEmpty(listRange)) {
//                    reportSuccess("frmAssignStatus:assignTypeMsgs", "common.emty.records");
//                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
                searchStockNumberDTO = new SearchNumberRangeDTO();
            }
        } else {
            if (uploadedFile == null || byteContent == null) {
                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.file.noselect");
                return;
            }

//            searchStockNumberDTO.setServiceType("1");
            //len danh sach
            try {
                //duyet file
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }

                int processStatus = processInputExcel();
                //validate noi dung
                if (processStatus == -1) {
                    reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.file.empty");
                    return;
                }
                if (processStatus == -2) {
                    return;
                }
                if (processStatus == -3) {
                    reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.file.max");
                    return;
                }
                if (DataUtil.isNullOrEmpty(searchStockNumberDTO.getMapISDN())) {
                    reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.file.empty");
                    return;
                }
                //kiem tra danh sach thue bao khong hop le
                lstError = stockNumberService.checkListStockNumber(searchStockNumberDTO);
                if (!DataUtil.isNullOrEmpty(lstError)) {
                    hasFileError = true;
//                    if (!DataUtil.isNullOrEmpty(searchStockNumberDTO.getMapISDN())) {
//                        for (int i = 0; i < lstError.size(); i++) {
//                            if (searchStockNumberDTO.getMapISDN().containsKey(lstError.get(i))) {
//                                searchStockNumberDTO.getMapISDN().remove(lstError.get(i));
//                            }
//                        }
//                    }
                }
                //len danh sach
                List<TableNumberRangeDTO> listRange = stockNumberService.getListRangeGrantFromFile(searchStockNumberDTO);
                if (listRange != null) {
                    listRangeGrant.addAll(listRange);
                    listRangeGrantSelected = Lists.newArrayList();
                }
//                if (DataUtil.isNullOrEmpty(listRange)) {
//                    reportSuccess("frmAssignStatus:assignTypeMsgs", "common.emty.records");
//                }
            } catch (LogicException ex) {
                reportError("", ex);
                topPage();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
//                searchStockNumberDTO = new SearchNumberRangeDTO();
            }
        }
    }


    @Secured("@")
    public void doConfirmStatusUpdate() {
//        int i = listRangeGrant.size();
        if (DataUtil.isNullOrEmpty(listRangeGrantSelected)) {
            reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.noselect");
            return;
        }

    }

    @Secured("@")
    public String getMessageConfirm() {
        if (!DataUtil.isNullObject(searchStockNumberDTO) && !DataUtil.isNullOrEmpty(searchStockNumberDTO.getStatus())) {
            return getText("mn.stock.status.update.confirm.msg").replace("{0}", getText(getStrStatus(searchStockNumberDTO.getStatus())));
        }
        return getText("mn.stock.status.update.confirm.msg.file");
    }

    @Secured("@")
    public void doUpdateStatus() {
        try {
            if (DataUtil.isNullOrEmpty(listRangeGrantSelected)) {
                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.update.noselect");
                return;
            }
            if (DataUtil.isNullObject(searchStockNumberDTO) || DataUtil.isNullOrEmpty(searchStockNumberDTO.getStatus())) {
                reportErrorValidateFail("frmdlgAssignStatus:msgDlgAssignStatus", "", "mn.stock.status.dlg.assign.newstatus.valid");
                return;
            }
            stockNumberService.updateRangeGrant(listRangeGrantSelected, BccsLoginSuccessHandler.getStaffDTO(), BccsLoginSuccessHandler.getIpAddress(), searchStockNumberDTO);
            showAttachFile = false;
            searchStockNumberDTO = new SearchNumberRangeDTO();
            hasFileError = false;
            listRangeGrantSelected = Lists.newArrayList();
            reportSuccess("", "mn.stock.status.isdn.update.success");
            return;
        } catch (LogicException le) {
            logger.error(le);
            reportError("", "", le.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("", "", "common.error.happened");
            return;
        }
    }

    @Secured("@")
    public void doUpdateStatusFromFile() {
        try {
            processInputExcel();
            if (tabIndex == 0) {
                for (int i = 0; i < listRangeGrantSelected.size(); i++) {
                    listRangeGrantSelected.get(i).setStatus(searchStockNumberDTO.getStatus());
                    listRangeGrantSelected.get(i).setStatusName(BundleUtil.getText(DataUtil.defaultIfNull(getStrStatus(searchStockNumberDTO.getStatus()), "")));
                }
            } else {
                for (int i = 0; i < listRangeGrantSelected.size(); i++) {
                    listRangeGrantSelected.get(i).setStatusName(BundleUtil.getText(DataUtil.defaultIfNull(getStrStatus(listRangeGrantSelected.get(i).getStatus()), "")));
                    listRangeGrantSelected.get(i).setOldStatusName(BundleUtil.getText(DataUtil.defaultIfNull(getStrStatus(listRangeGrantSelected.get(i).getOldStatus()), "")));
                }
            }
            stockNumberService.updateRangeGrant(listRangeGrantSelected, BccsLoginSuccessHandler.getStaffDTO(), BccsLoginSuccessHandler.getIpAddress(), searchStockNumberDTO);
            showAttachFile = false;
            searchStockNumberDTO = new SearchNumberRangeDTO();
            hasFileError = false;
            listRangeGrantSelected = Lists.newArrayList();
            reportSuccess("frmAssignStatus:assignTypeMsgs", "mn.stock.status.isdn.update.success");
            return;
        } catch (LogicException le) {
            logger.error(le);
            reportError("", "", le.getKeyMsg());
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
            return;
        }
    }

    @Secured("@")
    public String getTelecomServiceName(String id) {
        if (!DataUtil.isNullOrEmpty(optionSetValueDTOs)) {
            for (OptionSetValueDTO dto : optionSetValueDTOs) {
                if (Objects.equal(dto.getValue(), id)) {
                    return dto.getName();
                }
            }
        }
        return id;
    }

    @Secured("@")
    public void doReset() {
        searchStockNumberDTO = new SearchNumberRangeDTO();
        hasFileError = false;
        attachFileName = null;
        showAttachFile = false;
        shopInfoTag = new ShopInfoTag();
        uploadedFile = null;
        listRangeGrantSelected = Lists.newArrayList();
        renderedFromFile = false;
    }

    @Secured("@")
    public void onChangeRange(String areaId, String field, int type) {
        String fieldFormat = field.replaceAll("[^0-9]", "");
        if (!DataUtil.isNullObject(searchStockNumberDTO.getServiceType()) && "1".equals(searchStockNumberDTO.getServiceType())) {
            fieldFormat = fieldFormat.replaceAll("^0+", "");
        }
        if (type == 1) {
            searchStockNumberDTO.setStartRange(fieldFormat);
        } else {
            searchStockNumberDTO.setEndRange(fieldFormat);
        }
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Secured("@")
    public void onChangeStartRange(String areaId) {
        String field = searchStockNumberDTO.getStartRange();
        field = field.replaceAll("[^0-9]", "");
        if (!DataUtil.isNullObject(searchStockNumberDTO.getServiceType()) && "1".equals(searchStockNumberDTO.getServiceType())) {
            field = field.replaceAll("^0+", "");
        }
        searchStockNumberDTO.setStartRange(field);
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Secured("@")
    public void onChangeEndRange(String areaId) {
        String field = searchStockNumberDTO.getEndRange();
        field = field.replaceAll("[^0-9]", "");
        if (!DataUtil.isNullObject(searchStockNumberDTO.getServiceType()) && "1".equals(searchStockNumberDTO.getServiceType())) {
            field = field.replaceAll("^0+", "");
        }
        searchStockNumberDTO.setEndRange(field);
        RequestContext.getCurrentInstance().update(areaId);
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            hasFileError = false;
            mapISDN = new HashMap<String, String>();
            mapISDNError = new HashMap<String, String>();
            lstError = new ArrayList<String>();
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            byteContent = uploadedFile.getContents();
            showAttachFile = true;
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

    public int processInputExcel() {
        try {
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            if (totalRow == 0 || totalRow == 1) {
                return -1;
            }
            if (totalRow > 1000) {
                return -3;
            }
            searchStockNumberDTO.setMapISDN(new HashMap<String, String>());
            mapISDN = searchStockNumberDTO.getMapISDN();
            mapISDNError = new HashMap<String, String>();
            for (int i = 1; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    hasFileError = true;
                    break;
                }
                int totalCol = processingFile.getTotalColumnAtRow(row);
                if (totalCol < 2) {
                    hasFileError = true;
                    continue;
                }
                String isdn = processingFile.getStringValue(row.getCell(0));
                if (isdn == null) {
                    hasFileError = true;
                    continue;
                }
                String status = processingFile.getStringValue(row.getCell(1));
                if (!DataUtil.isNumber(isdn) || !IsdnUtil.validateNumberAndLength(isdn, MIN_LENGTH, MAX_LENGTH)) {
                    hasFileError = true;
                    continue;
                }
                int newStatus = checkStatus(status);
                if (newStatus != INT_NEW && newStatus != INT_SUSPEND) {
                    hasFileError = true;
                    continue;
                } else {
                    mapISDN.put(isdn, newStatus + "");
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            return -1;
        } catch (Exception ex) {
            //  ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            return -1;
        }
        return 0;
    }

    public int checkStatus(String newStatus) {
        if (STR_NEW.equals(newStatus)) {
            return INT_NEW;
        }
        if (STR_SUSPEND.equals(newStatus)) {
            return INT_SUSPEND;
        }
        return 0;
    }

    @Secured("@")
    public String getStrStatus(String status) {
        if (("" + INT_NEW).equals(status)) {
            return "mn.stock.status.isdn.update.status.new";
        }
        if (("" + INT_SUSPEND).equals(status)) {
            return "mn.stock.status.isdn.update.status.suspend";
        }
        if (("" + IN_USE).equals(status)) {
            return "mn.stock.status.isdn.update.status.inuse";
        }
        if (("" + AAA).equals(status)) {
            return "mn.stock.status.isdn.update.status.aaa";
        }
        return null;
    }

    @Secured("@")
    public String formatNumber(Long total) {
        DecimalFormat format = new DecimalFormat("####,###.###");
        return format.format(total).replace(",", ".");
    }

    @Secured("@")
    public List<ShopDTO> searchShop(String keyword) {
        filterShop = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(listShop)) {
            for (ShopDTO shop : listShop) {
                if (shop.getName().contains(keyword)) {
                    filterShop.add(shop);
                }
            }
        }
        return filterShop;
    }

    @Secured("@")
    public void selectShopSearch(SelectEvent event) {
        try {
            ShopDTO shop = ((ShopDTO) event.getObject());
            searchStockNumberDTO.setShopId(shop.getShopId());
            searchStockNumberDTO.setShopCode(shop.getShopCode());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmManageBrasIpPool:msgbrasIppool", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void downloadFileError() {
        try {
//            searchStockNumberDTO.setEndRange("123456789");
//            searchStockNumberDTO.setStartRange("123456789");
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_ERROR_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            Sheet errorSheet = workbook.getSheetAt(0);
            Sheet sheetUpload = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetUpload);
            if (totalRow == 0 || totalRow == 1) {
//                reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "common.error.happened");
                hasFileError = false;
                return;
            }
            //ghi file loi, bo dong header
            int j = 1;
            for (int i = 1; i < totalRow; i++) {
                Row rowUpload = sheetUpload.getRow(i);
                if (rowUpload == null) {
                    Row errorRow = errorSheet.createRow(j);
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.row.null"));
                    //sang ghi row tiep
                    j++;
                    continue;
                }
                int totalCol = processingFile.getTotalColumnAtRow(rowUpload);
                if (totalCol < 2) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.update.file.isdn.status.wrongformat"));
                    j++;
                    continue;
                }
                String isdn = processingFile.getStringValue(rowUpload.getCell(0));
                if (isdn == null) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.isdn.null"));
                    j++;
                    continue;
                }
                //kiem tra isdn
                if (!DataUtil.isNumber(isdn) || !IsdnUtil.validateNumberAndLength(isdn, MIN_LENGTH, MAX_LENGTH)) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.isdn.number"));
                    j++;
                    continue;
                }
                //validate status co dung dinh dang khong
                String status = processingFile.getStringValue(rowUpload.getCell(1));
                int newStatus = checkStatus(status);
                if (newStatus != INT_NEW && newStatus != INT_SUSPEND) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.status"));
                    j++;
                    continue;
                }
                //kiem tra isdn co trong list error khong
                if (lstError != null && lstError.contains(isdn)) {
                    hasFileError = true;
                    Row errorRow = errorSheet.createRow(j);
                    for (int k = 0; k < totalCol; k++) {
                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(rowUpload.getCell(k)));
                    }
                    errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.update.file.isdn.status.wrongisdn"));
                    j++;
                    continue;
                }
            }
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "error.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
//        return null;
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
//            byte[] content = new byte[1];
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "assignIsdnStatusPattern.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
//        return null;
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        listRangeGrant = Lists.newArrayList();
        listRangeGrantSelected = Lists.newArrayList();
        searchStockNumberDTO = new SearchNumberRangeDTO();
        uploadedFile = null;
        byteContent = null;

        if (tabIndex == 0) {
            renderedFromFile = false;
        } else {
            renderedFromFile = true;
        }
    }

    public static String getSelectFromFile() {
        return SELECT_FROM_FILE;
    }

    public static String getEnteredDigitalRanges() {
        return ENTERED_DIGITAL_RANGES;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOs() {
        return optionSetValueDTOs;
    }

    public void setOptionSetValueDTOs(List<OptionSetValueDTO> optionSetValueDTOs) {
        this.optionSetValueDTOs = optionSetValueDTOs;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public boolean isRenderedFromFile() {
        return renderedFromFile;
    }

    public void setRenderedFromFile(boolean renderedFromFile) {
        this.renderedFromFile = renderedFromFile;
    }

    public SearchNumberRangeDTO getSearchStockNumberDTO() {
        return searchStockNumberDTO;
    }

    public void setSearchStockNumberDTO(SearchNumberRangeDTO searchStockNumberDTO) {
        this.searchStockNumberDTO = searchStockNumberDTO;
    }

    public List<ShopDTO> getListShop() {
        return listShop;
    }

    public void setListShop(List<ShopDTO> listShop) {
        this.listShop = listShop;
    }

    public List<TableNumberRangeDTO> getListRangeGrant() {
        return listRangeGrant;
    }

    public void setListRangeGrant(List<TableNumberRangeDTO> listRangeGrant) {
        this.listRangeGrant = listRangeGrant;
    }

    public List<TableNumberRangeDTO> getListRangeGrantSelected() {
        return listRangeGrantSelected;
    }

    public void setListRangeGrantSelected(List<TableNumberRangeDTO> listRangeGrantSelected) {
        this.listRangeGrantSelected = listRangeGrantSelected;
    }

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public List<ShopDTO> getFilterShop() {
        return filterShop;
    }

    public void setFilterShop(List<ShopDTO> filterShop) {
        this.filterShop = filterShop;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public boolean isShowAttachFile() {
        return showAttachFile;
    }

    public void setShowAttachFile(boolean showAttachFile) {
        this.showAttachFile = showAttachFile;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public VShopStaffDTO getCurShopStaff() {
        return curShopStaff;
    }

    public void setCurShopStaff(VShopStaffDTO curShopStaff) {
        this.curShopStaff = curShopStaff;
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.RevokeKitDetail;
import com.viettel.bccs.inventory.model.Shop;
import com.viettel.bccs.inventory.service.RevokeKitDetailService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hellp on 9/27/2016.
 */

@ManagedBean
@Component
@Scope("view")
public class RevokeKitController extends TransCommonController {

    private static final int MAX_ROW_REVOKE_KIT = 5000;
    private static final int MAX_ROW_REVOKE_KIT_SEARCH = 300;

    private StaffDTO currentStaff;
    private ShopDTO shopLogin;
    private String attachFileName = "";
    private String attachFileNameSearch = "";
    private boolean errorImport = false;
    private byte[] byteContent;
    private UploadedFile fileUpload;
    private ExcellUtil processingFile;

    private byte[] byteContentSearch;
    private UploadedFile fileUploadSearch;
    private ExcellUtil processingFileSearch;
    private String typeSearch = "1";
    private String strIsdnInput = "";
    private String serialInput = "";

    private Date fromDate;
    private Date toDate;
    private int limitAutoComplete;
    private int tabIndex;
    private Long ownerId;
    private Long ownerType;
    private RevokeKitResultDTO revokeKitResultDTO;
    private RevokeKitResultDTO revokeKitResultDTOSearch;
    private boolean hasRoleRevoke;
    private boolean shopIsAgent = true;

    private List<RevokeKitFileImportDTO> lstRevokeStockKitOuput;
    private List<RevokeKitFileImportDTO> lstRevokeStockKitOuputSearch;
    private List<RevokeKitDetailDTO> lsRevokeKitDetail;
    private List<RevokeKitDetailDTO> lsRevokeKitDetailSearch;

    @Autowired
    private RevokeKitDetailService revokeKitDetailService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTag;

    @PostConstruct
    public void init() {
        try {
            StaffDTO staffLogin = BccsLoginSuccessHandler.getStaffDTO();
            shopLogin = shopService.findOne(staffLogin.getShopId());
            if (shopService.checkChannelIsAgent(shopLogin.getChannelTypeId())) {
                reportError("", "", getText("revoke.kit.agent.not.allow"));
                topPage();
                return;
            }
            shopIsAgent = false;
            initTab1();
            initTab2();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }


    public boolean isHasRoleRevoke() {
        return hasRoleRevoke;
    }

    public void setHasRoleRevoke(boolean hasRoleRevoke) {
        this.hasRoleRevoke = hasRoleRevoke;
    }

    public void initTab2() throws Exception {

        hasRoleRevoke = false;
        RequiredRoleMap requiredRoleMap = CustomAuthenticationProvider.getAllRoles();
        if (requiredRoleMap.hasRole(Const.PERMISION.BCCS2_IM_QLKHO_THU_HOI_KIT_THU_HOI)) {
            hasRoleRevoke = true;
        }
        fromDate = Calendar.getInstance().getTime();
        toDate = fromDate;
        currentStaff = BccsLoginSuccessHandler.getStaffDTO();
        ownerId = currentStaff.getShopId();
        ownerType = Const.OWNER_TYPE.SHOP_LONG;
        revokeKitResultDTO = new RevokeKitResultDTO();
        lstRevokeStockKitOuput = Lists.newArrayList();
        lsRevokeKitDetail = Lists.newArrayList();
        shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(currentStaff.getShopId()), true, Const.OWNER_TYPE.SHOP);
        shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
        staffInfoTag.initStaff(this, DataUtil.safeToString(currentStaff.getShopId()));
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        lstRevokeStockKitOuputSearch = Lists.newArrayList();
        lsRevokeKitDetailSearch = Lists.newArrayList();
        clearFileUpload();
    }

    public void initTab1() {
        clearFileUploadSearch();
        strIsdnInput = null;
        serialInput = null;
        lstRevokeStockKitOuputSearch = Lists.newArrayList();
        lsRevokeKitDetailSearch = Lists.newArrayList();
    }

    /**
     * ham tai ve file loi
     *
     * @return
     * @author thanhnt77
     */
    public StreamedContent exportImportErrorFile() {
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_REVOKE_KIT_ERR);
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setOutName("template_revoke_kit_" + Calendar.getInstance().getTimeInMillis() + ".xls");
            bean.putValue("lstRevokeStockKitOuput", lstRevokeStockKitOuput);
            return FileUtil.exportToStreamed(bean);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    /**
     * ham tai ve file loi
     *
     * @return
     * @author thanhnt77
     */
    public StreamedContent exportImportErrorFileSearch() {
        try {
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_REVOKE_SEARCH_KIT_ERR);
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setOutName("template_revoke_kit_search_" + Calendar.getInstance().getTimeInMillis() + ".xls");
            bean.putValue("lstRevokeStockKitOuput", lstRevokeStockKitOuputSearch);
            return FileUtil.exportToStreamed(bean);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public boolean isShowErrorKit() {
        return !DataUtil.isNullOrEmpty(lstRevokeStockKitOuput);
    }

    public boolean isShowErrorKitSearch() {
        return !DataUtil.isNullOrEmpty(lstRevokeStockKitOuputSearch);
    }

    /**
     * ham chon file
     *
     * @param event
     * @author thanhnt77
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            clearFileUpload();
            errorImport = false;
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            fileUpload = event.getFile();

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
            lstRevokeStockKitOuput = Lists.newArrayList();

        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * ham chon file
     *
     * @param event
     * @author thanhnt77
     */
    public void handleFileUploadSearch(FileUploadEvent event) {
        try {
            clearFileUploadSearch();
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            fileUploadSearch = event.getFile();

            BaseMessage message = validateFileUploadWhiteList(fileUploadSearch, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearFileUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContentSearch = fileUploadSearch.getContents();
            attachFileNameSearch = new String(fileUploadSearch.getFileName().getBytes(), "UTF-8");
            processingFileSearch = new ExcellUtil(fileUploadSearch, byteContentSearch);
            lstRevokeStockKitOuputSearch = Lists.newArrayList();

        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * xu ly clear xoa file upload cua nhap serial
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
     * xu ly clear xoa file upload cua nhap serial tab serach
     *
     * @author ThanhNT77
     */
    private void clearFileUploadSearch() {
        attachFileNameSearch = null;
        processingFileSearch = null;
        fileUploadSearch = null;
        byteContentSearch = null;
    }

    private RevokeKitResultDTO convertSerialIsdnToRevokeSearchDTO(String isdn, String serial) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(isdn) && DataUtil.isNullOrEmpty(serial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "revoke.stock.kit.data.isdn.serial");
        }
        RevokeKitResultDTO resultDTO = new RevokeKitResultDTO();
        resultDTO.setLsRevokeKitFileImportDTOs(Lists.newArrayList(new RevokeKitFileImportDTO(isdn, serial)));
        return resultDTO;
    }

    /**
     * ham tao RevokeKitResultDTO
     *
     * @param processingFile
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    private RevokeKitResultDTO convertFileToRevokeDTO(ExcellUtil processingFile) throws LogicException, Exception {
        errorImport = false;
        RevokeKitResultDTO revokeKit = new RevokeKitResultDTO();
        revokeKit.setOwnerId(ownerId);
        revokeKit.setOwnerType(ownerType);
        revokeKit.setActionStaffId(currentStaff.getStaffId());
        if (processingFile == null) {
            clearFileUpload();
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.valid.file.size");
        }
        Sheet sheetProcess = processingFile.getSheetAt(0);
        //map stockFull luu tru mat hang
        int totalRow = 0;
        List<RevokeKitFileImportDTO> lsImport = Lists.newArrayList();

        int maxRowConfig = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.MAX_ROW_REVOKE_KIT));
        maxRowConfig = (maxRowConfig == 0) ? MAX_ROW_REVOKE_KIT : maxRowConfig;
        for (Row row : sheetProcess) {
            totalRow++;
            if (totalRow <= 1) {
                continue;
            }
            String isdn = DataUtil.safeToString(processingFile.getStringValue(row.getCell(0))).trim();
            String serial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(1))).trim();
            String ownerCode = DataUtil.safeToString(processingFile.getStringValue(row.getCell(2))).trim();
            String ownerTypeFile = DataUtil.safeToString(processingFile.getStringValue(row.getCell(3))).trim();
            String revokeType = DataUtil.safeToString(processingFile.getStringValue(row.getCell(4))).trim();
            String contentRow = isdn + serial + ownerCode + ownerTypeFile + revokeType;
            if (DataUtil.isNullOrEmpty(contentRow)) {
                continue;
            }

            RevokeKitFileImportDTO revokeKitFileImportDTO = new RevokeKitFileImportDTO();
            revokeKitFileImportDTO.setIsdn(isdn);
            revokeKitFileImportDTO.setSerial(serial);
            revokeKitFileImportDTO.setOwnerCode(ownerCode);
            revokeKitFileImportDTO.setOwnerType(ownerTypeFile);
            revokeKitFileImportDTO.setRevokeType(revokeType);
            lsImport.add(revokeKitFileImportDTO);
        }
        if (DataUtil.isNullOrEmpty(lsImport)) {
            clearFileUpload();
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.valid.file.size");
        }
        if (lsImport.size() > maxRowConfig) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.maxline", maxRowConfig);
        }
        revokeKit.setLsRevokeKitFileImportDTOs(lsImport);
        return revokeKit;
    }

    /**
     * ham tao RevokeKitResultDTO
     *
     * @param processingFile
     * @return
     * @throws Exception
     * @author ThanhNT77
     */
    private RevokeKitResultDTO createRevokeKitResultDTOSearch(ExcellUtil processingFile) throws Exception {
        RevokeKitResultDTO revokeKit = new RevokeKitResultDTO();
        if (processingFile == null) {
            clearFileUploadSearch();
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.valid.file.size");
        }
        Sheet sheetProcess = processingFile.getSheetAt(0);
        //map stockFull luu tru mat hang
        int totalRow = 0;
        List<RevokeKitFileImportDTO> lsImport = Lists.newArrayList();

        int maxRowConfig = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.MAX_ROW_REVOKE_KIT_SEARCH));
        maxRowConfig = (maxRowConfig == 0) ? MAX_ROW_REVOKE_KIT_SEARCH : maxRowConfig;
        for (Row row : sheetProcess) {
            totalRow++;
            if (totalRow <= 1) {
                continue;
            }
            String isdn = DataUtil.safeToString(processingFile.getStringValue(row.getCell(0))).trim();
            String serial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(1))).trim();
            String contentRow = isdn + serial;
            if (DataUtil.isNullOrEmpty(contentRow)) {
                continue;
            }

            RevokeKitFileImportDTO revokeKitFileImportDTO = new RevokeKitFileImportDTO();
            revokeKitFileImportDTO.setIsdn(isdn);
            revokeKitFileImportDTO.setSerial(serial);
            lsImport.add(revokeKitFileImportDTO);
        }
        if (DataUtil.isNullOrEmpty(lsImport)) {
            clearFileUpload();
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.valid.file.size");
        }
        if (lsImport.size() > maxRowConfig) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.maxline", maxRowConfig);
        }
        revokeKit.setLsRevokeKitFileImportDTOs(lsImport);
        return revokeKit;
    }

    /**
     * ham xu ly tim kiem kit thu hoi
     *
     * @autho thanhnt77
     */
    @Secured("@")
    public void doSearch() {
        try {
//            validateDate(fromDate, toDate);
            lsRevokeKitDetail = DataUtil.defaultIfNull(revokeKitDetailService.searchRevokeKitDetailByShopAndDate(ownerId, fromDate, toDate), new ArrayList<>());
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
     * ham xu ly tim kiem kit thu hoi
     *
     * @autho thanhnt77
     */
    @Secured("@")
    public void doSearchIsdnSerial() {
        try {
            if (shopIsAgent) {
                reportError("", "", getText("revoke.kit.agent.not.allow"));
                topPage();
                return;
            }
            RevokeKitResultDTO resultDTO;
            HashMap<String, RevokeKitDetailDTO> mapKitDetail = new HashMap<>();

            if ("1".equals(typeSearch)) {
                resultDTO = convertSerialIsdnToRevokeSearchDTO(DataUtil.safeToString(strIsdnInput).trim(), DataUtil.safeToString(serialInput).trim());
            } else {
                BaseMessage message = validateFileUploadWhiteList(fileUploadSearch, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    clearFileUpload();
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                resultDTO = createRevokeKitResultDTOSearch(processingFileSearch);
            }
            resultDTO.setTypeSearch(typeSearch);
            revokeKitResultDTOSearch = revokeKitDetailService.searchRevokeKitBySerialAndIsdn(resultDTO);
            if (revokeKitResultDTOSearch != null) {
                if ("2".equals(typeSearch)) {
                    lstRevokeStockKitOuputSearch = DataUtil.defaultIfNull(revokeKitResultDTOSearch.getLsRevokeKitFileImportDTOs(), new ArrayList<>());
                    RevokeKitDetailDTO revokeKitImport;
                    for (RevokeKitFileImportDTO revokeKit : lstRevokeStockKitOuputSearch) {
                        revokeKitImport = new RevokeKitDetailDTO();
                        if (!mapKitDetail.containsKey(revokeKit.getSerial())) {
                            revokeKitImport.setSerial(revokeKit.getSerial());
                            revokeKitImport.setIsdn(revokeKit.getIsdn());
                            revokeKitImport.setVerifyDescription(!DataUtil.isNullOrEmpty(revokeKit.getResultRevoke()) ? revokeKit.getResultRevoke() : getText("revoke.kit.file.kit.is.not.revoke"));
                            mapKitDetail.put(revokeKit.getSerial(), revokeKitImport);
                        }
                    }
                }
                lsRevokeKitDetailSearch = new ArrayList<>();
                for (RevokeKitDetailDTO revokeKitSearch : revokeKitResultDTOSearch.getLsKitDetailDTO()) {
                    mapKitDetail.put(revokeKitSearch.getSerial(), revokeKitSearch);
                }
                lsRevokeKitDetailSearch = new ArrayList<>(mapKitDetail.values());

            }
            if (DataUtil.isNullOrEmpty(lsRevokeKitDetailSearch)) {
                reportSuccess("", getText("mn.invoice.invoiceType.nodata"));
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

    public boolean isShowErrorFileSearch() {
        return !DataUtil.isNullOrEmpty(lstRevokeStockKitOuputSearch);
    }

    /**
     * ham xuat excel
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doExportExcelKit() {
        try {
            List<RevokeKitDetailDTO> lsRevokeKitDetailInput;
            lsRevokeKitDetailInput = DataUtil.defaultIfNull(revokeKitDetailService.searchRevokeKitDetailByShopAndDate(ownerId, fromDate, toDate), new ArrayList<>());
            doExportExcel(lsRevokeKitDetailInput);
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

    @Secured("@")
    public void doExportExcelLookKit() {
        try {
            List<RevokeKitDetailDTO> lsRevokeKitDetailInput;
            lsRevokeKitDetailInput = DataUtil.cloneBean(lsRevokeKitDetailSearch);
            doExportExcel(lsRevokeKitDetailInput);
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

    private void doExportExcel(List<RevokeKitDetailDTO> lsRevokeKitDetailInput) throws Exception, LogicException {

        HSSFWorkbook workbook = getContentExportStockInspect(lsRevokeKitDetailInput);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_REVOKE_KIT_OUTPUT + "\"");
        externalContext.setResponseContentType("application/vnd.ms-excel");
        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();

    }

    /**
     * ham xu ly ve giao dien xuat excel
     *
     * @param lsRevokeKitDetailInput
     * @return
     * @author thanhnt77
     */
    private HSSFWorkbook getContentExportStockInspect(List<RevokeKitDetailDTO> lsRevokeKitDetailInput) throws Exception {
        InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_REVOKE_KIT_EXPORT);
        HSSFWorkbook workbook = new HSSFWorkbook(createStream);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        Sheet realSerialSheet = workbook.getSheetAt(0);
        int index;
        if (!DataUtil.isNullOrEmpty(lsRevokeKitDetailInput)) {
            for (int i = 0; i < lsRevokeKitDetailInput.size(); i++) {
                Row realSerial = realSerialSheet.createRow(i + 1);
                RevokeKitDetailDTO dto = lsRevokeKitDetailInput.get(i);
                index = 0;
                setValueAndStyle(realSerial.createCell(index++), i + 1, style);
                setValueAndStyle(realSerial.createCell(index++), dto.getIsdn(), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getSerial(), style);
                setValueAndStyle(realSerial.createCell(index++), DataUtil.safeToString(dto.getProductCode()), style);
                setValueAndStyle(realSerial.createCell(index++), DataUtil.safeToString(dto.getDataProductCode()), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getMainBalance(), style);
                //TKKM
                setValueAndStyle(realSerial.createCell(index++), dto.getTk10(), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getTk34(), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getTk27(), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getTk46(), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getTkData(), style);

//                setValueAndStyle(realSerial.createCell(index++), dto.getDataRegisterDate() != null ? DateUtil.date2ddMMyyyyString(dto.getDataRegisterDate()) : "", style);
                String strRegStatus = "";
                if (Const.LONG_OBJECT_ONE.equals(dto.getRegisterStatus())) {
                    strRegStatus = getText("revoke.kit.registerStatus.1");
                } else if (Const.LONG_OBJECT_ZERO.equals(dto.getRegisterStatus())) {
                    strRegStatus = getText("revoke.kit.registerStatus.0");
                }
                setValueAndStyle(realSerial.createCell(index++), strRegStatus, style);
                setValueAndStyle(realSerial.createCell(index++), dto.getVerifyDescription(), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getVerifyDate() != null ? DateUtil.date2ddMMyyyyString(dto.getVerifyDate()) : "", style);
                setValueAndStyle(realSerial.createCell(index++), dto.getPrice(), style);
                String strRevokeType = "";
                if (Const.LONG_OBJECT_ONE.equals(dto.getRevokeType())) {
                    strRevokeType = getText("revoke.kit.revokeType.1");
                } else if (Const.LONG_OBJECT_TWO.equals(dto.getRevokeType())) {
                    strRevokeType = getText("revoke.kit.revokeType.2");
                }
                setValueAndStyle(realSerial.createCell(index++), strRevokeType, style);

                setValueAndStyle(realSerial.createCell(index++), dto.getRevokeDescription() != null ? dto.getRevokeDescription() : getText("revoke.kit.revokestatus.value0"), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getRevokeDate() != null ? DateUtil.date2ddMMyyyyString(dto.getRevokeDate()) : "", style);
                setValueAndStyle(realSerial.createCell(index++), dto.getAddMoneyResult() != null && !Const.STRING_CONST_ZERO.equals(dto.getAddMoneyResult()) ? dto.getAddMoneyResult() : getText("revoke.kit.addmoneystatus.value0"), style);
                setValueAndStyle(realSerial.createCell(index++), dto.getLastModify() != null ? DateUtil.date2ddMMyyyyString(dto.getLastModify()) : "", style);
                setValueAndStyle(realSerial.createCell(index++), dto.getOrgOwnerCode(), style);
                String strOrgOwnerType = "";
                if (Const.LONG_OBJECT_ONE.equals(dto.getOrgOwnerType())) {
                    strOrgOwnerType = getText("revoke.kit.orgOwnerType.1");
                } else if (Const.LONG_OBJECT_TWO.equals(dto.getOrgOwnerType())) {
                    strOrgOwnerType = getText("revoke.kit.orgOwnerType.1");
                }
                setValueAndStyle(realSerial.createCell(index++), strOrgOwnerType, style);
                setValueAndStyle(realSerial.createCell(index), dto.getReasonCode(), style);
            }
        }
        return workbook;
    }

    /**
     * ham thu hoi kit
     *
     * @autho thanhnt77
     */
    @Secured("@")
    public void doRevokeKit() {
        try {
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearFileUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            RevokeKitResultDTO resultDTO = convertFileToRevokeDTO(processingFile);
            revokeKitResultDTO = revokeKitDetailService.executeRevokeKit(resultDTO);
            if (revokeKitResultDTO != null) {
                List<RevokeKitFileImportDTO> lsResult = DataUtil.defaultIfNull(revokeKitResultDTO.getLsRevokeKitFileImportDTOs(), new ArrayList<>());
                List<RevokeKitFileImportDTO> lsResultError =
                        DataUtil.defaultIfNull(lsResult.stream().filter(x -> !DataUtil.isNullOrEmpty(x.getResultRevoke())).collect(Collectors.toList()), new ArrayList<>());
                reportSuccess("", "revoke.stock.kit.data.sucess.msg", lsResult.size() - lsResultError.size(), lsResult.size());

                if (lsResultError.size() > 0) {
                    lstRevokeStockKitOuput = lsResult;
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
     * ham validate thu hoi kit
     *
     * @author thanhnt77
     */
    public void validateRevoke() {
        try {
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                clearFileUpload();
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (DataUtil.isNullOrZero(ownerId) || DataUtil.isNullOrZero(ownerType)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.check.report.require.shop");
            }
            lstRevokeStockKitOuput = Lists.newArrayList();
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
     * ham dowload file mau lap lenh
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_REVOKE_KIT);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_REVOKE_KIT + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    /**
     * ham dowload file mau lap lenh
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void downloadFileTemplateSearch() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.ERR_FROM_FILE_TEMPLATE_SEARCH_REVOKE_KIT);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "template_revoke_search_kit.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
//        ownerId = DataUtil.safeToLong(vShopStaffDTO.getOwnerId());
//        ownerType = DataUtil.safeToLong(vShopStaffDTO.getOwnerType());
        staffInfoTag.initStaff(this, vShopStaffDTO.getOwnerId());
    }

    public void clearShop() {
//        ownerId = null;
//        ownerType = null;
        staffInfoTag.resetProduct();
    }


    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        init();
    }

    //getter and setter
    public StaffDTO getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(StaffDTO currentStaff) {
        this.currentStaff = currentStaff;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public boolean isErrorImport() {
        return errorImport;
    }

    public void setErrorImport(boolean errorImport) {
        this.errorImport = errorImport;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public RevokeKitResultDTO getRevokeKitResultDTO() {
        return revokeKitResultDTO;
    }

    public void setRevokeKitResultDTO(RevokeKitResultDTO revokeKitResultDTO) {
        this.revokeKitResultDTO = revokeKitResultDTO;
    }

    public List<RevokeKitFileImportDTO> getLstRevokeStockKitOuput() {
        return lstRevokeStockKitOuput;
    }

    public void setLstRevokeStockKitOuput(List<RevokeKitFileImportDTO> lstRevokeStockKitOuput) {
        this.lstRevokeStockKitOuput = lstRevokeStockKitOuput;
    }

    public List<RevokeKitDetailDTO> getLsRevokeKitDetail() {
        return lsRevokeKitDetail;
    }

    public void setLsRevokeKitDetail(List<RevokeKitDetailDTO> lsRevokeKitDetail) {
        this.lsRevokeKitDetail = lsRevokeKitDetail;
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

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getAttachFileNameSearch() {
        return attachFileNameSearch;
    }

    public void setAttachFileNameSearch(String attachFileNameSearch) {
        this.attachFileNameSearch = attachFileNameSearch;
    }

    public byte[] getByteContentSearch() {
        return byteContentSearch;
    }

    public void setByteContentSearch(byte[] byteContentSearch) {
        this.byteContentSearch = byteContentSearch;
    }

    public UploadedFile getFileUploadSearch() {
        return fileUploadSearch;
    }

    public void setFileUploadSearch(UploadedFile fileUploadSearch) {
        this.fileUploadSearch = fileUploadSearch;
    }

    public ExcellUtil getProcessingFileSearch() {
        return processingFileSearch;
    }

    public void setProcessingFileSearch(ExcellUtil processingFileSearch) {
        this.processingFileSearch = processingFileSearch;
    }

    public List<RevokeKitDetailDTO> getLsRevokeKitDetailSearch() {
        return lsRevokeKitDetailSearch;
    }

    public void setLsRevokeKitDetailSearch(List<RevokeKitDetailDTO> lsRevokeKitDetailSearch) {
        this.lsRevokeKitDetailSearch = lsRevokeKitDetailSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getStrIsdnInput() {
        return strIsdnInput;
    }

    public void setStrIsdnInput(String strIsdnInput) {
        this.strIsdnInput = strIsdnInput;
    }

    public String getSerialInput() {
        return serialInput;
    }

    public void setSerialInput(String serialInput) {
        this.serialInput = serialInput;
    }

    public List<RevokeKitFileImportDTO> getLstRevokeStockKitOuputSearch() {
        return lstRevokeStockKitOuputSearch;
    }

    public void setLstRevokeStockKitOuputSearch(List<RevokeKitFileImportDTO> lstRevokeStockKitOuputSearch) {
        this.lstRevokeStockKitOuputSearch = lstRevokeStockKitOuputSearch;
    }


}

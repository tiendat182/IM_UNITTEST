package com.viettel.bccs.inventory.controller.list;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.VoucherInfoDTO;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.bccs.inventory.service.VoucherInfoService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by HoangAnh on 8/30/2016.
 */

@ManagedBean
@Component
@Scope("view")
public class ImportVoucherController extends InventoryController {
    private static final String FILE_VOUCHER_TEMPLATE_PATH = "IMPORT_VOUCHER_INFO_TEMPLATE.xls";
    private static final String FILE_VOUCHER_TEMPLATE_ERROR_PATH = "IMPORT_VOUCHER_INFO_TEMPLATE_ERROR.xls";
    private int maxRow = 1000;
    private Workbook bookError;
    private List<String[]> listError = Lists.newArrayList();
    private UploadedFile uploadedFile;
    private StaffDTO currentStaff;
    private String fileName;
    private byte[] contentByte;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String SPECIAL_CHAR_SERIAL = "[^A-Za-z0-9]+";
    private static final String SPECIAL_CHAR_STAFF_CODE = "[^A-Za-z0-9_]+";

    @Autowired
    private StaffService staffService;

    @Autowired
    private VoucherInfoService voucherInfoService;

    @PostConstruct
    public void init() {
        try {
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            List<OptionSetValueDTO> lstOption = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.NUM_ROWS_IMPORT_APN_AND_APNIP);
            if (!DataUtil.isNullOrEmpty(lstOption)) {
                maxRow = Integer.parseInt(lstOption.get(0).getValue());
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
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
            listError = Lists.newArrayList();
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

    public boolean validate() {
        if (DataUtil.isNullObject(contentByte)) {
            reportError("frmImportVoucher:messages", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            return false;
        }
        return true;
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_VOUCHER_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + FILE_VOUCHER_TEMPLATE_PATH + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
        }
    }

    public void validateImport() {
        validate();
    }

    @Secured("@")
    public void importData() {
        try {
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                contentByte = null;
                fileName = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (validate()) {
                List<VoucherInfoDTO> listValidDataInExcelFile = new ArrayList<>();
                try {
                    listValidDataInExcelFile = readDataInFile(contentByte);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    reportError("", "import.voucher.file.invalid", e);
                    return;
                }

                if (DataUtil.isNullOrEmpty(listValidDataInExcelFile)) {
                    reportError("", "", "import.voucher.not.success");
                } else {
                    BaseMessage msg = voucherInfoService.insertListVoucherInfo(listValidDataInExcelFile);
                    if (msg.isSuccess()) {
                        reportSuccess("", "import.voucher.success", (listValidDataInExcelFile.size()) + "/" + (listValidDataInExcelFile.size() + listError.size()));
                    } else {
                        reportError("", "", "import.voucher.not.success");
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    private List<VoucherInfoDTO> readDataInFile(byte[] content) throws Exception {
        listError.clear();
        List<VoucherInfoDTO> voucherInfoDTOList = new ArrayList<VoucherInfoDTO>();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getWorkbook().getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();
        for (int i = 3; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            String staffCode = safeToString(ex.getStringValue(row.getCell(0)));
            String serial = safeToString(ex.getStringValue(row.getCell(1)));
            String amount = safeToString(ex.getStringValue(row.getCell(2)));
            String fromDate = safeToString(ex.getStringValue(row.getCell(3)));
            String toDate = safeToString(ex.getStringValue(row.getCell(4)));

            if (!(DataUtil.isNullObject(staffCode) && DataUtil.isNullObject(serial)
                    && DataUtil.isNullOrEmpty(amount) && DataUtil.isNullObject(fromDate) && DataUtil.isNullObject(toDate))) {
                if (DataUtil.isNullObject(staffCode) || staffCode.trim().length() > 100 || Pattern.compile(SPECIAL_CHAR_STAFF_CODE).matcher(staffCode).find()) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.staffcode.error")};
                    listError.add(error);
                    continue;
                }

                if (DataUtil.isNullObject(serial) || serial.trim().length() > 50 || Pattern.compile(SPECIAL_CHAR_SERIAL).matcher(serial).find()) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.serial.error")};
                    listError.add(error);
                    continue;
                }

                if (DataUtil.isNullOrEmpty(amount) || !DataUtil.isNumber(amount) || amount.length() > 10) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.amount.error")};
                    listError.add(error);
                    continue;
                }

                if (!validateFormatDate(fromDate)) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.fromdate.error")};
                    listError.add(error);
                    continue;
                }

                if (!validateFormatDate(toDate)) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.todate.error")};
                    listError.add(error);
                    continue;
                }

                if (!compareDate(fromDate, toDate)) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.fromtodate.error")};
                    listError.add(error);
                    continue;
                }

                //check duplicate in file
                if (isDuplicateInFile(voucherInfoDTOList, serial)) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.serial.exist")};
                    listError.add(error);
                    continue;
                }

                //check staff_code trong database
                FilterRequest filterRequest = new FilterRequest("staffCode", FilterRequest.Operator.EQ, staffCode);
                List<FilterRequest> filterRequests = new ArrayList<FilterRequest>();
                filterRequests.add(filterRequest);
                if (DataUtil.isNullObject(staffService.getStaffByStaffCode(staffCode))) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.staffcode.notexist")};
                    listError.add(error);
                    continue;
                }

                //check duplicate trong db
                if (voucherInfoService.findBySerial(serial) != null) {
                    String[] error = {staffCode, serial, amount, fromDate, toDate, BundleUtil.getText("import.voucher.serial.exist")};
                    listError.add(error);
                    continue;
                }
            } else {
                continue;
            }

            VoucherInfoDTO voucherInfoDTO = new VoucherInfoDTO();
            voucherInfoDTO.setStaffCode(staffCode);
            voucherInfoDTO.setAmount(DataUtil.safeToLong(amount));
            voucherInfoDTO.setSerial(serial);
            voucherInfoDTO.setFromDate(formatDate(fromDate));
            voucherInfoDTO.setToDate(formatDate(toDate));
            voucherInfoDTO.setCreateDate(new Date());
            voucherInfoDTO.setStatus(Short.valueOf("0"));
            voucherInfoDTO.setPass(serial);

            voucherInfoDTOList.add(voucherInfoDTO);

            if (i > maxRow) {
                listError = Lists.newArrayList();
                throw new LogicException("", "import.voucher.file.maxline", maxRow);
            }
        }

        if (!DataUtil.isNullOrEmpty(listError)) {
            try {
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName(FILE_VOUCHER_TEMPLATE_ERROR_PATH);
                bean.putValue("lstData", listError);
                bookError = FileUtil.exportWorkBook(bean);
            } catch (Exception e) {
                reportError("", "common.error.happen", e);
                topPage();
            }
        }

        return voucherInfoDTOList;
    }


    public static boolean validateFormatDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date dateIn = simpleDateFormat.parse(dateStr);
            String dateOut = simpleDateFormat.format(dateIn);
            if (dateStr.equals(dateOut)) return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean compareDate(String fromDate, String toDate) {
        if (validateFormatDate(fromDate) && validateFormatDate(toDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date fromDateD = simpleDateFormat.parse(fromDate);
                Date toDateD = simpleDateFormat.parse(toDate);
                if (fromDateD.compareTo(toDateD) <= 0) {
                    return true;
                }
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    public Date formatDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public boolean isDuplicateInFile(List<VoucherInfoDTO> voucherInfoDTOList, String serial) {
        if (voucherInfoDTOList == null || voucherInfoDTOList.isEmpty()) return false;
        for (VoucherInfoDTO voucherInfoDTO : voucherInfoDTOList) {
            if (voucherInfoDTO.getSerial().equals(serial)) return true;
        }
        return false;
    }


    @Secured("@")
    public void downloadFileError() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "importVoucherError.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            bookError.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public String safeToString(String data) {
        if (data == null) return "";
        return data.trim();
    }

    /* ************************************* SETTERS AND GETTERS ***************************** */

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public Workbook getBookError() {
        return bookError;
    }

    public void setBookError(Workbook bookError) {
        this.bookError = bookError;
    }

    public List<String[]> getListError() {
        return listError;
    }

    public void setListError(List<String[]> listError) {
        this.listError = listError;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public StaffDTO getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(StaffDTO currentStaff) {
        this.currentStaff = currentStaff;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContentByte() {
        return contentByte;
    }

    public void setContentByte(byte[] contentByte) {
        this.contentByte = contentByte;
    }
}

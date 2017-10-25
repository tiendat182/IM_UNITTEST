package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.FileLookUpSerial;
import com.viettel.bccs.inventory.dto.LookupSerialCardAndKitByFileDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.StockCardService;
import com.viettel.bccs.inventory.service.StockKitService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hellp on 9/27/2016.
 */

@ManagedBean
@Component
@Scope("view")
public class LookupSerialCardAndKitByFileController extends InventoryController {
    private StaffDTO currentStaff;
    private UploadedFile uploadedFile;
    private String fileName;
    private byte[] contentByte;
    private static final String INPUT_FILE_TEMPLATE_PATH = "LOOKUP_SERIAL_CARD_AND_KIT_TEMPLATE.xlsx";
    private static final String OUTPUT_FILE_TEMPLATE_PATH = "LOOKUP_SERIAL_CARD_AND_KIT_RESULT_TEMPLATE.xlsx";
    private static final String RESULT_FILE = "LOOKUP_SERIAL_CARD_AND_KIT_RESULT_";
    private List<String[]> resultList = Lists.newArrayList();
    private Long searchOption;
    private Workbook book;

    public static Long CARD = 1L;
    public static Long KIT = 2L;
    public static Long MAX_ROW = 1000L;

    private List<String> serialList;
    private List<String> serialListSearch;
    private List<FileLookUpSerial> fileLookUpSerials;
    private List<String[]> serialListResult;
    private boolean directLink;

    private List<LookupSerialCardAndKitByFileDTO> lookupSerialCardAndKitByFileDTOs;

    HashMap<String, String[]> serialMapError;
    HashMap<String, List<String[]>> serialMapResult;

    @Autowired
    private StockCardService stockCardService;

    @Autowired
    private StockKitService stockKitService;

    @PostConstruct
    public void init() {
        try {
            helperSystem();
            searchOption = 1L;
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private void helperSystem(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String sys = request.getParameter(Const.SYSTEM_SALE);
        directLink = DataUtil.safeEqual(sys,Const.SYSTEM_SALE);
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + INPUT_FILE_TEMPLATE_PATH);
            XSSFWorkbook workbook = new XSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + INPUT_FILE_TEMPLATE_PATH + "\"");
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

    public void validateSearch() {
        validate();
    }

    public boolean validate() {
        if (DataUtil.isNullObject(contentByte)) {
            reportError("frmLookupSerialCardAndKitByFile:message", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            return false;
        }
        return true;
    }

    @Secured("@")
    public void search() {
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
                serialList = new ArrayList<String>();
                serialListSearch = new ArrayList<String>();
                fileLookUpSerials = new ArrayList<>();
                serialMapError = new HashMap<String, String[]>();
                serialListResult = new ArrayList<String[]>();

                message = readExcelData();
                if (!message.isSuccess()) {
                    reportError("frmLookupSerialCardAndKitByFile:message", "", message.getDescription());
                    return;
                }
                if (DataUtil.isNullOrEmpty(serialListSearch)) {
                    reportError("frmLookupSerialCardAndKitByFile:message", "", "lookup.serial.card.and.kit.by.file.data.invalid");
                    return;
                }
                if (!executeSearch().isSuccess()) {
                    reportError("frmLookupSerialCardAndKitByFile:message", "", "lookup.serial.card.and.kit.by.file.search.error");
                    return;
                }
                if (!DataUtil.isNullOrEmpty(lookupSerialCardAndKitByFileDTOs)) {
                    serialMapResult = convertListToMap(lookupSerialCardAndKitByFileDTOs);
                }
                int count = 1;
                for (int i = 0 ; i < serialList.size() ; i++) {
                    String serial = serialList.get(i);
                    FileLookUpSerial fileLookUpSerial = fileLookUpSerials.get(i);
                    if (!DataUtil.isNullOrEmpty(serialMapResult) && serialMapResult.containsKey(serial) && !serialMapError.containsKey(serial)) {
                        List<String[]> contentList = serialMapResult.get(serial);
                        for (String[] content : contentList) {
                            content[0] = "" + (count++);
                            updateContent(content, fileLookUpSerial);
                            serialListResult.add(cloneArray(content));
                        }
                    } else if (!DataUtil.isNullOrEmpty(serialMapError) && serialMapError.containsKey(serial)) {
                        String[] content;
                        content = serialMapError.get(serial);
                        content[0] = "" + (count++);
                        updateContent(content, fileLookUpSerial);
                        serialListResult.add(cloneArray(content));
                    } else {
                        addError(serial, BundleUtil.getText("lookup.serial.card.and.kit.by.file.serial.not.found"));
                        String[] content;
                        content = serialMapError.get(serial);
                        content[0] = "" + (count++);
                        updateContent(content, fileLookUpSerial);
                        serialListResult.add(cloneArray(content));
                    }
                }
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName(OUTPUT_FILE_TEMPLATE_PATH);
                bean.putValue("lstData", serialListResult);
                book = FileUtil.exportWorkBook(bean);
                if (DataUtil.isNullOrEmpty(lookupSerialCardAndKitByFileDTOs)) {
                    reportError("frmLookupSerialCardAndKitByFile:message", "", "lookup.serial.card.and.kit.by.file.result.invalid");
                } else {
                    reportSuccess("frmLookupSerialCardAndKitByFile:message", "lookup.serial.card.and.kit.by.file.search.success", serialMapResult.size(), serialList.size());
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    private String[] cloneArray(String [] content){
        String [] tempArr = new String[content.length];
        System.arraycopy(content,0,tempArr,0, content.length);
        return tempArr;
    }

    private void updateContent(String [] content, FileLookUpSerial fileLookUpSerial){
        content[1] = fileLookUpSerial.getId();
        content[2] = fileLookUpSerial.getCustomerCausePhone();
        content[3] = fileLookUpSerial.getDateCause();
        content[4] = fileLookUpSerial.getTimeCause();
    }

    private BaseMessage readExcelData() {
        BaseMessage message = new BaseMessage(false);
        try {
            ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
            Sheet sheet = ex.getSheetAt(0);
            Row title = sheet.getRow(2);
            if (!ex.getStringValue(title.getCell(1)).equals(BundleUtil.getText("lookup.serial.card.and.kit.by.file.title.id"))
                    || !ex.getStringValue(title.getCell(2)).equals(BundleUtil.getText("lookup.serial.card.and.kit.by.file.title.stb"))
                    || !ex.getStringValue(title.getCell(3)).equals(BundleUtil.getText("lookup.serial.card.and.kit.by.file.title.date"))
                    || !ex.getStringValue(title.getCell(4)).equals(BundleUtil.getText("lookup.serial.card.and.kit.by.file.title.time"))
                    || !ex.getStringValue(title.getCell(5)).equals(BundleUtil.getText("lookup.serial.card.and.kit.by.file.title.serial"))) {
                message.setDescription("lookup.serial.card.and.kit.by.file.file.invalid");
                return message;
            }
            if (sheet.getPhysicalNumberOfRows() - 3 > MAX_ROW) {
                message.setDescription("lookup.serial.card.and.kit.by.file.invalid.max.row");
                return message;
            }
            for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                FileLookUpSerial fileLookUpSerial = parseRowToFileLookUpSerial(row, ex);
                fileLookUpSerials.add(fileLookUpSerial);

                if (checkData(fileLookUpSerial.getSerial()).isSuccess()) {
                    serialListSearch.add(fileLookUpSerial.getSerial());
                }
            }
            message.setSuccess(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (DataUtil.isNullOrEmpty(message.getDescription()))
                message.setDescription("lookup.serial.card.and.kit.by.file.read.file.error");
        }
        return message;
    }

    private FileLookUpSerial parseRowToFileLookUpSerial(Row row, ExcellUtil ex){
        FileLookUpSerial fileLookUpSerial = new FileLookUpSerial();
        fileLookUpSerial.setId(ex.getStringValue(row.getCell(1)));
        fileLookUpSerial.setCustomerCausePhone(ex.getStringValue(row.getCell(2)));
        fileLookUpSerial.setDateCause(ex.getStringValue(row.getCell(3)));
        fileLookUpSerial.setTimeCause(ex.getStringValue(row.getCell(4)));
        fileLookUpSerial.setSerial(ex.getStringValue(row.getCell(5)).trim());
        return fileLookUpSerial;
    }

    private BaseMessage executeSearch() {
        BaseMessage message = new BaseMessage(false);
        try {
            if (searchOption.equals(CARD)) {
                lookupSerialCardAndKitByFileDTOs = stockCardService.getSerialList(serialListSearch);
                message.setSuccess(true);
            } else if (searchOption.equals(KIT)) {
                lookupSerialCardAndKitByFileDTOs = stockKitService.getSerialList(serialListSearch);
                message.setSuccess(true);
            } else {
                message.setDescription("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }
        return message;
    }

    private BaseMessage checkData(String serial) {
        BaseMessage message = new BaseMessage(false);
        if (DataUtil.isNullOrEmpty(serial) || serial.length() > 20 || !NumberUtils.isNumber(serial)) {
            serialList.add(serial);
            message.setDescription(BundleUtil.getText("lookup.serial.card.and.kit.by.file.serial.invalid"));
            addError(serial, message.getDescription());
            return message;
        }
//        check trung serial
//        if (serialListSearch.contains(serial)) {
//            serial = Long.valueOf(serial).toString();
//            serialList.add(serial);
//            message.setDescription(BundleUtil.getText("lookup.serial.card.and.kit.by.file.serial.duplicate"));
//            addError(serial, message.getDescription());
//            return message;
//        }
        serialList.add(serial);
        message.setSuccess(true);
        return message;
    }

    private HashMap<String, List<String[]>> convertListToMap(List<LookupSerialCardAndKitByFileDTO> list) {
        HashMap<String, List<String[]>> map = new HashMap<String, List<String[]>>();
        for (LookupSerialCardAndKitByFileDTO obj : list) {
            String[] content = new String[13];
            content[5] = Long.valueOf(obj.getSerial()).toString();
            if (obj.getDateExp() != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dateStr = df.format(obj.getDateExp());
                System.out.println(dateStr);
                content[7] = dateStr.substring(11, dateStr.length());
                content[6] = dateStr.substring(0, 10);
            }
            content[8] = obj.getStaffCode();
            content[9] = obj.getPrice();
            content[10] = obj.getProvinceName();
            content[11] = obj.getProvinceCode();
            if (map.containsKey(content[5])) {
                List<String[]> contentList = map.get(content[5]);
                contentList.add(content);
            } else {
                List<String[]> contentList = new ArrayList<>();
                contentList.add(content);
                map.put(content[5], contentList);
            }
            if (obj.getNote().equals("0")) {
                content[12] = BundleUtil.getText("lookup.serial.card.and.kit.by.file.serial.da.ban");
            } else {
                content[12] = BundleUtil.getText("lookup.serial.card.and.kit.by.file.serial.chua.ban");
            }
        }
        return map;
    }

    @Secured("@")
    public void downloadFileResult() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat time = new SimpleDateFormat("DDMMYYYYHHmmss");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + RESULT_FILE + time.format(cal.getTime()) + ".xlsx");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            book.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private void addError(String serial, String errorText) {
        String[] error = {"", "", "", "", "", serial, "", "", "", "", "", "", errorText};
        serialMapError.put(serial, error);
    }

    public StaffDTO getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(StaffDTO currentStaff) {
        this.currentStaff = currentStaff;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
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

    public List<String[]> getResultList() {
        return resultList;
    }

    public void setResultList(List<String[]> resultList) {
        this.resultList = resultList;
    }

    public Long getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(Long searchOption) {
        this.searchOption = searchOption;
    }

    public Workbook getBook() {
        return book;
    }

    public void setBook(Workbook book) {
        this.book = book;
    }

    public List<String> getSerialList() {
        return serialList;
    }

    public void setSerialList(List<String> serialList) {
        this.serialList = serialList;
    }

    public List<String> getSerialListSearch() {
        return serialListSearch;
    }

    public void setSerialListSearch(List<String> serialListSearch) {
        this.serialListSearch = serialListSearch;
    }

    public List<String[]> getSerialListResult() {
        return serialListResult;
    }

    public void setSerialListResult(List<String[]> serialListResult) {
        this.serialListResult = serialListResult;
    }

    public List<LookupSerialCardAndKitByFileDTO> getLookupSerialCardAndKitByFileDTOs() {
        return lookupSerialCardAndKitByFileDTOs;
    }

    public void setLookupSerialCardAndKitByFileDTOs(List<LookupSerialCardAndKitByFileDTO> lookupSerialCardAndKitByFileDTOs) {
        this.lookupSerialCardAndKitByFileDTOs = lookupSerialCardAndKitByFileDTOs;
    }

    public HashMap<String, String[]> getSerialMapError() {
        return serialMapError;
    }

    public void setSerialMapError(HashMap<String, String[]> serialMapError) {
        this.serialMapError = serialMapError;
    }

    public HashMap<String, List<String[]>> getSerialMapResult() {
        return serialMapResult;
    }

    public void setSerialMapResult(HashMap<String, List<String[]>> serialMapResult) {
        this.serialMapResult = serialMapResult;
    }

    public List<FileLookUpSerial> getFileLookUpSerials() {
        return fileLookUpSerials;
    }

    public void setFileLookUpSerials(List<FileLookUpSerial> fileLookUpSerials) {
        this.fileLookUpSerials = fileLookUpSerials;
    }

    public boolean isDirectLink() {
        return directLink;
    }

    public void setDirectLink(boolean directLink) {
        this.directLink = directLink;
    }
}

package com.viettel.bccs.inventory.controller.list;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.bccs.inventory.dto.ApnIpDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.ApnIpService;
import com.viettel.bccs.inventory.service.ApnService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sinhhv on 2/29/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ImportApnController extends InventoryController {

    @Autowired
    ApnService apnService;
    @Autowired
    ApnIpService apnIpService;
    @Autowired
    OptionSetValueService optionSetValueService;

    private String importType = "1";
    private String fileName;
    private UploadedFile uploadedFile;
    private byte[] contentByte;
    private static final String FILE_APN_TEMPLATE_PATH = "IMPORT_APN_TEMPLATE.xls";
    private static final String FILE_APN_IP_TEMPLATE_PATH = "IMPORT_APN_IP_TEMPLATE.xls";
    private List<String[]> listError = Lists.newArrayList();
    private Workbook bookError;
    private StaffDTO currentStaff;
    private int maxRow = 1000;
    private String apnStatus;
    private String apnIpStatus;
    private int tabIndex = 0;

    //    bien cho tab apn
    private ApnDTO inputAPN = new ApnDTO();
    private ApnDTO inputAPNAdd = new ApnDTO();
    private List<ApnDTO> listApn = Lists.newArrayList();
    private boolean isEdit = false;

    //bien cho tab apn ip
    private ApnIpDTO inputApnIp = new ApnIpDTO();
    private ApnIpDTO inputApnIpAdd = new ApnIpDTO();
    private List<ApnDTO> listApnForIp = Lists.newArrayList();
    private List<ApnIpDTO> listApnIp = Lists.newArrayList();

    private ApnDTO apnSelected = null;
    private ApnDTO apnSelectedAdd = null;


    @PostConstruct
    public void init() {
        try {
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            List<OptionSetValueDTO> lstOption = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.NUM_ROWS_IMPORT_APN_AND_APNIP);
            ApnDTO search = new ApnDTO();
            search.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            listApnForIp = apnService.searchApn(search);

            if (!DataUtil.isNullOrEmpty(lstOption)) {
                maxRow = Integer.parseInt(lstOption.get(0).getValue());
            }
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
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
            reportError("", "", getText("common.error.happened"));
        }
    }

    public boolean validate() {
        if (DataUtil.isNullObject(importType)) {
            reportError("frmImportApn:messages", "", "import.apn.type.require");
            return false;
        }
        if (DataUtil.isNullObject(contentByte)) {
            reportError("frmImportApn:messages", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            return false;
        }
        return true;
    }

    private List<ApnDTO> readExcelToListApn(byte[] content) throws LogicException, Exception {
        listError.clear();
        List<ApnDTO> listApn = Lists.newArrayList();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();
        int rowValid = 0;
        for (int i = 3; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            String apnId = ex.getStringValue(row.getCell(0));
            String apnCode = ex.getStringValue(row.getCell(1));
            String apnName = ex.getStringValue(row.getCell(2));
            String centerCode = ex.getStringValue(row.getCell(3));
            String errMessage = "";
            if (!DataUtil.isNullObject(apnId) || !DataUtil.isNullObject(apnCode) || !DataUtil.isNullObject(apnName) || !DataUtil.isNullObject(centerCode)) {
                //validate tren du lieu
                rowValid++;
                if (DataUtil.isNullObject(apnId) || !DataUtil.isNumber(apnId.trim()) || apnId.trim().length() > 10 || DataUtil.safeToLong(apnId.trim()) <= 0) {
                    errMessage += BundleUtil.getText("import.apn.apn_id.error");
                    String[] error = {apnId, apnCode, apnName, centerCode, errMessage};
                    listError.add(error);
                    continue;
                }
                if (DataUtil.isNullObject(apnCode) || apnCode.trim().length() > 50 || !apnCode.matches("^([\\d\\w])+$")) {
                    errMessage += BundleUtil.getText("import.apn.apn_code.error");
                    String[] error = {apnId, apnCode, apnName, centerCode, errMessage};
                    listError.add(error);
                    continue;
                }
                if (DataUtil.isNullObject(apnName) || apnName.trim().length() > 50) {
                    errMessage += BundleUtil.getText("import.apn.apn_name.error");
                    String[] error = {apnId, apnCode, apnName, centerCode, errMessage};
                    listError.add(error);
                    continue;
                }
                if (DataUtil.isNullObject(centerCode) || centerCode.trim().length() > 50) {
                    errMessage += BundleUtil.getText("import.apn.center_code.error");
                    String[] error = {apnId, apnCode, apnName, centerCode, errMessage};
                    listError.add(error);
                    continue;
                }
                //check trung tren file
                if (checkDuplicate(listApn, apnId.trim(), "APN_ID")) {
                    errMessage += BundleUtil.getText("import.apn.apn_id.dup");
                    String[] error = {apnId, apnCode, apnName, centerCode, errMessage};
                    listError.add(error);
                    continue;
                }
                if (checkDuplicate(listApn, apnCode.trim(), "APN_CODE")) {
                    errMessage += BundleUtil.getText("import.apn.apn_code.dup");
                    String[] error = {apnId, apnCode, apnName, centerCode, errMessage};
                    listError.add(error);
                    continue;
                }
            } else {
                continue;
            }
            ApnDTO apn = new ApnDTO();
            apn.setApnId(DataUtil.safeToLong(apnId));
            apn.setApnCode(apnCode.trim().toUpperCase());
            apn.setApnName(apnName.trim());
            apn.setCenterCode(centerCode.trim());
            apn.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            apn.setCreateUser(currentStaff.getStaffCode());
            apn.setLastUpdateUser(currentStaff.getStaffCode());
            listApn.add(apn);
        }
        if (rowValid > maxRow) {
            listError = Lists.newArrayList();
            throw new LogicException("", "mn.stock.status.isdn.delete.maxline", maxRow);
        }
        //check trung DB
        if (!DataUtil.isNullOrEmpty(listApn)) {
            List<ApnDTO> listDupApnId = checkDupicateDB(listApn, "APN_ID");
            for (ApnDTO ob : listDupApnId) {
                for (int i = listApn.size() - 1; i >= 0; i--) {
                    ApnDTO apnDTO = listApn.get(i);
                    if (apnDTO.getApnId().equals(ob.getApnId())) {
                        listApn.remove(i);
                        String[] error = {apnDTO.getApnId().toString(), apnDTO.getApnCode(), apnDTO.getApnName(), apnDTO.getCenterCode(), BundleUtil.getText("import.apn.apn_id.dup")};
                        listError.add(error);
                    }
                }
            }
            List<ApnDTO> listDupApnCode = checkDupicateDB(listApn, "APN_CODE");
            for (ApnDTO ob : listDupApnCode) {
                for (int i = listApn.size() - 1; i >= 0; i--) {
                    ApnDTO apnDTO = listApn.get(i);
                    if (apnDTO.getApnCode().toUpperCase().equals(ob.getApnCode())) {
                        listApn.remove(i);
                        String[] error = {apnDTO.getApnId().toString(), apnDTO.getApnCode(), apnDTO.getApnName(), apnDTO.getCenterCode(), BundleUtil.getText("import.apn.apn_code.dup")};
                        listError.add(error);
                    }
                }
            }
        }
        if (!DataUtil.isNullOrEmpty(listError)) {
            try {
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName("IMPORT_APN_TEMPLATE_ERROR.xls");
                bean.putValue("lstData", listError);
                bookError = FileUtil.exportWorkBook(bean);
            } catch (Exception e) {
                reportError("", "common.error.happen", e);
                topPage();
            }
        }
        return listApn;
    }

    public boolean checkDuplicate(List<ApnDTO> listApn, String valueNeedCheck, String typeCheck) {
        if ("APN_ID".equals(typeCheck)) {
            for (ApnDTO apn : listApn) {
                if (apn.getApnId().equals(DataUtil.safeToLong(valueNeedCheck))) {
                    return true;
                }
            }
        } else if ("APN_CODE".equals(typeCheck)) {
            for (ApnDTO apn : listApn) {
                if (apn.getApnCode().equals(valueNeedCheck.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDuplicateApnIp(List<ApnIpDTO> listApnIp, String apnId, String ip) {
        for (ApnIpDTO apnIp : listApnIp) {
            if (apnIp.getApnId().equals(DataUtil.safeToLong(apnId)) && apnIp.getIp().equals(ip)) {
                return true;
            }
        }
        return false;
    }

    private List<ApnDTO> checkDupicateDB(List<ApnDTO> listApn, String typeCheck) {
        return DataUtil.defaultIfNull(apnService.checkDuplicateApn(listApn, typeCheck), Lists.newArrayList());
    }

    private List<ApnIpDTO> checkDupicateApnIpDB(List<ApnIpDTO> listApnIp) {
        return DataUtil.defaultIfNull(apnIpService.checkDuplicateApnIp(listApnIp), Lists.newArrayList());
    }

    private List<ApnIpDTO> readExcelToListApnIP(byte[] content) throws LogicException, Exception {
        listError.clear();
        List<ApnIpDTO> listApnIp = Lists.newArrayList();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();
        int rowValid = 0;
        for (int i = 3; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            String apnId = ex.getStringValue(row.getCell(0));
            String ip = ex.getStringValue(row.getCell(1));
            String centerCode = ex.getStringValue(row.getCell(2));
            String subNet = ex.getStringValue(row.getCell(3));
            String status = ex.getStringValue(row.getCell(4));
            String errMessage = "";

            if (!DataUtil.isNullObject(apnId) || !DataUtil.isNullObject(ip) || !DataUtil.isNullObject(centerCode) || !DataUtil.isNullObject(subNet) || !DataUtil.isNullObject(status)) {
                rowValid++;
                //validate tren du lieu
                if (DataUtil.isNullObject(apnId) || !DataUtil.isNumber(apnId.trim()) || apnId.trim().length() > 10 || DataUtil.safeToLong(apnId.trim()) <= 0) {
                    errMessage += BundleUtil.getText("import.apn.apn_id.error");
                    String[] error = {apnId, ip, centerCode, subNet, status, errMessage};
                    listError.add(error);
                    continue;
                }
                if (DataUtil.isNullObject(ip) || ip.trim().length() > 50 || !ip.matches("^([\\d\\w\\.\\:])+$")) {
                    errMessage += BundleUtil.getText("import.apn.ip.error");
                    String[] error = {apnId, ip, centerCode, subNet, status, errMessage};
                    listError.add(error);
                    continue;
                }
                if (DataUtil.isNullObject(centerCode) || centerCode.trim().length() > 50) {
                    errMessage += BundleUtil.getText("import.apn.center_code.error");
                    String[] error = {apnId, ip, centerCode, subNet, status, errMessage};
                    listError.add(error);
                    continue;
                }

                if (DataUtil.isNullObject(subNet) || subNet.trim().length() > 50) {
                    errMessage += BundleUtil.getText("import.apn.subnet.error");
                    String[] error = {apnId, ip, centerCode, subNet, status, errMessage};
                    listError.add(error);
                    continue;
                }

                if (DataUtil.isNullObject(status) || (!DataUtil.safeEqual(status.trim(), "0") && !DataUtil.safeEqual(status.trim(), "1"))) {
                    errMessage += BundleUtil.getText("import.apn.status.error");
                    String[] error = {apnId, ip, centerCode, subNet, status, errMessage};
                    listError.add(error);
                    continue;
                }
                // check duplicate tren file
                if (checkDuplicateApnIp(listApnIp, apnId, ip)) {
                    errMessage += BundleUtil.getText("import.apn.ip.dup");
                    String[] error = {apnId, ip, centerCode, subNet, status, errMessage};
                    listError.add(error);
                    continue;
                }
            } else {
                continue;
            }
            ApnIpDTO apnIp = new ApnIpDTO();
            apnIp.setApnId(DataUtil.safeToLong(apnId));
            apnIp.setIp(ip.trim());
            apnIp.setCenterCode(centerCode.trim());
            apnIp.setSubNet(subNet.trim());
            apnIp.setStatus(DataUtil.safeToLong(status.trim()));
            apnIp.setCreateUser(currentStaff.getStaffCode());
            apnIp.setLastUpdateUser(currentStaff.getStaffCode());
            listApnIp.add(apnIp);
        }
        if (rowValid > maxRow) {
            listError = Lists.newArrayList();
            throw new LogicException("", "mn.stock.status.isdn.delete.maxline", maxRow);
        }
        //check trung DB
        if (!DataUtil.isNullOrEmpty(listApnIp)) {
            List<ApnIpDTO> listDupApnId = checkDupicateApnIpDB(listApnIp);
            for (ApnIpDTO ob : listDupApnId) {
                for (int i = listApnIp.size() - 1; i >= 0; i--) {
                    ApnIpDTO apnIpDTO = listApnIp.get(i);
                    if (apnIpDTO.getApnId().equals(ob.getApnId()) && apnIpDTO.getIp().equals(ob.getIp())) {
                        listApnIp.remove(i);
                        String[] error = {apnIpDTO.getApnId().toString(), apnIpDTO.getIp(), apnIpDTO.getCenterCode(), apnIpDTO.getSubNet(), apnIpDTO.getStatus().toString(), BundleUtil.getText("import.apn.ip.dup")};
                        listError.add(error);
                    }
                }
            }
        }
        if (!DataUtil.isNullOrEmpty(listError)) {
            try {
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName("IMPORT_APN_IP_TEMPLATE_ERROR.xls");
                bean.putValue("lstData", listError);
                bookError = FileUtil.exportWorkBook(bean);
            } catch (Exception e) {
                reportError("", "common.error.happen", e);
                topPage();
            }
        }
        return listApnIp;
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
                if (importType.equals(Const.IMPORT_APN_TYPE.IMPORT_APN)) {
                    List<ApnDTO> listApn = readExcelToListApn(contentByte);
                    if (DataUtil.isNullOrEmpty(listApn)) {
                        reportError("", "", "import.apn.not.success");
                    } else {
                        BaseMessage msg = apnService.insertListAPN(listApn);
                        if (msg.isSuccess()) {
                            reportSuccess("", "import.apn.success", (listApn.size()) + "/" + (listApn.size() + listError.size()));
                        } else {
                            reportError("", "", msg.getKeyMsg());
                        }
                    }
                } else if (importType.equals(Const.IMPORT_APN_TYPE.IMPORT_APN_IP)) {
                    List<ApnIpDTO> listApnIp = readExcelToListApnIP(contentByte);
                    if (DataUtil.isNullOrEmpty(listApnIp)) {
                        reportError("", "", "import.apn_ip.not.success");
                    } else {
                        BaseMessage msg = apnIpService.insertListAPNIP(listApnIp);
                        if (msg.isSuccess()) {
                            reportSuccess("", "import.apn_ip.success", (listApnIp.size()) + "/" + (listApnIp.size() + listError.size()));
                        } else {
                            reportError("", "", msg.getKeyMsg());
                        }
                    }
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
        }
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = null;
            String fileNameTemplate = "template";
            if (!DataUtil.isNullObject(importType) && "1".equals(importType)) {
                createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_APN_TEMPLATE_PATH);
                fileNameTemplate = FILE_APN_TEMPLATE_PATH;
            } else if (!DataUtil.isNullObject(importType) && "2".equals(importType)) {
                createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_APN_IP_TEMPLATE_PATH);
                fileNameTemplate = FILE_APN_IP_TEMPLATE_PATH;
            }

            if (createStream != null) {
                HSSFWorkbook workbook = new HSSFWorkbook(createStream);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileNameTemplate + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                workbook.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
            }
            return;
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
        }
    }

    @Secured("@")
    public void downloadFileError() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "importApnError.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            bookError.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
            topPage();
        }
    }

    @Secured("@")
    public void prepareData(ApnDTO apnDTO, boolean isEdit) {
        try {
            if (!checkStaffConnection(currentStaff.getStaffCode())) {
//                reportError("", "", "staff.code.invalid");
                throw new LogicException("", "staff.code.invalid");
            }
            this.isEdit = isEdit;
            if (isEdit) {
                inputAPNAdd = apnService.findApnById(apnDTO.getApnId());
                if (DataUtil.isNullObject(inputAPNAdd)) {
                    throw new LogicException("", "mn.apnim.error.apnIP");
                }
            } else {
                inputAPNAdd = new ApnDTO();
                inputAPNAdd.setStatus(Const.LONG_OBJECT_ONE);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_STANDARD.ERROR_INNER_VALIDATE, "mn.apnim.error.apn");
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
        }
    }

    @Secured("@")
    public void prepareDataIp(ApnIpDTO apnIpDTO, boolean isEdit) {
        try {
            ApnDTO apnDTO = new ApnDTO();
            if (!DataUtil.isNullObject(apnIpDTO) &&
                    !DataUtil.isNullOrZero(apnIpDTO.getApnId())) {
                apnDTO = apnService.findOne(apnIpDTO.getApnId());
            }
            if (!checkStaffConnection(currentStaff.getStaffCode())) {
//                reportError("", "", "staff.code.invalid");
                throw new LogicException("", "staff.code.invalid");
            }
            this.isEdit = isEdit;
            if (isEdit) {
                inputApnIpAdd = apnIpService.findApnIpById(apnIpDTO.getApnIpId());
                if (DataUtil.isNullObject(inputApnIpAdd)) {
                    throw new LogicException("", "", "mn.apnim.error.apnIP");
                }
                if (DataUtil.isNullOrZero(apnDTO.getStatus()))
                    apnSelectedAdd = apnService.findOne(apnIpDTO.getApnId());
                //            apnIpService.searchApnIp(inputApnIp);

            } else {
                inputApnIpAdd = new ApnIpDTO();
                inputApnIpAdd.setStatus(Const.LONG_OBJECT_ONE);
                apnSelectedAdd = null;
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_STANDARD.ERROR_INNER_VALIDATE, "mn.apnim.error.apnIP");
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
        }
    }

    @Secured("@")
    public void doAddApn(Boolean isEdit) {
        try {
            if (isEdit != null) {
                doValidate();
            }
            Date sysdate = optionSetValueService.getSysdateFromDB(false);
            if (!checkStaffConnection(currentStaff.getStaffCode())) {
//                reportError("", "", "staff.code.invalid");
                throw new LogicException("", getText("staff.code.invalid"));
            }
            //case Xoa
            if (isEdit == null) {
                if (validateApnHasApnIP()) {
                    throw new LogicException("", getTextParam("mn.import.apn.delete.validate.has.APNIP", inputAPNAdd.getApnCode()));
                }
                inputAPNAdd.setStatus(Const.STOCK_TRANS_RESCUE.STATUS_DELETE);
            } // case them
            else if (!isEdit) {
                inputAPNAdd.setCreateUser(currentStaff.getStaffCode());
                inputAPNAdd.setCreateTime(sysdate);
                ApnDTO apnDTO = apnService.findApnById(inputAPNAdd.getApnId());
                if (!DataUtil.isNullObject(apnDTO)) {
//                    reportError("", ErrorCode.ERROR_STANDARD.ERROR_INSERT, getTextParam("mn.import.apn.create.validate", inputAPNAdd.getApnCode()));
                    throw new LogicException("", getTextParam("mn.import.apn.create.validate", apnDTO.getApnCode()));
                }
            }
            inputAPNAdd.setLastUpdateUser(currentStaff.getStaffCode());
            inputAPNAdd.setLastUpdateTime(sysdate);
            apnService.create(inputAPNAdd);
            if (isEdit == null) {
                reportSuccess("", "common.msg.success.delete");
            } else if (!isEdit) {
                reportSuccess("", "common.msg.success.create");
            } else {
                reportSuccess("", "common.msg.success.update");
            }
            searchApn();
        } catch (LogicException ex) {
            topReportError("", ex);

        } catch (Exception ex) {
            reportError("", getText("common.error.happened") + ex.getMessage(), ex);
            topPage();
        }
    }

/**/

    @Secured("@")
    public void doAddApnIp(Boolean isEdit) {
        try {
            if (isEdit != null) {
                doValidateIp();
            }
            Date sysdate = optionSetValueService.getSysdateFromDB(false);
            if (isEdit == null) {
                inputApnIpAdd.setStatus(DataUtil.safeToLong(Const.STOCK_TRANS_RESCUE.STATUS_DELETE));
            } else if (!isEdit) {
                inputApnIpAdd.setCreateUser(currentStaff.getStaffCode());
                inputApnIpAdd.setCreateTime(sysdate);
                ApnIpDTO apnipDTO = apnIpService.findApnIpById(inputApnIpAdd.getApnIpId());
                if (!DataUtil.isNullObject(apnipDTO)) {
                    throw new LogicException("", ErrorCode.ERROR_STANDARD.ERROR_INSERT, "mn.import.apnIp.create.validate", inputApnIpAdd.getIp());
                }
            }
            inputApnIpAdd.setLastUpdateUser(currentStaff.getStaffCode());
            inputApnIpAdd.setLastUpdateTime(sysdate);
            apnIpService.create(inputApnIpAdd);
            if (isEdit == null) {
                reportSuccess("", "common.msg.success.delete");
            } else if (!isEdit) {
                reportSuccess("", getTextParam("msg.apnIp.success.create", inputApnIpAdd.getIp()));
            } else {
                reportSuccess("", "common.msg.success.update");
            }
            searchApnIp();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            reportError("", getText("common.error.happened") + ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidate() throws Exception {
        if (!checkStaffConnection(currentStaff.getStaffCode())) {
//            reportError("", "", "staff.code.invalid");
            focusElementByRawCSSSlector(".apnForIp");
            throw new LogicException("", "staff.code.invalid");
        }
        if (DataUtil.isNullObject(inputAPNAdd.getApnCode())) {
            reportError("", "", "mn.import.apn.code.require");
            focusElementByRawCSSSlector(".apnCodeTxt");
        }
        if (!DataUtil.isNullObject(inputAPNAdd.getApnCode()) && inputAPNAdd.getApnCode().length() > 50) {
            reportError("", "", "mn.import.apn.code.over");
            focusElementByRawCSSSlector(".apnCodeTxt");
        }
        if (DataUtil.isNullObject(inputAPNAdd.getApnName())) {
            reportError("", "", "mn.import.apn.name.require");
            focusElementByRawCSSSlector(".apnNameTxt");
        }
        if (!DataUtil.isNullObject(inputAPNAdd.getApnName()) && inputAPNAdd.getApnName().length() > 100) {
            reportError("", "", "mn.import.apn.name.over");
            focusElementByRawCSSSlector(".apnNameTxt");
        }
        if (DataUtil.isNullObject(inputAPNAdd.getCenterCode())) {
            reportError("", "", "mn.import.apn.name.require");
            focusElementByRawCSSSlector(".centerCode");
        }
        if (!DataUtil.isNullObject(inputAPNAdd.getCenterCode()) && inputAPNAdd.getCenterCode().length() > 50) {
            reportError("", "", "mn.import.apn.name.require");
            focusElementByRawCSSSlector(".centerCode");
        }
        if (!DataUtil.isNullObject(inputAPNAdd.getDescription()) && inputAPNAdd.getDescription().length() > 500) {
            reportError("", "", "mn.import.apn.description.over");
            focusElementByRawCSSSlector(".decscription");
        }
        if (!isEdit) {
            inputAPNAdd.setStatus(Const.LONG_OBJECT_ONE);
            if (!DataUtil.isNullObject(inputAPNAdd.getApnCode())) {
                ApnDTO validateDTO = new ApnDTO();
                validateDTO.setApnCode(inputAPNAdd.getApnCode().toLowerCase());
                List<ApnDTO> listChecked = apnService.searchApnCorrect(validateDTO);
                if (!DataUtil.isNullOrEmpty(listChecked)) {
//                    reportError("", "", "mn.import.apn.code.dup");
                    focusElementByRawCSSSlector(".apnCodeTxt");
                    throw new LogicException("", "mn.import.apn.code.dup");
                }
            }
        }
        topPage();

    }


    @Secured("@")
    public void doValidateIp() throws Exception {
        if (!checkStaffConnection(currentStaff.getStaffCode())) {
//            reportError("", "", "staff.code.invalid");
            focusElementByRawCSSSlector(".apnForIp");
            throw new LogicException("", "staff.code.invalid");
        }
        if (DataUtil.isNullObject(apnSelectedAdd)) {
            inputApnIpAdd.setApnId(null);
        } else {
            inputApnIpAdd.setApnId(apnSelectedAdd.getApnId());
        }
        if (DataUtil.isNullOrZero(inputApnIpAdd.getApnId())) {
            reportError("", "", "mn.import.apnip.apn.not.null");
            focusElementByRawCSSSlector(".apnForIp");
        }

        if (DataUtil.isNullObject(apnService.findOne(inputApnIpAdd.getApnId()))) {
            reportError("", "", "mn.import.apnip.apn.not.null");
            focusElementByRawCSSSlector(".apnForIp");
        }

        if (DataUtil.isNullOrEmpty(inputApnIpAdd.getIp())) {
            reportError("", "", "mn.import.apnip.ip.require");
            focusElementByRawCSSSlector(".iptxt");
        }

        if (!DataUtil.isNullOrEmpty(inputApnIpAdd.getIp())) {
            if (!DataUtil.validateStringByPattern(inputApnIpAdd.getIp(), Const.regexIpv4)
                    && !DataUtil.validateStringByPattern(inputApnIpAdd.getIp(), Const.regexIpv6)) {
                reportError("", "", "mn.import.apnip.ip.invalid");
                focusElementByRawCSSSlector(".iptxt");

            }
            if (DataUtil.validateStringByPattern(inputApnIpAdd.getIp(), Const.regexIpv4)
                    || DataUtil.validateStringByPattern(inputApnIpAdd.getIp(), Const.regexIpv6)) {
                String ip = normalizeIP(inputApnIpAdd.getIp());
                inputApnIpAdd.setIp(ip);
            }
        }

        if (DataUtil.isNullObject(inputApnIpAdd.getCenterCode())) {
            reportError("", "", "mn.import.apn.center.code.require");
            focusElementByRawCSSSlector(".centerCodeIp");
        }

        if (!DataUtil.isNullObject(inputApnIpAdd.getCenterCode()) && inputApnIpAdd.getCenterCode().length() > 50) {
            reportError("", "", "mn.import.apn.center.code.over");
            focusElementByRawCSSSlector(".centerCodeIp");
        }

        if (DataUtil.isNullObject(inputApnIpAdd.getSubNet())) {
            reportError("", "", getText("mn.import.apnip.subnet.not.null"));
            focusElementByRawCSSSlector(".subnet");
        }
        if (!DataUtil.isNullObject(inputApnIpAdd.getSubNet()) && inputApnIpAdd.getSubNet().length() > 50) {
            reportError("", "", "mn.import.apn.subnet.over");
            focusElementByRawCSSSlector(".subnet");
        }
//        if (!DataUtil.isNullObject(inputApnIpAdd.getSubNet()) && !DataUtil.validateStringByPattern(inputApnIpAdd.getSubNet(), Const.regexIpv4) && !DataUtil.validateStringByPattern(inputApnIpAdd.getSubNet(), Const.regexIpv6)) {
//            reportError("", "", "mn.import.apnip.subnet.invalidate");
//            focusElementByRawCSSSlector(".subnet");
//        }
        if (!isEdit) {
            inputApnIpAdd.setStatus(Const.LONG_OBJECT_ONE);
        }
        if (!DataUtil.isNullObject(inputApnIpAdd.getApnId()) && !DataUtil.isNullObject(inputApnIpAdd.getIp())) {
            ApnIpDTO validateIP = new ApnIpDTO();
            validateIP.setIp(inputApnIpAdd.getIp());
            List<ApnIpDTO> listChecked = apnIpService.searchApnIp(validateIP);
            if (!DataUtil.isNullOrEmpty(listChecked)) {
                if (DataUtil.isNullObject(inputApnIpAdd.getApnId())
                        || !DataUtil.safeEqual(inputApnIpAdd.getApnIpId(), listChecked.get(0).getApnIpId())) {
//                    reportError("", "", "mn.import.apnip.code.dup");
                    focusElementByRawCSSSlector(".iptxt");
                    throw new LogicException("", "mn.import.apnip.code.dup");
                }
            }
        }
        topPage();
    }

    @Secured("@")
    public void searchApn() {
        try {
            listApn = DataUtil.defaultIfNull(apnService.searchApn(inputAPN), Lists.newArrayList());
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
            topPage();
        }
    }

    @Secured("@")
    public void searchApnIp() {
        try {
            if (!DataUtil.isNullObject(apnSelected)) {
                inputApnIp.setApnId(apnSelected.getApnId());
            } else {
                inputApnIp.setApnId(null);
            }

            listApnIp = DataUtil.defaultIfNull(apnIpService.searchApnIp(inputApnIp), Lists.newArrayList());
        } catch (Exception ex) {
            reportError("", getText("common.error.happened"), ex);
            topPage();
        }
    }

    @Secured("@")
    public List<ApnDTO> autoCompleteApn(String input) throws Exception {
        listApnForIp = apnService.searchApnAutoComplete(input);
        return listApnForIp;
    }

    @Secured("@")
    public void resetAutoComplete() throws Exception {
        listApnForIp = new ArrayList<>();
        apnSelected = new ApnDTO();
    }

    @Secured("@")
    public String normalizeIP(String ip) throws Exception {
        String[] parts = ip.split("\\.");
        List<Integer> lstInt = new ArrayList<>();
        for (String str : parts) {
            lstInt.add(Integer.valueOf(str));
        }
        return lstInt.get(0) + "." + lstInt.get(1) + "." + lstInt.get(2) + "." + lstInt.get(3);
    }

    @Secured("@")
    public void btnResetAPNIP() {
        inputApnIp = new ApnIpDTO();
        apnSelected = new ApnDTO();
        focusElementByRawCSSSlector(".apnForIp");
    }

    @Secured("@")
    public void btnResetAPN() {
        inputAPN = new ApnDTO();
        focusElementByRawCSSSlector(".apnCodeTxt");
    }


    private boolean checkStaffConnection(String staffCode) throws Exception {
        StaffDTO staffDTO = staffService.getStaffByStaffCode(staffCode);
        if (DataUtil.isNullObject(staffDTO)
                || DataUtil.isNullOrZero(staffDTO.getStaffId())) {
            return false;
        }
        return true;
    }

    //validate APN has IP
    private boolean validateApnHasApnIP() throws Exception {
        ApnIpDTO apnIpDTO = new ApnIpDTO();
        apnIpDTO.setApnId(inputAPNAdd.getApnId());
        List<ApnIpDTO> apnIpDTOs = apnIpService.searchApnIp(apnIpDTO);
        if (!DataUtil.isNullOrEmpty(apnIpDTOs)) {
            return true;
        }
        return false;
    }

    @Secured("@")
    public void onChangeStatusAPN() {
        inputAPN.setStatus(null);
        if (DataUtil.isNullOrEmpty(apnStatus)) {
            return;
        }
        inputAPN.setStatus(Long.valueOf(apnStatus));
    }

    @Secured("@")
    public void onChangeStatusAPNIP() {
        inputApnIp.setStatus(null);
        if (DataUtil.isNullOrEmpty(apnIpStatus)) {
            return;
        }
        inputApnIp.setStatus(Long.valueOf(apnIpStatus));
    }

    public String getImportType() {
        return importType;
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

    public byte[] getContentByte() {
        return contentByte;
    }

    public void setContentByte(byte[] contentByte) {
        this.contentByte = contentByte;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public List<String[]> getListError() {
        return listError;
    }

    public void setListError(List<String[]> listError) {
        this.listError = listError;
    }

    public ApnDTO getInputAPN() {
        return inputAPN;
    }

    public void setInputAPN(ApnDTO inputAPN) {
        this.inputAPN = inputAPN;
    }

    public List<ApnDTO> getListApn() {
        return listApn;
    }

    public ApnDTO getInputAPNAdd() {
        return inputAPNAdd;
    }

    public void setInputAPNAdd(ApnDTO inputAPNAdd) {
        this.inputAPNAdd = inputAPNAdd;
    }

    public void setListApn(List<ApnDTO> listApn) {
        this.listApn = listApn;
    }

    public ApnIpDTO getInputApnIp() {
        return inputApnIp;
    }

    public void setInputApnIp(ApnIpDTO inputApnIp) {
        this.inputApnIp = inputApnIp;
    }

    public ApnIpDTO getInputApnIpAdd() {
        return inputApnIpAdd;
    }

    public void setInputApnIpAdd(ApnIpDTO inputApnIpAdd) {
        this.inputApnIpAdd = inputApnIpAdd;
    }

    public List<ApnDTO> getListApnForIp() {
        return listApnForIp;
    }

    public void setListApnForIp(List<ApnDTO> listApnForIp) {
        this.listApnForIp = listApnForIp;
    }

    public List<ApnIpDTO> getListApnIp() {
        return listApnIp;
    }

    public void setListApnIp(List<ApnIpDTO> listApnIp) {
        this.listApnIp = listApnIp;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public ApnDTO getApnSelected() {
        return apnSelected;
    }

    public void setApnSelected(ApnDTO apnSelected) {
        this.apnSelected = apnSelected;
    }

    public ApnDTO getApnSelectedAdd() {
        return apnSelectedAdd;
    }

    public void setApnSelectedAdd(ApnDTO apnSelectedAdd) {
        this.apnSelectedAdd = apnSelectedAdd;
    }

    public String getApnStatus() {
        return apnStatus;
    }

    public void setApnStatus(String apnStatus) {
        this.apnStatus = apnStatus;
    }

    public String getApnIpStatus() {
        return apnIpStatus;
    }

    public void setApnIpStatus(String apnIpStatus) {
        this.apnIpStatus = apnIpStatus;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}

package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.IsdnConst;
import com.viettel.bccs.inventory.common.IsdnUtil;
import com.viettel.bccs.inventory.dto.DeleteNumberDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tuyendv8 on 11/19/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "deleteIsdnController")
public class DeleteIsdnController extends InventoryController {

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private StockNumberService stockNumberService;

    private DeleteNumberDTO deleteStockNumberDTO = new DeleteNumberDTO();
    ;
    private static final String FILE_ERROR_TEMPLATE_PATH = "deleteIsdnFromStock_error.xls";
    private static final String FILE_TEMPLATE_PATH = "deleteIsdnFromStock.xls";
    private List<OptionSetValueDTO> optionSetValueDTOs = Lists.newArrayList();
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private boolean hasFileError = false;
    private boolean enableDelete = false;
    private String attachFileName;
    private ExcellUtil processingFile;
    private List<DeleteNumberDTO> lstProcess = new ArrayList<DeleteNumberDTO>();

    @PostConstruct
    public void init() {
        initOptionSet();
    }

    public void initOptionSet() {
        try {
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
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "deleteIsdnFromStock.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void downloadFileError() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_ERROR_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            validateFileInput(workbook);
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
    }

    @Secured("@")
    public void previewDelete() {
        lstProcess = Lists.newArrayList();
        try {
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            int errorCode = validateFileInput(null);
            if (errorCode == 1000) {
                reportErrorValidateFail("frmDelete:deleteMsgs", "", "mn.stock.status.isdn.delete.maxline");
                return;
            }

            if (DataUtil.isNullOrEmpty(lstProcess)) {
                reportErrorValidateFail("frmDelete:deleteMsgs", "", "mn.stock.status.isdn.delete.nodata");
                return;
            }

            lstProcess = stockNumberService.previewDeleteNumber(lstProcess, deleteStockNumberDTO);

            for (DeleteNumberDTO dto : lstProcess) {
                if (!DataUtil.isNullObject(dto)) {
                    if (!IsdnConst.DEL_AVAIL.equals(dto.getResult())) {
                        hasFileError = true;
                        if (enableDelete) {
                            break;
                        }
                    } else {
                        enableDelete = true;
                        if (hasFileError) {
                            break;
                        }
                    }
                }
            }
        } catch (LogicException le) {
            logger.error(le.getMessage(), le);
            reportError("", le);
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
        } finally {
            updateElemetId("frmDelete:deleteMsgs");
        }

    }

    private int validateFileInput(HSSFWorkbook errorWorkbook) {
        try {
            Sheet sheetProcess = processingFile.getSheetAt(0);
            Sheet errorSheet = null;
            boolean isError = false;
            if (errorWorkbook != null) {
                errorSheet = errorWorkbook.getSheetAt(0);
                isError = true;
            }
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            if (totalRow <= 2) {
                return 1;
            }
            if (totalRow > 1000) {
                return 1000;
            }
            int j = 1;
            List<DeleteNumberDTO> checkDup = new ArrayList<DeleteNumberDTO>();
            for (int i = 2; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    hasFileError = true;
                    if (isError) {
                        Row errorRow = errorSheet.createRow(j);
                        errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.row.null"));
//                        j++;
                    }
                    break;
                }
                int totalCol = processingFile.getTotalColumnAtRow(row);
                if (totalCol > 0) {
                    boolean contentLineNull = true;
                    for (int k = 0; k < totalCol; k++) {
                        String cellContent = processingFile.getStringValue(row.getCell(k));
                        if (!DataUtil.isNullOrEmpty(cellContent)) {
                            contentLineNull = false;
                        }
                    }
                    if (contentLineNull) {
                        continue;
                    }
                }

                if (totalCol < 2) {
                    hasFileError = true;
                    if (isError) {
                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.delete.digit.isdn.wrongformat"));
                        j++;
                    }
                    continue;
                }

                String fromIsdn = processingFile.getStringValue(row.getCell(0));
                String toISDN = processingFile.getStringValue(row.getCell(1));
                //kiem tra dong trong
                if (DataUtil.isNullOrEmpty(fromIsdn) && DataUtil.isNullOrEmpty(toISDN)) {
//                    hasFileError = true;
//                    if (isError) {
//                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.transfer.digit.isdn.from.number"));
//                        j++;
//                    }
                    continue;
                }
                if (DataUtil.isNullOrEmpty(fromIsdn) || !DataUtil.isNumber(fromIsdn)
                        || !IsdnUtil.validateNumberAndLength(fromIsdn, IsdnUtil.MIN_LENGTH, IsdnUtil.MAX_LENGTH)) {
                    hasFileError = true;
                    if (isError) {
                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.transfer.digit.isdn.from.number"));
                        j++;
                    }
                    continue;
                }

                if (DataUtil.isNullOrEmpty(toISDN) || !DataUtil.isNumber(toISDN)
                        || !IsdnUtil.validateNumberAndLength(toISDN, IsdnUtil.MIN_LENGTH, IsdnUtil.MAX_LENGTH)) {
                    hasFileError = true;
                    if (isError) {
                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.transfer.digit.isdn.to.number"));
                        j++;
                    }
                    continue;
                }

                //kiem tra ban ghi trung
//                if (checkDuplicate.contains(fromIsdn + "_" + toISDN)) {
//                    hasFileError = true;
//                    if (isError) {
//                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.manage.code.NGN.dup"));
//                        j++;
//                    }
//                    continue;
//                } else {
//                    checkDuplicate.add(fromIsdn + "_" + toISDN);
//                }

                if ((DataUtil.safeToLong(fromIsdn) > DataUtil.safeToLong(toISDN))) {
                    hasFileError = true;
                    if (isError) {
                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.delete.digit.isdn.from.to"));
                        j++;
                    }
                    continue;
                }
                //kiem tra chom
                DeleteNumberDTO deleteItem = new DeleteNumberDTO();
                Long from = DataUtil.safeToLong(fromIsdn);
                Long to = DataUtil.safeToLong(toISDN);
                deleteItem.setFromISDN(from);
                deleteItem.setToISDN(to);
                deleteItem.setRowKey(i + "");
                //kiem tra ban ghi chom
                if (containsRange(checkDup, from, to)) {
                    hasFileError = true;
                    if (isError) {
                        createErrorRow(errorSheet, row, totalCol, j, getText("mn.isdn.manage.code.NGN.dup"));
                        j++;
                    }
                    continue;
                } else {
                    checkDup.add(deleteItem);
                }
                if (!isError) {
                    lstProcess.add(deleteItem);
                } else if (!DataUtil.isNullOrEmpty(lstProcess)) {
                    for (DeleteNumberDTO dto : lstProcess) {
                        if (!DataUtil.isNullObject(dto)) {
                            if (dto.getFromISDN() == DataUtil.safeToLong(fromIsdn).longValue()
                                    && dto.getToISDN() == DataUtil.safeToLong(toISDN).longValue()) {
                                if (!IsdnConst.DEL_AVAIL.equals(dto.getResult()) && !IsdnConst.DEL_SUCCESS.equals(dto.getResult())) {
                                    hasFileError = true;
                                    createErrorRow(errorSheet, row, totalCol, j, getResultDesc(dto.getResult()));
                                    j++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return 0;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
        }
        return -1;
    }

    public boolean containsRange(List<DeleteNumberDTO> lstDelete, Long from, Long to) {
        if (DataUtil.isNullOrEmpty(lstDelete)) {
            return false;
        }
        for (DeleteNumberDTO dto : lstDelete) {
            if (((from >= dto.getFromISDN()) && (from <= dto.getToISDN()))
                    || ((to >= dto.getFromISDN() && (to <= dto.getToISDN())))) {
                return true;
            }
            if (((dto.getFromISDN() >= from) && (dto.getFromISDN() <= to))
                    || ((dto.getToISDN() >= from) && (dto.getToISDN() <= to))) {
                return true;
            }
        }
        return false;
    }

    public void createErrorRow(Sheet errorSheet, Row row, int totalCol, int j, String cellValue) throws Exception {
        Row errorRow = errorSheet.createRow(j);
        for (int k = 0; k < totalCol; k++) {
            errorRow.createCell(k).setCellValue(processingFile.getStringValue(row.getCell(k)));
        }
        errorRow.createCell(2).setCellValue(cellValue);
    }


    @Secured("@")
    public String getResultDesc(String key) {
        if (IsdnConst.DEL_SUCCESS.equals(key)) {
            return getText("mn.stock.status.isdn.delete.success");
        }
        if (IsdnConst.DEL_AVAIL.equals(key)) {
            return getText("mn.stock.status.isdn.delete.avail");
        }
        if (IsdnConst.DEL_INVALID_FROM.equals(key)) {
            return getText("mn.stock.status.isdn.from.length");
        }
        if (IsdnConst.DEL_INVALID_TO.equals(key)) {
            return getText("mn.stock.status.isdn.to.length");
        }
        if (IsdnConst.DEL_NOT_RANGE.equals(key)) {
            return getText("mn.isdn.delete.digit.list.msg.notrange");
        }
        if (IsdnConst.DEL_DOUBLE.equals(key)) {
            return getText("mn.isdn.delete.digit.isdn.dup");
        }
        return "xx";
    }

    @Secured("@")
    public void deleteNumber() {

        try {
            lstProcess = stockNumberService.deleteNumber(lstProcess, deleteStockNumberDTO, BccsLoginSuccessHandler.getUserName(), BccsLoginSuccessHandler.getIpAddress());
            reportSuccess("frmTransfer:transferMsgs", "mn.stock.status.isdn.delete.success");
        } catch (LogicException le) {
            reportError("", le.getKeyMsg(), le);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }

    }

    @Secured("@")
    public void doReset() {

        deleteStockNumberDTO = new DeleteNumberDTO();
        lstProcess = Lists.newArrayList();
        attachFileName = null;
        uploadedFile = null;
        hasFileError = false;
        enableDelete = false;
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            hasFileError = false;
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                uploadedFile = null;
                attachFileName = null;
                byteContent = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

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

    @Secured("@")
    public void doConfirmDelete() {
        if (!enableDelete) {
            reportErrorValidateFail("frmAssignStatus:assignTypeMsgs", "", "mn.stock.status.isdn.delete.noselect");
            return;
        }
    }

    public DeleteNumberDTO getDeleteStockNumberDTO() {
        return deleteStockNumberDTO;
    }

    public void setDeleteStockNumberDTO(DeleteNumberDTO deleteStockNumberDTO) {
        this.deleteStockNumberDTO = deleteStockNumberDTO;
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

    public List<OptionSetValueDTO> getOptionSetValueDTOs() {
        return optionSetValueDTOs;
    }

    public void setOptionSetValueDTOs(List<OptionSetValueDTO> optionSetValueDTOs) {
        this.optionSetValueDTOs = optionSetValueDTOs;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public List<DeleteNumberDTO> getLstProcess() {
        return lstProcess;
    }

    public void setLstProcess(List<DeleteNumberDTO> lstProcess) {
        this.lstProcess = lstProcess;
    }

    public boolean isEnableDelete() {
        return enableDelete;
    }

    public void setEnableDelete(boolean enableDelete) {
        this.enableDelete = enableDelete;
    }
}

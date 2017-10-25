package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.IsdnConst;
import com.viettel.bccs.inventory.common.IsdnUtil;
import com.viettel.bccs.inventory.dto.TransferNGNDTO;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
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
import java.util.List;

/**
 * @author:thanhnt77
 */
@Component
@Scope("view")
@ManagedBean(name = "tranferNGNBrancheController")
public class TranferNGNBrancheController extends InventoryController {

    @Autowired
    private StockNumberService stockNumberService;

    private static final String FILE_ERROR_TEMPLATE_PATH = "arrangeNgnIsdnToProvince_error.xls";
    private static final String FILE_TEMPLATE_PATH = "arrangeNgnIsdnToProvince.xls";

    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private boolean hasFileError = false;
    private String attachFileName;
    private ExcellUtil processingFile;
    private List<TransferNGNDTO> listTransfer = new ArrayList<TransferNGNDTO>();
    private List<TransferNGNDTO> listTransferAvail = new ArrayList<TransferNGNDTO>();
    private TransferNGNDTO transferDto = new TransferNGNDTO();
    private boolean enableTransfer;
    private String type;

    @Secured("@")
    @PostConstruct
    public void init() {
        transferDto = new TransferNGNDTO();
//        transferDto.setTelecomService(31L);
        type = "31";
    }

    @Secured("@")
    public void previewTransfer() {
        listTransfer = Lists.newArrayList();
        listTransferAvail = Lists.newArrayList();
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
                reportErrorValidateFail("frmTransfer:transferMsgs", "", "mn.stock.status.isdn.NGN.maxline");
                focusElementByRawCSSSlector(".outputAttachFile");
                return;
            }
            if (!DataUtil.isNullOrEmpty(listTransfer)) {
                transferDto.setTelecomService(DataUtil.safeToLong(type));
                listTransfer = stockNumberService.previewTransfer(listTransfer, transferDto);
            }
//            if (!DataUtil.isNullOrEmpty(listTransfer)) {
//                enableTransfer = true;
//            }
            for (TransferNGNDTO dto : listTransfer) {
                if (!IsdnConst.SUCCESS.equals(dto.getResult())) {
                    hasFileError = true;
                } else {
                    enableTransfer = true;
                    listTransferAvail.add(dto);
                }
            }
            if (!enableTransfer) {
                reportErrorValidateFail("frmTransfer:transferMsgs", "", "mn.isdn.manage.code.NGN.file.wrong");
                topPage();
            }

        } catch (LogicException le) {
            logger.error(le.getMessage(), le);
            reportError("", le);
            topPage();
        } catch (Exception ex) {
//            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void transfer() {
        try {
            listTransferAvail = stockNumberService.transferNGN(listTransferAvail, transferDto, BccsLoginSuccessHandler.getUserName(), BccsLoginSuccessHandler.getIpAddress());
            reportSuccess("frmTransfer:transferMsgs", "mn.stock.status.isdn.ngn.success");
            enableTransfer = false;
            topPage();
        } catch (LogicException le) {
            logger.error(le.getMessage(), le);
            reportError("", "", le.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doConfirmTransfer() {
        if (!enableTransfer) {
            reportErrorValidateFail("frmTransfer:transferMsgs", "", "mn.stock.status.isdn.NGN.noselect");
            topPage();
            return;
        }
    }

    @Secured("@")
    public void doReset() {
        attachFileName = null;
        uploadedFile = null;
        hasFileError = false;
        enableTransfer = false;
        type = "31";
        listTransfer = Lists.newArrayList();
        listTransferAvail = Lists.newArrayList();
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {

        try {
            uploadedFile = event.getFile();
            hasFileError = false;
            enableTransfer = false;
            listTransferAvail = Lists.newArrayList();
            listTransfer = Lists.newArrayList();

            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                byteContent = null;
                uploadedFile = null;
                attachFileName = null;
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
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_TEMPLATE_PATH);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "arrangeNgnIsdnToProvince.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
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
            if (totalRow <= 1) {
                return 1;
            }
            int j = 1;
            if (totalRow > 1000) {
                return 1000;
            }
            List<String> checkDup = new ArrayList<String>();
            for (int i = 1; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    hasFileError = true;
                    if (isError) {
                        Row errorRow = errorSheet.createRow(j);
                        errorRow.createCell(2).setCellValue(getText("mn.stock.status.isdn.file.row.null"));
                        //j++;
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
                            break;
                        }
                    }
                    if (contentLineNull) {
                        continue;
                    }
                }
                if (totalCol < 2) {
                    hasFileError = true;
                    if (isError) {
                        Row errorRow = errorSheet.createRow(j);
                        for (int k = 0; k < totalCol; k++) {
                            errorRow.createCell(k).setCellValue(processingFile.getStringValue(row.getCell(k)));
                        }
                        errorRow.createCell(2).setCellValue(getText("mn.isdn.transfer.digit.isdn.null"));
                        j++;
                    }
                    continue;
                }
                String isdn = processingFile.getStringValue(row.getCell(0)).trim();
                if (DataUtil.isNullOrEmpty(isdn) || !DataUtil.isNumber(isdn)
                        || !IsdnUtil.validateNumberAndLength(isdn, IsdnUtil.MIN_LENGTH, IsdnUtil.MAX_LENGTH)) {
                    hasFileError = true;
                    if (isError) {
                        Row errorRow = errorSheet.createRow(j);
                        for (int k = 0; k < totalCol; k++) {
                            errorRow.createCell(k).setCellValue(processingFile.getStringValue(row.getCell(k)));
                        }
                        errorRow.createCell(2).setCellValue(getText("mn.isdn.transfer.digit.isdn.number"));
                        j++;
                    }
                    continue;
                }
                String areaCode = processingFile.getStringValue(row.getCell(1)).trim();
                if (DataUtil.isNullOrEmpty(areaCode)) {
                    hasFileError = true;
                    if (isError) {
                        Row errorRow = errorSheet.createRow(j);
                        for (int k = 0; k < totalCol; k++) {
                            errorRow.createCell(k).setCellValue(processingFile.getStringValue(row.getCell(k)));
                        }
                        errorRow.createCell(2).setCellValue(getText("mn.isdn.transfer.digit.isdn.null"));
                        j++;
                    }
                    continue;
                }

                //kiem tra ban ghi trung
                if (checkDup.contains(isdn)) {
                    hasFileError = true;
                    if (isError) {
                        Row errorRow = errorSheet.createRow(j);
                        for (int k = 0; k < totalCol; k++) {
                            errorRow.createCell(k).setCellValue(processingFile.getStringValue(row.getCell(k)));
                        }
                        errorRow.createCell(2).setCellValue(getText("mn.isdn.delete.digit.isdn.dup"));
                        j++;
                    }
                    continue;
                } else {
                    checkDup.add(isdn);
                }

                if (!isError) {
                    TransferNGNDTO transferNGNDTO = new TransferNGNDTO();
                    transferNGNDTO.setIsdn(DataUtil.safeToLong(isdn));
                    transferNGNDTO.setNewAreaCode(areaCode);
                    transferNGNDTO.setRowKey(i + "");
                    listTransfer.add(transferNGNDTO);
                } else if (!DataUtil.isNullOrEmpty(listTransfer)) {
                    for (TransferNGNDTO dto : listTransfer) {
                        if (!DataUtil.isNullObject(dto)) {
                            if (dto.getIsdn() == DataUtil.safeToLong(isdn).longValue()) {
                                if (!IsdnConst.SUCCESS.equals(dto.getResult())) {
                                    hasFileError = true;
                                    Row errorRow = errorSheet.createRow(j);
                                    for (int k = 0; k < totalCol; k++) {
                                        errorRow.createCell(k).setCellValue(processingFile.getStringValue(row.getCell(k)));
                                    }
                                    errorRow.createCell(2).setCellValue(getResultDesc(dto.getResult()));
                                    j++;
//                                    listTransfer.remove(dto);
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
            topPage();
        }
        return -1;
    }

    public String getResultDesc(String key) {
        if (IsdnConst.NGN_WRONG_STATUS.equals(key)) {
            return "";
        }
        if (IsdnConst.NGN_INVALID_CODE.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.code.wrong");
        }
        if (IsdnConst.ISDN_NOT_EXIST.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.isdn.notexits");
        }
        if (IsdnConst.NGN_WRONG_SHOP.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.shop.wrong");
        }
        if (IsdnConst.SUCCESS.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.can.grant");
        }
        if (IsdnConst.GRANT_SUCCESS.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.grant.success");
        }
        if (IsdnConst.GRANT_FAIL.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.grant.fail");
        }
        if (IsdnConst.CODE_DUP.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.code.dup");
        }
        if (IsdnConst.NGN_AREA_CODE_EXPIRE.equals(key)) {
            return getText("mn.isdn.manage.code.NGN.areacode.expire");
        }
        return "xx";
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

    public boolean isHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public TransferNGNDTO getTransferDto() {
        return transferDto;
    }

    public void setTransferDto(TransferNGNDTO transferDto) {
        this.transferDto = transferDto;
    }

    public List<TransferNGNDTO> getListTransfer() {
        return listTransfer;
    }

    public void setListTransfer(List<TransferNGNDTO> listTransfer) {
        this.listTransfer = listTransfer;
    }

    public boolean isEnableTransfer() {
        return enableTransfer;
    }

    public void setEnableTransfer(boolean enableTransfer) {
        this.enableTransfer = enableTransfer;
    }

    public List<TransferNGNDTO> getListTransferAvail() {
        return listTransferAvail;
    }

    public void setListTransferAvail(List<TransferNGNDTO> listTransferAvail) {
        this.listTransferAvail = listTransferAvail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

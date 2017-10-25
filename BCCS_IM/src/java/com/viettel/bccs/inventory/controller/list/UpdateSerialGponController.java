package com.viettel.bccs.inventory.controller.list;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.UpdateSerialGponDTO;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockHandsetService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
import java.io.InputStream;
import java.util.List;

/**
 * Created by hoangnt14 on 6/28/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class UpdateSerialGponController extends TransCommonController {

    private String updateType;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private String fileName;
    private UploadedFile uploadedFile;
    List<UpdateSerialGponDTO> lstError = Lists.newArrayList();
    private static final String FILE_GPON_TEMPLATE_PATH = "updateSerialGpon.xls";
    private static final String FILE_MULTI_TEMPLATE_PATH = "updateSerialMulti.xls";
    private byte[] contentByte;
    private Long prodOfferingId;
    private List<UpdateSerialGponDTO> lstGpon = Lists.newArrayList();
    private int maxRow = 10000;
    private String update;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockHandsetService stockHandsetService;

    @PostConstruct
    public void init() {
        try {
            updateType = Const.UPDATE_SERIAL_GPON.TYPE_GPON;
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingForUpdateSerialGpon("", updateType);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void importData() {
        if (DataUtil.isNullObject(contentByte)) {
            reportError("", "", "mn.stock.status.isdn.update.file.noselect");
            focusElementByRawCSSSlector(".outputAttachFile");
            return;
        }
        try {
            lstGpon = readExcelToListGpon(contentByte);
            if (DataUtil.isNullOrEmpty(lstGpon)) {
                throw new LogicException("", "utilities.update.pincode.card.update.check.file.empty");
            }
            update = updateType;
            List<UpdateSerialGponDTO> lstResult = stockHandsetService.updateSerialGpon(updateType, lstGpon);
            int totalSuccess = 0;
            if (!DataUtil.isNullOrEmpty(lstResult)) {
                for (UpdateSerialGponDTO updateSerialGponDTO : lstResult) {
                    if (DataUtil.safeEqual(updateSerialGponDTO.getSuccess(), 1L)) {
                        totalSuccess++;
                    }
                    lstError.add(updateSerialGponDTO);
                }
            }
            reportSuccess("", "list.update.serial.gpon.update.result.success", totalSuccess, lstResult.size());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }
    }

    private List<UpdateSerialGponDTO> readExcelToListGpon(byte[] content) throws LogicException, Exception {
        lstError = Lists.newArrayList();
        List<UpdateSerialGponDTO> lstGpon = Lists.newArrayList();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getSheetAt(0);
        int totalRows = ex.getTotalRowAtSheet(sheet);
        if (totalRows > maxRow) {
            throw new LogicException("", "mn.stock.status.isdn.delete.maxline", maxRow);
        }
        for (int i = 1; i < totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            UpdateSerialGponDTO updateSerialGponDTO = new UpdateSerialGponDTO();
            if (DataUtil.safeEqual(updateType, Const.UPDATE_SERIAL_GPON.TYPE_GPON)) {
                updateSerialGponDTO.setSerial(ex.getStringValue(row.getCell(0)));
                updateSerialGponDTO.setStockGPon(ex.getStringValue(row.getCell(1)));
            } else {
                updateSerialGponDTO.setSerial(ex.getStringValue(row.getCell(0)));
                updateSerialGponDTO.setCadId(ex.getStringValue(row.getCell(1)));
                updateSerialGponDTO.setMacId(ex.getStringValue(row.getCell(2)));
            }
            updateSerialGponDTO.setProdOfferingId(prodOfferingId);
            lstGpon.add(updateSerialGponDTO);
        }
        return lstGpon;
    }

    @Secured("@")
    public void validateImport() {
        try {
            if (DataUtil.isNullObject(contentByte) || DataUtil.isNullObject(uploadedFile)) {
                focusElementError("outputAttachFile");
                throw new LogicException("", "utilities.update.pincode.card.file.require");
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void changeUpdateType() {
        //
        try {
            productOfferingTotalDTO = null;
            prodOfferingId = null;
            uploadedFile = null;
            fileName = null;
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingForUpdateSerialGpon("", updateType);
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doReset() {
        updateType = Const.UPDATE_SERIAL_GPON.TYPE_GPON;
        prodOfferingId = null;
        productOfferingTotalDTO = null;
        uploadedFile = null;
        fileName = null;
    }

    @Secured("@")
    public void downloadFileError() {
        try {
            if (!DataUtil.isNullOrEmpty(lstError)) {
                HSSFWorkbook workbook = getContentExportUpdateGpon(lstError, update);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "errorUpdate.xls" + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                workbook.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = null;
            String fileNameTemplate = "template";
            if (!DataUtil.isNullObject(updateType) && "1".equals(updateType)) {
                createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_GPON_TEMPLATE_PATH);
                fileNameTemplate = FILE_GPON_TEMPLATE_PATH;
            } else if (!DataUtil.isNullObject(updateType) && "2".equals(updateType)) {
                createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_MULTI_TEMPLATE_PATH);
                fileNameTemplate = FILE_MULTI_TEMPLATE_PATH;
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
            lstError = Lists.newArrayList();
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

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingForUpdateSerialGpon(inputData.toString(), updateType);
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        return lstProductOfferingTotalDTO;
    }

    @Secured("@")
    public void doSelectProductOffering() {
        try {
            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                prodOfferingId = productOfferingTotalDTO.getProductOfferingId();
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void resetLstProductOfferingTotal() {
        prodOfferingId = null;
        productOfferingTotalDTO = null;
        uploadedFile = null;
        fileName = null;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
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

    public List<UpdateSerialGponDTO> getLstError() {
        return lstError;
    }

    public void setLstError(List<UpdateSerialGponDTO> lstError) {
        this.lstError = lstError;
    }


}


package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.StockHandsetRepo;
import com.viettel.bccs.inventory.service.*;
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
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 6/30/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class UpdateLicensekeyController extends TransCommonController {
    private StaffDTO shopAnti;
    private UpdateLicenseDTO updateLicenseDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private String fileName;
    private UploadedFile uploadedFile;
    private byte[] contentByte;
    private List<OptionSetValueDTO> lstOptionSetValueDTOs;
    private List<OptionSetValueDTO> lstProductCodes;
    private boolean single;
    private static final String FILE_UPDATE_LICENSE_TEMPLATE_PATH = "updateLicenseKey.xls";
    private boolean checkFile;
    private List<UpdateLicenseDTO> lstLicenseDTOs = Lists.newArrayList();
    private List<String> lstCode = Lists.newArrayList();
    private boolean resultUpdate;


    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockHandsetService stockHandsetService;

    @PostConstruct
    public void init() {
        try {
            updateLicenseDTO = new UpdateLicenseDTO();
            lstOptionSetValueDTOs = optionSetValueService.getLsOptionSetValueByCode("ANTI_VIRUS_SHOP_CDBR");
            lstProductCodes = optionSetValueService.getLsOptionSetValueByCode("ANTI_VIRUS_STOCK_MODEL_CDBR");
            if (!DataUtil.isNullOrEmpty(lstProductCodes)) {
                for (OptionSetValueDTO optionSetValueDTO : lstProductCodes) {
                    lstCode.add(optionSetValueDTO.getValue());
                }
            }
            if (DataUtil.isNullOrEmpty(lstOptionSetValueDTOs)) {
                throw new LogicException("", "utilities.update.license.key.anti.virus.not.config.shop");
            }
            shopAnti = staffService.getStaffByStaffCode(lstOptionSetValueDTOs.get(0).getValue());
            doReset();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void downloadFileTemplate() {
        try {
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + FILE_UPDATE_LICENSE_TEMPLATE_PATH);
            String fileNameTemplate = FILE_UPDATE_LICENSE_TEMPLATE_PATH;
            if (!DataUtil.isNullObject(createStream)) {
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

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getLsProductOfferingByProductTypeAndCode(inputData.toString(), Const.PRODUCT_OFFER_TYPE.PHONE, lstCode);
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
                updateLicenseDTO.setProdOfferingId(productOfferingTotalDTO.getProductOfferingId());
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void importData() {
        try {
            int result;
            if (DataUtil.safeEqual(updateLicenseDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_SINGLE)) {
                stockHandsetService.updateLicenseKey(updateLicenseDTO);
                reportSuccess("", "utilities.update.license.key.anti.virus.update.success");
            } else {
                lstLicenseDTOs = readExcelToListPincode(contentByte);
                if (DataUtil.isNullOrEmpty(lstLicenseDTOs)) {
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.empty");
                }
                result = stockHandsetService.updateListLicenseKey(lstLicenseDTOs, updateLicenseDTO.getShopAntiId(),
                        updateLicenseDTO.getProdOfferingId(), updateLicenseDTO.getUpdateType());
                for (UpdateLicenseDTO updateLicenseDTO : lstLicenseDTOs) {
                    if (!DataUtil.isNullOrEmpty(updateLicenseDTO.getDesc())) {
                        updateLicenseDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.update.not.ok"));
                    } else {
                        updateLicenseDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.update.ok"));
                        updateLicenseDTO.setDesc("");
                    }
                }
                resultUpdate = true;
                reportSuccess("", "utilities.update.license.key.anti.virus.update.success.list", result, lstLicenseDTOs.size());
            }
            topPage();
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }

    }

    @Secured("@")
    public void validateImport() {
        try {
            if (DataUtil.safeEqual(updateLicenseDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_SINGLE)) {
                if (!DataUtil.checkNotSpecialCharacter(updateLicenseDTO.getSerial())) {
                    focusElementError("txtSerial");
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.not.macth.serial");
                }
                if (!DataUtil.checkNotSpecialCharacter(updateLicenseDTO.getLicenseKey())) {
                    focusElementError("txtPincode");
                    throw new LogicException("", "utilities.update.license.key.anti.virus.check.file.not.macth.license");
                }
            } else {
                if (DataUtil.isNullObject(contentByte) || DataUtil.isNullObject(uploadedFile)) {
                    focusElementError("outputAttachFile");
                    throw new LogicException("", "utilities.update.pincode.card.file.require");
                }
            }

        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doReset() {
        productOfferingTotalDTO = null;
        updateLicenseDTO.setShopAntiId(shopAnti.getStaffId());
        updateLicenseDTO.setDateImport(new Date());
        updateLicenseDTO.setUpdateType(Const.UPDATE_PINCODE_CARD.TYPE_NEW);
        updateLicenseDTO.setUpdateStyle(Const.UPDATE_PINCODE_CARD.STYLE_SINGLE);
        uploadedFile = null;
        fileName = null;
        single = true;
        checkFile = false;
        resultUpdate = false;
    }

    @Secured("@")
    public void previewFile() {
        try {
            if (DataUtil.isNullObject(contentByte) || DataUtil.isNullObject(uploadedFile)) {
                throw new LogicException("", "utilities.update.pincode.card.file.require");
            }
            lstLicenseDTOs = readExcelToListPincode(contentByte);
            if (DataUtil.isNullOrEmpty(lstLicenseDTOs)) {
                throw new LogicException("", "utilities.update.pincode.card.update.check.file.empty");
            }
            for (UpdateLicenseDTO updateLicenseDTO : lstLicenseDTOs) {
                if (!DataUtil.isNullOrEmpty(updateLicenseDTO.getDesc())) {
                    updateLicenseDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.not.ok"));
                } else {
                    updateLicenseDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.ok"));
                }
            }
            checkFile = true;
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }

    }

    @Secured("@")
    public void downloadFileError() {
        try {
            if (!DataUtil.isNullOrEmpty(lstLicenseDTOs)) {
                HSSFWorkbook workbook = getContentExportUpdateLicenseKey(lstLicenseDTOs);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "resultUpdate.xls" + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                workbook.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private List<UpdateLicenseDTO> readExcelToListPincode(byte[] content) throws LogicException, Exception {
        List<UpdateLicenseDTO> lstLicense = Lists.newArrayList();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getSheetAt(0);
        int totalRows = ex.getTotalRowAtSheet(sheet);
        boolean tempStatusRow;
        String stt;
        String serial;
        String license;
        for (int i = 1; i <= totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            tempStatusRow = true;
            stt = ex.getStringValue(row.getCell(0)).trim();
            serial = ex.getStringValue(row.getCell(1)).trim();
            license = ex.getStringValue(row.getCell(2)).trim();
            UpdateLicenseDTO updateLicenseTmpDTO = new UpdateLicenseDTO();
            updateLicenseTmpDTO.setStt(stt);
            updateLicenseTmpDTO.setSerial(serial);
            updateLicenseTmpDTO.setLicenseKey(license);
            if (!"".equals(serial)) {
                if (license.length() <= 20) {
                    if (DataUtil.checkNotSpecialCharacter(serial)) {
                        updateLicenseTmpDTO.setLicenseKey(serial);
                    } else {
                        updateLicenseTmpDTO.setLicenseKey(serial);
                        updateLicenseTmpDTO.setDesc(getText("utilities.update.license.update.check.file.too.long"));
                        tempStatusRow = false;
                    }
                }
            } else {
                updateLicenseTmpDTO.setDesc(getText("utilities.update.pincode.card.serial.require"));
                tempStatusRow = false;
            }
            if (tempStatusRow == true) {
                if (!"".equals(license)) {
                    if (license.length() <= 100) {
                        if (DataUtil.checkNotSpecialCharacter(license)) {
                            updateLicenseTmpDTO.setLicenseKey(license);
                        } else {
                            updateLicenseTmpDTO.setLicenseKey(license);
                            updateLicenseTmpDTO.setDesc(getText("utilities.update.license.update.check.file.not.macth.pincode"));
                            tempStatusRow = false;
                        }
                    } else {
                        updateLicenseTmpDTO.setLicenseKey(license);
                        updateLicenseTmpDTO.setDesc(getText("utilities.update.license.update.update.check.file.too.long.pincode"));
                        tempStatusRow = false;
                    }
                } else {
                    updateLicenseTmpDTO.setDesc(getText("utilities.update.license.key.anti.virus.require"));
                    tempStatusRow = false;
                }
            }
            if (tempStatusRow == true) {
                for (UpdateLicenseDTO temp : lstLicense) {
                    if (serial.equals(temp.getSerial())) {
                        updateLicenseTmpDTO.setDesc(MessageFormat.format(getText("utilities.update.pincode.card.update.check.file.exsit.serial"), serial));
                        tempStatusRow = false;
                        break;
                    }
                }
            }
            if (tempStatusRow == true) {
                try {
                    StockHandsetDTO stockHandsetDTO = stockHandsetService.checkExsitStockHandset(updateLicenseDTO.getProdOfferingId(), serial,
                            updateLicenseDTO.getShopAntiId(), Const.OWNER_TYPE.STAFF_LONG, Const.STATE_STATUS.NEW, updateLicenseDTO.getUpdateType());
                    if (DataUtil.isNullObject(stockHandsetDTO)) {
                        updateLicenseTmpDTO.setDesc(MessageFormat.format(getText("utilities.update.pincode.card.update.check.file.not.found.serial"), serial));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    updateLicenseTmpDTO.setDesc("Error check info:" + e.getMessage());
                }
            }
            lstLicense.add(updateLicenseTmpDTO);
        }
        return lstLicense;
    }

    @Secured("@")
    public void resetLstProductOfferingTotal() {
        updateLicenseDTO.setProdOfferingId(null);
        productOfferingTotalDTO = null;
    }

    @Secured("@")
    public void changeUpdateStyle() {
        if (DataUtil.safeEqual(updateLicenseDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_FILE_EXCEL)) {
            updateLicenseDTO.setSerial(null);
            updateLicenseDTO.setLicenseKey(null);
            single = false;
        } else {
            single = true;
            lstLicenseDTOs = null;
            uploadedFile = null;
            fileName = null;
            checkFile = false;
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
//            lstError = Lists.newArrayList();
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

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isCheckFile() {
        return checkFile;
    }

    public void setCheckFile(boolean checkFile) {
        this.checkFile = checkFile;
    }

    public List<UpdateLicenseDTO> getLstLicenseDTOs() {
        return lstLicenseDTOs;
    }

    public void setLstLicenseDTOs(List<UpdateLicenseDTO> lstLicenseDTOs) {
        this.lstLicenseDTOs = lstLicenseDTOs;
    }

    public UpdateLicenseDTO getUpdateLicenseDTO() {
        return updateLicenseDTO;
    }

    public void setUpdateLicenseDTO(UpdateLicenseDTO updateLicenseDTO) {
        this.updateLicenseDTO = updateLicenseDTO;
    }

    public StaffDTO getShopAnti() {
        return shopAnti;
    }

    public void setShopAnti(StaffDTO shopAnti) {
        this.shopAnti = shopAnti;
    }

    public boolean isResultUpdate() {
        return resultUpdate;
    }

    public void setResultUpdate(boolean resultUpdate) {
        this.resultUpdate = resultUpdate;
    }
}

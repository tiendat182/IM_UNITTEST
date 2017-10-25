package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockCardService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.payment.common.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
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
import java.io.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 6/30/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class UpdatePincodeCardController extends TransCommonController {
    private ShopDTO shopPincode;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private String fileName;
    private UploadedFile uploadedFile;
    private byte[] contentByte;
    private InputStream inputStream;
    private List<OptionSetValueDTO> lstOptionSetValueDTOs;
    private UpdatePincodeDTO updatePincodeDTO = new UpdatePincodeDTO();
    private boolean single;
    private static final String FILE_UPDATE_PINCODE_EXCEL_TEMPLATE_PATH = "updatePincodeCard.xls";
    private static final String FILE_UPDATE_PINCODE_TXT_TEMPLATE_PATH = "updatePincodeCard.txt";
    private boolean checkFile;
    private List<UpdatePincodeDTO> lstPincodes = Lists.newArrayList();
    private boolean resultUpdate;
    private int limitAutoComplete;
    private List<String> listError;
    public String newline = System.getProperty("line.separator");

    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private ShopInfoNameable shopInfoTag;

    @PostConstruct
    public void init() {
        try {
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            updatePincodeDTO = new UpdatePincodeDTO();
            String shopPincode = optionSetValueService.getValueByTwoCodeOption(Const.SHOP_PINCODE.SHOP_PINCODE, Const.SHOP_PINCODE.SHOP_PINCODE);
            if (DataUtil.isNullOrEmpty(shopPincode)) {
                throw new LogicException("", "utilities.update.pincode.card.not.config.shop");
            }
            List<String> lstShopCode = Lists.newArrayList(shopPincode.split(","));
            shopInfoTag.initShopByListShopCode(this, Const.MODE_SHOP.LIST_SHOP_CODE, lstShopCode);
            doReset();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void downloadFileError() {
        try {
            if (!DataUtil.isNullOrEmpty(lstPincodes)) {
                HSSFWorkbook workbook = getContentExportUpdatePincodeCard(lstPincodes);
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



    @Secured("@")
    public void downloadFileTemplate() {
        try {
            String fileNameTemplate = FILE_UPDATE_PINCODE_EXCEL_TEMPLATE_PATH;
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + fileNameTemplate);
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

    @Secured("@")
    public StreamedContent downloadFileText() {
        String fileNameTemplate = FILE_UPDATE_PINCODE_TXT_TEMPLATE_PATH;
        InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + fileNameTemplate);
        return new DefaultStreamedContent(createStream, FacesContext.getCurrentInstance().getExternalContext().getMimeType(fileNameTemplate), fileNameTemplate);
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), Const.PRODUCT_OFFER_TYPE.CARD);
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
                updatePincodeDTO.setProdOfferingId(productOfferingTotalDTO.getProductOfferingId());
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
            if (DataUtil.safeEqual(updatePincodeDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_SINGLE)) {
                if (!stockCardService.checkInfoStockCard(updatePincodeDTO.getPincode(), updatePincodeDTO.getShopPincodeId(), updatePincodeDTO.getSerial(), updatePincodeDTO.getProdOfferingId(),
                        updatePincodeDTO.getUpdateType(), DataUtil.safeToLong(Const.STATUS_ACTIVE))) {
                    reportError("", "", "utilities.update.pincode.card.update.check.file.not.found.serial", updatePincodeDTO.getSerial());
                    return;
                }
                stockCardService.updatePincodeForStockCard(updatePincodeDTO.getShopPincodeId(), updatePincodeDTO.getSerial(),
                        updatePincodeDTO.getProdOfferingId(), updatePincodeDTO.getPincode(), updatePincodeDTO.getUpdateType(), getStaffDTO());
                reportSuccess("", "utilities.update.pincode.card.update.success");

            } else {
                if (DataUtil.safeEqual(updatePincodeDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_FILE_EXCEL)) {
                    lstPincodes = readExcelToListPincode();
                } else {
                    lstPincodes = readTextToListPincode();
                }
                if (DataUtil.isNullOrEmpty(lstPincodes)) {
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.empty");
                }
                stockCardService.updateListPincodeForStockCard(lstPincodes, updatePincodeDTO.getShopPincodeId(),
                        updatePincodeDTO.getProdOfferingId(), updatePincodeDTO.getUpdateType(), getStaffDTO());
                for (UpdatePincodeDTO updatePincodeDTO : lstPincodes) {
                    if (!DataUtil.isNullOrEmpty(updatePincodeDTO.getDesc())) {
                        updatePincodeDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.update.not.ok"));
                    } else {
                        updatePincodeDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.update.ok"));
                        updatePincodeDTO.setDesc("");
                    }
                }
                resultUpdate = true;
//                reportSuccess("", "utilities.update.pincode.card.update.success.list", result, lstPincodes.size());
                reportSuccess("", "utilities.update.pincode.card.update.success");
            }
            topPage();
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void validateImport() {
        try {
            if (DataUtil.safeEqual(updatePincodeDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_SINGLE)) {
                if (!DataUtil.checkDigit(updatePincodeDTO.getSerial())) {
                    focusElementError("txtSerial");
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.not.macth.serial");
                }
                if (updatePincodeDTO.getSerial().length() > 13) {
                    focusElementError("txtSerial");
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.serial.too.long");
                }
                if (!DataUtil.checkNotSpecialCharacter(updatePincodeDTO.getPincode())) {
                    focusElementError("txtPincode");
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.not.macth.pincode");
                }
                if (updatePincodeDTO.getPincode().length() > 15) {
                    focusElementError("txtPincode");
                    throw new LogicException("", "utilities.update.pincode.card.update.check.file.too.long.pincode");
                }
            } else {
                if (DataUtil.isNullObject(contentByte) && DataUtil.isNullObject(uploadedFile)) {
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
        updatePincodeDTO.setShopPincodeId(null);
        updatePincodeDTO.setDateImport(new Date());
        updatePincodeDTO.setUpdateType(Const.UPDATE_PINCODE_CARD.TYPE_NEW);
        updatePincodeDTO.setUpdateStyle(Const.UPDATE_PINCODE_CARD.STYLE_SINGLE);
        uploadedFile = null;
        fileName = null;
        single = true;
        checkFile = false;
        resultUpdate = false;
        inputStream = null;
    }

    @Secured("@")
    public void previewFile() {
        try {
            if (DataUtil.isNullObject(contentByte) && DataUtil.isNullObject(uploadedFile)) {
                throw new LogicException("", "utilities.update.pincode.card.file.require");
            }
            if (DataUtil.safeEqual(updatePincodeDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_FILE_EXCEL)) {
                lstPincodes = readExcelToListPincode();
            } else {
                lstPincodes = readTextToListPincode();
            }
            if (DataUtil.isNullOrEmpty(lstPincodes)) {
                throw new LogicException("", "utilities.update.pincode.card.update.check.file.empty");
            }
            for (UpdatePincodeDTO updatePincodeDTO : lstPincodes) {
                if (!DataUtil.isNullOrEmpty(updatePincodeDTO.getDesc())) {
                    updatePincodeDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.not.ok"));
                } else {
                    updatePincodeDTO.setStatus(getText("utilities.update.pincode.card.update.check.file.status.ok"));
                }
            }
            checkFile = true;
            resultUpdate = true;
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }

    }

    private List<UpdatePincodeDTO> readTextToListPincode() throws LogicException, Exception {
        List<UpdatePincodeDTO> lstPincode = Lists.newArrayList();
        List<String> readerData = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(contentByte)));
        String lines;
        String serial = "";
        String pincode = "";
        //Bo qua dong dau tien
        reader.readLine();
        while ((lines = reader.readLine()) != null) {
            if (DataUtil.isNullOrEmpty(lines)) {
                continue;
            }
            if (DataUtil.safeEqual(lines.length(), Const.UPDATE_PINCODE_CARD.LENGTH_SERIAL_11)) {
                serial = lines.substring(0, 11);
                pincode = lines.substring(11);
            } else if (DataUtil.safeEqual(lines.length(), Const.UPDATE_PINCODE_CARD.LENGTH_SERIAL_13)) {
                serial = lines.substring(0, 13);
                pincode = lines.substring(13);
            } else {
                serial = lines;
            }
            UpdatePincodeDTO updatePincodeTmpDTO = validateUpdatePincode(serial, pincode, lstPincode);
            lstPincode.add(updatePincodeTmpDTO);
            if (!DataUtil.isNullOrEmpty(updatePincodeTmpDTO.getDesc())) {
                lines += Const.AppProperties4Sms.profileSparator.getValue().toString() + " ";
                lines += updatePincodeTmpDTO.getDesc();
            } else {
                lines += Const.AppProperties4Sms.profileSparator.getValue().toString() + " ";
                lines += getMessage("utilities.update.pincode.card.update.check.file.status.update.ok");
            }

            readerData.add(lines);
        }
        listError = Lists.newArrayList(readerData);
        return lstPincode;
    }


    private List<UpdatePincodeDTO> readExcelToListPincode() throws LogicException, Exception {
        List<UpdatePincodeDTO> lstPincode = Lists.newArrayList();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getSheetAt(0);
        int totalRows = ex.getTotalRowAtSheet(sheet);
        String serial;
        String pincode;
        for (int i = 1; i <= totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            serial = ex.getStringValue(row.getCell(1)).trim();
            pincode = ex.getStringValue(row.getCell(2)).trim();
            UpdatePincodeDTO updatePincodeTmpDTO = validateUpdatePincode(serial, pincode, lstPincode);
            lstPincode.add(updatePincodeTmpDTO);
        }
        return lstPincode;
    }

    public UpdatePincodeDTO validateUpdatePincode(String serial, String pincode, List<UpdatePincodeDTO> lstPincode) {
        UpdatePincodeDTO updatePincodeTmpDTO = new UpdatePincodeDTO();
        int length = serial.length() + pincode.length();
        if (!DataUtil.safeEqual(length, Const.UPDATE_PINCODE_CARD.LENGTH_SERIAL_11)
                && !DataUtil.safeEqual(length, Const.UPDATE_PINCODE_CARD.LENGTH_SERIAL_13)) {
            updatePincodeTmpDTO.setDesc(getText("im.update.pincode.txt.length.invalid"));
            return updatePincodeTmpDTO;
        }
        updatePincodeTmpDTO.setSerial(serial);
        updatePincodeTmpDTO.setPincode(pincode);
        if (DataUtil.isNullOrEmpty(serial)) {
            updatePincodeTmpDTO.setDesc(getText("utilities.update.pincode.card.serial.require"));
            return updatePincodeTmpDTO;
        }
        updatePincodeTmpDTO.setSerial(serial);
        if (DataUtil.isNullOrEmpty(pincode)) {
            updatePincodeTmpDTO.setDesc(getText("utilities.update.pincode.card.pincode.require"));
            return updatePincodeTmpDTO;
        }
        updatePincodeTmpDTO.setPincode(pincode);
        if (serial.length() > 13) {
            updatePincodeTmpDTO.setDesc(getText("utilities.update.pincode.card.update.check.file.serial.too.long"));
            return updatePincodeTmpDTO;
        }
        if (!DataUtil.checkDigit(serial)) {
            updatePincodeTmpDTO.setDesc(getText("utilities.update.pincode.card.update.check.file.not.macth.serial"));
            return updatePincodeTmpDTO;
        }
        if (pincode.length() > 15) {
            updatePincodeTmpDTO.setDesc(getText("utilities.update.pincode.card.update.check.file.too.long.pincode"));
            return updatePincodeTmpDTO;
        }
        if (!DataUtil.checkNotSpecialCharacter(pincode)) {
            updatePincodeTmpDTO.setDesc(getText("utilities.update.pincode.card.update.check.file.not.macth.pincode"));
            return updatePincodeTmpDTO;
        }

        for (UpdatePincodeDTO temp : lstPincode) {
            if (serial.equals(temp.getSerial())) {
                updatePincodeTmpDTO.setDesc(MessageFormat.format(getText("utilities.update.pincode.card.update.check.file.exsit.serial"), serial));
                return updatePincodeTmpDTO;
            }
        }

//            if (tempStatusRow == true) {
//                try {
//                    if (!stockCardService.checkInfoStockCard(pincode, updatePincodeDTO.getShopPincodeId(), serial, updatePincodeDTO.getProdOfferingId(),
//                            updatePincodeDTO.getUpdateType(), DataUtil.safeToLong(Const.STATUS_ACTIVE))) {
//                        updatePincodeTmpDTO.setDesc(MessageFormat.format(getText("utilities.update.pincode.card.update.check.file.not.found.serial"), serial));
//                    }
//                } catch (Exception e) {
//                    logger.error(e.getMessage(), e);
//                    updatePincodeTmpDTO.setDesc("Error check info:" + e.getMessage());
//                }
//            }
        return updatePincodeTmpDTO;
    }

    @Secured("@")
    public void resetLstProductOfferingTotal() {
        updatePincodeDTO.setProdOfferingId(null);
        productOfferingTotalDTO = null;
        doReset();
    }

    @Secured("@")
    public void changeUpdateStyle() {
        lstPincodes = null;
        uploadedFile = null;
        fileName = null;
        checkFile = false;
        inputStream = null;
        updatePincodeDTO.setSerial(null);
        updatePincodeDTO.setPincode(null);
        if (DataUtil.safeEqual(updatePincodeDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_SINGLE)) {
            single = true;
        } else {
            single = false;
        }
    }


    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            BaseMessage message;
            uploadedFile = event.getFile();
            if (DataUtil.safeEqual(updatePincodeDTO.getUpdateStyle(), Const.UPDATE_PINCODE_CARD.STYLE_FILE_EXCEL)) {
                message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            } else {
                message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_TEXT_TYPE_LIST, MAX_SIZE_2M);
            }
            if (!message.isSuccess()) {
                fileName = null;
                contentByte = null;
                inputStream = null;
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

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        updatePincodeDTO.setShopPincodeId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void clearShop() {
        doReset();
    }

    public List<UpdatePincodeDTO> getLstPincodes() {
        return lstPincodes;
    }

    public void setLstPincodes(List<UpdatePincodeDTO> lstPincodes) {
        this.lstPincodes = lstPincodes;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public UpdatePincodeDTO getUpdatePincodeDTO() {
        return updatePincodeDTO;
    }

    public void setUpdatePincodeDTO(UpdatePincodeDTO updatePincodeDTO) {
        this.updatePincodeDTO = updatePincodeDTO;
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


    public ShopDTO getShopPincode() {
        return shopPincode;
    }

    public void setShopPincode(ShopDTO shopPincode) {
        this.shopPincode = shopPincode;
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

    public boolean isCheckFile() {
        return checkFile;
    }

    public void setCheckFile(boolean checkFile) {
        this.checkFile = checkFile;
    }

    public boolean isResultUpdate() {
        return resultUpdate;
    }

    public void setResultUpdate(boolean resultUpdate) {
        this.resultUpdate = resultUpdate;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }
}

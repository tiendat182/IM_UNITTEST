package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.annotation.Security;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anonymus on 07/04/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ChangeDamagedProductController extends InventoryController {
    private List<Long> productOfferTypeChange = Lists.newArrayList();
    private boolean singleType = true;
    private String productOfferTypeId;
    private ChangeDamagedProductDTO changeDamagedProductDTO = new ChangeDamagedProductDTO();
    private int limitAutoComplete;
    private List<ProductOfferTypeDTO> listProductOfferType = Lists.newArrayList();
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    private String attachFileName = "";
    private List<ErrorChangeProductDTO> listSerialFileError = Lists.newArrayList();
    private List<ChangeDamagedProductDTO> listSerialFile = Lists.newArrayList();
    private boolean showReason = false;
    private List<ReasonDTO> listReason = Lists.newArrayList();

    private UploadedFile fileUpload;
    private byte[] byteContent;
    private ExcellUtil processingFile;
    private boolean errorImport;

    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ChangeDamagedProductService changeDamagedProductService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    private boolean directLink;

    @Security("@")
    @PostConstruct
    public void init() {
        try {
            helperSystem();
            resetData();
            List<OptionSetValueDTO> optionSetValueDTOs = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CHANGE_DAMAGE_PRODUCT_OFFER_TYPE);
            for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOs) {
                productOfferTypeChange.add(DataUtil.safeToLong(optionSetValueDTO.getValue()));
            }
            listProductOfferType = productOfferTypeService.getListProductOfferTypeById(productOfferTypeChange);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            List<OptionSetValueDTO> lstOptionSetValueDTO = optionSetValueService.getLsOptionSetValueByCode(Const.OPTION_SET.CARD_CHANGE_REASON_GROUP);
            if (!DataUtil.isNullOrEmpty(lstOptionSetValueDTO) && !DataUtil.isNullObject(lstOptionSetValueDTO.get(0))) {
                listReason = reasonService.getLsReasonByType(lstOptionSetValueDTO.get(0).getValue());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
        }

    }

    private void helperSystem(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String sys = request.getParameter(Const.SYSTEM_SALE);
        directLink = DataUtil.safeEqual(sys,Const.SYSTEM_SALE);
    }
    // Reset du lieu ban dau.
    private void resetData() {
        clearData();
        singleType = true;
        changeDamagedProductDTO = new ChangeDamagedProductDTO();
        changeDamagedProductDTO.setProdOfferId(null);
        changeDamagedProductDTO.setTypeImport(Const.ChangeDamagedProduct.SINGLE_TYPE.getValue());
    }

    private void clearData() {
        listSerialFileError = Lists.newArrayList();
        listSerialFile = Lists.newArrayList();
        showReason = false;
        attachFileName = null;
        productOfferTypeId = null;
        changeDamagedProductDTO.setProductOfferTypeId(null);
        changeDamagedProductDTO.setProdOfferId(null);
        changeDamagedProductDTO.setReasonId(null);
        changeDamagedProductDTO.setOldSerial(null);
        changeDamagedProductDTO.setNewSerial(null);
        changeDamagedProductDTO.setProductCode(null);
        changeDamagedProductDTO.setProductName(null);
        productOfferingTotalDTO = null;
        clearFileUpload();
        attachFileName = null;
        errorImport = false;
    }

    @Secured("@")
    public void doChangeDamagedProduct() {
        try {
            List<ChangeDamagedProductDTO> lstChangeProduct = Lists.newArrayList();
            changeDamagedProductDTO.setIpAddress(BccsLoginSuccessHandler.getIpAddress());
            lstChangeProduct.add(changeDamagedProductDTO);
            BaseMessage baseMessage = changeDamagedProductService.saveChangeDamagedProduct(changeDamagedProductDTO.getProductOfferTypeId(),
                    changeDamagedProductDTO.getProdOfferId(), changeDamagedProductDTO.getReasonId(),
                    lstChangeProduct, BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            if (!DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg(), baseMessage.getParamsMsg());
            }
            reportSuccess("", "stock.change.damaged.message.success");
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doInputNewSerial() {
        try {
            showReason = false;
            if (DataUtil.isNullOrEmpty(changeDamagedProductDTO.getNewSerial())) {
                return;
            }
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            List<ChangeDamagedProductDTO> lstSerial = changeDamagedProductService.checkNewSerialInput(changeDamagedProductDTO.getNewSerial(), staffDTO.getStaffId());
            if (!DataUtil.isNullOrEmpty(lstSerial)) {
                List<Long> productOfferTypeList = Lists.newArrayList();
                List<Long> prodOfferIdList = Lists.newArrayList();
                for (ChangeDamagedProductDTO changeProduct : lstSerial) {
                    productOfferTypeList.add(changeProduct.getProductOfferTypeId());
                    prodOfferIdList.add(changeProduct.getProdOfferId());
                }
                listProductOfferType = productOfferTypeService.getListProductOfferTypeById(productOfferTypeList);
                lsProductOfferingTotalDTO = productOfferingService.getProductOfferingByListId(prodOfferIdList);
                if (!DataUtil.isNullOrEmpty(listProductOfferType) && !DataUtil.isNullOrEmpty(lsProductOfferingTotalDTO)) {
                    productOfferingTotalDTO = lsProductOfferingTotalDTO.get(0);
                    changeDamagedProductDTO.setProductOfferTypeId(productOfferingTotalDTO.getProductOfferTypeId());
                    changeDamagedProductDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
                    if (DataUtil.safeEqual(productOfferingTotalDTO.getProductOfferTypeId(), Const.STOCK_TYPE.STOCK_CARD)) {
                        showReason = true;
                    }
                } else {
                    clearInputNewSerial();
                }
            } else {
                clearInputNewSerial();
            }
        } catch (LogicException ex) {
            clearInputNewSerial();
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    private void clearInputNewSerial() {
        try {
            productOfferingTotalDTO = null;
            changeDamagedProductDTO.setProductOfferTypeId(null);
            changeDamagedProductDTO.setProdOfferId(null);
            lsProductOfferingTotalDTO = Lists.newArrayList();
            showReason = false;
            listProductOfferType = productOfferTypeService.getListProductOfferTypeById(productOfferTypeChange);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }


    @Secured("@")
    public void doValidateChangeDamagedProduct() {
        try {
            if (DataUtil.isNullObject(changeDamagedProductDTO) || DataUtil.isNullObject(changeDamagedProductDTO.getProductOfferTypeId())) {
                throw new LogicException("", "export.order.type.product.not.blank");
            }
            if (DataUtil.isNullObject(changeDamagedProductDTO) || DataUtil.isNullObject(changeDamagedProductDTO.getProdOfferId())) {
                throw new LogicException("", "export.order.type.product.not.blank");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doChangeDamagedProductFile() {
        try {
            listSerialFileError = changeDamagedProductService.saveChangeDamagedProductFile(changeDamagedProductDTO.getProductOfferTypeId(),
                    changeDamagedProductDTO.getProdOfferId(), changeDamagedProductDTO.getReasonId(),
                    listSerialFile, BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            if (!DataUtil.isNullOrEmpty(listSerialFileError)) {
                RequestContext.getCurrentInstance().execute("clickLinkByClass('downloadError')");
                throw new LogicException("", "stock.change.damaged.message.fail");
            } else {
                reportSuccess("", "stock.change.damaged.message.success");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public boolean getShowErrorFile() {
        return !DataUtil.isNullOrEmpty(listSerialFileError);
    }

    @Secured("@")
    public boolean getImportFileSucess() {
        return !DataUtil.isNullOrEmpty(listSerialFile) || !DataUtil.isNullOrEmpty(listSerialFileError);
    }

    @Secured("@")
    public void doValidateChangeDamagedProductFile() {
        try {
            errorImport = false;
            listSerialFileError = Lists.newArrayList();
            listSerialFile = Lists.newArrayList();
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            validateFileImport(processingFile);
            if (DataUtil.isNullOrEmpty(listSerialFile)) {
                throw new LogicException("", "mn.stock.limit.msg.invalid.data");
            }
            if (!DataUtil.isNullOrEmpty(listSerialFileError)) {
                throw new LogicException("", "mn.stock.limit.msg.invalid.data");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void changeTypeImport() {
        try {
            listProductOfferType = productOfferTypeService.getListProductOfferTypeById(productOfferTypeChange);
            if (DataUtil.safeEqual(changeDamagedProductDTO.getTypeImport(), Const.ChangeDamagedProduct.SINGLE_TYPE.getValue())) {
                singleType = true;
            } else {
                singleType = false;
            }
            clearData();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void onChangeProductOfferType() {
        try {
            showReason = false;
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            if (DataUtil.isNullOrEmpty(productOfferTypeId)) {
                lsProductOfferingTotalDTO = Lists.newArrayList();
            } else {
                if (DataUtil.safeEqual(DataUtil.safeToLong(productOfferTypeId), Const.STOCK_TYPE.STOCK_CARD)) {
                    showReason = true;
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            if (DataUtil.isNullOrEmpty(productOfferTypeId)) {
                return new ArrayList<ProductOfferingTotalDTO>();
            }
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            List<ProductOfferingTotalDTO> test = DataUtil.defaultIfNull(productOfferingService.getListProductOfferingByProductType(input, DataUtil.safeToLong(productOfferTypeId)), new ArrayList<ProductOfferingTotalDTO>());
            this.lsProductOfferingTotalDTO = test;
            return test;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    @Secured("@")
    public void doSelectProductOffering() {
        try {
            if (DataUtil.isNullObject(productOfferingTotalDTO)) {
                changeDamagedProductDTO.setProdOfferId(null);
                reportError("", "", "stock.inspect.not.choose.product.offer");
            } else {
                changeDamagedProductDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void clearProduct() {
        this.productOfferingTotalDTO = null;
        changeDamagedProductDTO.setProdOfferId(null);
    }

    @Secured("@")
    public boolean disabledState() {
        return DataUtil.isNullObject(productOfferingTotalDTO) ? false : (DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId()) ? false : true);
    }

    @Secured("@")
    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            attachFileName = null;
            listSerialFileError = Lists.newArrayList();
            listSerialFile = Lists.newArrayList();
            errorImport = false;
            fileUpload = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(fileUpload, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = fileUpload.getContents();
            if (byteContent != null && (byteContent.length == 0 || byteContent.length > 2 * 1024 * 1024)) {
                clearFileUpload();
                reportErrorValidateFail("", "", "mn.stock.status.isdn.delete.valid.file.empty");
                return;
            }
            attachFileName = new String(fileUpload.getFileName().getBytes(), "UTF-8");
            processingFile = new ExcellUtil(fileUpload, byteContent);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doUploadFile() {
        try {
            errorImport = false;
            listSerialFileError = Lists.newArrayList();
            listSerialFile = Lists.newArrayList();
            validateFileImport(processingFile);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        //xu ly clear thong tin file upload
        clearFileUpload();
    }

    @Secured("@")
    public StreamedContent getAddTemplateFileByProd() {
        InputStream addStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH
                + Const.FILE_TEMPLATE.IMPORT_CHANGE_GOODS_TEMPLATE);
        return new DefaultStreamedContent(addStream, "application/xls", Const.FILE_TEMPLATE.IMPORT_CHANGE_GOODS_TEMPLATE);
    }

    @Secured("@")
    public StreamedContent exportImportListErrorFile() {
        try {
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setOutName(Const.FILE_TEMPLATE.TEMPLATE_ERROR_IMPORT_CHANGE_GOODS + "_" + staffDTO.getStaffCode() + "_" +
                    DateUtil.date2ddMMyyyyHHMMssNoSlash(new Date()) + Const.BRAS_IPPOOL.XLS);
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setTemplateName(Const.FILE_TEMPLATE.TEMPLATE_ERROR_IMPORT_CHANGE_GOODS_FILE);
            bean.putValue("listReport", listSerialFileError);
            return FileUtil.exportToStreamed(bean);
//            FileUtil.exportFile(bean);
//            return bean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    private void clearFileUpload() {
        processingFile = null;
        fileUpload = null;
        byteContent = null;
    }

    private void validateFileImport(ExcellUtil processingFile) throws LogicException, Exception {
        errorImport = false;
        if (processingFile == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.status.isdn.delete.valid.file.size");
        }
        Sheet sheetProcess = processingFile.getSheetAt(0);
        Row test = sheetProcess.getRow(1);
        if (test == null || processingFile.getTotalColumnAtRow(test) != 2) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "list.product.validate.fileError");
        }
        int totalRow = 0;
        List<String> oldSerialList = Lists.newArrayList();
        List<String> newSerialList = Lists.newArrayList();
        for (Row row : sheetProcess) {
            totalRow++;
            if (totalRow <= 1) {
                continue;
            }
            if (totalRow > 50000) {
                clearFileUpload();
                listSerialFile = Lists.newArrayList();
                listSerialFileError = Lists.newArrayList();
                throw new LogicException("", "stock.validate.stock.file.max.import");
            }
            ChangeDamagedProductDTO changeDamagedProduct = new ChangeDamagedProductDTO();
            changeDamagedProduct.setReasonId(changeDamagedProductDTO.getReasonId());
            changeDamagedProduct.setTypeImport(Const.ChangeDamagedProduct.FILE_TYPE.getValue());
            String oldSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(0))).trim();
            String newSerial = DataUtil.safeToString(processingFile.getStringValue(row.getCell(1))).trim();
            changeDamagedProduct.setOldSerial(oldSerial);
            changeDamagedProduct.setNewSerial(newSerial);
            ErrorChangeProductDTO errorChangeProductDTO = new ErrorChangeProductDTO();
            errorChangeProductDTO.setOldNumber(oldSerial);
            errorChangeProductDTO.setNewNumber(newSerial);
            if (DataUtil.isNullOrEmpty(oldSerial) && DataUtil.isNullOrEmpty(newSerial)) {
                continue;
            } else if (DataUtil.isNullOrEmpty(oldSerial) || DataUtil.isNullOrEmpty(newSerial)) {
                errorChangeProductDTO.setError(getText("input.list.product.file.content.empty"));
                listSerialFileError.add(errorChangeProductDTO);
                errorImport = true;
                continue;
            }
            if (oldSerialList.contains(oldSerial)) {
                errorChangeProductDTO.setError(getText("stock.validate.stock.file.oldSerial.duplicate"));
                listSerialFileError.add(errorChangeProductDTO);
                errorImport = true;
                continue;
            }
            if (newSerialList.contains(newSerial)) {
                errorChangeProductDTO.setError(getText("stock.validate.stock.file.newSerial.duplicate"));
                listSerialFileError.add(errorChangeProductDTO);
                errorImport = true;
                continue;
            }
            oldSerialList.add(oldSerial);
            newSerialList.add(newSerial);
            changeDamagedProduct.setIpAddress(BccsLoginSuccessHandler.getIpAddress());
            listSerialFile.add(changeDamagedProduct);
        }
        if (errorImport) {
            clearFileUpload();
            int totalFaile = listSerialFileError.size();
            listSerialFile = Lists.newArrayList();
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.stock.file.msg.errRead", totalFaile + "/" + (totalRow - 1));
        } else {
            reportSuccess("", "stock.change.damaged.stock.success", listSerialFile.size());
        }

    }

    public boolean isSingleType() {
        return singleType;
    }

    public void setSingleType(boolean singleType) {
        this.singleType = singleType;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public List<ProductOfferTypeDTO> getListProductOfferType() {
        return listProductOfferType;
    }

    public void setListProductOfferType(List<ProductOfferTypeDTO> listProductOfferType) {
        this.listProductOfferType = listProductOfferType;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public ChangeDamagedProductDTO getChangeDamagedProductDTO() {
        return changeDamagedProductDTO;
    }

    public void setChangeDamagedProductDTO(ChangeDamagedProductDTO changeDamagedProductDTO) {
        this.changeDamagedProductDTO = changeDamagedProductDTO;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public List<ErrorChangeProductDTO> getListSerialFileError() {
        return listSerialFileError;
    }

    public void setListSerialFileError(List<ErrorChangeProductDTO> listSerialFileError) {
        this.listSerialFileError = listSerialFileError;
    }

    public boolean isShowReason() {
        return showReason;
    }

    public void setShowReason(boolean showReason) {
        this.showReason = showReason;
    }

    public List<ReasonDTO> getListReason() {
        return listReason;
    }

    public void setListReason(List<ReasonDTO> listReason) {
        this.listReason = listReason;
    }

    public List<ChangeDamagedProductDTO> getListSerialFile() {
        return listSerialFile;
    }

    public void setListSerialFile(List<ChangeDamagedProductDTO> listSerialFile) {
        this.listSerialFile = listSerialFile;
    }

    public UploadedFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public ExcellUtil getProcessingFile() {
        return processingFile;
    }

    public void setProcessingFile(ExcellUtil processingFile) {
        this.processingFile = processingFile;
    }

    public boolean isErrorImport() {
        return errorImport;
    }

    public void setErrorImport(boolean errorImport) {
        this.errorImport = errorImport;
    }

    public boolean isDirectLink() {
        return directLink;
    }

    public void setDirectLink(boolean directLink) {
        this.directLink = directLink;
    }
}

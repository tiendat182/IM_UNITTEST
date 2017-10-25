package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by anhnv33 on 01/20/2015.
 */
@Component
@Scope("view")
@ManagedBean(name = "createFieldExportIsdnByFileController")
public class CreateFieldExportIsdnByFileController extends InventoryController {

    private String attachFileName;
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private List<FieldExportFileDTO> fieldExportFileDTOs;
    private List<FieldExportFileDTO> lstErrFieldExportIsdn;
    private ExcellUtil processingFile;
    private boolean selectedFile;
    private boolean previewOrder;
    private boolean hasFileError;
    private StaffDTO staffDTO;
    private List<ShopDTO> lstShop;

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat

    @PostConstruct
    @Secured("@")
    public void init() {
        try {
            fieldExportFileDTOs = Lists.newArrayList();
            lstErrFieldExportIsdn = Lists.newArrayList();
            orderStockTag.init(this, false);
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            shopInfoTag = orderStockTag.getShopInfoExportTagIsdn();
            shopInfoTag.initShopForIsdn(orderStockTag, DataUtil.safeToString(staffDTO.getShopId()), true, null);
            attachFileName = "";
            selectedFile = false;
            previewOrder = false;
            hasFileError = false;
        } catch (Exception ex) {
            logger.error(ex);
            reportErrorValidateFail("frmExportNote:msgs", "", "common.error.happened");
        }
    }


    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                attachFileName = null;
                uploadedFile = null;
                byteContent = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            byteContent = uploadedFile.getContents();
            attachFileName = uploadedFile.getFileName();

            selectedFile = true;
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
            InputStream createStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.CREATE_EXP_CMD_ISDN_FROM_FILE_PATTERN_TEMPLATE);
            HSSFWorkbook workbook = new HSSFWorkbook(createStream);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "createExpCmdIsdnFromFilePattern.xls" + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void previewField() {
        try {
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                attachFileName = null;
                uploadedFile = null;
                byteContent = null;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            validateFileUpload();
            if(!DataUtil.isNullOrEmpty(lstErrFieldExportIsdn)){
                hasFileError = true;
            }
            if(DataUtil.isNullOrEmpty(fieldExportFileDTOs)){
                reportErrorValidateFail("frmCreateField:msgs", "", "mn.isdn.manage.create.field.err.noOrder");
                return;
            }
            previewOrder = true;
            reportSuccess("frmCreateField:msgs","mn.isdn.manage.create.field.result",
                    DataUtil.safeToString(fieldExportFileDTOs.size()), DataUtil.safeToString(fieldExportFileDTOs.size() + lstErrFieldExportIsdn.size()));
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
    public void createField(){
        try {
            //validate
//            if(DataUtil.isNullOrEmpty(fieldExportIsdnDTOs)){
//                reportErrorValidateFail("frmCreateField:msgs", "", "mn.isdn.manage.create.field.err.noOrder");
//                return;
//            }
//            List<String> lstActionCode = Lists.newArrayList();
//            for(FieldExportIsdnDTO dto : fieldExportIsdnDTOs) {
//                lstActionCode.add(dto.getActionCode());
//            }
            HashMap<String, List<FieldExportFileDTO>> lstFiel = new HashMap<>();
            for(FieldExportFileDTO dto : fieldExportFileDTOs) {
                String key = dto.getActionCode() + "|" + dto.getFromOwnerCode() + "|" + dto.getToOwnerCode();
                if(lstFiel.containsKey(key)){
                    List<FieldExportFileDTO> value = lstFiel.get(key);
                    value.add(dto);
                    lstFiel.put(key, value);
                } else {
                    List<FieldExportFileDTO> value = Lists.newArrayList();
                    value.add(dto);
                    lstFiel.put(key, value);
                }
            }

            for(String key : lstFiel.keySet()){
                List<FieldExportFileDTO> lstDetail = lstFiel.get(key);
                FieldExportFileDTO firstElement = lstDetail.get(0);
                StockTransDTO stockTransDTO = new StockTransDTO();
                stockTransDTO.setFromOwnerType(Const.STOCK_TYPE.STORE);
                stockTransDTO.setFromOwnerId(DataUtil.safeToLong(firstElement.getExportShop().getShopId()));
                stockTransDTO.setToOwnerType(Const.STOCK_TYPE.STORE);
                stockTransDTO.setToOwnerId(DataUtil.safeToLong(firstElement.getReceiveShop().getShopId()));
                stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
                stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                stockTransDTO.setReasonId(Const.REASON_ID.XUAT_KHO_CAP_DUOI);
                stockTransDTO.setNote(firstElement.getNote());
                stockTransDTO.setTotalAmount(null);
                stockTransDTO.setCreateDatetime(getSysdateFromDB());
                StockTransDTO stockTrans = stockTransService.save(stockTransDTO);

                StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
                stockTransActionDTO.setStockTransId(stockTrans.getStockTransId());
                stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                stockTransActionDTO.setActionCode(firstElement.getActionCode());
                stockTransActionDTO.setCreateDatetime(getSysdateFromDB());
                stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
                stockTransActionService.save(stockTransActionDTO);

                for(FieldExportFileDTO dto : lstDetail){
                    StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                    stockTransDetailDTO.setStockTransId(stockTrans.getStockTransId());
                    stockTransDetailDTO.setProdOfferId(dto.getProductOfferId());
                    stockTransDetailDTO.setStateId(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                    stockTransDetailDTO.setQuantity(dto.getQuantity());
                    stockTransDetailDTO.setCreateDatetime(getSysdateFromDB());
                    stockTransDetailService.save(stockTransDetailDTO);
                }
            }
//            for(FieldExportIsdnDTO dto : fieldExportIsdnDTOs) {
                // Check trung trong DB
//                if(stockTransService.checkExistTransByfieldExportIsdnDTO(dto)){
//                    reportErrorValidateFail("frmCreateField:msgs", "", "mn.isdn.manage.create.field.err.exist.order");
//                    return;
//                }
//                StockTransDTO stockTransDTO = new StockTransDTO();
//                stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
//                stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
//                stockTransDTO.setTotalAmount(null);
//                stockTransDTO.setFromOwnerId(dto.getFromOwnerId());
//                stockTransDTO.setToOwnerId(dto.getToOwnerId());
//                stockTransDTO.setNote(dto.getNote());
//                stockTransDTO.setCreateDatetime(getSysdateFromDB());
//                StockTransDTO stockTrans = stockTransService.save(stockTransDTO);
////                StockTransDTO transDTOImport = DataUtil.cloneBean(stockTransDTO);
////                transDTOImport.setStockTransId(stockTrans.getStockTransId());
//
//                StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
//                stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
//                stockTransActionDTO.setActionCode(dto.getActionCode());
//                stockTransActionDTO.setStockTransId(stockTrans.getStockTransId());
//                stockTransActionDTO.setCreateDatetime(getSysdateFromDB());
//                stockTransActionService.save(stockTransActionDTO);
//
//                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
//                stockTransDetailDTO.setStockTransId(stockTrans.getStockTransId());
//                stockTransDetailDTO.setQuantity(dto.getQuantity());
//                stockTransDetailDTO.setProdOfferId(dto.getProductOfferId());
//                stockTransDetailDTO.setCreateDatetime(getSysdateFromDB());
//                stockTransDetailService.save(stockTransDetailDTO);
//            }
            previewOrder = false;
            reportSuccess("", "export.order.create.success");
            init();
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmCreateField:msgs", "", ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public StreamedContent downloadFileError() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName(Const.EXPORT_FILE_NAME.ERR_FIELD_ISDN);
            fileExportBean.setTempalatePath(FileUtil.getTemplatePath());
            fileExportBean.setOutPath(FileUtil.getOutputPath());
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.ERR_ISDN_FROM_FILE_TEMPLATE);
            fileExportBean.putValue("listNotes", lstErrFieldExportIsdn);

            return FileUtil.exportToStreamed(fileExportBean);
//                FileUtil.exportFile(fileExportBean);
//                return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("frmExportIsdn:msgs", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    private void validateFileUpload(){
        try {
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            //TODO validate tong so dong
//            if(totalRow <= 1){
//
//            }
            for(int i = 1 ; i < totalRow; i++){
                Row row = sheetProcess.getRow(i);
                FieldExportFileDTO dto = new FieldExportFileDTO();
                String actionCode = processingFile.getStringValue(row.getCell(0));
                String fromOwnerCode = processingFile.getStringValue(row.getCell(1));
                String toOwnerCode = processingFile.getStringValue(row.getCell(2));
                String productOfferCode = processingFile.getStringValue(row.getCell(3));
                Long quantity = DataUtil.safeToLong(processingFile.getStringValue(row.getCell(4)));
                String note = processingFile.getStringValue(row.getCell(5));
                dto.setActionCode(actionCode);
                dto.setFromOwnerCode(fromOwnerCode);
                dto.setToOwnerCode(toOwnerCode);
                dto.setProductOfferCode(productOfferCode);
                dto.setQuantity(quantity);
                dto.setNote(note);

                String msg = validateData(dto, fieldExportFileDTOs);
                if(!DataUtil.isNullObject(msg)){
//                    reportErrorValidateFail("frmCreateField:msgs", "", msg);
                    dto.setErrorMsg(msg);
                    lstErrFieldExportIsdn.add(dto);
                } else {
                    fieldExportFileDTOs.add(dto);
                }

            }
//            List<String> lstActionCode = Lists.newArrayList();
//            List<FieldExportIsdnDTO> lstTemp = Lists.newArrayList();
//            for(FieldExportIsdnDTO dto : fieldExportIsdnDTOs){
//                if(lstActionCode.contains(dto.getActionCode())){
//                    for(FieldExportIsdnDTO tmp : lstTemp){
//                        if(DataUtil.safeEqual(tmp.getActionCode(), dto.getActionCode())
//                                && DataUtil.safeEqual(tmp.getToOwnerCode(),dto.getToOwnerCode())){
//                            reportErrorValidateFail("frmCreateField:msgs", "", "mn.isdn.manage.create.field.err.file.action.exist");
//                            return;
//                        }
//                    }
//                }
//                lstActionCode.add(dto.getActionCode());
//                lstTemp.add(dto);
//            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
        }
    }

    private String validateData(FieldExportFileDTO fieldExportFileDTO, List<FieldExportFileDTO> fieldExportFileDTOs) throws Exception {
        String actionCode = fieldExportFileDTO.getActionCode();
        String fromOwnerCode = fieldExportFileDTO.getFromOwnerCode();
        String toOwnerCode = fieldExportFileDTO.getToOwnerCode();
        String productOfferCode = fieldExportFileDTO.getProductOfferCode();
        Long quantity = fieldExportFileDTO.getQuantity();
        String note = fieldExportFileDTO.getNote();
        if(DataUtil.isNullObject(actionCode) || actionCode.length() > 50 || Pattern.compile("[^A-Za-z0-9_]").matcher(actionCode).find())
            return getText("mn.isdn.manage.create.field.validate.actionCode");
        if(DataUtil.isNullObject(fromOwnerCode))
            return getText("mn.isdn.manage.create.field.validate.fromOwner");
        if(DataUtil.isNullObject(toOwnerCode))
            return getText("mn.isdn.manage.create.field.validate.toOwner");
        if(DataUtil.safeEqual(fromOwnerCode, toOwnerCode))
            return getText("mn.isdn.manage.create.field.validate.toOwnerEqualfromOwner");
        if(DataUtil.isNullObject(productOfferCode))
            return getText("mn.isdn.manage.create.field.validate.productOffer");
        if(DataUtil.isNullObject(quantity) || quantity > 500000)
            return getText("mn.isdn.manage.create.field.validate.quantity");
        if(note.length() > 450)
            return getText("mn.isdn.manage.create.field.validate.note");

        // validate kho
        List<ShopDTO> lstShopFrom = shopService.getListShopByStaffShopId(staffDTO.getShopId(), fromOwnerCode);
        if(DataUtil.isNullOrEmpty(lstShopFrom)) {
            return getText("mn.isdn.manage.creat.field.export.fromShopInvalid");
        } else {
            fieldExportFileDTO.setFromOwnerId(lstShopFrom.get(0).getShopId());
            fieldExportFileDTO.setExportShop(lstShopFrom.get(0));
        }

        List<ShopDTO> lstShopTo = shopService.getListShopByStaffShopId(staffDTO.getShopId(), toOwnerCode);
        if(DataUtil.isNullOrEmpty(lstShopTo)) {
            return getText("mn.isdn.manage.creat.field.export.toShopInvalid");
        } else {
            fieldExportFileDTO.setToOwnerId(lstShopTo.get(0).getShopId());
            fieldExportFileDTO.setReceiveShop(lstShopTo.get(0));
        }

        // validate mat hang
        ProductOfferingDTO dto = productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS.ACTIVE);
        if(DataUtil.isNullObject(dto) ||
                !DataUtil.safeEqual(dto.getStatus(), Const.STATUS.ACTIVE) ||
                !(DataUtil.safeEqual(dto.getProductOfferTypeId(),Const.PRODUCT_OFFER_TYPE.HP)
                        || DataUtil.safeEqual(dto.getProductOfferTypeId(),Const.PRODUCT_OFFER_TYPE.MOBILE)
                        || DataUtil.safeEqual(dto.getProductOfferTypeId(),Const.PRODUCT_OFFER_TYPE.PSTN))) {
            return getText("mn.isdn.manage.creat.field.export.productOffer");
        } else {
            fieldExportFileDTO.setProductOfferId(dto.getProductOfferingId());
            fieldExportFileDTO.setProductOfferingDTO(dto);
        }

        for(FieldExportFileDTO exportIsdnDTO : fieldExportFileDTOs){
            if(!DataUtil.safeEqual(exportIsdnDTO.getActionCode(), fieldExportFileDTO.getActionCode())){
                return getText("mn.isdn.manage.create.field.err.actionCodeNotEqual");
            }
            if(DataUtil.safeEqual(fieldExportFileDTO.getActionCode(), exportIsdnDTO.getActionCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getFromOwnerCode(), exportIsdnDTO.getFromOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getToOwnerCode(), exportIsdnDTO.getToOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getProductOfferCode(), exportIsdnDTO.getProductOfferCode())){
                return getText("mn.isdn.manage.create.field.err.file.action.exist");
            }
        }
        return null;
    }
    public Date getSysdateFromDB() {
        try {
            return optionSetValueService.getSysdateFromDB(false);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return new Date();
    }
    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<FieldExportFileDTO> getFieldExportFileDTOs() {
        return fieldExportFileDTOs;
    }

    public void setFieldExportFileDTOs(List<FieldExportFileDTO> fieldExportFileDTOs) {
        this.fieldExportFileDTOs = fieldExportFileDTOs;
    }

    public boolean getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(boolean selectedFile) {
        this.selectedFile = selectedFile;
    }

    public boolean getPreviewOrder() {
        return previewOrder;
    }

    public void setPreviewOrder(boolean previewOrder) {
        this.previewOrder = previewOrder;
    }

    public boolean getHasFileError() {
        return hasFileError;
    }

    public void setHasFileError(boolean hasFileError) {
        this.hasFileError = hasFileError;
    }
}

package com.viettel.bccs.inventory.controller.isdn;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.omnifaces.util.Faces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
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
 * @author:hoangnt14
 */
@Component
@Scope("view")
@ManagedBean(name = "establishExportIsdnController")
public class EstablishExportIsdnController extends TransCommonController {
    @Autowired
    private OrderStockTagNameable orderStockTag;//khai bao tag thong tin lenh xuat
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    ShopService shopService;
    @Autowired
    ReasonService reasonService;
    @Autowired
    private ShopInfoNameable shopGetInfoTag;
    @Autowired
    private ShopInfoNameable shopReceiveInfoTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockNumberService stockNumberService;

    private RequiredRoleMap requiredRoleMap;
    private StaffDTO staffDTO;
    private int limitAutoComplete;
    private Boolean writeOffice = true;
    private boolean printCommand = true;

    private int tabIndex;
    private String attachFileName;
    private UploadedFile uploadedFile;
    private byte[] byteContent;
    private List<FieldExportFileDTO> fieldExportFileDTOs;
    private List<FieldExportFileDTO> lstErrFieldExportIsdn;
    private ExcellUtil processingFile;
    private boolean selectedFile;
    private boolean previewOrder;
    private boolean hasFileError;
    private List<ShopDTO> lstShop;
    private TreeNode rootStock;
    List<InfoStockIsdnDto> lstInfoStock = Lists.newArrayList();

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            //get max gioi han autocomplete
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            //init cho vung thong tin ky vOffice
            writeOfficeTag.init(this, staffDTO.getShopId());
            //init cho vung thong tin lenh xuat
            orderStockTag.init(this, writeOffice);
            shopGetInfoTag = orderStockTag.getShopInfoExportTagIsdn();
            shopReceiveInfoTag = orderStockTag.getShopInfoReceiveTag();
            shopGetInfoTag.initShopForIsdn(orderStockTag, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, null);
            shopReceiveInfoTag.initShopForIsdn(orderStockTag, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, null);
            orderStockTag.setShopInfoExportTag(shopGetInfoTag);
            orderStockTag.setShopInfoExportTagIsdn(shopGetInfoTag);
            orderStockTag.setShopInfoReceiveTag(shopReceiveInfoTag);
            //init cho tag list danh sach hang hoa
            ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, staffDTO.getShopId(), Const.OWNER_TYPE.SHOP);
            config.setModeSerial(Const.MODE_SERIAL.MODE_NO_SERIAL);
            config.setShowTotalPrice(false);
            config.setProductOfferTable("STOCK_NUMBER");
            listProductTag.init(this, config);
            orderStockTag.getShopInfoExportTag().loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_STOCK_NUM_SHOP);

            // lap lenh theo file
            fieldExportFileDTOs = Lists.newArrayList();
            lstErrFieldExportIsdn = Lists.newArrayList();
//            orderStockTag.init(this, false);
//            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
//            shopInfoTag = orderStockTag.getShopInfoExportTagIsdn();
//            shopInfoTag.initShopForIsdn(orderStockTag, DataUtil.safeToString(staffDTO.getShopId()), true, null);
            attachFileName = "";
            selectedFile = false;
            previewOrder = false;
            hasFileError = false;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
        updateElemetId("establishExport:numberTabView:pnExportOffice");
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        try {
            System.out.println("tess");
            //stockDebitDTO  = stockDebitService.findStockDebitValue(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()), vShopStaffDTO.getOwnerType());
//           / updateElemetId("establishExport:pnStockDebit");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    private void validate() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());

            if (writeOffice) {
                try {
                    writeOfficeTag.validateVofficeAccount();
                } catch (LogicException e) {
                    reportError("", e.getKeyMsg(), e);
                    topPage();
                }
            }
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doCreateExportIsdn() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
            stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            stockTransDTO.setTotalAmount(null);
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
                stockTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
            }
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            for (StockTransDetailDTO stockTransDetail : stockTransDetailDTOs) {
                stockTransDetail.setStateId(DataUtil.safeToLong(Const.STOCK_STRANS_DETAIL.STATUS_NEW));
                stockTransDetail.setPrice(null);
                stockTransDetail.setAmount(null);
            }
            VShopStaffDTO shopReceive = shopReceiveInfoTag.getvShopStaffDTO();
            stockTransDTO.setToOwnerId(DataUtil.safeToLong(shopReceive.getOwnerId()));
            stockTransDTO.setToOwnerName(shopReceive.getOwnerCode() + "-" + shopReceive.getOwnerName());
            stockTransDTO.setToOwnerType(DataUtil.safeToLong(shopReceive.getOwnerType()));
            stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("", "export.order.create.success");
            printCommand = false;
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();

            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.ISDN);
            stockTransDTO.setCreateDatetime(optionSetValueService.getSysdateFromDB(true));
            stockTransDTO.setStockTransActionId(null);
            return exportStockTransDetail(stockTransDTO, listProductTag.getListTransDetailDTOs());
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    @Secured("@")
    public void onTabChange(TabChangeEvent event) {
        TabView tabView = (TabView) event.getComponent();
        tabIndex = tabView.getChildren().indexOf(event.getTab());
        init();
    }

    @Secured("@")
    public void doValidateUnderlyingOrder() {
        validate();
    }

    // Lap lenh theo file

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                uploadedFile = null;
                attachFileName = null;
                byteContent = null;
                selectedFile = false;
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = uploadedFile.getContents();
            attachFileName = event.getFile().getFileName();
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
            fieldExportFileDTOs = Lists.newArrayList();
            lstErrFieldExportIsdn = Lists.newArrayList();
            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }
            if (validateFileUpload()) {
                previewOrder = true;
                reportSuccess("", "mn.isdn.manage.create.field.result",
                        DataUtil.safeToString(fieldExportFileDTOs.size()), DataUtil.safeToString(fieldExportFileDTOs.size() + lstErrFieldExportIsdn.size()));
            }
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
    public void createField() {
        try {
            HashMap<String, List<FieldExportFileDTO>> lstFiel = new HashMap<>();
            for (FieldExportFileDTO dto : fieldExportFileDTOs) {
                String key = dto.getActionCode() + "|" + dto.getFromOwnerCode() + "|" + dto.getToOwnerCode();
                if (lstFiel.containsKey(key)) {
                    List<FieldExportFileDTO> value = lstFiel.get(key);
                    value.add(dto);
                    lstFiel.put(key, value);
                } else {
                    List<FieldExportFileDTO> value = Lists.newArrayList();
                    value.add(dto);
                    lstFiel.put(key, value);
                }
            }

            for (String key : lstFiel.keySet()) {
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
                stockTransDTO.setCreateDatetime(getFullSysdateFromDB());
                StockTransDTO stockTrans = stockTransService.save(stockTransDTO);

                StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
                stockTransActionDTO.setStockTransId(stockTrans.getStockTransId());
                stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                stockTransActionDTO.setActionCode(firstElement.getActionCode());
                stockTransActionDTO.setCreateDatetime(getFullSysdateFromDB());
                stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
                stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
                stockTransActionDTO.setNote(firstElement.getNote());
                stockTransActionService.save(stockTransActionDTO);

                for (FieldExportFileDTO dto : lstDetail) {
                    StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                    stockTransDetailDTO.setStockTransId(stockTrans.getStockTransId());
                    stockTransDetailDTO.setProdOfferId(dto.getProductOfferId());
                    stockTransDetailDTO.setStateId(DataUtil.safeToLong(Const.STATUS.ACTIVE));
                    stockTransDetailDTO.setQuantity(dto.getQuantity());
                    stockTransDetailDTO.setCreateDatetime(getFullSysdateFromDB());
                    stockTransDetailService.save(stockTransDetailDTO);
                }
            }
            previewOrder = false;
            reportSuccess("", "export.order.create.success");
            init();
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("", "", ex.getDescription());
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
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
        return null;
    }

    public boolean validateFileUpload() {
        try {
            processingFile = new ExcellUtil(uploadedFile, byteContent);
            Sheet sheetProcess = processingFile.getSheetAt(0);
            int totalRow = processingFile.getTotalRowAtSheet(sheetProcess);
            if (totalRow > 51) {
                reportErrorValidateFail("", "", "mn.isdn.manage.create.field.validate.maxRow");
                return false;
            }
            for (int i = 1; i < totalRow; i++) {
                Row row = sheetProcess.getRow(i);
                if (row == null) {
                    continue;
                }
                FieldExportFileDTO dto = new FieldExportFileDTO();
                String actionCode = processingFile.getStringValue(row.getCell(0)).trim();
                String fromOwnerCode = processingFile.getStringValue(row.getCell(1)).trim();
                String toOwnerCode = processingFile.getStringValue(row.getCell(2)).trim();
                String productOfferCode = processingFile.getStringValue(row.getCell(3)).trim();
                Long quantity = DataUtil.safeToLong(processingFile.getStringValue(row.getCell(4)).trim());
                String note = processingFile.getStringValue(row.getCell(5)).trim();
                dto.setActionCode(actionCode);
                dto.setFromOwnerCode(fromOwnerCode);
                dto.setToOwnerCode(toOwnerCode);
                dto.setProductOfferCode(productOfferCode);
                dto.setQuantity(quantity);
                dto.setNote(note);

                String msg = validateData(dto, fieldExportFileDTOs);
                if (DataUtil.isNullObject(msg)) {
                    fieldExportFileDTOs.add(dto);
                } else {
                    dto.setErrorMsg(msg);
                    lstErrFieldExportIsdn.add(dto);
                }

            }
            if (!DataUtil.isNullOrEmpty(lstErrFieldExportIsdn)) {
                hasFileError = true;
            }
            if (DataUtil.isNullOrEmpty(fieldExportFileDTOs)) {
                reportErrorValidateFail("", "", "mn.isdn.manage.create.field.err.noOrder");
                return false;
            }
            return true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            return false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            return false;
        }
    }

    private String validateData(FieldExportFileDTO fieldExportFileDTO, List<FieldExportFileDTO> fieldExportFileDTOs) throws Exception {
        String actionCode = fieldExportFileDTO.getActionCode();
        String fromOwnerCode = fieldExportFileDTO.getFromOwnerCode();
        String toOwnerCode = fieldExportFileDTO.getToOwnerCode();
        String productOfferCode = fieldExportFileDTO.getProductOfferCode();
        Long quantity = fieldExportFileDTO.getQuantity();
        String note = fieldExportFileDTO.getNote();
        if (DataUtil.isNullObject(actionCode) || actionCode.length() > 50 || Pattern.compile("[^A-Za-z0-9_]").matcher(actionCode).find())
            return getText("mn.isdn.manage.create.field.validate.actionCode");
        if (DataUtil.isNullObject(fromOwnerCode))
            return getText("mn.isdn.manage.create.field.validate.fromOwner");
        if (DataUtil.isNullObject(toOwnerCode))
            return getText("mn.isdn.manage.create.field.validate.toOwner");
        if (DataUtil.safeEqual(fromOwnerCode, toOwnerCode))
            return getText("mn.isdn.manage.create.field.validate.toOwnerEqualfromOwner");
        if (DataUtil.isNullObject(productOfferCode))
            return getText("mn.isdn.manage.create.field.validate.productOffer");
        if (DataUtil.isNullObject(quantity) || quantity > 50000 || quantity <= 0)
            return getText("mn.isdn.manage.create.field.validate.quantity");
        if (note.length() > 450)
            return getText("mn.isdn.manage.create.field.validate.note");

        // validate kho
        List<ShopDTO> lstShopFrom = shopService.getListShopByStaffShopId(staffDTO.getShopId(), fromOwnerCode);
        if (DataUtil.isNullOrEmpty(lstShopFrom)) {
            return getText("mn.isdn.manage.creat.field.export.fromShopInvalid");
        } else {
            fieldExportFileDTO.setFromOwnerId(lstShopFrom.get(0).getShopId());
            fieldExportFileDTO.setExportShop(lstShopFrom.get(0));
        }

        List<ShopDTO> lstShopTo = shopService.getListShopByStaffShopId(staffDTO.getShopId(), toOwnerCode);
        if (DataUtil.isNullOrEmpty(lstShopTo)) {
            return getText("mn.isdn.manage.creat.field.export.toShopInvalid");
        } else {
            fieldExportFileDTO.setToOwnerId(lstShopTo.get(0).getShopId());
            fieldExportFileDTO.setReceiveShop(lstShopTo.get(0));
        }

        // validate mat hang
        ProductOfferingDTO dto = productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS.ACTIVE);
        if (DataUtil.isNullObject(dto) ||
                !DataUtil.safeEqual(dto.getStatus(), Const.STATUS.ACTIVE) ||
                !(DataUtil.safeEqual(dto.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.HP)
                        || DataUtil.safeEqual(dto.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.MOBILE)
                        || DataUtil.safeEqual(dto.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PSTN))) {
            return getText("mn.isdn.manage.creat.field.export.productOffer");
        } else {
            fieldExportFileDTO.setProductOfferId(dto.getProductOfferingId());
            fieldExportFileDTO.setProductOfferingDTO(dto);
        }

        for (FieldExportFileDTO exportIsdnDTO : fieldExportFileDTOs) {
            if (!DataUtil.safeEqual(exportIsdnDTO.getActionCode(), fieldExportFileDTO.getActionCode())) {
                return getText("mn.isdn.manage.create.field.err.actionCodeNotEqual");
            }
            if (DataUtil.safeEqual(fieldExportFileDTO.getActionCode(), exportIsdnDTO.getActionCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getFromOwnerCode(), exportIsdnDTO.getFromOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getToOwnerCode(), exportIsdnDTO.getToOwnerCode())
                    && DataUtil.safeEqual(fieldExportFileDTO.getProductOfferCode(), exportIsdnDTO.getProductOfferCode())) {
                return getText("mn.isdn.manage.create.field.err.file.action.exist");
            }
        }
        return null;
    }

    private Date getFullSysdateFromDB() {
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
    // End lap lenh theo file

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public OptionSetValueService getOptionSetValueService() {
        return optionSetValueService;
    }

    public void setOptionSetValueService(OptionSetValueService optionSetValueService) {
        this.optionSetValueService = optionSetValueService;
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public boolean isPrintCommand() {
        return printCommand;
    }

    public void setPrintCommand(boolean printCommand) {
        this.printCommand = printCommand;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public TreeNode getRootStock() {
        return rootStock;
    }

    public void setRootStock(TreeNode rootStock) {
        this.rootStock = rootStock;
    }

    public List<InfoStockIsdnDto> getLstInfoStock() {
        return lstInfoStock;
    }

    public void setLstInfoStock(List<InfoStockIsdnDto> lstInfoStock) {
        this.lstInfoStock = lstInfoStock;
    }

    @Secured("@")
    public void viewStockIsdn() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            if (DataUtil.isNullObject(stockTransDTO) || DataUtil.isNullObject(stockTransDTO.getFromOwnerId()) || DataUtil.isNullObject(stockTransDTO.getFromOwnerType())) {
                reportError("", "", "view.info.stock.isdn.must.select.stock");
            } else {
                lstInfoStock = DataUtil.defaultIfNull(stockNumberService.viewInfoStockIsdn(stockTransDTO.getFromOwnerId(), stockTransDTO.getFromOwnerType()), Lists.newArrayList());
                rootStock = new DefaultTreeNode(new InfoStockIsdnDto(), null);
                Long currentTelecomServiceId = null;
                TreeNode currentGroup = null;
                for (InfoStockIsdnDto info : lstInfoStock) {
                    if (currentTelecomServiceId == null || !info.getTelecomServiceId().equals(currentTelecomServiceId)) {
                        currentTelecomServiceId = info.getTelecomServiceId();
                        InfoStockIsdnDto groupStock = new InfoStockIsdnDto();
                        groupStock.setProductCode(info.getTelecomServiceName());
                        currentGroup = new DefaultTreeNode(groupStock, rootStock);
                    }
                    new DefaultTreeNode(info, currentGroup);
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void downloadInfoStock() {
        try {
            if (DataUtil.isNullOrEmpty(lstInfoStock)) {
                reportError("", "", "view.info.stock.isdn.emty.data");
            } else {
                StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
                ShopDTO shop = shopService.findOne(stockTransDTO.getFromOwnerId());
                FileExportBean bean = new FileExportBean();
                bean.setTempalatePath(FileUtil.getTemplatePath());
                bean.setTemplateName("/infoStockIsdnTemplate.xls");
                bean.putValue("lstData", lstInfoStock);
                bean.putValue("stockCode", shop.getShopCode());
                Workbook bookError = FileUtil.exportWorkBook(bean);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "INFO_STOCK_ISDN_" + shop.getShopCode() + ".xls" + "\"");
                externalContext.setResponseContentType("application/vnd.ms-excel");
                bookError.write(externalContext.getResponseOutputStream());
                facesContext.responseComplete();
                return;
            }
        } catch (Exception ex) {
            reportError("", "common.error.happen", ex);
            topPage();
        }
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
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
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class PartnerExportStockController extends TransCommonController {

    private int limitAutoComplete;
    private byte[] byteContent;
    boolean errorImport;
    private boolean checkErp;
    private boolean canPrint;
    private boolean renderedImport = false;
    private String productOfferTypeId;
    private String typeExport = "2";
    private String attachFileName = "";
    private Long totalSerial;
    private Long stockTransActionId;

    private StockTransFullDTO stockTransFullDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private StaffDTO staffDTO;
    private UploadedFile uploadedFile;
    private ExcellUtil processingFile;
    private RequiredRoleMap requiredRoleMap;

    private List<PartnerDTO> lstPartnerDTOs;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private List<StockTransSerialDTO> listStockTransSerialDTOs;
    private List<StockTransSerialDTO> lstErrorSerial;

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private ReasonService reasonService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_DONGBO_ERP);
            stockTransFullDTO = new StockTransFullDTO();
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            shopInfoTag.initShopNotChanelTypeIdCurrentAndChild(this, Const.MODE_SHOP.PARTNER, staffDTO.getShopId().toString(), Const.OWNER_TYPE.SHOP, Const.CHANNEL_TYPE.CHANNEL_TYPE_AGENT);
            shopInfoTag.loadShop(staffDTO.getShopId().toString(), false);
            stockTransFullDTO.setFromOwnerId(staffDTO.getShopId());
            stockTransFullDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP);
            stockTransFullDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staffDTO));
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstPartnerDTOs = partnerService.findPartner(new PartnerDTO());
            Date currentDate = getSysdateFromDB();
            stockTransFullDTO.setCreateDatetime(currentDate);
            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            doResetTypeExport();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * ham xu ly reset vung thon tin kieu xuat hang, serial imei
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetTypeExport() {
        listStockTransSerialDTOs = Lists.newArrayList();
        resetUploadFile();
        resetTypeRangeNumber();
        resetImportNumber();
    }

    /**
     * ham reset thong tin file
     * @author thanhnt77
     */
    private void resetUploadFile() {
        attachFileName = null;
        processingFile = null;
        uploadedFile = null;
        byteContent = null;
        renderedImport = false;
        errorImport = false;
    }


    /**
     * ham reset thong tin nhap theo dai so
     * @author thanhnt77
     */
    private void resetTypeRangeNumber() {
        stockTransFullDTO.setFromSerial(null);
        stockTransFullDTO.setToSerial(null);
    }

    /**
     * ham reset thong tin nhap so luong
     * @author thanhnt77
     */
    private void resetImportNumber() {
        stockTransFullDTO.setQuantity(null);
        stockTransFullDTO.setStrQuantity(null);
    }

    @Secured("@")
    public void handlFileUpload(FileUploadEvent event) {
        try {
            resetUploadFile();
            if (event == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
            }
            uploadedFile = event.getFile();

            BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
            if (!message.isSuccess()) {
                LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                ex.setDescription(message.getDescription());
                throw ex;
            }

            byteContent = uploadedFile.getContents();
            attachFileName = new String(uploadedFile.getFileName().getBytes(), "UTF-8");
            processingFile = new ExcellUtil(uploadedFile, byteContent);

        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSelectProductOffering() {
        stockTransFullDTO.setProdOfferCode(this.productOfferingTotalDTO.getCode());
        stockTransFullDTO.setProdOfferId(this.productOfferingTotalDTO.getProductOfferingId());
        stockTransFullDTO.setProdOfferName(this.productOfferingTotalDTO.getName());
    }

    /**
     * ham xu ly chuyen noi trong file thanh chuoi de gui qua service
     * @author thanhnt77
     * @param excellUtil
     * @return
     * @throws Exception
     */
    private String getStrSerialFromFile(ExcellUtil excellUtil) throws Exception{
        if (excellUtil == null) {
            return "";
        }
        lstErrorSerial = Lists.newArrayList();
        Sheet sheetProcess = excellUtil.getSheetAt(0);
        StringBuilder strSerial = new StringBuilder("");
        boolean isHandset = Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId());
        int totalRow = 0;
        StockTransSerialDTO dto;
        for (Row row : sheetProcess) {
            if (row == null) {
                continue;
            }
            totalRow++;
            if (totalRow <= 5) {
                continue;
            }
            dto = new StockTransSerialDTO();
            String fromSerial = excellUtil.getStringValue(row.getCell(0)).trim();
            String toSerial = excellUtil.getStringValue(row.getCell(1)).trim();
            dto.setFromSerial(fromSerial);
            dto.setToSerial(toSerial);
            lstErrorSerial.add(dto);
            if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
                continue;
            }
            if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.SERIAL_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.SERIAL_REGEX) || fromSerial.length() > 19 || toSerial.length() > 19) {
                dto.setErrorMessage(getText("mm.divide.upload.valid.serial.length"));
                errorImport = true;
                continue;
            }
            if (fromSerial.length() != toSerial.length()) {
                dto.setErrorMessage(getText("mn.stock.partner.length.fromSerial.toSerial.valid"));
                errorImport = true;
                continue;
            }

            if (isHandset) {
                if (!fromSerial.equals(toSerial)) {
                    dto.setErrorMessage(getText("mn.stock.partner.serial.valid.hanset.valid.file"));
                    errorImport = true;
                    continue;
                }
            } else {
                if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                    dto.setErrorMessage(getTextParam("stock.trans.validate.fromSerial.toSerial.format", stockTransFullDTO.getProdOfferName()));
                    errorImport = true;
                    continue;
                }
                BigInteger fromSerialNum = new BigInteger(fromSerial);
                BigInteger toSerialNum = new BigInteger(toSerial);
                BigInteger result = toSerialNum.subtract(fromSerialNum);
                //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
                if (result.compareTo(new BigInteger("0")) < 0) {
                    dto.setErrorMessage(getText("stock.validate.inspect.fromSerial.less.toSerial"));
                    errorImport = true;
                    continue;
                }
            }
            if (!"".equals(strSerial.toString())) {//xu ly cach dong
                strSerial.append("&");
            }
            strSerial.append(fromSerial).append(":").append(toSerial);//xu ly cach from serial to serial
        }
        return strSerial.toString();
    }

    /**
     * ham xu ly xuat hang
     * @author thanhnt77
     */
    @Secured("@")
    public void doExportParner() {
        try {
            if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
                BaseMessage messageFile = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
                if (!messageFile.isSuccess()) {
                    LogicException ex = new LogicException(messageFile.getErrorCode(), messageFile.getKeyMsg());
                    ex.setDescription(messageFile.getDescription());
                    throw ex;
                }
            }

            stockTransFullDTO.setSyncERP(checkErp ? "0" : null);
            BasePartnerMessage message = executeStockTransService.createExportToPartner(Const.STOCK_TRANS.PARTNER, Const.STOCK_TRANS_TYPE.EXPORT,
                    stockTransFullDTO, getStaffDTO(), typeExport, getStrSerialFromFile(processingFile), null);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                LogicException logicException = new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
                throw logicException;
            }
            stockTransFullDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, staffDTO));
            reportSuccess("frmExportStockPartner:msgSearch", message.getKeyMsg(), message.getParamsMsg()[0],message.getParamsMsg()[1]);
            stockTransActionId = message.getStockTransActionId();
            canPrint = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStockPartner:msgSearch", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportStockPartner:msgSearch", "common.error.happened", ex);
        }
        topPage();
    }

    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                List<Long> lstActionID = Lists.newArrayList();
                lstActionID.add(stockTransActionId);
                List<StockTransFullDTO> lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lstActionID), new ArrayList<>());
                stockTransDTO.setStockTransActionId(stockTransActionId);
                return exportStockTransDetail(stockTransDTO, lsStockTransFull);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public StockTransDTO getStockTransDTOForPint() {
        try {
            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
            if (stockTransActionDTO != null) {
                StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());
                if (stockTransDTO != null) {
                    stockTransDTO.setActionCode(stockTransActionDTO.getActionCode());
                    String fromOwnerName = null;
                    String fromOwnerAddress = null;
                    String toOwnerName = null;
                    String toOwnerAddress = null;
                    if (DataUtil.safeEqual(stockTransDTO.getFromOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                        ShopDTO shopDTO = shopService.findOne(stockTransDTO.getFromOwnerId());
                        if (shopDTO != null) {
                            fromOwnerName = shopDTO.getName();
                            fromOwnerAddress = shopDTO.getAddress();
                        }
                    }
                    if (DataUtil.safeEqual(stockTransDTO.getToOwnerType(), Const.OWNER_TYPE.PARTNER_LONG)) {
                        PartnerDTO partnerDTO = partnerService.findOne(stockTransDTO.getToOwnerId());
                        if (partnerDTO != null) {
                            toOwnerName = partnerDTO.getPartnerName();
                            toOwnerAddress = partnerDTO.getAddress();
                        }
                    }
                    stockTransDTO.setFromOwnerName(fromOwnerName);
                    stockTransDTO.setFromOwnerAddress(fromOwnerAddress);
                    stockTransDTO.setToOwnerName(toOwnerName);
                    stockTransDTO.setToOwnerAddress(toOwnerAddress);
                    if (!DataUtil.isNullOrZero(stockTransDTO.getReasonId())) {
                        ReasonDTO reasonDTO = reasonService.findOne(stockTransDTO.getReasonId());
                        stockTransDTO.setReasonName(reasonDTO.getReasonName());
                    }
                }
                return stockTransDTO;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * check shop vtt
     *
     * @return
     * @author ThanhNT
     */
    public boolean getShopVTT() {
        return staffDTO != null && Const.SHOP.SHOP_VTT_ID.equals(staffDTO.getShopId());
    }

    /**
     * validate tam
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidate() {
        if (Const.EXPORT_PARTNER.EXP_BY_FILE.equals(typeExport)) {
            if (processingFile == null) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "payNoteAndReport.no.file.choose");
            }
        }
    }

    /**
     * ham tai template
     * @author thanhnt77
     * @return
     */
    @Secured("@")
    public StreamedContent downTemplateFile() {
        InputStream addStream = Faces.getResourceAsStream(Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.REPORT_TEMPLATE.IMPORT_SERIAL);
        return new DefaultStreamedContent(addStream, "application/xls", Const.REPORT_TEMPLATE.IMPORT_SERIAL_FILENAME);
    }

    /**
     * ham tai file loi
     * @author thanhnt77
     * @return
     */
    @Secured("@")
    public StreamedContent exportImportErrorFile() {
        try {
            String sysDateStr = DateUtil.date2yyyyMMddHHmmSsString(DateUtil.sysDate());
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setOutName("error_import_serial_" + staffDTO.getName() + "_" + sysDateStr +".xls");
            bean.setOutPath(FileUtil.getOutputPath());
            bean.setTemplateName(Const.REPORT_TEMPLATE.IMPORT_SERIAL_ERROR);
            bean.putValue("create_date", getSysdateFromDB());
            bean.putValue("lstSerial", lstErrorSerial);
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

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        try {
            lstProductOfferingTotalDTO = Lists.newArrayList();
            productOfferingTotalDTO = null;
            //neu chon mat hang no-serial thi rest kieu nhap hang ve so luong
            if (getIsNoSerial()) {
                typeExport = "1";
                doResetTypeExport();
            } else if ("1".equals(typeExport)) {
                typeExport = "2";
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * nau chon loai mat hang handset thi tra ve true
     * @author thanhnt
     * @return
     */
    public boolean getIsHandset() {
        return Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId());
    }

    /**
     * nau chon loai mat hang handset thi tra ve true
     * @author thanhnt
     * @return
     */
    public boolean getIsNoSerial() {
        return Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(stockTransFullDTO.getProductOfferTypeId());
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            lstProductOfferingTotalDTO = Lists.newArrayList();
            if (!DataUtil.isNullOrZero(stockTransFullDTO.getProductOfferTypeId())) {
                CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
                lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), stockTransFullDTO.getProductOfferTypeId());
            }
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
        return lstProductOfferingTotalDTO;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProductOfferingTotal() {
        productOfferingTotalDTO = null;
        stockTransFullDTO.setProdOfferId(null);
        stockTransFullDTO.setProdOfferName(null);
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        stockTransFullDTO.setFromOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockTransFullDTO.setFromOwnerType(vShopStaffDTO.getOwnerType());
    }

    /**
     * ham xu ly blur
     * @author thanhnt77
     */
    @Secured("@")
    public void onBlurToSerial() {
        stockTransFullDTO.setStrQuantity(null);
        if (!DataUtil.isNullOrEmpty(stockTransFullDTO.getFromSerial()) && !DataUtil.isNullOrEmpty(stockTransFullDTO.getToSerial())) {
            if (getIsHandset()) {
                if (stockTransFullDTO.getFromSerial().equals(stockTransFullDTO.getToSerial())) {
                    stockTransFullDTO.setStrQuantity("1");
                } else {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.serial.valid.hanset.valid.file");
                }
            } else {
                if (DataUtil.validateStringByPattern(stockTransFullDTO.getFromSerial(), Const.REGEX.NUMBER_REGEX) && DataUtil.validateStringByPattern(stockTransFullDTO.getToSerial(), Const.REGEX.NUMBER_REGEX)) {
                    BigInteger result = new BigInteger(stockTransFullDTO.getToSerial()).subtract(new BigInteger(stockTransFullDTO.getFromSerial())).add(BigInteger.ONE);
                    if (result.compareTo(BigInteger.ZERO) > 0 ) {
                        stockTransFullDTO.setStrQuantity(result.toString());
                    } else {
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.less.toSerial");
                    }
                }
            }
        }
    }

    public Boolean getShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }

    @Secured("@")
    public void clearShop() {
        stockTransFullDTO.setFromOwnerId(null);
        stockTransFullDTO.setFromOwnerType(null);
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public byte[] getByteContent() {
        return byteContent;
    }

    public void setByteContent(byte[] byteContent) {
        this.byteContent = byteContent;
    }

    public boolean isRenderedImport() {
        return renderedImport;
    }

    public void setRenderedImport(boolean renderedImport) {
        this.renderedImport = renderedImport;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getTypeExport() {
        return typeExport;
    }

    public void setTypeExport(String typeExport) {
        this.typeExport = typeExport;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public Long getTotalSerial() {
        return totalSerial;
    }

    public void setTotalSerial(Long totalSerial) {
        this.totalSerial = totalSerial;
    }

    public StockTransFullDTO getStockTransFullDTO() {
        return stockTransFullDTO;
    }

    public void setStockTransFullDTO(StockTransFullDTO stockTransFullDTO) {
        this.stockTransFullDTO = stockTransFullDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public ExcellUtil getProcessingFile() {
        return processingFile;
    }

    public void setProcessingFile(ExcellUtil processingFile) {
        this.processingFile = processingFile;
    }

    public List<PartnerDTO> getLstPartnerDTOs() {
        return lstPartnerDTOs;
    }

    public void setLstPartnerDTOs(List<PartnerDTO> lstPartnerDTOs) {
        this.lstPartnerDTOs = lstPartnerDTOs;
    }

    public List<ProductOfferTypeDTO> getLstProductOfferTypeDTO() {
        return lstProductOfferTypeDTO;
    }

    public void setLstProductOfferTypeDTO(List<ProductOfferTypeDTO> lstProductOfferTypeDTO) {
        this.lstProductOfferTypeDTO = lstProductOfferTypeDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public List<StockTransSerialDTO> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialDTO> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public boolean isCheckErp() {
        return checkErp;
    }

    public void setCheckErp(boolean checkErp) {
        this.checkErp = checkErp;
    }

    public boolean isErrorImport() {
        return errorImport;
    }

    public void setErrorImport(boolean errorImport) {
        this.errorImport = errorImport;
    }

    public boolean isCanPrint() {
        return canPrint;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.fss.util.NumberUtil;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 4/28/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class PartnerImportStockByFileController extends ExportFileStockTransController {

    private int limitAutoComplete;
    private boolean threeProvince;
    private RequiredRoleMap requiredRoleMap;
    private ImportPartnerRequestDTO importPartnerRequestDTO;
    private ImportPartnerDetailDTO importPartnerDetailDTO;
    private List<ShopDTO> lsRegionShop;
    private List<PartnerDTO> lstPartnerDTOs;
    private List<OptionSetValueDTO> lstCurrencyType;
    private List<OptionSetValueDTO> lstImportType;
    private boolean contractType;
    private boolean rateMoney;
    private UploadedFile uploadedFile;
    private boolean renderedError = false;
    private List<StockTransSerialDTO> listStockTransSerialDTOs;
    private List<String> listError;
    private StockTransSerialDTO stockTransSerialDTO;
    private boolean renderedImport = true;
    private boolean showDetail;
    private boolean renderPrintExp = false;
    private Long createdTransId;
    private boolean checkErp;
    private String fileName;
    private byte[] contentByte;
    private static final String FILE_PARTNER_TEMPLATE_PATH = "importFromPartnerByFile.xls";
    private static final String FILE_PARTNER_ERROR_TEMPLATE_PATH = "importFromPartnerByFileError.xls";
    private List<ImportPartnerDetailDTO> lstImportDetail = Lists.newArrayList();
    private boolean showError;
    private List<ImportPartnerDetailDTO> lstError = Lists.newArrayList();

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ImportStockFromPartnerService importStockFromPartnerService;
    @Autowired
    private ImportStockFromPartnerToBranchService importStockFromPartnerToBranchService;


    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            threeProvince = false;
            writeOfficeTag.init(this, BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_DONGBO_ERP);
            if (requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN)) {
                threeProvince = true;
            }
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lsRegionShop = DataUtil.defaultIfNull(shopService.getListShopByCodeOption(Const.OPTION_SET.REGION_SHOP), new ArrayList<ShopDTO>());
            lstPartnerDTOs = partnerService.findPartner(new PartnerDTO());

            lstCurrencyType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CURRENCY_TYPE);
            lstImportType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.IMPORT_TYPE_PARTNER);
            doReset();
            executeCommand("updateAll()");
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doReset() {
        try {
            importPartnerRequestDTO = new ImportPartnerRequestDTO();
            Date currentDate = getSysdateFromDB();
            importPartnerRequestDTO.setCreateDate(currentDate);
            importPartnerRequestDTO.setToOwnerId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            importPartnerRequestDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            importPartnerRequestDTO.setContractStatus(1L);
            importPartnerRequestDTO.setPoDate(currentDate);
            importPartnerRequestDTO.setContractDate(currentDate);
            importPartnerRequestDTO.setRequestDate(currentDate);
            importPartnerRequestDTO.setImportDate(currentDate);
            importPartnerRequestDTO.setDateKCS(currentDate);

            importPartnerDetailDTO = new ImportPartnerDetailDTO();
            contractType = true;
            importPartnerRequestDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.AGENT, BccsLoginSuccessHandler.getStaffDTO()));
            importPartnerRequestDTO.setSignVoffice(true);
            importPartnerRequestDTO.setImpType(DataUtil.safeToLong(lstImportType.get(0).getValue()));
            uploadedFile = null;
            renderedError = false;
            listStockTransSerialDTOs = Lists.newArrayList();
            stockTransSerialDTO = new StockTransSerialDTO();
            renderedImport = true;
            renderPrintExp = false;
            showDetail = false;
            createdTransId = 0L;
            lstImportDetail = Lists.newArrayList();
            importPartnerRequestDTO.setCurrencyType("VND");
            rateMoney = true;
            showError = false;
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    public StreamedContent exportStockTransDetail(String mode) {
        StreamedContent streamedContent = null;
        try {
            return printStockTransDetail(genStockTransDTO(getVStockTransDTO(mode)));
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return streamedContent;
    }


    private VStockTransDTO getVStockTransDTO(String mode) throws LogicException, Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        vStockTransDTO.setOwnerID(getStaffDTO().getShopId());
        vStockTransDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        //
        vStockTransDTO.setStartDate(optionSetValueService.getSysdateFromDB(true));
        vStockTransDTO.setEndDate(optionSetValueService.getSysdateFromDB(true));
        if (Const.STOCK_TRANS.TRANS_CODE_PX.equals(mode)) {
            if (createdTransId == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.trans");
            }
            vStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            vStockTransDTO.setStockTransID(createdTransId);
        } else {
            vStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            vStockTransDTO.setActionCode(importPartnerRequestDTO.getActionCode());
            vStockTransDTO.setOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            vStockTransDTO.setOwnerID(importPartnerRequestDTO.getPartnerId());
        }
        List<VStockTransDTO> vStockTransDTOs = transService.mmSearchVStockTrans(vStockTransDTO);
        if (DataUtil.isNullOrEmpty(vStockTransDTOs)) {
            return null;
        }
        return vStockTransDTOs.get(0);
    }

    @Secured("@")
    public void fileUploadAction(FileUploadEvent event) {
        try {
            BaseMessage message;
            uploadedFile = event.getFile();
            message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_EXCEL_TYPE_LIST, MAX_SIZE_5M);
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


    @Secured("@")
    public boolean getContractTypeDisable() {
        return importPartnerRequestDTO.getContractStatus() != null && DataUtil.safeEqual(importPartnerRequestDTO.getContractStatus(), 2L);
    }

    @Secured("@")
    public void doChangeRateMoney() {
        try {
            if (DataUtil.safeEqual(importPartnerRequestDTO.getCurrencyType(), "VND")) {
                rateMoney = true;
            } else {
                rateMoney = false;
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void previewFile() {
        try {
            if (DataUtil.isNullObject(contentByte) && DataUtil.isNullObject(uploadedFile)) {
                throw new LogicException("", "utilities.update.pincode.card.file.require");
            }
            lstImportDetail = readExcelToListDetail();
            if (DataUtil.isNullOrEmpty(lstImportDetail)) {
                throw new LogicException("", "utilities.update.pincode.card.update.check.file.empty");
            }
            showDetail = true;
            showError = false;
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }

    }

    @Secured("@")
    public void doImportValidate() {
        try {
            if (DataUtil.isNullObject(uploadedFile) || DataUtil.isNullOrEmpty(fileName)) {
                throw new LogicException("", "utilities.update.pincode.card.file.require");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private List<ImportPartnerDetailDTO> readExcelToListDetail() throws LogicException, Exception {
        List<ImportPartnerDetailDTO> lstImport = Lists.newArrayList();
        ExcellUtil ex = new ExcellUtil(uploadedFile, contentByte);
        Sheet sheet = ex.getSheetAt(0);
        int totalRows = ex.getTotalRowAtSheet(sheet);
        String prodOfferCode;
        String strStateId;
        String fromSerial;
        String toSerial;
        String strQuantity;
        String strTotalPrice;
        String orderCode;
        String startDateWarranty;
        for (int i = 1; i <= totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
            prodOfferCode = ex.getStringValue(row.getCell(0)).trim();
            importPartnerDetailDTO.setProdOfferCode(prodOfferCode);
            strStateId = ex.getStringValue(row.getCell(1)).trim();
            importPartnerDetailDTO.setStrStateId(strStateId);
            fromSerial = ex.getStringValue(row.getCell(2)).trim();
            importPartnerDetailDTO.setFromSerial(fromSerial);
            toSerial = ex.getStringValue(row.getCell(3)).trim();
            importPartnerDetailDTO.setToSerial(toSerial);
            strQuantity = ex.getStringValue(row.getCell(4)).trim();
            importPartnerDetailDTO.setStrQuantity(strQuantity);
            strTotalPrice = ex.getStringValue(row.getCell(5)).trim();
            importPartnerDetailDTO.setStrTotalPrice(strTotalPrice);
            orderCode = ex.getStringValue(row.getCell(6)).trim();
            importPartnerDetailDTO.setOrderCode(orderCode);
            startDateWarranty = ex.getStringValue(row.getCell(7)).trim();
            importPartnerDetailDTO.setStrStartDateWarranty(startDateWarranty);
            lstImport.add(importPartnerDetailDTO);
        }
        return lstImport;
    }

    @Secured("@")
    public void doImport() {
        try {
            //voffice
            if (importPartnerRequestDTO.isSignVoffice()) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                importPartnerRequestDTO.setStockTransVofficeDTO(getStockTransVofficeDTO(signOfficeDTO));
            }
            //thong tin chi tiet
            lstImportDetail = readExcelToListDetail();
            importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportDetail);
            //thong tin user dang nhap
            importPartnerRequestDTO.setImportStaffId(getStaffDTO().getStaffId());
            importPartnerRequestDTO.setImportStaffCode(getStaffDTO().getStaffCode());
            //tao giao dich - tra ve id cua giao dich xuat so
            lstError = DataUtil.defaultIfNull(importStockFromPartnerService.executeStockTransForPartnerByFile(importPartnerRequestDTO, getStaffDTO()), Lists.newArrayList());
            if (!DataUtil.isNullOrEmpty(lstError)) {
                showError = true;
            }
            topReportSuccess("", "partner.by.file.import.success", lstImportDetail.size() - lstError.size(), lstImportDetail.size());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }


    @Secured("@")
    public void downloadFileTemplate() {
        try {
            String fileNameTemplate = FILE_PARTNER_TEMPLATE_PATH;
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

    /**
     * goi khi thay doi buoc nhay hoac from serial khi nhap serial theo dai
     */

    public void exportErrorFile() {
        try {
            if (DataUtil.isNullOrEmpty(lstError)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.trans");
            }
            FileExportBean bean = new FileExportBean();
            bean.setTempalatePath(FileUtil.getTemplatePath());
            bean.setTemplateName(FILE_PARTNER_ERROR_TEMPLATE_PATH);
            bean.putValue("lstData", lstError);
            Workbook workbook = FileUtil.exportWorkBook(bean);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + FILE_PARTNER_ERROR_TEMPLATE_PATH + "\"");
            externalContext.setResponseContentType("application/vnd.ms-excel");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
            return;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    public Boolean getShowCheckErp() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_DONGBO_ERP);
    }


    @Secured("@")
    public void clearShop() {
        importPartnerRequestDTO.setToOwnerId(null);
    }

    public boolean isContractType() {
        return contractType;
    }

    public void setContractType(boolean contractType) {
        this.contractType = contractType;
    }

    public boolean isRenderedError() {
        return renderedError;
    }

    public void setRenderedError(boolean renderedError) {
        this.renderedError = renderedError;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public boolean isRateMoney() {
        return rateMoney;
    }

    public void setRateMoney(boolean rateMoney) {
        this.rateMoney = rateMoney;
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

    public boolean isThreeProvince() {
        return threeProvince;
    }

    public void setThreeProvince(boolean threeProvince) {
        this.threeProvince = threeProvince;
    }

    public List<OptionSetValueDTO> getLstImportType() {
        return lstImportType;
    }

    public void setLstImportType(List<OptionSetValueDTO> lstImportType) {
        this.lstImportType = lstImportType;
    }

    public ImportPartnerRequestDTO getImportPartnerRequestDTO() {
        return importPartnerRequestDTO;
    }

    public void setImportPartnerRequestDTO(ImportPartnerRequestDTO importPartnerRequestDTO) {
        this.importPartnerRequestDTO = importPartnerRequestDTO;
    }

    public List<ShopDTO> getLsRegionShop() {
        return lsRegionShop;
    }

    public void setLsRegionShop(List<ShopDTO> lsRegionShop) {
        this.lsRegionShop = lsRegionShop;
    }

    public List<PartnerDTO> getLstPartnerDTOs() {
        return lstPartnerDTOs;
    }

    public void setLstPartnerDTOs(List<PartnerDTO> lstPartnerDTOs) {
        this.lstPartnerDTOs = lstPartnerDTOs;
    }


    public ImportPartnerDetailDTO getImportPartnerDetailDTO() {
        return importPartnerDetailDTO;
    }

    public void setImportPartnerDetailDTO(ImportPartnerDetailDTO importPartnerDetailDTO) {
        this.importPartnerDetailDTO = importPartnerDetailDTO;
    }

    public List<OptionSetValueDTO> getLstCurrencyType() {
        return lstCurrencyType;
    }

    public void setLstCurrencyType(List<OptionSetValueDTO> lstCurrencyType) {
        this.lstCurrencyType = lstCurrencyType;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public StockTransSerialDTO getStockTransSerialDTO() {
        return stockTransSerialDTO;
    }

    public void setStockTransSerialDTO(StockTransSerialDTO stockTransSerialDTO) {
        this.stockTransSerialDTO = stockTransSerialDTO;
    }

    public boolean isRenderedImport() {
        return renderedImport;
    }

    public void setRenderedImport(boolean renderedImport) {
        this.renderedImport = renderedImport;
    }

    public List<StockTransSerialDTO> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialDTO> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public boolean isRenderPrintExp() {
        return renderPrintExp;
    }

    public void setRenderPrintExp(boolean renderPrintExp) {
        this.renderPrintExp = renderPrintExp;
    }

    public boolean isCheckErp() {
        return checkErp;
    }

    public void setCheckErp(boolean checkErp) {
        this.checkErp = checkErp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ImportPartnerDetailDTO> getLstImportDetail() {
        return lstImportDetail;
    }

    public void setLstImportDetail(List<ImportPartnerDetailDTO> lstImportDetail) {
        this.lstImportDetail = lstImportDetail;
    }

    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }
}

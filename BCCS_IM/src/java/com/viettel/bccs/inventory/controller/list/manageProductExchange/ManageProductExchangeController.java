package com.viettel.bccs.inventory.controller.list.manageProductExchange;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.lowagie.text.*;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.PartnerUpdateSimService;
import com.viettel.bccs.inventory.service.ProductExchangeService;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import org.primefaces.component.export.ExcelOptions;
//import org.primefaces.component.export.PDFOptions;

/**
 * Created by hoangnt14 on 5/18/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageProductExchangeController extends TransCommonController {

    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private PartnerUpdateSimService partnerUpdateSimService;
    @Autowired
    private ProductExchangeService productExchangeService;

    private int limitAutoComplete;
    private Long regionStockId;
    private List<ShopDTO> lsRegionShop;
    private ProductOfferTypeDTO productOfferTypeDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private ProductOfferingTotalDTO productOfferingTotalNewDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private ProductExchangeDTO productExchangeInputDTO;
    private ProfileDTO profileDTO;
    private RequiredRoleMap requiredRoleMap;
    private boolean threeProvince;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private UploadedFile uploadedFile;
    private List<String> listError;
    private boolean renderedError = false;
    private ImportPartnerDetailDTO importPartnerDetailDTO;

    private ProductExchangeDTO productExchangeDTO;
    private List<ProductExchangeDTO> lstProductExchangeDTOs;
    private ProductExchangeDTO removeProductExchange;
    private List<FieldExportFileRowDataDTO> lstExportFile = new ArrayList();

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            if (!DataUtil.isNullOrEmpty(lstProductOfferTypeDTO)) {
                for (ProductOfferTypeDTO productOfferTypeDTO : lstProductOfferTypeDTO) {
                    if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.KIT)) {
                        this.productOfferTypeDTO = DataUtil.cloneBean(productOfferTypeDTO);
                        break;
                    }
                }
            }
            Date currentDate = optionSetValueService.getSysdateFromDB(true);
            productExchangeInputDTO = new ProductExchangeDTO();
            productExchangeInputDTO.setStartDatetime(currentDate);
            productExchangeInputDTO.setEndDatetime(currentDate);
            importPartnerDetailDTO = new ImportPartnerDetailDTO();
            renderedError = false;
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }


    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), productOfferTypeDTO.getProductOfferTypeId());
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
                productExchangeInputDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
                profileDTO = productOfferingService.getProfileByProductId(productOfferingTotalDTO.getProductOfferingId());
//                if (profileDTO != null && !DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
//                    importPartnerRequestDTO.setListProfile(profileDTO.getLstColumnName());
//                }
//                if (!importPartnerRequestDTO.getProfile().contains("SERIAL")) {
//                    productOfferingTotalDTO = null;
//                    importPartnerRequestDTO.setProfile(null);
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.update.file.not.contain.serial");
//                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doSelectProductOfferingNew() {
        try {
            if (!DataUtil.isNullObject(productOfferingTotalNewDTO)) {
                productExchangeInputDTO.setNewProdOfferId(productOfferingTotalNewDTO.getProductOfferingId());
                profileDTO = productOfferingService.getProfileByProductId(productOfferingTotalNewDTO.getProductOfferingId());
//                if (profileDTO != null && !DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
//                    importPartnerRequestDTO.setListProfile(profileDTO.getLstColumnName());
//                }
//                if (!importPartnerRequestDTO.getProfile().contains("SERIAL")) {
//                    productOfferingTotalNewDTO = null;
//                    importPartnerRequestDTO.setProfile(null);
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.update.file.not.contain.serial");
//                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void resetLstProductOfferingTotal() {
        productOfferingTotalDTO = null;
//        importPartnerRequestDTO.setProfile(null);
//        importPartnerRequestDTO.setListProfile(null);

    }

    @Secured("@")
    public void resetLstProductOfferingNewTotal() {
        productOfferingTotalNewDTO = null;
//        importPartnerRequestDTO.setProfile(null);
//        importPartnerRequestDTO.setListProfile(null);

    }


    @Secured("@")
    public void doValidate() {
        try {
            boolean check = true;
            List<String> erroList = Lists.newArrayList();
            StaffDTO staffDTO = staffService.findOne(getStaffDTO().getStaffId());
            if (DataUtil.isNullOrZero(productExchangeInputDTO.getProdOfferId())) {
                focusElementByRawCSSSlector(".requireProductOfferId");
                erroList.add(getText("mn.list.product.exchange.required.prodOffer"));
                check = false;
            }
            if (DataUtil.isNullOrZero(productExchangeInputDTO.getNewProdOfferId())) {
                focusElementByRawCSSSlector(".requireProductOfferIdNew");
                erroList.add(getText("mn.list.product.exchange.required.prodOfferNew"));
                check = false;
            }
            if (DataUtil.isNullObject(productExchangeInputDTO.getStartDatetime())) {
                focusElementByRawCSSSlector(".requireStartDate");
                erroList.add(getText("mn.list.product.exchange.required.startDate"));
                check = false;
            }
            if (DataUtil.isNullObject(productExchangeInputDTO.getEndDatetime())) {
                focusElementByRawCSSSlector(".requireEndDate");
                erroList.add(getText("mn.list.product.exchange.required.endDate"));
                check = false;
            }
            if (!DataUtil.isNullObject(productExchangeInputDTO.getProdOfferId())
                    && !DataUtil.isNullObject(productExchangeInputDTO.getNewProdOfferId())
                    && productExchangeInputDTO.getProdOfferId().equals(productExchangeInputDTO.getNewProdOfferId())) {
                focusElementByRawCSSSlector(".requireProductOfferId");
                focusElementByRawCSSSlector(".requireProductOfferIdNew");
                erroList.add(getText("mn.list.product.exchange.required.prodOffer.invalid"));
                check = false;
            }
            if (!DataUtil.isNullObject(productExchangeInputDTO.getStartDatetime())
                    && !DataUtil.isNullObject(productExchangeInputDTO.getEndDatetime())
                    && (productExchangeInputDTO.getStartDatetime().compareTo(productExchangeInputDTO.getEndDatetime()) > 0)) {
                erroList.add(getText("mn.list.product.exchange.required.date.todate.fromdate"));
                focusElementByRawCSSSlector(".requireStartDate");
                check = false;
            }

            Calendar cal = Calendar.getInstance();
            Date currentDate = DateUtil.stripTimeFromDate(cal.getTime());
            if (!DataUtil.isNullObject(productExchangeInputDTO.getEndDatetime())
                    && (productExchangeInputDTO.getEndDatetime().compareTo(currentDate) < 0)) {
                erroList.add(getText("mn.list.product.exchange.required.currentDate"));
                focusElementByRawCSSSlector(".requireEndDate");
                check = false;
            }
            if (!check) {
                for (String erro : erroList) {
                    reportErrorValidateFail("", "", erro);
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened.retry", ex);
        }
    }

    @Secured("@")
    public void doSearch() {
        try {
            if (!DataUtil.isNullObject(productExchangeInputDTO.getStartDatetime())
                    && !DataUtil.isNullObject(productExchangeInputDTO.getEndDatetime())
                    && (productExchangeInputDTO.getStartDatetime().compareTo(productExchangeInputDTO.getEndDatetime()) > 0)) {
                focusElementByRawCSSSlector(".requireStartDate");
                reportErrorValidateFail("", "", getText("mn.list.product.exchange.required.date.todate.fromdate"));
                return;
            }

            productExchangeInputDTO.setProdOfferTypeId(Const.STOCK_TYPE.STOCK_KIT);
            lstProductExchangeDTOs = productExchangeService.searchProductExchange(productExchangeInputDTO);
//            lstExportFile = changeColumnDataExport(lstProductExchangeDTOs);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened.retry", ex);
        }
    }

    @Secured("@")
    public void doCreate() {
        try {
            StaffDTO staffDTO = getStaffDTO();
            productExchangeService.createProductExchange(productExchangeInputDTO, staffDTO.getStaffId());
            reportSuccess("", "mn.list.product.exchange.create.success");
            productOfferingTotalNewDTO = new ProductOfferingTotalDTO();
            productOfferingTotalDTO = new ProductOfferingTotalDTO();
            productExchangeInputDTO.setProdOfferId(null);
            productExchangeInputDTO.setNewProdOfferId(null);
            doSearch();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened.retry", ex);
        }
    }

    @Secured("@")
    public void doReset() {
        regionStockId = null;
        productOfferingTotalDTO = null;
        productOfferingTotalNewDTO = null;

        importPartnerDetailDTO = new ImportPartnerDetailDTO();
        uploadedFile = null;
    }

    @Secured("@")
    public void doRemove() {
        try {
            Date currentDate = optionSetValueService.getSysdateFromDB(true);
            StaffDTO staffDTO = getStaffDTO();
            removeProductExchange.setStatus(Const.STATUS.NO_ACTIVE);
            removeProductExchange.setUpdateDatetime(currentDate);
            removeProductExchange.setUpdateUser(staffDTO.getStaffCode());
            productExchangeService.update(removeProductExchange);
            reportSuccess("", "mn.list.product.exchange.remove.success");
            doSearch();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened.retry", ex);
        }
    }

    @Secured("@")
    public void doValidateRemove(ProductExchangeDTO productExchangeDTO) {
        try {
            StaffDTO staffDTO = getStaffDTO();
            ProductExchangeDTO prodExchangeDTO = productExchangeService.findOne(productExchangeDTO.getProductExchangeId());
            if (Const.STATUS.NO_ACTIVE.equals(prodExchangeDTO.getStatus())) {
                reportErrorValidateFail("", "", "mn.list.product.exchange.validate.productExchange.err");
            }
            removeProductExchange = prodExchangeDTO;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened.retry", ex);
        }
    }


    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
//        PdfWriter.getInstance(pdf, (OutputStream) document);
        pdf.open();
//        PdfWriter writer = (PdfWriter) document;

        Element element;
        // step 4

        // step 5

//        pdf.add()
//        String FONT = "resources/sentinel-layout/fonts/times.ttf";
//        Font font = FontFactory.getFont(FONT, "Cp1250", BaseFont.EMBEDDED);
//        Font f=new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,50.0f,Font.UNDERLINE,BaseColor.RED);
//        Paragraph p=new Paragraph(document.getpa,font);
//        pdf.close();
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        pdf.add(p);
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        externalContext.setRequestCharacterEncoding("UTF-8");
//        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
//
//        pdf.add(Image.getInstance(logo));
    }

    public boolean isThreeProvince() {
        return threeProvince;
    }

    public void setThreeProvince(boolean threeProvince) {
        this.threeProvince = threeProvince;
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


    public boolean isRenderedError() {
        return renderedError;
    }

    public void setRenderedError(boolean renderedError) {
        this.renderedError = renderedError;
    }

    public Long getRegionStockId() {
        return regionStockId;
    }

    public void setRegionStockId(Long regionStockId) {
        this.regionStockId = regionStockId;
    }

    public List<ShopDTO> getLsRegionShop() {
        return lsRegionShop;
    }

    public void setLsRegionShop(List<ShopDTO> lsRegionShop) {
        this.lsRegionShop = lsRegionShop;
    }

    public ProductOfferTypeDTO getProductOfferTypeDTO() {
        return productOfferTypeDTO;
    }

    public void setProductOfferTypeDTO(ProductOfferTypeDTO productOfferTypeDTO) {
        this.productOfferTypeDTO = productOfferTypeDTO;
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

//    public ImportPartnerRequestDTO getImportPartnerRequestDTO() {
//        return importPartnerRequestDTO;
//    }
//
//    public void setImportPartnerRequestDTO(ImportPartnerRequestDTO importPartnerRequestDTO) {
//        this.importPartnerRequestDTO = importPartnerRequestDTO;
//    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public ImportPartnerDetailDTO getImportPartnerDetailDTO() {
        return importPartnerDetailDTO;
    }

    public void setImportPartnerDetailDTO(ImportPartnerDetailDTO importPartnerDetailDTO) {
        this.importPartnerDetailDTO = importPartnerDetailDTO;
    }

    public List<ProductExchangeDTO> getLstProductExchangeDTOs() {
        return lstProductExchangeDTOs;
    }

    public void setLstProductExchangeDTOs(List<ProductExchangeDTO> lstProductExchangeDTOs) {
        this.lstProductExchangeDTOs = lstProductExchangeDTOs;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalNewDTO() {
        return productOfferingTotalNewDTO;
    }

    public void setProductOfferingTotalNewDTO(ProductOfferingTotalDTO productOfferingTotalNewDTO) {
        this.productOfferingTotalNewDTO = productOfferingTotalNewDTO;
    }

    public ProductExchangeDTO getProductExchangeInputDTO() {
        return productExchangeInputDTO;
    }

    public void setProductExchangeInputDTO(ProductExchangeDTO productExchangeInputDTO) {
        this.productExchangeInputDTO = productExchangeInputDTO;
    }

    public List<FieldExportFileRowDataDTO> getLstExportFile() {
        return lstExportFile;
    }

    public void setLstExportFile(List<FieldExportFileRowDataDTO> lstExportFile) {
        this.lstExportFile = lstExportFile;
    }
}

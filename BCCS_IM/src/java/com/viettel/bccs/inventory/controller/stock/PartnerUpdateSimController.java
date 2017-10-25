package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ExcellUtil;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ImportStockFromPartnerToBranchService;
import com.viettel.bccs.inventory.service.PartnerUpdateSimService;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 5/18/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class PartnerUpdateSimController extends TransCommonController {

    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private PartnerUpdateSimService partnerUpdateSimService;
    @Autowired
    private ImportStockFromPartnerToBranchService importStockFromPartnerToBranchService;

    private int limitAutoComplete;
    private Long regionStockId;
    private List<ShopDTO> lsRegionShop;
    private ProductOfferTypeDTO productOfferTypeDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private ImportPartnerRequestDTO importPartnerRequestDTO;
    private ProfileDTO profileDTO;
    private RequiredRoleMap requiredRoleMap;
    private boolean threeProvince;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private UploadedFile uploadedFile;
    private List<String> listError;
    private boolean renderedError = false;
    private ImportPartnerDetailDTO importPartnerDetailDTO;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            threeProvince = false;
            shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
            if (requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN)) {
                threeProvince = true;
            }
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lsRegionShop = DataUtil.defaultIfNull(shopService.getListShopByCodeOption(Const.OPTION_SET.REGION_SHOP), new ArrayList<ShopDTO>());
            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            if (!DataUtil.isNullOrEmpty(lstProductOfferTypeDTO)) {
                for (ProductOfferTypeDTO productOfferTypeDTO : lstProductOfferTypeDTO) {
                    if (DataUtil.safeEqual(productOfferTypeDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.EMPTY_SIM)) {
                        this.productOfferTypeDTO = DataUtil.cloneBean(productOfferTypeDTO);
                        break;
                    }
                }
            }
            importPartnerRequestDTO = new ImportPartnerRequestDTO();
            importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(shopInfoTag.getvShopStaffDTO().getOwnerId()));
            importPartnerDetailDTO = new ImportPartnerDetailDTO();
            renderedError = false;
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void handlFileUpload(FileUploadEvent event) {
        renderedError = false;
        if (event != null) {
            uploadedFile = event.getFile();
            try {
                BaseMessage message = validateFileUploadWhiteList(uploadedFile, ALOW_EXTENSION_TEXT_TYPE_LIST, MAX_SIZE_2M);
                if (!message.isSuccess()) {
                    uploadedFile = null;
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                readSerialFromFile(uploadedFile.getInputstream());
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
    }

    public StreamedContent exportErrorFile() {
        try {
            if (DataUtil.isNullOrEmpty(listError)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.trans");
            }
            return FileUtil.exportToStreamedText(listError, getStaffDTO().getStaffCode().toLowerCase() + "_" + Const.EXPORT_FILE_NAME.IMPORT_PARTNER_SERIAL_ERROR);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    private void readSerialFromFile(InputStream stream) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(importPartnerRequestDTO.getListProfile())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.valid.profile");
        }
        List<String> readerData = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String lines;
        boolean error = false;
        while ((lines = reader.readLine()) != null) {
            if (DataUtil.isNullOrEmpty(lines)) {
                continue;
            }
            String[] splits = lines.split(Const.AppProperties4Sms.profileSparator.getValue().toString());
            if (splits.length != importPartnerRequestDTO.getListProfile().size()) {
                uploadedFile = null;
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.data.notmatch");
            }
            String regex = "^[0-9a-zA-Z]+";
            for (String serial : splits) {
                if (!serial.trim().matches(regex)) {
                    error = true;
                    lines += Const.AppProperties4Sms.profileSparator.getValue().toString() + " ";
                    lines += getMessage("import.partner.valid.char");
                }
            }
            readerData.add(lines);
        }
        if (readerData.isEmpty()) {
            uploadedFile = null;
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "search.isdn.file.empty");
        }
        if (error) {
            uploadedFile = null;
            renderedError = true;
            listError = Lists.newArrayList(readerData);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "import.partner.valid.fileContent");
        }
        importPartnerDetailDTO.setLstParam(readerData);
    }


    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void clearShop() {
        importPartnerRequestDTO.setToOwnerId(null);
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
                importPartnerDetailDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
                profileDTO = productOfferingService.getProfileByProductId(productOfferingTotalDTO.getProductOfferingId());
                if (profileDTO != null && !DataUtil.isNullOrEmpty(profileDTO.getLstColumnName())) {
                    importPartnerRequestDTO.setListProfile(profileDTO.getLstColumnName());
                }
                if (!importPartnerRequestDTO.getProfile().contains("SERIAL")) {
                    productOfferingTotalDTO = null;
                    importPartnerRequestDTO.setProfile(null);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.update.file.not.contain.serial");
                }
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
        importPartnerRequestDTO.setProfile(null);
        importPartnerRequestDTO.setListProfile(null);

    }

    @Secured("@")
    public void doValidate() {
        try {
            if (DataUtil.isNullObject(uploadedFile)) {
                throw new LogicException("", "stock.change.damaged.file.not.found");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doUpdate() {
        try {
            //get session Id
            FacesContext fCtx = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
            String sessionId = session.getId() + "_" + DateUtil.dateTime2StringNoSlash(new Date());

            List<ImportPartnerDetailDTO> detailDTOs = Lists.newArrayList(importPartnerDetailDTO);
            importPartnerRequestDTO.setListImportPartnerDetailDTOs(detailDTOs);
            importPartnerRequestDTO.setRegionStockId(regionStockId);
            //thong tin user dang nhap
            importPartnerRequestDTO.setImportStaffId(getStaffDTO().getStaffId());
            importPartnerRequestDTO.setImportStaffCode(getStaffDTO().getStaffCode());
            //tao giao dich - tra ve id cua giao dich xuat so
            Long result = DataUtil.defaultIfNull(partnerUpdateSimService.executeStockTrans(importPartnerRequestDTO, getTransRequiRedRoleMap(), sessionId), 0L);
            if (DataUtil.safeEqual(0L, result)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.update.sim.file.fail");
            }
            //lay danh sach loi
            List<String> listErrors = DataUtil.defaultIfNull(partnerUpdateSimService.getErrorDetails(sessionId, importPartnerDetailDTO.getProdOfferId()), Lists.newArrayList());
            //kiem tra so ban ghi loi
            Long total;
            Long fail = new Long(listErrors.size());
            if (fail.compareTo(0L) > 0) {
                listError = listErrors;
                renderedError = true;
            } else {
                listError = Lists.newArrayList();
            }
            total = new Long(importPartnerDetailDTO.getLstParam().size());
            //khong thanh cong ban ghi nao
            if (total.compareTo(fail) == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.partner.update.sim.file.fail");
            }
            topReportSuccess("", "mn.stock.partner.update.sim.file.success", total - fail, total);
            importPartnerRequestDTO.setStatus(Const.LongSimpleItem.importPartnerRequestStatusImported.getValue());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doReset() {
        regionStockId = null;
        shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
        shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
        productOfferingTotalDTO = null;
        importPartnerRequestDTO = new ImportPartnerRequestDTO();
        importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(shopInfoTag.getvShopStaffDTO().getOwnerId()));
        importPartnerDetailDTO = new ImportPartnerDetailDTO();
        uploadedFile = null;
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

    public ImportPartnerRequestDTO getImportPartnerRequestDTO() {
        return importPartnerRequestDTO;
    }

    public void setImportPartnerRequestDTO(ImportPartnerRequestDTO importPartnerRequestDTO) {
        this.importPartnerRequestDTO = importPartnerRequestDTO;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}

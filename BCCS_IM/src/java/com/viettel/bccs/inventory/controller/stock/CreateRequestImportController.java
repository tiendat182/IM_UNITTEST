package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ImportPartnerRequestService;
import com.viettel.bccs.inventory.service.PartnerService;
import com.viettel.bccs.inventory.service.ProductOfferTypeService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

/**
 * Created by tuannm33 on 2/16/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "createRequestImportController")
public class CreateRequestImportController extends TransCommonController {
    private Object objectController;
    private int limitAutoComplete;
    private byte[] byteContent;
    private String[] fileType = {".doc", ".docx", ".pdf", ".xls", ".xlsx", ".png", ".jpg", ".jpeg", ".bmp"};
    private String requestCodeAuto;
    private String attachFileName = "";
    private String productOfferTypeId;
    private StaffDTO staffDTO;
    private PartnerDTO partnerDTO;
    private ImportPartnerRequestDTO importPartnerRequestDTO;
    private ImportPartnerDetailDTO importPartnerDetailDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private ProductOfferTypeDTO productOfferTypeDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private List<PartnerDTO> lstPartnerDTO;
    private List<OptionSetValueDTO> lstCurrencyType;
    private UploadedFile fileUpContract;

    @Autowired
    private ShopInfoNameable shopInfoReceiveTag;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    private ImportPartnerRequestService importPartnerRequestService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            doSetDefault();
            executeCommand("updateControls()");
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSetDefault() {
        clearCache();
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            String shopId = DataUtil.safeToString(staffDTO.getShopId());
            shopInfoReceiveTag.initShop(this, shopId, false);
            shopInfoReceiveTag.loadShop(shopId, true);
            //
            importPartnerRequestDTO.setToOwnerId(DataUtil.safeToLong(shopId));
            importPartnerRequestDTO.setToOwnerType(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            importPartnerRequestDTO.setRequestCode(importPartnerRequestService.getRequestCode());
            //
            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            productOfferTypeDTO = lstProductOfferTypeDTO.get(0);
            lstPartnerDTO = partnerService.findPartner(partnerDTO);
            lstCurrencyType = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.CURRENCY_TYPE);
            importPartnerRequestDTO.setCurrencyType(Const.CURRENCY_TYPE.VND);
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    private void clearCache() {
        productOfferTypeDTO = new ProductOfferTypeDTO();
        importPartnerRequestDTO = new ImportPartnerRequestDTO();
        importPartnerDetailDTO = new ImportPartnerDetailDTO();
        Date currentDate = getSysdateFromDB();
        importPartnerRequestDTO.setCreateDate(currentDate);
        partnerDTO = new PartnerDTO();
        lstProductOfferTypeDTO = Lists.newArrayList();
        lstProductOfferingTotalDTO = Lists.newArrayList();
        productOfferingTotalDTO = null;
    }

    @Secured("@")
    public void resetRequestCode () {
        try {
            importPartnerRequestDTO.setRequestCode(importPartnerRequestService.getRequestCode());
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        lstProductOfferingTotalDTO = Lists.newArrayList();
        productOfferingTotalDTO = null;
        if (DataUtil.isNullObject(productOfferTypeDTO)) {
            productOfferTypeDTO = new ProductOfferTypeDTO();
        }
        productOfferTypeDTO.setProductOfferTypeId(DataUtil.safeToLong(productOfferTypeId));
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
    public void handleFileUpContract(FileUploadEvent event) {
        try {
            if (DataUtil.isNullObject(event)) {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.upload.file");
                topPage();
                return;
            } else {
                fileUpContract = event.getFile();
                BaseMessage message = validateFileUploadWhiteList(fileUpContract, ALOW_EXTENSION_ALL_TYPE_LIST, MAX_SIZE_5M);
                if (!message.isSuccess()) {
                    LogicException ex = new LogicException(message.getErrorCode(), message.getKeyMsg());
                    ex.setDescription(message.getDescription());
                    throw ex;
                }
                byteContent = fileUpContract.getContents();
                attachFileName = new String(event.getFile().getFileName().getBytes(), "UTF-8");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * kiem tra dinh dang file
     *
     * @param extension
     * @author
     */
    private boolean isCorrectExtension(String extension) {
        boolean test = false;
        for (int i = 0; i < fileType.length; i++) {
            if (extension.contains(fileType[i])) {
                test = true;
                break;
            }
        }
        return test;
    }

    /**
     * reset mat hang da chon
     */
    @Secured("@")
    public void resetLstProductOfferingTotal() {
        productOfferingTotalDTO = null;
    }

    @Secured("@")
    public void onValidateConfirm() {
        if (DataUtil.isNullObject(fileUpContract)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.branch.attachFile.msg.require");
            topPage();
            return;
        }
    }

    /**
     * Lap yeu cau
     */
    @Secured("@")
    public void onCreateImportRequest() {
        try {
            BaseMessage messageFile = validateFileUploadWhiteList(fileUpContract, ALOW_EXTENSION_ALL_TYPE_LIST, MAX_SIZE_5M);
            if (!messageFile.isSuccess()) {
                LogicException ex = new LogicException(messageFile.getErrorCode(), messageFile.getKeyMsg());
                ex.setDescription(messageFile.getDescription());
                throw ex;
            }
            List<ImportPartnerDetailDTO> lstImportPartnerDtl = Lists.newArrayList();
            importPartnerDetailDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
            importPartnerDetailDTO.setProdOfferName(productOfferingTotalDTO.getName());
            lstImportPartnerDtl.add(importPartnerDetailDTO);
            importPartnerRequestDTO.setCreateShopId(staffDTO.getShopId());
            importPartnerRequestDTO.setCreateStaffId(staffDTO.getStaffId());
            importPartnerRequestDTO.setStatus(Const.LongSimpleItem.importPartnerRequestStatusCreate.getValue());
            importPartnerRequestDTO.setDocumentContent(byteContent);
            importPartnerRequestDTO.setDocumentName(attachFileName);
            importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDtl);
            BaseMessage message = importPartnerRequestService.saveImportPartnerRequest(importPartnerRequestDTO);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmCreateRequestImport:msgCreateRequestImport", getTextParam(getText("mn.stock.branch.create.success"), importPartnerRequestDTO.getRequestCode()));
            topPage();
            //doRefresh();
            productOfferingTotalDTO = null;
            doSetDefault();
            attachFileName = "";
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doRefresh() {
        try {
            importPartnerRequestDTO = new ImportPartnerRequestDTO();
            Date currentDate = getSysdateFromDB();
            importPartnerRequestDTO.setCreateDate(currentDate);
            importPartnerRequestDTO.setToOwnerId(staffDTO.getShopId());
            importPartnerRequestDTO.setToOwnerType(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            importPartnerRequestDTO.setRequestCode(importPartnerRequestService.getRequestCode()); // tu dong tang requestCode
            importPartnerDetailDTO = new ImportPartnerDetailDTO();
            productOfferingTotalDTO = null;
            lstProductOfferTypeDTO = productOfferTypeService.getListProduct();
            productOfferTypeDTO = lstProductOfferTypeDTO.get(0);
            attachFileName = "";
            importPartnerRequestDTO.setCurrencyType(Const.CURRENCY_TYPE.VND);
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            reportError("", e.getErrorCode(), e.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public Object getObjectController() {
        return objectController;
    }

    public void setObjectController(Object objectController) {
        this.objectController = objectController;
    }

    public ShopInfoNameable getShopInfoReceiveTag() {
        return shopInfoReceiveTag;
    }

    public void setShopInfoReceiveTag(ShopInfoNameable shopInfoReceiveTag) {
        this.shopInfoReceiveTag = shopInfoReceiveTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public String getRequestCodeAuto() {
        return requestCodeAuto;
    }

    public void setRequestCodeAuto(String requestCodeAuto) {
        this.requestCodeAuto = requestCodeAuto;
    }

    public ImportPartnerRequestDTO getImportPartnerRequestDTO() {
        return importPartnerRequestDTO;
    }

    public void setImportPartnerRequestDTO(ImportPartnerRequestDTO importPartnerRequestDTO) {
        this.importPartnerRequestDTO = importPartnerRequestDTO;
    }

    public ProductOfferTypeDTO getProductOfferTypeDTO() {
        return productOfferTypeDTO;
    }

    public void setProductOfferTypeDTO(ProductOfferTypeDTO productOfferTypeDTO) {
        this.productOfferTypeDTO = productOfferTypeDTO;
    }

    public List<ProductOfferTypeDTO> getLstProductOfferTypeDTO() {
        return lstProductOfferTypeDTO;
    }

    public void setLstProductOfferTypeDTO(List<ProductOfferTypeDTO> lstProductOfferTypeDTO) {
        this.lstProductOfferTypeDTO = lstProductOfferTypeDTO;
    }

    public ImportPartnerDetailDTO getImportPartnerDetailDTO() {
        return importPartnerDetailDTO;
    }

    public void setImportPartnerDetailDTO(ImportPartnerDetailDTO importPartnerDetailDTO) {
        this.importPartnerDetailDTO = importPartnerDetailDTO;
    }

    public PartnerDTO getPartnerDTO() {
        return partnerDTO;
    }

    public void setPartnerDTO(PartnerDTO partnerDTO) {
        this.partnerDTO = partnerDTO;
    }

    public List<PartnerDTO> getLstPartnerDTO() {
        return lstPartnerDTO;
    }

    public void setLstPartnerDTO(List<PartnerDTO> lstPartnerDTO) {
        this.lstPartnerDTO = lstPartnerDTO;
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

    public UploadedFile getFileUpContract() {
        return fileUpContract;
    }

    public void setFileUpContract(UploadedFile fileUpContract) {
        this.fileUpContract = fileUpContract;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public List<OptionSetValueDTO> getLstCurrencyType() {
        return lstCurrencyType;
    }

    public void setLstCurrencyType(List<OptionSetValueDTO> lstCurrencyType) {
        this.lstCurrencyType = lstCurrencyType;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

}

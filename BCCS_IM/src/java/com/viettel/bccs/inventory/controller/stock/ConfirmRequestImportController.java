package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.dto.PartnerDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.ImportPartnerDetailService;
import com.viettel.bccs.inventory.service.ImportPartnerRequestService;
import com.viettel.bccs.inventory.service.PartnerService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 *
 */
@Component
@Scope("view")
@ManagedBean
public class ConfirmRequestImportController extends TransCommonController {
    private boolean renderedDetail;
    private ImportPartnerRequestDTO requestDTO;
    private List<ImportPartnerRequestDTO> listImportPartnerRequestDTOs;
    private List<PartnerDTO> listPartnerDTOs;
    private ImportPartnerRequestDTO selectedImportPartnerRequestDTO;
    private List<ImportPartnerDetailDTO> detailDTO;

    @Autowired
    private ImportPartnerRequestService importPartnerRequestService;
    @Autowired
    private PartnerService partnerService;
    @Autowired
    ShopInfoNameable shopInfoNameable;

    @Autowired
    private ImportPartnerDetailService importPartnerDetailService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            renderedDetail = false;
            selectedImportPartnerRequestDTO = null;
            detailDTO = null;
            //
            shopInfoNameable.initShop(this, Const.VT_SHOP_ID, true);
            Date sysdate = optionSetValueService.getSysdateFromDB(true);
            listPartnerDTOs = partnerService.findPartner(new PartnerDTO());
            renderedDetail = false;
            requestDTO = new ImportPartnerRequestDTO();
            requestDTO.setFromDate(sysdate);
            requestDTO.setToDate(sysdate);
            requestDTO.setStatus(Const.LongSimpleItem.importPartnerRequestStatusCreate.getValue());
            listImportPartnerRequestDTOs = Lists.newArrayList();
            doSearchRequest();
            executeCommand("updateControls();");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }


    @Secured("@")
    public void doSearchRequest() {
        try {
            renderedDetail = false;
            validateDate(requestDTO.getFromDate(), requestDTO.getToDate());
            if (requestDTO.getRequestCode() != null) {
                requestDTO.setRequestCode(requestDTO.getRequestCode().trim());
            }
            listImportPartnerRequestDTOs = DataUtil.defaultIfNull(importPartnerRequestService.findImportPartnerRequest(requestDTO), Lists.newArrayList());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doApproveRequest(Long status) {
        try {
            ImportPartnerRequestDTO importPartnerRequestDTO = DataUtil.cloneBean(selectedImportPartnerRequestDTO);
            importPartnerRequestDTO.setStatus(status);
            importPartnerRequestDTO.setApproveStaffId(getStaffDTO().getStaffId());
            importPartnerRequestService.saveImportPartnerRequest(importPartnerRequestDTO);
            selectedImportPartnerRequestDTO.setStatus(status);
            selectedImportPartnerRequestDTO.setApproveStaffId(getStaffDTO().getStaffId());
            String key = "import.partner.request.reject";
            if (status.equals(1L)) {
                key = "limit.stock.approve";
            }
            topReportSuccess("", "import.partner.confirm.success", key);
            updateElemetId("frmImportPartner");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doValidate() {
        if (selectedImportPartnerRequestDTO != null) {
            if (selectedImportPartnerRequestDTO.getReason() == null) {
                topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.reason");
            } else {
                selectedImportPartnerRequestDTO.setReason(selectedImportPartnerRequestDTO.getReason().trim());
                try {
                    if (selectedImportPartnerRequestDTO.getReason().getBytes("UTF-8").length > 1000) {
                        topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.reason");
                    }
                } catch (Exception ex) {
                    topReportError("", "common.error.happened", ex);
                }
            }
        } else {
            topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.required.approve");
        }
    }

    @Secured("@")
    public void doSelectItem(ImportPartnerRequestDTO dto) {
        dto.setReason("");
        this.selectedImportPartnerRequestDTO = dto;
    }

    @Secured("@")
    public String getConfirmMessage(String mode) {
        try {
            if (selectedImportPartnerRequestDTO == null) {
                return "";
            }
            String key = "import.partner.confirm.reject";
            if (DataUtil.safeEqual(mode, "APPROVE")) {
                key = "import.partner.confim.approve";
            }
            return getMessage(key, selectedImportPartnerRequestDTO.getRequestCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    @Secured("@")
    public StreamedContent downloadAttackFile(ImportPartnerRequestDTO importPartnerRequestDTO) {
        try {
            byte[] content = importPartnerRequestService.getAttachFileContent(importPartnerRequestDTO.getImportPartnerRequestId());
            if (content == null || content.length == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.attach");
            }
            InputStream is = new ByteArrayInputStream(content);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent streamedContent = new DefaultStreamedContent(is, externalContext.getMimeType(importPartnerRequestDTO.getDocumentName()), importPartnerRequestDTO.getDocumentName());
            return streamedContent;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public void receiveUnit(VShopStaffDTO vShopStaffDTO) {
        try {
            requestDTO.setToOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    public void onDetail(ImportPartnerRequestDTO requestDTO) {
        try {
            if (requestDTO != null) {
                renderedDetail = true;
                selectedImportPartnerRequestDTO = requestDTO;
                ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
                importPartnerDetailDTO.setImportPartnerRequestId(selectedImportPartnerRequestDTO.getImportPartnerRequestId());
                detailDTO = DataUtil.defaultIfNull(importPartnerDetailService.findImportPartnerDetail(importPartnerDetailDTO), Lists.newArrayList());
            } else {
                renderedDetail = false;
                selectedImportPartnerRequestDTO = null;
                detailDTO = null;
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }

    }

    public void clearUnit() {
        requestDTO.setToOwnerId(null);
    }


    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isRenderedDetail() {
        return renderedDetail;
    }

    public void setRenderedDetail(boolean renderedDetail) {
        this.renderedDetail = renderedDetail;
    }

    public ImportPartnerRequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(ImportPartnerRequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    public ImportPartnerRequestService getImportPartnerRequestService() {
        return importPartnerRequestService;
    }

    public void setImportPartnerRequestService(ImportPartnerRequestService importPartnerRequestService) {
        this.importPartnerRequestService = importPartnerRequestService;
    }

    public List<ImportPartnerRequestDTO> getListImportPartnerRequestDTOs() {
        return listImportPartnerRequestDTOs;
    }

    public void setListImportPartnerRequestDTOs(List<ImportPartnerRequestDTO> listImportPartnerRequestDTOs) {
        this.listImportPartnerRequestDTOs = listImportPartnerRequestDTOs;
    }

    public List<PartnerDTO> getListPartnerDTOs() {
        return listPartnerDTOs;
    }

    public void setListPartnerDTOs(List<PartnerDTO> listPartnerDTOs) {
        this.listPartnerDTOs = listPartnerDTOs;
    }

    public ShopInfoNameable getShopInfoNameable() {
        return shopInfoNameable;
    }

    public void setShopInfoNameable(ShopInfoNameable shopInfoNameable) {
        this.shopInfoNameable = shopInfoNameable;
    }

    public ImportPartnerRequestDTO getSelectedImportPartnerRequestDTO() {
        return selectedImportPartnerRequestDTO;
    }

    public void setSelectedImportPartnerRequestDTO(ImportPartnerRequestDTO selectedImportPartnerRequestDTO) {
        this.selectedImportPartnerRequestDTO = selectedImportPartnerRequestDTO;
    }

    public List<ImportPartnerDetailDTO> getDetailDTO() {
        return detailDTO;
    }

    public void setDetailDTO(List<ImportPartnerDetailDTO> detailDTO) {
        this.detailDTO = detailDTO;
    }
}

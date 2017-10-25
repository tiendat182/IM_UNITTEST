package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.RequestLiquidateDetailService;
import com.viettel.bccs.inventory.service.RequestLiquidateSerialService;
import com.viettel.bccs.inventory.service.RequestLiquidateService;
import com.viettel.bccs.inventory.service.StockTransSerialService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tuannm33 on 2/16/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "liquidateApproveController")
public class LiquidateApproveController extends TransCommonController {

    private boolean renderDetail;
    private int limitAutoComplete;
    private StaffDTO staffDTO;
    private RequestLiquidateDTO requestLiquidateDTO;
    private RequestLiquidateDTO selectedRequestLiquidateDTO;
    private List<RequestLiquidateDTO> lstRequestLiquidateDTO;
    private List<RequestLiquidateDetailDTO> lstRequestLiquidateDetailDTO;
    private List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO;
    private List<OptionSetValueDTO> lstProdState;

    @Autowired
    private ShopInfoNameable shopProposeTag;
    @Autowired
    private RequestLiquidateService requestLiquidateService;
    @Autowired
    private RequestLiquidateDetailService requestLiquidateDetailService;
    @Autowired
    private RequestLiquidateSerialService requestLiquidateSerialService;
    @Autowired
    private StockTransSerialService stockTransSerialService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            doSetDefault();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSetDefault() {
        try {
            renderDetail = false;
            selectedRequestLiquidateDTO = null;
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            //shopProposeTag.initShop(this, Const.VT_SHOP_ID, true);
            shopProposeTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            lstProdState = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_STATE);
            //
            requestLiquidateDTO = new RequestLiquidateDTO();
            Date currentDate = getSysdateFromDB();
            requestLiquidateDTO.setFromDate(currentDate);
            requestLiquidateDTO.setToDate(currentDate);
            requestLiquidateDTO.setStatus(Const.LIQUIDATE_STATUS.NEW);
            requestLiquidateDTO.setApproveStaffId(staffDTO.getStaffId());
            lstRequestLiquidateDTO = Lists.newArrayList();
            lstRequestLiquidateDetailDTO = Lists.newArrayList();
            lstRequestLiquidateSerialDTO = Lists.newArrayList();
            doSearch();
            executeCommand("updateControls();");
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doSearch() {
        try {
            renderDetail = false;
            validateDate(requestLiquidateDTO.getFromDate(), requestLiquidateDTO.getToDate());
            lstRequestLiquidateDTO = requestLiquidateService.getLstRequestLiquidateDTO(requestLiquidateDTO);
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public StreamedContent downloadAttachFile(RequestLiquidateDTO rqLiquidateDTO) {
        try {
            byte[] content = requestLiquidateService.getAttachFileContent(rqLiquidateDTO.getRequestLiquidateId());
            if (content == null || content.length == 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.liquidate.file.msg.not.found");
            }
            InputStream is = new ByteArrayInputStream(content);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent streamedContent = new DefaultStreamedContent(is, externalContext.getMimeType(rqLiquidateDTO.getDocumentName()), rqLiquidateDTO.getDocumentName());
            return streamedContent;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doSelectItem(RequestLiquidateDTO dto) {
        try {
            List<RequestLiquidateDetailDTO> lstDetailDTO = requestLiquidateDetailService.getLstRequestLiquidateDetailDTO(dto.getRequestLiquidateId());
            List<RequestLiquidateDetailDTO> lstRequestLiquidateDetailDTO = Lists.newArrayList();
            for (RequestLiquidateDetailDTO dtlDTO : lstDetailDTO) {
                List<RequestLiquidateSerialDTO> lstReqLiquidateSerial = requestLiquidateSerialService.getLstRequestLiquidateSerialDTO(dtlDTO.getRequestLiquidateDetailId());
                RequestLiquidateDetailDTO requestDetail = new RequestLiquidateDetailDTO();
                requestDetail.setProdOfferId(dtlDTO.getProdOfferId());
                requestDetail.setStateId(dtlDTO.getStateId());
                requestDetail.setQuantity(dtlDTO.getQuantity());
                requestDetail.setLstRequestLiquidateSerialDTO(lstReqLiquidateSerial);
                lstRequestLiquidateDetailDTO.add(requestDetail);
            }
            dto.setLstRequestLiquidateDetailDTO(lstRequestLiquidateDetailDTO);
            dto.setRejectNote("");
            this.selectedRequestLiquidateDTO = dto;
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public String getConfirmMessage(String impactType) {
        try {
            if (DataUtil.isNullObject(selectedRequestLiquidateDTO)) {
                return "";
            }
            String key = "mn.stock.liquidate.approve.confirm.nok";
            if (DataUtil.safeEqual(impactType, "APPROVE")) {
                key = "mn.stock.liquidate.approve.confirm.ok";
            }
            return getMessage(key, selectedRequestLiquidateDTO.getRequestCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    @Secured("@")
    public void doApproveRequest() {
        try {
            RequestLiquidateDTO rqLiquidateDTO = DataUtil.cloneBean(selectedRequestLiquidateDTO);
            rqLiquidateDTO.setStatus(Const.LIQUIDATE_STATUS.APPROVE);
            rqLiquidateDTO.setApproveStaffId(getStaffDTO().getStaffId());
            requestLiquidateService.updateRequest(rqLiquidateDTO);
            selectedRequestLiquidateDTO.setStatus(Const.LIQUIDATE_STATUS.APPROVE);
            selectedRequestLiquidateDTO.setApproveStaffId(getStaffDTO().getStaffId());
            reportSuccess("frmLiquidateApprove:msgLiquidateApprove", getTextParam(getText("mn.stock.liquidate.approve.success"), rqLiquidateDTO.getRequestCode()));
            topPage();
            updateElemetId("frmLiquidateApprove");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doValidate() {
        try {
            if (DataUtil.isNullObject(selectedRequestLiquidateDTO)) {
                topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "reason.reject.validate");
            } else {
                if (DataUtil.isNullOrEmpty(selectedRequestLiquidateDTO.getRejectNote())) {
                    topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "reason.reject.validate");
                } else {
                    if (selectedRequestLiquidateDTO.getRejectNote().getBytes("UTF-8").length > 1000) {
                        topReportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "reason.reject.validate");
                    }
                }
            }
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doRejectRequest() {
        try {
            RequestLiquidateDTO rqLiquidateDTO = DataUtil.cloneBean(selectedRequestLiquidateDTO);
            rqLiquidateDTO.setStatus(Const.LIQUIDATE_STATUS.REJECT);
            rqLiquidateDTO.setApproveStaffId(getStaffDTO().getStaffId());
            requestLiquidateService.updateRequest(rqLiquidateDTO);
            reportSuccess("frmLiquidateApprove:msgLiquidateApprove", getTextParam(getText("mn.stock.liquidate.approve.reject"), rqLiquidateDTO.getRequestCode()));
            selectedRequestLiquidateDTO.setStatus(Const.LIQUIDATE_STATUS.REJECT);
            selectedRequestLiquidateDTO.setApproveStaffId(getStaffDTO().getStaffId());
            topPage();
            updateElemetId("frmLiquidateApprove");
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doShowDetail(RequestLiquidateDTO requestLiquidate) {
        try {
            if (DataUtil.isNullObject(requestLiquidate)) {
                renderDetail = false;
            } else {
                selectedRequestLiquidateDTO = requestLiquidate;
                lstRequestLiquidateDetailDTO = requestLiquidateDetailService.getLstRequestLiquidateDetailDTO(requestLiquidate.getRequestLiquidateId());
                if (DataUtil.isNullOrEmpty(lstRequestLiquidateDetailDTO)) {
                    renderDetail = false;
                    topReportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.liquidate.approve.detail.empty");
                } else {
                    renderDetail = true;
                }
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public String getStateName(Long stateId) {
        String str = "";
        if (!DataUtil.isNullObject(stateId)) {
            for (OptionSetValueDTO ops : lstProdState) {
                if (DataUtil.safeEqual(ops.getValue(), stateId.toString())) {
                    str = ops.getName();
                    break;
                }
            }
        }
        return str;
    }

    @Secured("@")
    public void showDialogSerial(Long requestLiquidateDetailId) {
        try {
            lstRequestLiquidateSerialDTO = DataUtil.defaultIfNull(requestLiquidateSerialService.getLstRequestLiquidateSerialDTO(requestLiquidateDetailId), new ArrayList<>());
            if (DataUtil.isNullOrEmpty(lstRequestLiquidateSerialDTO)) {
                topReportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.liquidate.model.serial.empty");
            }
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doReset() {
        doSetDefault();
    }

    @Secured("@")
    public void doProposeStock(VShopStaffDTO vShopStaffDTO) {
        try {
            requestLiquidateDTO.setCreateShopId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void clearStock() {
        requestLiquidateDTO.setCreateShopId(null);
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public ShopInfoNameable getShopProposeTag() {
        return shopProposeTag;
    }

    public void setShopProposeTag(ShopInfoNameable shopProposeTag) {
        this.shopProposeTag = shopProposeTag;
    }

    public RequestLiquidateDTO getRequestLiquidateDTO() {
        return requestLiquidateDTO;
    }

    public void setRequestLiquidateDTO(RequestLiquidateDTO requestLiquidateDTO) {
        this.requestLiquidateDTO = requestLiquidateDTO;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public List<RequestLiquidateDTO> getLstRequestLiquidateDTO() {
        return lstRequestLiquidateDTO;
    }

    public void setLstRequestLiquidateDTO(List<RequestLiquidateDTO> lstRequestLiquidateDTO) {
        this.lstRequestLiquidateDTO = lstRequestLiquidateDTO;
    }

    public RequestLiquidateDTO getSelectedRequestLiquidateDTO() {
        return selectedRequestLiquidateDTO;
    }

    public void setSelectedRequestLiquidateDTO(RequestLiquidateDTO selectedRequestLiquidateDTO) {
        this.selectedRequestLiquidateDTO = selectedRequestLiquidateDTO;
    }

    public boolean isRenderDetail() {
        return renderDetail;
    }

    public void setRenderDetail(boolean renderDetail) {
        this.renderDetail = renderDetail;
    }

    public List<RequestLiquidateDetailDTO> getLstRequestLiquidateDetailDTO() {
        return lstRequestLiquidateDetailDTO;
    }

    public void setLstRequestLiquidateDetailDTO(List<RequestLiquidateDetailDTO> lstRequestLiquidateDetailDTO) {
        this.lstRequestLiquidateDetailDTO = lstRequestLiquidateDetailDTO;
    }

    public List<RequestLiquidateSerialDTO> getLstRequestLiquidateSerialDTO() {
        return lstRequestLiquidateSerialDTO;
    }

    public void setLstRequestLiquidateSerialDTO(List<RequestLiquidateSerialDTO> lstRequestLiquidateSerialDTO) {
        this.lstRequestLiquidateSerialDTO = lstRequestLiquidateSerialDTO;
    }
}

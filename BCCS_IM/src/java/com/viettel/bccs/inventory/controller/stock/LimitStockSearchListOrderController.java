package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import com.viettel.bccs.inventory.dto.DebitRequestDetailDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.message.DebitRequestResponse;
import com.viettel.bccs.inventory.service.DebitRequestService;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
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
 * @author:thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class LimitStockSearchListOrderController extends InventoryController {

    //<editor-fold defaultstate="collapsed" desc="Khai bao bien">
    @Autowired
    private DebitRequestService debitRequestService;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    private List<DebitRequestDTO> debitRequestDTOs;
    private List<DebitRequestDetailDTO> debitRequestDetailDTOs;
    private List<VShopStaffDTO> staffDTOs;
    private DebitRequestDTO debitRequestDTO;
    private boolean limitStockUnit;
    private Boolean viewDetail = false;
    private boolean approvable = true; // Phân quyền
    private boolean deletable = true; //Phân quyền
    private DebitRequestDTO selectedDebitRequestDTO;
    private boolean defaultApproved = true;

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Get-set">


    public boolean getDefaultApproved() {
        return defaultApproved;
    }

    public void setDefaultApproved(boolean defaultApproved) {
        this.defaultApproved = defaultApproved;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public List<VShopStaffDTO> getStaffDTOs() {
        return staffDTOs;
    }

    public void setStaffDTOs(List<VShopStaffDTO> staffDTOs) {
        this.staffDTOs = staffDTOs;
    }

    public List<DebitRequestDTO> getDebitRequestDTOs() {
        return debitRequestDTOs;
    }

    public void setDebitRequestDTOs(List<DebitRequestDTO> debitRequestDTOs) {
        this.debitRequestDTOs = debitRequestDTOs;
    }

    public List<DebitRequestDetailDTO> getDebitRequestDetailDTOs() {
        return debitRequestDetailDTOs;
    }

    public void setDebitRequestDetailDTOs(List<DebitRequestDetailDTO> debitRequestDetailDTOs) {
        this.debitRequestDetailDTOs = debitRequestDetailDTOs;
    }

    public DebitRequestDTO getDebitRequestDTO() {
        return debitRequestDTO;
    }

    public void setDebitRequestDTO(DebitRequestDTO debitRequestDTO) {
        this.debitRequestDTO = debitRequestDTO;
    }

    public boolean isLimitStockUnit() {
        return limitStockUnit;
    }

    public void setLimitStockUnit(boolean limitStockUnit) {
        this.limitStockUnit = limitStockUnit;
    }

    public Boolean getViewDetail() {
        return viewDetail;
    }

    public void setViewDetail(Boolean viewDetail) {
        this.viewDetail = viewDetail;
    }

    public boolean isApprovable() {
        return approvable;
    }

    public void setApprovable(boolean approvable) {
        this.approvable = approvable;
    }

    public DebitRequestDTO getSelectedDebitRequestDTO() {
        return selectedDebitRequestDTO;
    }

    public void setSelectedDebitRequestDTO(DebitRequestDTO selectedDebitRequestDTO) {
        this.selectedDebitRequestDTO = selectedDebitRequestDTO;
    }
    //</editor-fold>

    @Secured("@")
    @PostConstruct
    public void init() {
        setDefault();
    }

    private void setDefault() {
        try {
            defaultApproved = true;
            Date sys = optionSetValueService.getSysdateFromDB(true);
            viewDetail = false;
            selectedDebitRequestDTO = null;
            approvable = authenticate(Const.IM_COMPONENT.STOCK_LIMIT_APPROVE);
            deletable = authenticate(Const.IM_COMPONENT.STOCK_LIMIT_DELETE);
            staffInfoTag.initStaff(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString());
            debitRequestDTOs = Lists.newArrayList();
            debitRequestDetailDTOs = Lists.newArrayList();
            debitRequestDTO = new DebitRequestDTO();
            debitRequestDTO.setToDate(sys);
            debitRequestDTO.setFromDate(sys);
            debitRequestDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED);
            debitRequestDTO.setRequestObjectType(Const.OWNER_TYPE.SHOP);
            debitRequestDTO.setCurrentShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            if (!approvable) {
                debitRequestDTO.setCreateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            }
            debitRequestDTOs = debitRequestService.findDebitRequest(debitRequestDTO);
            debitRequestDTO.setCreateUser(null);
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void doSearch() {
        try {
            defaultApproved = true;
            Date sys = optionSetValueService.getSysdateFromDB(true);
            if (debitRequestDTO.getFromDate() != null) {
                if (debitRequestDTO.getFromDate().after(sys)) {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.valid");
                    topPage();
                    return;
                }
                if (debitRequestDTO.getToDate() != null) {
                    if (debitRequestDTO.getToDate().before(debitRequestDTO.getFromDate())) {
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
                        topPage();
                        return;
                    }
                    if (debitRequestDTO.getToDate().getTime() - debitRequestDTO.getFromDate().getTime() > Const.MONTH_IN_MILLISECOND) {
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.to.valid");
                        topPage();
                        return;
                    }
                    viewDetail = false;
                    selectedDebitRequestDTO = null;
                    debitRequestDTO.setCurrentShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
                    debitRequestDTOs = debitRequestService.findDebitRequest(debitRequestDTO);
                } else {
                    reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
                    topPage();
                    return;
                }
            } else {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
                topPage();
                return;
            }
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * Ham xem chi tiet
     *
     * @author ThanhNT
     */
    @Secured("@")
    public void onClickDetail(boolean isView, DebitRequestDTO cDebitRequestDTO) {
        try {
            defaultApproved = true;
            viewDetail = isView;
            debitRequestDetailDTOs = null;
            if (isView) {
                try {
                    selectedDebitRequestDTO = cDebitRequestDTO;
                    debitRequestDetailDTOs = DataUtil.defaultIfNull(debitRequestService.findDebitRequestDetailByDebitRequest(cDebitRequestDTO), new ArrayList<DebitRequestDetailDTO>());
                    if (cDebitRequestDTO.getStatus().equals(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED)) {
                        for (DebitRequestDetailDTO detailDTO : debitRequestDetailDTOs) {
                            detailDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED);
                        }
                    }
                    limitStockUnit = cDebitRequestDTO.getRequestObjectType().trim().equals(Const.OWNER_TYPE.SHOP);
                } catch (Exception ex) {
                    reportError("", "common.error.happened", ex);
                    topPage();
                }
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void onDelDebitRequest() {
        try {
            selectedDebitRequestDTO.setCurrentStaff(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            BaseMessage msg = debitRequestService.deleteDebitRequest(selectedDebitRequestDTO);
            if (DataUtil.isNullOrEmpty(msg.getErrorCode())) {
                selectedDebitRequestDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_REJECT);
                if (!DataUtil.isNullOrEmpty(debitRequestDetailDTOs)) {
                    for (DebitRequestDetailDTO detailDTO : debitRequestDetailDTOs) {
                        detailDTO.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_REJECT);
                    }
                }
                reportSuccess("", "limit.stock.deleteSuccess", selectedDebitRequestDTO.getRequestCode());
            } else {
                reportError("", msg.getErrorCode(), msg.getKeyMsg());
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        topPage();
    }

    @Secured("@")
    public void validateSelected(DebitRequestDTO checkDTO) {
        onClickDetail(true, checkDTO);
        if (DataUtil.isNullObject(selectedDebitRequestDTO)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "limit.stock.approveOne");
            topPage();
        } else {
            if (DataUtil.isNullObject(selectedDebitRequestDTO.getRequestId())) {
                reportError("", "", "common.error.happened");
                topPage();
            } else {
                if (DataUtil.isNullOrEmpty(checkDTO.getStatus()) || !selectedDebitRequestDTO.getStatus().equals(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED)) {
                    reportError("", "", "common.error.happened");
                    topPage();
                }
            }
        }
    }

    @Secured("@")
    public void onApproveRequest() {
        if (DataUtil.isNullObject(selectedDebitRequestDTO)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "limit.stock.approveOne");
            return;
        }
        try {
            selectedDebitRequestDTO.setDebitDebitRequestDetails(debitRequestDetailDTOs);
            selectedDebitRequestDTO.setCurrentStaff(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            DebitRequestResponse response = debitRequestService.approveDebitRequest(selectedDebitRequestDTO);
            if (DataUtil.isNullOrEmpty(response.getErrorCode())) {
                selectedDebitRequestDTO = response.getDebitRequestDTO();
                if (!DataUtil.isNullObject(selectedDebitRequestDTO)) {
                    for (DebitRequestDTO check : debitRequestDTOs) {
                        if (DataUtil.safeToLong(check.getRequestId()).equals(DataUtil.safeToLong(selectedDebitRequestDTO.getRequestId()))) {
                            check.setStatus(selectedDebitRequestDTO.getStatus());
                            break;
                        }
                    }
                }
                reportSuccess("", "limit.stock.approveOk", selectedDebitRequestDTO.getRequestCode());
            } else {
                reportError("", response.getErrorCode(), response.getKeyMsg());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            try {
                debitRequestDetailDTOs = debitRequestService.findDebitRequestDetailByDebitRequest(selectedDebitRequestDTO);
            } catch (Exception e) {
                reportError("", "common.error.happened", e);
            }
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        topPage();
    }

    @Secured("@")
    public StreamedContent downloadFileAttach(DebitRequestDTO cDebitRequestDTO) {
        try {
            byte[] content = debitRequestService.getAttachFileContent(cDebitRequestDTO.getRequestId());
            InputStream is = new ByteArrayInputStream(content);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            StreamedContent streamedContent = new DefaultStreamedContent(is, externalContext.getMimeType(cDebitRequestDTO.getFileName().trim()), cDebitRequestDTO.getFileName().trim());
            return streamedContent;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doReset() {
        setDefault();
    }

    @Secured("@")
    public void staffChosed(VShopStaffDTO vShopStaffDTO) {
        debitRequestDTO.setCreateUser(vShopStaffDTO.getOwnerCode());
    }

    public void staffClear() {
        debitRequestDTO.setCreateUser(null);
    }

    @Secured("@")
    public void onChangeDefaultAction() {
        String defaultStatus = Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED;
        if (!defaultApproved) {
            defaultStatus = Const.DEBIT_REQUEST_STATUS.STATUS_REJECT;
        }
        if (!DataUtil.isNullObject(selectedDebitRequestDTO) && !DataUtil.isNullOrEmpty(debitRequestDetailDTOs)) {
            for (DebitRequestDetailDTO detail : debitRequestDetailDTOs) {
                detail.setStatus(defaultStatus);
            }
        }
    }

    @Secured("@")
    public void onChangeStatus() {
        defaultApproved = true;
        if (!DataUtil.isNullOrEmpty(debitRequestDetailDTOs)) {
            for (DebitRequestDetailDTO detail : debitRequestDetailDTOs) {
                if (DataUtil.isNullOrEmpty(detail.getStatus())) {
                    detail.setStatus(Const.DEBIT_REQUEST_STATUS.STATUS_APPROVED);
                } else {
                    if (detail.getStatus().equals(Const.DEBIT_REQUEST_STATUS.STATUS_REJECT)) {
                        defaultApproved = false;
                        break;
                    }
                }
            }
        }
    }

    @Secured("@")
    public boolean canApprove() {
        if (!approvable || DataUtil.isNullObject(selectedDebitRequestDTO) || !selectedDebitRequestDTO.getStatus().equals(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED)) {
            return false;
        }
        return true;
    }

    @Secured("@")
    public boolean canDelete(DebitRequestDTO checkDTO) {
        if (!DataUtil.isNullObject(checkDTO)
                && !DataUtil.isNullOrEmpty(checkDTO.getStatus())
                && checkDTO.getStatus().equals(Const.DEBIT_REQUEST_STATUS.STATUS_CREATED)) {
            if (deletable) {
                return true;
            }
            if (!DataUtil.isNullOrEmpty(checkDTO.getCreateUser()) && checkDTO.getCreateUser().toLowerCase().equals(getStaffDTO().getStaffCode().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}

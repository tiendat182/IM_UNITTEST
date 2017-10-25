package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luannt23 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class StaffConfirmImportController extends TransCommonController {

    //<editor-fold desc="Khai báo" defaultstate="collapsed">
    @Autowired
    private ShopService shopService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private StockTransService transService;

    @Autowired
    private StockTransSerialService serialService;

    @Autowired
    private ExecuteStockTransService executeStockTransService;

    @Autowired
    private ListProductNameable listProductTag;

    private RequiredRoleMap requiredRoleMap;
    private VStockTransDTO searchStockTrans;
    private boolean infoOrderDetail;
    List<VStockTransDTO> stockTransDTOs;
    private List<StockTransFullDTO> stockTransDetails;
    private List<StockTransSerialDTO> stockTransSerials;
    private VStockTransDTO selectedStockTrans;
    private StockTransFullDTO selectedStockTransDetail;
    private VStockTransDTO handleControls;
    private List<ReasonDTO> reasons;
    //</editor-fold>

    @PostConstruct
    public void init() {
        requiredRoleMap = getTransRequiRedRoleMap();
        initControls();
    }

    private void initControls() {
        try {
            //khoi tao cac
            stockTransDetails = Lists.newArrayList();
            stockTransDTOs = Lists.newArrayList();
            stockTransSerials = Lists.newArrayList();
            reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_IMP_STAFF), new ArrayList<ReasonDTO>());
            //khoi tao doi tuong tim kiem
            searchStockTrans = new VStockTransDTO();
            searchStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            searchStockTrans.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            searchStockTrans.setStartDate(getSysdateFromDB());
            searchStockTrans.setEndDate(getSysdateFromDB());
            searchStockTrans.setToOwnerID(getStaffDTO().getStaffId());
            searchStockTrans.setFromOwnerID(getStaffDTO().getShopId());
            searchStockTrans.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            searchStockTrans.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            searchStockTrans.setToOwnerName(getStaffDTO().getName());
            searchStockTrans.setFromOwnerName(getStaffDTO().getShopName());
            //update lai controls
            executeCommand("updateControls");
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    public void initHandle() throws LogicException, Exception {
        handleControls = new VStockTransDTO();
        handleControls.setNote(null);
        handleControls.setCreateDateTime(getSysdateFromDB());
        handleControls.setFromOwnerID(getStaffDTO().getShopId());
        handleControls.setFromOwnerName(getStaffDTO().getShopName());
        handleControls.setToOwnerID(getStaffDTO().getStaffId());
        handleControls.setToOwnerName(getStaffDTO().getName());
        handleControls.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, getStaffDTO()));
    }

    @Secured("@")
    public void showInfoOrderDetail(VStockTransDTO selected) {
        if (DataUtil.isNullObject(selected)) {
            this.infoOrderDetail = false;
        } else {
            selectedStockTrans = selected;
            try {
                List<Long> stockTransActionIds = Lists.newArrayList();
                stockTransActionIds.add(selected.getActionID());
                stockTransDetails = DataUtil.defaultIfNull(transService.searchStockTransDetail(stockTransActionIds), new ArrayList<StockTransFullDTO>());
                this.infoOrderDetail = true;
                ConfigListProductTagDTO conifg = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, selected.getFromOwnerID(), Const.OWNER_TYPE.SHOP);
                conifg.setShowSerialView(true);
                listProductTag.load(this, selected.getActionID(), conifg);
                initHandle();
                executeCommand("updateControls()");
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
    }

    @Secured("@")
    public void doBackPage() {
        this.infoOrderDetail = false;
        doSearch();
    }

    @Secured("@")
    public void doSearch() {
        try {
            validateDate(searchStockTrans.getStartDate(), searchStockTrans.getEndDate());

            if (!DataUtil.isNullOrEmpty(searchStockTrans.getActionCode())) {
                searchStockTrans.setActionCode(searchStockTrans.getActionCode().trim());
            }
            VStockTransDTO searcher = DataUtil.cloneBean(searchStockTrans);
            if (searchStockTrans.getStockTransStatus() != null && searchStockTrans.getStockTransStatus().contains(",")) {
                searcher.setStockTransStatus(null);
                String[] splits = searchStockTrans.getStockTransStatus().split(",");
                for (String status : splits) {
                    searcher.getLstStatus().add(status.trim());
                }
            }
            stockTransDTOs = transService.searchVStockTrans(searcher);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doCreateNote() {
        try {
            StockTransActionDTO stockTransActionDTO = createStockTransAction();
            //
            try {
                doValidateActionCode(handleControls.getActionCode());
            } catch (LogicException ex) {
                logger.error(ex.getMessage(), ex);
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "code.import.note.valid");
            }
            //
            if (!DataUtil.isNullOrEmpty(handleControls.getNote())
                    && handleControls.getNote().getBytes("UTF-8").length > 500) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.note.validate.maxlength");
            }
            //
            List<StockTransDetailDTO> lsStockTransDetail = Lists.newArrayList();
//            StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
//            for (StockTransFullDTO stockTransFullDTO : stockTransDetails) {
//                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
//                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
//                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
//                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
//                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
//                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
//
//                if ((Const.PRODUCT_OFFERING.CHECK_SERIAL.toString()).equals(stockTransFullDTO.getCheckSerial())) {
//                    stockTransSerialDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
//                    List<StockTransSerialDTO> lstSerial = serialService.findStockTransSerialByDTO(stockTransSerialDTO);
//                    stockTransDetailDTO.setLstStockTransSerial(lstSerial);
//                    stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
//                }
//                lsStockTransDetail.add(stockTransDetailDTO);
//            }
            StockTransDTO updateStockTrans = genStockTransDTO(selectedStockTrans);
            updateStockTrans.setImportReasonId(handleControls.getReasonID());
            updateStockTrans.setImportNote(handleControls.getNote());
            //hoangnt: check neu NV chua co tren TTNS thi khong cho xac nhan nhap hang
            StaffDTO staffDTO = staffService.findOne(updateStockTrans.getToOwnerId());
            if (DataUtil.isNullOrEmpty(staffDTO.getTtnsCode())) {
                throw new LogicException("", "export.order.staff.ttns.not.found");
            }
            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT, Const.STOCK_TRANS_TYPE.STAFF_IMP, updateStockTrans, stockTransActionDTO, lsStockTransDetail, requiredRoleMap);
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                topReportSuccess("", "stock.trans.import.success");
                selectedStockTrans.setActionID(message.getStockTransActionId());
                selectedStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            } else {
//                topReportError("", message.getErrorCode(), message.getKeyMsg());
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }

        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private StockTransActionDTO createStockTransAction() {
        StockTransActionDTO action = new StockTransActionDTO();
        action.setActionCode(handleControls.getActionCode());
        action.setStockTransId(selectedStockTrans.getActionID());
        action.setNote(handleControls.getNote());
        action.setCreateDatetime(getSysdateFromDB());
        action.setStatus(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
        action.setActionStaffId(getStaffDTO().getStaffId());
        action.setCreateUser(getStaffDTO().getStaffCode());
        return action;
    }

    public StreamedContent doPrintOrder() {
        try {
            StockTransDTO forPrint = genStockTransDTO(selectedStockTrans);
            forPrint.setActionCode(handleControls.getActionCode());
//            forPrint.setStockTransActionId(null);
            forPrint.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            forPrint.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            forPrint.setReasonName(handleControls.getReasonName());
            return exportStockTransDetail(forPrint, stockTransDetails);
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public void changeReason() {
        if (!DataUtil.isNullOrZero(handleControls.getReasonID()) && !DataUtil.isNullOrEmpty(reasons)) {
            for (ReasonDTO reason : reasons) {
                if (reason.getReasonId().equals(handleControls.getReasonID())) {
                    handleControls.setReasonName(reason.getReasonName());
                }
            }
        }
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public List<ReasonDTO> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonDTO> reasons) {
        this.reasons = reasons;
    }

    public VStockTransDTO getHandleControls() {
        return handleControls;
    }

    public void setHandleControls(VStockTransDTO handleControls) {
        this.handleControls = handleControls;
    }

    public List<StockTransSerialDTO> getStockTransSerials() {
        return stockTransSerials;
    }

    public List<VStockTransDTO> getStockTransDTOs() {
        return stockTransDTOs;
    }

    public void setStockTransDTOs(List<VStockTransDTO> stockTransDTOs) {
        this.stockTransDTOs = stockTransDTOs;
    }

    public void setStockTransSerials(List<StockTransSerialDTO> stockTransSerials) {
        this.stockTransSerials = stockTransSerials;
    }

    public VStockTransDTO getSelectedStockTrans() {
        return selectedStockTrans;
    }

    public void setSelectedStockTrans(VStockTransDTO selectedStockTrans) {
        this.selectedStockTrans = selectedStockTrans;
    }

    public StockTransFullDTO getSelectedStockTransDetail() {
        return selectedStockTransDetail;
    }

    public void setSelectedStockTransDetail(StockTransFullDTO selectedStockTransDetail) {
        this.selectedStockTransDetail = selectedStockTransDetail;
    }

    public List<StockTransFullDTO> getStockTransDetails() {
        return stockTransDetails;
    }

    public void setStockTransDetails(List<StockTransFullDTO> stockTransDetails) {
        this.stockTransDetails = stockTransDetails;
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public VStockTransDTO getSearchStockTrans() {
        return searchStockTrans;
    }

    public void setSearchStockTrans(VStockTransDTO searchStockTrans) {
        this.searchStockTrans = searchStockTrans;
    }

}

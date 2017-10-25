package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.impl.StaffInfoTag;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
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
public class StaffImportStockController extends TransCommonController {
    //<editor-fold defaultstate="collapsed" desc="Khai bao">
    @Autowired
    private StockTransService transService;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @Autowired
    private ExecuteStockTransService executeStockTransService;

    @Autowired
    private StaffInfoTag staffInfoTag;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private ListProductNameable listProductTag;

    private boolean infoOrderDetail;

    VStockTransDTO searchStockTrans;
    VStockTransDTO selectedStockTrans;
    List<VStockTransDTO> vStockTransDTOS;
    private List<StockTransFullDTO> stockTransDetails;
    private List<StockTransSerialDTO> stockTransSerials;
    private ConfigListProductTagDTO configTag;
    private Long stockTransActionId;

    @PostConstruct
    public void init() {
        initControls();
    }

    private void initControls() {
        try {
            stockTransActionId = null;
            configTag = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, getStaffDTO().getStaffId(), Const.OWNER_TYPE.STAFF);
            configTag.setShowSerialView(true);
            //
            List<String> channelTypes = commonService.getChannelTypes(Const.OWNER_TYPE.STAFF);
            staffInfoTag.initStaffWithChanelTypes(this, DataUtil.safeToString(getStaffDTO().getShopId()), null, channelTypes, false);
            //
            vStockTransDTOS = Lists.newArrayList();
            stockTransDetails = Lists.newArrayList();
            stockTransSerials = Lists.newArrayList();
            //khoi tao doi tuong tim kiem
            searchStockTrans = new VStockTransDTO();
            searchStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            searchStockTrans.setStartDate(getSysdateFromDB());
            searchStockTrans.setEndDate(getSysdateFromDB());
            //fix to
            searchStockTrans.setToOwnerID(getStaffDTO().getShopId());
            searchStockTrans.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            searchStockTrans.setToOwnerName(getStaffDTO().getShopName());
            searchStockTrans.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            //update lai controls
            executeCommand("updateControls()");
            showDialog("guide");
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }


    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public List<StockTransFullDTO> getStockTransDetails() {
        return stockTransDetails;
    }

    public void setStockTransDetails(List<StockTransFullDTO> stockTransDetails) {
        this.stockTransDetails = stockTransDetails;
    }

    public List<StockTransSerialDTO> getStockTransSerials() {
        return stockTransSerials;
    }

    public void setStockTransSerials(List<StockTransSerialDTO> stockTransSerials) {
        this.stockTransSerials = stockTransSerials;
    }

    public List<VStockTransDTO> getvStockTransDTOS() {
        return vStockTransDTOS;
    }

    public void setvStockTransDTOS(List<VStockTransDTO> vStockTransDTOS) {
        this.vStockTransDTOS = vStockTransDTOS;
    }

    @Secured("@")
    public VStockTransDTO getSelectedStockTrans() {
        return selectedStockTrans;
    }

    public void setSelectedStockTrans(VStockTransDTO selectedStockTrans) {
        this.selectedStockTrans = selectedStockTrans;
    }

    public VStockTransDTO getSearchStockTrans() {
        return searchStockTrans;
    }

    public void setSearchStockTrans(VStockTransDTO searchStockTrans) {
        this.searchStockTrans = searchStockTrans;
    }

    @Secured("@")
    public void showInfoOrderDetail(VStockTransDTO selectedStockTrans) {
        if (DataUtil.isNullObject(selectedStockTrans)) {
            infoOrderDetail = false;
            this.selectedStockTrans = null;
            stockTransActionId = null;
        } else {
            try {
                StockTransActionDTO checkVoffice = stockTransActionService.findOne(selectedStockTrans.getActionID());
                stockTransVofficeService.doSignedVofficeValidate(checkVoffice);
                infoOrderDetail = true;
                this.selectedStockTrans = selectedStockTrans;
                listProductTag.load(this, selectedStockTrans.getActionID(), configTag);
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }

        }
    }

    /**
     * ham set action id
     * @author thanhnt77
     * @param currentActionId
     */
    public void doSetActionId(Long currentActionId) {
        this.currentActionId = currentActionId;
    }

    /**
     * ham cap nhap trang thai vOffice
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSignVoffice() {
        try {
            if (DataUtil.isNullOrZero(currentActionId)) {
                return;
            }
            doSaveStatusOffice(currentActionId);
            //goi ham cap nhap thong tin vOffice
            reportSuccess("", "voffice.sign.office.susscess");
            doSearch();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExportNote", "common.error.happened", ex);
            topPage();
        }
    }


    @Secured("@")
    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public StaffInfoTag getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoTag staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Hàm">
    @Secured("@")
    public void doSearch() {
        try {
            validateStockTrans();
            vStockTransDTOS = DataUtil.defaultIfNull(transService.searchVStockTrans(searchStockTrans), new ArrayList<VStockTransDTO>());
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    private void validateStockTrans() throws LogicException, Exception {
        if (searchStockTrans == null) {
            searchStockTrans = new VStockTransDTO();
        }
        validateDate(searchStockTrans.getStartDate(), searchStockTrans.getEndDate());
        searchStockTrans.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        if (!DataUtil.isNullOrEmpty(searchStockTrans.getStockTransStatus())) {
            String[] split = searchStockTrans.getStockTransStatus().split(",");
            //truyen vao list status
            if (split.length > 1) {
                searchStockTrans.setStockTransStatus(null);
                searchStockTrans.setLstStatus(Lists.newArrayList(split));
            }
        }
        searchStockTrans.setActionType(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        searchStockTrans.setUserShopId(getStaffDTO().getShopId());
        searchStockTrans.setVtUnit(Const.VT_UNIT.VT);
        searchStockTrans.setObjectType(Const.CHANNEL_TYPE.OBJECT_TYPE_STAFF);
    }

    @Secured("@")
    public void selectStaff(VShopStaffDTO selected) {
        searchStockTrans.setFromOwnerID(DataUtil.safeToLong(selected.getOwnerId()));
        searchStockTrans.setFromOwnerName(selected.getOwnerName());
    }

    @Secured("@")
    public void clearStaff() {
        searchStockTrans.setFromOwnerID(null);
        searchStockTrans.setFromOwnerName(null);
    }

    @Secured("@")
    public StreamedContent doPrint() {
        try {
            StockTransDTO selectedStockTransDTO = genStockTransDTO(selectedStockTrans);
//            selectedStockTransDTO.setStockTransActionId(stockTransActionId);
            selectedStockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            selectedStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            return exportStockTransDetail(selectedStockTransDTO);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent printHandOver() {
        try {
            StockTransDTO selectedStockTransDTO = genStockTransDTO(selectedStockTrans);
            selectedStockTransDTO.setStockTransActionId(stockTransActionId);
            selectedStockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            selectedStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            return exportHandOverReport(selectedStockTransDTO, "XY");
        } catch (LogicException ex) {
            topReportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            topReportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doImportStock() {
        try {
            stockTransActionId = null;
            StockTransDTO selectedStockTransDTO = genStockTransDTO(selectedStockTrans);
            selectedStockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            //hoangnt: check neu NV chua co tren TTNS thi khong cho nhap hang
            StaffDTO staffDTO = staffService.findOne(selectedStockTransDTO.getFromOwnerId());
            if (DataUtil.isNullOrEmpty(staffDTO.getTtnsCode())) {
                throw new LogicException("", "export.order.staff.ttns.not.found.export");
            }
            StockTransActionDTO stockTransActionDTO = createStockTransAction(selectedStockTransDTO);
//            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            selectedStockTransDTO.setFromStock(Const.FROM_STOCK.STAFF_SHOP);

            BaseMessageStockTrans message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE,
                            Const.STOCK_TRANS_TYPE.IMPORT, selectedStockTransDTO,
                            stockTransActionDTO, Lists.newArrayList(), getTransRequiRedRoleMap());
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                topReportSuccess("", "stock.trans.import.success");
                selectedStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransActionId = message.getStockTransActionId();
            } else {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doValidate(VStockTransDTO vStockTransDTO) {

    }

    private StockTransActionDTO createStockTransAction(StockTransDTO selectedStockTrans) {
        StockTransActionDTO action = new StockTransActionDTO();
        action.setActionCode(selectedStockTrans.getActionCode());
        action.setStockTransId(selectedStockTrans.getStockTransId());
        action.setNote(selectedStockTrans.getNote());
        action.setCreateDatetime(getSysdateFromDB());
        action.setStatus(selectedStockTrans.getStatus());
        action.setActionStaffId(getStaffDTO().getStaffId());
        action.setCreateUser(getStaffDTO().getStaffCode());
        return action;
    }
    //</editor-fold>

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tuyendv8 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class StaffImportOrderController extends TransCommonController {

    private Object objectController;
    private Boolean infoRejectDetail = false;
    private Boolean infoOrderDetail = false;
    private boolean disableReject;
    private Boolean writeOffice = true;
    private boolean autoCreateNote;
    private int limitAutoComplete;
    private String actionCodeOrder;
    private String nameMethod = "receiveWriteOffice";
    private StaffDTO staffDTO;
    private StockTransDTO handleControls;
    private VStockTransDTO selectedStockTrans;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;
    private StockTransInputDTO transInputDTO;

    private List<String> lstChanelTypeId = Lists.newArrayList();
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<StockTransDTO> lstStockTrans;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<ReasonDTO> reasons = Lists.newArrayList();
    private List<ReasonDTO> importReasonDTOsList;

    @Autowired
    private OrderStaffTagNameable orderStaffTag;
    @Autowired
    private ShopInfoNameable shopInfoReceiveTag;
    @Autowired
    private StaffInfoNameable staffInfoExportTag;
    @Autowired
    private ListProductNameable listProductTag;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag; //Khai bao tag ky vOffice
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            doResetStockTrans();
            executeCommand("updateControls()");
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doResetStockTrans() throws LogicException, Exception {
        infoRejectDetail = false;
        transInputDTO = new StockTransInputDTO();
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        String shopId = DataUtil.safeToString(staffDTO.getShopId());
        lstChanelTypeId = commonService.getChannelTypes(Const.OWNER_TYPE.STAFF);
        shopInfoReceiveTag.initShop(this, shopId, false);
        shopInfoReceiveTag.loadShop(shopId, true);
        staffInfoExportTag.initStaffWithChanelTypes(this, shopId, null, lstChanelTypeId, false);
        optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC,Const.PERMISION.ROLE_AUTO_ORDER_NOTE,
                Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        orderStaffTag.init(this, writeOffice);
        //reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_IMP_FROM_STAFF), new ArrayList<ReasonDTO>());
        // danh sach hang hoa
        listProductTag.setAddNewProduct(false);
        //init cho vung thong tin ky vOffice
        writeOfficeTag.init(this, staffDTO.getShopId());
        //
        forSearch = new VStockTransDTO();
        Date currentDate = getSysdateFromDB();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
        //
        vStockTransDTOList = Lists.newArrayList();
        infoOrderDetail = false;
        staffInfoExportTag.initStaffWithChanelTypes(this, DataUtil.safeToString(staffDTO.getShopId()), null, lstChanelTypeId, false);
        doSearchStockTrans();
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            validateDate(forSearch.getStartDate(), forSearch.getEndDate());
            forSearch.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerID(staffDTO.getShopId());
            forSearch.setVtUnit(Const.VT_UNIT.VT);
            forSearch.setObjectType(Const.OWNER_TYPE.STAFF);
            forSearch.setUserShopId(staffDTO.getShopId());
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @param writeOffice
     * @author ThanhNT
     */
    @Secured("@")
    public void receiveWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
        updateElemetId("frmImportOrder:pnlInfoVOffice");
    }

    /**
     * ham xu ly nhan action ky office hay ko
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangeAutoOrderNote() {
        try {
            showAutoOrderNote = autoCreateNote;
            actionCodeNote = "";
            if (showAutoOrderNote) {
                String actionCodeTmp = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO());
                if (actionCodeTmp != null) {
                    int index = actionCodeTmp.lastIndexOf("_") + 1;
                    String prefix = actionCodeTmp.substring(0, index);
                    Long currenNum = DataUtil.safeToLong(actionCodeTmp.substring(index)) + 1L;
                    actionCodeNote = prefix + DataUtil.customFormat("000000", DataUtil.safeToDouble(currenNum));
                }
            }
            updateElemetId("frmImportOrder:pnlInfoExportNote");
            updateElemetId("frmImportOrder:pnlDataButton");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }

    }

    @Secured("@")
    public void doViewStockTransDetail(VStockTransDTO selected) {
        this.showAutoOrderNote = false;
        this.actionCodeOrder = "";
        this.autoCreateNote = false;
        if (DataUtil.isNullObject(selected)) {
            this.infoOrderDetail = false;
        } else {
            selectedStockTrans = selected;
            try {
//                List<Long> stockTransActionIds = Lists.newArrayList();
//                stockTransActionIds.add(selected.getActionID());
//                lsStockTransFull = DataUtil.defaultIfNull(transService.searchStockTransDetail(stockTransActionIds), new ArrayList<StockTransFullDTO>());
//                this.infoOrderDetail = true;
                ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, "", "");
                configListProductTagDTO.setShowSerialView(true);
                listProductTag.load(this, selected.getActionID(), configListProductTagDTO);
                infoOrderDetail = true;
                writeOffice = true;
                initHandle();
            } catch (LogicException ex) {
                topReportError("", ex);
            } catch (Exception ex) {
                topReportError("", "common.error.happened", ex);
            }
        }
    }

    @Secured("@")
    public void showInfoRejectDetail(VStockTransDTO selected) {
        try {
            if (DataUtil.isNullObject(selected)) {
                infoRejectDetail = false;
                return;
            }
            //Load thong tin phieu xuat
            selectedStockTrans = selected;
            infoRejectDetail = true;
            disableReject = false;

            //Load danh sach hang hoa
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, "", "");
            configListProductTagDTO.setShowSerialView(true);
            listProductTag.load(this, selectedStockTrans.getActionID(), configListProductTagDTO);

            //Load thong tin tu choi
            importReasonDTOsList = reasonService.getLsReasonByType(Const.REASON_CODE.IMP_UNDER_REJECT);//lu do tu choi
            transInputDTO.setCreateDatetime(new Date());
            //clear form
            transInputDTO.setActionCode(null);
            transInputDTO.setReasonId(null);
            transInputDTO.setRegionStockId(null);
            transInputDTO.setNote(null);

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    public void initHandle() throws LogicException, Exception {
        handleControls = new StockTransDTO();
        handleControls.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_LN, Const.STOCK_TRANS_TYPE.IMPORT, staffDTO));
        handleControls.setUserCreate(staffDTO.getName());
        handleControls.setCreateDatetime(new Date());
        handleControls.setFromOwnerId(getStaffDTO().getShopId());
        handleControls.setFromOwnerName(getStaffDTO().getShopName());
        handleControls.setToOwnerId(getStaffDTO().getStaffId());
        handleControls.setToOwnerName(getStaffDTO().getName());
        reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_IMP_FROM_STAFF), new ArrayList<ReasonDTO>());
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doExportStaff(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void resetExportStaff() {
        forSearch.setFromOwnerID(null);
    }

    /**
     * ham check hien thi check box lap phieu tu dong
     * @author thanhnt77
     * @return
     */
    public Boolean getRoleShowAutoOrderNote() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doCreateStaffImportOrder() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong

            List<StockTransDetailDTO> lsDetailDTOs = listProductTag.getListTransDetailDTOs();
            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setActionCode(handleControls.getActionCode());
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setNote(handleControls.getImportNote());
            StockTransDTO stockTransDTO = genStockTransDTO(selectedStockTrans);
            stockTransDTO.setImportNote(handleControls.getImportNote());
            stockTransDTO.setImportReasonId(handleControls.getReasonId());
            stockTransDTO.setActionCodeNote(actionCodeNote);
            stockTransDTO.setStaffId(staffDTO.getStaffId());
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
//                String passwordEncrypt1 = PassWordUtil.getInstance().encrypt(signOfficeDTO.getPassWord());
//                String passwordEncrypt2 = EncryptionUtils.encrypt(passwordEncrypt1, EncryptionUtils.getKey());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            BaseMessage message = executeStockTransService.executeStockTrans(this.showAutoOrderNote ? Const.STOCK_TRANS.ORDER_AND_NOTE_STAFF : Const.STOCK_TRANS.ORDER_FROM_STAFF,
                    Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                if (showAutoOrderNote) {
                    reportSuccess("frmImportOrder:msgImportOrder", "mn.stock.staff.create.import.order.note.success");
                } else {
                    reportSuccess("frmImportOrder:msgImportOrder", "mn.stock.staff.create.import.order.success");
                }

                topPage();
                selectedStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            } else {
//                topReportError("", message.getErrorCode(), message.getKeyMsg(),message.getParamsMsg());
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            actionCodeNotePrint = actionCodeNote;
            actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO());
        } catch (LogicException ex) {
            topReportError("frmImportOrder:msgImportOrder", ex);
        } catch (Exception ex) {
            topReportError("frmImportOrder:msgImportOrder", "common.error.happened", ex);

        }
    }

    @Secured("@")
    public void doRejectImportOrder() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
            List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                lsDetailDTOs.add(stockTransDetailDTO);
            }

            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setStockTransActionId(selectedStockTrans.getActionID());
            stockTransActionDTO.setActionCode(null);
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setNote(transInputDTO.getNote());

            StockTransDTO stockTransDTO = genStockTransDTO(selectedStockTrans);
            stockTransDTO.setRejectNote(transInputDTO.getNote());
            stockTransDTO.setRejectReasonId(transInputDTO.getReasonId());

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.REJECT_TRANS, null, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("frmExportOrder:msgExport", "mn.stock.underlying.rejectOrderImport.success");
            disableReject = true;
            topPage();
        } catch (LogicException ex) {
            topReportError("frmImportOrder:msgImportOrder", ex);
        } catch (Exception ex) {
            topReportError("frmImportOrder:msgImportOrder", "common.error.happened", ex);

        }
    }

    @Secured("@")
    public StreamedContent printStockTransDetail() {
        try {
            StockTransDTO forPrint = genStockTransDTO(selectedStockTrans);
            forPrint.setActionCode(handleControls.getActionCode());
            forPrint.setReasonName(handleControls.getReasonName());
            if (selectedStockTrans.getStockTransStatus().equals(Const.STOCK_TRANS_STATUS.IMPORT_ORDER)) {
                forPrint.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
                forPrint.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
                List<StockTransDetailDTO> details = listProductTag.getListTransDetailDTOs();
                forPrint.setStockTransActionId(null);
                return exportStockTransDetail(forPrint, details);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("frmImportOrder:msgImportOrder", ex);
        } catch (Exception ex) {
            topReportError("frmImportOrder:msgImportOrder", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent printStockTransDetailNote() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
            StockTransDTO forPrint = genStockTransDTO(selectedStockTrans);
            forPrint.setActionCode(actionCodeNotePrint);
            forPrint.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            forPrint.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            forPrint.setStockTransActionId(null);
            List<StockTransDetailDTO> details = listProductTag.getListTransDetailDTOs();
            return exportStockTransDetail(forPrint, details);
        } catch (LogicException ex) {
            topReportError("frmImportNote:msgImportNote", ex);
        } catch (Exception ex) {
            topReportError("frmImportNote:msgImportNote", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public void doBackPage() {
        try {
            infoOrderDetail = false;
            infoRejectDetail = false;
            orderStaffTag.resetOrderStaff();
            lsStockTransFull = Lists.newArrayList();
            doSearchStockTrans();
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean isInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
    }

    public Boolean getWriteOffice() {
        return writeOffice;
    }

    public void setWriteOffice(Boolean writeOffice) {
        this.writeOffice = writeOffice;
    }


    public String getNameMethod() {
        return nameMethod;
    }

    public void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public Boolean setWriteOffice() {
        return writeOffice;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public List<OptionSetValueDTO> getOptionSetValueDTOsList() {
        return optionSetValueDTOsList;
    }

    public void setOptionSetValueDTOsList(List<OptionSetValueDTO> optionSetValueDTOsList) {
        this.optionSetValueDTOsList = optionSetValueDTOsList;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public ShopInfoNameable getShopInfoReceiveTag() {
        return shopInfoReceiveTag;
    }

    public void setShopInfoReceiveTag(ShopInfoNameable shopInfoReceiveTag) {
        this.shopInfoReceiveTag = shopInfoReceiveTag;
    }

    public StaffInfoNameable getStaffInfoExportTag() {
        return staffInfoExportTag;
    }

    public void setStaffInfoExportTag(StaffInfoNameable staffInfoExportTag) {
        this.staffInfoExportTag = staffInfoExportTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public String getActionCodeOrder() {
        return actionCodeOrder;
    }

    public void setActionCodeOrder(String actionCodeOrder) {
        this.actionCodeOrder = actionCodeOrder;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public OrderStaffTagNameable getOrderStaffTag() {
        return orderStaffTag;
    }

    public void setOrderStaffTag(OrderStaffTagNameable orderStaffTag) {
        this.orderStaffTag = orderStaffTag;
    }

    public StockTransDTO getHandleControls() {
        return handleControls;
    }

    public void setHandleControls(StockTransDTO handleControls) {
        this.handleControls = handleControls;
    }

    public List<ReasonDTO> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonDTO> reasons) {
        this.reasons = reasons;
    }

    public VStockTransDTO getSelectedStockTrans() {
        return selectedStockTrans;
    }

    public void setSelectedStockTrans(VStockTransDTO selectedStockTrans) {
        this.selectedStockTrans = selectedStockTrans;
    }

    public List<StockTransDTO> getLstStockTrans() {
        return lstStockTrans;
    }

    public void setLstStockTrans(List<StockTransDTO> lstStockTrans) {
        this.lstStockTrans = lstStockTrans;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public Boolean getInfoRejectDetail() {
        return infoRejectDetail;
    }

    public void setInfoRejectDetail(Boolean infoRejectDetail) {
        this.infoRejectDetail = infoRejectDetail;
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public List<ReasonDTO> getImportReasonDTOsList() {
        return importReasonDTOsList;
    }

    public void setImportReasonDTOsList(List<ReasonDTO> importReasonDTOsList) {
        this.importReasonDTOsList = importReasonDTOsList;
    }

    public boolean isDisableReject() {
        return disableReject;
    }

    public void setDisableReject(boolean disableReject) {
        this.disableReject = disableReject;
    }

    public boolean isAutoCreateNote() {
        return autoCreateNote;
    }

    public void setAutoCreateNote(boolean autoCreateNote) {
        this.autoCreateNote = autoCreateNote;
    }
}

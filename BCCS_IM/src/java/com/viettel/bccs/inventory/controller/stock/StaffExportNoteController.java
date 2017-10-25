package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
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
import java.util.*;

/**
 * Created by tuyendv8 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class StaffExportNoteController extends TransCommonController {
    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = true;
    private Boolean linkDownload = false;
    private int limitAutoComplete;
    private String actionCodeNote;
    private String nameMethod = "receiveWriteOffice";
    private VStockTransDTO forSearch;
    private VStockTransDTO selectedStockTrans;
    private RequiredRoleMap requiredRoleMap;
    private List<String> lstChanelTypeId = Lists.newArrayList();
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<VStockTransDTO> selectedStockTransActions;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<StockTransFileDTO> stockTransFileDTOList = Lists.newArrayList();
    private List<FieldExportFileDTO> exportFileResult = Lists.newArrayList();
    private StaffDTO staffDTO;

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private OrderStaffTagNameable orderStaffTag;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;
    @Autowired
    private ShopInfoNameable shopInfoExportTag;
    @Autowired
    private StaffInfoNameable staffInfoReceiveTag;
    @Autowired
    private ListProductNameable listProductTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransDetailService stockTransDetailService;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            doResetStockTrans();
            executeCommand("updateControls()");
            showDialog("guide");
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doResetStockTrans() throws LogicException, Exception {
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        String shopId = DataUtil.safeToString(staffDTO.getShopId());
        lstChanelTypeId = commonService.getChannelTypes(Const.OWNER_TYPE.STAFF);
        shopInfoExportTag.initShop(this, shopId, false);
        shopInfoExportTag.loadShop(shopId, true);
        staffInfoReceiveTag.initStaffWithChanelTypes(this, shopId, null, lstChanelTypeId, false);
        optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK,
                Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        orderStaffTag.init(this, writeOffice);
        // init vung ky VOffice
        writeOfficeTag.init(this, staffDTO.getShopId());
        // danh sach hang hoa
        listProductTag.setAddNewProduct(false);
        //
        forSearch = new VStockTransDTO();
        Date currentDate = getSysdateFromDB();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        vStockTransDTOList = Lists.newArrayList();
        infoOrderDetail = false;
        staffInfoReceiveTag.initStaffWithChanelTypes(this, DataUtil.safeToString(staffDTO.getShopId()), null, lstChanelTypeId, false);
        doSearchStockTrans();
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            linkDownload = false;
            validateDate(forSearch.getStartDate(), forSearch.getEndDate());
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setFromOwnerID(staffDTO.getShopId());
            forSearch.setToOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            forSearch.setVtUnit(Const.VT_UNIT.VT);
            forSearch.setObjectType(Const.OWNER_TYPE.STAFF);
            forSearch.setUserShopId(staffDTO.getShopId());
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
            selectedStockTransActions = Lists.newArrayList();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doShowInfoOrderDetail(VStockTransDTO selected) {
        try {
            if (DataUtil.isNullObject(selected)) {
                this.infoOrderDetail = false;
            } else {
                //Validate ky voffice
                StockTransActionDTO actionDTO = stockTransActionService.findOne(selected.getActionID());
                stockTransVofficeService.doSignedVofficeValidate(actionDTO);

                selectedStockTrans = selected;
                orderStaffTag.loadOrderStaff(selected.getActionID(), true);
//                StockTransDTO stock = orderStaffTag.getStockTransDTO();
//                if (!DataUtil.isNullOrEmpty(stock.getLogicstic())) {
//                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.by.logistic");
//                }
                ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, "", "");
                configListProductTagDTO.setShowSerialView(true);
                listProductTag.load(this, selected.getActionID(), configListProductTagDTO);
                infoOrderDetail = true;
                writeOffice = true;
                actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            }
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

    @Secured("@")
    public void doDestroyStock(Long stockTransActionId) {
        try {
            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransActionDTO.setActionCode(null);
            stockTransActionDTO.setNote(null);

            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());
            stockTransDTO.setNote(null);

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS, null, stockTransDTO, stockTransActionDTO, new ArrayList<StockTransDetailDTO>(), requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmExportNote:msgExportNote", "export.order.stock.confirm.cancel.ok");
            doSearchStockTrans();
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

    /**
     * ham set action id
     *
     * @param currentActionId
     * @author thanhnt77
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
            doSearchStockTrans();
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
    public void doBackPage() {
        try {
            infoOrderDetail = false;
            orderStaffTag.resetOrderStaff();
            //lsStockTransFull = Lists.newArrayList();
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
            //doResetStockTrans();
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
    public void doExportShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doReceiveStaff(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void resetReceiveStaff() {
        forSearch.setToOwnerID(null);
    }

    /**
     * ham validate
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidateStaffExportNote() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doCreateExportNote() {
        try {
            StockTransActionDTO stockTransActionDTO = orderStaffTag.getStockTransActionDTO();
            stockTransActionDTO.setActionCode(actionCodeNote);
            stockTransActionDTO.setNote(null); // khong co truong note tren giao dien

            StockTransDTO stockTransDTO = orderStaffTag.getStockTransDTO();
            stockTransDTO.setStockTransActionId(stockTransActionDTO.getStockTransActionId());
            stockTransDTO.setNote(null);

            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
//                String passwordEncrypt1 = PassWordUtil.getInstance().encrypt(signOfficeDTO.getPassWord());
//                String passwordEncrypt2 = EncryptionUtils.encrypt(passwordEncrypt1, EncryptionUtils.getKey());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_ORDER, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, new ArrayList<StockTransDetailDTO>(), requiredRoleMap);
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                reportSuccess("frmExportNote:msgExportNote", "export.note.create.success");
                topPage();
                selectedStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            } else {
//                topReportError("", message.getErrorCode(), message.getKeyMsg());
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
        } catch (LogicException ex) {
            topReportError("frmExportNote:msgExportNote", ex);
        } catch (Exception ex) {
            topReportError("frmExportNote:msgExportNote", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doCreateMutilExportNote() {
        try {
            stockTransFileDTOList = Lists.newArrayList();
            SignOfficeDTO signOfficeDTO = null;
            if (writeOffice) {
                signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            }
            //set gia tri cho list stockTrans
            String actionCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            int index = actionCode.lastIndexOf("_") + 1;
            String prefix = actionCode.substring(0, index);
            String posfix = actionCode.substring(index);
            Long noteNumber = DataUtil.safeToLong(posfix);
            for (VStockTransDTO vStockTransDTO : selectedStockTransActions) {
                StockTransFileDTO stockTransFileDTO = new StockTransFileDTO();

                StockTransDTO stockTransDTO = genStockTransDTO(vStockTransDTO);
                stockTransDTO.setNote(null);
                stockTransDTO.setStaffId(staffDTO.getStaffId());
                StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(vStockTransDTO.getActionID());
                //set gia tri cho stockTransAction

                String validOffice = validateOfficeMulti(stockTransActionDTO);
                if (!DataUtil.isNullOrEmpty(validOffice)) {
                    stockTransFileDTO.setMsgError(validOffice);
                } else if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                        || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                        || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                    stockTransFileDTO.setMsgError(getTextParam("mn.stock.underlying.not.create.order.atutogen.fail", getText("export.order.btn.submit.field")));
                } else {
                    actionCode = prefix + DataUtil.customFormat("000000", DataUtil.safeToDouble(noteNumber));
                    stockTransActionDTO.setActionCode(actionCode);
                    noteNumber++;
                }

                stockTransActionDTO.setSignCaStatus(null);
                if (!DataUtil.isNullObject(signOfficeDTO)) {
                    stockTransDTO.setUserName(signOfficeDTO.getUserName());
                    stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                    stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                    stockTransDTO.setSignVoffice(true);
                    stockTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
                }

                stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
                stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());

                //set gia tri cho listStockTransDetail
                List<StockTransDetailDTO> lstStockTransDetailDTO = stockTransDetailService.findByStockTransId(vStockTransDTO.getStockTransID());

                stockTransFileDTO.setStockTransDTO(stockTransDTO);
                stockTransFileDTO.setStockTransActionDTO(stockTransActionDTO);
                stockTransFileDTO.setLstStockTransDetail(lstStockTransDetailDTO);

                stockTransFileDTOList.add(stockTransFileDTO);
            }

            List<BaseExtMessage> lsMessage = executeStockTransService.executeStockTransList(Const.STOCK_TRANS.NOTE_ORDER, Const.STOCK_TRANS_TYPE.EXPORT, stockTransFileDTOList, requiredRoleMap);

            Map mapResult = convertListErrorFile(lsMessage, "export.note.create.success");
            boolean isValidResult = (boolean) mapResult.get(CREATE_MULTI_DATA_KEY_STATUS);
            exportFileResult = (List<FieldExportFileDTO>) mapResult.get(CREATE_MULTI_DATA_KEY_LIST);
            if (isValidResult) {
                reportSuccess("", "stock.mutil.note.success.file.detail");
            } else {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.multi.export.staff");
            }


            doSearchStockTrans();
            linkDownload = true;
            topPage();
        } catch (LogicException ex) {
            topReportError("frmExportNote:msgExportNote", ex);
        } catch (Exception ex) {
            topReportError("frmExportNote:msgExportNote", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public StreamedContent doDownloadFileError() {
        try {
            return exportMultiNoteErrorDetail(exportFileResult);
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    @Secured("@")
    public StreamedContent printStockTransDetail() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
            StockTransDTO stockTransDTO = orderStaffTag.getStockTransDTOForPint();
            if (stockTransDTO != null && selectedStockTrans.getStockTransStatus().equals(Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                stockTransDTO.setActionCode(actionCodeNote);
                stockTransDTO.setStockTransActionId(null);
                return exportStockTransDetail(stockTransDTO, listProductTag.getListTransDetailDTOs());
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            topReportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        return null;
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
        updateElemetId("frmExportNote:pnlInfoVOffice");
    }

    public boolean getShowCreateMultiExportNote() {
        return !DataUtil.isNullOrEmpty(selectedStockTransActions);
    }

    public void showInfoOrderDetail() {
        infoOrderDetail = true;
    }

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public boolean getInfoOrderDetail() {
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

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
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

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public StaffInfoNameable getStaffInfoReceiveTag() {
        return staffInfoReceiveTag;
    }

    public void setStaffInfoReceiveTag(StaffInfoNameable staffInfoReceiveTag) {
        this.staffInfoReceiveTag = staffInfoReceiveTag;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public String getActionCodeNote() {
        return actionCodeNote;
    }

    public void setActionCodeNote(String actionCodeNote) {
        this.actionCodeNote = actionCodeNote;
    }

    public OrderStaffTagNameable getOrderStaffTag() {
        return orderStaffTag;
    }

    public void setOrderStaffTag(OrderStaffTagNameable orderStaffTag) {
        this.orderStaffTag = orderStaffTag;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ShopInfoNameable getShopInfoExportTag() {
        return shopInfoExportTag;
    }

    public void setShopInfoExportTag(ShopInfoNameable shopInfoExportTag) {
        this.shopInfoExportTag = shopInfoExportTag;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public VStockTransDTO getSelectedStockTrans() {
        return selectedStockTrans;
    }

    public void setSelectedStockTrans(VStockTransDTO selectedStockTrans) {
        this.selectedStockTrans = selectedStockTrans;
    }

    public List<VStockTransDTO> getSelectedStockTransActions() {
        return selectedStockTransActions;
    }

    public void setSelectedStockTransActions(List<VStockTransDTO> selectedStockTransActions) {
        this.selectedStockTransActions = selectedStockTransActions;
    }

    public Boolean getLinkDownload() {
        return linkDownload;
    }

    public void setLinkDownload(Boolean linkDownload) {
        this.linkDownload = linkDownload;
    }

    public List<FieldExportFileDTO> getExportFileResult() {
        return exportFileResult;
    }

    public void setExportFileResult(List<FieldExportFileDTO> exportFileResult) {
        this.exportFileResult = exportFileResult;
    }
}

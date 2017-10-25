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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tuyendv8 on 11/11/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class StaffImportNoteController extends TransCommonController {

    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = true;
    private Boolean linkDownload = false;
    private int limitAutoComplete;
    private String actionCodeNote;
    private String nameMethod = "receiveWriteOffice";
    private StaffDTO staffDTO;
    private VStockTransDTO selectedStockTrans;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;
    private List<String> lstChanelTypeId = Lists.newArrayList();
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<VStockTransDTO> selectedStockTransActions;
    private List<StockTransDTO> lstStockTrans;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<ReasonDTO> reasons;
    private List<FieldExportFileDTO> exportFileResult = Lists.newArrayList();
    private List<StockTransFileDTO> stockTransFileDTOList = Lists.newArrayList();

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private OrderStaffTagNameable orderStaffTag;
    @Autowired
    private ShopInfoNameable shopInfoReceiveTag;
    @Autowired
    private StaffInfoNameable staffInfoExportTag;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag; //Khai bao tag ky vOffice
    @Autowired
    private ListProductNameable listProductTag;
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
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
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
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doResetStockTrans() throws LogicException, Exception {
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        String shopId = DataUtil.safeToString(staffDTO.getShopId());
        lstChanelTypeId = commonService.getChannelTypes(Const.OWNER_TYPE.STAFF);
        shopInfoReceiveTag.initShop(this, shopId, false);
        shopInfoReceiveTag.loadShop(shopId, true);
        staffInfoExportTag.initStaffWithChanelTypes(this, shopId, null, lstChanelTypeId, false);
        optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
        orderStaffTag.init(this, writeOffice);
        //reasons = DataUtil.defaultIfNull(reasonService.getLsReasonByType(Const.REASON_TYPE.STOCK_EXP_STAFF), new ArrayList<ReasonDTO>());
        // danh sach hang hoa
        listProductTag.setAddNewProduct(false);
        //init cho vung thong tin ky vOffice
        writeOfficeTag.init(this, staffDTO.getShopId());
        //
        forSearch = new VStockTransDTO();
        Date currentDate = getSysdateFromDB();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setActionType(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
        infoOrderDetail = false;
        vStockTransDTOList = Lists.newArrayList();
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
        updateElemetId("frmImportNote:pnlInfoVOffice");
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

    @Secured("@")
    public void doViewStockTransDetail(VStockTransDTO selected) {
        try {
            if (DataUtil.isNullObject(selected)) {
                this.infoOrderDetail = false;
            } else {
                //Validate ky voffice
                StockTransActionDTO actionDTO = stockTransActionService.findOne(selected.getActionID());
                stockTransVofficeService.doSignedVofficeValidate(actionDTO);

                selectedStockTrans = selected;
                ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, "", "");
                configListProductTagDTO.setShowSerialView(true);
                listProductTag.load(this, selected.getActionID(), configListProductTagDTO);
                infoOrderDetail = true;
                writeOffice = true;
                actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, getStaffDTO());
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
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
     * ham lap phieu nhap
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doValidateImportNote() {
        //        List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
        //        for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
        //            if (DataUtil.isNullOrEmpty(stockTransFullDTO.getLstSerial())) {
        //                reportError("", ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.confirm.serial.empty", stockTransFullDTO.getProdOfferName());
        //                break;
        //            }
        //        }
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doCreateStaffImportNote() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
            List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());

                lsDetailDTOs.add(stockTransDetailDTO);
            }
            StockTransActionDTO stockTransActionDTO = new StockTransActionDTO();
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setActionCode(actionCodeNote);
            StockTransDTO stockTransDTO = genStockTransDTO(selectedStockTrans);
            stockTransDTO.setStaffId(staffDTO.getStaffId());
            stockTransDTO.setNote(null);
            stockTransDTO.setImportReasonId(selectedStockTrans.getImportReasonID());
            stockTransDTO.setImportNote(selectedStockTrans.getImportNote());
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
//                String passwordEncrypt1 = PassWordUtil.getInstance().encrypt(signOfficeDTO.getPassWord());
//                String passwordEncrypt2 = EncryptionUtils.encrypt(passwordEncrypt1, EncryptionUtils.getKey());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_FROM_STAFF, Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (DataUtil.isNullObject(message) || DataUtil.isNullOrEmpty(message.getErrorCode())) {
                reportSuccess("frmImportNote:msgImportNote", "mn.stock.staff.create.import.note.success");
                topPage();
                selectedStockTrans.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            } else {
//                topReportError("", message.getErrorCode(), message.getKeyMsg());
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmImportNote:msgImportNote", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmImportNote:msgImportNote", "common.error.happened", ex);
            topPage();
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
            String actionCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO());
            int index = actionCode.lastIndexOf("_") + 1;
            String prefix = actionCode.substring(0, index);
            String posfix = actionCode.substring(index);
            Long noteNumber = DataUtil.safeToLong(posfix);
            for (VStockTransDTO vStockTransDTO : selectedStockTransActions) {
                StockTransFileDTO stockTransFileDTO = new StockTransFileDTO();

                StockTransDTO stockTransDTO = genStockTransDTO(vStockTransDTO);
                stockTransDTO.setStaffId(staffDTO.getStaffId());
                stockTransDTO.setNote(null);
                stockTransDTO.setImportReasonId(vStockTransDTO.getImportReasonID());
                stockTransDTO.setImportNote(vStockTransDTO.getImportNote());
                StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(vStockTransDTO.getActionID());

                String validOffice = validateOfficeMulti(stockTransActionDTO);
                if (!DataUtil.isNullOrEmpty(validOffice)) {
                    stockTransFileDTO.setMsgError(validOffice);
                } else if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                        || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                        || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                    stockTransFileDTO.setMsgError(getTextParam("mn.stock.underlying.not.create.order.atutogen.fail", getText("mn.stock.staff.create.import.note")));
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

            List<BaseExtMessage> lsMessage = executeStockTransService.executeStockTransList(Const.STOCK_TRANS.NOTE_FROM_STAFF, Const.STOCK_TRANS_TYPE.IMPORT, stockTransFileDTOList, requiredRoleMap);

            Map mapResult = convertListErrorFile(lsMessage, "export.note.create.success");
            boolean isValidResult = (boolean) mapResult.get(CREATE_MULTI_DATA_KEY_STATUS);
            exportFileResult = (List<FieldExportFileDTO>) mapResult.get(CREATE_MULTI_DATA_KEY_LIST);
            if (isValidResult) {
                reportSuccess("", "stock.mutil.note.success.file.detail");
            } else {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.multi.import.staff");
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
    public StreamedContent printStockTransDetail() {
        try {
            doValidateListDetail(listProductTag.getListTransDetailDTOs());
            StockTransDTO forPrint = genStockTransDTO(selectedStockTrans);
            forPrint.setActionCode(actionCodeNote);
            if (selectedStockTrans.getStockTransStatus().equals(Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
                forPrint.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                forPrint.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
                forPrint.setStockTransActionId(null);
                List<StockTransDetailDTO> details = listProductTag.getListTransDetailDTOs();
                return exportStockTransDetail(forPrint, details);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
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
            orderStaffTag.resetOrderStaff();
            lsStockTransFull = Lists.newArrayList();
            //vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
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

    public boolean getShowCreateMultiExportNote() {
        return !DataUtil.isNullOrEmpty(selectedStockTransActions);
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

    public String getActionCodeNote() {
        return actionCodeNote;
    }

    public void setActionCodeNote(String actionCodeNote) {
        this.actionCodeNote = actionCodeNote;
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

    public Boolean getLinkDownload() {
        return linkDownload;
    }

    public void setLinkDownload(Boolean linkDownload) {
        this.linkDownload = linkDownload;
    }

    public List<VStockTransDTO> getSelectedStockTransActions() {
        return selectedStockTransActions;
    }

    public void setSelectedStockTransActions(List<VStockTransDTO> selectedStockTransActions) {
        this.selectedStockTransActions = selectedStockTransActions;
    }
}

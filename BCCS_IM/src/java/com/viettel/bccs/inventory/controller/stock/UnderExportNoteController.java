package com.viettel.bccs.inventory.controller.stock;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.model.FileExportBean;
import com.viettel.bccs.inventory.common.util.FileUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.util.EncryptionUtils;
import com.viettel.bccs.inventory.util.PassWordUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.*;

/**
 * controller lap phieu xuat kho
 * Created by thanhnt77 on 11/5/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class UnderExportNoteController extends TransCommonController {

    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = false;
    private Boolean linkDownload = false;
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private String actionCodeNote;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;
    private StaffDTO staffDTO;
    private boolean isPrintSucess;
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<StockTransFullDTO> lsStockTransFull;

    private List<VStockTransDTO> selectedStockTransActions;
    private List<StockTransFileDTO> stockTransFileDTOList = Lists.newArrayList();
    private List<FieldExportFileDTO> exportFileResult = Lists.newArrayList();

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private SignOfficeTagNameable signOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StaffService staffService;
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
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            String shopId = DataUtil.safeToString(staffDTO.getShopId());
            shopInfoTagReceive.initShop(this, shopId, false);
            shopInfoTagExport.initShop(this, shopId, false);
            shopInfoTagExport.loadShop(shopId, true);
            doResetStockTrans();
            optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_STATUS);
            signOfficeTag.init(this, staffDTO.getShopId());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            orderStockTag.init(this, writeOffice);
            RequestContext.getCurrentInstance().execute("updateAll()");
            showDialog("guide");
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    /**
     * @author ThanhNT77
     */
    @Secured("@")
    public void doSearchStockTrans() {
        try {
            linkDownload = false;
            currentActionId = null;
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
            //clear checkbox va set gia tri ky voffice
            selectedStockTransActions = Lists.newArrayList();
            writeOffice = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    /**
     * ham xu ly reset thong tin tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doResetStockTrans() {
        vStockTransDTOList = Lists.newArrayList();
        shopInfoTagReceive.resetShop();
        Date currentDate = Calendar.getInstance().getTime();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        forSearch.setFromOwnerID(staffDTO.getShopId());
        forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        forSearch.setVtUnit(Const.VT_UNIT.VT);
        forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
        forSearch.setUserShopId(staffDTO.getShopId());
        forSearch.setStockTransStatus(Const.STATUS.ACTIVE);
        forSearch.setStockTransType(null);
        infoOrderDetail = false;
    }

    /**
     * ham xu xem chi tiet kho
     *
     * @param stockTransActionId
     * @author ThanhNT77
     */
    @Secured("@")
    public void doShowInfoOrderDetail(Long stockTransActionId) {
        try {
            //Validate ky voffice
            StockTransActionDTO actionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);

            orderStockTag.loadOrderStock(stockTransActionId, true);
            StockTransDTO stock = orderStockTag.getStockTransDTO();
            if (!DataUtil.isNullOrEmpty(stock.getLogicstic())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.by.logistic");
            }
            orderStockTag.setNameThreeRegion("1");
            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            writeOffice = true;
            isPrintSucess = false;
            signOfficeTag.resetOffice();
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
    public void doBackPage() {
        infoOrderDetail = false;
        orderStockTag.resetOrderStock();
        lsStockTransFull = Lists.newArrayList();
        doSearchStockTrans();
    }

    @Secured("@")
    public void doExportShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doValidateCreateNote() {
        if (!DataUtil.validateStringByPattern(actionCodeNote, "[0-9a-zA-Z_-]{1,50}")) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.import.transCode.error.format.msg");
        }
    }

    /**
     * ham lap phieu xuat
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doCreateExportNote() {
        try {
            //validate user dang nhap trc khi xuat kho
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            valdiateUserLogin(stockTransDTO.getFromOwnerId(), stockTransDTO.getToOwnerId(), DataUtil.isNullOrZero(stockTransDTO.getRegionStockId()));
            stockTransDTO.setNote(null);
            stockTransActionDTO.setActionCode(actionCodeNote);
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = signOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }
            if (orderStockTag.getTranLogistics()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.by.logistic");
            }
            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.atutogen.fail", getText("export.order.btn.submit.field"));
            }
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE_ORDER, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, new ArrayList<>(), requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmExportOrder:msgExport", "export.note.create.success", stockTransActionDTO.getActionCode());
            isPrintSucess = true;
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doCreateMutilExportNote() {
        try {
            stockTransFileDTOList = Lists.newArrayList();
            SignOfficeDTO signOfficeDTO = null;
            if (writeOffice) {
                signOfficeDTO = signOfficeTag.validateVofficeAccount();
            }
            //set gia tri cho list stockTrans
            String actionCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PX, Const.STOCK_TRANS_TYPE.EXPORT, BccsLoginSuccessHandler.getStaffDTO());
            int index = actionCode.lastIndexOf("_") + 1;
            String prefix = actionCode.substring(0, index);
            String posfix = actionCode.substring(index);
            Long noteNumber = DataUtil.safeToLong(posfix);
            for (VStockTransDTO vStockTransDTO : selectedStockTransActions) {
                //set gia tri cho stockTrans
                StockTransFileDTO stockTransFileDTO = new StockTransFileDTO();
                StockTransDTO stockTransDTO = genStockTransDTO(vStockTransDTO);
                stockTransDTO.setNote(null);
                stockTransDTO.setStaffId(staffDTO.getStaffId());
//                stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);//fix validate kho xuat phai la cap tren lien ke kho nhan
                //set gia tri cho stockTransAction
                StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(vStockTransDTO.getActionID());

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

                stockTransDTO.setRegionStockId(stockTransActionDTO.getRegionOwnerId());
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
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.multi.export.stock");
            }
            doSearchStockTrans();
            linkDownload = true;
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
            topPage();
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

    /**
     * ham xu ly in phieu
     *
     * @return
     * @author ThanhNT77
     */
    @Secured("@")
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionCode(actionCodeNote);
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
            stockTransDTO.setStockTransActionId(null);
            return exportStockTransDetail(stockTransDTO, lsStockTransFull);
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public String getMsgConfirm() {
        return orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO() != null && !DataUtil.isNullOrEmpty(orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO().getItemLabel()) ?
                getTextParam("export.order.btn.submit.header.confirm.msg.field", orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO().getItemLabel()) : "";
    }

    /**
     * ham huy giao dich
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doDestroyStock() {
        try {
            if (DataUtil.isNullOrZero(currentActionId)) {
                return;
            }
            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(currentActionId);

            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());

            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.destroy.order.atutogen.fail");
            }

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS, null, stockTransDTO, stockTransActionDTO, new ArrayList<>(), requiredRoleMap);
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
    public void doSetActionId(Long stockTransActionId) {
        this.currentActionId = stockTransActionId;
    }

    public boolean isHasData() {
        return DataUtil.isNullOrEmpty(vStockTransDTOList);
    }

    @Secured("@")
    public void doValidateDesTroy(Long stockTransActionId) {
        this.currentActionId = stockTransActionId;
    }

    //getter and setter
    public Boolean getInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(Boolean infoOrderDetail) {
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

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public SignOfficeTagNameable getSignOfficeTag() {
        return signOfficeTag;
    }

    public void setSignOfficeTag(SignOfficeTagNameable signOfficeTag) {
        this.signOfficeTag = signOfficeTag;
    }

    public ShopInfoNameable getShopInfoTagExport() {
        return shopInfoTagExport;
    }

    public void setShopInfoTagExport(ShopInfoNameable shopInfoTagExport) {
        this.shopInfoTagExport = shopInfoTagExport;
    }

    public ShopInfoNameable getShopInfoTagReceive() {
        return shopInfoTagReceive;
    }

    public void setShopInfoTagReceive(ShopInfoNameable shopInfoTagReceive) {
        this.shopInfoTagReceive = shopInfoTagReceive;
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

    public boolean isPrintSucess() {
        return isPrintSucess;
    }

    public void setPrintSucess(boolean isCreateSucess) {
        this.isPrintSucess = isCreateSucess;
    }

    @Secured("@")
    public StreamedContent printStockTrans() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("export_trans_list.xls");
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.PRINT_LIST_TRANS);
            fileExportBean.putValue("lstStockTransFull", vStockTransDTOList);
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    @Secured("@")
    public StreamedContent printAllStockTransDetail() {
        try {
            FileExportBean fileExportBean = new FileExportBean();
            fileExportBean.setOutName("export_trans_list.xls");
            fileExportBean.setTemplateName(Const.REPORT_TEMPLATE.PRINT_LIST_TRANS_DETAIL);
            fileExportBean.putValue("lstStockTransFull", vStockTransDTOList);
            int index = 0;
            for (VStockTransDTO stockTransDTO : vStockTransDTOList) {
                stockTransDTO.setIndex(new Long(index++));
                List<Long> actionIDs = Lists.newArrayList(stockTransDTO.getActionID());
                List<StockTransFullDTO> lstDetail = transService.searchStockTransDetail(actionIDs);
                stockTransDTO.setListStockTransDetail(lstDetail);
            }
            return FileUtil.exportToStreamed(fileExportBean);
//            FileUtil.exportFile(fileExportBean);
//            return fileExportBean.getExportFileContent();
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
        return null;
    }

    public boolean getShowCreateMultiExportNote() {
        return !DataUtil.isNullOrEmpty(selectedStockTransActions);
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
}

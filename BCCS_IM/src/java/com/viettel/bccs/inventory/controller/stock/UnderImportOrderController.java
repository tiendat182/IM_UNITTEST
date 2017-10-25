package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
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
 * Created by thaont19 on 11/9/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class UnderImportOrderController extends TransCommonController {
    private Boolean disableRegionStock = true;
    private Boolean disableBtnImport = false;
    private Boolean infoOrderDetail = false;
    private Boolean infoRejectDetail = false;
    private Boolean writeOffice = false;
    private Boolean shopTranfer = false;//kho dieu chuyen
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;
    private boolean autoCreateNoteFile;


    private StockTransInputDTO transInputDTO;
    private StaffDTO staffDTO;
    private ShopDTO shopDTO;
    private List<String> listStatus;

    private List<ReasonDTO> importReasonDTOsList;

    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<StockTransFullDTO> lsStockTransFull;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private SignOfficeTagNameable signOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa

    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ShopInfoNameable shopInfoTagTranfer;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StaffService staffService;

    @PostConstruct
    public void init() {
        try {

            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            shopDTO = shopService.findOne(staffDTO.getShopId());
            vStockTransDTOList = Lists.newArrayList();
            shopInfoTagReceive.resetShop();
            shopInfoTagExport.resetShop();
            Date currentDate = Calendar.getInstance().getTime();
            forSearch = new VStockTransDTO(currentDate, currentDate);
            //set mac dinh cho cboStatus
            forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerID(shopDTO.getShopId());

            infoOrderDetail = false;
            infoRejectDetail = false;
            transInputDTO = new StockTransInputDTO();


            //kho nhan: load mac dinh kho user dang nhap va set readonly
            shopInfoTagReceive.initShop(this, DataUtil.safeToString(shopDTO.getShopId()), true);
            shopInfoTagReceive.loadShop(DataUtil.safeToString(shopDTO.getShopId()), true);
            //kho xuat: load ds kho cap duoi cua kho user dang nhap
            shopInfoTagExport.initShop(this, DataUtil.safeToString(shopDTO.getShopId()), false);

            //lay danh sach trang thai: 3,4,5,6,8
            listStatus = new ArrayList<>();
            listStatus.add(Const.STOCK_TRANS_STATUS.EXPORTED);
            listStatus.add(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            listStatus.add(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            listStatus.add(Const.STOCK_TRANS_STATUS.IMPORTED);
            listStatus.add(Const.STOCK_TRANS_STATUS.REJECT);
            optionSetValueDTOsList = optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_TRANS_STATUS, listStatus);

            signOfficeTag.init(this, BccsLoginSuccessHandler.getStaffDTO().getShopId());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK,
                    Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP, Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
            orderStockTag.init(this, writeOffice);

            //danh sách hang hoa
            listProductTag.setAddNewProduct(false);
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }


    @Secured("@")
    public void doSearchStockTrans() {
        try {
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            forSearch.setVtUnit(Const.VT_UNIT.VT);
            forSearch.setUserShopId(staffDTO.getShopId());
            forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
            forSearch.setLstStatus(listStatus);
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
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
        infoRejectDetail = false;
        orderStockTag.resetOrderStock();
        lsStockTransFull = Lists.newArrayList();
        doSearchStockTrans();
    }

    @Secured("@")
    public void doExportShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setFromOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doTranferShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            transInputDTO.setExchangeStockId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    private void doValidate() throws Exception {
        //nhan vien dang nhap ton tai trong db
        StaffDTO staff = staffService.getStaffByStaffCode(staffDTO.getStaffCode());
        if (DataUtil.isNullObject(staff)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid");
        }
        //shopid cua nhan vien dang nhap = toOwnerId
        StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
        if (DataUtil.isNullObject(staff.getShopId()) || !staff.getShopId().equals(stockTransDTO.getToOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.shop.not.permission");
        }
    }

    @Secured("@")
    public void doCreateImportOrder() {
        try {
            doValidate();

            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setImportNote(transInputDTO.getNote());
            stockTransDTO.setImportReasonId(transInputDTO.getReasonId());
            stockTransDTO.setActionCodeNote(this.actionCodeNote);
            stockTransDTO.setRegionStockId(DataUtil.isNullOrZero(transInputDTO.getRegionStockId()) ? null : transInputDTO.getRegionStockId());
            stockTransDTO.setExchangeStockId(DataUtil.isNullOrZero(transInputDTO.getExchangeStockId()) ? null : transInputDTO.getExchangeStockId());
            stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);//fix validate kho xuat phai la cap tren lien ke kho nhan


            //StockTransAction
            stockTransActionDTO.setNote(transInputDTO.getNote());
            stockTransActionDTO.setActionCode(transInputDTO.getActionCode());
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setCreateDatetime(new Date());

            //ky voffice
            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = signOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }


            //lay danh sach chi tiet hang hoa
            List<StockTransDetailDTO> listStockTransDetailDTOs = listProductTag.getListTransDetailDTOs();

            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.atutogen.fail");
            }


            BaseMessage message = executeStockTransService.executeStockTrans(showAutoOrderNote ? Const.STOCK_TRANS.ORDER_AND_NOTE : Const.STOCK_TRANS.ORDER,
                    Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, listStockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            disableBtnImport = true;
            if (showAutoOrderNote) {
                reportSuccess("frmExportOrder:msgExport", "import.order.note.create.success");
            } else {
                reportSuccess("frmExportOrder:msgExport", "mn.stock.underlying.createOrderImport.success");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        topPage();
    }

    @Secured("@")
    public void doRejectImportOrder() {
        try {
            doValidate();

            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setRejectNote(transInputDTO.getNote());
            stockTransDTO.setRejectReasonId(transInputDTO.getReasonId());

            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());
            stockTransActionDTO.setCreateDatetime(new Date());
            stockTransActionDTO.setActionCode(null);

            //lay danh sach chi tiet hang hoa
            List<StockTransDetailDTO> listStockTransDetailDTOs = listProductTag.getListTransDetailDTOs();

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.REJECT_TRANS, null, stockTransDTO, stockTransActionDTO, listStockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            reportSuccess("frmExportOrder:msgExport", "mn.stock.underlying.rejectOrderImport.success");
            disableBtnImport = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        topPage();
    }

    @Secured("@")
    public void showInfoOrderDetail(Long stockTransActionId) {
        try {
            showAutoOrderNote = false;
            autoCreateNoteFile = false;
            //Load thong tin phieu xuat
            orderStockTag.loadOrderStock(stockTransActionId, true);

            //outsource
            orderStockTag.setEnableTransfer(true);
            //end outsource
            infoOrderDetail = true;
            disableBtnImport = false;
            //an thong tin kho 3 mien, dieu chuyen neu ko phai la kho VT
            /*if (!Const.VT_SHOP_ID.equals(DataUtil.safeToString(staffDTO.getShopId()))) {
                disableRegionStock = true;
            } else {
                disableRegionStock = false;
            }*/

            //Load thong tin lenh nhap
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_LN, Const.STOCK_TRANS_TYPE.IMPORT, staffDTO));
            transInputDTO.setCreateUser(staffDTO.getName());
            transInputDTO.setCreateDatetime(new Date());
            importReasonDTOsList = reasonService.getLsReasonByCode(Const.REASON_TYPE.STOCK_IMP_UNDER); //Ly do nhap
            shopInfoTagTranfer.initShop(this, DataUtil.safeToString(staffDTO.getShopId()), false); //Kho dieu chuyen
            shopTranfer = false;
            transInputDTO.setNote("");
            writeOffice = true;

            //clear form
            transInputDTO.setReasonId(null);
            transInputDTO.setRegionStockId(null);
            transInputDTO.setExchangeStockId(null);
            transInputDTO.setNote(null);

            //Load danh sach hang hoa
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, "", "");
            configListProductTagDTO.setShowSerialView(true);
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);
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
    public void showInfoRejectDetail(Long stockTransActionId) {
        try {
            //Load thong tin phieu xuat
            orderStockTag.loadOrderStock(stockTransActionId, true);

            infoRejectDetail = true;
            disableBtnImport = false;

            //Load danh sach hang hoa
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW, "", "");
            configListProductTagDTO.setShowSerialView(true);
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);

            //Load thong tin tu choi
            importReasonDTOsList = reasonService.getLsReasonByCode(Const.REASON_CODE.IMP_UNDER_REJECT);//lu do tu choi
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

    @Secured("@")
    public StreamedContent exportHandOverReport() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttohanover");
            }

            StreamedContent content = exportHandOverReport(stockTransDTO);
            return content;
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
    public void changeShopTranfer() {
        //clear va disable kho 3 mien
        transInputDTO.setReasonId(null);
        transInputDTO.setRegionStockId(null);
        transInputDTO.setExchangeStockId(null);
        if (shopTranfer) {
            //enable kho dieu chuyen,
            shopInfoTagTranfer.setIsDisableEdit(false);
        } else {
            //disable va clear kho dieu chuyen,
            shopInfoTagTranfer.setIsDisableEdit(true);
            // endable kho 3 mien
            shopInfoTagTranfer.resetShop();
        }
    }

    /**
     * ham check hien thi check box lap phieu tu dong
     *
     * @return
     * @author thanhnt77
     */
    public Boolean getRoleCheckShowAutoOrderNote() {
        return requiredRoleMap != null && requiredRoleMap.hasRole(Const.PERMISION.ROLE_AUTO_ORDER_NOTE);
    }

    /**
     * an hien thi khi click vao checkBox tao phieu tu dong
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doChangeAutoOrderNotefile() {
        try {
            showAutoOrderNote = autoCreateNoteFile;
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
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionCode(transInputDTO.getActionCode());
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            lsStockTransFull = listProductTag.getLsStockTransFull();
            stockTransDTO.setStockTransActionId(null);
            return exportStockTransDetail(stockTransDTO, lsStockTransFull);
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
    public StreamedContent doPrintStockTransDetailNote() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionCode(actionCodeNote);
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            lsStockTransFull = listProductTag.getLsStockTransFull();
            stockTransDTO.setStockTransActionId(null);
            return exportStockTransDetail(stockTransDTO, lsStockTransFull);
        } catch (LogicException ex) {
            topReportError("", ex);
            topPage();
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
            topPage();
        }
        return null;
    }

    //getter and setter

    public Boolean getInfoRejectDetail() {
        return infoRejectDetail;
    }

    public void setInfoRejectDetail(Boolean infoRejectDetail) {
        this.infoRejectDetail = infoRejectDetail;
    }

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

    public SignOfficeTagNameable getSignOfficeTag() {
        return signOfficeTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
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

    public List<ReasonDTO> getImportReasonDTOsList() {
        return importReasonDTOsList;
    }

    public void setImportReasonDTOsList(List<ReasonDTO> importReasonDTOsList) {
        this.importReasonDTOsList = importReasonDTOsList;
    }

    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
    }

    public ShopInfoNameable getShopInfoTagTranfer() {
        return shopInfoTagTranfer;
    }

    public void setShopInfoTagTranfer(ShopInfoNameable shopInfoTagTranfer) {
        this.shopInfoTagTranfer = shopInfoTagTranfer;
    }

    public Boolean getShopTranfer() {
        return shopTranfer;
    }

    public void setShopTranfer(Boolean shopTranfer) {
        this.shopTranfer = shopTranfer;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public Boolean getDisableBtnImport() {
        return disableBtnImport;
    }

    public void setDisableBtnImport(Boolean disableBtnImport) {
        this.disableBtnImport = disableBtnImport;
    }

    public Boolean getDisableRegionStock() {
        return disableRegionStock;
    }

    public void setDisableRegionStock(Boolean disableRegionStock) {
        this.disableRegionStock = disableRegionStock;
    }

    public boolean isAutoCreateNoteFile() {
        return autoCreateNoteFile;
    }

    public void setAutoCreateNoteFile(boolean autoCreateNoteFile) {
        this.autoCreateNoteFile = autoCreateNoteFile;
    }
}

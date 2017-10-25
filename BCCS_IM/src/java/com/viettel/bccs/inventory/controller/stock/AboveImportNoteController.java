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
@ManagedBean(name = "aboveImportNoteController")
public class AboveImportNoteController extends TransCommonController {
    private Boolean disableBtnImport = false;
    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = false;
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private String actionCodeNote;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;

    private Long importReasonId;
    private String importUserName;
    private String importUserId;
    private String importNote;
    private Date importDate;
    private StaffDTO staffDTO;
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
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @PostConstruct
    public void init() {
        try {
            forSearch = new VStockTransDTO();
            vStockTransDTOList = Lists.newArrayList();
            shopInfoTagReceive.resetShop();
            shopInfoTagExport.resetShop();
            Date currentDate = Calendar.getInstance().getTime();
            forSearch = new VStockTransDTO(currentDate, currentDate);
            //set mac dinh cho cboStatus
            forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
            infoOrderDetail = false;

            staffDTO = BccsLoginSuccessHandler.getStaffDTO();

            ShopDTO shopDTO = shopService.findOne(staffDTO.getShopId());
            shopInfoTagReceive.initShop(this, DataUtil.safeToString(shopDTO.getShopId()), false);
            shopInfoTagReceive.loadShop(DataUtil.safeToString(shopDTO.getShopId()), true);

            shopInfoTagExport.initShop(this, DataUtil.safeToString(shopDTO.getParentShopId()), false);
            shopInfoTagExport.loadShop(DataUtil.safeToString(shopDTO.getParentShopId()), true);

            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setFromOwnerID(shopDTO.getParentShopId());
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerID(shopDTO.getShopId());

            //lay danh sach trang thai: 3,5,6
            listStatus = new ArrayList<>();
            listStatus.add(Const.STOCK_TRANS_STATUS.EXPORTED);
            listStatus.add(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            listStatus.add(Const.STOCK_TRANS_STATUS.IMPORTED);
            optionSetValueDTOsList = optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_TRANS_STATUS, listStatus);
            signOfficeTag.init(this, BccsLoginSuccessHandler.getStaffDTO().getShopId());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK,
                    Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            orderStockTag.init(this, writeOffice);

            //danh sách hang hoa
            listProductTag.setAddNewProduct(false);
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


    @Secured("@")
    public void doSearchStockTrans() {
        try {
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            forSearch.setLstStatus(listStatus);
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
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
    public void doValidateCreateImportNote() {
        if (!DataUtil.validateStringByPattern(actionCodeNote, "[0-9a-zA-Z_-]{1,50}")) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.import.transCode.error.format.msg");
        }
    }

    @Secured("@")
    public void doCreateImportNote() {
        try {
            //validate
            doValidate();

            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setImportNote(importNote);
            stockTransDTO.setNote(importNote);//set cho stockTransAction.note trong baseService
            stockTransDTO.setImportReasonId(importReasonId);

            //StockTransAction
            stockTransActionDTO.setActionCode(actionCodeNote);
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());

            if (writeOffice) {
                SignOfficeDTO signOfficeDTO = signOfficeTag.validateVofficeAccount();
                stockTransDTO.setUserName(signOfficeDTO.getUserName());
                stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                stockTransDTO.setSignVoffice(writeOffice);
            }

            //lay danh sach chi tiet hang hoa
            List<StockTransDetailDTO> listStockTransDetailDTOs = listProductTag.getListTransDetailDTOs();

//            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
//                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
//                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.atutogen.fail", getText("mn.stock.underlying.createNoteImport"));
//            }

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE, Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, listStockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("", "import.note.create.succes");
            disableBtnImport = true;
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public StreamedContent doPrintStockTransDetail() {
        try {
            orderStockTag.getTransInputDTO().setReasonId(importReasonId);
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.IMPORT);
            stockTransDTO.setActionCode(actionCodeNote);
            stockTransDTO.setNote(importNote);
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
    public void showInfoOrderDetail(Long stockTransActionId) {
        try {
            //Validate ky voffice
            StockTransActionDTO actionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);
            orderStockTag.loadOrderStock(stockTransActionId, true);
            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            writeOffice = true;
            disableBtnImport = false;
            //sinh ma phiep nhap tu dong
            actionCodeNote = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO());
            //load nguoi nhan
            importUserName = BccsLoginSuccessHandler.getStaffDTO().getName();
            //load ngay lap phieu
            importDate = new Date();
            //load ds ly do nhap
            importReasonDTOsList = reasonService.getLsReasonByCode(Const.REASON_CODE.STOCK_IMP_SERNIOR);

            //clear form
            importReasonId = null;
            importNote = null;

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

    public Long getImportReasonId() {
        return importReasonId;
    }

    public void setImportReasonId(Long importReasonId) {
        this.importReasonId = importReasonId;
    }

    public String getImportUserName() {
        return importUserName;
    }

    public void setImportUserName(String importUserName) {
        this.importUserName = importUserName;
    }

    public String getImportUserId() {
        return importUserId;
    }

    public void setImportUserId(String importUserId) {
        this.importUserId = importUserId;
    }

    public String getImportNote() {
        return importNote;
    }

    public void setImportNote(String importNote) {
        this.importNote = importNote;
    }

    public List<ReasonDTO> getImportReasonDTOsList() {
        return importReasonDTOsList;
    }

    public void setImportReasonDTOsList(List<ReasonDTO> importReasonDTOsList) {
        this.importReasonDTOsList = importReasonDTOsList;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Boolean getDisableBtnImport() {
        return disableBtnImport;
    }

    public void setDisableBtnImport(Boolean disableBtnImport) {
        this.disableBtnImport = disableBtnImport;
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
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
import java.util.*;

/**
 * Created by thaont19 on 11/9/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class UnderImportNoteController extends TransCommonController {
    private String regionExchangeLabel = null;
    private Boolean disableBtnImport = false;
    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = false;
    private Boolean linkDownload = false;
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;
    private StockTransInputDTO transInputDTO;
    private StaffDTO staffDTO;
    private ShopDTO shopDTO;
    private List<String> listStatus;

    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<VStockTransDTO> selectedStockTransActions;
    List<StockTransFileDTO> stockTransFileDTOList = Lists.newArrayList();
    private List<FieldExportFileDTO> exportFileResult = Lists.newArrayList();


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
    private ExecuteStockTransService executeStockTransService;

    @Autowired
    private StockTransDetailService stockTransDetailService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private ListProductNameable listProductTag;

    @PostConstruct
    public void init() {
        try {

            staffDTO = BccsLoginSuccessHandler.getStaffDTO();

            forSearch = new VStockTransDTO();
            vStockTransDTOList = Lists.newArrayList();
            shopInfoTagReceive.resetShop();
            shopInfoTagExport.resetShop();
            Date currentDate = Calendar.getInstance().getTime();
            forSearch = new VStockTransDTO(currentDate, currentDate);
            //set mac dinh cho cboStatus
            forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            infoOrderDetail = false;
            transInputDTO = new StockTransInputDTO();

            shopDTO = shopService.findOne(staffDTO.getShopId());

            //kho nhan: load mac dinh kho user dang nhap va set readonly
            shopInfoTagReceive.initShop(this, DataUtil.safeToString(shopDTO.getShopId()), true);
            shopInfoTagReceive.loadShop(DataUtil.safeToString(shopDTO.getShopId()), true);
            //kho xuat: load ds kho cap duoi cua kho user dang nhap
            shopInfoTagExport.initShop(this, DataUtil.safeToString(shopDTO.getShopId()), false);


            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerID(shopDTO.getShopId());

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
                    Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN, Const.PERMISION.ROLE_STOCK_NUM_SHOP);
            orderStockTag.init(this, writeOffice);

            listProductTag.setAddNewProduct(false);
            showDialog("guide");
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            forSearch.setActionType(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
            forSearch.setVtUnit(Const.VT_UNIT.VT);
            forSearch.setUserShopId(staffDTO.getShopId());
            forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
            forSearch.setLstStatus(listStatus);
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);

            //clear checkbox va set gia tri ky voffice
            selectedStockTransActions = Lists.newArrayList();
            writeOffice = true;
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
    public void doCreateImportNote() {
        try {
            doValidate();

            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setNote(null);
            stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);//fix validate kho xuat phai la cap tren lien ke kho nhan

            //StockTransAction
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            stockTransActionDTO.setActionCode(transInputDTO.getActionCode());
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

            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.create.order.atutogen.fail");
            }

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.NOTE, Const.STOCK_TRANS_TYPE.IMPORT, stockTransDTO, stockTransActionDTO, listStockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("", "import.note.create.succes");

            disableBtnImport = true;
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
    public void doCreateMutilImportNote() {
        try {
            stockTransFileDTOList = Lists.newArrayList();
            SignOfficeDTO signOfficeDTO = null;
            if (writeOffice) {
                signOfficeDTO = signOfficeTag.validateVofficeAccount();
            }
            //set gia tri cho list stockTrans
            String actionCode = staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO());
            int index = actionCode.lastIndexOf("_") + 1;
            String prefix = actionCode.substring(0, index);
            String posfix = actionCode.substring(index);
            Long noteNumber = DataUtil.safeToLong(posfix);


            //set gia tri cho list stockTrans
            for (VStockTransDTO vStockTransDTO : selectedStockTransActions) {
                StockTransFileDTO stockTransFileDTO = new StockTransFileDTO();
                //set gia tri cho stockTrans
                StockTransDTO stockTransDTO = genStockTransDTO(vStockTransDTO);
                stockTransDTO.setNote(null);
                stockTransDTO.setStaffId(staffDTO.getStaffId());
                stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);//fix validate kho xuat phai la cap tren lien ke kho nhan
                //Kiem tra kho 3 mien, dieu chuyen
                StockTransDTO mainStockTrans = stockTransService.findOne(stockTransDTO.getStockTransId());
                if (!DataUtil.isNullOrZero(mainStockTrans.getRegionStockTransId())) {
                    StockTransDTO regionTrans = stockTransService.findOne(mainStockTrans.getRegionStockTransId());
                    ShopDTO shopDTO = shopService.findOne(regionTrans.getToOwnerId());
                    if (DataUtil.safeEqual(regionTrans.getIsAutoGen(), 1L)) {
                        stockTransDTO.setRegionStockId(shopDTO.getShopId());
                    } else {
                        stockTransDTO.setExchangeStockId(shopDTO.getShopId());
                        StockTransActionDTO stockTransActionExchange = stockTransActionService.getStockTransActionByIdAndStatus(regionTrans.getStockTransId(),
                                Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
                        String validOffice = validateOfficeMulti(stockTransActionExchange);
                        if (!DataUtil.isNullOrEmpty(validOffice)) {
                            stockTransFileDTO.setMsgError(validOffice);
                        }
                    }
                    stockTransDTO.setRegionStockTransId(mainStockTrans.getRegionStockTransId());
                }
                //set gia tri cho stockTransAction
                StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(vStockTransDTO.getActionID());
                String validOffice = validateOfficeMulti(stockTransActionDTO);

                if (!DataUtil.isNullOrEmpty(validOffice)) {
                    stockTransFileDTO.setMsgError(validOffice);
                } else if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                        || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                        || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                    stockTransFileDTO.setMsgError(getTextParam("mn.stock.underlying.not.create.order.atutogen.fail", getText("staff.import.note.create")));
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

            List<BaseExtMessage> lsMessage = executeStockTransService.executeStockTransList(Const.STOCK_TRANS.NOTE, Const.STOCK_TRANS_TYPE.IMPORT, stockTransFileDTOList, requiredRoleMap);

            Map mapResult = convertListErrorFile(lsMessage, "export.note.create.success");
            boolean isValidResult = (boolean) mapResult.get(CREATE_MULTI_DATA_KEY_STATUS);
            exportFileResult = (List<FieldExportFileDTO>) mapResult.get(CREATE_MULTI_DATA_KEY_LIST);
            if (isValidResult) {
                reportSuccess("", "stock.mutil.note.success.file.detail");
            } else {
                reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.multi.import.stock");
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

    @Secured("@")
    public void showInfoOrderDetail(Long stockTransActionId) {
        try {
            //Validate ky voffice
            StockTransActionDTO actionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);
            //validate ki voffice cho giao dich dieu chuyen
            StockTransDTO stockTransDTO = stockTransService.findStockTransByActionId(stockTransActionId);
            if (!DataUtil.isNullOrZero(stockTransDTO.getRegionStockTransId())) {
                StockTransDTO stockTransExchange = stockTransService.findOne(stockTransDTO.getRegionStockTransId());
                if (!DataUtil.isNullObject(stockTransExchange) && DataUtil.safeEqual(stockTransExchange.getIsAutoGen(), Const.STOCK_TRANS.IS_TRANSFER)) {
                    StockTransActionDTO stockTransActionExchange = stockTransActionService.getStockTransActionByIdAndStatus(stockTransExchange.getStockTransId(),
                            Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
                    stockTransVofficeService.doSignedVofficeValidate(stockTransActionExchange);
                }
            }

            //Load thong tin lenh nhap
            orderStockTag.loadOrderStockImport(stockTransActionId, true);
            //Load ten kho 3 mien
            orderStockTag.setNameThreeRegion("2");
            if (!DataUtil.isNullOrZero(orderStockTag.getTransInputDTO().getRegionStockId())) {
                regionExchangeLabel = getText("export.order.stock.three.province");
            } else if (!DataUtil.isNullOrZero(orderStockTag.getTransInputDTO().getExchangeStockId())) {
                regionExchangeLabel = getText("tranfer.order.stock");
            }

            infoOrderDetail = true;
            disableBtnImport = false;
            writeOffice = true;
            //sinh ma phiep nhap tu dong
            transInputDTO.setActionCode(staffService.getTransCode(Const.STOCK_TRANS.TRANS_CODE_PN, Const.STOCK_TRANS_TYPE.IMPORT, BccsLoginSuccessHandler.getStaffDTO()));
//            StockTransDTO stockTransDTO = stockTransService.findOne(actionDTO.getStockTransId());

            //clear form
            transInputDTO.setNote(null);

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
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionCode(transInputDTO.getActionCode());
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


    public StockTransInputDTO getTransInputDTO() {
        return transInputDTO;
    }

    public void setTransInputDTO(StockTransInputDTO transInputDTO) {
        this.transInputDTO = transInputDTO;
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

    public String getRegionExchangeLabel() {
        return regionExchangeLabel;
    }

    public void setRegionExchangeLabel(String regionExchangeLabel) {
        this.regionExchangeLabel = regionExchangeLabel;
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

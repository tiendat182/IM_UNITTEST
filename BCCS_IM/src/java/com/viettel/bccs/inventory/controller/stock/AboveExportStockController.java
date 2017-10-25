package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.fw.logging.Kpi;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListFifoProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
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

public class AboveExportStockController extends TransCommonController {
    private boolean showPanelOffline;
    private boolean checkOffline;
    private Boolean disableBtnImport = false;
    private Boolean infoOrderDetail = false;
    private int limitAutoComplete;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;

    private StaffDTO staffDTO;
    private ShopDTO shopDTO;
    private List<String> listStatus;


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
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ListFifoProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockTransDetailService stockTransDetailService;

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
            forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setFromOwnerID(shopDTO.getShopId());
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerID(shopDTO.getParentShopId());

            infoOrderDetail = false;

            shopInfoTagReceive.initShop(this, DataUtil.safeToString(shopDTO.getParentShopId()), false);
            shopInfoTagReceive.loadShop(DataUtil.safeToString(shopDTO.getParentShopId()), true);

            shopInfoTagExport.initShop(this, DataUtil.safeToString(shopDTO.getShopId()), false);
            shopInfoTagExport.loadShop(DataUtil.safeToString(shopDTO.getShopId()), true);

            //lay danh sach trang thai: 0,2,3,7
            listStatus = new ArrayList<>();
            listStatus.add(Const.STOCK_TRANS_STATUS.PROCESSING);
            listStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            listStatus.add(Const.STOCK_TRANS_STATUS.EXPORTED);
            listStatus.add(Const.STOCK_TRANS_STATUS.DESTROYED);
            optionSetValueDTOsList = optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_TRANS_STATUS, listStatus);

            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
            orderStockTag.init(this, false);

            //danh sách hang hoa
            listProductTag.setAddNewProduct(false);
            showDialog("guide");
        } catch (Exception e) {
            logger.error(e);
            reportError("frmExportOrder:msgExport", "common.error.happened", e);
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
            reportError("frmExportOrder:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
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

    private void doValidate() throws Exception {
        //nhan vien dang nhap ton tai trong db
        StaffDTO staff = staffService.getStaffByStaffCode(staffDTO.getStaffCode());
        if(DataUtil.isNullObject(staff)){
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.code.invalid");
        }
        //shopid cua nhan vien dang nhap = toOwnerId
        StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
        if(DataUtil.isNullObject(staff.getShopId()) || !staff.getShopId().equals(stockTransDTO.getFromOwnerId())){
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staff.shop.not.permission");
        }
    }

    @Kpi("ID_KPI_ABOVE_EXP")
    @Secured("@")
    public void doCreateExportStock() {
        try {
            doValidate();

            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            stockTransActionDTO.setActionStaffId(staffDTO.getStaffId());
            stockTransActionDTO.setCreateUser(staffDTO.getStaffCode());


            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();

            if (stockTransDTO.getIsAutoGen() != null && (Const.STOCK_TRANS.IS_TRANSFER.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC.equals(stockTransDTO.getIsAutoGen())
                    || Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION.equals(stockTransDTO.getIsAutoGen()))) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.not.destroy.order.atutogen.fail", getText("mn.stock.underlying.export.stock"));
            }
            stockTransDTO.setNote(null);
            stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);
            stockTransDTO.setProcessOffline(checkOffline ? Const.PROCESS_OFFLINE : "");


            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
            List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {

                if (Const.PRODUCT_OFFERING.CHECK_SERIAL.equals(DataUtil.safeToLong(stockTransFullDTO.getCheckSerial())) &&
                        DataUtil.isNullOrEmpty(stockTransFullDTO.getLstSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.confirm.serial.empty", stockTransFullDTO.getProdOfferName());
                }

                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransId(stockTransFullDTO.getStockTransId());
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
                stockTransDetailDTO.setPrice(stockTransFullDTO.getPrice());
                stockTransDetailDTO.setAmount(stockTransFullDTO.getAmount());
                stockTransDetailDTO.setCreateDatetime(new Date());
                stockTransDetailDTO.setCheckSerial(DataUtil.safeToLong(stockTransFullDTO.getCheckSerial()));

                lsDetailDTOs.add(stockTransDetailDTO);
            }

            //validate xuat kho offline
            int configOffline = Const.PROCESS_OFFLINE_SIZE;
            List<OptionSetValueDTO> lstConfigOffline = optionSetValueService.getByOptionSetCode("MIN_QUANTITY_EXPORT_OFFLINE");
            if(!DataUtil.isNullOrEmpty(lstConfigOffline) && !DataUtil.isNullObject(lstConfigOffline.get(0).getValue()) ){
                configOffline = DataUtil.safeToInt(lstConfigOffline.get(0).getValue());
            }
            List<StockTransFullDTO> lstDetailDB = stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId()));
            Long total = 0L;
            for (StockTransFullDTO stock : lstDetailDB) {
                if(!DataUtil.isNullObject(stock.getCheckSerial()) && stock.getCheckSerial().equals(Const.PRODUCT_OFFERING._CHECK_SERIAL)){
                    total += DataUtil.safeToLong(stock.getQuantity());
                }
            }
            if(total < configOffline && checkOffline){
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.underlying.export.stock.offline.invalide");
            }

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(),message.getParamsMsg());
            }
            reportSuccess("frmExportOrder:msgExport", "export.stock.success");
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
    public void showInfoOrderDetail(Long stockTransActionId) {
        try {
            //Validate ky voffice
            StockTransActionDTO actionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);

            orderStockTag.loadOrderStock(stockTransActionId, true);
            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            disableBtnImport = false;

            //khoi tao tag chi tiet serial
            ConfigListProductTagDTO configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_SERIAL, Const.BRAS_IPPOOL.RESOURCE_TEMPLATE_PATH + Const.FILE_TEMPLATE.FILE_NAME_TEMPLATE, Const.FILE_TEMPLATE.FILE_NAME_TEMPLATE);
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);

            List<StockTransDetailDTO> lsDetail = listProductTag.getListTransDetailDTOs();
            Long total = 0L;
            for (StockTransDetailDTO stock : lsDetail) {
                if(!DataUtil.isNullObject(stock.getCheckSerial()) && stock.getCheckSerial().equals(1L)){
                    total += DataUtil.safeToLong(stock.getQuantity());
                }
            }
            //lay cau hinh xuat OFFLINE
            int configOffline = Const.PROCESS_OFFLINE_SIZE;
            List<OptionSetValueDTO> lstConfigOffline = optionSetValueService.getByOptionSetCode("MIN_QUANTITY_EXPORT_OFFLINE");
            if(!DataUtil.isNullOrEmpty(lstConfigOffline) && !DataUtil.isNullObject(lstConfigOffline.get(0).getValue()) ){
                configOffline = DataUtil.safeToInt(lstConfigOffline.get(0).getValue());
            }

            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();

            showPanelOffline = (total >= configOffline) && (DataUtil.isNullObject(stockTransDTO.getRegionStockTransId()));
            checkOffline=false;

            //xu ly load han muc kho nhan neu kho nhan khong phai la VT
            if (!(Const.SHOP.SHOP_VTT_ID.equals(stockTransDTO.getToOwnerId()) || Const.SHOP.SHOP_PARENT_VTT_ID.equals(stockTransDTO.getToOwnerId()))) {
                if (stockTransDTO.getToOwnerId() != null && stockTransDTO.getToOwnerType() != null) {
                    stockDebitDTO = stockDebitService.findStockDebitValue(DataUtil.safeToLong(stockTransDTO.getToOwnerId()), DataUtil.safeToString(stockTransDTO.getToOwnerType()));
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrder:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            reportError("frmExportOrder:msgExport", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
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

    @Secured("@")
    public void doDestroyStock(Long stockTransActionId){
        try {
            doValidate();

            StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransActionId);

            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());
            stockTransDTO.setFromStock(Const.FROM_STOCK.FROM_UNDERLYING);
            stockTransDTO.setNote(null);

            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS, null, stockTransDTO, stockTransActionDTO, new ArrayList<StockTransDetailDTO>(), requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmExportOrder:msgExport", "stock.export.cancel.success");
            doSearchStockTrans();

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
    public StreamedContent doPrintStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
            return exportStockTransDetail(stockTransDTO, lsStockTransFull);
        } catch (LogicException ex) {
            topReportError("frmExportOrder:msgExport", ex);
        } catch (Exception ex) {
            topReportError("frmExportOrder:msgExport", "common.error.happened", ex);
        }
        return null;
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

    //getter and setter
    public Boolean getInfoOrderDetail() {
        return infoOrderDetail;
    }

    public void setInfoOrderDetail(Boolean infoOrderDetail) {
        this.infoOrderDetail = infoOrderDetail;
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


    public ListFifoProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListFifoProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public Boolean getDisableBtnImport() {
        return disableBtnImport;
    }

    public void setDisableBtnImport(Boolean disableBtnImport) {
        this.disableBtnImport = disableBtnImport;
    }

    public boolean getCheckOffline() {
        return checkOffline;
    }

    public void setCheckOffline(boolean checkOffline) {
        this.checkOffline = checkOffline;
    }

    public boolean getShowPanelOffline() {
        return showPanelOffline;
    }

    public void setShowPanelOffline(boolean showPanelOffline) {
        this.showPanelOffline = showPanelOffline;
    }
}

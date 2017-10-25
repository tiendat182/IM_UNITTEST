package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author hoangnt
 */
@Component
@Scope("view")
@ManagedBean(name = "stockRescueWarrantyController")
public class StockRescueWarrantyController extends TransCommonController {
    private StockTransRescueDTO stockTransRescueDTOSearch;
    private StockTransRescueDTO stockTransRescueAdd;
    private StockTransRescueDTO stockTransRescueAction;
    private List<StockTransRescueDTO> lstSearch = Lists.newArrayList();
    private List<StockTransDetailRescueDTO> lstDetail = Lists.newArrayList();
    private List<StockTransSerialRescueDTO> lstDetailGive = Lists.newArrayList();
    private List<StockTransSerialRescueDTO> lstDetailSerial = Lists.newArrayList();
    private List<StockHandsetRescueDTO> lstStockHansetRescue = Lists.newArrayList();
    private List<StockHandsetRescueDTO> lstSelection = Lists.newArrayList();
    private List<OptionSetValueDTO> statusList;
    private StaffDTO staffDTO;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransRescueService stockTransRescueService;
    @Autowired
    private StockTransDetailRescueService stockTransDetailRescueService;
    @Autowired
    private StockTransSerialRescueService stockTransSerialRescueService;
    @Autowired
    private StockHandsetRescueService stockHandsetRescueService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopService shopService;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;

    private int limitAutoComplete;
    private boolean showDetail;
    private boolean showDetailSerial;
    private boolean createRequest;
    private VShopStaffDTO vShopDTO = null;
    private boolean download;
    private RequiredRoleMap requiredRoleMap;
    private boolean permission;
    private boolean roleExport;
    private boolean roleReceive;
    private boolean giveProduct;
    private boolean disableGive;
    private boolean receiveProduct;

    @PostConstruct
    public void init() {
        try {
            permission = true;
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY, Const.PERMISION.ROLE_RECEIVE_WARRANTY);
            if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY) && !requiredRoleMap.hasRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY)) {
                permission = false;
                reportError("", "", "stock.rescue.warranty.not.permission");
                return;
            }
            if (requiredRoleMap.hasRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY) && requiredRoleMap.hasRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY)) {
                permission = false;
                reportError("", "", "stock.rescue.warranty.permission.invalid");
                return;
            }
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            showDetail = false;
            createRequest = false;
            giveProduct = false;
            receiveProduct = false;
            statusList = optionSetValueService.getByOptionSetCode(Const.STOCK_TRANS_RESCUE.WARRANTY_STATUS);
            stockTransRescueDTOSearch = new StockTransRescueDTO();
            stockTransRescueAction = new StockTransRescueDTO();
            Calendar cal = Calendar.getInstance();   // this takes current date
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            stockTransRescueDTOSearch.setToDate(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, 1);
            stockTransRescueDTOSearch.setFromDate(cal.getTime());
            doSearchStockRescue();

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public boolean permissionCreateRequest() {
        if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY)) {
            return false;
        }
        return true;
    }

    @Secured("@")
    public void resetRequestCode() {
        try {
            stockTransRescueAdd.setRequestCode("RC_" + stockTransRescueService.getMaxId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doSearchStockRescue() {
        try {
            roleReceive = false;
            roleExport = false;
            if (requiredRoleMap.hasRole(Const.PERMISION.ROLE_EXPORT_RETURN_WARRANTY)) {
                roleExport = true;
                roleReceive = false;
                stockTransRescueDTOSearch.setFromOwnerId(staffDTO.getStaffId());
                stockTransRescueDTOSearch.setToOwnerId(null);
            }
            if (requiredRoleMap.hasRole(Const.PERMISION.ROLE_RECEIVE_WARRANTY)) {
                roleReceive = true;
                roleExport = false;
                stockTransRescueDTOSearch.setFromOwnerId(null);
                stockTransRescueDTOSearch.setToOwnerId(staffDTO.getShopId());
            }
            lstSearch = stockTransRescueService.searchStockRescue(stockTransRescueDTOSearch);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doReset() {
        download = false;
        initCreateRequest();
    }

    @Secured("@")
    public void doCreateRequest() {
        try {
            if (!DataUtil.isNullObject(vShopDTO)) {
                stockTransRescueAdd.setToOwnerId(DataUtil.safeToLong(vShopDTO.getOwnerId()));
            }
            stockTransRescueAdd.setFromOwnerId(staffDTO.getStaffId());
            stockTransRescueAdd.setFromOwnerType(Const.OWNER_TYPE.STAFF_LONG);
            stockTransRescueAdd.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransRescueAdd.setReasonId(stockTransRescueService.getReasonId());
            stockTransRescueAdd.setStatus(DataUtil.safeToLong(Const.STATUS_ACTIVE));
            stockTransRescueAdd.setLstSelection(lstSelection);
            stockTransRescueService.createRequest(stockTransRescueAdd, requiredRoleMap);
            download = true;
            reportSuccess("", "stock.rescue.warranty.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void validateCreateRequest() {
        try {
            if (DataUtil.isNullOrEmpty(lstSelection)) {
                throw new LogicException("", "stock.rescue.warranty.vaildate.selection");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    private void initCreateRequest() {
        try {
            lstSelection = Lists.newArrayList();
            List<Long> lstChannelTypes = shopService.getListChannelType();
            String[] lsShopId = staffDTO.getShopPath().split("_");
            String shopId;

            if (lsShopId.length < 4) {
                shopId = DataUtil.safeToString(staffDTO.getShopId());
            } else {
                shopId = lsShopId[3];
            }

            shopInfoTag.initShopAndAllChildWithChanelType(this, shopId, lstChannelTypes);
            stockTransRescueAdd = new StockTransRescueDTO();
            stockTransRescueAdd.setRequestCode("RC_" + stockTransRescueService.getMaxId());
            stockTransRescueAdd.setStaffRequest(staffDTO.getStaffCode());
            stockTransRescueAdd.setRequestDate(new Date());
            if (!DataUtil.isNullObject(vShopDTO)) {
                stockTransRescueAdd.setToOwnerId(DataUtil.safeToLong(vShopDTO.getOwnerId()));
            }
            lstStockHansetRescue = stockHandsetRescueService.getListHansetRescue(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doShowCreateRequest() {
        try {
            download = false;
            createRequest = true;
            initCreateRequest();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public void downloadFileExport() {

    }

    @Secured("@")
    public void downloadBBBG() {

    }

    @Secured("@")
    public void doComeBack() {
        showDetail = false;
        createRequest = false;
        giveProduct = false;
        receiveProduct = false;
        doSearchStockRescue();
    }

    @Secured("@")
    public void doViewDetail(Long stockTranId) {
        try {
            showDetail = true;
            showDetailSerial = false;
            lstDetail = stockTransDetailRescueService.viewDetail(stockTranId);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doViewDetailSerial(StockTransDetailRescueDTO stockTransDetailRescueDTO) {
        try {
            showDetailSerial = true;
            lstDetailSerial = stockTransSerialRescueService.viewDetailSerail(stockTransDetailRescueDTO.getStockTransId(), stockTransDetailRescueDTO.getProdOfferId(), null);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void deleteRequest() {
        try {
            stockTransRescueService.deleteStockRescue(stockTransRescueAction, requiredRoleMap);
            doSearchStockRescue();
            reportSuccess("", "stock.rescue.warranty.validate.delete.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void validateActionRequest(StockTransRescueDTO stockTransRescueAction) {
        try {
            this.stockTransRescueAction = stockTransRescueAction;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }


    @Secured("@")
    public void acceptRequest() {
        try {
            stockTransRescueService.acceptStockRescue(stockTransRescueAction, requiredRoleMap);
            doSearchStockRescue();
            reportSuccess("", "stock.rescue.warranty.accept.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }


    @Secured("@")
    public void notAcceptRequest() {
        try {
            stockTransRescueService.cancelStockRescue(stockTransRescueAction, requiredRoleMap);
            doSearchStockRescue();
            reportSuccess("", "stock.rescue.warranty.cancel.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doShowGiveRequest(StockTransRescueDTO stockTransRescueAction) {
        try {
            this.stockTransRescueAction = stockTransRescueAction;
            giveProduct = true;
            receiveProduct = false;
            disableGive = false;
            lstDetailGive = stockTransSerialRescueService.getListDetailSerial(stockTransRescueAction.getStockTransId());
//            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType("", Const.PRODUCT_OFFER_TYPE.PHONE);
            for (StockTransSerialRescueDTO stockTransSerialRescueDTO : lstDetailGive) {
                ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
                ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransSerialRescueDTO.getProdOfferId());
                productOfferingTotalDTO.setProductOfferingId(productOfferingDTO.getProductOfferingId());
                productOfferingTotalDTO.setCode(productOfferingDTO.getCode());
                productOfferingTotalDTO.setName(productOfferingDTO.getName());
                stockTransSerialRescueDTO.setProductOfferingTotalDTO(productOfferingTotalDTO);
                stockTransSerialRescueDTO.setLstProductOfferingTotalDTO(Lists.newArrayList(productOfferingTotalDTO));
            }
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doShowReceiveRequest(StockTransRescueDTO stockTransRescueAction) {
        try {
            this.stockTransRescueAction = stockTransRescueAction;
            giveProduct = true;
            receiveProduct = true;
            disableGive = false;
            lstDetailGive = stockTransSerialRescueService.getListDetailSerial(stockTransRescueAction.getStockTransId());
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void doValidateGive() {
        String lstErrorSerial = "";
        String lstErrorProduct = "";
        for (int i = 0; i < lstDetailGive.size(); i++) {
            StockTransSerialRescueDTO stockTransSerialRescueDTO = lstDetailGive.get(i);
            if (DataUtil.isNullOrEmpty(stockTransSerialRescueDTO.getSerialReturn())) {
                if (!DataUtil.isNullOrEmpty(lstErrorSerial)) {
                    lstErrorSerial += ",";
                }
                lstErrorSerial += i;
            }
            if (DataUtil.isNullObject(stockTransSerialRescueDTO.getProductOfferingTotalDTO())) {
                if (!DataUtil.isNullOrEmpty(lstErrorProduct)) {
                    lstErrorProduct += ",";
                }
                lstErrorProduct += i;
            }
        }
        if (!DataUtil.isNullOrEmpty(lstErrorSerial) || !DataUtil.isNullOrEmpty(lstErrorProduct)) {
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.rescue.warranty.serial.return.require");
            executeCommand("focusElementErrorByListClass('txtNumber','" + lstErrorSerial + "')");
            executeCommand("focusElementErrorByListClass('txtProduct','" + lstErrorProduct + "')");
        }
    }

    @Secured("@")
    public void giveRequest() {
        try {
            stockTransRescueService.returnStockRescue(stockTransRescueAction, requiredRoleMap, lstDetailGive);
            disableGive = true;
            reportSuccess("", "stock.rescue.warranty.give.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public void receiveRequest() {
        try {
            stockTransRescueService.receiveStockRescue(stockTransRescueAction, requiredRoleMap);
            disableGive = true;
            reportSuccess("", "stock.rescue.warranty.receive.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            int index = DataUtil.safeToInt(UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("index"));
            StockTransSerialRescueDTO stockTransSerialRescueDTO = lstDetailGive.get(index);
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getLsProductOfferingForChangeProduct(inputData.toString(), stockTransSerialRescueDTO.getProdOfferId(), false);
            stockTransSerialRescueDTO.setLstProductOfferingTotalDTO(lstProductOfferingTotalDTO);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "", ex);
            topPage();
        }
        return lstProductOfferingTotalDTO;
    }

    @Secured("@")
    public void resetLstProductOfferingTotal(int index) {
        lstDetailGive.get(index).setProductOfferingTotalDTO(null);
    }

    public void doSelectProductOffering() {
        try {
//            StockTransSerialRescueDTO stockTransSerialRescueDTO = lstDetailGive.get(0);
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void clearShop() {
        this.vShopDTO = null;
    }

    public StockTransRescueDTO getStockTransRescueDTOSearch() {
        return stockTransRescueDTOSearch;
    }

    public void setStockTransRescueDTOSearch(StockTransRescueDTO stockTransRescueDTOSearch) {
        this.stockTransRescueDTOSearch = stockTransRescueDTOSearch;
    }

    public List<OptionSetValueDTO> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<OptionSetValueDTO> statusList) {
        this.statusList = statusList;
    }

    public List<StockTransRescueDTO> getLstSearch() {
        return lstSearch;
    }

    public void setLstSearch(List<StockTransRescueDTO> lstSearch) {
        this.lstSearch = lstSearch;
    }

    public List<StockTransDetailRescueDTO> getLstDetail() {
        return lstDetail;
    }

    public void setLstDetail(List<StockTransDetailRescueDTO> lstDetail) {
        this.lstDetail = lstDetail;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public List<StockTransSerialRescueDTO> getLstDetailSerial() {
        return lstDetailSerial;
    }

    public void setLstDetailSerial(List<StockTransSerialRescueDTO> lstDetailSerial) {
        this.lstDetailSerial = lstDetailSerial;
    }

    public boolean isShowDetailSerial() {
        return showDetailSerial;
    }

    public void setShowDetailSerial(boolean showDetailSerial) {
        this.showDetailSerial = showDetailSerial;
    }

    public boolean isCreateRequest() {
        return createRequest;
    }

    public void setCreateRequest(boolean createRequest) {
        this.createRequest = createRequest;
    }

    public StockTransRescueDTO getStockTransRescueAdd() {
        return stockTransRescueAdd;
    }

    public void setStockTransRescueAdd(StockTransRescueDTO stockTransRescueAdd) {
        this.stockTransRescueAdd = stockTransRescueAdd;
    }

    public List<StockHandsetRescueDTO> getLstStockHansetRescue() {
        return lstStockHansetRescue;
    }

    public void setLstStockHansetRescue(List<StockHandsetRescueDTO> lstStockHansetRescue) {
        this.lstStockHansetRescue = lstStockHansetRescue;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public List<StockHandsetRescueDTO> getLstSelection() {
        return lstSelection;
    }

    public void setLstSelection(List<StockHandsetRescueDTO> lstSelection) {
        this.lstSelection = lstSelection;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public boolean isRoleExport() {
        return roleExport;
    }

    public void setRoleExport(boolean roleExport) {
        this.roleExport = roleExport;
    }

    public boolean isRoleReceive() {
        return roleReceive;
    }

    public void setRoleReceive(boolean roleReceive) {
        this.roleReceive = roleReceive;
    }

    public boolean isGiveProduct() {
        return giveProduct;
    }

    public void setGiveProduct(boolean giveProduct) {
        this.giveProduct = giveProduct;
    }

    public List<StockTransSerialRescueDTO> getLstDetailGive() {
        return lstDetailGive;
    }

    public void setLstDetailGive(List<StockTransSerialRescueDTO> lstDetailGive) {
        this.lstDetailGive = lstDetailGive;
    }

    public boolean isDisableGive() {
        return disableGive;
    }

    public void setDisableGive(boolean disableGive) {
        this.disableGive = disableGive;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public boolean isReceiveProduct() {
        return receiveProduct;
    }

    public void setReceiveProduct(boolean receiveProduct) {
        this.receiveProduct = receiveProduct;
    }
}

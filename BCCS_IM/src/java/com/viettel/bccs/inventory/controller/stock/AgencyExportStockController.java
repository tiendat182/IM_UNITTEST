package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.fw.logging.Kpi;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListFifoProductNameable;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
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
 * Created by hoangnt on 11/14/2015.
 */
@Component
@Scope("view")
@ManagedBean
public class AgencyExportStockController extends TransCommonController {
    private boolean infoOrderDetail;
    private boolean exportDepositAgent;
    private UploadedFile uploadedFile;
    @Autowired
    private ListFifoProductNameable listProductTag;
    @Autowired
    private ShopInfoNameable shopInfoTagExport;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    //    @Autowired
//    private ShopInfoNameable shopInfoTagExportDlg;
    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    protected ShopService shopService;
    @Autowired
    StockTransActionService stockTransActionService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    private boolean canPrint = false;
    private boolean haveDepositPrice = false;
    private int limitAutoComplete;
    private VStockTransDTO forSearch;
    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private StaffDTO staffDTO;
    private RequiredRoleMap requiredRoleMap;
    private Long stockTransActionId;
    private List<String> lstStatus;
    private int limitOffline;
    private boolean showPanelOffline;
    private boolean checkOffline;

    /**
     * ham view chi tiet
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doViewStockTransDetail(VStockTransDTO stocktrans) {
        try {
            stockTransActionId = stocktrans.getActionID();
            //Validate ky voffice
            StockTransActionDTO actionDTO = stockTransActionService.findOne(stockTransActionId);
            stockTransVofficeService.doSignedVofficeValidate(actionDTO);

            orderStockTag.loadOrderStock(stockTransActionId, true);
            if (orderStockTag.getTransInputDTO().getRegionStockTransId() != null) {
                StockTransDTO regionTransDTO = stockTransService.findOne(orderStockTag.getTransInputDTO().getRegionStockTransId());
                if (regionTransDTO != null) {
                    orderStockTag.getTransInputDTO().setRegionStockId(regionTransDTO.getFromOwnerId());
                    ShopDTO shopDTO = shopService.findOne(regionTransDTO.getFromOwnerId());
                    if (shopDTO != null) {
                        orderStockTag.getTransInputDTO().setRegionStockName(shopDTO.getName());
                    }
                }
            }
            this.setStockTransActionId(stockTransActionId);
            ConfigListProductTagDTO configListProductTagDTO;
            if (DataUtil.safeEqual(stocktrans.getStockTransStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_SERIAL);
            } else {
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_VIEW);
            }
            exportDepositAgent = false;
            // Neu xuat dat coc thi lay gia dat coc.
            if (DataUtil.isNullOrEmpty(stocktrans.getPayStatus()) &&
                    DataUtil.safeEqual(stocktrans.getDepositStatus(), Const.DEPOSIT_STATUS.DEPOSIT_HAVE)) {
                exportDepositAgent = true;
                ShopDTO shopImpDTO = shopService.findOne(stocktrans.getToOwnerID());
                configListProductTagDTO.setRetrieveExpense(true);
                configListProductTagDTO.setBranchId(shopService.getBranchId(shopImpDTO.getShopId()));
                configListProductTagDTO.setPricePolicyId(DataUtil.safeToLong(shopImpDTO.getPricePolicy()));
                configListProductTagDTO.setChannelTypeId(shopImpDTO.getChannelTypeId());
            }
            listProductTag.load(this, stockTransActionId, configListProductTagDTO);
            if (!DataUtil.isNullObject(listProductTag.getLsStockTransFull())
                    && DataUtil.isNullOrEmpty(stocktrans.getPayStatus()) &&
                    DataUtil.safeEqual(stocktrans.getDepositStatus(), Const.DEPOSIT_STATUS.DEPOSIT_HAVE)) {
                for (StockTransFullDTO stockTransFullDTO : listProductTag.getLsStockTransFull()) {
                    if (DataUtil.isNullObject(stockTransFullDTO.getDepositPrice())) {
                        haveDepositPrice = true;
                        break;
                    }
                }
            }
            infoOrderDetail = true;
            if (DataUtil.safeEqual(stocktrans.getStockTransStatus(), Const.STOCK_TRANS_STATUS.IMPORTED)) {
                canPrint = true;
            } else {
                canPrint = false;
            }
            List<StockTransDetailDTO> lsDetail = listProductTag.getListTransDetailDTOs();
            Long total = 0L;
            for (StockTransDetailDTO stock : lsDetail) {
                if (!DataUtil.isNullObject(stock.getCheckSerial()) && stock.getCheckSerial().equals(1L)) {
                    total += DataUtil.safeToLong(stock.getQuantity());
                }
            }
            showPanelOffline = total >= limitOffline;
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
     * ham reset back tro ve man tim kiem
     *
     * @author ThanhNT77
     */
    @Secured("@")
    public void doBackPage() {
        infoOrderDetail = false;
        haveDepositPrice = false;
        orderStockTag.resetOrderStock();
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            forSearch.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
            forSearch.setLstStatus(null);
            if (!DataUtil.isNullOrEmpty(forSearch.getStockTransStatus())) {
                forSearch.setStockTransStatus(forSearch.getStockTransStatus());
            } else {
                forSearch.setLstStatus(lstStatus);
            }
            forSearch.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            forSearch.setVtUnit(Const.VT_UNIT.AGENT);
            forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
            forSearch.setUserShopId(staffDTO.getShopId());
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
            if (!DataUtil.isNullObject(vStockTransDTOList)) {
                for (VStockTransDTO vStockTransDTO : vStockTransDTOList) {
                    for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOsList) {
                        if (DataUtil.safeEqual(vStockTransDTO.getStockTransStatus(), optionSetValueDTO.getValue())) {
                            vStockTransDTO.setStatusName(optionSetValueDTO.getName());
                            break;
                        }
                    }

                }
            }
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
    public void doResetSearhStockTrans() {
        vStockTransDTOList = Lists.newArrayList();
        shopInfoTagReceive.resetShop();
        Date currentDate = DateUtil.sysDate();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setActionType(Const.STOCK_TRANS_ACTION_TYPE.COMMAND);
        forSearch.setFromOwnerID(staffDTO.getShopId());
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        infoOrderDetail = false;
        doSearchStockTrans();
    }

    /**
     * ham lap lenh xuat
     *
     * @author ThanhNT77
     */
    @Kpi("ID_KPI_AGENCY_EXPORT_STOCK_CREATE_UNDER_LYING")
    @Secured("@")
    public void doCreateUnderlyingStock() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            List<StockTransDetailDTO> lsDetailDTOs = Lists.newArrayList();
            List<StockTransFullDTO> lsStockTransFull = listProductTag.getLsStockTransFull();
            Long totalDepositPrice = 0L;
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                stockTransDetailDTO.setStockTransDetailId(stockTransFullDTO.getStockTransDetailId());
                stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                stockTransDetailDTO.setLstStockTransSerial(stockTransFullDTO.getLstSerial());
                stockTransDetailDTO.setTableName(stockTransFullDTO.getTableName());
                stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
                stockTransDetailDTO.setDepositPrice(stockTransFullDTO.getDepositPrice());
                lsDetailDTOs.add(stockTransDetailDTO);
                if (!DataUtil.isNullObject(stockTransFullDTO.getDepositPrice())) {
                    totalDepositPrice += stockTransFullDTO.getDepositPrice() * stockTransFullDTO.getQuantity();
                }
            }
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setStockTransActionId(getStockTransActionId());
            stockTransDTO.setCreateUserIpAdress(BccsLoginSuccessHandler.getIpAddress());
            stockTransDTO.setDepositPrice(totalDepositPrice);
            stockTransDTO.setProcessOffline(checkOffline ? Const.PROCESS_OFFLINE : "");
            stockTransDTO.setShopId(staffDTO.getShopId());
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_AGENT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, lsDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg(), message.getParamsMsg());
            }
            doSearchStockTrans();
            canPrint = true;
            reportSuccess("frmExportNote:msgExport", "export.stock.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExport", "common.error.happened", ex);
            topPage();
        }

    }


    @Autowired
    private StockTransDetailService stockTransDetailService;

    @Secured("@")
    public void doCancelStockTrans(Long stockTransActionId) {
        try {
            orderStockTag.loadOrderStock(stockTransActionId, true);
            this.setStockTransActionId(stockTransActionId);
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();

            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            List<StockTransDetailDTO> stockTransDetailDTOs = stockTransDetailService.findByFilter(Lists.newArrayList(
                    new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                            stockTransDTO.getStockTransId())));
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.CANCEL_TRANS_AGENT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            doSearchStockTrans();
            reportSuccess("frmExportNote:msgExport", "stock.export.stock.confirm.cancel.success");
            topPage();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportNote:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportNote:msgExport", "", "common.error.happen");
            topPage();
        }

    }

    @PostConstruct
    public void init() {
        try {
            lstStatus = Lists.newArrayList();
            lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            lstStatus.add(Const.STOCK_TRANS_STATUS.IMPORTED);
            lstStatus.add(Const.STOCK_TRANS_STATUS.CANCEL);
            orderStockTag.init(this, false);
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            optionSetValueDTOsList = DataUtil.defaultIfNull(optionSetValueService.getStatusOptionSetValueByStockState(Const.OPTION_SET.STOCK_TRANS_STATUS_AGENT, lstStatus), new ArrayList<OptionSetValueDTO>());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_SYNC_LOGISTIC, Const.PERMISION.ROLE_TRANSPORT_STOCK, Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN);
            String shopId = DataUtil.safeToString(staffDTO.getShopId());
            shopInfoTagExport.initShop(this, shopId, false);
            shopInfoTagExport.loadShop(shopId, true);
            shopInfoTagReceive.initAgent(this, shopId);
//            shopInfoTagExportDlg.initAgent(this, shopId);
            doResetSearhStockTrans();
            haveDepositPrice = false;
            limitOffline = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.MIN_QUANTITY_EXPORT_OFFLINE));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Secured("@")
    public void showInfoOrderDetail() {
        infoOrderDetail = true;
    }
    //getter and setter


    public boolean isHaveDepositPrice() {
        return haveDepositPrice;
    }

    public void setHaveDepositPrice(boolean haveDepositPrice) {
        this.haveDepositPrice = haveDepositPrice;
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

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public StreamedContent exportStockTransDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                stockTransDTO.setActionType(Const.STOCK_TRANS_ACTION_TYPE.NOTE);
                stockTransDTO.setStockTransStatus(Const.STOCK_TRANS_TYPE.EXPORT);
                List<Long> lstActionID = Lists.newArrayList();
                lstActionID.add(stockTransActionId);
                List<StockTransFullDTO> lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lstActionID), new ArrayList<StockTransFullDTO>());
                return exportStockTransDetail(stockTransDTO, lsStockTransFull);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            reportError("frmExportNote:msgExport", ex);
            topPage();
        } catch (Exception ex) {
            reportError("frmExportNote:msgExport", "common.error.happen", ex);
            topPage();
        }
        return null;
    }


    public StreamedContent exportHandOverReport() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            stockTransDTO.setStockTransActionId(getStockTransActionId());
            FilterRequest filterRequest = new FilterRequest(StockTransAction.COLUMNS.STOCKTRANSID.name(), stockTransDTO.getStockTransId());
            FilterRequest filterRequest1 = new FilterRequest(StockTransAction.COLUMNS.STATUS.name(), 2L);
            List<StockTransActionDTO> stockTransActionDTOs = stockTransActionService.findByFilter(Lists.newArrayList(filterRequest, filterRequest1));
            if (stockTransActionDTOs == null || stockTransActionDTOs.isEmpty()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.stock.code.export.not.found");
            }
            stockTransDTO.setActionCode(stockTransActionDTOs.get(0).getActionCode());
            StreamedContent content = exportHandOverReport(stockTransDTO);
            return content;
        } catch (LogicException ex) {
            topReportError("frmExportNote:msgExport", ex);
        } catch (Exception ex) {
            topReportError("frmExportNote:msgExport", "common.error.happened", ex);
        }
        return null;
    }

    public boolean isExportDepositAgent() {
        return exportDepositAgent;
    }

    public void setExportDepositAgent(boolean exportDepositAgent) {
        this.exportDepositAgent = exportDepositAgent;
    }

    public boolean isCanPrint() {
        return canPrint;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public ListFifoProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListFifoProductNameable listProductTag) {
        this.listProductTag = listProductTag;
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

    public ExecuteStockTransService getExecuteStockTransService() {
        return executeStockTransService;
    }

    public void setExecuteStockTransService(ExecuteStockTransService executeStockTransService) {
        this.executeStockTransService = executeStockTransService;
    }

    public StockTransService getStockTransService() {
        return stockTransService;
    }

    public void setStockTransService(StockTransService stockTransService) {
        this.stockTransService = stockTransService;
    }

    public OptionSetValueService getOptionSetValueService() {
        return optionSetValueService;
    }

    public void setOptionSetValueService(OptionSetValueService optionSetValueService) {
        this.optionSetValueService = optionSetValueService;
    }
//
//    public ShopInfoNameable getShopInfoTagExportDlg() {
//        return shopInfoTagExportDlg;
//    }
//
//    public void setShopInfoTagExportDlg(ShopInfoNameable shopInfoTagExportDlg) {
//        this.shopInfoTagExportDlg = shopInfoTagExportDlg;
//    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
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

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public RequiredRoleMap getRequiredRoleMap() {
        return requiredRoleMap;
    }

    public void setRequiredRoleMap(RequiredRoleMap requiredRoleMap) {
        this.requiredRoleMap = requiredRoleMap;
    }

    public boolean isShowPanelOffline() {
        return showPanelOffline;
    }

    public void setShowPanelOffline(boolean showPanelOffline) {
        this.showPanelOffline = showPanelOffline;
    }

    public int getLimitOffline() {
        return limitOffline;
    }

    public void setLimitOffline(int limitOffline) {
        this.limitOffline = limitOffline;
    }

    public boolean isCheckOffline() {
        return checkOffline;
    }

    public void setCheckOffline(boolean checkOffline) {
        this.checkOffline = checkOffline;
    }
}

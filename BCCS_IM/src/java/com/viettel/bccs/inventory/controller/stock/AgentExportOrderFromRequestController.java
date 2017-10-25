package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.util.EncryptionUtils;
import com.viettel.bccs.inventory.util.PassWordUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

/**
 * Created by sinhhv on 3/14/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class AgentExportOrderFromRequestController extends BaseController {

    @Autowired
    private ShopInfoNameable agentRquest;
    @Autowired
    private StockOrderAgentService stockOrderAgentService;
    @Autowired
    private StockOrderAgentDetailService stockOrderAgentDetailService;
    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;

    private StockOrderAgentDTO searchForm = new StockOrderAgentDTO();
    private StockOrderAgentDTO currentOrderAgent = new StockOrderAgentDTO();
    private List<StockOrderAgentForm> stockOrderAgentList = Lists.newArrayList();
    private List<StockTransFullDTO> listProduct = Lists.newArrayList();
    private List<OptionSetValueDTO> listBank = Lists.newArrayList();
    private StaffDTO currentStaff;
    private boolean writeVoffice = true;
    private boolean showInfoOrder = false;
    private boolean showListProduct = false;
    private boolean showListOrderStock = true;
    private Long currentStockOrderAgent = null;
    private boolean showRegionStock = false;
    private String reasonDeny;
    private StockOrderAgentForm currentForm;


    @PostConstruct
    public void init() {
        try {
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            orderStockTag.init(this, writeVoffice);
            orderStockTag.getShopInfoExportTag().setIsDisableEdit(true);
            orderStockTag.getShopInfoReceiveTag().setIsDisableEdit(true);
            writeOfficeTag.init(this, currentStaff.getShopId());
            if (currentStaff.getStaffId().equals(Const.L_VT_SHOP_ID) && CustomAuthenticationProvider.hasRole(Const.PERMISION.ROLE_XUAT_KHO_BA_MIEN)) {
                showRegionStock = true;
            }
            agentRquest.initAgent(this, currentStaff.getShopId().toString());
            listBank = optionSetValueService.getByOptionSetCode(Const.STOCK_ORDER_AGENT.LIST_BANK);
            searchForm.setFromDate(new Date());
            searchForm.setToDate(new Date());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", "common.error.happen");
            topPage();
        }
    }

    private boolean validate() {
        if (DataUtil.isNullObject(searchForm.getFromDate())) {
            reportError("frmExportOrderFromRQ:messages", "", "mn.stock.from.date.not.blank");
            focusElementByRawCSSSlector(".fromDate");
            return false;
        }

        if (DataUtil.isNullObject(searchForm.getToDate())) {
            reportError("frmExportOrderFromRQ:messages", "", "mn.stock.to.date.not.blank");
            focusElementByRawCSSSlector(".toDate");
            return false;
        }

        if (!DataUtil.isNullObject(searchForm.getFromDate()) && !DataUtil.isNullObject(searchForm.getToDate())
                && (DateUtil.compareDateToDate(searchForm.getFromDate(), searchForm.getToDate()) > 0
                || DateUtil.daysBetween2Dates(searchForm.getFromDate(), searchForm.getToDate()) > 30)) {
            reportError("frmExportOrderFromRQ:messages", "", "stock.trans.from.to.valid");
            focusElementByRawCSSSlector(".toDate");
            return false;
        }
        return true;
    }

    @Secured("@")
    private String getStrStatus(Long status) {
        String strStatus = "";
        if (status == 0L) {
            strStatus = getText("stockOrderAgent.staus0");
        } else if (status == 1L) {
            strStatus = getText("stockOrderAgent.staus1");
        } else if (status == 2L) {
            strStatus = getText("stockOrderAgent.staus2");
        }
        if (status == 3L) {
            strStatus = getText("mn.stock.agency.retrieve.import.optionset.destroy");
        }
        return strStatus;
    }

    @Secured("@")
    public void searchStockOrderAgent() {
        try {
            if (validate()) {
                stockOrderAgentList = DataUtil.defaultIfNull(stockOrderAgentService.getStockOrderAgentForm(searchForm, currentStaff), Lists.newArrayList());
                for (StockOrderAgentForm dto : stockOrderAgentList) {
                    dto.setStrStatus(getStrStatus(dto.getStatus()));
                }
            }
            showListOrderStock = true;
            showListProduct = false;
            showInfoOrder = false;
            writeVoffice = false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", "common.error.happen");
            topPage();
        }
    }

    @Secured("@")
    public void doShowInfoOrderDetail(Long orderAgentId, boolean scroll) {
        try {
            if (!DataUtil.isNullObject(orderAgentId)) {
                StockOrderAgentDTO dto = stockOrderAgentService.findOne(orderAgentId);
                currentOrderAgent = dto;
                listProduct = stockOrderAgentDetailService.getListGood(orderAgentId);
                showListProduct = true;
                if (scroll) {
                    RequestContext.getCurrentInstance().execute("$('html, body').animate({scrollTop: document.getElementById( 'frmExportOrderFromRQ:pnlListProductAgent').offsetTop-60}, 'fast')");
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", "common.error.happen");
            topPage();
        }
    }

    public boolean checkValidateAgentBeforeExportOrder() {
        StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
        StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
        if (DataUtil.isNullObject(stockTransActionDTO.getActionCode())) {
            reportError("", "", "mn.stock.utilities.order.note.export.note.blank");
            topPage();
        }
        if (DataUtil.isNullObject(stockTransDTO.getReasonId())) {
            reportError("", "", "export.order.by.reason.not.blank");
            topPage();
        }
        if (writeVoffice) {
            try {
                writeOfficeTag.validateVofficeAccount();
            } catch (LogicException e) {
                reportError("", e.getKeyMsg(), e);
                topPage();
            } catch (Exception ex) {
                reportError("", "common.error.happen", ex);
                topPage();
            }
        }
        return true;
    }

    private boolean checkValidateRequire(StockOrderAgentForm orderAgentForm) throws Exception {
        StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentService.findOne(orderAgentForm.getStockOrderAgentId());
        if (DataUtil.isNullObject(stockOrderAgentDTO.getOrderType())) {
            reportError("", "", "mn.stock.agency.order.type.not.null");
            return false;
        }

//        if (stockOrderAgentDTO.getOrderType().toString().equals(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)) {
//            if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_DAT_COC)) {
//                reportError("", "", "mn.stock.agency.not.permission.deposit");
//                return false;
//            }
//        } else if (stockOrderAgentDTO.getOrderType().toString().equals(Const.STOCK_TRANS.STOCK_TRANS_PAY)) {
//            if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_BAN_DUT)) {
//                reportError("", "", "mn.stock.agency.not.permission.pay");
//                return false;
//            }
//        }

        if (DataUtil.isNullObject(stockOrderAgentDTO.getStockOrderAgentId())) {
            reportError("", "", "mn.stock.agency.request.not.exist");
            return false;
        }

        if (!DataUtil.safeEqual(stockOrderAgentDTO.getStatus(), Const.STOCK_ORDER_AGENT.STATUS_CREATE_REQUEST)) {
            reportError("", "", "mn.stock.agency.request.not.execute");
            return false;
        }
        return true;
    }

    @Secured("@")
    public void preExportOrderAgent(StockOrderAgentForm orderAgentForm) {
        try {
            if (checkValidateRequire(orderAgentForm)) {
                currentForm = orderAgentForm;
                writeVoffice = true;
                orderStockTag.init(this, writeVoffice);
                orderStockTag.getShopInfoExportTag().setIsDisableEdit(true);
                orderStockTag.getShopInfoReceiveTag().setIsDisableEdit(true);
                writeOfficeTag.init(this, currentStaff.getShopId());
                StockOrderAgentDTO orderAgent = stockOrderAgentService.findOne(currentForm.getStockOrderAgentId());
                StockTransInputDTO transInputDTO = new StockTransInputDTO();
                transInputDTO.setStockOrderAgentId(currentForm.getStockOrderAgentId());
                transInputDTO.setAgentRequestCode(orderAgent.getRequestCode());
                transInputDTO.setAgentOrderType(orderAgent.getOrderType());
                transInputDTO.setCreateUser(currentStaff.getStaffCode());
                transInputDTO.setCreateDatetime(new Date());
                transInputDTO.setFromOwnerId(currentStaff.getShopId());
                transInputDTO.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                transInputDTO.setToOwnerId(orderAgent.getShopId());
                transInputDTO.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                orderStockTag.setTransInputDTO(transInputDTO);
                orderStockTag.getShopInfoReceiveTag().loadShop(orderAgent.getShopId().toString(), true);
                doShowInfoOrderDetail(orderAgent.getStockOrderAgentId(), false);
                showInfoOrder = true;
                showListOrderStock = false;
                RequestContext.getCurrentInstance().execute("$('html, body').animate({scrollTop: document.getElementById( 'frmExportOrderFromRQ:exportStocksabc:pnViewExportOrderexportStocksabc').offsetTop-60}, 'fast')");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", "common.error.happen");
            topPage();
        }

    }

    @Secured("@")
    public void doCreateOrderExport() {
        try {
            if (checkValidateAgentBeforeExportOrder()) {
                StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
                stockTransDTO.setStaffId(currentStaff.getStaffId());
                stockTransDTO.setUserCreate(currentStaff.getStaffCode());
                stockTransDTO.setStockOrderAgentId(orderStockTag.getTransInputDTO().getStockOrderAgentId());
                StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
                if (writeVoffice) {
                    SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
                    stockTransDTO.setUserName(signOfficeDTO.getUserName());
                    stockTransDTO.setPassWord(signOfficeDTO.getPassWord());
                    stockTransDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
                    stockTransDTO.setSignVoffice(writeVoffice);
                    stockTransActionDTO.setSignCaStatus(Const.SIGN_VOFFICE);
                }
                List<StockTransDetailDTO> lstStockTransDetail = Lists.newArrayList();
                for (StockTransFullDTO stockTransFullDTO : listProduct) {
                    StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
                    stockTransDetailDTO.setProdOfferId(stockTransFullDTO.getProdOfferId());
                    stockTransDetailDTO.setProdOfferTypeId(stockTransFullDTO.getProductOfferTypeId());
                    stockTransDetailDTO.setUnit(stockTransFullDTO.getUnit());
                    stockTransDetailDTO.setStateId(stockTransFullDTO.getStateId());
                    stockTransDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
                    stockTransDetailDTO.setProdOfferName(stockTransFullDTO.getProdOfferName());
                    lstStockTransDetail.add(stockTransDetailDTO);
                }
                RequiredRoleMap requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_XUAT_DAT_COC, Const.PERMISION.ROLE_XUAT_BAN_DUT);
                //tao giao dich
                BaseMessage msg = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER_AGENT, Const.STOCK_TRANS_TYPE.EXPORT_REQUEST, stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);

                if (!msg.isSuccess() && !DataUtil.isNullObject(msg.getKeyMsg())) {
                    throw new LogicException("", msg.getKeyMsg(), msg.getParamsMsg());
                } else {
                    currentForm.setStatus(Const.STOCK_ORDER_AGENT.STATUS_CREATE_ORDER);
                    currentForm.setStrStatus(getStrStatus(Const.STOCK_ORDER_AGENT.STATUS_CREATE_ORDER));
                    reportSuccess("frmExportOrderFromRQ:messages", "export.order.create.success");
                    searchStockOrderAgent();
                    topPage();
                }
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", ex.getKeyMsg(), ex.getParamsMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", "common.error.happen");
            topPage();
        }
    }

    public void doValidateDesTroy(StockOrderAgentForm stockOrderAgentForm) {
        if (DataUtil.isNullOrEmpty(reasonDeny)) {
            reportError("dlgReason:mesagesDetail", "", "stockOrderAgent.note.requite");
            topPage();
        }
        reasonDeny = "";
        currentForm = stockOrderAgentForm;
        this.currentStockOrderAgent = currentForm.getStockOrderAgentId();
    }

    public void doValidateDesTroy2() {
        if (DataUtil.isNullObject(reasonDeny) || reasonDeny.length() > 500) {
            reportError("frmExportOrderFromRQ:messages", "", "agent.exp.request.reason.reject.validate");
            topPage();
        }
    }

    public void doDestroyRequest() {
        try {
            StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentService.findOne(currentStockOrderAgent);
            stockOrderAgentDTO.setNote(reasonDeny);
            if (!DataUtil.isNullObject(stockOrderAgentDTO) && stockOrderAgentDTO.getStatus().equals(0L)) {
//                RequiredRoleMap requiredRoleMap = CustomAuthenticationProvider.createRequiredRoleMap(Const.PERMISION.ROLE_XUAT_DAT_COC, Const.PERMISION.ROLE_XUAT_BAN_DUT);
//                if (DataUtil.safeEqual(stockOrderAgentDTO.getOrderType().toString(), Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT)) {
//                    if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_DAT_COC)) {
//                        reportError("formUpload:mesagesDetail", "", "mn.stock.agency.not.permission.deposit.cancel");
//                        return;
//                    }
//                } else if (DataUtil.safeEqual(stockOrderAgentDTO.getOrderType().toString(), Const.STOCK_TRANS.STOCK_TRANS_PAY)) {
//                    if (!requiredRoleMap.hasRole(Const.PERMISION.ROLE_XUAT_BAN_DUT)) {
//                        reportError("formUpload:mesagesDetail", "", "mn.stock.agency.not.permission.pay.cancel");
//                        return;
//                    }
//                }
                stockOrderAgentDTO.setStatus(Const.STOCK_ORDER_AGENT.STATUS_DENY_REQUEST);
                stockOrderAgentDTO.setUpdateStaffId(currentStaff.getStaffId());
                stockOrderAgentService.update(stockOrderAgentDTO);
                stockOrderAgentList = stockOrderAgentService.getStockOrderAgentForm(searchForm, currentStaff);
                for (StockOrderAgentForm dto : stockOrderAgentList) {
                    dto.setStrStatus(getStrStatus(dto.getStatus()));
                }
                currentForm.setStatus(Const.STOCK_ORDER_AGENT.STATUS_CANCEL);
                currentForm.setStrStatus(getStrStatus(Const.STOCK_ORDER_AGENT.STATUS_CANCEL));
                reportSuccess("frmExportOrderFromRQ:messages", "mn.stock.agency.denny.request.success");
                topPage();
            } else {
                reportError("frmExportOrderFromRQ:messages", "", "mn.stock.agency.denny.request.not.ok");
                topPage();
            }

        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("frmExportOrderFromRQ:messages", "", ex.getKeyMsg(), ex.getParamsMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportErrorValidateFail("frmExportOrderFromRQ:messages", "", "common.error.happen");
            topPage();
        }
    }

    public void receiveWriteOffice(Boolean writeVoffice) {
        this.writeVoffice = writeVoffice;
        updateElemetId("frmExportOrderFromRQ:pnExportOffice");
    }

    public void previousView() {
        showListOrderStock = true;
        showListProduct = false;
        showInfoOrder = false;
        writeVoffice = false;
    }

    @Secured("@")
    public void doSelectAgent(VShopStaffDTO dto) {
        searchForm.setShopId(DataUtil.safeToLong(dto.getOwnerId()));
    }

    @Secured("@")
    public void doResetAgent(String value) {
        searchForm.setShopId(null);
    }


    public StockOrderAgentDTO getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(StockOrderAgentDTO searchForm) {
        this.searchForm = searchForm;
    }

    public ShopInfoNameable getAgentRquest() {
        return agentRquest;
    }

    public void setAgentRquest(ShopInfoNameable agentRquest) {
        this.agentRquest = agentRquest;
    }

    public List<StockOrderAgentForm> getStockOrderAgentList() {
        return stockOrderAgentList;
    }

    public void setStockOrderAgentList(List<StockOrderAgentForm> stockOrderAgentList) {
        this.stockOrderAgentList = stockOrderAgentList;
    }

    public List<StockTransFullDTO> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<StockTransFullDTO> listProduct) {
        this.listProduct = listProduct;
    }

    public OrderStockTagNameable getOrderStockTag() {
        return orderStockTag;
    }

    public List<OptionSetValueDTO> getListBank() {
        return listBank;
    }

    public void setListBank(List<OptionSetValueDTO> listBank) {
        this.listBank = listBank;
    }

    public void setOrderStockTag(OrderStockTagNameable orderStockTag) {
        this.orderStockTag = orderStockTag;
    }

    public boolean getShowInfoOrder() {
        return showInfoOrder;
    }

    public void setShowInfoOrder(boolean showInfoOrder) {
        this.showInfoOrder = showInfoOrder;
    }

    public boolean getWriteVoffice() {
        return this.writeVoffice;
    }

    public void setWriteVoffice(boolean writeVoffice) {
        this.writeVoffice = writeVoffice;
    }

    public boolean getShowListProduct() {
        return showListProduct;
    }

    public void setShowListProduct(boolean showListProduct) {
        this.showListProduct = showListProduct;
    }

    public boolean isShowListOrderStock() {
        return showListOrderStock;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public void setShowListOrderStock(boolean showListOrderStock) {
        this.showListOrderStock = showListOrderStock;
    }

    public boolean getShowRegionStock() {
        return showRegionStock;
    }

    public void setShowRegionStock(boolean showRegionStock) {
        this.showRegionStock = showRegionStock;
    }

    public StockOrderAgentDTO getCurrentOrderAgent() {
        return currentOrderAgent;
    }

    public String getReasonDeny() {
        return reasonDeny;
    }

    public void setReasonDeny(String reasonDeny) {
        this.reasonDeny = reasonDeny;
    }

    public void setCurrentOrderAgent(StockOrderAgentDTO currentOrderAgent) {
        this.currentOrderAgent = currentOrderAgent;
    }
}

package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tuydv1 on 09/03/2016. - SDT: 0969051866
 */
@Component
@Scope("view")
@ManagedBean
public class StockOrderAgentController extends TransCommonController {

    @Autowired
    StockOrderAgentService stockOrderAgentService;
    @Autowired
    StockOrderAgentDetailService stockOrderAgentDetailService;
    @Autowired
    ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockDepositService stockDepositService;

    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    private StockOrderAgentDTO stockOrderAgentSearch = new StockOrderAgentDTO();
    private StockOrderAgentDTO stockOrderAgentAdd = new StockOrderAgentDTO();
    private List<StockOrderAgentDetailDTO> listStockOrderAgentDetailAdd = Lists.newArrayList();
    private StaffDTO currentStaff;
    private List<OptionSetValueDTO> listBank = Lists.newArrayList();
    private List<OptionSetValueDTO> listState = Lists.newArrayList();
    private List<StockOrderAgentDTO> stockOrderAgentList = Lists.newArrayList();
    private List<StockOrderAgentDTO> selectedStockOrderAgent = Lists.newArrayList();
    private List<StockOrderAgentDetailDTO> listStockOrderAgentDetail = Lists.newArrayList();
    private Date currentDate;
    private boolean showRole = false;
    private String message = "";
    private boolean showList = false;
    private boolean showListDetail = false;
    private boolean showRequest = false;
    private boolean showSearch = false;
    private Long currentStockOrderAgentId;
    private ConfigListProductTagDTO configListProductTagDTO;
    private String reason;
    private Long channelTypeId;

    @PostConstruct
    public void init() {
        try {
            currentStaff = BccsLoginSuccessHandler.getStaffDTO();
            executeCommand("updateControls();");
            message = "";
            try {
                Long shopId = currentStaff.getShopId();
                if (DataUtil.isNullOrEmpty(stockOrderAgentService.checkShopAgent(shopId))) {
                    message = getText("stockOrderAgent.require.agentRole");
                } else {
                    ShopDTO shopDTO = shopService.findOne(currentStaff.getShopId());
                    if (shopDTO == null) {
                        message = getText("stockOrderAgent.shop.notExit");
                    } else if (Const.L_VT_SHOP_ID.compareTo(shopDTO.getParentShopId()) == 0) {
                        message = getText("stockOrderAgent.shop.vtt");
                    } else if (Const.CHANNEL_TYPE.CHANNEL_TYPE_AGENT_VTT.compareTo(shopDTO.getChannelTypeId()) == 0) {
                        message = getText("stockOrderAgent.shop.vtt.notApplication");
                    }
                    channelTypeId = shopDTO.getChannelTypeId();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                message = getText("stockOrderAgent.require.agentRole");
            }

            if (DataUtil.isNullObject(message)) {
                showRole = true;
                showSearch = true;
                if (currentDate == null) {
                    currentDate = optionSetValueService.getSysdateFromDB(true);
                }
                stockOrderAgentSearch.setFromDate(currentDate);
                stockOrderAgentSearch.setToDate(currentDate);
                listBank = optionSetValueService.getByOptionSetCode(Const.STOCK_ORDER_AGENT.LIST_BANK);
                listState = optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.STATE);
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_NO_SERIAL, (Long) null, null);
                configListProductTagDTO.setShowTotalPrice(false);
                configListProductTagDTO.setStockOrderAgent(true);
                /*Long branchId = shopService.getBranchId(currentStaff.getShopId());
                configListProductTagDTO.setChannelTypeId(DataUtil.safeToLong(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_AGENT));
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_PAY);
                configListProductTagDTO.setBranchId(branchId);*/
                listProductTag.init(this, configListProductTagDTO);
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
        topPage();
    }


    @Secured("@")
    public String getCodeAndName() {
        String code = currentStaff.getStaffCode();
        String name = currentStaff.getName();
        return code + "-" + name;
    }

    @Secured("@")
    public String getStrStatus(Long status) {
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
            showSearch = true;
            showList = true;
            showListDetail = false;
            showRequest = false;
            selectedStockOrderAgent = Lists.newArrayList();
            listStockOrderAgentDetail = Lists.newArrayList();
            stockOrderAgentSearch.setShopId(currentStaff.getShopId());
            validateDate(stockOrderAgentSearch.getFromDate(), stockOrderAgentSearch.getToDate());
            stockOrderAgentSearch.setCreateStaffId(currentStaff.getStaffId());
            stockOrderAgentList = stockOrderAgentService.search(stockOrderAgentSearch);


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

    public String getMaxStockOrderAgentCode() throws Exception {
        Long stockOrderAgentId = stockOrderAgentService.getMaxStockOrderAgentId() + 1L;
        String result = "RC_" + stockOrderAgentId.toString();
        return result;
    }


    @Secured("@")
    public void doPreRequest() {
        try {
            showRequest = true;
            showSearch = false;
            showList = false;
            showListDetail = false;
            listProductTag.setLsListProductOfferDTO(Lists.newArrayList());
            listProductTag.doAddItem();
            stockOrderAgentAdd = new StockOrderAgentDTO();
            stockOrderAgentAdd.setRequestCode(getMaxStockOrderAgentCode());
            stockOrderAgentAdd.setCreateDate(optionSetValueService.getSysdateFromDB(false));

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
    public void resetProposeCode() {
        try {
            stockOrderAgentAdd.setRequestCode(getMaxStockOrderAgentCode());
        } catch (LogicException e) {
            logger.error(e.getMessage(), e);
            topReportError("", e.getErrorCode(), e.getKeyMsg());
        } catch (Exception ex) {
            topReportError("", "common.error.happened", ex);
        }
    }

    @Secured("@")
    public void doCreateRequest() {
        try {
            //validate max_stock trong stock_deposit
            for (StockOrderAgentDetailDTO stockOrderAgentDTO : listStockOrderAgentDetailAdd) {
                StockDepositDTO stockDepositDTO = stockDepositService.getStockDeposit(stockOrderAgentDTO.getProdOfferId(), channelTypeId, DataUtil.safeToLong(Const.STOCK_TRANS.STOCK_TRANS_PAY));
                if (DataUtil.isNullObject(stockDepositDTO)) {
                    throw new LogicException("", "agent.export.order.not.config.stock.deposit", stockOrderAgentDTO.getProdOfferName());
                }
                if (stockOrderAgentDTO.getQuantity() > stockDepositDTO.getMaxStock()) {
                    throw new LogicException("", "agent.export.order.stock.deposit.max", stockOrderAgentDTO.getProdOfferName(), stockDepositDTO.getMaxStock());
                }
            }
            stockOrderAgentAdd.setStatus(Const.STOCK_ORDER_AGENT.STATUS_NOREQUEST);
            stockOrderAgentAdd.setOrderType(DataUtil.safeToLong(Const.STOCK_TRANS.STOCK_TRANS_PAY));
            stockOrderAgentService.createRequestStockOrder(stockOrderAgentAdd, listStockOrderAgentDetailAdd, currentStaff);
            searchStockOrderAgent();
            reportSuccess("", "stockOrderAgent.add.sussces");
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
    public void doConfirmRequest() {
        try {
            listStockOrderAgentDetailAdd = Lists.newArrayList();
            StockOrderAgentDetailDTO detailDTO;
            List<ListProductOfferDTO> lsListProductOfferDTO = listProductTag.getLsListProductOfferDTO();
            if (DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
                reportError("", "", "stock.rescueInformation.validate.list");
                topPage();
            } else {
                List<StockTransDetailDTO> lsStockTransDetail = listProductTag.getListTransDetailDTOs();
                if (lsListProductOfferDTO.size() >= 2 && validateProductOfferingState(lsListProductOfferDTO)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.rescueInformation.validate.list.duplicate");
                }
                Map<String, String> mapError = Maps.newHashMap();
                String strProNameErrIndex = "";//luu tru danh sach cac class ma loi cua ten mat hang
                String strNumberErrIndex = "";//luu tru danh sach cac class ma loi so luong
                int i = 0;
                for (StockTransDetailDTO stockTransDetailDTO : lsStockTransDetail) {
                    // neu chua chon hang hoa thi loi, con chon roi thi moi kiem tra quyen
                    if (stockTransDetailDTO.getProdOfferId() == null) {
                        if (!"".equals(strProNameErrIndex)) {
                            strProNameErrIndex += ",";
                        }
                        strProNameErrIndex += i;
                    } else {
                        detailDTO = new StockOrderAgentDetailDTO();
                        detailDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                        detailDTO.setStateId(stockTransDetailDTO.getStateId());
                        detailDTO.setQuantity(stockTransDetailDTO.getQuantity());
                        detailDTO.setProdOfferName(stockTransDetailDTO.getProdOfferName());
                        listStockOrderAgentDetailAdd.add(detailDTO);
                    }
                    if (stockTransDetailDTO.getQuantity() == null || stockTransDetailDTO.getQuantity() == 0L) {
                        if (!"".equals(strNumberErrIndex)) {
                            strNumberErrIndex += ",";
                        }
                        strNumberErrIndex += i;
                    }
                    i++;
                }
                mapError.put("VALID_ERROR_EMPTY_PROD_NAME", strProNameErrIndex);
                mapError.put("VALID_ERROR_EMPTY_QUANTITY", strNumberErrIndex);
                if (!DataUtil.isNullOrEmpty(mapError)) {
                    boolean isError = false;
                    if (!DataUtil.isNullOrEmpty(mapError.get("VALID_ERROR_EMPTY_PROD_NAME"))) {
                        isError = true;
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.inputText.require.msg");
                        executeCommand("focusElementErrorByListClass('txtProductName','" + mapError.get("VALID_ERROR_EMPTY_PROD_NAME") + "')");
                    }
                    if (!DataUtil.isNullOrEmpty(mapError.get("VALID_ERROR_EMPTY_QUANTITY"))) {
                        isError = true;
                        reportError("", ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.number.format.msg");
                        executeCommand("focusElementErrorByListClass('txtNumber','" + mapError.get("VALID_ERROR_EMPTY_QUANTITY") + "')");
                    }
                    if (isError) {
                        topPage();
                    }
                }
            }
        } catch (LogicException lex) {
            reportError(lex);
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    // check validate
    private boolean validateProductOfferingState(List<ListProductOfferDTO> lsListProductOfferDTO) {
        if (!DataUtil.isNullOrEmpty(lsListProductOfferDTO)) {
            int size = lsListProductOfferDTO.size();
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    ListProductOfferDTO dtoBefor = lsListProductOfferDTO.get(i);
                    ListProductOfferDTO dtoAffer = lsListProductOfferDTO.get(j);
                    if (!DataUtil.isNullObject(dtoAffer.getProductOfferingTotalDTO())) {
                        if (DataUtil.safeEqual(dtoBefor.getProductOfferingTotalDTO().getProductOfferingId(), dtoAffer.getProductOfferingTotalDTO().getProductOfferingId())
                                && DataUtil.safeEqual(dtoBefor.getStrStateId(), dtoAffer.getStrStateId())) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    @Secured("@")
    public void next() {
        try {
            showRequest = false;
            showList = false;
            showSearch = true;
            showListDetail = false;
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    @Secured("@")
    public void doShowInfoOrderDetail(StockOrderAgentDTO stockOrderAgentDTO) {
        try {
            showListDetail = true;
            Long stockOrderAgentId = stockOrderAgentDTO.getStockOrderAgentId();
            String requestCode = stockOrderAgentDTO.getRequestCode();
            listStockOrderAgentDetail = stockOrderAgentDetailService.getByStockOrderAgentId(stockOrderAgentId);
            List<StockOrderAgentDetailDTO> listDetailTemp = Lists.newArrayList();
            if (!DataUtil.isNullOrEmpty(listStockOrderAgentDetail)) {
                for (StockOrderAgentDetailDTO dto : listStockOrderAgentDetail) {
                    dto.setRequestCode(requestCode);
                    listDetailTemp.add(dto);
                }
            }
            listStockOrderAgentDetail = DataUtil.cloneBean(listDetailTemp);
            RequestContext.getCurrentInstance().execute("$('html, body').animate({scrollTop: document.getElementById( 'frmStockOrderAgent:listStockDetail').offsetTop-60}, 'fast')");
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    public String getCodeAndNameProduct(Long producOfferId) throws Exception {
        String result;
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(producOfferId);
        result = productOfferingDTO.getCode() + "-" + productOfferingDTO.getName();
        return result;
    }

    public String getStrStateId(Long stateId) throws Exception {
        String result = "";
        for (OptionSetValueDTO optionSetValueDTO : listState) {
            if (optionSetValueDTO.getValue().equals(stateId.toString())) {
                result = optionSetValueDTO.getName();
            }
        }
        return result;
    }

    public String getStrBankName(String bankCode) throws Exception {
        String result = "";
        for (OptionSetValueDTO optionSetValueDTO : listBank) {
            if (optionSetValueDTO.getValue().equals(bankCode)) {
                result = optionSetValueDTO.getName();
            }
        }
        return result;
    }

    @Secured("@")
    public void doDestroyStock() {
        try {
            StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentService.findOne(currentStockOrderAgentId);
            if (!currentStaff.getStaffId().equals(stockOrderAgentDTO.getCreateStaffId())) {
                topReportError("", "", "stockOrderAgent.deposit.not.role");
            }
            stockOrderAgentDTO.setStatus(Const.STOCK_ORDER_AGENT.STATUS_CANCEL);
            stockOrderAgentDTO.setNote(reason);
            stockOrderAgentService.update(stockOrderAgentDTO);
            searchStockOrderAgent();
            topReportSuccess("", "stock.export.cancel.success");
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("formUpload:mesagesDetail", ex);
            updateElemetId("formUpload:mesagesDetail");
            focusElementByRawCSSSlector(".reason");
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateDesTroyTwo() {
        try {
            if (DataUtil.isNullObject(reason)) {
                reportError("dlgReason:mesagesDetail", "", "stockOrderAgent.note.requite");
                topPage();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateDesTroy(Long stockOrderAgentId) {
        try {
            reason = "";
            if (DataUtil.isNullObject(stockOrderAgentSearch.getNote())) {
                reportError("", "", "stockOrderAgent.note.requite");
                topPage();
            }
            StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentService.findOne(stockOrderAgentId);
            if (DataUtil.isNullObject(stockOrderAgentDTO) ||
                    !stockOrderAgentDTO.getCreateStaffId().equals(currentStaff.getStaffId())) {
                reportError("", "", "stockOrderAgent.requite.role");
                topPage();

            }
            this.currentStockOrderAgentId = stockOrderAgentId;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "stockOrderAgent.requite.role");
            topPage();
        }
    }

    public void validateDate(Date fromDate, Date toDate) throws LogicException, Exception {
        Date sysdate = getSysdateFromDB();
        if (fromDate == null || toDate == null) {
            if (fromDate == null) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            } else {
                executeCommand("focusElementError('toDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
        } else {
            if (fromDate.after(sysdate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.valid");
            }
            if (toDate.before(fromDate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
            }
            if (toDate.before(fromDate)) {
                executeCommand("focusElementError('fromDate')");
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
            }
        }

    }

    public StockOrderAgentDTO getStockOrderAgentSearch() {
        return stockOrderAgentSearch;
    }

    public void setStockOrderAgentSearch(StockOrderAgentDTO stockOrderAgentSearch) {
        this.stockOrderAgentSearch = stockOrderAgentSearch;
    }

    public List<OptionSetValueDTO> getListBank() {
        return listBank;
    }

    public void setListBank(List<OptionSetValueDTO> listBank) {
        this.listBank = listBank;
    }

    public boolean isShowRole() {
        return showRole;
    }

    public void setShowRole(boolean showRole) {
        this.showRole = showRole;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShowList() {
        return showList;
    }

    public void setShowList(boolean showList) {
        this.showList = showList;
    }

    public List<StockOrderAgentDTO> getStockOrderAgentList() {
        return stockOrderAgentList;
    }

    public void setStockOrderAgentList(List<StockOrderAgentDTO> stockOrderAgentList) {
        this.stockOrderAgentList = stockOrderAgentList;
    }

    public List<StockOrderAgentDTO> getSelectedStockOrderAgent() {
        return selectedStockOrderAgent;
    }

    public void setSelectedStockOrderAgent(List<StockOrderAgentDTO> selectedStockOrderAgent) {
        this.selectedStockOrderAgent = selectedStockOrderAgent;
    }

    public Long getCurrentStockOrderAgentId() {
        return currentStockOrderAgentId;
    }

    public void setCurrentStockOrderAgentId(Long currentStockOrderAgentId) {
        this.currentStockOrderAgentId = currentStockOrderAgentId;
    }

    public boolean isShowRequest() {
        return showRequest;
    }

    public void setShowRequest(boolean showRequest) {
        this.showRequest = showRequest;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public StockOrderAgentDTO getStockOrderAgentAdd() {
        return stockOrderAgentAdd;
    }

    public void setStockOrderAgentAdd(StockOrderAgentDTO stockOrderAgentAdd) {
        this.stockOrderAgentAdd = stockOrderAgentAdd;
    }

    public List<StockOrderAgentDetailDTO> getListStockOrderAgentDetailAdd() {
        return listStockOrderAgentDetailAdd;
    }

    public void setListStockOrderAgentDetailAdd(List<StockOrderAgentDetailDTO> listStockOrderAgentDetailAdd) {
        this.listStockOrderAgentDetailAdd = listStockOrderAgentDetailAdd;
    }

    public boolean isShowListDetail() {
        return showListDetail;
    }

    public void setShowListDetail(boolean showListDetail) {
        this.showListDetail = showListDetail;
    }

    public List<StockOrderAgentDetailDTO> getListStockOrderAgentDetail() {
        return listStockOrderAgentDetail;
    }

    public void setListStockOrderAgentDetail(List<StockOrderAgentDetailDTO> listStockOrderAgentDetail) {
        this.listStockOrderAgentDetail = listStockOrderAgentDetail;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}


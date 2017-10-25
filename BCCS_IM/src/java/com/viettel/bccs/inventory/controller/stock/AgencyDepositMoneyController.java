package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.OrderStockTagNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
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
 * Created by hoangnt14 on 1/11/2016.
 */

@Component
@Scope("view")
@ManagedBean
public class AgencyDepositMoneyController extends TransCommonController {

    private Boolean infoOrderDetail = false;
    private Boolean writeOffice = false;
    private String nameMethod = "receiveWriteOffice";
    private int limitAutoComplete;
    private String actionCodeNote;
    private VStockTransDTO forSearch;
    private RequiredRoleMap requiredRoleMap;

    private List<OptionSetValueDTO> optionSetValueDTOsList;
    private List<VStockTransDTO> vStockTransDTOList;
    private List<StockTransFullDTO> lsStockTransFull;
    private ReceiptExpenseDTO receiptExpenseDTO;
    private Long stockTransActionId;
    private Long shopId;
    private List<String> lstDepositStatus;

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReceiptExpenseService receiptExpenseService;

    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private StockTransDetailService stockTransDetailService;

    private StaffDTO staffDTO;
    private Boolean canPrint;
    private List<OptionSetValueDTO> listPayMethod;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            lstDepositStatus = new ArrayList<>();
            lstDepositStatus.add(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_ORDER);
            lstDepositStatus.add(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
            String shopId = DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTagReceive.initAgent(this, shopId);
            doResetStockTrans();
            optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.STOCK_TRANS_DEPOSIT_STATUS);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            orderStockTag.init(this, writeOffice);
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doResetStockTrans() {
        forSearch = new VStockTransDTO();
        vStockTransDTOList = Lists.newArrayList();
        shopInfoTagReceive.resetShop();
        Date currentDate = Calendar.getInstance().getTime();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setFromOwnerName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
        forSearch.setFromOwnerID(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setStockTransType(Const.STOCK_TRANS.STOCK_TRANS_TYPE_DEPOSIT);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
        forSearch.setLstDepositStatus(lstDepositStatus);

        forSearch.setUserShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setVtUnit(Const.VT_UNIT.AGENT);
        forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
        infoOrderDetail = false;
    }

    @Secured("@")
    public void doShowInfoOrderDetail(Long stockTransActionId, String depositStatus) {
        try {
            if (DataUtil.safeEqual(depositStatus, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE)) {
                canPrint = true;
            } else {
                canPrint = false;
            }
            orderStockTag.loadOrderStock(stockTransActionId, true);
            List<Long> lsStockTransId = Lists.newArrayList(stockTransActionId);
            this.stockTransActionId = stockTransActionId;
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            actionCodeNote = receiptExpenseService.generateReceiptNo(BccsLoginSuccessHandler.getStaffDTO().getShopCode());
            //check kho cap tren la chi nhanh
            shopId = DataUtil.safeToLong(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerId());
//            Long branchId = shopService.checkParentShopIsBranch(shopId);
            ShopDTO shopDTO = shopService.findOne(shopId);
            Long branchId = shopService.getBranchId(shopId);
            Long shopImp = DataUtil.safeToLong(orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO().getOwnerId());
            ShopDTO shopImpDTO = shopService.findOne(shopImp);
            Long channelTypeId = shopImpDTO.getChannelTypeId();
            Long pricePolicy = DataUtil.safeToLong(shopImpDTO.getPricePolicy());
            if (shopService.checkChannelIsAgent(shopDTO.getChannelTypeId())) { // Neu kho xuat la dai ly uy quyen thi lay gia theo kho xuat
                channelTypeId = shopDTO.getChannelTypeId();
                pricePolicy = DataUtil.safeToLong(shopDTO.getPricePolicy());
            }

            boolean hasFullPrice = true;
            if (DataUtil.isNullObject(lsStockTransFull) || DataUtil.isNullObject(shopImpDTO)) {
                hasFullPrice = false;
            } else {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    List<Long> lstPrice = Lists.newArrayList();
                    List<ProductOfferPriceDTO> lstProductOfferPriceDTOs = productOfferPriceService.getPriceDepositAmount(stockTransFullDTO.getProdOfferId(),
                            branchId, pricePolicy, channelTypeId, null, null);
                    if (!DataUtil.isNullObject(lstProductOfferPriceDTOs)) {
                        for (ProductOfferPriceDTO productOfferPriceDTO : lstProductOfferPriceDTOs) {
                            lstPrice.add(productOfferPriceDTO.getPrice());
                        }
                    }
                    if (DataUtil.isNullOrEmpty(lstPrice)) {
                        hasFullPrice = false;
                        stockTransFullDTO.setTotalPrice(null);
                    } else {
                        stockTransFullDTO.setBasisPrice(lstPrice.get(0));
                        stockTransFullDTO.setPriceId(lstProductOfferPriceDTOs.get(0).getProductOfferPriceId());
                        stockTransFullDTO.setTotalPrice(stockTransFullDTO.getBasisPrice() * stockTransFullDTO.getQuantity());
                        stockTransFullDTO.setProductOfferPriceDTO(lstProductOfferPriceDTOs.get(0));
                    }
                    stockTransFullDTO.setLstPrice(lstPrice);
                    stockTransFullDTO.setProductOfferPriceDTOs(lstProductOfferPriceDTOs);


                }
            }
            //
            receiptExpenseDTO = new ReceiptExpenseDTO();

            Long amount = 0L;

            if (hasFullPrice) {
                if (!DataUtil.isNullObject(lsStockTransFull)) {
                    for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                        amount = amount + stockTransFullDTO.getTotalPrice();
                    }
                }
                receiptExpenseDTO.setAmount(amount);
            }
            receiptExpenseDTO.setReceiptNo(actionCodeNote);
            receiptExpenseDTO.setReasonName(getText("agency.deposit.reason.value"));
            receiptExpenseDTO.setDeliverShopName(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerCode() + "-" + orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerName());
            //
            receiptExpenseDTO.setPayMethod(Const.STOCK_STRANS_DEPOSIT.HTTT);
            listPayMethod = optionSetValueService.getByOptionSetCode(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_PAY_METHOD);
            receiptExpenseDTO.setReasonId(Const.STOCK_STRANS_DEPOSIT.REASON_ID);
            receiptExpenseDTO.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            receiptExpenseDTO.setStaffId(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            receiptExpenseDTO.setReceiptTypeId(Const.STOCK_TRANS.STOCK_TRANS_TYPE_RETRIEVE_DEPOSIT);
            receiptExpenseDTO.setType(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
            receiptExpenseDTO.setStatus(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT);

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
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        if (vShopStaffDTO != null) {
            forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void clearCurrentShop(VShopStaffDTO vShopStaffDTO) {
        forSearch.setToOwnerID(null);
    }


    @Secured("@")
    public void changePrice() {
        if (!DataUtil.isNullObject(lsStockTransFull)) {
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                List<ProductOfferPriceDTO> list = stockTransFullDTO.getProductOfferPriceDTOs();
                if (list != null && stockTransFullDTO.getPriceId() != null) {
                    list.parallelStream().forEach(productOfferPriceDTO -> {
                        if (productOfferPriceDTO.getProductOfferPriceId() != null && productOfferPriceDTO.getProductOfferPriceId().equals(stockTransFullDTO.getPriceId())) {
                            stockTransFullDTO.setProductOfferPriceDTO(productOfferPriceDTO);
                            stockTransFullDTO.setBasisPrice(productOfferPriceDTO.getPrice());
                            stockTransFullDTO.setTotalPrice(stockTransFullDTO.getBasisPrice() * stockTransFullDTO.getQuantity());
                        }
                    });
                }
            }
        }
        Long amount = 0L;
        if (!DataUtil.isNullObject(lsStockTransFull)) {
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                amount = amount + stockTransFullDTO.getTotalPrice();
            }
        }
        receiptExpenseDTO.setAmount(amount);
    }

    @Secured("@")
    public void doCreateExportNote() {
        try {
            orderStockTag.loadOrderStock(stockTransActionId, true);
            FilterRequest stockTranId = new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), orderStockTag.getStockTransDTO().getStockTransId());
            List<StockTransDetailDTO> stockTransDetails = stockTransDetailService.findByFilter(Lists.newArrayList(stockTranId));
            if (stockTransDetails != null && lsStockTransFull != null) {
                stockTransDetails.parallelStream().forEach(stockTransDetailDTO -> {
                    lsStockTransFull.parallelStream().forEach(stockTransFullDTO -> {
                        if (stockTransFullDTO.getStockTransDetailId() != null && stockTransDetailDTO.getStockTransDetailId() != null && stockTransFullDTO.getStockTransDetailId().equals(stockTransDetailDTO.getStockTransDetailId())) {
                            stockTransDetailDTO.setProductOfferPriceId(stockTransFullDTO.getPriceId());
                            stockTransDetailDTO.setBasisPrice(stockTransFullDTO.getBasisPrice());
                            stockTransDetailDTO.setPrice(stockTransFullDTO.getBasisPrice());
                            stockTransDetailDTO.setAmount(stockTransFullDTO.getTotalPrice());
                        }
                    });
                });
            }

            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            stockTransActionDTO.setActionCode(receiptExpenseDTO.getReceiptNo());

            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            stockTransDTO.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            stockTransDTO.setUserCreate(BccsLoginSuccessHandler.getStaffDTO().getName());
            stockTransDTO.setTotalAmount(receiptExpenseDTO.getAmount());
            stockTransDTO.setCreateDatetime(new Date());
            stockTransDTO.setReasonId(receiptExpenseDTO.getReasonId());
            stockTransDTO.setDevliver(receiptExpenseDTO.getDeliverer());
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetails, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }

            reportSuccess("", "export.note.deposit.create.success");
            canPrint = true;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
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

    public ReceiptExpenseDTO getReceiptExpenseDTO() {
        return receiptExpenseDTO;
    }

    public void setReceiptExpenseDTO(ReceiptExpenseDTO receiptExpenseDTO) {
        this.receiptExpenseDTO = receiptExpenseDTO;
    }

    public Long getStockTransActionId() {
        return stockTransActionId;
    }

    public void setStockTransActionId(Long stockTransActionId) {
        this.stockTransActionId = stockTransActionId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<String> getLstDepositStatus() {
        return lstDepositStatus;
    }

    public void setLstDepositStatus(List<String> lstDepositStatus) {
        this.lstDepositStatus = lstDepositStatus;
    }

    public Boolean getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(Boolean canPrint) {
        this.canPrint = canPrint;
    }

    public void doCancelDeposit(Long stockTransActionId) {
        try {
            orderStockTag.loadOrderStock(stockTransActionId, true);
            StockTransActionDTO stockTransActionDTO = orderStockTag.getStockTransActionDTO();
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTO();
            List<StockTransDetailDTO> stockTransDetailDTOs = stockTransDetailService.findByFilter(Lists.newArrayList(
                    new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), FilterRequest.Operator.EQ,
                            stockTransDTO.getStockTransId())));
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.STOCK_TRANS_DEPOSIT_CANCEl, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
            reportSuccess("frmExportNote:msgExport", "export.order.deposit.confirm.cancel.ok");
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


    public StreamedContent exportDepostiDetail() {
        try {
            StockTransDTO stockTransDTO = orderStockTag.getStockTransDTOForPint();
            if (stockTransDTO != null && canPrint) {
                FilterRequest stockTranId = new FilterRequest(StockTransDetail.COLUMNS.STOCKTRANSID.name(), orderStockTag.getStockTransDTO().getStockTransId());
                List<StockTransDetailDTO> stockTransDetails = stockTransDetailService.findByFilter(Lists.newArrayList(stockTranId));
                if (stockTransDetails != null && lsStockTransFull != null) {
                    stockTransDetails.parallelStream().forEach(stockTransDetailDTO -> {
                        lsStockTransFull.parallelStream().forEach(stockTransFullDTO -> {
                            if (stockTransFullDTO.getStockTransDetailId() != null && stockTransDetailDTO.getStockTransDetailId() != null && stockTransFullDTO.getStockTransDetailId().equals(stockTransDetailDTO.getStockTransDetailId())) {
                                stockTransDetailDTO.setProductOfferPriceId(stockTransFullDTO.getPriceId());
                                stockTransDetailDTO.setBasisPrice(stockTransFullDTO.getBasisPrice());
                                stockTransDetailDTO.setPrice(stockTransFullDTO.getBasisPrice());
                                stockTransDetailDTO.setAmount(stockTransFullDTO.getTotalPrice());
                                stockTransDetailDTO.setProdOfferName(stockTransFullDTO.getProdOfferName());
                            }
                        });
                    });
                }
                return exportDepositDetail(stockTransDTO, receiptExpenseDTO, stockTransDetails);
            } else {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stockTrans.dto.print.not.exists");
            }
        } catch (LogicException ex) {
            topReportError("", ex);
        } catch (Exception ex) {
            topReportError("", "common.error.happen", ex);
        }
        return null;
    }

    public List<OptionSetValueDTO> getListPayMethod() {
        return listPayMethod;
    }

    public void setListPayMethod(List<OptionSetValueDTO> listPayMethod) {
        this.listPayMethod = listPayMethod;
    }
}
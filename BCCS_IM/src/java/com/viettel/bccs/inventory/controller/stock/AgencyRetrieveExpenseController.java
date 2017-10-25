package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
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
import com.viettel.web.common.controller.BaseController;
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
 * Created by hoangnt14 on 1/15/2016.
 */

@Component
@Scope("view")
@ManagedBean
public class AgencyRetrieveExpenseController extends BaseController {

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
    private Long agentId;
    private List<String> lstDepositStatus;

    @Autowired
    private OrderStockTagNameable orderStockTag;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransDetailService stockTransDetailService;
    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ExecuteStockTransService executeStockTransService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ReceiptExpenseService receiptExpenseService;
    @Autowired
    private DepositService depositService;
    @Autowired
    private DepositDetailService depositDetailService;
    private StaffDTO staffDTO;
    private Boolean doDelete;
    private Boolean canExpense;
    private List<OptionSetValueDTO> listPayMethod;


    @Secured("@")
    @PostConstruct
    public void init() {
        try {

            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            lstDepositStatus = new ArrayList<>();
            lstDepositStatus.add(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_NOTE);
            lstDepositStatus.add(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
            String shopId = DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTagReceive.initAgent(this, shopId);
            doResetStockTrans();
            optionSetValueDTOsList = optionSetValueService.getByOptionSetCode(Const.OPTION_SET.AGENT_RETRIEVE_EXPENSE_STATUS);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            if (!DataUtil.isNullOrEmpty(forSearch.getActionCode())) {
                forSearch.setActionCode(forSearch.getActionCode().trim());
            }
            vStockTransDTOList = stockTransService.searchVStockTrans(forSearch);
            if (!DataUtil.isNullObject(vStockTransDTOList)) {
                for (VStockTransDTO vStockTransDTO : vStockTransDTOList) {
                    for (OptionSetValueDTO optionSetValueDTO : optionSetValueDTOsList) {
                        if (DataUtil.safeEqual(vStockTransDTO.getDepositStatus(), optionSetValueDTO.getValue())) {
                            vStockTransDTO.setStatusName(optionSetValueDTO.getName());
                            break;
                        }
                    }
                }
            }
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
        forSearch.setToOwnerName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
        forSearch.setToOwnerID(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setStockTransType(Const.STOCK_TRANS.STOCK_TRANS_TYPE_RETRIEVE_DEPOSIT);
        forSearch.setStockTransStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        forSearch.setLstDepositStatus(lstDepositStatus);
        forSearch.setUserShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setVtUnit(Const.VT_UNIT.AGENT);
        forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
        infoOrderDetail = false;
    }

    @Secured("@")
    public void doShowInfoOrderDetail(VStockTransDTO stockTransDTO) {
        try {
            doDelete = false;
            canExpense = true;
            orderStockTag.loadOrderStock(stockTransDTO.getActionID(), true);
            List<Long> lsStockTransId = Lists.newArrayList(stockTransDTO.getActionID());
            this.stockTransActionId = stockTransDTO.getStockTransID();
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            actionCodeNote = receiptExpenseService.generateReceiptNo(BccsLoginSuccessHandler.getStaffDTO().getShopCode());
            shopId = DataUtil.safeToLong(orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO().getOwnerId());
//            Long branchId = shopService.checkParentShopIsBranch(shopId);
            ShopDTO shopDTO = shopService.findOne(shopId);
            Long branchId = shopService.getBranchId(shopId);
            Long shopImp = DataUtil.safeToLong(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerId());
            ShopDTO shopImpDTO = shopService.findOne(shopImp);
            Long channelTypeId = shopImpDTO.getChannelTypeId();
            Long pricePolicy = DataUtil.safeToLong(shopImpDTO.getPricePolicy());
            if (shopService.checkChannelIsAgent(shopDTO.getChannelTypeId())) { // Neu kho xuat la dai ly uy quyen thi lay gia theo kho xuat
                channelTypeId = shopDTO.getChannelTypeId();
                pricePolicy = DataUtil.safeToLong(shopDTO.getPricePolicy());
            }
            if (!DataUtil.isNullObject(lsStockTransFull) && !DataUtil.isNullObject(shopDTO)) {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    List<Long> lstPrice = Lists.newArrayList();
                    List<ProductOfferPriceDTO> lstProductOfferPriceDTOs = productOfferPriceService.getPriceDepositAmount(stockTransFullDTO.getProdOfferId(),
                            branchId, pricePolicy, channelTypeId, null, null);
                    if (!DataUtil.isNullObject(lstProductOfferPriceDTOs)) {
                        for (ProductOfferPriceDTO productOfferPriceDTO : lstProductOfferPriceDTOs) {
                            lstPrice.add(productOfferPriceDTO.getPrice());
                        }
                    }
//                    if (lstPrice.size() == 0) {
//                        throw new LogicException()
//                    }
                    if (lstPrice.size() == 0) {
                        canExpense = false;
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.product.empty");
                    }
                    stockTransFullDTO.setBasisPrice(lstPrice.get(0));
                    stockTransFullDTO.setLstPrice(lstPrice);
                }
            }
            //
            receiptExpenseDTO = new ReceiptExpenseDTO();
            if (!DataUtil.isNullObject(lsStockTransFull)) {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    stockTransFullDTO.setTotalPrice(stockTransFullDTO.getBasisPrice() * stockTransFullDTO.getQuantity());
                }
            }
            Long amount = 0L;
            if (!DataUtil.isNullObject(lsStockTransFull)) {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    amount = amount + stockTransFullDTO.getTotalPrice();
                }
            }
            //
            listPayMethod = optionSetValueService.getByOptionSetCode(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_PAY_METHOD);
            receiptExpenseDTO.setAmount(amount);
            receiptExpenseDTO.setReceiptNo(actionCodeNote);
            receiptExpenseDTO.setReasonName(stockTransDTO.getReasonName());
            receiptExpenseDTO.setDeliverShopName(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerCode() + "-" + orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerName());
            //
            receiptExpenseDTO.setPayMethod(Const.STOCK_STRANS_DEPOSIT.HTTT);
            receiptExpenseDTO.setReasonId(stockTransDTO.getReasonID());
            receiptExpenseDTO.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            receiptExpenseDTO.setStaffId(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            receiptExpenseDTO.setReceiptTypeId(Const.STOCK_TRANS.STOCK_TRANS_TYPE_EXPENSE);
            receiptExpenseDTO.setType(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
            receiptExpenseDTO.setStatus(Const.STOCK_TRANS.STOCK_TRANS_STATUS_DEPOSIT);

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
    public void doDeleteOrder(VStockTransDTO stockTransDTO) {
        try {
            doDelete = true;
            orderStockTag.loadOrderStock(stockTransDTO.getActionID(), true);
            List<Long> lsStockTransId = Lists.newArrayList(stockTransDTO.getActionID());
            this.stockTransActionId = stockTransDTO.getStockTransID();
            lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(lsStockTransId), new ArrayList<StockTransFullDTO>());
            infoOrderDetail = true;
            //check kho cap tren la chi nhanh
            shopId = DataUtil.safeToLong(orderStockTag.getShopInfoReceiveTag().getvShopStaffDTO().getOwnerId());
//            Long branchId = shopService.checkParentShopIsBranch(shopId);
            ShopDTO shopDTO = shopService.findOne(shopId);
            Long branchId = shopService.getBranchId(shopId);
            Long shopImp = DataUtil.safeToLong(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerId());
            ShopDTO shopImpDTO = shopService.findOne(shopImp);
            Long channelTypeId = shopImpDTO.getChannelTypeId();
            Long pricePolicy = DataUtil.safeToLong(shopImpDTO.getPricePolicy());
            if (shopService.checkChannelIsAgent(shopDTO.getChannelTypeId())) { // Neu kho xuat la dai ly uy quyen thi lay gia theo kho xuat
                channelTypeId = shopDTO.getChannelTypeId();
                pricePolicy = DataUtil.safeToLong(shopDTO.getPricePolicy());
            }

            if (!DataUtil.isNullObject(lsStockTransFull) && !DataUtil.isNullObject(shopDTO)) {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    List<Long> lstPrice = Lists.newArrayList();
                    List<ProductOfferPriceDTO> lstProductOfferPriceDTOs = productOfferPriceService.getPriceDepositAmount(stockTransFullDTO.getProdOfferId(),
                            branchId, pricePolicy, channelTypeId, null, null);
                    if (!DataUtil.isNullObject(lstProductOfferPriceDTOs)) {
                        for (ProductOfferPriceDTO productOfferPriceDTO : lstProductOfferPriceDTOs) {
                            lstPrice.add(productOfferPriceDTO.getPrice());
                        }
                    }
//                    if (lstPrice.size() == 0) {
//                        throw new LogicException()
//                    }
                    if (lstPrice.size() == 0) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.product.empty");
                    }
                    stockTransFullDTO.setBasisPrice(lstPrice.get(0));
                    stockTransFullDTO.setLstPrice(lstPrice);

                }
            }
            //
            receiptExpenseDTO = new ReceiptExpenseDTO();
            if (!DataUtil.isNullObject(lsStockTransFull)) {
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    stockTransFullDTO.setTotalPrice(stockTransFullDTO.getBasisPrice() * stockTransFullDTO.getQuantity());
                }
            }
            receiptExpenseDTO = receiptExpenseService.findByStockTransIdAndType(stockTransActionId, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TYPE_EXPENSE);
            receiptExpenseDTO.setReasonName(stockTransDTO.getReasonName());
            receiptExpenseDTO.setDeliverShopName(orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerCode() + "-" + orderStockTag.getShopInfoExportTag().getvShopStaffDTO().getOwnerName());
            //
            listPayMethod = optionSetValueService.getByOptionSetCode(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_PAY_METHOD);
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
            forSearch.setFromOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        }
    }

    @Secured("@")
    public void clearCurrentShop(String nameMethod) {
        forSearch.setFromOwnerID(null);
    }

    @Secured("@")
    public void changePrice() {
        if (!DataUtil.isNullObject(lsStockTransFull)) {
            for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                stockTransFullDTO.setTotalPrice(stockTransFullDTO.getBasisPrice() * stockTransFullDTO.getQuantity());
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
    public void doValidateExportNote() {
        try {
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionId);
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.not.exsits");
            }
            if (stockTransService.checkStatusRetrieveExpense(stockTransActionId, Const.STOCK_TRANS_STATUS.EXPORT_NOTE, null)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.not.status");
            }
            if (stockTransService.checkStatusRetrieveExpense(stockTransActionId, null, Const.STOCK_TRANS.STOCK_TRANS_STATUS_DEPOSIT)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.not.retrieve");
            }
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

    @Secured("@")
    public void doValidateDelete() {
        try {
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionId);
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.not.exsits");
            }
            if (stockTransService.checkStatusRetrieveExpense(stockTransActionId, Const.STOCK_TRANS_STATUS.EXPORT_NOTE, null)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.not.status");
            }
            if (stockTransService.checkStatusRetrieveExpense(stockTransActionId, null, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.agency.retrieve.expense.not.delete");
            }
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
            stockTransDTO.setStaffId(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            stockTransDTO.setUserCreate(BccsLoginSuccessHandler.getStaffDTO().getName());
            stockTransDTO.setTotalAmount(receiptExpenseDTO.getAmount());
            stockTransDTO.setCreateDatetime(new Date());
            stockTransDTO.setReasonId(receiptExpenseDTO.getReasonId());
            stockTransDTO.setDevliver(receiptExpenseDTO.getDeliverer());
            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.RETRIEVE_EXPENSE, Const.STOCK_TRANS_TYPE.EXPORT, stockTransDTO, stockTransActionDTO, stockTransDetails, requiredRoleMap);
            if (!DataUtil.isNullOrEmpty(message.getErrorCode())) {
                throw new LogicException(message.getErrorCode(), message.getKeyMsg());
            }
//            //save receipt
//            ReceiptExpenseDTO result = receiptExpenseService.create(receiptExpenseDTO, BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
//            //save deposit
//
//            if (!DataUtil.isNullObject(result)) {
//                DepositDTO depositDTO = new DepositDTO();
//                depositDTO.setAmount(result.getAmount());
//                depositDTO.setReceiptId(result.getReceiptId());
//                depositDTO.setReasonId(result.getReasonId());
//                depositDTO.setShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
//                depositDTO.setStaffId(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
//                depositDTO.setDeliverId(shopId);
//                depositDTO.setStockTransId(stockTransActionId);
//                depositDTO.setType(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TYPE_EXPENSE);
//                depositDTO.setStatus(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_NOTE);
//                depositDTO.setDepositTypeId(DataUtil.safeToLong(Const.STOCK_STRANS_DEPOSIT.DEPOSIT_TYPE_EXPENSE));
//                depositDTO = depositService.create(depositDTO);
//                //save deposit_detail
//                if (!DataUtil.isNullObject(lsStockTransFull)) {
//                    for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
//                        DepositDetailDTO depositDetailDTO = new DepositDetailDTO();
//                        depositDetailDTO.setAmount(stockTransFullDTO.getTotalPrice());
//                        depositDetailDTO.setDepositId(depositDTO.getDepositId());
//                        depositDetailDTO.setDepositType(DataUtil.safeToString(depositDTO.getDepositTypeId()));
//                        depositDetailDTO.setQuantity(stockTransFullDTO.getQuantity());
//                        depositDetailDTO.setPrice(stockTransFullDTO.getBasisPrice());
//                        depositDetailDTO.setStockModelId(stockTransFullDTO.getProdOfferId());
//                        depositDetailDTO.setPriceId(stockTransFullDTO.getProductOfferTypeId());
//                        depositDetailService.create(depositDetailDTO);
//                    }
//                }
//            }
//            //update deposit status stock_trans
//            stockTransService.updateDepositStatus(stockTransActionId, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
//            //
//            staffDTO = staffService.findOne(staffDTO.getStaffId());
//            staffDTO.setStockNumImp(staffDTO.getStockNumImp() + 1);
//            staffService.save(staffDTO);
            reportSuccess("", "export.note.deposit.expense.create.success");
            canExpense = false;
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

    @Secured("@")
    public void doDeleteNote() {
        try {
            depositService.update(receiptExpenseDTO.getDepositId(), Const.STOCK_STRANS_DEPOSIT.DEPOSIT_CANCEL);
            receiptExpenseService.update(receiptExpenseDTO, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_EXPENSE);
            stockTransService.updateDepositStatus(stockTransActionId, Const.STOCK_STRANS_DEPOSIT.DEPOSIT_RETRIEVE_NOTE);
            reportSuccess("", "export.note.deposit.expense.delete.success");
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

    public Boolean getDoDelete() {
        return doDelete;
    }

    public void setDoDelete(Boolean doDelete) {
        this.doDelete = doDelete;
    }

    public Boolean getCanExpense() {
        return canExpense;
    }

    public void setCanExpense(Boolean canExpense) {
        this.canExpense = canExpense;
    }

    public List<OptionSetValueDTO> getListPayMethod() {
        return listPayMethod;
    }

    public void setListPayMethod(List<OptionSetValueDTO> listPayMethod) {
        this.listPayMethod = listPayMethod;
    }
}
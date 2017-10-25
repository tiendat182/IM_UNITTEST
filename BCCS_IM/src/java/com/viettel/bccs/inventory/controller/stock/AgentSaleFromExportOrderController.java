package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.logging.Kpi;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.AddGoodSaleMessage;
import com.viettel.bccs.inventory.model.OptionSet;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.product.dto.SaleProgramDTO;
import com.viettel.bccs.product.service.SaleProgramService;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * anonymous
 */
@Component
@Scope("view")
@ManagedBean(name = "agentSaleFromExportOrderController")
public class AgentSaleFromExportOrderController extends BaseController {
    private boolean inforOrderDetail = false;
    private VStockTransDTO forSearch;
    private int limitAutoComplete;
    private List<VStockTransDTO> vStockTransDTOList;
    private VStockTransDTO stockTransDTO;
    private boolean hideCustomer;
    private SaleTransInfoDTO saleTransInfoDTO;
    private List<ReasonSaleToAgentDTO> listReasonSaleTransDTO = Lists.newArrayList();
    private List<OptionSetValueDTO> listTypePayment = Lists.newArrayList();
    private List<ProgramSaleDTO> listSaleProgramInventoryDTO = Lists.newArrayList();
    private List<DiscountDTO> listDiscountDTO = Lists.newArrayList();
    private List<ProductOfferingDTO> productOfferingDTOs = Lists.newArrayList();
    private boolean printInvoice = true;
    private boolean receiveInvoice = true;
    private boolean createTrans;
    private boolean stockOrderAgent;
    private boolean checkBranchBankplus;
    private Long saleProgramId;

    @Autowired
    private ShopInfoNameable shopInfoTagReceive;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockOrderAgentService stockOrderAgentService;
    @Autowired
    private SaleWs saleWs;
    @Autowired
    SaleProgramService saleProgramService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferingService productOfferingService;

    @PostConstruct
    public void init() {
        try {
            String shopId = DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            shopInfoTagReceive.initAgent(this, shopId);
            doResetStockTrans();
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doSearchStockTrans() {
        try {
            vStockTransDTOList = stockTransService.searchVStockTransAgent(forSearch);
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
        hideCustomer = false;
        forSearch = new VStockTransDTO();
        stockTransDTO = null;
        saleProgramId = null;
        vStockTransDTOList = Lists.newArrayList();
        Date currentDate = Calendar.getInstance().getTime();
        forSearch = new VStockTransDTO(currentDate, currentDate);
        forSearch.setFromOwnerName(BccsLoginSuccessHandler.getStaffDTO().getShopName());
        forSearch.setFromOwnerID(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setUserShopId(BccsLoginSuccessHandler.getStaffDTO().getShopId());
        forSearch.setVtUnit(Const.VT_UNIT.AGENT);
        forSearch.setObjectType(Const.OWNER_TYPE.SHOP);
        shopInfoTagReceive.resetShop();
        inforOrderDetail = false;
        createTrans = false;
        stockOrderAgent = true;
        checkBranchBankplus = true;
    }

    @Secured("@")
    public void doShowInfoOrderDetail(VStockTransDTO vStockTransDTO) {
        try {
            listDiscountDTO = null;
            saleTransInfoDTO = new SaleTransInfoDTO();
            stockTransDTO = vStockTransDTO;
            saleTransInfoDTO.setSaleDate(vStockTransDTO.getCreateDateTrans());
            saleTransInfoDTO.setCustName(vStockTransDTO.getToOwnerName());
            ShopDTO shopDTO = shopService.findOne(vStockTransDTO.getToOwnerID());
            if (DataUtil.isNullObject(shopDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_NULL_POINTER_EX, "agent.sale.shop.notFound");
            }
            if (!DataUtil.isNullObject(shopDTO)) {
                saleTransInfoDTO.setEmail(shopDTO.getEmail());
                saleTransInfoDTO.setTelNumber(shopDTO.getTelNumber());
            }
            // Kiem tra giao dich co thanh toan qua bankplus
            stockOrderAgent = true;
            checkBranchBankplus = true;
            if (DataUtil.safeEqual(Const.L_VT_SHOP_ID, shopDTO.getParentShopId())
                    || DataUtil.safeEqual(Const.CHANNEL_TYPE.CHANNEL_TYPE_AGENT_VTT, shopDTO.getChannelTypeId())) {
                checkBranchBankplus = false;
            }
            if (checkBranchBankplus) {
                List<StockOrderAgentDTO> stockOrderAgentDTOs = stockOrderAgentService.getStockOrderAgent(shopDTO.getShopId(), vStockTransDTO.getStockTransID());
                if (!DataUtil.isNullOrEmpty(stockOrderAgentDTOs)) {
                    StockOrderAgentDTO stockOrderAgentDTO = stockOrderAgentDTOs.get(0);
                    if (!DataUtil.isNullObject(stockOrderAgentDTO)) {
                        if (!DataUtil.safeEqual(Const.L_VT_SHOP_ID, shopDTO.getParentShopId())
                                && !DataUtil.safeEqual(Const.CHANNEL_TYPE.CHANNEL_TYPE_AGENT_VTT, shopDTO.getChannelTypeId())
                                && !DataUtil.isNullOrEmpty(stockOrderAgentDTO.getBankCode())) {
                            stockOrderAgent = false;
                            saleTransInfoDTO.setBankCode(stockOrderAgentDTO.getBankCode());
                        }
                    }
                }
            }
            saleTransInfoDTO.setTin(shopDTO.getTin());
            saleTransInfoDTO.setAddress(shopDTO.getAddress());
            saleTransInfoDTO.setStockTransId(vStockTransDTO.getStockTransID());
            // lay thong tin ly do
            List<ReasonSaleToAgentDTO> lstReason = saleWs.getReasonSaleToAgent();
            setListReasonSaleTransDTO(lstReason);
            // Lay thong tin hinh thuc hanh toan
            List<OptionSetValueDTO> lstPayMethod = Lists.newArrayList();
            if (stockOrderAgent) {
                lstPayMethod = saleWs.getPayMethod();
                // remove paymethodId = 16 (bankplus)
                List<OptionSetValueDTO> lstPayMethodNobankplus = Lists.newArrayList();
                for (OptionSetValueDTO optionSetValueDTO : lstPayMethod) {
                    if (DataUtil.safeEqual(optionSetValueDTO.getId(), Const.SALE_TRANS.PAY_METHOD_BANKPLUS)) {
                        continue;
                    }
                    lstPayMethodNobankplus.add(optionSetValueDTO);
                }
                lstPayMethod = lstPayMethodNobankplus;
            } else {
                OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
                optionSetValueDTO.setId(Const.SALE_TRANS.PAY_METHOD_BANKPLUS);
                optionSetValueDTO.setName(getText("paymethod.bankplus"));
                lstPayMethod.add(optionSetValueDTO);
            }
            setListTypePayment(lstPayMethod);
            // Lay thong tin chuong trinh ban hang
            List<SaleProgramDTO> saleProgramLst = saleProgramService.findAllSalesProgramActive();
            List<ProgramSaleDTO> lstProgram = null;
            if (!DataUtil.isNullOrEmpty(saleProgramLst)) {
                lstProgram = saleProgramLst.stream().map(
                        x -> new ProgramSaleDTO(x.getCode(),
                                x.getName(),
                                x.getSaleProgramId(),
                                x.getCreateDatetime(),
                                x.getStatus())).collect(Collectors.toList());
            }

            setListSaleProgramInventoryDTO(lstProgram);
            // Fill gia tri
            SaleTransInfoMin saleTransInfoMin = new SaleTransInfoMin();
            saleTransInfoMin.setStockTransId(vStockTransDTO.getStockTransID());
            saleTransInfoMin.setSaleDate(vStockTransDTO.getCreateDateTrans());
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            AddGoodSaleMessage addGoodSaleMessage = saleWs.addGoodAgent(saleTransInfoMin, staffDTO.getShopId(), staffDTO.getStaffId(), null, null, null);
            if (DataUtil.isNullObject(addGoodSaleMessage)
                    || DataUtil.isNullObject(addGoodSaleMessage.getSaleTransInfo())
                    || DataUtil.isNullObject(addGoodSaleMessage.getSaleTransInfo().getSaleTrans())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_NULL_POINTER_EX, "agent.sale.webservice.addGood.notFound");
            }
            if (!DataUtil.isNullObject(addGoodSaleMessage.getSuccess()) && !addGoodSaleMessage.getSuccess()) {
                if (DataUtil.safeEqual(addGoodSaleMessage.getErrorCode(), "EU103100016")) {
                    createTrans = true;
                } else {
                    throw new LogicException(addGoodSaleMessage.getErrorCode(), getText("agent.sale.call.ws") + " " + addGoodSaleMessage.getDescription());
                }
            }
            List<StockSaleInfo> lstStockSale = addGoodSaleMessage.getStockSaleInfoLst();
            productOfferingDTOs = Lists.newArrayList();
            if (!DataUtil.isNullOrEmpty(lstStockSale)) {
                for (StockSaleInfo stockSaleInfo : lstStockSale) {
                    ProductOfferingDTO productOfferingDTO = stockSaleInfo.getStockModel();
                    if (DataUtil.isNullObject(productOfferingDTO)) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "agent.sale.webservice.addGood.notFound");
                    }
                    ProductOfferingDTO productOfferingDTOTemp = productOfferingService.findOne(productOfferingDTO.getProductOfferingId());
                    if (DataUtil.isNullObject(productOfferingDTOTemp)) {
                        throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "crt.err.offer.not.found", productOfferingDTO.getProductOfferingId());
                    }
                    ProductOfferPriceDTO productOfferPriceDTO = stockSaleInfo.getPrice();
                    productOfferingDTO.setQuantity(stockSaleInfo.getQuantity());
                    productOfferingDTO.setAmount(DataUtil.safeToLong(stockSaleInfo.getQuantity()) * DataUtil.safeToLong(productOfferPriceDTO.getPrice()));
                    List<ProductOfferPriceDTO> lstProductOfferPrice = stockSaleInfo.getPriceLst();
                    if (DataUtil.isNullOrEmpty(lstProductOfferPrice)) {
                        createTrans = true;
                        //throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "agent.sale.webservice.addGood.product.offer.price.notFound", productOfferingDTOTemp.getName());
                    }
                    productOfferingDTO.setProductOfferPriceDTOList(lstProductOfferPrice);
                    productOfferingDTOs.add(productOfferingDTO);
                }
            }
            listDiscountDTO = addGoodSaleMessage.getDiscountInfoLst();
            Long totalDiscountAfterTax = 0L;
            if (!DataUtil.isNullOrEmpty(listDiscountDTO)) {
                for (DiscountDTO discountDTO : listDiscountDTO) {
                    totalDiscountAfterTax += discountDTO.getDiscountAmount();
                    discountDTO.setDiscountRateString(String.format("%.2g%n", DataUtil.safeToDouble(discountDTO.getDiscountRate()) * 100) + " %");
                }
            }
            SaleTransDTO saleTransDTO = addGoodSaleMessage.getSaleTransInfo().getSaleTrans();
            saleTransInfoDTO.setAmountNotTax(saleTransDTO.getAmountNotTax());
            saleTransInfoDTO.setTax(saleTransDTO.getTax());
            saleTransInfoDTO.setTotalDiscount(saleTransDTO.getDiscount());
            saleTransInfoDTO.setAmountTax(saleTransDTO.getAmountTax());
            saleTransInfoDTO.setTotalDiscountAfterTax(totalDiscountAfterTax);

            inforOrderDetail = true;
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doValidateCreateTrans() {
        try {
            if (receiveInvoice) {
                if (DataUtil.isNullOrEmpty(saleTransInfoDTO.getTelNumber()) && DataUtil.isNullOrEmpty(saleTransInfoDTO.getEmail())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "agent.sale.validate.telNumber.email");
                }
                if (DataUtil.isNullOrEmpty(saleTransInfoDTO.getCustName())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "agent.sale.validate.custName.not.empty");
                }
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Kpi("ID_KPI_AGENT_SALE_EXPORT_ORDER_CREATE_TRANS")
    @Secured("@")
    public void doCreateSaleTrans() {
        try {
            SaleTransInfoMin saleTransInfoMin = new SaleTransInfoMin();
            saleTransInfoMin.setStockTransId(saleTransInfoDTO.getStockTransId());
            saleTransInfoMin.setSaleDate(saleTransInfoDTO.getSaleDate());
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            Long reasonId = saleTransInfoDTO.getReasonId();
            Long paymentMethodId = saleTransInfoDTO.getPayMethodId();
            Long saleProgramId = saleTransInfoDTO.getRecordWorkId();
            PayBankAccount payBankAccount = new PayBankAccount();
            payBankAccount.setBankCode(saleTransInfoDTO.getBankCode());
            saleTransInfoMin.setPayBankAccount(payBankAccount);
            saleTransInfoMin.setStockOrderAgent(saleTransInfoDTO.isStockOrderAgent());
            saleTransInfoMin.setCustName(saleTransInfoDTO.getCustName());
            saleTransInfoMin.setPrintInvoice(printInvoice);
            if (receiveInvoice) {
                saleTransInfoMin.setTelNumber(saleTransInfoDTO.getTelNumber());
                saleTransInfoMin.setEmail(saleTransInfoDTO.getEmail());
            }
            AddGoodSaleMessage addGoodSaleMessage = saleWs.saleToAgent(saleTransInfoMin, staffDTO.getShopId(), staffDTO.getStaffId(), saleProgramId, paymentMethodId, reasonId);
            if (DataUtil.isNullObject(addGoodSaleMessage)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_NULL_POINTER_EX, "agent.sale.webservice.addGood.notFound");
            }
            if (!DataUtil.isNullObject(addGoodSaleMessage.getSuccess()) && !addGoodSaleMessage.getSuccess()) {
                throw new LogicException(addGoodSaleMessage.getErrorCode(), addGoodSaleMessage.getDescription());
            }
//            inforOrderDetail = false;
            createTrans = true;
            reportSuccess("", "agent.sale.pay.create.trans.success");
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    @Secured("@")
    public void doReceiveShop(VShopStaffDTO vShopStaffDTO) {
        forSearch.setToOwnerID(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void clearCurrentShop() {
        forSearch.setToOwnerID(null);
    }

    @Secured("@")
    public void doBackPage() {
        printInvoice = true;
        receiveInvoice = true;
        inforOrderDetail = false;
        vStockTransDTOList = Lists.newArrayList();
        saleTransInfoDTO = new SaleTransInfoDTO();
        listDiscountDTO = Lists.newArrayList();
        productOfferingDTOs = Lists.newArrayList();
        stockTransDTO = new VStockTransDTO();
        createTrans = false;
        doResetStockTrans();
    }

    @Secured("@")
    public void showCustomer() {
        hideCustomer = true;
    }

    @Secured("@")
    public void doChangePayment() {
        Long remainAmount;
        if (DataUtil.isNullObject(saleTransInfoDTO)) {
            return;
        }
        Long payAmount = DataUtil.safeToLong(saleTransInfoDTO.getPaidAmount());
        remainAmount = payAmount - DataUtil.safeToLong(saleTransInfoDTO.getAmountTax());
        saleTransInfoDTO.setRemainAmount(remainAmount);
    }

    @Secured("@")
    public void onChangePayMethod() {
        try {
            if (!DataUtil.isNullObject(stockTransDTO)) {
                // reset du lieu
                productOfferingDTOs = Lists.newArrayList();
                listDiscountDTO = Lists.newArrayList();
                saleTransInfoDTO.setAmountNotTax(0L);
                saleTransInfoDTO.setTax(0L);
                saleTransInfoDTO.setTotalDiscount(0L);
                saleTransInfoDTO.setAmountTax(0L);
                saleTransInfoDTO.setTotalDiscountAfterTax(0L);
                // end
                SaleTransInfoMin saleTransInfoMin = new SaleTransInfoMin();
                saleTransInfoMin.setStockTransId(stockTransDTO.getStockTransID());
                saleTransInfoMin.setSaleDate(stockTransDTO.getCreateDateTrans());
                StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
                Long programId = null;
                if (!DataUtil.isNullOrZero(saleProgramId)) {
                    programId = saleProgramId;
                }
                AddGoodSaleMessage addGoodSaleMessage = saleWs.addGoodAgent(saleTransInfoMin, staffDTO.getShopId(), staffDTO.getStaffId(), programId, null, null);
                if (DataUtil.isNullObject(addGoodSaleMessage)
                        || DataUtil.isNullObject(addGoodSaleMessage.getSaleTransInfo())
                        || DataUtil.isNullObject(addGoodSaleMessage.getSaleTransInfo().getSaleTrans())) {
                    throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_NULL_POINTER_EX, "agent.sale.webservice.addGood.notFound");
                }
                if (!DataUtil.isNullObject(addGoodSaleMessage.getSuccess()) && !addGoodSaleMessage.getSuccess()) {
                    throw new LogicException(addGoodSaleMessage.getErrorCode(), getText("agent.sale.call.ws") + " " + addGoodSaleMessage.getDescription());
                }
                List<StockSaleInfo> lstStockSale = addGoodSaleMessage.getStockSaleInfoLst();
                if (!DataUtil.isNullOrEmpty(lstStockSale)) {
                    for (StockSaleInfo stockSaleInfo : lstStockSale) {
                        ProductOfferingDTO productOfferingDTO = stockSaleInfo.getStockModel();
                        if (DataUtil.isNullObject(productOfferingDTO)) {
                            continue;
                        }
                        ProductOfferingDTO productOfferingDTOTemp = productOfferingService.findOne(productOfferingDTO.getProductOfferingId());
                        if (DataUtil.isNullObject(productOfferingDTOTemp)) {
                            continue;
                        }
                        ProductOfferPriceDTO productOfferPriceDTO = stockSaleInfo.getPrice();
                        productOfferingDTO.setQuantity(stockSaleInfo.getQuantity());
                        productOfferingDTO.setAmount(DataUtil.safeToLong(stockSaleInfo.getQuantity()) * DataUtil.safeToLong(productOfferPriceDTO.getPrice()));
                        productOfferingDTO.setProductOfferPriceDTOList(stockSaleInfo.getPriceLst());
                        productOfferingDTOs.add(productOfferingDTO);
                    }
                }
                listDiscountDTO = addGoodSaleMessage.getDiscountInfoLst();
                Long totalDiscountAfterTax = 0L;
                if (!DataUtil.isNullOrEmpty(listDiscountDTO)) {
                    for (DiscountDTO discountDTO : listDiscountDTO) {
                        totalDiscountAfterTax += discountDTO.getDiscountAmount();
                        discountDTO.setDiscountRateString(String.format("%.2g%n", DataUtil.safeToDouble(discountDTO.getDiscountRate()) * 100) + " %");
                    }
                }
                SaleTransDTO saleTransDTO = addGoodSaleMessage.getSaleTransInfo().getSaleTrans();
                saleTransInfoDTO.setAmountNotTax(saleTransDTO.getAmountNotTax());
                saleTransInfoDTO.setTax(saleTransDTO.getTax());
                saleTransInfoDTO.setTotalDiscount(saleTransDTO.getDiscount());
                saleTransInfoDTO.setAmountTax(saleTransDTO.getAmountTax());
                saleTransInfoDTO.setTotalDiscountAfterTax(totalDiscountAfterTax);
                createTrans = false;
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
            createTrans = true;
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
            createTrans = true;
        }
    }

    public boolean isInforOrderDetail() {
        return inforOrderDetail;
    }

    public void setInforOrderDetail(boolean inforOrderDetail) {
        this.inforOrderDetail = inforOrderDetail;
    }

    public VStockTransDTO getForSearch() {
        return forSearch;
    }

    public void setForSearch(VStockTransDTO forSearch) {
        this.forSearch = forSearch;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public List<VStockTransDTO> getvStockTransDTOList() {
        return vStockTransDTOList;
    }

    public void setvStockTransDTOList(List<VStockTransDTO> vStockTransDTOList) {
        this.vStockTransDTOList = vStockTransDTOList;
    }

    public boolean isHideCustomer() {
        return hideCustomer;
    }

    public void setHideCustomer(boolean hideCustomer) {
        this.hideCustomer = hideCustomer;
    }

    public SaleTransInfoDTO getSaleTransInfoDTO() {
        return saleTransInfoDTO;
    }

    public void setSaleTransInfoDTO(SaleTransInfoDTO saleTransInfoDTO) {
        this.saleTransInfoDTO = saleTransInfoDTO;
    }

    public List<ReasonSaleToAgentDTO> getListReasonSaleTransDTO() {
        return listReasonSaleTransDTO;
    }

    public void setListReasonSaleTransDTO(List<ReasonSaleToAgentDTO> listReasonSaleTransDTO) {
        this.listReasonSaleTransDTO = listReasonSaleTransDTO;
    }

    public List<OptionSetValueDTO> getListTypePayment() {
        return listTypePayment;
    }

    public void setListTypePayment(List<OptionSetValueDTO> listTypePayment) {
        this.listTypePayment = listTypePayment;
    }

    public List<ProgramSaleDTO> getListSaleProgramInventoryDTO() {
        return listSaleProgramInventoryDTO;
    }

    public void setListSaleProgramInventoryDTO(List<ProgramSaleDTO> listSaleProgramInventoryDTO) {
        this.listSaleProgramInventoryDTO = listSaleProgramInventoryDTO;
    }

    public List<DiscountDTO> getListDiscountDTO() {
        return listDiscountDTO;
    }

    public void setListDiscountDTO(List<DiscountDTO> listDiscountDTO) {
        this.listDiscountDTO = listDiscountDTO;
    }

    public List<ProductOfferingDTO> getProductOfferingDTOs() {
        return productOfferingDTOs;
    }

    public void ProductOfferingDTO(List<ProductOfferingDTO> productOfferingDTOs) {
        this.productOfferingDTOs = productOfferingDTOs;
    }

    public boolean isPrintInvoice() {
        return printInvoice;
    }

    public void setPrintInvoice(boolean printInvoice) {
        this.printInvoice = printInvoice;
    }

    public void setProductOfferingDTOs(List<ProductOfferingDTO> productOfferingDTOs) {
        this.productOfferingDTOs = productOfferingDTOs;
    }

    public ShopInfoNameable getShopInfoTagReceive() {
        return shopInfoTagReceive;
    }

    public void setShopInfoTagReceive(ShopInfoNameable shopInfoTagReceive) {
        this.shopInfoTagReceive = shopInfoTagReceive;
    }

    public VStockTransDTO getStockTransDTO() {
        return stockTransDTO;
    }

    public void setStockTransDTO(VStockTransDTO stockTransDTO) {
        this.stockTransDTO = stockTransDTO;
    }

    public boolean isCreateTrans() {
        return createTrans;
    }

    public void setCreateTrans(boolean createTrans) {
        this.createTrans = createTrans;
    }

    public boolean isStockOrderAgent() {
        return stockOrderAgent;
    }

    public void setStockOrderAgent(boolean stockOrderAgent) {
        this.stockOrderAgent = stockOrderAgent;
    }

    public boolean isReceiveInvoice() {
        return receiveInvoice;
    }

    public void setReceiveInvoice(boolean receiveInvoice) {
        this.receiveInvoice = receiveInvoice;
    }

    public boolean isCheckBranchBankplus() {
        return checkBranchBankplus;
    }

    public void setCheckBranchBankplus(boolean checkBranchBankplus) {
        this.checkBranchBankplus = checkBranchBankplus;
    }

    public Long getSaleProgramId() {
        return saleProgramId;
    }

    public void setSaleProgramId(Long saleProgramId) {
        this.saleProgramId = saleProgramId;
    }
}

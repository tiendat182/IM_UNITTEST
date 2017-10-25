package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.*;
import java.util.stream.Collectors;

/**
 * controller chuc nang thu hoi hang tot
 * Created by thanhnt77 20/102016
 */
@Component
@Scope("view")
@ManagedBean
public class RecoveryProductCreateRequestController extends InventoryController {

    private String stateId;
    private Long prodOfferTypeId = Const.STOCK_TYPE.STOCK_HANDSET;
    private int limitAutoComplete;
    private boolean showCreateRequest;
    private boolean viewDetaiRequest;
    private Long stockRequestOrderIdCancel;
    private String stockRequestOrderIdCancelOrderCode;

    private StaffDTO staffDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private VShopStaffDTO vShopStaffDTO;
    private StockRequestOrderDTO requestOrderDTOSearch;
    private StockRequestOrderDTO stockRequestOrderDTOView;

    private List<ProductOfferingTotalDTO> lsStockTotalSearch;
    private List<OptionSetValueDTO> lsProvince;
    private List<ProductOfferingTotalDTO> lsStockTotalView;
    private List<ProductOfferingTotalDTO> lsProdStockTotalImport;
    private List<ProductOfferTypeDTO> lstProductOfferTypeDTO;
    private Map<String, ProductOfferingTotalDTO> mapValidStockTotalSelect = Maps.newHashMap();
    private List<VShopStaffDTO> lsVshopStaffDTO;
    private List<StockRequestOrderDTO> lsStockOrder;
    private List<StockRequestOrderDetailDTO> lsStockRequestOrderDetail;

    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private StockRequestOrderService stockRequestOrderService;
    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @PostConstruct
    public void init() {
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            initDataSearch();
            initDataCreateRequest();
            lsStockTotalSearch = DataUtil.defaultIfNull(productOfferingService.getLsProOfferTotalDTORequest("", prodOfferTypeId, DataUtil.safeToLong(stateId)), new ArrayList<>());
            lstProductOfferTypeDTO = DataUtil.defaultIfNull(productOfferTypeService.getListProduct(), new ArrayList<>());
            lsVshopStaffDTO = getLsVShopStaffFromOptionSet();
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "common.error.happened");
        }
    }

    /**
     * ham xu chuyen doi cau hinh danh sach tinh sang cua hang
     *
     * @return
     * @throws Exception
     * @author thanhnt77
     */
    private List<VShopStaffDTO> getLsVShopStaffFromOptionSet() {
        List<OptionSetValueDTO> lsProvinceSearch = DataUtil.defaultIfNull(optionSetValueService.getLsOptionSetValueByCode(Const.OPTION_SET.PROVINCE_ORDER), new ArrayList<>());
        List<VShopStaffDTO> lsVshop = Lists.newArrayList();
        for (OptionSetValueDTO optionSetValueDTO : lsProvinceSearch) {
            if (optionSetValueDTO.getName().equals(DataUtil.safeToString(staffDTO.getShopId()))) {
                continue;
            }
            VShopStaffDTO vShop = new VShopStaffDTO();
            vShop.setOwnerType(Const.OWNER_TYPE.SHOP);
            vShop.setOwnerId(optionSetValueDTO.getName());
            vShop.setOwnerCode(optionSetValueDTO.getValue());
            vShop.setOwnerName(optionSetValueDTO.getDescription());
            vShop.setTablePk(optionSetValueDTO.getName());
            lsVshop.add(vShop);
        }
        return lsVshop;
    }

    /**
     * ham tra ve danh sach tinh co ton tai mat hang
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doSearchViewStock() {
        try {
            lsProvince = DataUtil.defaultIfNull(optionSetValueService.getLsProvince(staffDTO.getShopCode(), vShopStaffDTO != null ? vShopStaffDTO.getOwnerCode() : "",
                    prodOfferTypeId, getProdOfferIdSelect(), DataUtil.safeToLong(stateId)), new ArrayList<>());
            if (DataUtil.isNullOrEmpty(lsProvince)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.notfound.province");
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

    private void initDataCreateRequest() {
        lsStockTotalView = Lists.newArrayList();
        mapValidStockTotalSelect = Maps.newHashMap();
        prodOfferTypeId = Const.STOCK_TYPE.STOCK_HANDSET;
        lsProdStockTotalImport = Lists.newArrayList();
        lsProvince = Lists.newArrayList();
        vShopStaffDTO = new VShopStaffDTO();
        stateId = null;
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
    }

    private void initDataSearch() {
        Date sysDate = getSysdateFromDB();
        requestOrderDTOSearch = new StockRequestOrderDTO();
        requestOrderDTOSearch.setOwnerId(staffDTO.getShopId());
        requestOrderDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        requestOrderDTOSearch.setFromDate(sysDate);
        requestOrderDTOSearch.setEndDate(sysDate);
        requestOrderDTOSearch.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST);
        lsStockOrder = Lists.newArrayList();
    }

    /**
     * ham reset dieu kien tim kiem
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetSearchViewStock() {
        initDataCreateRequest();
    }

    /**
     * ham xu ly tim kiem yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doSearchRequestOrder() {
        try {
            if (DataUtil.isNullObject(requestOrderDTOSearch.getFromDate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.from.date.not.blank");
            }
            if (DataUtil.isNullObject(requestOrderDTOSearch.getEndDate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.to.date.not.blank");
            }
            long day = DateUtil.daysBetween2Dates(requestOrderDTOSearch.getEndDate(), requestOrderDTOSearch.getFromDate());
            if (day < 0 || day > 30) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.from.to.valid");
            }
            
            lsStockOrder = DataUtil.defaultIfNull(stockRequestOrderService.findSearhStockOrder(requestOrderDTOSearch.getOrderCode(), requestOrderDTOSearch.getFromDate(), requestOrderDTOSearch.getEndDate(),
                    requestOrderDTOSearch.getStatus(), requestOrderDTOSearch.getOwnerId(), requestOrderDTOSearch.getOwnerType()), new ArrayList<>());
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

    /**
     * ham tim kiem chi tiet
     *
     * @param stockRequestOrderDTO
     * @author thanhnt77
     */
    @Secured("@")
    public void doSeachRequestOrderDetail(StockRequestOrderDTO stockRequestOrderDTO) {
        try {
            viewDetaiRequest = true;
            stockRequestOrderDTOView = DataUtil.cloneBean(stockRequestOrderDTO);
            lsStockRequestOrderDetail = stockRequestOrderDetailService.findSearchStockRequestOrder(
                    stockRequestOrderDTOView.getStockRequestOrderId(), Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    /**
     * ham xu ly hien thi trang thai yeu cau
     *
     * @param status
     * @return
     * @author thanhnt77
     */
    private String getStatusName(String status) {
        String statusName;
        switch (status) {
            case "0":
                statusName = getText("mn.stock.branch.btn.create.request");
                break;
            case "1":
                statusName = getText("import.partner.request.status1");
                break;
            case "2":
                statusName = getText("import.partner.request.status2");
                break;
            case "3":
                statusName = getText("common.cancel");
                break;
            default:
                statusName = "";
        }
        return statusName;
    }

    /**
     * ham xu ly reset trang thai tim kiem yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetSearchRequestOrder() {
        initDataSearch();
    }

    /**
     * ham xu ly hien thi vung lap yeu cau thu hoi
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doChangeCreateRequest() {
        showCreateRequest = !showCreateRequest;
        initDataCreateRequest();
    }

    private Long getProdOfferIdSelect() {
        return productOfferingTotalDTO != null ? productOfferingTotalDTO.getProductOfferingId() : null;
    }

    /**
     * ham tra ve danh sach mat hang trong kho theo shop_code
     *
     * @param shopCode
     * @author thanhnt77
     */
    @Secured("@")
    public void doViewProdByShopCode(String shopCode) {
        try {
            ShopDTO shopDTO = shopService.getShopByShopCode(shopCode);
            if (shopDTO == null || !Const.STATUS.ACTIVE.equals(shopDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "sell.store.no.info", shopCode);
            }
            lsStockTotalView = productOfferingService.getLsRequestProductByShop(Const.OWNER_TYPE.SHOP_LONG, shopDTO.getShopId(), prodOfferTypeId,
                    getProdOfferIdSelect(), DataUtil.safeToLong(stateId));
            if (DataUtil.isNullOrEmpty(lsStockTotalView)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.notfound.product", shopCode);
            }
            lsStockTotalView.stream().forEach(x -> x.setShopDTO(shopDTO));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    /**
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        try {
            lsStockTotalView = Lists.newArrayList();
            productOfferingTotalDTO = null;
            //neu chon mat hang no-serial thi rest kieu nhap hang ve so luong
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * ham tra ve danh sach mat hang theo loai mat hang
     *
     * @param inputProduct
     * @return
     * @author thanhnt77
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            String input = inputProduct != null ? inputProduct.trim() : "";
            lsStockTotalSearch = DataUtil.defaultIfNull(productOfferingService.getLsProOfferTotalDTORequest(input, prodOfferTypeId, DataUtil.safeToLong(stateId)), new ArrayList<>());
            return lsStockTotalSearch;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }

    @Secured("@")
    public void doSelectProdOffer() {

    }

    /**
     * ham xu ly lua chon list danh sach tren dialog
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doSelectStockTotal() {
        try {
            List<ProductOfferingTotalDTO> lsProdStockTotalSelect = lsStockTotalView.stream().filter(x -> DataUtil.safeToLong(x.getRequestQuantityInput()).compareTo(0L) > 0).collect(Collectors.toList());
            if (DataUtil.isNullOrEmpty(lsProdStockTotalSelect)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.over.require.input.empty");
            }
            String key;
            int index = 0;
            for (ProductOfferingTotalDTO offerTotal : lsProdStockTotalSelect) {
                key = "";
                Long result = DataUtil.safeToLong(offerTotal.getAvailableQuantity()) - DataUtil.safeToLong(offerTotal.getRequestQuantity());
                if (DataUtil.safeToLong(offerTotal.getRequestQuantityInput()).compareTo(result) > 0) {
                    executeCommand("focusElementErrorByListClass('txtNumber','" + index + "')");
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.request.over.require.input",
                            DataUtil.safeToString(offerTotal.getCode()) + "-" + DataUtil.safeToString(offerTotal.getName()), result);
                }
                key += offerTotal.getShopDTO() != null ? DataUtil.safeToString(offerTotal.getShopDTO().getShopId()) : "";
                key += "_" + DataUtil.safeToString(offerTotal.getProductOfferingId());
                key += "_" + DataUtil.safeToString(offerTotal.getStateId());

                mapValidStockTotalSelect.put(key, offerTotal);
                index++;
            }
            lsProdStockTotalImport = Lists.newArrayList(mapValidStockTotalSelect.values());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    /**
     * ham xu ly xoa stock_total da nhap
     *
     * @param index
     * @author thanhnt77
     */
    @Secured("@")
    public void doRemoveStockTotal(int index) {
        ProductOfferingTotalDTO offerTotal = lsProdStockTotalImport.get(index);
        String key = "";
        key += offerTotal.getShopDTO() != null ? DataUtil.safeToString(offerTotal.getShopDTO().getShopId()) : "";
        key += "_" + DataUtil.safeToString(offerTotal.getProductOfferingId());
        key += "_" + DataUtil.safeToString(offerTotal.getStateId());
        mapValidStockTotalSelect.remove(key);
        lsProdStockTotalImport = Lists.newArrayList(mapValidStockTotalSelect.values());
    }

    @Secured("@")
    public List<VShopStaffDTO> doSelectShop(String inputShop) {
        try {
            String input = DataUtil.isNullOrEmpty(inputShop) ? "" : inputShop.trim().toUpperCase();
            if (DataUtil.isNullOrEmpty(input)) {
                return lsVshopStaffDTO;
            }
            CharSequence inputData = input;
            return lsVshopStaffDTO.stream().filter(obj -> (DataUtil.safeToString(obj.getOwnerName()).toUpperCase().contains(inputData)
                    || DataUtil.safeToString(obj.getOwnerCode()).toUpperCase().contains(inputData)
            )).collect(Collectors.toList());
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<VShopStaffDTO>();
    }

    /**
     * ham reset mat hang
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetProdOffer() {
        productOfferingTotalDTO = null;
    }

    /**
     * ham xu ly thay doi cua hang
     *
     * @param vShopStaffDTO
     * @author thanhnt77
     */
    @Secured("@")
    public void doChangeShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    /**
     * ham xu ly xoa don vi
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetShop() {
        this.vShopStaffDTO = new VShopStaffDTO();
    }

    /**
     * ham xu ly tao yeu cau dat hang
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doCreateRequest() {
        try {
            if (DataUtil.isNullOrEmpty(lsProdStockTotalImport)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.importData.empty");
            }
            StockRequestOrderDTO requestOrderDTO = new StockRequestOrderDTO();
            requestOrderDTO.createOrderFromImport(lsProdStockTotalImport, staffDTO);
            StockRequestOrderDTO requestOrderDTOSave = stockRequestOrderService.createGoodOrderFromProvince(requestOrderDTO);
            reportSuccess("", "mn.stock.request.title.request.success", requestOrderDTOSave.getOrderCode());
            lsProdStockTotalImport = Lists.newArrayList();
            mapValidStockTotalSelect = Maps.newHashMap();
            topPage();
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
    public void doValidateCreateRequest() {
        try {
            if (DataUtil.isNullOrEmpty(lsProdStockTotalImport)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.importData.empty");
            }
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham validate yeu cau
     * @author thanhnt77
     */
    @Secured("@")
    public void doBackPage() {
        showCreateRequest = false;
        viewDetaiRequest = false;
        doSearchRequestOrder();
    }

    @Secured("@")
    public void doValidateRequestCancel(Long stockRequestOrderIdCancel, String stockRequestOrderIdCancelOrderCode) {
        this.stockRequestOrderIdCancel = stockRequestOrderIdCancel;
        this.stockRequestOrderIdCancelOrderCode = stockRequestOrderIdCancelOrderCode;
    }

    @Secured("@")
    public void doRequestCancel() {
        try {
            StockRequestOrderDTO orderDTO = stockRequestOrderService.findOne(this.stockRequestOrderIdCancel);
            if (orderDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.none", stockRequestOrderIdCancelOrderCode);
            }

            orderDTO.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_3_CANCEL);
            orderDTO.setUpdateUser(staffDTO.getStaffCode());
            stockRequestOrderService.approveStockRequestOrder(orderDTO);
            reportSuccess("", "logistics.cancel.order.success.mgs");
            doSearchRequestOrder();
        } catch (LogicException ex) {
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    public boolean isLoginUserShopVT() {
        return staffDTO != null && Const.L_VT_SHOP_ID.equals(staffDTO.getShopId());
    }

    public boolean isShowProvince() {
        return !DataUtil.isNullOrEmpty(lsProvince);
    }

    //getter and setter
    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLsStockTotalView() {
        return lsStockTotalView;
    }

    public void setLsStockTotalView(List<ProductOfferingTotalDTO> lsStockTotalView) {
        this.lsStockTotalView = lsStockTotalView;
    }

    public List<OptionSetValueDTO> getLsProvince() {
        return lsProvince;
    }

    public void setLsProvince(List<OptionSetValueDTO> lsProvince) {
        this.lsProvince = lsProvince;
    }

    public List<ProductOfferTypeDTO> getLstProductOfferTypeDTO() {
        return lstProductOfferTypeDTO;
    }

    public void setLstProductOfferTypeDTO(List<ProductOfferTypeDTO> lstProductOfferTypeDTO) {
        this.lstProductOfferTypeDTO = lstProductOfferTypeDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProdStockTotalImport() {
        return lsProdStockTotalImport;
    }

    public void setLsProdStockTotalImport(List<ProductOfferingTotalDTO> lsProdStockTotalImport) {
        this.lsProdStockTotalImport = lsProdStockTotalImport;
    }

    public List<ProductOfferingTotalDTO> getLsStockTotalSearch() {
        return lsStockTotalSearch;
    }

    public void setLsStockTotalSearch(List<ProductOfferingTotalDTO> lsStockTotalSearch) {
        this.lsStockTotalSearch = lsStockTotalSearch;
    }

    public List<VShopStaffDTO> getLsVshopStaffDTO() {
        return lsVshopStaffDTO;
    }

    public void setLsVshopStaffDTO(List<VShopStaffDTO> lsVshopStaffDTO) {
        this.lsVshopStaffDTO = lsVshopStaffDTO;
    }

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public boolean isShowCreateRequest() {
        return showCreateRequest;
    }

    public void setShowCreateRequest(boolean showCreateRequest) {
        this.showCreateRequest = showCreateRequest;
    }

    public StockRequestOrderDTO getRequestOrderDTOSearch() {
        return requestOrderDTOSearch;
    }

    public void setRequestOrderDTOSearch(StockRequestOrderDTO requestOrderDTOSearch) {
        this.requestOrderDTOSearch = requestOrderDTOSearch;
    }

    public List<StockRequestOrderDTO> getLsStockOrder() {
        return lsStockOrder;
    }

    public void setLsStockOrder(List<StockRequestOrderDTO> lsStockOrder) {
        this.lsStockOrder = lsStockOrder;
    }

    @Override
    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public List<StockRequestOrderDetailDTO> getLsStockRequestOrderDetail() {
        return lsStockRequestOrderDetail;
    }

    public void setLsStockRequestOrderDetail(List<StockRequestOrderDetailDTO> lsStockRequestOrderDetail) {
        this.lsStockRequestOrderDetail = lsStockRequestOrderDetail;
    }

    public StockRequestOrderDTO getStockRequestOrderDTOView() {
        return stockRequestOrderDTOView;
    }

    public void setStockRequestOrderDTOView(StockRequestOrderDTO stockRequestOrderDTOView) {
        this.stockRequestOrderDTOView = stockRequestOrderDTOView;
    }

    public boolean isViewDetaiRequest() {
        return viewDetaiRequest;
    }

    public void setViewDetaiRequest(boolean viewDetaiRequest) {
        this.viewDetaiRequest = viewDetaiRequest;
    }
}

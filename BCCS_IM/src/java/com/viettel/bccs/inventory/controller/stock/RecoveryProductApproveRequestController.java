package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.StockRequestOrderDetailService;
import com.viettel.bccs.inventory.service.StockRequestOrderService;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
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
public class RecoveryProductApproveRequestController extends InventoryController {

    private int limitAutoComplete;
    private boolean viewDetaiRequest;
    private boolean disableBtnApprove;

    private StaffDTO staffDTO;
    private VShopStaffDTO vShopStaffDTO;
    private StockRequestOrderDTO requestOrderDTOSearch;
    private StockRequestOrderDTO stockRequestOrderDTOView;

    private List<ProductOfferingTotalDTO> lsStockTotalView;
    private Map<String, ProductOfferingTotalDTO> mapValidStockTotalSelect = Maps.newHashMap();
    private List<VShopStaffDTO> lsVshopStaffDTO;
    private List<StockRequestOrderDTO> lsStockOrder;
    private List<StockRequestOrderDetailDTO> lsStockRequestOrderDetail;

    @Autowired
    private StockRequestOrderService stockRequestOrderService;
    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice

    @PostConstruct
    public void init() {
        try {
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            initDataSearch();
            initDataCreateRequest();
            lsVshopStaffDTO = getLsVShopStaffFromOptionSet();
            for (VShopStaffDTO shop : lsVshopStaffDTO) {
                if (DataUtil.safeEqual(staffDTO.getShopId(), shop.getOwnerId())) {
                    vShopStaffDTO = DataUtil.cloneBean(shop);
                    break;
                }
            }
            writeOfficeTag.init(this, staffDTO.getShopId());
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            disableBtnApprove = false;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "common.error.happened");
        }
    }

    /**
     * ham xu chuyen doi cau hinh danh sach tinh sang cua hang
     *
     * @return List<OptionSetValueDTO>
     * @throws Exception
     * @author thanhnt77
     */
    private List<VShopStaffDTO> getLsVShopStaffFromOptionSet() {
        List<OptionSetValueDTO> lsProvinceSearch = DataUtil.defaultIfNull(optionSetValueService.getLsOptionSetValueByCode(Const.OPTION_SET.PROVINCE_ORDER), new ArrayList<>());
        List<VShopStaffDTO> lsVshop = Lists.newArrayList();
        for (OptionSetValueDTO optionSetValueDTO : lsProvinceSearch) {
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

    private void initDataCreateRequest() {
        lsStockTotalView = Lists.newArrayList();
        mapValidStockTotalSelect = Maps.newHashMap();
        vShopStaffDTO = new VShopStaffDTO();
    }

    private void initDataSearch() {
        Date sysDate = getSysdateFromDB();

        Date fromDate = DateUtil.addDate(sysDate, -10);

        requestOrderDTOSearch = new StockRequestOrderDTO();
        requestOrderDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        requestOrderDTOSearch.setOwnerId(staffDTO.getShopId());
        requestOrderDTOSearch.setFromDate(fromDate);
        requestOrderDTOSearch.setEndDate(sysDate);
        requestOrderDTOSearch.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST);
        lsStockOrder = Lists.newArrayList();
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
            this.requestOrderDTOSearch.setOwnerId(this.vShopStaffDTO != null ? DataUtil.safeToLong(this.vShopStaffDTO.getOwnerId()) : null);
            this.requestOrderDTOSearch.setOrderType(Const.OWNER_TYPE.SHOP_LONG);

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
            disableBtnApprove = !Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST.equals(stockRequestOrderDTOView.getStatus());
            lsStockRequestOrderDetail = stockRequestOrderDetailService.findSearchStockRequestOrder(
                    stockRequestOrderDTOView.getStockRequestOrderId(), null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
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
     * Load danh sach mat hang theo loai hang hoa
     */
    @Secured("@")
    public void onChangeOfferType() {
        try {
            lsStockTotalView = Lists.newArrayList();
            //neu chon mat hang no-serial thi rest kieu nhap hang ve so luong
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
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
     * ham xu ly xoa don vi
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetShop() {
        this.vShopStaffDTO = new VShopStaffDTO();
    }

    @Secured("@")
    public void doValidateApproveReJectRequest() {
    }

    public void doChangeQuantity(int index) {
        lsStockRequestOrderDetail.get(index).setEditApproveQuantity(true);
    }


    @Secured("@")
    public void doApproveRequest() {
        try {

            StockRequestOrderDTO orderDTO = stockRequestOrderService.findOne(stockRequestOrderDTOView.getStockRequestOrderId());
            if (orderDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.none", stockRequestOrderDTOView.getOrderCode());
            }
            if (!Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST.equals(orderDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.invalid.order.approve", stockRequestOrderDTOView.getOrderCode());
            }
            SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            orderDTO.setSignOfficeDTO(signOfficeDTO);

            orderDTO.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_1_APPROVE);
            orderDTO.setUpdateUser(staffDTO.getStaffCode());
            orderDTO.setActionStaffId(staffDTO.getStaffId());

            int countApprove = 0;
            for (StockRequestOrderDetailDTO orderDetailDTO : lsStockRequestOrderDetail) {
                if (DataUtil.safeToLong(orderDetailDTO.getApprovedQuantity()).equals(0L)) {
                    countApprove++;
                }
                if (DataUtil.safeToLong(orderDetailDTO.getApprovedQuantity()).compareTo(DataUtil.safeToLong(orderDetailDTO.getRequestQuantity())) > 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.invalid.requestquantity.approvequantity", orderDetailDTO.getProdOfferName());
                }
            }
            if (countApprove == lsStockRequestOrderDetail.size()) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.invalid.min.quantity.product");
            }
            orderDTO.setLsRequestOrderDetailDTO(this.lsStockRequestOrderDetail);
            stockRequestOrderService.approveStockRequestOrder(orderDTO);
            disableBtnApprove = true;
            topReportSuccess("", "import.partner.confirm.success", "limit.stock.approve");
            lsStockRequestOrderDetail.stream().forEach(x->x.setEditApproveQuantity(false));
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
    public void doRejectRequest() {
        try {
            StockRequestOrderDTO orderDTO = stockRequestOrderService.findOne(stockRequestOrderDTOView.getStockRequestOrderId());
            if (orderDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.none", stockRequestOrderDTOView.getOrderCode());
            }
            if (!Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST.equals(orderDTO.getStatus())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.invalid.order.reject", stockRequestOrderDTOView.getOrderCode());
            }
            orderDTO.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_2_REJECT);
            orderDTO.setUpdateUser(staffDTO.getStaffCode());
            stockRequestOrderService.approveStockRequestOrder(orderDTO);
            disableBtnApprove = true;
            topReportSuccess("", "import.partner.confirm.success", "import.partner.request.reject");
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
        viewDetaiRequest = false;
        doSearchRequestOrder();
    }

    public boolean isLoginUserShopVT() {
        return staffDTO != null && Const.L_VT_SHOP_ID.equals(staffDTO.getShopId());
    }

    //getter and setter

    public List<StockRequestOrderDetailDTO> getLsStockRequestOrderDetail() {
        return lsStockRequestOrderDetail;
    }

    public void setLsStockRequestOrderDetail(List<StockRequestOrderDetailDTO> lsStockRequestOrderDetail) {
        this.lsStockRequestOrderDetail = lsStockRequestOrderDetail;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public boolean isViewDetaiRequest() {
        return viewDetaiRequest;
    }

    public void setViewDetaiRequest(boolean viewDetaiRequest) {
        this.viewDetaiRequest = viewDetaiRequest;
    }

    @Override
    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public void setStaffDTO(StaffDTO staffDTO) {
        this.staffDTO = staffDTO;
    }

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    public StockRequestOrderDTO getRequestOrderDTOSearch() {
        return requestOrderDTOSearch;
    }

    public void setRequestOrderDTOSearch(StockRequestOrderDTO requestOrderDTOSearch) {
        this.requestOrderDTOSearch = requestOrderDTOSearch;
    }

    public StockRequestOrderDTO getStockRequestOrderDTOView() {
        return stockRequestOrderDTOView;
    }

    public void setStockRequestOrderDTOView(StockRequestOrderDTO stockRequestOrderDTOView) {
        this.stockRequestOrderDTOView = stockRequestOrderDTOView;
    }

    public List<ProductOfferingTotalDTO> getLsStockTotalView() {
        return lsStockTotalView;
    }

    public void setLsStockTotalView(List<ProductOfferingTotalDTO> lsStockTotalView) {
        this.lsStockTotalView = lsStockTotalView;
    }

    public Map<String, ProductOfferingTotalDTO> getMapValidStockTotalSelect() {
        return mapValidStockTotalSelect;
    }

    public void setMapValidStockTotalSelect(Map<String, ProductOfferingTotalDTO> mapValidStockTotalSelect) {
        this.mapValidStockTotalSelect = mapValidStockTotalSelect;
    }

    public List<VShopStaffDTO> getLsVshopStaffDTO() {
        return lsVshopStaffDTO;
    }

    public void setLsVshopStaffDTO(List<VShopStaffDTO> lsVshopStaffDTO) {
        this.lsVshopStaffDTO = lsVshopStaffDTO;
    }

    public List<StockRequestOrderDTO> getLsStockOrder() {
        return lsStockOrder;
    }

    public void setLsStockOrder(List<StockRequestOrderDTO> lsStockOrder) {
        this.lsStockOrder = lsStockOrder;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public boolean isDisableBtnApprove() {
        return disableBtnApprove;
    }

    public void setDisableBtnApprove(boolean disableBtnApprove) {
        this.disableBtnApprove = disableBtnApprove;
    }
}

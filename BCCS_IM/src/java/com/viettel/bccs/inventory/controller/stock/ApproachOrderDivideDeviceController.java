package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vanho on 26/03/2017.
 */
@Component
@Scope("view")
@ManagedBean
public class ApproachOrderDivideDeviceController extends TransCommonController {

    private boolean showSearch = true;
    private boolean disableChangeDivideDevice;
    private boolean viewDetailDivideDevice;
    private StockRequestDTO stockRequestDTOSearch;
    private StockRequestDTO stockRequestDTOSelect;
    private StockRequestDTO stockRequestDTOViewDetail;
    private List<StockRequestDTO> lsRequestSearch;
    private String shopPathSearch;
    private String currentRequestCode;
    private int limitAutoComplete;
    private List<StockTransFullDTO> lsStockTransFull;
    private StaffDTO staffDTO;
    private ConfigListProductTagDTO configListProductTagDTO;
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();
    private boolean isEnableApproveAndRejectBtn;
    @Autowired
    private DeviceConfigService deviceConfigService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private ShopInfoNameable shopInfoTagSearch;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTagSearch;
    @Autowired
    private StockRequestService stockRequestService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private ApproachOrderDivideDeviceService approachOrderDivideDeviceService;
    @Autowired
    private StockDeviceTransferService stockDeviceTransferService;

    @PostConstruct
    public void init() {
        try {
            initData();
            shopPathSearch = shopService.getShopByShopId(staffDTO.getShopId()).getShopPath();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void receiveSearch(VShopStaffDTO vShopStaffDTO) {
        stockRequestDTOSearch.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockRequestDTOSearch.setOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
        //shopPathSearch = DataUtil.safeToString(vShopStaffDTO.getShopPath());
    }

    @Secured("@")
    public void clearSearchShop() {
        stockRequestDTOSearch.setOwnerId(null);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
        //shopPathSearch = "";
    }

    @Secured("@")
    public void doSearch() {
        try {
            if (stockRequestDTOSearch.getFromDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (stockRequestDTOSearch.getToDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(stockRequestDTOSearch.getFromDate(), stockRequestDTOSearch.getToDate()) > 0)
                    || DateUtil.daysBetween2Dates(stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getFromDate()) > 30L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "view.stock.offer.cycel.fromDate.endDate", 30);
            }
            lsRequestSearch = approachOrderDivideDeviceService.getListOrderDivideDevicePendingApproach(shopPathSearch, stockRequestDTOSearch.getFromDate(), stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getOwnerId(), stockRequestDTOSearch.getRequestCode(), stockRequestDTOSearch.getStatus());
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

    public void approachRequest(){
        try {
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setRequestCode(currentRequestCode);
            stockRequestDTOSelect.setUpdateUser(staffDTO.getStaffCode());
            approachOrderDivideDeviceService.approachOrderDivide(stockRequestDTOSelect);
            reportSuccess("", "approach.request.divide.device.success");
            isEnableApproveAndRejectBtn = false;
            doSearch();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg(), ex.getParamsMsg());
            topPage();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
            topPage();
        }
    }

    public void rejectRequest(){
        try {
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setRequestCode(currentRequestCode);
            stockRequestDTOSelect.setUpdateUser(staffDTO.getStaffCode());
            approachOrderDivideDeviceService.rejectOrderDivide(stockRequestDTOSelect, lsStockTransFull, staffDTO.getStaffId());
            reportSuccess("", "reject.request.divide.device.success");
            isEnableApproveAndRejectBtn = false;
            doSearch();
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

    private void initData() throws LogicException, Exception {
        staffDTO = BccsLoginSuccessHandler.getStaffDTO();
        limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
        Date currentDate = optionSetValueService.getSysdateFromDB(true);
        stockRequestDTOSearch = new StockRequestDTO();
        stockRequestDTOSearch.setFromDate(currentDate);
        stockRequestDTOSearch.setToDate(currentDate);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);

        shopInfoTagSearch.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        staffInfoTagSearch.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

        shopInfoTag.initShopAndAllChild(this, DataUtil.safeToString(staffDTO.getShopId()), true, Const.OWNER_TYPE.SHOP);
        staffInfoTag.initStaff(this, DataUtil.safeToString(staffDTO.getShopId()));

        configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
        List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
        List<OptionSetValueDTO> lsProductStatus2 = lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.NEW.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList());
        configListProductTagDTO.setLsProductStatus(lsProductStatus2);
        configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DIVIDE);
        listProductTag.init(this, configListProductTagDTO);
        writeOfficeTag.init(this, staffDTO.getShopId());
        lsRequestSearch = Lists.newArrayList();
        disableChangeDivideDevice = false;
    }

    @Secured("@")
    public void doResetSearch() {
        try {
            initData();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    @Secured("@")
    public void doValidRequest() {

    }


    @Secured("@")
    public void changeOwnerTypeInput() {
        try {
            shopInfoTag.resetShop();
            staffInfoTag.resetProduct();
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOSelect.getOwnerType())) {
                shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
                stockRequestDTOSelect.setOwnerId(staffDTO.getShopId());
                stockRequestDTOSelect.setOwnerCode(staffDTO.getShopCode());
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
                List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
                configListProductTagDTO.setLsProductStatus(lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.NEW.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList()));
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DIVIDE);
                listProductTag.init(this, configListProductTagDTO);
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOSelect.getOwnerType())) {
                VShopStaffDTO vShopStaffDTO = staffService.findStaffById(DataUtil.safeToString(staffDTO.getStaffId()));
                staffInfoTag.loadStaff(vShopStaffDTO);
                stockRequestDTOSelect.setOwnerId(staffDTO.getStaffId());
                stockRequestDTOSelect.setOwnerCode(staffDTO.getStaffCode());
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getStaffId(), Const.OWNER_TYPE.STAFF);
                List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
                configListProductTagDTO.setLsProductStatus(lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.NEW.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList()));
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DIVIDE);
                listProductTag.init(this, configListProductTagDTO);
            }
            updateElemetId("frmChangeDOA:listProduct:listProductpnListProduct");
            updateElemetId("frmChangeDOA:msgSearch");
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
    public void doResetRequest() {
        try {
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            writeOfficeTag.resetOffice();
            changeOwnerTypeInput();
            updateElemetId("frmChangeDOA:pnlOffice");
            disableChangeDivideDevice = false;
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
     * ham xem chi tiet yeu cau
     *
     * @author thanhnt77
     */
    public void doShowInfoOrderDetail(StockRequestDTO stockRequestDTO) {
        try {
            isEnableApproveAndRejectBtn = "0".equals(stockRequestDTO.getStatus());
            Long stockTransId = stockRequestDTO.getStockTransId();
            StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE));
            if (stockTransActionDTO != null) {
                lsStockTransFull = DataUtil.defaultIfNull(stockTransService.searchStockTransDetail(Lists.newArrayList(stockTransActionDTO.getStockTransActionId())), new ArrayList<>());
                for (StockTransFullDTO stockTransFullDTO : lsStockTransFull) {
                    if ("1".equals(stockTransFullDTO.getCheckSerial())) {
                        stockTransFullDTO.setLstSerial(DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransFullDTO.getStockTransDetailId()), new ArrayList<>()));
                    }
                }
            }
            viewDetailDivideDevice = true;
            stockRequestDTOViewDetail = DataUtil.cloneBean(stockRequestDTO);
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOViewDetail.getOwnerType())) {
                ShopDTO shopDTO = shopService.findOne(stockRequestDTOViewDetail.getOwnerId());
                if (shopDTO != null) {
                    this.stockRequestDTOViewDetail.setOwnerCode(shopDTO.getShopCode());
                    this.stockRequestDTOViewDetail.setOwnerName(shopDTO.getName());
                }
            }
            currentRequestCode = stockRequestDTO.getRequestCode();
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
     * ham xem serial
     *
     * @param stockTransDetailId
     * @author thanhnt77
     */
    public void doShowViewSerial(Long stockTransDetailId) {
        try {
            lsSerial = DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransDetailId), new ArrayList<>());
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
    public void resetRequestCode() {
        try {
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
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

    public boolean isDisableChangeDivideDevice() {
        return disableChangeDivideDevice;
    }

    public void setDisableChangeDivideDevice(boolean disabldoSeChangeDivideDevice) {
        this.disableChangeDivideDevice = disableChangeDivideDevice;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public ConfigListProductTagDTO getConfigListProductTagDTO() {
        return configListProductTagDTO;
    }

    public void setConfigListProductTagDTO(ConfigListProductTagDTO configListProductTagDTO) {
        this.configListProductTagDTO = configListProductTagDTO;
    }

    public StaffInfoNameable getStaffInfoTagSearch() {
        return staffInfoTagSearch;
    }

    public void setStaffInfoTagSearch(StaffInfoNameable staffInfoTagSearch) {
        this.staffInfoTagSearch = staffInfoTagSearch;
    }

    public ShopInfoNameable getShopInfoTagSearch() {
        return shopInfoTagSearch;
    }

    public void setShopInfoTagSearch(ShopInfoNameable shopInfoTagSearch) {
        this.shopInfoTagSearch = shopInfoTagSearch;
    }

    public List<StockRequestDTO> getLsRequestSearch() {
        return lsRequestSearch;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public void setLsRequestSearch(List<StockRequestDTO> lsRequestSearch) {
        this.lsRequestSearch = lsRequestSearch;
    }

    public StockRequestDTO getStockRequestDTOSearch() {
        return stockRequestDTOSearch;
    }

    public void setStockRequestDTOSearch(StockRequestDTO stockRequestDTOSearch) {
        this.stockRequestDTOSearch = stockRequestDTOSearch;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public boolean isViewDetailDivideDevice() {
        return viewDetailDivideDevice;
    }

    public void setViewDetailDivideDevice(boolean viewDetailDivideDevice) {
        this.viewDetailDivideDevice = viewDetailDivideDevice;
    }

    public StockRequestDTO getStockRequestDTOSelect() {
        return stockRequestDTOSelect;
    }

    public void setStockRequestDTOSelect(StockRequestDTO stockRequestDTOSelect) {
        this.stockRequestDTOSelect = stockRequestDTOSelect;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public StockRequestDTO getStockRequestDTOViewDetail() {
        return stockRequestDTOViewDetail;
    }

    public void setStockRequestDTOViewDetail(StockRequestDTO stockRequestDTOViewDetail) {
        this.stockRequestDTOViewDetail = stockRequestDTOViewDetail;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    @Secured("@")
    public void doBackPage() {
        showSearch = true;
        viewDetailDivideDevice = false;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
    }

    public boolean isEnableApproveAndRejectBtn() {
        return isEnableApproveAndRejectBtn;
    }

    public void setEnableApproveAndRejectBtn(boolean enableApproveAndRejectBtn) {
        isEnableApproveAndRejectBtn = enableApproveAndRejectBtn;
    }

    private List<DeviceConfigDetailDTO> listAddDeviceConfigDetail;

    public List<DeviceConfigDetailDTO> getListAddDeviceConfigDetail() {
        return listAddDeviceConfigDetail;
    }

    public void setListAddDeviceConfigDetail(List<DeviceConfigDetailDTO> listAddDeviceConfigDetail) {
        this.listAddDeviceConfigDetail = listAddDeviceConfigDetail;
    }

    public void preToShowDeviceConfig(Long probOfferId, Long stateId){
        listAddDeviceConfigDetail = new ArrayList<>();
        try {
            List<DeviceConfigDTO> lstDeviceConfigUpdate = DataUtil.defaultIfNull(deviceConfigService.getDeviceConfigByProductAndState(probOfferId, stateId), new ArrayList<>());
            for (DeviceConfigDTO deviceConfigDTO1 : lstDeviceConfigUpdate) {
                DeviceConfigDetailDTO deviceConfigDetailDTO = new DeviceConfigDetailDTO();
                deviceConfigDetailDTO.setDeviceConfigDTO(new DeviceConfigDTO());

                BeanUtils.copyProperties(deviceConfigDetailDTO.getDeviceConfigDTO(), deviceConfigDTO1);
                deviceConfigDetailDTO.setStatus(stockDeviceTransferService.getDeviceConfigStateStrBy(deviceConfigDTO1.getProdOfferId(), Short.valueOf(deviceConfigDTO1.getStateId().toString()), deviceConfigDTO1.getNewProdOfferId()));
                try {
                    ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(deviceConfigDTO1.getNewProdOfferId());
                    deviceConfigDetailDTO.setProductOfferingTotalDTO(deviceConfigService.getLsProductOfferingByProductTypeAndState(productOfferingDTO.getCode(), 11L, null).get(0));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new LogicException("105", "balance.valid.prodInfo");
                }
                listAddDeviceConfigDetail.add(deviceConfigDetailDTO);
            }
        } catch (LogicException ex){
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            reportError("", "", "common.error.happened");
            return;
        }
    }
}

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
 * controller chuc nang chuyen doi hang DOA
 *
 * @author thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class CreateOrderChangeDOAController extends TransCommonController {

    private boolean showSearch = true;
    private int limitAutoComplete;
    private boolean disableChangeDOA;
    private boolean viewDetailDOA;

    private StaffDTO staffDTO;
    private StockRequestDTO stockRequestDTOSearch;
    private StockRequestDTO stockRequestDTOSelect;
    private StockRequestDTO stockRequestDTOViewDetail;
    private ConfigListProductTagDTO configListProductTagDTO;

    private List<StockRequestDTO> lsRequestSearch;
    private List<StockTransFullDTO> lsStockTransFull;
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();


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
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
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
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private StockTransService stockTransService;

    @PostConstruct
    public void init() {
        try {
            initData();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * author thanhnt77
     *
     * @throws LogicException
     * @throws Exception
     */
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
        configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DOA);
        listProductTag.init(this, configListProductTagDTO);
        writeOfficeTag.init(this, staffDTO.getShopId());
        lsRequestSearch = Lists.newArrayList();
        disableChangeDOA = false;
    }

    /**
     * ham xu ly tim kiem DOA
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doSearchChangeDOA() {
        try {
            if (stockRequestDTOSearch.getOwnerType() == null) {
                throw new LogicException("", "stock.type.not.null");
            }
            if (!(Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOSearch.getOwnerType()) || Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOSearch.getOwnerType()))) {
                throw new LogicException("", "stock.type.not.invalid");
            }
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

            lsRequestSearch = stockRequestService.getLsSearchStockRequest(stockRequestDTOSearch.getRequestCode(), stockRequestDTOSearch.getFromDate(),
                    stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getStatus(), staffDTO.getShopId(), stockRequestDTOSearch.getOwnerId(), stockRequestDTOSearch.getOwnerType());
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

    /**
     * ham xu ly reset thong tin tim kiem
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetSearch() {
        try {
            initData();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happen");
        }
    }

    /**
     * xu ly lap yeu cau, hien thi vung thong tin lap yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doCreateRequest() {
        try {
            showSearch = false;
            shopInfoTag.loadShop(DataUtil.safeToString(staffDTO.getShopId()), true);
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            stockRequestDTOSelect.setOwnerId(staffDTO.getShopId());

            stockRequestDTOSelect.setOwnerCode(staffDTO.getShopCode());

            configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
            List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
            configListProductTagDTO.setLsProductStatus(lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.NEW.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList()));
            configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DOA);
            listProductTag.init(this, configListProductTagDTO);
            disableChangeDOA = false;


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

    /**
     * ham validate yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doValidRequest() {

    }

    /**
     * ham chuyen doi yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doChangeRequest() {
        try {
            //validate vOffice
            SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            stockRequestDTOSelect.setSignOfficeDTO(signOfficeDTO);

            //lay thong tin mat hang
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            stockRequestDTOSelect.setStockTransDetailDTOs(stockTransDetailDTOs);

            stockRequestDTOSelect.setCreateUser(staffDTO.getStaffCode());
            stockRequestDTOSelect.setUpdateUser(staffDTO.getStaffCode());
            stockRequestDTOSelect.setActionStaffId(staffDTO.getStaffId());

            //thuc hien tao yeu cau
            stockRequestService.excuteRequestTrans(stockRequestDTOSelect);

            reportSuccess("", "stockOrderAgent.add.sussces");
            disableChangeDOA = true;
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


    /**
     * ham validate yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doResetRequest() {
        try {
            stockRequestDTOSelect = new StockRequestDTO();
            stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockRequestDTOSelect.setRequestCode(stockRequestService.getRequest());
            writeOfficeTag.resetOffice();
            changeOwnerTypeInput();
            updateElemetId("frmChangeDOA:pnlOffice");
            disableChangeDOA = false;
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
            viewDetailDOA = true;
            this.stockRequestDTOViewDetail = DataUtil.cloneBean(stockRequestDTO);
            if (Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOViewDetail.getOwnerType())) {
                ShopDTO shopDTO = shopService.findOne(stockRequestDTOViewDetail.getOwnerId());
                if (shopDTO != null) {
                    this.stockRequestDTOViewDetail.setOwnerCode(shopDTO.getShopCode());
                    this.stockRequestDTOViewDetail.setOwnerName(shopDTO.getName());
                }
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOViewDetail.getOwnerType())) {
                StaffDTO staffDTOTmp = staffService.findOne(stockRequestDTOViewDetail.getOwnerId());
                if (staffDTOTmp != null) {
                    this.stockRequestDTOViewDetail.setOwnerCode(staffDTOTmp.getStaffCode());
                    this.stockRequestDTOViewDetail.setOwnerName(staffDTOTmp.getName());
                }
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

    /**
     * ham validate yeu cau
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void doBackPage() {
        showSearch = true;
        viewDetailDOA = false;
    }

    /**
     * ham nhan du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void receiveSearch(VShopStaffDTO vShopStaffDTO) {
        stockRequestDTOSearch.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockRequestDTOSearch.setOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
    }

    /**
     * ham clear du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void clearSearchShop() {
        stockRequestDTOSearch.setOwnerId(null);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
    }

    /**
     * ham clear du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void clearSearchStaff() {
        stockRequestDTOSearch.setOwnerId(null);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
    }

    /**
     * ham nhan du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void receiveInput(VShopStaffDTO vShopStaffDTO) {
        stockRequestDTOSelect.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockRequestDTOSelect.setOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
        if (Const.OWNER_TYPE.STAFF.equals(vShopStaffDTO.getOwnerType())) {
            configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, DataUtil.safeToLong(vShopStaffDTO.getOwnerId()), Const.OWNER_TYPE.STAFF);
            configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DOA);
            listProductTag.init(this, configListProductTagDTO);
        }
        stockRequestDTOSelect.setOwnerCode(vShopStaffDTO.getOwnerCode());
        stockRequestDTOSelect.setOwnerName(vShopStaffDTO.getOwnerName());
        updateElemetId("frmChangeDOA:listProduct:listProductpnListProduct");
        updateElemetId("frmChangeDOA:msgSearch");
    }

    /**
     * ham clear du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void clearInputShop() {
        stockRequestDTOSelect.setOwnerId(null);
        stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
    }

    /**
     * ham clear du lieu tu tag shop
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void clearInputStaff() {
        stockRequestDTOSelect.setOwnerId(null);
        stockRequestDTOSelect.setOwnerType(Const.OWNER_TYPE.STAFF_LONG);
        listProductTag.setLsListProductOfferDTO(Lists.newArrayList());
        updateElemetId("frmChangeDOA:listProduct:listProductpnListProduct");
        updateElemetId("frmChangeDOA:msgSearch");
    }


    /**
     * thay doi loai kho xuat o vung tim kiem
     *
     * @author thanhnt77
     */
    @Secured("@")
    public void changeOwnerTypeSearch() {
        shopInfoTagSearch.resetShop();
        staffInfoTagSearch.resetProduct();
    }

    /**
     * thay doi loai kho xuat
     *
     * @author thanhnt77
     */
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
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DOA);
                listProductTag.init(this, configListProductTagDTO);
            } else if (Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOSelect.getOwnerType())) {
                VShopStaffDTO vShopStaffDTO = staffService.findStaffById(DataUtil.safeToString(staffDTO.getStaffId()));
                staffInfoTag.loadStaff(vShopStaffDTO);
                stockRequestDTOSelect.setOwnerId(staffDTO.getStaffId());
                stockRequestDTOSelect.setOwnerCode(staffDTO.getStaffCode());
                configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getStaffId(), Const.OWNER_TYPE.STAFF);
                List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
                configListProductTagDTO.setLsProductStatus(lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.NEW.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList()));
                configListProductTagDTO.setType(Const.CONFIG_PRODUCT.TYPE_DOA);
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

    //getter and setter
    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
    }

    public StockRequestDTO getStockRequestDTOSearch() {
        return stockRequestDTOSearch;
    }

    public void setStockRequestDTOSearch(StockRequestDTO stockRequestDTOSearch) {
        this.stockRequestDTOSearch = stockRequestDTOSearch;
    }

    public ConfigListProductTagDTO getConfigListProductTagDTO() {
        return configListProductTagDTO;
    }

    public void setConfigListProductTagDTO(ConfigListProductTagDTO configListProductTagDTO) {
        this.configListProductTagDTO = configListProductTagDTO;
    }

    public List<StockRequestDTO> getLsRequestSearch() {
        return lsRequestSearch;
    }

    public void setLsRequestSearch(List<StockRequestDTO> lsRequestSearch) {
        this.lsRequestSearch = lsRequestSearch;
    }

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
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

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public ShopInfoNameable getShopInfoTagSearch() {
        return shopInfoTagSearch;
    }

    public void setShopInfoTagSearch(ShopInfoNameable shopInfoTagSearch) {
        this.shopInfoTagSearch = shopInfoTagSearch;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public StaffInfoNameable getStaffInfoTagSearch() {
        return staffInfoTagSearch;
    }

    public void setStaffInfoTagSearch(StaffInfoNameable staffInfoTagSearch) {
        this.staffInfoTagSearch = staffInfoTagSearch;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {
        this.writeOfficeTag = writeOfficeTag;
    }

    public boolean isDisableChangeDOA() {
        return disableChangeDOA;
    }

    public void setDisableChangeDOA(boolean disableChangeDOA) {
        this.disableChangeDOA = disableChangeDOA;
    }

    public List<StockTransFullDTO> getLsStockTransFull() {
        return lsStockTransFull;
    }

    public void setLsStockTransFull(List<StockTransFullDTO> lsStockTransFull) {
        this.lsStockTransFull = lsStockTransFull;
    }

    public boolean isViewDetailDOA() {
        return viewDetailDOA;
    }

    public void setViewDetailDOA(boolean viewDetailDOA) {
        this.viewDetailDOA = viewDetailDOA;
    }

    public StockRequestDTO getStockRequestDTOViewDetail() {
        return stockRequestDTOViewDetail;
    }

    public void setStockRequestDTOViewDetail(StockRequestDTO stockRequestDTOViewDetail) {
        this.stockRequestDTOViewDetail = stockRequestDTOViewDetail;
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
    }
}


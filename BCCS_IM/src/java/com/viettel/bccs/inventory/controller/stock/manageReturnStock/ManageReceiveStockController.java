package com.viettel.bccs.inventory.controller.stock.manageReturnStock;

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
import com.viettel.fw.dto.BaseMessage;
import org.primefaces.model.StreamedContent;
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
 * Created by DungHA7 on 22/09/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ManageReceiveStockController extends TransCommonController {

    private boolean showSearch = true;
    private int limitAutoComplete;
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
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private ShopInfoNameable shopInfoTagSearch;

    @Autowired
    private StockRequestService stockRequestService;
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
        doSearchRequest();
    }

    /**
     * author thanhnt77
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
        stockRequestDTOSearch.setStatus("0");//lap yeu cau

        shopInfoTagSearch.initShopByListShopCode(this, Const.MODE_SHOP.LIST_SHOP_KV, null);

        configListProductTagDTO = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, BccsLoginSuccessHandler.getStaffDTO().getShopId(), Const.OWNER_TYPE.SHOP);
        List<OptionSetValueDTO> lsProductStatus = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.PRODUCT_STATUS), new ArrayList<>());
        List<OptionSetValueDTO> lsProductStatus2 = lsProductStatus.stream().filter(obj -> Const.GOODS_STATE.NEW.equals(DataUtil.safeToLong(obj.getValue()))).collect(Collectors.toList());
        configListProductTagDTO.setLsProductStatus(lsProductStatus2);
        listProductTag.init(this, configListProductTagDTO);
        lsRequestSearch = Lists.newArrayList();
    }

    /**
     * ham xu ly tim kiem DOA
     * @author thanhnt77
     */
    @Secured("@")
    public void doSearchRequest() {
        try {
            if (stockRequestDTOSearch.getOwnerType() == null) {
                throw new LogicException("","stock.type.not.null");
            }
            if (!(Const.OWNER_TYPE.SHOP_LONG.equals(stockRequestDTOSearch.getOwnerType()) || Const.OWNER_TYPE.STAFF_LONG.equals(stockRequestDTOSearch.getOwnerType()))) {
                throw new LogicException("","stock.type.not.invalid");
            }
            if (stockRequestDTOSearch.getFromDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
            }
            if (stockRequestDTOSearch.getToDate() == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
            }
            if ((DateUtil.compareDateToDate(stockRequestDTOSearch.getFromDate(), stockRequestDTOSearch.getToDate()) > 0)
                    || DateUtil.daysBetween2Dates(stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getFromDate()) > 30L) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT,  "view.stock.offer.cycel.fromDate.endDate", 30);
            }

            lsRequestSearch = stockRequestService.getLsSearchStockRequestTTBH(stockRequestDTOSearch.getRequestCode(), stockRequestDTOSearch.getFromDate(),
                    stockRequestDTOSearch.getToDate(), stockRequestDTOSearch.getStatus(), stockRequestDTOSearch.getOwnerId());
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
     * ham chuyen doi yeu cau
     * @author thanhnt77
     */
    @Secured("@")
    public void doAcceptRequest() {
        try {
            //thuc hien chap nhan request
            stockRequestService.excuteImportTTBGRequestTrans(stockRequestDTOViewDetail.getStockRequestId(), Const.STOCK_REQUEST_DOA.APPROVE, staffDTO);
            reportSuccess("", "mn.receive.stock.create.aprove.success");
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
     * ham chuyen doi yeu cau
     * @author thanhnt77
     */
    @Secured("@")
    public void doRejectRequest() {
        try {
            //thuc hien chap nhan request
            stockRequestService.excuteImportTTBGRequestTrans(stockRequestDTOViewDetail.getStockRequestId(), Const.STOCK_REQUEST_DOA.CANCEL, staffDTO);
            reportSuccess("", "mn.receive.stock.create.reject.success");
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
     * ham validate yeu cau
     * @author thanhnt77
     */
    @Secured("@")
    public void doValidRequest() {

    }

    /**
     * ham xem chi tiet yeu cau
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
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    /**
     * ham xem serial
     * @author thanhnt77
     * @param stockTransDetailId
     */
    public void doShowViewSerial(Long stockTransDetailId) {
        try {
            lsSerial = DataUtil.defaultIfNull(stockTransSerialService.findByStockTransDetailId(stockTransDetailId), new ArrayList<>());
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex);
            reportError("", "", "common.error.happened");
            topPage();
        }
    }

    /**
     * ham validate yeu cau
     * @author thanhnt77
     */
    @Secured("@")
    public void doBackPage() {
        showSearch = true;
        viewDetailDOA = false;
        doSearchRequest();
    }

    /**
     * ham nhan du lieu tu tag shop
     * @author thanhnt77
     */
    @Secured("@")
    public void receiveSearch(VShopStaffDTO vShopStaffDTO) {
        stockRequestDTOSearch.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        stockRequestDTOSearch.setOwnerType(DataUtil.safeToLong(vShopStaffDTO.getOwnerType()));
    }

    /**
     * ham clear du lieu tu tag shop
     * @author thanhnt77
     */
    @Secured("@")
    public void clearSearchShop() {
        stockRequestDTOSearch.setOwnerId(null);
        stockRequestDTOSearch.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
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

    public ShopInfoNameable getShopInfoTagSearch() {
        return shopInfoTagSearch;
    }

    public void setShopInfoTagSearch(ShopInfoNameable shopInfoTagSearch) {
        this.shopInfoTagSearch = shopInfoTagSearch;
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

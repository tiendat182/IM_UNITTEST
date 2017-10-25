package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.controller.BaseController;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.*;

/**
 * Created by hoangnt14 on 3/4/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "processStockController")
public class ProcessStockController extends BaseController {
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private ProductOfferTypeService productOfferTypeService;
    @Autowired
    private StockTransSerialService stockTransSerialService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private StockTransService stockTransService;
    @Autowired
    private StockTotalService stockTotalService;

    private int limitAutoComplete;
    private List<OptionSetValueDTO> listState = Lists.newArrayList();
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    private List<String> lstChanelTypeId = Lists.newArrayList();
    private VShopStaffDTO vStaffDTO = null;
    private VShopStaffDTO vShopDTO = null;
    private String stateId;
    private TreeNode rootStock;
    private boolean showDetail;
    private WareHouseInfoDTO wareHouseInfoDTO;
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();
    private Long quantity;
    private boolean balance;
    private ActionLogDTO actionLogDTO;


    @PostConstruct
    public void init() {
        try {
            shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            vShopDTO = shopInfoTag.getvShopStaffDTO();
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_STAFF);
            lstChanelTypeId.add(Const.CHANNEL_TYPE_ID.CHANNEL_TYPE_INSURRANCE);
            staffInfoTag.initStaffWithChanelTypesAndParrentShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), null, lstChanelTypeId, false);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            listState = optionSetValueService.getByOptionSetCode(Const.STOCK_INSPECT.STATE);
            lsProductOfferingTotalDTO = DataUtil.defaultIfNull(productOfferingService.getLsProductOfferingDTO("", getListProductOfferTypeID(),
                    null, null, null, null), new ArrayList<ProductOfferingTotalDTO>());
            rootStock = new DefaultTreeNode(new WareHouseInfoDTO(), null);
            showDetail = false;
            balance = false;
            doViewStock();
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    private List<Long> getListProductOfferTypeID() {
        try {
            List<Long> lstId = Lists.newArrayList();
            List<ProductOfferTypeDTO> lstProductOfferTypeDTOs = productOfferTypeService.getListProductOfferType();
            if (!DataUtil.isNullOrEmpty(lstProductOfferTypeDTOs)) {
                for (ProductOfferTypeDTO productOfferTypeDTO : lstProductOfferTypeDTOs) {
                    lstId.add(productOfferTypeDTO.getProductOfferTypeId());
                }
            }
            return lstId;
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
        return null;
    }

    @Secured("@")
    public void doViewStock() {
        //
        try {
            Long ownerId = null;
            Long ownerType = null;
            String shopName = "";
            String shopCode = "";
            Long productOfferId = null;
            if (!DataUtil.isNullObject(vShopDTO)) {
                ownerId = DataUtil.safeToLong(vShopDTO.getOwnerId());
                ownerType = Const.OWNER_TYPE.SHOP_LONG;
                shopName = vShopDTO.getOwnerName();
                shopCode = vShopDTO.getOwnerCode();
                if (!DataUtil.isNullObject(vStaffDTO)) {
                    ownerId = DataUtil.safeToLong(vStaffDTO.getOwnerId());
                    ownerType = Const.OWNER_TYPE.STAFF_LONG;
                    shopName = vStaffDTO.getOwnerName();
                    shopCode = vStaffDTO.getOwnerCode();
                }
            }

            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                productOfferId = productOfferingTotalDTO.getProductOfferingId();
            }
            //lay danh sach productOfferType
            List<ProductOfferingDTO> productOfferingList = productOfferingService.getListProductOfferTypeForProcessStock(DataUtil.safeToString(ownerType), DataUtil.safeToString(ownerId), productOfferId, DataUtil.safeToLong(stateId));

            //lay danh sach tock_total
            Map<Long, List<ProductOfferingTotalDTO>> map = new HashMap<Long, List<ProductOfferingTotalDTO>>();

            List<ProductOfferingTotalDTO> listStock = productOfferingService.getListProductOfferingForProcessStock(ownerType, ownerId, productOfferId, DataUtil.safeToLong(stateId));
            if (listStock != null) {
                for (ProductOfferingTotalDTO stockTotal : listStock) {
                    List<ProductOfferingTotalDTO> listStockChild = map.get(stockTotal.getProductOfferTypeId());
                    if (DataUtil.isNullOrEmpty(listStockChild)) {
                        listStockChild = new ArrayList<ProductOfferingTotalDTO>();
                        listStockChild.add(stockTotal);
                        map.put(stockTotal.getProductOfferTypeId(), listStockChild);
                    } else {
                        listStockChild.add(stockTotal);
                        map.put(stockTotal.getProductOfferTypeId(), listStockChild);
                    }
                }
            }
            //Tao rootStock
            rootStock = new DefaultTreeNode(new WareHouseInfoDTO(), null);
            String productCodeName = "";
            if (!DataUtil.isNullOrEmpty(productOfferingList)) {
                for (ProductOfferingDTO product : productOfferingList) {
                    TreeNode groupStock = new DefaultTreeNode(new WareHouseInfoDTO(product.getName()), rootStock);
                    List<ProductOfferingTotalDTO> listStockChild = map.get(product.getProductOfferTypeId());
                    if (!DataUtil.isNullOrEmpty(listStockChild)) {
                        for (ProductOfferingTotalDTO productTotal : listStockChild) {
                            if (!DataUtil.isNullObject(productTotal.getCode()) && !DataUtil.isNullObject(productTotal.getName())) {
                                productCodeName = productTotal.getCode() + "-" + productTotal.getName();
                            }
                            new DefaultTreeNode(new WareHouseInfoDTO(productCodeName, productTotal.getStateName(), productTotal.getUnit(),
                                    productTotal.getCurrentQuantity() - productTotal.getAvailableQuantity(), productTotal.getAvailableQuantity(), productTotal.getCurrentQuantity(),
                                    String.valueOf(productTotal.getProductOfferTypeId()), String.valueOf(productTotal.getProductOfferingId()),
                                    String.valueOf(productTotal.getOwnerType()), String.valueOf(productTotal.getOwnerId()),
                                    productTotal.getCheckSerial(), DataUtil.safeToString(productTotal.getStateId()), shopName, shopCode), groupStock);
                        }
                    }
                }
            }
            topPage();
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
    public void doResetProcessStock() {
        //
        this.vShopDTO = null;
        this.vStaffDTO = null;
        shopInfoTag.initShopAndAllChild(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true, Const.OWNER_TYPE.SHOP);
        shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
        vShopDTO = shopInfoTag.getvShopStaffDTO();
        staffInfoTag.initStaffWithChanelTypesAndParrentShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), null, lstChanelTypeId, false);
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
        stateId = null;
    }

    @Secured("@")
    public void doComeBack() {
        //
        showDetail = false;
        wareHouseInfoDTO = new WareHouseInfoDTO();
        doViewStock();
    }

    @Secured("@")
    public void doSelect() {

    }

    @Secured("@")
    public List<ProductOfferingTotalDTO> doChangeOffering(String inputProduct) {
        try {
            lsProductOfferingTotalDTO = productOfferingService.getAllLsProductOfferingDTOForProcessStock(inputProduct.trim(), getListProductOfferTypeID());
            return lsProductOfferingTotalDTO;
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return Lists.newArrayList();
    }


    @Secured("@")
    public void showDialogSerial(WareHouseInfoDTO wareHouseInfoDTO) {
        try {
            showDetail = true;
            balance = false;
            this.wareHouseInfoDTO = wareHouseInfoDTO;
            checkValidateSerial(DataUtil.safeToLong(wareHouseInfoDTO.getOwnerId()), DataUtil.safeToLong(wareHouseInfoDTO.getOwnerType())
                    , DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId()), DataUtil.safeToLong(wareHouseInfoDTO.getStateId()));
            lsSerial = stockTransSerialService.findStockTransSerialByProductOfferType(wareHouseInfoDTO);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "", "common.error.happened");
            topPage();
        }

    }

    @Secured("@")
    public void checkValidateSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId) throws LogicException, Exception {
        if (DataUtil.isNullOrZero(ownerId) || DataUtil.isNullOrZero(ownerType) ||
                DataUtil.isNullOrZero(prodOfferId) || DataUtil.isNullOrZero(stateId)) {
            throw new LogicException("", "process.stock.detail.serial.require");
        }
        VShopStaffDTO vShopStaffDTO = shopService.getShopByOwnerId(DataUtil.safeToString(ownerId), DataUtil.safeToString(ownerType));
        if (DataUtil.isNullObject(vShopStaffDTO)) {
            throw new LogicException("", "process.stock.detail.serial.require.shop");
        }
        ProductOfferingTotalDTO productOfferingTotalDTO = productOfferingService.getProductOffering(prodOfferId);
        if (DataUtil.isNullObject(productOfferingTotalDTO)) {
            throw new LogicException("", "process.stock.detail.serial.require.offering");
        }

    }

    @Secured("@")
    public void balanceStockProduct() {
        try {
            //Lay tong so luong serial
            quantity = 0L;
            if (!DataUtil.isNullObject(lsSerial)) {
                for (StockTransSerialDTO stockTransSerialDTO : lsSerial) {
                    quantity += stockTransSerialDTO.getQuantity();
                }
            }
            Long currQuantity = DataUtil.safeToLong(wareHouseInfoDTO.getCurrentQuanlity());
            //check giao dich treo
            Long ownerId = DataUtil.safeToLong(wareHouseInfoDTO.getOwnerId());
            Long ownerType = DataUtil.safeToLong(wareHouseInfoDTO.getOwnerType());
            Long prodOfferId = DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId());
            Long stateId = DataUtil.safeToLong(wareHouseInfoDTO.getStateId());
            boolean checkSusbpend = stockTransService.checkStockSusbpendForProcessStock(ownerId, DataUtil.safeToString(ownerType), prodOfferId);
            if (!checkSusbpend) {
                throw new LogicException("", "process.stock.detail.susbpend");
            }
            // cap nhat stock_total
            Long oldCurrQuantity = 0L;
            Long oldAvailQuantity = 0L;
            Long changeCurr;
            Long changeAvail;
            StockTotalDTO stockTotalDTO = stockTotalService.getStockTotalForProcessStock(ownerId, ownerType, prodOfferId, stateId);
            if (!DataUtil.isNullObject(stockTotalDTO)) {
                oldCurrQuantity = stockTotalDTO.getCurrentQuantity();
                oldAvailQuantity = stockTotalDTO.getAvailableQuantity();
            }
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId()));
            if (!DataUtil.isNullObject(productOfferingTotalDTO) && DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                changeCurr = currQuantity - oldCurrQuantity;
                changeAvail = currQuantity - oldAvailQuantity;
            } else {
                changeCurr = quantity - oldCurrQuantity;
                changeAvail = quantity - oldAvailQuantity;
            }

            StockTotalMessage stockTotalMessage = stockTotalService.changeStockTotal(ownerId, ownerType, prodOfferId, stateId, changeCurr,
                    changeAvail, 0L, BccsLoginSuccessHandler.getStaffDTO().getUserId(), 0L, 0L, new Date(), "", Const.STOCK_TRANS_TYPE.EXPORT, Const.SourceType.OTHER);
            if (!DataUtil.isNullOrEmpty(stockTotalMessage.getErrorCode())) {
                throw new LogicException(stockTotalMessage.getErrorCode(), stockTotalMessage.getKeyMsg());
            }
            balance = true;
            wareHouseInfoDTO.setCurrentQuanlity(quantity);
            wareHouseInfoDTO.setAvailableQuanlity(quantity);
            doViewStock();
            reportSuccess("", "process.stock.balance.success");
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
    public void validateBalanceStockSerial() throws LogicException, Exception {
        checkValidateSerial(DataUtil.safeToLong(wareHouseInfoDTO.getOwnerId()), DataUtil.safeToLong(wareHouseInfoDTO.getOwnerType())
                , DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId()), DataUtil.safeToLong(wareHouseInfoDTO.getStateId()));
    }


    @Secured("@")
    public void validateBalanceStockNoSerial(WareHouseInfoDTO item) {
        try {
            this.wareHouseInfoDTO = item;
            checkValidateSerial(DataUtil.safeToLong(wareHouseInfoDTO.getOwnerId()), DataUtil.safeToLong(wareHouseInfoDTO.getOwnerType())
                    , DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId()), DataUtil.safeToLong(wareHouseInfoDTO.getStateId()));
            ProductOfferingTotalDTO productOfferingTotalDTO = productOfferingService.getProductOffering(DataUtil.safeToLong(wareHouseInfoDTO.getProductOfferingId()));
            if (!DataUtil.isNullObject(productOfferingTotalDTO) && DataUtil.safeEqual(productOfferingTotalDTO.getCheckSerial(), "1")) {
                throw new LogicException("", "process.stock.balance.support.serial");
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

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopDTO = vShopStaffDTO;
        staffInfoTag.initStaffWithChanelTypesAndParrentShop(this, vShopDTO.getOwnerId(), null, lstChanelTypeId, false);
    }

    @Secured("@")
    public void clearShop() {
        this.vShopDTO = null;
        this.vStaffDTO = null;
        staffInfoTag.initEmptyStaff();
    }

    @Secured("@")
    public void receiveStaff(VShopStaffDTO vShopStaffDTO) {
        this.vStaffDTO = vShopStaffDTO;
    }

    @Secured("@")
    public void clearStaff() {
        this.vStaffDTO = null;
    }

    public WareHouseInfoDTO getWareHouseInfoDTO() {
        return wareHouseInfoDTO;
    }

    public void setWareHouseInfoDTO(WareHouseInfoDTO wareHouseInfoDTO) {
        this.wareHouseInfoDTO = wareHouseInfoDTO;
    }

    @Secured("@")
    public void clearProduct() {
        this.productOfferingTotalDTO = null;
    }

    @Secured("@")
    public boolean disabledState() {
        return DataUtil.isNullObject(productOfferingTotalDTO) ? false : (DataUtil.isNullOrZero(productOfferingTotalDTO.getProductOfferingId()) ? false : true);
    }

    public List<StockTransSerialDTO> getLsSerial() {
        return lsSerial;
    }

    public void setLsSerial(List<StockTransSerialDTO> lsSerial) {
        this.lsSerial = lsSerial;
    }

    public List<String> getLstChanelTypeId() {
        return lstChanelTypeId;
    }

    public void setLstChanelTypeId(List<String> lstChanelTypeId) {
        this.lstChanelTypeId = lstChanelTypeId;
    }

    public StaffInfoNameable getStaffInfoTag() {
        return staffInfoTag;
    }

    public void setStaffInfoTag(StaffInfoNameable staffInfoTag) {
        this.staffInfoTag = staffInfoTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }

    public List<OptionSetValueDTO> getListState() {
        return listState;
    }

    public void setListState(List<OptionSetValueDTO> listState) {
        this.listState = listState;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public ActionLogDTO getActionLogDTO() {
        return actionLogDTO;
    }

    public void setActionLogDTO(ActionLogDTO actionLogDTO) {
        this.actionLogDTO = actionLogDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public TreeNode getRootStock() {
        return rootStock;
    }

    public void setRootStock(TreeNode rootStock) {
        this.rootStock = rootStock;
    }

    public boolean isBalance() {
        return balance;
    }

    public void setBalance(boolean balance) {
        this.balance = balance;
    }
}

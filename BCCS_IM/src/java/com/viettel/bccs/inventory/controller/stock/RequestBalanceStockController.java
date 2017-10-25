package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockBalanceRequestService;
import com.viettel.bccs.inventory.tag.ListProductNameable;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangnt14 on 3/14/2016.
 */
@Component
@Scope("view")
@ManagedBean(name = "requestBalanceStockController")
public class RequestBalanceStockController extends TransCommonController {
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ListProductNameable listProductTag;//khai bao tag danh sach hang hoa
    @Autowired
    private SignOfficeTagNameable writeOfficeTag;//khai bao tag ky vOffice
    @Autowired
    private StockBalanceRequestService stockBalanceRequestService;
    private int limitAutoComplete;
    private String type;
    private List<OptionSetValueDTO> listType = Lists.newArrayList();
    private boolean showProduct;
    private VShopStaffDTO vShopStaffDTO;
    private StockBalanceRequestDTO stockBalanceRequestDTO = new StockBalanceRequestDTO();
    private List<StockBalanceDetailDTO> lstDetailDTO = Lists.newArrayList();
    private List<StockBalanceSerialDTO> lstSerial = Lists.newArrayList();

    @PostConstruct
    public void init() {
        try {
            vShopStaffDTO = new VShopStaffDTO();
            vShopStaffDTO.setOwnerId(DataUtil.safeToString(BccsLoginSuccessHandler.getStaffDTO().getShopId()));
            listProductTag.setAddNewProduct(true);
            shopInfoTag.initShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true);
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            listType = optionSetValueService.getByOptionSetCode(Const.STOCK_BALANCE.STOCK_BALANCE_TYPE);
            limitAutoComplete = DataUtil.safeToInt(optionSetValueService.getValueByNameFromOptionSetValue(Const.OPTION_SET.LIMIT_AUTOCOMPLETE));
            type = DataUtil.safeToString(Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT);
            initTagProduct(BccsLoginSuccessHandler.getStaffDTO().getShopId());
            writeOfficeTag.init(this, BccsLoginSuccessHandler.getStaffDTO().getShopId());
            stockBalanceRequestDTO.setRequestCode("RC_" + stockBalanceRequestService.getMaxId());
        } catch (Exception e) {
            logger.error(e);
            reportError("", "common.error.happened", e);
        }
    }

    private void initTagProduct(Long ownerId) {
        ConfigListProductTagDTO config = new ConfigListProductTagDTO(Const.MODE_SERIAL.MODE_ADD_ALL, ownerId, Const.OWNER_TYPE.SHOP);
        config.setShowTotalPrice(false);
        config.setBalanceStock(true);
        config.setBalanceStockType(DataUtil.safeToLong(type));
        listProductTag.init(this, config);
    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
        initTagProduct(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
    }

    @Secured("@")
    public void clearShop(String method) {
        vShopStaffDTO = null;
        listProductTag.setLsListProductOfferDTO(new ArrayList<ListProductOfferDTO>());
        listProductTag.setLsProductOfferTypeDTOTmp(new ArrayList<ProductOfferTypeDTO>());
        listProductTag.setLsProductOfferingTotalDTOTmp(new ArrayList<ProductOfferingTotalDTO>());
    }

    @Secured("@")
    public void doChangeType() {
        initTagProduct(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
        writeOfficeTag.resetOffice();
    }


    @Secured("@")
    public void doReset() {
        try {
            stockBalanceRequestDTO.setRequestCode("RC_" + stockBalanceRequestService.getMaxId());
            type = DataUtil.safeToString(Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT);
            writeOfficeTag.resetOffice();
            shopInfoTag.initShop(this, BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), true);
            shopInfoTag.loadShop(BccsLoginSuccessHandler.getStaffDTO().getShopId().toString(), false);
            initTagProduct(BccsLoginSuccessHandler.getStaffDTO().getShopId());
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
    public void doValidateSendRequest() {
        doValidateListDetail(listProductTag.getListTransDetailDTOs());
    }


    @Secured("@")
    public void doSendRequest() {
        try {
            List<StockTransDetailDTO> stockTransDetailDTOs = listProductTag.getListTransDetailDTOs();
            if (DataUtil.isNullOrEmpty(stockTransDetailDTOs) || DataUtil.isNullOrZero(stockTransDetailDTOs.get(0).getProdOfferId())) {
                throw new LogicException("", "stock.rescueInformation.validate.list");
            }
            // set stock_balance_detail va stock_balance_serial
            lstDetailDTO = Lists.newArrayList();
            for (StockTransDetailDTO stockTransDetailDTO : stockTransDetailDTOs) {
                if (DataUtil.isNullObject(stockTransDetailDTO.getQuantityFinance())) {
                    throw new LogicException("", "stock.balance.request.finance.require");
                }
                StockBalanceDetailDTO stockBalanceDetailDTO = new StockBalanceDetailDTO();
                stockBalanceDetailDTO.setProdOfferId(stockTransDetailDTO.getProdOfferId());
                stockBalanceDetailDTO.setQuantityBccs(stockTransDetailDTO.getQuantityBccs());
                stockBalanceDetailDTO.setQuantityReal(stockTransDetailDTO.getQuantityInspect());
                stockBalanceDetailDTO.setQuantityErp(stockTransDetailDTO.getQuantityFinance());
                stockBalanceDetailDTO.setQuantity(stockTransDetailDTO.getQuantity());
                stockBalanceDetailDTO.setProdOfferName(stockTransDetailDTO.getProdOfferName());
                if (DataUtil.safeEqual(type, Const.STOCK_BALANCE.BALANCE_TYPE_EXPORT)) {
                    if (stockTransDetailDTO.getQuantityFinance() >= stockTransDetailDTO.getQuantityBccs()) {
                        throw new LogicException("", "stock.balance.request.finance.export");
                    }
                } else {
                    if (DataUtil.safeEqual(type, Const.STOCK_BALANCE.BALANCE_TYPE_IMPORT)) {
                        if (stockTransDetailDTO.getQuantityFinance() <= stockTransDetailDTO.getQuantityBccs()) {
                            throw new LogicException("", "stock.balance.request.finance.import");
                        }
                    }
                }
                if (DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstStockTransSerial()) && DataUtil.safeEqual(stockTransDetailDTO.getCheckSerial(), Const.SERIAL_STATUS)) {
                    throw new LogicException("", "mn.stock.confirm.serial.empty", stockTransDetailDTO.getProdOfferName());
                }
                lstSerial = Lists.newArrayList();
                if (!DataUtil.isNullOrEmpty(stockTransDetailDTO.getLstStockTransSerial())) {
                    for (StockTransSerialDTO stockTransSerialDTO : stockTransDetailDTO.getLstStockTransSerial()) {
                        StockBalanceSerialDTO stockBalanceSerialDTO = new StockBalanceSerialDTO();
                        stockBalanceSerialDTO.setProdOfferId(stockTransSerialDTO.getProdOfferId());
                        stockBalanceSerialDTO.setFromSerial(stockTransSerialDTO.getFromSerial());
                        stockBalanceSerialDTO.setToSerial(stockTransSerialDTO.getToSerial());
                        lstSerial.add(stockBalanceSerialDTO);
                    }
                }
                stockBalanceDetailDTO.setLstSerialDTO(lstSerial);
                lstDetailDTO.add(stockBalanceDetailDTO);
            }
            //vOffice
            SignOfficeDTO signOfficeDTO = writeOfficeTag.validateVofficeAccount();
            stockBalanceRequestDTO.setAccountName(signOfficeDTO.getUserName());
            stockBalanceRequestDTO.setAccountPass(signOfficeDTO.getPassWord());
            stockBalanceRequestDTO.setSignFlowId(signOfficeDTO.getSignFlowId());
            //save
            stockBalanceRequestDTO.setOwnerId(DataUtil.safeToLong(vShopStaffDTO.getOwnerId()));
            stockBalanceRequestDTO.setType(DataUtil.safeToLong(type));
            stockBalanceRequestDTO.setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockBalanceRequestDTO.setCreateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            stockBalanceRequestDTO.setUpdateUser(BccsLoginSuccessHandler.getStaffDTO().getStaffCode());
            stockBalanceRequestDTO.setOwnerIdLogin(BccsLoginSuccessHandler.getStaffDTO().getStaffId());
            stockBalanceRequestService.saveStockBalanceRequest(stockBalanceRequestDTO, lstDetailDTO);
            doReset();
            reportSuccess("", "mn.balance.stock.request.success");
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
            stockBalanceRequestDTO.setRequestCode("RC_" + stockBalanceRequestService.getMaxId());
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

    public ListProductNameable getListProductTag() {
        return listProductTag;
    }

    public void setListProductTag(ListProductNameable listProductTag) {
        this.listProductTag = listProductTag;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public int getLimitAutoComplete() {
        return limitAutoComplete;
    }

    public void setLimitAutoComplete(int limitAutoComplete) {
        this.limitAutoComplete = limitAutoComplete;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OptionSetValueDTO> getListType() {
        return listType;
    }

    public void setListType(List<OptionSetValueDTO> listType) {
        this.listType = listType;
    }

    public SignOfficeTagNameable getWriteOfficeTag() {
        return writeOfficeTag;
    }

    public boolean isShowProduct() {
        return showProduct;
    }

    public void setShowProduct(boolean showProduct) {
        this.showProduct = showProduct;
    }

    public void setWriteOfficeTag(SignOfficeTagNameable writeOfficeTag) {

        this.writeOfficeTag = writeOfficeTag;
    }

    public VShopStaffDTO getvShopStaffDTO() {
        return vShopStaffDTO;
    }

    public void setvShopStaffDTO(VShopStaffDTO vShopStaffDTO) {
        this.vShopStaffDTO = vShopStaffDTO;
    }

    public StockBalanceRequestDTO getStockBalanceRequestDTO() {
        return stockBalanceRequestDTO;
    }

    public void setStockBalanceRequestDTO(StockBalanceRequestDTO stockBalanceRequestDTO) {
        this.stockBalanceRequestDTO = stockBalanceRequestDTO;
    }
}


package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.fw.controller.TransCommonController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockCardService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.bean.ApplicationContextProvider;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 8/5/2016.
 */
@Component
@Scope("view")
@ManagedBean
public class ExchangeCardBankplusController extends TransCommonController {

    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO;
    private ExchangeCardBankplusDTO exchangeCardBankplusDTO = new ExchangeCardBankplusDTO();
    private StaffDTO staffDTO;
    private List<OptionSetValueDTO> lstOptionSetValueDTOs;
    private Long shopId;

    @Autowired
    private StockCardService stockCardService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private ShopService shopService;

    @PostConstruct
    public void init() {
        try {
            exchangeCardBankplusDTO = new ExchangeCardBankplusDTO();
            exchangeCardBankplusDTO.setSaleDate(new Date());
            staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            lstOptionSetValueDTOs = optionSetValueService.getLsOptionSetValueByCode("SHOP_TMDT");
            if (DataUtil.isNullOrEmpty(lstOptionSetValueDTOs)) {
                throw new LogicException("", "utilities.exchange.card.bankplus.tmdt");
            }
            ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(lstOptionSetValueDTOs.get(0).getValue());
            if (DataUtil.isNullObject(shopDTO)) {
                throw new LogicException("", "utilities.exchange.card.bankplus.tmdt");
            }
            shopId = shopDTO.getShopId();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * Tim kiem mat hang khi nhap tay
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> searchListProductOffering(String input) {
        try {
            CharSequence inputData = DataUtil.isNullOrEmpty(input) ? "" : input.trim().toUpperCase();
            lstProductOfferingTotalDTO = productOfferingService.getListProductOfferingByProductType(inputData.toString(), Const.PRODUCT_OFFER_TYPE.CARD);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            reportError("", "", ex);
            topPage();
        }
        return lstProductOfferingTotalDTO;
    }

    @Secured("@")
    public void doSelectProductOffering() {
        try {
            if (!DataUtil.isNullObject(productOfferingTotalDTO)) {
                exchangeCardBankplusDTO.setProdOfferId(productOfferingTotalDTO.getProductOfferingId());
            }
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    @Secured("@")
    public void doExchange() {
        try {
            stockCardService.exchangeCardBankplus(exchangeCardBankplusDTO, shopId, staffDTO);
            reportSuccess("", "utilities.exchange.card.bankplus.success");
        } catch (LogicException e) {
            topReportError("", e);
        } catch (Exception e) {
            topReportError("", "common.error.happened", e);
        }
    }

    @Secured("@")
    public void doValidate() {
    }

    @Secured("@")
    public void doViewStock() {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        ViewInforWarehouseController controller = (ViewInforWarehouseController) context.getBean(ViewInforWarehouseController.class);
        controller.setTypeProductOffering("1");
        controller.getShopInfoTag().initShopAndAllChild(this, shopId.toString(), true, Const.OWNER_TYPE.SHOP);
        controller.getShopInfoTag().loadShop(shopId.toString(), false);
        controller.setDisableUnit(true);
        controller.getCurShopStaff().setOwnerId(shopId.toString());
        controller.searchStock();
    }

    @Secured("@")
    public void clearShop() {

    }

    @Secured("@")
    public void receiveShop(VShopStaffDTO vShopStaffDTO) {

    }

    @Secured("@")
    public void doReset() {
        exchangeCardBankplusDTO = new ExchangeCardBankplusDTO();
        exchangeCardBankplusDTO.setSaleDate(new Date());
        productOfferingTotalDTO = null;
    }

    @Secured("@")
    public void resetLstProductOfferingTotal() {
        exchangeCardBankplusDTO.setProdOfferId(null);
        productOfferingTotalDTO = null;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferingTotalDTO> getLstProductOfferingTotalDTO() {
        return lstProductOfferingTotalDTO;
    }

    public void setLstProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lstProductOfferingTotalDTO) {
        this.lstProductOfferingTotalDTO = lstProductOfferingTotalDTO;
    }

    public ExchangeCardBankplusDTO getExchangeCardBankplusDTO() {
        return exchangeCardBankplusDTO;
    }

    public void setExchangeCardBankplusDTO(ExchangeCardBankplusDTO exchangeCardBankplusDTO) {
        this.exchangeCardBankplusDTO = exchangeCardBankplusDTO;
    }

}

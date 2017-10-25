package com.viettel.bccs.inventory.dto;

import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class ProductOfferingTotalDTO extends BaseDTO {

    @JsonProperty("table_pk")
    private String tablePk;
    @JsonProperty("prod_offer_id")
    private Long productOfferingId;
    @JsonProperty("product_offer_type_id")
    private Long productOfferTypeId;
    @JsonProperty("product_offer_type_name")
    private String productOfferTypeName;
    @JsonProperty("telecom_service_id")
    private Long telecomServiceId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("code")
    private String code;
    @JsonProperty("accounting_model_code")
    private String accountingModelCode;
    @JsonProperty("accounting_model_name")
    private String accountingModelName;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("unit_name")
    private String unitName;
    @JsonProperty("source_price")
    private Long sourcePrice;
    @JsonProperty("owner_id")
    private Long ownerId;
    @JsonProperty("owner_type")
    private String ownerType;
    @JsonProperty("state_id")
    private Long stateId;
    @JsonProperty("state_name")
    private String stateName;
    @JsonProperty("current_quantity")
    private Long currentQuantity;
    @JsonProperty("available_quantity")
    private Long availableQuantity;
    @JsonProperty("hang_quantity")
    private Long hangQuantity;
    @JsonProperty("check_serial")
    private String checkSerial;
    private Long priceAmount;//gia ban le
    private Long priceDeposit;//gia dat coc
    private List<Long> lstPriceDeposit;
    private Long quantityBccs;
    private Long quantityInspect;
    private Long quantityFinance;
    private Long requestQuantity;
    private Long requestQuantityInput;
    private ShopDTO shopDTO;

    public ProductOfferingTotalDTO() {
    }

    public List<Long> getLstPriceDeposit() {
        return lstPriceDeposit;
    }

    public void setLstPriceDeposit(List<Long> lstPriceDeposit) {
        this.lstPriceDeposit = lstPriceDeposit;
    }

    public Boolean getCheckShowSerial() {
        return !DataUtil.isNullOrEmpty(checkSerial) && "1".equals(checkSerial);
    }

    public String getItemLabel() {
        String result = "";
        if (!DataUtil.isNullOrEmpty(code) && !DataUtil.isNullOrEmpty(name)) {
            result = code + "-" + name;
        }
        return result;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }

    public String getTablePk() {
        return tablePk;
    }

    public void setTablePk(String tablePk) {
        this.tablePk = tablePk;
    }

    public Long getProductOfferingId() {
        return productOfferingId;
    }

    public void setProductOfferingId(Long productOfferingId) {
        this.productOfferingId = productOfferingId;
    }

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getProductOfferTypeName() {
        return productOfferTypeName;
    }

    public void setProductOfferTypeName(String productOfferTypeName) {
        this.productOfferTypeName = productOfferTypeName;
    }

    public Long getTelecomServiceId() {
        return telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public String getAccountingModelName() {
        return accountingModelName;
    }

    public void setAccountingModelName(String accountingModelName) {
        this.accountingModelName = accountingModelName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(Long sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Long currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getHangQuantity() {
        return hangQuantity;
    }

    public void setHangQuantity(Long hangQuantity) {
        this.hangQuantity = hangQuantity;
    }

    public Long getQuantityBccs() {
        return quantityBccs;
    }

    public void setQuantityBccs(Long quantityBccs) {
        this.quantityBccs = quantityBccs;
    }

    public Long getQuantityInspect() {
        return quantityInspect;
    }

    public void setQuantityInspect(Long quantityInspect) {
        this.quantityInspect = quantityInspect;
    }

    public Long getQuantityFinance() {
        return quantityFinance;
    }

    public void setQuantityFinance(Long quantityFinance) {
        this.quantityFinance = quantityFinance;
    }

    public Long getRequestQuantity() {
        return requestQuantity;
    }

    public void setRequestQuantity(Long requestQuantity) {
        this.requestQuantity = requestQuantity;
    }

    public Long getRequestQuantityInput() {
        return requestQuantityInput;
    }

    public void setRequestQuantityInput(Long requestQuantityInput) {
        this.requestQuantityInput = requestQuantityInput;
    }

    public ShopDTO getShopDTO() {
        return shopDTO;
    }

    public void setShopDTO(ShopDTO shopDTO) {
        this.shopDTO = shopDTO;
    }

    @Override
    public String toString() {
        if (DataUtil.isNullObject(productOfferingId)) {
            return null;
        }
        return DataUtil.safeToString(productOfferingId);
    }

    public Long getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Long priceAmount) {
        this.priceAmount = priceAmount;
    }

    public Long getPriceDeposit() {
        return priceDeposit;
    }

    public void setPriceDeposit(Long priceDeposit) {
        this.priceDeposit = priceDeposit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductOfferingTotalDTO) {
            ProductOfferingTotalDTO prod = (ProductOfferingTotalDTO) obj;
            if (DataUtil.safeEqual(prod.getProductOfferingId(), productOfferingId)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductOfferingDTO extends BaseDTO {
    @JsonProperty("code")
    private String code;
    private Date createDatetime;
    private String createUser;
    private String description;
    private Date effectDatetime;
    private Date expireDatetime;
    @JsonProperty("name")
    private String name;
    @JsonProperty("product_offer_type_id")
    private Long productOfferTypeId;
    @JsonProperty("prod_offer_id")
    private Long productOfferingId;
    private Long productSpecId;
    private String status;
    @JsonProperty("day_stock_remain")
    private Long dayStockRemain;
    private String subType;
    private Long telecomServiceId;
    private Date updateDatetime;
    private String updateUser;
    private String nameUpper;
    private Long checkSerial;
    private String unit;
    private Long quantity;
    private String strQuantityKcs;
    private Long quantityKtts;
    private Long price;
    private Long amount;
    private List<ProductOfferPriceDTO> productOfferPriceDTOList;
    private List<ProductOfferingDTO> lsProdOfferSim = Lists.newArrayList();
    private Long prodOfferSimId;
    private Long stateId;
    private String stateIdName;
    private String productOfferTypeIdName;
    private List<StockTransSerialDTO> listStockTransSerialDTOs = new ArrayList<>();
    private StockTransSerialDTO stockTransSerialDTO = new StockTransSerialDTO();
    private String impType;
    private List<String> listProfile;
    private List<String> lstParam;
    private String profile;
    private boolean logistic;
    private Long availableQuantity;
    private String accountingModelCode;
    private Long priceCost;
    private String ownerCode;
    private Long ownerType;

    public Long getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(Long priceCost) {
        this.priceCost = priceCost;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public ProductOfferingDTO() {
    }

    public void createProfile(List<String> listProfiles) {
        if (listProfiles != null) {
            profile = "";
            for (String prof : listProfiles) {
                if (profile != null && !profile.isEmpty()) {
                    profile += ",";
                }
                profile += prof;
            }
        }
        this.listProfile = listProfiles;
    }

    public boolean getShowInputSerial() {
        return !Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(this.productOfferTypeId)
                && (DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_FILE).equals(this.impType) && DataUtil.isNullOrEmpty(this.lstParam)
                        || DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE).equals(this.impType) && DataUtil.isNullOrEmpty(this.listStockTransSerialDTOs));
    }

    public boolean getShowViewSerial() {
        return !Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL.equals(this.productOfferTypeId)
                && (DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_FILE).equals(this.impType) && !DataUtil.isNullOrEmpty(this.lstParam)
                || DataUtil.safeToString(Const.IMP_TYPE.IMP_BY_SERIAL_RANGE).equals(this.impType) && !DataUtil.isNullOrEmpty(this.listStockTransSerialDTOs));
    }


    public String getKeySet() {
        return keySet;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEffectDatetime() {
        return this.effectDatetime;
    }

    public void setEffectDatetime(Date effectDatetime) {
        this.effectDatetime = effectDatetime;
    }

    public Date getExpireDatetime() {
        return this.expireDatetime;
    }

    public void setExpireDatetime(Date expireDatetime) {
        this.expireDatetime = expireDatetime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductOfferTypeId() {
        return this.productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public Long getProductOfferingId() {
        return this.productOfferingId;
    }

    public void setProductOfferingId(Long productOfferingId) {
        this.productOfferingId = productOfferingId;
    }

    public Long getProductSpecId() {
        return this.productSpecId;
    }

    public void setProductSpecId(Long productSpecId) {
        this.productSpecId = productSpecId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long getTelecomServiceId() {
        return this.telecomServiceId;
    }

    public void setTelecomServiceId(Long telecomServiceId) {
        this.telecomServiceId = telecomServiceId;
    }

    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return "" + productOfferingId;
    }

    public String getNameUpper() {
        return nameUpper;
    }

    public void setNameUpper(String nameUpper) {
        this.nameUpper = nameUpper;
    }

    public Long getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(Long checkSerial) {
        this.checkSerial = checkSerial;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public List<ProductOfferPriceDTO> getProductOfferPriceDTOList() {
        return productOfferPriceDTOList;
    }

    public void setProductOfferPriceDTOList(List<ProductOfferPriceDTO> productOfferPriceDTOList) {
        this.productOfferPriceDTOList = productOfferPriceDTOList;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getProdOfferSimId() {
        return prodOfferSimId;
    }

    public void setProdOfferSimId(Long prodOfferSimId) {
        this.prodOfferSimId = prodOfferSimId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateIdName() {
        return stateIdName;
    }

    public void setStateIdName(String stateIdName) {
        this.stateIdName = stateIdName;
    }

    public String getProductOfferTypeIdName() {
        return productOfferTypeIdName;
    }

    public void setProductOfferTypeIdName(String productOfferTypeIdName) {
        this.productOfferTypeIdName = productOfferTypeIdName;
    }

    public List<ProductOfferingDTO> getLsProdOfferSim() {
        return lsProdOfferSim;
    }

    public boolean getIsExistSim() {
        return !DataUtil.isNullOrEmpty(lsProdOfferSim);
    }

    public void setLsProdOfferSim(List<ProductOfferingDTO> lsProdOfferSim) {
        this.lsProdOfferSim = lsProdOfferSim;
    }

    public Long getQuantityKtts() {
        return quantityKtts;
    }

    public void setQuantityKtts(Long quantityKtts) {
        this.quantityKtts = quantityKtts;
    }

    public String getStrQuantityKcs() {
        return strQuantityKcs;
    }

    public void setStrQuantityKcs(String strQuantityKcs) {
        this.strQuantityKcs = strQuantityKcs;
    }

    public List<StockTransSerialDTO> getListStockTransSerialDTOs() {
        return listStockTransSerialDTOs;
    }

    public void setListStockTransSerialDTOs(List<StockTransSerialDTO> listStockTransSerialDTOs) {
        this.listStockTransSerialDTOs = listStockTransSerialDTOs;
    }

    public String getImpType() {
        return impType;
    }

    public void setImpType(String impType) {
        this.impType = impType;
    }

    public List<String> getListProfile() {
        return listProfile;
    }

    public void setListProfile(List<String> listProfile) {
        this.listProfile = listProfile;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public StockTransSerialDTO getStockTransSerialDTO() {
        return stockTransSerialDTO;
    }

    public void setStockTransSerialDTO(StockTransSerialDTO stockTransSerialDTO) {
        this.stockTransSerialDTO = stockTransSerialDTO;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public List<String> getLstParam() {
        return lstParam;
    }

    public void setLstParam(List<String> lstParam) {
        this.lstParam = lstParam;
    }

    public boolean isLogistic() {
        return logistic;
    }

    public void setLogistic(boolean logistic) {
        this.logistic = logistic;
    }

    public Long getDayStockRemain() {
        return dayStockRemain;
    }

    public void setDayStockRemain(Long dayStockRemain) {
        this.dayStockRemain = dayStockRemain;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }
}

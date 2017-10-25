package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ThaoNT19 on 2/4/2016.
 */
public class WareHouseInfoDTO extends BaseDTO {

    private String name;
    private String stateName;
    private String unit;
    private Long requirementQuanlity;
    private Long availableQuanlity;
    private Long currentQuanlity;
    private String productOfferTypeId;
    private String productOfferingId;
    private String ownerType;
    private String ownerId;
    private String checkSerial;
    private String stateId;
    private boolean isExport;
    private String shopName;
    private String shopCode;
    private String productCode;
    private String productName;
    private String accountingModelCode;
    private Long realQuantity;
    private List<SubGoodsDTO> lstGoods;

    public WareHouseInfoDTO() {
    }

    public WareHouseInfoDTO(String name, String productOfferTypeId) {
        this.name = name;
        this.productOfferTypeId = productOfferTypeId;
    }

    public WareHouseInfoDTO(String name) {
        this.name = name;
    }

    public WareHouseInfoDTO(String name, String stateName, String productName, Long currentQuanlity, Long realQuantity) {
        this.name = name;
        this.stateName = stateName;
        this.productName = productName;
        this.currentQuanlity = currentQuanlity;
        this.realQuantity = realQuantity;
    }

    public WareHouseInfoDTO(String name, String stateName, String unit, Long requirementQuanlity, Long availableQuanlity,
                            Long currentQuanlity, String productOfferTypeId, String productOfferingId,
                            String ownerType, String ownerId, String checkSerial, String stateId) {
        this.name = name;
        this.stateName = stateName;
        this.unit = unit;
        this.requirementQuanlity = requirementQuanlity;
        this.availableQuanlity = availableQuanlity;
        this.currentQuanlity = currentQuanlity;
        this.productOfferTypeId = productOfferTypeId;
        this.productOfferingId = productOfferingId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.checkSerial = checkSerial;
        this.stateId = stateId;
    }

    public WareHouseInfoDTO(String name, String stateName, String unit, Long requirementQuanlity, Long availableQuanlity,
                            Long currentQuanlity, String productOfferTypeId, String productOfferingId,
                            String ownerType, String ownerId, String checkSerial, String stateId, String shopName, String shopCode) {
        this.name = name;
        this.stateName = stateName;
        this.unit = unit;
        this.requirementQuanlity = requirementQuanlity;
        this.availableQuanlity = availableQuanlity;
        this.currentQuanlity = currentQuanlity;
        this.productOfferTypeId = productOfferTypeId;
        this.productOfferingId = productOfferingId;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.checkSerial = checkSerial;
        this.stateId = stateId;
        this.shopName = shopName;
        this.shopCode = shopCode;
    }

    public Long getTotalSaleQuantity() {
        Long totalQuantity = 0L;
        if (lstGoods != null) {
            for (SubGoodsDTO lstGood : lstGoods) {
                totalQuantity += DataUtil.safeToLong(lstGood.getQuantity());
            }
        }
        return totalQuantity;
    }

    public String getShopCodeName() {
        if (!DataUtil.isNullOrEmpty(this.shopCode) && !DataUtil.isNullOrEmpty(this.shopName)) {
            return this.shopCode.trim() + "_" + this.shopName;
        }
        return "";
    }

    public boolean isShowInfo() {
        Long tmpProdOfferTypeId = DataUtil.safeToLong(productOfferTypeId);
        return (Const.STOCK_TYPE.STOCK_HANDSET.equals(DataUtil.safeToLong(tmpProdOfferTypeId))
                || Const.STOCK_TYPE.STOCK_ACCESSORIES.equals(tmpProdOfferTypeId)
                || Const.STOCK_TYPE.STOCK_NO_SERIAL.equals(tmpProdOfferTypeId))
                && Const.OWNER_TYPE.SHOP_LONG.equals(DataUtil.safeToLong(this.ownerType));
    }

    public Long getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(Long realQuantity) {
        this.realQuantity = realQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public boolean isExport() {
        return isExport;
    }

    public void setIsExport(boolean isExport) {
        this.isExport = isExport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getRequirementQuanlity() {
        return requirementQuanlity;
    }

    public void setRequirementQuanlity(Long requirementQuanlity) {
        this.requirementQuanlity = requirementQuanlity;
    }

    public Long getAvailableQuanlity() {
        return availableQuanlity;
    }

    public void setAvailableQuanlity(Long availableQuanlity) {
        this.availableQuanlity = availableQuanlity;
    }

    public Long getCurrentQuanlity() {
        return currentQuanlity;
    }

    public void setCurrentQuanlity(Long currentQuanlity) {
        this.currentQuanlity = currentQuanlity;
    }

    public String getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(String productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public String getProductOfferingId() {
        return productOfferingId;
    }

    public void setProductOfferingId(String productOfferingId) {
        this.productOfferingId = productOfferingId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCheckSerial() {
        return checkSerial;
    }

    public void setCheckSerial(String checkSerial) {
        this.checkSerial = checkSerial;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public List<SubGoodsDTO> getLstGoods() {
        return lstGoods;
    }

    public void setLstGoods(List<SubGoodsDTO> lstGoods) {
        this.lstGoods = lstGoods;
    }

    public String getAccountingModelCode() {
        return accountingModelCode;
    }

    public void setAccountingModelCode(String accountingModelCode) {
        this.accountingModelCode = accountingModelCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
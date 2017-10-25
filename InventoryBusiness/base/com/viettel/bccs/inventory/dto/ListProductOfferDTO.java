package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.Const.GOODS_STATE;
import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class ListProductOfferDTO extends BaseDTO implements Serializable {

    private Long stateId;
    private String strStateId;
    private ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
    private ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
    private String number;
    private List<ProductOfferTypeDTO> lsProductOfferTypeDTO = Lists.newArrayList();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO = Lists.newArrayList();
    //tuydv1
    private List<ProductOfferingDTO> lsProductOfferingDTO = Lists.newArrayList();
    private ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
    private String unit;
    private Long avaiableQuantity;
    private Long quantity;
    private Long totalQuantity;
    private String strStateIdAfter;
    private List<StockTransSerialDTO> lstSerial;
    private List<Long> lsProdOfferTypeIds = Lists.newArrayList();
    private boolean disAbleRemove;
    private String inputSearch;
    private ProductOfferingTotalDTO productOfferingTotalDTONew = new ProductOfferingTotalDTO();
    private List<ProductOfferingTotalDTO> lsProductOfferingTotalDTONew = Lists.newArrayList();
    private List<StockDeviceTransferDTO> lstStockDeviceTransfer = Lists.newArrayList();

    /**
     * ham tra ve list serial neu co data
     *
     * @return
     * @author ThanhNT
     */
    public boolean getHaveListSerial() {
        return lstSerial != null && lstSerial.size() > 0;
    }

    public Long getTotalPriceAmount() {
        Long result = 0L;
        if (!DataUtil.isNullOrEmpty(number) && DataUtil.safeToLong(number) > 0L
                && productOfferingTotalDTO.getPriceAmount() != null
                && productOfferingTotalDTO.getPriceAmount() > 0L
                && Const.GOODS_STATE.NEW.equals(productOfferingTotalDTO.getStateId())) {
            return productOfferingTotalDTO.getPriceAmount() * DataUtil.safeToLong(number);
        }
        return result;
    }

    public Long getTotalPriceAmountNoState() {
        Long result = 0L;
        if (!DataUtil.isNullOrEmpty(number) && DataUtil.safeToLong(number) > 0L
                && productOfferingTotalDTO.getPriceAmount() != null
                && productOfferingTotalDTO.getPriceAmount() > 0L) {
            return productOfferingTotalDTO.getPriceAmount() * DataUtil.safeToLong(number);
        }
        return result;
    }

    public StockTransDetailDTO toStockTransDetailDTO() {
        return new StockTransDetailDTO(productOfferingTotalDTO == null ? null : DataUtil.safeToLong(productOfferingTotalDTO.getProductOfferingId()), stateId);
    }

    public ListProductOfferDTO() {
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStrStateId() {
        return strStateId;
    }

    public void setStrStateId(String strStateId) {
        this.stateId = DataUtil.safeToLong(strStateId);
        this.strStateId = strStateId;
    }

    public ProductOfferTypeDTO getProductOfferTypeDTO() {
        return productOfferTypeDTO;
    }

    public void setProductOfferTypeDTO(ProductOfferTypeDTO productOfferTypeDTO) {
        this.productOfferTypeDTO = productOfferTypeDTO;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public List<ProductOfferTypeDTO> getLsProductOfferTypeDTO() {
        return lsProductOfferTypeDTO;
    }

    public void setLsProductOfferTypeDTO(List<ProductOfferTypeDTO> lsProductOfferTypeDTO) {
        this.lsProductOfferTypeDTO = lsProductOfferTypeDTO;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTO() {
        return lsProductOfferingTotalDTO;
    }

    public void setLsProductOfferingTotalDTO(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTO) {
        this.lsProductOfferingTotalDTO = lsProductOfferingTotalDTO;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<ProductOfferingDTO> getLsProductOfferingDTO() {
        return lsProductOfferingDTO;
    }

    public void setLsProductOfferingDTO(List<ProductOfferingDTO> lsProductOfferingDTO) {
        this.lsProductOfferingDTO = lsProductOfferingDTO;
    }

    public ProductOfferingDTO getProductOfferingDTO() {
        return productOfferingDTO;
    }

    public void setProductOfferingDTO(ProductOfferingDTO productOfferingDTO) {
        this.productOfferingDTO = productOfferingDTO;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getAvaiableQuantity() {
        return avaiableQuantity;
    }

    public void setAvaiableQuantity(Long avaiableQuantity) {
        this.avaiableQuantity = avaiableQuantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getStrStateIdAfter() {
        return strStateIdAfter;
    }

    public void setStrStateIdAfter(String strStateIdAfter) {
        this.strStateIdAfter = strStateIdAfter;
    }

    public List<StockTransSerialDTO> getLstSerial() {
        return lstSerial;
    }

    public void setLstSerial(List<StockTransSerialDTO> lstSerial) {
        this.lstSerial = lstSerial;
    }

    public List<Long> getLsProdOfferTypeIds() {
        return lsProdOfferTypeIds;
    }

    public void setLsProdOfferTypeIds(List<Long> lsProdOfferTypeIds) {
        this.lsProdOfferTypeIds = lsProdOfferTypeIds;
    }

    public boolean isDisAbleRemove() {
        return disAbleRemove;
    }

    public void setDisAbleRemove(boolean disAbleRemove) {
        this.disAbleRemove = disAbleRemove;
    }

    public String getInputSearch() {
        return inputSearch;
    }

    public void setInputSearch(String inputSearch) {
        this.inputSearch = inputSearch;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTONew() {
        return productOfferingTotalDTONew;
    }

    public void setProductOfferingTotalDTONew(ProductOfferingTotalDTO productOfferingTotalDTONew) {
        this.productOfferingTotalDTONew = productOfferingTotalDTONew;
    }

    public List<ProductOfferingTotalDTO> getLsProductOfferingTotalDTONew() {
        return lsProductOfferingTotalDTONew;
    }

    public void setLsProductOfferingTotalDTONew(List<ProductOfferingTotalDTO> lsProductOfferingTotalDTONew) {
        this.lsProductOfferingTotalDTONew = lsProductOfferingTotalDTONew;
    }

    public List<StockDeviceTransferDTO> getLstStockDeviceTransfer() {
        return lstStockDeviceTransfer;
    }

    public void setLstStockDeviceTransfer(List<StockDeviceTransferDTO> lstStockDeviceTransfer) {
        this.lstStockDeviceTransfer = lstStockDeviceTransfer;
    }
}

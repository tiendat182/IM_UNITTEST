package com.viettel.bccs.inventory.message;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viettel.bccs.inventory.dto.DiscountDTO;
import com.viettel.bccs.inventory.dto.SaleTransInfoDTO;
import com.viettel.bccs.inventory.dto.StockSaleInfo;

import java.util.List;

/**
 * Created by DungPT16 on 1/15/2016.
 */
public class AddGoodSaleMessage {
    private String description;
    private String errorCode;
    private String keyMsg;
    private Boolean success;
    private SaleTransInfoDTO saleTransInfo;
    @XStreamImplicit(itemFieldName = "stockSaleInfoLst")
    private List<StockSaleInfo> stockSaleInfoLst;
    @XStreamImplicit(itemFieldName = "discountInfoLst")
    private List<DiscountDTO> discountInfoLst;

    public SaleTransInfoDTO getSaleTransInfo() {
        return saleTransInfo;
    }

    public void setSaleTransInfo(SaleTransInfoDTO saleTransInfo) {
        this.saleTransInfo = saleTransInfo;
    }

    public List<StockSaleInfo> getStockSaleInfoLst() {
        return stockSaleInfoLst;
    }

    public void setStockSaleInfoLst(List<StockSaleInfo> stockSaleInfoLst) {
        this.stockSaleInfoLst = stockSaleInfoLst;
    }

    public List<DiscountDTO> getDiscountInfoLst() {
        return discountInfoLst;
    }

    public void setDiscountInfoLst(List<DiscountDTO> discountInfoLst) {
        this.discountInfoLst = discountInfoLst;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getKeyMsg() {
        return keyMsg;
    }

    public void setKeyMsg(String keyMsg) {
        this.keyMsg = keyMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

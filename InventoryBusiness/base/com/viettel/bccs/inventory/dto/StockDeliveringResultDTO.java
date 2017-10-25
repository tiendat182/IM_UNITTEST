package com.viettel.bccs.inventory.dto;

import java.util.List;

/**
 * Created by hoangnt14 on 7/22/2016.
 */
public class StockDeliveringResultDTO {
    private String responseCode;
    private String description;
    private List<StockDeliveringBillDetailDTO> lstStockDeliveringBills;

    public StockDeliveringResultDTO(String responseCode, String description, List<StockDeliveringBillDetailDTO> lstStockDeliveringBills) {
        this.responseCode = responseCode;
        this.description = description;
        this.lstStockDeliveringBills = lstStockDeliveringBills;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StockDeliveringBillDetailDTO> getLstStockDeliveringBills() {
        return lstStockDeliveringBills;
    }

    public void setLstStockDeliveringBills(List<StockDeliveringBillDetailDTO> lstStockDeliveringBills) {
        this.lstStockDeliveringBills = lstStockDeliveringBills;
    }
}

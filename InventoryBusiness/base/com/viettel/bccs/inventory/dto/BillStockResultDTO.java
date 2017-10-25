package com.viettel.bccs.inventory.dto;

import java.util.List;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
public class BillStockResultDTO {
    private List<BillStockDTO> lstBillStockDTO;
    private String synTransCode;
    private String responseCode;
    private String description;

    public BillStockResultDTO(String responseCode, String description) {
        this.responseCode = responseCode;
        this.description = description;
    }

    public BillStockResultDTO(String responseCode, String description, List<BillStockDTO> lstBillStockDTO) {
        this.responseCode = responseCode;
        this.description = description;
        this.lstBillStockDTO = lstBillStockDTO;
    }

    public BillStockResultDTO(String responseCode, String description, List<BillStockDTO> lstBillStockDTO, String synTransCode) {
        this.responseCode = responseCode;
        this.description = description;
        this.lstBillStockDTO = lstBillStockDTO;
        this.synTransCode = synTransCode;
    }


    public BillStockResultDTO() {
    }

    public List<BillStockDTO> getLstBillStock() {
        return lstBillStockDTO;
    }

    public void setLstBillStock(List<BillStockDTO> lstBillStockDTO) {
        this.lstBillStockDTO = lstBillStockDTO;
    }

    public String getSynTransCode() {
        return synTransCode;
    }

    public void setSynTransCode(String synTransCode) {
        this.synTransCode = synTransCode;
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
}

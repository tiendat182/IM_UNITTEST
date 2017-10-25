package com.viettel.bccs.inventory.dto;

import java.util.List;

/**
 * Created by hoangnt14 on 7/22/2016.
 */
public class StockDeliveringBillDetailDTO {
    private Long stockTransId;
    private Long id;
    private String deliveryNote;
    private String deliveryCommand;
    private String deliveryDate;
    private String stockCode;
    private String stockName;
    private String stockAddress;
    private String receiptCode;
    private String receiptName;
    private String receiptAddress;
    private String transportType;
    private String transportSource;
    private String staffExport;
    private String staffImport;
    private List<ProductInfoDTO> lstProductInfos;

    public List<ProductInfoDTO> getLstProductInfos() {
        return lstProductInfos;
    }

    public void setLstProductInfos(List<ProductInfoDTO> lstProductInfos) {
        this.lstProductInfos = lstProductInfos;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getDeliveryCommand() {
        return deliveryCommand;
    }

    public void setDeliveryCommand(String deliveryCommand) {
        this.deliveryCommand = deliveryCommand;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockAddress() {
        return stockAddress;
    }

    public void setStockAddress(String stockAddress) {
        this.stockAddress = stockAddress;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportSource() {
        return transportSource;
    }

    public void setTransportSource(String transportSource) {
        this.transportSource = transportSource;
    }

    public String getStaffExport() {
        return staffExport;
    }

    public void setStaffExport(String staffExport) {
        this.staffExport = staffExport;
    }

    public String getStaffImport() {
        return staffImport;
    }

    public void setStaffImport(String staffImport) {
        this.staffImport = staffImport;
    }
}

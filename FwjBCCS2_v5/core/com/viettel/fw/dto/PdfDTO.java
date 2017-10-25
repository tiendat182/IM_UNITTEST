package com.viettel.fw.dto;

/**
 * Created by thiendn1 on 18/1/2016.
 */
public class PdfDTO {
    private Long invoiceUsedId;
    private String fileName;

    byte[] pdfBytes; // file excel di cung hoa don bang ke
    byte[] excelBytes; // file excel di cung hoa don bang ke

    private boolean removeAble = false;

    public Long getInvoiceUsedId() {
        return invoiceUsedId;
    }

    public void setInvoiceUsedId(Long invoiceUsedId) {
        this.invoiceUsedId = invoiceUsedId;
    }

    public byte[] getPdfBytes() {
        return pdfBytes;
    }

    public void setPdfBytes(byte[] pdfBytes) {
        this.pdfBytes = pdfBytes;
    }

    public byte[] getExcelBytes() {
        return excelBytes;
    }

    public void setExcelBytes(byte[] excelBytes) {
        this.excelBytes = excelBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isRemoveAble() {
        return removeAble;
    }

    public void setRemoveAble(boolean removeAble) {
        this.removeAble = removeAble;
    }
}

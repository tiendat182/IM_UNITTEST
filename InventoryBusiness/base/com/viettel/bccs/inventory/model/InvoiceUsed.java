package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by pham on 9/26/2016.
 */
@Entity
@Table(name = "INVOICE_USED")
public class InvoiceUsed implements Serializable {
    public static enum COLUMNS {SERIALNO, INVOICENO, INVOICEDATE, INVOICEUSEDID}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INVOICE_USED_ID")
    private Long invoiceUsedId;
    @Column(name = "SERIAL_NO")
    private String serialNo;
    @Column(name = "INVOICE_NO")
    private String invoiceNo;
    @Column(name = "INVOICE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDate;

    public InvoiceUsed() {
    }


    public Long getInvoiceUsedId() {
        return invoiceUsedId;
    }

    public void setInvoiceUsedId(Long invoiceUsedId) {
        this.invoiceUsedId = invoiceUsedId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceUsedId != null ? invoiceUsedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceUsed)) {
            return false;
        }
        InvoiceUsed other = (InvoiceUsed) object;
        if ((this.invoiceUsedId == null && other.invoiceUsedId != null) || (this.invoiceUsedId != null && !this.invoiceUsedId.equals(other.invoiceUsedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.InvoiceUsed[ invoiceUsedId=" + invoiceUsedId + " ]";
    }

}

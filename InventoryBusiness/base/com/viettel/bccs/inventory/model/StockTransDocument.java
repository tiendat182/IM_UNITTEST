/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThanhNT77
 */
@Entity
@Table(name = "STOCK_TRANS_DOCUMENT")
@XmlRootElement
public class StockTransDocument implements Serializable {
public static enum COLUMNS {FILEATTACH, STOCKTRANSDOCUMENTID, STOCKTRANSID, TRANSTYPE, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_TRANS_DOCUMENT_ID")
    @SequenceGenerator(name = "STOCK_TRANS_DOCUMENT_ID_GENERATOR", sequenceName = "STOCK_TRANS_DOCUMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TRANS_DOCUMENT_ID_GENERATOR")
    private Long stockTransDocumentId;
    @Column(name = "TRANS_TYPE")
    private Long transType;
    @Column(name = "STOCK_TRANS_ID")
    private Long stockTransId;
    @Lob
    @Column(name = "FILE_ATTACH")
    private byte[] fileAttach;

    public StockTransDocument() {
    }

    public StockTransDocument(Long stockTransDocumentId) {
        this.stockTransDocumentId = stockTransDocumentId;
    }

    public Long getStockTransDocumentId() {
        return stockTransDocumentId;
    }

    public void setStockTransDocumentId(Long stockTransDocumentId) {
        this.stockTransDocumentId = stockTransDocumentId;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }

    public Long getStockTransId() {
        return stockTransId;
    }

    public void setStockTransId(Long stockTransId) {
        this.stockTransId = stockTransId;
    }

    public byte[] getFileAttach() {
        return fileAttach;
    }

    public void setFileAttach(byte[] fileAttach) {
        this.fileAttach = fileAttach;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockTransDocumentId != null ? stockTransDocumentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockTransDocument)) {
            return false;
        }
        StockTransDocument other = (StockTransDocument) object;
        if ((this.stockTransDocumentId == null && other.stockTransDocumentId != null) || (this.stockTransDocumentId != null && !this.stockTransDocumentId.equals(other.stockTransDocumentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.StockTransDocument[ stockTransDocumentId=" + stockTransDocumentId + " ]";
    }
    
}

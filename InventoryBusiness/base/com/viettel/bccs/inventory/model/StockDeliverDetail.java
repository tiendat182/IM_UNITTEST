package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 1/23/2017.
 */

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_DELIVER_DETAIL")
@NamedQueries({
        @NamedQuery(name = "StockDeliverDetail.findAll", query = "SELECT s FROM StockDeliverDetail s")})
public class StockDeliverDetail implements Serializable {
    public static enum COLUMNS {CREATEDATE, NOTE, PRODOFFERID, PRODOFFERTYPEID, QUANTITYREAL, QUANTITYSYS, STATEID, STOCKDELIVERDETAILID, STOCKDELIVERID, EXCLUSE_ID_LIST}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_DELIVER_DETAIL_ID")
    @SequenceGenerator(name = "STOCK_DELIVER_DETAIL_ID_GENERATOR", sequenceName = "STOCK_DELIVER_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_DELIVER_DETAIL_ID_GENERATOR")
    private Long stockDeliverDetailId;
    @Basic(optional = false)
    @Column(name = "STOCK_DELIVER_ID")
    private Long stockDeliverId;
    @Column(name = "PROD_OFFER_TYPE_ID")
    private Long prodOfferTypeId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "QUANTITY_SYS")
    private Long quantitySys;
    @Column(name = "QUANTITY_REAL")
    private Long quantityReal;
    @Column(name = "NOTE")
    private String note;

    public StockDeliverDetail() {
    }

    public StockDeliverDetail(Long stockDeliverDetailId) {
        this.stockDeliverDetailId = stockDeliverDetailId;
    }

    public StockDeliverDetail(Long stockDeliverDetailId, Long stockDeliverId) {
        this.stockDeliverDetailId = stockDeliverDetailId;
        this.stockDeliverId = stockDeliverId;
    }

    public Long getStockDeliverDetailId() {
        return stockDeliverDetailId;
    }

    public void setStockDeliverDetailId(Long stockDeliverDetailId) {
        this.stockDeliverDetailId = stockDeliverDetailId;
    }

    public Long getStockDeliverId() {
        return stockDeliverId;
    }

    public void setStockDeliverId(Long stockDeliverId) {
        this.stockDeliverId = stockDeliverId;
    }

    public Long getProdOfferTypeId() {
        return prodOfferTypeId;
    }

    public void setProdOfferTypeId(Long prodOfferTypeId) {
        this.prodOfferTypeId = prodOfferTypeId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getQuantitySys() {
        return quantitySys;
    }

    public void setQuantitySys(Long quantitySys) {
        this.quantitySys = quantitySys;
    }

    public Long getQuantityReal() {
        return quantityReal;
    }

    public void setQuantityReal(Long quantityReal) {
        this.quantityReal = quantityReal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockDeliverDetailId != null ? stockDeliverDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDeliverDetail)) {
            return false;
        }
        StockDeliverDetail other = (StockDeliverDetail) object;
        if ((this.stockDeliverDetailId == null && other.stockDeliverDetailId != null) || (this.stockDeliverDetailId != null && !this.stockDeliverDetailId.equals(other.stockDeliverDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai2.StockDeliverDetail[ stockDeliverDetailId=" + stockDeliverDetailId + " ]";
    }

}

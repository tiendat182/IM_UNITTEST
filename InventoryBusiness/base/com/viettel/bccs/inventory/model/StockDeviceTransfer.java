package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by vanho on 12/04/2017.
 */
@Entity
@Table(name = "STOCK_DEVICE_TRANSFER", schema = "BCCS_IM", catalog = "")
public class StockDeviceTransfer {
    private Long id;
    private Long prodOfferId;
    private Short stateId;
    private Long newProdOfferId;
    private Short newStateId;
    private Date createDate;
    private String createUser;
    private Long stockRequestId;

    public static enum COLUMNS {CREATEDATE, CREATEUSER, ID, NEWPRODOFFERID, NEWSTATEID, PRODOFFERID, STATEID, STOCKREQUESTID, EXCLUSE_ID_LIST};

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "STOCK_DEVICE_TRANSFER_SEQ", sequenceName = "STOCK_DEVICE_TRANSFER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_DEVICE_TRANSFER_SEQ")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PROD_OFFER_ID")
    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
    }

    @Basic
    @Column(name = "STATE_ID")
    public Short getStateId() {
        return stateId;
    }

    public void setStateId(Short stateId) {
        this.stateId = stateId;
    }

    @Basic
    @Column(name = "NEW_PROD_OFFER_ID")
    public Long getNewProdOfferId() {
        return newProdOfferId;
    }

    public void setNewProdOfferId(Long newProdOfferId) {
        this.newProdOfferId = newProdOfferId;
    }

    @Basic
    @Column(name = "NEW_STATE_ID")
    public Short getNewStateId() {
        return newStateId;
    }

    public void setNewStateId(Short newStateId) {
        this.newStateId = newStateId;
    }

    @Basic
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "CREATE_USER")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Basic
    @Column(name = "STOCK_REQUEST_ID")
    public Long getStockRequestId() {
        return stockRequestId;
    }

    public void setStockRequestId(Long stockRequestId) {
        this.stockRequestId = stockRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockDeviceTransfer that = (StockDeviceTransfer) o;

        if (id != that.id) return false;
        if (prodOfferId != that.prodOfferId) return false;
        if (stockRequestId != that.stockRequestId) return false;
        if (stateId != null ? !stateId.equals(that.stateId) : that.stateId != null) return false;
        if (newProdOfferId != null ? !newProdOfferId.equals(that.newProdOfferId) : that.newProdOfferId != null)
            return false;
        if (newStateId != null ? !newStateId.equals(that.newStateId) : that.newStateId != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createUser != null ? !createUser.equals(that.createUser) : that.createUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Long result = id;
        result = 31 * result + prodOfferId;
        result = 31 * result + (stateId != null ? stateId.hashCode() : 0);
        result = 31 * result + (newProdOfferId != null ? newProdOfferId.hashCode() : 0);
        result = 31 * result + (newStateId != null ? newStateId.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createUser != null ? createUser.hashCode() : 0);
        result = 31 * result + stockRequestId;
        return Integer.valueOf(result.toString());
    }
}

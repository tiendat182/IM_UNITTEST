package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 1/11/2016.
 */

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "DEPOSIT_DETAIL")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "DepositDetail.findAll", query = "SELECT d FROM DepositDetail d")})
public class DepositDetail implements Serializable {
    public static enum COLUMNS {AMOUNT, CREATEDATE, DEPOSITDETAILID, DEPOSITID, DEPOSITTYPE, EXPIREDDATE, POSID, PRICE, PRICEID, QUANTITY, SERIAL, STOCKMODELID, SUPPLYMONTH, SUPPLYPROGRAM, EXCLUSE_ID_LIST}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DEPOSIT_DETAIL_ID")
    @SequenceGenerator(name = "DEPOSIT_DETAIL_ID_GENERATOR", sequenceName = "DEPOSIT_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPOSIT_DETAIL_ID_GENERATOR")
    private Long depositDetailId;
    @Basic(optional = false)
    @Column(name = "DEPOSIT_ID")
    private Long depositId;
    @Basic(optional = false)
    @Column(name = "STOCK_MODEL_ID")
    private Long stockModelId;
    @Column(name = "DEPOSIT_TYPE")
    private String depositType;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Basic(optional = false)
    @Column(name = "PRICE_ID")
    private Long priceId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "EXPIRED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;
    @Column(name = "SUPPLY_MONTH")
    private Long supplyMonth;
    @Column(name = "SUPPLY_PROGRAM")
    private String supplyProgram;
    @Column(name = "SERIAL")
    private String serial;
    @Column(name = "POS_ID")
    private Long posId;

    public DepositDetail() {
    }

    public DepositDetail(Long depositDetailId) {
        this.depositDetailId = depositDetailId;
    }

    public DepositDetail(Long depositDetailId, Long depositId, Long stockModelId, Long priceId) {
        this.depositDetailId = depositDetailId;
        this.depositId = depositId;
        this.stockModelId = stockModelId;
        this.priceId = priceId;
    }

    public Long getDepositDetailId() {
        return depositDetailId;
    }

    public void setDepositDetailId(Long depositDetailId) {
        this.depositDetailId = depositDetailId;
    }

    public Long getDepositId() {
        return depositId;
    }

    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Long getSupplyMonth() {
        return supplyMonth;
    }

    public void setSupplyMonth(Long supplyMonth) {
        this.supplyMonth = supplyMonth;
    }

    public String getSupplyProgram() {
        return supplyProgram;
    }

    public void setSupplyProgram(String supplyProgram) {
        this.supplyProgram = supplyProgram;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depositDetailId != null ? depositDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepositDetail)) {
            return false;
        }
        DepositDetail other = (DepositDetail) object;
        if ((this.depositDetailId == null && other.depositDetailId != null) || (this.depositDetailId != null && !this.depositDetailId.equals(other.depositDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.DepositDetail[ depositDetailId=" + depositDetailId + " ]";
    }

}


package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hellpoethero on 07/09/2016.
 */
@Entity
@Table(name = "KCS_LOG_DETAIL")
@NamedQueries({
        @NamedQuery(name = "KCSLogDetail.findAll", query = "SELECT i FROM KcsLogDetail i")})
public class KcsLogDetail implements Serializable {
    public static enum COLUMNS {KCSLOGDETAILID, KCSLOGID, CREATE_DATE, STOCKMODELID, QUANTITY, STATEID, STATUS}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "KCS_LOG_DETAIL_ID")
    @SequenceGenerator(name = "KCS_LOG_DETAIL_ID_GENERATOR", sequenceName = "KCS_LOG_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KCS_LOG_DETAIL_ID_GENERATOR")
    private Long kcsLogDetailId;
    @Column(name = "KCS_LOG_ID")
    private Long kcsLogId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STOCK_MODEL_ID")
    private Long stockModelId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "STATUS")
    private Long status;

    public KcsLogDetail() {
    }

    public Long getKcsLogDetailId() {
        return kcsLogDetailId;
    }

    public void setKcsLogDetailId(Long kcsLogDetailId) {
        this.kcsLogDetailId = kcsLogDetailId;
    }

    public Long getKcsLogId() {
        return kcsLogId;
    }

    public void setKcsLogId(Long kcsLogId) {
        this.kcsLogId = kcsLogId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStockModelId() {
        return stockModelId;
    }

    public void setStockModelId(Long stockModelId) {
        this.stockModelId = stockModelId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kcsLogDetailId != null ? kcsLogDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KcsLogDetail)) {
            return false;
        }
        KcsLogDetail other = (KcsLogDetail) object;
        if ((this.kcsLogDetailId == null && other.kcsLogDetailId != null) || (this.kcsLogDetailId != null && !this.kcsLogDetailId.equals(other.kcsLogDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.KCSLogDetail[ KCSLogDetailId=" + kcsLogDetailId + " ]";
    }
}

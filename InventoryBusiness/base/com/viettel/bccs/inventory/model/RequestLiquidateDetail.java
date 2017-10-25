/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tuannm33
 */
@Entity
@Table(name = "REQUEST_LIQUIDATE_DETAIL")
public class RequestLiquidateDetail implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, PRODOFFERID, QUANTITY, REQUESTLIQUIDATEDETAILID, REQUESTLIQUIDATEID, STATEID, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_LIQUIDATE_DETAIL_ID")
    @SequenceGenerator(name = "REQUEST_LIQUIDATE_DETAIL_ID_GENERATOR", sequenceName = "REQUEST_LIQUIDATE_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_LIQUIDATE_DETAIL_ID_GENERATOR")
    private Long requestLiquidateDetailId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "REQUEST_LIQUIDATE_ID")
    private Long requestLiquidateId;

    public RequestLiquidateDetail() {
    }

    public RequestLiquidateDetail(Long requestLiquidateDetailId) {
        this.requestLiquidateDetailId = requestLiquidateDetailId;
    }

    public Long getRequestLiquidateDetailId() {
        return requestLiquidateDetailId;
    }

    public void setRequestLiquidateDetailId(Long requestLiquidateDetailId) {
        this.requestLiquidateDetailId = requestLiquidateDetailId;
    }

    public Long getProdOfferId() {
        return prodOfferId;
    }

    public void setProdOfferId(Long prodOfferId) {
        this.prodOfferId = prodOfferId;
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

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getRequestLiquidateId() {
        return requestLiquidateId;
    }

    public void setRequestLiquidateId(Long requestLiquidateId) {
        this.requestLiquidateId = requestLiquidateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestLiquidateDetailId != null ? requestLiquidateDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestLiquidateDetail)) {
            return false;
        }
        RequestLiquidateDetail other = (RequestLiquidateDetail) object;
        if ((this.requestLiquidateDetailId == null && other.requestLiquidateDetailId != null) || (this.requestLiquidateDetailId != null && !this.requestLiquidateDetailId.equals(other.requestLiquidateDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RequestLiquidateDetail[ requestLiquidateDetailId=" + requestLiquidateDetailId + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.bccs.inventory.model;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author tuannm33
 */
@Entity
@Table(name = "REQUEST_LIQUIDATE_SERIAL")
public class RequestLiquidateSerial implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, FROMSERIAL, QUANTITY, REQUESTLIQUIDATEID, REQUESTLIQUIDATESERIALID, REQUESTLIQUIDATEDETAILID, TOSERIAL, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "REQUEST_LIQUIDATE_SERIAL_ID")
    @SequenceGenerator(name = "REQUEST_LIQUIDATE_SERIAL_ID_GENERATOR", sequenceName = "REQUEST_LIQUIDATE_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_LIQUIDATE_SERIAL_ID_GENERATOR")
    private Long requestLiquidateSerialId;
    @Column(name = "REQUEST_LIQUIDATE_ID")
    private Long requestLiquidateId;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "REQUEST_LIQUIDATE_DETAIL_ID")
    private Long requestLiquidateDetailId;

    public RequestLiquidateSerial() {
    }

    public RequestLiquidateSerial(Long requestLiquidateSerialId) {
        this.requestLiquidateSerialId = requestLiquidateSerialId;
    }

    public Long getRequestLiquidateSerialId() {
        return requestLiquidateSerialId;
    }

    public void setRequestLiquidateSerialId(Long requestLiquidateSerialId) {
        this.requestLiquidateSerialId = requestLiquidateSerialId;
    }

    public Long getRequestLiquidateId() {
        return requestLiquidateId;
    }

    public void setRequestLiquidateId(Long requestLiquidateId) {
        this.requestLiquidateId = requestLiquidateId;
    }

    public String getFromSerial() {
        return fromSerial;
    }

    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    public String getToSerial() {
        return toSerial;
    }

    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getRequestLiquidateDetailId() {
        return requestLiquidateDetailId;
    }

    public void setRequestLiquidateDetailId(Long requestLiquidateDetailId) {
        this.requestLiquidateDetailId = requestLiquidateDetailId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestLiquidateSerialId != null ? requestLiquidateSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestLiquidateSerial)) {
            return false;
        }
        RequestLiquidateSerial other = (RequestLiquidateSerial) object;
        if ((this.requestLiquidateSerialId == null && other.requestLiquidateSerialId != null) || (this.requestLiquidateSerialId != null && !this.requestLiquidateSerialId.equals(other.requestLiquidateSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.RequestLiquidateSerial[ requestLiquidateSerialId=" + requestLiquidateSerialId + " ]";
    }

}

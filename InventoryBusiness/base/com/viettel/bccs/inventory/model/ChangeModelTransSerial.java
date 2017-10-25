package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "CHANGE_MODEL_TRANS_SERIAL")
@XmlRootElement
public class ChangeModelTransSerial implements Serializable {
    public static enum COLUMNS {CHANGEMODELTRANSDETAILID, CHANGEMODELTRANSSERIALID, CREATEDATE, FROMSERIAL, QUANTITY, TOSERIAL, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CHANGE_MODEL_TRANS_SERIAL_ID")
    @SequenceGenerator(name = "CHANGE_MODEL_TRANS_SERIAL_ID_GENERATOR", sequenceName = "CHANGE_MODEL_TRANS_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_MODEL_TRANS_SERIAL_ID_GENERATOR")
    private Long changeModelTransSerialId;
    @Column(name = "CHANGE_MODEL_TRANS_DETAIL_ID")
    private Long changeModelTransDetailId;
    @Column(name = "FROM_SERIAL")
    private String fromSerial;
    @Column(name = "TO_SERIAL")
    private String toSerial;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public ChangeModelTransSerial() {
    }

    public ChangeModelTransSerial(Long changeModelTransSerialId) {
        this.changeModelTransSerialId = changeModelTransSerialId;
    }

    public Long getChangeModelTransSerialId() {
        return changeModelTransSerialId;
    }

    public void setChangeModelTransSerialId(Long changeModelTransSerialId) {
        this.changeModelTransSerialId = changeModelTransSerialId;
    }

    public Long getChangeModelTransDetailId() {
        return changeModelTransDetailId;
    }

    public void setChangeModelTransDetailId(Long changeModelTransDetailId) {
        this.changeModelTransDetailId = changeModelTransDetailId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (changeModelTransSerialId != null ? changeModelTransSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChangeModelTransSerial)) {
            return false;
        }
        ChangeModelTransSerial other = (ChangeModelTransSerial) object;
        if ((this.changeModelTransSerialId == null && other.changeModelTransSerialId != null) || (this.changeModelTransSerialId != null && !this.changeModelTransSerialId.equals(other.changeModelTransSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.ChangeModelTransSerial[ changeModelTransSerialId=" + changeModelTransSerialId + " ]";
    }

}

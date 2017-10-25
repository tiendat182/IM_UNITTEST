package com.viettel.bccs.inventory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hoangnt14
 */
@Entity
@Table(name = "CHANGE_MODEL_TRANS_DETAIL")
@XmlRootElement
public class ChangeModelTransDetail implements Serializable {
public static enum COLUMNS {CHANGEMODELTRANSDETAILID, CHANGEMODELTRANSID, CREATEDATE, NEWPRODOFFERID, NOTE, OLDPRODOFFERID, QUANTITY, STATEID, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CHANGE_MODEL_TRANS_DETAIL_ID")
    @SequenceGenerator(name = "CHANGE_MODEL_TRANS_DETAIL_ID_GENERATOR", sequenceName = "CHANGE_MODEL_TRANS_DETAIL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHANGE_MODEL_TRANS_DETAIL_ID_GENERATOR")
    private Long changeModelTransDetailId;
    @Column(name = "CHANGE_MODEL_TRANS_ID")
    private Long changeModelTransId;
    @Column(name = "OLD_PROD_OFFER_ID")
    private Long oldProdOfferId;
    @Column(name = "NEW_PROD_OFFER_ID")
    private Long newProdOfferId;
    @Column(name = "STATE_ID")
    private Long stateId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "NOTE")
    private String note;

    public ChangeModelTransDetail() {
    }

    public ChangeModelTransDetail(Long changeModelTransDetailId) {
        this.changeModelTransDetailId = changeModelTransDetailId;
    }

    public Long getChangeModelTransDetailId() {
        return changeModelTransDetailId;
    }

    public void setChangeModelTransDetailId(Long changeModelTransDetailId) {
        this.changeModelTransDetailId = changeModelTransDetailId;
    }

    public Long getChangeModelTransId() {
        return changeModelTransId;
    }

    public void setChangeModelTransId(Long changeModelTransId) {
        this.changeModelTransId = changeModelTransId;
    }

    public Long getOldProdOfferId() {
        return oldProdOfferId;
    }

    public void setOldProdOfferId(Long oldProdOfferId) {
        this.oldProdOfferId = oldProdOfferId;
    }

    public Long getNewProdOfferId() {
        return newProdOfferId;
    }

    public void setNewProdOfferId(Long newProdOfferId) {
        this.newProdOfferId = newProdOfferId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (changeModelTransDetailId != null ? changeModelTransDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChangeModelTransDetail)) {
            return false;
        }
        ChangeModelTransDetail other = (ChangeModelTransDetail) object;
        if ((this.changeModelTransDetailId == null && other.changeModelTransDetailId != null) || (this.changeModelTransDetailId != null && !this.changeModelTransDetailId.equals(other.changeModelTransDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai1.ChangeModelTransDetail[ changeModelTransDetailId=" + changeModelTransDetailId + " ]";
    }

}

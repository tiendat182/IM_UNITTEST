package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hellpoethero on 07/09/2016.
 */
@Entity
@Table(name = "KCS_LOG")
public class KcsLog implements Serializable {
    public static enum COLUMNS {KCSLOGID, OWNERID, OWNERTYPE, CREATEDATE, STATUS};
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "KCS_LOG_ID")
    @SequenceGenerator(name = "KCS_LOG_ID_GENERATOR", sequenceName = "KCS_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KCS_LOG_ID_GENERATOR")
    private Long kcsLogId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "STATUS")
    private Long status;

    public KcsLog () {
    }
    public Long getKcsLogId() {
        return kcsLogId;
    }

    public void setKcsLogId(Long kcsLogId) {
        this.kcsLogId = kcsLogId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        hash += (kcsLogId != null ? kcsLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KcsLog)) {
            return false;
        }
        KcsLog other = (KcsLog) object;
        if ((this.kcsLogId == null && other.kcsLogId != null) || (this.kcsLogId != null && !this.kcsLogId.equals(other.kcsLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.KCSLog[ kcsLogId=" + kcsLogId + " ]";
    }
}

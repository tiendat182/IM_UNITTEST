package com.viettel.bccs.inventory.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hellpoethero on 07/09/2016.
 */
@Entity
@Table(name = "KCS_LOG_SERIAL")
@NamedQueries({
        @NamedQuery(name = "KCSLogSerial.findAll", query = "SELECT i FROM KcsLogSerial i")})
public class KcsLogSerial implements Serializable {
    public static enum COLUMNS {KCSLOGSERIALID, KCSLOGDETAILID, CREATE_DATE, SERIAL}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "KCS_LOG_SERIAL_ID")
    @SequenceGenerator(name = "KCS_LOG_SERIAL_ID_GENERATOR", sequenceName = "KCS_LOG_SERIAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KCS_LOG_SERIAL_ID_GENERATOR")
    private Long kcsLogSerialId;
    @Column(name = "KCS_LOG_DETAIL_ID")
    private Long kcsLogDetailId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "SERIAL")
    private String serial;

    public KcsLogSerial() {
    }

    public Long getKcsLogSerialId() {
        return kcsLogSerialId;
    }

    public void setKcsLogSerialId(Long kcsLogSerialId) {
        this.kcsLogSerialId = kcsLogSerialId;
    }

    public Long getKcsLogDetailId() {
        return kcsLogDetailId;
    }

    public void setKcsLogDetailId(Long kcsLogDetailId) {
        this.kcsLogDetailId = kcsLogDetailId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kcsLogSerialId != null ? kcsLogSerialId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KcsLogSerial)) {
            return false;
        }
        KcsLogSerial other = (KcsLogSerial) object;
        if ((this.kcsLogSerialId == null && other.kcsLogSerialId != null) || (this.kcsLogSerialId != null && !this.kcsLogSerialId.equals(other.kcsLogSerialId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.KCSLogSerial[ KCSLogSerialId=" + kcsLogSerialId + " ]";
    }
}

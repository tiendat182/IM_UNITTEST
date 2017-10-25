package com.viettel.bccs.inventory.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.*;

/**
 * Created by vanho on 29/05/2017.
 */
@Entity
@Table(name = "IMSI_INFO", schema = "BCCS_IM", catalog = "")
public class ImsiInfo {
	public static enum COLUMNS {CREATEDATE, DOCNO, ENDDATE, ID, IMSI, LASTUPDATETIME, LASTUPDATEUSER, STARTDATE, STATUS, USERCREATE, EXCLUSE_ID_LIST};

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name = "IMSI_INFO_SEQ_GEN", sequenceName = "IMSI_INFO_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMSI_INFO_SEQ_GEN")
    private Long id;
    @Column(name = "IMSI")
    private String imsi;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "DOC_NO")
    private String docNo;
    @Column(name = "USER_CREATE")
    private String userCreate;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;
    @Basic
    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImsiInfo imsiInfo = (ImsiInfo) o;

        if (id != imsiInfo.id) return false;
        if (imsi != null ? !imsi.equals(imsiInfo.imsi) : imsiInfo.imsi != null) return false;
        if (status != null ? !status.equals(imsiInfo.status) : imsiInfo.status != null) return false;
        if (startDate != null ? !startDate.equals(imsiInfo.startDate) : imsiInfo.startDate != null) return false;
        if (endDate != null ? !endDate.equals(imsiInfo.endDate) : imsiInfo.endDate != null) return false;
        if (docNo != null ? !docNo.equals(imsiInfo.docNo) : imsiInfo.docNo != null) return false;
        if (userCreate != null ? !userCreate.equals(imsiInfo.userCreate) : imsiInfo.userCreate != null) return false;
        if (createDate != null ? !createDate.equals(imsiInfo.createDate) : imsiInfo.createDate != null) return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(imsiInfo.lastUpdateTime) : imsiInfo.lastUpdateTime != null)
            return false;
        if (lastUpdateUser != null ? !lastUpdateUser.equals(imsiInfo.lastUpdateUser) : imsiInfo.lastUpdateUser != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (imsi != null ? imsi.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (docNo != null ? docNo.hashCode() : 0);
        result = 31 * result + (userCreate != null ? userCreate.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        result = 31 * result + (lastUpdateUser != null ? lastUpdateUser.hashCode() : 0);
        return result;
    }
}

package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * Created by vanho on 31/03/2017.
 */
@Entity
@Table(name = "DEVICE_CONFIG", schema = "BCCS_IM", catalog = "")
public class DeviceConfig {
    private Long id;
    private Long prodOfferId;
    private Long stateId;
    private Long newProdOfferId;
    private String status;
    private Date createDate;
    private String createUser;
    private Date updateDate;
    private String updateUser;

    public static enum COLUMNS {CREATEDATE, CREATEUSER, ID, NEWPRODOFFERID, PRODOFFERID, STATEID, STATUS, UPDATEDATE, UPDATEUSER, EXCLUSE_ID_LIST};
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "DEVICE_CONFIG_SEQ_GENERATOR", sequenceName = "DEVICE_CONFIG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEVICE_CONFIG_SEQ_GENERATOR")
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
    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
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
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Basic
    @Column(name = "UPDATE_USER")
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceConfig that = (DeviceConfig) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (prodOfferId != null ? !prodOfferId.equals(that.prodOfferId) : that.prodOfferId != null) return false;
        if (stateId != null ? !stateId.equals(that.stateId) : that.stateId != null) return false;
        if (newProdOfferId != null ? !newProdOfferId.equals(that.newProdOfferId) : that.newProdOfferId != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (createUser != null ? !createUser.equals(that.createUser) : that.createUser != null) return false;
        if (updateDate != null ? !updateDate.equals(that.updateDate) : that.updateDate != null) return false;
        if (updateUser != null ? !updateUser.equals(that.updateUser) : that.updateUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (prodOfferId != null ? prodOfferId.hashCode() : 0);
        result = 31 * result + (stateId != null ? stateId.hashCode() : 0);
        result = 31 * result + (newProdOfferId != null ? newProdOfferId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createUser != null ? createUser.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + (updateUser != null ? updateUser.hashCode() : 0);
        return result;
    }
}

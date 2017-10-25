package com.viettel.bccs.inventory.model;

/**
 * Created by hoangnt14 on 1/23/2017.
 */

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author hoangnt14
 */
@Entity
@Table(name = "STOCK_DELIVER")
@NamedQueries({
        @NamedQuery(name = "StockDeliver.findAll", query = "SELECT s FROM StockDeliver s")})
public class StockDeliver implements Serializable {
    public static enum COLUMNS {CREATEDATE, CREATEUSER, CREATEUSERID, CREATEUSERNAME, NEWSTAFFID, NEWSTAFFOWNERID, NOTE, OLDSTAFFID, OLDSTAFFOWNERID, OWNERID, OWNERTYPE, STATUS, STOCKDELIVERID, UPDATEDATE, EXCLUSE_ID_LIST}

    ;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "STOCK_DELIVER_ID")
    @SequenceGenerator(name = "STOCK_DELIVER_ID_GENERATOR", sequenceName = "STOCK_DELIVER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_DELIVER_ID_GENERATOR")
    private Long stockDeliverId;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_USER_NAME")
    private String createUserName;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "OLD_STAFF_OWNER_ID")
    private Long oldStaffOwnerId;
    @Column(name = "OLD_STAFF_ID")
    private Long oldStaffId;
    @Column(name = "NEW_STAFF_OWNER_ID")
    private Long newStaffOwnerId;
    @Column(name = "NEW_STAFF_ID")
    private Long newStaffId;
    @Column(name = "CODE")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public StockDeliver() {
    }

    public StockDeliver(Long stockDeliverId) {
        this.stockDeliverId = stockDeliverId;
    }

    public Long getStockDeliverId() {
        return stockDeliverId;
    }

    public void setStockDeliverId(Long stockDeliverId) {
        this.stockDeliverId = stockDeliverId;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getOldStaffOwnerId() {
        return oldStaffOwnerId;
    }

    public void setOldStaffOwnerId(Long oldStaffOwnerId) {
        this.oldStaffOwnerId = oldStaffOwnerId;
    }

    public Long getOldStaffId() {
        return oldStaffId;
    }

    public void setOldStaffId(Long oldStaffId) {
        this.oldStaffId = oldStaffId;
    }

    public Long getNewStaffOwnerId() {
        return newStaffOwnerId;
    }

    public void setNewStaffOwnerId(Long newStaffOwnerId) {
        this.newStaffOwnerId = newStaffOwnerId;
    }

    public Long getNewStaffId() {
        return newStaffId;
    }

    public void setNewStaffId(Long newStaffId) {
        this.newStaffId = newStaffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockDeliverId != null ? stockDeliverId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockDeliver)) {
            return false;
        }
        StockDeliver other = (StockDeliver) object;
        if ((this.stockDeliverId == null && other.stockDeliverId != null) || (this.stockDeliverId != null && !this.stockDeliverId.equals(other.stockDeliverId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bai2.StockDeliver[ stockDeliverId=" + stockDeliverId + " ]";
    }

}


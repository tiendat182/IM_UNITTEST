/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DungPT16
 */
@Entity
@Table(name = "PRODUCT_ERP")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "ProductErp.findAll", query = "SELECT p FROM ProductErp p")})
public class ProductErp implements Serializable {
public static enum COLUMNS {CREATEDATE, CREATEUSER, OWNERID, OWNERTYPE, PRODOFFERID, PRODUCTERPID, QUANTITY, UPDATEDATETIME, UPDATEUSER, EXCLUSE_ID_LIST};
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_ERP_ID")
    private Long productErpId;
    @Column(name = "OWNER_TYPE")
    private Long ownerType;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "PROD_OFFER_ID")
    private Long prodOfferId;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;

    public ProductErp() {
    }

    public ProductErp(Long productErpId) {
        this.productErpId = productErpId;
    }

    public Long getProductErpId() {
        return productErpId;
    }

    public void setProductErpId(Long productErpId) {
        this.productErpId = productErpId;
    }

    public Long getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Long ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productErpId != null ? productErpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductErp)) {
            return false;
        }
        ProductErp other = (ProductErp) object;
        if ((this.productErpId == null && other.productErpId != null) || (this.productErpId != null && !this.productErpId.equals(other.productErpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.bccs.inventory.model.ProductErp[ productErpId=" + productErpId + " ]";
    }

}

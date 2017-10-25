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
 * @author sinhhv
 */
@Entity
@Table(name = "PRODUCT_OFFER_TYPE")
public class ProductOfferType implements Serializable {
    public static enum COLUMNS {CREATEDATETIME, CREATEUSER, DESCRIPTION, NAME, PARENTID, PRODUCTOFFERTYPEID, STATUS, CHECKEXP, UPDATEDATETIME, UPDATEUSER,TABLENAME, EXCLUSE_ID_LIST}

    ;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "PRODUCT_OFFER_TYPE_ID_GENERATOR", sequenceName = "PRODUCT_OFFER_TYPE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_OFFER_TYPE_ID_GENERATOR")
    @Column(name = "PRODUCT_OFFER_TYPE_ID")
    private Long productOfferTypeId;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDatetime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDatetime;
    @Column(name = "CHECK_EXP")
    private Long checkExp;

    @Column(name = "TABLE_NAME")
    private String tableName;

    public Long getCheckExp() {
        return checkExp;
    }

    public void setCheckExp(Long checkExp) {
        this.checkExp = checkExp;
    }

    public ProductOfferType() {
    }

    public ProductOfferType(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public ProductOfferType(Long productOfferTypeId, String name) {
        this.productOfferTypeId = productOfferTypeId;
        this.name = name;
    }

    public Long getProductOfferTypeId() {
        return productOfferTypeId;
    }

    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productOfferTypeId != null ? productOfferTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductOfferType)) {
            return false;
        }
        ProductOfferType other = (ProductOfferType) object;
        if ((this.productOfferTypeId == null && other.productOfferTypeId != null) || (this.productOfferTypeId != null && !this.productOfferTypeId.equals(other.productOfferTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "genentity.ProductOfferType[ productOfferTypeId=" + productOfferTypeId + " ]";
    }

}

package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductOfferTypeDTO extends BaseDTO implements Serializable {

    private Date createDatetime;
    private String createUser;
    private String description;
    private String name;
    private Long parentId;
    private Long productOfferTypeId;
    private String status;
    private Date updateDatetime;
    private String updateUser;
    private Long checkExp;
    private String tableName;

    private List<StockTotalFullDTO> lsStockTotalFullDto;

    public Long getCheckExp() {
        return checkExp;
    }
    public void setCheckExp(Long checkExp) {
        this.checkExp = checkExp;
    }
    public String getKeySet() { return keySet; }
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    public String getCreateUser() {
        return this.createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getParentId() {
        return this.parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public Long getProductOfferTypeId() {
        return this.productOfferTypeId;
    }
    public void setProductOfferTypeId(Long productOfferTypeId) {
        this.productOfferTypeId = productOfferTypeId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    public String getUpdateUser() {
        return this.updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<StockTotalFullDTO> getLsStockTotalFullDto() {
        return lsStockTotalFullDto;
    }

    public void setLsStockTotalFullDto(List<StockTotalFullDTO> lsStockTotalFullDto) {
        this.lsStockTotalFullDto = lsStockTotalFullDto;
    }

    @Override
    public String toString() {
        return "" + productOfferTypeId + "";
    }
}

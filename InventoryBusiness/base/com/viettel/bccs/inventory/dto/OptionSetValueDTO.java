package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseDTO;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OptionSetValueDTO extends BaseDTO {

    private String createdBy;
    private Date creationDate;
    private String description;
    private Long displayOrder;
    private Long id;
    private Date lastUpdateDate;
    private String lastUpdatedBy;
    private String name;
    private String value;
    private String status;
    private Long optionSetId;

    public String getKeySet() {
        return keySet;
    }

    /**
     * Init data arrName
     *
     * @author ThanhNT
     */
    public List<String> getListProductVasName() {
        List<String> lsArrName = Lists.newArrayList();
        if (!DataUtil.isNullOrEmpty(value)) {
            String[] temp = value.split(",");
            if (temp.length > 0) {
                Collections.addAll(lsArrName, temp);
            }
        }
        return lsArrName;
    }

    public String getNameWithDescription() {
        return DataUtil.safeToString(value) + "-" + DataUtil.safeToString(description);
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOptionSetId() {
        return optionSetId;
    }

    public void setOptionSetId(Long optionSetId) {
        this.optionSetId = optionSetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionSetValueDTO that = (OptionSetValueDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

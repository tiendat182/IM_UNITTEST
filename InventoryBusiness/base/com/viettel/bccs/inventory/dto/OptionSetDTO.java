package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OptionSetDTO extends BaseDTO {

    private String code;
    private String createdBy;
    private Date creationDate;
    private String description;
    private Long id;
    private Date lastUpdateDate;
    private String lastUpdatedBy;
    private String name;
    private String status;
    private List<OptionSetValueDTO> lsOptionSetValueDto = new ArrayList<>();

    public String getKeySet() {
        return keySet;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OptionSetValueDTO> getLsOptionSetValueDto() {
        return lsOptionSetValueDto;
    }

    public void setLsOptionSetValueDto(List<OptionSetValueDTO> lsOptionSetValueDto) {
        this.lsOptionSetValueDto = lsOptionSetValueDto;
    }

}

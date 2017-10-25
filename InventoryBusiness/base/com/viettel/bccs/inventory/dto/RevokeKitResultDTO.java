package com.viettel.bccs.inventory.dto;

import com.google.common.collect.Lists;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class RevokeKitResultDTO extends BaseDTO{
public String getKeySet() {
 return keySet; }

    private String typeResult = "1";//1 la import thanh cong het, 2 la vua import loi + import thanh cong, 3 la import loi het
    private Long ownerType;
    private Long ownerId;
    private Long actionStaffId;
    private String typeSearch;

    private List<RevokeKitFileImportDTO> lsRevokeKitFileImportDTOs = Lists.newArrayList();
    private List<RevokeKitDetailDTO> lsKitDetailDTO = Lists.newArrayList();

    public RevokeKitResultDTO() {
    }

    public List<RevokeKitFileImportDTO> getLsRevokeKitFileImportDTOs() {
        return lsRevokeKitFileImportDTOs;
    }

    public void setLsRevokeKitFileImportDTOs(List<RevokeKitFileImportDTO> lsRevokeKitFileImportDTOs) {
        this.lsRevokeKitFileImportDTOs = lsRevokeKitFileImportDTOs;
    }

    public String getTypeResult() {
        return typeResult;
    }

    public void setTypeResult(String typeResult) {
        this.typeResult = typeResult;
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

    public Long getActionStaffId() {
        return actionStaffId;
    }

    public void setActionStaffId(Long actionStaffId) {
        this.actionStaffId = actionStaffId;
    }

    public List<RevokeKitDetailDTO> getLsKitDetailDTO() {
        return lsKitDetailDTO;
    }

    public void setLsKitDetailDTO(List<RevokeKitDetailDTO> lsKitDetailDTO) {
        this.lsKitDetailDTO = lsKitDetailDTO;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }
}

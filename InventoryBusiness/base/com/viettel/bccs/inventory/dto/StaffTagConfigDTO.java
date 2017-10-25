package com.viettel.bccs.inventory.dto;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 2/4/2016.
 */
public class StaffTagConfigDTO extends BaseDTO implements Serializable {
    private String inputSearch;
    private String shopID;
    private String staffID;
    private List<String> channelTypes;
    private int searchMode;
    private boolean disableEdit = false;

    public StaffTagConfigDTO() {
        searchMode = Const.StaffSearchMode.allChild.value();
    }

    public StaffTagConfigDTO(String inputSearch, String shopID, List<String> channelTypes) {
        this.inputSearch = inputSearch;
        this.shopID = shopID;
        this.channelTypes = channelTypes;
        this.searchMode = Const.StaffSearchMode.allChild.value();
    }

    public StaffTagConfigDTO(String inputSearch, String shopID, List<String> channelTypes, int searchMode) {
        this.inputSearch = inputSearch;
        this.shopID = shopID;
        this.channelTypes = channelTypes;
        this.searchMode = searchMode;
    }

    public String getInputSearch() {
        return inputSearch;
    }

    public void setInputSearch(String inputSearch) {
        this.inputSearch = inputSearch;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public List<String> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<String> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public int getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;
    }

    public boolean isDisableEdit() {
        return disableEdit;
    }

    public void setDisableEdit(boolean disableEdit) {
        this.disableEdit = disableEdit;
    }
}

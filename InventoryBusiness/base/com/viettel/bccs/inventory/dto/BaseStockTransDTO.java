package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BaseStockTransDTO extends BaseDTO implements Serializable {
    private String staffCode;
    private boolean roleStockNumShop;
    private List<ProductOfferingDTO> listproductOfferingDTOList;

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public boolean isRoleStockNumShop() {
        return roleStockNumShop;
    }

    public void setRoleStockNumShop(boolean roleStockNumShop) {
        this.roleStockNumShop = roleStockNumShop;
    }

    public List<ProductOfferingDTO> getListproductOfferingDTOList() {
        return listproductOfferingDTOList;
    }

    public void setListproductOfferingDTOList(List<ProductOfferingDTO> listproductOfferingDTOList) {
        this.listproductOfferingDTOList = listproductOfferingDTOList;
    }
}

package com.viettel.bccs.inventory.dto;

/**
 * Created by sinhhv on 2/5/2016.
 */
public class ConfigProductTagDTO {
    private String mode = "default";
    private ProductOfferingTotalDTO searchProduct = new ProductOfferingTotalDTO();

    public ProductOfferingTotalDTO getSearchProduct() {
        return searchProduct;
    }

    public void setSearchProduct(ProductOfferingTotalDTO searchProduct) {
        this.searchProduct = searchProduct;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

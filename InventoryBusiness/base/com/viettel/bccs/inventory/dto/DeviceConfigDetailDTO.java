package com.viettel.bccs.inventory.dto;

/**
 * Created by vanho on 01/04/2017.
 */
public class DeviceConfigDetailDTO {

    private DeviceConfigDTO deviceConfigDTO;
    private ProductOfferingTotalDTO productOfferingTotalDTO;
    private String status;

    public DeviceConfigDetailDTO() {
        productOfferingTotalDTO = new ProductOfferingTotalDTO();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductOfferingTotalDTO getProductOfferingTotalDTO() {
        return productOfferingTotalDTO;
    }

    public void setProductOfferingTotalDTO(ProductOfferingTotalDTO productOfferingTotalDTO) {
        this.productOfferingTotalDTO = productOfferingTotalDTO;
    }

    public DeviceConfigDTO getDeviceConfigDTO() {
        return deviceConfigDTO;
    }

    public void setDeviceConfigDTO(DeviceConfigDTO deviceConfigDTO) {
        this.deviceConfigDTO = deviceConfigDTO;
    }
}

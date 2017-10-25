package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.dto.ConfigProductTagDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * @author ThanhNT
 */
public interface ProductInfoNameable {

    /**
     * hien thi danh sanh shop tren man hinh
     *
     * @author ThanhNT
     */
    @Secured("@")
    public List<ProductOfferingTotalDTO> doSelectProduct(String inputProduct);

    /**
     * hàm init shop
     *
     * @param objectController
     * @author ThanhNT
     */
    @Secured("@")
    public void initProduct(Object objectController, ConfigProductTagDTO config);

    @Secured("@")
    public void loadProduct(String productId, boolean disableEdit);

    /**
     * @author ThanhNT
     */
    @Secured("@")
    public void resetProduct();

    /**
     * get data from method of object
     *
     * @return methodName tên method set data
     * @author ThanhNT
     */
    @Secured("@")
    public void doChangeProduct(String methodName);

    @Secured("@")
    public void doClearProduct(String methodName);

    public Boolean getIsDisableEdit();

    public void setIsDisableEdit(Boolean isDisableEdit);

    public ProductOfferingTotalDTO getCurrentProduct();

    public void setCurrentProduct(ProductOfferingTotalDTO currentProduct);

    public List<ProductOfferingTotalDTO> getListProduct();

    public void setListProduct(List<ProductOfferingTotalDTO> listProduct);
}

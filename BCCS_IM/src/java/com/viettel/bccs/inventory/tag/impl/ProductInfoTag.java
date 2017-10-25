package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.ConfigProductTagDTO;
import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.tag.ProductInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * tag controller danh cho tag location
 *
 * @author ThanhNT77
 */
@Service
@Scope("prototype")
public class ProductInfoTag extends BaseController implements ProductInfoNameable {

    @Autowired
    ProductOfferingService productService;

    private Boolean isDisableEdit = false;
    private ProductOfferingTotalDTO currentProduct;
    private ConfigProductTagDTO config;
    private List<ProductOfferingTotalDTO> listProduct = Lists.newArrayList();
    private Object objectController;

    @Override
    public List<ProductOfferingTotalDTO> doSelectProduct(String inputProduct) {
        try {
            String input = DataUtil.isNullOrEmpty(inputProduct) ? "" : inputProduct.trim();
            List<ProductOfferingTotalDTO> list = getListProductForTag(input, config);
            this.listProduct = list;
            return list;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    private List<ProductOfferingTotalDTO> getListProductForTag(String input, ConfigProductTagDTO config) throws LogicException, Exception {
        if (DataUtil.safeEqual(config.getMode(), "default")) {
            return DataUtil.defaultIfNull(productService.getListProductForTag(input, config), new ArrayList<ProductOfferingTotalDTO>());
        }
        return new ArrayList<ProductOfferingTotalDTO>();
    }

    @Override
    public void initProduct(Object objectController, ConfigProductTagDTO config) {
        this.objectController = objectController;
        this.config = config;
        /*try {
            this.listProduct = getListProductForTag("", config);
        } catch (LogicException ex) {
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            topPage();
        }*/
    }

    @Override
    public void loadProduct(String productId, boolean disableEdit) {
        try {
            currentProduct = productService.getProductOffering(DataUtil.safeToLong(productId));
            this.isDisableEdit = disableEdit;
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getKeyMsg(), ex);
            topPage();
        } catch (Exception e) {
            reportError("", "common.error.happened", e);
            logger.error(e.getMessage(), e);
            topPage();
        }
    }

    @Override
    public void resetProduct() {
        this.currentProduct = new ProductOfferingTotalDTO();
    }

    @Override
    public void doChangeProduct(String methodName) {
        try {
            Class<?> c = this.objectController.getClass();
            String param = DataUtil.safeToString(UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()).getAttributes().get("param"));
            if (!DataUtil.isNullOrEmpty(param)) {
                Method method = c.getMethod(methodName, ProductOfferingTotalDTO.class, String.class);
                method.invoke(this.objectController, this.currentProduct, param);
            } else {
                Method method = c.getMethod(methodName, ProductOfferingTotalDTO.class);
                method.invoke(this.objectController, this.currentProduct);
            }
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "msg.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "msg.error.happened", ex);
            logger.error("", ex);
        }
    }

    @Override
    public void doClearProduct(String methodName) {
        this.currentProduct = null;
        try {
            Class<?> c = this.objectController.getClass();
            Method method = c.getMethod(methodName);
            method.invoke(this.objectController);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
            reportError("", "common.error.happened", ex);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            reportError("", "common.error.happened", ex);
            logger.error("", ex);
        }
    }

    public Boolean getIsDisableEdit() {
        return isDisableEdit;
    }

    public ProductOfferingTotalDTO getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(ProductOfferingTotalDTO currentProduct) {
        this.currentProduct = currentProduct;
    }

    public void setIsDisableEdit(Boolean isDisableEdit) {
        this.isDisableEdit = isDisableEdit;
    }

    public List<ProductOfferingTotalDTO> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductOfferingTotalDTO> listProduct) {
        this.listProduct = listProduct;
    }
}

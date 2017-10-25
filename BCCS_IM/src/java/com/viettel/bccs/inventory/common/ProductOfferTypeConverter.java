package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.ProductOfferTypeDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by ThanhNT77 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("productOfferTypeConverter")
public class ProductOfferTypeConverter implements javax.faces.convert.Converter {
    List<ProductOfferTypeDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ProductOfferTypeDTO ret = null;
        if (list != null)
            for (ProductOfferTypeDTO c : list) {
                if (c != null && c.getProductOfferTypeId() != null && DataUtil.safeToString(c.getProductOfferTypeId()).equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new ProductOfferTypeDTO();
            ret.setProductOfferTypeId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        ProductOfferTypeDTO ci;
        if (o instanceof String || o instanceof Long) {
            ci = new ProductOfferTypeDTO();
            ci.setProductOfferTypeId(DataUtil.safeToLong(o));
        } else {
            ci = (ProductOfferTypeDTO) o;
        }
        return ci.toString();
    }

    public List<ProductOfferTypeDTO> getList() {
        return list;
    }

    public void setList(List<ProductOfferTypeDTO> list) {
        this.list = list;
    }
}

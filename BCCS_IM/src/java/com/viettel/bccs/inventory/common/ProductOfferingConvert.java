package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.ProductOfferingDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by tuydv1 on 1/4/2016.
 */
@javax.faces.convert.FacesConverter("productOffConvert")
public class ProductOfferingConvert implements javax.faces.convert.Converter {
    List<ProductOfferingDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ProductOfferingDTO ret = null;
        if (list != null)
            for (ProductOfferingDTO c : list) {
                if (c != null && c.getProductOfferingId() != null && c.getProductOfferingId().toString().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new ProductOfferingDTO();
            ret.setProductOfferingId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        ProductOfferingDTO ci;
        if (o instanceof String) {
            ci = new ProductOfferingDTO();
            ci.setProductOfferingId(DataUtil.safeToLong(o));
        } else
            ci = (ProductOfferingDTO) o;
        return ci.getProductOfferingId() == null ? null : ci.getProductOfferingId().toString();
    }



    public List<ProductOfferingDTO> getList() {
        return list;
    }

    public void setList(List<ProductOfferingDTO> list) {
        this.list = list;
    }
}


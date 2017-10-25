package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by ThanhNT77 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("productOfferingConverter")
public class ProductOfferingConverter implements javax.faces.convert.Converter {
    List<ProductOfferingTotalDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ProductOfferingTotalDTO ret = null;
        if (list != null)
            for (ProductOfferingTotalDTO c : list) {
                if (c != null && c.getProductOfferingId() != null && DataUtil.safeToString(c.getProductOfferingId()).equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new ProductOfferingTotalDTO();
            ret.setProductOfferingId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        ProductOfferingTotalDTO ci;
        if (o instanceof String || o instanceof  Long) {
            ci = new ProductOfferingTotalDTO();
            ci.setProductOfferingId(DataUtil.safeToLong(o));
        } else {
            ci = (ProductOfferingTotalDTO) o;
        }
        return ci.toString();
    }

    public List<ProductOfferingTotalDTO> getList() {
        return list;
    }

    public void setList(List<ProductOfferingTotalDTO> list) {
        this.list = list;
    }
}

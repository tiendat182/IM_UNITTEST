package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("shopConverter")
public class ShopConverter implements javax.faces.convert.Converter {
    List<ShopDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ShopDTO ret = null;
        if (list != null)
            for (ShopDTO c : list) {
                if (c != null && c.getShopId() != null && c.getShopId().toString().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new ShopDTO();
            ret.setShopId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        ShopDTO ci;
        if (o instanceof String) {
            ci = new ShopDTO();
            ci.setShopId(DataUtil.safeToLong(o));
        } else
            ci = (ShopDTO) o;
        return ci.getShopId() == null ? null : ci.getShopId().toString();
    }

    public List<ShopDTO> getList() {
        return list;
    }

    public void setList(List<ShopDTO> list) {
        this.list = list;
    }
}

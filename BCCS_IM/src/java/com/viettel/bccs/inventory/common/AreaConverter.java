package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.AreaDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("areaConverter")
public class AreaConverter implements javax.faces.convert.Converter {
    List<AreaDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        AreaDTO ret = null;
        if (list != null)
            for (AreaDTO c : list) {
                if (c != null && c.getAreaCode() != null && c.getAreaCode().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new AreaDTO();
            ret.setAreaCode(s);
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        AreaDTO ci;
        if (o instanceof String) {
            ci = new AreaDTO();
            ci.setAreaCode(DataUtil.safeToString(o));
        } else
            ci = (AreaDTO) o;
        return ci.getAreaCode() ;
    }

    public List<AreaDTO> getList() {
        return list;
    }

    public void setList(List<AreaDTO> list) {
        this.list = list;
    }
}

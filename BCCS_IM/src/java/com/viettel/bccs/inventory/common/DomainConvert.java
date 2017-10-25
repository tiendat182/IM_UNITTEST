package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.DomainDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("domainConvert")
public class DomainConvert implements javax.faces.convert.Converter {
    List<DomainDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        DomainDTO ret = null;
        if (list != null)
            for (DomainDTO c : list) {
                if (c != null && c.getId() != null && c.getId().toString().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new DomainDTO();
            ret.setId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        DomainDTO ci;
        if (o instanceof String) {
            ci = new DomainDTO();
            ci.setId(DataUtil.safeToLong(o));
        } else
            ci = (DomainDTO) o;
        return ci.getId() == null ? null : ci.getId().toString();
    }



    public List<DomainDTO> getList() {
        return list;
    }

    public void setList(List<DomainDTO> list) {
        this.list = list;
    }
}

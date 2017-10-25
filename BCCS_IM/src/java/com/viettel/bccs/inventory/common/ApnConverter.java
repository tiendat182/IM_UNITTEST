package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.ApnDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("apnConverter")
public class ApnConverter implements javax.faces.convert.Converter {
    List<ApnDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ApnDTO ret = null;
        if (list != null)
            for (ApnDTO c : list) {
                if (c != null && c.getApnId() != null && c.getApnId().equals(DataUtil.safeToLong(s))) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new ApnDTO();
            ret.setApnId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        ApnDTO ci;
        if (o instanceof String) {
            ci = new ApnDTO();
            ci.setApnId(DataUtil.safeToLong(o));
        } else
            ci = (ApnDTO) o;
        return ci.getApnId() == null ? null : ci.getApnId().toString();
    }

    public List<ApnDTO> getList() {
        return list;
    }

    public void setList(List<ApnDTO> list) {
        this.list = list;
    }
}

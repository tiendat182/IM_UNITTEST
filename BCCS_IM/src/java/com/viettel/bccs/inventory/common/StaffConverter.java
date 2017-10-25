package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.StaffDTO;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("staffConverter")
public class StaffConverter implements javax.faces.convert.Converter {
    List<StaffDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        StaffDTO ret = null;
        if (list != null)
            for (StaffDTO c : list) {
                if (c != null && c.getStaffId() != null && c.getStaffId().toString().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new StaffDTO();
            ret.setStaffId(Long.valueOf(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        StaffDTO ci;
        if (o instanceof String) {
            ci = new StaffDTO();
            ci.setStaffCode(o.toString());
        } else
            ci = (StaffDTO) o;
        return ci.getStaffId().toString();
    }

    public List<StaffDTO> getList() {
        return list;
    }

    public void setList(List<StaffDTO> list) {
        this.list = list;
    }
}

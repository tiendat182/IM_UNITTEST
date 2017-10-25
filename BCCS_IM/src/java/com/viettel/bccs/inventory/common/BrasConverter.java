package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.BrasDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 01/12/2015.
 */
@javax.faces.convert.FacesConverter("brasConverter")
public class BrasConverter implements javax.faces.convert.Converter {
    List<BrasDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        BrasDTO ret = null;
        if (list != null)
            for (BrasDTO c : list) {
                if (c != null && c.getBrasId() != null && c.getBrasId().toString().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new BrasDTO();
            ret.setBrasId(DataUtil.safeToLong(s));
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        BrasDTO ci;
        if (o instanceof String) {
            ci = new BrasDTO();
            ci.setBrasId(DataUtil.safeToLong(o));
        } else
            ci = (BrasDTO) o;
        return ci.getBrasId() == null ? null : ci.getBrasId().toString();
    }

    public List<BrasDTO> getList() {
        return list;
    }

    public void setList(List<BrasDTO> list) {
        this.list = list;
    }
}

package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by anhvv4 on 30/11/2015.
 */
@javax.faces.convert.FacesConverter("InvoiceTypeConverter")
public class InvoiceTypeConverter implements javax.faces.convert.Converter {
    List<InvoiceTypeDTO> list = null;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        InvoiceTypeDTO ret = null;
        if (list != null)
            for (InvoiceTypeDTO c : list) {
                if (c != null && c.getKeySet() != null && c.getKeySet().equals(s)) {
                    ret = c;
                    break;
                }
            }
        if (ret == null) {
            ret = new InvoiceTypeDTO();
            ret.setInvoiceType(s);
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) return "";
        InvoiceTypeDTO ci;
        if (o instanceof String) {
            ci = new InvoiceTypeDTO();
            ci.setInvoiceType(o.toString());
        } else
            ci = (InvoiceTypeDTO) o;
        return ci.getKeySet();
    }

    public List<InvoiceTypeDTO> getList() {
        return list;
    }

    public void setList(List<InvoiceTypeDTO> list) {
        this.list = list;
    }
}

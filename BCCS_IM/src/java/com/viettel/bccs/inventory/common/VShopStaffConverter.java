package com.viettel.bccs.inventory.common;

import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.fw.common.util.DataUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhvh1 on 2015-05-13.
 */
@javax.faces.convert.FacesConverter("VShopStaffConverter")
public class VShopStaffConverter implements javax.faces.convert.Converter {
    List<VShopStaffDTO> list = new ArrayList<>();
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if(DataUtil.isNullOrEmpty(s) || list == null || list.isEmpty()) return new VShopStaffDTO();
        for(VShopStaffDTO vShopStaffDTO:list){
            if(vShopStaffDTO.getTablePk() != null && vShopStaffDTO.getTablePk().equals(s))
                return vShopStaffDTO;
        }
        return new VShopStaffDTO();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if(o == null) return "";
        if(o instanceof VShopStaffDTO == false) return o.toString();
        return ((VShopStaffDTO)o).getTablePk();
    }

    public List<VShopStaffDTO> getList() {
        return list;
    }

    public void setList(List<VShopStaffDTO> list) {
        this.list = list;
    }
}

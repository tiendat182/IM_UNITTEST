package com.viettel.ws.provider;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;

/**
 * Created by thiendn1 on 4/1/2015.
 */
public class WsDtoContainer {
    @XStreamImplicit(itemFieldName = "list")
    private List<BaseDTO> list;

    public List<BaseDTO> getList() {
        return list;
    }

    public void setList(List<BaseDTO> list) {
        this.list = list;
    }

}

package com.viettel.ws.provider;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by thien on 31/01/2016.
 */
public class WsDtoJaxbContainer<T> {
    private List<T> list;

    @XmlElement(name = "return")
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}


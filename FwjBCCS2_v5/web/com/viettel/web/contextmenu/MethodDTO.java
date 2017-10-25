package com.viettel.web.contextmenu;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by Thien on 11/8/2015.
 */
public class MethodDTO {
    private String name;
    @XStreamImplicit(itemFieldName = "parameteres")
    private List<ParameterDTO> parameteres;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterDTO> getParameteres() {
        return parameteres;
    }

    public void setParameteres(List<ParameterDTO> parameteres) {
        this.parameteres = parameteres;
    }
}

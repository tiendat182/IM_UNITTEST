package com.viettel.fw.common.util;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by LamNV5 on 5/14/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RequiredRoleMap implements Serializable {

    @XmlElement(nillable = true)
    private List<String> values = Lists.newArrayList();

    public boolean hasRole(String key) {
        for (String value : values) {
            if (value.equals(key)) return true;
        }

        return false;
    }

    public void add(String role) {
        values.add(role);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}

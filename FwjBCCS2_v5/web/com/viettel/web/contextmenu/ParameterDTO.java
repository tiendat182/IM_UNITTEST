package com.viettel.web.contextmenu;

import com.viettel.ws.common.utils.XmlSchema;

/**
 * Created by thiendn1 on 10/11/2015.
 */
public class ParameterDTO {
    private String type;
    private String value;
    private boolean isMapping;

    public String getType() {
        return XmlSchema.convertToFullType(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMapping() {
        return isMapping;
    }

    public void setIsMapping(boolean isMapping) {
        this.isMapping = isMapping;
    }

    public Object getTrueValue(){
        try {
            return XmlSchema.localXmlToObject(value,type,null);
        } catch (ClassNotFoundException e) {
            return value;
        }
    }

}

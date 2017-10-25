package com.viettel.web.contextmenu;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by Thien on 11/8/2015.
 */
@XStreamAlias("MenuItem")
public class MenuItem {
    private String id;
    private String name;
    private String url;
    private String icon;
    private boolean skipParseValue;

    @XStreamImplicit(itemFieldName = "sources")
    private List<FieldDTO> sources;

    @XStreamImplicit(itemFieldName = "fields")
    private List<FieldDTO> fields;

    @XStreamImplicit(itemFieldName = "methods")
    private List<MethodDTO> methods;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDTO> fields) {
        this.fields = fields;
    }

    public List<MethodDTO> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDTO> methods) {
        this.methods = methods;
    }

    public List<FieldDTO> getSources() {
        return sources;
    }

    public void setSources(List<FieldDTO> sources) {
        this.sources = sources;
    }

    public boolean isSkipParseValue() {
        return skipParseValue;
    }

    public void setSkipParseValue(boolean skipParseValue) {
        this.skipParseValue = skipParseValue;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

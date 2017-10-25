package com.viettel.web.contextmenu;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by thiendn1 on 9/11/2015.
 */
public class MenuGroup {
    private String id;
    private String name;
    @XStreamImplicit(itemFieldName = "item")
    private List<MenuItem> menuItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

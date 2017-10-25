package com.viettel.web.contextmenu;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by thiendn1 on 9/11/2015.
 */
public class ContextMenuConfigHolder {
    @XStreamImplicit(itemFieldName = "group")
    List<MenuGroup> menuGroups;

    @XStreamImplicit(itemFieldName = "item")
    List<MenuItem> menuItems;

    public List<MenuGroup> getMenuGroups() {
        return menuGroups;
    }

    public void setMenuGroups(List<MenuGroup> menuGroups) {
        this.menuGroups = menuGroups;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}

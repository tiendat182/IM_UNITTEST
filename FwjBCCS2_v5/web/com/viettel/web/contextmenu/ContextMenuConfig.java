package com.viettel.web.contextmenu;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thiendn1 on 9/11/2015.
 */
public class ContextMenuConfig {
    private static ContextMenuConfig instance = new ContextMenuConfig();
    private ConcurrentHashMap<String, MenuGroup> groups = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, MenuItem> items = new ConcurrentHashMap<>();

    static {
        instance.loadConfig();
    }

    public static ContextMenuConfig getInstance() {
        return instance;
    }

    public void loadConfig() {

        //insert item into hashmap
        for (MenuItem menuItem : getMenuItems()) {
            items.put(menuItem.getId(), menuItem);
        }
        //insert group into hashmap
        for (MenuGroup menuGroup : getMenuGroups()) {
            //get information of item to display in web
            List<MenuItem> items = menuGroup.getMenuItems();
            for (MenuItem item : items) {
                if (!item.isSkipParseValue()) {
                    MenuItem fullItem = this.items.get(item.getId());
                    if (fullItem == null) {
                        continue;
                    }
                    //get enough data of item here
                    item.setName(fullItem.getName());
                    item.setUrl(fullItem.getUrl());
                }
            }
            groups.put(menuGroup.getId(), menuGroup);
        }
    }


    private InputStream getResource(String file){
        InputStream in = ContextMenuConfigHolder.class.getClassLoader().getResourceAsStream(file);
        //thiendn1: for test only
//        InputStream in = null;
//        try {
//            in = new FileInputStream(new File("D:\\My Document\\BCCS 2.0\\BCCS_STORE\\06.SOURCE\\Draft\\Framework\\FwjBCCS2_v3\\web\\java\\contextmenu\\context-menu-group.xml"));
//        } catch (FileNotFoundException e) {
//            return null;
//        }
        return in;
    }

    private List<MenuGroup> getMenuGroups() {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("ContextMenu", ContextMenuConfigHolder.class);
        xstream.alias("item", MenuItem.class);
        xstream.useAttributeFor("id", String.class);
        xstream.useAttributeFor("skipParseValue", Boolean.class);
        xstream.autodetectAnnotations(true);
        ContextMenuConfigHolder contextMenuConfigHolder = (ContextMenuConfigHolder) xstream.fromXML(getResource("context-menu-group.xml"));
        return contextMenuConfigHolder.getMenuGroups();

    }

    private List<MenuItem> getMenuItems() {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("ContextMenu", ContextMenuConfigHolder.class);
        xstream.alias("item", MenuItem.class);
        xstream.useAttributeFor("id", String.class);
        xstream.useAttributeFor("skipParseValue", Boolean.class);
        xstream.autodetectAnnotations(true);
        ContextMenuConfigHolder contextMenuConfigHolder = (ContextMenuConfigHolder) xstream.fromXML(getResource("context-menu-item.xml"));
        return contextMenuConfigHolder.getMenuItems();

    }

    public ConcurrentHashMap<String, MenuGroup> getGroups() {
        return groups;
    }

    public void setGroups(ConcurrentHashMap<String, MenuGroup> groups) {
        this.groups = groups;
    }

    public ConcurrentHashMap<String, MenuItem> getItems() {
        return items;
    }

    public void setItems(ConcurrentHashMap<String, MenuItem> items) {
        this.items = items;
    }
}

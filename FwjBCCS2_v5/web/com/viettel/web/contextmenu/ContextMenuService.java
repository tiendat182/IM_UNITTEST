package com.viettel.web.contextmenu;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by thiendn1 on 16/10/2015.
 */
@Service
public class ContextMenuService {

    public static final String CONTEXT_MENU_SEPARATOR = "\\.";
    public static final String FLASH_ATTRIBUTE_SOURCE = "CONTEXTMENU_CONTROLLER_SOURCE";
    public static final String FLASH_ATTRIBUTE_CONTEXTMENUITEM = "CONTEXTMENU_CONTROLLER_MENU_ITEM";

    public List<MenuItem> getMenuItemInGroup(String groupId) {
        return ContextMenuConfig.getInstance().getGroups().get(groupId).getMenuItems();
    }


    public MenuItem getMenuItem(String itemId) throws IOException {
        return ContextMenuConfig.getInstance().getItems().get(itemId);
    }

}

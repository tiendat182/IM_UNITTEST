package com.viettel.web.contextmenu;

import com.viettel.web.common.controller.BaseController;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.omnifaces.util.Faces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thiendn1 on 16/10/2015.
 */
@Component
@ManagedBean
public class ContextModelController {

    @Autowired
    ContextMenuService contextMenuService;

    @Autowired
    ApplicationContext context;


    Map<String, MenuModel> modelMap = new ConcurrentHashMap<>();

    public MenuModel getModel(String groupId) {
        if (modelMap.get(groupId) != null) {
            return modelMap.get(groupId);
        }

        MenuModel model = new DefaultMenuModel();
        List<MenuItem> list = contextMenuService.getMenuItemInGroup(groupId);
        if (list != null && !list.isEmpty()) {
            Collections.sort(list, new Comparator<MenuItem>() {
                @Override
                public int compare(MenuItem o1, MenuItem o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (MenuItem contextMenuItem : list) {
                DefaultMenuItem item = new DefaultMenuItem(contextMenuItem.getName());
                String command = "#{contextModelController.transfer('" + contextMenuItem.getId() + "')}";
                item.setCommand(command);
                if (contextMenuItem.getIcon() != null) {
                    item.setIcon(contextMenuItem.getIcon());
                } else {
                    item.setIcon("ui-icon-link");
                }
                model.addElement(item);
            }
            modelMap.put(groupId, model);
        }
        return model;
    }


    public String transfer(String itemId) throws IOException, ClassNotFoundException, IllegalAccessException {
        MenuItem contextMenuItem = contextMenuService.getMenuItem(itemId);
        Map<String, Object> map = new HashMap<>();
        List<FieldDTO> sourceFields = contextMenuItem.getSources();
        Map<String, Object> viewMap = Faces.getViewRoot().getViewMap();
        if (sourceFields != null) {
            for (FieldDTO fieldDTO : sourceFields) {
                String controller = fieldDTO.getController();
                Class sourceClass = Class.forName(controller);
                String beanNameForType = context.getBeanNamesForType(sourceClass)[0];
                String nameField = fieldDTO.getName();
                String nameMapField = null;
                String[] names = nameField.split(ContextMenuService.CONTEXT_MENU_SEPARATOR);
                if (names.length > 1) {
                    nameField = names[0];
                    nameMapField = names[1];
                }
                Object targetObject = new CglibHelper(viewMap.get(beanNameForType)).getTargetObject();
                Field field = FieldUtils.getField(targetObject.getClass(), nameField, true);
                if (nameMapField != null) {
                    map.put(fieldDTO.getName(), FieldUtils.readField(field.get(targetObject), nameMapField, true));
                } else {
                    field.setAccessible(true);
                    map.put(fieldDTO.getName(), field.get(new CglibHelper(viewMap.get(beanNameForType)).getTargetObject()));
                    field.setAccessible(false);

                }
            }
        }
        Faces.setSessionAttribute(ContextMenuService.FLASH_ATTRIBUTE_SOURCE, map);
        Faces.setSessionAttribute(ContextMenuService.FLASH_ATTRIBUTE_CONTEXTMENUITEM, contextMenuItem);
        return "pretty:" + contextMenuItem.getUrl();

    }


}

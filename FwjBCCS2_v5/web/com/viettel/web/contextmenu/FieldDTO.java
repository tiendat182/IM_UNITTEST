package com.viettel.web.contextmenu;

/**
 * Created by Thien on 11/8/2015.
 */
public class FieldDTO {
    // 0 = source , 1 = setter
    private String name;
    private String nameMap;
    private String controller;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameMap() {
        return nameMap;
    }

    public void setNameMap(String nameMap) {
        this.nameMap = nameMap;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

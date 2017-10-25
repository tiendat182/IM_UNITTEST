package com.viettel.bccs.inventory.common;

/**
 * Created by tuyendv8 on 11/20/2015.
 */
import java.io.Serializable;

public class Document implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;

    public Document(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}